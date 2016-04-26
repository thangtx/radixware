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

import java.sql.Connection;
import java.sql.SQLException;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.dialogs.db.DbDialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.dds.actualizer.Actualizer;
import org.radixware.kernel.designer.dds.actualizer.oracle.OracleDriver;


public class CheckDbStructureAction extends CallableSystemAction {

    @Override
    public void performAction() {
        final RadixObject radixObject = WindowManager.getDefault().getRegistry().getActivated().getLookup().lookup(RadixObject.class);
        if (radixObject == null) {
            DialogUtils.messageError("There is no object selected.");
            return;
        }

        final Connection connection = DbDialogUtils.connectToDatabase();
        if (connection == null) {
            return;
        }

        final OracleDriver driver;

        try {
            driver = new OracleDriver(connection);
        } catch (SQLException ex) {
            DialogUtils.messageError(ex);
            return;
        }

        final Actualizer actualizer = Actualizer.Factory.newInstance(driver, radixObject);

        final ProgressHandle handle = ProgressHandleFactory.createHandle("Check Database Structure");
        try {
            handle.start();
            final String sql = actualizer.getActualizationScript();
            if (sql.isEmpty()) {
                DialogUtils.messageInformation("Database is actual!");
            } else {
                DialogUtils.showText(sql, "Alter", "sql");
            }
        } finally {
            handle.finish();
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(CheckDbStructureAction.class, "CTL_CheckDbStructureAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
