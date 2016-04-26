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

package org.radixware.kernel.common.soap;

import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.ESoapMessageType;
import org.radixware.kernel.common.sc.SapClientOptions;


public class RadixSoapMessage {

    private XmlObject bodyDoc;
    private ESoapMessageType type;
    private Map<String, String> attrs;
    private Class resultClass;
    private Long systemId;
    private Long thisInstanceId;
    private String serviceUri;
    private List<SapClientOptions> additionalSaps;
    private int keepConnectTimeSec;
    private int receiveTimeoutSec;
    private int connectTimeoutSec;
    public String destinationInfo;
    private int spentReceiveMillis;

    public RadixSoapMessage() {
    }

    public RadixSoapMessage(XmlObject bodyDoc, ESoapMessageType type, Map<String, String> messsageAttrs, Class resultClass, Long systemId, Long thisInstanceId, String serviceUri, List<SapClientOptions> additionalSaps, String destinationInfo, int keepConnectTimeSec, int receiveTimeoutSec, int connectTimeoutSec) {
        this.bodyDoc = bodyDoc;
        this.type = type;
        this.attrs = messsageAttrs;
        this.resultClass = resultClass;
        this.systemId = systemId;
        this.thisInstanceId = thisInstanceId;
        this.serviceUri = serviceUri;
        this.additionalSaps = additionalSaps;
        this.keepConnectTimeSec = keepConnectTimeSec;
        this.destinationInfo = destinationInfo;
        this.receiveTimeoutSec = receiveTimeoutSec;
        this.connectTimeoutSec = connectTimeoutSec;
    }

    public int getSpentReceiveMillis() {
        return spentReceiveMillis;
    }

    public void setSpentReceiveMillis(int spentSpentReceiveMillis) {
        this.spentReceiveMillis = spentSpentReceiveMillis;
    }

    public String getDestinationInfo() {
        return destinationInfo;
    }

    public void setDestinationInfo(String destinationInfo) {
        this.destinationInfo = destinationInfo;
    }

    public Class getResultClass() {
        return resultClass;
    }

    public void setResultClass(Class resultClass) {
        this.resultClass = resultClass;
    }

    public Long getSystemId() {
        if (systemId == null) {
            return 1l;
        }
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getThisInstanceId() {
        return thisInstanceId;
    }

    public void setThisInstanceId(Long thisInstanceId) {
        this.thisInstanceId = thisInstanceId;
    }

    public String getServiceUri() {
        return serviceUri;
    }

    public void setServiceUri(String serviceUri) {
        this.serviceUri = serviceUri;
    }

    public List<SapClientOptions> getAdditionalSaps() {
        return additionalSaps;
    }

    public void setAdditionalSaps(List<SapClientOptions> additionalSaps) {
        this.additionalSaps = additionalSaps;
    }

    public int getKeepConnectTimeSec() {
        return keepConnectTimeSec;
    }

    public void setKeepConnectTimeSec(int keepConnectTimeSec) {
        this.keepConnectTimeSec = keepConnectTimeSec;
    }

    public int getReceiveTimeoutSec() {
        return receiveTimeoutSec;
    }

    public void setReceiveTimeoutSec(int receiveTimeoutSec) {
        this.receiveTimeoutSec = receiveTimeoutSec;
    }

    public int getConnectTimeoutSec() {
        return connectTimeoutSec;
    }

    public void setConnectTimeoutSec(int connectTimeoutSec) {
        this.connectTimeoutSec = connectTimeoutSec;
    }

    public XmlObject getBodyDocument() {
        return bodyDoc;
    }

    public void setBodyDoc(XmlObject bodyDoc) {
        this.bodyDoc = bodyDoc;
    }

    public ESoapMessageType getType() {
        return type;
    }

    public void setType(ESoapMessageType type) {
        this.type = type;
    }

    public Map<String, String> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, String> messsageAttrs) {
        this.attrs = messsageAttrs;
    }
}
