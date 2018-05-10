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
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlSelectorPresentationDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.valeditors.ValSqmlEditor;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.schemas.xscml.Sqml;


final class AdditionalSelectorConditionAttributeEditor extends AbstractAttributeEditor<Sqml>{
    
    private final QLabel lbCondition;
    private final ValSqmlEditor valConditionEditor;
    
    public AdditionalSelectorConditionAttributeEditor(final IClientEnvironment environment, final boolean isReadonly, final QWidget parent){
        super(environment);
        lbCondition = new QLabel(getAttribute().getTitle(environment), parent);
        lbCondition.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Fixed);
        lbCondition.setObjectName("lbCondition");
        valConditionEditor = new ValSqmlEditor(environment, parent, false, isReadonly);
        valConditionEditor.setObjectName("valConditionEditor");
        lbCondition.setBuddy(valConditionEditor);
        updateSettings();
        valConditionEditor.setSizePolicy(QSizePolicy.Policy.Minimum, QSizePolicy.Policy.Fixed);
        valConditionEditor.setDialogTitle(environment.getMessageProvider().translate("SqmlEditor", "Additional Condition"));
        valConditionEditor.valueChanged.connect(this, "onValueChanged()");
    }
    
    private void updateSettings() {
        final ETextOptionsMarker lbMarker;
        if (valConditionEditor.isReadOnly()) {
            lbMarker = ETextOptionsMarker.READONLY_VALUE;
        } else {
            lbMarker = ETextOptionsMarker.REGULAR_VALUE;
        }
        ExplorerTextOptions.Factory.getLabelOptions(lbMarker).applyTo(lbCondition);
    }
    
    private void setVisible(final boolean isVisible){
        valConditionEditor.setVisible(isVisible);
        lbCondition.setVisible(isVisible);
    }
    
    private void setEnabled(final boolean isEnabled){
        valConditionEditor.setEnabled(isEnabled);
        lbCondition.setEnabled(isEnabled);
    }
    
    private void setupEditor(final EValType valType, final Id selectorPresentationId){
        if (valType == EValType.PARENT_REF || valType == EValType.ARR_REF) {
            setVisible(true);
            RadSelectorPresentationDef presentation=null;
            if (selectorPresentationId!=null){
                try{
                    presentation = getEnvironment().getDefManager().getSelectorPresentationDef(selectorPresentationId);
                }catch(DefinitionError error){
                   getEnvironment().getTracer().debug(error);               
                }
            }        
            if (presentation==null || !presentation.isCustomFiltersEnabled()){
                valConditionEditor.setValue(null);
                setEnabled(false);
            }else{
                setEnabled(true);
                valConditionEditor.setContextClassId(presentation.getOwnerClassId());
            }        
        }else{
            setVisible(false);
        }
    }
    
    @SuppressWarnings("unused")
    private void onValueChanged() {
        attributeChanged.emit(this);
        updateSettings();
    }

    @Override
    public boolean updateParameter(final ISqmlParameter parameter) {
        if (parameter instanceof ISqmlModifiableParameter) {
            ((ISqmlModifiableParameter)parameter).setParentSelectorAdditionalCondition(getAttributeValue());
        }
        return true;
    }

    @Override
    public void updateEditor(final ISqmlParameter parameter) {
        valConditionEditor.setValue(parameter.getParentSelectorAdditionalCondition());
        setupEditor(parameter.getType(), parameter.getParentSelectorPresentationId());
    }

    @Override
    public EFilterParamAttribute getAttribute() {
        return EFilterParamAttribute.ADDITIONAL_SELECTOR_CONDITION;
    }

    @Override
    public Sqml getAttributeValue() {
        return valConditionEditor.getValue();
    }

    @Override
    public EnumSet<EFilterParamAttribute> getBaseAttributes() {
        return EnumSet.of(EFilterParamAttribute.VALUE_TYPE, EFilterParamAttribute.PROPERTY, EFilterParamAttribute.SELECTOR_PRESENTATION);
    }

    @Override
    public void onBaseAttributeChanged(final AbstractAttributeEditor linkedEditor) {
        valConditionEditor.setValue(null);
        EValType valType = null;
        Id selectorPresentationId = null;
        if (linkedEditor instanceof ValueTypeAttributeEditor){
            valType = ((ValueTypeAttributeEditor)linkedEditor).getAttributeValue();
        }else if (linkedEditor instanceof TargetPropertyAttributeEditor){
            final ISqmlColumnDef column = ((TargetPropertyAttributeEditor)linkedEditor).getAttributeValue();
            valType = column.getType();
            selectorPresentationId = column.getSelectorPresentationId();
        }else if (linkedEditor instanceof SelectorPresentationAttributeEditor){
            valType = EValType.PARENT_REF;
            final ISqmlSelectorPresentationDef selectorPresDef = 
                    ((SelectorPresentationAttributeEditor)linkedEditor).getAttributeValue();
           selectorPresentationId = selectorPresDef==null ? null : selectorPresDef.getId();
        }
        setupEditor(valType, selectorPresentationId);
    }

    @Override
    public QLabel getLabel() {
        return lbCondition;
    }

    @Override
    public QWidget getEditorWidget() {
        return valConditionEditor;
    }

    @Override
    public void free() {
        valConditionEditor.close();
    }
}