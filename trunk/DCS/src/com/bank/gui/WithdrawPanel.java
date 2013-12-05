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
public class WithdrawPanel extends javax.swing.JPanel {

    private boolean validate;
    private CommunicationWrapper cw;
    private String branchCode;
    private int portNo;

    /**
     * Creates new form WithdrawPanel
     */
    public WithdrawPanel(CommunicationWrapper cw, String branchCode, int portNo) {
        initComponents();
        this.cw = cw;
        this.branchCode = branchCode;
        this.portNo = portNo;
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void reset() {
        jtfAccountNumber.setText("");
        jtfICNumber.setText("");
        jtfAmountWithdraw.setText("");
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
        jLabel2 = new javax.swing.JLabel();
        jtfICNumber = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jtfAmountWithdraw = new javax.swing.JTextField();
        jbtSubmit = new javax.swing.JButton();
        jbtReset = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(440, 380));
        setLayout(null);

        jLabel1.setText("Account Number ");
        add(jLabel1);
        jLabel1.setBounds(20, 60, 390, 30);

        AbstractDocument aDoc = (AbstractDocument)jtfAccountNumber.getDocument();
        aDoc.setDocumentFilter(new TextFieldLimiter("\\d{0,14}"));
        add(jtfAccountNumber);
        jtfAccountNumber.setBounds(20, 90, 390, 30);

        jLabel2.setText("IC Number");
        add(jLabel2);
        jLabel2.setBounds(20, 130, 220, 30);

        AbstractDocument aDocIC = (AbstractDocument)jtfICNumber.getDocument();
        aDocIC.setDocumentFilter(new TextFieldLimiter("\\d{0,12}"));
        add(jtfICNumber);
        jtfICNumber.setBounds(20, 160, 390, 30);

        jLabel3.setText("Amount to Withdraw");
        add(jLabel3);
        jLabel3.setBounds(20, 200, 180, 30);

        AbstractDocument aDocAmount = (AbstractDocument)jtfAmountWithdraw.getDocument();
        aDocAmount.setDocumentFilter(new TextFieldLimiter("\\d{0,5}"));
        add(jtfAmountWithdraw);
        jtfAmountWithdraw.setBounds(20, 230, 390, 30);

        jbtSubmit.setText("Submit");
        jbtSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSubmitActionPerformed(evt);
            }
        });
        add(jbtSubmit);
        jbtSubmit.setBounds(260, 320, 170, 50);

        jbtReset.setText("Reset");
        jbtReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtResetActionPerformed(evt);
            }
        });
        add(jbtReset);
        jbtReset.setBounds(10, 320, 170, 50);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Withdraw");
        jLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204), 3));
        add(jLabel4);
        jLabel4.setBounds(100, 20, 250, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void jbtSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSubmitActionPerformed
        StringBuilder sb = new StringBuilder("<html>Errors");
        int height = 50;
        validate = true;

        if (jtfAccountNumber.getText().length() == 0) {
            validate = false;
            height += 10;
            sb.append("<br />Please enter account number");
        } else if (jtfAccountNumber.getText().length() > 0 && jtfAccountNumber.getText().length() != 14) {
            validate = false;
            height += 10;
            sb.append("<br />Please enter 12 digit account number");
        }

        if (jtfICNumber.getText().length() == 0) {
            height += 10;
            validate = false;
            sb.append("<br />Please enter ic number");
        }
        if (jtfAmountWithdraw.getText().length() == 0) {
            height += 10;
            validate = false;
            sb.append("<br />Please enter amount to withdraw");
        }
        if (!validate) {
            sb.append("</html>");
            Toast.makeText(getParent(), height, "" + sb, Toast.LENGTH_LONG).display();
        } else {
            try {

                JSONObject j = new JSONObject();
                j.put("operation", Operation.WITHDRAW);
                JSONObject content = new JSONObject();
                content.put("accNo", jtfAccountNumber.getText());
                content.put("icNo", jtfICNumber.getText());
                content.put("amount", Double.parseDouble(jtfAmountWithdraw.getText()));
                content.put("port", this.portNo);
                content.put("bCode", this.branchCode);
                content.put("address", InetAddress.getLocalHost().getHostAddress());
                j.put("content", content);
                cw.send(j, InetAddress.getLocalHost(), 5000);
                JSONObject js = cw.receive();
                JSONObject contents = js.getJSONObject("content");
                System.out.println(js.toString());
                if(contents.get("result").toString().equalsIgnoreCase("Success")){
                    Toast.makeText(getParent(), "Success", Toast.LENGTH_LONG).display();
                }else{
                    Toast.makeText(getParent(), "Please try again later", Toast.LENGTH_LONG).display();
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
                System.out.println(ex);
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
                Logger.getLogger(DepositPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }//GEN-LAST:event_jbtSubmitActionPerformed

    private void jbtResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtResetActionPerformed
        reset();
    }//GEN-LAST:event_jbtResetActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton jbtReset;
    private javax.swing.JButton jbtSubmit;
    private javax.swing.JTextField jtfAccountNumber;
    private javax.swing.JTextField jtfAmountWithdraw;
    private javax.swing.JTextField jtfICNumber;
    // End of variables declaration//GEN-END:variables
}
