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
package org.radixware.kernel.designer.eas.client;

import java.io.File;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.swing.SwingUtilities;
import org.ietf.jgss.GSSCredential;
import org.openide.LifecycleManager;
import org.openide.awt.StatusDisplayer;
import org.radixware.kernel.common.client.Clipboard;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.dialogs.IEnterPasswordDialog;
import org.radixware.kernel.common.client.dialogs.IMessageBox;
import org.radixware.kernel.common.client.eas.EasSession;
import org.radixware.kernel.common.client.eas.IEasClient;
import org.radixware.kernel.common.client.eas.IEasSession;
import org.radixware.kernel.common.client.eas.IKerberosCredentialsProvider;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions.SslOptions;
import org.radixware.kernel.common.client.eas.connections.Connections;
import org.radixware.kernel.common.client.eas.resources.IResourceManager;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.env.IEventLoop;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.env.progress.ProgressHandleManager;
import org.radixware.kernel.common.client.errors.EasError;
import org.radixware.kernel.common.client.errors.KerberosError;
import org.radixware.kernel.common.client.errors.UnsupportedDefinitionVersionError;
import org.radixware.kernel.common.client.exceptions.CantUpdateVersionException;
import org.radixware.kernel.common.client.exceptions.ExceptionMessage;
import org.radixware.kernel.common.client.exceptions.SignatureException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitions;
import org.radixware.kernel.common.client.models.groupsettings.FilterSettingsStorage;
import org.radixware.kernel.common.client.models.groupsettings.GroupSettingsStorage;
import org.radixware.kernel.common.client.text.ITextOptionsProvider;
import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.client.tree.IExplorerTreeManager;
import org.radixware.kernel.common.client.types.TimeZoneInfo;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.IMainStatusBar;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.*;
import org.radixware.kernel.common.kerberos.KerberosCredentials;
import org.radixware.kernel.common.kerberos.KerberosLoginConfiguration;
import org.radixware.kernel.common.client.utils.TokenProcessor;
import org.radixware.kernel.common.kerberos.KerberosException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.eas.dialogs.LogonDialog;
import org.radixware.schemas.eas.CreateSessionRs;
import org.radixware.schemas.eas.Definition;
import sun.misc.BASE64Decoder;

public class DesignerClientEnvironment implements IClientEnvironment {

    private static class DummyEventLoop implements IEventLoop {

        private final AtomicBoolean isInProgress = new AtomicBoolean(false);
        private final AtomicBoolean wasFinished = new AtomicBoolean(false);
        private final List<Runnable> scheduledTasks = new LinkedList<>();

        @Override
        public void scheduleTask(final Runnable task) {
            if (task != null) {
                scheduledTasks.add(task);
            }
        }

        @Override
        public void start() {
            isInProgress.set(true);
            wasFinished.set(false);
            for (Runnable task : scheduledTasks) {
                try {
                    task.run();
                } catch (RuntimeException exception) {
                    Logger.getLogger(DesignerClientEnvironment.class.getName()).log(Level.SEVERE, null, exception);
                }
            }
            scheduledTasks.clear();
            while (!wasFinished.get()) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DesignerClientEnvironment.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            isInProgress.set(false);
        }

        @Override
        public void stop() {
            wasFinished.set(true);
        }

        @Override
        public boolean isInProgress() {
            return isInProgress.get();
        }
    }
    private String userName;
    private String stationName;
    private TimeZoneInfo serverTimeZoneInfo;
    private final EasSession session;
    private List<Id> contextlessCommands = null;
    private Set<EDrcServerResource> allowedResources = null;
    private DesignerConfigStore configStore = new DesignerConfigStore();
    private DesignerProgressHandleManager progressHandleManager = new DesignerProgressHandleManager();
    private final DesignerClientApplication application = new DesignerClientApplication(this);
    private ConnectionOptions predefinedConnection;
    private ConnectionOptions.SslOptions sslOptions;
    private boolean connected = false;
    private List<IClientEnvironment.ConnectionListener> connectionListeners;

    public DesignerClientEnvironment(EasSession src) {
        session = new EasSession(this);
    }

    public DesignerClientEnvironment() {
        session = new EasSession(this);
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getStationName() {
        return stationName;
    }

    @Override
    public TimeZoneInfo getServerTimeZoneInfo() {
        return serverTimeZoneInfo == null ? new TimeZoneInfo(TimeZone.getDefault(), getLocale()) : serverTimeZoneInfo;
    }

    @Override
    public Timestamp getCurrentServerTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IResourceManager getResourceManager() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MessageProvider getMessageProvider() {
        return application.getMessageProvider();
    }

    @Override
    public EIsoLanguage getLanguage() {
        return application.getClientLanguage();
    }

    @Override
    public EIsoCountry getCountry() {
        return EIsoCountry.AFGHANISTAN;
    }

    @Override
    public Locale getLocale() {
        return Locale.getDefault();
    }

    @Override
    public IClientApplication getApplication() {
        return application;
    }

    @Override
    public ClientTracer getTracer() {
        return application.getTracer();
    }

    @Override
    public String getTraceProfile() {
        return "Debug";
    }

    @Override
    public String getTraceFile() {
        return null;
    }

    @Override
    public void processException(String title, final Throwable e) {
        if (e instanceof InterruptedException) {
            return;
        }
        for (Throwable err = e; err != null && err.getCause() != err; err = err.getCause()) {
            if (err instanceof UnsupportedDefinitionVersionError) {
                processUnsupportedVersionException();
                return;
            }
            if (err instanceof CantUpdateVersionException) {
                final String ttl = getMessageProvider().translate("ExplorerMessage", "Cant update version");
                final String message = getMessageProvider().translate("ExplorerMessage",
                        "Current version is no longer supported. Application restart required.\n"
                        + "Please, export all unsaved works to local files and restart application");
                messageError(title, message);
                return;
            }
        }
        getTracer().put(e);
        final ExceptionMessage exceptionMessage = new ExceptionMessage(this, e);
        try {
            if (exceptionMessage.hasDialogMessage()) {
                exceptionMessage.display(title, null);
            }
        } finally {
            if (exceptionMessage.getSeverity() == EEventSeverity.ALARM) {
                disconnect();
            }
        }
    }

    private void processUnsupportedVersionException() {
        getDefManager().getAdsVersion().makeUnsupported();
        if (!getDefManager().getAdsVersion().isNewVersionAvailable()) {
            getTracer().warning(getMessageProvider().translate("TraceMessage", "Current definition version is not supported by server, but client is not in old version mode"));
            if (getDefManager().getAdsVersion().checkForUpdates(this) == null) {
                getTracer().error(getMessageProvider().translate("TraceMessage", "Current definition version is not supported by server, but client have not newer version"));
                messageError(getMessageProvider().translate("ExplorerError", "Current version is no longer supported"));
                return;
            }
        }

        if (getDefManager().getAdsVersion().isKernelWasChanged()) {
            final String title = getMessageProvider().translate("ExplorerMessage", "Restart Application");
            final String message = getMessageProvider().translate("ExplorerMessage",
                    "Current version is no longer supported. It is impossible to continue work until restart.\n"
                    + "Please, close application and start it again");
            if (messageConfirmation(title, message)) {
                LifecycleManager.getDefault().exit();
            }
            return;
        }

        final String title = getMessageProvider().translate("ExplorerMessage", "Confirm to Update Version");
        final String message = getMessageProvider().translate("ExplorerMessage",
                "Current version is no longer supported. It is impossible to continue work until updates will be installed.\n"
                + "Do you want to update now (all your unsaved data will be lost) ?");

        if (messageConfirmation(title, message)) {
            getDefManager().getAdsVersion().checkForUpdates(this);
            try {
                getDefManager().getAdsVersion().updateToNewVersion();
            } catch (CantUpdateVersionException ex) {
                ex.showMessage(this);
            } catch (Exception ex) {
                processException("", ex);
            }
        }
    }

    @Override
    public void messageInformation(String title, String message) {
        DialogUtils.messageInformation(message);
    }

    private void disconnect() {
        session.close(false);
        connected = false;
        serverTimeZoneInfo = null;
        notifyDisconnected(false);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LifecycleManager.getDefault().exit();
            }
        });
    }

    @Override
    public void messageError(String message) {
        DialogUtils.messageError(message);
    }

    @Override
    public void messageError(String title, String message) {
        DialogUtils.messageError(message);
    }

    @Override
    public void messageWarning(String message) {
        DialogUtils.messageInformation(message);
    }

    @Override
    public void messageWarning(String title, String message) {
        DialogUtils.messageInformation(message);
    }

    @Override
    public void messageException(String title, String message, Throwable ex) {
        DialogUtils.messageError(ex);
    }

    @Override
    public boolean messageConfirmation(String title, String message) {
        return DialogUtils.messageConfirmation(message);
    }

    @Override
    public EDialogButtonType messageBox(String string, String string1, EDialogIconType edit, Set<EDialogButtonType> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IMessageBox newMessageBoxDialog(String string, String string1, EDialogIconType edit, Set<EDialogButtonType> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void alarmBeep() {
    }

    public Connections getConnections() {
        if (predefinedConnection != null) {
            return new DesignerConnections(this, predefinedConnection);
        } else {
            return new DesignerConnections(this, new File(getWorkPath()));
        }
    }

    public boolean connect(char[] password) {
        try {
            if (!connected) {
//                
//                CreateSessionRs response = null;
//                if (src != null) {
//                    response = session.copy(src, myId);
//                    if (response == null) {
//                        DialogUtils.messageError("Unable to connect to server");
//                        LifecycleManager.getDefault().exit();
//                    }
//                } else {
                final Id myId = Id.Factory.loadFrom("parP2VNBBVCGNEF7DYDM6UE3E6WLI");
                CreateSessionRs response = connectAutonomely(password, myId);

                if (response != null) {
                    setContextlessCommands(response.getContextlessCommands());
                    setServerResources(response.getServerResources());
                    final org.radixware.schemas.eas.TimeZone timeZone = response.getServerTimeZone();
                    serverTimeZoneInfo = timeZone == null ? null : TimeZoneInfo.parse(timeZone);
                    getTracer().getBuffer().setMaxSize(getConfigStore().readInteger(SettingNames.SYSTEM + "_TRACE_MAX_SIZE", 500));
                    connected = true;
                    notifyConnected();
                    return true;
                } else {
                    DialogUtils.messageError("Connection cancelled by user");
                    LifecycleManager.getDefault().exit(1);
                    return false;
                }
            } else {
                return true;
            }
        } catch (Throwable e) {
            cleanup();
            processException("", e);
            return false;
        }
    }

    @Override
    public void processException(Throwable thrwbl) {
        processException("", thrwbl);
    }

    private static final class KerberosCredentialsProvider implements IKerberosCredentialsProvider {

        private final String userName;
        private final char[] password;
        private final String spn;

        public KerberosCredentialsProvider(final String userName, final char[] password, final String spn) {
            this.userName = userName;
            this.password = password == null ? null : Arrays.copyOf(password, password.length);
            this.spn = spn;
        }

        @Override
        public KerberosCredentials createCredentials(final IClientEnvironment environment) throws InterruptedException, KerberosError {
            final CallbackHandler handler = new CallbackHandler() {
                @Override
                public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                    for (Callback cb : callbacks) {
                        if (cb instanceof PasswordCallback) {
                            if (password == null) {

                                IEnterPasswordDialog pwdDialog = environment.getApplication().getDialogFactory().newEnterPasswordDialog(environment);
                                pwdDialog.setMessage("Please enter domain (kerberos) password:");
                                if (pwdDialog.execDialog() == IDialog.DialogResult.ACCEPTED) {
                                    ((PasswordCallback) cb).setPassword(pwdDialog.getPassword().toCharArray());
                                } else {
                                    DialogUtils.messageError("Authentication cancelled by user");
                                    LifecycleManager.getDefault().exit();
                                }
                            } else {
                                ((PasswordCallback) cb).setPassword(password);
                            }
                        }
                    }
                }
            };
            try {
                return createKerberosClientCredentials(userName, spn, handler);
            } catch (Exception exception) {
                throw new KerberosError(exception);
            }
        }

        private static KerberosCredentials createKerberosClientCredentials(final String userName, final String spn, final CallbackHandler pwdCallbackHandler) throws KerberosException {
            final KerberosLoginConfiguration loginConfig = createKerberosLoginConfiguration(userName, pwdCallbackHandler);
            return KerberosCredentials.Factory.newClientCredentials(userName,
                    spn,
                    loginConfig,
                    GSSCredential.DEFAULT_LIFETIME);
        }

        private static KerberosLoginConfiguration createKerberosLoginConfiguration(final String userName, final CallbackHandler pwdCallbackHandler) {
            final Map<String, String> parameters = new HashMap<>();
            parameters.put("useTicketCache", "true");
            parameters.put("principal", userName);
            return KerberosLoginConfiguration.Factory.newInstance("Explorer", parameters, pwdCallbackHandler);
        }
    }

    private CreateSessionRs connectAutonomely(char[] pwd, Id myId) throws IllegalUsageError {
        //first try connect using parent settings
        String un = RunParams.getUserName();
        String connectionName = RunParams.getConnectionName();

        if (connectionName != null) {
            final ConnectionOptions connection = getConnections().findByName(connectionName);
            if (connection != null) {
                sslOptions = connection.getSslOptions() == null ? null : new ConnectionOptions.SslOptions(connection.getSslOptions());
                final EasClientProvider provider = new EasClientProvider(this, connection);
                final String connectionStationName = connection.getStationName();
                final EAuthType authType = connection.getAuthType();
                for (;;) {
                    try {
                        final char[] password;
                        final byte[] pwdHash;
                        if (pwd != null) {
                            BASE64Decoder decoder = new BASE64Decoder();
                            final byte[] bytes = decoder.decodeBuffer(new String(pwd));
                            switch (authType) {
                                case PASSWORD: {
                                    pwdHash = new TokenProcessor().decryptBytes(bytes);
                                    password = null;
                                }
                                break;
                                default: {
                                    password = new TokenProcessor().decrypt(bytes);
                                    pwdHash = null;

                                }
                                break;
                            }
                        } else if (authType == EAuthType.CERTIFICATE || authType == EAuthType.PASSWORD) {
                            pwdHash = null;
                            IEnterPasswordDialog pwdDialog = getApplication().getDialogFactory().newEnterPasswordDialog(DesignerClientEnvironment.this);
                            if (authType == EAuthType.PASSWORD) {
                                pwdDialog.setMessage("Please enter password for user " + un + ":");
                            } else {
                                pwdDialog.setMessage("Please enter keystore password");
                            }
                            if (pwdDialog.execDialog() == IDialog.DialogResult.ACCEPTED) {
                                password = pwdDialog.getPassword().toCharArray();
                            } else {
                                DialogUtils.messageError("Authentication cancelled by user");
                                LifecycleManager.getDefault().exit();
                                return null;//???
                            }
                        } else {
                            pwdHash = null;
                            password = null;
                        }

                        final IEasClient easClient;
                        if (authType == EAuthType.CERTIFICATE) {
                            easClient = provider.createEasClient(password);
                        } else {
                            easClient = provider.createEasClient(new char[]{});
                        }

                        try {
                            if (authType == EAuthType.CERTIFICATE || authType == EAuthType.PASSWORD) {
                                try {
                                    userName = un;
                                    final CreateSessionRs response;
                                    if (pwdHash == null) {
                                        response = session.open(easClient, connectionStationName, un, new String(password), authType, myId);
                                    } else {
                                        response = session.open(easClient, connectionStationName, un, pwdHash, myId);
                                    }
                                    userName = response.getUser();
                                    return response;
                                } catch (ServiceClientException | EasError | InterruptedException ex) {
                                    processException("", ex);
                                }
                            } else {
                                userName = un;
                                final String spn = connection.getKerberosOptions().getServicePrincipalName();
                                final KerberosCredentialsProvider krbCredsProvider
                                        = new KerberosCredentialsProvider(userName, password, spn);
                                try {
                                    final CreateSessionRs response
                                            = session.open(easClient, connectionStationName, krbCredsProvider, null, myId, null);
                                    userName = response.getUser();
                                    return response;
                                } catch (KerberosError | ServiceClientException | EasError | InterruptedException ex) {
                                    cleanup();
                                    processException("", ex);
                                }
                            }
                        } finally {
                            if (password != null) {
                                Arrays.fill(password, ' ');
                            }
                            if (pwdHash != null) {
                                Arrays.fill(pwdHash, (byte) 0);
                            }

                        }
                    } catch (IOException | KeystoreControllerException | CertificateUtilsException ex) {
                        processException("", ex);
                        return null;
                    }
                }
            }
        }

        loop:
        for (;;) {
            LogonDialog dialog = new LogonDialog(this);
            dialog.setUserName(un);

            if (!dialog.show()) {
                return null;
            }

            String password = dialog.getPassword();
            un = dialog.getUserName();
            ConnectionOptions connection = dialog.getConnection();
            if (connection == null) {
                final String message = getMessageProvider().translate("ExplorerMessage", "No connection found for user '%s'");
                messageError(getMessageProvider().translate("ExplorerMessage", "Connection Error"), String.format(message, un));
                continue;
            }
            String sn = connection.getStationName();
            EAuthType authType = EAuthType.PASSWORD;
            sslOptions = (connection.getSslOptions() == null) ? null : new ConnectionOptions.SslOptions(connection.getSslOptions());

            if (!connection.hasResolvedAddress()) {
                if (connection.getInitialServerAddresses().size() == 1) {
                    final String message = getMessageProvider().translate("ExplorerMessage", "Unknown server '%s'");
                    final String hostname = connection.getInitialServerAddresses().get(0).getHostName();
                    messageError(getMessageProvider().translate("ExplorerMessage", "Connection Error"), String.format(message, hostname));
                } else {
                    final String message = getMessageProvider().translate("ExplorerMessage", "Server was not found");
                    messageError(getMessageProvider().translate("ExplorerMessage", "Connection Error"), message);
                }
                continue;
            }
//            if (connection.getLanguage() != getLanguage()) {
//                //setupLanguage(connection.getLanguage());
//                //afterLanguageChange();
//                //mainWindow.setWindowTitle(getMessageProvider().translate("MainWindow", "RadixWare Explorer"));
//                //Application.setStatusBarLabel(getMessageProvider().translate("StatusBar", "Connecting..."));
//            }
            connection.setUserName(un);
            //connection.setStationName(stationName);
            connection.setLanguage(getLanguage());

            try {
                EasClientProvider provider = new EasClientProvider(this, connection);
                IEasClient easClient = provider.createEasClient(password.toCharArray());
                this.userName = un;
                this.stationName = sn;
                final CreateSessionRs response
                        = session.open(easClient, sn, un, password, authType, myId);
                userName = response.getUser();
                return response;
            } catch (ServiceClientException | EasError | InterruptedException | IllegalUsageError | KeystoreControllerException | CertificateUtilsException ex) {
                cleanup();
                processException("", ex);
                continue;
            }
        }
    }

    private void cleanup() {
        session.close(false);
    }

    @Override
    public IEasSession getEasSession() {
        return session;
    }

    @Override
    public SslOptions getSslOptions() {
        return sslOptions;
    }

    @Override
    public ProgressHandleManager getProgressHandleManager() {
        return progressHandleManager;
    }
    private Clipboard cb = new Clipboard(this);

    @Override
    public Clipboard getClipboard() {
        return cb;
    }

    @Override
    public FilterSettingsStorage getFilterSettingsStorage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ClientSettings getConfigStore() {
        return configStore;
    }

    @Override
    public ITextOptionsProvider getTextOptionsProvider() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    private GroupSettingsStorage groupSettingsStorage;

    @Override
    public GroupSettingsStorage getGroupSettingsStorage() {
        if (this.groupSettingsStorage == null) {
            this.groupSettingsStorage = new GroupSettingsStorage(this) {
                @Override
                protected String getStorageDescription() {
                    return "Designer group settings storage";
                }

                @Override
                protected ClientSettings createSettings() {
                    return new DesignerConfigStore();
                }
            };
        }
        return this.groupSettingsStorage;
    }

    @Override
    public IExplorerTreeManager getTreeManager() {
        return null;
    }

    @Override
    public IWidget getMainWindow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public IMainStatusBar getStatusBar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canViewAuditTable() {
        return isServerResourceAccessible(EDrcServerResource.VIEW_AUDIT);
    }

    @Override
    public boolean isContextlessCommandAccessible(final Id cmdId) {
        return contextlessCommands != null && contextlessCommands.contains(cmdId);
    }

    @Override
    public boolean isCustomFiltersAccessible() {
        return isServerResourceAccessible(EDrcServerResource.EAS_FILTER_CREATION);
    }

    @Override
    public boolean isCustomSortingsAccessible() {
        return isServerResourceAccessible(EDrcServerResource.EAS_SORTING_CREATION);
    }

    protected boolean isServerResourceAccessible(EDrcServerResource resource) {
        return allowedResources != null && allowedResources.contains(resource);
    }

    protected void setContextlessCommands(CreateSessionRs.ContextlessCommands commands) {
        contextlessCommands = new ArrayList();
        if (commands != null && commands.getItemList() != null) {
            for (Definition def : commands.getItemList()) {
                contextlessCommands.add(def.getId());
            }
        }
    }

    protected void setServerResources(CreateSessionRs.ServerResources resources) {
        allowedResources = EnumSet.noneOf(EDrcServerResource.class);
        if (resources != null && resources.getItemList() != null) {
            for (CreateSessionRs.ServerResources.Item item : resources.getItemList()) {
                try {
                    allowedResources.add(EDrcServerResource.getForValue(item.getId().toString()));
                } catch (NoConstItemWithSuchValueError err) {
                    final String message = getMessageProvider().translate("ExplorerError", "Unknown server resource \'%s\'");
                    getTracer().put(EEventSeverity.WARNING, String.format(message, item.getTitle()), EEventSource.EXPLORER);
                }
            }
        }
    }

    @Override
    public List<EIsoLanguage> getSupportedLanguages() {
        return Collections.<EIsoLanguage>emptyList();
    }

    @Override
    public ISqmlDefinitions getSqmlDefinitions() {
        return null;
    }

    @Override
    public String getWorkPath() {
        final String configPath = RunParams.getConfigPath();
        if (configPath == null || configPath.isEmpty()) {
            return SystemTools.getRadixApplicationDataPath(ERadixApplication.EXPLORER).getAbsolutePath();
        } else {
            File path = new File(configPath);
            if (path.isDirectory() || path.mkdirs()) {
                return path.getAbsolutePath();
            } else {
                return SystemTools.getRadixApplicationDataPath(ERadixApplication.EXPLORER).getAbsolutePath();
            }
        }
    }   

    @Override
    public <T> T runInGuiThread(final Callable<T> task) throws InterruptedException, ExecutionException {
        try{
            return task.call();
        }catch(Exception ex){
            throw new ExecutionException(ex);
        }
    }

    @Override
    public void runInGuiThreadAsync(final Runnable task) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DefManager getDefManager() {
        return application.getDefManager();
    }

    @Override
    public String setStatusBarLabel(String string) {
        StatusDisplayer.getDefault().setStatusText(string);
        return string;
    }

    @Override
    public IEventLoop newEventLoop() {
        return new DummyEventLoop();
    }

    @Override
    public String signText(final String string, final X509Certificate xc) throws SignatureException {
        final String message
                = getMessageProvider().translate("ExplorerError", "Failed to sign message: operation is not supported");
        throw new SignatureException(SignatureException.EReason.UNSUPPORTED_OPERATION, message);
    }

    private void notifyConnected() {
        if (connectionListeners != null) {
            final List<IClientEnvironment.ConnectionListener> listeners = new LinkedList<>(connectionListeners);
            for (IClientEnvironment.ConnectionListener listener : listeners) {
                listener.afterOpenConnection();
            }
        }
    }

    private void notifyDisconnected(final boolean forced) {
        if (connectionListeners != null) {
            final List<IClientEnvironment.ConnectionListener> listeners = new LinkedList<>(connectionListeners);
            for (IClientEnvironment.ConnectionListener listener : listeners) {
                listener.afterCloseConnection(forced);
            }
        }
    }

    @Override
    public void addConnectionListener(final ConnectionListener listener) {
        if (listener != null && (connectionListeners == null || !connectionListeners.contains(listener))) {
            if (connectionListeners == null) {
                connectionListeners = new LinkedList<>();
            }
            connectionListeners.add(listener);
        }
    }

    @Override
    public void removeConnectionListener(final ConnectionListener listener) {
        if (connectionListeners != null) {
            connectionListeners.remove(listener);
        }
    }
}
