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

package org.radixware.ws.servlet;

import java.util.LinkedList;
import java.util.List;


public class StarterArgsParser {

    private final String paramString;

    public StarterArgsParser(String paramString) {
        this.paramString = paramString;
    }

    public String[] toArgv() {
        List<String> args = new LinkedList<String>();
        boolean waitForParam = true;
        int paramStart = -1;
        int paramEnd = -1;
        boolean inQuote = false;
        for (int i = 0; i < paramString.length(); i++) {
            char c = paramString.charAt(i);
            switch (c) {
                case ' ':
                    if (!inQuote) {
                        if (!waitForParam) {
                            acceptParam(paramStart, paramEnd, args);
                            waitForParam = true;
                        }
                    }
                    break;
                case '"':
                    inQuote = !inQuote;
                    if (!waitForParam) {
                        paramEnd++;
                    } else {
                        paramStart = i;
                        paramEnd = i;
                        waitForParam = false;
                    }
                    break;
                default:
                    if (waitForParam) {
                        paramStart = i;
                        paramEnd = i;
                        waitForParam = false;
                    } else {
                        paramEnd++;
                    }
            }
        }
        if (!waitForParam) {
            acceptParam(paramStart, paramEnd, args);
        }

        int classIndex = -1;
        for (int i = 0; i < args.size(); i++) {
            String arg = args.get(i);
            if (!arg.startsWith("-")) {
                //class name
                classIndex = i;
                if (!"org.radixware.wps.WebServer".equals(arg)) {
                    args.set(i, "org.radixware.wps.WebServer");
                }
            }
        }
        if (classIndex < 0) {
            args.add("org.radixware.wps.WebServer");
        }

        return args.toArray(new String[args.size()]);
    }

    private void acceptParam(int start, int end, List<String> params) {
        if (end - start > 0) {
            String param = paramString.substring(start, end + 1);
            params.add(param);
        }
    }
}
