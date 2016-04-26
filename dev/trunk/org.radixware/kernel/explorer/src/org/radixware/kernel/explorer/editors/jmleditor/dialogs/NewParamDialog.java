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

import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QToolButton;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.env.Application;


public class NewParamDialog extends ExplorerDialog{
    
    private final MethodParameter newParameter;
    private ValStrEditor edParamType;
    //private final List<MethodParameter> parametersList;
    private final ProfileDialog parent;
    
    NewParamDialog(final ProfileDialog parent/*,final List<MethodParameter> parametersList*/){
        super(parent.getEnvironment(),parent,"NewParamDialog"); 
        setWindowTitle(Application.translate("JmlEditor", "New Parameter"));
        this.parent=parent;
        newParameter=MethodParameter.Factory.newInstance();
        newParameter.setType(null);
        //this.parametersList=parametersList;
        createUi();
    }
    
    private void createUi(){
        final QGridLayout gridLayout=new QGridLayout();
        final QLabel lbName=new QLabel(this);
        lbName.setText(Application.translate("JmlEditor", "Name")+":");
        gridLayout.addWidget(lbName, 0, 0);
        
        final QLineEdit edName=new QLineEdit(this);    
        edName.textEdited.connect(this, "paramNameChanged(String)");
        gridLayout.addWidget(edName, 0, 1);
        edName.setText(newParameter.getName());
        
        final QLabel lbParamType=new QLabel(this);
        lbParamType.setText(Application.translate("JmlEditor", "Type")+":");
        gridLayout.addWidget(lbParamType, 1, 0);
        
        final EditMaskStr editMask=new EditMaskStr();
        editMask.setNoValueStr("<undefined>");
        edParamType=new ValStrEditor(getEnvironment(), this, editMask, true, false);
        final QToolButton btnEditReturnType = new QToolButton();
        btnEditReturnType.setObjectName("btnEditReturnType");
        btnEditReturnType.setText("...");
        btnEditReturnType.clicked.connect(this, "btnEditParamTypeClicked()"); 
        edParamType.addButton(btnEditReturnType);
        gridLayout.addWidget(edParamType, 1, 1);   
        
        dialogLayout().addLayout(gridLayout);
        
        addButton(EDialogButtonType.OK);
        addButton(EDialogButtonType.CANCEL);
        acceptButtonClick.connect(this,"accept()");
        rejectButtonClick.connect(this,"reject()");
        check();
    } 

    @Override
    public void reject() {
        super.reject();
    }
    
    @Override
    public void accept(){
       super.accept();
    }
    
    @SuppressWarnings("unused")
    private void paramNameChanged(final String name){
       newParameter.setName(name);
       check();
    }
    
    private void check(){
        final String paramName=newParameter.getName();
        if(newParameter.getType()!=AdsTypeDeclaration.UNDEFINED  && paramName!=null && !paramName.isEmpty()){
            /*for(MethodParameter p:parametersList){
                if(p.getName().equals(newParameter.getName()))
                    return false;
            }*/
            getButton(EDialogButtonType.OK).setEnabled(true);
        }else{         
            getButton(EDialogButtonType.OK).setEnabled(false);
        }
    }
    
    MethodParameter getNewParameter(){        
        return newParameter;
    }
    
    @SuppressWarnings("unused")
    private void btnEditParamTypeClicked(){
       newParameter.setType(parent.getType());
       edParamType.setValue(newParameter.getType().getQualifiedName(parent.getUserFunc()));
       check();
    }   
    
}
