/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dms.utils;

import com.dms.workers.RBackupWorker;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author My Work
 */
public class RBackupManager {

    String workingDirectory = CommonDA.getProperties().getProperty("workingDirectory").trim();
    String Local_workingDirectory = CommonDA.getProperties().getProperty("Local_workingDirectory").trim();
    private static long totalSize = 0;
    private int totalCount=0;
    RBackupWorker worker;

    public boolean getFullBackup(RBackupWorker wrk) {
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
                        pullAllFiles(workingDirectory, lftp, null, ftp);
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

    public void pullAllFiles(String path, LocalFtpWrapper lftp, Connection con,FtpWrapper ftp ) throws SQLException {

        try {
            
            String currentFolder = path;

            //System.out.println(lftp.printWorkingDirectory());

            lftp.changeWorkingDirectory(currentFolder);

            String newFolderpath = currentFolder; //.substring(1);

            if (!ftp.changeWorkingDirectory(newFolderpath)) {
                ftp.makeDirectory(newFolderpath);
                ftp.changeWorkingDirectory(newFolderpath);
                System.out.println("Change DIR");
            }

            FTPFile[] files = lftp.listFiles();
            System.out.println(lftp.printWorkingDirectory());
            System.out.println(ftp.printWorkingDirectory());
            
            FTPFile[] lfiles = ftp.listFiles();

            ArrayList<String> tempList = arrayToList(lfiles);
            
            for (FTPFile aFile : files) {
            
                if (!aFile.isDirectory()) {
                     
                    if (!tempList.contains(aFile.getName())) {
                        InputStream stream = lftp.retrieveFileStream(aFile.getName());
                        if (stream != null) {
                            boolean flagg = ftp.storeFile(aFile.getName(), stream);
                            System.out.println(ftp.getReplyCode());
                            System.out.println("com.dms.utils.RBackupManager.pullAllFiles(): "+flagg);
                            worker.totalSize += aFile.getSize();
                            worker.totalCount++;
                            totalCount++;
                            worker.setProgressHelper(totalCount%10);
                            System.out.println(new Date()+" - [PUSH] "+lftp.printWorkingDirectory() + "/" + aFile.getName());
                        }
                        lftp.completePendingCommand();
                        //ftp.completePendingCommand();
                    } else {
                        //worker.totalSize += aFile.getSize();
                        //worker.totalCount++;
                        System.out.println(new Date()+" - [SKIP] "+lftp.printWorkingDirectory() + "/" + aFile.getName());
                    }
                }

                if (worker.isCancelled == true) {
                    //System.out.println("com.dms.utils.BackupManager.pullAllFiles()" + worker.isCancelled);
                    throw new Exception("AbortException");
                }
            }

            @SuppressWarnings("unchecked")
            Vector<String> subDirs = lftp.listSubdirNames();

            for (String aDir : subDirs) {
                String aDirPath = currentFolder + "/" + aDir;
                pullAllFiles(aDirPath, lftp, con, ftp);
                lftp.changeToParentDirectory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String args[]) {
        RBackupManager bmgr = new RBackupManager();
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
