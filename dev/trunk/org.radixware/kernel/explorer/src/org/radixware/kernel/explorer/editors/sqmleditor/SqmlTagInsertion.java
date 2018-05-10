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

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QTextCursor;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReferences;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndexDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReference;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.SqmlTreeModelProxy;
import org.radixware.kernel.explorer.editors.xscmleditor.TagInfo;

import org.radixware.kernel.explorer.models.SqmlTreeModel;


public class SqmlTagInsertion {

    private SqmlEditor editor;
    private SqmlProcessor tagConverter;

    public SqmlTagInsertion(final SqmlEditor editor) {
        this.editor = editor;
        tagConverter = editor.getSqmlProcessor();
    }

    /*public void setPropTree(QTreeView propTree){
    this.propTree=propTree;
    }*/
    public void insertTag(final Object obj, final String tableAlias, final QModelIndex curIndex) {
        final List<ISqmlTableReference> path = getPath(obj, curIndex);
        final long pos = editor.getTextEditor().textCursor().position();
        //editText.prepareInsertNewTag();
        final TagInfo tag = tagConverter.createTag(obj, pos, path, tableAlias);
        if (tag != null) {
            editor.getTextEditor().insertTag(tag, "");
        }
    }

    public void insertParentCondition(final boolean isParentCondition, final List<Object> values, final ISqmlColumnDef prop, final ESqlConditionOperator operator, final String tableAlias, final QTextCursor tc) {
        long pos = tc.position();
        if (isParentCondition) {
            Reference ref = null;
            if ((values != null) && (!values.isEmpty())){
                final Object value = values.get(0);
                if (value instanceof Pid){
                    final Pid pid = (Pid) values.get(0);
                    try {
                        ref = new Reference(pid, pid.getDefaultEntityTitle(editor.getEnvironment().getEasSession()));
                    } catch (ServiceClientException ex) {
                        Logger.getLogger(SqmlTagInsertion.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                    }
                }else if (value instanceof Reference){
                    ref = (Reference)value;
                }
            }

            final TagInfo tag = tagConverter.createParentCondition(prop, operator, ref, pos, tableAlias);
            editor.getTextEditor().insertTag(tag, "");
        } else {
            //List<ISqmlOutgoingReference> path=getPath(prop,curIndex);
            TagInfo tag = tagConverter.createTag(prop, pos, null, tableAlias);
            editor.getTextEditor().insertTag(tag, "");
            editor.getTextEditor().insertText(operatorToStr(operator));
            if (values != null 
                && operator != ESqlConditionOperator.IS_NULL 
                && operator != ESqlConditionOperator.IS_NOT_NULL) {
                for (int i = 0, size = values.size(); i < size; i++) {
                    pos = tc.position();
                    Pid pid;
                    if (values.get(i) instanceof Reference) {
                        pid = ((Reference) values.get(i)).getPid();
                    } else {
                        pid = (Pid) values.get(i);
                    }
                    boolean isPrimaryKey = prop.getReferenceIndex().isPrimaryKey();
                    ISqmlTableIndexDef secondaryKey = null;
                    if (!isPrimaryKey) {
                        secondaryKey = prop.getOwnerTable().getIndices().getIndexById(prop.getReferenceIndex().getId());
                    }
                    final Id secondaryKeyId = secondaryKey == null ? null : secondaryKey.getId();
                    tag = tagConverter.createEntityRefValue(pid, pos, isPrimaryKey, secondaryKeyId);
                    editor.getTextEditor().insertTag(tag, "");
                    tc.insertText(insertSeparator(operator, i == (size - 1)));
                }
            }
        }
    }

    public void insertCondition(final List<Object> values, 
                                             final ISqmlColumnDef prop, 
                                             final ESqlConditionOperator operator, 
                                             final String tableAlias, 
                                             final QModelIndex curIndex) {
        long pos = editor.getTextEditor().textCursor().position();
        final List<ISqmlTableReference> path = getPath(prop, curIndex);
        TagInfo tag = tagConverter.createTag(prop, pos, path, tableAlias);
        editor.getTextEditor().insertTag(tag, "");
        editor.getTextEditor().insertText(operatorToStr(operator));
        if (values != null 
            && operator != ESqlConditionOperator.IS_NULL 
            && operator != ESqlConditionOperator.IS_NOT_NULL) {
            for (int i = 0, size = values.size(); i < size; i++) {
                //if(!isValEmptyString(prop.getType(),values.get(i))){
                pos = editor.getTextEditor().textCursor().position();
                final Object val = isValEmptyString(prop.getType(), values.get(i)) ? null : values.get(i);
                tag = tagConverter.createTypifiedValue(prop, val, pos);
                editor.getTextEditor().insertTag(tag, "");
                editor.getTextEditor().textCursor().insertText(insertSeparator(operator, i == (size - 1)));
                //}
            }
        }
    }
    
    public void insertParamValCountTag(final ISqmlParameter parameter){
        final long pos = editor.getTextEditor().textCursor().position();
        final TagInfo tag = tagConverter.createParamValCount(parameter, pos);
        if (tag != null) {
            editor.getTextEditor().insertTag(tag, "");
        }
    }

    private boolean isValEmptyString(final EValType propType, final Object val) {
        return ((((propType == EValType.STR) || (propType == EValType.ARR_STR))) && (val != null) && (val.equals("")));
    }

    private List<ISqmlTableReference> getPath(final Object obj, final QModelIndex curIndex) {
        List<ISqmlTableReference> path = null;
        if ((obj instanceof ISqmlColumnDef) && (curIndex != null)) {
            path = new ArrayList<>();
            QModelIndex parentIndex = curIndex.parent();
            Object parentItem = getObject(parentIndex);
            while ((parentItem instanceof ISqmlTableReference) || (parentItem instanceof ISqmlTableReferences)) {
                if (parentItem instanceof ISqmlTableReference) {
                    ISqmlTableReference refDef = (ISqmlTableReference) parentItem;
                    path.add(0, refDef);
                }
                parentIndex = parentIndex.parent();
                parentItem = getObject(parentIndex);
            }
            if (path.isEmpty()) {
                return null;
            }
        }
        return path;
    }

    private static Object getObject(final QModelIndex modelIndex) {
        final QModelIndex sourceIndex = ((SqmlTreeModelProxy) modelIndex.model()).mapToSource(modelIndex);
        final SqmlTreeModel model = (SqmlTreeModel) ((SqmlTreeModelProxy) modelIndex.model()).sourceModel();
        return model.data(sourceIndex, Qt.ItemDataRole.UserRole);
    }

    private String insertSeparator(final ESqlConditionOperator operator, final boolean isLast) {
        if (operator==ESqlConditionOperator.IN || operator==ESqlConditionOperator.NOT_IN){
            return isLast ? ") " : ", ";
        }else if (operator==ESqlConditionOperator.BETWEEN){
            return isLast ? "" : "and ";
        }else{
            return "";
        }
    }

    private static String operatorToStr(final ESqlConditionOperator operator) {
        if (operator==null){
            return "";
        }else if (operator==ESqlConditionOperator.IN || operator==ESqlConditionOperator.NOT_IN){
            return operator.getText()+" ( ";
        }else{
            return operator.getText()+" ";
        }
    }
    
    public SqmlEditor getEditor() {
        return editor;
    }
}
