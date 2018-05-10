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
package org.radixware.kernel.server.sc;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import javax.net.ssl.SSLContext;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallSendException;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.kernel.common.sc.SapClientOptions;
import org.radixware.kernel.common.sc.ServiceClient;
import org.radixware.kernel.common.soap.RadixSoapMessage;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.server.aio.ServiceManifestServerLoader;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.common.cache.ObjectCache;
import org.radixware.kernel.common.enums.EAadcMember;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.soap.CxfClientFactory;
import org.radixware.kernel.server.soap.ICxfClientContext;
import org.radixware.kernel.server.soap.ServerSoapUtils;

public class ServerServicesClient extends ServiceClient {

    private static final long SLEEP_ON_ALL_BUSY_MILLIS = SystemPropUtils.getLongSystemProp("rdx.arte.sleep.on.saps.busy.millis", 100);
    private final Arte arte;
    private final ServiceManifestServerLoader manifestLoader;

    public ServerServicesClient(final Arte arte, final LocalTracer tracer) throws ServiceCallException {
        super(tracer, null, null, null, new CxfClientFactory(new CxfClientContextImpl(arte, arte.getObjectCache(), tracer)));
        this.arte = arte;

        manifestLoader = new ServiceManifestServerLoader() {
            @Override
            protected Connection getDbConnection() {
                return arte.getDbConnection().get();
            }

            @Override
            protected Arte getArte() {
                return arte;
            }
        };
    }

    @Override
    protected List<SapClientOptions> refresh(final Long systemId, final Long thisInstanceId, final String serviceUri) {
        return manifestLoader.readSaps(systemId, thisInstanceId, serviceUri, scpName, REFRESH_PERIOD_MILLIS / 3);
    }

    @Override
    protected void onAllSapsBusy(int availableButBusyCount) throws InterruptedException, ServiceCallException {
        if (arte.needBreak()) {
            throw new ServiceCallSendException("ARTE service request aborted by client");
        }
        Thread.sleep(SLEEP_ON_ALL_BUSY_MILLIS);
    }

    @Override
    protected SSLContext prepareSslContext(final SapClientOptions sap) throws Exception {
        return CertificateUtils.prepareServerSslContext(sap.getClientKeyAliases(), sap.getServerCertAliases());
    }

    @Override
    protected boolean isSslPossible() {
        return true; //server keystore always exists
    }

    @Override
    public XmlObject invokeService(RadixSoapMessage message, EAadcMember aadcMember) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        arte.markInactive("invoke service");
        try {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_SC_INVOKE);
            try {
                return super.invokeService(message, aadcMember);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_SC_INVOKE);
            }
        } finally {
            arte.markActive("end invoke service");
        }
    }

    private static class CxfClientContextImpl implements ICxfClientContext {

        private final Arte arte;
        private final ObjectCache cache;
        private final LocalTracer tracer;

        public CxfClientContextImpl(Arte arte, ObjectCache cache, final LocalTracer tracer) {
            this.arte = arte;
            this.cache = cache;
            this.tracer = tracer;
        }

        @Override
        public URL getWsdlUrl(SapClientOptions sap) throws IOException {
            return ServerSoapUtils.getWsdlUrl(sap.getSoapServiceOptions(), arte.getDbConnection().get(), tracer);
        }

        @Override
        public ObjectCache getObjectCache() {
            return cache;
        }
    }
}
