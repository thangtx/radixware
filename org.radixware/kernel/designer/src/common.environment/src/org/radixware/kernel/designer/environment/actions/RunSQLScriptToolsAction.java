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

package org.radixware.kernel.designer.environment.actions;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.windows.TopComponent;
import org.radixware.kernel.designer.dds.editors.SqlModalEditor;


@ActionID(id = "org.radixware.kernel.designer.common.dialogs.sqlscript.RunSQLScriptToolsAction", category = "Tools")
@ActionRegistration(displayName = "#run-sqlscript-tools-action-title")
@ActionReference(path = "Menu/RadixTools", position = 1250)
public class RunSQLScriptToolsAction extends AbstractAction {

    private static int COUNTER = 1;

    @Override
    public void actionPerformed(ActionEvent e) {
        final SqlModalEditor.ICfg cfg = new SqlModalEditor.ICfg() {

            @Override
            public String getSql() {
                return "";
            }

            @Override
            public void setSql(String sql) {
                //do nothing
            }

            @Override
            public String getTitle() {
                return "";
            }

            @Override
            public JPanel getAdditionalPanel() {
                return null;
            }

            @Override
            public boolean canCloseEditor() {
                return true;
            }

            @Override
            public void showClosingProblems() {
            }
        };

        TopComponent tc = new TopComponent() {

            @Override
            public int getPersistenceType() {
                return PERSISTENCE_NEVER;
            }
        };
        tc.setLayout(new BorderLayout());
        tc.add(new SqlModalEditor.SqlEditor(cfg), BorderLayout.CENTER);
        tc.setDisplayName("SQLScript #" + COUNTER);
        //should be called only from AWT thread, no synchronization needed
        COUNTER++;
        tc.open();
        tc.requestActive();
    }
}
