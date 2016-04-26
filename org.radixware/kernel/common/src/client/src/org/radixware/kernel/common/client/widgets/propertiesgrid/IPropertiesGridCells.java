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

import java.util.Collection;
import org.radixware.kernel.common.client.widgets.IModelWidget;


interface IPropertiesGridCells<L extends IModelWidget, E extends IModelWidget> {
    
    abstract class CellsFinder<L extends IModelWidget, E extends IModelWidget>{
        abstract public boolean isTarged(IPropertiesGridCell<L,E> cell);
        public boolean isCancelled(){
            return false;
        }
    }
    
    IPropertiesGridCell<L,E> get(int column, int row);
    int getRowsCount();
    int getColumnsCount();
    boolean isEmptySpaceInRow(int row, int fromColumn, int toColumn);
    boolean isEmptySpaceInColumn(int column, int fromRow, int toRow);
    Collection<IPropertiesGridCell<L,E>> find(CellsFinder<L,E> finder);
}