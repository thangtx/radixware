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

package org.radixware.kernel.explorer.editors.monitoring.diagram;

import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.Application;


public class TabAddingDialog extends ExplorerDialog{
     private String varsionName;

     
     public TabAddingDialog(final IClientEnvironment environment, QWidget parent,String name,String dialogTitle) {
        super(environment, parent);
        this.setWindowTitle(dialogTitle);
        createUI(name);
    }
     
     private void createUI(String name) {
        this.setMinimumSize(100, 100);

        QHBoxLayout hLayout=new QHBoxLayout();
        QLabel lbName=new QLabel(Application.translate("SystemMonitoring", "Tab name")+": ",this);
        QLineEdit edName=new QLineEdit(this);
        edName.setText(name);
        edName.textChanged.connect(this, "nameChanged(String)");        
        hLayout.addWidget(lbName);
        hLayout.addWidget(edName);
        
        /*QCheckBox cbCopySource=new QCheckBox(this);
        cbCopySource.setText(Application.translate("JmlEditor", "Copy source from editing source version"));
        cbCopySource.stateChanged.connect(this, "cbCopyS*/
        
        dialogLayout().addLayout(hLayout);
        
        //dialogLayout().addWidget(cbCopySource);
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true).get(EDialogButtonType.OK).setEnabled(false);

        //this.setWindowModality(WindowModality.WindowModal);
        edName.setFocus();
    }

   /* @Override
    public void accept() {
        if(userFunc.getSourceVersions().containsKey(varsionName)){
            String errorMessage=Application.translate("JmlEditor", "Source version with such name is already exists");
            Application.messageError(Application.translate("CertificateRequestDialog", "Input Error"), errorMessage);
        } else{
            super.accept();
        }
    }*/
     
     
    @SuppressWarnings("unused") 
     private void nameChanged(String text){
         getButton(EDialogButtonType.OK).setEnabled(!text.isEmpty());
         varsionName=text;
     }
     
     public String getTabName(){
         return varsionName;
     }
   
}
