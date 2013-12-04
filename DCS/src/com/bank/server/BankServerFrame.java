/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.server;

import com.bank.entity.AccountApplication;
import com.bank.entity.Deposit;
import com.bank.entity.LoanApplication;
import com.bank.entity.MySQLConnection;
import com.bank.entity.TransactionLog;
import com.bank.entity.Withdraw;
import com.bank.utils.CommunicationWrapper;
import com.bank.utils.Operation;
import com.bank.utils.TransactionType;
import java.awt.List;
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
    private Withdraw withdraw;
    private Deposit deposit;
    private TransactionLog tLog;
    private LoanApplication loan;
    private AccountApplication account;
    private Connection dbCon;
    private Thread thread;
    private Executor executor;

    /**
     * Creates new form BankServerFrame
     */
    public BankServerFrame() {
        initComponents();
        dbCon = MySQLConnection.getConnection();
        executor = Executors.newFixedThreadPool(10);
        try {
            cw = new CommunicationWrapper(5000);
            jtaMessage.setText("Server started, in idle state\n");
            startThread();
        } catch (SocketException ex) {
            System.out.println("Unable to open socket : " + ex.getMessage());
            System.out.println("Closing server..");
            try {
                Thread.sleep(3000);
                System.exit(1);
            } catch (InterruptedException ex1) {
            }
        }
    }

    private void startThread() {
        thread = new Thread(new ServerThread());
        thread.start();
    }

    private void withdraw(JSONObject json) throws JSONException, UnknownHostException {
        jtaMessage.append("Request received, Processing now..\n");
        jtaMessage.append("Operation Type : Withdraw\n");
        JSONObject wData = json.getJSONObject("content");
        jtaMessage.append("Reading data received..\n");
        withdraw = new Withdraw();
        withdraw.setAccNo(wData.getString("accNo"));
        withdraw.setAmount(wData.getDouble("amount"));
        withdraw.setIcNo(wData.getString("icNo"));
        jtaMessage.append("Processing withdrawal\n");
        String result = withdraw.withdraw(dbCon);
        JSONObject returnValue = new JSONObject();
        returnValue.put("result", result);
        if (result.equals("Success")) {
            tLog = new TransactionLog();
            tLog.setAccNo(wData.getString("accNo"));
            tLog.setAmount(wData.getDouble("amount"));
            tLog.setTransactionDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            tLog.setTransactionType(TransactionType.DBT.toString());
            tLog.createTransactionLog(dbCon);
            jtaMessage.append("Operation success\n");
            jtaMessage.append("Sending back respond\n");
            cw.send(returnValue, InetAddress.getByName(wData.getString("address")), wData.getInt("port"));
        } else {
            cw.send(returnValue, InetAddress.getByName(wData.getString("address")), wData.getInt("port"));
            jtaMessage.append("Unable to withdraw : " + result + "\n");
        }
        jtaMessage.append("Operation ended, return to idle state\n");
    }

    private void deposit(JSONObject json) throws JSONException, UnknownHostException {
        jtaMessage.append("Request received, Processing now..\n");
        jtaMessage.append("Operation Type : Deposit\n");
        JSONObject dData = json.getJSONObject("content");
        jtaMessage.append("Reading data received..\n");
        deposit = new Deposit();
        deposit.setAccNo(dData.getString("accNo"));
        deposit.setAmount(dData.getDouble("amount"));
        deposit.setIcNo(dData.getString("icNo"));
        jtaMessage.append("Processing deposit\n");
        String result = deposit.deposit(dbCon);
        JSONObject returnValue = new JSONObject();
        returnValue.put("result", result);
        if (result.equals("Success")) {
            tLog = new TransactionLog();
            tLog.setAccNo(dData.getString("accNo"));
            tLog.setAmount(dData.getDouble("amount"));
            tLog.setTransactionDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            tLog.setTransactionType(TransactionType.DBT.toString());
            tLog.createTransactionLog(dbCon);
            jtaMessage.append("Operation success\n");
            jtaMessage.append("Sending back respond\n");
            cw.send(returnValue, InetAddress.getByName(dData.getString("address")), dData.getInt("port"));
        } else {
            cw.send(returnValue, InetAddress.getByName(dData.getString("address")), dData.getInt("port"));
            jtaMessage.append("Unable to deposit : " + result + "\n");
        }
        jtaMessage.append("Operation ended, return to idle state\n");
    }

    private void transfer(JSONObject json) throws JSONException, UnknownHostException {
        jtaMessage.append("Request received, Processing now..\n");
        jtaMessage.append("Operation Type : Transfer\n");
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
        }else{
            returnValue.put("result", "No record");
            cw.send(returnValue, InetAddress.getByName(uData.getString("address")), uData.getInt("port"));
            jtaMessage.append("Unable to process request : " + "No record found" + "\n");
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

        setSize(new java.awt.Dimension(466, 389));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
                }
            } catch (JSONException | UnknownHostException ex) {
                Logger.getLogger(BankServerFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
