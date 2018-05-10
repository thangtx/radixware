/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */

package org.radixware.kernel.common.enums;

import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;

public enum EInstanceThreadKind implements IKernelStrEnum {
    INSTANCE("INSTANCE"),
    UNIT("UNIT"),
    ARTE("ARTE"),
    ARTES_DB_LOG_FLUSHER("ARTES_DB_LOG_FLUSHER"),
    SERVER_CHILD("SERVER_CHILD");
    
    private final String val;

    private EInstanceThreadKind(String val) {
        this.val = val;
    }

    @Override
    public String getName() {
        return name();
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

    public static EInstanceThreadKind getForValue(final String str) {
        if (str == null) {
            return null;
        }
        for (EInstanceThreadKind type : EInstanceThreadKind.values()) {
            if (type.getValue().equals(str)) {
                return type;
            }
        }
        throw new NoConstItemWithSuchValueError("EInstanceThreadKind has no item with value " + str, str);
    }

}
