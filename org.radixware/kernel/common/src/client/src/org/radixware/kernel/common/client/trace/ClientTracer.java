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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.FileException;
import org.radixware.kernel.common.defs.utils.MlsProcessor;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.trace.IRadixTrace;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.trace.TraceProfile;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.starter.log.DelegateLogFactory;
import org.radixware.kernel.starter.log.StarterLog;

public abstract class ClientTracer<T extends IClientTraceItem> extends LocalTracer implements IRadixTrace {

    public interface TraceLevelChangeListener {

        void traceLevelChanged(String traceProfileAsStr);
    }
    private final List<TraceLevelChangeListener> listeners = new LinkedList<>();

    public void addTraceLevelChangeListener(TraceLevelChangeListener l) {
        synchronized (listeners) {
            if (!listeners.contains(l)) {
                listeners.add(l);
            }
        }
    }

    public void removeTraceLevelChangeListener(TraceLevelChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    private void notifyListeners() {
        synchronized (listeners) {
            if (!listeners.isEmpty()) {
                final String traceProfileAsStr = profile.toString();
                for (TraceLevelChangeListener l : listeners) {
                    l.traceLevelChanged(traceProfileAsStr);
                }
            }
        }
    }

    private class ClientTraceProfile extends TraceProfile {

        protected ClientTraceProfile() {
            super(EEventSeverity.NONE, new HashMap<String, EEventSeverity>());
        }

        protected ClientTraceProfile(final String profileAsStr) {
            super(profileAsStr);
        }

        protected ClientTraceProfile(final TraceProfile source) {
            super(source);
        }

        @Override
        public void add(final TraceProfile profile) {
            super.add(profile);
            notifyListeners();
        }

        @Override
        public void set(final String source, final EEventSeverity severity, final String options) {
            super.set(source, severity, options);
            notifyListeners();
        }

        @Override
        public void setDefaultSeverity(final EEventSeverity severity) {
            super.setDefaultSeverity(severity);
            notifyListeners();
        }

        @Override
        public void setUseInheritedSeverity(final String source) {
            super.setUseInheritedSeverity(source);
            notifyListeners();
        }
    };

    private class DelegateStarterLogFactoryImpl extends DelegateLogFactory {

        @Override
        public Log createApacheLog(final String name) {
            return new StarterLogger(name, ClientTracer.this);
        }
    }

    private static class FileLogger<T extends IClientTraceItem>  implements AbstractTraceBuffer.TraceBufferListener<T> {

        private final TraceFile traceFile;
        private final AbstractTraceBuffer<T> selfBuffer;

        public FileLogger(final TraceFile file, final AbstractTraceBuffer<T> buffer) {
            traceFile = file;
            selfBuffer = buffer;
        }

        @Override
        public void newItemInBuffer(final T traceItem) {
            try {
                traceFile.put(traceItem);
            } catch (FileException exception) {
                selfBuffer.removeTraceBufferListener(this);
                try {
                    traceFile.close();
                } catch (IOException ex) {//NOPMD
                    //ignore
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
                final String traceMessage;
                if (exception.getCause() == null || exception.getCause().getMessage() == null || exception.getCause().getMessage().isEmpty()) {
                    traceMessage = exception.getMessage();
                } else {
                    traceMessage = exception.getMessage() + ": " + exception.getCause().getMessage();
                }
                selfBuffer.put(EEventSeverity.ERROR,traceMessage,EEventSource.CLIENT.getValue());
            }
        }

        @Override
        public void maxSeverityChanged(final EEventSeverity eventSeverity) {
        }

        @Override
        public void cleared() {
        }
    }
    private TraceProfile profile;
    private final TraceProfile commandLineTraceProfile;
    private AbstractTraceBuffer<T> buffer;
    private boolean serverTraceAllowed;
    final private List<String> sourceContext = new ArrayList<>();
    private FileLogger<T> fileLogger;
    private final TraceFile traceFile;
    final private static String ENCODING = RunParams.getConsoleEncoding();
    private OutputStreamWriter err;
    private OutputStreamWriter out;
    private IClientEnvironment environment;
    private final MlsProcessor mlsProcessor = new MlsProcessor() {
        @Override
        public EIsoLanguage getDefLanguage() {
            return environment.getLanguage();
        }

        @Override
        public EEventSeverity getEventSeverityByCode(final String code) {
            return EEventSeverity.ERROR;
        }

        @Override
        public String getEventSourceByCode(final String code) {
            return "??? (code=" + code + ")";
        }

        @Override
        public String getEventTitleByCode(final String code, final EIsoLanguage lang) {
            return environment.getDefManager().getEventTitleByCode(code);
        }
    };

    public ClientTracer(final IClientEnvironment environment) {
        super();
        this.environment = environment;
        TraceProfile initialProfile;
        try {
            initialProfile = new ClientTraceProfile(environment.getTraceProfile());
        } catch (WrongFormatError error) {
            initialProfile = new ClientTraceProfile();
        }
        commandLineTraceProfile = initialProfile;
        profile = new ClientTraceProfile(initialProfile);
        notifyListeners();

        if (ENCODING != null) {
            try {
                err = new OutputStreamWriter(System.err, ENCODING);
                out = new OutputStreamWriter(System.out, ENCODING);
            } catch (UnsupportedEncodingException ex) {
                environment.processException(ex);
                err = new OutputStreamWriter(System.err);
                out = new OutputStreamWriter(System.out);
            }
        } else {
            err = new OutputStreamWriter(System.err);
            out = new OutputStreamWriter(System.out);
        }
        sourceContext.add(EEventSource.EXPLORER.getValue());
        if (environment.getTraceFile() != null) {
            TraceFile tf;
            try {
                tf = new TraceFile(environment, ENCODING);
            } catch (FileException e) {
                tf = null;
                System.err.println("Can`t use trace file: " + e.getMessage());
            }
            traceFile = tf;
        } else {
            traceFile = null;
        }


    }

    public void registerStarterLogger() {
        StarterLog.setFactory(new ClientTracer.DelegateStarterLogFactoryImpl());
        //clear cached loggers to ensure that all log events will be handled by
        //our log implementation
        LogFactory.releaseAll();
    }

    public void readProfileFromString(final String profileAsStr) {
        profile = new ClientTraceProfile(profileAsStr);
        notifyListeners();
    }

    public void alarm(final String localizedMess) {
        put(EEventSeverity.ALARM, localizedMess, getCurrentSource());
    }

    public void alarm(final String code, final ArrStr words) {        
        put(EEventSeverity.ALARM, getMessageText(code, words), getCurrentSource());
    }

    public void error(final String localizedMess) {
        put(EEventSeverity.ERROR, localizedMess, getCurrentSource());
    }

    public void error(final String code, final ArrStr words) {
        put(EEventSeverity.ERROR, getMessageText(code, words), getCurrentSource());
    }

    public void event(final String localizedMess) {
        put(EEventSeverity.EVENT, localizedMess, getCurrentSource());
    }

    public void event(final String code, final ArrStr words) {
        put(EEventSeverity.EVENT, getMessageText(code, words), getCurrentSource());
    }

    public void warning(final String localizedMess) {
        put(EEventSeverity.WARNING, localizedMess, getCurrentSource());
    }

    public void warning(final String code, final ArrStr words) {
        put(EEventSeverity.WARNING, getMessageText(code, words), getCurrentSource());
    }

    public void debug(final String localizedMess) {
        put(EEventSeverity.DEBUG, localizedMess, getCurrentSource());
    }

    @Override
    public void put(final EEventSeverity sev, final String mess, final EEventSource source) {
        put(sev,mess,source==null ? getCurrentSource() : source.getValue());
    }

    @Override
    public void put(final EEventSeverity sev, final String code, final List<String> words, final String source) {
        put(sev,getMessageText(code, words),source);
    }

    public void put(final EEventSeverity sev, final String code, final List<String> words, final EEventSource source) {
        put(sev,getMessageText(code, words),source==null ? getCurrentSource() : source.getValue());        
    }

    public void put(final EEventSeverity severity, final String localizedMess) {
        put(severity,localizedMess,getCurrentSource());
    }

    public void put(final EEventSeverity severity, final String localizedMess, final String code, final List<String> words) {
        put(severity, localizedMess, code, words, false);
    }

    @Override
    public void put(final EEventSeverity severity, final String localizedMess, final String code, final List<String> words, final boolean isSensitive) {
        if (code != null && !code.isEmpty()) {
            put(severity,getMessageText(code, words),getCurrentSource());
        } else {
            put(severity,localizedMess,getCurrentSource());
        }
    }
    
    private String getMessageText(final String code, final List<String> words){
        return TraceItem.getMess(mlsProcessor, code, environment.getLanguage(), words);
    }
    
    public void put (final EEventSeverity severity, final String messageText, final String source){
        final String eventSource = source==null ? getCurrentSource() : source;
        final EEventSeverity eventSeverity = severity==null ? EEventSeverity.DEBUG : severity;
        if (profile.itemMatch(eventSeverity,eventSource) || commandLineTraceProfile.itemMatch(eventSeverity,eventSource)) {
            if (buffer != null) {
                buffer.put(eventSeverity, messageText, eventSource);
            } else {
                try {
                    final String formattedMessage = 
                        TraceItem.formatTraceMessage(eventSeverity, System.currentTimeMillis(), eventSource, null, messageText);
                    if (severity.getValue() > EEventSeverity.EVENT.getValue()) {                        
                        err.write(formattedMessage + "\n");
                    } else {
                        out.write(formattedMessage + "\n");
                    }
                } catch (IOException ex) {
                    environment.processException(ex);
                }
            }
        }        
    }        

    @Override
    public long getMinSeverity() {
        return profile.getMinSeverity();
    }

    @Override
    public long getMinSeverity(final EEventSource eventSource) {
        return profile.getMinSeverity(eventSource);
    }

    @Override
    public long getMinSeverity(final String eventSource) {
        return profile.getMinSeverity(eventSource);
    }

    public AbstractTraceBuffer<T> getBuffer() {
        return buffer;
    }

    public void setBuffer(final AbstractTraceBuffer<T> buffer) {
        if (this.buffer != null && fileLogger != null) {
            this.buffer.removeTraceBufferListener(fileLogger);
        }
        this.buffer = buffer;
        if (buffer != null && traceFile != null) {
            fileLogger = new FileLogger<>(traceFile, buffer);
            buffer.addTraceBufferListener(fileLogger);
        }
    }

    public TraceProfile getProfile() {
        return profile;
    }

    public boolean isServerTraceAllowed() {
        return serverTraceAllowed;
    }

    public void setServerTraceAllowed(final boolean allowed) {
        serverTraceAllowed = allowed;
    }

    public String getCurrentSource() {
        final StringBuffer result = new StringBuffer("");
        if (!sourceContext.isEmpty()) {
            result.append(sourceContext.get(0));
            for (int i = 1; i < sourceContext.size(); i++) {
                result.append('.');
                result.append(sourceContext.get(i));
            }
        }
        return result.toString();
    }

    public void enterEventSource(final String eventSource) {
        final StringTokenizer tokens = new StringTokenizer(eventSource, ".");
        while (tokens.hasMoreTokens()) {
            sourceContext.add(tokens.nextToken());
        }
    }

    public void leaveEventSource(final String eventSource) {
        final int index = sourceContext.lastIndexOf(eventSource);
        if (index >= 0) {
            for (int i = sourceContext.size() - 1; i >= index; i--) {
                sourceContext.remove(i);
            }
        }
    }

    public void put(final Throwable error) {
        //throw new UnsupportedOperationException("Not yet implemented");
        //put(error,EEventSource.DBP_EXPLORER);
        error("Unhandled exception", error);
    }

    public void error(final String localizedMess, final Throwable error, final EEventSource source) {
        String finalMessage = localizedMess;
        if (error != null) {
            final StringBuilder messageBuilder = new StringBuilder();
            if (finalMessage!=null){
                messageBuilder.append(finalMessage);
            }
            final String reason = ClientException.getExceptionReason(environment.getMessageProvider(), error);
            if (reason != null && !reason.isEmpty()) {
                if (finalMessage != null && !finalMessage.isEmpty()) {
                    messageBuilder.append(":\n");
                }
                messageBuilder.append(reason);
            }
            messageBuilder.append('\n');
            messageBuilder.append(ClientException.exceptionStackToString(error));
            finalMessage = messageBuilder.toString() ;
        }
        if (finalMessage == null || finalMessage.isEmpty()) {
            finalMessage = environment.getMessageProvider().translate("ExplorerError", "Unknown error");
        }
        final EEventSource finalSource = source != null ? source : EEventSource.EXPLORER;
        put(EEventSeverity.ERROR, finalMessage, finalSource);
    }

    public void error(final Throwable error) {
        error(null, error, EEventSource.EXPLORER);
    }

    public void error(final String localizedMess, final Throwable error) {
        error(localizedMess, error, EEventSource.EXPLORER);
    }

    public void close() {
        StarterLog.setFactory(null);
        if (traceFile != null) {
            try {
                traceFile.close();
            } catch (final IOException ex) {
                if (ex.getMessage() != null && !ex.getMessage().isEmpty()) {
                    environment.messageError(environment.getMessageProvider().translate("ExplorerError", "Error on trace file closing") + ":\n" + ex.getMessage());
                } else {
                    environment.messageError(environment.getMessageProvider().translate("ExplorerError", "Error on trace file closing"));
                }
            }
        }
        if (buffer!=null){
            buffer.clearTraceBufferListeners();
        }
        LogFactory.releaseAll();
    }
    
    public abstract AbstractTraceBuffer<T> createTraceBuffer();
}
