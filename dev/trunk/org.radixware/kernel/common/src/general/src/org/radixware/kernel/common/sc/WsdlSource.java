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


public class WsdlSource {

    private final EWsdlSourceType type;
    private final Long sapId;
    private final String serviceWsdlUri;

    public WsdlSource(Long sapId) {
        this.sapId = sapId;
        this.type = EWsdlSourceType.SAP;
        this.serviceWsdlUri = null;
    }

    public WsdlSource(String serviceDefinitionUri) {
        this.serviceWsdlUri = serviceDefinitionUri;
        this.type = EWsdlSourceType.DATASCHEME_TABLE;
        this.sapId = null;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.type);
        hash = 37 * hash + Objects.hashCode(this.sapId);
        hash = 37 * hash + Objects.hashCode(this.serviceWsdlUri);
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
        final WsdlSource other = (WsdlSource) obj;
        if (this.type != other.type) {
            return false;
        }
        if (!Objects.equals(this.sapId, other.sapId)) {
            return false;
        }
        if (!Objects.equals(this.serviceWsdlUri, other.serviceWsdlUri)) {
            return false;
        }
        return true;
    }

    public EWsdlSourceType getType() {
        return type;
    }

    public Long getSapId() {
        return sapId;
    }

    public String getServiceWsdlUri() {
        return serviceWsdlUri;
    }

    @Override
    public String toString() {
        if (type == EWsdlSourceType.SAP) {
            return "Sap #" + (sapId == null ? "<null>" : sapId.toString());
        } else {
            return "Data Scheme " + ((serviceWsdlUri == null ? "<null>" : serviceWsdlUri));
        }
    }
}
