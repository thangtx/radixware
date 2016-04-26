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


public enum EDefinitionUploadMode implements IKernelIntEnum {

    ON_STARTUP("On startup", 1),
    ON_DEMAND("On demand", 0);
    private final long value;
    private final String name;

    private EDefinitionUploadMode(final String name, final long value) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getValue() {
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

    public static EDefinitionUploadMode getForName(String name){
        for (EDefinitionUploadMode item : EDefinitionUploadMode.values()){
            if (item.getName().equals(name)){
                return item;
            }
        }
        return null;
    }

    public static EDefinitionUploadMode getForValue(final long value) {
        for (EDefinitionUploadMode e : EDefinitionUploadMode.values()) {
            if (e.value == value) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError(EDefinitionUploadMode.class.getSimpleName() + " has no item with value: " + String.valueOf(value),value);
    }
}
