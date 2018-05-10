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

import java.util.EnumMap;
import org.radixware.kernel.common.client.views.IPropertiesGroupWidget;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.client.widgets.propertiesgrid.IPropertiesGridCell.ELinkageDirection;


abstract class AbstractLinkableCell<L extends IModelWidget, E extends IModelWidget, G extends IPropertiesGroupWidget> implements IPropertiesGridCell<L,E,G>{
        
    private final EnumMap<ELinkageDirection, IPropertiesGridCell<L,E,G>> stickedCells = new EnumMap<>(ELinkageDirection.class);
    
    public AbstractLinkableCell(){        
    }
    
    protected AbstractLinkableCell(final AbstractLinkableCell<L,E,G> copy){
        stickedCells.putAll(copy.stickedCells);
    }
    
    @Override
    public boolean isEmpty() {
        if (getModelItem()!=null && isModelItemVisible()){
            return false;
        } else {
            for (ELinkageDirection direction: ELinkageDirection.values()){
                for (IPropertiesGridCell cell=getLinkedCell(direction); cell!=null; cell=cell.getLinkedCell(direction)){
                    if (cell.isVisible()){
                        return false;
                    }
                }
            }
            return true;
        }
    }

    @Override
    public boolean isLinked(final ELinkageDirection direction) {
        return stickedCells.containsKey(direction);
    }
    
    @Override
    public IPropertiesGridCell<L, E, G> getLinkedCell(final ELinkageDirection direction) {
        return stickedCells.get(direction);
    }

    @Override
    public void linkWith(final IPropertiesGridCell<L, E, G> cell, final ELinkageDirection direction) {
        stickedCells.put(direction, cell);
    }

    @Override
    public int getLinkedCellsCount(final ELinkageDirection direction) {
        int length = 0;
        for (IPropertiesGridCell cell=getLinkedCell(direction); cell!=null; cell=cell.getLinkedCell(direction)){
            length++;
        }
        return length;
    }
        
    @Override
    public String toString() {
        return getDescription(null);
    }    
}
