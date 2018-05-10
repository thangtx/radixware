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

package org.radixware.ws.servlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.utils.FileUtils;


public class WPSBridge {

    private static Object webServer = null;
    private static final Object regLock = new Object();
    private static volatile Method method;
    private static Thread starterThread;
    private static File tmpFile;

    private static class StdOutWrapper extends OutputStream {

        private final StringBuilder buffer;
        StringWriter w = new StringWriter();

        public StdOutWrapper(StringBuilder buffer) {
            this.buffer = buffer;
        }

        @Override
        public void write(int b) throws IOException {
            w.write(b);
        }

        @Override
        public void flush() throws IOException {
            synchronized (this) {
                super.flush();
                buffer.append(w.toString());
            }
        }
    }

    public static String wpsInit() throws Throwable {
        if (webServer == null) {
            String configFileContent = "[Starter]\n"
                    + "workDir = /home/akrylov/ssd/radix/dev/trunk\n"
                    + "topLayerUri = org.radixware\n"
                    + "appClass = org.radixware.wps.WebServer\n"
                    + "[WebPresentationServer]\n"
                    + "development\n"
                    + "[App]\n"
                    + "askUserPwdAfterInactivity\n"
                    + "uniquePwdSeqLen = 3\n"
                    + "pwdMustContainNChars\n"
                    + "blockUserInvalidLogonCnt = 3\n"
                    + "blockUserInvalidLogonMins = 3\n"
                    + "pwdExpirationPeriod = 365\n";

            tmpFile = File.createTempFile("rdx_config", "rdx_config");
            org.radixware.kernel.common.utils.FileUtils.writeString(tmpFile, configFileContent, "UTF-8");
            String configFile = tmpFile.getAbsolutePath();



            final StringBuilder stdErr = new StringBuilder();
            final StdOutWrapper errWrapper = new StdOutWrapper(stdErr);
            final String[] args = new String[]{"-configFile", configFile};
            starterThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    System.setErr(new PrintStream(errWrapper));
                    try {
                        Starter.main(args);
                    } catch (Exception ex) {
                        Logger.getLogger(WPSBridge.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    webServer = null;
                    try {
                        errWrapper.flush();
                    } catch (IOException ex) {
                        //ignore
                    }

                }
            });
            try {
                starterThread.setDaemon(true);
                starterThread.start();
                for (;;) {
                    synchronized (regLock) {
                        regLock.wait(100);
                        if (webServer != null) {
                            return null;
                        } else {
                            if (!starterThread.isAlive()) {
                                if (stdErr.length() != 0) {
                                    return stdErr.toString();
                                } else {
                                    return "Web presentation server startup was failed.";
                                }
                            }
                        }
                    }
                }

            } catch (Throwable t) {
                return "";
            }
        } else {
            return null;
        }
    }

    public static void register(Object obj) throws Exception {
        synchronized (regLock) {
            try {
                webServer = obj;
                method = webServer.getClass().getMethod("processRequest", HttpServletRequest.class, HttpServletResponse.class);
                regLock.notifyAll();
            } catch (NoSuchMethodException ex) {
                throw ex;
            } catch (SecurityException ex) {
                throw ex;
            }
        }
    }

    public static void shutdown() {
        if (tmpFile != null) {
            FileUtils.deleteFile(tmpFile);
        }
        if (webServer != null) {
            try {

                Method shutdownMethod = webServer.getClass().getMethod("shutdown", new Class[0]);
                try {
                    shutdownMethod.invoke(webServer, new Object[0]);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(WPSBridge.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(WPSBridge.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(WPSBridge.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(WPSBridge.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(WPSBridge.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                webServer = null;
            }
        }
    }
//    private static class Flow {
//
//        private AtomicBoolean isBuisy = new AtomicBoolean(false);
//
//        public void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
//            try {
//                if (method != null) {
//                    method.invoke(webServer, request, response);
//                } else {
//                    throw new Exception("Web presentation server is not initialized");
//                }
//            } finally {
//                release();
//            }
//        }
//
//        private boolean capture() {
//            synchronized (this) {
//                return isBuisy.compareAndSet(false, true);
//            }
//        }
//
//        private void release() {
//            synchronized (this) {
//                isBuisy.set(false);
//            }
//        }
//    }
//    private static final List<Flow> flows = new LinkedList<Flow>();
//
//    private static Flow getFlow() {
//        synchronized (flows) {
//            for (Flow flow : flows) {
//                if (!flow.capture()) {
//                    return flow;
//                }
//            }
//            Flow f = new Flow();
//            flows.add(f);
//            f.capture();
//            return f;
//        }
//    }

    public static void processRequest(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        //getFlow().processRequest(request, response);
        try {
            if (method != null) {
                method.invoke(webServer, request, response);
            } else {
                throw new Exception("Web presentation server is not initialized");
            }
        } finally {
        }
    }
}
