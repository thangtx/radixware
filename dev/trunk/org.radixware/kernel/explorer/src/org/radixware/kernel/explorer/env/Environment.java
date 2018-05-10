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

package org.radixware.kernel.explorer.env;

import com.trolltech.qt.core.QDir;
import com.trolltech.qt.core.QSettings;
import com.trolltech.qt.core.QTimer;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.apache.xmlbeans.XmlException;
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
import org.radixware.kernel.common.client.eas.resources.IResourceManager;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.env.EnvironmentVariables;
import org.radixware.kernel.common.client.env.IEventLoop;
import org.radixware.kernel.common.client.env.ITaskWaiter;
import org.radixware.kernel.common.client.env.LocaleManager;
import org.radixware.kernel.common.client.env.ProductInstallationOptions;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.env.progress.ProgressHandleManager;
import org.radixware.kernel.common.client.errors.AccessViolationError;
import org.radixware.kernel.common.client.errors.CredentialsWasNotDefinedError;
import org.radixware.kernel.common.client.errors.EasError;
import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.errors.KerberosError;
import org.radixware.kernel.common.client.errors.SqmlIsNotAccessibleError;
import org.radixware.kernel.common.client.errors.WrongPasswordError;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.ExceptionMessage;
import org.radixware.kernel.common.client.exceptions.KernelClassModifiedException;
import org.radixware.kernel.common.client.exceptions.SignatureException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitions;
import org.radixware.kernel.common.client.models.groupsettings.FilterSettingsStorage;
import org.radixware.kernel.common.client.models.groupsettings.GroupSettingsStorage;
import org.radixware.kernel.common.client.trace.JavaProxyLogger;
import org.radixware.kernel.common.client.tree.IExplorerTreeManager;
import org.radixware.kernel.common.client.types.ExplorerRoot;
import org.radixware.kernel.common.client.types.TimeZoneInfo;
import org.radixware.kernel.common.client.utils.CryptoUtils;
import org.radixware.kernel.common.client.utils.ISecretStore;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EAuthType;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.enums.EDrcServerResource;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EIsoCountry;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EKeyStoreType;
import org.radixware.kernel.common.enums.ERadixApplication;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.dialogs.ExplorerMessageBox;
import org.radixware.kernel.explorer.dialogs.LogonDialog;
import org.radixware.kernel.explorer.dialogs.trace.TraceDialog;
import org.radixware.kernel.explorer.env.session.Connections;
import org.radixware.kernel.explorer.text.DefaultTextOptionsProvider;
import org.radixware.kernel.explorer.tree.ExplorerTreeManager;
import org.radixware.kernel.common.client.utils.Pkcs11Token;
import org.radixware.kernel.common.client.utils.ThreadDumper;
import org.radixware.kernel.common.client.widgets.IMainStatusBar;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.utils.RequestProcessor;
import org.radixware.kernel.explorer.Explorer;
import org.radixware.kernel.explorer.env.trace.ExplorerCompressableTraceBuffer;
import org.radixware.kernel.explorer.env.trace.ExplorerTracer;
import org.radixware.kernel.explorer.utils.LeakedWidgetsDetector;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.MainWindow;
import org.radixware.kernel.explorer.webdriver.WebDrvServer;
import org.radixware.kernel.explorer.webdriver.WebDrvSessionsManager;
import org.radixware.kernel.explorer.widgets.propeditors.PropEditorsPool;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.kernel.starter.radixloader.RadixSVNLoader;
import org.radixware.schemas.clientstate.ConnectionParams;
import org.radixware.schemas.eas.CreateSessionRs;
import org.radixware.schemas.eas.Definition;


class Environment implements IClientEnvironment {

    private static final int POLL_INTERVAL = 2000;
    private final ExplorerTracer tracer;
    private final List<IClientEnvironment.ConnectionListener> connectionListeners = new LinkedList<>();
    private Connections connections;
    private TraceDialog traceDialog;
    private final Application app;
    private IExplorerTreeManager treeManager = null;
    private final EasSession easSession;
    private TimeZoneInfo serverTimeZoneInfo;
    private ProductInstallationOptions productInstallationOptions;
    final ExplorerLocaleManager localeManager;
    QDir workPath;
    ConnectionOptions connection;
    private String connectingUserName;
    private List<Id> contextlessCommands;
    private List<EDrcServerResource> allowedResources;
    private final org.radixware.kernel.explorer.env.progress.ExplorerProgressHandleManager progressHandleManager;
    private final Clipboard clipboard = new Clipboard(this);
    private ClientSettings configStore;
    private final MainWindow mainWindow;
    private final ResponseNotificationScheduler easResponseNotificator;
    private final ExplorerStatusBar statusBar;
    private final QTimer tokenPoller;
    private Pkcs11Token token;
    private final ISecretStore pwdStore;
    private final FilterSettingsStorage filterSettingsStorage = new FilterSettingsStorage();
    private ISqmlDefinitions sqmlDefinitions;
    private SqmlLoadingTask sqmlLoadingTask;
    private Thread sqmlLoadingThread;    
    private final ExplorerCompressableTraceBuffer traceBuffer = new ExplorerCompressableTraceBuffer();
    private org.radixware.schemas.clientstate.ClientStateDocument clientStateXml;

    TraceDialog getTraceDialog() {
        if (traceDialog == null) {
            traceDialog = new TraceDialog(this, mainWindow);
        }
        return traceDialog;
    }

    void closeTraceDialog() {
        if (traceDialog != null) {
            traceDialog.close();
            traceDialog = null;
        }
    }

    @SuppressWarnings("LeakingThisInConstructor")
    Environment(final Application app, final MainWindow mainWindow, final byte[] appState) {
        this.app = app;
        this.mainWindow = mainWindow;
        localeManager = new ExplorerLocaleManager(this, app.getCodeBase());
        progressHandleManager = new org.radixware.kernel.explorer.env.progress.ExplorerProgressHandleManager(this, app);
        pwdStore = app.newSecretStore();
                
        tracer = new ExplorerTracer(this, appState!=null && appState.length>0);
        tracer.registerStarterLogger();        
        tracer.setBuffer(traceBuffer);
        JavaProxyLogger.addProxyForSource("com.trolltech.qt", this);
        JavaProxyLogger.addProxyForSource("org.radixware.kernel", this);

        if (mainWindow == null) {
            statusBar = null;
        } else {
            statusBar = new ExplorerStatusBar();
            mainWindow.setStatusBar(statusBar);
        }

        setupLocale(RunParams.getLanguage(), RunParams.getCountry());
        initSettings(RunParams.getConfigPath());
        easResponseNotificator = new ResponseNotificationScheduler(this);
        easSession = new EasSession(this, pwdStore, easResponseNotificator);
        resourceManager = new org.radixware.kernel.explorer.env.session.resources.ExplorerResourceManager(this);

        tokenPoller = new QTimer(app);
        tokenPoller.setInterval(POLL_INTERVAL); //FIX magic constant to nonmagic
        tokenPoller.timeout.connect(this, "onPollerTimeout()");
        
        if (appState!=null && appState.length>0){
            try{
                clientStateXml = 
                    org.radixware.schemas.clientstate.ClientStateDocument.Factory.parse(new ByteArrayInputStream(appState));
            }catch(XmlException | IOException exception){
                final String message = app.getMessageProvider().translate("TraceMessage","Failed to read application state");
                tracer.error(message, exception);
                clientStateXml = null;
            }
        }
    }

    Connections getConnections() {
        if (connections==null){
            //RADIX-9272. Creating here to be able to process exception on read connections.xml file
            connections = new Connections(this, new File(workPath.absolutePath()));
        }
        return connections;
    }
    private boolean initingTraceDialog = false;

    final void onAdsVersionUpdate() {
        if (!initingTraceDialog) {
            getTraceDialog().init();
        }
        EnvironmentVariables.clearAll(this);
        PropEditorsPool.getInstance().clear();
        final boolean preloadBranch = 
            app.isExtendedMetaInformationAccessible() && RunParams.isExtendedMetaInformationPreloadEnabled();
        if (app.isSqmlAccessible() && RunParams.isSqmlPreloadEnabled()){
            startSqmlLoadingThread(Thread.MIN_PRIORITY,preloadBranch);
        }else if (preloadBranch){
            getDefManager().getRepository().preloadBranch();
        }
        sqmlDefinitions = null;
        clipboard.clear();
    }

    @Override
    public String getUserName() {
        return connection == null ? "" : connection.getUserName();
    }

    @Override
    public String getStationName() {
        return connection == null ? "" : connection.getStationName();
    }

    @Override
    public TimeZoneInfo getServerTimeZoneInfo() {
        return serverTimeZoneInfo == null ? new TimeZoneInfo(TimeZone.getDefault(), getLocale()) : serverTimeZoneInfo;
    }

    @Override
    public Timestamp getCurrentServerTime() {
        if (serverTimeZoneInfo == null) {
            return new Timestamp(Calendar.getInstance().getTimeInMillis());
        } else {
            final int selfTimeZoneOffsetMills =
                    Calendar.getInstance().get(Calendar.DST_OFFSET) + Calendar.getInstance().get(Calendar.ZONE_OFFSET);
            final long offsetDiffMills = getServerTimeZoneInfo().getOffsetMills() - selfTimeZoneOffsetMills;
            return new Timestamp(Calendar.getInstance().getTimeInMillis() + offsetDiffMills);
        }
    }
    private final org.radixware.kernel.explorer.env.session.resources.ExplorerResourceManager resourceManager;

    @Override
    public IResourceManager getResourceManager() {
        return resourceManager;
    }

    @Override
    public MessageProvider getMessageProvider() {
        return app.getMessageProvider();
    }

    @Override
    public EIsoLanguage getLanguage() {
        return localeManager.getLanguage();
    }

    @Override
    public EIsoCountry getCountry() {
        return localeManager.getCountry();
    }

    @Override
    public Locale getLocale() {
        return localeManager.getLocale();
    }

    @Override
    public IClientApplication getApplication() {
        return app;
    }

    @Override
    public ExplorerTracer getTracer() {
        return tracer;
    }

    @Override
    public String getTraceProfile() {
        return RunParams.getTraceProfile();
    }

    @Override
    public String getTraceFile() {
        return RunParams.getTraceFile();
    }

    @Override
    public void processException(Throwable e) {
        Application.getInstance().processException(e);
    }

    @Override
    public void processException(final String title, Throwable e) {
        Application.getInstance().processException(title, e);
    }

    @Override
    public void messageInformation(String title, String message) {
        Application.messageInformation(title, message);
    }

    @Override
    public void messageError(String message) {
        Application.messageError(message);
    }

    @Override
    public void messageError(String title, String message) {
        Application.messageError(title, message);
    }

    @Override
    public void messageWarning(String message) {
        Application.messageWarning(message);
    }

    @Override
    public void messageWarning(String title, String message) {
        Application.messageWarning(title, message);
    }

    @Override
    public void messageException(String title, String message, Throwable e) {
        Application.messageException(title, message, e);
    }

    @Override
    public boolean messageConfirmation(String title, String message) {
        return Application.messageConfirmation(title, message);
    }

    @Override
    public EDialogButtonType messageBox(String message, String title, EDialogIconType icon, Set<EDialogButtonType> buttons) {
        return Application.messageBox(message, title, icon, buttons);
    }

    @Override
    public void alarmBeep() {
        Application.beep();
    }

    @Override
    public IEasSession getEasSession() {
        return easSession;
    }

    @Override
    public SslOptions getSslOptions() {
        return connection == null ? null : connection.getSslOptions();
    }

    @Override
    public String getConnectionName() {
        return connection == null ? null : connection.getName();
    }        

    @Override
    public ProgressHandleManager getProgressHandleManager() {
        return progressHandleManager;
    }    

    @Override
    public Clipboard getClipboard() {
        return clipboard;
    }
    

    @Override
    public FilterSettingsStorage getFilterSettingsStorage() {
        return filterSettingsStorage;
    }

    private void initSettings(final String configPath) {
        if (configPath == null || configPath.isEmpty()) {
            final File dir = SystemTools.getRadixApplicationDataPath(ERadixApplication.EXPLORER);
            if (dir.exists() || dir.mkdirs()) {
                workPath = new QDir(dir.getAbsolutePath());
            }
        } else {
            workPath = new QDir(configPath);
        }
        if (!workPath.exists() && !workPath.mkpath(workPath.absolutePath())) {
            workPath = null;
            configStore = new ExplorerSettings(this, mainWindow);
        } else {
            QSettings.setPath(QSettings.Format.IniFormat, QSettings.Scope.UserScope, workPath.canonicalPath());
            configStore = new ExplorerSettings(this, QSettings.Format.IniFormat, QSettings.Scope.UserScope, "settings");
        }
    }

    @Override
    public ClientSettings getConfigStore() {
        return configStore;
    }

    @Override
    public DefaultTextOptionsProvider getTextOptionsProvider() {
        return DefaultTextOptionsProvider.getInstance();
    }
    private GroupSettingsStorage groupSettingStorage;

    @Override
    public GroupSettingsStorage getGroupSettingsStorage() {
        if (groupSettingStorage == null) {
            groupSettingStorage = new GroupSettingsStorage(this) {
                @Override
                protected String getStorageDescription() {
                    return settings.getDescription();
                }

                @Override
                protected ClientSettings createSettings() {
                    final String filePath = getWorkPath() + "/group-settings.ini";
                    return new ExplorerSettings(Environment.this, filePath, com.trolltech.qt.core.QSettings.Format.IniFormat, null, -1);
                }
            };
        }
        return groupSettingStorage;
    }

    @Override
    public IExplorerTreeManager getTreeManager() {
        synchronized (this) {
            if (treeManager == null) {
                treeManager = initTreeManager();
            }
            return treeManager;
        }
    }

    private IExplorerTreeManager initTreeManager() {
        final Iterator<IExplorerTreeManager> ps = ServiceLoader.load(IExplorerTreeManager.class).iterator();
        if (ps.hasNext()) {
            return ps.next();
        } else {
            return new ExplorerTreeManager(this);
        }
    }

    @Override
    public String getWorkPath() {
        return workPath == null ? null : workPath.absolutePath();
    }

    @Override
    public IWidget getMainWindow() {
        return mainWindow;
    }

    @Override
    public boolean canViewAuditTable() {
        return isServerResourceAccessible(EDrcServerResource.VIEW_AUDIT);
    }

    protected void setContextlessCommands(CreateSessionRs.ContextlessCommands commands) {
        contextlessCommands = new ArrayList<>();
        if (commands != null && commands.getItemList() != null) {
            for (Definition def : commands.getItemList()) {
                contextlessCommands.add(def.getId());
            }
        }
    }

    protected void setServerResources(CreateSessionRs.ServerResources resources) {
        allowedResources = new ArrayList<>();
        if (resources != null && resources.getItemList() != null) {
            for (CreateSessionRs.ServerResources.Item item : resources.getItemList()) {
                try {
                    allowedResources.add(EDrcServerResource.getForValue(item.getId().toString()));
                } catch (NoConstItemWithSuchValueError err) {
                    final String message = getMessageProvider().translate("ExplorerError", "Unknown server resource \'%s\'");
                    tracer.put(EEventSeverity.WARNING, String.format(message, item.getTitle()), EEventSource.EXPLORER);
                }
            }
        }
    }
    
    boolean connect(){
        try{
            return connectImpl();
        }finally{
            clientStateXml = null;
        }
    }

    private boolean connectImpl() {
        setStatusBarLabel(getMessageProvider().translate("StatusBar", "Connecting..."));        
        ConnectionParams connectionParams;        
        if (clientStateXml==null || clientStateXml.getClientState()==null){
            connectionParams = null;
        }else{
            connectionParams = clientStateXml.getClientState().getConnection();            
        }
        Long replacedSessionId = null;
        final boolean isWebDrvServerStarted = WebDrvServer.getInstance().started();
        while (true) {
            if (connectionParams==null){
                pwdStore.clearSecret();
            }else{
                if (connectionParams.isSetEncSessionKey()){
                    pwdStore.setSecret(connectionParams.getEncSessionKey());
                }
                final byte[] encSessionId = connectionParams.getEncSessionId();
                if (encSessionId!=null && encSessionId.length>0){
                    final byte[] decryptedSessionId;
                    if (pwdStore.isEmpty()){
                        decryptedSessionId = 
                               CryptoUtils.decrypt3Des(connectionParams.getEncSessionId(), new byte[]{});
                    }else{
                        decryptedSessionId = 
                               CryptoUtils.decrypt3Des(connectionParams.getEncSessionId(), pwdStore);
                    }
                    final ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE/Byte.SIZE);            
                    buffer.put(decryptedSessionId, 0, decryptedSessionId.length);
                    buffer.flip();//need flip 
                    replacedSessionId = buffer.getLong();                
                }
            }
            final LogonDialog logonDialog = createLogonDialog(connectionParams);
            connectionParams = null;            
            if (logonDialog.execDialog() != DialogResult.ACCEPTED) {
                break;
            }
            try {
                connection = logonDialog.getConnection();
                connectingUserName  = logonDialog.getUserName();
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
                if (setupLocale(connection.getLanguage(), connection.getCountry())) {
                    mainWindow.setWindowTitle(getMessageProvider().translate("MainWindow", "RadixWare Explorer"));
                    setStatusBarLabel(getMessageProvider().translate("StatusBar", "Connecting..."));
                }
                tracer.getProfile().setDefaultSeverity(connection.getEventSeverity());
                
                if (RadixLoader.getInstance() instanceof RadixSVNLoader){
                    try{
                        ((RadixSVNLoader)RadixLoader.getInstance()).ensureConnected();
                    }catch(RadixLoaderException exception){
                        tracer.error(exception);
                        final String title = getMessageProvider().translate("ExplorerMessage", "Connection Problem");
                        final String message = getMessageProvider().translate("ExplorerMessage", "Connection to subversion server has been lost.\nApplication may work unstable.\nDo you want to continue?");
                        if (!messageConfirmation(title, message)){
                            continue;
                        }
                    }
                }
                final boolean preloadBranch = 
                    app.isExtendedMetaInformationAccessible() && RunParams.isExtendedMetaInformationPreloadEnabled();
                if (app.isSqmlAccessible() && RunParams.isSqmlPreloadEnabled()){
                    runInGuiThreadAsync(new Runnable() {
                        @Override
                        public void run() {
                            if (sqmlLoadingThread == null) {
                                startSqmlLoadingThread(Thread.MIN_PRIORITY,preloadBranch);
                            }
                        }
                    });
                }else if (preloadBranch){
                    getDefManager().getRepository().preloadBranch();
                }
                final IEasClient client;
                final CreateSessionRs response;                
                try {
                    client = logonDialog.createEasClient(connection);
                    final EAuthType authType = connection.getAuthType();
                    if (authType == EAuthType.KERBEROS) {
                        final String spn = connection.getKerberosOptions().getServicePrincipalName();
                        final IKerberosCredentialsProvider krbCredsProvider =
                                new KerberosCredentialsProvider(logonDialog.getUserName(), pwdStore, spn);
                        final IProgressHandle ph = getProgressHandleManager().newProgressHandle();
                        ph.startProgress(getMessageProvider().translate("StatusBar", "Connecting..."), false);
                        try {
                            response = easSession.open(client, 
                                                                       connection.getStationName(), 
                                                                       krbCredsProvider, 
                                                                       null, 
                                                                       getInitialExplorerRootId(connection), 
                                                                       null,
                                                                       replacedSessionId,
                                                                       isWebDrvServerStarted);
                        } catch (KerberosError error) {
                            rejectConnection();
                            processException(error);
                            continue;
                        } finally {
                            ph.finishProgress();
                        }
                    } else {
                        response = easSession.open(client, 
                                                                   connection.getStationName(),
                                                                   logonDialog.getUserName(), 
                                                                   null, 
                                                                   authType,
                                                                   getInitialExplorerRootId(connection),
                                                                   replacedSessionId,
                                                                   isWebDrvServerStarted);
                    }
                    connectingUserName = null;
                    connection.setConnectedUserName(response.getUser());
                } finally {
                    replacedSessionId = null;
                    logonDialog.clear();                    
                }
                final List<ExplorerRoot> explorerRoots =
                        ExplorerRoot.loadFromResponse(response.getExplorerRoots(), this);
                if (explorerRoots == null || explorerRoots.isEmpty()) {
                    messageError(getMessageProvider().translate("ExplorerMessage", "Connection failed"),
                            getMessageProvider().translate("ExplorerMessage", "You have no rights to use the system"));
                    rejectConnection();
                    continue;
                }
                /*
                 * final Id explorerRootId =
                 * connection.getExplorerRootId(explorerRoots); if
                 * (explorerRootId==null){ session.close();private continue; }
                 */

                setContextlessCommands(response.getContextlessCommands());
                setServerResources(response.getServerResources());
                final org.radixware.schemas.eas.TimeZone timeZone = response.getServerTimeZone();
                serverTimeZoneInfo = timeZone == null ? null : TimeZoneInfo.parse(timeZone);
                productInstallationOptions =  ProductInstallationOptions.loadOptions(response.getProductInstallationOptions());
                configStore.setConfigProfile(connection.getUserName() + "_" + connection.getStationName());
                configStore.beginGroup(SettingNames.SYSTEM);
                configStore.beginGroup(SettingNames.APP_STYLE);
                final String styleName = configStore.readString(SettingNames.STYLENAME, Application.getDefaultStyleName());
                configStore.endGroup();
                configStore.endGroup();
                Application.setStyle(styleName);
                getTracer().readProfileFromString((String) configStore.value(SettingNames.SYSTEM + "_TRACE_PROFILE", RunParams.getTraceProfile()));
                getTracer().getBuffer().setMaxSize(configStore.readInteger(SettingNames.SYSTEM + "_TRACE_MAX_SIZE", 500));

                app.mainToolBar.setHidden(true);
                LeakedWidgetsDetector.getInstance().init();
                if (RunParams.isDevelopmentMode()) {                    
                    final boolean isMemLeakDetectorEnabled =
                            configStore.readBoolean(SettingNames.SYSTEM + "/" + SettingNames.MEM_LEAK_DETECTOR, true);
                    app.getActions().memoryLeakDetector.setChecked(isMemLeakDetectorEnabled);
                    LeakedWidgetsDetector.getInstance().setEnabled(isMemLeakDetectorEnabled);
                }
                if (treeManager.openTree(explorerRoots, mainWindow, clientStateXml==null ? null : clientStateXml.getClientState()) == null) {
                    rejectConnection();
                    continue;
                }
                getTraceDialog().init();

                final String mainWindowTitleTemplate = getMessageProvider().translate("MainWindow", "%s Explorer - Connection \"%s\", User \"%s\", Station \"%s\"");
                final String topLayerName = Explorer.getProductName();
                final String mainWindowTitle = String.format(mainWindowTitleTemplate, topLayerName, connection.getName(), getUserName(), getStationName());
                mainWindow.setWindowTitle(mainWindowTitle);

                if (treeManager.getCurrentTree() == null || treeManager.getCurrentTree().getCurrent() == null) {
                    setStatusBarLabel(getMessageProvider().translate("StatusBar", "Connected"));
                }
                final Application.Actions appActions = app.getActions();
                appActions.runSettingsDialog.setEnabled(true);
                appActions.memoryLeakDetector.setEnabled(true);
                appActions.clearSettings.setEnabled(true);
                appActions.checkForUpdates.setEnabled(true);
                if (!response.isSetCanChangePassword() || response.getCanChangePassword()) {
                    appActions.changePassword.setEnabled(true);
                }
                if (RunParams.getTestOptionsFile() != null) {
                    app.startAutoTest(RunParams.getTestOptionsFile());
                }

                final SslOptions sslOptions = connection.getSslOptions();
                if (sslOptions != null && sslOptions.useSSLAuth() && sslOptions.getKeyStoreType() == EKeyStoreType.PKCS11) {
                    token =
                            Pkcs11Token.Factory.newInstance(sslOptions.getPkcs11Lib(), sslOptions.getSlotId(), this);
                    tokenPoller.start();
                }
                notifyConnected();
                return true;
            } catch (InterruptedException ex) {
                //connection operation was cancelled - no message need here.                
                treeManager.closeAll(true);
                RunParams.removeRestoringContextParam();
                rejectConnection();
            } catch (WrongPasswordError error) {
                if (error.getLocalizedMessage() != null && !error.getLocalizedMessage().isEmpty()) {
                    final String traceMessage =
                            getMessageProvider().translate("ExplorerMessage", "Unable to establish connection\n%1$s");
                    getTracer().event(String.format(traceMessage, error.getLocalizedMessage()));
                    messageError(getMessageProvider().translate("ExplorerMessage", "Connection Error"), error.getLocalizedMessage());
                }
                treeManager.closeAll(true);
                RunParams.removeRestoringContextParam();                
                rejectConnection();
            } catch (EasError error){
                final String reason;
                if (error instanceof IClientError){
                    reason = ((IClientError)error).getLocalizedMessage(getMessageProvider());
                }else{
                    reason = error.getLocalizedMessage();
                }                
                final EEventSeverity severity;
                if (error.getSouceFault()==null){
                    severity = EEventSeverity.ERROR;
                }else{
                    severity = ClientException.getFaultSeverity(error.getSouceFault());
                }
                final String traceMessage =
                        getMessageProvider().translate("ExplorerMessage", "Failed to establish connection") + "\n" + reason;
                getTracer().put(severity, traceMessage);
                final String title;
                if (error instanceof IClientError){
                    title = ((IClientError)error).getTitle(getMessageProvider());
                }else{
                    title = getMessageProvider().translate("ExplorerMessage", "Connection Problem");
                }
                messageError(title, reason);
                treeManager.closeAll(true);
                RunParams.removeRestoringContextParam();                
                rejectConnection();
            } catch (Exception ex) {
                if (!processExceptionOnConnect(ex)){
                    return false;
                }
            } catch (java.lang.ExceptionInInitializerError ex) {
                messageException(getMessageProvider().translate("ExplorerMessage", "Connection Error"), getMessageProvider().translate("ExplorerMessage", "Can't establish connection"), ex);
                treeManager.closeAll(true);
                RunParams.removeRestoringContextParam();                
                rejectConnection();
            }
        }
        connection = null;
        connectingUserName = null;
        setStatusBarLabel(getMessageProvider().translate("StatusBar", "Disconnected"));
        RunParams.removeRestoringContextParam();
        app.getActions().runSettingsDialog.setEnabled(false);
        app.getActions().memoryLeakDetector.setEnabled(false);
        app.getActions().clearSettings.setEnabled(false);
        app.mainToolBar.setHidden(false);
        if (connections!=null){
            connections.store();
        }
        return false;
    }    
    
    private LogonDialog createLogonDialog(final ConnectionParams connectionParams){
        final LogonDialog logonDialog = new LogonDialog(this, mainWindow, getConnections(), pwdStore);
        RunParams.clearPassword();//use this parameters only first time
        String connectionName = connectionParams==null ? null : connectionParams.getConnectionName();
        if (connectionName==null){
            connectionName = RunParams.getConnectionName();
        }
        if (connectionName!=null){
            logonDialog.setConnectionName(connectionName);
        }                        
        String userName = connectionParams==null ? null : connectionParams.getUserName();
        if (userName==null){
            userName = RunParams.getUserName();
        }
        if (userName!=null){
            logonDialog.setUserName(userName);
        }
        logonDialog.setAutoLogin(connectionParams!=null || RunParams.needToRestoreConnection());
        return logonDialog;
    }
    
    private boolean processExceptionOnConnect(final Exception ex){
        ServiceCallFault fault = null;
        for (Throwable exception = ex; exception != null; exception = exception.getCause()) {
            if (exception instanceof KernelClassModifiedException) {
                final KernelClassModifiedException kernelModifiedExc =
                        (KernelClassModifiedException) exception;
                if (kernelModifiedExc.restartScheduled()) {
                    //Если был RestartAppScheduledException, то на этот момент disconnect уже был вызван 
                    setStatusBarLabel(getMessageProvider().translate("StatusBar", "Disconnected"));
                    return false;
                }
                break;
            }else if (exception instanceof ServiceCallFault){
                fault = (ServiceCallFault)exception;
            }
        }
        if (KeystoreController.isIncorrectPasswordException(ex)) {           
            final String messageTitle = getMessageProvider().translate("ExplorerMessage", "Connection Error");
            final String message = getMessageProvider().translate("ExplorerError", "Password is invalid!");
            messageError(messageTitle, message);
            final String traceMessage = 
                getMessageProvider().translate("ExplorerMessage", "Unable to establish connection:\n%1$s");
            getTracer().event(String.format(traceMessage,message));
        } else if (fault instanceof AccessViolationError){
            final String messageTitle = getMessageProvider().translate("ExplorerMessage", "Unable to Establish Connection");
            final String messageText =
                getMessageProvider().translate("ExplorerMessage", "You do not have permission to login. Please contact your system administrator.");
            messageError(messageTitle, messageText);
        }else{
            final MessageProvider mp = getMessageProvider();
            final String messageTitle = mp.translate("ExplorerMessage", "Connection Error");
            final EEventSeverity severity = fault==null ? EEventSeverity.ERROR : ClientException.getFaultSeverity(fault);
            if (severity==EEventSeverity.ERROR){
                getTracer().error(mp.translate("ExplorerMessage", "Failed to establish connection"), ex);
            }else{
                final String traceMessage = 
                    getMessageProvider().translate("ExplorerMessage", "Unable to establish connection:\n%1$s");
                final String reason = new ExceptionMessage(this, fault).getDialogMessage();
                getTracer().put(severity, String.format(traceMessage, reason));
            }
            messageException(messageTitle, mp.translate("ExplorerMessage", "Unable to establish connection"), ex);
        }
        treeManager.closeAll(true);
        RunParams.removeRestoringContextParam();
        rejectConnection();  
        return true;
    }

    private void startSqmlLoadingThread(final int priority, final boolean preloadBranch) {
        sqmlLoadingTask = new SqmlLoadingTask(this, preloadBranch);
        sqmlLoadingThread = new Thread(sqmlLoadingTask, "SQML Loader");
        sqmlLoadingThread.setPriority(priority);
        sqmlLoadingThread.start();
    }
    
    private void stopSqmlLoadingThread(){
        if (sqmlLoadingThread != null) {
            if (sqmlLoadingThread.isAlive()) {
                sqmlLoadingTask.cancel();
                try {
                    waitForSqmlDefinitionsLoad();
                } catch (DefinitionError error) {//NOPMD
                    //ignore
                }
            } else {
                sqmlLoadingThread = null;
                sqmlLoadingTask = null;
            }
        }
        sqmlDefinitions = null;        
    }

    private void rejectConnection() {
        connectingUserName = null;
        easSession.close(false);
        pwdStore.clearSecret();
        if (connection != null) {
            connection.onClose();
        }
        configStore.setConfigProfile(null);
    }

    boolean disconnect() {
        return disconnect(false, false);
    }

    boolean disconnect(final boolean forced, final boolean onExit) {

        if (connection == null) {
            if (onExit){
                traceBuffer.stopCompressing();
                progressHandleManager.clear();
                closeRequestProcessor();
                stopSqmlLoadingThread();
                if (app.getDefManager().getAdsVersion() != null) {
                    app.getDefManager().getAdsVersion().clear();//stop branch loading thread
                }
            }
            return true;
        }

        SslOptions sslOptions = connection.getSslOptions();
        if (sslOptions != null && sslOptions.useSSLAuth() && sslOptions.getKeyStoreType() == EKeyStoreType.PKCS11) {
            tokenPoller.stop();
            token = null;
        }

        serverTimeZoneInfo = null;

        if (!forced && progressHandleManager.getActive() != null) {
            return false;
        }
        if (treeManager.closeAll(forced)) {            
            easSession.close(forced && onExit);
            pwdStore.clearSecret();
            RunParams.clearConnectionParams();
            connection.onClose();

            getTraceDialog().clear();
            getFilterSettingsStorage().clear();
            contextlessCommands = null;
            allowedResources = null;
            setStatusBarLabel(getMessageProvider().translate("StatusBar", "Disconnected"));
            app.getActions().clearSettings.setEnabled(false);
            app.getActions().runSettingsDialog.setEnabled(false);
            app.getActions().memoryLeakDetector.setEnabled(false);
            //actions.checkForUpdates.setEnabled(false);
            configStore.setValue(SettingNames.SYSTEM + "_TRACE_PROFILE", tracer.getProfile().toString());
            configStore.writeInteger(SettingNames.SYSTEM + "_TRACE_MAX_SIZE", tracer.getBuffer().getMaxSize());
            configStore.setConfigProfile(null);
            groupSettingStorage = null;
            if (connections!=null){
                connections.store();
            }
            if (configStore.isWritable()) {
                configStore.sync();
            }

            connection = null;
            if (!onExit && mainWindow != null) {
                if (!Objects.equals(Application.getDefaultStyleName(), Application.getCurrentStyleName())) {
                    Application.setStyle(Application.getDefaultStyleName());
                }
                mainWindow.setWindowTitle(getMessageProvider().translate("MainWindow", "RadixWare Explorer"));
                app.mainToolBar.setHidden(false);
            }
            notifyDisconnected(forced);
            stopSqmlLoadingThread();
            if (app.getDefManager().getAdsVersion() != null) {
                EnvironmentVariables.clearAll(this);
                if (!onExit){
                    PropEditorsPool.getInstance().clear();
                    WidgetUtils.CustomStyle.releaseAll();
                }
                app.getDefManager().getAdsVersion().clear();
            }
            if (onExit){
                traceBuffer.stopCompressing();
                progressHandleManager.clear();                
                closeRequestProcessor();
            }                        
            return true;
        }      
        return false;
    }
    
    private void closeRequestProcessor(){
        RequestProcessor.shutdown();
        try{
            RequestProcessor.awaitTermination(3, TimeUnit.SECONDS);
        }catch(InterruptedException exception){
            
        }
    }

    private Id getInitialExplorerRootId(ConnectionOptions connection) {
        if (ExplorerRoot.getDefault(this) != null) {
            return ExplorerRoot.getDefault(this).getId();
        } else {
            return connection.getExplorerRootId(null);
        }
    }

    void afterLanguageChange(final EIsoLanguage language) {
        app.afterLanguageChange(language);
        if (traceDialog != null) {
            initingTraceDialog = true;
            try {
                final boolean traceDialogIsVisible = traceDialog.isVisible();
                if (traceDialogIsVisible) {
                    traceDialog.close();
                }
                traceDialog = new TraceDialog(this, mainWindow);
                if (traceDialogIsVisible) {
                    traceDialog.show();
                }
            } finally {
                initingTraceDialog = false;
            }
        }
    }

    @Override
    public boolean isContextlessCommandAccessible(final Id cmdId) {
        return contextlessCommands != null && contextlessCommands.contains(cmdId);
    }

    @Override
    public boolean isCustomFiltersAccessible() {
        return isServerResourceAccessible(EDrcServerResource.EAS_FILTER_CREATION) && app.isSqmlAccessible();
    }

    @Override
    public boolean isCustomSortingsAccessible() {
        return isServerResourceAccessible(EDrcServerResource.EAS_SORTING_CREATION);
    }
    
    @Override
    public boolean isUserFuncDevelopmentAccessible() {
        return isServerResourceAccessible(EDrcServerResource.USER_FUNC_DEV);
    }

    protected boolean isServerResourceAccessible(EDrcServerResource resource) {
        return allowedResources != null && allowedResources.contains(resource);
    }

    private boolean setupLocale(EIsoLanguage lng, EIsoCountry country) {
        final List<EIsoLanguage> supportedLanguages = getSupportedLanguages();
        if (lng != null && !supportedLanguages.contains(lng) && !supportedLanguages.isEmpty()) {
            final String messageTemplate =
                    getMessageProvider().translate("TraceMessage", "Selected language (%1$s) does not supported by current layer. Using %2$s language.");
            getTracer().debug(String.format(messageTemplate, lng.getName(), supportedLanguages.get(0).getName()));
            lng = supportedLanguages.get(0);
            country = LocaleManager.getDefaultCountry(lng);
        }
        if (lng == getLanguage() && country==getCountry()) {
            return false;
        } else {
            localeManager.changeLocale(lng, country);
            return true;
        }
    }

    @Override
    public List<EIsoLanguage> getSupportedLanguages() {
        return localeManager.getSupportedLanguages();
    }

    void applySettings(ClientSettings settings) {
        configStore = settings;
    }    

    @Override
    public ISqmlDefinitions getSqmlDefinitions() {
        if (sqmlDefinitions == null) {
            if (!app.isSqmlAccessible()){
                throw new SqmlIsNotAccessibleError();
            }
            if (sqmlLoadingThread == null) {
                startSqmlLoadingThread(Thread.NORM_PRIORITY,false);
                sqmlDefinitions = waitForSqmlDefinitionsLoad();
            } else if (sqmlLoadingThread.isAlive()) {
                sqmlLoadingThread.setPriority(Thread.NORM_PRIORITY);
                sqmlDefinitions = waitForSqmlDefinitionsLoad();
            } else {
                try {
                    sqmlDefinitions = sqmlLoadingTask.getSqmlDefinitions();
                } catch (Throwable ex) {
                    throw new DefinitionError("Can't init branch", ex);
                }
                sqmlLoadingThread = null;
                sqmlLoadingTask = null;
            }
        }
        return sqmlDefinitions;
    }

    private ISqmlDefinitions waitForSqmlDefinitionsLoad() {
        final ITaskWaiter taskWaiter = getApplication().newTaskWaiter();
        try {
            taskWaiter.setMessage(getMessageProvider().translate("Wait Dialog", "Loading SQML"));
            ISqmlDefinitions definitions = taskWaiter.runAndWait(new Callable<ISqmlDefinitions>() {
                @Override
                public ISqmlDefinitions call() throws Exception {
                    sqmlLoadingThread.join(300000);
                    if (sqmlLoadingThread.isAlive()) {
                        final String dumps = ThreadDumper.dumpSync(sqmlLoadingThread, 10, 5000);
                        if (dumps!=null){
                            throw new DefinitionError("Unable to load SQML definitions:\n"+dumps);
                        }
                    }
                    return sqmlLoadingTask.getSqmlDefinitions();
                }
            });
            return definitions;
        } catch (ExecutionException | InterruptedException | CancellationException ex) {
            throw new DefinitionError("Can't init branch", ex);
        } finally {
            sqmlLoadingThread = null;
            sqmlLoadingTask = null;
            taskWaiter.close();
        }
    }

    @Override
    public DefManager getDefManager() {
        return app.getDefManager();
    }

    @Override
    public IMessageBox newMessageBoxDialog(String message, String title, EDialogIconType icon, Set<EDialogButtonType> buttons) {
        ExplorerMessageBox messageBox = new ExplorerMessageBox(mainWindow);
        messageBox.setWindowTitle(title);
        messageBox.setText(message);
        messageBox.setIcon(icon);
        if (buttons != null && !buttons.isEmpty()) {
            for (EDialogButtonType b : buttons) {
                messageBox.addButton(ExplorerMessageBox.getQButton(b));
            }
        }
        return messageBox;
    }

    @Override
    public String setStatusBarLabel(final String text) {
        if (statusBar==null){
            return "";
        }else{
            final String currentText = statusBar.getText();
            statusBar.setText(text);
            return currentText;
        }
    }

    @Override
    public IMainStatusBar getStatusBar() {
        return statusBar;
    }
        

    @SuppressWarnings("unused")
    private void onPollerTimeout() {
        if (token != null && !getEasSession().isBusy() && !token.isPresent()) {
            tokenPoller.stop();
            final IEnterPasswordDialog passwordDialog = getApplication().getDialogFactory().newEnterPasswordDialog(this);
            passwordDialog.setMessage(getMessageProvider().translate("PKCS11", "HSM has been removed.\nPlug in HSM and enter your password or press cancel to disconnect"));
            while (true) {
                try {
                    if (passwordDialog.execDialog() == DialogResult.REJECTED) {
                        throw new CredentialsWasNotDefinedError();
                    }
                } finally {
                    getProgressHandleManager().unblockProgress();
                }
                try {
                    final char[] keyStorePassword = passwordDialog.getPassword().toCharArray();
                    easSession.renewSslContext(keyStorePassword);
                    Arrays.fill(keyStorePassword, '\0');
                    tokenPoller.start();
                    break;
                } catch (KeystoreControllerException | CertificateUtilsException exception) {
                    if (KeystoreController.isIncorrectPasswordException(exception)) {
                        final String message = getMessageProvider().translate("PKCS11", "Wrong password to hardware security module");
                        messageError(message);
                    } else {
                        final String title = getMessageProvider().translate("PKCS11", "Hardware Security Module Error");
                        processException(title, exception);
                    }
                }
            }
        }
    }

    @Override
    public <T> T runInGuiThread(final Callable<T> task) throws InterruptedException, ExecutionException {
        return app.runInGuiThread(task);
    }

    @Override
    public void runInGuiThreadAsync(final Runnable task) {
        app.runInGuiThreadAsync(task);
    }     

    @Override
    public IEventLoop newEventLoop() {
        return new ExplorerEventLoop(app);
    }

    @Override
    public String signText(String text, X509Certificate certificate) throws SignatureException {
        final String message =
                getMessageProvider().translate("ExplorerError", "Failed to sign message: operation is not supported");
        throw new SignatureException(SignatureException.EReason.UNSUPPORTED_OPERATION, message);
    }

    private void notifyConnected() {
        final List<IClientEnvironment.ConnectionListener> listeners = new LinkedList<>(connectionListeners);
        for (IClientEnvironment.ConnectionListener listener : listeners) {
            listener.afterOpenConnection();
        }
    }

    private void notifyDisconnected(final boolean forced) {
        final List<IClientEnvironment.ConnectionListener> listeners = new LinkedList<>(connectionListeners);
        for (IClientEnvironment.ConnectionListener listener : listeners) {
            listener.afterCloseConnection(forced);
        }
    }

    @Override
    public void addConnectionListener(final ConnectionListener listener) {
        if (listener != null && !connectionListeners.contains(listener)) {
            connectionListeners.add(listener);
        }
    }

    @Override
    public void removeConnectionListener(final ConnectionListener listener) {
        connectionListeners.remove(listener);
    }

    @Override
    public void writeConnectionParametersToXml(final ConnectionParams xml) {
        if (connection!=null){
            xml.setConnectionName(connection.getName());
            if (connectingUserName!=null && !connectingUserName.isEmpty()){
                xml.setUserName(connectingUserName);
            }else{
                xml.setUserName(connection.getUserName());
            }
            xml.setEncSessionKey(pwdStore.getSecret());
            easSession.writeSessionId(xml);
        }
    }

    @Override
    public ProductInstallationOptions getProductInstallationOptions() {
        return productInstallationOptions;
    }        
}
