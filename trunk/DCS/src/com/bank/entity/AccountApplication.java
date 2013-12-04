/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Kenny
 */
public class AccountApplication {

    private String icno;
    private String firstname;
    private String lastname;
    private String gender;
    private String address;
    private String state;
    private String city;
    private String postcode;
    private String contactNo;
    private String email;
    private String acctype;
    private Integer bid;
    private String createAccHolder =
            "INSERT INTO "
            + "accountholder(accHolder, firstname, lastname, gender, "
            + "address, state, city, postCode, contactNo, email)"
            + " VALUES(?,?,?,?,?,?,?,?,?,?);";
    private String createAcc =
            "INSERT INTO account(accType, accHolder, dateCreated, bid) VALUES(?,?,CURDATE(),?);";
    private String updateAccNo = "UPDATE account SET accNo = ? WHERE aid = ?;";

    public AccountApplication() {
    }

    public String getIcno() {
        return icno;
    }

    public void setIcno(String icno) {
        this.icno = icno;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAcctype() {
        return acctype;
    }

    public void setAcctype(String acctype) {
        this.acctype = acctype;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public String applyAccount(Connection con) {
        try {
            con.setAutoCommit(false);
            PreparedStatement pstmtInsert = con.prepareStatement(this.createAccHolder);
            pstmtInsert.setString(1, this.icno);
            pstmtInsert.setString(2, this.firstname);
            pstmtInsert.setString(3, this.lastname);
            pstmtInsert.setString(4, this.gender);
            pstmtInsert.setString(5, this.address);
            pstmtInsert.setString(6, this.state);
            pstmtInsert.setString(7, this.city);
            pstmtInsert.setString(8, this.postcode);
            pstmtInsert.setString(9, this.contactNo);
            pstmtInsert.setString(10, this.email);
            int rowCount = pstmtInsert.executeUpdate();
            if (rowCount > 0) {
                pstmtInsert.close();
                pstmtInsert = con.prepareStatement(this.createAcc, Statement.RETURN_GENERATED_KEYS);
                pstmtInsert.setString(1, this.acctype);
                pstmtInsert.setString(2, this.icno);
                pstmtInsert.setInt(3, this.bid);
                pstmtInsert.executeUpdate();
                ResultSet rs = pstmtInsert.getGeneratedKeys();
                if (rs.next()) {
                    Branch branch = new Branch();
                    branch.setBid(this.bid);
                    String branchCode = branch.obtainBranchCode(con);
                    int id = rs.getInt(1);
                    System.out.println(rs.getString(1));
                    String accNo = new StringBuilder().append(branchCode).append(String.format("%010d", id)).toString();
                    System.out.println(accNo);
                    PreparedStatement pstmtUpdate = con.prepareStatement(this.updateAccNo);
                    pstmtUpdate.setString(1, accNo);
                    pstmtUpdate.setInt(2, id);
                    int code = pstmtUpdate.executeUpdate();
                    if (code > 0) {
                        con.commit();
                        return "Success";
                    } else {
                        con.rollback();
                        return "Unable to create account.";
                    }
                } else {
                    con.rollback();
                    return "Unable to create account.";
                }
            } else {
                con.rollback();
                return "Unable to create account.";
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
