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

package org.radixware.kernel.common.enums;

import java.util.List;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;


public enum ELdapX500AttrType implements IKernelStrEnum {//see RFC 2253
    
    COMMON_NAME("CN","commonName"),
    LOCALITY_NAME("L","localityName"),
    STATE_OR_PROVINCE_NAME("ST","stateOrProvinceName"),
    ORGANIZATION_NAME("O","organizationName"),
    ORGANIZATIONAL_UNIT_NAME("OU","organizationalUnitName"),
    COUNTRY_NAME("C","countryName"),
    STREET_ADDRESS("STREET","streetAddress"),
    DOMAIN_COMPONENT("DC","domainComponent"),
    USER_ID("UID","userid");
    
    private final String value,name;
    
    private ELdapX500AttrType(final String value, final String name){
        this.value = value;
        this.name = name;
    }
            
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }    
}
