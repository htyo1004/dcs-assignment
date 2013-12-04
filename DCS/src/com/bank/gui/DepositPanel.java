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
public class DepositPanel extends javax.swing.JPanel {

    private CommunicationWrapper cw;
    /**
     * Creates new form DepositPanel
     */
    private boolean validate;

    public DepositPanel(CommunicationWrapper cw) {
        this.cw = cw;
        initComponents();
    }

    private void reset() {
        txtAccNumber.setText("");
        txtICNumber.setText("");
        txtAmountDeposit.setText("");
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
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
        txtAccNumber = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtICNumber = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtAmountDeposit = new javax.swing.JTextField();
        btnReset = new javax.swing.JButton();
        btnSubmit = new javax.swing.JButton();

        setLayout(null);

        jLabel1.setText("Account Number ");
        add(jLabel1);
        jLabel1.setBounds(20, 60, 220, 30);

        AbstractDocument aDoc = (AbstractDocument)txtAccNumber.getDocument();
        aDoc.setDocumentFilter(new TextFieldLimiter("\\d{0,14}"));
        add(txtAccNumber);
        txtAccNumber.setBounds(20, 90, 390, 30);

        jLabel2.setText("IC Number");
        add(jLabel2);
        jLabel2.setBounds(20, 130, 220, 30);

        AbstractDocument aDocIC = (AbstractDocument)txtICNumber.getDocument();
        aDocIC.setDocumentFilter(new TextFieldLimiter("\\d{0,12}"));
        add(txtICNumber);
        txtICNumber.setBounds(20, 160, 390, 30);

        jLabel3.setText("Amount to Deposit");
        add(jLabel3);
        jLabel3.setBounds(20, 200, 180, 30);

        AbstractDocument aDocAmount = (AbstractDocument)txtAmountDeposit.getDocument();
        aDocAmount.setDocumentFilter(new TextFieldLimiter("\\d{0,5}"));
        txtAmountDeposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmountDepositActionPerformed(evt);
            }
        });
        add(txtAmountDeposit);
        txtAmountDeposit.setBounds(20, 230, 390, 30);

        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        add(btnReset);
        btnReset.setBounds(10, 320, 170, 50);

        btnSubmit.setText("Submit");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });
        add(btnSubmit);
        btnSubmit.setBounds(260, 320, 170, 50);
    }// </editor-fold>//GEN-END:initComponents

    private void txtAmountDepositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountDepositActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAmountDepositActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        reset();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        StringBuilder check = new StringBuilder("<html>Errors");

        validate = true;



        if (txtAccNumber.getText().length() == 0) {
            validate = false;
            check.append("<br />Please enter account number");
        }

        if (txtICNumber.getText().length() == 0) {
            validate = false;
            check.append("<br />Please enter ic number");
        }

        if (txtAmountDeposit.getText().length() == 0) {
            validate = false;
            check.append("<br />Please enter deposit amount");
        }

        if (!validate) {
            check.append("</html>");
            Toast.makeText(getParent(), 150, "" + check, Toast.LENGTH_LONG).display();
        } else {




            try {

                JSONObject j = new JSONObject();
                j.put("operation", Operation.DEPOSIT);
                JSONObject content = new JSONObject();
                content.put("accNo", txtAccNumber.getText());
                content.put("icNo", txtICNumber.getText());
                content.put("amount", Double.parseDouble(txtAmountDeposit.getText()));
                content.put("port", 5500);
                content.put("address", InetAddress.getLocalHost().getHostAddress());
                System.out.println(InetAddress.getLocalHost().getHostAddress());
                j.put("content", content);
                cw.send(j, InetAddress.getLocalHost(), 5000);
                JSONObject js = cw.receive();
                System.out.println(js.toString());
            } catch (JSONException ex) {
                ex.printStackTrace();
                System.out.println(ex);
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
                Logger.getLogger(DepositPanel.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_btnSubmitActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField txtAccNumber;
    private javax.swing.JTextField txtAmountDeposit;
    private javax.swing.JTextField txtICNumber;
    // End of variables declaration//GEN-END:variables
}
