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
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;

import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.explorer.editors.valeditors.ValBoolEditor;




final class PersistentValDefinedAttributeEditor extends AbstractAttributeEditor<Boolean> {

    private final QLabel lbDefined;
    private final ValBoolEditor valDefined;

    protected PersistentValDefinedAttributeEditor(IClientEnvironment environment, final boolean isReadonly, final QWidget parent) {
        super(environment);
        lbDefined = new QLabel(getAttribute().getTitle(), parent);
        lbDefined.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Fixed);
        lbDefined.setObjectName("lbDefined");
        valDefined = new ValBoolEditor(environment, parent, new EditMaskNone(), true, false);
        valDefined.setObjectName("valDefined");       
        setupLabelTextOptions(lbDefined, false);
        lbDefined.setBuddy(valDefined);
        valDefined.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed);
        valDefined.setValue(Boolean.FALSE);
        valDefined.valueChanged.connect(this, "onValueChanged()");
    }

    @SuppressWarnings("unused")
    private void onValueChanged() {
        attributeChanged.emit(this);
    }

    @Override
    public boolean updateParameter(ISqmlParameter parameter) {
        return true;
    }

    @Override
    public void updateEditor(ISqmlParameter parameter) {
        if (!parameter.canHavePersistentValue()) {
            valDefined.setReadOnly(true);
            setupLabelTextOptions(lbDefined, true);
        } else {
            valDefined.setValue(parameter.getPersistentValue() != null);
        }
    }

    @Override
    public EFilterParamAttribute getAttribute() {
        return EFilterParamAttribute.PERSISTENT_VALUE_DEFINED;
    }

    @Override
    public Boolean getAttributeValue() {
        return valDefined.getValue();
    }

    @Override
    public EnumSet<EFilterParamAttribute> getBaseAttributes() {
        return EnumSet.of(EFilterParamAttribute.VALUE_TYPE,
                //                                    EFilterParamAttribute.EDIT_MASK,
                EFilterParamAttribute.PROPERTY);
    }

    @Override
    public void onBaseAttributeChanged(AbstractAttributeEditor linkedEditor) {
        valDefined.setValue(false);
    }

    @Override
    public QLabel getLabel() {
        return lbDefined;
    }

    @Override
    public QWidget getEditorWidget() {
        return valDefined;
    }

    @Override
    public void free() {
        valDefined.close();
    }
}
