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
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;

/**
 * KeyStore type
 *
 */
public enum EKeyStoreType implements IKernelIntEnum {

    FILE(new Long(0), "jceks"),
    PKCS11(new Long(1), "pkcs11");
    private final Long value;
    private final String fileExt;

    private EKeyStoreType(Long value, String fileExt) {
        this.value = value;
        this.fileExt = fileExt;
    }

    @Override
    public Long getValue() {
        return value;
    }

    /**
     * @return default file extension for this key store type
     */
    public String getFileExt() {
        return fileExt;
    }

    @Override
    public String getName() {
        return name();
    }

    public static EKeyStoreType getForValue(final Long val) {
        for (EKeyStoreType e : EKeyStoreType.values()) {
            if (e.getValue().equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EKeyStoreType has no item with value: " + String.valueOf(val),val);
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
