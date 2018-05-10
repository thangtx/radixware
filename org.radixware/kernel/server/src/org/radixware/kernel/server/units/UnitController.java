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

package org.radixware.kernel.server.units;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import org.radixware.kernel.server.utils.OracleDbServerTraceConfig;
import org.radixware.kernel.server.widgets.OracleDbServerTraceConfigDialog;
import java.sql.Connection;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;

/**
 * GUI Event handler for
 *
 * @UnitView
 *
 */
public class UnitController extends WindowAdapter {

    private final UnitView view;

    UnitController(final UnitView instanceView) {
        super();
        view = instanceView;
    }

    public void start() {
        new Thread() {
            @Override
            public void run() {
                try {
                    setName("UnitController start");
                    view.getUnit().getInstance().getTrace().clearLastFloodMessageTime(view.getUnit().getPostponedDueToDuplicateFloodKey());
                    view.getUnit().start("command from GUI");
                } catch (Throwable e) {
                    messageError(ExceptionTextFormatter.getExceptionMess(e));
                }
            }
        }.start();
    }

    public void stop() {
        if (!messageConfirmation(Messages.CONFIRM_UNIT_STOP + view.getUnit().getFullTitle() + "?")) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    setName("UnitController stop");
                    view.getUnit().stop("command from GUI");
                } catch (Throwable e) {
                    messageError(ExceptionTextFormatter.getExceptionMess(e));
                }
            }
        }.start();
    }

    public void restart() {
        if (!messageConfirmation(Messages.CONFIRM_UNIT_RESTART + view.getUnit().getFullTitle() + "?")) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    setName("UnitController restart");
                    view.getUnit().restart("command from GUI");
                } catch (Throwable e) {
                    messageError(ExceptionTextFormatter.getExceptionMess(e));
                }
            }
        }.start();
    }

    public void abort() {
        if (!messageConfirmation(String.format(Messages.CONFIRM_UNIT_ABORT, view.getUnit().getFullTitle()) + "?")) {
            return;
        }
        view.getUnit().abortAndUnload("GUI command");
    }

    void startServerSideTraceOptionsDialog() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final Connection db = view.getUnit().getDbConnection();
                if (db == null) {
                    return;
                }
                final OracleDbServerTraceConfig cfg = new OracleDbServerTraceConfig(db);
                final JDialog optDlg = new OracleDbServerTraceConfigDialog(view.getDialog(), cfg, true);
                optDlg.setLocation(view.getDialog().getX() + 40, view.getDialog().getY() + 40);
                optDlg.setVisible(true);
            }
        });
    }

    private final boolean messageConfirmation(final String mess) {
        return JOptionPane.showConfirmDialog(view.getDialog(), mess, Messages.TITLE_CONFIRM, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
    }

    private final void messageError(final String mess) {
        JOptionPane.showMessageDialog(view.getDialog(), mess, Messages.TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void windowClosing(final WindowEvent e) {
        //e == null  ��� �������� �� esc 
        view.setVisible(false);
    }
}
