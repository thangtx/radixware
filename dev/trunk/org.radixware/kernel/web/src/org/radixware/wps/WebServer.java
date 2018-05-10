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

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.SimpleLog;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.traceprofile.TraceProfileTreeController;
import org.radixware.kernel.common.client.models.groupsettings.Filters;
import org.radixware.kernel.common.client.models.groupsettings.Sortings;
import org.radixware.kernel.common.client.tree.UserExplorerItemsStorage;
import org.radixware.kernel.common.client.utils.IContextEnvironment;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.kerberos.KerberosCredentials;
import org.radixware.kernel.common.kerberos.KerberosUtils;
import org.radixware.kernel.common.meta.ILanguageContext;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.log.DelegateLogFactory;
import org.radixware.kernel.starter.log.StarterLog;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.kernel.starter.radixloader.RadixSVNLoader;
import org.radixware.kernel.starter.radixloader.ReplacementEntry;
import org.radixware.kernel.starter.radixloader.ReplacementFile;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.icons.images.WsIcons;
import org.radixware.wps.rwt.Banner;

public class WebServer {

    private static final String HTTP_SESSION_CONTEXT_ATTR_NAME = "rdxSessionContext";
    private static final String PARAM_COOKIE_NAME = "paramsId";
    private static final WebServer instance = new WebServer();
    private final Object krbSem = new Object();
    private volatile KerberosCredentials serviceCreds;
    
    public static interface IUploadListener {

        void uploadingComplete();
    }

    public static final class UploadSystemHandler {

        private UploadSystemHandler() {
        }
        private final Object semaphore = new Object();
        private IUploadListener listener;
        private boolean disposed;
        private CountDownLatch latch;
        private File file;
        private Exception exception;

        public void upload(final File file) {
            synchronized (semaphore) {
                if (disposed) {
                    instance.disposeUpload(null, file.getName());
                } else {
                    this.file = file;
                    if (listener != null) {
                        listener.uploadingComplete();
                    }
                    if (latch != null) {
                        latch.countDown();
                    }
                }
            }
        }

        public void fail(Exception e) {
            synchronized (semaphore) {
                exception = e;
                if (listener != null) {
                    listener.uploadingComplete();
                }
                if (latch != null) {
                    latch.countDown();
                }
            }
        }

        public void setListener(final IUploadListener listener) {
            if (listener != null) {
                synchronized (semaphore) {
                    if (disposed) {
                        throw new IllegalStateException("Uploading was disposed");
                    }
                    if (file != null || exception != null) {
                        listener.uploadingComplete();
                    } else {
                        this.listener = listener;
                    }

                }
            }
        }

        public boolean waitForReady(final long timeoutSec) throws InterruptedException {
            synchronized (semaphore) {
                if (file != null || exception != null || disposed) {
                    return true;
                }
                latch = new CountDownLatch(1);
            }
            return latch.await(timeoutSec, TimeUnit.SECONDS);
        }

        public File getFile() throws Exception {
            synchronized (semaphore) {
                if (disposed) {
                    throw new IllegalStateException("Uploading was disposed");
                }
                if (exception != null) {
                    throw exception;
                }
                return file;
            }
        }

        public boolean isReady() {
            synchronized (semaphore) {
                return disposed || file != null || exception != null;
            }
        }

        public void dispose() {
            synchronized (semaphore) {
                if (file != null) {
                    instance.disposeUpload(null, file.getName());
                    file = null;
                }
                listener = null;
                disposed = true;
                if (latch != null) {
                    latch.countDown();
                }
            }
        }

        public boolean isDisposed() {
            synchronized (semaphore) {
                return disposed;
            }
        }
    }

    static class WsThread extends Thread implements IContextEnvironment, ILanguageContext {

        volatile IClientEnvironment userSession;

        public WsThread(Runnable target) {

            super(target);
            setDaemon(true);
        }

        @Override
        public IClientEnvironment getClientEnvironment() {
            return userSession;
        }

        @Override
        public EIsoLanguage getLanguage() {
            return userSession == null ? EIsoLanguage.ENGLISH : userSession.getLanguage();
        }
    }
    final ExecutorService threadPool = Executors.newCachedThreadPool(new ThreadFactory() {
        private int counter = 1;

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new WsThread(r);

            t.setName("RadixWare WPS Event handler thread#" + (counter++));
            return t;
        }
    });
    private static final Object shutdownLock = new Object();

    static KeystoreController getKeystoreController() {
        try {
            return KeystoreController.getServerKeystoreController();
        } catch (KeystoreControllerException ex) {
            return null;
        }
    }
    public static final String VERSION = "2.1.13";

    public static void main(String[] args) throws Exception {        
        final WebServerRunParams runParams;
        if (args.length==0){
            runParams = WebServerRunParams.EMPTY;
        }else{
            runParams = WebServerRunParams.processArgs(args);
            if (runParams==null){
                throw new IllegalArgumentException("Wrong Web Presentation Server arguments");
            }
        }
        final Logger logger = Logger.getLogger(WebServer.class.getName());
        printStarterInformation(logger);
        printWebServerArguments(runParams, logger);
        printProductVersions(logger);
        printSystemInformation(logger);
        {//check 
            final String dbPath = runParams.getSettingsDatabaseDir();
            if (dbPath != null) {
                checkDbPath(dbPath, logger);
            }
        }
        getStarterClassLoader().loadClass("org.radixware.ws.servlet.WPSBridge").getMethod("register", Object.class).invoke(null, instance);
        {// set limit for uploading
            final Integer uploadingLimit = WebServerRunParams.getUploadHardLimitMb();
            getStarterClassLoader().loadClass("org.radixware.ws.servlet.FileUploader").getMethod("setSizeLimit", Integer.class).invoke(null, uploadingLimit);
        }

        StarterLog.setFactory(new DelegateLogFactory() {
            @Override
            public org.apache.commons.logging.Log createApacheLog(String name) {
                return new  SimpleLog("Radix WPS");
            }
        });
        LogFactory.releaseAll();

        synchronized (shutdownLock) {
            try {
                shutdownLock.wait();
                synchronized (imagesDirLock) {
                    if (imagesDir != null) {
                        FileUtils.deleteDirectory(imagesDir);
                    }
                }
            } catch (InterruptedException e) {
            }
        }
    }
    
    private static void printStarterInformation(final Logger logger) {
        final String arguments = Starter.getArguments().asStrWithoutAppParams().replace(" ", "\n\t");
        logger.log(Level.INFO, "Starter arguments: '{'\n\t{0}\n'}'", arguments);
        final StringBuilder starterParams = new StringBuilder();
        final RadixLoader rxLoader = RadixLoader.getInstance();
        starterParams.append("{\n\tRevision number: ");
        try {
            starterParams.append(String.valueOf(rxLoader.getCurrentRevision()));
        } catch (RadixLoaderException exception) {
            starterParams.append("Unknown");
        }
        if (rxLoader instanceof RadixSVNLoader) {
            starterParams.append("\n\tDirectory for cache: ");
            starterParams.append(rxLoader.getRoot());
        }
        starterParams.append("\n\tDirectory for temporary files: ");
        starterParams.append(org.radixware.kernel.starter.utils.SystemTools.getTmpDir());
        if (rxLoader instanceof RadixSVNLoader) {
            starterParams.append("\n\tSubversion URL list: {");
            final List<String> urls = ((RadixSVNLoader) rxLoader).getSvnUrls();
            for (String url : urls) {
                starterParams.append("\n\t\t");
                starterParams.append(url);
            }
            starterParams.append("\n\t}");
        } else {
            starterParams.append("\n\tWorking directory: ");
            starterParams.append(rxLoader.getRepositoryUri());
        }
        starterParams.append("\n}");
        logger.log(Level.INFO, "Starter information: {0}", starterParams.toString());
        final List<ReplacementFile> replacements = rxLoader.getLocalFiles().getAllReplacementsEx();
        if (replacements != null && !replacements.isEmpty()) {
            final StringBuilder fileReplacements = new StringBuilder("{");
            for (ReplacementFile repFile : replacements) {
                for (ReplacementEntry replacement : repFile.getEntries()) {
                    fileReplacements.append("\n\t");
                    fileReplacements.append(replacement.getRemote());
                    fileReplacements.append(" => ");
                    fileReplacements.append(repFile.getHumanReadableLocal(replacement.getRemote()));
                }
            }
            fileReplacements.append("\n}");
            logger.log(Level.INFO, "File replacements: {0}", fileReplacements.toString());
        }
    }
    
    private static void printSystemInformation(final Logger logger) {
        final StringBuilder sysInfoBuilder = new StringBuilder("\nJava version: {0}\n");
        sysInfoBuilder.append("System information: {1} {2} running on {3}; {4}; {5}_{6}\n");
        final String osVersion = System.getProperty("os.version").replace("-", "\u2011");
        String pid;
        try{
            pid = String.valueOf(org.radixware.kernel.starter.utils.SystemTools.getCurrentProcessPid());            
        }catch(IllegalStateException | NumberFormatException exception){
            logger.log(Level.INFO, "Failed to get PID: {0}", exception.toString());
            pid = null;
        }
        if (pid==null){
            logger.log(Level.INFO, sysInfoBuilder.toString(), 
                new Object[]{System.getProperty("java.runtime.version"),System.getProperty("os.name"), 
                osVersion, System.getProperty("os.arch"),System.getProperty("file.encoding"), 
                System.getProperty("user.language"), System.getProperty("user.country")});            
        }else{
            sysInfoBuilder.append("Process identifier: {7}");
            logger.log(Level.INFO, sysInfoBuilder.toString(), 
                new Object[]{System.getProperty("java.runtime.version"),System.getProperty("os.name"), 
                osVersion, System.getProperty("os.arch"),System.getProperty("file.encoding"), 
                System.getProperty("user.language"), System.getProperty("user.country"), pid});
        }
        
    }

    private static void printWebServerArguments(final WebServerRunParams runParams, final Logger logger) {
        logger.log(Level.INFO, "Web Presentation Server arguments: {0}", runParams.print());
        {// init kerberos
            final WebServerRunParams.KrbWpsOptions krbOptions = runParams.getKerberosOptions();
            if (krbOptions != null && krbOptions.isKerberosOptionsEnabled()) {
                final KrbLoginEventsPrinter printer
                        = new KrbLoginEventsPrinter(logger);
                instance.serviceCreds = KerberosUtils.createCredentials(krbOptions, printer);
                if (krbOptions.getRemoteAuthScheme() != ERemoteKerberosAuthScheme.DISABLED
                        && !WebServerRunParams.isSshRequired()) {
                    logger.warning("It is not recommend to use insecure connection and allow transfer of kerberos identification data");
                }
            }
        }
    }

    private static void printProductVersions(final Logger logger) {
        final RadixClassLoader loader = (RadixClassLoader) WebServer.class.getClassLoader();
        List<LayerMeta> layers = loader.getRevisionMeta().getAllLayersSortedFromBottom();
        String releaseNumber;
        final StringBuilder versions = new StringBuilder("{");

        for (LayerMeta layer : layers) {
            releaseNumber = layer.getReleaseNumber();
            versions.append("\n\t");
            versions.append(layer.getUri());
            versions.append(":\t\t\t");
            versions.append(releaseNumber);
        }
        versions.append("\n}");
        logger.log(Level.INFO, "Product versions: {0}", versions.toString());
    }

    private static void checkDbPath(final String dbPath, final Logger logger) {
        final File dbDir = new File(dbPath);
        final String path = dbDir.getAbsolutePath();
        final String messageTemplate = "Failed to use local settings storage: {0}";
        if (!dbDir.exists()) {
            try {
                if (!dbDir.mkdirs()) {
                    final String reason = "failed to create \'" + path + "\'";
                    logger.log(Level.SEVERE, messageTemplate, reason);
                    return;
                }
            } catch (SecurityException ex) {
                final String reason = "failed to create \'" + path + "\'";
                logger.log(Level.SEVERE, messageTemplate, reason);
                return;
            }
        }
        if (!dbDir.isDirectory()) {
            final String reason = "\'" + path + "\' is not a directory";
            logger.log(Level.SEVERE, messageTemplate, reason);
            return;
        }
        if (!dbDir.canRead()) {
            final String reason = "\'" + path + "\' is not accessible";
            logger.log(Level.SEVERE, messageTemplate, reason);
            return;
        }
        if (!dbDir.canWrite()) {
            final String reason = "\'" + path + "\' is not writable";
            logger.log(Level.SEVERE, messageTemplate, reason);
        }
    }

    public KerberosCredentials getKerberosServiceCredentials() {
        synchronized (krbSem) {
            return serviceCreds;
        }
    }    

    public void shutdown() {
        synchronized (shutdownLock) {
            stopped = true;
            shutdownLock.notify();
        }
    }
    
    private boolean stopped = false;    
    private final List<WpsApplication> applicationCache = new LinkedList<>();
    private final Map<String, String> urlParamsByCookie = new ConcurrentHashMap<>();
    private final Random rnd = new SecureRandom();
    private long lastCreatedVersion;
    private long lastActualizedVersion;
    private boolean actualizerThreadStarted = false;    

    private WebServer() {        
    }
    
    private static File getBannerDir(final WebServerRunParams runParams){
        Banner.Options bannerOptions = runParams.getBannerOptions();
        if (bannerOptions == null) {
            return null;
        }
        final String bannerDirPath = bannerOptions.getDirPath();
        if (bannerDirPath==null || bannerDirPath.isEmpty()){
            return null;
        }else{
            final File banner = new File(bannerDirPath);
            return banner.isDirectory() ? banner : null;
        }        
    }

    private static ClassLoader getStarterClassLoader() {
        ClassLoader startersClassLoader = Starter.class.getClassLoader();
        while (RadixClassLoader.class.getName().equals(startersClassLoader.getClass().getName())) {
            startersClassLoader = startersClassLoader.getClass().getClassLoader();
        }
        return startersClassLoader;
    }

    private boolean processRequestImpl(final WpsApplication application, 
                                                           final WebServerRunParams runParams,
                                                           final HttpServletRequest rq, 
                                                           final HttpServletResponse rs) {
        String fullUri = rq.getRequestURI();
        String contextPath = rq.getContextPath();

        if (fullUri.startsWith(contextPath)) {
            fullUri = fullUri.substring(contextPath.length());
        }

        if (fullUri.startsWith("/icons")) {
            if (fullUri.startsWith("/icons/def/")) {
            } else {
                int dim[] = new int[2];
                String iconPath = WpsIcon.getOriginalUrl(fullUri.substring(7), dim);

                WpsIcon icon = WsIcons.findIcon(iconPath);
                if (icon == null) {
                    icon = (WpsIcon) ((WpsImageManager) application.getImageManager()).loadIcon(iconPath);
                }
                if (icon != null) {
                    try {
                        icon.save(dim, rs);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    } catch (Throwable e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    System.out.println("Icon not found");
                }
                return true;
            }
        } else if (fullUri.startsWith("/rwtext/")) {
            if (fullUri.length() > "/rwtext/".length()) {
                try {
                    if (fullUri.startsWith("/rwtext/file/")) {
                        String fileId = fullUri.substring(13);
                        WpsApplication.FileInfo content = application.getFileResource(fileId);
                        if (content != null) {
                            FileInputStream stream = null;
                            try {
                                stream = new FileInputStream(content.file);
                                if (content.save) {
                                    final String fileName = content.desc;
                                    final String encodedFileName = FileNameEncoder.encode(fileName);
                                    final String userAgent = rq.getHeader("user-agent");
                                    final boolean isIE8 = userAgent!=null && userAgent.contains("MSIE 8");                                    
                                    final StringBuilder contentDispositionBuilder = new StringBuilder("attachment; filename=");
                                    
                                    if (isIE8){
                                        contentDispositionBuilder.append(encodedFileName);
                                    }else{
                                        contentDispositionBuilder.append('\"');
                                        contentDispositionBuilder.append(fileName);
                                        contentDispositionBuilder.append("\"; filename*=UTF-8''");
                                        contentDispositionBuilder.append(encodedFileName);
                                    }
                                    rs.addHeader("content-disposition", contentDispositionBuilder.toString());
                                }
                                rs.setContentType(content.mimeType);
                                OutputStream out = rs.getOutputStream();
                                try {
                                    FileUtils.copyStream(stream, out);
                                } finally {
                                    try {
                                        stream.close();
                                    } catch (IOException ex) {
                                    }
                                    try {
                                        out.close();
                                    } catch (IOException e) {
                                    }
                                    if (content.delete) {
                                        FileUtils.deleteFile(content.file);
                                    }
                                }
                            } catch (IOException ex) {
                            }
                        }
                    } else {
                        final String resourceName = fullUri.substring(8);
                        if (resourceName.endsWith(".css")) {
                            rs.setContentType("text/css");
                        } else {
                            rs.setContentType("text/javascript");
                        }
                        final InputStream stream = getClass().getClassLoader().getResourceAsStream(resourceName);
                        if (stream != null){
                            FileUtils.copyStream(stream, rs.getOutputStream());
                        }
                    }
                } catch (IOException e) {
                } catch (Throwable e) {
                }
            }
            return true;
        } else if (fullUri.startsWith("/bannerDir/")) {
            final File bannerDir = getBannerDir(runParams);
            if (bannerDir!=null){
                final String filePath;
                try{
                    filePath = URLDecoder.decode(fullUri.substring(11), "UTF-8");
                }catch(UnsupportedEncodingException ex){
                    return true;
                }
                final File bannerResource = new File(bannerDir, filePath);
                if (bannerResource.isFile() && isInSubDirectory(bannerDir, bannerResource)){
                    if (fullUri.endsWith(".css")){
                        rs.setContentType("text/css");
                    }else if (fullUri.endsWith(".js")){
                        rs.setContentType("text/javascript");
                    }
                    try{
                        final OutputStream out = rs.getOutputStream();
                        final InputStream in = new FileInputStream(bannerResource);
                        try{
                            FileUtils.copyStream(in, out);
                        }catch(IOException ex){
                        }finally{
                            try{
                                in.close();
                            }catch(IOException ex){

                            }
                            try{
                                out.close();
                            }catch(IOException ex){

                            }
                        }
                    }catch(IOException ex){
                    }
                    return true;
                }
            }
            try{
                if (!rs.isCommitted()){
                    rs.sendError(404);
                }
            }catch(IOException ex){
            } 
            return true;
        }
        return false;
    }
        
    private static boolean isInSubDirectory(final File dir, final File file) {
        for (File parent=file.getParentFile(); parent!=null; parent=parent.getParentFile()){
            if (parent.equals(dir)){
                return true;
            }
        }
        return false;
    }
                
    WpsApplication getLatestAppVersion(final boolean ignoreRadixLoaderException, 
                                                           final boolean useLatestExistingApplication, 
                                                           final Collection<Id> changedIds,
                                                           final long targetVersion) throws RadixLoaderException {
        synchronized (applicationCache) {
            if (applicationCache.isEmpty()) {
                long versionNumber = -1;
                try {
                    versionNumber = WpsAdsVersion.actualize(true, RadixLoader.getInstance().getCurrentRevision());
                } catch (RadixLoaderException exception) {
                    if (ignoreRadixLoaderException) {
                        Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, "Unexpected exception on actualizing version", exception);
                    } else {
                        throw exception;
                    }
                }
                final WpsApplication app = new WpsApplication(this, changedIds, versionNumber);
                lastActualizedVersion = lastCreatedVersion = app.getAdsVersionNumber();
                applicationCache.add(app);
                /*if (!actualizerThreadStarted) {
                    actualizerThreadStarted = true;
                    Thread actualizer = new Thread(new Runnable() {
                        @Override
                        @SuppressWarnings("SleepWhileInLoop")
                        public void run() {
                            Thread.currentThread().setContextClassLoader(null);
                            for (;;) {
                                if (WebServer.this.stopped) {
                                    return;
                                }
                                try {
                                    Thread.sleep(60000);
                                } catch (InterruptedException ex) {
                                    return;
                                }
                                try {
                                    actualizeImpl(true);
                                } catch (RadixLoaderException ex) {
                                    Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, "Unexpected exception on actualizing version", ex);
                                }
                            }
                        }
                    });
                    actualizer.setName("RadixWare background version actualizer thread");
                    actualizer.setDaemon(true);
                    actualizer.start();
                }*/
                return app;
            } else {
                long lastActualVersion = actualizeImpl(false);
                if (useLatestExistingApplication && lastCreatedVersion == lastActualVersion) {
                    WpsApplication top = null;
                    long topVersion = -1;
                    for (int i = applicationCache.size() - 1; i >= 0; i--) {
                        WpsApplication app = applicationCache.get(i);
                        long v = app.getAdsVersionNumber();
                        if (v > topVersion) {
                            topVersion = v;
                            top = app;
                        }
                    }
                    if (top != null) {
                        return top;
                    }
                }

                long checkAgainstVersion = -1;
                if (targetVersion<0){
                    try {
                        checkAgainstVersion = actualizeImpl(true);
                    } catch (RadixLoaderException exception) {
                        if (ignoreRadixLoaderException) {
                            Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, "Unexpected exception on actualizing version", exception);
                        } else {
                            throw exception;
                        }
                    }
                }else{
                    checkAgainstVersion = targetVersion;
                }
                for (int i = applicationCache.size() - 1; i >= 0; i--) {
                    WpsApplication app = applicationCache.get(i);
                    if (app.getAdsVersionNumber() < 0 || app.getAdsVersionNumber() == checkAgainstVersion) {
                        return app;
                    }
                }
                WpsApplication app = new WpsApplication(this, changedIds, checkAgainstVersion);
                lastCreatedVersion = app.getAdsVersionNumber();
                applicationCache.add(app);
                return app;
            }
        }
    }
    private final Lock actualizeLock = new ReentrantLock();

    private long actualizeImpl(boolean reallyActualize) throws RadixLoaderException {
        if (actualizeLock.tryLock()) {
            try {
                if (reallyActualize) {
                    return lastActualizedVersion = WpsAdsVersion.getLatestVersionNumber();
                } else {
                    return lastActualizedVersion;
                }
            } finally {
                actualizeLock.unlock();
            }
        } else {
            return lastActualizedVersion;
        }
    }

    void destroyApplication(WpsApplication app) {
        synchronized (applicationCache) {
            applicationCache.remove(app);
            app.closeOwnTracer();
            UserExplorerItemsStorage.clearCache(app);
            Filters.clearCache(app);
            Sortings.clearCache(app);
            TraceProfileTreeController.clearCache(app);
        }
    }
    private long rqIdx = 0;

    @SuppressWarnings("deprecation")
    public void processRequest(HttpServletRequest rq, HttpServletResponse rs) {
        try {
            Logger.getLogger(WebServer.class.getName()).log(Level.FINE, "Request accepted from: {0} Request id #{1}", new Object[]{rq.getRemoteAddr(), rqIdx});
            javax.servlet.http.HttpUtils.parseQueryString("");

            doProcessRequest(rq, rs);
        } catch (RadixError e) {//NOPMD
            throw e;
        } catch (HttpSessionTerminatedError e){
            throw e;
        } catch (Throwable e) {
            RadixError wrapper = new RadixError("Unexpected exception on request processing", e);
            Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, "Unexpected exception on request processing", wrapper);
            throw wrapper;
        } finally {
            Logger.getLogger(WebServer.class.getName()).log(Level.FINE, "Request processed #{0}", rqIdx);
            rqIdx++;
        }
    }

    private static class ContextFinalizer {

        private final HttpSessionContext context;
        private final HttpQuery query;
        private final HttpServletRequest rq;
        private final HttpServletResponse rs;

        public ContextFinalizer(final HttpSessionContext context, 
                                           final HttpQuery query, 
                                           final HttpServletRequest rq, 
                                           final HttpServletResponse rs) {
            this.context = context;
            this.query = query;
            this.rq = rq;
            this.rs = rs;
        }

        public void finalizeContext(final boolean processRequest) {
            try {
                if (processRequest){
                    context.processRequest(query, rq, rs);
                }
                context.dispose();
            } catch(HttpSessionTerminatedError e){
                //Already disposed - ignore
            } catch (Throwable e) {
                Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, "Unexpected exception on session context finalization", e);
            }
        }
    }

    private static class ContextHolder implements HttpSessionBindingListener {

        private final List<HttpSessionContext> contexts = new LinkedList<>();

        @Override
        public void valueBound(HttpSessionBindingEvent hsbe) {
        }

        @Override
        public void valueUnbound(HttpSessionBindingEvent hsbe) {
            synchronized (contexts) {
                for (HttpSessionContext context : contexts) {
                    try {
                        context.dispose();
                    } catch(HttpSessionTerminatedError e){
                        //Already disposed - ignore
                    } catch (Throwable e) {
                        Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, "Unexpected exception on session context finalization", e);
                    }
                }
                contexts.clear();
            }
        }

        public void addContext(HttpSessionContext context) {
            synchronized (contexts) {
                contexts.add(context);
            }
        }

        public List<HttpSessionContext> getContexts() {
            synchronized (contexts) {
                return new ArrayList<>(contexts);
            }
        }

        public void removeContext(HttpSessionContext context) {
            synchronized (contexts) {
                contexts.remove(context);
            }
        }

        public boolean isEmpty() {
            synchronized (contexts) {
                return contexts.isEmpty();
            }
        }
    }

    private void doProcessRequest(HttpServletRequest rq, HttpServletResponse rs) throws RadixLoaderException, UnsupportedEncodingException {
        final String queryString = rq.getQueryString();
        final String decodedQueryString = queryString == null ? null : URLDecoder.decode(queryString, "UTF-8");
        final String rqAuthzHeader = rq.getHeader(HttpHeaderConstants.AUTHZ_HEADER);
        final HttpQuery query = new HttpQuery(decodedQueryString, rqAuthzHeader);
        final HttpSession session = rq.getSession(true);
        
        ContextHolder holder = null;        
        HttpSessionContext context = null;
        boolean requestProcessedByContext = false;

        ContextFinalizer contextFinalizer = null;
        boolean requestWasProcessed = false;
        try {
            synchronized (session) {

                Object contextObj = null;
                try {
                    contextObj = session.getAttribute(HTTP_SESSION_CONTEXT_ATTR_NAME);
                } catch (IllegalStateException e) {
                    throw new RadixError("SESSION_STATE_INVALID", e);
                }
                
                if (contextObj instanceof ContextHolder) {
                    holder = (ContextHolder) contextObj;
                }
                if (holder != null) {
                    for (HttpSessionContext ctx : holder.getContexts()) {
                        if (Utils.equals(ctx.getRootId(), query.getRootId())) {
                            context = ctx;
                            break;
                        }
                    }
                }

                final boolean isDisposeRequest = query.isDisposeRequest();
                if (isDisposeRequest || (context != null && context.wasDisposed())) {
                    boolean shouldInvalidate;
                    if (context != null) {
                        requestProcessedByContext = true;
                        if (!context.wasDisposed()) {
                            if (!checkQueryTocken(context, query, rs)){
                                return;
                            }
                            context.beforeDispose();
                            contextFinalizer = new ContextFinalizer(context, query, rq, rs);
                        }
                        holder.removeContext(context);
                        shouldInvalidate = holder.isEmpty() && isDisposeRequest;
                        if (!isDisposeRequest) {
                            context = null;
                        }
                    } else {
                        shouldInvalidate = holder == null || holder.isEmpty();
                    }
                    if (shouldInvalidate) {
                        rs.setContentType("text/xml;charset=UTF-8");
                         try (PrintWriter out = rs.getWriter()) {
                            out.println("<disposed/>");
                            out.flush();
                        } catch (IOException ex) {//NOPMD
                        }
                        session.invalidate();
                        return;
                    }
                }

                if (context == null) {
                    String rootId = query.getRootId();
                    if (rootId != null && !rootId.isEmpty()) {
                        rs.setContentType("text/xml;charset=UTF-8");
                        try (PrintWriter out = rs.getWriter()) {
                            out.println("<failure reason=\"timeout\"/>");
                            out.flush();
                            return;
                        } catch (IOException ex) {//NOPMD
                        }
                    }
                    StringBuilder remoteInfo = new StringBuilder();
                    remoteInfo.append(rq.getRemoteAddr()).append('_').append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));//NOPMD
                    final WpsApplication application = getLatestAppVersion(true, true, null, -1);
                    final WebServerRunParams runParams = WebServerRunParams.newInstance();
                    String cookieKey = null;
                    Cookie curCookie = null;
                    Cookie[] cookies = rq.getCookies();
                    if (cookies != null) {
                        for (Cookie cookie : cookies) {
                            if (PARAM_COOKIE_NAME.equals(cookie.getName())) {
                                cookieKey = cookie.getValue();
                                curCookie = cookie;
                                break;
                            }
                        }
                    }
                    String cookieValueStr = null;
                    if (cookieKey != null && !urlParamsByCookie.isEmpty() && query.isInitRequest()) {
                        cookieValueStr = urlParamsByCookie.remove(cookieKey);
                    }
                    if (cookieValueStr != null && curCookie != null) {
                        curCookie.setMaxAge(0);
                        rs.addCookie(curCookie);
                    }
                    final HttpConnectionOptions httpConnectionOptions = 
                        new HttpConnectionOptions(rq, runParams.getCertAttrForUserName(),cookieValueStr);
                    context = 
                        new HttpSessionContext(application, httpConnectionOptions, remoteInfo.toString(), runParams);
                    if (holder == null) {
                        holder = new ContextHolder();
                        session.setAttribute(HTTP_SESSION_CONTEXT_ATTR_NAME, holder);
                    }
                    holder.addContext(context);
                }
            }
            
            int inactiveInterval = context.getRunParams().getSessionInactiveInteraval();
            if (inactiveInterval > 0) {
                session.setMaxInactiveInterval(inactiveInterval);
            }            

            if (processRequestImpl(context.application, context.getRunParams(), rq, rs)) {
                requestWasProcessed = true;
                return;
            }
            if (!requestProcessedByContext 
                && context.getHttpConnectionOptions().isSecurityOptionsAcceptable(rq)
                && checkQueryTocken(context, query, rs)) {
                final String referer = rq.getHeader(HttpHeaderConstants.REFERER);
                if (!referer.isEmpty() && referer.contains("?")){  
                    rs.setContentType("text/xml;charset=UTF-8");
                    final String paramCoockie = generateParamCookie();
                    urlParamsByCookie.put(paramCoockie, referer);
                    Cookie cookie = new Cookie(PARAM_COOKIE_NAME, paramCoockie);
                    cookie.setPath("/");
                    cookie.setMaxAge(60*2);
                    cookie.setSecure(false);
                    rs.addCookie(cookie);
                    try (PrintWriter out = rs.getWriter()) {
                        if (holder!=null){
                            context.beforeDispose();
                            holder.removeContext(context);
                            session.invalidate();                         
                        }
                        out.println("<redirect/>");
                        out.flush();
                        return;
                    } catch (IOException ex) {//NOPMD
                    }
                }
                context.rqCheckDelay = session.getMaxInactiveInterval() * 1000;
                context.processRequest(query, rq, rs);
                requestWasProcessed = true;
            }

        } finally {
            if (contextFinalizer != null) {
                contextFinalizer.finalizeContext(!requestWasProcessed);
            }
            Thread.currentThread().setContextClassLoader(null);
        }
    }
    
    private String generateParamCookie(){
        final byte[] arrBytes = new byte[16];
        rnd.nextBytes(arrBytes);
        return Hex.encode(arrBytes);
    }
    
    private static boolean checkQueryTocken(final HttpSessionContext httpContext, final HttpQuery query, final HttpServletResponse response){
        if (!httpContext.checkQueryToken(query)){
            try{
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }catch(IOException ex){                
            }
            return false;
        }
        return true;
    }

    public static WebServer getInstance() {
        return instance;
    }
//    public IClientApplication getEnvironment() {
//        return wpsEnvironment;
//    }
    private static final Object imagesDirLock = new Object();
    private static File imagesDir = null;

    public static File getImagesDir() throws IOException {
        synchronized (imagesDirLock) {
            if (imagesDir == null) {
                imagesDir = RadixLoader.getInstance().createTempFile("rdx_images_store");//File.createTempFile("rdx_images", "store");RADIX-9148
                if (imagesDir.exists()) {
                    FileUtils.deleteFile(imagesDir);
                }
                imagesDir.mkdirs();
                if (!imagesDir.exists()) {
                    throw new IOException("Unable to create images directory");
                }
            }
            return imagesDir;
        }
    }

    void requestUpload(final String itemId, final String uploadId, final Object wrapper) {
        try {
            Class<?> uploadStorage = getClass().getClassLoader().loadClass("org.radixware.ws.servlet.UploadStore");
            Method register = uploadStorage.getMethod("registerWaiter", String.class, Object.class);
            register.invoke(null, uploadId, wrapper);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException ex) {//NOPMD
        }
    }

    UploadSystemHandler requestUpload(final String itemId, final String uploadId) {
        final UploadSystemHandler handler = new UploadSystemHandler();
        requestUpload(itemId, uploadId, handler);
        return handler;
    }

    void disposeUpload(final String itemId, final String uploadId) {
        try {
            Class<?> uploadStorage = getClass().getClassLoader().loadClass("org.radixware.ws.servlet.UploadStore");
            Method register = uploadStorage.getMethod("removeFile", String.class);
            register.invoke(null, uploadId);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException ex) {//NOPMD
        }
    }
}
