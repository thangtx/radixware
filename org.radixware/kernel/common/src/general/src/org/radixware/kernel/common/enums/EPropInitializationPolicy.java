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
 * При создании объекта возможно три варианта инициализации свойства с наследованием значения:
 *  <ul>
 * <li>{@linkplain #DO_NOT_DEFINE не создавать значение}</li>
 * <li>{@linkplain #DEFINE_IF_NOT_INHERITED создавать, если не наследуется (из контекста)} - default вариант</li>
 * <li>{@linkplain #DEFINE_ALWAYS создавать всегда}</li>
 * </ul> 
 */
public enum EPropInitializationPolicy implements IKernelIntEnum {

    /**
     * создавать, если не наследуется (из контекста) (значение - 0)
     */
    DEFINE_IF_NOT_INHERITED("Define if not inherited", 0),
    /**
     * создавать всегда (значение - 1)
     */
    DEFINE_ALWAYS("Define always", 1),
    /**
     * не создавать значение (значение - 2)
     */
    DO_NOT_DEFINE("Dont define", 2);
    private final String name;
    private final long value;

    public static final EPropInitializationPolicy DEFAULT = DO_NOT_DEFINE;

    private EPropInitializationPolicy(String name, long value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getValue() {
        return Long.valueOf(value);
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }

    public static EPropInitializationPolicy getForValue(final long val) {
        for (EPropInitializationPolicy e : EPropInitializationPolicy.values()) {
            if (e.value == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError(EPropInitializationPolicy.class.getSimpleName() + " has no item with value: " + String.valueOf(val),val);
    }
}
