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
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;


final class NullTitleAttributeEditor extends AbstractAttributeEditor<String> {

    private final QLabel lbNullTitle;
    private final ValStrEditor valNullTitle;

    protected NullTitleAttributeEditor(IClientEnvironment environment, final boolean isReadonly, final QWidget parent) {
        super(environment);
        lbNullTitle = new QLabel(getAttribute().getTitle(environment), parent);
        lbNullTitle.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Fixed);
        lbNullTitle.setObjectName("lbNullTitle");
        valNullTitle = new ValStrEditor(environment, parent, new EditMaskStr(), true, isReadonly);
        valNullTitle.setObjectName("valNullTitle");
        lbNullTitle.setBuddy(valNullTitle);
        setupLabelTextOptions(lbNullTitle, isReadonly);
        valNullTitle.setSizePolicy(QSizePolicy.Policy.Minimum, QSizePolicy.Policy.Fixed);
        valNullTitle.valueChanged.connect(this, "onValueChanged()");
    }

    @SuppressWarnings("unused")
    private void onValueChanged() {
        attributeChanged.emit(this);
    }

    @Override
    public boolean updateParameter(ISqmlParameter parameter) {
        if (parameter instanceof ISqmlModifiableParameter) {
            ((ISqmlModifiableParameter) parameter).setNullString(valNullTitle.getValue());
        }
        return true;
    }

    @Override
    public void updateEditor(ISqmlParameter parameter) {
        valNullTitle.setValue(parameter.getNullString());
    }

    @Override
    public EFilterParamAttribute getAttribute() {
        return EFilterParamAttribute.NULL_TITLE;
    }

    @Override
    public String getAttributeValue() {
        return valNullTitle.getValue();
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
        return lbNullTitle;
    }

    @Override
    public QWidget getEditorWidget() {
        return valNullTitle;
    }

    @Override
    public void free() {
        valNullTitle.close();
    }
}
