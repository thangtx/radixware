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

package org.radixware.kernel.explorer.editors.filterparameditor;

import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.EditMaskRef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.editors.valeditors.ValListEditor;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;


final class ParentRefEditingModeAttributeEditor extends AbstractAttributeEditor<Boolean> {
    
    private final QLabel lbUseDropDownList;
    private final ValListEditor valUseDropDownList;
    private final ISqmlTableDef table;

    protected ParentRefEditingModeAttributeEditor(final IClientEnvironment environment, 
                             final boolean isReadonly, 
                             final ISqmlTableDef context,
                             final QWidget parent) {
        super(environment);
        lbUseDropDownList = new QLabel(getAttribute().getTitle(environment), parent);
        lbUseDropDownList.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Fixed);
        lbUseDropDownList.setObjectName("lbUseDropDownList");        
        valUseDropDownList = new ValListEditor(environment, parent, createEditMask(environment, null), true, isReadonly);
        valUseDropDownList.setObjectName("valUseDropDownList");
        lbUseDropDownList.setBuddy(valUseDropDownList);
        if (isReadonly) {
            ExplorerTextOptions.Factory.getLabelOptions(ETextOptionsMarker.READONLY_VALUE).applyTo(lbUseDropDownList);
        } else {
            ExplorerTextOptions.Factory.getLabelOptions(ETextOptionsMarker.REGULAR_VALUE).applyTo(lbUseDropDownList);
        }
        valUseDropDownList.setSizePolicy(QSizePolicy.Policy.Minimum, QSizePolicy.Policy.Fixed);
        valUseDropDownList.setValue(Boolean.FALSE.toString());
        valUseDropDownList.valueChanged.connect(this, "onValueChanged()");        
        table = context;
    }
    

    @SuppressWarnings("unused")
    private void onValueChanged() {
        attributeChanged.emit(this);
    }

    @Override
    public boolean updateParameter(final ISqmlParameter parameter) {
        if (parameter instanceof ISqmlModifiableParameter) {
            ((ISqmlModifiableParameter) parameter).setUseDropDownList(getAttributeValue());
        }
        return true;
    }

    @Override
    public void updateEditor(final ISqmlParameter parameter) {
        if (parameter instanceof ISqmlModifiableParameter){
            if (parameter.getBasePropertyId()==null){
                updateEditMask(null);
            }else{
                final ISqmlColumnDef baseColumn = table.getColumns().getColumnById(parameter.getBasePropertyId());
                if (baseColumn!=null && baseColumn.getEditMask() instanceof EditMaskRef){
                    updateEditMask(((EditMaskRef) baseColumn.getEditMask()).isUseDropDownList());
                }else{
                    updateEditMask(false);
                }
            }
            final Boolean value = ((ISqmlModifiableParameter)parameter).getUseDropDownList();
            valUseDropDownList.setValue(value==null ? null : value.toString());
        }else{
            final EditMask editMask = parameter.getEditMask();
            if (editMask instanceof EditMaskRef){
                if (((EditMaskRef)editMask).isUseDropDownList()){
                    valUseDropDownList.setValue(Boolean.TRUE.toString());
                }else{
                    valUseDropDownList.setValue(Boolean.FALSE.toString());
                }
            }else{
                valUseDropDownList.setValue(Boolean.FALSE.toString());
            }
        }        
    }

    @Override
    public EFilterParamAttribute getAttribute() {
        return EFilterParamAttribute.PARENT_REF_EDITING_MODE;
    }

    @Override
    public Boolean getAttributeValue() {
        if (valUseDropDownList.getValue()==null){
            return null;
        }else if (Boolean.TRUE.toString().equals(valUseDropDownList.getValue())){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    @Override
    public EnumSet<EFilterParamAttribute> getBaseAttributes() {
        return EnumSet.of(EFilterParamAttribute.VALUE_TYPE, EFilterParamAttribute.PROPERTY);
    }

    @Override
    public void onBaseAttributeChanged(final AbstractAttributeEditor linkedEditor) {
        if (linkedEditor instanceof ValueTypeAttributeEditor){
            final EValType valType = ((ValueTypeAttributeEditor)linkedEditor).getAttributeValue();
            if (valType==EValType.PARENT_REF){
                updateEditMask(null);                
                setVisible(true);                
            }else{
                setVisible(false);
            }
        }else if (linkedEditor instanceof TargetPropertyAttributeEditor){
            final ISqmlColumnDef column = ((TargetPropertyAttributeEditor)linkedEditor).getAttributeValue();
            final EValType valType = column.getType();
            if (valType==EValType.PARENT_REF){                
                final EditMask editMask = column.getEditMask();
                if (editMask instanceof EditMaskRef){
                    final boolean inheritedValue = ((EditMaskRef)editMask).isUseDropDownList();
                    updateEditMask(inheritedValue);
                }else{
                    updateEditMask(null);
                }
                setVisible(true);
            }else{
                setVisible(false);
            }
        }
    }
    
    private void setVisible(final boolean isVisible) {
        lbUseDropDownList.setVisible(isVisible);
        valUseDropDownList.setVisible(isVisible);
    }    

    @Override
    public QLabel getLabel() {
        return lbUseDropDownList;
    }

    @Override
    public QWidget getEditorWidget() {
        return valUseDropDownList;
    }

    @Override
    public void free() {
        valUseDropDownList.close();
    }    
    
    private void updateEditMask(final Boolean inheritedValue){
        valUseDropDownList.setEditMask(createEditMask(environment, inheritedValue));
    }
    
    private static EditMaskList createEditMask(final IClientEnvironment environment, final Boolean inheritedValue){
        final EditMaskList mask = new EditMaskList();
        final String dialogMode = environment.getMessageProvider().translate("SqmlEditor", "Modal dailog");
        final String listMode = environment.getMessageProvider().translate("SqmlEditor", "Drop down list");
        final String inheritance = environment.getMessageProvider().translate("SqmlEditor", "Inherit from property (%1$s)");
        if (inheritedValue!=null){
            if (inheritedValue){
                mask.addItem(String.format(inheritance, listMode), (String)null);
            }else{
                mask.addItem(String.format(inheritance, dialogMode), (String)null);
            }
        }
        mask.addItem(dialogMode, Boolean.FALSE.toString());
        mask.addItem(listMode, Boolean.TRUE.toString());
        return mask;
    }
}
