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

package org.radixware.wps;

import java.io.IOException;
import java.io.OutputStream;
import org.radixware.kernel.common.utils.FileUtils;


public final class HttpResponseContent {
    private final OutputStream out;
    private final String requestId;
    private final String requestTokenXmlAttr;
    private final long rqStartProcessTime;

    HttpResponseContent(final OutputStream out, final String requestId, final String requestToken, final long startProcessTime) {
        this.out = out;
        this.requestId = requestId;
        this.requestTokenXmlAttr = "token=\"" + requestToken + "\"";
        this.rqStartProcessTime = startProcessTime;
    }

    public String getRequestTokenXmlAttr() {
        return requestTokenXmlAttr;
    }

    public long getProcessRequestTime() {
        return System.currentTimeMillis() - rqStartProcessTime;
    }

    public void writeResponseXmlAttrs(final StringBuilder xmlBuilder, final boolean writeRequestToken) {
        if (writeRequestToken) {
            xmlBuilder.append(" id=\"");
            xmlBuilder.append(requestId);
            xmlBuilder.append('\"');
        }
        xmlBuilder.append(' ').append(requestTokenXmlAttr);
        xmlBuilder.append(" processTime=\"").append(String.valueOf(getProcessRequestTime())).append('\"');
    }

    public void writeString(final String string) throws IOException {
        FileUtils.writeString(out, string, FileUtils.XML_ENCODING);
    }

}
