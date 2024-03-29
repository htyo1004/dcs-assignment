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
import com.bank.entity.Query;
import com.bank.entity.TransactionLog;
import com.bank.entity.Withdraw;
import com.bank.utils.CommunicationWrapper;
import com.bank.utils.CustomScrollBarUI;
import com.bank.utils.FormattedMessage;
import com.bank.utils.Operation;
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
    private FormattedMessage fm = new FormattedMessage();
    private Query query;
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
    private boolean stopServer = false;
    private boolean serverStarted = false;

    /**
     * Creates new form BankServerFrame
     */
    public BankServerFrame() {
        initComponents();
        dbCon = MySQLConnection.getConnection();
        executor = Executors.newFixedThreadPool(10);
        jtaMessage.setText(fm.formatMessage("SERVER", "Please configure your server's branch code\n"));
        populateBranchCode();
    }
    
    /**
     * Populate branch code by retrieving data from database
     */

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
            jtaMessage.append(fm.formatMessage("ERROR", "No branches found, please configure your database first."));
            gotBranch = false;
        }
    }
    
    /**
     * Start the server's thread
     */

    private void startThread() {
        if (gotBranch) {
            try {
                cw = new CommunicationWrapper(5000);
                jtaMessage.append(fm.formatMessage("SERVER", "Server started, in idle state\n"));
                thread = new Thread(new ServerThread());
                thread.start();
                stopServer = false;
                serverStarted = true;
            } catch (SocketException ex) {
                jtaMessage.append(fm.formatMessage("ERROR", "Unable to start server : " + ex.getMessage() + "\n"));
            }
        } else {
            jtaMessage.append(fm.formatMessage("ERROR", "No branches found, please configure your database first."));
        }
    }
    
    /**
     * Check account's balance with the given account number
     * 
     * @param json JSON string received
     * @throws JSONException
     * @throws UnknownHostException 
     */

    private void query(JSONObject json) throws JSONException, UnknownHostException {
        // request and data received
        jtaMessage.append(fm.formatMessage("SERVER", "Request received, Processing now..\n"));
        jtaMessage.append(fm.formatMessage(Operation.QUERY.toString(), "Operation Type : Check balance\n"));
        // reading data received
        JSONObject qData = json.getJSONObject("content");
        JSONObject returnValue = new JSONObject();
        JSONObject returnContent = new JSONObject();
        jtaMessage.append(fm.formatMessage(Operation.QUERY.toString(), "Reading data received..\n"));
        // strip branch code from account number
        String accBranch = qData.getString("accNo").substring(0, 4);
        returnContent.put("bCode", qData.getString("bCode"));
        returnContent.put("address", qData.getString("address"));
        returnContent.put("port", qData.getInt("port"));
        // check if belongs to this branch
        if (accBranch.equals(this.branchCode)) {
            // it's belong to this branch
            query = new Query();
            query.setAccNo(qData.getString("accNo"));
            jtaMessage.append(fm.formatMessage(Operation.QUERY.toString(), "Checking balance\n"));
            // try to check for account balance
            String result = query.query(dbCon);
            String[] split = result.split(" ");
            // prepare for respond back
            returnValue.put("operation", Operation.RESPONSE);
            if (split[0].equals("Success")) {
                // got this account, put in return value
                String balance = split[1];
                jtaMessage.append(fm.formatMessage(Operation.QUERY.toString(), "Operation success\n"));
                jtaMessage.append(fm.formatMessage("SERVER", "Sending back respond\n"));
                returnContent.put("balance", Double.parseDouble(balance));
                returnContent.put("result", split[0]);
                returnValue.put("content", returnContent);
                // send out respond
                respond(returnValue);
            } else {
                // account not found, put into return value
                returnContent.put("result", result);
                returnValue.put("content", returnContent);
                // send out respond
                respond(returnValue);
                jtaMessage.append(fm.formatMessage("ERROR", "Unable to check account : " + result + "\n"));
            }
        } else {
            // not belongs to this branch
            jtaMessage.append(fm.formatMessage("SERVER", "Not belongs to this branch, send to next branch\n"));
            branch = new Branch();
            // get neighbor's branch code
            branch.setBranchCode(cw.whoIsNeighbors(branchCode));
            // send to neighbor brnanch
            cw.send(json, InetAddress.getByName(branch.obtainBranchIp(dbCon)), 5000);
        }
        jtaMessage.append(fm.formatMessage("SERVER", "Operation ended, return to idle state\n"));
    }
    
    /**
     * Withdraw money from the specific account
     * 
     * @param json JSON string received
     * @throws JSONException
     * @throws UnknownHostException 
     */

    private void withdraw(JSONObject json) throws JSONException, UnknownHostException {
        // request and data received
        jtaMessage.append(fm.formatMessage("SERVER", "Request received, Processing now..\n"));
        jtaMessage.append(fm.formatMessage(Operation.WITHDRAW.toString(), "Operation Type : Withdraw\n"));
        // reading data received
        JSONObject wData = json.getJSONObject("content");
        JSONObject returnValue = new JSONObject();
        JSONObject returnContent = new JSONObject();
        jtaMessage.append(fm.formatMessage(Operation.WITHDRAW.toString(), "Reading data received..\n"));
        // strip branch code from account number
        String accBranch = wData.getString("accNo").substring(0, 4);
        returnContent.put("bCode", wData.getString("bCode"));
        returnContent.put("address", wData.getString("address"));
        returnContent.put("port", wData.getInt("port"));
        // check if belongs to this branch
        if (accBranch.equals(this.branchCode)) {
            // it's belongs to this branch
            withdraw = new Withdraw();
            withdraw.setAccNo(wData.getString("accNo"));
            withdraw.setAmount(wData.getDouble("amount"));
            withdraw.setIcNo(wData.getString("icNo"));
            jtaMessage.append(fm.formatMessage(Operation.WITHDRAW.toString(), "Processing withdrawal\n"));
            // try to withdraw money from account
            String result = withdraw.withdraw(dbCon);
            // prepare for respond bacl
            returnValue.put("operation", Operation.RESPONSE);
            if (result.equals("Success")) {
                // withdraw success, prepare for create transaction log
                tLog = new TransactionLog();
                tLog.setAccNo(wData.getString("accNo"));
                tLog.setAmount(wData.getDouble("amount"));
                tLog.setTransactionDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                tLog.setTransactionType(TransactionType.DBT.toString());
                // create log with provided data
                tLog.createTransactionLog(dbCon);
                query = new Query();
                query.setAccNo(wData.getString("accNo"));
                // get back account balance
                String balance = query.query(dbCon).split(" ")[1];
                jtaMessage.append(fm.formatMessage(Operation.WITHDRAW.toString(), "Operation success\n"));
                jtaMessage.append(fm.formatMessage("SERVER", "Sending back respond\n"));
                returnContent.put("balance", Double.parseDouble(balance));
                returnContent.put("result", result);
                returnValue.put("content", returnContent);
                // send out respond
                respond(returnValue);
            } else {
                // withdrawal failed
                returnContent.put("result", result);
                returnValue.put("content", returnContent);
                // send out respond
                respond(returnValue);
                jtaMessage.append(fm.formatMessage("ERROR", "Unable to withdraw : " + result + "\n"));
            }
        } else {
            // not belongs to this branch, prepare to send to next branch
            jtaMessage.append(fm.formatMessage("SERVER", "Not belongs to this branch, send to next branch\n"));
            branch = new Branch();
            // get neighbor's branch code
            branch.setBranchCode(cw.whoIsNeighbors(branchCode));
            // send to next branch
            cw.send(json, InetAddress.getByName(branch.obtainBranchIp(dbCon)), 5000);
        }
        jtaMessage.append(fm.formatMessage("SERVER", "Operation ended, return to idle state\n"));
    }
    
    /**
     * Deposit money to the specific account
     * 
     * @param json JSON string received
     * @throws JSONException
     * @throws UnknownHostException 
     */

    private void deposit(JSONObject json) throws JSONException, UnknownHostException {
        // request and data received
        jtaMessage.append(fm.formatMessage("SERVER", "Request received, Processing now..\n"));
        jtaMessage.append(fm.formatMessage(Operation.DEPOSIT.toString(), "Operation Type : Deposit\n"));
        // reading data received
        JSONObject dData = json.getJSONObject("content");
        JSONObject returnValue = new JSONObject();
        JSONObject returnContent = new JSONObject();
        jtaMessage.append(fm.formatMessage(Operation.DEPOSIT.toString(), "Reading data received..\n"));
        // strip branch code from account number
        String accBranch = dData.getString("accNo").substring(0, 4);
        System.out.println(accBranch);
        returnContent.put("bCode", dData.getString("bCode"));
        returnContent.put("address", dData.getString("address"));
        returnContent.put("port", dData.getInt("port"));
        // check if it belongs to this branch
        if (accBranch.equals(this.branchCode)) {
            // it's belongs to this branch
            deposit = new Deposit();
            deposit.setAccNo(dData.getString("accNo"));
            deposit.setAmount(dData.getDouble("amount"));
            deposit.setIcNo(dData.optString("icNo"));
            jtaMessage.append(fm.formatMessage(Operation.DEPOSIT.toString(), "Processing deposit\n"));
            // try to deposit money to account
            String result = deposit.deposit(dbCon);
            // prepare for respond back
            returnValue.put("operation", Operation.RESPONSE);
            if (result.equals("Success")) {
                // deposit successful, prepare for create transction log
                tLog = new TransactionLog();
                tLog.setAccNo(dData.getString("accNo"));
                tLog.setAmount(dData.getDouble("amount"));
                tLog.setTransactionDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                tLog.setTransactionType(TransactionType.CRD.toString());
                // create transaction log based on given details
                tLog.createTransactionLog(dbCon);
                query = new Query();
                query.setAccNo(dData.getString("accNo"));
                // get account's balance
                String balance = query.query(dbCon).split(" ")[1];
                jtaMessage.append(fm.formatMessage(Operation.DEPOSIT.toString(), "Operation success\n"));
                jtaMessage.append(fm.formatMessage("SERVER", "Sending back respond\n"));
                returnContent.put("balance", Double.parseDouble(balance));
                returnContent.put("result", result);
                returnValue.put("content", returnContent);
                // send out respond
                respond(returnValue);
            } else {
                // deposit failed
                returnContent.put("result", result);
                returnValue.put("content", returnContent);
                // send out respond
                respond(returnValue);
                jtaMessage.append(fm.formatMessage("ERROR", "Unable to deposit : " + result + "\n"));
            }
        } else {
            // not belongs to this branch, prepare to send to next branch
            jtaMessage.append(fm.formatMessage("SERVER", "Not belongs to this branch, send to next branch\n"));
            branch = new Branch();
            // get neighbor's branch code
            branch.setBranchCode(cw.whoIsNeighbors(branchCode));
            // send to next branch
            cw.send(json, InetAddress.getByName(branch.obtainBranchIp(dbCon)), 5000);
        }
        jtaMessage.append(fm.formatMessage("SERVER", "Operation ended, return to idle state\n"));
    }
    
    /**
     * Transfer money from source account to destination account
     * 
     * 
     * @param json JSON string received
     * @throws JSONException
     * @throws UnknownHostException 
     */

    private void transfer(JSONObject json) throws JSONException, UnknownHostException {
        // request and data received
        jtaMessage.append(fm.formatMessage("SERVER", "Request received, Processing now..\n"));
        jtaMessage.append(fm.formatMessage(Operation.TRANSFER.toString(), "Operation Type : Transfer\n"));
        // reading data received
        JSONObject tData = json.getJSONObject("content");
        JSONObject returnValue = new JSONObject();
        JSONObject returnContent = new JSONObject();
        jtaMessage.append(fm.formatMessage(Operation.TRANSFER.toString(), "Reading data received..\n"));
        returnContent.put("bCode", tData.getString("bCode"));
        returnContent.put("address", tData.getString("address"));
        returnContent.put("port", tData.getInt("port"));
        // check if withdrawal has been done on source account or not
        if (json.optBoolean("withdraw", false) == false) {
            // withdrawal have not yet done on source account
            // strip branch code from account number
            String accBranch = tData.getString("accNo").substring(0, 4);
            // check if it belongs to this branch or not
            if (accBranch.equals(this.branchCode)) {
                // it's belongs to this branch
                withdraw = new Withdraw();
                withdraw.setAccNo(tData.getString("accNo"));
                withdraw.setAmount(tData.getDouble("amount"));
                jtaMessage.append(fm.formatMessage(Operation.TRANSFER.toString(), "Transfering to account\n"));
                // try to take out money from source account
                String res = withdraw.transferWithdraw(dbCon);
                if (res.equals("Success")) {
                    // successfully take out money, create a transaction log
                    tLog = new TransactionLog();
                    tLog.setAccNo(tData.getString("accNo"));
                    tLog.setAmount(tData.getDouble("amount"));
                    tLog.setTransactionDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    tLog.setTransactionType(TransactionType.DBT.toString());
                    tLog.createTransactionLog(dbCon);
                    query = new Query();
                    // get source account's new balance
                    query.setAccNo(tData.getString("accNo"));
                    String balance = query.query(dbCon).split(" ")[1];
                    // set withdraw to true
                    json.put("withdraw", true);
                    json.put("balance", Double.parseDouble(balance));
                    // transfer to destination account
                    transfer(json);
                } else {
                    // operation failed, unable to take out money
                    returnContent.put("result", res);
                    returnValue.put("content", returnContent);
                    // send back respond
                    respond(returnValue);
                    jtaMessage.append(fm.formatMessage("ERROR", "Unable to take out money : " + res + "\n"));
                }
            } else {
                // not belongs to this branch, prepare to send to next branch
                jtaMessage.append(fm.formatMessage("SERVER", "Not belongs to this branch, send to next branch\n"));
                branch = new Branch();
                // get neighbor's branch code
                branch.setBranchCode(cw.whoIsNeighbors(branchCode));
                // send to next branch
                cw.send(json, InetAddress.getByName(branch.obtainBranchIp(dbCon)), 5000);
            }
        } else {
            // withdrawal has been done on source account, proceed to deposit to
            // destination account
            // strip branch code from account number
            String accReceiver = tData.getString("accReceiver").substring(0, 4);
            // check if it belongs to this branch
            if (accReceiver.equals(this.branchCode)) {
                // it's belongs to this branch
                deposit = new Deposit();
                deposit.setAccNo(tData.getString("accReceiver"));
                deposit.setAmount(tData.getDouble("amount"));
                // try to transfer the amount to destination account
                String result = deposit.transferDeposit(dbCon);
                jtaMessage.append(fm.formatMessage(Operation.TRANSFER.toString(), "Transfering to account\n"));
                // prepare for respond back
                returnValue.put("operation", Operation.RESPONSE);
                if (result.equals("Success")) {
                    // transfer succeed, create a transaction log
                    tLog = new TransactionLog();
                    tLog.setAccNo(tData.getString("accNo"));
                    tLog.setAmount(tData.getDouble("amount"));
                    tLog.setTransactionDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    tLog.setTransactionType(TransactionType.CRD.toString());
                    tLog.createTransactionLog(dbCon);
                    jtaMessage.append(fm.formatMessage(Operation.TRANSFER.toString(), "Operation success\n"));
                    jtaMessage.append(fm.formatMessage("SERVER", "Sending back respond\n"));
                    // put in necessary data for respond
                    returnContent.put("result", result);
                    returnContent.put("balance", json.getDouble("balance"));
                    returnValue.put("content", returnContent);
                    // send out respond
                    respond(returnValue);
                } else {
                    // transfer failed
                    returnContent.put("result", result);
                    returnValue.put("content", returnContent);
                    // send out respond
                    respond(returnValue);
                    jtaMessage.append(fm.formatMessage("ERROR", "Unable to transfer the money : " + result + "\n"));
                }
            } else {
                // not belongs to this branch, prepare to send to next branch
                jtaMessage.append(fm.formatMessage("SERVER", "Not belongs to this branch, send to next branch\n"));
                branch = new Branch();
                // get neighbor's branch code
                branch.setBranchCode(cw.whoIsNeighbors(branchCode));
                // send to next branch
                cw.send(json, InetAddress.getByName(branch.obtainBranchIp(dbCon)), 5000);
            }
        }
        jtaMessage.append(fm.formatMessage("SERVER", "Operation ended, return to idle state\n"));


    }
    
    /**
     * Apply a loan on this branch with the details provided 
     * 
     * @param json JSON string received
     * @throws JSONException
     * @throws UnknownHostException 
     */

    private void loan(JSONObject json) throws JSONException, UnknownHostException {
        // request and data received
        jtaMessage.append(fm.formatMessage("SERVER", "Request received, Processing now..\n"));
        jtaMessage.append(fm.formatMessage(Operation.LOAN.toString(), "Operation Type : Loan Application\n"));
        //reading data received
        JSONObject lData = json.getJSONObject("content");
        jtaMessage.append(fm.formatMessage(Operation.LOAN.toString(), "Reading data received..\n"));
        // create a new loan application
        loan = new LoanApplication();
        loan.setLoantype(lData.getString("type"));
        loan.setLoanamount(lData.getDouble("loanamount"));
        loan.setInterestrate(lData.getDouble("rate"));
        loan.setDuration(lData.getString("duration"));
        loan.setName(lData.getString("name"));
        loan.setIcNo(lData.getString("icno"));
        loan.setContactNo(lData.getString("contact"));
        jtaMessage.append(fm.formatMessage(Operation.LOAN.toString(), "Processing Application\n"));
        // try to apply for loan
        String result = loan.applyLoan(dbCon);
        //prepare for respond back
        JSONObject returnValue = new JSONObject();
        returnValue.put("result", result);
        if (result.equals("Success")) {
            // application succeed, send back respond to client
            jtaMessage.append(fm.formatMessage(Operation.LOAN.toString(), "Application successful.\n"));
            jtaMessage.append(fm.formatMessage("SERVER", "Sending back respond\n"));
            cw.send(returnValue, InetAddress.getByName(lData.getString("address")), lData.getInt("port"));
        } else {
            // application failed, send back respond to client
            cw.send(returnValue, InetAddress.getByName(lData.getString("address")), lData.getInt("port"));
            jtaMessage.append(fm.formatMessage("ERROR", "Unable to apply loan : " + result + "\n"));
        }
        jtaMessage.append(fm.formatMessage("SERVER", "Operation ended, return to idle state\n"));
    }
    
    /**
     * Create a new account on this branch with the provided details
     * 
     * @param json JSON string received
     * @throws JSONException
     * @throws UnknownHostException 
     */

    private void register(JSONObject json) throws JSONException, UnknownHostException {
        // request and data received
        jtaMessage.append(fm.formatMessage("SERVER", "Request received, Processing now..\n"));
        jtaMessage.append(fm.formatMessage(Operation.REGISTER.toString(), "Operation Type : Account Registration\n"));
        // reading data received
        JSONObject rData = json.getJSONObject("content");
        jtaMessage.append(fm.formatMessage(Operation.REGISTER.toString(), "Reading data received..\n"));
        // create a new account application
        account = new AccountApplication();
        account.setAcctype(rData.getString("type"));
        account.setAddress(rData.getString("addressss"));
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
        jtaMessage.append(fm.formatMessage(Operation.REGISTER.toString(), "Processing Application\n"));
        // try to apply for account
        String result = account.applyAccount(dbCon);
        // prepare for respond back
        JSONObject returnValue = new JSONObject();
        returnValue.put("result", result);
        if (result.equals("Success")) {
            // application succeed, send back respond
            jtaMessage.append(fm.formatMessage(Operation.REGISTER.toString(), "Application successful.\n"));
            jtaMessage.append(fm.formatMessage("SERVER", "Sending back respond\n"));
            cw.send(returnValue, InetAddress.getByName(rData.getString("address")), rData.getInt("port"));
        } else {
            // application failed, send back respond
            cw.send(returnValue, InetAddress.getByName(rData.getString("address")), rData.getInt("port"));
            jtaMessage.append(fm.formatMessage("ERROR", "Unable to apply new account : " + result + "\n"));
        }
        jtaMessage.append(fm.formatMessage("SERVER", "Operation ended, return to idle state\n"));
    }
    
    /**
     * Check if update passbook request is belongs to this branch or not
     * if yes, retrieve data from database and send out along with the respond,
     * else, send to next branch's server
     * 
     * @param json JSON String received
     * @throws JSONException
     * @throws UnknownHostException 
     */

    private void updatePassbook(JSONObject json) throws JSONException, UnknownHostException {
        // request and data received
        jtaMessage.append(fm.formatMessage("SERVER", "Request received, Processing now..\n"));
        jtaMessage.append(fm.formatMessage(Operation.TRANSACTION.toString(), "Operation Type : Update Passbook\n"));
        // reading data received
        JSONObject uData = json.getJSONObject("content");
        JSONObject returnValue = new JSONObject();
        JSONObject returnContent = new JSONObject();
        jtaMessage.append(fm.formatMessage(Operation.TRANSACTION.toString(), "Reading data received..\n"));
        // strip branch code from account number
        String accBranch = uData.getString("accno").substring(0, 4);
        returnContent.put("bCode", uData.getString("bCode"));
        returnContent.put("address", uData.getString("address"));
        returnContent.put("port", uData.getInt("port"));
        // check if it belongs to this branch
        if (accBranch.equals(this.branchCode)) {
            // it's belongs to this branch
            tLog = new TransactionLog();
            // retrieve transaction logs from database
            ArrayList<TransactionLog> log = tLog.getTransactionLog(dbCon, uData.getString("accno"));
            JSONArray jsonArr = new JSONArray();
            // construct data from the list of logs
            for (int i = 0; i < log.size(); i++) {
                TransactionLog transactionLog = log.get(i);
                JSONObject temp = new JSONObject();
                temp.put("accNo", transactionLog.getAccNo());
                temp.put("transactionType", transactionLog.getTransactionType());
                temp.put("transactionDate", transactionLog.getTransactionDate());
                temp.put("amount", transactionLog.getAmount());
                jsonArr.put(temp);
            }
            jtaMessage.append(fm.formatMessage(Operation.TRANSACTION.toString(), "Processing update passbook request\n"));
            // prepare to respond back
            returnValue.put("operation", Operation.RESPONSE);
            if (jsonArr.length() > 0) {
                // if got records
                returnContent.put("result", jsonArr);
                returnValue.put("content", returnContent);
                returnValue.put("record", true);
                jtaMessage.append(fm.formatMessage(Operation.TRANSACTION.toString(), "Request successful\n"));
                jtaMessage.append(fm.formatMessage("SERVER", "Sending back respond\n"));
                // send out respond
                respond(returnValue);
            } else {
                // no records found
                returnContent.put("result", JSONObject.NULL);
                returnValue.put("content", returnContent);
                returnValue.put("record", false);
                // send out respond
                respond(returnValue);
                jtaMessage.append(fm.formatMessage("ERROR", "Unable to process request : " + "No record found" + "\n"));
            }
        } else {
            // not belongs to this branch, prepare to send to next branch
            jtaMessage.append(fm.formatMessage("SERVER", "Not belongs to this branch, send to next branch\n"));
            branch = new Branch();
            // get branch's neighbor's branch code
            branch.setBranchCode(cw.whoIsNeighbors(branchCode));
            // send to next branch
            cw.send(json, InetAddress.getByName(branch.obtainBranchIp(dbCon)), 5000);
        }
        jtaMessage.append(fm.formatMessage("SERVER", "Operation ended, return to idle state\n"));
    }

    /**
     * Check if respond being sent back to this server belongs to this branch
     * or not, if not, send out to next branch, else, send back to the client
     * 
     * 
     * @param json JSON string received
     * @throws JSONException
     * @throws UnknownHostException 
     */
    private void respond(JSONObject json) throws JSONException, UnknownHostException {
        // request and data received
        jtaMessage.append(fm.formatMessage("SERVER", "Request received, Processing now..\n"));
        jtaMessage.append(fm.formatMessage(Operation.RESPONSE.toString(), "Operation Type : Response\n"));
        // reading data received
        JSONObject js = json.getJSONObject("content");
        String bCode = js.getString("bCode");
        // check if this respond belongs to this branch
        if (bCode.equals(this.branchCode)) {
            // respond belongs to this branch
            jtaMessage.append(fm.formatMessage(Operation.RESPONSE.toString(), "Respond belongs to this branch\n"));
            jtaMessage.append(fm.formatMessage(Operation.RESPONSE.toString(), "Returning to client\n"));
            // send back respond to client
            cw.send(json, InetAddress.getByName(js.getString("address")), js.getInt("port"));
        } else {
            // respond not belongs to this branch, prepare to send to next branch
            jtaMessage.append(fm.formatMessage(Operation.RESPONSE.toString(), "Respond not belongs to this branch\n"));
            jtaMessage.append(fm.formatMessage(Operation.RESPONSE.toString(), "Send to neighbor branch\n"));
            branch = new Branch();
            // get branch's neighbor's branch code
            branch.setBranchCode(cw.whoIsNeighbors(branchCode));
            // send to next branch
            cw.send(json, InetAddress.getByName(branch.obtainBranchIp(dbCon)), 5000);
        }
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
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Bank Ladesh Banking Server");
        setResizable(false);
        getContentPane().setLayout(null);

        jspMessage.getVerticalScrollBar().setUI(new CustomScrollBarUI());

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
        jspMessage.setBounds(10, 50, 650, 330);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Set Branch Code");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 10, 110, 30);

        jComboBox1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select the branch you belongs to" }));
        getContentPane().add(jComboBox1);
        jComboBox1.setBounds(120, 10, 210, 30);

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setText("Start");
        jButton1.setFocusPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(340, 10, 100, 30);

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton2.setText("Stop");
        jButton2.setEnabled(false);
        jButton2.setFocusPainted(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(450, 10, 100, 30);

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton3.setText("Clear");
        jButton3.setFocusPainted(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3);
        jButton3.setBounds(560, 10, 100, 30);

        setSize(new java.awt.Dimension(686, 429));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // start server if user selected a valid branch code
        if (jComboBox1.getSelectedIndex() != 0) {
            branchCode = jComboBox1.getSelectedItem().toString();
            this.setTitle("Bank Ladesh Banking Server - Branch " + branchCode);
            jtaMessage.append(fm.formatMessage("SERVER", "Branch Code configured\n"));
            jtaMessage.append(fm.formatMessage("SERVER", "Starting server...\n"));
            startThread();
            if (serverStarted) {
                jButton2.setEnabled(true);
                jComboBox1.setEnabled(false);
                jButton1.setEnabled(false);
                jLabel1.setEnabled(false);
            }
        } else {
            jtaMessage.append(fm.formatMessage("ERROR", "Please select a vaild branch code\n"));
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // stop server if server is started
        if (serverStarted) {
            stopServer = true;
            jButton2.setEnabled(false);
            jComboBox1.setEnabled(true);
            jButton1.setEnabled(true);
            cw.close();
            jtaMessage.append(fm.formatMessage("SERVER", "Server stopped\n"));
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // clear text area's message
        jtaMessage.setText(fm.formatMessage("SERVER", "Message cleared\n"));
    }//GEN-LAST:event_jButton3ActionPerformed

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
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jspMessage;
    private javax.swing.JTextArea jtaMessage;
    // End of variables declaration//GEN-END:variables

    /**
     * Server's thread that keep running until user stop the server
     */
    public class ServerThread implements Runnable {

        @Override
        public void run() {
            while (!stopServer) {
                try {
                    JSONObject dataReceived = cw.receive();
                    System.out.println(dataReceived.toString());
                    boolean error = dataReceived.optBoolean("error");
                    if (!error) {
                        String op = dataReceived.getString("operation");
                        Operation operation = Operation.valueOf(op.toUpperCase());
                        executor.execute(new WorkHelper(operation, dataReceived));
                    } else {
                        jtaMessage.append(fm.formatMessage("ERROR", dataReceived.optString("errorMessage", "no data received") + "\n"));
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    /**
     * helper class that allows the server for multithreading
     */

    public class WorkHelper implements Runnable {

        private Operation op;
        private JSONObject json;

        public WorkHelper(Operation op, JSONObject json) {
            this.op = op;
            this.json = json;
        }

        @Override
        public void run() {
            // check operation type and execute accordingly
            try {
                switch (op) {
                    case QUERY:
                        query(json);
                        break;
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
