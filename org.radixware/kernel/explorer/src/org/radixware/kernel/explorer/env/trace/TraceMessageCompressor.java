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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;


final class TraceMessageCompressor extends Thread{
    
    public final BlockingQueue<ExplorerTraceItem> queue = new LinkedBlockingQueue<>();
    private final AtomicBoolean running = new AtomicBoolean(true);
    
    public TraceMessageCompressor(){
        super("Trace Message Compressor");
        setDaemon(true);
    }

    @Override
    public void run() {
        while(running.get()){
            try {
                queue.take().compress();
            } catch (InterruptedException ex) {//NOPMD               
            }
        }
    }        
    
    public void compress(final ExplorerTraceItem item){
        try {
            queue.put(item);
        } catch (InterruptedException ex) {//NOPMD            
        }
    }
    
    public void remove(final ExplorerTraceItem item){
        queue.remove(item);
    }
    
    public void clear(){
        queue.clear();
    }
    
    public void close(){
        running.set(false);
    }

}
