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

package org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs;

import com.trolltech.qt.core.QDir;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.env.Application;


public class SaveToFileDialog extends ExplorerDialog{    
    private QRadioButton rbSaveAllPages;
    private QRadioButton rbSaveCurrentPage;  
    private final ValEditor<String> edFile;   
    
    public SaveToFileDialog(final IClientEnvironment environment, MetricHistWidget parent){
         super(environment,parent, "SaveToFileDialog");
         this.setWindowTitle(Application.translate("SystemMonitoring", "Save to file"));
         edFile=new ValEditor<>(parent.getEnvironment(), this, new EditMaskNone(), false, true);
         createUi();
    }
    
    private void createUi(){
        this.setMinimumSize(100, 100);

        QHBoxLayout layout=new QHBoxLayout();
        QLabel lbSince = new QLabel(Application.translate("SystemMonitoring", "File") + ":", this);
        final QAction action = new QAction(this);
        action.triggered.connect(this, "actChooseFile()");        
        edFile.addButton("...", action);       
        layout.addWidget(lbSince);
        layout.addWidget(edFile);
        
        rbSaveAllPages=new QRadioButton(Application.translate("SystemMonitoring" ,"Save all tabs"),this);
        rbSaveAllPages.toggled.connect(this, "changeSaveMode(boolean)");        
        rbSaveCurrentPage=new QRadioButton(Application.translate("SystemMonitoring" ,"Save current tab"),this);
        rbSaveCurrentPage.toggled.connect(this, "changeSaveMode(boolean)"); 
        rbSaveCurrentPage.setChecked(false);
        rbSaveAllPages.setChecked(true);       

        dialogLayout().setContentsMargins(10, 10, 10, 10);
        dialogLayout().addLayout(layout);
        dialogLayout().addWidget(rbSaveAllPages);
        dialogLayout().addWidget(rbSaveCurrentPage);
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true).get(EDialogButtonType.OK).setEnabled(false);
        
        this.setWindowModality(Qt.WindowModality.WindowModal);
        this.setVisible(true);
    }
    
    @SuppressWarnings("unused")
    private void changeSaveMode(boolean b){
       if(rbSaveAllPages.isChecked()){
           rbSaveCurrentPage.setChecked(false);
       }else{
           rbSaveAllPages.setChecked(false);
       }
    } 
    
    protected void actChooseFile(){
        String filename = QFileDialog.getSaveFileName(this,"Export to File",QDir.homePath(),new com.trolltech.qt.gui.QFileDialog.Filter("XML Files (*.xml)"));
        getButton(EDialogButtonType.OK).setEnabled(filename!=null && !filename.isEmpty());
        edFile.setValue(filename);
    }  
    
    public String getFileName(){
        return edFile.getValue();
    }
    
    public boolean saveCurrentPage(){
        return rbSaveCurrentPage.isChecked();
    }
    
    @Override
    public void accept() {        
        saveGeometryToConfig();        
        super.accept();
    }

    @Override
    public void reject() {
        super.reject();
    }

}

