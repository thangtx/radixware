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

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import org.openide.util.NbBundle;


final class FindAndReplaceDialogSupport extends WindowAdapter {

    private final FindAndReplacePanel findAndReplacePanel;
    private final Dialog dialog;

    @SuppressWarnings("deprecation")
    public FindAndReplaceDialogSupport(ReplaceNavigator navigator) {
        findAndReplacePanel = new FindAndReplacePanel();
        findAndReplacePanel.addCloseActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });
        findAndReplacePanel.open(navigator);

        this.dialog = org.netbeans.editor.DialogSupport.createDialog(
                NbBundle.getMessage(FindAndReplaceDialogSupport.class, "FindAndReplaceDialog.Title"),
                findAndReplacePanel, false, findAndReplacePanel.getButtons(), true, 0, 3, findAndReplacePanel);

        dialog.addWindowListener(this);
    }

    @Override
    public void windowActivated(WindowEvent e) {
        findAndReplacePanel.activate();
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void showDialog() {
        dialog.pack();
        dialog.setVisible(true);
    }
}
