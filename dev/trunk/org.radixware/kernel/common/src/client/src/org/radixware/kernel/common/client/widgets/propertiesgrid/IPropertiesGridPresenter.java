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

import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.PropertiesGroupModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.views.IPropertiesGroupWidget;
import org.radixware.kernel.common.client.widgets.IModelWidget;


public interface IPropertiesGridPresenter<L extends IModelWidget, E extends IModelWidget, G extends IPropertiesGroupWidget> {

    interface IPresenterItem<L extends IModelWidget, E extends IModelWidget, G extends IPropertiesGroupWidget> {

        L getPropertyLabel();

        E getPropertyEditor();
        
        G getPropertiesGroupWidget();

        ModelItem getModelItem();
        
        boolean isModelItemVisible();
        
        boolean isModelItemReadOnly();

        int getColumn();

        int getRow();

        int getColumnSpan();
        
        int getRowSpan();
    }

    L createPropertyLabel(Property property);

    E createPropertyEditor(Property property);
    
    G createPropertiesGroup(PropertiesGroupModelItem propertiesGroup);

    void destroyWidgets(L label, E editor, G propertiesGroup);

    int getCellHeight(IPresenterItem<L, E, G> item);

    void beforeUpdateCellsPresentation(int columnsCount, int rowsCount);

    void presentCell(IPresenterItem<L, E, G> item, int columnsCount);

    public void presentSpanColumn(int col);
    
    public void presentSpanRow(int row);

    void clearCellPresentation(IPresenterItem<L, E, G> item);

    void afterUpdateCellsPresentation();

    void scrollToCell(IPresenterItem<L, E, G> item);
    
    void updateGeometry();
}
