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

package org.radixware.kernel.common.defs.ads.ui.enums;

import java.util.List;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.Id;


public enum EArrayClassName implements UIEnum {

    ARR_INT("org.radixware.kernel.common.types", "ArrInt", EValType.ARR_INT),
    ARR_STR("org.radixware.kernel.common.types", "ArrStr", EValType.ARR_STR),
    ARR_CHAR("org.radixware.kernel.common.types", "ArrChar", EValType.ARR_CHAR),
    ARR_BIN("org.radixware.kernel.common.types", "ArrBin", EValType.ARR_BIN),
    ARR_NUM("org.radixware.kernel.common.types", "ArrNum", EValType.ARR_NUM),
    ARR_BLOB("org.radixware.kernel.common.types", "ArrBlob", EValType.ARR_BLOB),
    ARR_CLOB("org.radixware.kernel.common.types", "ArrClob", EValType.ARR_CLOB),
    ARR_REF("org.radixware.kernel.common.client.types", "ArrRef", EValType.ARR_REF),
    ARR_BOOL("org.radixware.kernel.common.types", "ArrBool", EValType.ARR_BOOL),
    ARR_DATE_TIME("org.radixware.kernel.common.types", "ArrDateTime", EValType.ARR_DATE_TIME);
    private final String className;
    private final String simpleName;
    private EValType radixType;

    private EArrayClassName(String packageName, String className, EValType radixType) {
        this.className = packageName + "." + className;
        this.simpleName = className;
        this.radixType = radixType;

    }

    @Override
    public String getQualifiedValue() {
        return className;
    }

    @Override
    public String getQualifiedEnum() {
        return className;
    }

    @Override
    public String getName() {
        return simpleName;
    }

    @Override
    public String getValue() {
        return className;
    }

    public EValType getRadixEnum() {
        return radixType;

    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }

    public static EArrayClassName getForValue(final String value) {

        for (EArrayClassName val : EArrayClassName.values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        throw new NoConstItemWithSuchValueError("EArrayClassName has no item with value: " + String.valueOf(value), value);
    }
}
