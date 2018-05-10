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

import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JLabel;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.*;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.calendar.SingleDaySelectionModel;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.dialogs.CreateConfigFilePanel;
import org.radixware.kernel.server.dialogs.Login;
import org.radixware.kernel.starter.Starter;

final class InstanceMenu extends JMenuBar {

    private static final long serialVersionUID = 2009882282697829711L;
    private final InstanceView view;
    private final JMenuItem menuItemStart;
    private final JMenuItem menuItemStop;
    private final JMenuItem menuItemSrvTrcOptDlg;
    private final JMenuItem menuItemSensitiveTrace;
    private final JMenuItem menuItemCreateRecoveryInstance;
    private final JMenuItem menuItemActualizeVer;
    private final JMenuItem menuItemCreateConfigFile;
    private final JMenuItem menuItemRefreshUnits;
    private final JMenuItem menuItemScheduleAllMaintenanceTasks;
    private final JMenuItem menuItemReloadArtePool;

    InstanceMenu(final InstanceView view) {
        super();
        this.view = view;
        final JMenu mainMenu = new JMenu(Messages.MNU_MENU);
        this.add(mainMenu);

        menuItemStart = new JMenuItem(Messages.MNU_START);
        menuItemStart.addActionListener(new StartActionListener());
        mainMenu.add(menuItemStart);
        menuItemStop = new JMenuItem(Messages.MNU_STOP);
        menuItemStop.addActionListener(new StopActionListener());
        mainMenu.add(menuItemStop);

        menuItemActualizeVer = new JMenuItem(Messages.MNU_ACTUALIZE_VER);
        menuItemActualizeVer.addActionListener(new ActualizeVerActionListener());
        if (SrvRunParams.getIsDevelopmentMode()) {
            mainMenu.add(menuItemActualizeVer);
        }
        mainMenu.addSeparator();

        mainMenu.add(menuItemRefreshUnits = new JMenuItem(Messages.MNU_REFRESH_UNITS));
        menuItemRefreshUnits.addActionListener(new RefreshUnitsListListener());
        mainMenu.add(menuItemScheduleAllMaintenanceTasks = new JMenuItem(Messages.MNU_SCHEDULE_MAINTENANCE_TASKS));
        menuItemScheduleAllMaintenanceTasks.addActionListener(new ScheduleInstanceMaintenanceListener());
        mainMenu.add(menuItemReloadArtePool = new JMenuItem(Messages.MNU_RELOAD_ARTE_POOL));
        menuItemReloadArtePool.addActionListener(new ReloadArtePoolListener());
        mainMenu.addSeparator();

        menuItemSrvTrcOptDlg = new StartSrvTraceOptDlgMenuItem();
        mainMenu.add(menuItemSrvTrcOptDlg);
        menuItemSensitiveTrace = new EnableSensitiveTracingMenuItem();
        mainMenu.add(menuItemSensitiveTrace);
        mainMenu.addSeparator();

        menuItemCreateRecoveryInstance = new JMenuItem(Messages.MNU_RECOVERY_INSTANCE);
        menuItemCreateRecoveryInstance.addActionListener(new CreateRecoveryInstanceActionListener());
        mainMenu.add(menuItemCreateRecoveryInstance);
        mainMenu.addSeparator();

        menuItemCreateConfigFile = new JMenuItem(Messages.MNU_CREATE_CONF_FILE);
        menuItemCreateConfigFile.addActionListener(new CreateConfigFileListener());
        mainMenu.add(menuItemCreateConfigFile);
        mainMenu.addSeparator();

        final JMenuItem aboutMenu = new JMenuItem(Messages.MNU_ABOUT);
        aboutMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                (new ReleaseNumberDialog(view.getFrame())).show();
            }
        });
        mainMenu.add(aboutMenu);

        actualize();
        mainMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuCanceled(final MenuEvent e) {
            }

            @Override
            public void menuDeselected(final MenuEvent e) {
            }

            @Override
            public void menuSelected(final MenuEvent e) {
                actualize();
            }
        });
    }

    private void actualize() {
        final InstanceState st = view.getInstance().getState();
        menuItemStop.setEnabled(st == InstanceState.STARTED);
        menuItemActualizeVer.setEnabled(st == InstanceState.STARTED);
        menuItemStart.setEnabled(st == InstanceState.STOPPED);
        menuItemCreateRecoveryInstance.setEnabled(st == InstanceState.STOPPED);
        menuItemSrvTrcOptDlg.setEnabled(st == InstanceState.STARTED && view.getInstance().getDbConnection() != null);
        menuItemSensitiveTrace.setEnabled(st == InstanceState.STARTED);
        menuItemRefreshUnits.setEnabled(st == InstanceState.STARTED);
        menuItemScheduleAllMaintenanceTasks.setEnabled(st == InstanceState.STARTED);
    }

    private final class RefreshUnitsListListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            view.getInstance().scheduleRefreshUnitsList();
        }
    }

    private final class ScheduleInstanceMaintenanceListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            view.getInstance().requestMaintenance(Messages.MNU_MENU);
        }
    }

    private final class ReloadArtePoolListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            view.getInstance().requestArtePoolReload("GUI Command");
        }
    }

    private final class CreateConfigFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            CreateConfigFilePanel.showDialog(view.getFrame());
        }
    }

    private final class ActualizeVerActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (System.getenv("RESTART_ON_URC") != null) {
                Starter.mustRestart(SrvRunParams.getRestartParams());
                view.getInstance().stop("restart on Update Runtime Components");
                view.getController().windowClosing(null);
            } else {
                view.getController().actualizeVer();
            }
        }
    }

    private final class CreateRecoveryInstanceActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            view.getController().startRecoveryInstance();
        }
    }

    private final class StartActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            Login.onManualStart();
            view.getController().start();
        }
    }

    private final class StopActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            view.getController().stop();
        }
    }

    private final class StartSrvTraceOptDlgMenuItem extends JMenuItem {

        public StartSrvTraceOptDlgMenuItem() {
            super(Messages.MNU_SRV_SQL_TRACE_CFG + "...");
            addActionListener(new StartSrvTraceOptDlg());
        }

        private final class StartSrvTraceOptDlg implements ActionListener {

            @Override
            public void actionPerformed(final ActionEvent e) {
                view.getController().startServerSideTraceOptionsDialog();
            }
        }
    }

    private final class EnableSensitiveTracingMenuItem extends JMenuItem {

        public EnableSensitiveTracingMenuItem() {
            super(Messages.MNU_ENABLE_SENSITIVE_TRACE + "...");
            addActionListener(new EnableSensitiveTracingAction());
        }

        private final class EnableSensitiveTracingAction implements ActionListener {

            private final String[] DATE_FORMAT = new String[]{"HH:mm   dd MMMMM yyyy"};
            private final DateFormat HH_MM_FORMAT = new SimpleDateFormat("HH:mm ddMMyyyy");

            @Override
            public void actionPerformed(final ActionEvent e) {
                final GridLayout layout = new GridLayout(2, 2);
                layout.setHgap(5);
                final JPanel panel = new JPanel(layout);
                final boolean globalPerm = view.getInstance().isGlobalSensitiveTracingOn();
                panel.add(new JLabel(Messages.LBL_GLB_SENSITIVE_TRC_PERMIS));
                final JLabel lbl = new JLabel((globalPerm ? Messages.LBL_IS_ON : Messages.LBL_IS_OFF) + ".");
                lbl.setToolTipText(Messages.HINT_YOU_HAVE_TO_ENABLE_SENSITIVE_TRC_GLOBALY);
                lbl.setAlignmentX(JLabel.LEFT_ALIGNMENT);
                lbl.setForeground(Color.red);
                panel.add(lbl);
                final JLabel lbl2 = new JLabel(Messages.LBL_LOCAL_SENSITIVE_TRC_FINISH_TIME);
                panel.add(lbl2);
                final JXDatePicker field = new JXDatePicker();// new JTextField("data");
                lbl2.setLabelFor(field);
                field.getMonthView().setSelectionModel(new SingleDaySelectionModel());
                final long localMillis = view.getInstance().getLocalSensitiveTracingFinishMillis();
                field.setDate(localMillis != 0 ? new Date(localMillis) : null);
                field.getEditor().addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        final Document document = field.getEditor().getDocument();
                        int offs = field.getEditor().getCaretPosition();
                        try {
                            final int colonOffs = 2;
                            if (Character.isDigit(e.getKeyChar()) && offs < 5) {
                                if (offs == colonOffs) {
                                    offs = colonOffs + 1;
                                }
                                final String prevTime = document.getText(0, 5);
                                document.remove(offs, 1);
                                document.insertString(offs, Character.toString(e.getKeyChar()), null);
                                try {
                                    HH_MM_FORMAT.setLenient(false);
                                    HH_MM_FORMAT.parse(document.getText(0, 5) + " 01012000");
                                    field.getEditor().setCaretPosition(offs + 1);
                                } catch (ParseException ex) {
                                    document.remove(0, 5);
                                    document.insertString(0, prevTime, null);
                                    field.getEditor().setCaretPosition(offs);
                                }

                            }
                        } catch (BadLocationException ex) {
                            //ignore
                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                        e.consume();
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            return;
                        }
                        e.consume();
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            return;
                        }
                        final Document document = field.getEditor().getDocument();
                        int offs = field.getEditor().getCaretPosition();
                        final int colonOffs = 2;
                        try {
                            if ((e.getKeyCode() == KeyEvent.VK_BACK_SPACE && offs > 0) || (e.getKeyCode() == KeyEvent.VK_DELETE && offs < 5)) {
                                if (e.getKeyCode() == KeyEvent.VK_DELETE && offs == colonOffs) {
                                    offs = colonOffs + 1;
                                }
                                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                                    if (offs == colonOffs + 1) {
                                        offs = colonOffs;
                                    }
                                    offs--;
                                }
                                document.remove(offs, 1);
                                document.insertString(offs, "_", null);
                                field.getEditor().setCaretPosition(offs);

                            }
                        } catch (BadLocationException ex) {
                            //ignore
                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                        e.consume();
                    }
                });
                field.setFormats(DATE_FORMAT);
                field.setToolTipText(Messages.HINT_YOU_HAVE_TO_ENABLE_SENSITIVE_TRC_LOCALY);
                panel.add(field);
                field.getEditor().setCaretPosition(0);
                JOptionPane pane = new JOptionPane(panel, JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE, null, null, null);
                final JDialog dialog = pane.createDialog(view.getFrame(), Messages.TITLE_SENSITIVE_DATA_TRACING_OPTIONS);
                dialog.setMinimumSize(new Dimension(400, dialog.getPreferredSize().height));
                dialog.setMaximumSize(new Dimension(800, dialog.getPreferredSize().height));
                dialog.setResizable(true);
                dialog.setVisible(true);
                if (Integer.valueOf(JOptionPane.OK_OPTION).equals(pane.getValue())) {
                    final long finishMillis = field.getDate() != null ? field.getDate().getTime() : 0;
                    if (finishMillis - System.currentTimeMillis() > SrvRunParams.SENS_TRACE_TIME_MAX_MILLIS) {
                        JOptionPane.showMessageDialog(view.getFrame(), Messages.MSG_ATTEMPT_TO_ENABLE_TRACE_LONGER_THAN_8_H, Messages.TITLE_INFO, JOptionPane.PLAIN_MESSAGE);
                    }
                    SrvRunParams.setSensitiveTraceFinishMillis(finishMillis, null);
                    view.getInstance().updateLocalSensitiveTracingFinishMillis();
                    if (!globalPerm) {
                        JOptionPane.showMessageDialog(view.getFrame(), Messages.MSG_SENSITIVE_TRC_IS_OFF_DUE_TO_GLBL_SET, Messages.TITLE_INFO, JOptionPane.PLAIN_MESSAGE);
                    } else {
                        final long sensitiveTracingFinishMillis = view.getInstance().getSensitiveTracingFinishMillis();
                        if (sensitiveTracingFinishMillis < System.currentTimeMillis()) {
                            JOptionPane.showMessageDialog(view.getFrame(), Messages.MSG_SENSITIVE_TRC_IS_OFF, Messages.TITLE_INFO, JOptionPane.PLAIN_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(view.getFrame(), Messages.LBL_SENSITIVE_TRC_IS_ON_TILL_ + new SimpleDateFormat("HH:mm dd MMMMM yyyy").format(new Date(SrvRunParams.getLocalSensitiveTracingFinishMillis())), Messages.TITLE_INFO, JOptionPane.PLAIN_MESSAGE);
                        }
                    }
                }
            }
        }
    }
}
