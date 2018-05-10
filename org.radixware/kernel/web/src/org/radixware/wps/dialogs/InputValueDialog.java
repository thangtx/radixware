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

package org.radixware.wps.dialogs;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.views.editors.valeditors.IValEditor;
import org.radixware.wps.views.editors.valeditors.ValEditorFactory;


public class InputValueDialog extends Dialog {
    
    IValEditor valEditor;
    
    public InputValueDialog (EValType valType, EditMask mask, String labelTitle, IClientEnvironment environment) {
        this(valType, mask, environment.getMessageProvider().translate("ExplorerDialog", "Input Value"), labelTitle, environment);
    }
    
     public InputValueDialog (EValType valType, EditMask mask, String windowTitle, String labelTitle, IClientEnvironment environment) {
        super(environment, "");
        this.setHeight(90);
        this.setMinimumHeight(90);
        this.setMaxHeight(90);
        this.setMinimumWidth(350);
        this.setWidth(350);
        this.valEditor = ValEditorFactory.getDefault().createValEditor(valType, mask, environment);
        addCloseAction(EDialogButtonType.OK).setDefault(true);
        addCloseAction(EDialogButtonType.CANCEL);
        this.setWindowTitle(windowTitle);
        InputBox valEditorInputBox = (InputBox)valEditor;
        if (labelTitle != null && !labelTitle.isEmpty() && !(labelTitle.charAt(labelTitle.length() - 1) == ':')) {
            labelTitle += ':'; 
        }
        valEditorInputBox.setLabel(labelTitle);
        valEditorInputBox.setLabelVisible(true);
        add((UIObject)valEditor);
        valEditorInputBox.setTop(5);
        valEditorInputBox.setLeft(5);
        valEditorInputBox.getAnchors().setRight(new Anchors.Anchor(1, -5));
     }

    @Override
    protected DialogResult onClose(String action, DialogResult actionResult) {
        if (actionResult == DialogResult.ACCEPTED && valEditor.getController().isMandatory() && valEditor.getValue() == null) {
            String errorMessageTitle = getEnvironment().getMessageProvider().translate("ExplorerDialog", "Value must be defined");
            String errorMessage = getEnvironment().getMessageProvider().translate("ExplorerDialog", "Please enter value");
            getEnvironment().messageError(errorMessageTitle, errorMessage);
            return null;
        } else {
            return super.onClose(action, actionResult); 
        }
    }
    
    
    
    public Object getValue() {
        if (this.execDialog()==Dialog.DialogResult.ACCEPTED){
            return valEditor.getValue();
        }else{
            return null;
        }
    }
    
    public void setMandatory(boolean isMandatory) {
        valEditor.getController().setMandatory(isMandatory);
    }
    
    public boolean isMandatory() {
        return valEditor.getController().isMandatory();
    }
    
    @SuppressWarnings("unchecked")
    public void setValue(Object value) {
        valEditor.setValue(value);
    }
    
    public IValEditor getValEditor() {
        return valEditor;
    }
}
