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

package org.radixware.kernel.explorer.editors.sqmleditor.tageditors;

import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;


public class ThisTableSqmlId_Dialog extends ExplorerDialog {

    public ThisTableSqmlId_Dialog(final IClientEnvironment environment, final QWidget w, final String name, final String title) {
        super(environment, w, "ThisTableSqmlId Dialog");
        this.setWindowTitle(environment.getMessageProvider().translate("SqmlEditor", "This Table"));//����������� �������
        createUI(name, title);
    }

    private void createUI(final String name, final String title) {
        final QGridLayout layout = new QGridLayout();
        final QLabel lbName = new QLabel(this);
        lbName.setText(getEnvironment().getMessageProvider().translate("SqmlEditor", "Name: "));
        lbName.setEnabled(false);
        final QLabel lbTitle = new QLabel(this);
        lbTitle.setText(getEnvironment().getMessageProvider().translate("SqmlEditor", "Title: "));
        lbTitle.setEnabled(false);

        final QLineEdit editName = new QLineEdit(this);
        editName.setObjectName("editName");
        editName.setReadOnly(true);
        editName.setText(name);
        editName.setEnabled(false);

        final QLineEdit editTitle = new QLineEdit(this);
        editTitle.setObjectName("editTitle");
        editTitle.setEnabled(false);
        editTitle.setText(title);
        editTitle.setEnabled(false);

        layout.addWidget(lbName, 0, 0);
        layout.addWidget(editName, 0, 1);
        layout.addWidget(lbTitle, 1, 0);
        layout.addWidget(editTitle, 1, 1);

        dialogLayout().addLayout(layout);
        addButtons(EnumSet.of(EDialogButtonType.CLOSE), true);
    }
}