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

import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import java.sql.Timestamp;
import java.util.EnumSet;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.profiling.ProfilerWidget;
import org.radixware.kernel.explorer.editors.valeditors.ValDateTimeEditor;
import org.radixware.kernel.explorer.env.Application;



public class ChoosePeriod extends ExplorerDialog {

    private ValDateTimeEditor editorFrom;
    private ValDateTimeEditor editorTo;

    public ChoosePeriod(final ProfilerWidget editor, final Timestamp curPeriod) {
        super(editor.getEnvironment(), editor, "ChoosePeriodForProfileEditor");
        this.setWindowTitle(Application.translate("ProfilerDialog", "Choose Period"));
        createUI(curPeriod);
    }

    private void createUI(final Timestamp curPeriod) {
        
        final QLabel lbFrom = new QLabel(Application.translate("ProfilerDialog", "From") + ":", this);
        final QLabel lbTo = new QLabel(Application.translate("ProfilerDialog", "To") + ":", this);
        final EditMaskDateTime mask = new EditMaskDateTime("HH:mm:ss.zzz dd/MM/yy", null, null);

        editorFrom = new ValDateTimeEditor(getEnvironment(), this, mask, false, false);
        editorTo = new ValDateTimeEditor(getEnvironment(), this, mask, false, false);
        editorFrom.setValue(curPeriod);
        editorTo.setValue(curPeriod);

        final QGridLayout gridLayout = new QGridLayout();
        gridLayout.addWidget(lbFrom, 0, 0);
        gridLayout.addWidget(editorFrom, 0, 1);
        gridLayout.addWidget(lbTo, 1, 0);
        gridLayout.addWidget(editorTo, 1, 1);
        gridLayout.setEnabled(false);

        dialogLayout().addLayout(gridLayout);
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);

        editorFrom.valueChanged.connect(this, "onFromValueChanged(Object)");
        editorTo.valueChanged.connect(this, "onToValueChanged(Object)");

        //this.setWindowModality(WindowModality.WindowModal);

    }

    @SuppressWarnings("unused")
    private void onFromValueChanged(final Object obj) {
        if (getPeriodTo() == null && obj == null) {
            getButton(EDialogButtonType.OK).setEnabled(false);
        } else {
            getButton(EDialogButtonType.OK).setEnabled(true);
        }
    }

    @SuppressWarnings("unused")
    private void onToValueChanged(final Object obj) {
        if (getPeriodFrom() == null && obj == null) {
            getButton(EDialogButtonType.OK).setEnabled(false);
        } else {
            getButton(EDialogButtonType.OK).setEnabled(true);
        }
    }

    @Override
    public void accept() {
        if (checkInput(editorFrom) && checkInput(editorTo)){
            saveGeometryToConfig();
            super.accept();
        }
    }
    
    private boolean checkInput(final ValDateTimeEditor valEditor){
        final MessageProvider messageProvider = getEnvironment().getMessageProvider();
        final String title = messageProvider.translate("ProfilerDialog", "Period is Invalid");
        return valEditor.checkInput(title, null);
    }

    /*@Override
    public void reject() {
        super.reject();
    }*/

    public Timestamp getPeriodFrom() {
        return editorFrom.getValue();
    }

    public Timestamp getPeriodTo() {
        return editorTo.getValue();
    }
}
