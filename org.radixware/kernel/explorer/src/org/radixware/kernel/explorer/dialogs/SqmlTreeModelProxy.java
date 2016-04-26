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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.CaseSensitivity;
import com.trolltech.qt.gui.QSortFilterProxyModel;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitionsFilter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReferences;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReference;
import org.radixware.kernel.explorer.models.SqmlTreeModel;

public final class SqmlTreeModelProxy extends QSortFilterProxyModel {

    private final ISqmlDefinitionsFilter filter;

    public SqmlTreeModelProxy(final SqmlTreeModel definitionsModel, final ISqmlDefinitionsFilter definitionsFilter) {
        super();
        filter = definitionsFilter;
        setSourceModel(definitionsModel);
        setSortCaseSensitivity(CaseSensitivity.CaseInsensitive);
    }

    @Override
    protected boolean filterAcceptsRow(final int sourceRow, final QModelIndex parentIndex) {
        final QModelIndex index = sourceModel().index(sourceRow, 0, parentIndex);
        if (index != null) {
            final QAbstractItemModel model = sourceModel();
            final Object value = model.data(index, Qt.ItemDataRole.UserRole);
            final Object parentValue = parentIndex == null ? null : model.data(parentIndex, Qt.ItemDataRole.UserRole);
            if ((parentValue instanceof ISqmlTableDef)
                    && tableHasAlias((ISqmlTableDef) parentValue)
                    && isReference(value)) {
                //Для таблиц с псевдонимом ссылки не показываются
                return false;
            }
            if (filter != null && (value instanceof ISqmlDefinition)) {
                Object parentVal = null;
                for (QModelIndex parentIdx = parentIndex; parentIdx != null; parentIdx = parentIdx.parent()) {
                    parentVal = model.data(parentIdx, Qt.ItemDataRole.UserRole);
                    if (parentVal instanceof ISqmlDefinition) {
                        break;
                    }
                }
                if (filter.isAccepted((ISqmlDefinition) value, (ISqmlDefinition) parentVal)) {
                    return super.filterAcceptsRow(sourceRow, parentIndex);
                } else {
                    return false;
                }
            }
        }
        return super.filterAcceptsRow(sourceRow, parentIndex);
    }

    @Override
    protected boolean lessThan(final QModelIndex left, final QModelIndex right) {
        if (left != null && right != null) {
            final QAbstractItemModel model = sourceModel();
            final Object leftValue = model.data(left, Qt.ItemDataRole.UserRole);
            final Object rightValue = model.data(right, Qt.ItemDataRole.UserRole);
            if ((leftValue instanceof ISqmlColumnDef) && isReference(rightValue)) {
                return true;
            }
            if ((rightValue instanceof ISqmlColumnDef) && isReference(leftValue)) {
                return false;
            }
            if (leftValue instanceof ISqmlParameters) {
                return false;
            }
            if (rightValue instanceof ISqmlParameters) {
                return true;
            }
            if((leftValue instanceof ISqmlTableDef) && tableHasAlias((ISqmlTableDef)leftValue)) {
                return false;
            }
            if((rightValue instanceof ISqmlTableDef) && tableHasAlias((ISqmlTableDef)rightValue)) {
                return false;
            }
        }
        return super.lessThan(left, right);
    }

    private static boolean isReference(final Object obj) {
        return (obj instanceof ISqmlTableReference) || (obj instanceof ISqmlTableReferences);
    }

    private static boolean tableHasAlias(ISqmlTableDef table) {
        return table.getAlias() != null && !table.getAlias().isEmpty();
    }
    
    
    public ISqmlDefinition definition(final QModelIndex index) {
        final QModelIndex sourceModelIndex = this.mapToSource(index);
        return ((SqmlTreeModel)sourceModel()).definition(sourceModelIndex);
    }
}