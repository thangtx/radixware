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

package org.radixware.kernel.common.client.widgets;

import java.util.EnumSet;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMask;

/**
 * Интерфейс редактора маски редактирования
 */
public interface IEditMaskEditor extends IWidget{

    /**
     * Получить инстанцию маски клиента
     */
    EditMask getEditMask();

    /**
     * Прочитать настройки из инстанции маски клиента
     */
    void setEditMask(EditMask editMask);

    /**
     * Скрыть редакторы настроек
     */
    void setHiddenOptions(EnumSet<EEditMaskOption> options);

    /**
     * Показать редакторы настроек
     */
    void setVisibleOptions(EnumSet<EEditMaskOption> options);

    /**
     * Разрешить редактирование настроек
     */
    void setEnabledOptions(EnumSet<EEditMaskOption> options);

    /**
     * Запретить редактирование настроек
     */
    void setDisabledOptions(EnumSet<EEditMaskOption> options);

    /**
     * Проверить совместимость значений настроек
     */
    boolean checkOptions();
}
