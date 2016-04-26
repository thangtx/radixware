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
package org.radixware.kernel.utils.nblauncher;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.eas.AbstractEasSession;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.utils.FileUtils;
import org.radixware.kernel.starter.utils.SystemTools;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.starter.StarterArguments;
import sun.misc.BASE64Encoder;

public class NbLauncher {

    public static class ApplicationConfig {

        public static final String EAS_SESSION = "eas-session";
        public static final String FEATURES = "features";
        public static final String TIMEOUT = "timeout";
        private static ApplicationConfig instance;
        private Map<String, Object> properties = new HashMap<>();
        private final String appName;
        private boolean await = false;

        public ApplicationConfig(String appName) {
            this.appName = appName;
        }

        public ApplicationConfig(String[] args) {
            if (args.length == 0) {
                throw new IllegalStateException("Application name parameter expected");
            }
            this.appName = args[0];
            if (args.length > 1) {
                for (int i = 1; i < args.length; i++) {
                    if ("--await".equals(args[i])) {
                        this.await = true;
                    } else {
                        throw new IllegalStateException("Unexpected parameter name: " + args[i]);
                    }
                }
            }
        }

        public static ApplicationConfig getInstance() {
            return instance;
        }

        private static void setInstance(ApplicationConfig instance) {
            ApplicationConfig.instance = instance;
        }

        public void putValue(String key, Object value) {
            properties.put(key, value);
        }

        public Object getValue(String key) {
            return properties.get(key);
        }
    }

    private static boolean deleteDirectory(File path) {
        if (path != null && path.exists()) {
            final String[] names = path.list();
            if (names != null) {
                for (int i = 0; i < names.length; i++) {
                    File file = new File(path, names[i]);
                    if (file.isDirectory()) {
                        if (!deleteDirectory(file)) {
                            return false;
                        }
                    } else {
                        if (!deleteFile(file)) {
                            return false;
                        }
                    }
                }
            }
            return deleteFileImpl(path);
        } else {
            return false;
        }
    }

    private static boolean deleteFile(File file) {
        if (file.isDirectory()) {
            return deleteDirectory(file);
        }
        return deleteFileImpl(file);
    }

    private static boolean deleteFileImpl(File file) {
        int counter = 0;
        while (counter < 10) {
            if (file.delete()) {
                return true;
            }
            if (!file.exists()) {
                return true;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
            counter++;

        }
        return false;
    }

    public static void main(String[] args) {
        try {

            File lockFileDir = new File(SystemTools.getApplicationDataPath("radixware.org"), "report-editor");

            File varDir = new File(lockFileDir, "var");
            if (varDir.isDirectory()) {
                deleteDirectory(varDir);
            }
            File configDir = new File(lockFileDir, "config");
            if (configDir.isDirectory()) {
                deleteDirectory(configDir);
            }

            File lockFile = new File(lockFileDir, "ts_lock");

            if (lockFile.exists()) {
                FileUtils.deleteFile(lockFile);
            }

            RunParams.initialize(args);

            String feature = null;
            boolean start = false;
            for (String arg : args) {
                if (arg.startsWith("-feature=")) {
                    int index = arg.indexOf("=");
                    feature = arg.substring(index + 1);
                } else if (arg.equals("-start")) {
                    start = true;
                }
            }

            ApplicationConfig config = new ApplicationConfig("reporteditor");
            if (feature != null) {
                config.putValue(ApplicationConfig.FEATURES, feature);
            }
            if (!start) {
                //config.await = true;
                try {
                    InputStreamReader reader = new InputStreamReader(System.in);
                    char[] cbuf = new char[1024];
                    int count;
                    StringBuilder pipeInput = new StringBuilder();
                    while ((count = reader.read(cbuf)) >= 0) {
                        pipeInput.append(cbuf, 0, count);
                    }

                    String in = pipeInput.toString();
                    if (in.startsWith("--") && in.endsWith("--") && in.length() > 4) {
                        String secret = in.substring(2, in.length() - 2);
                        config.putValue("epwd", secret.toCharArray());
                    }
                    pipeInput = null;
                    in = null;
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error report is saved to " + writeErrorReport(lockFileDir, e), "UDS Designer Launch Failure", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            config.await = true;
            config.putValue("timeout", 3000);
            launchNbApplicationImpl(config);
        } catch (Throwable e) {
            File lockFileDir = new File(SystemTools.getApplicationDataPath("radixware.org"), "report-editor");
            JOptionPane.showMessageDialog(null, "Error report is saved to " + writeErrorReport(lockFileDir, e), "UDS Designer Launch Failure", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static File writeErrorReport(File lockFileDir, Throwable error) {
        String fileName = SimpleDateFormat.getDateInstance().format(new Date()) + ".log";
        File reportFile = new File(lockFileDir, fileName);
        try {
            FileWriter writer = new FileWriter(reportFile, true); 
            try {
                String time = SimpleDateFormat.getTimeInstance().format(new Date());
                writer.append("\n------------------------");
                writer.append(time);
                writer.append("------------------------\n");
                Throwable e = error;
                while (e != null) {
                    writer.append(e.getClass().getName() + "\n");
                    if (e.getMessage() != null) {
                        writer.append(e.getMessage() + "\n");
                    }
                    for (int i = 0; i < e.getStackTrace().length; i++) {
                        writer.append("    " + e.getStackTrace()[i].toString() + "\n");
                    }
                    e = e.getCause();
                    if (e != null) {
                        writer.append("Caused by: ");
                    }
                }
            } finally {
                writer.flush();
                writer.close();
            }
        } catch (IOException ex) {
        }
        return reportFile;
    }
    private static final Lock launchLock = new ReentrantLock();
    private static final Map<String, Object> runnungApps = new HashMap<>();

    private static class AsyncStreamReader implements Runnable {

        private InputStream in;
        private ClientTracer tracer;
        private EEventSeverity severity;

        public AsyncStreamReader(InputStream in, ClientTracer tracer, EEventSeverity severity) {
            this.in = in;
            this.tracer = tracer;
            this.severity = severity;
        }

        @Override
        public void run() {

            InputStreamReader reader = new InputStreamReader(in);
            try {
                char[] cbuf = new char[1024];
                for (;;) {
                    try {
                        int count = reader.read(cbuf);
                        if (count < 0) {
                            break;
                        }
                        String string = new String(cbuf, 0, count);
                        tracer.put(severity, string, "Client.Designer");
                    } catch (IOException ex) {
                        break;
                    }
                }
            } finally {
                try {
                    reader.close();
                } catch (IOException ex) {
                }
            }
        }
    }
    private static final boolean debug = false;

    public static boolean launchNbApplication(final ApplicationConfig config, final ClientTracer tracer) {
        StarterArguments starterArgs = Starter.getStarterArguments();
        Map<String, String> params = starterArgs.getStarterParameters();
        String[] args = RunParams.getArgs();
        final String java_home = System.getProperty("java.home");
        final String excutable = java_home + "/bin/java";
        List<String> allArgs = new LinkedList<>();

        allArgs.add(excutable);
        if (debug) {
            allArgs.add("-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=9009");
        }

        String heapSize = RunParams.getExtDesignerMaxHeapSize();
        if (heapSize == null) {
            boolean memSizeComputed = false;
            long totalMemory = 0;
            try {
                com.sun.management.OperatingSystemMXBean bean
                        = (com.sun.management.OperatingSystemMXBean) java.lang.management.ManagementFactory.getOperatingSystemMXBean();
                totalMemory = bean.getTotalPhysicalMemorySize();
                memSizeComputed = true;
            } catch (Throwable e) {
            }

            if (!memSizeComputed) {
                totalMemory = Runtime.getRuntime().totalMemory();
            }
            long totalMemoryMByte = totalMemory / 1024 / 1024;
            long wantedMemSize = Math.round(totalMemoryMByte * 0.35);
            if (wantedMemSize > 4096) {
                wantedMemSize = 4096;
            }
            if (wantedMemSize < 800) {
                wantedMemSize = 800;
            }
            heapSize = String.valueOf(wantedMemSize) + "m";
            Logger.getLogger(NbLauncher.class.getName()).log(Level.FINE, "Calculated mem size" + heapSize);
        }

        String permGenSize = RunParams.getExtDesignerMaxPermSize();
        if (permGenSize == null) {
            permGenSize = "200m";
        }
        allArgs.add("-Xmx" + heapSize);
        allArgs.add("-XX:MaxPermSize=" + permGenSize);
        allArgs.add("-jar");

        String classPath = System.getProperty("java.class.path");
        if (classPath != null) {
            String[] components = classPath.split(File.pathSeparator);
            for (String c : components) {
                if (c.contains("starter.jar")) {
                    classPath = c;
                    break;
                }
            }
        }
        allArgs.add(classPath);

        for (Map.Entry<String, String> e : params.entrySet()) {
            if (e.getValue() == null) {
                allArgs.add("-" + e.getKey());
                continue;
            }
            allArgs.add("-" + e.getKey() + "=" + e.getValue());
        }

        allArgs.add("-devTools");
        allArgs.add("org.radixware.kernel.utils.nblauncher.NbLauncher");
        allArgs.add("reporteditor");

        final String featuresStr;
        if (config.getValue(ApplicationConfig.FEATURES) != null) {
            allArgs.add("-feature=" + config.getValue(ApplicationConfig.FEATURES));
            featuresStr = String.valueOf(config.getValue(ApplicationConfig.FEATURES));
        } else {
            featuresStr = "NO_FEATURE";
        }
        Object easSession = config.getValue(ApplicationConfig.EAS_SESSION);
        byte[] bytes = null;
        if (easSession instanceof AbstractEasSession) {
            try {
                Field f = AbstractEasSession.class.getDeclaredField("secretStore");
                if (f != null) {
                    f.setAccessible(true);
                    try {
                        Object obj = f.get(easSession);

                        if (obj != null) {
                            Method getter = obj.getClass().getDeclaredMethod("getSecret", new Class[0]);
                            if (getter != null) {
                                try {
                                    getter.setAccessible(true);
                                    bytes = (byte[]) getter.invoke(obj, new Object[0]);
                                } finally {
                                    getter.setAccessible(false);
                                }
                            }
                        }
                    } finally {
                        f.setAccessible(false);
                    }

                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        allArgs.addAll(Arrays.asList(args));

        final ProcessBuilder pb = new ProcessBuilder(allArgs);
        final byte[] data = bytes;
        final CountDownLatch startLock = new CountDownLatch(1);
        final boolean[] started = new boolean[]{false};
        final boolean[] already_started = new boolean[]{false};
        Thread processThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    launchLock.lock();
                    Process process = null;
                    boolean startAccepted = false;
                    try {
                        if (runnungApps.containsKey(featuresStr)) {
                            already_started[0] = true;
                            return;
                        }
                        pb.redirectErrorStream(true);
                        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                        File lockFileDir = new File(SystemTools.getApplicationDataPath("radixware.org"), "report-editor");
                        lockFileDir.mkdirs();
                        if (!lockFileDir.exists()) {
                            return;
                        }

                        File lockFile = new File(lockFileDir, "ts_lock");
                        RandomAccessFile fs = new RandomAccessFile(lockFile, "rw");
                        fs.write(0);
                        fs.close();

                        process = pb.start();

                        final long awaitTime = debug ? 120000 : 60000;
                        long cs = System.currentTimeMillis();
                        while (true) {
                            if (lockFile.exists()) {
                                Thread.sleep(100);
                                if (System.currentTimeMillis() - cs > awaitTime) {
                                    FileUtils.deleteFile(lockFile);
                                    return;
                                }
                                continue;
                            } else {
                                startAccepted = true;
                                break;
                            }
                        }

                        try (OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream())) {

                            if (data != null) {
                                BASE64Encoder encoder = new BASE64Encoder();
                                String encodedStirng = encoder.encode(data);
                                writer.write("--");
                                writer.write(encodedStirng);
                                writer.write("--");
                                encodedStirng = null;
                            } else {
                                writer.write("start");
                            }
                            writer.flush();
                        }

                    } finally {

                        started[0] = startAccepted;
                        startLock.countDown();
                        if (process != null && startAccepted) {
                            runnungApps.put(featuresStr, null);
                        } else {
                        }
                        launchLock.unlock();

                    }
                    if (process != null && startAccepted) {
                        try {
                            int exitCode = process.waitFor();
                        } finally {
                            runnungApps.remove(featuresStr);
                        }
                    }

                } catch (IOException ex) {
                    Logger.getLogger(NbLauncher.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Throwable e) {
                    Logger.getLogger(NbLauncher.class.getName()).log(Level.SEVERE, null, e);
                } finally {
                    if (!started[0]) {
                        startLock.countDown();
                    }

                }
            }
        });
        processThread.setDaemon(true);
        processThread.start();
        try {
            startLock.await();
        } catch (InterruptedException ex) {
        }
        if (already_started[0]) {
            throw new IllegalStateException("STARTED");
        }
        return started[0];

//            try {
//                process.waitFor();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(NbLauncher.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            InputStream s = process.getInputStream();
//            InputStreamReader reader = new InputStreamReader(s);
//            char[] buf = new char[100];
//            int count;
//            while ((count = reader.read(buf)) > 0) {
//                System.out.print(buf);
//            }
    }

//    private static URLStreamHandlerFactory getCurrentURLFactory() {
//        try {
//            Field f = URL.class.getDeclaredField("factory");
//            f.setAccessible(true);
//            return (URLStreamHandlerFactory) f.get(null);
//        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
//            Logger.getLogger(NbLauncher.class.getName()).log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//
//    private static void setCurrentURLFactory(URLStreamHandlerFactory factory) {
//        try {
//            Field f = URL.class.getDeclaredField("factory");
//            f.setAccessible(true);
//            f.set(null, null);
//            URL.setURLStreamHandlerFactory(factory);
//        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
//            Logger.getLogger(NbLauncher.class.getName()).log(Level.SEVERE, null, ex);
//
//        }
//    }
//    private static void launchNbApplicationImpl(ApplicationConfig config) {
//
//        if (config == null) {
//            throw new IllegalStateException("invalid-config");
//        }
//
//        if (ApplicationConfig.getInstance() == null) {
//            try {
//                ApplicationConfig.setInstance(config);
//                try {
//                    final CountDownLatch holdAction;
//                    if (config.await) {
//                        holdAction = new CountDownLatch(1);
//                    } else {
//                        holdAction = null;
//                    }
//
//                    Object app = config.getValue("application");
//                    ClassLoader cl = Thread.currentThread().getContextClassLoader();
//                    AdsClassLoader adsl = null;
//
//                    final URLStreamHandlerFactory urlFactory = getCurrentURLFactory();
//
//                    if (app instanceof IClientApplication) {
//
//                        try {
//                            Set<String> groups = new HashSet<>();
//                            /*
//                             * groups.add("KernelCommon");
//                             * groups.add("KernelClient");
//                             * groups.add("KernelExplorer");
//                             * groups.add("KernelWeb");
//                             * groups.add("KernelOther");
//                             * groups.add("KernelServer");
//                             */
//                            groups.add("KernelDesigner");
//
//
//                            cl = RadixLoader.getInstance().createClassLoader(new AdsClassLoader.Factory((IClientApplication) app), groups, app.getClass().getClassLoader());
//                            adsl = (AdsClassLoader) cl;
//                        } catch (IOException ex) {
//                        }
//                    } else {
//                        adsl = null;
//                    }
//
//                    final AdsClassLoader close = adsl;
//
//                    Enumeration e = System.getProperties().propertyNames();
//                    while (e.hasMoreElements()) {
//                        System.out.println(e.nextElement());
//                    }
//
//                    Thread.currentThread().setContextClassLoader(cl);
//                    //Branding setup
//                    Class c = cl.loadClass("org.openide.util.NbBundle");
//
//                    c.getMethod("setBranding", new Class[]{String.class}).invoke(null, config.appName);
//                    System.setProperty("netbeans.close.no.exit", "true");
//                    //startup actions
//                    System.setProperty("netbeans.user", new File(SystemTools.getApplicationDataPath("radixware.org"), "report-editor").getAbsolutePath());
//                    c = cl.loadClass("org.netbeans.core.startup.Main");
//                    String[] nbargs = new String[]{};
//                    final Object killer[] = new Object[1];
//                    if (config.await) {
//                        config.putValue("org.radixware.devtool.holder", new Runnable() {
//
//                            @Override
//                            public void run() {
//                                try {
//                                    holdAction.countDown();
//                                    ApplicationConfig.setInstance(null);
//                                } finally {
//                                    setCurrentURLFactory(urlFactory);
//                                }
//                            }
//                        });
//                    } else {
//                        config.putValue("org.radixware.devtool.holder", new Runnable() {
//
//                            @Override
//                            public void run() {
//                                Thread killerThread = new Thread(new Runnable() {
//
//                                    @Override
//                                    public void run() {
//                                        try {
//                                            if (close != null) {
//                                                close.close();
//                                            }
//                                            ApplicationConfig.setInstance(null);
//                                        } finally {
//
//
//                                            Frame[] frames = Frame.getFrames();
//                                            for (Frame f : frames) {
//                                                f.dispose();
//                                            }
//
//                                            setCurrentURLFactory(urlFactory);
//                                            if (killer[0] instanceof Runnable) {
//                                                ((Runnable) killer[0]).run();
//                                            }
//                                        }
//                                    }
//                                });
//                                killerThread.setDaemon(true);
//                                killerThread.start();
//                            }
//                        });
//                    }
//                    //URL.setURLStreamHandlerFactory(null)
//
//
//
//
//                    c.getMethod("main", String[].class).invoke(null, new Object[]{nbargs});
//
//                    killer[0] = config.getValue("org.radixware.devtool.killer");
//
//
//                    if (!(killer[0] instanceof Runnable)) {
//                        throw new IllegalStateException("start-failed");
//                    }
//
//                    if (config.await) {
//                        try {
//                            holdAction.await();
//                        } catch (InterruptedException ex) {
//                            Logger.getLogger(NbLauncher.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//
//
//
//                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException ex) {
//                    Logger.getLogger(NbLauncher.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            } finally {
//                //ApplicationConfig.setInstance(null);
//            }
//        } else {
//            throw new IllegalStateException("running");
//        }
//
//    }
    private static void launchNbApplicationImpl(ApplicationConfig config) {

        if (config == null) {
            throw new IllegalStateException("invalid-config");
        }

        if (ApplicationConfig.getInstance() == null) {
            try {
                ApplicationConfig.setInstance(config);
                try {
                    final CountDownLatch holdAction;
                    if (config.await) {
                        holdAction = new CountDownLatch(1);
                    } else {
                        holdAction = null;
                    }

                    //   Thread.currentThread().setContextClassLoader(NbLauncher.class.getClassLoader());
                    //Branding setup
                    ClassLoader cl = Thread.currentThread().getContextClassLoader();
                    Class c = cl.loadClass("org.openide.util.NbBundle");

                    c.getMethod("setBranding", new Class[]{String.class}).invoke(null, config.appName);
                    //System.setProperty("netbeans.close.no.exit", "true");
                    //startup actions
                    System.setProperty("netbeans.user", new File(SystemTools.getApplicationDataPath("radixware.org"), "report-editor").getAbsolutePath());
                    c = cl.loadClass("org.netbeans.core.startup.Main");
                    Locale.setDefault(new Locale("en", "US"));
                    String[] nbargs = new String[]{};
                    final Object killer[] = new Object[1];
                    if (config.await) {
                        config.putValue("org.radixware.devtool.holder", new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    holdAction.countDown();
                                    ApplicationConfig.setInstance(null);
                                } finally {
                                }
                            }
                        });
                    }
                    c.getMethod("main", String[].class).invoke(null, new Object[]{nbargs});

                    killer[0] = config.getValue("org.radixware.devtool.killer");

                    if (!(killer[0] instanceof Runnable)) {
                        throw new IllegalStateException("start-failed");
                    }

                    if (config.await) {
                        try {
                            holdAction.await();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(NbLauncher.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException ex) {
                    Logger.getLogger(NbLauncher.class.getName()).log(Level.SEVERE, null, ex);
                }
            } finally {
                //ApplicationConfig.setInstance(null);
            }
        } else {
            throw new IllegalStateException("running");
        }

    }
}
