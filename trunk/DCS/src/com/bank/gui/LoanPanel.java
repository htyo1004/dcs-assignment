package com.bank.gui;

import com.bank.utils.CommunicationWrapper;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Moofie
 */
public class LoanPanel extends javax.swing.JPanel {
    private CommunicationWrapper cw;
    private String branchCode;
    /**
     * Creates new form LoanPanel
     */
    public LoanPanel(CommunicationWrapper cw, String branchCode) {
        this.cw = cw;
        this.branchCode = branchCode;
        initComponents();
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

        setLayout(null);

        jLabel1.setText("Loan Type");
        add(jLabel1);
        jLabel1.setBounds(20, 20, 140, 30);

        jcbLoanType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Housing Loan", "Personal Loan", "Car Loan" }));
        jcbLoanType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbLoanTypeActionPerformed(evt);
            }
        });
        add(jcbLoanType);
        jcbLoanType.setBounds(160, 20, 260, 30);

        jLabel2.setText("Loan Amount");
        add(jLabel2);
        jLabel2.setBounds(20, 60, 140, 30);
        add(txtLoanAmount);
        txtLoanAmount.setBounds(160, 60, 260, 30);

        jLabel3.setText("Loan Interest Rate");
        add(jLabel3);
        jLabel3.setBounds(20, 100, 140, 30);
        add(txtLoanInterest);
        txtLoanInterest.setBounds(160, 100, 260, 30);

        jLabel4.setText("Duration");
        add(jLabel4);
        jLabel4.setBounds(20, 140, 140, 30);

        add(jcbDuration);
        jcbDuration.setBounds(160, 140, 260, 30);

        jLabel5.setText("Loaner Name");
        add(jLabel5);
        jLabel5.setBounds(20, 180, 140, 30);
        add(txtloanerName);
        txtloanerName.setBounds(160, 180, 260, 30);

        jLabel6.setText("Loaner IC Number");
        add(jLabel6);
        jLabel6.setBounds(20, 220, 140, 30);
        add(txtLoanerIC);
        txtLoanerIC.setBounds(160, 220, 260, 30);

        jLabel7.setText("Loaner Contact No");
        add(jLabel7);
        jLabel7.setBounds(20, 260, 140, 30);
        add(txtloanerContact);
        txtloanerContact.setBounds(160, 260, 260, 30);

        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        add(btnReset);
        btnReset.setBounds(10, 320, 170, 50);

        btnSubmit.setText("Submit");
        add(btnSubmit);
        btnSubmit.setBounds(260, 320, 170, 50);
    }// </editor-fold>//GEN-END:initComponents

    private void jcbLoanTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbLoanTypeActionPerformed
        // TODO add your handling code here:
         Integer month=12;
        Integer item = 0;
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
    }//GEN-LAST:event_jcbLoanTypeActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        clear();
    }//GEN-LAST:event_btnResetActionPerformed
    
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
    private javax.swing.JComboBox jcbDuration;
    private javax.swing.JComboBox jcbLoanType;
    private javax.swing.JTextField txtLoanAmount;
    private javax.swing.JTextField txtLoanInterest;
    private javax.swing.JTextField txtLoanerIC;
    private javax.swing.JTextField txtloanerContact;
    private javax.swing.JTextField txtloanerName;
    // End of variables declaration//GEN-END:variables
}
