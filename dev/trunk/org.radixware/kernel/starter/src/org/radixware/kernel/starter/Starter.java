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
package org.radixware.kernel.starter;

import java.awt.GraphicsEnvironment;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Provider;
import java.security.Security;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.starter.config.ConfigFileAccessor;
import org.radixware.kernel.starter.log.SafeLogger;
import org.radixware.kernel.starter.log.StarterLog;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.*;
import org.radixware.kernel.starter.radixloader.RadixLoader.LocalFiles;
import org.radixware.kernel.starter.url.UrlFactory;
import org.radixware.kernel.starter.utils.KerberosConfig;
import org.radixware.kernel.starter.utils.SystemTools;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class Starter {

    public static final String STARTER_APP_DATA_ROOT = "radixware.org/starter";
    private static final String VERSION_FOR_RESTART_PROP = "rdx.starter.version.for.restart";
    private static final String APP_STATE_FOR_RESTART_PROP = "rdx.starter.app.state.for.restart";
    private static final String AUTH_PASSWORD_INTERACTIVE_PROP = "rdx.starter.auth.password.interactive";
    private static final String SSH_KEY_PASSWORD_INTERACTIVE_PROP = "rdx.starter.ssh.key.password.interactive";
    private static final String STARTER_CONFIG_FILE = "rdx.starter.config.file";
    private static DateFormat SHUTDOWN_LOG_DATE_FORMAT = new SimpleDateFormat("HH-mm-dd-MM-yyyy");
    private volatile static String[] restartParameters;
    public static final String VERSION = "1.41";
    private static volatile Runnable cleanupHook = null;
    private static final AtomicBoolean cleanupLaunched = new AtomicBoolean(false);
    private static final CountDownLatch cleanupIsDoneLatch = new CountDownLatch(1);
    private static volatile boolean isRoot;
    private static volatile Thread cleanTempFilesFromPrevRunThread;
    private static final CountDownLatch childHooksLatch = new CountDownLatch(1);
    private static final CountDownLatch starterMainDoneLatch = new CountDownLatch(1);
    private static volatile Thread starterMainThread = null;
    private static volatile StarterArguments starterArgs;
    private static volatile String productName;
    private static volatile Integer restartExitCode;
    private static volatile boolean restartByExitCodeRequired;
    private static volatile byte[] appRestartState;
    private static volatile long restartVersion;
    private static volatile boolean restartByNewProcess;
    //XXX: do not rename, used via reflection
    private static volatile Map<String, String> restartedProcessState;
    private static volatile String[] mainArgs;
    private static volatile long targetVer = -1;
    private static volatile int aadcMemberId = 0;

    static {
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
    }

    private static final class InvalidUsageException extends Exception {
    }

    /**
     * Java replaces calls to static constant fields to literals during
     * compilation, so this method is preferred way to get actual starter
     * version.
     *
     * <p>
     * XXX: do not rename, used via reflection! </P
     *
     * @return
     */
    public static String getVersion() {
        return VERSION;
    }

    public static String getRootStarterVersion() {
        if (isRootStarter_TrickyButGuaranteed()) {
            return VERSION;
        } else {
            try {
                Object result = getRootStarterClassLoader().loadClass(Starter.class.getName()).getMethod("getVersion").invoke(null);
                return result == null ? null : result.toString();
            } catch (Throwable t) {
                return null;
            }
        }
    }

    static void invalidUsage(final String message) throws InvalidUsageException {
        System.err.println("Invalid usage: " + message);
        use();
        throw new InvalidUsageException();
    }

    static void invalidParamValue(final String message) throws InvalidUsageException {
        System.err.println(message);
        throw new InvalidUsageException();
    }

    static void copyright() {
        System.err.println("RadixWare Starter. (C) CompassPlus Ltd.");
    }

    static void use() {
        System.err.println("Use:\n"
                + "1) Starter <[-svnHomeUrl=<url> [-downloadServer] [-downloadExplorer] [-downloadWeb]] | [-workDir=<dir>]> -topLayerUri=<uri1,uri2,...> [-localFileCacheDir=<path>] [-authUser=<user>] [-authPassword=<password>] [-authPasswordInreractive] [-krbParams=<params>] [-sshKeyFile=<file>] [-sshKeyPassword=<pwd>] [-sshKeyPasswordInteractive] [-svnServerCertFile=<path>] [-svnLog] [-noRestart] [-showSplashScreen[=<title>]] <[-localFileList=<path>] | [-localReplacementDir=<path>]> class [arguments]\n"
                + "2) Starter -configFile <path-to-file>"
                + "3) Starter <[-svnHomeUrl=<url> [-authUser=<user>] [-authPassword=<password>] [-authPasswordInreractive] [-krbParams=<params>] [-sshKeyFile=<file>] [-sshKeyPassword=<pwd>] [-sshKeyPasswordInteractive] [-svnServerCertFile=<path>] [-svnLog] export <filename>\n"
                + "4) Starter -configFile <path-to-file> export <filepath> [destname]");
    }

    public static String getProductName() {
        return productName;
    }

    public static StarterArguments getStarterArguments() {
        return starterArgs;
    }

    public static void main(String[] args) throws Exception {
        try {
            try {
                starterMainThread = Thread.currentThread();
                try {
                    try {
                        starterMain(args);
                    } finally {
                        starterMainDoneLatch.countDown();
                    }
                } finally {
                    starterMainThread = null;
                }
            } finally {
                ShutdownHooks.discard();
            }
        } catch (Exception ex) {
            if (isRootStarter_TrickyButGuaranteed()) {
                System.err.println("Unexpected error:");
                ex.printStackTrace(System.err);
            }
            throw ex;
        }
        if (restartByExitCodeRequired) {
            System.exit(restartExitCode);
        }
    }

    private static boolean isRootStarter_TrickyButGuaranteed() {
        try {
            return !Starter.class.getClassLoader().getClass().getName().toLowerCase().contains("radix");
        } catch (Exception ex) {
            return true;
        }
    }

    public static Thread getStarterMainThread() {
        return starterMainThread;
    }

    public static void starterMain(String[] args) throws Exception {
        if (args.length == 1) {
            copyright();
            use();
            return;
        }
        try {
            System.setSecurityManager(null);
            mainArgs = args;
            starterArgs = StarterCommandLineParser.parse(args);
            if (starterArgs.isFromConfigFile()) {
                args = getArgs(starterArgs);
                System.setProperty(STARTER_CONFIG_FILE, starterArgs.getConfigFileName());
            }
            final Map<String, String> starterParameters = starterArgs.getStarterParameters();
            final List<String> appParameters = starterArgs.getAppParameters();
            final int countOfStarterParams = args.length - appParameters.size() + 1;//+1  for class to start
            final String svnHomeUrl = trimLastSlash(starterParameters.get(StarterArguments.SVN_HOME_URL));
            final String workDir = starterParameters.get(StarterArguments.WORK_DIR);
            File localFileCacheDirFile = null;
            productName = starterParameters.get(StarterArguments.PRODUCT_NAME);
            if (starterParameters.containsKey(StarterArguments.LOCAL_FILE_CACHE_DIR)) {
                final String localFileCacheDir = starterParameters.get(StarterArguments.LOCAL_FILE_CACHE_DIR);
                localFileCacheDirFile = new File(localFileCacheDir);
                if (localFileCacheDirFile.exists()) {
                    if (!localFileCacheDirFile.isDirectory()) {
                        invalidParamValue("File \"" + localFileCacheDir + "\" defined by " + StarterArguments.LOCAL_FILE_CACHE_DIR + " parameter is not a directory");
                    }
                } else {
                    if (!localFileCacheDirFile.mkdirs()) {
                        invalidParamValue("Can't create file (\"" + localFileCacheDir + "\") using path defined by " + StarterArguments.LOCAL_FILE_CACHE_DIR + " parameter");
                    }
                }
            }

            final boolean noRestart = starterParameters.containsKey(StarterArguments.NO_RESTART);
            isRoot = isRootStarter_TrickyButGuaranteed();
            final boolean isExplorerOnMac = isExplorerOnMac(appParameters.get(0));//for workaround of JDK-8017776
            if (isRoot) {
                tricksOnRootStarterInitializaton(isExplorerOnMac);
                if (starterArgs.getStarterParameters().containsKey(StarterArguments.RESTART_EXIT_CODE)) {
                    restartExitCode = Integer.parseInt(starterArgs.getStarterParameters().get(StarterArguments.RESTART_EXIT_CODE));
                } else if (System.getenv(StarterArguments.RESTART_EXIT_CODE_ENV_VARIABLE) != null) {
                    restartExitCode = Integer.parseInt(System.getenv(StarterArguments.RESTART_EXIT_CODE_ENV_VARIABLE));
                }
            }
            readRestartState();

            if (targetVer == -1 && starterParameters.containsKey(StarterArguments.START_VERSION)) {
                targetVer = Long.parseLong(starterParameters.get(StarterArguments.START_VERSION));
            }

            int restartedCount = 0;
            if (starterParameters.containsKey(StarterArguments.RESTARTED_COUNT)) {
                restartedCount = Integer.parseInt(starterParameters.get(StarterArguments.RESTARTED_COUNT));
            }
            final String authUser = starterParameters.get(StarterArguments.AUTH_USER);
            String authPassword;
            if (starterParameters.containsKey(StarterArguments.AUTH_PASSWORD_INTERACTIVE)) {
                if (restartedProcessState != null && restartedProcessState.containsKey(AUTH_PASSWORD_INTERACTIVE_PROP)) {
                    authPassword = restartedProcessState.get(AUTH_PASSWORD_INTERACTIVE_PROP);
                } else {
                    if (System.console() == null) {
                        throw new IllegalArgumentException("'" + StarterArguments.AUTH_PASSWORD_INTERACTIVE + "' parameter is not supported in environments without command prompt");
                    }
                    System.out.print("Enter user password: ");
                    authPassword = new String(System.console().readPassword());
                }
                System.setProperty(AUTH_PASSWORD_INTERACTIVE_PROP, "true");
            } else {
                authPassword = starterParameters.get(StarterArguments.AUTH_PASSWORD);
            }
            final String krb_realm = starterParameters.get(StarterArguments.KRB_REALM);
            final String krb_kdc = starterParameters.get(StarterArguments.KRB_KDC);
            final String krb_params_str = starterParameters.get(StarterArguments.KRB_PARAMS);
            final String sshKeyFile = starterParameters.get(StarterArguments.SSH_KEY_FILE);
            final String sshKeyPwd;
            if (starterParameters.containsKey(StarterArguments.SSH_KEY_PASSWORD_INTERACTIVE)) {
                if (restartedProcessState != null && restartedProcessState.containsKey(SSH_KEY_PASSWORD_INTERACTIVE_PROP)) {
                    sshKeyPwd = restartedProcessState.get(SSH_KEY_PASSWORD_INTERACTIVE_PROP);
                } else {
                    System.out.print("Enter SSH key password: ");
                    sshKeyPwd = new String(System.console().readPassword());
                }
                System.setProperty(SSH_KEY_PASSWORD_INTERACTIVE_PROP, "true");
            } else {
                sshKeyPwd = starterParameters.get(StarterArguments.SSH_KEY_PASSWORD);
            }
            final boolean isDevTools = starterParameters.containsKey(StarterArguments.DEV_TOOLS);
            final boolean isSvnLogOn = starterParameters.containsKey(StarterArguments.SVN_LOG);
            final boolean isExport = !appParameters.isEmpty() && "export".equals(appParameters.get(0));
            final HashMap<String, String> krb_params = new HashMap<String, String>();
            if (krb_params_str != null) {
                try {
                    final StringTokenizer st = new StringTokenizer(krb_params_str, " ");
                    while (st.hasMoreElements()) {
                        final StringTokenizer st1 = new StringTokenizer(st.nextToken(), "=");
                        final String key = st1.nextToken();
                        final String val = st1.nextToken();
                        krb_params.put(key, val);
                    }
                } catch (Exception e) {
                    invalidUsage("invalid format of " + StarterArguments.KRB_PARAMS);
                }
            }
            if (krb_realm != null) {
                KerberosConfig.setRealm(krb_realm);
            }
            if (krb_kdc != null) {
                KerberosConfig.setKdc(krb_kdc);
            }
            KerberosConfig.setConfiguration(krb_params);
            if (svnHomeUrl == null && workDir == null) {
                invalidUsage("svnHomeUrl or workDir must be specified");
            }

            if (svnHomeUrl != null && workDir != null) {
                invalidUsage("svnHomeUrl and workDir can not be specified at the same time");
            }

            final String urisString = starterParameters.get("topLayerUri");
            if (!isExport && (urisString == null || urisString.isEmpty())) {
                invalidUsage("topLayerUri must be specified");
            }

            System.setProperty("org.apache.commons.logging.Log", StarterLog.class.getName());
            LogFactory.releaseAll();
            //actualize() -> lock -> load LogFactory.class -> getFile() -> lock again -> exception
            SafeLogger.getInstance();
            LogFactory.getLog(Starter.class);
            SAXParserFactory.newInstance().newSAXParser();

            final List<String> topLayerUris;
            if (isExport) {
                topLayerUris = new ArrayList<>();
            } else {
                topLayerUris = urisString != null ? Arrays.asList(urisString.split(",")) : null;
            }
            final String localFileListPath = starterParameters.get(StarterArguments.LOCAL_FILE_LIST_PATH);
            final String localReplacementDir = starterParameters.get(StarterArguments.LOCAL_REPLACEMENT_DIR_PATH);
            if (localFileListPath != null && localReplacementDir != null) {
                System.err.println("ERROR: Parameters -localFileList and -localReplacementDir can't be used at same time");
                copyright();
                use();
                return;
            }
            final LocalFiles localFiles = RadixLoader.LocalFiles.getInstance(isExport ? null : localFileListPath, isExport ? null : localReplacementDir);
            if (svnHomeUrl != null) {
                final String svnServerCertFile = starterParameters.get(StarterArguments.SVN_SERVER_CERT_FILE);
                final String additionalSvnUrlsString = starterParameters.get(StarterArguments.ADDITIONAL_SVN_URLS);
                final List<String> additionalSvnUrls = additionalSvnUrlsString == null ? Collections.<String>emptyList() : Arrays.asList(additionalSvnUrlsString.split(","));

                final RadixSVNLoader l = new RadixSVNLoader(svnHomeUrl, additionalSvnUrls, topLayerUris, authUser, authPassword, sshKeyFile, sshKeyPwd, svnServerCertFile, isSvnLogOn, localFileCacheDirFile, localFiles);
                RadixLoader.setInstance(l);
            } else {
                RadixLoader.setInstance(new RadixDirLoader(workDir, topLayerUris, localFiles));
            }
            if (isExport) {
                String srcName = appParameters.get(1);
                String dstName;
                if (appParameters.size() == 3) {
                    dstName = appParameters.get(2);
                } else {
                    int nameStartIdx = srcName.lastIndexOf("/");
                    if (nameStartIdx == -1) {
                        nameStartIdx = srcName.lastIndexOf("\\");
                    }
                    if (nameStartIdx == -1) {
                        dstName = srcName;
                    } else {
                        dstName = srcName.substring(nameStartIdx + 1);
                    }
                }
                final IRepositoryEntry entry = RadixLoader.getInstance().exportFile(appParameters.get(1), -1);
                final Path path = Files.write(new File(dstName).toPath(), entry.getData());
                SafeLogger.getInstance().eventFromSource("Export", "Exported '" + srcName + "' to '" + path.toFile().getAbsolutePath() + "', revision " + entry.getRevisionNum() + " from " + new Date(entry.getRevisionCreationTimeMillis()));
                return;
            }

            long aadcStickedVer = -1;
            if (starterParameters.containsKey(StarterArguments.AADC_MEMBER_ID)) {
                aadcMemberId = Integer.parseInt(starterParameters.get(StarterArguments.AADC_MEMBER_ID));
                if (aadcMemberId < 1 || aadcMemberId > 2) {
                    throw new RadixLoaderException("AADC member id should be 1 or 2 (actual " + aadcMemberId + ")");
                }
                aadcStickedVer = readAadcStickedVersions()[aadcMemberId - 1];
            }

            if (aadcStickedVer != -1) {
                if (targetVer != -1 && aadcStickedVer != targetVer) {
                    throw new RadixLoaderException("Unable to start in requested revision '" + targetVer + "': AADC sticked revision is present ('" + aadcStickedVer + "') and differs");
                }
                targetVer = aadcStickedVer;
            }

            boolean showSplash = false;
            String splashTitle = null;
            if (starterParameters.containsKey(StarterArguments.SHOW_SPLASH_SCREEN)) {
                if (GraphicsEnvironment.isHeadless()) {
                    if (isRoot) {
                        SafeLogger.getInstance().warningFromSource(getShortDesc(), "Splash screen will no not be shown because environment is headless");
                    }
                } else if (starterParameters.containsKey(StarterArguments.DISABLE_SWING_PRELOAD)) {
                    if (isRoot) {
                        SafeLogger.getInstance().warningFromSource(getShortDesc(), "Parameter '" + StarterArguments.DISABLE_SWING_PRELOAD + "' prevented showing of the splash screen");
                    }
                } else if (isExplorerOnMac) {
                    if (isRoot) {
                        SafeLogger.getInstance().warningFromSource(getShortDesc(), "Splash screen will no not be shown on OSX");
                    }
                } else {
                    showSplash = true;
                    splashTitle = starterParameters.get(StarterArguments.SHOW_SPLASH_SCREEN);
                    StarterSplashScreen.setStatusProvider(RadixLoader.getInstance());
                }
            }
            if (splashTitle == null) {
                splashTitle = appParameters.get(0);//name of the class to start
            }

            if (isRoot) {
                cleanTempFilesFromPrevRunThread = new Thread(TempFiles.createRemoveTask());
                cleanTempFilesFromPrevRunThread.setDaemon(true);
                cleanTempFilesFromPrevRunThread.start();
                registerWaitRootStarterShutdownHook();
                if (!GraphicsEnvironment.isHeadless() && !starterParameters.containsKey(StarterArguments.DISABLE_SWING_PRELOAD) && !isExplorerOnMac) {
                    initializeAwtThreadWithSystemContextClassLoader();
                }
            }

            ShutdownHooks.init();

            try {
                final ClassLoader startersClassLoader = getRootStarterClassLoader();
                //URL factory should not be versioned
                final Class<?> urlFactory = startersClassLoader.loadClass(UrlFactory.class.getName());
                urlFactory.getMethod("setRadixLoader", new Class[]{Object.class}).invoke(null, RadixLoader.getInstance());
                if (isRoot) {
                    RadixURLTool.setUrlStreamHandlerFactory(urlFactory);
                }
                if (!noRestart) {
                    for (;;) {
                        if (showSplash) {
                            StarterSplashScreen.show(splashTitle);
                        }
                        try {
                            tryActualizeLoader();
                            final RadixClassLoader class_loader = RadixLoader.getInstance().createClassLoader(null, Starter.class.getClassLoader());
                            class_loader.setSelfLoader(true);
                            final Class cl = class_loader.loadClass(Starter.class.getName());
                            String clVer;
                            try {
                                clVer = (String) cl.getField("VERSION").get(null);
                            } catch (NoSuchFieldException e) {
                                clVer = null;
                            }
                            if (!VERSION.equals(clVer)) {
                                System.err.println(String.format("Version of the local starter (%s) does not correspond to the version available in the repository (%s)", VERSION, clVer));
                            }
                            final Method method = cl.getMethod("main", String[].class);

                            final String[] new_args = new String[args.length + 2];
                            restartedCount++;
                            for (int i = 0, j = 0; j < new_args.length;) {
                                if (i == j && args[i].charAt(0) != '-') {
                                    new_args[j++] = "-noRestart";
                                    new_args[j++] = "-restartedCount=" + restartedCount;
                                }
                                if (i == j && args[i].equals("-authPasswordInteractive")) {
                                    new_args[j++] = "-authPassword=" + authPassword;
                                } else if (i == j && args[i].equals("-sshKeyPasswordInteractive")) {
                                    new_args[j++] = "-sshKeyPassword=" + sshKeyPwd;
                                } else {
                                    new_args[j++] = args[i];
                                }
                                i++;
                            }
                            method.invoke(null, (Object) new_args);
                            try {
                                final String[] restart_params = (String[]) cl.getMethod("getRestartParameters").invoke(null);
                                if (restart_params == null) {
                                    break;
                                }
                                if (restartExitCode != null) {
                                    restartByExitCodeRequired = true;
                                    break;
                                }
                                final String[] restart_args = new String[countOfStarterParams + restart_params.length];
                                for (int i = 0; i < countOfStarterParams; i++) {
                                    restart_args[i] = args[i];
                                }
                                for (int i = 0; i < restart_params.length; i++) {
                                    restart_args[countOfStarterParams + i] = restart_params[i];
                                }
                                args = restart_args;
                            } catch (NoSuchMethodException e) {
                                break;
                            }
                        } finally {
                            StarterSplashScreen.hide();
                        }
                    }
                } else {
                    if (isRoot() && showSplash) {
                        StarterSplashScreen.show(splashTitle);
                    }
                    if (svnHomeUrl != null) {//RADIX-4315
                        final RadixSVNLoader l = (RadixSVNLoader) RadixLoader.getInstance();
                        if (starterParameters.containsKey("downloadExplorer")) {
                            l.preloadExplorer();
                        }
                        if (starterParameters.containsKey("downloadServer")) {
                            l.preloadServer();
                        }
                        if (starterParameters.containsKey("downloadWeb")) {
                            l.preloadWeb();
                        }
                    }
                    tryActualizeLoader();
                    final String configFileName = System.getProperty(STARTER_CONFIG_FILE);
                    if (configFileName != null && starterArgs.getStarterParameters().containsKey(StarterArguments.START_VERSION)) {
                        ConfigFileAccessor fileAccessor = ConfigFileAccessor.get(configFileName, StarterArguments.STARTER_SECTION);
                        fileAccessor.update(Collections.singletonList(StarterArguments.START_VERSION), null);
                    }
                    final Set<String> group_set = new HashSet<>();
                    group_set.add("KernelCommon");
                    group_set.add("KernelClient");
                    group_set.add("KernelExplorer");
                    group_set.add("KernelWeb");
                    group_set.add("KernelOther");
                    group_set.add("KernelServer");
                    if (isDevTools) {
                        group_set.add("KernelDesigner");
                    }
                    if (appParameters.isEmpty()) {
                        invalidUsage("no class");
                    }
                    final String className = appParameters.get(0);
                    final RadixClassLoader class_loader = RadixLoader.getInstance().createClassLoader(group_set, Starter.class.getClassLoader());
                    try {
                        final ClassLoader prevContextClassLoader = Thread.currentThread().getContextClassLoader();
                        Thread.currentThread().setContextClassLoader(class_loader);
                        try {
                            LogFactory.releaseAll();
                            final Class cl = class_loader.loadClass(className);
                            final Method method = cl.getMethod("main", String[].class);
                            appParameters.remove(0);
                            StarterSplashScreen.hide();
                            if (restartedProcessState != null && restartedProcessState.containsKey(APP_STATE_FOR_RESTART_PROP)) {
                                appRestartState = Hex.decodeHex(restartedProcessState.remove(APP_STATE_FOR_RESTART_PROP).toCharArray());
                            }
                            method.invoke(null, (Object) appParameters.toArray(new String[0]));
                            if (restartByNewProcess) {
                                final Map<String, String> state = new HashMap<>();
                                if (Boolean.getBoolean(AUTH_PASSWORD_INTERACTIVE_PROP)) {
                                    state.put(AUTH_PASSWORD_INTERACTIVE_PROP, authPassword);
                                }
                                if (Boolean.getBoolean(SSH_KEY_PASSWORD_INTERACTIVE_PROP)) {
                                    state.put(SSH_KEY_PASSWORD_INTERACTIVE_PROP, sshKeyPwd);
                                }
                                if (appRestartState != null) {
                                    state.put(APP_STATE_FOR_RESTART_PROP, Hex.encodeHexString(appRestartState));
                                }
                                if (restartVersion != -1) {
                                    state.put(VERSION_FOR_RESTART_PROP, String.valueOf(restartVersion));
                                }
                                ProcessRestarter.launchRestarter(state, restartVersion);
                            }
                        } finally {
                            Thread.currentThread().setContextClassLoader(prevContextClassLoader);
                        }
                    } finally {
                        tricksAfterApplicationTermination(class_loader);
                    }
                }
            } finally {
                cleanup(RadixLoader.getInstance());
                SafeLogger.interrupt();
            }
        } catch (InvalidUsageException e) {
            //do not print stack
        }
    }

    public static long getAadcMemberId() {
        return aadcMemberId;
    }

    public static long[] readAadcStickedVersions() throws RadixLoaderException {
        byte[] aboutData = findAboutXml();
        if (aboutData != null && aboutData.length > 0) {
            try {
                final Document doc = LayerMeta.getDocumentBuilder().parse(new ByteArrayInputStream(aboutData));
                return new long[]{getAadcStickedVersion(1, doc), getAadcStickedVersion(2, doc)};
            } catch (Exception ex) {
                throw new RadixLoaderException("Unable to determine stick revision for aadc memeber: " + ex.getMessage(), ex);
            }
        }
        return new long[]{-1l, -1l};
    }

    private static long getAadcStickedVersion(final int aadcMemberId, final Document aboutDocument) {
        final String stickToRevision = getSimpleXpathVal(aboutDocument, "About/AadcMember" + aadcMemberId + "/@StickToRevision");
        if (stickToRevision != null) {
            return Long.parseLong(stickToRevision);
        }
        return -1;
    }

    private static byte[] findAboutXml() throws RadixLoaderException {
        for (IRepositoryEntry entry : RadixLoader.getInstance().listDir("", -1, false)) {
            if (!entry.isDirectory() && entry.getName().equals(RevisionMeta.ABOUT_XML)) {
                return RadixLoader.getInstance().export(entry.getPath(), entry.getRevisionNum());
            }
        }
        return null;
    }

    private static String getSimpleXpathVal(Node node, String path) {
        final int slashIdx = path.indexOf("/");
        final String targetName;
        if (slashIdx >= 0) {
            targetName = path.substring(0, slashIdx);
        } else {
            targetName = path;
        }
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            final Node childNode = node.getChildNodes().item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getLocalName().equals(targetName)) {
                if (slashIdx >= 0) {
                    final String subPath = path.substring(slashIdx + 1);
                    if (subPath.startsWith("@")) {
                        return childNode.getAttributes().getNamedItem(subPath.substring(1)).getNodeValue();
                    } else {
                        return getSimpleXpathVal(childNode, subPath);
                    }
                } else {
                    return childNode.getNodeValue();
                }
            }
        }
        return null;
    }

    private static String trimLastSlash(final String s) {
        if (s == null || !s.endsWith("/")) {
            return s;
        }
        return s.substring(0, s.length() - 1);
    }

    private static void tryActualizeLoader() throws RadixLoaderException {
        try {
            RadixLoader.getInstance().actualize(targetVer, null, null, null, null);
        } catch (RadixLoaderException ex) {
            if (RadixLoader.getInstance().getCurrentRevisionMeta() == null || (ex instanceof RadixLoaderPreloadException)) {
                throw ex;
            } else {
                SafeLogger.getInstance().error(Starter.class, "Unable to determine latest changes in repostory", ex);
                //actualization failed after loading cached revision, we can 
                //start anyway
            }
        }
    }

    /**
     *
     * @return "Root starter" or "App starter"
     */
    public static String getShortDesc() {
        return (isRootStarter_TrickyButGuaranteed() ? "Root" : "App") + " starter";
    }

    //dirty hacks to prevent classloader/memory  leaks
    private static void tricksAfterApplicationTermination(final ClassLoader applicationClassLoader) {
        releaseSecurityProviders(applicationClassLoader);
    }

    private static void tricksOnRootStarterInitializaton(final boolean isExplorerOnMac) {
        forceSomeStaticFieldsToReferToSystemClassloader(isExplorerOnMac);
    }

    private static void forceSomeStaticFieldsToReferToSystemClassloader(final boolean isExplorerOnMac) {
        //taken from org.apache.cxf.common.logging.JDKBugHacks
        try {
            if (!getArguments().getStarterParameters().containsKey(StarterArguments.DISABLE_SWING_PRELOAD)
                    && !isExplorerOnMac) {
                //Trigger a call to sun.awt.AppContext.getAppContext()
                ImageIO.getCacheDirectory();
            }
        } catch (Throwable t) {
            //ignore
        }
        try {
            //DocumentBuilderFactory seems to SOMETIMES pin the classloader
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.newDocumentBuilder();
        } catch (Throwable e) {
            //ignore
        }
        // Several components end up calling:
        // sun.misc.GC.requestLatency(long)
        //
        // Those libraries / components known to trigger memory leaks due to
        // eventual calls to requestLatency(long) are:
        // - javax.management.remote.rmi.RMIConnectorServer.start()
        try {
            Class<?> clazz = Class.forName("sun.misc.GC");
            Method method = clazz.getDeclaredMethod("currentLatencyTarget");
            Long l = (Long) method.invoke(null);
            if (l != null && l.longValue() == 0) {
                //something already set it, move on
                method = clazz.getDeclaredMethod("requestLatency",
                        new Class[]{Long.TYPE});
                method.invoke(null, Long.valueOf(36000000));
            }
        } catch (Throwable e) {
            //ignore
        }

        // Calling getPolicy retains a static reference to the context 
        // class loader.
        try {
            // Policy.getPolicy();
            Class<?> policyClass = Class
                    .forName("javax.security.auth.Policy");
            Method method = policyClass.getMethod("getPolicy");
            method.invoke(null);
        } catch (Throwable e) {
            // ignore
        }
        try {
            // Initializing javax.security.auth.login.Configuration retains a static reference 
            // to the context class loader.
            Class.forName("javax.security.auth.login.Configuration", true,
                    ClassLoader.getSystemClassLoader());
        } catch (Throwable e) {
            // Ignore
        }
    }

    private static void releaseSecurityProviders(final ClassLoader radixClassLoader) {
        try {
            for (Provider provider : Security.getProviders()) {
                if (provider.getClass().getClassLoader() == radixClassLoader) {
                    Security.removeProvider(provider.getName());
                }
            }
            final Class clazz = Class.forName("javax.crypto.JceSecurity");
            final Field f = clazz.getDeclaredField("verificationResults");
            f.setAccessible(true);
            final Object mapObj = f.get(null);

            mapObj.getClass().getMethod("clear").invoke(mapObj);
        } catch (Exception ex) {
//            ex.printStackTrace(System.err);
        }
    }

    private static void initializeAwtThreadWithSystemContextClassLoader() {
        try {
            //AWT thread has static final field with context classloader.
            //This field is filled during EventQueue initialization
            //and is taken from the thread that performes initialization.
            //If event queue is initialized from Starter's main thread after
            //context class loader has been set to RadixClassLoader,
            //reference to this class loader will be stored forever, which 
            //leads to memory leak.
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    //awt thread is initialized now
                }
            });
        } catch (Throwable t) {
            //skip
        }

    }

    private static String[] getArgs(final StarterArguments starterArgs) {
        final List<String> args = new ArrayList<String>();
        for (Map.Entry<String, String> entry : starterArgs.getStarterParameters().entrySet()) {
            if (entry.getValue() == null) {
                args.add("-" + entry.getKey());
            } else {
                args.add("-" + entry.getKey() + "=" + entry.getValue());
            }
        }
        for (String param : starterArgs.getAppParameters()) {
            args.add(param);
        }
        return args.toArray(new String[args.size()]);
    }

    private static void readRestartState() throws IOException {
        if (isRootStarter_TrickyButGuaranteed()) {
            restartedProcessState = ProcessRestarter.consumeRestartProcessState();
        } else {
            try {
                final Class rootStarterClass = getRootStarterClassLoader().loadClass(Starter.class.getCanonicalName());
                final Field f = rootStarterClass.getDeclaredField("restartedProcessState");
                f.setAccessible(true);
                final Object val = f.get(null);
                f.set(null, null);
                if (val instanceof Map) {
                    restartedProcessState = (Map<String, String>) val;
                }
            } catch (Exception ex) {
                throw new IOException("Unable to get restarted process state", ex);
            }
        }
        if (restartedProcessState != null && restartedProcessState.containsKey(VERSION_FOR_RESTART_PROP)) {
            targetVer = Long.parseLong(restartedProcessState.get(VERSION_FOR_RESTART_PROP));
        }
    }

    public static StarterArguments getArguments() {
        return starterArgs;
    }

    public static boolean isRoot() {
        return isRoot;
    }

    public static ClassLoader getRootStarterClassLoader() {
        if (isRoot) {
            return Starter.class.getClassLoader();
        }
        ClassLoader rootLoader = Starter.class.getClassLoader();
        while (RadixClassLoader.class.getName().equals(rootLoader.getClass().getName())) {
            rootLoader = rootLoader.getClass().getClassLoader();
        }
        return rootLoader;
    }

    private static void cleanup(final RadixLoader loader) {
        if (Thread.currentThread() != starterMainThread) {
            //Main thread is still working and it may need RadixLoader
            //for correct termination
            try {
                starterMainDoneLatch.await(1, TimeUnit.MINUTES);
            } catch (InterruptedException ex) {
                //continue cleanup
            }
        }
        if (!cleanupLaunched.getAndSet(true)) {
            try {
                if (cleanTempFilesFromPrevRunThread != null) {
                    cleanTempFilesFromPrevRunThread.interrupt();
                }
                if (loader != null) {
                    loader.close();
                }
            } finally {
                cleanupIsDoneLatch.countDown();
            }
        } else {
            try {
                //cleanup can be launched from main thread of from shutdown hook
                //thread. If shutdown hook thread was the one who started cleanup,
                //main thread should wait for cleanup to finish, otherwise control 
                //will be returned to the parent starter and parent starter may close
                //itself before this starter cleanup is finished.
                cleanupIsDoneLatch.await(1, TimeUnit.MINUTES);
            } catch (InterruptedException ex) {
                //do nothing
            }
        }
    }

    /**
     * Hook that allows main thread to finish its work when JVM is shutting down
     * by SIG_TERM.
     */
    private static void registerWaitRootStarterShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    starterMainDoneLatch.await(1, TimeUnit.MINUTES);
                } catch (InterruptedException ex) {
                    return;
                }
            }
        }));
    }

    /**
     * dirty workaround
     */
    private static boolean isExplorerOnMac(final String appClassName) {
        return (SystemTools.isOSX) && appClassName != null && appClassName.startsWith("org.radixware.kernel.explorer");
    }

    /**
     * Add shutdown hook for application launched by starter. App shutdown hooks
     * are executed before starter shutdown hooks
     *
     * @param runnable
     * @throws IllegalStateException if shutdown is in progress
     */
    public static void addAppShutdownHook(final Runnable runnable) throws IllegalStateException {
        ShutdownHooks.addAppHook(runnable);
    }

    /**
     * Remove shutdown hook for application launched by starter. App shutdown
     * hooks are executed before starter shutdown hooks
     *
     * @param runnable
     * @throws IllegalStateException if shutdown is in progress
     */
    public static void removeAppShutdownHook(final Runnable runnable) throws IllegalStateException {
        ShutdownHooks.removeAppHook(runnable);
    }

    /**
     * Add shutdown hook for starter itself. Starter hooks are executed after
     * app hooks.
     *
     * @param runnable
     * @throws IllegalStateException if shutdown is in progress
     */
    public static void addStarterShutdownHook(final Runnable runnable) throws IllegalStateException {
        ShutdownHooks.addStarterHook(runnable);
    }

    /**
     * Remove shutdown hook for starter itself. Starter hooks are executed after
     * app hooks.
     *
     * @param runnable
     * @throws IllegalStateException if shutdown is in progress
     */
    public static void removeStarterShutdownHook(final Runnable runnable) throws IllegalStateException {
        ShutdownHooks.removeStarterHook(runnable);
    }

    /**
     * for child starter
     */
    static void countDownHooksLatch() {
        childHooksLatch.countDown();
    }

    /**
     * for child starter
     */
    static void awaitChildHooks() {
        ShutdownHooks.awaitChildHooks();
    }

    public static void mustRestart(final String[] restartParameters) {
        Starter.restartParameters = restartParameters;
    }

    /**
     *
     * @param appState
     * @param targetVersion - version to switch after restart, -1 - newest.
     */
    public static void mustRestartByNewProcess(final byte[] appState, final long targetVersion) {
        appRestartState = appState;
        restartByNewProcess = true;
        restartVersion = targetVersion;
    }

    public static synchronized byte[] consumeAppRestartState() {
        if (appRestartState != null) {
            final byte[] b = new byte[appRestartState.length];
            System.arraycopy(appRestartState, 0, b, 0, appRestartState.length);
            Arrays.fill(appRestartState, (byte) 0);
            appRestartState = null;
            return b;
        }
        return null;
    }

    public static String[] getRestartParameters() {
        return restartParameters;
    }

    private static class ShutdownHooks {

        private static final List<Runnable> appHooks = new ArrayList<>();
        private static final List<Runnable> starterHooks = new ArrayList<>();
        private static volatile Thread jvmShutdownHook = null;
        //guarded by ShutdownHooks.class
        private static boolean shutdownStarted = false;
        private static volatile boolean awaitChildHooks = false;

        public static void init() {
            final Runnable r = new Runnable() {
                public void run() {
                    if (isRoot && awaitChildHooks) {
                        try {
                            childHooksLatch.await(1, TimeUnit.MINUTES);
                        } catch (InterruptedException ex) {
                            //do nothing
                        }
                    }
                    synchronized (ShutdownHooks.class) {
                        shutdownStarted = true;
                    }
                    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
                    final PrintStream ps = new PrintStream(outputStream);
                    runHooks(appHooks, ps);
                    runHooks(starterHooks, ps);

                    if (!isRoot) {
                        notifyParentStarter();
                    }

                    if (outputStream.size() > 0) {
                        logShutdownErrors(outputStream.toString());
                    }
                }
            };
            jvmShutdownHook = new Thread(r, (isRoot ? "root" : "app") + "-starter-shutdown-hook");
            Runtime.getRuntime().addShutdownHook(jvmShutdownHook);
            if (!isRoot) {
                askParentStarterToAwaitChildHooks();
            }
        }

        public static synchronized void discard() {
            if (!shutdownStarted) {
                appHooks.clear();
                starterHooks.clear();
                try {
                    if (jvmShutdownHook != null) {
                        Runtime.getRuntime().removeShutdownHook(jvmShutdownHook);
                        jvmShutdownHook = null;
                    }
                } catch (IllegalStateException ex) {
                    //shutdown is already started, continue
                }
            }
        }

        public static void awaitChildHooks() {
            awaitChildHooks = true;
        }

        private static void askParentStarterToAwaitChildHooks() {
            final ClassLoader classLoader = getRootStarterClassLoader();
            try {
                final Class starterClass = classLoader.loadClass(Starter.class.getName());
                try {
                    try {
                        final Method m = starterClass.getDeclaredMethod("awaitChildHooks");
                        m.setAccessible(true);
                        m.invoke(null);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        //do nothing
                    }
                } catch (NoSuchMethodException | SecurityException ex) {
                    //do nothing
                }
            } catch (ClassNotFoundException ex) {
                //do nothing
            }
        }

        private static void notifyParentStarter() {
            final ClassLoader classLoader = getRootStarterClassLoader();
            try {
                final Class starterClass = classLoader.loadClass(Starter.class.getName());
                try {
                    try {
                        final Method m = starterClass.getDeclaredMethod("countDownHooksLatch");
                        m.setAccessible(true);
                        m.invoke(null);
                    } catch (IllegalAccessException ex) {
                        //do nothing
                    } catch (IllegalArgumentException ex) {
                        //do nothing
                    } catch (InvocationTargetException ex) {
                        //do nothing
                    }
                } catch (NoSuchMethodException ex) {
                    //do nothing
                } catch (SecurityException ex) {
                    //do nothing
                }
            } catch (ClassNotFoundException ex) {
                //do nothing
            }
        }

        private static void logShutdownErrors(final String errors) {
            final Date date = new Date();
            final File f = new File((isRoot ? "root" : "app") + "-starter-shutdown-log-" + SHUTDOWN_LOG_DATE_FORMAT.format(date));
            OutputStream outputStream = null;
            boolean shouldCloseStream;
            try {
                outputStream = new BufferedOutputStream(new FileOutputStream(f));
                shouldCloseStream = true;
            } catch (FileNotFoundException ex) {
                outputStream = System.err;
                shouldCloseStream = false;
            }
            try {
                outputStream.write(errors.getBytes());
            } catch (IOException ex) {
                //do nothing
            } finally {
                try {
                    if (shouldCloseStream) {
                        outputStream.close();
                    }
                } catch (IOException ex) {
                    //do noting
                }
            }
        }

        private static void runHooks(final List<Runnable> hooks, final PrintStream logStream) {
            for (Runnable hook : hooks) {
                try {
                    hook.run();
                } catch (Throwable t) {
                    t.printStackTrace(logStream);
                    logStream.println();
                }
            }
        }

        public static synchronized void addAppHook(final Runnable runnable) {
            addHook(runnable, appHooks);
        }

        public static synchronized void addStarterHook(final Runnable runnable) {
            addHook(runnable, starterHooks);
        }

        public static synchronized void removeAppHook(final Runnable runnable) {
            removeHook(runnable, appHooks);
        }

        public static synchronized void removeStarterHook(final Runnable runnable) {
            removeHook(runnable, starterHooks);
        }

        private static void addHook(final Runnable hook, final List<Runnable> list) {
            if (shutdownStarted) {
                throw new IllegalStateException("Shutdown is already started");
            }
            list.add(hook);
        }

        private static void removeHook(final Runnable hook, final List<Runnable> hooks) {
            if (shutdownStarted) {
                throw new IllegalStateException("Shutdown is already started");
            }
            if (!hooks.remove(hook)) {
                throw new IllegalArgumentException("The hook is not registered");
            }

        }
    }
}
