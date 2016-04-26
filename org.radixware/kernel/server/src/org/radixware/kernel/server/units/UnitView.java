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

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.radixware.kernel.server.SrvRunParams;

import org.radixware.kernel.server.instance.InstanceConfigStore;
import org.radixware.kernel.server.widgets.TraceView;

public class UnitView {

    private final Unit unit;
    private volatile UnitController controller = null;
    private volatile JDialog dlg;
    protected volatile TraceView traceList = null;
    protected volatile UnitMenu menu = null;

    public UnitView(final Unit model) {
        unit = model;
        if (SrvRunParams.getIsGuiOn()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    createDlg();  //FIXME unconstructed this escaped
                }
            });
        }
    }

    void updateTitle() {
        if (dlg != null) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    dlg.setTitle(unit.getFullTitle());
                }
            });
        }
    }

    private void createDlg() {
        if (SrvRunParams.getIsGuiOn()) {
            if (unit.getInstance() == null
                    || unit.getInstance().getView() == null
                    || unit.getInstance().getView().getFrame() == null) {
                //delay createDlg until instance view will be created
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        createDlg();  //FIXME unconstructed this escaped
                    }
                });
            } else {
                dlg = new JDialog(unit.getInstance().getView().getFrame(), false);
                dlg.setVisible(false);
                dlg.setResizable(true);
                init();
            }
        }
    }

    protected void init() {
        initController();

        initMainWindow();
        initContentPane();
        initTraceList();
        menu = initMenu();
        if (menu != null) {
            dlg.setJMenuBar(menu);
        }
        initEscListener();
    }

    protected void initController() {
        dlg.addWindowListener(controller = new UnitController(this));
    }

    protected void initEscListener() {
        if (controller == null || contentPane == null) {
            return;
        }
        contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "hide_on_esc");
        contentPane.getActionMap().put("hide_on_esc", new AbstractAction() {

            private static final long serialVersionUID = 5676714211418302901L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                controller.windowClosing(null);
            }
        });
    }

    protected UnitMenu initMenu() {
        return new UnitMenu(this);
    }

    protected void initTraceList() {
        contentPane.add(traceList = new TraceView(unit.getTrace()));
    }

    public void setStatus(final String status) {
        if (traceList != null) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    final TraceView traceView = traceList;
                    if (traceView != null) {
                        traceView.setStatus(status);
                    }
                }
            });
        }
    }

    public TraceView getTraceList() {
        return traceList;
    }
    private JPanel contentPane = null;

    protected void initContentPane() {
        dlg.setContentPane(contentPane = new JPanel());
        contentPane.setLayout(new BorderLayout(5, 5));
    }

    protected void initMainWindow() {
        dlg.setSize(300, 350);
        dlg.setLocationRelativeTo(null);//center window
        InstanceConfigStore.restoreUnitViewBounds(this);
        dlg.setTitle(unit.getFullTitle());
        dlg.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        unit.registerListener(new UnitListener() {

            @Override
            public void stateChanged(final Unit unit, final UnitState oldState, final UnitState newState) {
                if (newState == UnitState.STARTED) {
                    dlg.setTitle(unit.getFullTitle());
                }
            }
        });
    }

    public Unit getUnit() {
        return unit;
    }

    public UnitController getController() {
        return controller;
    }
//dispose

    public void dispose() {
        if (dlg != null) {
            InstanceConfigStore.storeUnitViewBounds(this);
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
    }
    private boolean isDisposed = false;

    public boolean isDisposed() {
        return isDisposed;
    }

    public JDialog getDialog() {
        return dlg;
    }

    public void setVisible(final boolean b) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                getDialog().setVisible(b);
            }
        });
    }

    public boolean isVisible() {
        return getDialog().isVisible();
    }

    protected JPanel getContentPane() {
        return contentPane;
    }
}
