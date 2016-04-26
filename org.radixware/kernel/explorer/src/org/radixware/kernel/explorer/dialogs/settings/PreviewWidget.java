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
package org.radixware.kernel.explorer.dialogs.settings;

import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;

import org.radixware.kernel.explorer.utils.WidgetUtils;

public class PreviewWidget extends QWidget {

    private final ValStrEditor readOnlyValEditor;
    private final ValStrEditor mandatoryValEditor;
    private final ValStrEditor otherValEditor;

    public PreviewWidget(final QWidget parent, final IClientEnvironment environment) {
        super(parent);

        readOnlyValEditor = new ValStrEditor(environment, this);
        readOnlyValEditor.setMandatory(false);
        readOnlyValEditor.setValue(Application.translate("Settings Dialog", "Readonly property text"));
        mandatoryValEditor = new ValStrEditor(environment, this);
        mandatoryValEditor.setValue(Application.translate("Settings Dialog", "Mandatory property text"));
        otherValEditor = new ValStrEditor(environment, this);
        otherValEditor.setMandatory(false);
        otherValEditor.setValue(Application.translate("Settings Dialog", "Other property text"));

        QVBoxLayout vboxLayout = new QVBoxLayout();
        vboxLayout.setAlignment(new Alignment(AlignmentFlag.AlignCenter, AlignmentFlag.AlignTop));

        vboxLayout.addWidget(readOnlyValEditor);
        vboxLayout.addWidget(mandatoryValEditor);
        vboxLayout.addWidget(otherValEditor);

        QGroupBox groupBox = new QGroupBox(Application.translate("Settings Dialog", Application.translate("Settings Dialog", "Preview")));
        groupBox.setLayout(vboxLayout);
        groupBox.setSizePolicy(Policy.Minimum, Policy.Minimum);
        QVBoxLayout mainLayout = WidgetUtils.createVBoxLayout(this);
        mainLayout.addWidget(groupBox);
    }

    public void update(final ExplorerTextOptions readonlyTextOptions, final ExplorerTextOptions mandatoryTextOptions, final ExplorerTextOptions otherTextOptions, final boolean isDef) {
        if (isDef) {
            readOnlyValEditor.setDefaultTextOptions(readonlyTextOptions);
            otherValEditor.setDefaultTextOptions(otherTextOptions);
        } else {
            readOnlyValEditor.setTextOptionsForMarker(ETextOptionsMarker.UNDEFINED_VALUE, readonlyTextOptions);
            otherValEditor.setTextOptionsForMarker(ETextOptionsMarker.UNDEFINED_VALUE, otherTextOptions);
        }
        mandatoryValEditor.setDefaultTextOptions(mandatoryTextOptions);
    }
}
