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

package org.radixware.kernel.server.instance;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.enums.EEventSeverity;

import org.radixware.kernel.server.dialogs.Login;
import org.radixware.kernel.server.dialogs.Login.Event;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.utils.OracleDbServerTraceConfig;
import org.radixware.kernel.server.utils.RecoveryInstanceFactory.InstanceCreationError;
import org.radixware.kernel.server.widgets.OracleDbServerTraceConfigDialog;
import java.sql.Connection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.server.RadixLoaderActualizer;
import org.radixware.kernel.server.Server;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.dialogs.RecoveryInstanceParamsDialog;
import org.radixware.kernel.server.exceptions.InvalidInstanceState;
import org.radixware.kernel.starter.Starter;

/**
 * ����������� ����������
 * �������
 *
 *
 */
final class InstanceController extends WindowAdapter {

    private final InstanceView view;

    InstanceController(final InstanceView instanceView) {
        view = instanceView;
    }
    private Login dlgLogin = null;

    public void start() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                dlgLogin = new Login(
                        view.getFrame(),
                        new LoginListenerImpl(),
                        new LocalTracerImpl(),
                        Login.EDialogMode.LAUNCH_INSTANCE);
                dlgLogin.setVisible(true);
            }
        });
    }

    public void startRecoveryInstance() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                dlgLogin = new Login(
                        view.getFrame(),
                        new Login.Listener() {

                            @Override
                            public void actionPerformed(Event e) {
                                try {
                                    final int instanceId = RecoveryInstanceParamsDialog.createNewInstance(dlgLogin, e.dbConnection);
                                    if (instanceId == -1) {
                                        return;
                                    } else {
                                        if (e.getSource() instanceof JDialog) {
                                            ((JDialog) e.getSource()).dispose();
                                        }
                                    }
                                    Event event = new Event(this, e.oraDataSource, e.proxyOraUser, e.dbConnection, instanceId, "Recovery Instance #" + instanceId, false);
                                    new LoginListenerImpl().actionPerformed(event);
                                } catch (InstanceCreationError ex) {
                                    messageError(ExceptionTextFormatter.exceptionStackToString(ex));
                                }
                            }

                            @Override
                            public void restart() {
                                throw new UnsupportedOperationException("Not supported yet.");
                            }
                        },
                        new LocalTracerImpl(),
                        Login.EDialogMode.OPEN_DB_CONNECTION);
                dlgLogin.setVisible(true);
            }
        });
    }

    void actualizeVer() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                view.getInstance().scheduleActualizeVer();
            }
        });
    }

    private class LoginListenerImpl implements Login.Listener {

        @Override
        public void actionPerformed(final Event e) {
            try {
                if (!e.isAutoStart && !SrvRunParams.getIsIgnoreDdsWarnings()) {
                    final List<RadixLoaderActualizer.DdsVersionWarning> warnings = RadixLoaderActualizer.checkDbStructCompatibility(e.dbConnection);
                    if (!warnings.isEmpty()) {
                        final Object[][] rowData = new Object[warnings.size()][3];
                        for (int i = 0; i < warnings.size(); i++) {
                            rowData[i][0] = warnings.get(i).getLayerUri();
                            rowData[i][1] = warnings.get(i).getServerVer();
                            rowData[i][2] = warnings.get(i).getDbCompatibleVersions();
                        }
                        //final JTable tab = new JTable(new DdsWarningsTable(warnings));
                        final JTable tab = new JTable(rowData, new String[]{Messages.COL_LAYER, Messages.COL_META_VER, Messages.COL_DB_COMPATIBLE_VER});
                        tab.setEnabled(false);
                        tab.setVisible(true);
                        final javax.swing.JScrollPane scrollPane = new JScrollPane(tab);
                        final JViewport viewPort = new JViewport();
                        viewPort.setVisible(true);
                        scrollPane.setPreferredSize(new Dimension(480, (warnings.size() + 2) * (tab.getRowHeight() + 3)));
                        scrollPane.setColumnHeader(viewPort);
                        final Box pane = Box.createVerticalBox();//new JPanel();
                        final JLabel lbl1 = new JLabel(Messages.ERR_DDS_VER_ON_START);
                        lbl1.setAlignmentX(JComponent.LEFT_ALIGNMENT);
                        pane.add(lbl1);
                        pane.add(scrollPane);
                        scrollPane.setAlignmentX(JComponent.LEFT_ALIGNMENT);
                        final JLabel lbl2 = new JLabel(Messages.START_ANYWAY);
                        lbl2.setAlignmentX(JComponent.LEFT_ALIGNMENT);
                        pane.add(lbl2);
                        if (JOptionPane.showConfirmDialog(InstanceController.this.view.getFrame(), pane, Messages.TITLE_CONFIRM, JOptionPane.OK_CANCEL_OPTION)
                                != JOptionPane.OK_OPTION) {
                            return;
                        }
                    }
                }
                final Runnable shutdownHook = new Runnable() {

                    @Override
                    public void run() {
                        System.out.println("Stopping server...");
                        try {
                            InstanceController.this.view.getInstance().stop("shutdown signal received");
                        } catch (InvalidInstanceState ex) {
                            //not started - ok
                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                        Server.returnFromMain();
                        System.out.println("Server has been stopped.");
                    }
                };
                Starter.addAppShutdownHook(shutdownHook);
                InstanceController.this.view.getInstance().registerListener(new InstanceListener() {

                    @Override
                    public void stateChanged(Instance instance, InstanceState oldState, InstanceState newState) {
                        if (newState == InstanceState.STOPPED) {
                            try {
                                Starter.removeAppShutdownHook(shutdownHook);
                            } catch (IllegalStateException ex) {
                                //shutdown is in progress;
                                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                            } finally {
                                instance.unregisterListener(this);
                            }
                        }
                    }
                });
                InstanceController.this.view.getInstance().start(e.oraDataSource, e.proxyOraUser, e.dbConnection, e.instanceId, e.instanceName);

            } catch (Throwable ex) {
                messageError(ExceptionTextFormatter.getExceptionMess(ex));
            }
        }

        @Override
        public void restart() {
            start();
        }
    }

    private class LocalTracerImpl extends LocalTracer {

        @Override
        public void put(final EEventSeverity severity, final String localizedMess, final String code, final List<String> words, final boolean isSensitive) {
            view.getInstance().getTrace().put(severity, localizedMess, code, words, EEventSource.INSTANCE.getValue(), isSensitive);
        }

        @Override
        public long getMinSeverity() {
            return view.getInstance().getTrace().getMinSeverity();
        }

        @Override
        public long getMinSeverity(final String eventSource) {
            return view.getInstance().getTrace().getMinSeverity(eventSource);
        }
    }

    public void stop() {
        if (!messageConfirmation(Messages.CONFIRM_SRV_STOP)) {
            return;
        }
        new Thread() {

            @Override
            public void run() {
                try {
                    setName("InstanceController stop");
                    view.getInstance().stop("command from GUI");
                } catch (Throwable err) {
                    messageError(ExceptionTextFormatter.getExceptionMess(err));
                }
            }
        }.start();
    }

    private boolean messageConfirmation(String mess) {
        return JOptionPane.showConfirmDialog(view.getFrame(), mess, Messages.TITLE_CONFIRM, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
    }

    private void messageError(final String mess) {
        JOptionPane.showMessageDialog(view.getFrame(), mess, Messages.TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void windowClosing(final WindowEvent e) {
        if (view.getInstance().getState() != InstanceState.STOPPED) {
            if (view.getInstance().getState() != InstanceState.STOPPING
                    && !messageConfirmation(Messages.CONFIRM_SRV_STOP)) {
                return;
            }
            stopServer();
        } else if (dlgLogin == null || !dlgLogin.isVisible()) {
            stopServer();
        }
    }

    private void stopServer() {
        if (view.getInstance().getState() != InstanceState.STOPPED) {
            view.getInstance().registerListener(new InstanceListener() {

                @Override
                public void stateChanged(final Instance unit, final InstanceState oldState, final InstanceState newState) {
                    if (newState == InstanceState.STOPPED) {
                        view.getInstance().unregisterListener(this);
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                view.dispose();
                            }
                        });
                    }
                }
            });
            if (view.getInstance().getState() != InstanceState.STOPPING) {
                new Thread() {

                    @Override
                    public void run() {
                        try {
                            setName("InstanceController stop on window closing");
                            view.getInstance().stop("Window closed");
                        } catch (Throwable err) {
                            messageError(ExceptionTextFormatter.getExceptionMess(err));
                        }
                    }
                }.start();
            }
        } else {
            view.dispose();
        }
    }

    void startServerSideTraceOptionsDialog() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                final Connection db = view.getInstance().getDbConnection();
                if (db == null) {
                    return;
                }
                final OracleDbServerTraceConfig cfg = new OracleDbServerTraceConfig(db);
                final JDialog optDlg = new OracleDbServerTraceConfigDialog(view.getFrame(), cfg, true);
                optDlg.setLocation(view.getFrame().getX() + 40, view.getFrame().getY() + 40);
                optDlg.setVisible(true);
            }
        });
    }

    @Override
    public void windowOpened(final WindowEvent e) {
        start();
    }
}