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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 */
public class LifecycleListener implements ServletContextListener {

    private boolean inited = false;
    
    private final static String WPS_CONTEXT_PATH_PROPERTY = "org.radixware.ws.servlet.WPSContextPath";
    private final static String DISABLE_SINGLE_WPS_SERVLET_CHECK_PROPERTY = "org.radixware.ws.servlet.DisableSingleServletCheck";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        synchronized (this) {
            if (!inited) {
                final String contextPath = System.getProperty(WPS_CONTEXT_PATH_PROPERTY);
                if (contextPath!=null){
                    if (contextPath.isEmpty()){
                        Logger.getLogger(LifecycleListener.class.getName()).log(Level.SEVERE, "Another instance of Web Presentation Server bridge module was already registered at root context path");
                    }else{
                        Logger.getLogger(LifecycleListener.class.getName()).log(Level.SEVERE, "Another instance of Web Presentation Server bridge module was already registered at \"{0}\" context path",contextPath);
                    }                    
                    final String displayFailure = "Another instance of Web Presentation Server bridge module was already registered"; //Message to show on web page
                    sce.getServletContext().setAttribute(Dispatcher.WPS_SERVLET_FAILURE_CONTEXT_ATTRIBUTE, displayFailure);
                    return;
                }
                Logger.getLogger(LifecycleListener.class.getName()).log(Level.INFO, "Initializing Web Presentation Server bridge module");
                String initResult = WPSBridge.wpsInit();
                if (initResult == null) {
                    Logger.getLogger(LifecycleListener.class.getName()).log(Level.INFO,
                            "Web Presentation Server bridge module was successfully initialized");
                } else {
                    Logger.getLogger(LifecycleListener.class.getName()).log(Level.SEVERE, "Web Presentation Server bridge module is initialised with message {0}", initResult);
                }
                if (System.getProperty(DISABLE_SINGLE_WPS_SERVLET_CHECK_PROPERTY)==null){
                    System.setProperty(WPS_CONTEXT_PATH_PROPERTY,sce.getServletContext().getContextPath());
                }
                inited = true;
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        synchronized (this) {
            if (inited) {
                inited = false;
            }
        }

        try {
            Logger.getLogger(LifecycleListener.class.getName()).log(Level.SEVERE, "Starting thread locals cleanup..");
            cleanThreadLocals();
            Logger.getLogger(LifecycleListener.class.getName()).log(Level.SEVERE, "End thread locals cleanup");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void cleanThreadLocals() throws NoSuchFieldException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException {

        Thread[] threadgroup = new Thread[256];
        Thread.enumerate(threadgroup);

        for (int i = 0; i < threadgroup.length; i++) {
            if (threadgroup[i] != null) {
                cleanThreadLocals(threadgroup[i]);
            }
        }
    }

    private void cleanThreadLocals(Thread thread) throws NoSuchFieldException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException {

        Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
        threadLocalsField.setAccessible(true);

        Class threadLocalMapKlazz = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
        Field tableField = threadLocalMapKlazz.getDeclaredField("table");
        tableField.setAccessible(true);

        Object fieldLocal = threadLocalsField.get(thread);
        if (fieldLocal == null) {
            return;
        }
        Object table = tableField.get(fieldLocal);

        int threadLocalCount = Array.getLength(table);

        for (int i = 0; i < threadLocalCount; i++) {
            Object entry = Array.get(table, i);
            if (entry != null) {
                Field valueField = entry.getClass().getDeclaredField("value");
                valueField.setAccessible(true);
                Object value = valueField.get(entry);
                if (value != null) {
                    String valueClassName = value.getClass().getName();
                    if (valueClassName.startsWith("org.radixware.kernel.") || valueClassName.startsWith("org.apache.xmlbeans.") || valueClassName.startsWith("org.tmatesoft.svn.")) {
                        valueField.set(entry, null);
                    }
                }
            }
        }
    }
}
