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

import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.client.widgets.propertiesgrid.IPropertiesGridCell.ELinkageDirection;


abstract class AbstractLinkableCell<L extends IModelWidget, E extends IModelWidget> implements IPropertiesGridCell<L,E>{
    private IPropertiesGridCell<L,E> stickedLeft, stickedRight;
    
    public AbstractLinkableCell(){        
    }
    
    protected AbstractLinkableCell(final AbstractLinkableCell<L,E> copy){
        stickedLeft = copy.stickedLeft;
        stickedRight = copy.stickedRight;
    }
    
    @Override
    public boolean isEmpty() {
        if (getProperty().isVisible()){
            return false;
        }
        else{
            for (IPropertiesGridCell cell=stickedLeft; cell!=null; cell=cell.getLinkedCell(ELinkageDirection.LEFT)){
                if (cell.isVisible()){
                    return false;
                }
            }
            for (IPropertiesGridCell cell=stickedRight; cell!=null; cell=cell.getLinkedCell(ELinkageDirection.RIGHT)){
                if (cell.isVisible()){
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public boolean isLinked(final ELinkageDirection direction) {
        return direction==ELinkageDirection.LEFT ? stickedLeft!=null : stickedRight!=null;
    }        
    
    @Override
    public IPropertiesGridCell<L, E> getLinkedCell(final ELinkageDirection direction) {
        return direction==ELinkageDirection.LEFT ? stickedLeft : stickedRight;
    }

    @Override
    public void linkWith(final IPropertiesGridCell<L, E> cell, final ELinkageDirection direction) {
        if (direction==ELinkageDirection.LEFT){
            stickedLeft = cell;
        }
        else{
            stickedRight = cell;
        }
    }

    @Override
    public int getLinkedCellsCount(final ELinkageDirection direction) {
        int length = 0;
        if (direction==ELinkageDirection.LEFT){            
            for (IPropertiesGridCell cell=stickedLeft; cell!=null; cell=cell.getLinkedCell(ELinkageDirection.LEFT)){
                length++;
            }  
            return length;
        }
        else{
            for (IPropertiesGridCell cell=stickedRight; cell!=null; cell=cell.getLinkedCell(ELinkageDirection.RIGHT)){
                length++;
            }
        }
        return length;
    }
    
    

    @Override
    public String toString() {
        return getDescription(null);
    }    
}
