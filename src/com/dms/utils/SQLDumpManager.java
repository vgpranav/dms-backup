/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dms.utils;

import com.dms.workers.SQLDumpWorker;
import com.ibatis.common.jdbc.ScriptRunner;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author My Work
 */
public class SQLDumpManager {

    String RemoteSSH_IP = CommonDA.getProperties().getProperty("RemoteSSH_IP").trim();
    String RemoteSSH_PORT = CommonDA.getProperties().getProperty("RemoteSSH_PORT").trim();
    String RemoteSSH_USERNAME = CommonDA.getProperties().getProperty("RemoteSSH_USERNAME").trim();
    String RemoteSSH_PASSWORD = CommonDA.getProperties().getProperty("RemoteSSH_PASSWORD").trim();
    String MYSQl_DumpFileName = CommonDA.getProperties().getProperty("MYSQl_DumpFileName").trim();
    SQLDumpWorker wrkr;
            
    public boolean BackupDbToLocal(SQLDumpWorker worker) {
        boolean flag = false;
        try {
            wrkr = worker;
            String remoteCommand = "sudo mysqldump -u odsdbuser dms | gzip > " + MYSQl_DumpFileName;
            boolean dumpCreated = executeCommand(remoteCommand);
            if (dumpCreated) {
                wrkr.setProgressHelper(1);
                InputStream fileStream = copyFileToLocal(MYSQl_DumpFileName);
                if (fileStream != null) {
                    wrkr.setProgressHelper(2);
                    importFileToLocalDB(fileStream);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static void main(String args[]) {
        SQLDumpManager dm = new SQLDumpManager();
        dm.BackupDbToLocal(null);
    }

    public boolean importFileToLocalDB(InputStream fileStream) {
        boolean flag = false;
                
        try {
            Connection con = LocalDBConnect.getConnection();
            
            GZIPInputStream gzis = new GZIPInputStream(fileStream);
            InputStreamReader reader = new InputStreamReader(gzis);
            
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
        FtpWrapper ftp = null;
        try {
            ftp = new FtpWrapper();
            String hostDomain = ftp.getServerName();
            String Id = ftp.getUsername();
            String Password = ftp.getPassword();

            if (ftp.connectAndLogin(hostDomain, Id, Password)) {
                ftp.setPassiveMode(true);
                ftp.binary();
                ftp.setBufferSize(1024000);

                InputStream stream = ftp.retrieveFileStream(filename);
                if (stream != null) {
                    return stream;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftp!=null && ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ex) {
                    Logger.getLogger(SQLDumpManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    public boolean executeCommand(String command) {
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
    }
}