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

package org.radixware.kernel.explorer.dialogs.certificates;

import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.gui.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import javax.security.auth.x500.X500Principal;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.eas.connections.Pkcs11Config;
import org.radixware.kernel.common.client.eas.connections.Pkcs11SlotDetector;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ITaskWaiter;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.FileException;
import org.radixware.kernel.common.client.exceptions.Pkcs11Exception;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EKeyStoreType;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.ssl.*;
import org.radixware.kernel.explorer.dialogs.EnterPasswordDialog;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.common.client.utils.Pkcs11Token;
import org.radixware.kernel.explorer.dialogs.pkcs11.SelectPkcs11TokenDialog;


public class CertificateManager extends ExplorerDialog {

    static class Icons extends ClientIcon {
        public Icons(final String path, final boolean useSvg) {
            super(path, useSvg);
        }
        
        public final static Icons CERTIFICATE = new Icons("classpath:images/certificate.svg", true);
        public final static Icons TRUSTCERT = new Icons("classpath:images/certificate_manager.svg", true);
        public final static Icons GENERATE = new Icons("classpath:images/cert_generate.png", false);
        public final static Icons REQUEST = new Icons("classpath:images/cert_request.png", false);
        public final static Icons RECEIVE = new Icons("classpath:images/cert_receive.png", false);
        public final static Icons IMPORT_CERT = new Icons("classpath:images/import_cert.png", false);
        public final static Icons IMPORT_KEY = new Icons("classpath:images/import_key.svg", true);
        public final static Icons DELETE = new Icons("classpath:images/cert_delete.png", false);
        public final static Icons SIGN = new Icons("classpath:images/cert_sign.png", false);
    }
    
    private final static int PERSONAL = 0;
    private final static int TRUSTED = 1;
    private final static int COLUMNS = 5;
    private final static char[] PASSWORD = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3', '4'};
    private boolean empty = true;
    private boolean certTreeHasFocus = false;
    private volatile KeystoreController personalStore = null;
    private volatile KeystoreController trustStore = null;
    private final List<KeystoreEntry> personals = new LinkedList<>();
    private final List<KeystoreEntry> trusteds = new LinkedList<>();
    private volatile String keyAlias = "org.radixware/explorer/key";
    private final ConnectionOptions connection;
    
    private final QAction generateKeyAction;
    private final QAction requestSignAction;
    private final QAction receiveCertAction;
    private final QAction importKeyAction;
    private final QAction importCertAction;
    private final QAction deleteKeyAction;
    
    private QTreeWidget tree;
    private QListWidget list;
    private final boolean isPkcs11;
    private char[] password;
    
    public static CertificateManager newCertificateManager(final IClientEnvironment environment, final QWidget parent, final ConnectionOptions connection) throws KeystoreControllerException, InterruptedException{
        return newCertificateManager(environment, parent, connection, null);
    }
    
    @SuppressWarnings({"null", "ConstantConditions"})
    public static CertificateManager newCertificateManager(final IClientEnvironment environment, final QWidget parent, final ConnectionOptions connection, final String password) throws KeystoreControllerException, InterruptedException{
        final ConnectionOptions.SslOptions sslOptions = connection.getSslOptions();
        final boolean isPKCS11 = sslOptions!=null && sslOptions.getKeyStoreType() == EKeyStoreType.PKCS11;
        final String ksPassword;
        if(isPKCS11) {
            if (password==null || password.isEmpty()){
                final EnterPasswordDialog pwdDialog = new EnterPasswordDialog(environment, parent);
                pwdDialog.setMessage(environment.getMessageProvider().translate("PKCS11", "Enter password for HSM access:"));
                if(pwdDialog.execDialog() != DialogResult.ACCEPTED) {
                    throw new InterruptedException();
                }else{
                    ksPassword = pwdDialog.getPassword();
                }
            }else{
                ksPassword = password;
            }
            if (sslOptions.isAutoDetectSlotId()){
                final Pkcs11Config pkcs11Config = connection.getPkcs11Config();
                Integer actualSlotIndex = 
                    Pkcs11SlotDetector.findSlotIndexByCertAlias(environment, pkcs11Config, sslOptions.getCertificateAlias(), ksPassword.toCharArray(), true);
                if (actualSlotIndex==null){
                    final String pkcs11Lib = pkcs11Config.getFieldValue(Pkcs11Config.Field.LIBPATH);
                    final Pkcs11Token selectedToken = SelectPkcs11TokenDialog.selectToken(environment, pkcs11Lib, parent);
                    if (selectedToken==null){
                        throw new InterruptedException();
                    }
                    actualSlotIndex = Integer.valueOf((int)selectedToken.getSlotId());
                }
                if (actualSlotIndex.intValue()!=sslOptions.getSlotId()){
                    pkcs11Config.setFieldValue(Pkcs11Config.Field.SLOTLI, actualSlotIndex.toString());
                    connection.setPkcs11Config(pkcs11Config);
                }
            }
        }else{
            ksPassword = null;
        }
        final CertificateManager certManager = new CertificateManager(environment, parent, connection);
        certManager.reload(ksPassword==null ? null : ksPassword.toCharArray());
        return certManager;
    }
    
    @SuppressWarnings("LeakingThisInConstructor")
    private CertificateManager(final IClientEnvironment environment, 
                               final QWidget parent, 
                               final ConnectionOptions connection) {
        super(environment, parent);
        final MessageProvider provider = environment.getMessageProvider();
        setAttribute(WidgetAttribute.WA_DeleteOnClose);
        setWindowTitle(provider.translate("CertificateManagerDialog", "Certificate Manager"));
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Connection.CERTIFICATE_MANAGER));
        this.connection = connection;
        this.isPkcs11 = connection.getSslOptions() != null && connection.getSslOptions().getKeyStoreType() == EKeyStoreType.PKCS11;
        generateKeyAction = new QAction(
                ExplorerIcon.getQIcon(Icons.GENERATE),
                provider.translate("CertificateManagerDialog", "Generate key pair"),
                null);
        generateKeyAction.triggered.connect(this, "generateKeypair()");
        requestSignAction = new QAction(
                ExplorerIcon.getQIcon(Icons.REQUEST),
                provider.translate("CertificateManagerDialog", "Request signing"), 
                null);
        requestSignAction.triggered.connect(this, "requestKeyCertificate()");
        receiveCertAction = new QAction(
                ExplorerIcon.getQIcon(Icons.RECEIVE),
                provider.translate("CertificateManagerDialog", "Receive certificate"),
                null);
        receiveCertAction.triggered.connect(this, "receiveCertificate()");
        importCertAction = new QAction(
                ExplorerIcon.getQIcon(Icons.IMPORT_CERT),
                provider.translate("CertificateManagerDialog", "Import trusted certificate"),
                null);
        importCertAction.triggered.connect(this, "importTrustedCertificate()");
        importKeyAction = new QAction(
                ExplorerIcon.getQIcon(Icons.IMPORT_KEY),
                provider.translate("CertificateManagerDialog", "Import personal certificate and RSA key from PKCS#12 keystore"),
                null);
        importKeyAction.triggered.connect(this, "importKey()");
        deleteKeyAction = new QAction(
                ExplorerIcon.getQIcon(Icons.DELETE),
                provider.translate("CertificateManagerDialog", "Delete entry"),
                null);
        deleteKeyAction.triggered.connect(this, "deleteKey()");
       
        setUpTree(environment);
        setUpUi();
    }
    
    private void reload(final char[] ksPassword) throws InterruptedException, Pkcs11Exception, KeystoreControllerException{
        closeStores();
        final ITaskWaiter taskWaiter = getEnvironment().getApplication().newTaskWaiter();
        try {
            taskWaiter.setMessage(getEnvironment().getMessageProvider().translate("CertificateManagerDialog", "Reading Certificates"));
            taskWaiter.runAndWait(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    load(ksPassword);
                    return (Void)null;
                }
            });
        } catch (ExecutionException ex) {
            if (isPkcs11){
                throw new Pkcs11Exception(ex);
            }else{
                final String message = 
                    getEnvironment().getMessageProvider().translate("CertificateManagerDialog", "Unable to read certificates");
                throw new KeystoreControllerException(message, ex);
            }
        }finally{
            taskWaiter.close();
        }
        if (isPkcs11){
            password = ksPassword;
        }
        rereadKeystore();
    }
     
    private void load(char[] ksPassword) throws KeystoreControllerException {
        try {
            if(isPkcs11) {
                keyAlias = connection.getSslOptions().getCertificateAlias();
                if (ksPassword == null) ksPassword = new char[]{};
                personalStore = KeystoreController.newClientInstance(
                    getEnvironment().getWorkPath(),
                    connection.getId().toString(),
                    connection.getSslOptions().getKeyStoreType(),
                    ksPassword/*,
                    0*/);
                trustStore = KeystoreController.newClientInstance(
                        getEnvironment().getWorkPath(),
                        connection.getId().toString(),
                        EKeyStoreType.FILE,
                        PASSWORD/*,
                        0*/);
            } else {
                personalStore = KeystoreController.newClientInstance(
                        getEnvironment().getWorkPath(),
                        connection.getId().toString(),
                        EKeyStoreType.FILE,
                        PASSWORD/*,
                        0*/);
                // all objects are stored in the same file
                trustStore = personalStore;
            }
                       
        } catch (KeystoreControllerException ex) {
            closeStores();
            throw ex;
        }
    }
    
    private void setUpUi() {
        final QToolBar toolBar = createToolBar();
        layout().addWidget(toolBar);
        
        final QHBoxLayout innerLayout = new QHBoxLayout();
        innerLayout.setMargin(0);
        innerLayout.addWidget(createList());
        innerLayout.addWidget(tree);
        
        ((QVBoxLayout)layout()).addLayout(innerLayout);
        
        addButtons(EnumSet.of(EDialogButtonType.CLOSE),true);
        layout().setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignTop));
    }

    private QToolBar createToolBar() {
        final QToolBar toolBar = new QToolBar(this);
        toolBar.setFloatable(true);        
        toolBar.addAction(generateKeyAction);
        toolBar.addAction(importKeyAction);
        toolBar.addAction(importCertAction);
        toolBar.addSeparator();
        toolBar.addAction(requestSignAction);
        toolBar.addAction(receiveCertAction);
        toolBar.addAction(deleteKeyAction);
        
        return toolBar;
    }
    
    private QWidget createList() {
        list = new QListWidget(this);
        list.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Expanding);
        list.setSortingEnabled(false);
        
        final QListWidgetItem personal = new QListWidgetItem();
        personal.setText(getEnvironment().getMessageProvider().translate("CertificateManagerDialog", "Personal"));
        personal.setData(Qt.ItemDataRole.UserRole, PERSONAL);
        personal.setIcon(ExplorerIcon.getQIcon(Icons.CERTIFICATE));
        list.addItem(personal);
        
        final QListWidgetItem trusted = new QListWidgetItem();
        trusted.setText(getEnvironment().getMessageProvider().translate("CertificateManagerDialog", "Trusted"));
        trusted.setData(Qt.ItemDataRole.UserRole, TRUSTED);
        trusted.setIcon(ExplorerIcon.getQIcon(Icons.TRUSTCERT));
        list.addItem(trusted);
        
        list.currentRowChanged.connect(this, "onCertificateTypeChange(Integer)");
        list.setCurrentRow(PERSONAL);
        list.setMaximumWidth(list.sizeHintForColumn(0) + 10);
        return list;
    }
    
    private void setUpTree(final IClientEnvironment env) {
        final MessageProvider msgProvider = env.getMessageProvider();
        tree = new QTreeWidget(this);
        tree.setColumnCount(COLUMNS);
        tree.header().setStretchLastSection(true);
        
        
        final List<String> colNames = new ArrayList<>(COLUMNS);
        colNames.add(msgProvider.translate("CertificateManagerDialog", "Alias"));
        colNames.add(msgProvider.translate("CertificateManagerDialog", "Issued to"));
        colNames.add(msgProvider.translate("CertificateManagerDialog", "Issued by"));
        colNames.add(msgProvider.translate("CertificateManagerDialog", "Valid from"));
        colNames.add(msgProvider.translate("CertificateManagerDialog", "Valid to"));
        
        final QTreeWidgetItem item = new QTreeWidgetItem(colNames);
        tree.setHeaderItem(item);
        tree.currentItemChanged.connect(this, "onCurrentEntryChange(QTreeWidgetItem, QTreeWidgetItem)");
        tree.setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu);
        tree.customContextMenuRequested.connect(this, "onItemsRightClick(QPoint)");
    }
    
    private void drawRsaEntries(final List<KeystoreEntry> entries) {
        tree.clear();
        empty = true;        
        for(KeystoreEntry e : entries) {
            try {
                final KeystoreRsaKeyEntry rsaEntry = (KeystoreRsaKeyEntry) e;
                final Certificate [] chain = rsaEntry.getCertificateChain();
                final QTreeWidgetItem certItem = readCertificateToRow((X509Certificate)chain[0], e.getAlias());
                tree.addTopLevelItem(certItem);
                for(int i = 1; i < chain.length; i++) {
                    certItem.addChild(readCertificateToRow((X509Certificate)chain[i], ""));
                }
                empty = false;
            } catch (ClassCastException ex) {
                continue;
            }
        }
       
        if(empty) {
            tree.addTopLevelItem(createEmptyRow());
        }
    }
    
    private void drawTrustedEntries(final List<KeystoreEntry> entries) {
        tree.clear();
        if(entries.isEmpty()) {
            tree.addTopLevelItem(createEmptyRow());
        } else {
            empty = false;
            for(KeystoreEntry e : entries) {
                final X509Certificate cert = (X509Certificate) ((KeystoreTrustedCertificateEntry)e).getCertificate();
                tree.addTopLevelItem(readCertificateToRow(cert, e.getAlias()));
            }
        }
    }
    
    private QTreeWidgetItem readCertificateToRow(final X509Certificate cert, final String alias) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        final QTreeWidgetItem treeItem = new QTreeWidgetItem();
        treeItem.setFlags(Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsSelectable);
        // Alias
        //String decoded = new String(Charset.forName("Latin1").encode(alias).array());
        treeItem.setText(0, alias);
        // Issued to
        treeItem.setText(1, getCommonName(cert.getSubjectX500Principal()));
        // Issued by
        treeItem.setText(2, getCommonName(cert.getIssuerX500Principal()));
        //Valid from
        treeItem.setText(3, dateFormat.format(cert.getNotBefore()));
        //Valid to
        treeItem.setText(4, dateFormat.format(cert.getNotAfter()));
        treeItem.setData(0, Qt.ItemDataRole.UserRole, alias);
        
        return treeItem;
    }
    
    private String getCurrentKeyAlias(){
        if (isPkcs11){
            final QTreeWidgetItem currentItem = tree.currentItem();
            return currentItem==null ? keyAlias : (String)currentItem.data(0, Qt.ItemDataRole.UserRole);
        }else{
            return keyAlias;
        }
    }
    
    private QTreeWidgetItem createEmptyRow() {
        final QTreeWidgetItem emptyItem = new QTreeWidgetItem();
        emptyItem.setFirstColumnSpanned(true);
        emptyItem.setFlags(Qt.ItemFlag.ItemIsEnabled);
        emptyItem.setForeground(0, new QBrush(QColor.gray));
        emptyItem.setText(0, getEnvironment().getMessageProvider().translate("Value", "<empty>"));
        empty = true;
        return emptyItem;
    }
   
    @SuppressWarnings("unused")
    private void onCertificateTypeChange(final Integer index) {
        
        final boolean personalStorage;
        if(index == PERSONAL) {
            drawRsaEntries(personals);
            personalStorage = true;
        } else if (index == TRUSTED){
            drawTrustedEntries(trusteds);
            personalStorage = false;
        } else {
            return;
        }
        generateKeyAction.setDisabled(!personalStorage);
        requestSignAction.setDisabled(!personalStorage || empty || !certTreeHasFocus);
        receiveCertAction.setDisabled(!personalStorage || empty || !certTreeHasFocus);
        importCertAction.setDisabled(personalStorage);
        importKeyAction.setDisabled(!personalStorage || isPkcs11);
        deleteKeyAction.setDisabled(empty || !certTreeHasFocus);
    }
    
    @SuppressWarnings("unused")
    private void deleteKey() {
        final String alias = (String) tree.currentItem().data(0, Qt.ItemDataRole.UserRole);
        final String message = getEnvironment().getMessageProvider().translate("CertificateManagerDialog", "Do you really want to delete key with alias '%s' ?");
        if (!Application.messageConfirmation(String.format(message, alias))) {
            return;
        }
        final boolean isPersonalKey = list.currentRow() == PERSONAL;
        try {
            final ITaskWaiter taskWaiter = getEnvironment().getApplication().newTaskWaiter();            
            try {
                taskWaiter.setMessage(getEnvironment().getMessageProvider().translate("CertificateManagerDialog", "Deleting Certificate"));
                taskWaiter.runAndWait(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        if(connection.getSslOptions().getKeyStoreType() == EKeyStoreType.FILE) {
                            personalStore.deleteEntry(alias); 
                        } else {
                            if(isPersonalKey) {
                                personalStore.deleteEntry(alias); 
                            } else {
                                trustStore.deleteEntry(alias);
                            }
                        }
                        return (Void)null;
                    }
                });
            } catch (ExecutionException ex) {
                if (isPkcs11){
                    getEnvironment().processException(new Pkcs11Exception(ex));
                }else{
                    getEnvironment().processException(ex.getCause());
                }
            }finally{
                taskWaiter.close();
            }                    
            
        } catch (Exception e) {
            getEnvironment().processException(e);
        }
        saveKeystore();
        rereadKeystore();
    }
        
    @SuppressWarnings("unused")
    private void onCurrentEntryChange(final QTreeWidgetItem current, final QTreeWidgetItem prev) {
        if(current != null) {
            final boolean disable = current.parent() != null;
            deleteKeyAction.setDisabled(disable || empty);
            requestSignAction.setDisabled(disable || list.currentRow() == TRUSTED || empty);
            receiveCertAction.setDisabled(disable || list.currentRow() == TRUSTED || empty);
        }
    }
    
    @SuppressWarnings("unused")
    private void requestKeyCertificate() {
        final CertificateRequestDialog dialog = new CertificateRequestDialog(getEnvironment(), this, true);
        final MessageProvider msgProvider = getEnvironment().getMessageProvider();
        if (dialog.execDialog() == DialogResult.ACCEPTED) {
            final char[] keyPassword = dialog.getKeyPassword();
            final String filePath = dialog.getFilePath();
            try {
                final KeystoreRsaKeyEntry keyEntry = personalStore.getKeystoreRsaKeyEntry(getCurrentKeyAlias(), keyPassword);
                CertificateUtils.requestCertificate(keyEntry, filePath, personalStore.getSecurityProviderName());
            } catch (KeystoreControllerException e) {
                if (e.getCause() instanceof UnrecoverableKeyException) {
                    Application.messageException(msgProvider.translate("CertificateManagerDialog", "Certificate Request"),
                            msgProvider.translate("CertificateManagerDialog", "Cannot recover key\nPossible cause: wrong key password"),
                            e);
                } else {
                    getEnvironment().processException(e);
                }
            } catch (CertificateUtilsException e) {
                getEnvironment().processException(e);
            }
        }
    }
    
    @SuppressWarnings({"unused", "UseSpecificCatch"})
    private void generateKeypair() {
        final GenerateKeyDialog dialog = new GenerateKeyDialog(getEnvironment(), this, isPkcs11);
        dialog.setCertificateAlias(keyAlias);
        final MessageProvider translator = getEnvironment().getMessageProvider();
        if(dialog.execDialog() == DialogResult.ACCEPTED) {
            final int keyLength = dialog.getKeyLength();
            final int publicExponent = dialog.getKeyPublicExponent();
            final String distinguishedName = CertificateUtils.buildDistinguishedName(
                    dialog.getCN(), dialog.getOU(), dialog.getO(),
                    dialog.getL(), dialog.getST(), dialog.getC(), dialog.getUID());
            final String alias = isPkcs11 ? dialog.getCertificateAlias() : keyAlias;
            final Date expirationDate = new Date(System.currentTimeMillis() + (long) 1000 * 3600 * 24 * dialog.getKeyValidity());
            final char[] keyPassword = dialog.getKeyPassword();
            try {
                final boolean needToDeleteEntry;
                if (personalStore.containsRsaKey(alias)) {
                    final String title = translator.translate("CertificateManagerDialog", "Key Generation");
                    final String message = translator.translate("CertificateManagerDialog", "Do you want to overwrite existing key '%s' ?");
                    if (!Application.messageConfirmation(title, String.format(message, alias))) {
                        return;
                    }
                    needToDeleteEntry = isPkcs11;
                }else{
                    needToDeleteEntry = false;
                }
                final ITaskWaiter taskWaiter = getEnvironment().getApplication().newTaskWaiter();
                try {
                    taskWaiter.setMessage(getEnvironment().getMessageProvider().translate("CertificateManagerDialog", "Key Generation"));
                    taskWaiter.runAndWait(new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            final KeystoreRsaKeyEntry entry = CertificateUtils.generateAndSignKey(keyLength, publicExponent, distinguishedName, expirationDate, alias);
                            if (needToDeleteEntry){
                                personalStore.deleteEntry(alias);
                            }
                            personalStore.storeRsaKey(entry, alias, keyPassword);
                            return (Void)null;
                        }
                    });
                } catch (ExecutionException ex) {
                    if (isPkcs11){
                        getEnvironment().processException(new Pkcs11Exception(ex));
                    }else{
                        getEnvironment().processException(ex.getCause());
                    }
                }finally{
                    taskWaiter.close();
                }
                saveKeystore();
                if (isPkcs11){
                    reload(password);
                }else{
                    rereadKeystore();
                }                
            } catch (Exception e) {
                getEnvironment().processException(e);
            }            
        }
    }
    
    @SuppressWarnings("unused")
    private void receiveCertificate() {
        final CertificateRequestDialog dialog = new CertificateRequestDialog(getEnvironment(), this, false);
        final MessageProvider msgProvider = getEnvironment().getMessageProvider();
        if (dialog.execDialog() == DialogResult.ACCEPTED) {
            final String filePath = dialog.getFilePath();
            final char[] keyPassword = dialog.getKeyPassword();
            try {
                final X509Certificate[] chain = CertificateUtils.readCertificateChain(filePath);
                if (!verifyCertificateChain(chain)) {
                    return;
                }
                final String currentKeyAlias = getCurrentKeyAlias();
                final ITaskWaiter taskWaiter = getEnvironment().getApplication().newTaskWaiter();
                try {
                    taskWaiter.setMessage(getEnvironment().getMessageProvider().translate("CertificateManagerDialog", "Importing certificate"));                    
                    taskWaiter.runAndWait(new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            personalStore.storeCertificateChain(chain, currentKeyAlias, keyPassword);
                            return (Void)null;
                        }
                    });
                    saveKeystore();
                    if (isPkcs11){
                        reload(password);
                    }else{
                        rereadKeystore();
                    }                    
                } catch (ExecutionException ex) {
                    if (isPkcs11){
                        getEnvironment().processException(new Pkcs11Exception(ex));
                    }else{
                        if (ex.getCause() instanceof KeystoreControllerException && 
                            ex.getCause().getCause() instanceof UnrecoverableKeyException){
                            Application.messageException(msgProvider.translate("CertificateManagerDialog", "Load Key Certificate"),
                                    msgProvider.translate("CertificateManagerDialog", "Cannot recover key\nPossible cause: wrong key password"),
                                    ex.getCause());
                        }
                        getEnvironment().processException(ex.getCause());
                    }
                }catch (InterruptedException ex){
                    //ignore
                }finally{
                    taskWaiter.close();
                }                                
            } catch (KeystoreControllerException e) {
                if (e.getCause() instanceof UnrecoverableKeyException) {
                    Application.messageException(msgProvider.translate("CertificateManagerDialog", "Load Key Certificate"),
                            msgProvider.translate("CertificateManagerDialog", "Cannot recover key\nPossible cause: wrong key password"),
                            e);
                } else {
                    getEnvironment().processException(e);
                }
            } catch (CertificateUtilsException e) {
                getEnvironment().processException(e);
            }
        }
    }
    
    @SuppressWarnings("unused")
    private void importKey() {
        final ImportKeyDialog imortDialog = new ImportKeyDialog(getEnvironment(), this);
        final MessageProvider msgProvider = getEnvironment().getMessageProvider();
        if (imortDialog.execDialog() == DialogResult.ACCEPTED) {
            final String filePath = imortDialog.getKeystoreFilePath();
            final char[] outerKeystorePassword = imortDialog.getKeystorePassword();
            
            final KeyStore outerKeyStore;
            try{
                outerKeyStore = KeyStore.getInstance("PKCS12");
            }catch(KeyStoreException exception){
                final String messageText = msgProvider.translate("CertificateManagerDialog", "Failed to import RSA key");
                Application.messageError(msgProvider.translate("CertificateManagerDialog", "Failed to import RSA key"), messageText);
                final String traceMessage = msgProvider.translate("CertificateManagerDialog", "Failed to import RSA key: %s\n%s");
                final String exceptionReason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), exception);
                final String exceptionStack = ClientException.exceptionStackToString(exception);
                getEnvironment().getTracer().error(String.format(traceMessage, exceptionReason, exceptionStack));
                return;
            }
            
            final FileInputStream inputStream;
            try {
                inputStream = new FileInputStream(filePath);
            } catch (FileNotFoundException e) {
                getEnvironment().processException(new FileException(getEnvironment(), FileException.EExceptionCode.FILE_NOT_FOUND, filePath, e));                
                return;
            }
            
            try{
                try{
                    outerKeyStore.load(inputStream, outerKeystorePassword);
                } catch (GeneralSecurityException | IOException exception) {
                    final String messageTextTemplate = msgProvider.translate("CertificateManagerDialog", "Failed to read '%1s' file.\nPossible cause: wrong key password or file corruption");
                    final String messageText = String.format(messageTextTemplate, filePath);
                    Application.messageError(msgProvider.translate("CertificateManagerDialog", "Failed to read file"), messageText);
                    final String traceMessage = msgProvider.translate("CertificateManagerDialog", "Can't import key: %s\n%s");
                    final String exceptionReason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), exception);
                    final String exceptionStack = ClientException.exceptionStackToString(exception);
                    getEnvironment().getTracer().error(String.format(traceMessage, exceptionReason, exceptionStack));                
                    return;
                }
                
                final Map<String,X509Certificate> certificates = new HashMap<>();
                final Enumeration<String> aliasEnumeration = outerKeyStore.aliases();
                while (aliasEnumeration.hasMoreElements()) {
                    final String outerAlias = aliasEnumeration.nextElement();
                    if (outerKeyStore.isKeyEntry(outerAlias)) {
                        final Certificate certificate = outerKeyStore.getCertificate(outerAlias);
                        if (certificate instanceof X509Certificate){
                            certificates.put(outerAlias, (X509Certificate)certificate);
                        }
                    }
                }
                final String alias;
                if (certificates.isEmpty()){
                    final String messageTextTemplate = msgProvider.translate("CertificateManagerDialog", "File \'%1s\' does not contains RSA keys");
                    final String messageText = String.format(messageTextTemplate, filePath);
                    final String messageTitle = msgProvider.translate("CertificateManagerDialog", "Unable to import key");
                    getEnvironment().messageInformation(messageTitle, messageText);
                    return;
                }else if (certificates.size()==1){
                    alias = certificates.keySet().iterator().next();
                }else{
                    final ChooseCertificateDialog chooseDialog = 
                            new ChooseCertificateDialog(certificates, getEnvironment(), this);
                    chooseDialog.setWindowTitle(msgProvider.translate("CertificateManagerDialog", "Choose Certificate You Want to Import"));
                    if (chooseDialog.exec() == QDialog.DialogCode.Accepted.value()){
                        alias = chooseDialog.getChoosenCertificateAlias();
                    }else{
                        return;
                    }
                }
                //different store and key passwords are not supported for PKCS#12 KeyStores
                final Key outerKey = outerKeyStore.getKey(alias, outerKeystorePassword);
                final Certificate[] outerCertChain = outerKeyStore.getCertificateChain(alias);
                final KeystoreRsaKeyEntry rsaKeyEntry = new KeystoreRsaKeyEntry(alias, outerCertChain, outerKey);
                if (!verifyCertificateChain(rsaKeyEntry.getCertificateChain())){
                    return;
                }
                final String confirmTitle = 
                    getEnvironment().getMessageProvider().translate("CertificateManagerDialog", "Confirm to Replace Certificate");
                final String confirmMessage = 
                    getEnvironment().getMessageProvider().translate("CertificateManagerDialog", "Do you really want to replace current certificate and RSA key?");
                if (!empty && !getEnvironment().messageConfirmation(confirmTitle, confirmMessage)){
                    return;
                }
                final EnterPasswordDialog pwdDialog = new EnterPasswordDialog(getEnvironment(), this);
                final String messageTemplate = 
                    getEnvironment().getMessageProvider().translate("CertificateManagerDialog","Please enter password to protect imported RSA key");
                pwdDialog.setMessage(String.format(messageTemplate, alias));
                pwdDialog.setPasswordConfirmationRequired(true);
                if (pwdDialog.exec()!=QDialog.DialogCode.Accepted.value()){
                    return;
                }
                try{
                    personalStore.storeRsaKey(rsaKeyEntry, keyAlias, pwdDialog.getPassword().toCharArray());
                }catch(KeystoreControllerException exception){
                    final String messageText = msgProvider.translate("CertificateManagerDialog", "Failed to import RSA key");
                    Application.messageError(msgProvider.translate("CertificateManagerDialog", "Failed to import RSA key"), messageText);
                    final String traceMessage = msgProvider.translate("CertificateManagerDialog", "Failed to import RSA key: %s\n%s");
                    final String exceptionReason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), exception);
                    final String exceptionStack = ClientException.exceptionStackToString(exception);
                    getEnvironment().getTracer().error(String.format(traceMessage, exceptionReason, exceptionStack));
                    return;
                }
            }catch(GeneralSecurityException exception){
                final String messageTextTemplate = msgProvider.translate("CertificateManagerDialog", "Failed to read '%1s' file.\nPossible cause: wrong key password or file corruption");
                final String messageText = String.format(messageTextTemplate, filePath);
                Application.messageError(msgProvider.translate("CertificateManagerDialog", "Failed to read file"), messageText);
                final String traceMessage = msgProvider.translate("CertificateManagerDialog", "Can't import key: %s\n%s");
                final String exceptionReason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), exception);
                final String exceptionStack = ClientException.exceptionStackToString(exception);
                getEnvironment().getTracer().error(String.format(traceMessage, exceptionReason, exceptionStack));                
                return;                
            }finally{                
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    //ignore
                }
            }
            saveKeystore();
            rereadKeystore();            
        }
    }
    
    @SuppressWarnings({"unused", "UseSpecificCatch"})
    private void importTrustedCertificate() {
        final ImportTrustedCertificateDialog dialog = new ImportTrustedCertificateDialog(getEnvironment(), this);
        final MessageProvider msgProvider = getEnvironment().getMessageProvider();
        if (dialog.execDialog() == DialogResult.ACCEPTED) {
            final String filePath = dialog.getCertificateFilePath();
            final String alias = dialog.getAlias();
            try {
                final X509Certificate certificate = CertificateUtils.readTrustedCertificate(filePath);
                final String confirmTrustCert = msgProvider.translate("CertificateManagerDialog", "Please confirm that you trust the certificate:");
                final String owner = String.format(msgProvider.translate("CertificateManagerDialog", "Owner: %s"),
                        certificate.getSubjectX500Principal().getName());
                final String issuer = String.format(msgProvider.translate("CertificateManagerDialog", "Issuer: %s"),
                        certificate.getIssuerX500Principal().getName());
                final String serialNumber = String.format(msgProvider.translate("CertificateManagerDialog", "Serial number: %s"),
                        certificate.getSerialNumber().toString());
                final String validFrom = String.format(msgProvider.translate("CertificateManagerDialog", "Valid from: %s"),
                        certificate.getNotBefore().toString());
                final String validUntil = String.format(msgProvider.translate("CertificateManagerDialog", "Valid until: %s"),
                        certificate.getNotAfter().toString());
                final String sha1 = String.format(
                        msgProvider.translate("CertificateManagerDialog", "SHA-1 fingerprint: %s"),
                        CertificateUtils.getCertificateFingerprintAsString(CertificateUtils.ALGORITHM_SHA1, certificate));
                final String message = confirmTrustCert + "\n\n" + owner + "\n" + issuer + "\n" + serialNumber + "\n" + validFrom + "\n" + validUntil + "\n" + sha1;
                if (!Application.messageConfirmation(
                        msgProvider.translate("CertificateManagerDialog", "Trusted Certificate Verification"),
                        message)) {
                    return;
                }
                trustStore.storeTrustedCertificate(alias, certificate);
            } catch (Exception e) {
                getEnvironment().processException(e);
            }
            saveKeystore();
            rereadKeystore();
        }
    }
    // </editor-fold>
    

    private static String getCommonName(final X500Principal principal) {
        final String [] fields = principal.getName().split(",");
        String result = "";
        for(String s : fields) {
            if(s.contains("CN")) {
                String [] keyValue = s.split("=");
                if(keyValue.length == 2) {
                    result = s.split("=")[1];
                }
                break;
            }
        }
        
        return result;
    }

    @Override
    public void done(final int result) {
        closeStores();
        super.done(result);
    }
    
    private void closeStores() {
        try {
            if(personalStore != null) {
                personalStore.close();
            }
            if(trustStore != null) {
                trustStore.close();
            }
        } catch (KeystoreControllerException ex) {
            getEnvironment().getTracer().error(ex);
        }
    }
    
    private void saveKeystore() {
        try {
            final String keyStoreFilePath = Application.getWorkPath()
                        + "/"
                        + connection.getId().toString()
                        + ".jceks";
            if(personalStore.getType() == EKeyStoreType.FILE) {
                personalStore.saveKeyStoreToFile(keyStoreFilePath, PASSWORD);
            } else if(trustStore != null) {
                trustStore.saveKeyStoreToFile(keyStoreFilePath, PASSWORD);
            }
        } catch (Exception e) {
            getEnvironment().processException(e);
        }
    }
    
    private void rereadKeystore() {
        try {
            personals.clear();
            trusteds.clear();
            final Iterable<KeystoreEntry> entries = personalStore.getKeyStoreEntries();
             for(KeystoreEntry e : entries) {
                if(e instanceof KeystoreRsaKeyEntry) {
                    personals.add(e);
                } else if(e instanceof KeystoreTrustedCertificateEntry) {
                    trusteds.add(e);
                }
            }
            
            if(isPkcs11) {
                for(KeystoreEntry e : trustStore.getKeyStoreEntries()) {
                    if(e instanceof KeystoreTrustedCertificateEntry) {
                        trusteds.add(e);
                    }
                }
            }
            list.currentRowChanged.emit(list.currentRow());
        } catch (KeystoreControllerException ex) {
            getEnvironment().processException(ex);
        }
    }
    
    private boolean verifyCertificateChain(final Certificate[] chain) {
        final MessageProvider msgProvider = getEnvironment().getMessageProvider();
        final StringBuilder certificateChainBuilder = new StringBuilder();
        for (Certificate certificate : chain) {
            final X509Certificate x509certificate = (X509Certificate) certificate;
            if (certificateChainBuilder.length()==0) {
                certificateChainBuilder.append(msgProvider.translate("CertificateManagerDialog", "Own certificate"));
            } else {
                certificateChainBuilder.append(msgProvider.translate("CertificateManagerDialog", "CA certificate"));
            }
            certificateChainBuilder.append("\n");
            String fingerprintSha1 = "";
            try {
                fingerprintSha1 = CertificateUtils.getCertificateFingerprintAsString(CertificateUtils.ALGORITHM_SHA1, x509certificate);
            } catch (Exception e) {
            }
            final String subject = String.format(msgProvider.translate("CertificateManagerDialog", "Subject: %s"),
                    x509certificate.getSubjectX500Principal().getName());
            final String issuer = String.format(
                    msgProvider.translate("CertificateManagerDialog", "Issuer: %s"),
                    x509certificate.getIssuerX500Principal().getName());
            final String sha1 = String.format(
                    msgProvider.translate("CertificateManagerDialog", "SHA-1 fingerprint: %s"),
                    fingerprintSha1);
            certificateChainBuilder.append(subject);
            certificateChainBuilder.append("\n");
            certificateChainBuilder.append(issuer);
            certificateChainBuilder.append("\n");
            certificateChainBuilder.append(sha1);
            certificateChainBuilder.append("\n\n");
        }
        return Application.messageConfirmation(
                msgProvider.translate("CertificateManagerDialog", "Key Certificate Verification"),
                msgProvider.translate("CertificateManagerDialog", "Please confirm that you trust the certificate chain:") + "\n\n" + certificateChainBuilder.toString());
    }
    
    @SuppressWarnings("unused")
    private void onItemsRightClick(final QPoint point) {
        final QTreeWidgetItem currentItem = tree.itemAt(point);
        tree.setCurrentItem(currentItem);
        if(currentItem != null && currentItem.parent() == null && !empty) {
            QMenu contextMenu = new QMenu(tree);
            contextMenu.addAction(requestSignAction);
            contextMenu.addAction(receiveCertAction);
            contextMenu.addAction(deleteKeyAction);
            contextMenu.exec(tree.viewport().mapToGlobal(point));//.add(new QPoint(contextMenu.width(), contextMenu.height())).add(tree.pos()));
            
        }
    }
    
    private boolean processException(KeystoreControllerException ex) {
        final String message = ex.getMessage();
        if(message.contains("CKR_PIN_INCORRECT")) {
            final String wrongPwdMessage = getEnvironment().getMessageProvider().translate("CertificateManagerDialog", "Wrong password");
            getEnvironment().messageError(wrongPwdMessage);
            return true;
        } else if(message.contains("Can't instantiate a keystore, because the device driver hasn't been properly loaded")) {
            final String msg = getEnvironment().getMessageProvider().translate("CertificateManagerDialog", "Hardware security module is not loaded yet.");
            final String title = getEnvironment().getMessageProvider().translate("CertificateManagerDialog", "Hardware security module error");
            getEnvironment().messageError(title, msg);
            return true;
        } else {
            return false;
        }
    }
}
