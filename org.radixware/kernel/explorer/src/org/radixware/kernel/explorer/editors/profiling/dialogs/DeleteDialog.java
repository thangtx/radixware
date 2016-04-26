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

import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QRadioButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.Application;


public class DeleteDialog extends ExplorerDialog {

    private QRadioButton rbHide;
    private QRadioButton rbDeleteFromDB;

    public DeleteDialog(final IClientEnvironment environment, final QWidget editor) {
        super(environment, editor, "SelectDeletingMode");
        this.setWindowTitle(Application.translate("ProfilerDialog", "Choose Deleting Mode"));
        createUi();
    }

    private void createUi() {

        rbHide = new QRadioButton(Application.translate("ProfilerDialog", "Hide from List"), this);
        rbHide.clicked.connect(this, "hideSelected()");
        rbHide.setChecked(true);

        rbDeleteFromDB = new QRadioButton(Application.translate("ProfilerDialog", "Delete from Database"), this);
        rbDeleteFromDB.clicked.connect(this, "deleteFromDbSelected()");
        rbDeleteFromDB.setChecked(false);

        final QGroupBox groupBox = new QGroupBox(Application.translate("ProfilerDialog", "Deleting Mode:"), this);

        final QVBoxLayout layout = new QVBoxLayout();
        layout.addWidget(rbHide);
        layout.addWidget(rbDeleteFromDB);
        groupBox.setLayout(layout);

        dialogLayout().addWidget(groupBox);
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);

        //this.setWindowModality(WindowModality.WindowModal);
    }

    @SuppressWarnings("unused")
    private void hideSelected() {
        rbDeleteFromDB.setChecked(false);
    }

    @SuppressWarnings("unused")
    private void deleteFromDbSelected() {
        rbHide.setChecked(false);
    }

    public boolean isDeleteFromDb() {
        return rbDeleteFromDB.isChecked();
    }

    @Override
    public void accept() {
        saveGeometryToConfig();
        super.accept();
    }
}
