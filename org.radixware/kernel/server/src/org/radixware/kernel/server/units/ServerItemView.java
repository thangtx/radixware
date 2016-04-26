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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Connection;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.radixware.kernel.server.utils.OracleDbServerTraceConfig;
import org.radixware.kernel.server.widgets.OracleDbServerTraceConfigDialog;
import org.radixware.kernel.server.widgets.TraceView;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import org.radixware.kernel.common.enums.EEventSeverity;


public final class ServerItemView {

    private static final long serialVersionUID = -1188041005445470384L;
    private volatile JDialog dlg = null;
    private volatile TraceView traceList = null;
    private final ViewModel item;
    private boolean isDisposed = false;

    public static final class Factory {

        public static ServerItemView newInstance(final ViewModel item) {
            final ServerItemView v = new ServerItemView(item);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    v.createDlg();
                }
            });
            return v;
        }
    }

    private ServerItemView(final ViewModel item) {
        this.item = item;
    }

    public TraceView getTraceList() {
        return traceList;
    }

    private final void createDlg() {
        dlg = new JDialog(item.getParentView());
        dlg.setModal(false);
        dlg.setVisible(false);
        final JPanel contentPane;
        dlg.setContentPane(contentPane = new JPanel());
        contentPane.setLayout(new BorderLayout(5, 5));
        contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "hide_on_esc");
        contentPane.getActionMap().put("hide_on_esc", new AbstractAction() {
            private static final long serialVersionUID = -8652461513237290321L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                dlg.setVisible(false);
            }
        });
        traceList = new TraceView(item.getTrace());
        contentPane.add(traceList);

        dlg.setSize(600, 500);
        dlg.setLocationRelativeTo(null);//center window
        final int orig = (-5 + (int) item.getSeqNumber() % 10) * 20; //cascade
        dlg.setBounds(dlg.getX() + orig, dlg.getY() + orig, dlg.getWidth(), dlg.getHeight());
        dlg.setTitle(item.getTitle());
        dlg.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        dlg.setJMenuBar(new Menu(this));
    }

    public EEventSeverity getTraceMaxSeverity() {
        return traceList == null ? EEventSeverity.NONE : traceList.getTraceMaxSeverity();
    }

//	dispose
    public final void dispose() {
        isDisposed = true;
        if (traceList != null) {
            traceList.close();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                dlg.dispose();
            }
        });

    }

    public final boolean isDisposed() {
        return isDisposed;
    }

    public final void setVisible(final boolean b) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                dlg.setVisible(b);
            }
        });
    }

    public boolean isVisible() {
        return dlg != null && dlg.isVisible();
    }

    private static final class Menu extends JMenuBar {

        private final ServerItemView view;

        public Menu(final ServerItemView view) {
            super();
            this.view = view;
            add(new MenuRoot());
        }

        private final class MenuRoot extends JMenu {

            private final StartSrvTraceOptDlgMenuItem menuItemStartSrvTraceOptDlg;

            public MenuRoot() {
                super(MenuText.MENU);
                menuItemStartSrvTraceOptDlg = new StartSrvTraceOptDlgMenuItem();
                add(menuItemStartSrvTraceOptDlg);
                addMenuListener(new MenuListener() {
                    @Override
                    public void menuSelected(final MenuEvent e) {
                        actualize();
                    }

                    @Override
                    public void menuCanceled(final MenuEvent e) {
                        //do nothing
                    }

                    @Override
                    public void menuDeselected(final MenuEvent e) {
                        //do nothing
                    }
                });
                actualize();

            }

            private void actualize() {
                menuItemStartSrvTraceOptDlg.setEnabled(view.item.getDbConnection() != null);
            }

            private final class StartSrvTraceOptDlgMenuItem extends JMenuItem {

                public StartSrvTraceOptDlgMenuItem() {
                    super(MenuText.SRV_SQL_TRACE_CFG);
                    addActionListener(new StartSrvTraceOptDlg());
                }

                private final class StartSrvTraceOptDlg implements ActionListener {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                final Connection db = view.item.getDbConnection();
                                if (db == null) {
                                    return;
                                }
                                final OracleDbServerTraceConfig cfg = new OracleDbServerTraceConfig(db);
                                final JDialog optDlg = new OracleDbServerTraceConfigDialog(view.dlg, cfg, true);
                                optDlg.setLocation(view.dlg.getX() + 40, view.dlg.getY() + 40);
                                optDlg.setVisible(true);
                            }
                        });
                    }
                }
            }
        }

        private static final class MenuText {

            static {
                final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.units.mess.messages");
                MENU = bundle.getString("MNU_MENU");
                SRV_SQL_TRACE_CFG = bundle.getString("MNU_SRV_SQL_TRACE_CFG");
            }
            static final String MENU;
            static final String SRV_SQL_TRACE_CFG;
        }
    }
}
