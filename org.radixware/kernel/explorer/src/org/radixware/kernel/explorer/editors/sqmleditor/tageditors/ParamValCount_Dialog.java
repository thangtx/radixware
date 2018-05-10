/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.editors.sqmleditor.tageditors;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.sqmleditor.SqmlEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValListEditor;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class ParamValCount_Dialog extends ExplorerDialog{
    
    private final boolean isReadOnly;    
    private final ValListEditor selParameter;
    private Id selectedId;
    
    public ParamValCount_Dialog(final IClientEnvironment environment, final Id parameterId, final ISqmlParameters parameters, final XscmlEditor editText){
        super(environment,editText,null);
        isReadOnly = editText.isReadOnly();
        selParameter = 
            new ValListEditor(environment, this, createMaskList(parameters), true, isReadOnly);
        selectedId = parameterId;
        selParameter.setValue(parameterId==null ? null : parameterId.toString());
        setupUi();
    }
    
    private void setupUi(){
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
        setWindowTitle(getEnvironment().getMessageProvider().translate("SqmlEditor", "Count of values in parameter"));
        setWindowIcon(ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_PARAM_VAL_COUNT));
        final QHBoxLayout layout = new QHBoxLayout(this);
        final QLabel label = new QLabel(getEnvironment().getMessageProvider().translate("SqmlEditor", "Parameter:"),this);
        layout.addWidget(label);
        layout.addWidget(selParameter);
        dialogLayout().addLayout(layout);        
        final EnumSet<EDialogButtonType> buttons;
        if (isReadOnly){
            buttons = EnumSet.of(EDialogButtonType.CLOSE);
        }else{
            buttons = EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL);
        }
        addButtons(buttons, true);        
        updateButtons();
        selParameter.setMinimumWidth(200);
        selParameter.valueChanged.connect(this,"updateButtons()");
        setAutoSize(true);
    }
    
    private void updateButtons(){
        final IPushButton buttonOk = getButton(EDialogButtonType.OK);
        if (buttonOk!=null){
            selectedId = 
                selParameter.getValue()==null ? null : Id.Factory.loadFrom((String)selParameter.getValue());
            buttonOk.setEnabled(selectedId!=null && selParameter.getValidationResult()==ValidationResult.ACCEPTABLE);
        }
    }
    
    private static EditMaskList createMaskList(final ISqmlParameters parameters){        
        final List<EditMaskList.Item> items = new LinkedList<>();
        for (int i=0,count=parameters.size(); i<count; i++){
            final ISqmlParameter parameter = parameters.get(i);
            items.add(new EditMaskList.Item(parameter.getTitle(), parameter.getId().toString()));
        }
        return new EditMaskList(items);
    }
    
    public Id getSelectedId(){
        return selectedId;
    }
}