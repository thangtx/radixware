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

package org.radixware.kernel.common.client.trace;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.enums.EEventSeverity;


public final class JavaProxyLogger extends Logger {

    private class LogMessageHandler extends Handler {

        @Override
        public void publish(final LogRecord record) {
            synchronized(SEMAPHORE){
                if (!INSTANCES.contains(JavaProxyLogger.this)){
                    return;
                }
            }
            try{
                final EEventSeverity severity;
                if (record.getLevel() == Level.SEVERE) {
                    severity = EEventSeverity.ERROR;
                } else if (record.getLevel() == Level.WARNING) {
                    severity = EEventSeverity.WARNING;
                } else if (record.getLevel() == Level.CONFIG) {
                    severity = EEventSeverity.EVENT;
                } else {              
                    severity = EEventSeverity.DEBUG;
                }            
                final String logMessage;
                if (record.getParameters()==null || record.getParameters().length==0){
                    logMessage = record.getMessage();
                }
                else{
                    logMessage = MessageFormat.format(record.getMessage(), record.getParameters());
                }
                final String msgSource = environment.getMessageProvider().translate("TraceMessage", "Message source: %s");
                final String sourceMethod = record.getSourceClassName() + "." + record.getSourceMethodName();
                if (record.getThrown() != null) {
                    final String finalMessage = (logMessage == null ? "" : logMessage + "\n")
                            + ClientException.getExceptionReason(environment.getMessageProvider(), record.getThrown()) + "\n"
                            + ClientException.exceptionStackToString(record.getThrown()) + "\n"
                            + String.format(msgSource, sourceMethod);
                    environment.getTracer().put(severity, finalMessage);
                } else if (logMessage != null) {
                    final String finalMessage = logMessage + "\n" + String.format(msgSource, sourceMethod);
                    environment.getTracer().put(severity, finalMessage);
                }
            }catch(Throwable error){
                System.err.println("Failed to write trace message \'"+record.getMessage()+"\'");                
                error.printStackTrace();
            }
        }

        @Override
        public void flush() {
//            environment.getTracer().getBuffer();
//            QApplication.sendPostedEvents(Environment.tracer.getBuffer(), QEvent.Type.User.value());
        }

        @Override
        public void close() throws SecurityException {
        }
    }
    
    private IClientEnvironment environment;

    private JavaProxyLogger(final  String sourcePackage, final IClientEnvironment environment) {
        super(sourcePackage, null);
        this.environment = environment;
        setUseParentHandlers(false);
        setLevel(Level.ALL);
        addHandler(new LogMessageHandler());
    }
    
    /*
     * The application should retain its own reference to the Logger object to avoid it being garbage collected. 
     * The LogManager may only retain a weak reference. 
     */            
    private final static List<JavaProxyLogger>  INSTANCES = new LinkedList<>();
    private final static Object SEMAPHORE = new Object();
    
    public static void addProxyForSource(final  String sourcePackage, final IClientEnvironment environment){
        final JavaProxyLogger logger = new JavaProxyLogger(sourcePackage, environment);
        java.util.logging.LogManager.getLogManager().addLogger(logger);
        synchronized(SEMAPHORE){
            INSTANCES.add(logger);
        }
    }
    
    public static void clean(){
        synchronized(SEMAPHORE){
            INSTANCES.clear();
        }
    }
}