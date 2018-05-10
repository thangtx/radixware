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
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlSelectorPresentationDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;


final class SelectorPresentationAttributeEditor extends AbstractAttributeEditor<ISqmlSelectorPresentationDef> {

    private final QLabel lbSelPresLabel;
    private final ValSelPresEditor editor;

    protected SelectorPresentationAttributeEditor(final IClientEnvironment environment, final boolean isReadonly, final QWidget parent) {
        super(environment);
        lbSelPresLabel = new QLabel(getAttribute().getTitle(environment), parent);
        lbSelPresLabel.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Fixed);
        lbSelPresLabel.setObjectName("lbSelPresLabel");
        editor = new ValSelPresEditor(environment, parent, isReadonly);
        editor.setObjectName("editor");
        lbSelPresLabel.setBuddy(editor);
        updateSettings();
        editor.setSizePolicy(QSizePolicy.Policy.Minimum, QSizePolicy.Policy.Fixed);
        editor.valueChanged.connect(this, "onValueChanged()");
    }

    @SuppressWarnings("unused")
    private void onValueChanged() {
        attributeChanged.emit(this);
        updateSettings();
    }

    private void updateSettings() {
        final ETextOptionsMarker lbMarker;
        if (editor.isReadOnly()) {
            lbMarker = ETextOptionsMarker.READONLY_VALUE;
        } else if (editor.getValue() == null) {
            lbMarker = ETextOptionsMarker.MANDATORY_VALUE;
        } else {
            lbMarker = ETextOptionsMarker.REGULAR_VALUE;
        }
        ExplorerTextOptions.Factory.getLabelOptions(lbMarker).applyTo(lbSelPresLabel);
    }

    @Override
    public boolean updateParameter(final ISqmlParameter parameter) {
        if (editor.isVisible() && (parameter instanceof ISqmlModifiableParameter)) {
            final ISqmlSelectorPresentationDef presentationDef = getAttributeValue();
            if (presentationDef == null) {
                Application.messageInformation(Application.translate("SqmlEditor", "Selector Presentation Was Not Specified!"),
                        Application.translate("SqmlEditor", "Please choose selector presentation"));
                editor.setFocus();
                return false;
            } else {
                final Id classId = presentationDef.getOwnerClassId();
                ((ISqmlModifiableParameter) parameter).setParentSelectorPresentation(classId, presentationDef.getId());
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void updateEditor(final ISqmlParameter parameter) {
        final EValType valType = parameter.getType();
        if (valType == EValType.PARENT_REF || valType == EValType.ARR_REF) {
            final Id presentationId = parameter.getParentSelectorPresentationId();
            if (presentationId != null) {
                editor.setPresentation(parameter.getParentSelectorPresentationClassId(), presentationId);
            }
        } else {
            setVisible(false);
        }
    }

    @Override
    public EFilterParamAttribute getAttribute() {
        return EFilterParamAttribute.SELECTOR_PRESENTATION;
    }

    @Override
    public ISqmlSelectorPresentationDef getAttributeValue() {
        return editor.getValue();
    }

    @Override
    public EnumSet<EFilterParamAttribute> getBaseAttributes() {
        return EnumSet.of(EFilterParamAttribute.VALUE_TYPE);
    }

    @Override
    public void onBaseAttributeChanged(final AbstractAttributeEditor baseEditor) {
        if (baseEditor.getAttribute() == EFilterParamAttribute.VALUE_TYPE) {
            final ValueTypeAttributeEditor valTypeEditor =
                    (ValueTypeAttributeEditor) baseEditor;
            final EValType valType = valTypeEditor.getAttributeValue();
            setVisible(valType == EValType.PARENT_REF || valType == EValType.ARR_REF);
        }
    }

    private void setVisible(final boolean isVisible) {
        editor.setVisible(isVisible);
        lbSelPresLabel.setVisible(isVisible);
    }

    @Override
    public QLabel getLabel() {
        return lbSelPresLabel;
    }

    @Override
    public QWidget getEditorWidget() {
        return editor;
    }

    @Override
    public void free() {
        editor.close();
    }
}
