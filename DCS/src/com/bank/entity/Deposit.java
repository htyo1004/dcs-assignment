/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Kenny
 */
public class Deposit {

    private String accNo;
    private Double amount;
    private String icNo;
    private String checkAccount = "SELECT * FROM account WHERE accNo = ? AND accHolder = ?;";
    private String depositMoney = "UPDATE account SET balance = (balance + ?) WHERE accNo = ?;";
    private String transfer = "SELECT * FROM account WHERE accNo = ?;";

    public Deposit() {
    }

    public Deposit(String accNo, Double amount, String icNo) {
        this.accNo = accNo;
        this.amount = amount;
        this.icNo = icNo;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getIcNo() {
        return icNo;
    }

    public void setIcNo(String icNo) {
        this.icNo = icNo;
    }

    public String deposit(Connection con) {
        try {
            con.setAutoCommit(false);
            PreparedStatement pstmtSelect = con.prepareStatement(this.checkAccount);
            pstmtSelect.setString(1, this.accNo);
            pstmtSelect.setString(2, this.icNo);
            ResultSet check = pstmtSelect.executeQuery();
            if (check.next()) {
                pstmtSelect.close();
                PreparedStatement pstmtUpdate = con.prepareStatement(this.depositMoney);
                pstmtUpdate.setDouble(1, this.amount);
                pstmtUpdate.setString(2, this.accNo);
                pstmtUpdate.executeUpdate();
                con.commit();
                return "Success";
            } else {
                return "Account not found.";
            }
        } catch (SQLException ex) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex1) {
                    ex1.printStackTrace();
                }
            }
            return ex.getMessage();
        }
    }
    
    
     public String transferDeposit(Connection con) {
        try {
            con.setAutoCommit(false);
            PreparedStatement pstmtSelect = con.prepareStatement(this.transfer);
            pstmtSelect.setString(1, this.accNo);
            ResultSet check = pstmtSelect.executeQuery();
            if (check.next()) {
                pstmtSelect.close();
                PreparedStatement pstmtUpdate = con.prepareStatement(this.depositMoney);
                pstmtUpdate.setDouble(1, this.amount);
                pstmtUpdate.setString(2, this.accNo);
                pstmtUpdate.executeUpdate();
                con.commit();
                return "Success";
            } else {
                return "Account not found.";
            }
        } catch (SQLException ex) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex1) {
                    ex1.printStackTrace();
                }
            }
            return ex.getMessage();
        }
    }
}
