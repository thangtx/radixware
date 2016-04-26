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
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.Id;

/**
 * Свойства файла.
 * Используется при вызове метода {@link org.radixware.kernel.server.arte.resources.FileDirResource#read() FileDirResource.read}
 * для указания свойств, значения которых требуется получить.
 */
public enum EFileDirReadProperties implements IKernelStrEnum {

    /**
     * Тип файла (каталог или обычный файл)
     */
    ATTRIBUTES("Attributes"),
    /**
     * Время модификации
     */
    MODIFY_TIME("ModifyTime"),
    /**
     * Размер файла.
     * Если файл является каталогом, то значение данного свойства не включает размер его содержимого и
     * зависит от типа файловой системы.
     */
    SIZE("Size");
    
    private String val;

    private EFileDirReadProperties(final String val) {
        this.val = val;
    }

    @Override
    public String getValue() {
        return val;
    }

    @Override
    public String getName() {
        return null;
    }

    public static EFileDirReadProperties getForValue(final String val) {
        for (EFileDirReadProperties e : EFileDirReadProperties.values()) {
            if (e.getValue().equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EFileDirReadProperties has no item with value: " + String.valueOf(val), val);
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
