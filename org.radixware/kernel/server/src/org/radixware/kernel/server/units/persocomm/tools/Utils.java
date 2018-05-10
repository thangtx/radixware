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
package org.radixware.kernel.server.units.persocomm.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.common.types.IKernelStrEnum;

public class Utils {
    private static final int        SMPP_SLICE_SIZE = 134;
    private static final int        SMPP_HEADER_SIZE = 6;
    
    public static void removeDirTree(final File dir) {
        if (dir == null) {
            throw new IllegalArgumentException("Directory can't be null!");
        }
        else {
            if (dir.exists()) {
                if (dir.isDirectory()) {
                    for (File f : dir.listFiles()) {
                        removeDirTree(f);
                    }
                }
                dir.delete();
            }
        }
    }
    
    public static String getAvailableEnumValues(final IKernelStrEnum[] list) {
        if (list == null) {
            throw new IllegalArgumentException("Enumeration values list can't be null!");
        }
        else {
            final StringBuilder     sb = new StringBuilder();

            for (int index = 0; index < list.length; index++) {
                sb.append(index == 0 ? '[' : ',').append(list[index].getValue());
            }
            return sb.append(']').toString();
        }
    }
    
    public static EMimeType getMimeTypeByFileName(final String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name can't be null or empty!");
        }
        else if (!fileName.contains(".")) {
            throw new IllegalArgumentException("File name ["+fileName+"] doesn't contain extension! MIMIE type can't be defined!");
        }
        else {
            final String            ext = fileName.substring(fileName.lastIndexOf('.')+1);
            final StringBuilder     sb = new StringBuilder();
         
            for (EMimeType item : EMimeType.values()) {
                if (item.getExt().equals(ext)) {
                    return item;
                }
                sb.append(',').append(item.getExt());
            }
            throw new IllegalArgumentException("File name extension for ["+fileName+"] is not known in the EMimeType enumeration. Known extensions are ["+sb.toString().substring(1)+"]");
        }
    }
    
    public static long copyStream(final InputStream is, final OutputStream os) throws IOException {
        final byte[]    buffer = new byte[4096];
        long            common = 0;
        int             len;
        
        while((len = is.read(buffer)) > 0) {
            os.write(buffer,0,len);
            common += len;
        }
        os.flush();
        
        return common;
    }
    
    public static String[] matches(final String value, final String template) {
        if (value == null) {
            return null;
        }
        else if (template == null) {
            return new String[0];
        }
        else {  // Template is  value[,..]. Value can contains '%' or '?'.
            final char[]    source = value.toCharArray(), templ = template.toCharArray();
            int             targetSize = 0;
            
            for (int index = 0; index < templ.length; index++) {    // Calculate max available pairs.
                if (templ[index] == '%' || templ[index] == '_') {
                    targetSize++;
                }
            }
            
            final int[][]   pairs = new int[targetSize][]; 

            if (matches(source,0,templ,0,0,pairs,0)) {
                int             count = pairs.length;
                
                for (int index = 0; index < pairs.length; index++) {    // Calculate max available pairs.
                    if (pairs[index] == null) {
                        count = index;
                        break;
                    }
                }
                
                final String[]  result = new String[count];
                
                for (int index = 0; index < result.length; index++) {    // Calculate max available pairs.
                    result[index] = new String(source,pairs[index][0],pairs[index][1]);
                }
                
                return result;
            }
            else {
                return null;
            }
        }
    }
    
    public static String matchAndReplace(final String source, final String match, final String replacement) {
        if (replacement == null) {
            throw new IllegalArgumentException("replacement can't be null!");
        }
        else {
            final String[]  matches = matches(source,match);

            if (matches == null) {
                return null;
            }
            else {
                return substitute(replacement,matches);
            }
        }
    }
    
    private static boolean matches(final char[] source, int sourceIndex, final char[] templ, int templIndex, final int state, final int[][] pairs, int pairsIndex) {
        switch (state) {
            case 0  :
                while (!matches(source,sourceIndex,templ,templIndex,1,pairs,pairsIndex)) {
                    while(templIndex < templ.length && templ[templIndex] != ',') {
                        templIndex++;
                    }
                    if (templIndex >= templ.length) {
                        return false;
                    }
                    else {
                        templIndex++;
                        for (int index = 0; index < pairs.length; index++) {
                            pairs[index] = null;
                        }
                    }
                }
                return true;
            case 1  :
                while (sourceIndex < source.length && templIndex < templ.length && (source[sourceIndex] == templ[templIndex] || templ[templIndex] == '_')) {
                    if (templ[templIndex] == '_') {
                        pairs[pairsIndex++] = new int[]{sourceIndex,1};
                    }
                    sourceIndex++;      templIndex++;
                }
                if (sourceIndex >= source.length && (templIndex >= templ.length || templ[templIndex] == ',')) {
                    return true;
                }
                else if (sourceIndex >= source.length) {
                    if (templIndex >= templ.length || templ[templIndex] == ',') {
                        return true;
                    }
                    else if (templ[templIndex] == '%') {
                        return matches(source,sourceIndex,templ,templIndex+1,2,pairs,pairsIndex);
                    }
                    else {
                        return false;
                    }
                }
                else if (templIndex < templ.length && templ[templIndex] == '%') {
                    return matches(source,sourceIndex,templ,templIndex+1,2,pairs,pairsIndex);
                }
                else {
                    return false;
                }
            case 2  :
                final int   startVar = sourceIndex;
                
                if (templIndex >= templ.length || templ[templIndex] == ',') {
                    pairs[pairsIndex] = new int[]{sourceIndex,source.length-sourceIndex};
                    return true;
                }
                else {
                    sourceIndex--;
                    do {sourceIndex++;  // Skip hipothese
                        while (sourceIndex < source.length && templIndex < templ.length && templ[templIndex] != '_' && source[sourceIndex] != templ[templIndex]) {
                            sourceIndex++;
                        }
                        if (sourceIndex >= source.length) {
                            return false;
                        }
                        else {
                            pairs[pairsIndex] = new int[]{startVar,sourceIndex-startVar};
                        }
                    } while (!matches(source,sourceIndex,templ,templIndex,1,pairs,pairsIndex+1));
                    return true;
                }
        }
        
        return false;
    }
    
    private static String substitute(final String template, final String[] matches) {
        final StringBuilder sb = new StringBuilder();
        final char[]        source = template.toCharArray();
        int                 indexFrom = 0, matchIndex = 0;
        
        for (int index = 0; index < source.length; index++) {
            if (source[index] == '%' || source[index] == '_') {
                sb.append(source,indexFrom,index-indexFrom);
                if (matchIndex < matches.length) {
                    sb.append(matches[matchIndex++]);
                }
                indexFrom = index+1;
            }
        }
        sb.append(source,indexFrom,source.length-indexFrom);
        
        return sb.toString();
    }
}
