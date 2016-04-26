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
import java.util.ArrayList;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitionsFilter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.valeditors.ValSqmlDefEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.models.SqmlTreeModel;


final class EnumAttributeEditor extends AbstractAttributeEditor<ISqmlEnumDef> {

    private final QLabel lbEnumLabel;
    private final ValSqmlDefEditor editor;

    private static class EnumFilter implements ISqmlDefinitionsFilter {

        private EValType enumItemType;

        public EnumFilter() {
        }

        public void setEnumItemType(final EValType itemType) {
            enumItemType = itemType;
        }

        @Override
        public boolean isAccepted(ISqmlDefinition definition, ISqmlDefinition ownerDefinition) {
            if (definition instanceof ISqmlEnumDef) {
                return enumItemType == null || ((ISqmlEnumDef) definition).getItemType() == enumItemType;
            }
            return true;
        }
    }
    private final EnumFilter filter = new EnumFilter();

    protected EnumAttributeEditor(IClientEnvironment environment, final boolean isReadonly, final QWidget parent) {
        super(environment);
        lbEnumLabel = new QLabel(getAttribute().getTitle(), parent);
        lbEnumLabel.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Fixed);
        lbEnumLabel.setObjectName("lbEnumLabel");
        final EnumSet<SqmlTreeModel.ItemType> items =
                EnumSet.of(SqmlTreeModel.ItemType.MODULE_INFO);
        final ArrayList<ISqmlDefinition> allEnums = new ArrayList<>();
        allEnums.addAll(environment.getSqmlDefinitions().getEnums());
        final SqmlTreeModel treeModel = new SqmlTreeModel(environment, allEnums, items);
        //treeModel.setShowModulesInfo(true);
        editor = new ValSqmlDefEditor(environment, parent, treeModel, false, isReadonly);
        editor.setObjectName("editor");
        editor.setDialogTitle(Application.translate("SqmlEditor", "Enumeration Definition Choice"));
        editor.setDefinitionsFilter(filter);
        lbEnumLabel.setBuddy(editor);
        setupLabelTextOptions(lbEnumLabel, isReadonly);
        editor.setSizePolicy(QSizePolicy.Policy.Minimum, QSizePolicy.Policy.Fixed);
        editor.valueChanged.connect(this, "onValueChanged()");
    }

    @SuppressWarnings("unused")
    private void onValueChanged() {
        attributeChanged.emit(this);
    }

    @Override
    public boolean updateParameter(ISqmlParameter parameter) {
        if (editor.isVisible() && (parameter instanceof ISqmlModifiableParameter)) {
            if (getAttributeValue() != null) {
                final Id enumId = getAttributeValue().getId();
                final EditMaskConstSet editMask = new EditMaskConstSet(enumId, null, null, null);
                ((ISqmlModifiableParameter) parameter).setEditMask(editMask);
            } else {
                ((ISqmlModifiableParameter) parameter).setEditMask(null);
            }
        }
        return true;
    }

    @Override
    public void updateEditor(ISqmlParameter parameter) {
        final EValType valType = parameter.getType();
        if (valType.isArrayType()) {
            filter.setEnumItemType(valType.getArrayItemType());
        } else {
            filter.setEnumItemType(valType);
        }
        final Id enumId = parameter.getEnumId();
        if (enumId != null) {
            final ISqmlEnumDef enumDef = getEnvironment().getSqmlDefinitions().findEnumById(enumId);
            if (enumDef==null){
                final String enumTypeName = getEnvironment().getMessageProvider().translate("SqmlEditor", "Enum");
                editor.setValue(getEnvironment().getSqmlDefinitions().createBrokenDefinition(enumId, enumTypeName));
            }
            else{
                editor.setValue(enumDef);
            }
        } else {
            editor.setValue(null);
        }
    }

    @Override
    public EFilterParamAttribute getAttribute() {
        return EFilterParamAttribute.ENUM;
    }

    @Override
    public EnumSet<EFilterParamAttribute> getBaseAttributes() {
        return EnumSet.of(EFilterParamAttribute.VALUE_TYPE);
    }

    @Override
    public void onBaseAttributeChanged(AbstractAttributeEditor baseEditor) {
        if (baseEditor.getAttribute() == EFilterParamAttribute.VALUE_TYPE) {
            final ValueTypeAttributeEditor valTypeEditor = (ValueTypeAttributeEditor) baseEditor;
            final EValType valType = valTypeEditor.getAttributeValue();
            if (valType == EValType.ARR_INT || valType == EValType.ARR_STR || valType == EValType.ARR_CHAR
                    || valType == EValType.INT || valType == EValType.STR || valType == EValType.CHAR || valType == EValType.ARR_BOOL
                    || valType == EValType.BOOL) {
                editor.setValue(null);
                if (valType.isArrayType()) {
                    filter.setEnumItemType(valType.getArrayItemType());
                } else {
                    filter.setEnumItemType(valType);
                }
                setVisible(true);
            } else {
                setVisible(false);
            }
        }
    }

    private void setVisible(final boolean isVisible) {
        lbEnumLabel.setVisible(isVisible);
        editor.setVisible(isVisible);
    }

    @Override
    public QLabel getLabel() {
        return lbEnumLabel;
    }

    @Override
    public QWidget getEditorWidget() {
        return editor;
    }

    @Override
    public void free() {
        editor.close();
    }

    @Override
    public ISqmlEnumDef getAttributeValue() {
        return editor.getValidationResult()==ValidationResult.ACCEPTABLE ? (ISqmlEnumDef) editor.getValue() : null;
    }
}
