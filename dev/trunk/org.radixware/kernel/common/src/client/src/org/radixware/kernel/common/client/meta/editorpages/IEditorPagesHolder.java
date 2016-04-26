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

package org.radixware.kernel.common.client.meta.editorpages;

import org.radixware.kernel.common.client.meta.IModelDefinition;
import org.radixware.kernel.common.types.Id;

/**
 * Интерфейс поставщика дефиниций страниц редактирования.
 * Класс, реализующий данный интерфейс, предоставляет доступ к описаниям страниц редактирования.
 */
public interface IEditorPagesHolder extends IModelDefinition{

    /**
     * Поиск дефиниции страницы редактирования.
     * Метод осуществляет поиск по указанному идентификатору страницы редактирования.
     * @param pageId идентификатор страницы.
     * @return дефиниция страницы редактирования или <code>null</code>, если не найдена
     */        
    public RadEditorPageDef findEditorPageById(final Id pageId);

    /**
     * Получение набора страниц редактирования.
     * Метод возвращает конечный (с учетом наследования) набор дефиниций страниц (вкладок)
     * @return набор страниц редактированияра. Не может быть <code>null</code>
     */    
    public RadEditorPages getEditorPages();
}
