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

package org.radixware.kernel.explorer.editors.filterparameditor;

import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.valeditors.ValEditMaskEditor;


final class EditMaskAttributeEditor extends AbstractAttributeEditor<EditMask>{
    
    private final QLabel lbEditMask;
    private final ValEditMaskEditor valEditMask;
    private EValType valType;
    
    private final static EnumSet<EValType> SUPPORTED_VAL_TYPES = 
            EnumSet.of(EValType.ARR_CHAR, EValType.ARR_DATE_TIME, EValType.ARR_INT, EValType.ARR_NUM,  EValType.ARR_STR,
                       EValType.CHAR,     EValType.DATE_TIME,     EValType.INT,      EValType.NUM,      EValType.STR,
                      EValType.BOOL);
    
    protected EditMaskAttributeEditor(IClientEnvironment environment, final boolean isReadonly, final QWidget parent) {
        super(environment);
        lbEditMask = new QLabel(getAttribute().getTitle(environment), parent);
        lbEditMask.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Fixed);
        lbEditMask.setObjectName("lbEditMask");
        valEditMask = new ValEditMaskEditor(environment, parent, EEditMaskType.STR);
        valEditMask.setReadOnly(isReadonly);
        valEditMask.setObjectName("valEditMask");
        lbEditMask.setBuddy(valEditMask);
        setupLabelTextOptions(lbEditMask, isReadonly);
        valEditMask.setSizePolicy(QSizePolicy.Policy.Minimum, QSizePolicy.Policy.Fixed);
        valEditMask.valueChanged.connect(this, "onValueChanged()");
        
    }
    
    @SuppressWarnings("unused")
    private void onValueChanged() {
        attributeChanged.emit(this);
    }    

    @Override
    public boolean updateParameter(final ISqmlParameter parameter) {
        if ((parameter instanceof ISqmlModifiableParameter) && isVisible()) {
            ((ISqmlModifiableParameter) parameter).setEditMask(valEditMask.getValue());
        }
        return true;
    }

    @Override
    public void updateEditor(final ISqmlParameter parameter) {
        if (parameter.getEditMask()==null || parameter.getEditMask().getType()==null){
            setVisible(false);
        }
        else{
            setVisible(true);
            valType = parameter.getType();
            if (parameter.getEnumId()==null){
                valEditMask.setValType(parameter.getType());
            }
            valEditMask.setValue(parameter.getEditMask());
        }
    }

    @Override
    public EFilterParamAttribute getAttribute() {
        return EFilterParamAttribute.EDIT_MASK;
    }

    @Override
    public EditMask getAttributeValue() {
        return valEditMask.getValue();
    }

    @Override
    public EnumSet<EFilterParamAttribute> getBaseAttributes() {
        return EnumSet.of(EFilterParamAttribute.ENUM, 
                          EFilterParamAttribute.PROPERTY, 
                          EFilterParamAttribute.VALUE_TYPE);
    }

    @Override
    public void onBaseAttributeChanged(AbstractAttributeEditor linkedEditor) {
        switch (linkedEditor.getAttribute()){
            case ENUM:{
                if (linkedEditor.getAttributeValue()==null){                                        
                    valEditMask.setValType(valType);
                }else{
                    final Id enumId = ((ISqmlEnumDef)linkedEditor.getAttributeValue()).getId();
                    final RadEnumPresentationDef enumDef = 
                            getEnvironment().getDefManager().getEnumPresentationDef(enumId);
                    valEditMask.setEnumDef(enumDef);
                }
                break;                
            }
            case PROPERTY:{
                final ISqmlColumnDef column = (ISqmlColumnDef)linkedEditor.getAttributeValue();
                if (column!=null){
                    if (column.getEditMask().getType()==null){
                        setVisible(false);
                    }
                    else{
                        setVisible(true);
                        valEditMask.setValType(column.getType());
                        valEditMask.setValue(column.getEditMask());
                    }
                }
                break;
            }
            case VALUE_TYPE:{
                final EValType valType = (EValType)linkedEditor.getAttributeValue();                
                if (valType!=null){
                    if (SUPPORTED_VAL_TYPES.contains(ValueConverter.serverValType2ClientValType(valType))){
                        setVisible(true);
                        this.valType = valType;
                        valEditMask.setValType(valType);
                    }
                    else{
                        setVisible(false);
                    }
                }
                break;
            }
        }
    }

    @Override
    public QLabel getLabel() {
        return lbEditMask;
    }

    @Override
    public QWidget getEditorWidget() {
        return valEditMask;
    }

    @Override
    public void free() {
        valEditMask.close();
    }

    private void setVisible(final boolean isVisible){
        lbEditMask.setVisible(isVisible);
        valEditMask.setVisible(isVisible);
    }
    
    private boolean isVisible(){
        return lbEditMask.isVisible();
    }
}
