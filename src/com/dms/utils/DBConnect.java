/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dms.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author My Work
 */
public class DBConnect {
    
        private static String SQLDriver = CommonDA.getProperties().getProperty("SQLDriver").trim();
	private static String SQLURL = CommonDA.getProperties().getProperty("SQLURL").trim();
	private static String SQLUsername = CommonDA.getProperties().getProperty("SQLUsername").trim();
	private static String SQLPassword = CommonDA.getProperties().getProperty("SQLPassword").trim();
        private static Connection con;

        public static Connection getConnection() {
         try {
             Class.forName(SQLDriver);
             try {
                 con = DriverManager.getConnection(SQLURL, SQLUsername, SQLPassword);
             } catch (SQLException ex) { 
                 System.out.println("Failed to create the database connection."); 
             }
         } catch (ClassNotFoundException ex) {
             // log an exception. for example:
             System.out.println("Driver not found."); 
         }
         return con;
     }
        
    
}
