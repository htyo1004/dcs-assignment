/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.gui;

import com.bank.utils.CommunicationWrapper;
import com.bank.utils.Operation;
import com.bank.utils.TextFieldLimiter;
import com.bank.utils.Toast;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.text.AbstractDocument;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kenny
 */
public class QueryPanel extends javax.swing.JPanel {

    private CommunicationWrapper cw;
    private String branchCode;
    private int portNo;

    /**
     * Creates new form QueryPanel
     */
    public QueryPanel(CommunicationWrapper cw, String branchCode, int portNo) {
        this.cw = cw;
        this.branchCode = branchCode;
        this.portNo = portNo;
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
        jtfAccountNumber = new javax.swing.JTextField();
        jlblAccBalance = new javax.swing.JLabel();
        jlblBalance = new javax.swing.JLabel();
        jbtReset = new javax.swing.JButton();
        jbtSubmit = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setLayout(null);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Account Number ");
        add(jLabel1);
        jLabel1.setBounds(20, 40, 390, 30);

        AbstractDocument aDoc = (AbstractDocument)jtfAccountNumber.getDocument();
        aDoc.setDocumentFilter(new TextFieldLimiter("\\d{0,14}"));
        jtfAccountNumber.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jtfAccountNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                onReleased(evt);
            }
        });
        add(jtfAccountNumber);
        jtfAccountNumber.setBounds(20, 70, 400, 30);

        jlblAccBalance.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jlblAccBalance.setText("Account Balance :");
        add(jlblAccBalance);
        jlblAccBalance.setBounds(160, 110, 120, 30);

        jlblBalance.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        jlblBalance.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        add(jlblBalance);
        jlblBalance.setBounds(20, 140, 400, 100);

        jbtReset.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jbtReset.setText("Reset");
        jbtReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtResetActionPerformed(evt);
            }
        });
        add(jbtReset);
        jbtReset.setBounds(20, 320, 160, 50);

        jbtSubmit.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jbtSubmit.setText("Submit");
        jbtSubmit.setEnabled(false);
        jbtSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSubmitActionPerformed(evt);
            }
        });
        add(jbtSubmit);
        jbtSubmit.setBounds(260, 320, 160, 50);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("CHECK BALANCE");
        add(jLabel5);
        jLabel5.setBounds(20, 5, 400, 20);
        add(jSeparator1);
        jSeparator1.setBounds(10, 25, 420, 5);
    }// </editor-fold>//GEN-END:initComponents

    private void jbtResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtResetActionPerformed
        jtfAccountNumber.setText(null);
        jlblBalance.setText(null);
        jbtSubmit.setEnabled(false);
    }//GEN-LAST:event_jbtResetActionPerformed

    private void jbtSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSubmitActionPerformed
        try {
            JSONObject json = new JSONObject();
            json.put("operation", Operation.QUERY);
            JSONObject content = new JSONObject();
            content.put("accNo", jtfAccountNumber.getText());
            content.put("port", this.portNo);
            content.put("bCode", this.branchCode);
            content.put("address", InetAddress.getLocalHost().getHostAddress());
            json.put("content", content);
            System.out.println(json.toString());
            cw.send(json, InetAddress.getLocalHost(), 5000);
            JSONObject js = cw.receive();
            System.out.println(js.toString());
            JSONObject result = js.getJSONObject("content");
            if (result.get("result").toString().equalsIgnoreCase("Success")) {
                Toast.makeText(getParent(), "Success", Toast.LENGTH_SHORT).display();
                jlblBalance.setText("RM " + String.format("%.2f", result.getDouble("balance")));
            } else {
                Toast.makeText(getParent(), result.getString("result"), Toast.LENGTH_SHORT).display();
            }
        } catch (JSONException | UnknownHostException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jbtSubmitActionPerformed

    private void onReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_onReleased
        if (jtfAccountNumber.getText().length() == 14) {
            jbtSubmit.setEnabled(true);
        } else {
            jbtSubmit.setEnabled(false);
        }
    }//GEN-LAST:event_onReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbtReset;
    private javax.swing.JButton jbtSubmit;
    private javax.swing.JLabel jlblAccBalance;
    private javax.swing.JLabel jlblBalance;
    private javax.swing.JTextField jtfAccountNumber;
    // End of variables declaration//GEN-END:variables
}
