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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import org.apache.commons.logging.LogFactory;

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
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.log.DelegateLogFactory;
import org.radixware.kernel.starter.log.StarterLog;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.kernel.starter.radixloader.RadixSVNLoader;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.icons.images.WsIcons;

public class WebServer {

    private static final String HTTP_SESSION_CONTEXT_ATTR_NAME = "rdxSessionContext";
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
            return userSession.getLanguage();
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
    public static final String VERSION = "1.2.21";

    public static void main(String[] args) {
        try {
            if (args.length != 0 && !WebServerRunParams.processArgs(args)) {
                throw new IllegalArgumentException("Wrong Web Presentation Server arguments");
            }
            final Logger logger = Logger.getLogger(WebServer.class.getName());            
            printStarterInformation(logger);            
            printWebServerArguments(logger);
            printProductVersions(logger);
            {//check 
                final String dbPath = WebServerRunParams.getSettingsDatabaseDir();
                if (dbPath!=null){
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
                    return new StarterLog("Radix WPS");
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
        } catch (IllegalArgumentException | ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
        }
    }
    
    private static void printStarterInformation(final Logger logger){
        final String arguments = Starter.getArguments().asStrWithoutAppParams().replace(" ", "\n\t");
        logger.log(Level.INFO, "Starter arguments: '{'\n\t{0}\n'}'", arguments);
        final StringBuilder starterParams = new StringBuilder();
        final RadixLoader rxLoader = RadixLoader.getInstance();
        starterParams.append("{\n\tRevision number: ");
        try{
            starterParams.append(String.valueOf(rxLoader.getCurrentRevision()));
        }catch(RadixLoaderException exception){
            starterParams.append("Unknown");
        }
        if (rxLoader instanceof RadixSVNLoader){
            starterParams.append("\n\tDirectory for cache: ");
            starterParams.append(rxLoader.getRoot());
        }
        starterParams.append("\n\tDirectory for temporary files: ");
        starterParams.append(org.radixware.kernel.starter.utils.SystemTools.getTmpDir());
        if (rxLoader instanceof RadixSVNLoader){
            starterParams.append("\n\tSubversion URL list: {");
            final List<String> urls = ((RadixSVNLoader)rxLoader).getSvnUrls();
            for (String url: urls){
                starterParams.append("\n\t\t");
                starterParams.append(url);
            }
            starterParams.append("\n\t}");
        }else{
            starterParams.append("\n\tWorking directory: ");
            starterParams.append(rxLoader.getRepositoryUri());
        }
        starterParams.append("\n}");
        logger.log(Level.INFO, "Starter information: {0}", starterParams.toString());
        final Map<String,String> replacements = rxLoader.getLocalFiles().getAllReplacements();
        if (replacements!=null && !replacements.isEmpty()){
            final StringBuilder fileReplacements = new StringBuilder("{");
            for (Map.Entry<String,String> replacement: replacements.entrySet()){
                fileReplacements.append("\n\t");
                fileReplacements.append(replacement.getKey());
                fileReplacements.append(" => ");
                fileReplacements.append(replacement.getValue());
            }
            fileReplacements.append("\n}");
            logger.log(Level.INFO, "File replacements: {0}", fileReplacements.toString());
        }
    }        
    
    private static void printWebServerArguments(final Logger logger){
        logger.log(Level.INFO, "Web Presentation Server arguments: {0}", WebServerRunParams.print());
        {// init kerberos
            final WebServerRunParams.KrbWpsOptions krbOptions = WebServerRunParams.getKerberosOptions();
            if (krbOptions != null) {
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
    
    private static void printProductVersions(final Logger logger){
        final RadixClassLoader loader = (RadixClassLoader)WebServer.class.getClassLoader();
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
        logger.log(Level.INFO,"Product versions: {0}", versions.toString());
    }

    private static void checkDbPath(final String dbPath, final Logger logger){
        final File dbDir = new File(dbPath);
        final String path = dbDir.getAbsolutePath();
        final String messageTemplate = "Failed to use local settings storage: {0}";
        if (!dbDir.exists()) {
            try {
                if (!dbDir.mkdirs()) {
                    final String reason = "failed to create \'"+path+"\'";
                    logger.log(Level.SEVERE, messageTemplate,reason);
                    return;
                }                      
            } catch (SecurityException ex) {
                final String reason = "failed to create \'"+path+"\'";
                logger.log(Level.SEVERE, messageTemplate,reason);
                return;
            }
        }
        if (!dbDir.isDirectory()){
            final String reason = "\'"+path+"\' is not a directory";
            logger.log(Level.SEVERE, messageTemplate,reason);
            return;            
        }
        if (!dbDir.canRead()){
            final String reason = "\'"+path+"\' is not accessible";
            logger.log(Level.SEVERE, messageTemplate,reason);
            return;                        
        }
        if (!dbDir.canWrite()){
            final String reason = "\'"+path+"\' is not writable";
            logger.log(Level.SEVERE, messageTemplate,reason);                        
        }
    }

    public KerberosCredentials getKerberosServiceCredentials() {
        synchronized (krbSem) {
            return serviceCreds;
        }
    }
    private boolean stopped = false;

    public void shutdown() {
        synchronized (shutdownLock) {
            stopped = true;
            shutdownLock.notify();
        }
    }

    private WebServer() {
    }

    private static ClassLoader getStarterClassLoader() {
        ClassLoader startersClassLoader = Starter.class.getClassLoader();
        while (RadixClassLoader.class.getName().equals(startersClassLoader.getClass().getName())) {
            startersClassLoader = startersClassLoader.getClass().getClassLoader();
        }
        return startersClassLoader;
    }

    private boolean processRequestImpl(WpsApplication application, final HttpServletRequest rq, final HttpServletResponse rs) {
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
                                    String fileName = content.desc;
                                    if (fileName == null || fileName.isEmpty()) {
                                        fileName = content.file.getName();
                                    } else {
                                        String fileExt = FileUtils.getFileExt(content.file);
                                        boolean extComputed = false;
                                        if (fileExt == null || fileExt.isEmpty()) {
                                            extComputed = true;
                                            String mt = content.mimeType;
                                            if (mt != null && !mt.isEmpty()) {
                                                int index = mt.indexOf("/");
                                                if (index >= 0 && index + 1 < mt.length()) {
                                                    fileExt = mt.substring(index + 1);
                                                } else {
                                                    fileExt = mt;
                                                }
                                            }
                                        }
                                        if (extComputed && fileExt != null && !fileExt.isEmpty()) {
                                            fileName += "." + fileExt;
                                        }
                                    }
                                    rs.addHeader("content-disposition", "attachment;filename=\"" + fileName + "\"");
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
                        String resourceName = fullUri.substring(8);
                        InputStream stream = getClass().getClassLoader().getResourceAsStream(resourceName);
                        if (stream != null) {
                            if (resourceName.endsWith(".css")) {
                                rs.setContentType("text/css");
                            } else {
                                rs.setContentType("text/javascript");
                            }
                            FileUtils.copyStream(stream, rs.getOutputStream());
                        }
                    }
                } catch (IOException e) {
                } catch (Throwable e) {
                }
            }
            return true;
        }
        return false;
    }
    private final List<WpsApplication> applicationCache = new LinkedList<>();
    private long lastCreatedVersion;
    private long lastActualizedVersion;
    private boolean actualizerThreadStarted = false;

    WpsApplication getLatestAppVersion(final boolean ignoreRadixLoaderException, boolean useLatestExistingApplication, Collection<Id> changedIds) throws RadixLoaderException {
        synchronized (applicationCache) {
            if (applicationCache.isEmpty()) {
                try {
                    WpsAdsVersion.actualize(true);
                } catch (RadixLoaderException exception) {
                    if (ignoreRadixLoaderException) {
                        Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, "Unexpected exception on actualizing version", exception);
                    } else {
                        throw exception;
                    }
                }
                WpsApplication app = new WpsApplication(this, changedIds);
                lastActualizedVersion = lastCreatedVersion = app.getAdsVersionNumber();
                applicationCache.add(app);
                if (!actualizerThreadStarted) {
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
                }
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

                long checkAgainsVersion = -1;
                try {
                    checkAgainsVersion = actualizeImpl(true);
                } catch (RadixLoaderException exception) {
                    if (ignoreRadixLoaderException) {
                        Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, "Unexpected exception on actualizing version", exception);
                    } else {
                        throw exception;
                    }
                }
                for (int i = applicationCache.size() - 1; i >= 0; i--) {
                    WpsApplication app = applicationCache.get(i);
                    if (app.getAdsVersionNumber() < 0 || app.getAdsVersionNumber() == checkAgainsVersion) {
                        return app;
                    }
                }
                WpsApplication app = new WpsApplication(this, changedIds);
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

        public ContextFinalizer(HttpSessionContext context, HttpQuery query, HttpServletRequest rq, HttpServletResponse rs) {
            this.context = context;
            this.query = query;
            this.rq = rq;
            this.rs = rs;
        }

        public void finalizeContext() {
            try {
                context.processRequest(query, rq, rs);
                context.dispose();
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

    private void doProcessRequest(HttpServletRequest rq, HttpServletResponse rs) throws RadixLoaderException {
        final String queryString = rq.getQueryString();
        final String rqAuthzHeader = rq.getHeader(HttpHeaderConstants.AUTHZ_HEADER);
        final HttpQuery query = new HttpQuery(queryString, rqAuthzHeader);
        final HttpSession session = rq.getSession(true);

        int inactiveInterval = WebServerRunParams.getSessionInactiveInteraval();
        if (inactiveInterval > 0) {
            session.setMaxInactiveInterval(inactiveInterval);
        }

        HttpSessionContext context = null;
        boolean requestProcessedByContext = false;

        ContextFinalizer contextFinalizer = null;
        try {
            synchronized (session) {

                Object contextObj = null;
                try {
                    contextObj = session.getAttribute(HTTP_SESSION_CONTEXT_ATTR_NAME);
                } catch (IllegalStateException e) {
                    throw new RadixError("SESSION_STATE_INVALID", e);
                }

                ContextHolder holder = null;
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

                if (query.isDisposeRequest() || (context != null && context.wasDisposed())) {
                    boolean shouldInvalidate;
                    if (context != null) {
                        requestProcessedByContext = true;
                        if (!context.wasDisposed()) {
                            contextFinalizer = new ContextFinalizer(context, query, rq, rs);
                        }
                        holder.removeContext(context);
                        shouldInvalidate = holder.isEmpty() && query.isDisposeRequest();
                        if (!query.isDisposeRequest()) {
                            context = null;
                        }
                    } else {
                        shouldInvalidate = holder == null || holder.isEmpty();
                    }
                    if (shouldInvalidate) {
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
                    context = new HttpSessionContext(getLatestAppVersion(true, true, null), new HttpConnectionOptions(rq), remoteInfo.toString());
                    if (holder == null) {
                        holder = new ContextHolder();
                        session.setAttribute(HTTP_SESSION_CONTEXT_ATTR_NAME, holder);
                    }
                    holder.addContext(context);
                } /*else {
                 context.checkVersionIsCurrent();
                 }*/

            }

            //final ClassLoader cl = context.apllication.getDefManager().getAdsVersion().setupCurrentThread();
            if (processRequestImpl(context.application, rq, rs)) {
                return;
            }
            if (!requestProcessedByContext && context.getHttpConnectionOptions().isSecurityOptionsAcceptable(rq)) {
                context.rqCheckDelay = session.getMaxInactiveInterval() * 1000;
                context.processRequest(query, rq, rs);
            }

        } finally {
            if (contextFinalizer != null) {
                contextFinalizer.finalizeContext();
            }
            Thread.currentThread().setContextClassLoader(null);
        }
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
