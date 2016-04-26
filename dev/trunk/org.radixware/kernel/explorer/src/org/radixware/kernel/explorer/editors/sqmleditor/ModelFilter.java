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

package org.radixware.kernel.explorer.editors.sqmleditor;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.Qt.CaseSensitivity;
import com.trolltech.qt.gui.QSortFilterProxyModel;
import com.trolltech.qt.gui.QTreeModel;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReferences;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReference;


public class ModelFilter extends QSortFilterProxyModel {

    private boolean isPropFilter = false;

    public ModelFilter(final QAbstractItemModel model) {
        super();
        setSourceModel(model);
        setSortCaseSensitivity(CaseSensitivity.CaseInsensitive);
    }

    @Override
    protected boolean filterAcceptsRow(final int sourceRow, final QModelIndex parentIndex) {
        final QModelIndex index = sourceModel().index(sourceRow, 0, parentIndex);
        if (index != null) {
            final QTreeModel model = (QTreeModel) sourceModel();
            final Object value = model.indexToValue(index),
                    parentValue = parentIndex == null ? null : model.indexToValue(parentIndex);
            if ((parentValue instanceof ISqmlTableDef) && isReference(value)) //Для таблиц с псевдонимом ссылки не показываются
            {
                return ((ISqmlTableDef) parentValue).getAlias() == null
                        || ((ISqmlTableDef) parentValue).getAlias().isEmpty();
            }
        }
        return super.filterAcceptsRow(sourceRow, parentIndex);
    }

    @Override
    protected boolean lessThan(final QModelIndex left, final QModelIndex right) {
        if (left != null && right != null) {
            final QTreeModel model = (QTreeModel) sourceModel();
            final Object leftValue = model.indexToValue(left),
                    rightValue = model.indexToValue(right);
            if ((leftValue instanceof ISqmlColumnDef) && isReference(rightValue)) {
                return true;
            }
            if ((rightValue instanceof ISqmlColumnDef) && isReference(leftValue)) {
                return false;
            }
        }
        return super.lessThan(left, right);
    }

    private boolean isReference(final Object obj) {
        return (obj instanceof ISqmlTableReference) || (obj instanceof ISqmlTableReferences);
    }

    /*public ModelFilter(QAbstractItemModel model){
    super();
    setSourceModel(model);
    this.setSortCaseSensitivity(CaseSensitivity.CaseInsensitive);
    }
    
    @Override
    protected boolean filterAcceptsRow(int sourceRow, QModelIndex parentIndex) {        
    QModelIndex index = sourceModel().index(sourceRow, 0, parentIndex);
    if (index!=null){
    QTreeModel model =(QTreeModel) sourceModel();
    Object value = model.indexToValue(index);
    if (value instanceof ISqmlColumnDef){
    if(checkProp((RadPropertyDef)value)){
    if(!isPropFilter)
    return true;
    }else
    return false;
    }else if (value instanceof RadEnumPresentationDef.Item){
    return true;
    }else{
    if((isPropFilter) &&(parentIndex==null))
    return true; 
    if(parentIndex!=null && parentIndex.parent()==null){
    final Object parentValue = model.indexToValue(parentIndex);
    if (parentValue instanceof RadClassPresentationDef&& !(value instanceof RadPropertyDef){
    //��� ���������� ������� �������� ������ ��������                        
    return ((ClassDefTreeModel)model).userClassTitle(parentIndex)==null;
    }
    }
    }
    }
    return super.filterAcceptsRow(sourceRow,parentIndex);
    //���� source_parent = null, ����� ���������� true
    }
    
    private boolean checkProp(RadPropertyDef prop){
    if(checkNature(prop) && checkType(prop))
    return true;
    return false;
    }
    
    private boolean checkNature(RadPropertyDef prop){
    if((prop.getNature()==EPropNature.USER)||(prop.getNature()==EPropNature.INNATE)||
    (prop.getNature()==EPropNature.DETAIL_PROP)||((prop.getNature()==EPropNature.PARENT_PROP)&&(checkNature(prop.getOrigProp()))))
    return true;
    return false;
    }
    
    private boolean checkType(RadPropertyDef prop){
    EValType type=prop.getType();
    if((type==EValType.OBJECT)||(type==EValType.ARR_BIN)||(type==EValType.ARR_BLOB)||
    (type==EValType.ARR_BOOL)||(type==EValType.ARR_CHAR)||(type==EValType.ARR_CLOB)||
    (type==EValType.ARR_DATE_TIME)||(type==EValType.ARR_INT)||(type==EValType.ARR_NUM)||
    (type==EValType.ARR_REF)||(type==EValType.ARR_STR))
    return false;
    return true;
    }
    
    @Override
    protected boolean lessThan(QModelIndex left, QModelIndex right) {
    if (left!=null && right!=null){
    QTreeModel model = (QTreeModel)sourceModel();
    Object leftValue = model.indexToValue(left);
    Object rightValue = model.indexToValue(right);
    if ((leftValue instanceof RadPropertyDef) && (rightValue  instanceof List))
    return true;
    if ((rightValue instanceof RadPropertyDef) && (leftValue instanceof List))//(leftValue instanceof ReferenceDef))
    return false;                
    }
    return super.lessThan(left, right);
    }*/
    public final void setPropFilter(final boolean flag) {
        if (flag != isPropFilter) {
            isPropFilter = flag;
        }
    }
}
