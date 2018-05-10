/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.explorer.inspector.propertyEditors;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.widgets.IEditMaskEditor;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;

public class EditMaskDlg extends ExplorerDialog {

    private final IEditMaskEditor maskEditorWidget;
    private EditMask editMask;

    public EditMaskDlg(IClientEnvironment environment, QWidget parent, IEditMaskEditor maskEditorWidget) {
        super(environment, parent);
        this.maskEditorWidget = maskEditorWidget;
        this.setWindowModality(Qt.WindowModality.WindowModal);
        setupUI();
    }

    private void setupUI() {
        this.layout().addWidget((QWidget) maskEditorWidget);
        this.addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
        this.setAutoHeight(true);
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        if (!maskEditorWidget.checkOptions()) {
            event.ignore();
            return;
        }
        super.closeEvent(event);
    }

    @Override
    public void done(int result) {
        if (result == QDialog.DialogCode.Accepted.value()) {
            if (!maskEditorWidget.checkOptions()) {
                return;
            }
            this.editMask = maskEditorWidget.getEditMask();
        }
        super.done(result);
    }

    public EditMask getEditMask() {
        return editMask;
    }
}
