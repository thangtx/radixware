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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QLayout.SizeConstraint;
import com.trolltech.qt.gui.QMessageBox;
import com.trolltech.qt.gui.QShowEvent;
import com.trolltech.qt.gui.QWidget;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import org.radixware.kernel.common.auth.PasswordHash;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.eas.AbstractSslContextFactory;
import org.radixware.kernel.common.client.eas.AbstractSslTrustManager;
import org.radixware.kernel.common.client.eas.EasClient;
import org.radixware.kernel.common.client.eas.IEasClient;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.FileException;
import org.radixware.kernel.common.client.exceptions.SslTrustManagerException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.utils.ISecretStore;
import org.radixware.kernel.common.client.utils.TokenProcessor;
import org.radixware.kernel.common.enums.EAuthType;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EKeyStoreType;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.dialogs.certificates.CertificateManager;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.session.Connections;
import org.radixware.kernel.explorer.env.session.SslTrustManager;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public class LogonDialog extends ExplorerDialog {
    
    private static final class CachedKrbCredentials{    
        
        private static Class<?> krb5CredentialsClass;
        private static Method acquireTGTFromCache;
        private static Method getEndTime;
        private static Method isRenewable;
        private static Method getRenewTill;
        private static Method renew;
        private static Class<?> krbPrincipalNameClass;
        private static Constructor krbPrincipalNameCtor;
        private static Boolean initComplete;
        
        private final Object krb5CredentialsInstance;
        
        private CachedKrbCredentials(final Object instance){
            krb5CredentialsInstance = instance;
        }
        
        @SuppressWarnings("PMD.NonThreadSafeSingleton")
        private static boolean init(){
            if (initComplete==null){
                try{
                    krb5CredentialsClass = Class.forName("sun.security.krb5.Credentials");
                    krbPrincipalNameClass = Class.forName("sun.security.krb5.PrincipalName");
                    acquireTGTFromCache = 
                        krb5CredentialsClass.getDeclaredMethod("acquireTGTFromCache", krbPrincipalNameClass, String.class);
                    getEndTime = krb5CredentialsClass.getDeclaredMethod("getEndTime");
                    isRenewable = krb5CredentialsClass.getDeclaredMethod("isRenewable");
                    getRenewTill = krb5CredentialsClass.getDeclaredMethod("getRenewTill");
                    renew = krb5CredentialsClass.getDeclaredMethod("renew");
                    krbPrincipalNameCtor = krbPrincipalNameClass.getConstructor(String.class);                                        
                }catch(ClassNotFoundException | LinkageError | NoSuchMethodException | SecurityException e){//NOPMD
                    initComplete = Boolean.FALSE;
                    return false;
                }
                initComplete = Boolean.TRUE;
            }
            return initComplete.booleanValue();
        }
        
        private static Object createKrbPrincipalName(final String userName){
            try{
                return krbPrincipalNameCtor.newInstance(userName);
            }catch(IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException e){//NOPMD
                return null;
            }
        }
        
        public static CachedKrbCredentials acquireTGTFromCache(final String userName){
            if (init()){
                final Object principalName = createKrbPrincipalName(userName);
                if (principalName!=null){
                    final Object invocationResult;
                    try{
                        invocationResult = acquireTGTFromCache.invoke(null, principalName, null);
                    }catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e){//NOPMD
                        return null;
                    }
                    if (krb5CredentialsClass.isInstance(invocationResult)){
                        return new CachedKrbCredentials(invocationResult);
                    }else{
                        return null;
                    }
                }
            }
            return null;            
        }
        
        private boolean isRenewable(){
            final Object invocationResult;
            try{
                invocationResult = isRenewable.invoke(krb5CredentialsInstance);
            }catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e){//NOPMD
                return false;
            }
            return invocationResult instanceof Boolean ? ((Boolean)invocationResult).booleanValue() : false;
        }
        
        private boolean canRenew(){
            if (isRenewable()){
                final Object invocationResult;
                try{
                    invocationResult = getRenewTill.invoke(krb5CredentialsInstance);
                }catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e){//NOPMD
                    return false;
                }
                if (invocationResult instanceof Date){
                    return ((Date)invocationResult).getTime()>System.currentTimeMillis();
                }
            }
            return false;
        }
        
        public boolean renew(){
            if (canRenew()){
                try{
                    return krbPrincipalNameClass.isInstance(renew.invoke(krb5CredentialsInstance));
                }catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e){//NOPMD
                    return false;
                }
            }
            return false;
        }
        
        public boolean isActual(){
            final Object invocationResult;
            try{
                invocationResult = getEndTime.invoke(krb5CredentialsInstance);
            }catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e){//NOPMD
                return false;
            }
            if (invocationResult instanceof Date){
                return ((Date)invocationResult).getTime()>System.currentTimeMillis();
            }else{
                return false;
            }
        }
    }
    
    private final Ui_LogonDialog uiCreator = new Ui_LogonDialog();
    private final Connections connections;
    private final ConnectionsManager connectionsDialog;
    private final static String LAST_CONNECTIONS_KEY = "lastConnections";
    private final ArrStr connectionNamesByOrder;
    private final ISecretStore pwdStore;
    private final QEventFilter focusOutListener = new QEventFilter(this){  
        @Override
        public boolean eventFilter(final QObject source, final QEvent event) {
            if (event!=null && event.type() == QEvent.Type.FocusOut) {
                userName2Password();
            }
            return false;
        }
    };
    
    private boolean readyToLogin;
    private boolean autoLogin;
    private String resultUserName;
    private ConnectionOptions resultConnectionOptions;

    @SuppressWarnings("LeakingThisInConstructor")
    public LogonDialog(final IClientEnvironment environment, 
                                 final QWidget parent, 
                                 final Connections connections, 
                                 final ISecretStore pwdStore) {
        super(environment, parent, false);
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
        this.connections = connections;
        this.pwdStore = pwdStore;
        connectionsDialog = new ConnectionsManager(environment, this, connections);
        setupUi();
        if (connections.size() == 0) {
            connectionsDialog.createConnection();
        }
        final String lastConnections = getEnvironment().getConfigStore().readString(SettingNames.SYSTEM + "/" + LAST_CONNECTIONS_KEY, null);
        connectionNamesByOrder = (lastConnections != null ? ArrStr.fromValAsStr(lastConnections) : new ArrStr());
        fillConnections();
        uiCreator.cbConnection.setCurrentIndex(0);
        onIndexChanged();
        uiCreator.cbConnection.currentIndexChanged.connect(this, "onIndexChanged()");
        readParams();
    }

    private void setupUi() {
        uiCreator.setupUi(this);
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Connection.KEY));
        dialogLayout().setSpacing(9);
        dialogLayout().setContentsMargins(6, 3, 6, 9);
        dialogLayout().addWidget(uiCreator.contentFrame);
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), false);
        dialogLayout().setSizeConstraint(SizeConstraint.SetFixedSize);
        focusOutListener.setProcessableEventTypes(EnumSet.of(QEvent.Type.FocusOut));
        uiCreator.leUserName.installEventFilter(focusOutListener);

        if (!RunParams.isDevelopmentMode()) {
            getButton(EDialogButtonType.OK).setEnabled(false);
            uiCreator.lePassword.textChanged.connect(this, "updateButton()");
            uiCreator.cbConnection.currentIndexChanged.connect(this, "updateButton()");
        }
        acceptButtonClick.connect(this, "onAccepted()");
        rejectButtonClick.connect(this, "reject()");
        accepted.connect(this, "userName2Password()");
        uiCreator.tbEdit.clicked.connect(this, "onEdit()");
        finished.connect(connections, "store()");
        if (WidgetUtils.isGnome3Classic()){
            WidgetUtils.centerDialog(this);
        }
    }

    private void fillConnections() {
        uiCreator.cbConnection.clear();
        for (int i = connectionNamesByOrder.size() - 1; i >= 0; i--) {
            final ConnectionOptions connection = connections.findByName(connectionNamesByOrder.get(i));
            if (connection != null) {
                uiCreator.cbConnection.insertItem(0, connection.getName(), connection);
            } else {
                connectionNamesByOrder.remove(i);
            }
        }

        for (ConnectionOptions connection : connections) {
            if (connectionNamesByOrder.indexOf(connection.getName()) < 0) {
                uiCreator.cbConnection.addItem(connection.getName(), connection);
            }
        }

        getButton(EDialogButtonType.OK).setEnabled(connections.size() > 0);
        if (connections.size() == 0) {
            uiCreator.cbConnection.addItem(Application.translate("LogonDialog", "<No connections>"), null);
        }
    }
    
    public void setConnectionName(final String connectionName){
        int currentIndex = 0;
        if (connectionName != null) {
            ConnectionOptions connection;
            for (int i = 0; i < uiCreator.cbConnection.count(); i++) {
                connection = (ConnectionOptions) uiCreator.cbConnection.itemData(i);
                if (connection != null && connection.getName().equals(connectionName)) {
                    currentIndex = i;
                    uiCreator.cbConnection.setCurrentIndex(currentIndex);
                    readyToLogin = true;
                    break;
                }
            }
        }        
    }
    
    public void setUserName(final String userName){
        if (userName!=null){
            final ConnectionOptions connection = getCurrentConnection();
            if (connection==null || connection.getKerberosOptions()==null || !SystemTools.isWindows){
                uiCreator.leUserName.setText(userName);
            }            
        }
    }
    
    public void setAutoLogin(final boolean autoLogin){
        this.autoLogin = autoLogin;
    }

    private void readParams() {
        if (RunParams.getPassword() != null) {
            uiCreator.lePassword.setText(RunParams.getPassword());
        }
    }        

    public final String getUserName() {
        return resultUserName;
    }
    
    private String getCurrentUserName(){
        return uiCreator.leUserName.text();
    }

    private char[] getPassword() {
        return uiCreator.lePassword.isEnabled() ? uiCreator.lePassword.text().toCharArray() : null;
    }

    public void clear() {
        resultConnectionOptions = null;
    }
    
    private ConnectionOptions getCurrentConnection(){
        final int idx = uiCreator.cbConnection.currentIndex();
        if (idx < 0) {
            return null;
        }else{
            return (ConnectionOptions) uiCreator.cbConnection.itemData(idx);
        }        
    }

    public final ConnectionOptions getConnection() {
        return resultConnectionOptions==null ? null : resultConnectionOptions.createUnmodifableCopy();
    }

    public IEasClient createEasClient(final ConnectionOptions connection) throws IllegalUsageError, KeystoreControllerException, CertificateUtilsException {
        if (connection.getSslOptions() == null) {
            return new EasClient(getEnvironment(), 
                                            connection.getInitialServerAddresses(),
                                            connection.getStationName(), 
                                            connection.getAuthType(), 
                                            connection.getAddressTranslationFilePath(),
                                            connection.isSapDiscoveryEnabled());
        } else {
            final AbstractSslContextFactory sslContextFactory;
            if (connection.getAuthType()==EAuthType.KERBEROS){
                sslContextFactory = new AbstractSslContextFactory(connection, getEnvironment()){
                    @Override
                    public AbstractSslTrustManager createSslTrustManager(final IClientEnvironment environment, final String trustStorePath, final X509Certificate certificate) {
                        return new AbstractSslTrustManager(environment, trustStorePath, certificate){
                            @Override
                            protected AbstractSslTrustManager.Confirmation confirm(final X509Certificate cert, final AbstractSslTrustManager.ConfirmationReason reason) throws SslTrustManagerException {
                                return AbstractSslTrustManager.Confirmation.ACCEPT;//server authorized via kerberos
                            }                            
                        };
                    }
                };            
            }else{
                sslContextFactory = new AbstractSslContextFactory(connection, getEnvironment()){
                    @Override
                    public AbstractSslTrustManager createSslTrustManager(final IClientEnvironment environment, final String trustStorePath, final X509Certificate certificate) {
                        return new SslTrustManager(environment, trustStorePath, certificate);
                    }
                };
            }
            final IEasClient easClient = new EasClient(getEnvironment(), 
                                                       connection.getInitialServerAddresses(),
                                                       connection.getStationName(), 
                                                       connection.getAuthType(),
                                                       connection.getAddressTranslationFilePath(),
                                                       connection.isSapDiscoveryEnabled(),
                                                       sslContextFactory,
                                                       connection.getSslOptions().useSSLAuth() ? pwdStore : null);
            return easClient;
        }
    }

    private void onIndexChanged() {
        final ConnectionOptions connection = getCurrentConnection();
        if (connection != null) {
            updateUserName(connection);
            uiCreator.lePassword.clear();
        }
        final boolean needForPassword;
        if (connection != null && connection.getSslOptions() != null && connection.getSslOptions().useSSLAuth()) {
            needForPassword = true;
            if (connection.getSslOptions().getKeyStoreType()==EKeyStoreType.FILE){
                uiCreator.lbPassword.setText(Application.translate("LogonDialog", "RSA key access &password:"));
            }else{
                uiCreator.lbPassword.setText(Application.translate("LogonDialog", "Security device access &password:"));
            }
        } else {
            uiCreator.lbPassword.setText(Application.translate("LogonDialog", "&Password:"));            
            if (connection!=null && connection.getKerberosOptions() != null){
                needForPassword = SystemTools.isWindows ? false : !canUseCachedKrbTicked(connection);
            }else{
                needForPassword = true;
            }            
        }
        uiCreator.lbPassword.setEnabled(needForPassword);
        uiCreator.lePassword.setEnabled(needForPassword);
        if (!needForPassword) {
            getButton(EDialogButtonType.OK).setEnabled(true);
        }        
        if (uiCreator.lePassword.isEnabled()){
            uiCreator.lePassword.setFocus();
        }
    }

    private void updateUserName(final ConnectionOptions connection) {
        if (connection.getSslOptions() != null && connection.getSslOptions().useSSLAuth()) {
            uiCreator.leUserName.setEnabled(false);
            uiCreator.lbUserName.setEnabled(false);
            uiCreator.leUserName.clear();
            final char[] pwd = getPassword();
            try {                
                final X509Certificate certificate = connection.getCertificate(pwd);
                if (certificate != null) {
                    final String clientDN = certificate.getSubjectX500Principal().getName();
                    final HashMap<String, String> clientDetails = CertificateUtils.parseDistinguishedName(clientDN);
                    uiCreator.leUserName.setText(clientDetails.get("CN"));
                }
            } catch (FileException ex) {//NOPMD Just leave empty uiCreator.leUserName

            } catch (KeystoreControllerException ex) {
                getEnvironment().getTracer().error(ex);
            }
            finally{
                if (pwd!=null){
                    Arrays.fill(pwd, ' ');
                }
            }
        } else if (connection.getKerberosOptions() != null){
            if (SystemTools.isWindows){
                uiCreator.lbUserName.setEnabled(false);
                uiCreator.leUserName.setEnabled(false);
                uiCreator.leUserName.setText(System.getProperty("user.name"));                
            }else{
                uiCreator.lbUserName.setEnabled(true);
                uiCreator.leUserName.setEnabled(true);
                if (connection.getUserName()==null || connection.getUserName().isEmpty()){
                    uiCreator.leUserName.setText(System.getProperty("user.name"));
                }else{
                    uiCreator.leUserName.setText(connection.getUserName());
                }
            }
        }else {
            uiCreator.leUserName.setEnabled(true);
            uiCreator.lbUserName.setEnabled(true);
            uiCreator.leUserName.setText(connection.getUserName());
        }
    }

    private void userName2Password() {
        final char[] pwd = getPassword();
        try{
            if (RunParams.isDevelopmentMode() && uiCreator.lePassword.isEnabled() && pwd.length==0) {
                uiCreator.lePassword.setText(getCurrentUserName());
            }
        }finally{
            if (pwd!=null){
                Arrays.fill(pwd, ' ');
            }
        }
    }

    @SuppressWarnings("unused")
    private void onEdit() {
        if (connections.size() == 0) {
            connectionsDialog.createConnection();
        } else {
            if (uiCreator.cbConnection.currentIndex() >= 0 && uiCreator.cbConnection.itemData(uiCreator.cbConnection.currentIndex()) instanceof ConnectionOptions) {
                connectionsDialog.setCurrentConnection((ConnectionOptions) uiCreator.cbConnection.itemData(uiCreator.cbConnection.currentIndex()));
            }
            connectionsDialog.exec();
        }
        fillConnections();
        if (connections.size() > 0) {
            final int idx = uiCreator.cbConnection.findData(connectionsDialog.getCurrentConnection());
            if (idx >= 0) {
                uiCreator.cbConnection.setCurrentIndex(idx);
            }
        }
    }

    @SuppressWarnings("unused")
    private void updateButton() {
        getButton(EDialogButtonType.OK).setEnabled(!uiCreator.lePassword.text().isEmpty() && getCurrentConnection() != null);
    }

    @Override
    protected void showEvent(QShowEvent arg__1) {
        super.showEvent(arg__1);
        if (Boolean.getBoolean("_rdx.explorer.autologin") && getButton(EDialogButtonType.OK).isEnabled()) {
           QTimer timer = new QTimer(this);
           timer.setSingleShot(true);
           timer.timeout.connect(this, "onAccepted()");
           timer.start(1);
        }
    }
    
    private void onAccepted() {
        userName2Password();
        if (!RunParams.isDevelopmentMode() && uiCreator.lePassword.text().isEmpty() && uiCreator.lePassword.isEnabled()) {
            Application.messageError(Application.translate("LogonDialog", "Password is Empty"),
                    Application.translate("LogonDialog", "Please enter password"));
            uiCreator.lePassword.setFocus();
            return;
        }
        connectionNamesByOrder.clear();
        for (int i = 0; i < uiCreator.cbConnection.count(); i++) {
            if (i != uiCreator.cbConnection.currentIndex()) {
                connectionNamesByOrder.add(uiCreator.cbConnection.itemText(i));
            }
        }
        connectionNamesByOrder.add(0, uiCreator.cbConnection.currentText());

        final ConnectionOptions connection = getCurrentConnection();
        if (connection != null) {
            getEnvironment().getTracer().getProfile().setDefaultSeverity(connection.getEventSeverity());
            if (connection.getSslOptions() != null && connection.getSslOptions().useSSLAuth()) {
                final char[] pwd = getPassword();
                try {
                    final X509Certificate certificate = connection.getCertificate(pwd);
                    if (certificate == null) {
                        final String traceMessage = Application.translate("LogonDialog", "There are no accessible certificates for connection '%s'");
                        getEnvironment().getTracer().error(String.format(traceMessage, connection.getName()));
                        cantGetCertificateForConnection(connection);
                        return;
                    }else{
                        final byte[] encrypted = new TokenProcessor().encrypt(pwd);
                        pwdStore.setSecret(encrypted);
                        Arrays.fill(encrypted, (byte)0);                                                
                    }
                } catch (FileException ex) {
                    final String traceMessage = Application.translate("LogonDialog", "There are no accessible certificates for connection '%s'");
                    getEnvironment().getTracer().error(String.format(traceMessage, connection.getName()) + ": " + ex.getMessage());
                    cantGetCertificateForConnection(connection);
                    return;
                } catch (KeystoreControllerException ex) {
                    getEnvironment().processException(ex);                    
                    clear();
                    return;
                }finally{
                    connection.onClose();
                    Arrays.fill(pwd, ' ');
                }
            } else {
                if (connection.getKerberosOptions()!=null ){
                    final char[] pwd = getPassword();
                    if (pwd!=null){
                        final byte[] encrypted = new TokenProcessor().encrypt(pwd);
                        Arrays.fill(pwd, ' ');
                        pwdStore.setSecret(encrypted);
                        Arrays.fill(encrypted, (byte)0);                        
                    }                    
                }else{//Password auth
                    final char[] pwd = getPassword();
                    final PasswordHash pwdHash = PasswordHash.Factory.newInstance(getCurrentUserName(), pwd);
                    Arrays.fill(pwd, ' ');
                    try{
                        final byte[] pwdHashData = pwdHash.export();
                        try{
                            final byte[] encryptedPwdHash = new TokenProcessor().encrypt(pwdHashData);                            
                            pwdStore.setSecret(encryptedPwdHash);
                            Arrays.fill(encryptedPwdHash, (byte)0);
                        }finally{
                            Arrays.fill(pwdHashData, (byte)0);
                        }
                    }finally{
                        pwdHash.erase();
                    }
                }
                connection.setConnectedUserName(getCurrentUserName());
            }
            getEnvironment().getConfigStore().writeString(SettingNames.SYSTEM + "/" + LAST_CONNECTIONS_KEY, connectionNamesByOrder.toString());
            accept();
        }
    }

    private void cantGetCertificateForConnection(final ConnectionOptions connection) {
        final MessageProvider msgProvider = getEnvironment().getMessageProvider();
        final String title = msgProvider.translate("LogonDialog", "Can't Use this Connection");
        final String message;
        final QMessageBox.StandardButtons buttons;

        if (connection.getSslOptions().getKeyStoreType() == EKeyStoreType.PKCS11) {
            final String nonLocalizedMessage = msgProvider.translate(
                    "LogonDialog",
                    "Can't retrieve a certificate with alias \"%s\"");
            message = String.format(nonLocalizedMessage, connection.getSslOptions().getCertificateAlias());
            buttons = new QMessageBox.StandardButtons(QMessageBox.StandardButton.Ok);
        } else {
            message = msgProvider.translate("LogonDialog", "There are no accessible certificates for this connection. "
                    + "You must generate a certificate key to use this connection.\nPress OK button to run certificate manager dialog.");
            buttons = new QMessageBox.StandardButtons(QMessageBox.StandardButton.Ok, QMessageBox.StandardButton.Cancel);
        }

        final QMessageBox.StandardButton answer =
                Application.messageBox(title, message, QMessageBox.Icon.Information, buttons);
        if (answer == QMessageBox.StandardButton.Ok) {
            final CertificateManager dialog;
            try{
                dialog = CertificateManager.newCertificateManager(getEnvironment(), this, connection, uiCreator.lePassword.text());
            }catch(KeystoreControllerException ex){
                getEnvironment().processException(ex);
                return;
            }catch(InterruptedException ex){
                return;
            }
            dialog.exec();
        }
    }

    @Override
    public int exec() {
        final ConnectionOptions connection = getCurrentConnection();
        if (connection!=null && readyToLogin && autoLogin
            && (!pwdStore.isEmpty() || canUseCachedKrbTicked(connection))) {
            getEnvironment().getTracer().getProfile().setDefaultSeverity(connection.getEventSeverity());
            resultUserName = getCurrentUserName();
            connection.setConnectedUserName(resultUserName);
            resultConnectionOptions = connection;
            return QDialog.DialogCode.Accepted.value();
        }
        return super.exec();
    }

    @Override
    public void done(final int result) {
        if (result==QDialog.DialogCode.Accepted.value()){
            resultUserName = getCurrentUserName();
            resultConnectionOptions = getCurrentConnection();
        }
        super.done(result);
    }

    private boolean canUseCachedKrbTicked(final ConnectionOptions connection) {
        final String userName = getCurrentUserName();
        if (connection.getKerberosOptions() != null && userName != null && !userName.isEmpty()) {
            final CachedKrbCredentials krbCreds = CachedKrbCredentials.acquireTGTFromCache(userName);
            return krbCreds!=null && (krbCreds.isActual() || krbCreds.renew());
        }
        return false;
    }
}