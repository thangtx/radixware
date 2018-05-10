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

package org.radixware.kernel.explorer.editors.sqmleditor;

import com.trolltech.qt.gui.QFormLayout;
import com.trolltech.qt.gui.QLabel;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditorFactory;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.schemas.xscml.Sqml;


final class InputPreprocessorParamValuesDialog extends ExplorerDialog{
    
    private final List<ISqmlParameter> preprocessorParams = new LinkedList<>();
    private final Map<ISqmlParameter, QLabel> paramLabels = new HashMap<>();
    private final Map<ISqmlParameter, ValEditor> paramEditors = new HashMap<>();
    private final Map<Id,Object> paramValues = new HashMap<>();
    private final ExplorerTextOptions regularParamLabelTextOptions;
    private final ExplorerTextOptions mandatoryParamLabelTextOptions;

    public InputPreprocessorParamValuesDialog(final IClientEnvironment environment,
                                                  final SqmlEditor sqmlEditor) {
        super(environment, sqmlEditor, false);
        fillPreprocessorParams(sqmlEditor.getSqml(), sqmlEditor.getParamters());
        fillPreprocessorParams(sqmlEditor.getAdditionalFrom(), sqmlEditor.getParamters());
        final SqmlProcessor processor =  sqmlEditor.getSqmlProcessor();
        final EDefinitionDisplayMode displayMode = processor==null ? EDefinitionDisplayMode.SHOW_SHORT_NAMES : processor.getShowMode();        
        regularParamLabelTextOptions = 
            ExplorerTextOptions.Factory.getOptions(ETextOptionsMarker.LABEL, ETextOptionsMarker.EDITOR, ETextOptionsMarker.REGULAR_VALUE);
        mandatoryParamLabelTextOptions = 
            ExplorerTextOptions.Factory.getOptions(ETextOptionsMarker.LABEL, ETextOptionsMarker.EDITOR, ETextOptionsMarker.MANDATORY_VALUE);
        setupUi(displayMode);
        updateLabelSettings();
        updateOKButton();
    }
    
    private void setupUi(final EDefinitionDisplayMode displayMode){
        if (preprocessorParams.size()==1){
            setWindowTitle(getEnvironment().getMessageProvider().translate("SqmlEditor", "Input Parameter Value"));
        }else{
            setWindowTitle(getEnvironment().getMessageProvider().translate("SqmlEditor", "Input Parameter Values"));
        }
        setWindowIcon(ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_SQML_TRAN));
        final QFormLayout mainLayout = new QFormLayout();
        mainLayout.setContentsMargins(0, 0, 0, 0);
        for (ISqmlParameter parameter: preprocessorParams){
            mainLayout.addRow(createParamLabel(parameter, displayMode), createParamEditor(parameter));            
        }
        dialogLayout().addLayout(mainLayout);
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
        setAutoHeight(true);
        setDisposeAfterClose(true);
    }
    
    private QLabel createParamLabel(final ISqmlParameter parameter, final EDefinitionDisplayMode displayMode){        
        final String text;
        switch (displayMode){
            case SHOW_SHORT_NAMES:{
                text = parameter.getShortName();
                break;
            }
            case SHOW_FULL_NAMES:{
                text = parameter.getFullName();
                break;
            }
            case SHOW_TITLES:{
                text = parameter.getTitle();
                break;
            }
            default:
                text = parameter.getShortName();                
        }        
        final QLabel label = new QLabel(text+":", this);
        paramLabels.put(parameter, label);
        return label;
    }
    
    @SuppressWarnings("unchecked")
    private ValEditor createParamEditor(final ISqmlParameter parameter){
        final ValEditor editor = 
            ValEditorFactory.getDefault().createValEditor(parameter.getType(), parameter.getEditMask(), getEnvironment(), this);
        final ValAsStr valAsStr = parameter.getInitialVal();
        final Object initialValue = valAsStr==null ? null : ValueConverter.valAsStr2Obj(valAsStr, parameter.getType());
        if (initialValue==null){
            paramValues.put(parameter.getId(), null);
        }else{
            editor.setValue(initialValue);
            paramValues.put(parameter.getId(), initialValue);
        }
        editor.valueChanged.connect(this, "onParamValueChanged()");
        editor.setMandatory(parameter.isMandatory());
        paramEditors.put(parameter, editor);
        return editor;
    }
    
    private void fillPreprocessorParams(final Sqml sqml,
                                                         final ISqmlParameters params){
        final List<Sqml.Item> sqmlItems = sqml==null ? Collections.<Sqml.Item>emptyList() : sqml.getItemList();
        Sqml.Item item;
        for (int i=0,size=sqmlItems.size(); i<size; i++) {
            item = sqmlItems.get(i);
            if (item.isSetIfParam()) {
                final ISqmlParameter parameter = params.getParameterById(item.getIfParam().getParamId());
                if (parameter!=null && !preprocessorParams.contains(parameter)){
                    preprocessorParams.add(parameter);
                }
            }
        }
    }
    
    private void updateLabelSettings(){
        QLabel label;
        for (ISqmlParameter parameter: preprocessorParams){
            label = paramLabels.get(parameter);
            if (hasValidMandatoryValue(parameter, paramValues.get(parameter.getId()))){
                regularParamLabelTextOptions.applyTo(label);                
            }else{
                mandatoryParamLabelTextOptions.applyTo(label);
            }
        }
    }
    
    private static boolean hasValidMandatoryValue(final ISqmlParameter parameter, final Object value){
        if (parameter.isMandatory() && value==null) {
            return false;
        }
        if (parameter.getEditMask() instanceof EditMaskStr && (value==null || value instanceof String)) {
            final EditMaskStr mask = (EditMaskStr) parameter.getEditMask();            
            return value == null || !((String)value).isEmpty() || mask.isEmptyStringAllowed();
        }
        return true;    
    }
    
    private void updateOKButton(){
        Object paramValue;
        for (ISqmlParameter parameter: preprocessorParams){
            paramValue = paramValues.get(parameter.getId());
            if (!hasValidMandatoryValue(parameter, paramValue)
               || parameter.getEditMask().validate(getEnvironment(), paramValue)!=ValidationResult.ACCEPTABLE){
                getButton(EDialogButtonType.OK).setEnabled(false);
                return;
            }
        }
        getButton(EDialogButtonType.OK).setEnabled(true);
    }
    
    private Object getParamValue(final ISqmlParameter parameter){
        return paramEditors.get(parameter).getValue();
    }
    
    @SuppressWarnings("unused")
    private void onParamValueChanged(){
        for (ISqmlParameter parameter: preprocessorParams){
            paramValues.put(parameter.getId(), getParamValue(parameter));
        }
        updateLabelSettings();
        updateOKButton();
    }
    
    public Map<Id,Object> getParamValues(){
        return new HashMap<>(paramValues);
    }

}
