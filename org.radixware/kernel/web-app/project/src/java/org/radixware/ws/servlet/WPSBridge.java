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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.radixware.kernel.starter.Starter;


public class WPSBridge {

    private static Object webServer = null;
    private static InvalidVersionError invalidVersionError = null;
    private static final Object regLock = new Object();
    private static volatile Method method;
    private static Thread starterThread;
    private static final Object initLock = new Object();

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
    private static String initError = null;

    public static String wpsInit() {
        synchronized (initLock) {
            if (initError != null) {
                Logger.getLogger(LifecycleListener.class.getName()).log(Level.SEVERE, "Previous attemt to initialize Web Presentation Server bridge module failed {0}. Skipping this attemt", initError);
                return initError;
            } else {
                try {
                    return initError = wpsInitImpl();
                } catch (Throwable ex) {
                    Logger.getLogger(WPSBridge.class.getName()).log(Level.SEVERE, null, ex);

                    StringBuilder stack = new StringBuilder();
                    while (ex != null) {
                        stack.append(ex.getClass().getName());
                        if (ex.getMessage() != null) {
                            stack.append(": ").append(ex.getMessage());
                        }
                        stack.append('\n');
                        for (StackTraceElement e : ex.getStackTrace()) {
                            stack.append(e.toString()).append('\n');
                        }
                        ex = ex.getCause();
                        if (ex != null) {
                            stack.append("Caused by ");
                        }
                    }
                    return initError = stack.toString();
                }

            }
        }
    }

    public static String wpsInitImpl() throws Throwable {
        if (webServer == null) {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            String[] appArgList = null;
            String configFile = null;
            String varName = null;
            try {
                configFile = (String) envCtx.lookup("configFile");
            } catch (NamingException e) {
            }
            if (configFile != null) {
                appArgList = new String[]{"-configFile", configFile};
                varName = "configFile";
                Logger.getLogger(WPSBridge.class.getName()).log(Level.SEVERE, "Initialize Web Presentation Server configuration from {0}", configFile);
            } else {
                try {
                    String runParams = (String) envCtx.lookup("runParams");
                    if (runParams != null) {
                        varName = "runParams";
                        StarterArgsParser parser = new StarterArgsParser(runParams);
                        appArgList = parser.toArgv();
                    }
                } catch (NamingException e) {
                }
            }
            if (varName == null) {
                return "No sutable configuration description found\n"
                        + "Check environment variables runParams or configFile in your context description file";
            }
            if (appArgList == null || appArgList.length == 0) {
                return "Incorrect or missing value of variable " + varName
                        + "\nCheck  your context description file";
            }

            final StringBuilder stdErr = new StringBuilder();
            final StdOutWrapper errWrapper = new StdOutWrapper(stdErr);
            final String[] args = appArgList;
            starterThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    System.setErr(new PrintStream(errWrapper));
                    try {
                        Starter.main(args);
                    } catch (Exception exception) {
                        exception.printStackTrace();
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
                invalidVersionError = null;
                starterThread.setDaemon(true);
                starterThread.start();
                for (;;) {
                    synchronized (regLock) {
                        regLock.wait(100);
                        if (webServer != null) {
                            return null;
                        } else {
                            if (invalidVersionError != null) {
                                return invalidVersionError.getMessage();
                            }
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
            Logger.getLogger(LifecycleListener.class.getName()).log(Level.FINE, "Web Presentation Server bridge module is already initialized. Ignore this attemt");
            return null;
        }
    }
    private static final String VERSION = "1.2.21";

    public static String getWPSVersion() {
        return VERSION;
    }

    private static void checkVersion(Object obj) {
        Field versionField;
        try {
            versionField = obj.getClass().getField("VERSION");
            Object fieldVal = versionField.get(null);
            if (fieldVal instanceof String) {
                String serverVersion = (String) fieldVal;
                if (!VERSION.equals(serverVersion)) {
                    throw new InvalidVersionError("Launcher version " + VERSION + " does not match to server version " + serverVersion + ". Launcher update requred");
                } else {
                    return;
                }
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            //  Logger.getLogger(WPSBridge.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new InvalidVersionError("Launcher version is not compatible with product installation. Replace yor launcher with one from your product distribution");

    }

    private static class InvalidVersionError extends RuntimeException {
        
        private static final long serialVersionUID = 6209179613415004769L;

        public InvalidVersionError(String message) {
            super(message);
        }
    }

    public static void register(Object obj) throws Exception {
        synchronized (regLock) {

            try {
                try {
                    checkVersion(obj);
                } catch (InvalidVersionError e) {
                    invalidVersionError = e;
                    throw e;
                }
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

    public static void processRequest(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        //getFlow().processRequest(request, response);
        long time = System.currentTimeMillis();
        try {

            if (method != null) {
                try {
                    method.invoke(webServer, request, response);

                } catch (InvocationTargetException e) {
                    Throwable cause = e.getCause();
                    if (cause != null) {
                        if ("SESSION_STATE_INVALID".equals(cause.getMessage())) {
                            //Browser was closed and dispose request processed
                            Logger.getLogger(WPSBridge.class.getName()).log(Level.FINE, "Session was disposed. Possiby browser was closed before request processing finished. Request processed by {0} ms", (System.currentTimeMillis() - time));
                        } else {
                            throw cause;
                        }
                    }
                } catch (Throwable e) {
                    throw e;
                }
            } else {
                throw new Exception("Web presentation server is not initialized");
            }
        } finally {
            Logger.getLogger(WPSBridge.class.getName()).log(Level.FINE, "Request processed by {0} ms", (System.currentTimeMillis() - time));
        }
    }
}
