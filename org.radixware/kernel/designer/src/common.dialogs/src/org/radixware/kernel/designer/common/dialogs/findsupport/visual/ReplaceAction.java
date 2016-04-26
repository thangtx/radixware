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

package org.radixware.kernel.designer.common.dialogs.findsupport.visual;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;
import org.netbeans.editor.BaseAction;
import org.radixware.kernel.designer.common.dialogs.findsupport.visual.FindAndReplaceSupport;


public final class ReplaceAction extends BaseAction {

    public static final String SEQUENCE_KEY = "SEARCH_NAVIGATOR_SEQUENCE";
    public static final String ACTION_NAME = "replace";
    private final FindAndReplaceSupport actionSupport;

    public ReplaceAction() {
        this(FindAndReplaceSupport.getInstance());
    }

    public ReplaceAction(FindAndReplaceSupport actionSupport) {
        super(ACTION_NAME, ABBREV_RESET | MAGIC_POSITION_RESET | UNDO_MERGE_RESET | NO_RECORDING);
        this.actionSupport = actionSupport;
    }

    @Override
    public void actionPerformed(ActionEvent evt, JTextComponent target) {
        if (actionSupport != null) {
            actionSupport.showReplaceDialog();
        }
    }
}
