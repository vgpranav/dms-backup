/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dms.gui;

import com.dms.utils.DBConnect;
import com.dms.utils.DBQueries;
import com.dms.utils.FtpWrapper;
import com.dms.utils.LocalDBConnect;
import com.dms.utils.LocalFtpWrapper;
import com.dms.workers.BackupWorker;
import com.dms.workers.RBackupWorker;
import com.dms.workers.RSQLDumpWorker;
import com.dms.workers.SQLDumpWorker;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author My Work
 */
public class RestoreApp extends javax.swing.JFrame {

    private static FtpWrapper globalFtp;
    private static String workingDirectory;
    protected JFrame frame = new JFrame();
    private static String lastbackupdate;
    RBackupWorker bw;
    RSQLDumpWorker dw;
    JLabel imageLabel;
    JLabel imageLabel1;
    int counter=0;
    /**
     * Creates new form NewApplication
     */
    public RestoreApp() {
        initComponents();
        initApplication();
        initWindowClose();

        ImageIcon image = new ImageIcon(ClassLoader.getSystemResource("progress.gif"));
        imageLabel = new JLabel(image);
        imageLabel.setBounds(-50, -30, 400, 400);
        imageLabel.setVisible(false);
        jPanel1.add(imageLabel);
        
        ImageIcon image1 = new ImageIcon(ClassLoader.getSystemResource("progress.gif"));
        imageLabel1 = new JLabel(image1);
        imageLabel1.setBounds(-45, -30, 400, 400);
        imageLabel1.setVisible(false);
        jPanel2.add(imageLabel1);
    }

    public final void initApplication() {

        /*jLabel2.setText("Connecting..");
        if (checkRemoteFTPServer()) {
            jLabel2.setText("Remote Server Available");
        }
        if (checkLocalFTPServer()) {
            jLabel4.setText("Local Server Available");
        }
         */
        displayLastUpdateDate();
        jButton2.setEnabled(false);
    }

    public final void initWindowClose() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                confirmAndExit();
            }
        });
    }

    public boolean testDBConnection() {
        Connection con = null;
        try {
            con = DBConnect.getConnection();
            if (!con.isClosed()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(RestoreApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return false;
    }

    
        public boolean testLocalDBConnection() {
        Connection con = null;
        try {
            con = LocalDBConnect.getConnection();
            if (!con.isClosed()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(RestoreApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return false;
    }

    public void displayLastUpdateDate() {
        try {
            Connection con = DBConnect.getConnection();
            PreparedStatement pst = con.prepareStatement(DBQueries.getConfigValue);
            pst.setObject(1, "lastbackupdate");
            ResultSet rst = pst.executeQuery();
            if (rst.next()) {
                lastbackupdate = rst.getString("configvalue");
                jLabel3.setText("Last Backup at \n" + lastbackupdate);
            }
            rst.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final boolean checkRemoteFTPServer() {
        boolean flag = false;
        try {
            FtpWrapper ftp = new FtpWrapper();
            String hostDomain = ftp.getServerName();
            String Id = ftp.getUsername();
            String Password = ftp.getPassword();
            if (ftp.connectAndLogin(hostDomain, Id, Password)) {
                flag = true;
                ftp.disconnect();
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return flag;
    }

    public final boolean checkLocalFTPServer() {
        boolean flag = false;
        try {
            LocalFtpWrapper ftp = new LocalFtpWrapper();
            String hostDomain = ftp.getServerName();
            String Id = ftp.getUsername();
            String Password = ftp.getPassword();
            if (ftp.connectAndLogin(hostDomain, Id, Password)) {
                flag = true;
                ftp.disconnect();
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return flag;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 51, 51), 2, true));

        jButton1.setText("Start Restore");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Stop Restore");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 60)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("0");

        jLabel6.setText("Files Pushed");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("FILE RESTORE");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addComponent(jLabel6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(15, 15, 15)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addGap(37, 37, 37))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("ODS - RESTORE MANAGEMENT TOOLKIT");

        jLabel4.setForeground(new java.awt.Color(153, 153, 153));
        jLabel4.setText("Pranav VG");

        jLabel3.setText("Last Backup");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 153), 2));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("DB RESTORE");

        jButton3.setText("Start Restore");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel8.setText("1");

        jLabel9.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel9.setText("2");

        jLabel10.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel10.setText("3");

        jLabel11.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel11.setText("4");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3)
                    .addComponent(jLabel7))
                .addGap(88, 88, 88))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(14, 14, 14)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(44, 44, 44))
        );

        fileMenu.setMnemonic('f');
        fileMenu.setText("File");

        jMenuItem2.setText("Backup");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem2);

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setActionCommand("Exit");
        exitMenuItem.setLabel("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);
        exitMenuItem.getAccessibleContext().setAccessibleDescription("");

        menuBar.add(fileMenu);

        editMenu.setMnemonic('e');
        editMenu.setText("Run");

        jMenuItem1.setText("Diagnostics");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        editMenu.add(jMenuItem1);

        menuBar.add(editMenu);

        helpMenu.setMnemonic('h');
        helpMenu.setText("Help");

        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 514, Short.MAX_VALUE)
                        .addComponent(jLabel4))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(180, 180, 180)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addGap(6, 6, 6))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void confirmAndExit() {
        String ObjButtons[] = {"Yes", "No"};
        int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure to exit?", "Confirmation", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
        if (PromptResult == JOptionPane.YES_OPTION) {
            //Thread.sleep(1000);
            System.exit(0);
        }
    }
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        confirmAndExit();
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (testDBConnection() && checkLocalFTPServer() && checkRemoteFTPServer() && testLocalDBConnection()) {
            //System.out.println("Btn");
            jButton1.setEnabled(false);
            jButton2.setEnabled(true);
            bw = new RBackupWorker();
            counter=0;
            
            bw.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if ("progress".equals(evt.getPropertyName())) {
                        int value = (Integer) evt.getNewValue();
                        //System.out.println("propertyChange called with: " + value);
                        
                        if(value==100) {
                            //bw.cancel(true);
                            jButton1.setEnabled(true);
                            imageLabel.setVisible(false);
                            jButton2.setEnabled(false);
                            updateBackupdatetoDB();
                            displayLastUpdateDate();
                        }
                        
                        counter++;
                        jLabel5.setText(String.valueOf(counter));
                    }
                }
            });
            bw.execute();
            imageLabel.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(rootPane, "Connection Failed.\nPlease check using Run>Diagnostics", "Error", 2);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        JOptionPane.showMessageDialog(rootPane, "ODS - Backup Management Toolkit\nDeveloped By Pranav VG\nv 1.0", "About", 1);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        bw.cancel(true);
        jButton1.setEnabled(true);
        imageLabel.setVisible(false);
        jButton2.setEnabled(false);
        updateBackupdatetoDB();
        displayLastUpdateDate();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        long startmillis = System.currentTimeMillis();
        boolean rftp = checkRemoteFTPServer();
        long rfptmillis = System.currentTimeMillis();
        boolean lftp = checkLocalFTPServer();
        long lfptmillis = System.currentTimeMillis();
        boolean dbcon = testDBConnection();
        long dbmillis = System.currentTimeMillis();
        boolean localdbcon = testLocalDBConnection();
        long localdbmillis = System.currentTimeMillis();
        
        String msg = "Remote FTP Available : " + convertBoolToYesNo(rftp) + " \tLatency : " + (rfptmillis - startmillis) / 1000 + " sec(s)";
        msg += "\nLocal FTP Available : " + convertBoolToYesNo(lftp) + " \tLatency : " + (lfptmillis - rfptmillis) / 1000 + " sec(s)";
        msg += "\nRemote DB Available : " + convertBoolToYesNo(dbcon) + " \tLatency : " + (dbmillis - lfptmillis) / 1000 + " sec(s)";
        msg += "\nLocal DB Available : " + convertBoolToYesNo(localdbcon) + " \tLatency : " + (localdbmillis - dbmillis) / 1000 + " sec(s)";

        JOptionPane.showMessageDialog(rootPane, msg, "Diagnostics", 1);
// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private String convertBoolToYesNo(boolean flag){
        if(flag)
            return "Yes";
        else
            return "No";
    }
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        
        if (testDBConnection() && checkLocalFTPServer() && checkRemoteFTPServer() && testLocalDBConnection()){
        jButton3.setEnabled(false);
        imageLabel1.setVisible(true);
        jLabel8.setText("1 Exporting Local DB");
        dw = new RSQLDumpWorker();
            dw.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if ("progress".equals(evt.getPropertyName())) {
                        int value = (Integer) evt.getNewValue();
                        System.out.println("propertyChange called with: " + value);
                        switch(value){
                            case 1 : jLabel9.setText("2 Copying to Remote Storage");break;
                            case 2 : jLabel10.setText("3 Importing to Remote DB");break;
                            case 3 : jLabel11.setText("4 Finished !!");break;
                            default: break;
                        }
                        if(value==3){
                            jButton3.setEnabled(true);
                            jLabel11.setText("4 Finished");
                            imageLabel1.setVisible(false);
                            updateBackupdatetoDB();
                            displayLastUpdateDate();
                        }
                    }
                }
            });
            dw.execute();
        }else{
        JOptionPane.showMessageDialog(rootPane, "Connection Failed.\nPlease check using Run>Diagnostics", "Error", 2);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        new MainApp().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RestoreApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RestoreApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RestoreApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RestoreApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RestoreApp().setVisible(true);
            }
        });

    }

    public void updateBackupdatetoDB() {
        try {
            /*Connection con = DBConnect.getConnection();
            PreparedStatement pst = con.prepareStatement(DBQueries.updateBackupdatetoDB);
            pst.setObject(1, new Date());
            pst.setObject(2, "lastbackupdate");
            pst.execute();
            pst.close();
            con.close();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables

}
