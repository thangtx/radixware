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

package org.radixware.kernel.common.exceptions;

import org.radixware.kernel.common.types.Id;

/**
 * Definition not found error.
 * Вызывается, если функция поиска дефиниции (обычно по идентификатору) не нашла дефиницию.
 */

/* Ошибка не сделана обрабатываемым исключением по причине того, 
что на сервере очень много мест, где ее пришлось бы обрабатывать,
в то время как это нарушение целостности проекта,
что должно привести к остановке выполнения запросов.
 */
public class DefinitionNotFoundError extends DefinitionError {

    private final Id definitionId;

    /**
     * Конструктор, используемый в случае, если дефиницию не удалось найти,
     * по причине ее отсутствия, например, ищется несуществующее свойство класса.
     * @param definitionId
     */
    public DefinitionNotFoundError(Id definitionId) {
        super("Definition #" + String.valueOf(definitionId) + " not found.");
        this.definitionId = definitionId;
    }

    /**
     * Конструктор, принимающий причину, по которой не удалось найти дефиницию,
     * например, по причине того, что не удалось открыть ее файл.
     */
    public DefinitionNotFoundError(Id definitionId, Throwable cause) {
        super("Definition #" + String.valueOf(definitionId) + " not found.", cause);
        this.definitionId = definitionId;
    }

    public Id getDefinitionId() {
        return definitionId;
    }
}
