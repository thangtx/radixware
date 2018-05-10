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
package org.radixware.wps;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.radixware.kernel.common.auth.PasswordHash;
import org.radixware.kernel.common.client.Clipboard;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.dialogs.IMessageBox;
import org.radixware.kernel.common.client.eas.AbstractEasSession;
import org.radixware.kernel.common.client.eas.EasClient;
import org.radixware.kernel.common.client.eas.EasSession;
import org.radixware.kernel.common.client.eas.IEasSession;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.eas.resources.IResourceManager;
import org.radixware.kernel.common.client.editors.traceprofile.TraceProfileTreeController;
import org.radixware.kernel.common.client.env.AdsVersion;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.env.EntryPointRequest;
import org.radixware.kernel.common.client.env.EnvironmentVariables;
import org.radixware.kernel.common.client.env.IEntryPointHandler;
import org.radixware.kernel.common.client.env.IEventLoop;
import org.radixware.kernel.common.client.env.ProductInstallationOptions;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.env.progress.ProgressHandleManager;
import org.radixware.kernel.common.client.errors.AccessViolationError;
import org.radixware.kernel.common.client.errors.AuthError;
import org.radixware.kernel.common.client.errors.CredentialsWasNotDefinedError;
import org.radixware.kernel.common.client.errors.EasError;
import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.errors.KerberosError;
import org.radixware.kernel.common.client.errors.UnsupportedDefinitionVersionError;
import org.radixware.kernel.common.client.errors.WrongPasswordError;
import org.radixware.kernel.common.client.exceptions.CantUpdateVersionException;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.ExceptionMessage;
import org.radixware.kernel.common.client.exceptions.NoSapsForCurrentVersion;
import org.radixware.kernel.common.client.exceptions.SignatureException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitions;
import org.radixware.kernel.common.client.meta.sqml.SqmlDefinitionsLoader;
import org.radixware.kernel.common.client.models.groupsettings.FilterSettingsStorage;
import org.radixware.kernel.common.client.models.groupsettings.Filters;
import org.radixware.kernel.common.client.models.groupsettings.GroupSettingsStorage;
import org.radixware.kernel.common.client.models.groupsettings.Sortings;
import org.radixware.kernel.common.client.trace.DefaultClientTracer;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.common.client.tree.IExplorerTreeManager;
import org.radixware.kernel.common.client.tree.UserExplorerItemsStorage;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.types.ExplorerRoot;
import org.radixware.kernel.common.client.types.TimeZoneInfo;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.utils.IContextEnvironment;
import org.radixware.kernel.common.client.utils.ISecretStore;
import org.radixware.kernel.common.client.utils.TokenProcessor;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.widgets.IMainStatusBar;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.enums.EAuthType;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.enums.EDrcServerResource;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EIsoCountry;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.common.environment.IEnvironmentAccessor;
import org.radixware.kernel.common.environment.IRadixEnvironment;
import org.radixware.kernel.common.exceptions.CertificateUtilsException;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.kerberos.KerberosCredentials;
import org.radixware.kernel.common.kerberos.KerberosException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.schemas.clientstate.ConnectionParams;
import org.radixware.schemas.eas.CreateSessionRs;
import org.radixware.schemas.eas.Definition;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.dialogs.LogonDialog;
import org.radixware.wps.dialogs.TraceDialog;
import org.radixware.wps.rwt.MessageBox;
import org.radixware.wps.rwt.RootPanel;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.uploading.FileDescriptor;
import org.radixware.wps.rwt.uploading.IUploadedDataReader;
import org.radixware.wps.rwt.uploading.UploadException;
import org.radixware.wps.rwt.uploading.UploadHandler;
import org.radixware.wps.settings.ISettingsChangeListener;
import org.radixware.wps.text.DefaultTextOptionsProvider;
import org.radixware.wps.tree.TreeManager;
import org.radixware.wps.views.RwtAction;
import org.radixware.wps.views.admin.AdminPanel;

public class WpsEnvironment implements IClientEnvironment {
    
    static EasSessionFactory sessionFactory = null;
    static ConnectionsFactory connectionsFactory = null;
    static boolean test_mode = false;
    static final long PING_PERIOD = 3 * 1000;
    
    enum CheckConnectionState {
        DONE,
        FAULT,
        CANCELLED
    }    
    
    abstract static class EasSessionFactory {

        public abstract EasSession createSession(WpsEnvironment env);
    }

    abstract static class ConnectionsFactory {

        public abstract ConnectionOptions createConnection(WpsEnvironment env);
    }    

    private final static class DelegateClassLoader extends ClassLoader implements IEnvironmentAccessor {

        private final IClientApplication env;

        public DelegateClassLoader(ClassLoader parent, IClientApplication env) {
            super(parent);
            this.env = env;
        }

        @Override
        public IRadixEnvironment getEnvironment() {
            return env;
        }
    }

    private final static class RwtSyncTaskExecutor<T> implements Runnable{

        private final static int TIMEOUNT_IN_SECONDS = 5*60;

        private final Callable<T> task;
        private final CountDownLatch latch = new CountDownLatch(1);
        private final Object semaphore = new Object();
        private Exception exception;
        private T result;

        public RwtSyncTaskExecutor(final Callable<T> tsk){
            task = tsk;
        }

        public T execute(final HttpSessionContext context) throws InterruptedException,ExecutionException{
            context.scheduleTask(this);
            if (latch.await(TIMEOUNT_IN_SECONDS, TimeUnit.SECONDS)){
                synchronized(semaphore){
                    if (exception==null){
                        return result;
                    }else{
                        throw new ExecutionException(exception);
                    }
                }
            }else{
                throw new ExecutionException(new InterruptedException("interrupted by timeout"));
            }
        }

        @Override
        public void run() {
            synchronized(semaphore){
                try{
                    result = task.call();
                }catch(Exception ex){
                    exception = ex;
                }
            }
            latch.countDown();
        }
    }
    
    private final class EasResponseNotificator implements AbstractEasSession.IResponseNotificationScheduler{

        @Override
        public void block() {       
        }

        @Override
        public void unblock() {
        }

        @Override
        public void scheduleNotificationTask(final Runnable task) {
            WpsEnvironment.this.runInGuiThreadAsync(task);
        }
    }

    private final static class MessageBoxTask implements Callable<EDialogButtonType>{

        private final String title;
        private final String message;
        private final EDialogIconType dialogType;
        private final Set<EDialogButtonType> buttons;
        private final IDialogDisplayer displayer;
        private final String detailsTitle;
        private final String details;

        public MessageBoxTask(final String title,
                final String message,
                final EDialogIconType dialogIconType,
                final Set<EDialogButtonType> buttons,
                              final IDialogDisplayer displayer){
            this.title = title;
            this.message = message;
            this.dialogType = dialogIconType;
            this.buttons = buttons;
            this.displayer = displayer;
            detailsTitle = null;
            details = null;
        }

        public MessageBoxTask(final String title,
                final String message,
                final IDialogDisplayer displayer,
                final String detailsTitle,
                              final String details){
            this.title = title;
            this.message = message;
            this.dialogType = null;
            this.buttons = null;
            this.displayer = displayer;
            this.detailsTitle = detailsTitle;
            this.details = details;
        }

        @Override
        public EDialogButtonType call() throws Exception {
            if (dialogType==null){
                return MessageBox.exceptionBox(displayer, title, message, detailsTitle, details).execMessageBox();
            }else{
                return new MessageBox(displayer, title, message, null, buttons, dialogType).execMessageBox();
            }
        }
    }        
    
    private class UpdateVersionController implements TreeManager.IUpdateVersionController {

        private WpsApplication preparedApplication;

        @Override
        public boolean prepareNewVersion(final Collection<Id> changedDefinitions, final long version) {
            preparedApplication = null;
            try {
                preparedApplication = 
                    context.application.server.getLatestAppVersion(false, false, changedDefinitions, version);
                return canUpdateToVersion(version);
            } catch (RadixLoaderException exception) {
                final String message = getMessageProvider().translate("ExplorerError", "Can't load runtime components");
                getTracer().error(message, exception);
                final String title = getMessageProvider().translate("ExplorerMessage", "Can't Update Version");
                messageInformation(title, message);
                return false;
            }
        }

        @Override
        public void switchToNewVersion() {
            if (preparedApplication != null) {
                switchToNewApplicationContext(preparedApplication);
                preparedApplication = null;
            }
        }
    }    

    private class WpsEventLoop implements IEventLoop {

        private final AtomicBoolean isInProgress = new AtomicBoolean(false);
        private final AtomicBoolean wasFinished = new AtomicBoolean(false);
        private final Object semaphore = new Object();
        private final List<Runnable> scheduledTasks = new LinkedList<>();
        private volatile CountDownLatch latch;

        private boolean isGuiThread(){
            return WpsEnvironment.this.getApplication().isInGuiThread();
        }

        @Override
        public void scheduleTask(final Runnable task) {
            if (task != null) {
                synchronized(semaphore){
                    scheduledTasks.add(task);
                }
            }
        }
        
        private void runScheduledTasks(){
            List<Runnable> tasks;
            synchronized(semaphore){
                tasks = new ArrayList<>(scheduledTasks);
                scheduledTasks.clear();
            }
            for (Runnable task : tasks) {
                try {
                    task.run();
                } catch (RuntimeException exception) {
                    Logger.getLogger(WpsEnvironment.class.getName()).log(Level.SEVERE, null, exception);
                }
            }
        }

        @Override
        @SuppressWarnings("SleepWhileInLoop")
        public void start() {//timerSemaphore must be does not captured here!            
            final boolean isGuiThread = isGuiThread();
            synchronized (semaphore) {
                isInProgress.set(true);
                wasFinished.set(false);
                if (!isGuiThread){
                    latch = new CountDownLatch(1);
                }
            }
            try {
                runScheduledTasks();
                if (isGuiThread){
                    while (!wasFinished.get()) {
                        processEvents();
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(WpsEnvironment.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        runScheduledTasks();
                    }
                }else{
                    try{
                        latch.await();
                    }catch(InterruptedException exception){
                        Logger.getLogger(WpsEventLoop.class.getName()).log(Level.SEVERE, null, exception);
                    }
                }
            } finally {
                isInProgress.set(false);
            }
        }

        @Override
        public void stop() {
            synchronized (semaphore) {
                wasFinished.set(true);
                scheduledTasks.clear();
                if (latch!=null){
                    latch.countDown();
                    latch  =null;
                }
            }
        }

        @Override
        public boolean isInProgress() {
            return isInProgress.get();
        }
    }

    private final ArrayList<WeakReference<ISettingsChangeListener>> settingsRefs = new ArrayList<>();
    private TimeZoneInfo serverTimeZoneInfo;
    private final WpsLocaleManager localeManager = new WpsLocaleManager(this);
    private EasSession session;
    private EasClient client;
    final HttpSessionContext context;
    private final DefaultClientTracer tracer;
    private WpsProgressHandleManager phManager;
    private final TreeManager treeManager;
    private final RootPanel rootPanel;
    private List<Id> contextlessCommands = null;
    private Set<EDrcServerResource> allowedResources = null;
    private final UpdateVersionController updateVersionController = new UpdateVersionController();
    private final EasResponseNotificator responseNotificator = new EasResponseNotificator();
    public final Action showTraceAction;
    private final List<IClientEnvironment.ConnectionListener> connectionListeners = new LinkedList<>();
    private final EIsoLanguage defaultLanguage;
    private ConnectionOptions connection = null;
    private List<ExplorerRoot> explorerRootsList = null;
    private ProductInstallationOptions productInstallationOptions;
    private boolean disposed;//NOPMD (need for debug reasons)
    private Clipboard clipboard;
    private TraceDialog traceDialog;
    private FilterSettingsStorage filterSettingsStorage = new FilterSettingsStorage();
    private DefaultTextOptionsProvider textOptionsProvider;
    private long lastPingTime = 0;    
    private boolean eventsBlocked = false;
    private boolean inUnsupportedVersionProcess;
    private boolean connectFirstTime = true;
    
    WpsEnvironment(final HttpSessionContext context) {
        this.context = context;
        tracer = new DefaultClientTracer(this);
        tracer.setBuffer(tracer.createTraceBuffer());
        treeManager = new TreeManager(this, updateVersionController);

        
        final EIsoLanguage clientLanguage = localeManager.autoSelectLanguage(context.getHttpConnectionOptions(), null);
        if (clientLanguage != null && clientLanguage != localeManager.getLanguage()) {
            localeManager.changeLocale(clientLanguage, null);
        }
        
        defaultLanguage = localeManager.getLanguage();
        clipboard = new Clipboard(this);
        checkThreadState();
        
        showTraceAction = createAction(ClientIcon.Dialog.TRACE, "show_trace", new Action.ActionListener() {
            @Override
            public void triggered(final Action action) {
                if (traceDialog == null) {
                    traceDialog = new TraceDialog(WpsEnvironment.this, rootPanel);
                    if (WpsEnvironment.this.client != null)//was connected
                    {
                        traceDialog.init();
                        traceDialog.showDialog();
                    }
                } else {
                    traceDialog.getHtml().renew();
                    if (traceDialog.wasClosed()) {
                        traceDialog.showDialog();
                    }
                }
            }
        });        
        
        textOptionsProvider = new DefaultTextOptionsProvider(this);
        
        if (context!=null && context.getHttpConnectionOptions().isAdminPanel()){
            rootPanel = new AdminPanel(this);
        }else{
            rootPanel = new DefaultRootPanel(this);
        }
        if (context != null) {
            rootPanel.setTitle(WpsApplication.getProductName());
        }
        
        updateTranslations();
    }
    
    private RwtAction createAction(final ClientIcon icon, final String name, final Action.ActionListener listener){
        final RwtAction action = new RwtAction(this, icon);
        action.setObjectName(name);
        if (listener!=null){
            action.addActionListener(listener);
        }
        return action;
    }

    void dispose() {
        if (phManager!=null){
            phManager.disableSelfCheck();
        }
        disconnect(true);
        synchronized (this) {
            if (settings != null) {
                settings.syncDb(false);
                settings.close();
                settings.endAllGroups();
            }
            connectionListeners.clear();
        }
        UserExplorerItemsStorage.clearCache(this);
        Filters.clearCache(this);
        Sortings.clearCache(this);
        TraceProfileTreeController.clearCache(this);
        EnvironmentVariables.clearAll(this);
        tracer.close();
        disposed = true;
    }

    public void applySettings(final WpsSettings settings) {
        this.settings = settings;
        synchronized (settingsRefs) {
            List<ISettingsChangeListener> listeners = new ArrayList<>(settingsRefs.size());

            for (int i = 0; i < settingsRefs.size();) {
                WeakReference<ISettingsChangeListener> ref = settingsRefs.get(i);
                ISettingsChangeListener listener = ref.get();
                if (listener == null) {
                    settingsRefs.remove(i);
                } else {
                    listeners.add(listener);
                    i++;
                }
            }

            for (ISettingsChangeListener listener : listeners) {
                listener.onSettingsChanged();
            }
        }
    }

    public void addSettingsChangeListener(final ISettingsChangeListener l) {
        synchronized (settingsRefs) {
            if (l != null) {
                boolean found = false;
                for (int i = 0; i < settingsRefs.size();) {
                    WeakReference<ISettingsChangeListener> ref = settingsRefs.get(i);
                    ISettingsChangeListener listener = ref.get();
                    if (listener == null) {
                        settingsRefs.remove(i);
                    } else {
                        if (listener == l) {//NOPMD
                            found = true;
                        }
                        i++;
                    }
                }
                if (!found) {
                    settingsRefs.add(new WeakReference<>(l));
                }
            }
        }
    }

    public void removeSettingsChangeListener(final ISettingsChangeListener l) {
        synchronized (settingsRefs) {
            for (int i = 0; i < settingsRefs.size();) {
                WeakReference<ISettingsChangeListener> ref = settingsRefs.get(i);
                ISettingsChangeListener listener = ref.get();
                if (listener == l || listener == null) {//NOPMD
                    settingsRefs.remove(i);
                } else {
                    i++;
                }
            }
        }
    }

    private void updateTranslations() {
        showTraceAction.setText(getMessageProvider().translate("EnvironmentAction", "Trace"));
        rootPanel.updateTranslations();
        ((TreeManager) this.getTreeManager()).translate();        
    }
    
    
    private final Object appLock = new Object();

    void switchToNewApplicationContext(final WpsApplication app) {
        getClipboard().clear();
        getFilterSettingsStorage().clear();
        synchronized (appLock) {
            this.context.application.unregister(this.context);
            this.context.application = app;
            this.context.application.register(this.context);
        }
        if (traceDialog != null) {
            traceDialog.init();
        }
    }

    @Override
    public EDialogButtonType messageBox(final String title, final String message, final EDialogIconType icon, final Set<EDialogButtonType> buttons) {
        try {
            return runInGuiThread(new MessageBoxTask(title, message, icon, buttons, context.dialogDisplayer));
        } catch (InterruptedException ex) {
        } catch (ExecutionException ex) {
            getTracer().error(ex);
        }
        return null;
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

    @Override
    public boolean isUserFuncDevelopmentAccessible() {
        return isServerResourceAccessible(EDrcServerResource.USER_FUNC_DEV);
    }

    protected boolean isServerResourceAccessible(EDrcServerResource resource) {
        return allowedResources != null && allowedResources.contains(resource);
    }

    protected void setContextlessCommands(final CreateSessionRs.ContextlessCommands commands) {
        contextlessCommands = new ArrayList<>();
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
                    tracer.put(EEventSeverity.WARNING, String.format(message, item.getTitle()), EEventSource.EXPLORER);
                }
            }
        }
    }

    @Override
    public IMessageBox newMessageBoxDialog(final String message, final String title, final EDialogIconType icon, final Set<EDialogButtonType> buttons) {
        return new MessageBox(this.getDialogDisplayer(), title, message, null/*details*/, buttons, icon);
    }

    public IDialogDisplayer getDialogDisplayer() {
        return context.dialogDisplayer;
    }

    final void checkThreadState() {
        synchronized (appLock) {
            if (context == null) {
                return;
            }
            final ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (!(cl instanceof IEnvironmentAccessor) || ((IEnvironmentAccessor) cl).getEnvironment() != this) {
                Thread.currentThread().setContextClassLoader(new DelegateClassLoader(this.getClass().getClassLoader(), context.application));
            }
        }
    }

    public Connections getConnections() {
        return new Connections(this);
    }

    private EasClient createEasClient(ConnectionOptions c, String stationName, boolean useSsl) throws IllegalUsageError, KeystoreControllerException, GeneralSecurityException, CertificateUtilsException {
        if (c == null) {
            throw new IllegalUsageError("Connection is not defined");
        }
        if (!useSsl) {
            return new EasClient(WpsEnvironment.this.getMessageProvider(), 
                                             session.getSessionTrace(), 
                                             c.getInitialServerAddresses(), 
                                             stationName, 
                                             c.getAuthType(), 
                                             c.getAddressTranslationFilePath(), 
                                             c.isSapDiscoveryEnabled(),
                                             null, 
                                             null);
        } else {
            final ISecretStore secretStore = getApplication().newSecretStore();            
            final char[] trustStorePassword = c.getSslOptions().getTrustStorePassword();
            if (trustStorePassword!=null && trustStorePassword.length>0){
                secretStore.setSecret(new TokenProcessor().encrypt(trustStorePassword));
            }
            return new EasClient(this,
                    c.getInitialServerAddresses(),
                    stationName,
                    c.getAuthType(),
                    c.getAddressTranslationFilePath(),
                    c.isSapDiscoveryEnabled(),
                    new SslContextFactory(c, this),
                    secretStore);
        }
    }

    private boolean checkSignAbility() {
        final String browser = getConfigStore().getBrowser().toLowerCase();
        boolean showWarning = false;
        if (!("firefox".equals(browser) || "msie".equals(browser))) {
            showWarning = true;
        } else {
            final int[] version = getConfigStore().getBrowserVersion();
            if (version.length < 2) {
                showWarning = true;
            } else {
                switch (browser) {
                    case "firefox":
                        if (version[0] < 3) {
                            showWarning = true;
                        }
                        break;
                    case "msie":
                        if (version[0] < 5) {
                            showWarning = true;
                        } else {
                            showWarning = version[0] == 5 && version[1] < 5;
                        }
                        break;
                }
            }
        }

        if (showWarning) {
            String message = true ? "Ваш браузер, возможно, не содержит компонентов, необходимых для выбранного способа аутентификации\n"
                    + "Рекомендуется использование Internet Explorer версии 9.0 и выше или Firefox версии 3.0 и выше.\n"
                    + "Продолжить?"
                    : "Your browser might not support digital signing, required by current authentication type.\n"
                    + "Recommended browsers are MS Internet Explorer, version 9.0 or later or Firefox, version 3.0 or later.\n"
                    + "Try anyway?";
            if (!messageConfirmation(getMessageProvider().translate("ExplorerMessage", "Warning"), message)) {
                return false;
            }
        }
        return true;
    }

    CheckConnectionState checkConnection() {
        try {
            if (this.session == null) {
                final X509Certificate[] clientCertificates;
                if (!context.getHttpConnectionOptions().isSecure()) {
                    if (WebServerRunParams.isSshRequired()) {
                        messageInformation(getMessageProvider().translate("EnvironmentAction", "Security alert"), getMessageProvider().translate("EnvironmentAction", "Security connection required to work with this application."));
                        return CheckConnectionState.CANCELLED;
                    } else if (!messageConfirmation(getMessageProvider().translate("EnvironmentAction", "Security alert"), getMessageProvider().translate("EnvironmentAction", "Your connection is not secure. Do you want to continue?"))) {
                        return CheckConnectionState.CANCELLED;
                    }
                    clientCertificates = null;
                } else {
                    clientCertificates
                            = context.getHttpConnectionOptions().getClientCertificates();
                }
                final boolean isCertificateProvided = clientCertificates != null && clientCertificates.length > 0;

                final KerberosCredentials serviceCredentials
                        = WebServer.getInstance().getKerberosServiceCredentials();
                final WebServerRunParams.KrbWpsOptions krbOptions = context.getRunParams().getKerberosOptions();
                boolean transmitClientCertificates = false;
                KrbClientCredentialsProvider krbCredsProvider;
                final LogonDialog dialog;
                if (serviceCredentials != null && krbOptions.isKerberosOptionsEnabled()) { 
                    final HttpSessionContext.NegotiateAuthResult authResult;
                    if (krbOptions.isSpnego()) {
                        authResult = context.negotiateAuthentication(serviceCredentials);
                    } else {
                        authResult = null;
                    }
                    if (authResult != null && authResult.isNtlmRequested()) {
                        final String traceMessage
                                = getMessageProvider().translate("TraceMessage", "Using of unsupported NTLM authentication protocol requested");
                        getTracer().debug(traceMessage);
                    }
                    final boolean isCertAuth = krbOptions.canUseCertificate() && isCertificateProvided
                            && (authResult == null || (authResult.isNtlmRequested() && krbOptions.downgradeNtlm()));
                    final boolean isBasicAuth = krbOptions.getRemoteAuthScheme() == ERemoteKerberosAuthScheme.BASIC
                            && (authResult == null || (authResult.isNtlmRequested() && krbOptions.downgradeNtlm()));
                    final boolean isRadixAuth = krbOptions.getRemoteAuthScheme() == ERemoteKerberosAuthScheme.RADIX
                            && (authResult == null || (authResult.isNtlmRequested() && krbOptions.downgradeNtlm()));
                    String userName;
                    if (isCertAuth) {
                        krbCredsProvider = new KrbClientCredentialsProvider(krbOptions, null);
                        userName = context.getHttpConnectionOptions().getUserNameFromCertificate();
                        transmitClientCertificates = true;
                        {
                            final String traceMessage
                                    = messageProvider.translate("TraceMessage", "Certificate provided for \"%1$s\" account");
                            getTracer().debug(String.format(traceMessage, userName));
                        }
                    } else {
                        if (authResult != null && !authResult.isNtlmRequested()) {
                            final GSSContext gssContext = authResult.getContext();
                            if (gssContext != null && krbOptions.isCredentialsDelegationAllowed() && gssContext.getCredDelegState()) {
                                krbCredsProvider = new KrbClientCredentialsProvider(krbOptions, gssContext.getDelegCred());
                            } else {
                                krbCredsProvider = new KrbClientCredentialsProvider(krbOptions, null);
                            }
                            userName = authResult.getUserName();
                            authResult.dispose();
                        } else if (isBasicAuth) {
                            krbCredsProvider = doBasicAuth(krbOptions);
                            if (krbCredsProvider == null) {
                                return CheckConnectionState.CANCELLED;
                            }
                            userName = krbCredsProvider.getUserName();
                        } else if (isRadixAuth) {
                            krbCredsProvider = null;
                            userName = null;
                        } else {
                            if (krbOptions.isKerberosAuthRequired()) {
                                final String traceMessage
                                        = getMessageProvider().translate("TraceMessage", "Unable to identify user by kerberos protocol");
                                getTracer().debug(traceMessage);
                                final String title = getMessageProvider().translate("ExplorerMessage", "Can't establish connection");
                                final String message = getMessageProvider().translate("ExplorerError", "Unable to identify the user");
                                messageInformation(title, message);
                                return CheckConnectionState.CANCELLED;
                            } else {
                                krbCredsProvider = null;
                                userName = null;
                            }
                        }
                    }
                    if (userName == null) {
                        dialog = new LogonDialog(this);
                    } else {
                        final boolean isKerberosRequired
                                = krbOptions.isKerberosAuthRequired() && !isRadixAuth;
                        final ConnectionOptions connectionOptions
                                = getValidConnectionOptions(userName, !isKerberosRequired);
                        if (connectionOptions == null) {
                            if (isKerberosRequired) {
                                return CheckConnectionState.CANCELLED;
                            } else {
                                krbCredsProvider = null;
                                dialog = new LogonDialog(this);
                            }
                        } else if (connectionOptions.getAuthType() != EAuthType.KERBEROS) {
                            if (isKerberosRequired) {
                                final String traceMessage
                                        = getMessageProvider().translate("TraceMessage", "Kerberos authentication required but connection options contains another authentication type");
                                getTracer().debug(traceMessage);
                                final String title
                                        = getMessageProvider().translate("ExplorerMessage", "Can't establish connection");
                                final String message
                                        = getMessageProvider().translate("ExplorerError", "Unable to identify the user");
                                messageInformation(title, message);
                                return CheckConnectionState.CANCELLED;
                            } else {
                                krbCredsProvider = null;
                                if (connectionOptions.getAuthType() != EAuthType.CERTIFICATE) {
                                    dialog = new LogonDialog(this);
                                } else {
                                    dialog = new LogonDialog(this, userName);
                                }
                            }
                        } else {
                            dialog = new LogonDialog(this, userName);
                        }
                    }
                } else if (krbOptions != null && krbOptions.isKerberosAuthRequired()) {
                    final String traceMessage
                            = getMessageProvider().translate("TraceMessage", "Unable to identify user: kerberos authentication disabled");
                    getTracer().debug(traceMessage);
                    final String title = getMessageProvider().translate("ExplorerMessage", "Can't establish connection");
                    final String message = messageProvider.translate("ExplorerError", "Unable to identify the user");
                    messageInformation(title, message);
                    return CheckConnectionState.CANCELLED;
                } else if (isCertificateProvided) {
                    krbCredsProvider = null;
                    final String userName = context.getHttpConnectionOptions().getUserNameFromCertificate();
                    {
                        final String traceMessage
                                = messageProvider.translate("TraceMessage", "Certificate provided for \"%1$s\" account");
                        getTracer().debug(String.format(traceMessage, userName));
                    }
                    final ConnectionOptions connectionOptions = getValidConnectionOptions(userName, true);
                    if (connectionOptions == null) {
                        final String traceMessage
                                = messageProvider.translate("TraceMessage", "Connection options not found for user \"%1$s\"");
                        getTracer().debug(String.format(traceMessage, userName));
                        dialog = new LogonDialog(this);
                    } else {
                        if (connectionOptions.getAuthType() != EAuthType.CERTIFICATE) {
                            dialog = new LogonDialog(this);
                        } else {
                            dialog = new LogonDialog(this, userName);
                        }
                    }
                } else {
                    krbCredsProvider = null;
                    HttpConnectionOptions options = context.getHttpConnectionOptions();
                    dialog = new LogonDialog(this, options.getUserName(), options.getStationName(), false);
                }
                //  X509Certificate[] cert = WebServer.getKeystoreController() != null ? clientCert : null;
                //boolean autoConnectDone = false;
                for (;;) {
                    String userName;
                    String password = null;
                    String pwdHash1 = null;
                    String pwdHash256 = null;
                    try {
                        if (krbCredsProvider != null) {
                            userName = dialog.getUserName();
                            connection = getValidConnectionOptions(userName, false);
                            if (connection == null) {
                                return CheckConnectionState.CANCELLED;
                            }
                        } else if (isCertificateProvided) {
                            userName = context.getHttpConnectionOptions().getUserNameFromCertificate();
                            connection = getValidConnectionOptions(userName, true);
                            if (connection != null && connection.getAuthType() != EAuthType.CERTIFICATE) {
                                connection = null;//need other connection
                            }
                        } else {
                            userName = context.getHttpConnectionOptions().getUserName();
                            password = connectFirstTime ? context.getHttpConnectionOptions().getPassword() : null;
                            pwdHash1 = connectFirstTime ? context.getHttpConnectionOptions().getPwdHash1() : null;
                            pwdHash256 = connectFirstTime ? context.getHttpConnectionOptions().getPwdHash256() : null;
                            connection = getValidConnectionOptions(userName, true);
                            if (connection != null && connection.getAuthType() != EAuthType.PASSWORD) {
                                connection = null;
                            }
                            if (password==null && pwdHash1 == null && pwdHash256 == null){
                                connection = null;
                            }
                        }
                        String stationName = context.getHttpConnectionOptions().getStationName();

                        EAuthType authType = null;
                        final boolean needForLoginDialog;
                        if (userName == null || userName.isEmpty() || connection == null) {
                            needForLoginDialog = true;
                        } else {
                            if (stationName == null || stationName.isEmpty()) {
                                stationName = connection.getStationName();
                            }
                            authType = connection.getAuthType();
                            needForLoginDialog
                                    = stationName == null || stationName.isEmpty();
                            if (needForLoginDialog) {
                                final String traceMessage
                                        = messageProvider.translate("TraceMessage", "Station name was not defined in connection options. User must define value of this option.");
                                getTracer().debug(String.format(traceMessage));
                            }
                        }
                        if (needForLoginDialog) {
                            if (dialog.execDialog() != DialogResult.ACCEPTED) {
                                return CheckConnectionState.CANCELLED;
                            }
                            userName = dialog.getUserName();
                            connection = getValidConnectionOptions(userName, false);
                            if (connection == null) {
                                continue;
                            }

                            authType = connection.getAuthType();

                            password = dialog.getPassword();
                            if (authType == EAuthType.PASSWORD && (password == null || password.isEmpty()) && WebServerRunParams.getIsDevelopmentMode()) {
                                password = userName;
                            }

                            if (password == null) {
                                password = "";
                            }

                            setLanguage(dialog.getLanguage());
                            stationName = dialog.getStationName();

                            final boolean isKerberosAuthRequired = krbOptions != null && krbOptions.isKerberosAuthRequired();

                            if (isKerberosAuthRequired && authType != EAuthType.KERBEROS) {
                                final String traceMessage
                                        = getMessageProvider().translate("TraceMessage", "Kerberos authentication required but connection options contains another authentication type");
                                getTracer().debug(traceMessage);
                                final String title = getMessageProvider().translate("ExplorerMessage", "Can't establish connection");
                                final String message = getMessageProvider().translate("ExplorerError", "Unable to identify the user");
                                messageInformation(title, message);
                                continue;
                            } else if (authType == EAuthType.KERBEROS && krbCredsProvider == null) {
                                if (krbOptions == null || krbOptions.getRemoteAuthScheme() != ERemoteKerberosAuthScheme.RADIX) {
                                    final String traceMessage;
                                    if (krbOptions == null) {
                                        traceMessage
                                                = getMessageProvider().translate("TraceMessage", "Kerberos authentication disabled but connection options contains kerberos authentication type");
                                    } else {
                                        traceMessage
                                                = getMessageProvider().translate("TraceMessage", "Transfer of user name and password is not allowed for kerberos authentication type");
                                    }
                                    getTracer().debug(traceMessage);
                                    final String title = getMessageProvider().translate("ExplorerMessage", "Can't establish connection");
                                    final String message = getMessageProvider().translate("ExplorerError", "Unable to identify the user");
                                    messageInformation(title, message);
                                    continue;
                                }
                                krbCredsProvider = new KrbClientCredentialsProvider(this, userName, password.toCharArray());
                            } else if (authType == EAuthType.CERTIFICATE) {

                                final boolean validCertificate
                                        = isCertificateProvided && userName.equals(context.getHttpConnectionOptions().getUserNameFromCertificate());
                                if (!validCertificate) {
                                    final String traceMessage
                                            = getMessageProvider().translate("TraceMessage", "Certificate was not provided for user \"%1$s\"");
                                    getTracer().debug(String.format(traceMessage, userName));
                                    final String title = getMessageProvider().translate("ExplorerMessage", "Can't establish connection");
                                    final String message = getMessageProvider().translate("ExplorerError", "Unable to identify the user");
                                    messageInformation(title, message);
                                    continue;
                                }
                            }
                        } else {
                            EIsoLanguage lang = localeManager.autoSelectLanguage(context.getHttpConnectionOptions(), connection);
                            setLanguage(lang);
                        }
                        setStatusBarLabel(getMessageProvider().translate("StatusBar", "Connecting..."));

                        connection.setStationName(stationName);
                        tracer.getProfile().setDefaultSeverity(connection.getEventSeverity());
                        try {
                            this.session = sessionFactory != null && test_mode ? sessionFactory.createSession(this) : new EasSession(this, responseNotificator);
                            if (stationName == null || stationName.isEmpty()) {
                                stationName = connection.getStationName();
                            }

                            EasClient easClient = createEasClient(connection, stationName, connection.getSslOptions() != null);
                            checkThreadState();
                            CreateSessionRs response = null;
                            dialog.clear();
                            for (int i=1; i<=3; i++){
                                try{
                                    if (krbCredsProvider == null) {
                                        if (authType == EAuthType.CERTIFICATE) {
                                            response
                                                    = session.open(easClient, 
                                                                           stationName, 
                                                                           clientCertificates, 
                                                                           connection.getExplorerRootId(null),
                                                                           null,
                                                                           false);
                                        } else if (pwdHash1 != null && !pwdHash1.isEmpty() || pwdHash256 != null && !pwdHash256.isEmpty()) {
                                           PasswordHash pwdHash = null;
                                            if (pwdHash1 != null && !pwdHash1.isEmpty() && pwdHash256 != null && !pwdHash256.isEmpty()) {
                                                int length = (pwdHash1.length() + pwdHash256.length())/2 + 2;
                                                final byte[] pwdHashArray = new byte[length];
                                                byte[] pwdHash1ByteArr = Hex.decode(pwdHash1);
                                                pwdHashArray[0] = (byte)pwdHash1ByteArr.length;
                                                System.arraycopy(pwdHash1ByteArr, 0, pwdHashArray, 1, pwdHash1ByteArr.length);
                                                byte[] pwdHash256ByteArr = Hex.decode(pwdHash256);
                                                pwdHashArray[pwdHash1ByteArr.length + 1] = (byte)pwdHash256ByteArr.length;
                                                System.arraycopy(pwdHash256ByteArr, 0, pwdHashArray, pwdHash1ByteArr.length + 2, pwdHash256ByteArr.length);
                                                pwdHash = PasswordHash.Factory.fromBytes(pwdHashArray);
                                            } else if (pwdHash1 != null && !pwdHash1.isEmpty()) {
                                                pwdHash = PasswordHash.Factory.fromBytes(Hex.decode(pwdHash1), PasswordHash.Algorithm.SHA1);
                                            } else {
                                                pwdHash = PasswordHash.Factory.fromBytes(Hex.decode(pwdHash256), PasswordHash.Algorithm.SHA256);
                                            }
                                            response = session.open(easClient, stationName, userName, pwdHash, connection.getExplorerRootId(null), null, false);
                                        } else {
                                            response
                                                    = session.open(easClient, 
                                                                           stationName, 
                                                                           userName, 
                                                                           password, 
                                                                           EAuthType.PASSWORD, 
                                                                           connection.getExplorerRootId(null),
                                                                           null,
                                                                           false);
                                        }
                                    } else {
                                        final SpnegoGssTokenProvider gssProvider;
                                        if (krbCredsProvider.isDirectAuthentication() || transmitClientCertificates) {
                                            gssProvider = null;
                                        } else {
                                            gssProvider = new SpnegoGssTokenProvider(null, context, this);
                                        }
                                        final String spn = connection.getKerberosOptions().getServicePrincipalName();
                                        krbCredsProvider.setServicePrincipalName(spn);
                                        if (transmitClientCertificates) {
                                            response
                                                    = session.open(easClient, 
                                                                           stationName, 
                                                                           krbCredsProvider, 
                                                                           gssProvider, 
                                                                           connection.getExplorerRootId(null), 
                                                                           clientCertificates,
                                                                           null,
                                                                           false);
                                        } else {
                                            response
                                                    = session.open(easClient, 
                                                                           stationName, 
                                                                           krbCredsProvider, 
                                                                           gssProvider, 
                                                                           connection.getExplorerRootId(null), 
                                                                           null,
                                                                           null,
                                                                           false);
                                        }
                                    }
                                    break;
                                }catch(UnsupportedDefinitionVersionError | NoSapsForCurrentVersion exception){
                                    if (!processUnsupportedVersionException(exception)){
                                        return CheckConnectionState.FAULT;
                                    }
                                }                            
                            }

                            if (response==null){
                                cleanup(false);
                                return CheckConnectionState.FAULT;                            
                            }

                            this.client = easClient;
                            connection.setConnectedUserName(response.getUser());
                            setContextlessCommands(response.getContextlessCommands());
                            setServerResources(response.getServerResources());
                            productInstallationOptions = ProductInstallationOptions.loadOptions(response.getProductInstallationOptions());

                            final org.radixware.schemas.eas.TimeZone timeZone = response.getServerTimeZone();
                            serverTimeZoneInfo = timeZone == null ? null : TimeZoneInfo.parse(timeZone);
                            getConfigStore().endAllGroups();
                            for (int i=1; i<=3; i++){
                                try{
                                    getConfigStore().syncDb(true);
                                    break;
                                }catch(UnsupportedDefinitionVersionError exception){
                                    if (!processUnsupportedVersionException(exception)){
                                        return CheckConnectionState.FAULT;
                                    }
                                }
                            }


                            getTracer().getBuffer().setMaxSize(getConfigStore().readInteger(SettingNames.SYSTEM + "_TRACE_MAX_SIZE", 500));
                            final String traceProfile = context.getHttpConnectionOptions().getTraceProfile();
                            if (traceProfile == null || traceProfile.isEmpty()) {
                                getTracer().readProfileFromString((String) getConfigStore().value(SettingNames.SYSTEM + "_TRACE_PROFILE", getTraceProfile()));
                            } else {
                                getTracer().readProfileFromString(traceProfile);
                            }

                            final List<ExplorerRoot> explorerRoots = ExplorerRoot.loadFromResponse(response.getExplorerRoots(), this);

                            if (explorerRoots == null || explorerRoots.isEmpty()) {
                                messageError(getMessageProvider().translate("ExplorerMessage", "Connection failed"),
                                        getMessageProvider().translate("ExplorerMessage", "You have no rights to use the system"));
                                cleanup(false);
                                return CheckConnectionState.FAULT;
                            } else {
                                IExplorerTree tree = null;
                                for (int i=1; i<=3; i++){
                                    try{
                                        tree = getTreeManager().openTree(explorerRoots, rootPanel.getExplorerView(), null);
                                        break;
                                    }catch(UnsupportedDefinitionVersionError | NoSapsForCurrentVersion exception){
                                        if (!processUnsupportedVersionException(exception)){
                                            return CheckConnectionState.FAULT;
                                        }                                    
                                    }
                                }                                

                                if (tree == null) {
                                    getTreeManager().closeAll(true);
                                    rootPanel.getExplorerView().close();
                                    cleanup(false);
                                    return CheckConnectionState.FAULT;
                                } else {
                                    explorerRootsList = explorerRoots;
                                    final List<IExplorerTreeNode> rootNodes = tree.getRootNodes();
                                    final IExplorerTreeNode rootNode
                                            = rootNodes == null || rootNodes.isEmpty() ? null : rootNodes.get(0);
                                    connected(response, rootNode);
                                    notifyConnected();
                                    String entryPoint = connectFirstTime ? context.getHttpConnectionOptions().getEntryPoint() : null;
                                    if (entryPoint != null && !entryPoint.isEmpty()) {
                                        handleEntryPoint(entryPoint, context.getHttpConnectionOptions().getCustomParams());
                                    }
                                    return CheckConnectionState.DONE;
                                }
                            }

                        } catch(CredentialsWasNotDefinedError error){
                            treeManager.closeAll(true);
                            RunParams.removeRestoringContextParam();
                            cleanup(false);
                            processException(error);
                        }catch (WrongPasswordError error) {
                            if (error.getLocalizedMessage() != null && !error.getLocalizedMessage().isEmpty()) {
                                final String traceMessage =
                                    getMessageProvider().translate("ExplorerMessage", "Unable to establish connection\n%1$s");
                                getTracer().event(String.format(traceMessage, error.getLocalizedMessage()));
                                messageError(getMessageProvider().translate("ExplorerMessage", "Connection Error"), error.getLocalizedMessage());
                            }
                            treeManager.closeAll(true);
                            RunParams.removeRestoringContextParam();
                            cleanup(false);
                        } catch (AccessViolationError error){
                        final String messageTitle = getMessageProvider().translate("ExplorerMessage", "Unable to Establish Connection");
                        final String messageText =
                            getMessageProvider().translate("ExplorerMessage", "You do not have permission to login. Please contact your system administrator.");
                        messageError(messageTitle, messageText);                        
                        treeManager.closeAll(true);
                        RunParams.removeRestoringContextParam();
                        cleanup(false);
                        if (needForLoginDialog) {
                            continue;
                        } else {
                            return CheckConnectionState.CANCELLED;
                        }
                        } catch (InterruptedException ex) {
                            cleanup(false);
                            if (needForLoginDialog) {
                                continue;
                            } else {
                                return CheckConnectionState.CANCELLED;
                            }
                        } catch (ServiceClientException | IllegalUsageError | KeystoreControllerException | GeneralSecurityException ex) {
                            cleanup(false);
                            processException(ex);
                            if (needForLoginDialog) {
                                continue;
                            } else {
                                return CheckConnectionState.CANCELLED;
                            }
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
                            final String title;
                            if (error instanceof IClientError){
                                title = ((IClientError)error).getTitle(getMessageProvider());
                            }else{
                                title = getMessageProvider().translate("ExplorerMessage", "Connection Problem");
                            }
                            messageError(title, reason);
                            getTracer().put(severity, traceMessage);
                            treeManager.closeAll(true);
                            RunParams.removeRestoringContextParam();
                            cleanup(false);
                            if (!needForLoginDialog) {
                                return CheckConnectionState.CANCELLED;
                            }
                        }
                        } finally {
                            connectFirstTime = false;
                        }
            }
            } else {
                return CheckConnectionState.DONE;
            }
        } catch (GSSException | KerberosException | CertificateUtilsException e) {
            cleanup(false);
            processException(e);
            return CheckConnectionState.FAULT;
        } catch(KerberosError er) {
            cleanup(false);
            processException(er);
            context.setAfterError();
            return CheckConnectionState.CANCELLED;
        }
    }
    
    private boolean processUnsupportedVersionException(final Throwable exception){
        final long requiredVersion;
        if (exception instanceof UnsupportedDefinitionVersionError){
            requiredVersion = ((UnsupportedDefinitionVersionError)exception).getRequiredVersion();
        }else if (exception instanceof NoSapsForCurrentVersion){
            requiredVersion = ((NoSapsForCurrentVersion)exception).getRequiredVersion();
        }else{
            cleanup(false);
            return false;
        }
        if (canUpdateToVersion(requiredVersion)){
            switchToVersion(requiredVersion);
            return true;
        }else{
            cleanup(false);
            return false;
        }        
    }

    private KrbClientCredentialsProvider doBasicAuth(final WebServerRunParams.KrbWpsOptions options) {
        final HttpSessionContext.BasicAuthResult basicAuthResult
                = context.basicAuthentication(options.getKerberosPrincipal().getRealm());
        if (basicAuthResult == null) {
            return null;//CancelButton
        } else {
            final String userName = basicAuthResult.getUserName();
            final KrbClientCredentialsProvider result
                    = new KrbClientCredentialsProvider(this, userName, basicAuthResult.getPassword());
            basicAuthResult.dispose();
            return result;
        }
    }

    private ConnectionOptions getValidConnectionOptions(final String userName, final boolean quied) {
        final ConnectionOptions connectionOptions = connectionsFactory != null && test_mode ? connectionsFactory.createConnection(this) : getConnections().getConnectionByUserName(userName);
        if (connectionOptions == null) {
            if (!quied) {
                final String message
                        = getMessageProvider().translate("ExplorerMessage", "No connection options found for user '%s'");
                final String title
                        = getMessageProvider().translate("ExplorerMessage", "Can't establish connection");
                messageError(title, String.format(message, userName));
                getTracer().debug(String.format(message, userName));
            }
            setStatusBarLabel(getMessageProvider().translate("StatusBar", "Disconnected"));
            return null;
        }
        if (!connectionOptions.hasResolvedAddress()) {
            if (!quied) {
                final String title
                        = getMessageProvider().translate("ExplorerMessage", "Can't establish connection");
                if (connectionOptions.getInitialServerAddresses().size() == 1) {
                    final String message
                            = getMessageProvider().translate("ExplorerMessage", "Unknown server '%s'");
                    final String hostName = connectionOptions.getInitialServerAddresses().get(0).getHostName();
                    messageError(title, String.format(message, hostName));
                    getTracer().debug(String.format(message, hostName));
                } else {
                    final String message
                            = getMessageProvider().translate("ExplorerMessage", "Server was not found");
                    messageError(title, message);
                    getTracer().debug(message);
                }
            }
            setStatusBarLabel(getMessageProvider().translate("StatusBar", "Disconnected"));
            return null;
        }
        final String traceMessage
                = messageProvider.translate("TraceMessage", "Connection options \"%1$s\" with authentication type \"%2$s\" found for user \"%3$s\"");
        getTracer().debug(String.format(traceMessage, connectionOptions.getName(), connectionOptions.getAuthType().getValue(), userName));
        return connectionOptions;
    }

    private void connected(final CreateSessionRs response, final IExplorerTreeNode rootNode) {
        //connection.setConnectedUserName(response.getUser());
        final String mainWindowTitleTemplate = getMessageProvider().translate("MainWindow", "%s Explorer - Connection \"%s\", User \"%s\", Station \"%s\"");
        final String topLayerName = WpsApplication.getProductName();
        final String mainWindowTitle
                = String.format(mainWindowTitleTemplate, topLayerName, connection.getName(), getUserName(), getStationName());
        getMainWindow().setTitle(mainWindowTitle);
        if (rootNode != null && rootNode.isValid()) {
            getMainWindow().setIcon(rootNode.getView().getIcon());
        }
        if (traceDialog != null) {
            traceDialog.init();
        }
        rootPanel.onConnected(response);
    }

    public boolean disconnect() {
        return disconnect(false);
    }        

    private boolean disconnect(final boolean forced) {
        if (session == null) {
            return true;
        }
        if (!forced && getProgressHandleManager().getActive() != null) {
            return false;
        }
        serverTimeZoneInfo = null;
        if (treeManager.closeAll(forced)) {
            if (session.isBusy()) {
                session.breakRequest();
            } else if (!forced){
                pushSettings();
            }
            if (filterSettingsStorage != null) {
                filterSettingsStorage.clear();
            }
            contextlessCommands = null;
            allowedResources = null;
            cleanup(false/*always close session*/);
            notifyDisconnected(forced);
            settings.endAllGroups();
            EnvironmentVariables.clearAll(this);
            return true;
        }
        return false;
    }
    
    private void pushSettings(){
        if (settings != null) {
            try {
                settings.syncDb(false);
            } catch (EasError | AuthError error) {
                final String message = error.getMessage();
                if (message!=null && !message.isEmpty()){
                    Logger.getLogger(WpsEnvironment.class.getName()).log(Level.INFO, "Unable to synchronize config store: {0}", message);
                }else{
                    Logger.getLogger(WpsEnvironment.class.getName()).log(Level.INFO, "Unable to synchronize config store");
                }
            } catch (HttpSessionTerminatedError e){
                throw e;
            } catch (Throwable e) {
                Logger.getLogger(WpsEnvironment.class.getName()).log(Level.SEVERE, "Error on config store dispose", e);
            }
        }
    }

    private void cleanup(final boolean forced) {
        if (this.session != null) {
            this.session.close(forced);
            this.session = null;
        }

        if (this.client != null) {
            this.client.close();
            this.client = null;
        }
        if (connection != null) {
            connection.setConnectedUserName(null);
            connection = null;
        }
        setLanguage(defaultLanguage);
        rootPanel.cleanup(forced);
        if (traceDialog != null) {
            traceDialog.clear();
        }
        getMainWindow().setTitle(WpsApplication.getProductName());
        getMainWindow().setIcon(null);
        setStatusBarLabel(getMessageProvider().translate("StatusBar", "Disconnected"));
    }

    @Override
    public RootPanel getMainWindow() {
        return rootPanel;
    }

    @Override
    public String getUserName() {
        return connection == null ? null : connection.getUserName();
    }

    @Override
    public String getStationName() {
        return connection == null ? context.getHttpConnectionOptions().getStationName() : connection.getStationName();
    }

    @Override
    public String getConnectionName() {
        return connection==null ? null : connection.getName();
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
            final long selfTimeZoneOffsetMills
                    = Calendar.getInstance().get(Calendar.DST_OFFSET) + Calendar.getInstance().get(Calendar.ZONE_OFFSET);
            final long offsetDiffMills = getServerTimeZoneInfo().getOffsetMills() - selfTimeZoneOffsetMills;
            return new Timestamp(Calendar.getInstance().getTimeInMillis() + offsetDiffMills);
        }
    }
    private final WpsResourceManager resourceManager = new WpsResourceManager(this);

    @Override
    public IResourceManager getResourceManager() {
        return resourceManager;
    }
    private final MessageProvider messageProvider = new MessageProvider() {
        @Override
        public String translate(String key, String message) {
            return WpsLocaleManager.translate(localeManager.getLanguage(), key, message);
        }
    };

    private void setLanguage(EIsoLanguage language) {
        if (language != getLanguage()) {
            final boolean needToShowTraceDialog;
            if (traceDialog != null) {
                needToShowTraceDialog = !traceDialog.wasClosed();
                traceDialog.clear();
                traceDialog.close(DialogResult.REJECTED);
                traceDialog = null;
            } else {
                needToShowTraceDialog = false;
            }
            localeManager.changeLocale(language, null);
            updateTranslations();
            if (needToShowTraceDialog) {
                showTraceAction.trigger();
            }
        }
    }        

    @Override
    public final MessageProvider getMessageProvider() {
        return messageProvider;
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
        synchronized (appLock) {
            return context.application;
        }
    }

    @Override
    public DefaultClientTracer getTracer() {
        return tracer;

    }

    @Override
    public String getTraceProfile() {
        if (context.getHttpConnectionOptions().getTraceProfile() == null
                || context.getHttpConnectionOptions().getTraceProfile().isEmpty()) {
            return context.getRunParams().getTraceProfile();
        } else {
            return context.getHttpConnectionOptions().getTraceProfile();
        }
    }
    private static final String NO_TRACE_FILE = "ntf";
    private String traceFile = null;

    @Override
    public String getTraceFile() {
        if (traceFile == null) {
            String traceDir = context.getRunParams().getTraceDir();
            if (traceDir == null) {
                traceFile = NO_TRACE_FILE;
            } else {
                traceDir = FileFinder.findFile(traceDir);
                File file = new File(traceDir, FileUtils.string2UniversalFileName(context.remoteInfo, '-'));
                try {
                    if (!file.exists()) {
                        File dir = file.getParentFile();
                        if (dir.exists()) {
                            if (!dir.isDirectory()) {
                                traceFile = NO_TRACE_FILE;
                            }
                        } else {
                            if (!dir.mkdirs()) {
                                traceFile = NO_TRACE_FILE;
                            }
                        }
                    }
                } catch (Throwable e) {
                    traceFile = NO_TRACE_FILE;
                }
                if (traceFile == null) {
                    traceFile = file.getAbsolutePath();
                }
            }
        }
        return (traceFile == null ? NO_TRACE_FILE == null : traceFile.equals(NO_TRACE_FILE)) ? null : traceFile;
    }

    @Override
    public void processException(final Throwable e) {
        processException(null, e);
    }

    @Override
    public void processException(final String title, final Throwable e) {
        if (e instanceof InterruptedException) {
            return;
        }
        if (e instanceof HttpSessionTerminatedError){
            throw (HttpSessionTerminatedError)e;            
        }
        for (Throwable err = e; err != null && err.getCause() != err; err = err.getCause()) {            
            if (err instanceof UnsupportedDefinitionVersionError){
                final UnsupportedDefinitionVersionError error = (UnsupportedDefinitionVersionError)err;
                processUnsupportedVersionException(error.processQuiet(), error.getRequiredVersion());
                return;                
            }else if (err instanceof NoSapsForCurrentVersion) {
                final NoSapsForCurrentVersion error = (NoSapsForCurrentVersion)err;
                final boolean quiet = !RunParams.isDevelopmentMode();                
                processUnsupportedVersionException(quiet, error.getRequiredVersion());
                return;
            }
        }        
        final ExceptionMessage exceptionMessage = new ExceptionMessage(this, e);
        final String dialogTitle;
        if (title == null || title.isEmpty()) {
            dialogTitle = exceptionMessage.getDialogTitle();
        } else {
            dialogTitle = ClientValueFormatter.capitalizeIfNecessary(this, title);
        }
        exceptionMessage.trace(getTracer(), title);

        if (exceptionMessage.hasDialogMessage()) {
            getProgressHandleManager().blockProgress();
            try {
                exceptionMessage.display(dialogTitle, this.getMainWindow());
            } finally {
                getProgressHandleManager().unblockProgress();
            }
        }
        if (exceptionMessage.getSeverity() == EEventSeverity.ALARM) {
            disconnect(true);
        }
    }
    
    private void processUnsupportedVersionException(final boolean quiet, final long requiredVersion){
        getDefManager().getAdsVersion().makeUnsupported();
        if (inUnsupportedVersionProcess){
            return;
        }
        inUnsupportedVersionProcess = true;
        try{            
            if (canUpdateToVersion(requiredVersion)){
                final MessageProvider mp = getMessageProvider();
                final String title = mp.translate("ExplorerMessage", "Confirm to Update Version");
                final String message = mp.translate("ExplorerMessage",
                    "Current version is no longer supported. It is impossible to continue work until updates will be installed.\n"
                    + "Do you want to update now (all your unsaved data will be lost) ?");

                if (quiet || messageConfirmation(title, message)) {
                    switchToVersion(requiredVersion);
                }
            }
        }finally{
            inUnsupportedVersionProcess = false;
        }
    }

    @Override
    public void messageInformation(String title, String message) {
        final EnumSet<EDialogButtonType>  buttons = EnumSet.of(EDialogButtonType.CLOSE);
        messageBox(title, message, EDialogIconType.INFORMATION, buttons);
    }

    @Override
    public void messageError(String message) {
        final EnumSet<EDialogButtonType>  buttons = EnumSet.of(EDialogButtonType.CLOSE);
        messageBox(getMessageProvider().translate("MessageBox", "Error"), message, EDialogIconType.CRITICAL, buttons);
    }

    @Override
    public void messageError(String title, String message) {
        final EnumSet<EDialogButtonType>  buttons = EnumSet.of(EDialogButtonType.CLOSE);
        messageBox(title, message, EDialogIconType.CRITICAL, buttons);
    }

    @Override
    public void messageWarning(final String message) {
        final EnumSet<EDialogButtonType>  buttons = EnumSet.of(EDialogButtonType.CLOSE);
        messageBox(getMessageProvider().translate("MessageBox", "Warning"), message, EDialogIconType.WARNING, buttons);
    }

    @Override
    public void messageWarning(final String title, final String message) {
        final EnumSet<EDialogButtonType>  buttons = EnumSet.of(EDialogButtonType.CLOSE);
        messageBox(title, message, EDialogIconType.WARNING, buttons);
    }

    @Override
    public void messageException(final String title, final String message, final Throwable e) {
        if (ClientException.isSpecialFault(e)) {
            if (ClientException.isInformationMessage(e)) {
                messageInformation(title, ClientException.getSpecialFaultMessage(this, e));
            } else {
                messageError(ClientException.getSpecialFaultMessage(this, e));
            }
            return;
        }
        final String trace;
        final StringBuilder detailsBuilder = new StringBuilder();
        if (e instanceof IClientError) {
            detailsBuilder.append(((IClientError) e).getDetailMessage(getMessageProvider()));
            trace = ClientException.exceptionStackToString(e);
        } else if (e instanceof ServiceCallFault) {
            final ServiceCallFault fault = (ServiceCallFault) e;
            if (fault.getFaultCode() != null && !fault.getFaultCode().isEmpty()) {
                detailsBuilder.append("fault code: ").append(fault.getFaultCode()).append('\n');
            }
            if (fault.getFaultString() != null && !fault.getFaultString().isEmpty()) {
                detailsBuilder.append("fault string: ").append(fault.getFaultString()).append('\n');
            }
            if (fault.getCauseExClass() != null && !fault.getCauseExClass().isEmpty()) {
                detailsBuilder.append("exception class: ").append(fault.getCauseExClass()).append('\n');
            }
            if (fault.getCauseExMessage() != null && !fault.getCauseExMessage().isEmpty()) {
                detailsBuilder.append("exception cause: ").append(fault.getCauseExMessage()).append('\n');
            }
            if (fault.getMessage() != null && !fault.getMessage().isEmpty()) {
                detailsBuilder.append("exception message: ").append(fault.getMessage()).append('\n');
            }
            trace = ((ServiceCallFault) e).getCauseExStack();
        } else {
            detailsBuilder.append("exception: ").append(e.getClass().getName());
            detailsBuilder.append("\nexception message: ").append(e.getMessage());
            trace = ClientException.exceptionStackToString(e);
        }

        final String windowTitle = ClientValueFormatter.capitalizeIfNecessary(this, title);
        try{
            runInGuiThread(new MessageBoxTask(windowTitle, message, getDialogDisplayer(), detailsBuilder.toString(), trace));
        }catch(InterruptedException ex){

        }catch(ExecutionException ex){
            getTracer().error(ex.getCause());
        }
    }

    @Override
    public boolean messageConfirmation(final String title, final String message) {
        final EnumSet<EDialogButtonType>  buttons = EnumSet.of(EDialogButtonType.YES, EDialogButtonType.NO);
        return messageBox(title, message, EDialogIconType.QUESTION, buttons) == EDialogButtonType.YES;
    }

    @Override
    public void alarmBeep() {
    }

    @Override
    public IEasSession getEasSession() {
        return session;
    }

    @Override
    public ConnectionOptions.SslOptions getSslOptions() {
        return connection == null ? null : connection.getSslOptions();
    }

    public Id selectExplorerRootId(final List<ExplorerRoot> explorerRoots) {
        if (connection != null) {
            if (explorerRoots != null) {
                if (connectFirstTime) {
                    String explorerRootIdStr = context.getHttpConnectionOptions().getExplorerRootId();
                    if (explorerRootIdStr != null && !explorerRootIdStr.isEmpty()) {
                        Id id = Id.Factory.loadFrom(explorerRootIdStr);
                        for (ExplorerRoot explorerRoot : explorerRoots) {
                            if (explorerRoot.getId().equals(id)) {
                                return id;
                            }
                        }
                        final String str = getMessageProvider().translate("ExplorerMessage", "Explorer root %1$s is not accessible");
                        getTracer().warning(String.format(str, explorerRootIdStr));
                        return connection.getExplorerRootId(explorerRoots);
                    }
                }
                return connection.getExplorerRootId(explorerRoots);
            } else {
                return connection.selectExplorerRootId(explorerRootsList);
            }
        }
        return null;
    }

    @Override
    public ProgressHandleManager getProgressHandleManager() {
        synchronized (this) {
            if (phManager == null) {
                phManager = new WpsProgressHandleManager(this);
            }
            return phManager;
        }
    }

    @Override
    public Clipboard getClipboard() {
        return clipboard;
    }

    @Override
    public FilterSettingsStorage getFilterSettingsStorage() {
        return filterSettingsStorage;
    }
    private WpsSettings settings;

    @Override
    public WpsSettings getConfigStore() {
        synchronized (this) {
            if (settings == null) {
                settings = new WpsSettings(this);
            }
            return settings;
        }
    }
    private GroupSettingsStorage groupSettingsStorage = null;

    @Override
    public GroupSettingsStorage getGroupSettingsStorage() {
        synchronized (this) {
            if (groupSettingsStorage == null) {
                groupSettingsStorage = new GroupSettingsStorage(this) {
                    @Override
                    protected String getStorageDescription() {
                        return "WPS group settings storage";
                    }

                    @Override
                    protected ClientSettings createSettings() {
                        return new WpsSettings(WpsEnvironment.this);
                    }
                };
            }
            return groupSettingsStorage;
        }
    }

    @Override
    public IExplorerTreeManager getTreeManager() {
        return treeManager;
    }
    private ISqmlDefinitions sqmlDefinitions;

    @Override
    public ISqmlDefinitions getSqmlDefinitions() {
        synchronized (this) {
            if (sqmlDefinitions == null) {
                sqmlDefinitions = SqmlDefinitionsLoader.getInstance().load(this);
            }   
            return sqmlDefinitions;
        }
    }

    @Override
    public List<EIsoLanguage> getSupportedLanguages() {
        return localeManager.getSupportedLanguages();
    }

    @Override
    public String getWorkPath() {
        return "";
    }

    @Override
    public DefManager getDefManager() {
        synchronized (appLock) {
            return context.application.getDefManager();
        }
    }

    @Override
    public DefaultTextOptionsProvider getTextOptionsProvider() {
        return textOptionsProvider;
    }

    public void sendFileToTerminal(String desc, File file, String mimeType, boolean open) {
        sendFileToTerminal(desc, file, mimeType, open, true);
    }

    public void sendFileToTerminal(String desc, File file, String mimeType, boolean open, boolean deleteOnDownload) {
        final String fileName;
        if (mimeType==null){
            fileName = desc;
        }else{
            EMimeType type;
            try{
                type = EMimeType.getForValue(mimeType);
            }catch(NoConstItemWithSuchValueError error){
                type = null;
            }
            if (type!=null 
                && type!=EMimeType.ALL_SUPPORTED
                && type!=EMimeType.ALL_FILES
                && type.getExt()!=null
                && !type.getExt().isEmpty()){
                final String fileExt = "."+type.getExt();
                if (desc.endsWith(fileExt)){
                    fileName = desc;
                }else{
                    fileName = desc+fileExt;
                }
            }else{
                fileName = desc;
            }
        }
        rootPanel.requestDownload(desc, ((WpsApplication) getApplication()).registerFileResource(fileName, file, mimeType, open, deleteOnDownload), open);
    }

    public String registerResource(String desc, File file, String mimeType) {
        return "/rwtext/file/" + ((WpsApplication) getApplication()).registerFileResource(desc, file, mimeType, true, true);
    }

    public String registerImageResource(String desc, File file, String mimeType) {
        return "rwtext/file/" + ((WpsApplication) getApplication()).registerFileResource(desc, file, mimeType, false, false);
    }

    private boolean canUpdateToVersion(final long version) {
        final long currentVersion = getDefManager().getAdsVersion().getNumber();    
        if (currentVersion!=version && WpsAdsVersion.isKernelModified()) {
            final MessageProvider mp = getMessageProvider();
            final String title = mp.translate("ExplorerMessage", "Version Mismatch");
            final String messageTemplate = mp.translate("ExplorerMessage", "Web Presentation Server version (%1$s) does not correspond to  radix application server version (%2$s).\nPlease contact your system administrator.");
            final String message = String.format(messageTemplate, String.valueOf(currentVersion), String.valueOf(version));
            messageInformation(title, message);
            return false;
        }
        return true;
    }
    
    public void checkForUpdates() {
        if (getTreeManager() != null) {
            try {
                final AdsVersion version = getDefManager().getAdsVersion();
                final Collection<Id> definitionIds = version.checkForUpdates(this);
                final long newVersionNumber = version.getTargetVersionNumber();
                if (newVersionNumber>-1) {
                    if (canUpdateToVersion(newVersionNumber)) {
                        final String title = getMessageProvider().translate("ExplorerMessage", "Confirm to Update Version");
                        final String message = getMessageProvider().translate("ExplorerMessage", "New version found. Do you want to update now?");
                        if (messageConfirmation(title, message)) {
                            updateToVersion(definitionIds, newVersionNumber);
                        }
                    }
                } else {
                    final String message = getMessageProvider().translate("ExplorerMessage", "There are no updates available for web explorer");
                    messageInformation(null, message);
                }
            } catch (CantUpdateVersionException ex) {
                ex.showMessage(this);
            }
        }
    }

    public void switchToVersion(final long versionNumber) {        
        final AdsVersion adsVersion = getDefManager().getAdsVersion();        
        final long targetVersion = versionNumber>-1 ? versionNumber : adsVersion.getTargetVersionNumber();
        if (canUpdateToVersion(targetVersion)) {
            IProgressHandle handle = getProgressHandleManager().newProgressHandle();
            try {
                handle.startProgress(getMessageProvider().translate("ExplorerMessage", "Updating Version"), false);
                final Collection<Id> changedDefs = adsVersion.prepareSwitchVersion(this, versionNumber, true);
                updateToVersion(changedDefs, versionNumber);
            } finally {
                handle.finishProgress();
            }
        }
    }

    private void updateToVersion(final Collection<Id> changedDefs, final long versionNumber) {
        if (updateVersionController.prepareNewVersion(changedDefs, versionNumber)) {
            updateVersionController.switchToNewVersion();
            if (treeManager != null && treeManager.isOpened()) {
                treeManager.updateVersion(this.context.application.getCreateReason());
            }
        }
    }

    static WpsEnvironment getEnvironmentStatic() {
        final Thread t = Thread.currentThread();
        if (t instanceof IContextEnvironment) {
            return (WpsEnvironment) ((IContextEnvironment) t).getClientEnvironment();
        } else {
            return null;
        }
    }

    public String requestUpload(String itemId, String uploadId, Object wrapper) {
        String registerId = this.rootPanel.requestUpload(itemId, uploadId);
        this.context.requestUpload(itemId, registerId, wrapper);
        return registerId;
    }

    public UploadHandler uploadFile(final FileDescriptor fileDescriptor, final IUploadedDataReader reader) throws UploadException {
        if (fileDescriptor.getFileSize() != null) {
            final long hardFileSizeLimitMb = WebServerRunParams.getUploadHardLimitMb();
            if (hardFileSizeLimitMb >= 0) {
                final long hardFileSizeLimit = hardFileSizeLimitMb * 1024l * 1024l;
                if (hardFileSizeLimit < fileDescriptor.getFileSize()) {
                    final String messageTemplate
                            = getMessageProvider().translate("ExplorerMessage", "This file cannot be uploaded because it's size exceeds %1$sMb");
                    final String message = String.format(messageTemplate, hardFileSizeLimitMb);
                    final String title = getMessageProvider().translate("ExplorerMessage", "File Size is Too Great");
                    messageInformation(title, message);
                    return null;
                }
            }
            final long softFileSizeLimitMb = context.getRunParams().getUploadSoftLimitMb();
            if (softFileSizeLimitMb >= 0) {
                final long softFileSizeLimit = softFileSizeLimitMb * 1024l * 1024l;
                if (softFileSizeLimit < fileDescriptor.getFileSize()) {
                    final String messageTemplate
                            = getMessageProvider().translate("ExplorerMessage", "Size of this file is greater than %1$sMb. Do you really want to upload?");
                    final String message = String.format(messageTemplate, softFileSizeLimitMb);
                    final String title = getMessageProvider().translate("ExplorerMessage", "Confirm File Uploading");
                    if (!messageConfirmation(title, message)) {
                        return null;
                    }
                }
            }
        }
        final String uploadContextId = fileDescriptor.getContextObjectId();
        final String uploadId
                = rootPanel.requestUpload(uploadContextId, UUID.randomUUID().toString());
        final WebServer.UploadSystemHandler handler
                = context.startUpload(uploadContextId, uploadId, fileDescriptor.getFileName());
        return new UploadHandler(this, handler, fileDescriptor, reader);
    }

    public void disposeUpload(String itemId, String uploadId) {
        this.context.disposeUpload(itemId, uploadId);
    }

    public String getUploadURL(String id, String uuid) {
        return this.rootPanel.getUploadURL(id, uuid);
    }

    @Override
    public IMainStatusBar getStatusBar() {
        return rootPanel.getMainStatusBar();
    }

    @Override
    public String setStatusBarLabel(final String text) {
        final IMainStatusBar statusBar = getStatusBar();
        if (statusBar==null){
            return null;
        }else{
            final String currentText = statusBar.getText();
            statusBar.setText(text);
            return currentText;
        }
    }

    public void writeToCookies(final String name, final String value) {
        rootPanel.writeToCookies(name, value);
    }
    
    void blockEvents() {
        eventsBlocked = true;
    }

    void unblockEvents() {
        eventsBlocked = false;
    }

    void forceEvents() {
        lastPingTime = 0;
    }

    private void processEvents() {
        if (!eventsBlocked) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastPingTime > PING_PERIOD) {
                lastPingTime = currentTime;
                ((WpsProgressHandleManager) getProgressHandleManager()).ping();
            }
        }
    }

    @Override
    public IEventLoop newEventLoop() {
        return new WpsEventLoop();
    }

    @Override
    public <T> T runInGuiThread(final Callable<T> task) throws InterruptedException, ExecutionException {
        if (getApplication().isInGuiThread()){
            try{
                return task.call();
            }catch(HttpSessionTerminatedError ex){
                throw ex;
            }catch(Exception ex){
                throw new ExecutionException(ex);
            }
        }else{
            final RwtSyncTaskExecutor<T> taskExecutor = new RwtSyncTaskExecutor<>(task);
            return taskExecutor.execute(context);
        }
    }

    @Override
    public void runInGuiThreadAsync(final Runnable task) {
        context.scheduleTask(task);
    }

    @Override
    public String signText(String text, X509Certificate certificate) throws SignatureException {
        if (checkSignAbility()) {
            return context.signText(text, certificate);
        } else {
            throw new SignatureException(SignatureException.EReason.USER_CANCELED, getLanguage() == EIsoLanguage.RUSSIAN ? "Операция прервана пользователем" : "Operation cancelled by user");
        }
    }

    public String jsInvoke(final UIObject obj, final String methodName, final String methodParam) throws InvocationTargetException {
        return jsInvoke(obj.getHtmlId(), methodName, methodParam);
    }

    public String jsInvoke(final String htmlId, final String methodName, final String methodParam) throws InvocationTargetException {
        return context.jsInvoke(htmlId, methodName, methodParam);
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
    
    public WebServerRunParams getRunParams(){
        return context.getRunParams();
    }

    public long getSessionTimeOut() {
        return context.rqCheckDelay;
    }

    public EWebBrowserEngineType getBrowserEngineType() {
        return context.getHttpConnectionOptions().getBrowserEngineType();
    }
    
    public LinkedHashMap<String, String> getVersionInfo() {
        final LinkedHashMap<String, String> versions = new LinkedHashMap<>();
        if (WpsEnvironment.class.getClassLoader() instanceof RadixClassLoader) {
            final RadixClassLoader loader = getDefManager().getClassLoader();
            List<LayerMeta> layers = loader.getRevisionMeta().getAllLayersSortedFromBottom();
            String releaseNumber;
            for (LayerMeta layer : layers) {
                releaseNumber = layer.getReleaseNumber();
                versions.put(layer.getUri(), releaseNumber == null ? "" : releaseNumber);
            }
        }
        return versions;
    }

    @Override
    public void writeConnectionParametersToXml(ConnectionParams xml) {
    }

    @Override
    public ProductInstallationOptions getProductInstallationOptions() {
        return productInstallationOptions;
    }
    
    private void handleEntryPoint(String entryPoint, Map<String, String> customParams) {
        List<IEntryPointHandler> entryList = new ArrayList<>();
        for (IEntryPointHandler handler: ServiceLoader.load(IEntryPointHandler.class, getDefManager().getClassLoader())){
            entryList.add(handler);
        }
        Collections.sort(entryList, new Comparator<IEntryPointHandler>() {
            @Override
            public int compare(IEntryPointHandler o1, IEntryPointHandler o2) {
                return -Integer.compare(o1.getPriority(), o2.getPriority());
            }
        });
        
        for (IEntryPointHandler handler : entryList) {
            if (handler.handle(new EntryPointRequest(this, entryPoint, customParams))) {
                break;
            }
        }
    }  
}
