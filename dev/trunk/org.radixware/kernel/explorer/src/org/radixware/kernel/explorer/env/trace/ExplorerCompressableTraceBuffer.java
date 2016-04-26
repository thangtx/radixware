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


public class ExplorerCompressableTraceBuffer extends ExplorerTraceBuffer{
    
    private final TraceMessageCompressor compressor = new TraceMessageCompressor();

    public ExplorerCompressableTraceBuffer(){
        compressor.start();
    }

    @Override
    protected void afterClearTraceItems() {
        compressor.clear();
    }

    @Override
    protected void afterRemoveTraceItem(final ExplorerTraceItem traceItem) {
        compressor.remove(traceItem);
    }

    @Override
    protected void afterPutTraceItem(final ExplorerTraceItem traceItem) {
        compressor.compress(traceItem);
    }
    
    public void stopCompressing(){
        compressor.close();
    }
}
