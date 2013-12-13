package com.bank.gui;

import com.bank.entity.Branch;
import com.bank.entity.MySQLConnection;
import com.bank.utils.CommunicationWrapper;
import com.bank.utils.Operation;
import com.bank.utils.TextFieldLimiter;
import com.bank.utils.Toast;
import java.net.InetAddress;
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
public class LoanPanel extends javax.swing.JPanel {
    Integer month =12;
    private boolean validate;
    private CommunicationWrapper cw;
    private String branchCode;
    private int portNo;
    /**
     * Creates new form LoanPanel
     */
    public LoanPanel(CommunicationWrapper cw, String branchCode, int portNo) {
        this.cw = cw;
        this.branchCode = branchCode;
        this.portNo = portNo;
        initComponents();
        Integer []year ={20,25,30,35};
            for (int i = 0 ; i<year.length;i++){
                jcbDuration.addItem(""+(year[i]*month)+" Months");
            }
    }

    private void getCombo(){

        jcbDuration.removeAllItems();
        if(jcbLoanType.getSelectedIndex()==0){
            Integer []year ={20,25,30,35};
            for (int i = 0 ; i<year.length;i++){
                jcbDuration.addItem(""+(year[i]*month)+" Months");
            }
        }else if(jcbLoanType.getSelectedIndex()==1){
            Integer []year ={10,20,30};
            for (int i = 0 ; i<year.length;i++){
                jcbDuration.addItem(""+(year[i]*month)+" Months");
            }
        }else if(jcbLoanType.getSelectedIndex()==2){
            Integer []year ={5,7,9};
            for (int i = 0 ; i<year.length;i++){
                jcbDuration.addItem(""+(year[i]*month)+" Months");
            }
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

        jLabel1 = new javax.swing.JLabel();
        jcbLoanType = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        txtLoanAmount = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtLoanInterest = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jcbDuration = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        txtloanerName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtLoanerIC = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtloanerContact = new javax.swing.JTextField();
        btnReset = new javax.swing.JButton();
        btnSubmit = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setLayout(null);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Loan Type");
        add(jLabel1);
        jLabel1.setBounds(20, 40, 140, 30);

        jcbLoanType.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jcbLoanType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Housing Loan", "Personal Loan", "Car Loan" }));
        jcbLoanType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbLoanTypeActionPerformed(evt);
            }
        });
        add(jcbLoanType);
        jcbLoanType.setBounds(160, 40, 260, 30);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Loan Amount");
        add(jLabel2);
        jLabel2.setBounds(20, 80, 140, 30);

        AbstractDocument aDocloan = (AbstractDocument)txtLoanAmount.getDocument();
        aDocloan.setDocumentFilter(new TextFieldLimiter("\\d{0,8}"));
        txtLoanAmount.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        add(txtLoanAmount);
        txtLoanAmount.setBounds(160, 80, 260, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Loan Interest Rate");
        add(jLabel3);
        jLabel3.setBounds(20, 120, 140, 30);

        AbstractDocument aDocInterest = (AbstractDocument)txtLoanInterest.getDocument();
        aDocInterest.setDocumentFilter(new TextFieldLimiter("\\d{0,3}"));
        txtLoanInterest.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        add(txtLoanInterest);
        txtLoanInterest.setBounds(160, 120, 260, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Duration");
        add(jLabel4);
        jLabel4.setBounds(20, 160, 140, 30);

        jcbDuration.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        add(jcbDuration);
        jcbDuration.setBounds(160, 160, 260, 30);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Loaner Name");
        add(jLabel5);
        jLabel5.setBounds(20, 200, 140, 30);

        AbstractDocument aDocname = (AbstractDocument)txtloanerName.getDocument();
        aDocname.setDocumentFilter(new TextFieldLimiter("^[a-z A-Z]+$"));
        txtloanerName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        add(txtloanerName);
        txtloanerName.setBounds(160, 200, 260, 30);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Loaner IC Number");
        add(jLabel6);
        jLabel6.setBounds(20, 240, 140, 30);

        AbstractDocument aDocPostCode = (AbstractDocument)txtLoanerIC.getDocument();
        aDocPostCode.setDocumentFilter(new TextFieldLimiter("\\d{0,12}"));
        txtLoanerIC.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        add(txtLoanerIC);
        txtLoanerIC.setBounds(160, 240, 260, 30);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Loaner Contact No");
        add(jLabel7);
        jLabel7.setBounds(20, 280, 140, 30);

        AbstractDocument aDoccontact = (AbstractDocument)txtloanerContact.getDocument();
        aDoccontact.setDocumentFilter(new TextFieldLimiter("\\d{0,10}"));
        txtloanerContact.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        add(txtloanerContact);
        txtloanerContact.setBounds(160, 280, 260, 30);

        btnReset.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        add(btnReset);
        btnReset.setBounds(20, 320, 160, 50);

        btnSubmit.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnSubmit.setText("Submit");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });
        add(btnSubmit);
        btnSubmit.setBounds(260, 320, 160, 50);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("LOAN APPLICATION");
        add(jLabel8);
        jLabel8.setBounds(20, 5, 400, 20);
        add(jSeparator1);
        jSeparator1.setBounds(10, 25, 420, 5);
    }// </editor-fold>//GEN-END:initComponents

    private void jcbLoanTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbLoanTypeActionPerformed

         getCombo();
    }//GEN-LAST:event_jcbLoanTypeActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        clear();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        StringBuilder check = new StringBuilder("<html>Errors");
        validate = true;

        if (txtLoanAmount.getText().length() == 0) {
            validate = false;
            check.append("<br />Please enter loan amount");
        }

        if (txtLoanInterest.getText().length() == 0) {
            validate = false;
            check.append("<br />Please enter interest");
        }

        if (txtloanerName.getText().length() == 0) {
            validate = false;
            check.append("<br />Please enter loaner name");
        }
        if (txtLoanerIC.getText().length() == 0) {
            validate = false;
            check.append("<br />Please enter loaner ic");
        }
        if (txtloanerContact.getText().length() == 0) {
            validate = false;
            check.append("<br />Please enter loaner contact number");
        }

        if (!validate) {
            check.append("</html>");
            Toast.makeText(getParent(), 150, "" + check, Toast.LENGTH_LONG).display();
        } else {

            try {
                JSONObject j = new JSONObject();
                j.put("operation", Operation.LOAN);
                JSONObject content = new JSONObject();
                content.put("type", jcbLoanType.getSelectedItem().toString());
                content.put("loanamount", txtLoanAmount.getText().trim());
                content.put("rate", txtLoanInterest.getText().trim());
                content.put("duration", jcbDuration.getSelectedItem().toString());
                content.put("name", txtloanerName.getText());
                content.put("icno", txtLoanerIC.getText().trim());
                content.put("contact", txtloanerContact.getText().trim());
                Branch b = new Branch();
                b.setBranchCode(this.branchCode);
                
                content.put("port", this.portNo);
                content.put("address", InetAddress.getLocalHost().getHostAddress());
                System.out.println(InetAddress.getLocalHost().getHostAddress());
                j.put("content", content);
                b.setBranchCode(this.branchCode);
                cw.send(j, InetAddress.getLocalHost(), 5000);
                JSONObject js = cw.receive();
                if (js.getString("result").equalsIgnoreCase("Success")) {
                    Toast.makeText(getParent(), "Loan application successful", Toast.LENGTH_SHORT).display();
                } else {
                    Toast.makeText(getParent(), "Unable to apply loan now, try again later", Toast.LENGTH_SHORT).display();
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
                System.out.println(ex);
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
                Logger.getLogger(DepositPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnSubmitActionPerformed
    
    public void clear(){
        jcbLoanType.setSelectedIndex(0);
        txtLoanAmount.setText("");
        txtLoanInterest.setText("");
        jcbDuration.removeAllItems();
        txtloanerName.setText("");
        txtLoanerIC.setText("");
        txtloanerContact.setText("");  
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JComboBox jcbDuration;
    private javax.swing.JComboBox jcbLoanType;
    private javax.swing.JTextField txtLoanAmount;
    private javax.swing.JTextField txtLoanInterest;
    private javax.swing.JTextField txtLoanerIC;
    private javax.swing.JTextField txtloanerContact;
    private javax.swing.JTextField txtloanerName;
    // End of variables declaration//GEN-END:variables
}