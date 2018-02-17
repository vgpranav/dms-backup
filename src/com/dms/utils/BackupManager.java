/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dms.utils;

import com.dms.workers.BackupWorker;
import java.awt.List;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author My Work
 */
public class BackupManager {

    String workingDirectory = CommonDA.getProperties().getProperty("workingDirectory").trim();
    String Local_workingDirectory = CommonDA.getProperties().getProperty("Local_workingDirectory").trim();
    private static long totalSize = 0;
    private int totalCount=0;
    BackupWorker worker;

    public boolean getFullBackup(BackupWorker wrk) {
        worker = wrk;
        System.out.println("=== Backup Manager Logs ===");
        FtpWrapper ftp = new FtpWrapper();
        String hostDomain = ftp.getServerName();
        String Id = ftp.getUsername();
        String Password = ftp.getPassword();

        LocalFtpWrapper lftp = new LocalFtpWrapper();
        String lhostDomain = lftp.getServerName();
        String lId = lftp.getUsername();
        String lPassword = lftp.getPassword();

        boolean flag = false;
        try {
            if (ftp.connectAndLogin(hostDomain, Id, Password)) {
                ftp.setPassiveMode(true);
                ftp.binary();
                ftp.setBufferSize(1024000);
                String currentFolder = workingDirectory;

                if (lftp.connectAndLogin(lhostDomain, lId, lPassword)) {
                    lftp.setPassiveMode(true);
                    lftp.binary();
                    lftp.setBufferSize(1024000);
                    try{
                        pullAllFiles(workingDirectory, ftp, null, lftp);
                    }catch(Exception e){
                        e.getMessage();
                    }
                }
            }
            //Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public void pullAllFiles(String path, FtpWrapper ftp, Connection con, LocalFtpWrapper lftp) throws SQLException {

        try {
            String currentFolder = path;

            //System.out.println(lftp.printWorkingDirectory());

            ftp.changeWorkingDirectory(currentFolder);

            String newFolderpath = currentFolder; //.substring(1);

            if (!lftp.changeWorkingDirectory(newFolderpath)) {
                lftp.makeDirectory(newFolderpath);
                lftp.changeWorkingDirectory(newFolderpath);
            }

            FTPFile[] files = ftp.listFiles();
            FTPFile[] lfiles = lftp.listFiles();

            ArrayList<String> tempList = arrayToList(lfiles);

            for (FTPFile aFile : files) {

                if (!aFile.isDirectory()) {
                     
                    if (!tempList.contains(aFile.getName())) {
                        InputStream stream = ftp.retrieveFileStream(aFile.getName());
                        if (stream != null) {
                            lftp.storeFile(aFile.getName(), stream);
                            worker.totalSize += aFile.getSize();
                            worker.totalCount++;
                            totalCount++;
                            worker.setProgressHelper(totalCount%10);
                            System.out.println(new Date()+" - [FETCH] "+ftp.printWorkingDirectory() + "/" + aFile.getName());
                        }
                        ftp.completePendingCommand();
                    } else {
                        //worker.totalSize += aFile.getSize();
                        //worker.totalCount++;
                        System.out.println(new Date()+" - [SKIP] "+ftp.printWorkingDirectory() + "/" + aFile.getName());
                    }
                }

                if (worker.isCancelled == true) {
                    //System.out.println("com.dms.utils.BackupManager.pullAllFiles()" + worker.isCancelled);
                    throw new Exception("AbortException");
                }
            }

            @SuppressWarnings("unchecked")
            Vector<String> subDirs = ftp.listSubdirNames();

            for (String aDir : subDirs) {
                String aDirPath = currentFolder + "/" + aDir;
                pullAllFiles(aDirPath, ftp, con, lftp);
                ftp.changeToParentDirectory();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String args[]) {
        BackupManager bmgr = new BackupManager();
        bmgr.getFullBackup(null);
    }

    public ArrayList<String> arrayToList(FTPFile[] files) {
        ArrayList<String> tempList = new ArrayList<>();
        for (FTPFile tempFile : files) {
            tempList.add(tempFile.getName());
        }
        return tempList;
    }
    
    
    
}
