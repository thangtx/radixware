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
 * Параметры сортировки файлов.
 * Используется при вызове метода {@link org.radixware.kernel.server.arte.resources.FileDirResource#read() FileDirResource.read}
 * для управления порядком элементов в результирующем списке.
 */
public enum EFileDirReadSortOptions implements IKernelStrEnum{

    /**
     * Элементы, описывающие каталоги, всегда предшествуют элементам, описывающем файлы.
     * Это соблюдается даже если в наборе присутствует элемент DESCENDING.
     */
    DIRECTORIES_FIRST("DirsFirst"),
    /**
     * Сортировка строковых значений свойств осуществляется с учетом регистра символов.
     */
    CASE_SENSITIVE("CaseSensitive"),
    /**
     * Обратный порядок сортировки
     */
    DESCENDING("Desc");

    private String val;

    private EFileDirReadSortOptions(final String val) {
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
    
    public static EFileDirReadSortOptions getForValue(final String val) {
        for (EFileDirReadSortOptions e : EFileDirReadSortOptions.values()) {
            if (e.getValue().equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EFileDirReadSortOptions has no item with value: " + String.valueOf(val),val);
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
