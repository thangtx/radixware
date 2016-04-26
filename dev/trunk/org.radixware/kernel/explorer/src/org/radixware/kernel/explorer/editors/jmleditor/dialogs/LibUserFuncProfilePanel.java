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

package org.radixware.kernel.explorer.editors.jmleditor.dialogs;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodParameters;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.env.Application;


public class LibUserFuncProfilePanel  extends QWidget{
    private QTableWidget tableWidget;
    private final JmlEditor editor;
    private AdsMethodParameters params;
    //private boolean isReadOnly=false;
    
    private final static int PARAM_TYPE_COLUMN=0;
    private final static int RAPAM_NAME_COLUMN=1;
    //private final static int RAPAM_VALUE_COLUMN=2;
    
    LibUserFuncProfilePanel(final JmlEditor editor, final AdsMethodParameters params, final List<String> paramsVal){
        this.editor=editor;
        this.params=params;
        createTable(/*paramsVal*/);
    }
    
    LibUserFuncProfilePanel(final JmlEditor editor){
        this.editor=editor;
        //this.params=params;
        createTable(/*null*/);
    }
    
    public void setParams(final AdsMethodParameters params,final List<String> paramsVal){
        this.params=params;
        fillTable(/*paramsVal*/);
    }
    
    void setReadOnly(final boolean isReadOnly){
        tableWidget.setEnabled(!isReadOnly);
    }
    
    private void createTable(/*List<String> paramsVal*/){ 
        final QVBoxLayout layout = new QVBoxLayout();
        tableWidget=new QTableWidget(this);
        final List<String> columnName = new ArrayList<>();
        columnName.add(Application.translate("JmlEditor", "Type"));
        columnName.add(Application.translate("JmlEditor", "Name"));
        //columnName.add(Application.translate("JmlEditor", "Created/Modified"));
        //tableWidget.currentItemChanged.connect(this, "tableCellChanged()");
        tableWidget.setColumnCount(2);
        tableWidget.setHorizontalHeaderLabels(columnName);
        tableWidget.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
        tableWidget.verticalHeader().setVisible(false);
        tableWidget.setSelectionMode(QAbstractItemView.SelectionMode.SingleSelection);

        final ParamValItemDelegate paramValItemDelegate = new ParamValItemDelegate();
        tableWidget.setItemDelegateForColumn(RAPAM_NAME_COLUMN, paramValItemDelegate);
        
        fillTable(/*paramsVal*/);
        tableWidget.resizeColumnsToContents();
        tableWidget.horizontalHeader().setResizeMode(PARAM_TYPE_COLUMN, QHeaderView.ResizeMode.ResizeToContents);
        tableWidget.horizontalHeader().setResizeMode(RAPAM_NAME_COLUMN, QHeaderView.ResizeMode.Stretch);
        layout.addWidget(tableWidget);
        this.setLayout(layout);
    }
      
    private void fillTable(/*List<String> paramsVal*/){
        tableWidget.setRowCount(0);
        if(params!=null){
            int rowCount=0;
            //TreeSet<String> keys = new TreeSet<String>(srcVersions.keySet());
            for(MethodParameter parameter:params){
                addRow( parameter.getType().getQualifiedName(editor.getUserFunc()), parameter.getName(),rowCount/*,paramsVal*/) ;
                rowCount++;
            }
            //tableWidget.sortByColumn(LAST_CHANGE_DATETIME_COLUMN, Qt.SortOrder.DescendingOrder);
            //if(tableWidget.currentItem()==null){
            //     getButton(EDialogButtonType.OPEN).setEnabled(false);
            //     getButton(EDialogButtonType.OK).setEnabled(false);
            //}
        }
    }
    
    public List<String> getParamVals(){
        final List<String> vals=new LinkedList<>();
        for(int i=0;i<tableWidget.rowCount();i++){
             final QTableWidgetItem itemType=tableWidget.item(i, RAPAM_NAME_COLUMN);
             vals.add(itemType.text());
        }
        return vals;
    }
    
    private void addRow(final String paramType, final String paramName, final int rowCount/*,List<String> paramsVal*/){        
        tableWidget.insertRow(rowCount);
        final QTableWidgetItem itemType=new  QTableWidgetItem(paramType);
        itemType.setData(Qt.ItemDataRole.UserRole, paramType);
        itemType.setData(Qt.ItemDataRole.DisplayRole, paramType);
        itemType.setFlags(new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled,Qt.ItemFlag.ItemIsSelectable));
        tableWidget.setItem(rowCount, PARAM_TYPE_COLUMN, itemType);
        
        final QTableWidgetItem itemName=new  QTableWidgetItem(paramName);
        itemName.setData(Qt.ItemDataRole.UserRole, paramName);
        itemName.setData(Qt.ItemDataRole.DisplayRole, paramName);
        itemName.setFlags(new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled,Qt.ItemFlag.ItemIsSelectable));
        tableWidget.setItem(rowCount, RAPAM_NAME_COLUMN, itemName);
        
        /*String paramVal="";
        if(paramsVal!=null && paramsVal.size()>rowCount){
            paramVal=paramsVal.get(rowCount);
        }            
        QTableWidgetItem itemVal=new  QTableWidgetItem(paramVal);
        itemVal.setData(Qt.ItemDataRole.UserRole, paramVal);
        itemVal.setData(Qt.ItemDataRole.DisplayRole, paramVal);
        itemVal.setFlags(new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled,Qt.ItemFlag.ItemIsSelectable,Qt.ItemFlag.ItemIsEditable));
        tableWidget.setItem(rowCount, RAPAM_VALUE_COLUMN, itemVal);   */  
    }
    
    private class ParamValItemDelegate extends QItemDelegate {

        private ValStrEditor srcEditor;
        @Override
        public QWidget createEditor(final QWidget parent, final com.trolltech.qt.gui.QStyleOptionViewItem option, final QModelIndex index) {
           final EditMaskStr editMask = new EditMaskStr();
           editMask.setNoValueStr("");
           srcEditor=new ValStrEditor(editor.getEnvironment(),parent,editMask,true,false);
           srcEditor.setValue(tableWidget.item(index.row(), index.column()).text());
           return srcEditor;
        }

        @Override
        public void setModelData(final QWidget editor, final QAbstractItemModel model, final QModelIndex index) {
            super.setModelData(editor, model, index);
            tableWidget.item(index.row(), index.column()).setText(srcEditor.getValue());
        }
    }    
}
