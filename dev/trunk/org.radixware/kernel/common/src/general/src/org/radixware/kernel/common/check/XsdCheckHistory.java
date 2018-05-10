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
package org.radixware.kernel.common.check;

import java.util.HashMap;
import java.util.Map;

public class XsdCheckHistory {
    public static final String XSD_CHECK_HISTORY_KEY = "XSD_CHECK_HISTORY";
    
    Map<String, String> ns2CyclicImportPath = new HashMap<>();
    
    private XsdCheckHistory() {        
    }
    
    public static XsdCheckHistory getOrCreate(Map<Object, Object> checkerCache) {
        if (checkerCache == null) {
            return null;
        }
        
        if (checkerCache.containsKey(XSD_CHECK_HISTORY_KEY)) {
            return (XsdCheckHistory) checkerCache.get(XSD_CHECK_HISTORY_KEY);
        } else {
            XsdCheckHistory result = new XsdCheckHistory();
            checkerCache.put(XSD_CHECK_HISTORY_KEY, result);
            
            return result;
        }
    }
    
    public void addCyclicImportPath(String schemaNamespace, String path) {
        ns2CyclicImportPath.put(schemaNamespace, path);
    }
    
    public String getCyclicImportPath(String schemaNamespace) {
        return ns2CyclicImportPath.get(schemaNamespace);
    }        
}
