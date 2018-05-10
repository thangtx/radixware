/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.starter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import org.apache.commons.codec.binary.Hex;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.utils.FileUtils;
import org.radixware.kernel.starter.utils.SystemTools;

public class ProcessRestarter {

    private static final String RESTARTER_STATE_PREFIX = "rdx.restarter.";
    //
    private static final String DEBUG_PROP = RESTARTER_STATE_PREFIX + "debug";
    private static final boolean DEBUG = Boolean.getBoolean(DEBUG_PROP);
    //
    private static final String JVM_ARGS = RESTARTER_STATE_PREFIX + "jvm.args";
    private static final String TEMP_FILES = RESTARTER_STATE_PREFIX + "temp.files";
    private static final String TARGET_STARTER_JAR = RESTARTER_STATE_PREFIX + "target.starter.jar";
    private static final String NEW_STARTER_JAR = RESTARTER_STATE_PREFIX + "new.starter.jar";
    private static final String PREV_PROCESS_PID = RESTARTER_STATE_PREFIX + "prev.process.pid";
    private static final String TEMP_STARTER_FILE_NAME_PREFIX = "starter-upgrader";
    private static final String NEW_STARTER_FILE_NAME_PREFIX = "starter-new";
    private static final String IS_RESTART_BY_NEW_PROCESS = "rdx.is.restart.by.new.process";
    private static final String GET_STATE_PORT_PROP = RESTARTER_STATE_PREFIX + "get.state.port";
    private static final byte STATE_CONSUMED_MARKER = '\n';
    private static volatile String LOG_PREFIX = "Starter";

    public static void main(String[] args) throws Exception {
        LOG_PREFIX = "Upgrader";
        debug("Started");
        Map<String, String> state = new HashMap<>(readState());

        int prevPid = Integer.parseInt(state.get(PREV_PROCESS_PID));
        if (prevPid >= 0) {
            boolean finished = false;
            final int maxWaitSeconds = 10;
            for (int i = 0; i < maxWaitSeconds; i++) {
                if (!SystemTools.getRunningProcessPids().contains(prevPid)) {
                    finished = true;
                    break;
                }
                Thread.sleep(1000);
            }
            if (!finished) {
                sout("It seems that previous process (pid=" + prevPid + ") is unable to stop, please kill it and launch again manually");
                return;
            }
        } else {
            Thread.sleep(5000);
        }

        final File newStarter = new File(state.get(NEW_STARTER_JAR));
        final File targetStarter = new File(state.get(TARGET_STARTER_JAR));

        Files.move(newStarter.toPath(), targetStarter.toPath(), StandardCopyOption.REPLACE_EXISTING);
        sout("'" + targetStarter.getAbsolutePath() + "' has been upgraded");

        final String[] jvmArgs = state.get(JVM_ARGS).split("\\n");

        final Iterator<String> it = state.keySet().iterator();
        while (it.hasNext()) {
            if (it.next().startsWith(RESTARTER_STATE_PREFIX)) {
                it.remove();
            }
        }
        launchJvm(jvmArgs, state);
    }

    private static String[] concat(String[] a, String... b) {
        final String[] res = new String[a.length + b.length];
        System.arraycopy(a, 0, res, 0, a.length);
        System.arraycopy(b, 0, res, a.length, b.length);
        return res;
    }

    public static Map<String, String> consumeRestartProcessState() throws IOException {
        if (System.getProperties().containsKey(IS_RESTART_BY_NEW_PROCESS)) {
            System.getProperties().remove(IS_RESTART_BY_NEW_PROCESS);
            final Map<String, String> state = new HashMap<>(readState());
            cleanupTempFiles(state);
            return state;
        }
        return null;
    }

    private static Map<String, String> readState() throws IOException {
        try (final Socket s = SocketFactory.getDefault().createSocket()) {
            s.setSoTimeout(5000);
            s.setTcpNoDelay(true);
            final InetSocketAddress address = new InetSocketAddress("127.0.0.1", Integer.parseInt(System.getProperty(GET_STATE_PORT_PROP)));
            debug("Connecting to " + address + "...");
            s.connect(address);
            debug("Connected to " + address);
            final ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            try {
                Map<String, String> map = (Map<String, String>) ois.readObject();
                debug("State has been read");
                s.getOutputStream().write(STATE_CONSUMED_MARKER);
                s.getOutputStream().flush();
                debug("Confirmation has been written");
                return map;
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void cleanupTempFiles(final Map<String, String> state) {
        if (state != null && state.containsKey(TEMP_FILES)) {
            final String tempFilesStr = state.get(TEMP_FILES);
            if (tempFilesStr != null) {
                for (final String fileName : tempFilesStr.split("\\n")) {
                    if (FileUtils.deleteFile(new File(fileName))) {
                        debug("Deleted '" + fileName + "'");
                    }
                }
            }
        }
    }

    public static void launchRestarter(Map<String, String> state, final long starterVersion) throws Exception {
        final Map<String, String> map = new HashMap<>();

        final File tmpDir = SystemTools.getTmpDir();
        final byte[] starterData = RadixLoader.getInstance().export("org.radixware/kernel/starter/bin/dist/starter.jar", RadixLoader.getInstance().getCurrentRevision());
        final File tmpStarter = new File(tmpDir, TEMP_STARTER_FILE_NAME_PREFIX + "-" + UUID.randomUUID().toString() + ".jar");
        Files.write(tmpStarter.toPath(), starterData);

        final byte[] newStarterData = RadixLoader.getInstance().export("org.radixware/kernel/starter/bin/dist/starter.jar", starterVersion == -1 ? RadixLoader.getInstance().getLatestRevision() : starterVersion);
        final File newStarter = new File(tmpDir, NEW_STARTER_FILE_NAME_PREFIX + "-" + UUID.randomUUID().toString() + ".jar");
        Files.write(newStarter.toPath(), newStarterData);
        map.put(NEW_STARTER_JAR, newStarter.getAbsolutePath());

        final List<String> args = new ArrayList<>(ManagementFactory.getRuntimeMXBean().getInputArguments());

        final String isRestartByNewProcessProp = "-D" + IS_RESTART_BY_NEW_PROCESS;
        if (!args.contains(isRestartByNewProcessProp)) {
            args.add(isRestartByNewProcessProp);
        }

        args.add("-cp");
        args.add(System.getProperty("java.class.path"));
        args.add(Starter.class.getCanonicalName());

        args.addAll(Arrays.asList(getRootStarterArgs()));
        final String argsStr = arrToStr(args.toArray(new String[args.size()]));
        map.put(JVM_ARGS, argsStr);
        map.put(TEMP_FILES, arrToStr(new String[]{tmpStarter.getAbsolutePath(), newStarter.getAbsolutePath()}));

        try {
            final Class rootStarterClass = Starter.getRootStarterClassLoader().loadClass(Starter.class.getCanonicalName());
            final File currentStarterJar = new File(rootStarterClass.getProtectionDomain().getCodeSource().getLocation().toURI());
            map.put(TARGET_STARTER_JAR, currentStarterJar.getAbsolutePath());
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

        map.put(PREV_PROCESS_PID, String.valueOf(SystemTools.getCurrentProcessPid()));

        if (state != null) {
            map.putAll(state);
        }
        launchJvm(new String[]{"-D" + DEBUG_PROP + "=" + (DEBUG ? "true" : "false"), "-cp", tmpStarter.getAbsolutePath(), ProcessRestarter.class.getCanonicalName()}, map);
    }

    private static String[] getRootStarterArgs() {
        try {
            final Class c = Starter.getRootStarterClassLoader().loadClass(Starter.class.getCanonicalName());
            final Field f = c.getDeclaredField("mainArgs");
            f.setAccessible(true);
            return (String[]) f.get(null);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String arrToStr(final String[] arr) {
        final StringBuilder sb = new StringBuilder();
        if (arr != null) {
            for (String s : arr) {
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append(s);
            }
        }
        return sb.toString();
    }

    private static void launchJvm(final String[] commandLineArgs, final Map<String, String> state) throws Exception {
        final String javaCmd = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        final List<String> commandAndArgs = new ArrayList<>();
        commandAndArgs.add(javaCmd);
        commandAndArgs.addAll(Arrays.asList(commandLineArgs));

        try (StateTransferServer server = new StateTransferServer()) {

            for (int i = 0; i < commandAndArgs.size(); i++) {
                if (commandAndArgs.get(i).contains(GET_STATE_PORT_PROP)) {
                    commandAndArgs.remove(i);
                    break;
                }
            }

            commandAndArgs.add(1, "-D" + GET_STATE_PORT_PROP + "=" + server.getPort());

            debug("Starting '" + commandAndArgs + "'");

            final ProcessBuilder pb = new ProcessBuilder(commandAndArgs);
            pb.inheritIO();
            final Process p = pb.start();

            debug("Child process started");

            if (!server.awaitTransfer(state, 30)) {
                throw new IOException("Confirmation of successful start of child process was not received in time");
            }
        }
    }

    private static void debug(final String mess) {
        if (DEBUG) {
            sout("(debug) " + mess);
        }
    }

    private static void sout(final String mess) {
        System.out.println(new Date() + ": " + LOG_PREFIX + ": " + mess);
    }

    private static class StateTransferServer implements AutoCloseable {

        private final ServerSocket serverSocket;

        public StateTransferServer() throws IOException {
            serverSocket = ServerSocketFactory.getDefault().createServerSocket();
            serverSocket.bind(new InetSocketAddress("127.0.0.1", 0));
        }

        public int getPort() {
            return serverSocket.getLocalPort();
        }

        public boolean awaitTransfer(final Map<String, String> state, int timeoutSeconds) throws InterruptedException, SocketException, IOException {
            serverSocket.setSoTimeout(timeoutSeconds * 1000);
            try (final Socket s = serverSocket.accept()) {
                s.setSoTimeout(timeoutSeconds * 1000);
                s.setTcpNoDelay(true);
                final InputStream socketInputStream = s.getInputStream();
                final HashMap<String, String> map = state == null ? new HashMap<String, String>() : new HashMap<>(state);
                try (ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream())) {
                    oos.writeObject(map);
                    oos.flush();
                    debug("State has been written");
                    try {
                        int c = socketInputStream.read();
                        if (c != STATE_CONSUMED_MARKER) {
                            throw new IllegalStateException("Expected marker " + Hex.encodeHexString(new byte[]{STATE_CONSUMED_MARKER}) + ", got " + Hex.encodeHexString(new byte[]{(byte) c}));
                        }
                        debug("State transfer confirmation has been received");
                    } catch (Exception ex) {
                        debug("Error: '" + ex.getMessage() + "' while waiting for transfer confirmation");
                    }
                }
            }
            return true;
        }

        @Override
        public void close() throws Exception {
            serverSocket.close();
        }

    }

}
