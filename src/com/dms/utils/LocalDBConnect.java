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
public class LocalDBConnect {
    
        private static String Local_SQLDriver = CommonDA.getProperties().getProperty("Local_SQLDriver").trim();
	private static String Local_SQLURL = CommonDA.getProperties().getProperty("Local_SQLURL").trim();
	private static String Local_SQLUsername = CommonDA.getProperties().getProperty("Local_SQLUsername").trim();
	private static String Local_SQLPassword = CommonDA.getProperties().getProperty("Local_SQLPassword").trim();
        private static Connection con;

        public static Connection getConnection() {
         try {
             Class.forName(Local_SQLDriver);
             try {
                 con = DriverManager.getConnection(Local_SQLURL, Local_SQLUsername, Local_SQLPassword);
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
