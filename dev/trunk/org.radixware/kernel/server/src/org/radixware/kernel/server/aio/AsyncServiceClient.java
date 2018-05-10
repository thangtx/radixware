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
package org.radixware.kernel.server.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.wsdl.WSDLException;

import org.apache.xmlbeans.XmlObject;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EHttpParameter;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.sc.SapClientOptions;
import org.radixware.kernel.common.soap.IClientMessageProcessorFactory;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.cache.ObjectCache;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.instance.aadc.AadcManager;
import org.radixware.kernel.server.soap.CxfClientFactory;
import org.radixware.kernel.server.soap.ICxfClientContext;

public class AsyncServiceClient {

    private static final long SAP_WARN_PERIOD_MILLIS = 10000;
    //
    protected EventDispatcher dispatcher;
    protected LocalTracer tracer;
    protected InetSocketAddress myAddress;
    protected final Map<String, String> rqFrameAttrs = new HashMap<>();
    protected final List<ServiceClientSeance> seances = new ArrayList<>();
    protected EPortSecurityProtocol securityProtocol = EPortSecurityProtocol.NONE;
    private static final int SAPS_REFRESH_PERIOD_MILLIS = 20 * 1000; //20 sec
    private final ServiceManifestLoader manifest;
    private final Long systemId;
    private final Long thisInstanceId;
    private final String serviceUri;
    private String scpName;
    private final List<SapClientOptions> saps = new ArrayList<>();
    private long lastSapsTableHash = -1;
    private long lastSapsRefreshTime;
    private boolean forceRereadSaps = false;
    private final CxfClientFactory cxfClientFactory;
    private final ObjectCache objectCache = new ObjectCache();
    private final Map<String, Long> sapLastWarnTimeMillis = new HashMap<>();
    private final Map<String, Long> noSapsByScpLastWarnTimeMillis = new HashMap<>();
    private final AadcManager aadcManager;

    public AsyncServiceClient(EventDispatcher dispatcher, LocalTracer tracer, final ServiceManifestLoader manifest, Long systemId, Long thisInstanceId, String serviceUri, String scpName, InetSocketAddress myAddress) {
        this.tracer = tracer;
        this.dispatcher = dispatcher;
        this.manifest = manifest;
        this.systemId = systemId;
        this.serviceUri = serviceUri;
        this.scpName = scpName;
        this.myAddress = myAddress;
        this.thisInstanceId = thisInstanceId;
        rqFrameAttrs.put(EHttpParameter.HTTP_REQ_METHOD_PARAM.getValue(), "POST");
        rqFrameAttrs.put(EHttpParameter.HTTP_REQ_PATH_PARAM.getValue(), "/");
        rqFrameAttrs.put(EHttpParameter.HTTP_CONTENT_TYPE_ATTR.getValue(), "text/xml; charset=\"UTF-8\"");
        rqFrameAttrs.put(EHttpParameter.HTTP_USER_AGENT_ATTR.getValue(), "Radixware");
        rqFrameAttrs.put(EHttpParameter.HTTP_CACHE_CONTROL_ATTR.getValue(), "no-cache");
        this.cxfClientFactory = new CxfClientFactory(new ICxfClientContext() {
            @Override
            public URL getWsdlUrl(SapClientOptions sap) throws IOException {
                return manifest.getWsdlUrl(sap);
            }

            @Override
            public ObjectCache getObjectCache() {
                objectCache.maintenance();
                return objectCache;
            }
        });
        final Instance instance = Instance.get();
        aadcManager = instance == null ? null : instance.getAadcManager();
    }

    public AadcManager getAadcManager() {
        return aadcManager;
    }

    public void setScpName(String scpName) {
        if (Utils.equals(this.scpName, scpName)) {
            return;
        }
        this.scpName = scpName;
        refreshSaps();
    }

    public String getScpName() {
        return scpName;
    }
    
    public ServiceClientSeance invoke(final XmlObject rqEnvBodyDoc, final Class resultClass, boolean keepConnect, int timeoutMillis, final EventHandler responseHandler) {
        return invoke(rqEnvBodyDoc, resultClass, null, keepConnect, timeoutMillis, responseHandler);
    }

    public ServiceClientSeance invoke(final XmlObject rqEnvBodyDoc, final Class resultClass, final Map<String, String> requestParams, boolean keepConnect, int timeoutMillis, final EventHandler responseHandler) {
        return invoke(rqEnvBodyDoc, resultClass, requestParams, keepConnect, timeoutMillis, responseHandler, null);
    }

    public ServiceClientSeance invoke(final XmlObject rqEnvBodyDoc, final Class resultClass, final Map<String, String> requestParams, boolean keepConnect, int timeoutMillis, final EventHandler responseHandler, final AadcAffinity aadcAffinity) {
        ServiceClientSeance seance = null;
        for (ServiceClientSeance s : seances) {
            if (!s.busy()) {
                seance = s;
                break;
            }
        }
        if (seance == null) {
            seance = new ServiceClientSeance(this);
            seances.add(seance);
        }
        if (responseHandler != null) {
            dispatcher.waitEvent(new ServiceClientSeance.ResponseEvent(seance), responseHandler, -1);
        }
        seance.invoke(rqEnvBodyDoc, resultClass, requestParams, keepConnect, timeoutMillis, aadcAffinity);
        return seance;
    }

    public int getActiveSeanceCount() {
        return seances.size();
    }

    public void interruptWaitingForConnectionSeances() {
        if (!seances.isEmpty()) {
            final ServiceClientSeance[] arr = new ServiceClientSeance[seances.size()];
            seances.toArray(arr);
            for (ServiceClientSeance s : arr) {
                if (s.busy() && !s.connected()) {
                    s.close();
                    dispatcher.notify(new ServiceClientSeance.ResponseEvent(s, new ServiceCallException("Seance was interrupted before establishing connection"), null));
                    seances.remove(s);
                }
            }
        }
    }

    public void closeActiveSeances() {
        if (!seances.isEmpty()) {
            final ServiceClientSeance[] arr = new ServiceClientSeance[seances.size()];
            seances.toArray(arr);
            for (ServiceClientSeance s : arr) {
                dispatcher.unsubscribe(new ServiceClientSeance.ResponseEvent(s));
                s.close();
            }
        }
    }

    protected IClientMessageProcessorFactory createMessageProcessorFactory(final SapClientOptions sap) throws IOException, WSDLException {
        return cxfClientFactory.createMessageProcessorFactory(sap, tracer);
    }

    protected SapClientOptions getSap(boolean enableBlocked, Integer aadcMemberId) {
        long curTime = System.currentTimeMillis();
        if (curTime - lastSapsRefreshTime > SAPS_REFRESH_PERIOD_MILLIS || (saps.isEmpty() && enableBlocked)) {
            refreshSaps();
        }
        if (saps.isEmpty()) {
            return null;
        }
        long sumPriority = 0;
        for (SapClientOptions s : saps) {
            if (aadcMemberId == null || Objects.equals(s.getAadcMemberId(), aadcMemberId)) {
                if (s.getBlockTime() > 0) {
                    if (curTime > s.getBlockTime()) {
                        s.unblock();
                    } else {
                        continue;
                    }
                }
                sumPriority += s.getPriorityAccountingLoad();
            }
        }
        if (sumPriority > 0) {
            long p = (long) (Math.random() * sumPriority);
            for (SapClientOptions s : saps) {
                if (aadcMemberId == null || Objects.equals(s.getAadcMemberId(), aadcMemberId)) {
                    if (s.getBlockTime() > 0) {
                        continue;
                    }
                    p -= s.getPriorityAccountingLoad();
                    if (p < 0) {
                        return s;
                    }
                }
            }
        }
        if (!enableBlocked) {
            return null;
        }
        sumPriority = 0;
        for (SapClientOptions s : saps) {
            if (aadcMemberId == null || Objects.equals(s.getAadcMemberId(), aadcMemberId)) {
                sumPriority += s.getPriorityAccountingLoad();
            }
        }
        long p = (long) (Math.random() * sumPriority);
        for (SapClientOptions s : saps) {
            if (aadcMemberId == null || Objects.equals(s.getAadcMemberId(), aadcMemberId)) {
                p -= s.getPriorityAccountingLoad();
                if (p < 0) {
                    return s;
                }
            }
        }
        return null; //never	
    }

    private void refreshSaps() {
        final long cacheValidTimeMillis = (forceRereadSaps || saps.isEmpty()) ? 0 : SAPS_REFRESH_PERIOD_MILLIS;
        forceRereadSaps = false;
        final List<SapClientOptions> new_saps;
        try {
            new_saps = manifest.readSaps(systemId, thisInstanceId, serviceUri, scpName, cacheValidTimeMillis);
        } catch (Exception ex) {
            tracer.put(EEventSeverity.WARNING, "Unable to refresh SAPs list: " + ExceptionTextFormatter.throwableToString(ex), null, null, false);
            return;
        }
        for (SapClientOptions newSap : new_saps) {
            for (SapClientOptions oldSap : saps) {
                if (oldSap.getName().equals(newSap.getName())) {
                    newSap.copyBlockStateFrom(oldSap);
                    oldSap.setSuccessor(newSap);
                    break;
                }
            }
        }
        saps.clear();
        saps.addAll(new_saps);

        Comparator<SapClientOptions> comparator = new Comparator<SapClientOptions>() {
            @Override
            public int compare(SapClientOptions o1, SapClientOptions o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
        Collections.sort(saps, comparator);

        final long curSapsTableHash = calcSapsTableHash();

        tracer.debug("SAP list refreshed, " + ((curSapsTableHash == lastSapsTableHash) ? "no changes" : "changes found"), false);

        lastSapsTableHash = curSapsTableHash;

        lastSapsRefreshTime = System.currentTimeMillis();
    }

    private long calcSapsTableHash() {
        final StringBuilder sb = new StringBuilder();
        for (SapClientOptions opts : saps) {
            sb.append(opts.getName());
            sb.append("~");
            sb.append(opts.getAddress());
            sb.append("~");
            sb.append(opts.getPriority());
            sb.append("|");
        }
        return sb.toString().hashCode();
    }

    public void setLocalTracer(LocalTracer tracer) {
        this.tracer = tracer;
    }

    /**
     * Will give no more that one event in ~10 sec, could be used to protect
     * trace from flood
     *
     * @return true if event was generated
     */
    public boolean notifySapUnusable(final String sapName, final String reason) {
        final Long prevWarnTimeMillis = sapLastWarnTimeMillis.get(sapName);
        final long curTimeMillis = System.currentTimeMillis();
        if (prevWarnTimeMillis == null || curTimeMillis - prevWarnTimeMillis > SAP_WARN_PERIOD_MILLIS) {
            tracer.put(EEventSeverity.EVENT, "SAP '" + sapName + "' is " + reason, null, null, false);
            sapLastWarnTimeMillis.put(sapName, curTimeMillis);
            return true;
        }
        return false;
    }

    /**
     * Will give no more that one error in ~10 sec, could be used to protect
     * trace from flood
     *
     * @return true if error was generated
     */
    public boolean notifyNoSaps(final String scpName) {
        final Long prevWarnMillis = noSapsByScpLastWarnTimeMillis.get(scpName);
        final long curTimeMillis = System.currentTimeMillis();
        if (prevWarnMillis == null || curTimeMillis - prevWarnMillis > SAP_WARN_PERIOD_MILLIS) {
            //introducing of the putFloodControlled made all additional code unnecessary
            tracer.putFloodControlled("nosaps" + scpName, EEventSeverity.ERROR, "There are no available SAPs for scp '" + scpName + "'", null, null, false);
            noSapsByScpLastWarnTimeMillis.put(scpName, curTimeMillis);
            return true;
        }
        return false;
    }
}
