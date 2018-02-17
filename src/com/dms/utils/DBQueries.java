/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dms.utils;

/**
 *
 * @author My Work
 */
public class DBQueries {
    
    public static String getConfigValue = "select configvalue from config where configkey=?";
    public static String updateBackupdatetoDB = "update config set configvalue=? where configkey=?";
}
