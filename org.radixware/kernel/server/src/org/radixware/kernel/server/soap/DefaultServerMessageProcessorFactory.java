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

package org.radixware.kernel.server.soap;

import org.radixware.kernel.common.soap.DefaultServerSoapMessageProcessor;
import org.radixware.kernel.common.soap.IServerMessageProcessorFactory;
import org.radixware.kernel.common.soap.IServerSoapMessageProcessor;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.cache.ObjectCache;
import org.radixware.kernel.server.sap.SapOptions;


public class DefaultServerMessageProcessorFactory implements IServerMessageProcessorFactory {

    private final IServerSoapMessageProcessor processor = new DefaultServerSoapMessageProcessor();
    private final SapOptions sapOptions;
    private final LocalTracer tracer;
    private final ObjectCache cache;

    public DefaultServerMessageProcessorFactory(SapOptions sapOptions, LocalTracer tracer, ObjectCache cache) {
        this.sapOptions = sapOptions;
        this.tracer = tracer;
        this.cache = cache;
    }

    @Override
    public IServerSoapMessageProcessor createProcessor() {
        if (sapOptions != null && sapOptions.getSoapServiceOptions() != null && sapOptions.getSoapServiceOptions().getWsdlSource() != null) {
            return new CxfServerSoapMessageProcessor(sapOptions, tracer, cache);
        } else {
            return processor;
        }
    }
}