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
package org.radixware.kernel.common.utils.namefilter;

import java.util.List;
import org.radixware.kernel.common.enums.EAuthType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;

public enum ESearchType implements IKernelStrEnum {

    EXACT_NAME("EXACT_NAME"), 
    CASE_INSENSITIVE_EXACT_NAME("CASE_INSENSITIVE_EXACT_NAME"), 
    PREFIX("PREFIX"), 
    CASE_INSENSITIVE_PREFIX("CASE_INSENSITIVE_PREFIX"), 
    CAMEL_CASE("CAMEL_CASE"), 
    REGEXP("REGEXP"), 
    CASE_INSENSITIVE_REGEXP("CASE_INSENSITIVE_REGEXP");
    
    private final String val;

    private ESearchType(String val) {
        this.val = val;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getValue() {
        return val;
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
    
    public static ESearchType getForValue(final String str) {
        for (ESearchType type : ESearchType.values()) {
            if (type.getValue().equals(str)) {
                return type;
            }
        }
        throw new NoConstItemWithSuchValueError("ESearchType has no item with value " + str, str);
    }
}
