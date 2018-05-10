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

package org.radixware.kernel.explorer.env.trace;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QIcon;
import java.awt.Dimension;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.trace.ClientTraceItem;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class ExplorerTraceItem extends ClientTraceItem{
    
    private static int uncompressedMessageMaxLength;
    {
        try{
            uncompressedMessageMaxLength = 
                Integer.parseInt(System.getProperty("org.radixware.kernel.explorer.uncompressedTraceMessageMaxLength ","1000"));
        }catch(NumberFormatException exception){
            uncompressedMessageMaxLength = 1000;
        }
    }
    
    private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger(0);
    
    private final Object semaphore = new Object();
    private final int index;
    private Dimension cachedSize;
    private String messageText;
    private String stackTrace;
    private byte compressedMessage[];
    private byte compressedStack[];
        
    public ExplorerTraceItem(final String messageText, final EEventSeverity severity, final String source) {
        super(null, severity, source, (String)null); 
        this.messageText = messageText;
        stackTrace = calcStackTrace(source, 7);
        index = INSTANCE_COUNTER.getAndIncrement();       
    }
        
    public ExplorerTraceItem(final EEventSeverity severity, final String asText){
        super(severity, null, (String)null);
        this.messageText = asText;
        stackTrace = calcStackTrace(getSource(), 7);
        index = INSTANCE_COUNTER.getAndIncrement();
    }
    
    public ExplorerTraceItem(final String messageText, final EEventSeverity severity, final String source, final long timeMillis) {
        super(null, severity, source, timeMillis, (String)null);
        this.messageText = messageText;
        stackTrace = calcStackTrace(source, 7);
        index = INSTANCE_COUNTER.getAndIncrement();
    }
        
    public ExplorerTraceItem(final ExplorerTraceItem copy){
        this(copy.messageText,copy.getSeverity(),copy.getSource(),copy.getTime());
        synchronized(copy.semaphore){
            messageText = copy.messageText;
            compressedMessage = copy.compressedMessage;
            cachedSize = copy.cachedSize;
            stackTrace = calcStackTrace(copy.getSource(), 7);
            compressedStack = null;
        }
    }

    @Override
    public String getMessageText() {
        synchronized(semaphore){
            if (compressedMessage!=null){
                try{
                    return Utils.decompressString(compressedMessage);
                }catch(IOException ex){
                    return getDisplayText();
                }
            }else{
                return messageText;
            }
        }
    }

    @Override
    public String getStackTrace() {
        synchronized(semaphore){
            if (compressedStack!=null){
                try{
                    return Utils.decompressString(compressedStack);
                }catch(IOException ex){
                    return null;
                }
            }else{
                return stackTrace;
            }
        }
    }
    
    

    public void cacheSize(final QSize size){
        if (size!=null && size.isValid()){
            cachedSize = new Dimension(size.width(), size.height());
        }
    }
    
    public Dimension getCachedSize(){
        return cachedSize;
    }    

    public QIcon getIcon(final IClientEnvironment environment){
        return ExplorerIcon.getQIcon(ExplorerIcon.TraceLevel.findEventSeverityIcon(getSeverity(), environment));
    }    
    
    public int getIndex(){
        return index;
    }
    
    public void compress(){
        synchronized(semaphore){            
            if (messageText!=null && uncompressedMessageMaxLength>0 && messageText.length()>uncompressedMessageMaxLength){
                compressedMessage = Utils.compressString(messageText);
                messageText = null;
            }
            if (stackTrace!=null){
                compressedStack = Utils.compressString(stackTrace);
                stackTrace = null;
            }
        }
    }    
}