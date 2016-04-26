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
import java.util.Collection;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.env.Application;

import org.radixware.kernel.explorer.utils.WidgetUtils;


final class NameAttributeEditor extends AbstractAttributeEditor<String> {

    private final QLabel label;
    private final ValStrEditor editor;
    private final Collection<String> restrictedNames;

    protected NameAttributeEditor(IClientEnvironment environment, final Collection<String> restrictedNames, final boolean isReadonly, final QWidget parent) {
        super(environment);
        this.restrictedNames = restrictedNames;
        setObjectName("attrEditor_" + getAttribute().name());
        label = new QLabel(getAttribute().getTitle(), parent);
        label.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Fixed);
        label.setObjectName("label");
        editor = new ValStrEditor(environment, parent, new EditMaskStr(), true, isReadonly);
        editor.setObjectName("editor");
        label.setBuddy(editor);
        editor.setSizePolicy(QSizePolicy.Policy.Minimum, QSizePolicy.Policy.Fixed);
        editor.valueChanged.connect(this, "onValueChanged()");
        updateLabelTextOptions();
        
    }

    @SuppressWarnings("unused")
    private void onValueChanged() {
        updateLabelTextOptions();
        attributeChanged.emit(this);
    }
    
    private void updateLabelTextOptions(){
        final ETextOptionsMarker marker;
        if (editor.getValue() == null || editor.getValue().isEmpty()) {
            marker = ETextOptionsMarker.MANDATORY_VALUE;            
        } else if (editor.isReadOnly()) {
            marker = ETextOptionsMarker.READONLY_VALUE;            
        } else {
            marker = ETextOptionsMarker.LABEL;
        }
        WidgetUtils.applyTextOptions(label,marker);        
    }

    @Override
    public boolean updateParameter(ISqmlParameter parameter) {
        if (!(parameter instanceof ISqmlModifiableParameter)) {
            return true;
        }
        if (editor.getValue() == null || editor.getValue().isEmpty()) {
            Application.messageInformation(Application.translate("SqmlEditor", "Parameter Name Was Not Specified!"),
                    Application.translate("SqmlEditor", "Please enter parameter name"));
            editor.setFocus();
            return false;
        } else if (restrictedNames != null && restrictedNames.contains(editor.getValue())) {
            Application.messageInformation(Application.translate("SqmlEditor", "Can't save condition parameter!"),
                    Application.translate("SqmlEditor", "Parameter with such name is already exist"));
            editor.setFocus();
            return false;
        } else {
            ((ISqmlModifiableParameter) parameter).setTitle(editor.getValue());
            return true;
        }
    }

    @Override
    public void updateEditor(ISqmlParameter parameter) {
        editor.setValue(parameter.getTitle());
    }

    @Override
    public EFilterParamAttribute getAttribute() {
        return EFilterParamAttribute.NAME;
    }

    @Override
    public String getAttributeValue() {
        return editor.getValue();
    }

    @Override
    public EnumSet<EFilterParamAttribute> getBaseAttributes() {
        return EnumSet.noneOf(EFilterParamAttribute.class);
    }

    @Override
    public void onBaseAttributeChanged(final AbstractAttributeEditor baseEditor) {
    }

    @Override
    public QLabel getLabel() {
        return label;
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
