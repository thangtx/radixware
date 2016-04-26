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

package org.radixware.kernel.designer.common.dialogs.sqlscript.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import org.radixware.kernel.designer.common.dialogs.actions.AbstractRadixAction;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


class UnavailableRunSqlScriptActionImpl extends AbstractRadixAction {

    private JButton button = new JButton();

    public UnavailableRunSqlScriptActionImpl() {
        button.setVisible(false);
    }

    @Override
    public Component getToolbarPresenter() {
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DialogUtils.messageError("Action is not available");
            }
        });
    }
}