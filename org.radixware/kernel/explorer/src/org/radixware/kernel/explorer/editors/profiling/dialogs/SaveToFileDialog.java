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

package org.radixware.kernel.explorer.editors.profiling.dialogs;

import com.trolltech.qt.core.QDir;
import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.gui.QAccessible.Role;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QVBoxLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.profiling.ProfilerWidget;
import org.radixware.kernel.explorer.editors.profiling.ProfilerWidget.ColumnHeaderInfo;
import org.radixware.kernel.explorer.editors.profiling.SummaryTree;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.env.Application;



public class SaveToFileDialog extends ExplorerDialog {

    private ValEditor<String> edFileName;
    private QListWidget columnListWidget;
    private QCheckBox cbTreeMode;
    private QCheckBox cbFlatMode;
    private QCheckBox cdContextMode;
    private final ProfilerWidget editor;
    private final boolean isSummaryTree; 

    public SaveToFileDialog(final ProfilerWidget editor) {
        super(editor.getEnvironment(), editor, "SaveToFileDialod");
        this.editor = editor;
        isSummaryTree=editor.getCurTree() instanceof SummaryTree;
        this.setWindowTitle(Application.translate("ProfilerDialog", "Save to File"));
        createUi();
    }

    private void createUi() {

        final QLabel lbFileName = new QLabel(Application.translate("ProfilerDialog", "File Name:"), this);
        edFileName = new ValEditor<>(getEnvironment(), this, new EditMaskNone(), false, true);
        edFileName.setObjectName("editLine");
        edFileName.setReadOnly(true);
        final QAction action = new QAction(this);
        action.triggered.connect(this, "actChooseFile()");
        edFileName.addButton("...", action);

        final QLabel lbColumns = new QLabel(Application.translate("ProfilerDialog", "Columns:"), this);        
        columnListWidget = new QListWidget(this);   
        fillList();
        /*Map<QTreeWidgetItem, Boolean> colums = editor.getColumnsForCurTree();
        for (QTreeWidgetItem item : colums.keySet()) {
            QListWidgetItem listItem = new QListWidgetItem();
            listItem.setText(item.text(column));
            //listItem.setToolTip(columnName);
            CheckState checkState = colums.get(columnName) ? CheckState.Checked : CheckState.Unchecked;
            listItem.setCheckState(checkState);
            columnListWidget.addItem(listItem);
        }*/

         QGroupBox groupBox = null;
        if(isSummaryTree){
            cbTreeMode = new QCheckBox(Application.translate("ProfilerDialog", "Classification Tree"), this);
            cbTreeMode.setChecked(true);
            cbFlatMode = new QCheckBox(Application.translate("ProfilerDialog", "Flat List"), this);
            cbFlatMode.setChecked(true);
            cdContextMode = new QCheckBox(Application.translate("ProfilerDialog", "Context Tree"), this);
            cdContextMode.setChecked(true);        

            groupBox = new QGroupBox(Application.translate("ProfilerDialog", "Mode:"), this);
            final QVBoxLayout layout = new QVBoxLayout();
            layout.addWidget(cbTreeMode);
            layout.addWidget(cbFlatMode);
            layout.addWidget(cdContextMode);
            groupBox.setLayout(layout);
            
            cbTreeMode.stateChanged.connect(this, "modeSelectionChanged()");
            cbFlatMode.stateChanged.connect(this, "modeSelectionChanged()");
            cdContextMode.stateChanged.connect(this, "modeSelectionChanged()");
        }

        dialogLayout().addWidget(lbFileName);
        dialogLayout().addWidget(edFileName);
        dialogLayout().addWidget(lbColumns);
        dialogLayout().addWidget(columnListWidget);
        if(groupBox!=null){
            dialogLayout().addWidget(groupBox);
        }
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true).get(EDialogButtonType.OK).setEnabled(false);

        //this.setWindowModality(WindowModality.WindowModal);
    }
    
     private void fillList(){
        final List<ColumnHeaderInfo> columnHeaderInfos=editor.getHeaderItem();
        for (int i = 0; i < columnHeaderInfos.size(); i++) {
            final QListWidgetItem listItem = new QListWidgetItem();
            final ColumnHeaderInfo columnHeaderInfo=columnHeaderInfos.get(i);
            listItem.setText(columnHeaderInfo.getText());
            listItem.setToolTip(columnHeaderInfo.getToolTip());
            final CheckState checkState = !columnHeaderInfo.getIsHidden() ? CheckState.Checked : CheckState.Unchecked;
            listItem.setCheckState(checkState);
            listItem.setData(Role.UserRole.value(), i+1);
            columnListWidget.addItem(listItem);                       
        }
     }

   @SuppressWarnings("unused")
    private void  modeSelectionChanged(){
       /*if((cbTreeMode.checkState()==CheckState.Checked ||
          cbFlatMode.checkState()==CheckState.Checked ||
          cdContextMode.checkState()==CheckState.Checked)&&edFileName.get)
            buttonBox.button(StandardButton.Ok).setEnabled(true);
       else*/
            getButton(EDialogButtonType.OK).setEnabled(canAccept());
    }

    @SuppressWarnings("unused")
    private void actChooseFile(){
        final String filename = QFileDialog.getSaveFileName(editor,"Save to File",QDir.homePath(),new com.trolltech.qt.gui.QFileDialog.Filter("HTML Files (*.html)"));
        //chooseFile(QFileDialog.FileMode.AnyFile,QFileDialog.AcceptMode.AcceptOpen,);
        if(filename!=null && !filename.isEmpty()){
            edFileName.setValue(filename);
        }
        getButton(EDialogButtonType.OK).setEnabled(canAccept());
    }

    private boolean canAccept(){
        if((edFileName.getValue()!=null) &&(!isSummaryTree || (cbTreeMode.checkState()==CheckState.Checked ||
          cbFlatMode.checkState()==CheckState.Checked ||
          cdContextMode.checkState()==CheckState.Checked)))
                return true;
        return false;
    }

   /* private String chooseFile(QFileDialog.FileMode fileMode,QFileDialog.AcceptMode acceptMode,String title){
        QFileDialog dialog=new QFileDialog(editor, title,QDir.homePath());
        dialog.setFileMode(fileMode);
	dialog.setAcceptMode(acceptMode);
        dialog.setDefaultSuffix("html");
        dialog.setFilter("*.html");
        if (dialog.exec()==QDialog.DialogCode.Accepted.value())
            return dialog.selectedFiles().get(0);
	else
            return null;
    }*/

    @Override
    public void accept(){
        saveGeometryToConfig();
        final boolean isTreeMode=!isSummaryTree || cbTreeMode.checkState()==CheckState.Checked;
        final boolean isFlatListMode=!isSummaryTree ||cbFlatMode.checkState()==CheckState.Checked;
        final boolean isContextTreeMode=!isSummaryTree ||cdContextMode.checkState()==CheckState.Checked;
        //List<String> columns=getColumns();
        final List<Integer> columnsIndex= getColumnsIndex();
        columnsIndex.add(0, 0);
        editor.saveToFile(edFileName.getValue(),isTreeMode,isFlatListMode,isContextTreeMode,columnsIndex);
        super.accept();
        final File f=new File(edFileName.getValue());
        try {
            java.awt.Desktop.getDesktop().open(f);
        } catch (IOException ex) {
            Logger.getLogger(SaveToFileDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*private List<String> getColumns() {
        List<String> res = new ArrayList<String>();
        for (int i = 0; i < columnListWidget.count(); i++) {
            QListWidgetItem item = columnListWidget.item(i);
            if (item.checkState() == CheckState.Checked) {
                res.add(item.text());
            }
        }
        return res;
    }*/
    
    private List<Integer> getColumnsIndex() {
        final List<Integer> res = new ArrayList<>();
        for (int i = 0; i < columnListWidget.count(); i++) {
            final QListWidgetItem item = columnListWidget.item(i);
            if (item.checkState() == CheckState.Checked) {
                res.add((Integer)item.data(Role.UserRole.value()));
            }
        }
        return res;
    }
}
