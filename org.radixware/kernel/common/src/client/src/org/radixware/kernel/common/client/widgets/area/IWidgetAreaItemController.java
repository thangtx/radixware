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

package org.radixware.kernel.common.client.widgets.area;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.area.WidgetAreaItem.ECreationMode;


public interface IWidgetAreaItemController {
    IWidget createContent(final WidgetAreaItem item, ECreationMode creationMode, XmlObject storedItem);//Может вернуть null и тогда создание WidgetAreaItem должно быть отменено.
    XmlObject store(WidgetAreaItem item);
    String getTitle();//Возвращает заголовок типа элементов, которые управляются данным контроллером. Вызывается при создании элемента по запросу пользователя.
    boolean isCreationEnabled(IWidgetArea area);//Если метод возвращает false, то создание элемента данного типа по запросу пользователя запрещено.
    boolean beforeClose(WidgetAreaItem item);
    boolean isModified(WidgetAreaItem item); //Если метод возвращает true, то данные виджета изменены и нужно применить изменения.
    boolean applyChanges(WidgetAreaItem item); 
    void cancelChanges(WidgetAreaItem item);
}
