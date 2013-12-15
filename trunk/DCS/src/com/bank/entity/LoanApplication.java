/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Kenny
 */
public class LoanApplication {

    private String loantype;
    private Double loanamount;
    private Double interestrate;
    private String duration;
    private String name;
    private String icNo;
    private String contactNo;
    private String applyNewLoan =
            "INSERT INTO loan"
            + "(loanType, loanAmount, interestRate, duration, loanerName, loanerIc, loanerContact) "
            + "VALUES (?,?,?,?,?,?,?);";

    public LoanApplication() {
    }

    public LoanApplication(String loantype, Double loanamount, Double interestrate, String duration, String name, String icNo, String contactNo) {
        this.loantype = loantype;
        this.loanamount = loanamount;
        this.interestrate = interestrate;
        this.duration = duration;
        this.name = name;
        this.icNo = icNo;
        this.contactNo = contactNo;
    }

    public String getLoantype() {
        return loantype;
    }

    public void setLoantype(String loantype) {
        this.loantype = loantype;
    }

    public Double getLoanamount() {
        return loanamount;
    }

    public void setLoanamount(Double loanamount) {
        this.loanamount = loanamount;
    }

    public Double getInterestrate() {
        return interestrate;
    }

    public void setInterestrate(Double interestrate) {
        this.interestrate = interestrate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcNo() {
        return icNo;
    }

    public void setIcNo(String icNo) {
        this.icNo = icNo;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
    
    /**
     * Create a new loan application with the given details
     * 
     * @param con connection to database
     * @return result of applying loan
     */

    public String applyLoan(Connection con) {
        try {
            con.setAutoCommit(false);
            PreparedStatement pstmtInsert = con.prepareStatement(this.applyNewLoan);
            pstmtInsert.setString(1, this.loantype);
            pstmtInsert.setDouble(2, this.loanamount);
            pstmtInsert.setDouble(3, this.interestrate);
            pstmtInsert.setString(4, this.duration);
            pstmtInsert.setString(5, this.name);
            pstmtInsert.setString(6, this.icNo);
            pstmtInsert.setString(7, this.contactNo);
            pstmtInsert.executeUpdate();
            con.commit();
            return "Success";
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
