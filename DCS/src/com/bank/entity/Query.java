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
public class Query {
    private String accNo;
    private String selectBalance = "SELECT balance FROM account WHERE accNo = ?;";

    public Query() {
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }
    
    /**
     * Check account's balance with the given account number
     * 
     * @param con connection to database
     * @return result of checking account
     */
    
    public String query(Connection con){
        try{
            PreparedStatement pstmt = con.prepareStatement(this.selectBalance);
            pstmt.setString(1, this.accNo);
            ResultSet result = pstmt.executeQuery();
            if(result.next()){
                Double balance = result.getDouble(1);
                return "Success " + balance;
            }else{
                return "Account not found";
            }
        }catch(SQLException ex){
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
