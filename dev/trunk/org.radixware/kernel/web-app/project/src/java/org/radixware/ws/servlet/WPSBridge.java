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
import java.io.PrintWriter;
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
    private static final Object initLock = new Object();
    private static String initError = null;
    
    private static class StarterThread extends Thread{
        
        private final StringBuilder stdErr = new StringBuilder();
        private final StdOutWrapper errWrapper = new StdOutWrapper(stdErr);        
        private final String[] arguments;
        
        public StarterThread(final String[] args){
            super("RadixWare Starter Thread");
            arguments = args;
            setDaemon(true);
        }

        @Override
        public void run() {
            System.setErr(new PrintStream(errWrapper));
            String stackTrace = null;
            try {
                Starter.main(arguments);
            } catch (Exception exception) {
                stackTrace = WPSBridge.getStackTrace(exception);
            }
            webServer = null;
            try {
                errWrapper.flush();
            } catch (IOException ex) {
            }
            if (stackTrace!=null){
                stdErr.append('\n');
                stdErr.append(stackTrace);
            }
        }
        
        public String getThreadErrorMessage(){
            return stdErr.toString();
        }
        
    }

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
                    initError = getStackTrace(ex);
                    return initError;
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
            final StarterThread starterThread = new StarterThread(appArgList);
            try {
                invalidVersionError = null;
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
                                final String errorMessage = starterThread.getThreadErrorMessage();
                                if (errorMessage!=null && !errorMessage.isEmpty()){
                                    return errorMessage;
                                }else{
                                    return "Web presentation server startup was failed.";
                                }
                            }
                        }
                    }
                }

            } catch (Throwable t) {
                return getStackTrace(t);
            }
        } else {
            Logger.getLogger(LifecycleListener.class.getName()).log(Level.FINE, "Web Presentation Server bridge module is already initialized. Ignore this attemt");
            return null;
        }
    }

    public static String getStackTrace(final Throwable exception){
        final StringWriter writer = new StringWriter();
        exception.printStackTrace(new PrintWriter(writer));
        return writer.toString();        
    }
    
    private static void checkVersion(Object obj) {
        Field versionField;
        try {
            final ClassLoader cl = obj.getClass().getClassLoader();
            final Class versionClass = cl.loadClass("org.radixware.ws.servlet.WPSVersion");
            versionField = versionClass.getField("VERSION");
            final Object fieldVal = versionField.get(null);
            if (fieldVal instanceof String) {
                String serverVersion = (String) fieldVal;
                if (!WPSVersion.VERSION.equals(serverVersion)) {
                    throw new InvalidVersionError("Launcher version " + WPSVersion.VERSION + " does not match to server version " + serverVersion + ". Launcher update requred");
                } else {
                    return;
                }
            }
        } catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
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
