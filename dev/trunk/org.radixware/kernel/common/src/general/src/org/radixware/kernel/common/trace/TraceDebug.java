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

package org.radixware.kernel.common.trace;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.radixware.kernel.common.utils.FileUtils;


public class TraceDebug {
    
    private static final String RADIX_TRACE_DEGUG = "RadixTraceDebug";
    private static final String  RADIX_TRACE_DEGUG_LOG_SHORT_NAME = "RadixTraceDebugLog.log";
    
    // -J-DRadixTraceDebug=1  
    public static boolean isEnabledTraceDebug(){
        if (enabledCache!=null){
            return enabledCache;
        }
        String val = System.getProperties().getProperty(RADIX_TRACE_DEGUG);
        if (val!=null){
            val = val.trim().toLowerCase();
        }
        return enabledCache = "1".equals(val) || "true".equals(val);
    }
    static private Boolean enabledCache = null;
    
    public static String getTraceDirectoryPath(){
        return System.getProperties().getProperty("user.dir");
    }
    
    public static void tryTraceDebug(final String message){
        try{
            if (!isEnabledTraceDebug()){
                return;
            }
            
            final File file = new File(getTraceDirectoryPath() + "/" + RADIX_TRACE_DEGUG_LOG_SHORT_NAME);
            final Date date = (new Date());
            FileUtils.appendToFile(file,  DATE_FORMAT.format(date) + "  " + message + "\n");                    
            
        }
        catch (Throwable th){
            if (System.err!=null){
                System.err.println("Trace debug error -" + th.getMessage());
            }
        }
    }
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
    
}
