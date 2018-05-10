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
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.explorer.editors.valeditors.ValBoolEditor;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;



final class NotNullAttributeEditor extends AbstractAttributeEditor<Boolean> {

    private final QLabel lbNotNull;
    private final ValBoolEditor valNotNull;

    protected NotNullAttributeEditor(IClientEnvironment environment, final boolean isReadonly, final QWidget parent) {
        super(environment);
        lbNotNull = new QLabel(getAttribute().getTitle(environment), parent);
        lbNotNull.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Fixed);
        lbNotNull.setObjectName("lbNotNull");
        valNotNull = new ValBoolEditor(environment, parent, new EditMaskNone(), true, isReadonly);
        valNotNull.setObjectName("valNotNull");
        lbNotNull.setBuddy(valNotNull);
        if (isReadonly) {
            ExplorerTextOptions.Factory.getLabelOptions(ETextOptionsMarker.READONLY_VALUE).applyTo(lbNotNull);
        } else {
            ExplorerTextOptions.Factory.getLabelOptions(ETextOptionsMarker.REGULAR_VALUE).applyTo(lbNotNull);
        }
        valNotNull.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed);
        valNotNull.setValue(Boolean.FALSE);
        valNotNull.valueChanged.connect(this, "onValueChanged()");
    }

    @SuppressWarnings("unused")
    private void onValueChanged() {
        attributeChanged.emit(this);
    }

    @Override
    public boolean updateParameter(ISqmlParameter parameter) {
        if (parameter instanceof ISqmlModifiableParameter) {
            ((ISqmlModifiableParameter) parameter).setMandatory(valNotNull.getValue());
        }
        return true;
    }

    @Override
    public void updateEditor(ISqmlParameter parameter) {
        valNotNull.setValue(parameter.isMandatory());
    }

    @Override
    public EFilterParamAttribute getAttribute() {
        return EFilterParamAttribute.IS_MANDATORY;
    }

    @Override
    public Boolean getAttributeValue() {
        return valNotNull.getValue();
    }

    @Override
    public EnumSet<EFilterParamAttribute> getBaseAttributes() {
        return EnumSet.noneOf(EFilterParamAttribute.class);
    }

    @Override
    public void onBaseAttributeChanged(AbstractAttributeEditor linkedEditor) {
    }

    @Override
    public QLabel getLabel() {
        return lbNotNull;
    }

    @Override
    public QWidget getEditorWidget() {
        return valNotNull;
    }

    @Override
    public void free() {
        valNotNull.close();
    }
}
