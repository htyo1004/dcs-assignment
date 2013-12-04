/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Kenny
 */
public class TransactionLog {

    private String transactionType;
    private Double amount;
    private String transactionDate;
    private String accNo;
    private String createTransactionLog =
            "INSERT INTO transactionlog(transactionType, amount, transactionDate, aid) "
            + "VALUES(?, ?, ?, (SELECT aid FROM account WHERE accNo = ?));";
    private String getTransactionLog =
            "SELECT accNo, transactionType, amount, transactionDate "
            + "FROM transactionlog t, account a "
            + "WHERE t.aid = a.aid AND t.aid = (SELECT aid FROM account WHERE accNo = ?) ;";

    public TransactionLog() {
    }

    public TransactionLog(String transactionType, Double amount, String transactionDate, String accNo) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.accNo = accNo;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public boolean createTransactionLog(Connection con) {
        try {
            con.setAutoCommit(false);
            PreparedStatement pstmtInsert = con.prepareStatement(this.createTransactionLog);
            pstmtInsert.setString(1, this.transactionType);
            pstmtInsert.setDouble(2, this.amount);
            pstmtInsert.setString(3, this.transactionDate);
            pstmtInsert.setString(4, this.accNo);
            pstmtInsert.executeUpdate();
            con.commit();
            return true;
        } catch (SQLException ex) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex1) {
                    ex1.printStackTrace();
                }
            }
            ex.printStackTrace();
            return false;
        }
    }

    public ArrayList<TransactionLog> getTransactionLog(Connection con, String accNo) {
        ArrayList<TransactionLog> transactionLog = new ArrayList<>();
        try {
            PreparedStatement pstmtSelect = con.prepareStatement(this.getTransactionLog);
            pstmtSelect.setString(1, accNo);
            ResultSet log = pstmtSelect.executeQuery();
            while (log.next()) {
                TransactionLog tempLog = new TransactionLog();
                tempLog.setAccNo(log.getString("accNo"));
                tempLog.setAmount(log.getDouble("amount"));
                tempLog.setTransactionDate(log.getString("transactionDate"));
                tempLog.setTransactionType(log.getString("transactionType"));
                transactionLog.add(tempLog);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return transactionLog;
    }

    @Override
    public String toString() {
        return "TransactionLog{" + "\ntransactionType=" + transactionType + ", \namount=" + amount + ", \ntransactionDate=" + transactionDate + ", \naccNo=" + accNo + "}\n";
    }
}
