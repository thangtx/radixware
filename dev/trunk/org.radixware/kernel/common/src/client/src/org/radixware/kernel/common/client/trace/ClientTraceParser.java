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

package org.radixware.kernel.common.client.trace;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.trace.TraceParser;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.types.StrDocument;

public final class ClientTraceParser extends TraceParser {
    private static final Id PARSE_COMMAND_ID = Id.Factory.loadFrom("clcRICMQ3IWQZFRBAN72ZXWSDV7VU");
    private final IClientEnvironment env;
    
    public ClientTraceParser(final IClientEnvironment env) {
        super(env.getDefManager().getClassLoader());
        this.env = env;
    }

    @Override
    public String parse(final String traceText) {
        if (env.getEasSession() == null)
            return traceText;
                    
        StrDocument doc = StrDocument.Factory.newInstance();
        doc.setStr(traceText);
        try {
            doc = (StrDocument)env.getEasSession().executeContextlessCommand(PARSE_COMMAND_ID, doc, StrDocument.class);
        } catch (InterruptedException | ServiceClientException ex) {
            // do nothing
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        return super.parse(doc.getStr());
    }
}