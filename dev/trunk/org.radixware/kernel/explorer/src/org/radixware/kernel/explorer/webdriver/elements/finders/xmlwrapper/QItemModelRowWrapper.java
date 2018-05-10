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

package org.radixware.kernel.explorer.webdriver.elements.finders.xmlwrapper;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.explorer.webdriver.elements.ItemViewCellInfo;
import org.radixware.kernel.explorer.webdriver.elements.ItemViewRowInfo;
import org.radixware.kernel.explorer.webdriver.elements.UIElementPropertySet;
import org.radixware.kernel.explorer.webdriver.elements.UIElementReference;
import org.radixware.kernel.explorer.webdriver.elements.WebDrvUIElementsManager;
import org.w3c.dom.Document;

final class QItemModelRowWrapper extends UIElementWrapper{
    
    private final ItemViewRowInfo rowInfo;
    private final UIElementPropertySet props;
    
    public QItemModelRowWrapper(final ItemViewRowInfo row, final BaseNodeWrapper parent, final Document document){
        super("Row", parent, document);
        rowInfo = row;
        props = new UIElementPropertySet(rowInfo);
    }

    @Override
    public List<UIElementWrapper> getChildElements() {        
        final List<UIElementWrapper> children = new ArrayList<>();
        final List<ItemViewRowInfo> childRows = rowInfo.getChildRows();
        for (ItemViewRowInfo childRow: childRows){
            children.add(new QItemModelRowWrapper(childRow, this, getOwnerDocument()));
        }
        final List<ItemViewCellInfo> cells = rowInfo.getCells();
        for (ItemViewCellInfo cell: cells){
            children.add(new QItemModelCellWrapper(cell, this, getOwnerDocument()));
        }
        return children;
    }

    @Override
    public UIElementPropertySet getPropertySet() {
        return props;
    }

    @Override
    public UIElementReference getElementReference(final WebDrvUIElementsManager manager) {
        return rowInfo.getRowReference(manager);
    }        

}
