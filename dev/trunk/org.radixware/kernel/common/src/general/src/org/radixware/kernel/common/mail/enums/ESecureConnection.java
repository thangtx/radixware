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

package org.radixware.kernel.common.mail.enums;

import java.util.List;
import org.radixware.kernel.common.enums.ESoapMessageType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;


public enum ESecureConnection implements IKernelStrEnum {

    NONE("None", "None"),
    TLS_ENCRYPTION("TlsEncryption", "TLS encryption");

    final String value;
    final String descr;

    ESecureConnection(String value, String descr) {
        this.value = value;
        this.descr = descr;
    }

    @Override
    public String getValue() {
        return value;
    }

//    public String getDescr() {
//        return descr;
//    }

    @Override
    public String toString() {
        return descr;
    }

    public static ESecureConnection getForValue(final String val) {
        for (ESecureConnection e : ESecureConnection.values()) {
            if (e.getValue().equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError(ESecureConnection.class.getSimpleName() + " has no item with value: " + String.valueOf(val),val);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
};
