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

package org.radixware.kernel.common.utils;

import java.util.List;
import java.util.Map;

public class CommandLineParser {
	
   static public void parse(String[] args, Map<String, String> flags, List<String> params) {
        int i=0;
        while (i < args.length) {
            if (args[i].charAt(0) == '-') {
                String par, val=null;
                int index = args[i].indexOf('=');
                if (index != -1) {
                    par = args[i].substring(1, index);
                    val = args[i].substring(index+1);
                }
                else
                    par = args[i].substring(1);
                flags.put(par, val);
                i++;
            }
            else
                break;
        }
        while (i<args.length)
            params.add(args[i++]);
   }
   
}
