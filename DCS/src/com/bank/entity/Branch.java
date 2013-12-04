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
public class Branch {

    private Integer bid;
    private String branchCode;
    private String ipaddress;
    private String getAllBranchCode = "SELECT branchCode FROM branch;";
    private String getBranchCode = "SELECT branchCode FROM branch WHERE bid = ?;";
    private String getBranchId = "SELECT bid FROM branch WHERE branchCode = ?;";
    private String getBranchIP = "SELECT ipAddress FROM branch WHERE branchCode = ?;";

    public Branch() {
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public ArrayList<String> obtainAllBranchCode(Connection con) {
        ArrayList<String> data = new ArrayList<>();
        try {
            PreparedStatement pstmtSelect = con.prepareStatement(this.getAllBranchCode);
            ResultSet rs = pstmtSelect.executeQuery();
            while (rs.next()) {
                String bCode = rs.getString(1);
                data.add(bCode);
            }
        } catch (SQLException ex) {
        }
        return data;
    }

    public String obtainBranchCode(Connection con) {
        try {
            PreparedStatement pstmtSelect = con.prepareStatement(this.getBranchCode);
            pstmtSelect.setInt(1, this.bid);
            ResultSet rs = pstmtSelect.executeQuery();
            if (rs.next()) {
                String bCode = rs.getString(1);
                return bCode;
            } else {
                return "Branch not found.";
            }
        } catch (SQLException ex) {
            return ex.getMessage();
        }
    }
    
    public String obtainBranchId(Connection con) {
        try {
            PreparedStatement pstmtSelect = con.prepareStatement(this.getBranchId);
            pstmtSelect.setString(1, this.branchCode);
            ResultSet rs = pstmtSelect.executeQuery();
            if (rs.next()) {
                String bCode = rs.getString(1);
                return bCode;
            } else {
                return "Branch not found.";
            }
        } catch (SQLException ex) {
            return ex.getMessage();
        }
    }

    public String obtainBranchIp(Connection con) {
        try {
            PreparedStatement pstmtSelect = con.prepareStatement(this.getBranchIP);
            pstmtSelect.setString(1, this.branchCode);
            ResultSet rs = pstmtSelect.executeQuery();
            if (rs.next()) {
                String ipAdd = rs.getString(1);
                return ipAdd;
            } else {
                return "Branch not found.";
            }
        } catch (SQLException ex) {
            return ex.getMessage();
        }
    }
}
