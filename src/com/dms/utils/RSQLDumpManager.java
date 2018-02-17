/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dms.utils;

import com.dms.workers.RSQLDumpWorker;
import com.ibatis.common.jdbc.ScriptRunner;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.Properties;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author My Work
 */
public class RSQLDumpManager {

    //String RemoteSSH_IP = CommonDA.getProperties().getProperty("RemoteSSH_IP").trim();
    //String RemoteSSH_PORT = CommonDA.getProperties().getProperty("RemoteSSH_PORT").trim();
    //String RemoteSSH_USERNAME = CommonDA.getProperties().getProperty("RemoteSSH_USERNAME").trim();
    //String RemoteSSH_PASSWORD = CommonDA.getProperties().getProperty("RemoteSSH_PASSWORD").trim();
    //String MYSQl_DumpFileName = CommonDA.getProperties().getProperty("MYSQl_DumpFileName").trim();
    String Local_SQLUsername = CommonDA.getProperties().getProperty("Local_SQLUsername").trim();
    String Local_SQLPassword = CommonDA.getProperties().getProperty("Local_SQLPassword").trim();
    String db_name = CommonDA.getProperties().getProperty("db_name").trim();
    String Restore_filename = CommonDA.getProperties().getProperty("Restore_filename").trim();
    String MYSQLDump_Filepath = CommonDA.getProperties().getProperty("MYSQLDump_Filepath").trim();
    RSQLDumpWorker wrkr;
            
    public boolean BackupDbToLocal(RSQLDumpWorker worker) {
        boolean flag = false;
        try {
            wrkr = worker;
            String cmd = "\""+MYSQLDump_Filepath+"\" -u "+Local_SQLUsername+" -p"+Local_SQLPassword+" "+db_name+" > "+Restore_filename;
            //System.out.println("com.dms.utils.RSQLDumpManager.BackupDbToLocal() :: "+ cmd);
            Process exec = Runtime.getRuntime().exec(new String[]{"cmd.exe","/c",cmd});
            
            if(exec.waitFor()==0)
            {
                wrkr.setProgressHelper(1);
                Thread.sleep(100);
                InputStream fileStream = copyFileToLocal(Restore_filename);
                if (fileStream != null) {
                    wrkr.setProgressHelper(2);
                    importFileToLocalDB(fileStream);
                }
            } else{
                InputStream errorStream = exec.getErrorStream();
                byte[] buffer = new byte[errorStream.available()];
                errorStream.read(buffer);

                String str = new String(buffer);
                System.out.println(str);
            }
            
            /*String remoteCommand = "sudo mysqldump -u odsdbuser dms | gzip > " + MYSQl_DumpFileName;
            boolean dumpCreated = executeCommand(remoteCommand);
            if (dumpCreated) {
                
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static void main(String args[]) {
        RSQLDumpManager dm = new RSQLDumpManager();
        dm.BackupDbToLocal(null);
    }

    public boolean importFileToLocalDB(InputStream fileStream) {
        boolean flag = false;
                
        try {
            Connection con = DBConnect.getConnection();
            
            /*GZIPInputStream gzis = new GZIPInputStream(fileStream);
            InputStreamReader reader = new InputStreamReader(gzis);*/
            
            InputStreamReader reader = new InputStreamReader(fileStream);
            ScriptRunner runner = new ScriptRunner(con, false, false);
            runner.runScript(new BufferedReader(reader));
            wrkr.setProgressHelper(3);
            System.out.println("com.dms.utils.SQLDumpManager.importFileToLocalDB() 3");
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public InputStream copyFileToLocal(String filename) {
        try {
             File initialFile = new File(filename);
             InputStream stream = new FileInputStream(initialFile);
             return stream; 
        } catch (Exception e) {
            e.printStackTrace();
        }   
        return null;
    }

   /*public boolean executeCommand(String command) {
        boolean flag = false;
        try {

            JSch jsch = new JSch();
            Session session = jsch.getSession(RemoteSSH_USERNAME, RemoteSSH_IP, Integer.valueOf(RemoteSSH_PORT));
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(RemoteSSH_PASSWORD);
            session.connect();
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            InputStream in = channelExec.getInputStream();
            channelExec.setCommand(command);
            channelExec.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            int index = 0;

            while ((line = reader.readLine()) != null) {
                System.out.println(++index + " : " + line);
            }
            channelExec.disconnect();
            session.disconnect();
            flag = true;
            System.out.println("Done!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }*/
}