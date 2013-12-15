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
public class Withdraw {

    private String accNo;
    private Double amount;
    private String icNo;
    private String checkAccount = "SELECT balance FROM account WHERE accNo = ? AND accHolder = ?;";
    private String withdrawMoney = "UPDATE account SET balance = (balance - ?) WHERE accNo = ?";
    private String transfer = "SELECT balance FROM account WHERE accNo = ?;";

    public Withdraw() {
    }

    public Withdraw(String accNo, Double amount, String icNo) {
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
    
    /**
     * Withdraw money from the given account number
     * 
     * @param con connection to database
     * @return result of withdrawal
     */

    public String withdraw(Connection con) {
        try {
            con.setAutoCommit(false);
            PreparedStatement pstmtSelect = con.prepareStatement(this.checkAccount);
            pstmtSelect.setString(1, this.accNo);
            pstmtSelect.setString(2, this.icNo);
            ResultSet check = pstmtSelect.executeQuery();
            if (check.next()) {
                Double balance = check.getDouble("balance");
                if ((balance - 1) > amount) {
                    PreparedStatement pstmtInsert = con.prepareStatement(this.withdrawMoney);
                    pstmtInsert.setDouble(1, this.amount);
                    pstmtInsert.setString(2, this.accNo);
                    pstmtInsert.executeUpdate();
                    con.commit();
                    return "Success";
                } else {
                    con.commit();
                    return "Insufficient balance in account.";
                }
            } else {
                con.commit();
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
    
    /**
     * Withdrawal used in transfer operation, take out the specific
     * amount of money from source account
     * 
     * @param con connection to database
     * @return result of taking out money
     */
    
     public String transferWithdraw(Connection con) {
        try {
            con.setAutoCommit(false);
            PreparedStatement pstmtSelect = con.prepareStatement(this.transfer);
            pstmtSelect.setString(1, this.accNo);
         
            ResultSet check = pstmtSelect.executeQuery();
            if (check.next()) {
                Double balance = check.getDouble("balance");
                if ((balance - 1) > amount) {
                    PreparedStatement pstmtInsert = con.prepareStatement(this.withdrawMoney);
                    pstmtInsert.setDouble(1, this.amount);
                    pstmtInsert.setString(2, this.accNo);
                    pstmtInsert.executeUpdate();
                    con.commit();
                    return "Success";
                } else {
                    con.commit();
                    return "Insufficient balance in account.";
                }
            } else {
                con.commit();
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
