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

import com.trolltech.qt.QSignalEmitter;
import com.trolltech.qt.QSignalEmitter.Signal0;
import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QEventLoop;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QMenuBar;
import com.trolltech.qt.gui.QMessageBox;
import com.trolltech.qt.gui.QStatusBar;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QWidget;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.dialogs.DialogFactory;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.editors.traceprofile.TraceProfileTreeController;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.env.ITaskWaiter;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.errors.UnsupportedDefinitionVersionError;
import org.radixware.kernel.common.client.exceptions.CantUpdateVersionException;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.ExceptionMessage;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.groupsettings.Filters;
import org.radixware.kernel.common.client.models.groupsettings.Sortings;
import org.radixware.kernel.common.client.trace.AbstractTraceBuffer;
import org.radixware.kernel.common.client.trace.JavaProxyLogger;
import org.radixware.kernel.common.client.tree.UserExplorerItemsStorage;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.utils.ISecretStore;
import org.radixware.kernel.common.client.widgets.IEditMaskEditorFactory;
import org.radixware.kernel.common.client.widgets.WidgetFactory;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.enums.EDrcServerResource;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.AppError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.trace.IRadixTrace;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.Explorer;
import org.radixware.kernel.explorer.dialogs.AboutDialog;
import org.radixware.kernel.explorer.dialogs.ChangePasswordDialog;
import org.radixware.kernel.explorer.dialogs.ConnectionsManager;
import org.radixware.kernel.explorer.dialogs.ExceptionMessageDialog;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.dialogs.ExplorerMessageBox;
import org.radixware.kernel.explorer.dialogs.settings.SettingsDialog;
import org.radixware.kernel.explorer.editors.editmask.EditMaskEditorFactory;
import org.radixware.kernel.explorer.env.progress.TaskWaiter;
import org.radixware.kernel.explorer.env.session.Connections;
import org.radixware.kernel.explorer.env.trace.ExplorerTraceItem;
import org.radixware.kernel.explorer.env.trace.ExplorerTracer;
import org.radixware.kernel.explorer.macros.gui.ExplorerMacrosWindow;
import org.radixware.kernel.explorer.tester.TesterWindow;
import org.radixware.kernel.explorer.text.ExplorerFontManager;
import org.radixware.kernel.explorer.text.TextOptionsManager;
import org.radixware.kernel.explorer.trace.TraceTrayItem;
import org.radixware.kernel.explorer.tree.ExplorerTreeManager;
import org.radixware.kernel.explorer.utils.LeakedWidgetsDetector;
import org.radixware.kernel.explorer.utils.QtJambiExecutor;
import org.radixware.kernel.explorer.views.MainWindow;
import org.radixware.kernel.explorer.utils.EQtStyle;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.schemas.eas.CreateSessionRs;
import org.radixware.schemas.eas.Definition;


public class Application extends QObject implements IClientApplication {

    private static class UnsupportedVersionEvent extends QEvent {

        public UnsupportedVersionEvent() {
            super(QEvent.Type.User);
        }
    }   
    
    private static class ForcedDisconnectEvent extends QEvent{
        
        private final boolean onExit;
        private final Runnable task;
        
        public ForcedDisconnectEvent(final boolean onExit, final Runnable onDisconnect){
            super(QEvent.Type.User);
            this.onExit = onExit;            
            task = onDisconnect;
        }
        
        public boolean onExit(){
            return onExit;
        }
        
        public Runnable getAfterDisconnectTask(){
            return task;
        }        
    }
    
    private final static class ExecuteEvent extends QEvent {

        private final Runnable task;

        public ExecuteEvent(final Runnable task) {
            super(QEvent.Type.User);
            this.task = task;
        }

        public void execute() {
            task.run();
        }
    }

    
    private final static class QuitListener implements Explorer.IQuitListener{
                
        private final Environment environment;
        
        public QuitListener(final Environment environment){
            this.environment = environment;
        }

        @Override
        public boolean beforeQuit() {
            if (environment.getEasSession().isBusy()){
                environment.getEasSession().breakRequest();                
                return false;
            }
            environment.disconnect(true, true);            
            return true;
        }                
    }        
   
    @Override
    public ERuntimeEnvironmentType getRuntimeEnvironmentType() {
        return ERuntimeEnvironmentType.EXPLORER;
    }

    @Override
    public List<EIsoLanguage> getLanguages() {
        return defManager.getRepository().getLanguages();
    }

    public static class Actions extends QSignalEmitter {

        public final Signal0 settingsChanged = new Signal0();
        public final QAction connect;
        public final QAction disconnect;
        public final QAction forcedDisconnect;
        public final QAction changePassword;
        public final QAction runSettingsDialog;
        public final QAction clearSettings;
        public final QAction showConnectionsManager;
        public final QAction showTrace;
        public final QAction checkForUpdates;
        public final QAction showAbout;
        public final QAction runTester;
        public final QAction runMacrosWindow;
        public final QAction memoryLeakDetector;
        public final QAction restart;
        public final QAction exit;
        private final Application app;

        @SuppressWarnings("LeakingThisInConstructor")
        public Actions(final Application application, final MainWindow mainWindow) {
            app = application;
            connect = new QAction(mainWindow);
            connect.setIcon(ExplorerIcon.getQIcon(ClientIcon.Connection.CONNECT));
            connect.triggered.connect(this, "connectAction()");

            disconnect = new QAction(mainWindow);
            disconnect.setIcon(ExplorerIcon.getQIcon(ClientIcon.Connection.DISCONNECT));
            disconnect.triggered.connect(this, "disconnectAction()");
            disconnect.setEnabled(false);

            forcedDisconnect = new QAction(mainWindow);
            forcedDisconnect.triggered.connect(this, "forcedDisconnectAction()");

            changePassword = new QAction(mainWindow);
            changePassword.setIcon(ExplorerIcon.getQIcon(ClientIcon.Connection.KEY));
            changePassword.triggered.connect(this, "changePasswordAction()");
            changePassword.setEnabled(false);

            showTrace = new QAction(mainWindow);
            showTrace.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.TRACE));
            showTrace.triggered.connect(this, "showTraceAction()");

            showAbout = new QAction(mainWindow);
            showAbout.setMenuRole(QAction.MenuRole.AboutRole);
            showAbout.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.ABOUT));
            showAbout.triggered.connect(this, "showAbout()");

            showConnectionsManager = new QAction(mainWindow);
            showConnectionsManager.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.CONNECTIONS_MANAGER));
            showConnectionsManager.triggered.connect(this, "showConnectionsManagerAction()");

            checkForUpdates = new QAction(mainWindow);
            checkForUpdates.setShortcut(new QKeySequence("Ctrl+F9"));
            checkForUpdates.triggered.connect(this, "checkForUpdates()");

            runSettingsDialog = new QAction(mainWindow);
            runSettingsDialog.setMenuRole(QAction.MenuRole.PreferencesRole);
            runSettingsDialog.setIcon(ExplorerIcon.getQIcon(SettingsDialog.DialogIcons.APPEARANCE_SETTINGS));
            runSettingsDialog.triggered.connect(this, "runSettingsAction()");

            clearSettings = new QAction(mainWindow);
            clearSettings.setIcon(ExplorerIcon.getQIcon(SettingsDialog.DialogIcons.RESET_SETTINGS));
            clearSettings.triggered.connect(this, "clearSettingsAction()");

            runTester = new QAction(mainWindow);
            runTester.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.TESTER));
            runTester.triggered.connect(this, "runTesterAction()");

            runMacrosWindow = new QAction(mainWindow);
            runMacrosWindow.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.TESTER));
            runMacrosWindow.triggered.connect(this, "runMacrosWindowAction()");

            memoryLeakDetector = new QAction(mainWindow);
            memoryLeakDetector.setCheckable(true);
            memoryLeakDetector.setChecked(false);
            memoryLeakDetector.setEnabled(false);
            memoryLeakDetector.toggled.connect(this,"memoryLeakDetectorAction(boolean)");
            
            restart = new QAction(mainWindow);
            restart.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.REFRESH));
            restart.triggered.connect(this,"restartAction()");
            
            exit = new QAction(mainWindow);
            exit.setMenuRole(QAction.MenuRole.QuitRole);
            exit.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.EXIT));
            exit.triggered.connect(this, "exitAction()");

            
            translate();
        }

        @SuppressWarnings("unused")
        private void connectAction() {
            if (app.getEnvironmentImpl().connect()) {
                connect.setEnabled(false);
                disconnect.setEnabled(true);
            }
        }

        @SuppressWarnings("unused")
        private void changePasswordAction() {
            if (getConnectionOptions() != null) {
                ChangePasswordDialog dialog = new ChangePasswordDialog(app.getEnvironmentImpl(), getMainWindow());
                {
                    final String title = Application.translate("ChangePasswordDialog", "Change Password for '%s' Account");
                    dialog.setTitle(String.format(title, app.getEnvironmentImpl().getUserName()));
                }
                while (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
                    try {
                        app.getEnvironmentImpl().getEasSession().changePassword(dialog.getOldPassword(), dialog.getNewPassword());
                        final String title = Application.translate("ExplorerMessage", "Success!");
                        final String message = Application.translate("ExplorerMessage", "Your password was successfully updated!");
                        Application.messageInformation(title, message);
                        return;
                    } catch (InterruptedException ex) {
                    } catch (ServiceCallFault fault) {
                        final String title = Application.translate("ChangePasswordDialog", "Can`t Change Password");
                        if (org.radixware.schemas.eas.ExceptionEnum.INVALID_PASSWORD.toString().equals(fault.getFaultString())) {                            
                            final String message = Application.translate("ChangePasswordDialog", "Current password is not correct");
                            Application.messageError(title, message);
                        } else {
                            app.processException(title, fault);
                            if (!ClientException.isSpecialFault(fault)) {
                                return;
                            }
                        }
                    } catch (ServiceClientException ex) {
                        app.processException(ex);
                    } finally {
                        dialog.clear();
                    }
                }
            }
        }

        @SuppressWarnings("unused")
        private void showConnectionsManagerAction() {
            app.showConnectionsManager();
        }

        @SuppressWarnings("unused")
        private void disconnectAction() {
            final String title = 
                app.getMessageProvider().translate("ExplorerMessage", "Confirm to Disconnect");
            final String message = 
                app.getMessageProvider().translate("ExplorerMessage", "Do you really want to disconnect?");
            if (Application.messageConfirmation(title, message)){
                app.disconnect(false);
            }
        }

        @SuppressWarnings("unused")
        private void forcedDisconnectAction() {
            app.forcedDisconnect(false,null);
        }

        @SuppressWarnings("unused")
        private void exitAction() {
            app.exit(false);
        }

        @SuppressWarnings("unused")
        private void runTesterAction() {
            org.radixware.kernel.explorer.tester.TesterWindow tester = new org.radixware.kernel.explorer.tester.TesterWindow(app.getEnvironmentImpl());
            tester.exec();
        }
        
        private org.radixware.kernel.explorer.macros.gui.ExplorerMacrosWindow macrosWindow;

        @SuppressWarnings("unused")
        private void runMacrosWindowAction() {
            if (macrosWindow == null) {
                macrosWindow = new ExplorerMacrosWindow(app.getEnvironmentImpl());
            }
            macrosWindow.show();
        }
        
        private void memoryLeakDetectorAction(final boolean isEnabled){
            app.getEnvironmentImpl().getConfigStore().writeBoolean(SettingNames.SYSTEM+"/"+SettingNames.MEM_LEAK_DETECTOR, isEnabled);
            LeakedWidgetsDetector.getInstance().setEnabled(isEnabled);
        }
                
        private SettingsDialog settingsDialog = null;

        @SuppressWarnings("unused")
        private void runSettingsAction() {
            if (settingsDialog == null) {
                settingsDialog = new SettingsDialog(app.getEnvironmentImpl(), app.mainWindow);
            }
            settingsDialog.exec();
        }

        @SuppressWarnings("unused")
        private void clearSettingsAction() {
            final String title = Application.translate("ExplorerMessage", "Confirm to Reset Settings");
            final String message = Application.translate("ExplorerMessage", "Do you really want to reset all settings?");
            if (messageConfirmation(title, message)) {
                getInstance().getEnvironmentImpl().getConfigStore().clear();
                getInstance().getEnvironmentImpl().getFilterSettingsStorage().clear();
                settingsChanged.emit();
            }
        }

        @SuppressWarnings("unused")
        private void showTraceAction() {
            final Qt.WindowStates state = getInstance().getEnvironmentImpl().getTraceDialog().windowState();
            //clear WindowMinimized flag and set WindowActive flag
            state.setValue(state.value() & ~Qt.WindowState.WindowMinimized.value()
                    | Qt.WindowState.WindowActive.value());
            getInstance().getEnvironmentImpl().getTraceDialog().setWindowState(state);
            getInstance().getEnvironmentImpl().getTraceDialog().show();
            getInstance().getEnvironmentImpl().getTraceDialog().raise();
            getInstance().getEnvironmentImpl().getTraceDialog().activateWindow();
        }

        @SuppressWarnings("unused")
        private void checkForUpdates() {
            if (getTreeManager() != null) {
                final Collection<Id> definitionIds = app.getDefManager().getAdsVersion().checkForUpdates(app.getEnvironment());
                if (app.getDefManager().getAdsVersion().isNewVersionAvailable()) {
                    final String title = Application.translate("ExplorerMessage", "Confirm to Update Version");
                    if (app.getDefManager().getAdsVersion().isKernelWasChanged()) {
                        final String message = Application.translate("ExplorerMessage", "New version found. It is necessary to restart explorer to install this version.\nDo you want to restart now ?");
                        if (messageConfirmation(title, message)) {
                            Explorer.restart();
                        }
                    } else {
                        final String message = Application.translate("ExplorerMessage", "New version found. Do you want to update now?");
                        if (messageConfirmation(title, message)) {
                            getTreeManager().updateVersion(definitionIds);
                        }
                    }
                } else {
                    final String message = Application.translate("ExplorerMessage", "There are no updates available for explorer");
                    messageInformation(null, message);
                }
            }
        }
        
        @SuppressWarnings("unused")
        private void restartAction(){
            Explorer.restart();
        }

        @SuppressWarnings("unused")
        private void showAbout() {
            AboutDialog dialog = new AboutDialog(getInstance().getEnvironmentImpl());
            dialog.exec();
        }

        private void translate() {
            connect.setText(Application.translate("EnvironmentAction", "&Connect..."));
            connect.setToolTip(Application.translate("EnvironmentAction", "Connect to Server"));
            disconnect.setText(Application.translate("EnvironmentAction", "&Disconnect"));
            disconnect.setToolTip(Application.translate("EnvironmentAction", "Disconnect"));
            changePassword.setText(Application.translate("EnvironmentAction", "Change &Password..."));
            changePassword.setToolTip(Application.translate("EnvironmentAction", "Change Password for Current Account"));
            showConnectionsManager.setText(Application.translate("EnvironmentAction", "&Connections Manager"));
            showTrace.setText(Application.translate("EnvironmentAction", "&Trace"));
            showAbout.setText(Application.translate("EnvironmentAction", "&About"));
            checkForUpdates.setText(Application.translate("EnvironmentAction", "&Check for Updates"));
            runSettingsDialog.setText(Application.translate("EnvironmentAction", "&Appearance Settings..."));
            clearSettings.setText(Application.translate("EnvironmentAction", "&Reset Settings"));
            exit.setText(Application.translate("EnvironmentAction", "&Exit"));
            runTester.setText(Application.translate("EnvironmentAction", "Teste&r"));
            runMacrosWindow.setText(Application.translate("EnvironmentAction", "&Macros"));
            memoryLeakDetector.setText(Application.translate("EnvironmentAction", "Memory Leaks Detection"));
            restart.setText(Application.translate("EnvironmentAction", "Restart Application"));
            settingsDialog = null;
        }
    }        
    
    private static class ShowQMessageBoxTask implements Callable<QMessageBox.StandardButton>{        
        private final QWidget parent;
        private final String title;
        private final String message;
        private final QMessageBox.Icon icon;
        private final QMessageBox.StandardButtons buttons;
        private final QMessageBox.StandardButton defaultButton;
        
        public ShowQMessageBoxTask(final QWidget parent, final String title, final String message, final QMessageBox.Icon icon, final QMessageBox.StandardButtons buttons, final QMessageBox.StandardButton defaultButton){
            this.parent = parent;
            this.title = title;
            this.message = message;
            this.icon = icon;
            this.buttons = buttons;
            this.defaultButton = defaultButton;
        }
        
        @Override
        public QMessageBox.StandardButton call() {
            return ExplorerMessageBox.showMessage(parent, title, message, icon, buttons, defaultButton);
        }                
    }

    private static class ShowEMessageBoxTask implements Callable<EDialogButtonType>{        
        private final String title;
        private final String message;
        private final EDialogIconType icon;
        private final Set<EDialogButtonType> buttons;
        private final EDialogButtonType defaultButton;
        
        public ShowEMessageBoxTask(final String title, final String message, final EDialogIconType icon, final Set<EDialogButtonType> buttons, final EDialogButtonType defaultButton){
            this.title = title;
            this.message = message;
            this.icon = icon;
            this.buttons = buttons;
            this.defaultButton = defaultButton;
        }
        
        @Override
        public EDialogButtonType call() {
            return ExplorerMessageBox.showMessage(title, message, icon, buttons, defaultButton);
        }
    }        
    
    private class AppTraceBufferListener implements AbstractTraceBuffer.TraceBufferListener<ExplorerTraceItem>{
        
        @Override
        public void newItemInBuffer(ExplorerTraceItem traceItem) {
        }

        @Override
        public void maxSeverityChanged(final EEventSeverity eventSeverity) {
            QApplication.postEvent(traceTrayItem, new TraceTrayItem.ChangeSeverityEvent(eventSeverity));
        }

        @Override
        public void cleared() {
        }
    }
    
    private final static class AwtWindowWaiter  extends QEventLoop{
        
        private final static int CHECK_INTERVAL_MILLIS = 100;
        private final static EnumSet<QEvent.Type> INPUT_EVENT_TYPES = EnumSet.of(QEvent.Type.Shortcut,
                                                                                 QEvent.Type.ShortcutOverride,
                                                                                 QEvent.Type.Wheel,
                                                                                 QEvent.Type.KeyPress,
                                                                                 QEvent.Type.KeyRelease,
                                                                                 QEvent.Type.Enter,
                                                                                 QEvent.Type.Leave,
                                                                                 QEvent.Type.MouseButtonPress, 
                                                                                 QEvent.Type.MouseButtonDblClick, 
                                                                                 QEvent.Type.MouseButtonRelease);
        
        private final QEventFilter userInputFilter = new QEventFilter(this);
        
        private java.awt.Window window;
        private int timerId;
        
        public AwtWindowWaiter (final QObject parent){
            super(parent);
            userInputFilter.setProhibitedEventTypes(INPUT_EVENT_TYPES);
        }
        
        public void waitForClose(final java.awt.Window awtWindow){
            window = awtWindow;
            timerId = startTimer(CHECK_INTERVAL_MILLIS);
            QApplication.instance().installEventFilter(userInputFilter);
            exec();
        }

        @Override
        protected void timerEvent(final QTimerEvent event) {
            if (event.timerId()==timerId){
                if (window.isDisplayable()){                    
                    if (QApplication.activeWindow()!=null){                        
                        window.toFront();//Works not for all platforms
                    }
                }else{
                    exit();
                    QApplication.instance().removeEventFilter(userInputFilter);
                }
                event.accept();
            }else{
                super.timerEvent(event);
            }
        }                        
    }
    
    private Actions actions;
    private MainWindow mainWindow;
    private final URL baseUrl;
    private MessageFilter messageFilter = null;
    private ArrayList<Id> contextlessCommands;
    private ArrayList<EDrcServerResource> allowedResources;
    private final String defaultStyleName;
    private String currentStyleName;
    //private final Connections connections;
    private QMenu optionsMenu;
    //   private static TraceDialog traceDialog;
    private TraceTrayItem traceTrayItem;
    private final AppTraceBufferListener traceListener = new AppTraceBufferListener();
    final QToolBar mainToolBar = new QToolBar();
    private final Environment environment;
    private final QtEventsDispatcher eventsDispatcher;
    private boolean disconnecting;
    private final DefManager defManager;
    private final QtJambiExecutor qtExecutor = new QtJambiExecutor(this);
    private final QEventLoop pauseLoop = new QEventLoop(this);
    private AwtWindowWaiter awtWindowWaiter;//do not create instance in constructor. It does not work on OSX.
    private ExplorerStandardViewsFactory viewsFactory;
    private QObject inputFilter;
    private static Application instance;    
    
    private final AdsVersion.VersionListener adsVersionListener = new AdsVersion.VersionListener() {

        @Override
        public void versionUpdated() {
            environment.onAdsVersionUpdate();
        }
    };

    @SuppressWarnings("LeakingThisInConstructor")
    protected Application(final MainWindow window) {
        super(window);
        baseUrl = getJnlpUrl();
        mainWindow = window;
        environment = new Environment(this, mainWindow);
        viewsFactory = new ExplorerStandardViewsFactory(mainWindow,environment);
        defManager = new DefManager(this) {

            @Override
            protected AdsVersion createVersion(IClientApplication env) {
                return new org.radixware.kernel.explorer.env.AdsVersion(env, environment);
            }
        };
        if (RadixLoader.getInstance() != null) {
            defManager.getAdsVersion().addVersionListener(adsVersionListener);
        }
        if (EQtStyle.getDefault()!=EQtStyle.Unknown){
            defaultStyleName = EQtStyle.getDefault().getTitle();
            QApplication.setStyle(defaultStyleName);
        }else{
            defaultStyleName = "";
        }
        currentStyleName = defaultStyleName;
       
        //RADIX-2890
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                processException(e);
            }
        });
        AWTExceptionHandler.register();
       
        if (SystemTools.isOSX){            
            if (mainWindow!=null && mainWindow.frameGeometry().width()==mainWindow.statusBar().frameGeometry().width()){
                mainWindow.statusBar().setContentsMargins(2, 0, 8, 0);
            }
        }
        eventsDispatcher = new QtEventsDispatcher(this, environment.getEasSession());
        Explorer.addQuitListener(new QuitListener(environment));
    }    
    
    private static URL getJnlpUrl(){
        final Class<?> jnlpBasicServiceClass;
        final Class<?> jnlpServiceManagerClass;
        final Method lookup;
        final Method getCodeBase;
        try{
            jnlpServiceManagerClass = Class.forName("javax.jnlp.ServiceManager");
            jnlpBasicServiceClass = Class.forName("javax.jnlp.BasicService");
            lookup = jnlpServiceManagerClass.getDeclaredMethod("lookup", String.class);
            getCodeBase = jnlpBasicServiceClass.getDeclaredMethod("getCodeBase");
        }catch(ClassNotFoundException | LinkageError | NoSuchMethodException | SecurityException e){//NOPMD
            return null;
        }
        final Object jnlpServiceInstance;
        try{
            jnlpServiceInstance = lookup.invoke(null, "javax.jnlp.BasicService");
        }catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e){//NOPMD
            return null;
        }
        if (jnlpBasicServiceClass.isInstance(jnlpServiceInstance)){
            final Object invocationResult;
            try{
                invocationResult = getCodeBase.invoke(jnlpServiceInstance);
            }catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e){//NOPMD
                return null;
            }
            return invocationResult instanceof URL ? (URL)invocationResult : null;
        }else{
            return null;
        }
    }
            
    private void closeTracer(){
        getTracer().getBuffer().removeTraceBufferListener(traceListener);
        getTracer().close();
    }
    
    public boolean exit(final boolean forced){
        try {
            if (!forced){
                final String title = getMessageProvider().translate("ExplorerMessage", "Confirm to Close Application");
                final String message = getMessageProvider().translate("ExplorerMessage", "Do you really want to close application?");
                if (!messageConfirmation(title, message)){
                    return false;
                }
            }
            if (!disconnectImpl(forced,true)) {
                return false;
            }
            getEnvironmentImpl().closeTraceDialog();
            if (mainWindow != null) {
                ((ExplorerSettings)getEnvironmentImpl().getConfigStore()).writeQByteArray(SettingNames.SYSTEM + "/" + "mainWindowGeometry", mainWindow.saveGeometry());
            }
            getStandardViewsFactory().blockScheduledViewCreation();
            getStandardViewsFactory().clearPools();
            getEnvironmentImpl().getConfigStore().sync();
            getTracer().debug(getMessageProvider().translate("TraceMessage", "Closing application"));
            getActions().exit.disconnect();
            Thread.currentThread().setUncaughtExceptionHandler(null);
            closeTracer();
            UserExplorerItemsStorage.clearCache(this);
            Filters.clearCache(this);
            Sortings.clearCache(this); 
            TraceProfileTreeController.clearCache(this);
            JavaProxyLogger.clean();            
        } catch (Throwable exception) {
            if (!ClientException.isSystemFault(exception)) {
                if (forced){
                    exception.printStackTrace();                    
                }else{
                    processException(exception);
                }
            }
            getEnvironmentImpl().getEasSession().close(true);
        }
        Thread.currentThread().setUncaughtExceptionHandler(null);
        if (RadixLoader.getInstance()!=null){
            RadixLoader.getInstance().setActualizeController(null);
        }
        messageFilter = null;
        if (mainWindow!=null){
            mainWindow.forceClose();
            mainWindow = null;
        }
        if (instance.environment!=null){
            instance.environment.localeManager.clear();
        }
        WidgetUtils.CustomStyle.releaseAll();
        instance = null;
        com.trolltech.qt.gui.QApplication.exit();
        return true;
    }

    @SuppressWarnings("unused")
    private void showConnectionsManager() {
        final Connections connections = getEnvironmentImpl().getConnections();
        final ConnectionsManager connectionsDialog = new ConnectionsManager(getEnvironmentImpl(), mainWindow, connections);
        connectionsDialog.exec();
        connections.store();
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
                    final String message = translate("ExplorerError", "Unknown server resource \'%s\'");
                    getTracer().put(EEventSeverity.WARNING, String.format(message, item.getTitle()), EEventSource.EXPLORER);
                }
            }
        }
    }

    public static MainWindow getMainWindow() {
        return instance.mainWindow;
    }

    public static Locale getLocale() {
        return getInstance().getEnvironmentImpl().localeManager.getLocale();
    }

    public static String getWorkPath() {
        return getInstance().getEnvironmentImpl().workPath != null ? getInstance().getEnvironmentImpl().workPath.absolutePath() : null;
    }

    public URL getCodeBase() {
        return baseUrl;
    }

    public static void applySettings(ExplorerSettings settings) {
        settings.clearCache();
        getInstance().getEnvironmentImpl().applySettings(settings);
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.APP_STYLE);
        final String stylename = settings.readString(SettingNames.STYLENAME);//, getDefaultStyleName());        
        settings.endGroup();
        settings.endGroup();
        setStyle(stylename);
        getInstance().actions.settingsChanged.emit();
    }

    public static String getDefaultStyleName() {
        return instance.defaultStyleName;
    }
    
    public static String getCurrentStyleName() {
        return instance.currentStyleName;
    }
    
    static void setStyle(final String styleName){        
        if (!Objects.equals(instance.currentStyleName, styleName)){
            instance.getTracer().debug("Changing application style to "+styleName);
            QApplication.setStyle(styleName);
            instance.currentStyleName = styleName;
            instance.getTracer().debug("Current application style is "+QApplication.style().objectName());
        }
    }

    private Environment getEnvironmentImpl() {
        return environment;
    }

    public IClientEnvironment getEnvironment() {
        return getEnvironmentImpl();
    }

    private void processUnsupportedVersionException() {
        getDefManager().getAdsVersion().makeUnsupported();
        if (!getDefManager().getAdsVersion().isNewVersionAvailable()) {
            getTracer().warning(translate("TraceMessage", "Current definition version is not supported by server, but client is not in old version mode"));
            if (getDefManager().getAdsVersion().checkForUpdates(getEnvironment()) == null) {
                getTracer().error(translate("TraceMessage", "Current definition version is not supported by server, but client have not newer version"));
                messageError(translate("ExplorerError", "Current version is no longer supported"));
                return;
            }
        }

        if (getDefManager().getAdsVersion().isKernelWasChanged()) {
            final String title = Application.translate("ExplorerMessage", "Confirm to Restart");
            final String message = translate("ExplorerMessage",
                    "Current version is no longer supported. It is impossible to continue work until explorer will be restarted.\n"
                    + "Do you want to restart now (all your unsaved data will be lost) ?");
            if (messageConfirmation(title, message)) {
                Explorer.restart();
            }
            return;
        }

        final String title = Application.translate("ExplorerMessage", "Confirm to Update Version");
        final String message = translate("ExplorerMessage",
                "Current version is no longer supported. It is impossible to continue work until updates will be installed.\n"
                + "Do you want to update now (all your unsaved data will be lost) ?");

        if (messageConfirmation(title, message)) {
            final Collection<Id> definitionIds = getDefManager().getAdsVersion().checkForUpdates(getEnvironment());
            getDefManager().getAdsVersion().checkForUpdates(getEnvironment());
            try {
                getTreeManager().updateVersion(definitionIds);
            } catch (CantUpdateVersionException ex) {
                ex.showMessage(getEnvironmentImpl());
            } catch (Exception ex) {
                processException(ex);
            }
        }
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof UnsupportedVersionEvent) {
            event.accept();
            processUnsupportedVersionException();
            return;
        } else if (event instanceof ForcedDisconnectEvent){
            event.accept();
            final ForcedDisconnectEvent disconnectEvent = (ForcedDisconnectEvent)event;
            if (tryToForcedDisconnect(disconnectEvent.onExit(), disconnectEvent.getAfterDisconnectTask()) && inputFilter!=null){
                eventsDispatcher.unblock();
                QApplication.instance().removeEventFilter(inputFilter);
                disconnecting = false;
                final Runnable task = disconnectEvent.getAfterDisconnectTask();
                if (task!=null){
                    task.run();
                }
            }
        } else if (event instanceof ExecuteEvent){
            event.accept();
            ((ExecuteEvent)event).execute();
        }
        super.customEvent(event);
    }
    
    public void processException(final Throwable e) {
        processException(null, e);
    }

    public void processException(final String title, final Throwable e) {
        if (e instanceof InterruptedException) {
            return;
        }
        for (Throwable err = e; err != null && err.getCause() != err; err = err.getCause()) {
            if (err instanceof UnsupportedDefinitionVersionError) {
                QApplication.removePostedEvents(this);//предусмотрена ситуация последовательного выполнения нескольких запросов
                Application.processEventWhenEasSessionReady(this, new UnsupportedVersionEvent());
                return;
            }
        }        
        final ExceptionMessage exceptionMessage = new ExceptionMessage(getEnvironment(), e);
        final String dialogTitle;
        if (title==null || title.isEmpty()){
            dialogTitle = exceptionMessage.getDialogTitle();
        }else{
            dialogTitle = ClientValueFormatter.capitalizeIfNecessary(getEnvironmentImpl(), title);
        }
        exceptionMessage.trace(getTracer(), title);
        if (exceptionMessage.getDetails()!=null && !exceptionMessage.getDetails().isEmpty()
            && messageFilter != null
            && !messageFilter.beforeMessageException(dialogTitle, exceptionMessage.getDialogMessage(), exceptionMessage.getDetails(), e)) {
            return;
        }
        if (exceptionMessage.hasDialogMessage()) {
            getEnvironment().getProgressHandleManager().blockProgress();
            try {
                exceptionMessage.display(dialogTitle, mainWindow);
            } finally {
                getEnvironment().getProgressHandleManager().unblockProgress();
            }            
        }
        if (exceptionMessage.getSeverity() == EEventSeverity.ALARM) {
            actions.forcedDisconnect.trigger();
        }
    }

    public static void installMessageFilter(MessageFilter filter) {
        if (Application.class.getClassLoader() != filter.getClass().getClassLoader()) {
            throw new IllegalArgumentException("Message filter cannot be instance of versioned class");
        }
        instance.messageFilter = filter;
    }

    public static void removeMessageFilter() {
        instance.messageFilter = null;
    }

    public static MessageFilter getMessageFilter() {
        return instance.messageFilter;
    }

    //Messages
    public static void messageInformation(final String message) {
        messageInformation(null, message);
    }

    public static void messageInformation(final String title, final String message) {
        messageBox(title, message, QMessageBox.Icon.Information,
                new QMessageBox.StandardButtons(QMessageBox.StandardButton.Ok), null);

    }

    public static void messageWarning(final String message) {
        messageWarning(null, message);
    }

    public static void messageWarning(final String title, final String message) {
        messageBox(title, message, QMessageBox.Icon.Warning,
                new QMessageBox.StandardButtons(QMessageBox.StandardButton.Ok), null);
    }

    public static void messageError(final String message) {
        messageError(null, message);
    }

    public static void messageError(final String title, final String message) {
        messageBox(title, message, QMessageBox.Icon.Critical,
                new QMessageBox.StandardButtons(QMessageBox.StandardButton.Ok), null);        
    }

    public static boolean messageConfirmation(final String message) {
        return messageConfirmation(null, message);
    }

    public static boolean messageConfirmation(final String title, final String message) {
        return messageBox(title, message, QMessageBox.Icon.Question,
                new QMessageBox.StandardButtons(QMessageBox.StandardButton.Yes,
                QMessageBox.StandardButton.No), null) == QMessageBox.StandardButton.Yes;
    }

    public static Boolean messageQuestion(final String message) {
        return messageQuestion(null, message);
    }

    public static Boolean messageQuestion(final String title, final String message) {
        QMessageBox.StandardButton res = messageBox(
                title, message, QMessageBox.Icon.Question,
                new QMessageBox.StandardButtons(
                        QMessageBox.StandardButton.Yes,
                        QMessageBox.StandardButton.No,
                        QMessageBox.StandardButton.Cancel
                ), null);
        if (res == QMessageBox.StandardButton.Yes)
            return true;
        else if (res == QMessageBox.StandardButton.No)
            return false;
        else
            return null;
    }

    public static QMessageBox.StandardButton messageBox(final String title, final String message, final QMessageBox.Icon icon, final QMessageBox.StandardButtons buttons, final QMessageBox.StandardButton defaultButton) {
        return messageBoxImpl(getMainWindow(),title, message, icon, buttons, defaultButton);
    }

    public static QMessageBox.StandardButton messageBox(final String title, final String message, final QMessageBox.Icon icon, final QMessageBox.StandardButtons buttons) {
        return messageBoxImpl(getMainWindow(),title, message, icon, buttons, null);
    }

    public static EDialogButtonType messageBox(final String title, final String message, final EDialogIconType icon, final Set<EDialogButtonType> buttons) {
        return messageBoxImpl(title,  message,  icon,  buttons, null);
    }

    public static EDialogButtonType messageBox(final String title, final String message, final EDialogIconType icon, final Set<EDialogButtonType> buttons, final EDialogButtonType defaultButton) {
        return messageBoxImpl(title,  message,  icon,  buttons, defaultButton);
    }
    
    private static QMessageBox.StandardButton messageBoxImpl(final QWidget parent, final String title, final String message, final QMessageBox.Icon icon, final QMessageBox.StandardButtons buttons, final QMessageBox.StandardButton defaultButton){        
        final ShowQMessageBoxTask task = new ShowQMessageBoxTask(parent, title, message, icon, buttons, defaultButton);
        if (QApplication.instance().thread()==Thread.currentThread()){
            return task.call();
        }else{
            try{
                return getInstance().qtExecutor.invoke(task);
            }catch(InterruptedException | ExecutionException exception){
                throw new AppError("Failed to show message box dialog",exception);
            }
        }
    }
    
    private static  EDialogButtonType messageBoxImpl(final String title, final String message, final EDialogIconType icon, final Set<EDialogButtonType> buttons, final EDialogButtonType defaultButton){
        final ShowEMessageBoxTask task = new ShowEMessageBoxTask(title, message, icon, buttons, defaultButton);
        if (QApplication.instance().thread()==Thread.currentThread()){
            return task.call();
        }else{
            try{
                return getInstance().qtExecutor.invoke(task);
            }catch(InterruptedException | ExecutionException exception){
                throw new AppError("Failed to show message box dialog",exception);
            }
        }
    }

    public static void messageException(final String title, final String message, final Throwable e) {
        getInstance().messageExceptionImpl(title, message, e);
    }

    private void messageExceptionImpl(final String title, final String message, final Throwable e) {        
        if (ClientException.isSpecialFault(e)) {
            if (ClientException.isInformationMessage(e)) {
                messageInformation(title, ClientException.getSpecialFaultMessage(getEnvironmentImpl(), e));
            } else {
                messageError(ClientException.getSpecialFaultMessage(getEnvironmentImpl(), e));
            }
            return;
        }
        final String detail, trace;
        if (e instanceof IClientError) {
            detail = ((IClientError) e).getDetailMessage(getMessageProvider());
            trace = ClientException.exceptionStackToString(e);
        } else if (e instanceof ServiceCallFault) {
            detail = ClientException.getSerivceCallFaultDetails((ServiceCallFault) e);
            trace = ((ServiceCallFault) e).getCauseExStack();
        } else {
            detail = "exception: " + e.getClass().getName()
                    + "\nexception message: " + e.getMessage();
            trace = ClientException.exceptionStackToString(e);
        }

        final String windowTitle = ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), title);
        if (messageFilter != null && !messageFilter.beforeMessageException(windowTitle, message, detail, e)) {
            return;
        }
        
        /* boolean treeIsLocked = false;
         * if (tree != null && tree.getCurrentTreeView() != null &&
         * tree.getCurrentTreeView().isLocked()) { treeIsLocked = true;
         * tree.getCurrentTreeView().unlock(); }
         */

        final ExceptionMessageDialog dialog =
                new ExceptionMessageDialog((ExplorerSettings) getEnvironmentImpl().getConfigStore(), mainWindow);

        getEnvironment().getProgressHandleManager().blockProgress();
        dialog.setTitle(windowTitle);
        dialog.setMessage(message);
        dialog.setDetails(detail, trace);
        dialog.exec();
        getEnvironment().getProgressHandleManager().unblockProgress();

        /*
         * if (treeIsLocked) { tree.getCurrentTreeView().lock(); }
         */
    }

    public static void beep() {
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

    public static LinkedHashMap<String, String> getVersionInfo() {
        final LinkedHashMap<String, String> versions = new LinkedHashMap<>();
        if (Explorer.class.getClassLoader() instanceof RadixClassLoader) {
            final RadixClassLoader loader;
            if (instance.isReleaseRepositoryAccessible()) {
                loader = instance.getDefManager().getClassLoader();
            } else {
                loader = (RadixClassLoader) Explorer.class.getClassLoader();
            }
            List<LayerMeta> layers = loader.getRevisionMeta().getAllLayersSortedFromBottom();
            String releaseNumber;
            for (LayerMeta layer : layers) {
                releaseNumber = layer.getReleaseNumber();
                versions.put(layer.getUri(), releaseNumber == null ? "" : releaseNumber);
            }
        }
        return versions;
    }

    public static String getProductName() {
        if (Starter.getProductName() != null && !Starter.getProductName().isEmpty()) {
            return Starter.getProductName();
        }
        if (RadixLoader.getInstance()==null){
            return "RadixWare";
        }
        final List<String> topLayerUris = RadixLoader.getInstance().getTopLayerUris();
        if (topLayerUris == null) {
            return "";
        } else if (topLayerUris.size() == 1) {
            try {
                final String title = RadixLoader.getInstance().getCurrentRevisionMeta().getTopLayers().get(0).getTitle();
                if (title == null || title.isEmpty()) {
                    return topLayerUris.get(0);
                } else {
                    return title;
                }
            } catch (RuntimeException ex) {
                return topLayerUris.get(0);
            }
        } else {
            return RadixLoader.getInstance().getTopLayerUrisAsString();
        }
    }

    void afterLanguageChange(final EIsoLanguage language) {
        mp = language==EIsoLanguage.ENGLISH ? new DummyMessageProvider() : new Mp();
        if (actions!=null){
            actions.translate();
            if (mainWindow!=null){
                createMenu();
            }
        }
        if (environment!=null) {
            if (mainWindow!=null && viewsFactory!=null){
                viewsFactory.clearPools();
                viewsFactory = new ExplorerStandardViewsFactory(mainWindow, environment);                
            }
            if (getTreeManager() != null){
                getTreeManager().translate();
            }
        }
    }
    
    @SuppressWarnings("SleepWhileInLoop")
    public static void showModalSwingDialog(final JFrame frame) {
        if (canShowModalAwtWindow()){
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            showModalAwtWindow(frame);
        }
    }
        

    @SuppressWarnings("SleepWhileInLoop")
    public static void showModalSwingDialog(final JDialog dialog) {
        if (canShowModalAwtWindow()){
            dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            showModalAwtWindow(dialog);
        }
    }
    
    static void showModalAwtWindow(final java.awt.Window window) {
        if (SystemTools.isOSX){
            final MessageProvider mp = getInstance().getMessageProvider();
            final String title = mp.translate("ExplorerMessage", "Unsupported operation");
            final String message = mp.translate("ExplorerMessage", "This operation is not supported on OSX platform");
            messageInformation(title, message);
            return;
        }
        final QWidget qtWindow = QApplication.activeWindow();
        boolean wasEnabled = false;
        final QEventFilter eventFilter;
        if (qtWindow!=null){
            eventFilter = new QEventFilter(qtWindow) {
                @Override
                public boolean eventFilter(final QObject receiver, final QEvent event) {
                    if (event!=null && event.type() == QEvent.Type.WindowActivate) {
                        event.ignore();
                        window.toFront();//works not for all platforms
                        return true;
                    }
                    if (event instanceof QCloseEvent) {
                        event.ignore();
                        return true;
                    }
                    return super.eventFilter(receiver, event);
                }
            };
            eventFilter.setProcessableEventTypes(EnumSet.of(QEvent.Type.WindowActivate, QEvent.Type.Close));
            qtWindow.installEventFilter(eventFilter);
            wasEnabled = qtWindow.isEnabled();
            qtWindow.setDisabled(true);
        }else{
            eventFilter = null;
        }
        window.setVisible(true);
        if (instance.awtWindowWaiter==null){
            instance.awtWindowWaiter=new AwtWindowWaiter(instance);
        }
        instance.awtWindowWaiter.waitForClose(window);

        if (qtWindow != null) {
            qtWindow.removeEventFilter(eventFilter);
            qtWindow.setEnabled(wasEnabled);
        }        
    }
    
    private static boolean canShowModalAwtWindow(){
        if (SystemTools.isOSX){
            final MessageProvider mp = getInstance().getMessageProvider();
            final String title = mp.translate("ExplorerMessage", "Unsupported operation");
            final String message = mp.translate("ExplorerMessage", "This operation is not supported on OSX platform");
            messageInformation(title, message);
            return false;
        }else{
            return true;
        }
    }

    protected void setupUi() {
        actions = new Actions(this,mainWindow);
        if (mainWindow != null) {
            QApplication.setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.EXPLORER));
            final String topLayerName = getProductName();
            final String windowTitle = String.format(translate("MainWindow", "%s Explorer"), topLayerName == null ? "RadixWare" : topLayerName);
            mainWindow.setWindowTitle(windowTitle);
            mainWindow.setObjectName("wndExplorerMainWindow");
            mainWindow.resize(new QSize(800, 575).expandedTo(mainWindow.minimumSizeHint()));
            {//main menu
                final QMenuBar menubar = new QMenuBar(mainWindow);
                menubar.setObjectName("menubar");
                menubar.setGeometry(new QRect(0, 0, 800, 29));
                mainWindow.setMenuBar(menubar);
            }
            {
                mainToolBar.setIconSize(new QSize(24,24));
                mainToolBar.addAction(actions.connect);
                mainToolBar.addAction(actions.disconnect);
                mainToolBar.setObjectName("main_window_toolbar");
                mainToolBar.setFloatable(false);
                mainToolBar.setMovable(false);
                mainWindow.addToolBar(mainToolBar);
                mainWindow.setContextMenuPolicy(Qt.ContextMenuPolicy.NoContextMenu);
            }
            {
                //status bar setup
                final QStatusBar statusBar = mainWindow.statusBar();
                if (statusBar!=null){                
                    traceTrayItem = new TraceTrayItem(statusBar, environment);
                    statusBar.addPermanentWidget(traceTrayItem);            
                }
            }

            createMenu();
            actions.translate();
        
            final String key = SettingNames.SYSTEM + "/" + "mainWindowGeometry";
            if (environment.getConfigStore().contains(key)) {
                mainWindow.restoreGeometry(((ExplorerSettings) environment.getConfigStore()).readQByteArray(key));
            }else{
                final QRect rec = mainWindow.rect();
                rec.moveCenter(QApplication.desktop().frameGeometry().center());
                mainWindow.move(rec.topLeft());                                            
            }
            
            getTracer().getBuffer().addTraceBufferListener(traceListener);
            if (!getTracer().getBuffer().isEmpty()){
                traceListener.maxSeverityChanged(getTracer().getBuffer().getMaxSeverity());
            }
            
            mainWindow.show();
        }        
    }

    private void createMenu() {
        mainWindow.menuBar().clear();
        final QMenu serverMenu = mainWindow.menuBar().addMenu(Application.translate("MainMenu", "&Server"));
        serverMenu.addAction(actions.connect);
        serverMenu.addAction(actions.disconnect);
        serverMenu.addAction(actions.changePassword);
        serverMenu.addSeparator();
        serverMenu.addAction(actions.showTrace);
        serverMenu.addAction(actions.checkForUpdates);
        serverMenu.addSeparator();
        if (SystemTools.isOSX){
            serverMenu.addAction(actions.showAbout);
        }
        serverMenu.addAction(actions.exit);
        ((ExplorerTreeManager) getEnvironmentImpl().getTreeManager()).setupMainMenu(mainWindow.menuBar());

        optionsMenu = mainWindow.menuBar().addMenu(Application.translate("MainMenu", "&Options"));
        optionsMenu.addAction(actions.runSettingsDialog);
        optionsMenu.addAction(actions.clearSettings);
        optionsMenu.addSeparator();
        optionsMenu.addAction(actions.showConnectionsManager);
        
        if (!SystemTools.isOSX){
            final QMenu helpMenu = mainWindow.menuBar().addMenu(Application.translate("MainMenu", "&Help"));
            helpMenu.addAction(actions.showAbout);
        }
        if (RunParams.isDevelopmentMode()) {
            final QMenu developmentMenu = mainWindow.menuBar().addMenu(Application.translate("MainMenu", "For &Developers"));
            developmentMenu.addAction(actions.runTester);
            //developmentMenu.addAction(actions.runMacrosWindow);
            developmentMenu.addAction(actions.memoryLeakDetector);
            developmentMenu.addAction(actions.restart);
        }
    }
    private TesterWindow autoTester;

    void startAutoTest(final String testOptionsFilePath) {
        final File testOptionsFile = new File(testOptionsFilePath);
        if (!testOptionsFile.exists()) {
            final String message = translate("ExplorerError", "Can't start testing: file '%s' not found");
            messageError(String.format(message, testOptionsFilePath));
        }
        final Id explorerItemId = RunParams.getTestExplorerItemId();
        if (explorerItemId != null && getTreeManager() != null && getTreeManager().getCurrentTree() != null) {
            final IExplorerTreeNode node = getTreeManager().getCurrentTree().findNodeByExplorerItemId(explorerItemId);
            if (node != null) {
                getTreeManager().getCurrentTree().setCurrent(node);
            }
        }
        autoTester = new TesterWindow(getEnvironmentImpl());
        autoTester.testFinished.connect(autoTester, "reject()");
        autoTester.testFinished.connect(this, "onTestFinished()");
        autoTester.startTestring(testOptionsFilePath);
        autoTester.setModal(true);
        autoTester.show();
    }

    @SuppressWarnings("unused")
    private void onTestFinished() {        
        final String reportPath;
        if (RunParams.getJUnitReportFile() == null) {
            reportPath = new File(getWorkPath(), "explorerTesting.xml").getAbsolutePath();
        } else {
            reportPath = RunParams.getJUnitReportFile();
        }
        final File report = new File(reportPath);
        if (report.exists() && !report.delete()) {
            final String title = Application.translate("ExplorerError", "Input/Output exception");
            final String message = Application.translate("ExplorerError", "Can't save testing report '%s' ");
            Application.messageError(title, String.format(message, reportPath));
            return;
        }
        final XmlObject xml = autoTester.getJUnitTestReport();
        try {
            final XmlOptions options = new XmlOptions();
            options.setSavePrettyPrint();
            xml.save(report, options);
            forcedDisconnect(true, new Runnable() {
                @Override
                public void run() {
                    exit(true);
                }
            });
        } catch (IOException exception) {
            final String title = Application.translate("ExplorerError", "Input/Output exception");
            final String message = Application.translate("ExplorerError", "Can't save testing report '%s' ");
            Application.messageException(title, String.format(message, reportPath), exception);
        }
    }

    private static class Mp implements MessageProvider {

        private final HashMap<String,String> localization = new HashMap<>(1024);
        
        @Override
        public String translate(String key, String message) {
            final String cacheKey = key+"/"+message;
            String msg = localization.get(cacheKey);
            if (msg==null){
                msg = com.trolltech.qt.core.QCoreApplication.translate(key, message);
                localization.put(cacheKey, msg);
            }
            return msg;
        }
    }
    
    private static class DummyMessageProvider implements MessageProvider{

        @Override
        public String translate(final String key, final String message) {
            return message;
        }
        
    }
    
    private MessageProvider mp = new DummyMessageProvider();

    @Override
    public MessageProvider getMessageProvider() {
        return mp;
    }

    public static Application getInstance() {
        return instance;
    }
    
    public static Application newInstance(final MainWindow mainWindow){
        instance = new Application(mainWindow);
        instance.setupUi();
        return instance;
    }
    
    private ImageManager imageManager = null;
    private final Object lock = new Object();

    @Override
    public org.radixware.kernel.common.client.env.ImageManager getImageManager() {
        synchronized (lock) {
            if (imageManager == null) {
                imageManager = new ImageManager(this);
            }
            return imageManager;
        }
    }

    @Override
    public final ExplorerTracer getTracer() {
        return getEnvironmentImpl().getTracer();
    }

    @Override
    public DefManager getDefManager() {
        return defManager;
    }

    @Override
    public boolean isReleaseRepositoryAccessible() {
        return RadixLoader.getInstance() != null
                && getDefManager() != null
                && getDefManager().getAdsVersion().getNumber() > -1                
                && getDefManager().getClassLoader() != null;
    }

    // this methods must be deleted in future release
    public static String translate(String key, String value) {
        return getInstance().getMessageProvider().translate(key, value);
    }

    @Deprecated
    public static ExplorerSettings getConfigStore() {
        return (ExplorerSettings) getInstance().getEnvironmentImpl().getConfigStore();
    }

    @Deprecated
    public static EIsoLanguage getLanguage() {
        return getInstance().getEnvironmentImpl().getLanguage();
    }

    public static ExplorerTreeManager getTreeManager() {
        return (ExplorerTreeManager) getInstance().getEnvironmentImpl().getTreeManager();
    }

    public static List<EIsoLanguage> getSupportedLanguages() {
        return getInstance().getEnvironmentImpl().getSupportedLanguages();
    }

    public static ConnectionOptions getConnectionOptions() {
        return getInstance().getEnvironmentImpl().connection;
    }

    @Override
    public DialogFactory getDialogFactory() {
        return new ExplorerDialogFactory();
    }

    @Override
    public ExplorerStandardViewsFactory getStandardViewsFactory() {
        return viewsFactory;
    }

    @Override
    public ExplorerFontManager getFontManager() {
        return ExplorerFontManager.getInstance();
    }

    @Override
    public TextOptionsManager getTextOptionsManager() {
        return TextOptionsManager.getInstance();
    }     
    
    private final EnvironmentCache environmentCache = new EnvironmentCache();

    @Override
    public EnvironmentCache getEnvironmentCache() {
        return environmentCache;
    }

    @Override
    public WidgetFactory getWidgetFactory() {
        return new ExplorerWidgetFactory();
    }

    @Override
    public IEditMaskEditorFactory getEditMaskEditorFactory() {
        return new EditMaskEditorFactory();
    }

    @Override
    public ITaskWaiter newTaskWaiter() {
        return new TaskWaiter(getEnvironmentImpl());
    }

    @Override
    public EIsoLanguage getClientLanguage() {
        return getEnvironmentImpl().getLanguage();
    }

    @Override
    public IRadixTrace getTrace() {
        return getTracer();
    }
    
    public void forcedDisconnect(final boolean onExit, final Runnable onDisconnect){
        if (!disconnecting){
            disconnecting = true;
            eventsDispatcher.block();
            if (inputFilter==null){
                inputFilter = new UserInputFilter(this,true,false);
                QApplication.instance().installEventFilter(inputFilter);
            }
            QApplication.postEvent(this, new ForcedDisconnectEvent(onExit, onDisconnect));
        }
    }
    
    private boolean tryToForcedDisconnect(final boolean onExit, final Runnable onDisconnect){
        final QWidget modalWidget = findModalWidget();
        if (modalWidget==null){
            disconnectImpl(true, onExit);
            return true;
        }else{
            closeModalWidget(modalWidget);
            QApplication.postEvent(this, new ForcedDisconnectEvent(onExit, onDisconnect));
            return false;
        }        
    }
    
    private QWidget findModalWidget(){
        if (mainWindow!=null){
            final QWidget activeWidget = QApplication.activeModalWidget();
            if (activeWidget !=null && activeWidget!=mainWindow){
                return activeWidget;
            }
        }
        return null;
    }
    
    private boolean closeModalWidget(final QWidget activeWidget){
        if (activeWidget instanceof ExplorerDialog) {
            ((ExplorerDialog) activeWidget).forceClose();
            return true;
        } else {
            return activeWidget.close();
        }        
    } 
    
    public boolean disconnect(final boolean onExit){
        return disconnectImpl(false, onExit);
    }
    
    public Actions getActions(){
        return actions;
    }
    
    private boolean disconnectImpl(final boolean forced, final boolean onExit){
        final boolean disconnected = getEnvironmentImpl().disconnect(forced,onExit);
        if (disconnected && !onExit) {
            actions.connect.setEnabled(true);
            actions.disconnect.setEnabled(false);
            actions.changePassword.setEnabled(false);
        }
        if (disconnected){
            eventsDispatcher.reset();
        }
        return disconnected;
    }

    protected static boolean isContextlessCommandAccessible(Id id) {
        return getInstance().getEnvironmentImpl().isContextlessCommandAccessible(id);

    }

    protected static boolean isServerResourceAccessible(EDrcServerResource resource) {
        return getInstance().getEnvironmentImpl().isServerResourceAccessible(resource);
    }

    protected static QIcon loadIcon(String fileName) {
        return (QIcon) getInstance().imageManager.loadIcon(fileName);
    }

    protected static QIcon loadSvgIcon(String fileName, QColor bColor) {        
        return (QIcon) getInstance().imageManager.loadSvgIcon(fileName, bColor);
    }
        
    private static class SecretStore implements ISecretStore{

        private QByteArray byteArray;
        
        @Override
        @SuppressWarnings("PMD.MethodReturnsInternalArray")
        public byte[] getSecret() {
            return byteArray == null ? null : byteArray.toByteArray();
        }

        @Override
        @SuppressWarnings("PMD.ArrayIsStoredDirectly")
        public void setSecret(byte[] secret) {
            clearSecret();
            byteArray = new QByteArray(secret);
        }

        @Override
        public void clearSecret() {
            if (byteArray != null) {
                byteArray.fill((byte) 0);
                byteArray.clear();
                byteArray = null;
            }
        }
    }

    @Override
    public ISecretStore newSecretStore() {
        return new SecretStore();
    }

    @Override
    public boolean isInGuiThread() {
        return thread()==Thread.currentThread();
    }

    @Override
    public boolean isExtendedMetaInformationAccessible() {
        return RunParams.isExtendedMetaInformationAccessible();
    }        
        
    public <T> T runInGuiThread(final Callable<T> task) throws InterruptedException, ExecutionException {
        return this.qtExecutor.invoke(task);
    }

    public void runInGuiThreadAsync(final Runnable task) {
        QApplication.postEvent(this, new ExecuteEvent(task));
    }    
            
    public static long processEventWhenEasSessionReady(final QObject receiver, final QEvent event){
        return instance==null ? 0 : instance.eventsDispatcher.scheduleEvent(receiver, event);
    }
    
    public static boolean removeScheduledEvent(final long eventId){        
        return instance==null ? true : instance.eventsDispatcher.dropEvent(eventId);
    }
    
    public static boolean isInMainThread(){
        return instance.isInGuiThread();
    }        
    
    public static void sleep(final int millis){
        QTimer.singleShot(millis, instance.pauseLoop, "quit()");
        instance.pauseLoop.exec();
    }
}