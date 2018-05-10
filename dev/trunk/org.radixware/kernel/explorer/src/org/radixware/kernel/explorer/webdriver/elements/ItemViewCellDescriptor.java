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

package org.radixware.kernel.explorer.webdriver.elements;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QAbstractListModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.gui.QAbstractItemView;
import java.util.Objects;

final class ItemViewCellDescriptor  implements UIElementDescriptor{

    private final long viewNativeId;
    private final int column;
    private final ItemViewRowPath rowPath;

    public ItemViewCellDescriptor(final QAbstractItemView view, final QModelIndex index, final boolean useObjNames) {
        viewNativeId = view.nativeId();
        column = index.column();
        rowPath = ItemViewRowPath.newInstance(index, useObjNames);        
    }
    
    public QModelIndex resolveToIndex(final QAbstractItemModel model){
        final QModelIndex rowIndex = rowPath.resolve(model);
        if (rowIndex==null){
            return null;
        }
        if (model instanceof QAbstractListModel){
            return model.index(rowIndex.row(), column, null);
        }
        return model.index(rowIndex.row(), column, rowIndex.parent());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (int) (this.viewNativeId ^ (this.viewNativeId >>> 32));
        hash = 17 * hash + this.column;
        hash = 17 * hash + Objects.hashCode(this.rowPath);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ItemViewCellDescriptor other = (ItemViewCellDescriptor) obj;
        if (this.viewNativeId != other.viewNativeId) {
            return false;
        }
        if (this.column != other.column) {
            return false;
        }
        if (!Objects.equals(this.rowPath, other.rowPath)) {
            return false;
        }
        return true;
    }
    
    
}
