/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.utils.keyStoreAdmin;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.crypto.SecretKey;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.jdesktop.application.Action;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.SingleFrameApplication;

import org.radixware.kernel.common.enums.EKeyStoreType;
import org.radixware.kernel.common.enums.ERadixApplication;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.common.ssl.KeystoreDesKeyEntry;
import org.radixware.kernel.common.ssl.KeystoreEntry;
import org.radixware.kernel.common.ssl.KeystoreRsaKeyEntry;
import org.radixware.kernel.common.ssl.KeystoreTrustedCertificateEntry;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.utils.SystemTools;

import org.radixware.kernel.utils.keyStoreAdmin.utils.KeyStoreAdminFileFilter;
import org.radixware.kernel.utils.keyStoreAdmin.utils.KeyStoreAdminFileSelector;
import org.radixware.kernel.utils.keyStoreAdmin.utils.KeyStoreAdminFileSelector.SelectNewFileMode;
import org.radixware.kernel.utils.keyStoreAdmin.utils.KeyStoreEntryTreeRenderer.KeystoreTreeItem;
import org.radixware.kernel.utils.keyStoreAdmin.utils.KeyStoreEntryTreeRenderer;
import org.radixware.kernel.utils.keyStoreAdmin.dialogs.CertificateRequestDialog;
import org.radixware.kernel.utils.keyStoreAdmin.dialogs.ChangePasswordDialog;
import org.radixware.kernel.utils.keyStoreAdmin.dialogs.DistinguishedNameDialog;
import org.radixware.kernel.utils.keyStoreAdmin.dialogs.ExportCertificateDialog;
import org.radixware.kernel.utils.keyStoreAdmin.dialogs.GenerateDesKeyDialog;
import org.radixware.kernel.utils.keyStoreAdmin.dialogs.GenerateKeyPairDialog;
import org.radixware.kernel.utils.keyStoreAdmin.dialogs.ImportDesKeyDialog;
import org.radixware.kernel.utils.keyStoreAdmin.dialogs.InputDesKeyComponentDialog;
import org.radixware.kernel.utils.keyStoreAdmin.dialogs.KeyPairExchangeDialog;
import org.radixware.kernel.utils.keyStoreAdmin.dialogs.LoadTrustedCertificateDialog;
import org.radixware.kernel.utils.keyStoreAdmin.dialogs.OpenKeyStoreDialog;
import org.radixware.kernel.utils.keyStoreAdmin.dialogs.PasswordDialog;
import org.radixware.kernel.utils.keyStoreAdmin.dialogs.SignCertificateDialog;
import org.radixware.kernel.utils.keyStoreAdmin.utils.Crypto;

/**
 * The application's main frame.
 */

public final class KeyStoreAdminView extends FrameView {

    public KeyStoreAdminView(final SingleFrameApplication app) {
        super(app);
        initComponents();
        
        //getFrame().setResizable(false);
        getFrame().setMinimumSize(new Dimension(640, 400));
        getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        getFrame().addWindowListener(
            new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    closeApplication();
                }
        });

        readLocalSettings();
        displayStatus(bundle.getString("Status.Welcome"));
    }

    /**
     * Constructor to be called when a specific keystore needs to be opened at
     * startup.
     * @param app instance of <code>KeyStoreAdmin</code> application
     * @param path keystore path
     * @param password keystore password
     * Since this constructor is always called explicitly from another application,
     * JavaVM will not be terminated after KeyStoreAdmin's window is closed
     */
    public KeyStoreAdminView(final SingleFrameApplication app, String path, char[] password){
        this(app);
        keystorePath = path;
        keystorePassword = password;
        setWait(true);
        try{
            keystoreController = new KeystoreController(keystorePath, keystorePassword);
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.OpenKeystore"), e, "Constructor with predefined keystore address and password");
            setWait(false);
            return;
        }
        openedKeyStoreType = EKeyStoreType.FILE;
        setKeystoreOpened(true);
        displayKeyStoreEntries();
        displayStatus(bundle.getString("Status.FileKeystoreOpened"));
        setWait(false);
        closeApplication = false; //don't terminate JavaVM when keyStoreAdmin window is closed
    }

    private File getSettingsFile(){
        return new File(SystemTools.getRadixApplicationDataPath(ERadixApplication.KEYSTORE_ADMIN), SETTINGS_FILENAME);
    }

    private void readLocalSettings(){
        final Properties properties = new Properties();
        try{
            properties.load(new FileInputStream(getSettingsFile()));
        } catch (IOException e){
            return;
        }
        CN = properties.getProperty("UserName", "");
        OU = properties.getProperty("OrganizationUnit", "");
        O = properties.getProperty("Organization", "");
        L = properties.getProperty("Locality", "");
        ST = properties.getProperty("State", "");
        C = properties.getProperty("Country", "");
        UID = properties.getProperty("UID", "");
        defaultKeystorePath = properties.getProperty("KeyStorePath", "");
        defaultPKCS11ConfigPath = properties.getProperty("PKCS11ConfigPath", "");
        defaultCertificateSigningRequestPath = properties.getProperty("CertificateSigningRequestPath", "");
        defaultSignedCertificatePath = properties.getProperty("SignedCertificatePath", "");
        defaultTrustedCertificatePath = properties.getProperty("TrustedCertificatePath", "");
        defaultCertificateExportPath = properties.getProperty("CertificateExportPath", "");
        defaultPKCS12Path = properties.getProperty("PKCS12Path", "");
        final String sDefaultSelfCertificateDurationDays = properties.getProperty("SelfCertificateDurationDays", "");
        if (sDefaultSelfCertificateDurationDays.length()>0)
            defaultSelfCertificateDurationDays = Integer.parseInt(sDefaultSelfCertificateDurationDays);
        final String sDefaultCertificateDurationDays = properties.getProperty("CertificateDurationDays", "");
        if (sDefaultCertificateDurationDays.length()>0)
            defaultCertificateDurationDays = Integer.parseInt(sDefaultCertificateDurationDays);

        final String sShowDnDialogEachTime = properties.getProperty("ShowDnDialogEachTime", Boolean.TRUE.toString());
        showDnDialogEachTime = Boolean.valueOf(sShowDnDialogEachTime);
    }

    private void saveLocalSettings(){
        final Properties properties = new Properties();

        properties.setProperty("UserName", CN);
        properties.setProperty("OrganizationUnit", OU);
        properties.setProperty("Organization", O);
        properties.setProperty("Locality", L);
        properties.setProperty("State", ST);
        properties.setProperty("Country", C);
        properties.setProperty("UID", UID);

        properties.setProperty("KeyStorePath", defaultKeystorePath);
        properties.setProperty("PKCS11ConfigPath", defaultPKCS11ConfigPath);
        properties.setProperty("CertificateSigningRequestPath", defaultCertificateSigningRequestPath);
        properties.setProperty("SignedCertificatePath", defaultSignedCertificatePath);
        properties.setProperty("TrustedCertificatePath", defaultTrustedCertificatePath);
        properties.setProperty("CertificateExportPath", defaultCertificateExportPath);
        properties.setProperty("PKCS12Path", defaultPKCS12Path);
        properties.setProperty("SelfCertificateDurationDays", Integer.toString(defaultSelfCertificateDurationDays));
        properties.setProperty("CertificateDurationDays", Integer.toString(defaultCertificateDurationDays));

        properties.setProperty("ShowDnDialogEachTime", Boolean.toString(showDnDialogEachTime));

        try{
            properties.store(new FileOutputStream(getSettingsFile()), "KeyStoreAdmin local settings");
        } catch (IOException e){
        }
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            final JFrame mainFrame = KeyStoreAdmin.getApplication().getMainFrame();
            aboutBox = new KeyStoreAdminAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        KeyStoreAdmin.getApplication().show(aboutBox);
    }

    @Action
    public void closeApplication(){
        saveLocalSettings();
        checkSaveKeyStoreBeforeExit();
        if (closeApplication)
            KeyStoreAdmin.getApplication().exit();
        else
            KeyStoreAdmin.getApplication().hide(this);
    }

    private void checkSaveKeyStoreBeforeExit(){
        if (isKeystoreChanged() && confirm("ConfirmSaveKeyStore", "ClosingKeyStore"))
            saveKeyStore();
    }

    private void checkDistinguishedNameDialogRequired(){
        if (showDnDialogEachTime || (isDNEmpty() && confirm("ConfirmSetupDN", "EmptyDN")))
            setupDistinguishedName();
    }

    private boolean isDNEmpty(){
        return (CN.length()==0 && OU.length()==0 && O.length()==0 &&
                L.length()==0 && ST.length()==0 && C.length()==0);
    }

    private boolean confirm(final String messageBundle, final String titleBundle, final Object... parameter){
        final String messageBoxTitle = bundle.getString(titleBundle);
        final String message = String.format(bundle.getString(messageBundle), parameter);
        final int confirm = JOptionPane.showConfirmDialog(getFrame(), message, messageBoxTitle, JOptionPane.YES_NO_OPTION);
        return (confirm==JOptionPane.YES_OPTION);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTreeKeyStoreEntries = new javax.swing.JTree();
        jPanelStatus = new javax.swing.JPanel();
        jLabelStatus = new javax.swing.JLabel();
        jToolBar = new javax.swing.JToolBar();
        jButtonCreateFileKeyStore = new javax.swing.JButton();
        jButtonOpenFileKeyStore = new javax.swing.JButton();
        jButtonOpenPKCS11KeyStore = new javax.swing.JButton();
        jButtonSaveKeyStore = new javax.swing.JButton();
        jButtonChangeKeystorePasword = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButtonGenerateKeyPair = new javax.swing.JButton();
        jButtonGenerateAndExportToPKCS12 = new javax.swing.JButton();
        jButtonExportToPKCS12 = new javax.swing.JButton();
        jButtonImportFromPKCS12 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButtonExportCertificate = new javax.swing.JButton();
        jButtonLoadTrustedCertificate = new javax.swing.JButton();
        jButtonPrepareCertificateRequest = new javax.swing.JButton();
        jButtonParseCertificateResponse = new javax.swing.JButton();
        jButtonProcessCertificateRequest = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButtonGenerateDesKey = new javax.swing.JButton();
        jButtonImportDesKey = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButtonDeleteEntry = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItemCreateFile = new javax.swing.JMenuItem();
        jMenuItemOpenFile = new javax.swing.JMenuItem();
        jMenuItemOpenPKCS11 = new javax.swing.JMenuItem();
        jMenuItemSave = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        settingsMenu = new javax.swing.JMenu();
        jMenuItemDistinguishedName = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();

        mainPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        mainPanel.setName("mainPanel"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("no keystore was opened");
        jTreeKeyStoreEntries.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTreeKeyStoreEntries.setCellRenderer(new KeyStoreEntryTreeRenderer());
        jTreeKeyStoreEntries.setName("jTreeKeyStoreEntries"); // NOI18N
        jTreeKeyStoreEntries.setRowHeight(20);
        jTreeKeyStoreEntries.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTreeKeyStoreEntriesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTreeKeyStoreEntries);

        jPanelStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanelStatus.setName("jPanelStatus"); // NOI18N

        jLabelStatus.setText("Status text"); // NOI18N
        jLabelStatus.setName("jLabelStatus"); // NOI18N

        javax.swing.GroupLayout jPanelStatusLayout = new javax.swing.GroupLayout(jPanelStatus);
        jPanelStatus.setLayout(jPanelStatusLayout);
        jPanelStatusLayout.setHorizontalGroup(
            jPanelStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(315, Short.MAX_VALUE))
        );
        jPanelStatusLayout.setVerticalGroup(
            jPanelStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelStatus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jToolBar.setFloatable(false);
        jToolBar.setRollover(true);
        jToolBar.setName("jToolBar"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.radixware.kernel.utils.keyStoreAdmin.KeyStoreAdmin.class).getContext().getActionMap(KeyStoreAdminView.class, this);
        jButtonCreateFileKeyStore.setAction(actionMap.get("createFileKeyStore")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.radixware.kernel.utils.keyStoreAdmin.KeyStoreAdmin.class).getContext().getResourceMap(KeyStoreAdminView.class);
        jButtonCreateFileKeyStore.setIcon(resourceMap.getIcon("jButtonCreateFileKeyStore.icon")); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/utils/keyStoreAdmin/resources/KeyStoreAdminView"); // NOI18N
        jButtonCreateFileKeyStore.setToolTipText(bundle.getString("jButtonCreateFileKeyStore.text")); // NOI18N
        jButtonCreateFileKeyStore.setFocusable(false);
        jButtonCreateFileKeyStore.setHideActionText(true);
        jButtonCreateFileKeyStore.setName("jButtonCreateFileKeyStore"); // NOI18N
        jToolBar.add(jButtonCreateFileKeyStore);

        jButtonOpenFileKeyStore.setAction(actionMap.get("openFileKeyStore")); // NOI18N
        jButtonOpenFileKeyStore.setIcon(resourceMap.getIcon("jButtonOpenFileKeyStore.icon")); // NOI18N
        jButtonOpenFileKeyStore.setToolTipText(bundle.getString("jButtonOpenFileKeyStore.text")); // NOI18N
        jButtonOpenFileKeyStore.setFocusable(false);
        jButtonOpenFileKeyStore.setHideActionText(true);
        jButtonOpenFileKeyStore.setName("jButtonOpenFileKeyStore"); // NOI18N
        jToolBar.add(jButtonOpenFileKeyStore);

        jButtonOpenPKCS11KeyStore.setAction(actionMap.get("openPKCS11KeyStore")); // NOI18N
        jButtonOpenPKCS11KeyStore.setIcon(resourceMap.getIcon("jButtonOpenPKCS11KeyStore.icon")); // NOI18N
        jButtonOpenPKCS11KeyStore.setToolTipText(bundle.getString("jButtonOpenPKCS11KeyStore.text")); // NOI18N
        jButtonOpenPKCS11KeyStore.setFocusable(false);
        jButtonOpenPKCS11KeyStore.setHideActionText(true);
        jButtonOpenPKCS11KeyStore.setName("jButtonOpenPKCS11KeyStore"); // NOI18N
        jToolBar.add(jButtonOpenPKCS11KeyStore);

        jButtonSaveKeyStore.setAction(actionMap.get("saveKeyStore")); // NOI18N
        jButtonSaveKeyStore.setIcon(resourceMap.getIcon("jButtonSaveKeyStore.icon")); // NOI18N
        jButtonSaveKeyStore.setToolTipText(bundle.getString("jButtonSaveKeyStore.text")); // NOI18N
        jButtonSaveKeyStore.setFocusable(false);
        jButtonSaveKeyStore.setHideActionText(true);
        jButtonSaveKeyStore.setName("jButtonSaveKeyStore"); // NOI18N
        jToolBar.add(jButtonSaveKeyStore);

        jButtonChangeKeystorePasword.setAction(actionMap.get("changeKeystorePassword")); // NOI18N
        jButtonChangeKeystorePasword.setIcon(resourceMap.getIcon("jButtonChangeKeystorePassword.icon")); // NOI18N
        jButtonChangeKeystorePasword.setToolTipText(bundle.getString("jButtonChangeKeystorePassword.text")); // NOI18N
        jButtonChangeKeystorePasword.setFocusable(false);
        jButtonChangeKeystorePasword.setHideActionText(true);
        jButtonChangeKeystorePasword.setName("jButtonChangeKeystorePassword"); // NOI18N
        jToolBar.add(jButtonChangeKeystorePasword);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jToolBar.add(jSeparator1);

        jButtonGenerateKeyPair.setAction(actionMap.get("generateKeyPair")); // NOI18N
        jButtonGenerateKeyPair.setIcon(resourceMap.getIcon("jButtonGenerateKeyPair.icon")); // NOI18N
        jButtonGenerateKeyPair.setToolTipText(bundle.getString("jButtonGenerateKeyPair.text")); // NOI18N
        jButtonGenerateKeyPair.setFocusable(false);
        jButtonGenerateKeyPair.setHideActionText(true);
        jButtonGenerateKeyPair.setName("jButtonGenerateKeyPair"); // NOI18N
        jToolBar.add(jButtonGenerateKeyPair);

        jButtonGenerateAndExportToPKCS12.setAction(actionMap.get("generateAndExportKeyPair")); // NOI18N
        jButtonGenerateAndExportToPKCS12.setIcon(resourceMap.getIcon("jButtonGenerateAndExportToPKCS12.icon")); // NOI18N
        jButtonGenerateAndExportToPKCS12.setToolTipText(bundle.getString("jButtonGenerateAndExportToPKCS11.text")); // NOI18N
        jButtonGenerateAndExportToPKCS12.setFocusable(false);
        jButtonGenerateAndExportToPKCS12.setHideActionText(true);
        jButtonGenerateAndExportToPKCS12.setName("jButtonGenerateAndExportToPKCS12"); // NOI18N
        jToolBar.add(jButtonGenerateAndExportToPKCS12);

        jButtonExportToPKCS12.setAction(actionMap.get("exportKeyPair")); // NOI18N
        jButtonExportToPKCS12.setIcon(resourceMap.getIcon("jButtonExportToPKCS12.icon")); // NOI18N
        jButtonExportToPKCS12.setToolTipText(bundle.getString("jButtonExportToPKCS12.text")); // NOI18N
        jButtonExportToPKCS12.setFocusable(false);
        jButtonExportToPKCS12.setHideActionText(true);
        jButtonExportToPKCS12.setName("jButtonExportToPKCS12"); // NOI18N
        jToolBar.add(jButtonExportToPKCS12);

        jButtonImportFromPKCS12.setAction(actionMap.get("importKeyPair")); // NOI18N
        jButtonImportFromPKCS12.setIcon(resourceMap.getIcon("jButtonImportFromPKCS12.icon")); // NOI18N
        jButtonImportFromPKCS12.setToolTipText(bundle.getString("jButtonImportFromPKCS12.text")); // NOI18N
        jButtonImportFromPKCS12.setFocusable(false);
        jButtonImportFromPKCS12.setHideActionText(true);
        jButtonImportFromPKCS12.setName("jButtonImportFromPKCS12"); // NOI18N
        jToolBar.add(jButtonImportFromPKCS12);

        jSeparator2.setName("jSeparator2"); // NOI18N
        jToolBar.add(jSeparator2);

        jButtonExportCertificate.setAction(actionMap.get("exportCertificate")); // NOI18N
        jButtonExportCertificate.setIcon(resourceMap.getIcon("jButtonExportCertificate.icon")); // NOI18N
        jButtonExportCertificate.setToolTipText(bundle.getString("jButtonExportCertificate.text")); // NOI18N
        jButtonExportCertificate.setFocusable(false);
        jButtonExportCertificate.setHideActionText(true);
        jButtonExportCertificate.setName("jButtonExportCertificate"); // NOI18N
        jToolBar.add(jButtonExportCertificate);

        jButtonLoadTrustedCertificate.setAction(actionMap.get("loadTrustedCertificate")); // NOI18N
        jButtonLoadTrustedCertificate.setIcon(resourceMap.getIcon("jButtonLoadTrustedCertificate.icon")); // NOI18N
        jButtonLoadTrustedCertificate.setToolTipText(bundle.getString("jButtonLoadTrustedCertificate.text")); // NOI18N
        jButtonLoadTrustedCertificate.setFocusable(false);
        jButtonLoadTrustedCertificate.setHideActionText(true);
        jButtonLoadTrustedCertificate.setName("jButtonLoadTrustedCertificate"); // NOI18N
        jToolBar.add(jButtonLoadTrustedCertificate);

        jButtonPrepareCertificateRequest.setAction(actionMap.get("prepareCertificateRequest")); // NOI18N
        jButtonPrepareCertificateRequest.setIcon(resourceMap.getIcon("jButtonPrepareCertificateRequest.icon")); // NOI18N
        jButtonPrepareCertificateRequest.setToolTipText(bundle.getString("jButtonPrepareCertificateRequest.text")); // NOI18N
        jButtonPrepareCertificateRequest.setFocusable(false);
        jButtonPrepareCertificateRequest.setHideActionText(true);
        jButtonPrepareCertificateRequest.setName("jButtonPrepareCertificateRequest"); // NOI18N
        jToolBar.add(jButtonPrepareCertificateRequest);

        jButtonParseCertificateResponse.setAction(actionMap.get("readCertificateResponse")); // NOI18N
        jButtonParseCertificateResponse.setIcon(resourceMap.getIcon("jButtonParseCertificateResponse.icon")); // NOI18N
        jButtonParseCertificateResponse.setToolTipText(bundle.getString("jButtonParseCertificateResponse.text")); // NOI18N
        jButtonParseCertificateResponse.setFocusable(false);
        jButtonParseCertificateResponse.setHideActionText(true);
        jButtonParseCertificateResponse.setName("jButtonParseCertificateResponse"); // NOI18N
        jToolBar.add(jButtonParseCertificateResponse);

        jButtonProcessCertificateRequest.setAction(actionMap.get("signClientCertificate")); // NOI18N
        jButtonProcessCertificateRequest.setIcon(resourceMap.getIcon("jButtonProcessCertificateRequest.icon")); // NOI18N
        jButtonProcessCertificateRequest.setToolTipText(bundle.getString("jButtonProcessCertificateRequest.text")); // NOI18N
        jButtonProcessCertificateRequest.setFocusable(false);
        jButtonProcessCertificateRequest.setHideActionText(true);
        jButtonProcessCertificateRequest.setName("jButtonProcessCertificateRequest"); // NOI18N
        jToolBar.add(jButtonProcessCertificateRequest);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jToolBar.add(jSeparator3);

        jButtonGenerateDesKey.setAction(actionMap.get("generateDesKey")); // NOI18N
        jButtonGenerateDesKey.setIcon(resourceMap.getIcon("jButtonGenerateDesKey.icon")); // NOI18N
        jButtonGenerateDesKey.setText(""); // NOI18N
        jButtonGenerateDesKey.setToolTipText(bundle.getString("jButtonGenerateDesKey.text")); // NOI18N
        jButtonGenerateDesKey.setFocusable(false);
        jButtonGenerateDesKey.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonGenerateDesKey.setName("jButtonGenerateDesKey"); // NOI18N
        jButtonGenerateDesKey.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar.add(jButtonGenerateDesKey);

        jButtonImportDesKey.setAction(actionMap.get("importDesKey")); // NOI18N
        jButtonImportDesKey.setIcon(resourceMap.getIcon("jButtonImportDesKey.icon")); // NOI18N
        jButtonImportDesKey.setText(""); // NOI18N
        jButtonImportDesKey.setToolTipText(bundle.getString("jButtonImportDesKey.text")); // NOI18N
        jButtonImportDesKey.setFocusable(false);
        jButtonImportDesKey.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonImportDesKey.setName("jButtonImportDesKey"); // NOI18N
        jButtonImportDesKey.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar.add(jButtonImportDesKey);

        jSeparator5.setName("jSeparator5"); // NOI18N
        jToolBar.add(jSeparator5);

        jButtonDeleteEntry.setAction(actionMap.get("deleteEntry")); // NOI18N
        jButtonDeleteEntry.setIcon(resourceMap.getIcon("jButtonDeleteEntry.icon")); // NOI18N
        jButtonDeleteEntry.setToolTipText(bundle.getString("jButtonDeleteEntry.text")); // NOI18N
        jButtonDeleteEntry.setFocusable(false);
        jButtonDeleteEntry.setHideActionText(true);
        jButtonDeleteEntry.setName("jButtonDeleteEntry"); // NOI18N
        jToolBar.add(jButtonDeleteEntry);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelStatus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 682, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE)
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(bundle.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        jMenuItemCreateFile.setAction(actionMap.get("createFileKeyStore")); // NOI18N
        jMenuItemCreateFile.setIcon(resourceMap.getIcon("jMenuItemCreateFile.icon")); // NOI18N
        jMenuItemCreateFile.setText(bundle.getString("jMenuItemCreateFile.text")); // NOI18N
        jMenuItemCreateFile.setName("jMenuItemCreateFile"); // NOI18N
        fileMenu.add(jMenuItemCreateFile);

        jMenuItemOpenFile.setAction(actionMap.get("openFileKeyStore")); // NOI18N
        jMenuItemOpenFile.setIcon(resourceMap.getIcon("jMenuItemOpenFile.icon")); // NOI18N
        jMenuItemOpenFile.setText(bundle.getString("jMenuItemOpenFile.text")); // NOI18N
        jMenuItemOpenFile.setName("jMenuItemOpenFile"); // NOI18N
        fileMenu.add(jMenuItemOpenFile);

        jMenuItemOpenPKCS11.setAction(actionMap.get("openPKCS11KeyStore")); // NOI18N
        jMenuItemOpenPKCS11.setIcon(resourceMap.getIcon("jMenuItemOpenPKCS11.icon")); // NOI18N
        jMenuItemOpenPKCS11.setText(bundle.getString("jMenuItemOpenPKCS11.text")); // NOI18N
        jMenuItemOpenPKCS11.setName("jMenuItemOpenPKCS11"); // NOI18N
        fileMenu.add(jMenuItemOpenPKCS11);

        jMenuItemSave.setAction(actionMap.get("saveKeyStore")); // NOI18N
        jMenuItemSave.setIcon(resourceMap.getIcon("jMenuItemSave.icon")); // NOI18N
        jMenuItemSave.setText(bundle.getString("jMenuItemSave.text")); // NOI18N
        jMenuItemSave.setName("jMenuItemSave"); // NOI18N
        fileMenu.add(jMenuItemSave);

        jSeparator4.setName("jSeparator4"); // NOI18N
        fileMenu.add(jSeparator4);

        exitMenuItem.setAction(actionMap.get("closeApplication")); // NOI18N
        exitMenuItem.setText(bundle.getString("exitMenuItem.text")); // NOI18N
        exitMenuItem.setToolTipText(bundle.getString("exitMenuItem.toolTipText")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        settingsMenu.setText(bundle.getString("settingsMenu.text")); // NOI18N
        settingsMenu.setName("settingsMenu"); // NOI18N

        jMenuItemDistinguishedName.setAction(actionMap.get("setupDistinguishedName")); // NOI18N
        jMenuItemDistinguishedName.setText(bundle.getString("jMenuItemDistinguishedName.text")); // NOI18N
        jMenuItemDistinguishedName.setName("jMenuItemDistinguishedName"); // NOI18N
        settingsMenu.add(jMenuItemDistinguishedName);

        menuBar.add(settingsMenu);

        helpMenu.setText(bundle.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setText(bundle.getString("aboutMenuItem.text")); // NOI18N
        aboutMenuItem.setToolTipText(bundle.getString("aboutMenuItem.toolTipText")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(jPanelStatus);
        setToolBar(jToolBar);
    }// </editor-fold>//GEN-END:initComponents

    private void jTreeKeyStoreEntriesValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTreeKeyStoreEntriesValueChanged
        selectedEntry = null;
        final DefaultMutableTreeNode node = (DefaultMutableTreeNode)jTreeKeyStoreEntries.getLastSelectedPathComponent();
        if (node==null){ //nothing is selected
            return;
        }
        final DefaultMutableTreeNode root = (DefaultMutableTreeNode)jTreeKeyStoreEntries.getModel().getRoot();
        if (node==root){
            return;
        }
        final Object object = node.getUserObject();
        if (object instanceof KeystoreEntry){
            onKeyStoreEntrySelected((KeystoreEntry)object);
        } else if (object instanceof RSAPrivateKey || object instanceof Certificate[] || object instanceof X509Certificate || object instanceof String){
            final TreePath path = evt.getPath().getParentPath();
            final DefaultMutableTreeNode parent = KeyStoreEntryTreeRenderer.getParentKeyStoreEntryNode(root, path);
            if (parent.getUserObject() instanceof KeystoreEntry)
                onKeyStoreEntrySelected((KeystoreEntry)parent.getUserObject());
        }
    }//GEN-LAST:event_jTreeKeyStoreEntriesValueChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonChangeKeystorePasword;
    private javax.swing.JButton jButtonCreateFileKeyStore;
    private javax.swing.JButton jButtonDeleteEntry;
    private javax.swing.JButton jButtonExportCertificate;
    private javax.swing.JButton jButtonExportToPKCS12;
    private javax.swing.JButton jButtonGenerateAndExportToPKCS12;
    private javax.swing.JButton jButtonGenerateDesKey;
    private javax.swing.JButton jButtonGenerateKeyPair;
    private javax.swing.JButton jButtonImportDesKey;
    private javax.swing.JButton jButtonImportFromPKCS12;
    private javax.swing.JButton jButtonLoadTrustedCertificate;
    private javax.swing.JButton jButtonOpenFileKeyStore;
    private javax.swing.JButton jButtonOpenPKCS11KeyStore;
    private javax.swing.JButton jButtonParseCertificateResponse;
    private javax.swing.JButton jButtonPrepareCertificateRequest;
    private javax.swing.JButton jButtonProcessCertificateRequest;
    private javax.swing.JButton jButtonSaveKeyStore;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JMenuItem jMenuItemCreateFile;
    private javax.swing.JMenuItem jMenuItemDistinguishedName;
    private javax.swing.JMenuItem jMenuItemOpenFile;
    private javax.swing.JMenuItem jMenuItemOpenPKCS11;
    private javax.swing.JMenuItem jMenuItemSave;
    private javax.swing.JPanel jPanelStatus;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar jToolBar;
    private javax.swing.JTree jTreeKeyStoreEntries;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu settingsMenu;
    // End of variables declaration//GEN-END:variables

    private final String SETTINGS_FILENAME = "keyStoreAdmin.properties";

    private JDialog aboutBox;
    private DefaultTreeModel treeModel = null;
    private boolean keystoreOpened = false;
    private boolean keystoreChanged = false;
    private boolean keyEntryPresent = false;
    private boolean certificateEntryPresent = false;
    private boolean keyExportAllowed = false;
    private boolean keystoreSaveAllowed = false;
    private boolean keystorePasswordChangeAllowed = false;
    private String keystorePath = null;
    private char[] pkcs11KeyStorePin = null;
    private KeystoreEntry selectedEntry = null;
    private final ResourceBundle bundle = ResourceBundle.getBundle("org/radixware/kernel/utils/keyStoreAdmin/resources/KeyStoreAdminView");
    private KeystoreController keystoreController;
    private EKeyStoreType openedKeyStoreType = null;
    private char[] keystorePassword = null;

    //parts of distinguished name
    private String CN = "";
    private String OU = "";
    private String O = "";
    private String L = "";
    private String ST = "";
    private String C = "";
    private String UID = "";

    private String defaultKeystorePath = "";
    private String defaultPKCS11ConfigPath = "";
    private String defaultCertificateSigningRequestPath = "";
    private String defaultSignedCertificatePath = "";
    private String defaultTrustedCertificatePath = "";
    private String defaultCertificateExportPath = "";
    private String defaultPKCS12Path = "";
    private int defaultSelfCertificateDurationDays = 365;
    private int defaultCertificateDurationDays = 365;

    private boolean showDnDialogEachTime = true;

    private boolean closeApplication = true; //set to false if KeyStoreAdmin is opened from another application

    public boolean isKeystoreOpened() {
        return keystoreOpened;
    }
    public void setKeystoreOpened(final boolean keyStoreOpened) {
        final boolean oldValue = this.keystoreOpened;
        this.keystoreOpened = keyStoreOpened;
        firePropertyChange("keystoreOpened", oldValue, this.keystoreOpened);
        setKeystoreChanged(false);
        setKeyExportAllowed(keyStoreOpened && openedKeyStoreType==EKeyStoreType.FILE && isKeyEntryPresent());
        setKeystorePasswordChangeAllowed(keyStoreOpened && openedKeyStoreType==EKeyStoreType.FILE);
    }

    public boolean isKeystoreChanged() {
        return keystoreChanged;
    }
    public void setKeystoreChanged(final boolean keyStoreChanged) {
        final boolean oldValue = this.keystoreChanged;
        this.keystoreChanged = keyStoreChanged;
        firePropertyChange("keystoreChanged", oldValue, this.keystoreChanged);

        setKeystoreSaveAllowed(openedKeyStoreType==EKeyStoreType.FILE && keyStoreChanged);
    }

    public boolean isKeystoreSaveAllowed(){
        return keystoreSaveAllowed;
    }

    public void setKeystoreSaveAllowed(final boolean keystoreSaveAllowed){
        final boolean oldValue = this.keystoreSaveAllowed;
        this.keystoreSaveAllowed = keystoreSaveAllowed;
        firePropertyChange("keystoreSaveAllowed", oldValue, this.keystoreSaveAllowed);
    }

    public boolean isKeystorePasswordChangeAllowed(){
        return keystorePasswordChangeAllowed;
    }

    public void setKeystorePasswordChangeAllowed(boolean keystorePasswordChangeAllowed){
        final boolean oldValue = this.keystorePasswordChangeAllowed;
        this.keystorePasswordChangeAllowed = keystorePasswordChangeAllowed;
        firePropertyChange("keystorePasswordChangeAllowed", oldValue, this.keystorePasswordChangeAllowed);
    }

    public boolean isKeyEntryPresent() {
        return keyEntryPresent;
    }
    public void setKeyEntryPresent(final boolean keyEntryPresent) {
        final boolean oldValue = this.keyEntryPresent;
        this.keyEntryPresent = keyEntryPresent;
        firePropertyChange("keyEntryPresent", oldValue, this.keyEntryPresent);
        firePropertyChange("entryPresent", oldValue || certificateEntryPresent, this.keyEntryPresent || certificateEntryPresent);
        setKeyExportAllowed(isKeystoreOpened() && openedKeyStoreType==EKeyStoreType.FILE && isKeyEntryPresent());
    }

    public boolean isCertificateEntryPresent() {
        return certificateEntryPresent;
    }
    public void setCertificateEntryPresent(final boolean certificateEntryPresent) {
        final boolean oldValue = this.certificateEntryPresent;
        this.certificateEntryPresent = certificateEntryPresent;
        firePropertyChange("certificateEntryPresent", oldValue, this.certificateEntryPresent);
        firePropertyChange("entryPresent", oldValue || keyEntryPresent, this.certificateEntryPresent || keyEntryPresent);
    }

    public boolean isEntryPresent(){
        return keyEntryPresent || certificateEntryPresent;
    }

    public boolean isKeyExportAllowed(){
        return keyExportAllowed;
    }

    public void setKeyExportAllowed(final boolean keyExportAllowed){
        final boolean oldValue = this.keyExportAllowed;
        this.keyExportAllowed = keyExportAllowed;
        firePropertyChange("keyExportAllowed", oldValue, this.keyExportAllowed);
    }

    private void onKeyStoreEntrySelected(final KeystoreEntry entry){
        selectedEntry = entry;
    }

    private String buildDistinguishedName(){
        return CertificateUtils.buildDistinguishedName(CN, OU, O, L, ST, C, UID);
    }

    private void doOpenFileKeystore(boolean createNewKeystore){
        setWait(true);
        if (keystoreController!=null)
            checkSaveKeyStoreBeforeExit();
        final OpenKeyStoreDialog dialog = new OpenKeyStoreDialog(getFrame(), defaultKeystorePath, createNewKeystore);
        dialog.setLocationRelativeTo(getFrame());
        dialog.setVisible(true);
        if (dialog.getReturnStatus()!=OpenKeyStoreDialog.RET_OK){
            setWait(false);
            return;
        }
        keystorePath = dialog.getKeyStorePath();
        keystorePassword = dialog.getKeyStorePassword();
        defaultKeystorePath = new File(keystorePath).getParent();
        try{
            if (createNewKeystore){
                keystoreController = new KeystoreController();
                saveKeyStoreToFile();
            } else
                keystoreController = new KeystoreController(keystorePath, keystorePassword);
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.OpenKeystore"), e, "doOpenFileKeystore");
            setWait(false);
            return;
        }

        openedKeyStoreType = EKeyStoreType.FILE;
        setKeystoreOpened(true);
        displayKeyStoreEntries();
        displayStatus(bundle.getString(createNewKeystore ? "Status.FileKeystoreCreated" : "Status.FileKeystoreOpened"));
        setWait(false);
    }

    @Action
    public void createFileKeyStore(){
        doOpenFileKeystore(true);
    }

    @Action
    public void openFileKeyStore(){
        doOpenFileKeystore(false);
    }

    @Action
    public void openPKCS11KeyStore(){
        setWait(true);
        if (keystoreController!=null)
            checkSaveKeyStoreBeforeExit();
        final KeyStoreAdminFileFilter filterCfg = new KeyStoreAdminFileFilter(new String[]{"cfg"}, "*.cfg");
        //final KeyStoreAdminFileFilter filterPkcs11 = new KeyStoreAdminFileFilter(new String[]{"pkcs11"}, "*.pkcs11");
        final String title = bundle.getString("openPKCS11ConfigChooser.dialogTitle");
        final File file = KeyStoreAdminFileSelector.selectFileForOpen(getFrame(), title, new KeyStoreAdminFileFilter[]{filterCfg/*, filterPkcs11*/}, defaultPKCS11ConfigPath, SelectNewFileMode.FORBIDDEN);
        if (file!=null){
            defaultPKCS11ConfigPath = file.getParent();
            try{
                keystoreController = new KeystoreController(file.getAbsolutePath(), new PasswordCallbackHandler());
            } catch (KeystoreControllerException e){
                showError(bundle.getString("Error.OpenKeystore"), e, "openPKCS11KeyStore");
                setWait(false);
                return;
            }
        } else{
            setWait(false);
            return;
        }

        openedKeyStoreType = EKeyStoreType.PKCS11;
        setKeystoreOpened(true);
        displayKeyStoreEntries();
        displayStatus(bundle.getString("Status.PKCS11KeystoreOpened"));
        setWait(false);
    }

    @Action (enabledProperty = "keystoreSaveAllowed")
    public void saveKeyStore(){
        setWait(true);
        switch (openedKeyStoreType){ //check the type of current keystore
            case FILE:
                saveKeyStoreToFile();
                break;
            case PKCS11:
                savePKCS11KeyStore();
                break;
            default:
                showError(String.format(bundle.getString("UnknownKeyStoreType"), openedKeyStoreType), "saveKeyStore");
                return;
        }
        displayStatus(bundle.getString("Status.KeystoreSaved"));
        setWait(false);
    }

    private void saveKeyStoreToFile(){
        try {
            keystoreController.saveKeyStoreToFile(keystorePath, keystorePassword);
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.SaveKeystore"), e, "saveKeyStoreToFile");
            return;
        }
        setKeystoreChanged(false);
    }

    private void savePKCS11KeyStore(){
        try {
            keystoreController.savePkcs11KeyStore(pkcs11KeyStorePin);
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.SaveKeystore"), e, "savePKCS11KeyStore");
        }
    }

    private void displayStatus(final String status){
        jLabelStatus.setText(status);
    }

    private void displayKeyStoreEntries(){
        clearKeyStoreEntryList();
        setKeyEntryPresent(false);
        setCertificateEntryPresent(false);

        final Iterable<KeystoreEntry> keyStoreEntryList;
        try{
            keyStoreEntryList = keystoreController.getKeyStoreEntries();
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.GetKeyAliases"), e, "displayKeyStoreEntries");
            return;
        }
        for (KeystoreEntry entry : keyStoreEntryList){
            if (entry instanceof KeystoreRsaKeyEntry){
                addRsaKeyEntryToList((KeystoreRsaKeyEntry)entry);
                setKeyEntryPresent(true);
            } else if (entry instanceof KeystoreTrustedCertificateEntry)
                addTrustedCertificateEntryToList((KeystoreTrustedCertificateEntry)entry);
            else if (entry instanceof KeystoreDesKeyEntry)
                addDesKeyEntryToList((KeystoreDesKeyEntry)entry);
            setCertificateEntryPresent(true); //can get a certificate from both types of records
        }

        applyKeyStoreEntryList();
    }

    private void clearKeyStoreEntryList(){
        String keystoreName = keystoreController.getKeyStoreName();
        if (keystoreName.isEmpty())
            keystoreName = bundle.getString("NewKeystore");
        final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new KeystoreTreeItem(keystoreName));
        treeModel = new DefaultTreeModel(rootNode);
    }

    private void addRsaKeyEntryToList(final KeystoreRsaKeyEntry entry){
        if (treeModel==null){
            showError(bundle.getString("TreeModelIsNull"), "addRsaKeyEntryToList");
            return;
        }
        final DefaultMutableTreeNode entryNode = new DefaultMutableTreeNode(entry);
        final DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)treeModel.getRoot();
        treeModel.insertNodeInto(entryNode, rootNode, treeModel.getChildCount(rootNode));
        final Certificate[] certificateChain = entry.getCertificateChain();
        final DefaultMutableTreeNode certificateChainNode = new DefaultMutableTreeNode(certificateChain);
        treeModel.insertNodeInto(certificateChainNode, entryNode, entryNode.getChildCount());
        final DefaultMutableTreeNode parentNode = certificateChainNode;
        
        for (Certificate certificate : certificateChain){
            final X509Certificate x509certificate = (X509Certificate)certificate;
            final DefaultMutableTreeNode certificateNode = new DefaultMutableTreeNode(x509certificate);
            treeModel.insertNodeInto(certificateNode, parentNode, parentNode.getChildCount());
            addCertificateInfoEntriesToList(certificateNode, x509certificate);
        }
    }

    private void addCertificateInfoEntriesToList(final DefaultMutableTreeNode certificateNode, final X509Certificate certificate){
        final DefaultMutableTreeNode subjectNode = new DefaultMutableTreeNode(bundle.getString("CertificateSubject")+certificate.getSubjectX500Principal().getName());
        treeModel.insertNodeInto(subjectNode, certificateNode, certificateNode.getChildCount());
        final DefaultMutableTreeNode issuerNode = new DefaultMutableTreeNode(bundle.getString("CertificateIssuer")+certificate.getIssuerX500Principal().getName());
        treeModel.insertNodeInto(issuerNode, certificateNode, certificateNode.getChildCount());
        final DefaultMutableTreeNode notBeforeNode = new DefaultMutableTreeNode(bundle.getString("CertificateNotBefore")+certificate.getNotBefore().toString());
        treeModel.insertNodeInto(notBeforeNode, certificateNode, certificateNode.getChildCount());
        final DefaultMutableTreeNode notAfterNode = new DefaultMutableTreeNode(bundle.getString("CertificateNotAfter")+certificate.getNotAfter().toString());
        treeModel.insertNodeInto(notAfterNode, certificateNode, certificateNode.getChildCount());
        try{
            final String fingerprintMD5 = CertificateUtils.getCertificateFingerprintAsString(CertificateUtils.ALGORITHM_MD5, certificate);
            final String fingerprintSHA1 = CertificateUtils.getCertificateFingerprintAsString(CertificateUtils.ALGORITHM_SHA1, certificate);
            final DefaultMutableTreeNode fingerprintMD5Node = new DefaultMutableTreeNode(bundle.getString("CertificateFingerprintMD5")+fingerprintMD5);
            treeModel.insertNodeInto(fingerprintMD5Node, certificateNode, certificateNode.getChildCount());
            final DefaultMutableTreeNode fingerprintSHA1Node = new DefaultMutableTreeNode(bundle.getString("CertificateFingerprintSHA1")+fingerprintSHA1);
            treeModel.insertNodeInto(fingerprintSHA1Node, certificateNode, certificateNode.getChildCount());
        } catch (CertificateUtilsException e){
            //ignore ?
        }
    }

    private void addTrustedCertificateEntryToList(final KeystoreTrustedCertificateEntry entry){
        if (treeModel==null){
            showError(bundle.getString("TreeModelIsNull"), "addTrustedCertificateToList");
            return;
        }
        final DefaultMutableTreeNode entryNode = new DefaultMutableTreeNode(entry);
        final DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)treeModel.getRoot();
        treeModel.insertNodeInto(entryNode, rootNode, treeModel.getChildCount(rootNode));
        final X509Certificate certificate = (X509Certificate)entry.getCertificate();
        final DefaultMutableTreeNode certificateNode = new DefaultMutableTreeNode(certificate);
        treeModel.insertNodeInto(certificateNode, entryNode, entryNode.getChildCount());
        addCertificateInfoEntriesToList(certificateNode, certificate);
    }
    
    private void addDesKeyEntryToList(final KeystoreDesKeyEntry entry){
        if (treeModel==null){
            showError(bundle.getString("TreeModelIsNull"), "addDesKeyEntryToList");
            return;
        }
        
        KeystoreDesKeyEntry fullKeyEntry; //input entry contains only alias; try to read entry containing key
        try{
            fullKeyEntry = keystoreController.getKeystoreDesKeyEntry(entry.getAlias(), keystorePassword);
        } catch (KeystoreControllerException e){
            fullKeyEntry = entry;
        }
        
        final DefaultMutableTreeNode entryNode = new DefaultMutableTreeNode(fullKeyEntry);
        final DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)treeModel.getRoot();
        treeModel.insertNodeInto(entryNode, rootNode, treeModel.getChildCount(rootNode));
    }

    private void applyKeyStoreEntryList(){
        if (treeModel==null){
            showError(bundle.getString("TreeModelIsNull"), "applyKeyStoreEntryList");
            return;
        }
        jTreeKeyStoreEntries.setModel(treeModel);
    }

    private void showError(final String message, final Throwable cause, final String functionName){
        showError(message+"\n"+bundle.getString("Cause")+cause.toString(), functionName);
    }

    private void showError(final String message, final String functionName){
        showError(message/*+"\nSource: "+functionName*/);
    }

    private void showError(final String message){
        JOptionPane.showMessageDialog(getFrame(), message, bundle.getString("ErrorMessageTitle"), JOptionPane.ERROR_MESSAGE);
    }

    private void setWait(final boolean on){
        if (on)
            mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        else
            mainPanel.setCursor(Cursor.getDefaultCursor());
    }

    @Action
    public void setupDistinguishedName(){
        setWait(true);
        final DistinguishedNameDialog dialog = new DistinguishedNameDialog(getFrame(), CN, OU, O, L, ST, C, UID, showDnDialogEachTime);
        dialog.setLocationRelativeTo(getFrame());
        dialog.setVisible(true);
        if (dialog.getReturnStatus()!=DistinguishedNameDialog.RET_OK){
            setWait(false);
            return;
        }
        CN = dialog.getCN();
        OU = dialog.getOU();
        O = dialog.getO();
        L = dialog.getL();
        ST = dialog.getST();
        C = dialog.getC();
        UID = dialog.getUID();
        showDnDialogEachTime = dialog.getShowEachTime();
        displayStatus(bundle.getString("Status.DistinguishedNameSet"));
        setWait(false);
    }

    @Action (enabledProperty = "keystoreOpened")
    public void generateKeyPair(){
        checkDistinguishedNameDialogRequired();
        setWait(true);
        final String[] aliases;
        try{
            aliases = keystoreController.getAliases();
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.GetAliases"), e, "generateKeyPair");
            setWait(false);
            return;
        }
        final GenerateKeyPairDialog dialog = new GenerateKeyPairDialog(getFrame(), aliases, defaultSelfCertificateDurationDays);
        dialog.setLocationRelativeTo(getFrame());
        dialog.setVisible(true);
        if (dialog.getReturnStatus()!=GenerateKeyPairDialog.RET_OK){
            setWait(false);
            return;
        }
        final String alias = dialog.getAlias();
        final int keyLength = dialog.getKeyLength();
        final int publicExponent = dialog.getPublicExponent();
        final int durationDays = dialog.getDuration();
        defaultSelfCertificateDurationDays = durationDays;
        final Date expiration = new Date(System.currentTimeMillis()+(long)1000*3600*24*durationDays);
        try{
            final KeystoreRsaKeyEntry entry = CertificateUtils.generateAndSignKey(keyLength, publicExponent, buildDistinguishedName(), expiration);
            keystoreController.storeRsaKey(entry, alias, keystorePassword);
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.StoreKey"), e, "generateKeyPair");
            setWait(false);
            return;
        } catch (CertificateUtilsException e){
            showError(bundle.getString("Error.GenerateAndSignKey"), e, "generateKeyPair");
            setWait(false);
            return;
        }
        displayKeyStoreEntries();
        setKeystoreChanged(true);
        displayStatus(bundle.getString("Status.KeyPairGenerated"));
        setWait(false);
    }

    @Action (enabledProperty = "keyEntryPresent")
    public void prepareCertificateRequest(){
        setWait(true);
        final String[] aliases;
        try{
            aliases = keystoreController.getRsaKeyAliases();
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.GetKeyAliases"), e, "prepareCertificateRequest");
            setWait(false);
            return;
        }
        final String defaultAlias = (selectedEntry!=null ? selectedEntry.getAlias() : "");
        final CertificateRequestDialog dialog = new CertificateRequestDialog(getFrame(), CertificateRequestDialog.TYPE_REQUEST, aliases, defaultAlias, defaultCertificateSigningRequestPath);
        dialog.setLocationRelativeTo(getFrame());
        dialog.setVisible(true);
        if (dialog.getReturnStatus()!=CertificateRequestDialog.RET_OK){
            setWait(false);
            return;
        }
        final String alias = dialog.getAlias();
        final String filePath = dialog.getFilePath();
        final File selectedFile = new File(filePath);
        defaultCertificateSigningRequestPath = selectedFile.getParent();
        try{
            KeystoreRsaKeyEntry keyEntry = keystoreController.getKeystoreRsaKeyEntry(alias, keystorePassword);
            CertificateUtils.requestCertificate(keyEntry, filePath, keystoreController.getSecurityProviderName());
        } catch (CertificateUtilsException e){
            showError(bundle.getString("Error.RequestCertificate"), e, "prepareCertificateRequest");
            setWait(false);
            return;
        } catch (KeystoreControllerException e){
            showError(String.format(bundle.getString("Error.GetKeyEntry"), alias), e, "prepareCertificateRequest");
            setWait(false);
            return;
        }
        displayStatus(bundle.getString("Status.CertificateRequestCreated"));
        setWait(false);
    }

    @Action (enabledProperty = "keyEntryPresent")
    public void readCertificateResponse(){
        setWait(true);
        final String[] aliases;
        try{
            aliases = keystoreController.getRsaKeyAliases();
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.GetKeyAliases"), e, "readCertificateResponse");
            setWait(false);
            return;
        }
        final String defaultAlias = (selectedEntry!=null ? selectedEntry.getAlias() : "");
        final CertificateRequestDialog dialog = new CertificateRequestDialog(getFrame(), CertificateRequestDialog.TYPE_RESPONSE, aliases, defaultAlias, defaultSignedCertificatePath);
        dialog.setLocationRelativeTo(getFrame());
        dialog.setVisible(true);
        if (dialog.getReturnStatus()!=CertificateRequestDialog.RET_OK){
            setWait(false);
            return;
        }
        final String alias = dialog.getAlias();
        final String filePath = dialog.getFilePath();
        final File selectedFile = new File(filePath);
        defaultSignedCertificatePath = selectedFile.getParent();
        try{
            final X509Certificate[] chain = CertificateUtils.readCertificateChain(filePath);
            String certificateInfo = "";
            for (X509Certificate certificate : chain){
                final String subjectDN = certificate.getSubjectDN().toString();
                final String issuerDN = certificate.getIssuerDN().toString();
                final String fingerprintMD5 = CertificateUtils.getCertificateFingerprintAsString(CertificateUtils.ALGORITHM_MD5, certificate);
                final String fingerprintSHA1 = CertificateUtils.getCertificateFingerprintAsString(CertificateUtils.ALGORITHM_SHA1, certificate);
                if (certificateInfo.length()==0)
                    certificateInfo += bundle.getString("OwnCertificate");
                else
                    certificateInfo += bundle.getString("CACertificate");
                certificateInfo += String.format(bundle.getString("CertificateInfo"), subjectDN, issuerDN, fingerprintMD5, fingerprintSHA1);
            }
            if (!confirm("ConfirmCertificateChainTrusted", "VerifyCertificateChain", certificateInfo)){
                setWait(false);
                return;
            }
            keystoreController.storeCertificateChain(chain, alias, keystorePassword);
        } catch (CertificateUtilsException e){
            showError(bundle.getString("Error.ReadCertificateChain"), e, "readCertificateResponse");
            setWait(false);
            return;
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.StoreCertificateChain"), e, "readCertificateResponse");
            setWait(false);
            return;
        }
        displayKeyStoreEntries();
        setKeystoreChanged(true);
        displayStatus(bundle.getString("Status.CertificateReceived"));
        setWait(false);
    }

    @Action (enabledProperty = "keyEntryPresent")
    public void signClientCertificate(){
        setWait(true);
        final String[] aliases;
        try{
            aliases = keystoreController.getRsaKeyAliases();
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.GetKeyAliases"), e, "signClientCertificate");
            setWait(false);
            return;
        }
        final String defaultAlias = (selectedEntry!=null ? selectedEntry.getAlias() : "");
        final SignCertificateDialog dialog = new SignCertificateDialog(getFrame(), aliases, defaultAlias, defaultCertificateSigningRequestPath, defaultSignedCertificatePath, defaultCertificateDurationDays);
        dialog.setLocationRelativeTo(getFrame());
        dialog.setVisible(true);
        if (dialog.getReturnStatus()!=SignCertificateDialog.RET_OK){
            setWait(false);
            return;
        }
        final String alias = dialog.getAlias();
        final String requestFilePath = dialog.getRequestFilePath();
        final File requestFile = new File(requestFilePath);
        defaultCertificateSigningRequestPath = requestFile.getParent();
        final String certificateFilePath = dialog.getCertificateFilePath();
        final File certificateFile = new File(certificateFilePath);
        defaultSignedCertificatePath = certificateFile.getParent();
        final int durationDays = dialog.getDurationDays();
        final Date expirationDate = new Date(System.currentTimeMillis()+(long)1000*3600*24*durationDays);
        defaultCertificateDurationDays = durationDays;
        try{
            KeystoreRsaKeyEntry issuerKeyEntry = keystoreController.getKeystoreRsaKeyEntry(alias, keystorePassword);
            CertificateUtils.processCertificateRequest(issuerKeyEntry, requestFilePath, expirationDate, certificateFilePath);
        } catch (CertificateUtilsException e){
            showError(bundle.getString("Error.ProcessCertificateRequest"), e, "signClientCertificate");
            setWait(false);
            return;
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.GetKeyEntry"), e, "signClientCertificate");
            setWait(false);
            return;
        }

        displayStatus(bundle.getString("Status.ClientCertificateSigned"));
        setWait(false);
    }

    @Action (enabledProperty = "keystoreOpened")
    public void loadTrustedCertificate(){
        setWait(true);
        final String[] aliases;
        try{
            aliases = keystoreController.getAliases();
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.GetAliases"), e, "loadTrustedCertificate");
            setWait(false);
            return;
        }
        final LoadTrustedCertificateDialog dialog = new LoadTrustedCertificateDialog(getFrame(), aliases, defaultTrustedCertificatePath);
        dialog.setLocationRelativeTo(getFrame());
        dialog.setVisible(true);
        if (dialog.getReturnStatus()!=LoadTrustedCertificateDialog.RET_OK){
            setWait(false);
            return;
        }
        final String alias = dialog.getAlias();
        final String certificateFilePath = dialog.getCertificateFilePath();
        final File certificateFile = new File(certificateFilePath);
        defaultTrustedCertificatePath = certificateFile.getParent();
        final X509Certificate certificate;
        try{
            certificate = CertificateUtils.readTrustedCertificate(certificateFilePath);
            final String subjectDN = certificate.getSubjectDN().toString();
            final String issuerDN = certificate.getIssuerDN().toString();
            final String serialNumber = certificate.getSerialNumber().toString();
            final String validFrom = certificate.getNotBefore().toString();
            final String validUntil = certificate.getNotAfter().toString();
            final String fingerprintMD5 = CertificateUtils.getCertificateFingerprintAsString(CertificateUtils.ALGORITHM_MD5, certificate);
            final String fingerprintSHA1 = CertificateUtils.getCertificateFingerprintAsString(CertificateUtils.ALGORITHM_SHA1, certificate);
            if (!confirm("ConfirmCertificateTrusted", "VerifyCertificate", subjectDN, issuerDN, serialNumber, validFrom, validUntil, fingerprintMD5, fingerprintSHA1)){
                setWait(false);
                return;
            }
            keystoreController.storeTrustedCertificate(alias, certificate);
        } catch (CertificateUtilsException e){
            showError(bundle.getString("Error.ReadTrustedCertificate"), e, "loadTrustedCertificate");
            setWait(false);
            return;
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.StoreTrustedCertificate"), e, "loadTrustedCertificate");
            setWait(false);
            return;
        }
        displayKeyStoreEntries();
        setKeystoreChanged(true);
        displayStatus(bundle.getString("Status.TrustedCertificateLoaded"));
        setWait(false);
    }

    @Action (enabledProperty = "certificateEntryPresent")
    public void exportCertificate(){
        setWait(true);
        final String[] aliases;
        try{
            aliases = keystoreController.getAliases();
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.GetAliases"), e, "exportCertificate");
            setWait(false);
            return;
        }
        final String defaultAlias = (selectedEntry!=null ? selectedEntry.getAlias() : "");
        final ExportCertificateDialog dialog = new ExportCertificateDialog(getFrame(), aliases, defaultAlias, defaultCertificateExportPath);
        dialog.setLocationRelativeTo(getFrame());
        dialog.setVisible(true);
        if (dialog.getReturnStatus()!=ExportCertificateDialog.RET_OK){
            setWait(false);
            return;
        }
        final String alias = dialog.getAlias();
        final String certificateFilePath = dialog.getCertificateFilePath();
        final File certificateFile = new File(certificateFilePath);
        defaultCertificateExportPath = certificateFile.getParent();
        try{
            X509Certificate certificate = keystoreController.getCertificate(alias);
            if (certificate==null){
                showError(String.format(bundle.getString("Error.CertificateIsNullForAlias"), alias), "exportCertificate");
                return;
            }
            CertificateUtils.exportCertificate(certificate, certificateFilePath);
        } catch (CertificateUtilsException e){
            showError(bundle.getString("Error.ExportCertificate"), e, "exportCertificate");
            return;
        } catch (KeystoreControllerException e){
            showError(String.format(bundle.getString("Error.GetCertificate"), alias), e, "exportCertificate");
            return;
        } finally{
            setWait(false);
        }
        displayStatus(bundle.getString("Status.CertificateExported"));
    }

    @Action (enabledProperty = "keyExportAllowed")
    public void exportKeyPair(){
        setWait(true);
        final String[] aliasList;
        try{
            aliasList = keystoreController.getRsaKeyAliases();
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.GetKeyAliases"), e, "exportKeyPair");
            setWait(false);
            return;
        }
        final String defaultAlias = (selectedEntry!=null ? selectedEntry.getAlias() : "");
        final KeyPairExchangeDialog dialog = new KeyPairExchangeDialog(getFrame(), KeyPairExchangeDialog.TYPE_EXPORT, aliasList, defaultAlias, null, defaultPKCS12Path);
        dialog.setLocationRelativeTo(getFrame());
        dialog.setVisible(true);
        if (dialog.getReturnStatus()!=KeyPairExchangeDialog.RET_OK){
            setWait(false);
            return;
        }
        final String alias = dialog.getAlias();
        final String outerKeyStorePath = dialog.getOuterKeyStorePath();
        final File outerKeyStoreFile = new File(outerKeyStorePath);
        defaultPKCS12Path = outerKeyStoreFile.getParent();
        char[] outerKeyStorePassword = dialog.getOuterKeyStorePassword();
        try{
            keystoreController.exportRsaKeyToPkcs12(alias, keystorePassword, outerKeyStorePath, outerKeyStorePassword);
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.ExportKeyToPKCS12"), e, "exportKeyPair");
            setWait(false);
            return;
        }
        displayStatus(bundle.getString("Status.KeyPairExported"));
        setWait(false);
    }

    @Action (enabledProperty = "keystoreOpened")
    public void generateAndExportKeyPair(){
        checkDistinguishedNameDialogRequired();
        setWait(true);
        final String[] aliases;
        try{
            aliases = keystoreController.getAliases();
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.GetAliases"), e, "generateAndExportKeyPair");
            setWait(false);
            return;
        }
        //keypair generation dialog
        final GenerateKeyPairDialog generateKeyPairDialog = new GenerateKeyPairDialog(getFrame(), aliases, defaultSelfCertificateDurationDays);
        generateKeyPairDialog.setLocationRelativeTo(getFrame());
        generateKeyPairDialog.setVisible(true);
        if (generateKeyPairDialog.getReturnStatus()!=GenerateKeyPairDialog.RET_OK){
            setWait(false);
            return;
        }
        final String alias = generateKeyPairDialog.getAlias();
        final int keyLength = generateKeyPairDialog.getKeyLength();
        final int publicExponent = generateKeyPairDialog.getPublicExponent();
        final int durationDays = generateKeyPairDialog.getDuration();
        defaultSelfCertificateDurationDays = durationDays;

        //keypair export dialog
        final String defaultAlias = alias;
        final KeyPairExchangeDialog exportKeyPairDialog = new KeyPairExchangeDialog(getFrame(), KeyPairExchangeDialog.TYPE_GENERATE_AND_EXPORT, aliases, defaultAlias, keystorePassword, defaultPKCS12Path);
        exportKeyPairDialog.setLocationRelativeTo(getFrame());
        exportKeyPairDialog.setVisible(true);
        if (exportKeyPairDialog.getReturnStatus()!=KeyPairExchangeDialog.RET_OK){
            setWait(false);
            return;
        }
        final String outerKeyStorePath = exportKeyPairDialog.getOuterKeyStorePath();
        final File outerKeyStoreFile = new File(outerKeyStorePath);
        defaultPKCS12Path = outerKeyStoreFile.getParent();
        final char[] outerKeyStorePassword = exportKeyPairDialog.getOuterKeyStorePassword();

        try{
            final Date expiration = new Date(System.currentTimeMillis()+(long)1000*3600*24*durationDays);
            final KeystoreRsaKeyEntry entry = CertificateUtils.generateAndSignKey(keyLength, publicExponent, buildDistinguishedName(), expiration);
            keystoreController.storeRsaKey(entry, alias, keystorePassword);
        } catch (CertificateUtilsException e){
            showError(bundle.getString("Error.GenerateAndSignKey"), e, "generateAndExportKeyPair");
            setWait(false);
            return;
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.StoreKey"), e, "generateAndExportKeyPair");
            setWait(false);
            return;
        }

        try{
            keystoreController.exportRsaKeyToPkcs12(alias, keystorePassword, outerKeyStorePath, outerKeyStorePassword);
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.ExportKeyToPKCS12"), e, "generateAndExportKeyPair");
            setWait(false);
            return;
        }

        displayKeyStoreEntries();
        setKeystoreChanged(true);
        displayStatus(bundle.getString("Status.KeyPairGeneratedAndExported"));
        setWait(false);
    }

    @Action (enabledProperty = "keystoreOpened")
    public void importKeyPair(){
        setWait(true);
        final String[] aliasList;
        try{
            aliasList = keystoreController.getAliases();
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.GetAliases"), e, "importKeyPair");
            setWait(false);
            return;
        }
        final String defaultAlias = (selectedEntry!=null ? selectedEntry.getAlias() : "");
        final KeyPairExchangeDialog dialog = new KeyPairExchangeDialog(getFrame(), KeyPairExchangeDialog.TYPE_IMPORT, aliasList, defaultAlias, null, defaultPKCS12Path);
        dialog.setLocationRelativeTo(getFrame());
        dialog.setVisible(true);
        if (dialog.getReturnStatus()!=KeyPairExchangeDialog.RET_OK){
            setWait(false);
            return;
        }
        final String alias = dialog.getAlias();
        final String outerKeyStorePath = dialog.getOuterKeyStorePath();
        final File outerKeyStoreFile = new File(outerKeyStorePath);
        defaultPKCS12Path = outerKeyStoreFile.getParent();
        final char[] outerKeyStorePassword = dialog.getOuterKeyStorePassword();
        try{
            final KeystoreRsaKeyEntry entry = KeystoreController.readRsaKeyFromPkcs12(outerKeyStorePath, outerKeyStorePassword);
            keystoreController.storeRsaKey(entry, alias, keystorePassword);
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.StoreKey"), e, "importKeyPair");
            setWait(false);
            return;
        }
        displayKeyStoreEntries();
        setKeystoreChanged(true);
        displayStatus(bundle.getString("Status.KeyPairImported"));
        setWait(false);
    }

    @Action (enabledProperty = "entryPresent")
    public void deleteEntry(){
        setWait(true);
        final String[] aliases;
        try{
            aliases = keystoreController.getAliases();
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.GetAliases"), e, "deleteEntry");
            setWait(false);
            return;
        }
        final String defaultAlias;
        if (selectedEntry!=null)
            defaultAlias = selectedEntry.getAlias();
        else
            defaultAlias = (aliases.length>0 ? aliases[0] : "");
        final String alias = (String)JOptionPane.showInputDialog(getFrame(),
                bundle.getString("AliasToBeDeleted"), bundle.getString("DeleteEntry"),
                JOptionPane.PLAIN_MESSAGE, null, aliases, defaultAlias);
        if (alias==null || alias.length()==0 || !confirm("ConfirmDeleteEntry", "DeleteEntry", alias)){
            setWait(false);
            return;
        }
        try{
            keystoreController.deleteEntry(alias);
        } catch (KeystoreControllerException e){
            showError(String.format(bundle.getString("Error.DeleteEntry"), alias), e, "deleteEntry");
            setWait(false);
            return;
        }
        displayKeyStoreEntries();
        setKeystoreChanged(true);
        displayStatus(String.format(bundle.getString("Status.EntryDeleted"), alias));
        setWait(false);
    }
    
    @Action (enabledProperty = "keystoreOpened")
    public void generateDesKey(){
        setWait(true);
        final String[] aliases;
        try{
            aliases = keystoreController.getAliases();
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.GetAliases"), e, "generateDesKey");
            setWait(false);
            return;
        }
        final GenerateDesKeyDialog dialog = new GenerateDesKeyDialog(getFrame(), aliases);
        dialog.setLocationRelativeTo(getFrame());
        dialog.setVisible(true);
        if (dialog.getReturnStatus()!=GenerateKeyPairDialog.RET_OK){
            setWait(false);
            return;
        }
        final String alias = dialog.getAlias();
        final Crypto.DesKeyLength keyLength = Crypto.DesKeyLength.DOUBLE; //add "key length" setting to GenerateDesKeyDialog if needed
        final SecretKey key;
        final byte[] checkValue;
        try{
            key = Crypto.generateDesKey(keyLength);
            checkValue = Crypto.calc3desKeyCheckValue(key);
        } catch (KeyStoreAdminException e){
            showError(bundle.getString("Error.GenerateDesKey"), e, "generateDesKey");
            setWait(false);
            return;
        }
        
        try{
            final KeystoreDesKeyEntry entry = new KeystoreDesKeyEntry(alias, key);
            keystoreController.storeDesKey(entry, alias, keystorePassword);
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.StoreKey"), e, "generateDesKey");
            setWait(false);
            return;
        }
        displayKeyStoreEntries();
        setKeystoreChanged(true);
        
        final String sCheckValue = Hex.encode(checkValue).substring(0, 6);
        displayStatus(String.format(bundle.getString("Status.DesKeyGenerated"), alias, sCheckValue));
        setWait(false);
    }
    
    @Action (enabledProperty = "keystoreOpened")
    public void importDesKey(){
        setWait(true);
        final String[] aliases;
        try{
            aliases = keystoreController.getAliases();
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.GetAliases"), e, "importDesKey");
            setWait(false);
            return;
        }
        final int expectedComponentLength = 16;
        final byte[] keyData = new byte[expectedComponentLength];
        Arrays.fill(keyData, (byte)0);
        int componentCount = 0;
        InputDesKeyComponentDialog componentDialog;
        do{
            componentCount++;
            componentDialog = new InputDesKeyComponentDialog(getFrame(), componentCount);
            componentDialog.setLocationRelativeTo(getFrame());
            componentDialog.setVisible(true);
            if (componentDialog.getReturnStatus()==InputDesKeyComponentDialog.RET_CANCEL){
                setWait(false);
                return;
            }
            final byte[] component = componentDialog.getComponent();
            if (component.length!=expectedComponentLength){
                showError(String.format(bundle.getString("Error.InvalidKeyComponentLength"), component.length, expectedComponentLength), "importDesKey");
                setWait(false);
                return;
            }
            for (int i=0; i<expectedComponentLength; i++)
                keyData[i] ^= component[i];
        } while (componentDialog.getReturnStatus()==InputDesKeyComponentDialog.RET_NEXT_COMPONENT);
        
        final byte[] checkValue;
        try{
            checkValue = Crypto.calc3desKeyCheckValue(keyData);
        } catch (KeyStoreAdminException e){
            showError(bundle.getString("Error.CalculateKeyCheckValue"), e, "importDesKey");
            setWait(false);
            return;
        }
        final String sCheckValue = Hex.encode(checkValue).substring(0, 6);
        final ImportDesKeyDialog dialog = new ImportDesKeyDialog(getFrame(), aliases, sCheckValue);
        dialog.setLocationRelativeTo(getFrame());
        dialog.setVisible(true);
        if (dialog.getReturnStatus()!=GenerateKeyPairDialog.RET_OK){
            setWait(false);
            return;
        }
        final String alias = dialog.getAlias();
        final SecretKey key;
        try{
            key = Crypto.build3DesKey(keyData);
        } catch (KeyStoreAdminException e){
            showError(bundle.getString("Error.BuildDesKey"), e, "importDesKey");
            setWait(false);
            return;
        }
        try{    
            final KeystoreDesKeyEntry entry = new KeystoreDesKeyEntry(alias, key);
            keystoreController.storeDesKey(entry, alias, keystorePassword);
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.StoreKey"), e, "importDesKey");
            setWait(false);
            return;
        }
        displayKeyStoreEntries();
        setKeystoreChanged(true);
        displayStatus(String.format(bundle.getString("Status.DesKeyImported"), alias, sCheckValue));
        setWait(false);
    }

    private void cancelKeystoreChanges(boolean closeOnFail){
        try{
            keystoreController = new KeystoreController(keystorePath, keystorePassword);
            displayKeyStoreEntries();
            setKeystoreChanged(false);
            displayStatus(bundle.getString("Status.FileKeystoreReopened"));
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.CancelKeystoreChanges")+(closeOnFail ? "\n"+bundle.getString("ApplicationWillBeClosed") : ""),
                      e, "cancelKeystoreChanges");
            if (closeOnFail){
                setKeystoreChanged(false);
                closeApplication();
            }
        }
    }

    @Action (enabledProperty = "keystorePasswordChangeAllowed")
    public void changeKeystorePassword(){
        if (isKeystoreChanged() && !confirm("ConfirmSaveBeforeContinue", "ChangeKeystorePassword"))
            return;

        final ChangePasswordDialog dialog = new ChangePasswordDialog(getFrame(), bundle.getString("ChangeKeystorePassword"));
        dialog.setLocationRelativeTo(getFrame());
        dialog.setVisible(true);
        if (dialog.getReturnStatus()!=KeyPairExchangeDialog.RET_OK)
            return;

        final char[] newKeystorePassword = dialog.getNewPassword();

        String currentAlias = null;
        try{
            final String[] rsaKeyAliases = keystoreController.getRsaKeyAliases();
            for (String alias : rsaKeyAliases){
                currentAlias = alias;
                doChangeRsaKeyPassword(alias, keystorePassword, newKeystorePassword);
            }
            final String[] desKeyAliases = keystoreController.getDesKeyAliases();
            for (String alias : desKeyAliases){
                currentAlias = alias;
                doChangeDesKeyPassword(alias, keystorePassword, newKeystorePassword);
            }
        } catch (KeystoreControllerException e){
            showError(currentAlias==null ? bundle.getString("Error.GetKeyAliases") :
                                           String.format(bundle.getString("Error.ChangeKeyPassword"), currentAlias),
                      e, "changeKeystorePassword");
            //if passwords of some keys have not been changed, cancel changes (reread keystore)
            //if keystore can't be reread, close the application to avoid inconsistent state of passwords
            cancelKeystoreChanges(true);
            return;
        }

        keystorePassword = newKeystorePassword;
        saveKeyStoreToFile();
        displayStatus(bundle.getString("Status.KeystorePasswordChanged"));
    }

    private void doChangeRsaKeyPassword(final String alias, final char[] password, final char[] newPassword) throws KeystoreControllerException{
        KeystoreRsaKeyEntry entry = keystoreController.getKeystoreRsaKeyEntry(alias, password);
        keystoreController.storeRsaKey(entry, alias, newPassword);
    }
    
    private void doChangeDesKeyPassword(final String alias, final char[] password, final char[] newPassword) throws KeystoreControllerException{
        KeystoreDesKeyEntry entry = keystoreController.getKeystoreDesKeyEntry(alias, password);
        keystoreController.storeDesKey(entry, alias, newPassword);
    }

    //action is obsolete: RADIX-4655
    /*@Action (enabledProperty = "keyExportAllowed")
    public void changeKeyPassword(){
        final String[] aliases;
        try{
            aliases = keystoreController.getRsaKeyAliases();
        } catch (KeystoreControllerException e){
            showError(bundle.getString("Error.GetKeyAliases"), e, "changeKeyPassword");
            return;
        }
        final String defaultAlias = (selectedEntry!=null ? selectedEntry.getAlias() : "");

        final ChangeKeyPasswordDialog dialog = new ChangeKeyPasswordDialog(getFrame(), aliases, defaultAlias);
        dialog.setLocationRelativeTo(getFrame());
        dialog.setVisible(true);
        if (dialog.getReturnStatus()!=KeyPairExchangeDialog.RET_OK)
            return;

        String alias = dialog.getAlias();
        char[] password = dialog.getOldPassword();
        char[] newPassword = dialog.getNewPassword();
        try{
            doChangeRsaKeyPassword(alias, password, newPassword);
        } catch (KeystoreControllerException e){
            showError(String.format(bundle.getString("Error.ChangeKeyPassword"), alias), e, "changeKeyPassword");
            return;
        }
        setKeystoreChanged(true);
        displayStatus(String.format(bundle.getString("Status.KeyPasswordChanged"), alias));
    }*/

    private class PasswordCallbackHandler implements CallbackHandler{
        @Override
        public void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException{
            for (int i=0; i<callbacks.length; i++){
                if (callbacks[i] instanceof javax.security.auth.callback.PasswordCallback){
                    //prompt the user for sensitive information
                    final PasswordCallback passwordCallback = (PasswordCallback)callbacks[i];
                    final PasswordDialog dialog = new PasswordDialog(getFrame(), PasswordDialog.TYPE_PKCS11KEYSTORE, passwordCallback.getPrompt());
                    dialog.setLocationRelativeTo(getFrame());
                    dialog.setVisible(true);
                    if (dialog.getReturnStatus()!=PasswordDialog.RET_OK)
                        return;
                    pkcs11KeyStorePin = dialog.getPassword();
                    passwordCallback.setPassword(pkcs11KeyStorePin);
                } else{
                    throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
                }
            }
        }
    }
}