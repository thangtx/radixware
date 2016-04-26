/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.sc;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import javax.net.ssl.SSLContext;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.kernel.common.sc.SapClientOptions;
import org.radixware.kernel.common.sc.ServiceClient;
import org.radixware.kernel.common.soap.RadixSoapMessage;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.server.aio.ServiceManifestLoader;
import org.radixware.kernel.server.instance.ObjectCache;
import org.radixware.kernel.server.soap.CxfClientFactory;
import org.radixware.kernel.server.soap.ICxfClientContext;
import org.radixware.kernel.server.soap.ServerSoapUtils;
import org.radixware.kernel.server.units.Unit;

/**
 *
 * @author dsafonov
 */
public class UnitServicesClient extends ServiceClient {

    protected final Unit unit;
    protected final ServiceManifestLoader manifestLoader;
    protected final ObjectCache objectCache;

    public UnitServicesClient(final Unit unit, final ServiceManifestLoader manifestLoader) {
        this(unit, manifestLoader, new ObjectCache());
    }

    protected UnitServicesClient(final Unit unit, final ServiceManifestLoader manifestLoader, final ObjectCache objectCache) {
        super(unit.createTracer(), null, null, null, new CxfClientFactory(new CxfClientContextImpl(unit, objectCache)));
        this.unit = unit;
        this.manifestLoader = manifestLoader;
        this.objectCache = objectCache;
    }

    @Override
    public XmlObject invokeService(final RadixSoapMessage message) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException {
        objectCache.maintenance();
        return super.invokeService(message);
    }

    @Override
    protected List<SapClientOptions> refresh(final Long systemId, final Long thisInstanceId, final String serviceUri) {
        return manifestLoader.readSaps(systemId, thisInstanceId, serviceUri, unit.getScpName(), REFRESH_PERIOD_MILLIS / 3);
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
    public void close() {
        try {
            super.close();
        } finally {
            objectCache.clear();
        }
    }

    private static class CxfClientContextImpl implements ICxfClientContext {

        private final Unit unit;
        private final ObjectCache cache;
        private final LocalTracer tracer;

        public CxfClientContextImpl(final Unit unit, final ObjectCache objectCache) {
            this.unit = unit;
            this.cache = objectCache;
            this.tracer = unit.createTracer();
        }

        @Override
        public URL getWsdlUrl(SapClientOptions sap) throws IOException {
            return ServerSoapUtils.getWsdlUrl(sap.getSoapServiceOptions(), unit.getDbConnection(), tracer);
        }

        @Override
        public ObjectCache getObjectCache() {
            return cache;
        }
    }
}
