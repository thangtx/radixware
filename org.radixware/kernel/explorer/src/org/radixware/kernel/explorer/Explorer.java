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
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QMessageBox;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.AppError;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.inspector.InspectorImplementation;
import org.radixware.kernel.explorer.inspector.InspectorService;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.MainWindow;
import org.radixware.kernel.explorer.widgets.PropLabelsPool;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.kernel.starter.utils.SystemTools;

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
        
        public void afterExit(){
            application.getImageManager().clearCache(false);
        }
    }
    
    //This classes used in QtJambi shutdown hook when radix class loader already closed
    private static final String[] PRELOADING_CLASSES = {"com.trolltech.qt.gui.QPen",
        "com.trolltech.qt.gui.QColor",
        "com.trolltech.qt.gui.QBrush",
        "com.trolltech.qt.internal.fileengine.JarCache"
    };

    @SuppressWarnings("CallToThreadDumpStack")
    private static void preloadQtClasses() {
        final ClassLoader cl = Explorer.class.getClassLoader();
        try {
            for (String className : PRELOADING_CLASSES) {
                cl.loadClass(className);
            }
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }
    
    public static interface IQuitListener{
        boolean beforeQuit();
    }
    
    private final static class RestartEvent extends QEvent{
        
        private final boolean restoreConnection;
        private final String connectionName;
        private final String userName;
        
        public RestartEvent(final boolean restoreConnection, final String connectionName, final String userName){
            super(QEvent.Type.User);
            this.restoreConnection = restoreConnection;
            this.connectionName = connectionName;
            this.userName = userName;
        }

        public boolean needToRestoreConnection() {
            return restoreConnection;
        }

        public String getConnectionName() {
            return connectionName;
        }

        public String getUserName() {
            return userName;
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
                restart(restartEvent.needToRestoreConnection(),restartEvent.getConnectionName(),restartEvent.getUserName());
            }else{
                super.customEvent(event);
            }
        }
        
        private void restart(final boolean restoreConnection, final String connectionName, final String userName){
            Application.getInstance().forcedDisconnect(false, new Runnable(){
                @Override
                public void run() {
                    PropLabelsPool.getInstance().clear();
                    if (restoreConnection && connectionName != null) {
                        RunParams.setConnectionParams(connectionName, userName);//clear restore connection
                        RunParams.addRestartParams();//set restore connecton
                    }
                    Starter.mustRestart(RunParams.getArgs());
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
        final String appName = Application.getProductName()+" Explorer";
        QApplication.initialize(appFactory, appName, args);
        System.setProperty(com.trolltech.qt.QtJambi_LibraryInitializer.TERMINATE_APPLICATION_TIMEOUT_MILLS_PROPERTY_NAME,"5000");

        if (SystemTools.isOSX){
            QApplication.setAttribute(Qt.ApplicationAttribute.AA_UseHighDpiPixmaps);
        }
        
        preloadQtClasses();

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
            if (!Starter.getVersion().equals(clVer)) {//RADIX-6548
                final String message = String.format("Local starter version (%s) doesn't correspond to repository starter version (%s).\nApplication may work unstable!", clVer, Starter.VERSION);
                //Application is not inited here so we must use QMessageBox instead of ExplorerMessageBox
                QMessageBox.warning(null, "Starter is Out of Sate", message);
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
        if (Starter.getRestartParameters()!=null){
            com.trolltech.qt.internal.QtJambiRuntime.setClassLoader(null);
            Thread.currentThread().setContextClassLoader(Explorer.class.getClassLoader());
            final int classLoaderId = Explorer.class.getClassLoader().hashCode();
            final int cachedClasses = 
                com.trolltech.qt.internal.MetaObjectTools.safelyClearCachedMetaData(classLoaderId);
            if (cachedClasses>0){
                String cachedClassName;
                int tryCount=0;
                do{
                    tryCount++;
                    System.gc();//NOPMD
                    try{
                        Thread.sleep(50);
                    }catch(InterruptedException exception){//NOPMD
                    }
                    cachedClassName = 
                        com.trolltech.qt.internal.MetaObjectTools.findClassNameByClassLoader(classLoaderId);
                }while(cachedClassName!=null && !cachedClassName.isEmpty() && tryCount<=10);
                if (cachedClassName!=null && !cachedClassName.isEmpty()){
                    System.err.println("An instance of \'"+cachedClassName+"\' class was not disposed");
                    System.err.flush();
                    com.trolltech.qt.internal.MetaObjectTools.forcedlyClearCachedMetaData(classLoaderId);
                }else{
                    finalQtCleanup();
                }
            }else{
                finalQtCleanup();
            }
        }
    }
    
    @SuppressWarnings("SleepWhileInLoop")
    private static void finalQtCleanup(){
        ShutdownHookRegistrator.beforeRestart();
        for (int i=3; i>0; i--){
            System.gc();//NOPMD
            try{
                Thread.sleep(1000);
            }catch(InterruptedException exception){//NOPMD
            }
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

    public static void restart() {
        final String stationName = Application.getInstance().getEnvironment().getStationName();        
        final boolean isConnected = stationName != null && !stationName.isEmpty();
        final boolean isCurrentVersionSupported = Application.getInstance().getDefManager().getAdsVersion().isSupported();
        final String connectionName = RunParams.getConnectionName();
        final String userName = RunParams.getUserName();
        RunParams.addRestartParams();//to store current explorer item in ExplorerTreeManager.closeAll
        final boolean restoreConnection =
                Application.getTreeManager() != null
                && Application.getTreeManager().closeAll(!isCurrentVersionSupported)
                && isConnected;
        if (!restoreConnection) {
            RunParams.clearRestartParams();
        }
        
        QApplication.postEvent(QApplication.instance(), new RestartEvent(restoreConnection, connectionName, userName));
    }
}