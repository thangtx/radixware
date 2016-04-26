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

package org.radixware.kernel.explorer.editors.jmleditor.dialogs;

import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;


public class LibUserFuncDialog  extends ExplorerDialog{
    private final AdsUserFuncDef libFunc;
    private final JmlEditor parent;
    private final List<String> paramVals;
    
    public LibUserFuncDialog(final JmlEditor parent,final AdsUserFuncDef libFunc, final List<String> paramVals,final String dialogTitle) {
        super(parent.getEnvironment(), parent, dialogTitle/*environment.getMessageProvider().translate("SqmlEditor", "Edit Localized String")*/);
        this.parent = parent;
        this.paramVals=paramVals;
        this.libFunc=libFunc;
        this.setWindowTitle(dialogTitle);
        accepted.connect(this, "setLocalizedStrVals()");

        createUi();
    }
    
    private void createUi() {    
        final EnumSet<EDialogButtonType> buttons;
        final LibUserFuncProfilePanel userFuncProfilepanel=new LibUserFuncProfilePanel( parent, libFunc.findProfile().getParametersList(), paramVals); 
        userFuncProfilepanel.setReadOnly(parent.isReadOnly());
        if (parent.isReadOnly()) {
            buttons = EnumSet.of(EDialogButtonType.CLOSE);            
        } else {
            buttons = EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL);
        }
        this.dialogLayout().addWidget(userFuncProfilepanel);
        addButtons(buttons, true); 
    }    
}
