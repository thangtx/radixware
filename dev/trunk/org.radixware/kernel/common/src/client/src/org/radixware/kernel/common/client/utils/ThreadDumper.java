/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.utils;

import org.radixware.kernel.common.utils.Utils;


public final class ThreadDumper {
    
    private ThreadDumper(){        
    }
    
    public static String dumpSync(){
        return dumpSync(Thread.currentThread(), 1, 0);
    }
    
    public static String dumpSync(final Thread thread, final int count, final long intervalnMills){
        final StringBuilder builder = new StringBuilder();
        final boolean currentThread = thread==Thread.currentThread();
        for (int i=0; i<count; i++){
            if (i>0 && !currentThread){
                try{
                    thread.join(intervalnMills);
                }catch(InterruptedException exception){
                    return null;
                }
            }
            final StackTraceElement[] stackTrace = thread.getStackTrace();
            if (stackTrace==null || stackTrace.length==0){
                return null;//thread was terminated
            }
            if (i>0){
                builder.append("\n\n");                
            }
            builder.append(Utils.stackToString(stackTrace));
        }
        return builder.toString();
    }

}
