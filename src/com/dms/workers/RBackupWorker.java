/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dms.workers;

import com.dms.utils.RBackupManager;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

/**
 *
 * @author My Work
 */
public class RBackupWorker extends SwingWorker<Void, String>{
    
    private RBackupManager backupManager;    
    public boolean isCancelled=false;
    public long totalSize=0;
    public int totalCount=0;
    private JLabel label;

    
    @Override
    protected Void doInBackground() throws Exception {
        backupManager = new RBackupManager();
        //System.out.println("Wrkr");
        //System.out.println("com.dms.workers.BackupWorker.doInBackground()"+isCancelled());
       //here you make heavy task this is running in another thread not in EDT
        //setProgress(30);
        backupManager.getFullBackup(this);
        //setProgress(30); // this is if you want to use with a progressBar
        //businessDelegate.saveToSomeDataBase();
        
        return null;
    }
 
    public void setProgressHelper(int val){
        setProgress(val);
    }
    
    @Override
    protected void process(List<String> chunks){
       //this is executed in EDT you can update a label for example
        //label.setText(String.valueOf(totalSize));
        System.out.println(chunks.toString());
    }
    
    @Override
    protected void done()
    {
        try {
            //backupManager.updateBackupdatetoDB();
            //JOptionPane.showMessageDialog(null,"Backup Completed ! \n Files Transferred: "+totalCount+"\n Size: "+totalSize);
            isCancelled=true;
            setProgress(100);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
