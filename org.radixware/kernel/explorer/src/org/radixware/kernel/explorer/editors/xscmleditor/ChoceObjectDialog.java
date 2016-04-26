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

package org.radixware.kernel.explorer.editors.xscmleditor;

import com.trolltech.qt.gui.QWidget;
import java.util.EnumMap;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;


public abstract class ChoceObjectDialog extends ExplorerDialog {

    protected final boolean isReadOnly;
    private boolean canAcceptInitial = false;

    public ChoceObjectDialog(final IClientEnvironment environment, final QWidget editor, final String name, final boolean isReadOnly) {
        super(environment, editor, name);
        this.isReadOnly = isReadOnly;
    }

    protected void createUI() {
        this.setMinimumSize(600, 400);

        createListUi();        
        if (isReadOnly) {
            addButtons(EnumSet.of(EDialogButtonType.CLOSE),true);
        } else {
            final EnumMap<EDialogButtonType, IPushButton> buttons = 
                addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL),true);
            buttons.get(EDialogButtonType.OK).setEnabled(canAcceptInitial);
        }
    }
    
    protected final void setCanAccept(final boolean canAccept){
        final IPushButton buttonOk = getButton(EDialogButtonType.OK);
        if (buttonOk==null){//was not created yet
            canAcceptInitial = canAccept;
        }else{
            buttonOk.setEnabled(canAccept);
        }
    }

    protected abstract void createListUi();
}
