/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dms.utils;

import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author My Work
 */
public class Test {
    
    public static void main(String args[]){
    
           try{
               
                LocalFtpWrapper lftp = new LocalFtpWrapper();
            String lhostDomain = lftp.getServerName();
            String lId = lftp.getUsername();
            String lPassword = lftp.getPassword();
            
            if (lftp.connectAndLogin(lhostDomain, lId, lPassword)) {
                    lftp.setPassiveMode(true);
                    lftp.binary();
                    lftp.setBufferSize(1024000);
                    
                    //boolean flag = lftp.doCommand("mkdir test1", "");
                    
                    System.out.println(lftp.sendSiteCommand("dir"));
                    
                    FTPFile[] files = lftp.listFiles();
                    for(FTPFile f : files){
                        //System.out.println(f.);
                    }
                    
                    
            }
           
           }catch(Exception e ){
               e.printStackTrace();
           }
    }
}
