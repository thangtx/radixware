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

import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.trace.TraceItem;


public class ClientTraceItem implements IClientTraceItem{
    
    private final static int MAX_DISPLAY_TEXT_LENGTH = 1000;
    
    private final String text;
    private String displayText;
    private final long time;
    private final String source;
    private final EEventSeverity severity;
    private final String stackTrace;    
    
    protected ClientTraceItem(final String messageText, final EEventSeverity severity, final String source, final String stack) {
        text = messageText;
        time = System.currentTimeMillis();
        this.source = source;
        this.severity = severity;
        stackTrace = stack;
    }
    
    protected ClientTraceItem(final EEventSeverity severity, final String asText, final String stack){
        text = asText;
        time = System.currentTimeMillis();
        source = EEventSource.CLIENT.toString();
        this.severity = severity;
        stackTrace = stack;
    }
    
    protected ClientTraceItem(final String messageText, final EEventSeverity severity, final String source, final long timeMillis, final String stack) {
        text = messageText;
        time = timeMillis;
        this.source = source;
        this.severity = severity;
        stackTrace = stack;
    }    
    
    public ClientTraceItem(final String messageText, final EEventSeverity severity, final String source) {
        text = messageText;
        time = System.currentTimeMillis();
        this.source = source;
        this.severity = severity;
        stackTrace = calcStackTrace(source);        
    }
        
    public ClientTraceItem(final EEventSeverity severity, final String asText){
        text = asText;
        time = System.currentTimeMillis();
        source = EEventSource.CLIENT.toString();
        this.severity = severity;
        stackTrace = calcStackTrace(source);
    }
    
    public ClientTraceItem(final String messageText, final EEventSeverity severity, final String source, final long timeMillis) {
        text = messageText;
        time = timeMillis;
        this.source = source;
        this.severity = severity;
        stackTrace = calcStackTrace(source);
    }
        
    public ClientTraceItem(final ClientTraceItem copy){
        text = copy.text;
        time = copy.time;
        source = copy.source;
        severity = copy.severity;
        displayText = copy.displayText;
        stackTrace = calcStackTrace(source,7);
    }    
    
    public String getStackTrace(){
        return stackTrace;
    }       

    @Override
    public String getMessageText() {
        return text;
    }

    @Override
    public String getDisplayText() {
        if (displayText==null){
            final String formattedString = TraceItem.formatTraceMessage(null, time, source, null, getMessageText());
            final int pos = formattedString.indexOf('\n');
            if (pos>0){
                displayText = formattedString.substring(0, Math.min(pos, MAX_DISPLAY_TEXT_LENGTH))+"...";
            }else if (formattedString.length()>MAX_DISPLAY_TEXT_LENGTH){
                displayText = formattedString.substring(0, MAX_DISPLAY_TEXT_LENGTH)+"...";
            }else{
                displayText = formattedString;
            }            
        }
        return displayText;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public EEventSeverity getSeverity() {
        return severity;
    }            

    @Override
    public String getFormattedMessage() {
        return TraceItem.formatTraceMessage(getSeverity(), getTime(), getSource(), null, getMessageText());
    }

    @Override
    public String toString() {
        return getFormattedMessage();
    }            
    
    private static String calcStackTrace(final String source){
        return calcStackTrace(source, 6);
    }
    
    protected static String calcStackTrace(final String source, final int startFrom){
        if (RunParams.isDevelopmentMode()
            && ( source==null || source.startsWith(EEventSource.CLIENT.getValue()) ) 
           ){
            final StringBuilder stackTraceBuilder = new StringBuilder();        
            final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            for (int i=startFrom; i<stackTraceElements.length; i++){
                if (stackTraceBuilder.length()>0){
                    stackTraceBuilder.append("\n");
                }
                stackTraceBuilder.append(" at ");
                stackTraceBuilder.append(stackTraceElements[i].toString());
            }
            return stackTraceBuilder.toString();
        }else{
            return null;
        }
    }    
}
 