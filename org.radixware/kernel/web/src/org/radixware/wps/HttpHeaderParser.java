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

package org.radixware.wps;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;


final class HttpHeaderParser {

    static public Map<String,String> parseQueryString(String s) {        
        Map<String,String> params = new HashMap<>();
        if (s == null) {
            return params;
        }
        String decodedUrlStr = null;
        try {
            decodedUrlStr = URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException | IllegalArgumentException ex) {
            Logger.getLogger(HttpHeaderParser.class.getName()).log(Level.SEVERE, "Can't decode query", ex);
            return params;
        }
        final int paramsDeviderPos = decodedUrlStr.indexOf('?');
        final StringTokenizer st;
        if (paramsDeviderPos<0){
            st = new StringTokenizer(decodedUrlStr, "&");
        }else{
            st = new StringTokenizer(decodedUrlStr.substring(paramsDeviderPos+1), "&");
        }
        while (st.hasMoreTokens()) {
            String pair = st.nextToken();
            int pos = pair.indexOf('=');
            if (pos == -1) {
                if (!pair.isEmpty()){
                    params.put(pair,"");
                }
                continue;
            }          
                String key = pair.substring(0, pos);
                String val = pair.substring(pos + 1, pair.length());
                params.put(key, val);
        }
        return params;
    } 
}
