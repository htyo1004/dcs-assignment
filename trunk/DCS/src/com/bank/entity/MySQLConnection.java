/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Moofie
 */
public class MySQLConnection {

    private static Connection con;

    public MySQLConnection() {
    }
    
    /**
     * Create a connection to database to allow system to interact with database
     * 
     * @return connection to database
     */

    public static Connection getConnection() {

        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "bank";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "";
        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url + dbName, userName, password);
            return con;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            System.out.println(ex);
        }
        return con;
    }
}
