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

package org.radixware.kernel.explorer.editors.profiling.dialogs;

import com.trolltech.qt.gui.QDoubleSpinBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import java.util.EnumSet;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.profiling.ProfilerWidget;
import org.radixware.kernel.explorer.env.Application;


public class FilterByPercentDialog extends ExplorerDialog {

    QDoubleSpinBox spinBox;

    public FilterByPercentDialog(final ProfilerWidget editor) {
        super(editor.getEnvironment(), editor, "ChoceObjForSqmlEditor");
        this.setWindowTitle("Set Percent Filter");

        final Double initValue = editor.getPercentFilter();
        createUi(initValue);
    }

    private void createUi(final Double initValue) {

        final QHBoxLayout hLayout = new QHBoxLayout();
        final QLabel lb = new QLabel(Application.translate("ProfilerDialog", "Show sections with percent load more than: "), this);

        spinBox = new QDoubleSpinBox(this);
        spinBox.setValue(initValue);
        spinBox.setMinimum(0);
        spinBox.setMaximum(99.99);
        spinBox.setDecimals(2);

        hLayout.addWidget(lb);
        hLayout.addWidget(spinBox);
        dialogLayout().addLayout(hLayout);
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
        //this.setWindowModality(WindowModality.WindowModal);
        this.setVisible(true);
    }

    @Override
    public void accept() {
        saveGeometryToConfig();
        super.accept();
    }


    public double getPercentFilterVal() {
        return spinBox.value();
    }
}
