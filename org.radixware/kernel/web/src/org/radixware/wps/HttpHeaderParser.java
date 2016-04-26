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

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


final class HttpHeaderParser {

    static public Map<String,String> parseQueryString(String s) {        
        Map<String,String> params = new HashMap<>();
        if (s == null) {
            return params;
        }
        final int paramsDeviderPos = s.indexOf('?');
        final StringTokenizer st;
        if (paramsDeviderPos<0){
            st = new StringTokenizer(s, "&");
        }else{
            st = new StringTokenizer(s.substring(paramsDeviderPos+1), "&");
        }
        final StringBuffer sb = new StringBuffer(); 
        while (st.hasMoreTokens()) {
            String pair = st.nextToken();
            int pos = pair.indexOf('=');
            if (pos == -1) {
                continue;
            }
            try{            
                String key = parseName(pair.substring(0, pos), sb);
                String val = parseName(pair.substring(pos + 1, pair.length()), sb);
                params.put(key, val);
            }catch(NumberFormatException exception){
                //ignore
            }
        }
        return params;
    }

    static private String parseName(String s, StringBuffer sb) {
        sb.setLength(0);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '+':
                    sb.append(' ');
                    break;
                case '%':
                    try {
                        sb.append((char) Integer.parseInt(s.substring(i + 1, i + 3),
                                16));
                        i += 2;
                    } catch (StringIndexOutOfBoundsException e) {
                        String rest = s.substring(i);
                        sb.append(rest);
                        if (rest.length() == 2) {
                            i++;
                        }
                    }

                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }
}
