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

package org.radixware.kernel.common.client.eas;

import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.trace.AbstractTraceBuffer;
import org.radixware.kernel.common.client.trace.ClientTraceItem;
import org.radixware.kernel.common.client.trace.TraceBuffer;
import org.radixware.kernel.common.defs.utils.MlsProcessor;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.trace.TraceProfile;


class EasTrace extends LocalTracer {

    private final IClientEnvironment environment;
    private final TraceProfile commandLineTraceProfile;
    private final AbstractTraceBuffer buffer;
    private final MlsProcessor mlsProcessor = new MlsProcessor() {

        @Override
        public EIsoLanguage getDefLanguage() {
            return environment.getLanguage();
        }

        @Override
        public EEventSeverity getEventSeverityByCode(String code) {
            return EEventSeverity.ERROR;
        }

        @Override
        public String getEventSourceByCode(String code) {
            return "??? (code=" + code + ")";
        }

        @Override
        public String getEventTitleByCode(String code, EIsoLanguage lang) {
            return environment.getDefManager().getEventTitleByCode(code);
        }
    };
    

    EasTrace(IClientEnvironment environment) {
        this.environment = environment;
        TraceProfile profile;
        try{
            profile = new TraceProfile(environment.getTraceProfile());
        }catch(WrongFormatError error){
           profile = new TraceProfile("");
        }
        commandLineTraceProfile = profile;
        buffer = environment.getTracer().createTraceBuffer();
        buffer.beQuiet();
    }
    
    @Override
    public void put(final EEventSeverity severity, final String localizedMess, final String code, final List<String> words, final boolean isSensitive) {
        final String source = EEventSource.CLIENT_SESSION.getValue();
        final String message;
        if (code != null && !code.isEmpty()) {
            message = TraceItem.getMess(mlsProcessor, code, environment.getLanguage(), words);
        } else {
            message = localizedMess;
        }
        if (environment.getTracer().getProfile().itemMatch(severity,source) || commandLineTraceProfile.itemMatch(severity,source)) {
            buffer.put(severity,message,source);
        }
    }

    @Override
    public long getMinSeverity() {
        return Math.min(environment.getTracer().getMinSeverity(), commandLineTraceProfile.getMinSeverity());
    }

    @Override
    public long getMinSeverity(String eventSource) {
        return Math.min(environment.getTracer().getMinSeverity(eventSource), commandLineTraceProfile.getMinSeverity());
    }

    public void clear() {
        buffer.clear();
    }

    public AbstractTraceBuffer getBuffer() {
        return buffer;
    }
}
