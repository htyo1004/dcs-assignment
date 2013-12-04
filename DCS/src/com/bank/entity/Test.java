/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Kenny
 */
public class Test {

    public static void main(String[] args) {
        TransactionLog tl = new TransactionLog();
        tl.setAccNo("66490000000001");
//        tl.setAmount(253.23);
//        tl.setTransactionDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
//        tl.setTransactionType("DBT");
//        boolean result = tl.createTransactionLog(MySQLConnection.getConnection());
//        System.out.println(result);
        ArrayList<TransactionLog> da = tl.getTransactionLog(MySQLConnection.getConnection(), "66490000000001");
        for (int i = 0; i < da.size(); i++) {
            TransactionLog transactionLog = da.get(i);
            System.out.println(transactionLog.toString() + "\n");
        }
    }
}
