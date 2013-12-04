package com.bank.gui;

import com.bank.entity.Branch;
import com.bank.entity.MySQLConnection;
import com.bank.utils.CommunicationWrapper;
import com.bank.utils.Operation;
import com.bank.utils.TextFieldLimiter;
import com.bank.utils.Toast;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.AbstractDocument;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Moofie
 */
public class RegisterPanel extends javax.swing.JPanel {
    private boolean validate;
    private CommunicationWrapper cw;
    private String branchCode;
    
    /**
     * Creates new form RegisterPanel
     */
    public RegisterPanel(CommunicationWrapper cw,String branchCode) {
        this.cw=cw;
        this.branchCode = branchCode;
    
        initComponents();
    }
    
    private void reset(){
        txtFname.setText("");
        txtLname.setText("");
        jrbMale.setSelected(true);
        txtAddress.setText("");
        jcbState.setSelectedItem("Perak");
        txtCity.setText("");
        txtPostCode.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        jcbAccType.setSelectedItem("Saving");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btngrp = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        txtFname = new javax.swing.JTextField();
        txtLname = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jrbMale = new javax.swing.JRadioButton();
        jrbFemale = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jcbState = new javax.swing.JComboBox();
        txtCity = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPostCode = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtContact = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        btnSubmit = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jcbAccType = new javax.swing.JComboBox();

        setLayout(null);

        jLabel1.setText("Firstname");
        add(jLabel1);
        jLabel1.setBounds(20, 10, 150, 30);

        AbstractDocument aDocname = (AbstractDocument)txtPostCode.getDocument();
        aDocname.setDocumentFilter(new TextFieldLimiter("^[a-zA-Z]+$"));
        add(txtFname);
        txtFname.setBounds(170, 10, 250, 30);
        add(txtLname);
        txtLname.setBounds(170, 40, 250, 30);

        jLabel2.setText("Lastname");
        add(jLabel2);
        jLabel2.setBounds(20, 40, 150, 30);

        jLabel3.setText("Gender");
        add(jLabel3);
        jLabel3.setBounds(20, 70, 150, 30);
        add(txtAddress);
        txtAddress.setBounds(170, 100, 250, 30);

        jLabel4.setText("Address");
        add(jLabel4);
        jLabel4.setBounds(20, 100, 150, 30);

        btngrp.add(jrbMale);
        jrbMale.setSelected(true);
        jrbMale.setText("Male");
        add(jrbMale);
        jrbMale.setBounds(213, 70, 70, 30);

        btngrp.add(jrbFemale);
        jrbFemale.setText("Female");
        add(jrbFemale);
        jrbFemale.setBounds(320, 70, 59, 30);

        jLabel5.setText("State");
        add(jLabel5);
        jLabel5.setBounds(20, 130, 150, 30);

        jcbState.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Perak", "Selangor", "Seremban", "Melaka", "Wilayah Persekutuan" }));
        add(jcbState);
        jcbState.setBounds(170, 130, 250, 30);
        add(txtCity);
        txtCity.setBounds(170, 160, 250, 30);

        jLabel6.setText("City");
        add(jLabel6);
        jLabel6.setBounds(20, 160, 150, 30);

        AbstractDocument aDocPostCode = (AbstractDocument)txtPostCode.getDocument();
        aDocPostCode.setDocumentFilter(new TextFieldLimiter("\\d{0,5}"));
        add(txtPostCode);
        txtPostCode.setBounds(170, 190, 250, 30);

        jLabel7.setText("Postal Code");
        add(jLabel7);
        jLabel7.setBounds(20, 190, 150, 30);

        AbstractDocument aDocContact = (AbstractDocument)txtPostCode.getDocument();
        aDocContact.setDocumentFilter(new TextFieldLimiter("\\d{0,10}"));
        add(txtContact);
        txtContact.setBounds(170, 220, 250, 30);

        jLabel8.setText("Contact Number");
        add(jLabel8);
        jLabel8.setBounds(20, 220, 150, 30);

        jLabel9.setText("Email");
        add(jLabel9);
        jLabel9.setBounds(20, 250, 150, 30);
        add(txtEmail);
        txtEmail.setBounds(170, 250, 250, 30);

        btnSubmit.setText("Submit");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });
        add(btnSubmit);
        btnSubmit.setBounds(260, 320, 170, 50);

        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        add(btnReset);
        btnReset.setBounds(10, 320, 170, 50);

        jLabel10.setText("Account Type");
        add(jLabel10);
        jLabel10.setBounds(20, 280, 150, 30);

        jcbAccType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Saving", "Current" }));
        add(jcbAccType);
        jcbAccType.setBounds(170, 280, 250, 30);
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        clear();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        StringBuilder check = new StringBuilder("<html>Errors");
        validate = true;
        
        if (txtFname.getText().length() == 0) {
            validate = false;
            check.append("<br />Please enter first name");
        } 

        if (txtLname.getText().length() == 0) {
            validate = false;
            check.append("<br />Please enter last name");
        }

        if (txtAddress.getText().length() == 0) {
            validate = false;
            check.append("<br />Please enter address");
        } 
        if (txtCity.getText().length() == 0) {
            validate = false;
            check.append("<br />Please enter city");
        } 
        if (txtPostCode.getText().length() == 0) {
            validate = false;
            check.append("<br />Please enter postal code");
        } 

        if (txtContact.getText().length() == 0) {
            validate = false;
            check.append("<br />Please enter contact number");
        } 
        if (txtEmail.getText().length() == 0) {
            validate = false;
            check.append("<br />Please enter email");
        } 
        
        if (!validate) {
            check.append("</html>");
            Toast.makeText(getParent(), 150, "" + check, Toast.LENGTH_LONG).display();
        } else {
            CommunicationWrapper cw = null;
            try {
                cw = new CommunicationWrapper(5500);
            } catch (SocketException ex) {
                Logger.getLogger(DepositPanel.class.getName()).log(Level.SEVERE, null, ex);
            }


//            if (cw.isBranchReachable("6652", "6649")) {
//            try {
//
//                    JSONObject j = new JSONObject();
//                    j.put("operation", Operation.DEPOSIT);
//                    String message;
//                    JSONObject content = new JSONObject();
//                    content.put("accNo", txtAccNumber.getText());
//                    content.put("icNo", txtICNumber.getText());
//                    content.put("amount", Double.parseDouble(txtAmountDeposit.getText()));
//                    content.put("port", 5500);
//                    content.put("address", InetAddress.getLocalHost().getHostAddress());
//                    System.out.println(InetAddress.getLocalHost().getHostAddress());
//                    j.put("content", content);
//                    Branch b = new Branch();
//                    b.setBranchCode("6649");
//                    String ip = b.obtainBranchIp(MySQLConnection.getConnection());
//                    cw.send(j, InetAddress.getByName(ip), 5000);
//                    JSONObject js = cw.receive();
//                    System.out.println(js.toString());
//                } catch (JSONException ex) {
//                    ex.printStackTrace();
//                System.out.println(ex);
//            } catch (UnknownHostException ex) {
//                ex.printStackTrace();
//                Logger.getLogger(DepositPanel.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            } else {
//                System.out.println("Branch unreacbable");
//    }
        
        
        
      
            String fname = txtFname.getText().trim();
            String lname = txtLname.getText().trim();
            String gender = "";
            if(jrbMale.isSelected())
                gender = "Male";
            else{
                gender = "Female";
            }
            String addr = txtAddress.getText().trim();
            String state = jcbState.getSelectedItem().toString();
            String city = txtCity.getText().trim();
            String postcode = txtPostCode.getText().trim();
            String contact = txtContact.getText().trim();
            String email =txtEmail.getText().trim();
            String acctype = jcbAccType.getSelectedItem().toString();
        }
    }//GEN-LAST:event_btnSubmitActionPerformed
    
    public void clear() {
        txtFname.setText("");
        txtLname.setText("");
        jrbMale.setSelected(true);        
        txtAddress.setText("");
        jcbState.setSelectedIndex(0);
        txtCity.setText("");        
        txtPostCode.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        jcbAccType.setSelectedIndex(0);
        
    }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSubmit;
    private javax.swing.ButtonGroup btngrp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JComboBox jcbAccType;
    private javax.swing.JComboBox jcbState;
    private javax.swing.JRadioButton jrbFemale;
    private javax.swing.JRadioButton jrbMale;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtContact;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFname;
    private javax.swing.JTextField txtLname;
    private javax.swing.JTextField txtPostCode;
    // End of variables declaration//GEN-END:variables
}
