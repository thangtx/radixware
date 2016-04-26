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
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;

/**
 * Действие при удалении объекта, у которого есть подобъекты.
 * Используется приложением RadixWare Explorer.
 */

public enum EDeleteMode implements IKernelStrEnum {

    /**
     * Просто удалить объект.
     */
    NONE("NONE"),
    /**
     * Запрещать удаление.
     */
    RESTRICT("RESTRICT"),
    /**
     * Удалять каскадно.
     */
    CASCADE("CASCADE"),
    /**
     * Сбрасывать ссылку на объект у подобъектов в NULL.
     */
    SET_NULL("SET_NULL"),
    /**
     * Удалить ассоциацию между объектом и пользовательским свойством
     */
    REMOVE_VALUE("REMOVE_VALUE");
    private final String value;

    private EDeleteMode(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getName() {
        return null;
    }

    public static EDeleteMode getForValue(final String val) {
        for (EDeleteMode e : EDeleteMode.values()) {
            if (e.getValue().equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EDeleteMode has no item with value: " + String.valueOf(val), val);
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
