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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;

public class PresentationInfoDialog extends ExplorerDialog {
    QGridLayout layout;
    IClientEnvironment env;
    public PresentationInfoDialog(IClientEnvironment env, String title, String classId, String className, String presentationId, String presentationName, String explorerItemId, String pid) {
        super(env, (QWidget) env.getMainWindow());
        this.env = env;
        MessageProvider mp = env.getMessageProvider();
        setWindowTitle(title);
        layout = new QGridLayout();
        setDisposeAfterClose(true);
        setSizePolicy(QSizePolicy.Policy.MinimumExpanding, QSizePolicy.Policy.Fixed);
        if (classId != null && !classId.isEmpty()) {
            addNewRow(mp.translate("PresentationInfoDialog", "Class ID:"), classId, 0);
        }
        
        if (className != null && !className.isEmpty()) {
            addNewRow(mp.translate("PresentationInfoDialog", "Class name:"), className, 1);
        }
        
        addNewRow(mp.translate("PresentationInfoDialog","Presentation ID:"), presentationId, 2);
        addNewRow(mp.translate("PresentationInfoDialog","Presentation name:"), presentationName, 3);
        if (explorerItemId != null && !explorerItemId.isEmpty()) {
            addNewRow(mp.translate("PresentationInfoDialog","ExplorerItem ID:"), explorerItemId, 4);
        }
        if (pid != null) {
            addNewRow(mp.translate("PresentationInfoDialog","PID:"), pid, 5);
        }
        dialogLayout().addLayout(layout);
        addButtons(EnumSet.of(EDialogButtonType.OK),true);
        layout.setSpacing(5);
        setMinimumWidth(380);
        setFixedHeight(sizeHint().height());
    }
    
    public void addNewRow (String title, String value, int row) {
        layout.addWidget(new QLabel(title, this), row, 0);
        ValStrEditor editor = new ValStrEditor(env, this, new EditMaskStr(), true, true);
        editor.setValue(value);
        layout.addWidget(editor, row, 1);
    }
}
