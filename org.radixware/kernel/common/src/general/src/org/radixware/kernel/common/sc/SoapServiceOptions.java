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

package org.radixware.kernel.common.sc;

import java.util.Objects;
import javax.xml.namespace.QName;


public class SoapServiceOptions {

    private final WsdlSource wsdlSource;
    private final QName serviceName;
    private final QName portName;
    private final long lastUpdateTimeMillis;

    public SoapServiceOptions(WsdlSource source, QName serviceName, QName portName, final long lastUpdateTimeMillis) {
        this.lastUpdateTimeMillis = lastUpdateTimeMillis;
        this.serviceName = serviceName;
        this.portName = portName;
        this.wsdlSource = source;
    }

    public WsdlSource getWsdlSource() {
        return wsdlSource;
    }

    public QName getServiceName() {
        return serviceName;
    }

    public QName getPortName() {
        return portName;
    }

    public long getLastUpdateTimeMillis() {
        return lastUpdateTimeMillis;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.wsdlSource);
        hash = 67 * hash + Objects.hashCode(this.serviceName);
        hash = 67 * hash + Objects.hashCode(this.portName);
        hash = 67 * hash + (int) (this.lastUpdateTimeMillis ^ (this.lastUpdateTimeMillis >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SoapServiceOptions other = (SoapServiceOptions) obj;
        if (!Objects.equals(this.wsdlSource, other.wsdlSource)) {
            return false;
        }
        if (!Objects.equals(this.serviceName, other.serviceName)) {
            return false;
        }
        if (!Objects.equals(this.portName, other.portName)) {
            return false;
        }
        if (this.lastUpdateTimeMillis != other.lastUpdateTimeMillis) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SoapServiceOptions{" + "wsdlSource=" + wsdlSource + ", serviceName=" + serviceName + ", portName=" + portName + ", lastUpdateTimeMillis=" + lastUpdateTimeMillis + '}';
    }
}
