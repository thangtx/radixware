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

package org.radixware.kernel.explorer;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.core.QTranslator;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QMessageBox;
import com.trolltech.qt.gui.QPixmap;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;

import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.AppError;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.inspector.InspectorImplementation;
import org.radixware.kernel.explorer.inspector.InspectorService;
import org.radixware.kernel.explorer.tree.ExplorerTreeManager;
import org.radixware.kernel.explorer.utils.TranslationsFile;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.MainWindow;
import org.radixware.kernel.explorer.widgets.PropLabelsPool;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.kernel.starter.radixloader.RadixSVNLoader;
import org.radixware.kernel.starter.utils.SystemTools;
import org.radixware.schemas.clientstate.ClientState;
import org.radixware.schemas.clientstate.ClientStateDocument;

public class Explorer {        

    public static class QtJambiNativeLibraryLoader {

        private final static String LAYER_URI = "org.radixware";

        public static void loadNativeLibrary(final String library) {
            final String libPath = System.getProperty("org.radixware.kernel.explorer.local-library-path." + library);
            if (libPath != null && !libPath.isEmpty() && Files.isReadable(Paths.get(libPath))) {
                Runtime.getRuntime().load(libPath);
                return;
            }
            if (isRadixClassLoader()) {
                loadLibraryByRadixClassLoader(library);
            } else if (!loadNativeLibraryInBranch(library)) {
                throw new AppError("Unable to load Qt library \'" + library + "\' from radix project branch");
            }
        }

        private static boolean isRadixClassLoader() {
            return (Explorer.class.getClassLoader() instanceof RadixClassLoader);
        }

        private static void loadLibraryByRadixClassLoader(final String lib) {
            try {
                ((RadixClassLoader) Explorer.class.getClassLoader()).loadNativeLibrary(ERuntimeEnvironmentType.EXPLORER.getValue(), lib);
            } catch (RadixLoaderException ex) {
                throw new AppError("Unable to load Qt library \'" + lib + "\' by means of RadixClassLoader: " + String.valueOf(ex.getMessage()), ex);
            }
        }

        private static boolean loadNativeLibraryInBranch(final String lib) {
            ClassLoader loader = Explorer.class.getClassLoader();
            final String classFile = Explorer.class.getName().replace(".", "/") + ".class";
            final URL url = loader.getResource(classFile);
            if (url != null) {
                final String urlAsStr;
                try {
                    urlAsStr = URLDecoder.decode(url.getPath(), "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    throw new AppError("Unable to find Qt library \'" + lib + "\' in radix project branch: " + String.valueOf(ex.getMessage()), ex);
                }
                final int endIndex = urlAsStr.lastIndexOf(LAYER_URI);
                if (endIndex > 0) {
                    final String branchPath;
                    if (url.getProtocol().equals("jar")) {
                        branchPath = urlAsStr.substring(5, endIndex + LAYER_URI.length());
                    } else {
                        branchPath = urlAsStr.substring(0, endIndex + LAYER_URI.length());
                    }
                    final String libraryPath = branchPath + "/" + SystemTools.getExplorerNativeLibLayerPath(lib);
                    final File libraryFile = new File(libraryPath);
                    if (libraryFile.exists()) {
                        Runtime.getRuntime().load(libraryFile.getAbsolutePath());
                        return true;
                    }
                }
            }
            return false;
        }
    }    
    
    public static class ShutdownHookRegistrator{
        
        private static Runnable shutdownHook;
        
        public static void registerShutdownHook(final Runnable hook){            
            Starter.addAppShutdownHook(hook);
            shutdownHook = hook;
        }
        
        static void beforeRestart(){
            if (shutdownHook!=null){
                Starter.removeAppShutdownHook(shutdownHook);
                shutdownHook.run();
                shutdownHook = null;
            }
        }
        
    }

    private static class ExplorerMainWindow extends MainWindow {
        
        private static class StartAppEvent extends QEvent{        
            public StartAppEvent(){
                super(QEvent.Type.User);
            }
        }                

        private final Application application = Application.newInstance(this);        
        private int startTimer;

        @SuppressWarnings("LeakingThisInConstructor")
        public ExplorerMainWindow() {
            setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
            if (WidgetUtils.isGnome3Classic()){
                startTimer = startTimer(50);//some time before window takes valid geometry
            }else{
                QApplication.postEvent(this,new StartAppEvent());
            }
        }                   

        @Override
        protected void closeEvent(final QCloseEvent event) {            
            if (isForcedlyClosing() || Application.getInstance().exit(false)){
                super.closeEvent(event);
            }else{
                event.ignore();
            }
        }

        @Override
        protected void customEvent(final QEvent event) {
            if (event instanceof StartAppEvent){
                event.accept();
                application.getActions().connect.trigger();
            }else{
                super.customEvent(event);
            }
        }

        @Override
        protected void timerEvent(QTimerEvent timerEvent) {
            if (timerEvent.timerId()==startTimer){
                timerEvent.accept();
                killTimer(startTimer);
                startTimer = 0;
                application.getActions().connect.trigger();
                return;
            }
            super.timerEvent(timerEvent);
        }

        @Override
        protected void keyPressEvent(final QKeyEvent event) {
            super.keyPressEvent(event);
        }                
        
        public void afterExit(){
            application.getImageManager().clearCache(false);
        }
    }
    
    private static boolean restarting;
        
    private static final String[] PRELOADING_QT_CLASSES = {
        "com.trolltech.qt.gui.QApplication",
        "com.trolltech.qt.gui.QMessageBox",
        //This classes used in QtJambi shutdown hook when radix class loader already closed
        "com.trolltech.qt.gui.QPen",
        "com.trolltech.qt.gui.QColor",
        "com.trolltech.qt.gui.QBrush",
        "com.trolltech.qt.internal.fileengine.JarCache"
    };
    
    private static final String[] PRELOADING_CLIENT_CLASSES = {
        "org.radixware.kernel.common.client.IClientApplication",
        //This classes used in QtJambi shutdown hook when radix class loader already closed
        "org.radixware.kernel.common.client.exceptions.ExceptionMessage"
    };
        
    private static void preloadQtClasses() throws ClassNotFoundException {
        final ClassLoader cl = Explorer.class.getClassLoader();
        for (String className : PRELOADING_QT_CLASSES) {
            cl.loadClass(className);
        }        
    }
    
    private static void preloadClientClasses() throws ClassNotFoundException {
        final ClassLoader cl = Explorer.class.getClassLoader();
        for (String className : PRELOADING_CLIENT_CLASSES) {
            cl.loadClass(className);
        }        
    }
    
    
    public static interface IQuitListener{
        boolean beforeQuit();
    }
    
    private final static class RestartEvent extends QEvent{
        
        private final org.radixware.schemas.clientstate.ClientStateDocument appState;
        private final long version;
        
        public RestartEvent(final org.radixware.schemas.clientstate.ClientStateDocument state,
                                      final long version){
            super(QEvent.Type.User);
            appState = state;
            this.version = version;
        }

        public org.radixware.schemas.clientstate.ClientStateDocument getAppState() {
            return appState;
        }                

        public long getVersion() {
            return version;
        }                
    }    
    
    private final static class ExplorerApplication extends QApplication{
                
        private final List<IQuitListener> listeners = new LinkedList<>();
        
        private static final class TerminateEvent extends QEvent{            
            public TerminateEvent(){
                super(QEvent.Type.User);
            }            
        }
        
        public ExplorerApplication(final String[] args){
            super(args);
        }
        
        public ExplorerApplication(final String appName, final String[] args){
            super(appName,args);
        }

        @Override
        public boolean event(final QEvent event) {
            if (event!=null && event.type()==QEvent.Type.Quit){
                terminate();
                event.accept();
                return true;
            }else{
                return super.event(event);
            }
        }

        @Override
        protected void customEvent(final QEvent event) {
            if (event instanceof TerminateEvent){
                event.accept();
                terminate();
            }else if (event instanceof RestartEvent){
                event.accept();
                final RestartEvent restartEvent = (RestartEvent)event;
                restart(restartEvent.getAppState(), restartEvent.getVersion());
            }else{
                super.customEvent(event);
            }
        }
        
        private void restart(final org.radixware.schemas.clientstate.ClientStateDocument appState, final long version){
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();            
            try{
                appState.save(byteStream);
            }catch(IOException exception){
                final MessageProvider mp = Application.getInstance().getEnvironment().getMessageProvider();
                final String message = mp.translate("TraceMessage","Failed to save application state");
                Application.getInstance().getEnvironment().getTracer().error(message, exception);
                byteStream = null;
            }
            final byte[] appStateAsBytes = byteStream==null ? new byte[]{} : byteStream.toByteArray();
            Application.getInstance().forcedDisconnect(true, new Runnable(){
                @Override
                public void run() {
                    PropLabelsPool.getInstance().clear();                    
                    Starter.mustRestartByNewProcess(appStateAsBytes, version);
                    Application.getInstance().exit(true);
                }
            });
        }
                        
        private void terminate(){
            if (notifyListeners()){
                quit();                
            }else{
                QApplication.postEvent(this, new TerminateEvent());
            }            
        }
        
        public void addQuitListener(final IQuitListener listener){
            listeners.add(listener);
        }
        
        public void removeQuitListener(final IQuitListener listener){
            listeners.remove(listener);
        }
        
        private boolean notifyListeners(){
            final List<IQuitListener> copy = new LinkedList<>(listeners);
            for (IQuitListener listener: copy){
                if (!listener.beforeQuit()){
                    return false;
                }
                listeners.remove(listener);
            }
            return true;
        }
    }
    
    public static void addQuitListener(final IQuitListener listener){
        final QApplication app = QApplication.instance();
        if (app instanceof ExplorerApplication){
            ((ExplorerApplication)app).addQuitListener(listener);
        }
    }
    
    public static void removeQuitListener(final IQuitListener listener){
        final QApplication app = QApplication.instance();
        if (app instanceof ExplorerApplication){
            ((ExplorerApplication)app).removeQuitListener(listener);
        }
    }
    
    /**
     * @param args
     */
    @SuppressWarnings("SleepWhileInLoop")
    public static void main(final String[] args) {
        final String K_com_trolltech_qt_debug = "com.trolltech.qt.debug";
        if (System.getProperty(K_com_trolltech_qt_debug) == null) {
            System.setProperty(K_com_trolltech_qt_debug, "Release");
        }
        System.setProperty("com.trolltech.qt.native-library-loader-override", "org.radixware.kernel.explorer.Explorer$QtJambiNativeLibraryLoader");
        if (isRadixClassLoader()){
            System.setProperty("com.trolltech.qt.shutdown-hook-registrator-override", "org.radixware.kernel.explorer.Explorer$ShutdownHookRegistrator");
        }
        
        try {
            preloadQtClasses();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.err.println("Failed to start application. Unable to load QtJambi library.");
            return;
        }
        
        final QApplication.Factory appFactory = new QApplication.Factory(){

            @Override
            public QApplication newInstance(final String applicationName, final String[] args) {
                if (applicationName==null){
                    return new ExplorerApplication(args);
                }else{
                    return new ExplorerApplication(applicationName,args);
                }
            } 
        };
        
        final String appName = getProductName()+" Explorer";        
        QApplication.initialize(appFactory, appName, args);
        System.setProperty(com.trolltech.qt.QtJambi_LibraryInitializer.TERMINATE_APPLICATION_TIMEOUT_MILLS_PROPERTY_NAME,"5000");

        if (SystemTools.isOSX){
            QApplication.setAttribute(Qt.ApplicationAttribute.AA_UseHighDpiPixmaps);
        }
        
        try {
            preloadClientClasses();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.err.println("Failed to load application");
            if (ex.getCause() instanceof RadixLoaderException
                && ex.getCause().getMessage()!=null
                && !ex.getCause().getMessage().isEmpty()){
                QMessageBox.critical(null, "Failed to Load Application", ex.getCause().getMessage());
            }else{
                QMessageBox.critical(null, "Error", "Failed to load application");
            }
            return;
        }        

        try {
            RunParams.initialize(args);
        } catch (Exception e) {
            QMessageBox.critical(null, "Invalid parameters", e.getMessage());
            return;
        }
 
        if (isRadixClassLoader()){
            String clVer;
            try {
                Class cl = getStarterClassLoader().loadClass(Starter.class.getName());
                try {
                    clVer = (String) cl.getField("VERSION").get(null);
                } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException exception) {
                    clVer = null;
                }
            } catch (ClassNotFoundException e) {
                clVer = null;
            }
            if (!Starter.getVersion().equals(clVer)) {
                final TranslationsFile translationsFile = TranslationsFile.getDefaultAppTranslations();
                final String title;
                final String message;
                final String updateStarterButtonText;
                final String closeAppButtonText;
                final String runAppButtonText;
                if (translationsFile.getLanguage()==EIsoLanguage.ENGLISH){
                    title = "Starter Update Needed";
                    message = "The version of the local starter does not correspond to the version available in the repository.\nThe application may work unstable!";
                    updateStarterButtonText = "Update Starter";
                    closeAppButtonText = "Close Application";
                    runAppButtonText =  "Launch Application";
                }else{
                    final QTranslator translator = translationsFile.loadTranslator(null);
                    if (translator==null){
                        title = "Starter Update Needed";
                        message = "The version of the local starter does not correspond to the version available in the repository.\nThe application may work unstable!";
                        updateStarterButtonText = "Update Starter";
                        closeAppButtonText = "Close Application";
                        runAppButtonText =  "Launch Application";
                    }else{
                        title = translator.translate("ExplorerMessage", "Starter Update Needed");
                        message = translator.translate("ExplorerMessage", "The version of the local starter does not correspond to the version available in the repository.\nThe application may work unstable!");
                        updateStarterButtonText = translator.translate("ExplorerMessage", "Update Starter");
                        closeAppButtonText = translator.translate("ExplorerMessage", "Close Application");
                        runAppButtonText =  translator.translate("ExplorerMessage", "Launch Application");
                    }
                }
                //Application is not inited here so we must use QMessageBox instead of ExplorerMessageBox
                if (RadixLoader.getInstance() instanceof RadixSVNLoader){                    
                    final QMessageBox.StandardButtons buttons = 
                        new QMessageBox.StandardButtons(QMessageBox.StandardButton.Ignore, 
                                                                              QMessageBox.StandardButton.Close, 
                                                                              QMessageBox.StandardButton.Retry);                    
                    final QMessageBox messageBox = 
                        new QMessageBox(QMessageBox.Icon.Question, title, message, buttons);
                    final QAbstractButton retryBtn = messageBox.button(QMessageBox.StandardButton.Retry);
                    retryBtn.setText(updateStarterButtonText);
                    final QIcon retryIcon = 
                        new QIcon(new QPixmap(":/trolltech/styles/commonstyle/images/refresh-24.png"));
                    retryBtn.setIcon(retryIcon);
                    final QAbstractButton ignoreBtn = messageBox.button(QMessageBox.StandardButton.Ignore);
                    ignoreBtn.setText(runAppButtonText);
                    ignoreBtn.setIcon(QIcon.fromTheme("dialog-warning"));                    
                    messageBox.button(QMessageBox.StandardButton.Close).setText(closeAppButtonText);
                    messageBox.exec();
                    final QAbstractButton clickedButton = messageBox.clickedButton();
                    if (clickedButton==messageBox.button(QMessageBox.StandardButton.Close)){
                        return;
                    }
                    if (clickedButton==messageBox.button(QMessageBox.StandardButton.Retry)){
                        Starter.mustRestartByNewProcess(null, -1);
                        return;
                    }
                }else{
                    QMessageBox.warning(null, title, message);
                }
            }
        }

        final InspectorService inspector;
        if (isRadixClassLoader() && RunParams.getInspectorAgentAddress() != null){
            inspector = new InspectorService(new InspectorImplementation());
        }else{
            inspector = null;
        }
        
        try {
            if (inspector != null) {
                ((RadixClassLoader) Explorer.class.getClassLoader()).getGroupTypes().remove("KernelServer");//Hack to fix problem with searching resources in server jars during jax-ws initialization
                inspector.start();
            }

            final ExplorerMainWindow mainWindow = new ExplorerMainWindow();            
            QApplication.execStatic();
            mainWindow.afterExit();
        } finally {
            if (inspector != null) {
                inspector.stop();
            }
        }
    }   
    
    public static String getProductName() {
        if (Starter.getProductName() != null && !Starter.getProductName().isEmpty()) {
            return Starter.getProductName();
        }
        if (RadixLoader.getInstance() == null) {
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
    
    private static boolean isRadixClassLoader() {
        return (Explorer.class.getClassLoader() instanceof RadixClassLoader);
    }    

    private static ClassLoader getStarterClassLoader() {
        ClassLoader startersClassLoader = Starter.class.getClassLoader();
        while (RadixClassLoader.class.getName().equals(startersClassLoader.getClass().getName())) {
            startersClassLoader = startersClassLoader.getClass().getClassLoader();
        }
        return startersClassLoader;
    }

    public static void restart(final boolean forced, final long version) {
        if (restarting){
            return;
        }
        restarting = true;
        final IClientEnvironment environment = Application.getInstance().getEnvironment(); 
        final String stationName = environment.getStationName();        
        final boolean isConnected = stationName != null && !stationName.isEmpty();        
        final ExplorerTreeManager treeManager = isConnected ? Application.getTreeManager() : null;
        if (treeManager==null){
            QApplication.postEvent(QApplication.instance(), new RestartEvent(null, version));
        }else{
            final ClientStateDocument document = ClientStateDocument.Factory.newInstance();            
            final ClientState clientState = document.addNewClientState();
            treeManager.writeStateToXml(clientState);
            environment.writeConnectionParametersToXml(clientState.addNewConnection());
            boolean treeManagerClosed = false;
            try{
                treeManagerClosed = treeManager.closeAll(forced);
            }finally{
                if (treeManagerClosed || forced){
                    QApplication.postEvent(QApplication.instance(), new RestartEvent(document, version));
                }else{
                    restarting  = false;
                }
            }            
        }
    }
}