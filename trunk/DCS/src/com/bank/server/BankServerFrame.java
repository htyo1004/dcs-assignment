/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.server;

import com.bank.entity.AccountApplication;
import com.bank.entity.Branch;
import com.bank.entity.Deposit;
import com.bank.entity.LoanApplication;
import com.bank.entity.MySQLConnection;
import com.bank.entity.TransactionLog;
import com.bank.entity.Withdraw;
import com.bank.utils.CommunicationWrapper;
import com.bank.utils.Operation;
import com.bank.utils.Toast;
import com.bank.utils.TransactionType;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.text.DefaultCaret;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kenny
 */
public class BankServerFrame extends javax.swing.JFrame {

    private CommunicationWrapper cw;
    private Branch branch;
    private Withdraw withdraw;
    private Deposit deposit;
    private TransactionLog tLog;
    private LoanApplication loan;
    private AccountApplication account;
    private Connection dbCon;
    private Thread thread;
    private Executor executor;
    private DefaultComboBoxModel dcbmBranch;
    private String branchCode;
    private boolean gotBranch = false;

    /**
     * Creates new form BankServerFrame
     */
    public BankServerFrame() {
        initComponents();
        dbCon = MySQLConnection.getConnection();
        executor = Executors.newFixedThreadPool(10);
        populateBranchCode();
        jtaMessage.setText("Please configure your server's branch code\n");
    }

    private void populateBranchCode() {
        dcbmBranch = new DefaultComboBoxModel();
        dcbmBranch.addElement("Select the branch you belongs to");
        branch = new Branch();
        ArrayList<String> d = branch.obtainAllBranchCode(dbCon);
        if (!d.isEmpty()) {
            for (int i = 0; i < d.size(); i++) {
                dcbmBranch.addElement(d.get(i));
            }
            jComboBox1.setModel(dcbmBranch);
            gotBranch = true;
        } else {
            Toast.makeText(BankServerFrame.this, "No branch found.", Toast.LENGTH_SHORT).display();
            gotBranch = false;
        }
    }

    private void startThread() {
        if (gotBranch) {
            try {
                cw = new CommunicationWrapper(5000);
                jtaMessage.append("Server started, in idle state\n");
                thread = new Thread(new ServerThread());
                thread.start();
            } catch (SocketException ex) {
                System.out.println("Unable to open socket : " + ex.getMessage());
                System.out.println("Closing server..");
                try {
                    Thread.sleep(2000);
                    System.exit(1);
                } catch (InterruptedException ex1) {
                }
            }
        } else {
            jtaMessage.append("No branches found, please configure your database first.");
        }
    }

    private void withdraw(JSONObject json) throws JSONException, UnknownHostException {
        jtaMessage.append("Request received, Processing now..\n");
        jtaMessage.append("Operation Type : Withdraw\n");
        JSONObject wData = json.getJSONObject("content");
        JSONObject returnValue = new JSONObject();
        JSONObject returnContent = new JSONObject();
        jtaMessage.append("Reading data received..\n");
        String accBranch = wData.getString("accNo").substring(0, 4);
        System.out.println(accBranch);
        returnContent.put("bCode", wData.getString("bCode"));
        returnContent.put("address", wData.getString("address"));
        returnContent.put("port", wData.getInt("port"));
        if (accBranch.equals(this.branchCode)) {
            withdraw = new Withdraw();
            withdraw.setAccNo(wData.getString("accNo"));
            withdraw.setAmount(wData.getDouble("amount"));
            withdraw.setIcNo(wData.getString("icNo"));
            jtaMessage.append("Processing withdrawal\n");
            String result = withdraw.withdraw(dbCon);
            returnValue.put("operation", Operation.RESPONSE);
            if (result.equals("Success")) {
                tLog = new TransactionLog();
                tLog.setAccNo(wData.getString("accNo"));
                tLog.setAmount(wData.getDouble("amount"));
                tLog.setTransactionDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                tLog.setTransactionType(TransactionType.DBT.toString());
                tLog.createTransactionLog(dbCon);
                jtaMessage.append("Operation success\n");
                jtaMessage.append("Sending back respond\n");
                returnContent.put("result", result);
                returnValue.put("content", returnContent);
                respond(returnValue);
            } else {
                cw.send(returnValue, InetAddress.getByName(wData.getString("address")), wData.getInt("port"));
                jtaMessage.append("Unable to withdraw : " + result + "\n");
            }
        } else {
            jtaMessage.append("Not belongs to this branch, send to next branch\n");
            branch = new Branch();
            branch.setBranchCode(cw.whoIsNeighbors(branchCode));
            cw.send(json, InetAddress.getByName(branch.obtainBranchIp(dbCon)), 5000);
        }
        jtaMessage.append("Operation ended, return to idle state\n");
    }
    
    private void transferWithdraw(JSONObject json) throws JSONException, UnknownHostException {
        jtaMessage.append("Request received, Processing now..\n");
        jtaMessage.append("Operation Type : Withdraw\n");
        JSONObject wData = json.getJSONObject("content");
        JSONObject returnValue = new JSONObject();
        jtaMessage.append("Reading data received..\n");
        String accBranch = wData.getString("accNo").substring(0, 4);
        System.out.println(accBranch);
        if (accBranch.equals(this.branchCode)) {
            withdraw = new Withdraw();
            withdraw.setAccNo(wData.getString("accNo"));
            withdraw.setAmount(wData.getDouble("amount"));
            jtaMessage.append("Processing withdrawal\n");
            String result = withdraw.transferWithdraw(dbCon);
            if (result.equals("Success")) {
                tLog = new TransactionLog();
                tLog.setAccNo(wData.getString("accNo"));
                tLog.setAmount(wData.getDouble("amount"));
                tLog.setTransactionDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                tLog.setTransactionType(TransactionType.DBT.toString());
                tLog.createTransactionLog(dbCon);
                jtaMessage.append("Operation success\n");
                jtaMessage.append("Sending back respond\n");
                transferDeposit(json);
            } else {
                returnValue.put("result", result);
                cw.send(returnValue, InetAddress.getByName(wData.getString("address")), wData.getInt("port"));
                jtaMessage.append("Unable to withdraw : " + result + "\n");
            }
        } else {
            jtaMessage.append("Not belongs to this branch, send to next branch\n");
            branch = new Branch();
            branch.setBranchCode(cw.whoIsNeighbors(branchCode));
            cw.send(json, InetAddress.getByName(branch.obtainBranchIp(dbCon)), 5000);
        }
        jtaMessage.append("Operation ended, return to idle state\n");
    }

    private void deposit(JSONObject json) throws JSONException, UnknownHostException {
        jtaMessage.append("Request received, Processing now..\n");
        jtaMessage.append("Operation Type : Deposit\n");
        JSONObject dData = json.getJSONObject("content");
        JSONObject returnValue = new JSONObject();
        JSONObject returnContent = new JSONObject();
        jtaMessage.append("Reading data received..\n");
        String accBranch = dData.getString("accNo").substring(0, 4);
        System.out.println(accBranch);
        returnContent.put("bCode", dData.getString("bCode"));
        returnContent.put("address", dData.getString("address"));
        returnContent.put("port", dData.getInt("port"));
        if (accBranch.equals(this.branchCode)) {
            deposit = new Deposit();
            deposit.setAccNo(dData.getString("accNo"));
            deposit.setAmount(dData.getDouble("amount"));
            deposit.setIcNo(dData.optString("icNo"));
            jtaMessage.append("Processing deposit\n");
            String result = deposit.deposit(dbCon);
            returnValue.put("operation", Operation.RESPONSE);
            if (result.equals("Success")) {
                tLog = new TransactionLog();
                tLog.setAccNo(dData.getString("accNo"));
                tLog.setAmount(dData.getDouble("amount"));
                tLog.setTransactionDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                tLog.setTransactionType(TransactionType.DBT.toString());
                tLog.createTransactionLog(dbCon);
                jtaMessage.append("Operation success\n");
                jtaMessage.append("Sending back respond\n");
                returnContent.put("result", result);
                returnValue.put("content", returnContent);
                respond(returnValue);
            } else {
                cw.send(returnValue, InetAddress.getByName(dData.getString("address")), dData.getInt("port"));
                jtaMessage.append("Unable to deposit : " + result + "\n");
            }
        } else {
            jtaMessage.append("Not belongs to this branch, send to next branch\n");
            branch = new Branch();
            branch.setBranchCode(cw.whoIsNeighbors(branchCode));
            cw.send(json, InetAddress.getByName(branch.obtainBranchIp(dbCon)), 5000);
        }
        jtaMessage.append("Operation ended, return to idle state\n");
    }
    
    private void transferDeposit(JSONObject json) throws JSONException, UnknownHostException {
        jtaMessage.append("Request received, Processing now..\n");
        jtaMessage.append("Operation Type : Deposit\n");
        JSONObject dData = json.getJSONObject("content");
        JSONObject returnValue = new JSONObject();
        JSONObject returnContent = new JSONObject();
        jtaMessage.append("Reading data received..\n");
        String accBranch = dData.getString("accReceiver").substring(0, 4);
        System.out.println(accBranch);
        returnContent.put("bCode", dData.getString("bCode"));
        returnContent.put("address", dData.getString("address"));
        returnContent.put("port", dData.getInt("port"));
        if (accBranch.equals(this.branchCode)) {
            deposit = new Deposit();
            deposit.setAccNo(dData.getString("accReceiver"));
            deposit.setAmount(dData.getDouble("amount"));
            jtaMessage.append("Processing deposit\n");
            String result = deposit.transferDeposit(dbCon);
            returnValue.put("operation", Operation.RESPONSE);
            if (result.equals("Success")) {
                tLog = new TransactionLog();
                tLog.setAccNo(dData.getString("accNo"));
                tLog.setAmount(dData.getDouble("amount"));
                tLog.setTransactionDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                tLog.setTransactionType(TransactionType.DBT.toString());
                tLog.createTransactionLog(dbCon);
                jtaMessage.append("Operation success\n");
                jtaMessage.append("Sending back respond\n");
                returnContent.put("result", result);
                returnValue.put("content", returnContent);
                respond(returnValue);
            } else {
                cw.send(returnValue, InetAddress.getByName(dData.getString("address")), dData.getInt("port"));
                jtaMessage.append("Unable to deposit : " + result + "\n");
            }
        } else {
            jtaMessage.append("Not belongs to this branch, send to next branch\n");
            branch = new Branch();
            branch.setBranchCode(cw.whoIsNeighbors(branchCode));
            cw.send(json, InetAddress.getByName(branch.obtainBranchIp(dbCon)), 5000);
        }
        jtaMessage.append("Operation ended, return to idle state\n");
    }

    private void transfer(JSONObject json) throws JSONException, UnknownHostException {
        jtaMessage.append("Request received, Processing now..\n");
        jtaMessage.append("Operation Type : Transfer\n");
        JSONObject j = json.getJSONObject("content");
        transferWithdraw(j);
        jtaMessage.append("Operation ended, return to idle state\n");
        
        
    }

    private void loan(JSONObject json) throws JSONException, UnknownHostException {
        jtaMessage.append("Request received, Processing now..\n");
        jtaMessage.append("Operation Type : Loan Application\n");
        JSONObject lData = json.getJSONObject("content");
        jtaMessage.append("Reading data received..\n");
        loan = new LoanApplication();
        loan.setLoantype(lData.getString("type"));
        loan.setLoanamount(lData.getDouble("loanamount"));
        loan.setInterestrate(lData.getDouble("rate"));
        loan.setDuration(lData.getString("duration"));
        loan.setName(lData.getString("name"));
        loan.setIcNo(lData.getString("icno"));
        loan.setContactNo(lData.getString("contact"));
        jtaMessage.append("Processing Application\n");
        String result = loan.applyLoan(dbCon);
        JSONObject returnValue = new JSONObject();
        returnValue.put("result", result);
        if (result.equals("Success")) {
            jtaMessage.append("Application successful.\n");
            jtaMessage.append("Sending back respond\n");
            cw.send(returnValue, InetAddress.getByName(lData.getString("address")), lData.getInt("port"));
        } else {
            cw.send(returnValue, InetAddress.getByName(lData.getString("address")), lData.getInt("port"));
            jtaMessage.append("Unable to apply loan : " + result + "\n");
        }
        jtaMessage.append("Operation ended, return to idle state\n");
    }

    private void register(JSONObject json) throws JSONException, UnknownHostException {
        jtaMessage.append("Request received, Processing now..\n");
        jtaMessage.append("Operation Type : Account Registration\n");
        JSONObject rData = json.getJSONObject("content");
        jtaMessage.append("Reading data received..\n");
        account = new AccountApplication();
        account.setAcctype(rData.getString("type"));
        account.setAddress(rData.getString("address"));
        account.setBid(rData.getInt("bid"));
        account.setCity(rData.getString("city"));
        account.setState(rData.getString("state"));
        account.setPostcode(rData.getString("postcode"));
        account.setContactNo(rData.getString("contact"));
        account.setEmail(rData.getString("email"));
        account.setFirstname(rData.getString("firstname"));
        account.setLastname(rData.getString("lastname"));
        account.setGender(rData.getString("gender"));
        account.setIcno(rData.getString("icno"));
        jtaMessage.append("Processing Application\n");
        String result = account.applyAccount(dbCon);
        JSONObject returnValue = new JSONObject();
        returnValue.put("result", result);
        if (result.equals("Success")) {
            jtaMessage.append("Application successful.\n");
            jtaMessage.append("Sending back respond\n");
            cw.send(returnValue, InetAddress.getByName(rData.getString("address")), rData.getInt("port"));
        } else {
            cw.send(returnValue, InetAddress.getByName(rData.getString("address")), rData.getInt("port"));
            jtaMessage.append("Unable to apply new account : " + result + "\n");
        }
        jtaMessage.append("Operation ended, return to idle state\n");
    }

    private void updatePassbook(JSONObject json) throws JSONException, UnknownHostException {
        jtaMessage.append("Request received, Processing now..\n");
        jtaMessage.append("Operation Type : Update Passbook\n");
        JSONObject uData = json.getJSONObject("content");
        jtaMessage.append("Reading data received..\n");
        tLog = new TransactionLog();
        JSONObject returnValue = new JSONObject();
        ArrayList<TransactionLog> log = tLog.getTransactionLog(dbCon, uData.getString("accno"));
        JSONArray jsonArr = new JSONArray();
        for (int i = 0; i < log.size(); i++) {
            TransactionLog transactionLog = log.get(i);
            JSONObject temp = new JSONObject();
            temp.put("accNo", transactionLog.getAccNo());
            temp.put("transactionType", transactionLog.getTransactionType());
            temp.put("transactionDate", transactionLog.getTransactionDate());
            temp.put("amount", transactionLog.getAmount());
            jsonArr.put(temp);
        }
        jtaMessage.append("Processing update passbook request\n");
        if (jsonArr.length() > 0) {
            returnValue.put("result", jsonArr);
            jtaMessage.append("Request successful\n");
            jtaMessage.append("Sending back respond\n");
            cw.send(returnValue, InetAddress.getByName(uData.getString("address")), uData.getInt("port"));
        } else {
            returnValue.put("result", "No record");
            cw.send(returnValue, InetAddress.getByName(uData.getString("address")), uData.getInt("port"));
            jtaMessage.append("Unable to process request : " + "No record found" + "\n");
        }
        jtaMessage.append("Operation ended, return to idle state\n");
    }

    private void respond(JSONObject json) throws JSONException, UnknownHostException {
        jtaMessage.append("Request received, Processing now..\n");
        jtaMessage.append("Operation Type : Response\n");
        JSONObject js = json.getJSONObject("content");
        String bCode = js.getString("bCode");
        if (bCode.equals(this.branchCode)) {
            jtaMessage.append("Respond belongs to this branch\n");
            jtaMessage.append("Returning to client\n");
            cw.send(json, InetAddress.getByName(js.getString("address")), js.getInt("port"));
        } else {
            jtaMessage.append("Respond not belongs to this branch\n");
            jtaMessage.append("Send to neighbor branch\n");
            branch = new Branch();
            branch.setBranchCode(cw.whoIsNeighbors(branchCode));
            cw.send(json, InetAddress.getByName(branch.obtainBranchIp(dbCon)), 5000);
        }
        jtaMessage.append("Operation ended, return to idle state\n");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jspMessage = new javax.swing.JScrollPane();
        jtaMessage = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(null);

        jtaMessage.setEditable(false);
        jtaMessage.setColumns(20);
        jtaMessage.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jtaMessage.setRows(5);
        jtaMessage.setWrapStyleWord(true);
        jtaMessage.setLineWrap(true);
        DefaultCaret caret = (DefaultCaret)jtaMessage.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        jspMessage.setViewportView(jtaMessage);

        getContentPane().add(jspMessage);
        jspMessage.setBounds(10, 10, 430, 330);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Set Branch Code");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 350, 110, 30);

        jComboBox1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select the branch you belongs to" }));
        getContentPane().add(jComboBox1);
        jComboBox1.setBounds(120, 350, 210, 30);

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setText("Set");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(340, 350, 100, 30);

        setSize(new java.awt.Dimension(466, 429));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (jComboBox1.getSelectedIndex() != 0) {
            branchCode = jComboBox1.getSelectedItem().toString();
            jtaMessage.append("Branch Code configured\n");
            jtaMessage.append("Starting server...\n");
            startThread();
            jComboBox1.setEnabled(false);
            jButton1.setEnabled(false);
            jLabel1.setEnabled(false);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BankServerFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jspMessage;
    private javax.swing.JTextArea jtaMessage;
    // End of variables declaration//GEN-END:variables

    public class ServerThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    JSONObject dataReceived = cw.receive();
                    boolean error = dataReceived.optBoolean("error");
                    if (!error) {
                        String op = dataReceived.getString("operation");
                        Operation operation = Operation.valueOf(op.toUpperCase());
                        executor.execute(new WorkHelper(operation, dataReceived));
                    } else {
                        jtaMessage.append("Error occured : " + dataReceived.getString("errorMessage"));
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(BankServerFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public class WorkHelper implements Runnable {

        private Operation op;
        private JSONObject json;

        public WorkHelper(Operation op, JSONObject json) {
            this.op = op;
            this.json = json;
        }

        @Override
        public void run() {
            try {
                switch (op) {
                    case WITHDRAW:
                        withdraw(json);
                        break;
                    case DEPOSIT:
                        deposit(json);
                        break;
                    case TRANSFER:
                        transfer(json);
                        break;
                    case LOAN:
                        loan(json);
                        break;
                    case REGISTER:
                        register(json);
                        break;
                    case TRANSACTION:
                        updatePassbook(json);
                        break;
                    case RESPONSE:
                        respond(json);
                        break;
                }
            } catch (JSONException | UnknownHostException ex) {
                Logger.getLogger(BankServerFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
