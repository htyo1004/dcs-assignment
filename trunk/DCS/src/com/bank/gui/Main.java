package com.bank.gui;

import com.bank.entity.Branch;
import com.bank.entity.MySQLConnection;
import com.bank.server.BankServerFrame;
import com.bank.utils.CommunicationWrapper;
import com.bank.utils.TextFieldLimiter;
import com.bank.utils.Toast;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Moofie
 */
public class Main extends javax.swing.JFrame {

    private CardLayout cl;
    private CommunicationWrapper cw;
    private DefaultComboBoxModel dcbmBranch;
    private QueryPanel qPanel;
    private WithdrawPanel wPanel;
    private DepositPanel dPanel;
    private LoanPanel lPanel;
    private PassbookPanel pPanel;
    private TransferPanel tPanel;
    private RegisterPanel rPanel;
    private JComboBox jcb;
    private JPanel jpanel;
    private JLabel jlabel;
    private String branchCode;
    private int portNo;

    /**
     * Creates new form Main
     */
    public Main() {
        jcb = new JComboBox();
        jpanel = new JPanel();
        jlabel = new JLabel("Select the branch this pc belongs to");
        populateBranchCode();
        jpanel.setLayout(new GridLayout(2, 1));
        jpanel.add(jlabel);
        jpanel.add(jcb);
        JOptionPane.showMessageDialog(Main.this, jpanel, "Options", JOptionPane.QUESTION_MESSAGE);
        branchCode = jcb.getSelectedItem().toString();
        System.out.println(branchCode);
        portNo = generatePortNumber();
        try {
            cw = new CommunicationWrapper(portNo);
        } catch (SocketException ex) {
            Toast.makeText(Main.this, "Unable to open socket : " + ex.getMessage(), Toast.LENGTH_SHORT);
        }

        initComponents();
        addCard();
        cl = (CardLayout) jpMainCard.getLayout();
    }

    private void addCard() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                qPanel = new QueryPanel(cw, branchCode, portNo);
                wPanel = new WithdrawPanel(cw, branchCode, portNo);
                dPanel = new DepositPanel(cw, branchCode, portNo);
                tPanel = new TransferPanel(cw, branchCode, portNo);
                rPanel = new RegisterPanel(cw, branchCode, portNo);
                lPanel = new LoanPanel(cw, branchCode, portNo);
                pPanel = new PassbookPanel(cw, branchCode, portNo);
                jpMainCard.add(qPanel, "query");
                jpMainCard.add(wPanel, "withdraw");
                jpMainCard.add(dPanel, "deposit");
                jpMainCard.add(tPanel, "transfer");
                jpMainCard.add(rPanel, "register");
                jpMainCard.add(lPanel, "loan");
                jpMainCard.add(pPanel, "passbook");
            }
        }).start();

    }

    private void populateBranchCode() {
        dcbmBranch = new DefaultComboBoxModel();
        Branch branch = new Branch();
        ArrayList<String> d = branch.obtainAllBranchCode(MySQLConnection.getConnection());
        if (!d.isEmpty()) {
            for (int i = 0; i < d.size(); i++) {
                dcbmBranch.addElement(d.get(i));
            }
            jcb.setModel(dcbmBranch);
        } else {
            Toast.makeText(Main.this, "No branch found.", Toast.LENGTH_SHORT).display();
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

        jPanel1 = new javax.swing.JPanel();
        jbtWithdraw = new javax.swing.JButton();
        jbtDeposit = new javax.swing.JButton();
        jbtTransfer = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jbtRegister = new javax.swing.JButton();
        jbtLoan = new javax.swing.JButton();
        jbtUpdate = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jpMainCard = new javax.swing.JPanel();
        jpNullCard = new javax.swing.JPanel();
        jbtQuery = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(null);

        jbtWithdraw.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/b.png"))); // NOI18N
        jbtWithdraw.setText("Withdraw");
        jbtWithdraw.setFocusPainted(false);
        jbtWithdraw.setIconTextGap(0);
        jbtWithdraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtWithdrawActionPerformed(evt);
            }
        });
        jPanel1.add(jbtWithdraw);
        jbtWithdraw.setBounds(10, 115, 120, 50);

        jbtDeposit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/a.jpg"))); // NOI18N
        jbtDeposit.setText("Deposit");
        jbtDeposit.setFocusPainted(false);
        jbtDeposit.setIconTextGap(6);
        jbtDeposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDepositActionPerformed(evt);
            }
        });
        jPanel1.add(jbtDeposit);
        jbtDeposit.setBounds(10, 170, 120, 50);

        jbtTransfer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/c.png"))); // NOI18N
        jbtTransfer.setText("Transfer");
        jbtTransfer.setFocusPainted(false);
        jbtTransfer.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jbtTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtTransferActionPerformed(evt);
            }
        });
        jPanel1.add(jbtTransfer);
        jbtTransfer.setBounds(10, 225, 120, 50);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Ong Ong Lai Banking System");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(0, 0, 570, 60);

        jbtRegister.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/f.jpg"))); // NOI18N
        jbtRegister.setText("Register");
        jbtRegister.setFocusPainted(false);
        jbtRegister.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jbtRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtRegisterActionPerformed(evt);
            }
        });
        jPanel1.add(jbtRegister);
        jbtRegister.setBounds(10, 280, 120, 50);

        jbtLoan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/e.png"))); // NOI18N
        jbtLoan.setText("Loan");
        jbtLoan.setFocusPainted(false);
        jbtLoan.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jbtLoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtLoanActionPerformed(evt);
            }
        });
        jPanel1.add(jbtLoan);
        jbtLoan.setBounds(10, 335, 120, 50);

        jbtUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/d.jpg"))); // NOI18N
        jbtUpdate.setText("<html>Update<br/> passbook");
        jbtUpdate.setFocusPainted(false);
        jbtUpdate.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jbtUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtUpdateActionPerformed(evt);
            }
        });
        jPanel1.add(jbtUpdate);
        jbtUpdate.setBounds(10, 390, 120, 50);

        jpMainCard.setPreferredSize(new java.awt.Dimension(440, 380));
        jpMainCard.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout jpNullCardLayout = new javax.swing.GroupLayout(jpNullCard);
        jpNullCard.setLayout(jpNullCardLayout);
        jpNullCardLayout.setHorizontalGroup(
            jpNullCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );
        jpNullCardLayout.setVerticalGroup(
            jpNullCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
        );

        jpMainCard.add(jpNullCard, "null");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jpMainCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jpMainCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(jPanel2);
        jPanel2.setBounds(130, 65, 440, 380);

        jbtQuery.setText("<html>Check<br/>Balance");
        jbtQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtQueryActionPerformed(evt);
            }
        });
        jPanel1.add(jbtQuery);
        jbtQuery.setBounds(10, 60, 120, 50);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(592, 498));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jbtWithdrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtWithdrawActionPerformed
        cl.show(jpMainCard, "withdraw");
    }//GEN-LAST:event_jbtWithdrawActionPerformed

    private void jbtDepositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDepositActionPerformed
        cl.show(jpMainCard, "deposit");
    }//GEN-LAST:event_jbtDepositActionPerformed

    private void jbtTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtTransferActionPerformed
        cl.show(jpMainCard, "transfer");
    }//GEN-LAST:event_jbtTransferActionPerformed

    private void jbtRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtRegisterActionPerformed
        cl.show(jpMainCard, "register");
    }//GEN-LAST:event_jbtRegisterActionPerformed

    private void jbtLoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtLoanActionPerformed
        cl.show(jpMainCard, "loan");
    }//GEN-LAST:event_jbtLoanActionPerformed

    private void jbtUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtUpdateActionPerformed
        cl.show(jpMainCard, "passbook");
    }//GEN-LAST:event_jbtUpdateActionPerformed

    private void jbtQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtQueryActionPerformed
        cl.show(jpMainCard, "query");
    }//GEN-LAST:event_jbtQueryActionPerformed

    
    private int generatePortNumber() {
        Random rand = new Random();
        int port = rand.nextInt((60000 - 5111) + 1) + 5111;
        return port;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbtDeposit;
    private javax.swing.JButton jbtLoan;
    private javax.swing.JButton jbtQuery;
    private javax.swing.JButton jbtRegister;
    private javax.swing.JButton jbtTransfer;
    private javax.swing.JButton jbtUpdate;
    private javax.swing.JButton jbtWithdraw;
    private javax.swing.JPanel jpMainCard;
    private javax.swing.JPanel jpNullCard;
    // End of variables declaration//GEN-END:variables
}
