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
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.ItemDataRole;
import com.trolltech.qt.core.Qt.ItemFlag;
import com.trolltech.qt.core.Qt.ItemFlags;
import com.trolltech.qt.core.Qt.SortOrder;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QAbstractItemView.SelectionBehavior;
import com.trolltech.qt.gui.QAbstractItemView.SelectionMode;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QHeaderView.ResizeMode;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;
import org.radixware.kernel.explorer.editors.jmleditor.JmlProcessor;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.editors.xscmleditor.Highlighter;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class ChooseSrcVersionDialog extends ExplorerDialog{
   
    public  final static String CURRENT_SOURCE=Application.translate("JmlEditor", "<current>");
    private final static int VERSION_NAME_COLUMN=0;
    private final static int AUTHOR_COLUMN=1;
    private final static int LAST_CHANGE_DATETIME_COLUMN=2;
    
    private String curSrcVersionName;
    private QTableWidget tableWidget;
    private final Map<String, Jml>  srcVersions;
    private final List<String>  changedSrcVersions;
    private final List<String>  oldSrcVersions;
    private final AdsUserFuncDef userFunc;       
   
    private final XscmlEditor previewWidget;
    private final QPushButton btnDelete=new QPushButton();
    private boolean editVersionList=false;
    private boolean loadAsCurrentSource=false;
    
    public ChooseSrcVersionDialog(final JmlEditor editor, final String name, final AdsUserFuncDef userFunc, final boolean isReadOnlyMode){
        super(editor.getEnvironment(), editor, name);        
        srcVersions=new HashMap<>(userFunc.getSourceVersions());
        changedSrcVersions=new ArrayList<>();
        oldSrcVersions=new ArrayList<>();
        this.userFunc=userFunc;
        this.curSrcVersionName=editor.getEditingVersionName();
        this.setWindowTitle(Application.translate("JmlEditor", "Versions"));
        previewWidget = createPreviewWidget(editor);
        createUI(isReadOnlyMode);        
    }
    
    private void createUI(final boolean isReadOnlyMode) {
        this.setMinimumSize(100, 100);
        
        final QHBoxLayout tableLayout=new QHBoxLayout(); 
        btnDelete.setObjectName("btnDeleteSourceVersion");
        btnDelete.setParent(this);
        btnDelete.setToolTip(Application.translate("JmlEditor", "Delete Source Version"));
        btnDelete.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.DELETE));
        btnDelete.setIconSize(new QSize(22,22));
        btnDelete.clicked.connect(this, "deleteSourceVersion()");
        btnDelete.setEnabled(!isReadOnlyMode);
        btnDelete.setShortcut(QKeySequence.StandardKey.Delete);
              
        createTable();
        
        tableLayout.addWidget(tableWidget);
        tableLayout.addWidget(btnDelete,0,AlignmentFlag.AlignTop);
        dialogLayout().addLayout(tableLayout);
        
        dialogLayout().addWidget(previewWidget);
        
        final QAbstractButton btnLoadAsCurrent = (QAbstractButton)addButton(EDialogButtonType.OK);                
        btnLoadAsCurrent.setObjectName("btnLoadAsCurrent");        
        btnLoadAsCurrent.clicked.connect(this, "loadAsCurrent()");
        btnLoadAsCurrent.setText(Application.translate("JmlEditor", "Load as Current"));
        btnLoadAsCurrent.setEnabled(false);
        final QAbstractButton btnOpen = (QAbstractButton)addButton(EDialogButtonType.OPEN);
        btnOpen.setEnabled(false);
        btnOpen.clicked.connect(this, "accept()");
        addButton(EDialogButtonType.CANCEL);
        rejectButtonClick.connect(this,"reject()");
        tableCellChanged();
        
        //this.setWindowModality(WindowModality.WindowModal);
    }
    
    private XscmlEditor createPreviewWidget(JmlEditor editor) {
        final JmlProcessor converter=new JmlProcessor(editor);
        final XscmlEditor xscmlEditor=new XscmlEditor( getEnvironment(),  converter,  this);
        new Highlighter(getEnvironment(), xscmlEditor, converter, "org.radixware.explorer/S_E/SYNTAX_JML/");        
        xscmlEditor.setReadOnly(true);
        return xscmlEditor;
    }
        
    private void openJmlPreview(Jml jml) {
        final Jml jmlCopy = Jml.Factory.newCopy(userFunc, jml);
        previewWidget.getTagConverter().open(jmlCopy);
        previewWidget.open(userFunc);        
        jmlCopy.delete();
    }
    
    @SuppressWarnings("unused")
    private void deleteSourceVersion(){
        final String message=Application.translate("JmlEditor", "Do you really want to delete source version?");
        if(Application.messageConfirmation(message)){ 
            final int row=tableWidget.currentRow();
            final QTableWidgetItem item=tableWidget.item(row, VERSION_NAME_COLUMN);
            final String delVersionName=item.text();             
            if(userFunc.getSourceVersions().containsKey(delVersionName)){                
                userFunc.getSourceVersions().remove(delVersionName);
                ((JmlEditor)parent()).removeSourceVersion(delVersionName);                
            }
            srcVersions.remove(delVersionName);
            tableWidget.removeRow(row);
            if(tableWidget.rowCount()>0){
                tableWidget.setCurrentItem(tableWidget.item(0, VERSION_NAME_COLUMN));
            }  
        }
    }
    
    public  Map<String, Jml>  getSourceVersions(){
        return srcVersions;
    }
    
    public  List<String>  getChangedSourceVersions(){
        return changedSrcVersions;
    }
    
    public  List<String>  getOldSourceVersions(){
        return oldSrcVersions;
    }
  
    public boolean isLoadAsCurrent(){
        return loadAsCurrentSource;
    }  
    
    @SuppressWarnings("unused")
    private void tableCellChanged(){        
        final boolean notCurrentSelected= tableWidget.currentItem()!=null &&  
                !CURRENT_SOURCE.equals(tableWidget.item(tableWidget.currentRow(), VERSION_NAME_COLUMN).text());
        if (getButton(EDialogButtonType.OPEN)!=null){
            getButton(EDialogButtonType.OPEN).setEnabled(tableWidget.currentItem()!=null);
            getButton(EDialogButtonType.OK).setEnabled(notCurrentSelected);
        }
        btnDelete.setEnabled(notCurrentSelected);
        
        if (notCurrentSelected) {
            openJmlPreview(srcVersions.get(tableWidget.item(tableWidget.currentRow(), VERSION_NAME_COLUMN).text()));
        } else if (tableWidget.currentItem() != null) {
            openJmlPreview(userFunc.getSource());
        }
        tableWidget.setFocus();
    }
    
    protected  void createTable(){        
        tableWidget=new QTableWidget(this);
        final List<String> columnName = new ArrayList<>();
        columnName.add(Application.translate("JmlEditor", "Name"));
        columnName.add(Application.translate("JmlEditor", "Author"));
        columnName.add(Application.translate("JmlEditor", "Created/Modified"));
        tableWidget.currentItemChanged.connect(this, "tableCellChanged()");
        tableWidget.setColumnCount(3);
        tableWidget.setHorizontalHeaderLabels(columnName);
        tableWidget.setSelectionBehavior(SelectionBehavior.SelectRows);
        tableWidget.verticalHeader().setVisible(false);
        tableWidget.setSelectionMode(SelectionMode.SingleSelection);

        final SourceNameItemDelegate sourceNameItemDelegate = new SourceNameItemDelegate();
        tableWidget.setItemDelegateForColumn(VERSION_NAME_COLUMN, sourceNameItemDelegate);
        
        fillTable();
        tableWidget.resizeColumnsToContents();
        tableWidget.horizontalHeader().setResizeMode(VERSION_NAME_COLUMN, ResizeMode.ResizeToContents);
        tableWidget.horizontalHeader().setResizeMode(LAST_CHANGE_DATETIME_COLUMN, ResizeMode.Stretch);
    }
    
    private void fillTable(){  
        addRow( null, 0, true) ;
        if(srcVersions!=null){
            int rowCount=1;
            final TreeSet<String> keys = new TreeSet<>(srcVersions.keySet());
            for(String versionName:keys){
                addRow( versionName, rowCount,false) ;
                rowCount++;
            }
            tableWidget.sortByColumn(LAST_CHANGE_DATETIME_COLUMN, SortOrder.DescendingOrder);
            if(tableWidget.currentItem()==null){
                 getButton(EDialogButtonType.OPEN).setEnabled(false);
                 getButton(EDialogButtonType.OK).setEnabled(false);
            }
        }
    }
    
    private void finishEdit(){
         final QTableWidgetItem curItem=tableWidget.currentItem();
         tableWidget.setCurrentItem(null); 
         tableWidget.setCurrentItem(curItem); 
    }
    
    @Override
    public void accept() {
         finishEdit();
         if(editVersionList){            
            final String message=Application.translate("JmlEditor", "Save changes in source version list?");
            if(!Application.messageConfirmation(Application.translate("CertificateRequestDialog", "Save changes?"), message))
                editVersionList=false;
         }
         super.accept();
    }
     
     @Override
    public void reject() {
         finishEdit();
         if(editVersionList){            
             final String message=Application.translate("JmlEditor", "Save changes in source version list?");
             if(!Application.messageConfirmation(Application.translate("CertificateRequestDialog", "Save changes?"), message))
                editVersionList=false;
         }
         super.reject();
    }
     
    @SuppressWarnings("unused")     
    private void loadAsCurrent(){
         if(editVersionList){
            final String message=Application.translate("JmlEditor", "Save changes in source version list?");
            if(!Application.messageConfirmation(Application.translate("CertificateRequestDialog", "Save changes?"), message))
                editVersionList=false;
         }
         loadAsCurrentSource=true;
         accept();
    }
    
    protected void addRow(String versionName, final int rowCount,final boolean isNameReadOnly){        
        tableWidget.insertRow(rowCount);
        versionName= versionName==null ? CURRENT_SOURCE : versionName;
        final QTableWidgetItem itemName=new  QTableWidgetItem(versionName);
        itemName.setData(Qt.ItemDataRole.UserRole, versionName);
        itemName.setData(ItemDataRole.DisplayRole, versionName);
        if(isNameReadOnly)
            itemName.setFlags(new ItemFlags(ItemFlag.ItemIsEnabled,ItemFlag.ItemIsSelectable));
        else{
            itemName.setFlags(new ItemFlags(ItemFlag.ItemIsEnabled,ItemFlag.ItemIsSelectable,ItemFlag.ItemIsEditable));
        }
        tableWidget.setItem(rowCount, VERSION_NAME_COLUMN, itemName);
        if(curSrcVersionName==null && versionName.equals(CURRENT_SOURCE) || versionName.equals(curSrcVersionName)){
            tableWidget.setCurrentItem(itemName);
            tableCellChanged();
        }  

        if(CURRENT_SOURCE.equals(versionName))
            versionName=null;
        String lastUpdateUser=userFunc.getVersionUserName(versionName);
        lastUpdateUser=lastUpdateUser==null?"":lastUpdateUser;
        tableWidget.setItem(rowCount, AUTHOR_COLUMN, new  QTableWidgetItem(lastUpdateUser));   

        final Calendar lastUpdateTime=userFunc.getVersionUpdateTime(versionName);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        final String strLastUpdateTime=lastUpdateTime==null ? "" :  sdf.format(lastUpdateTime.getTime());
        final LastUpdateDataTimeItem itemLastUpdateTime=new  LastUpdateDataTimeItem(strLastUpdateTime);
        itemLastUpdateTime.setFlags(ItemFlag.ItemIsEnabled,ItemFlag.ItemIsSelectable);        
        tableWidget.setItem(rowCount, LAST_CHANGE_DATETIME_COLUMN, itemLastUpdateTime);       
    }
    
    
    public String getVersionName(){
        final int row=tableWidget.currentRow();
        final QTableWidgetItem item=tableWidget.item(row, VERSION_NAME_COLUMN);
        String name= item.text();
        if(!editVersionList){
            name=(String)item.data(Qt.ItemDataRole.UserRole);
        }     
        return CURRENT_SOURCE.equals(name) ? null : name;
    }
    
    public boolean isEditVersionList(){
        return editVersionList;
    }

     private class SourceNameItemDelegate extends QItemDelegate {

        private ValStrEditor srcEditor;
        @Override
        public QWidget createEditor(final QWidget parent, final com.trolltech.qt.gui.QStyleOptionViewItem option, final QModelIndex index) {
           final EditMaskStr editMask = new EditMaskStr();
           editMask.setNoValueStr("");
           srcEditor=new ValStrEditor(getEnvironment(),parent,editMask,true,false);
           srcEditor.setValue(tableWidget.item(index.row(), index.column()).text());
           return srcEditor;
        }

        @Override
        public void setModelData(final QWidget editor, final QAbstractItemModel model, final QModelIndex index) {
            final String oldName=tableWidget.item(index.row(), index.column()).text();
            super.setModelData(editor, model, index);

            final String newName=srcEditor.getValue();
            if(!newName.equals(oldName)){
                if(srcVersions.containsKey(newName)){
                    final String errorMessage=Application.translate("JmlEditor", "Source version with such name is already exists");
                    Application.messageError(Application.translate("JmlEditor", "Source Version Name Error"), errorMessage);
                    tableWidget.item(index.row(), index.column()).setText(oldName);
                    return;
                }
                editVersionList=true;
                srcVersions.put(newName, srcVersions.get(oldName));
                srcVersions.remove(oldName); 
                changedSrcVersions.add(newName);
                changedSrcVersions.remove(oldName);
                oldSrcVersions.add(oldName);

            }
            tableWidget.item(index.row(), index.column()).setText(srcEditor.getValue());
        }
    }
    
     private class LastUpdateDataTimeItem extends QTableWidgetItem {

        @Override
        public boolean operator_less(final QTableWidgetItem qtwi) {
            if(super.text()==null || super.text().isEmpty())
                return false;
            if(qtwi.text()==null || qtwi.text().isEmpty())
                return true;
            return super.operator_less(qtwi);
        }

        LastUpdateDataTimeItem(final String title) {
            this.setText(title );
        } 
    }
    
}
