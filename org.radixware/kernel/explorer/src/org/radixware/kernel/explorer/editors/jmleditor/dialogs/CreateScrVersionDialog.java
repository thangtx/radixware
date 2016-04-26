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

import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import java.util.EnumSet;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;
import org.radixware.kernel.explorer.env.Application;


public class CreateScrVersionDialog extends ExplorerDialog{
     private String versionName;
     private boolean copySource=true;
     private final AdsUserFuncDef userFunc;
     
     public CreateScrVersionDialog(final JmlEditor editor,final AdsUserFuncDef userFunc, final String name) {
        super(editor.getEnvironment(), editor, name);
        this.userFunc=userFunc;
        this.setWindowTitle(Application.translate("JmlEditor", "Create Source Version"));
        createUI();
    }
     
     private void createUI() {
        this.setMinimumSize(100, 100);


        final QHBoxLayout hLayout=new QHBoxLayout();
        final QLabel lbName=new QLabel(Application.translate("JmlEditor", "Name")+": ",this);
        final QLineEdit edName=new QLineEdit(this);
        edName.textChanged.connect(this, "nameChanged(String)");        
        hLayout.addWidget(lbName);
        hLayout.addWidget(edName);
        
        final QCheckBox cbCopySource=new QCheckBox(this);
        cbCopySource.setText(Application.translate("JmlEditor", "Copy source code from edited version"));//Copy source from editing source version
        cbCopySource.stateChanged.connect(this, "cbCopySourceChanged(Integer)");
        cbCopySource.setCheckState(CheckState.Checked);
        
        dialogLayout().addLayout(hLayout);
        dialogLayout().addWidget(cbCopySource);
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true).get(EDialogButtonType.OK).setEnabled(false);

        //this.setWindowModality(WindowModality.WindowModal);
        edName.setFocus();
    }

    @Override
    public void accept() {
        if(userFunc.getSourceVersions().containsKey(versionName)){
            final String errorMessage=Application.translate("JmlEditor", "Source version with such name is already exists");
            Application.messageError(Application.translate("CertificateRequestDialog", "Input Error"), errorMessage);
        } else{
            super.accept();
        }
    }
     
     
    @SuppressWarnings("unused") 
     private void nameChanged(final String text){
         getButton(EDialogButtonType.OK).setEnabled(!text.isEmpty());
         versionName=text;
     }
     @SuppressWarnings("unused")
     private void cbCopySourceChanged(final Integer state){
         copySource = (state!=null && state.equals(CheckState.Checked.value()));
     }
     
     public String getVersionName(){
         return versionName;
     }
     
     public boolean isCopySource(){
         return copySource;
     }    
}
