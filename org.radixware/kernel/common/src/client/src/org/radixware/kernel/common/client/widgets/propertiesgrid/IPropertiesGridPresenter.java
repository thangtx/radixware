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

package org.radixware.kernel.common.client.widgets.propertiesgrid;

import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.widgets.IModelWidget;


public interface IPropertiesGridPresenter<L extends IModelWidget, E extends IModelWidget> {

    interface IPresenterItem<L extends IModelWidget, E extends IModelWidget> {

        L getPropertyLabel();

        E getPropertyEditor();

        Property getProperty();

        int getColumn();

        int getRow();

        int getColumnSpan();
    }

    L createPropertyLabel(Property property);

    E createPropertyEditor(Property property);

    void destroyWidgets(L label, E editor);

    int getCellHeight(IPresenterItem<L, E> item);

    void beforeUpdateCellsPresentation(int columnsCount, int rowsCount);

    void presentCell(IPresenterItem<L, E> item, int columnsCount);

    public void presentSpanColumn(int col);

    void clearCellPresentation(IPresenterItem<L, E> item);

    void afterUpdateCellsPresentation();

    void scrollToCell(IPresenterItem<L, E> item);
    
    void updateGeometry();
}
