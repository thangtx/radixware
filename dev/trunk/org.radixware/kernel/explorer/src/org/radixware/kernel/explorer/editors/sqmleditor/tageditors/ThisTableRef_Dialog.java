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

import com.trolltech.qt.gui.QLayout.SizeConstraint;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EPidTranslationMode;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class ThisTableRef_Dialog extends ExplorerDialog {

    private final boolean isReadonly;
    private final PidTranslationModePanel pidTranslModePanel;

    public ThisTableRef_Dialog(final IClientEnvironment environment, final ISqmlTableDef table, final XscmlEditor editText) {
        super(environment, editText, null);
        this.isReadonly = editText.isReadOnly();
        pidTranslModePanel = new PidTranslationModePanel(environment, table, isReadonly);
        setupUi(table);
    }

    private void setupUi(final ISqmlTableDef table) {

        layout().addWidget(pidTranslModePanel);
        final EnumSet<EDialogButtonType> buttons;
        if (isReadonly) {
            buttons = EnumSet.of(EDialogButtonType.CLOSE);
        } else {
            buttons = EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL);
        }
        addButtons(buttons, true);
        setWindowTitle(getEnvironment().getMessageProvider().translate("SqmlEditor", "Context Table Reference Tag"));
        setWindowIcon(ExplorerIcon.getQIcon(table.getIcon()));
        layout().setSizeConstraint(SizeConstraint.SetFixedSize);
    }

    public void writeToTag(final org.radixware.schemas.xscml.Sqml.Item.ThisTableRef thisTableRef) {
        final EPidTranslationMode translationMode = pidTranslModePanel.getTranslationMode();
        thisTableRef.setPidTranslationMode(translationMode);
        final Id indecesValue = pidTranslModePanel.getIndecesValue();
        if (translationMode == EPidTranslationMode.SECONDARY_KEY_PROPS && indecesValue != null) {
            thisTableRef.setPidTranslationSecondaryKeyId(indecesValue);
        }
    }

    public void readFromTag(final org.radixware.schemas.xscml.Sqml.Item.ThisTableRef thisTableRef) {
        pidTranslModePanel.readFromTag(thisTableRef.getPidTranslationMode(), thisTableRef.getPidTranslationSecondaryKeyId());
    }
}
