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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.server.Server;
import org.radixware.kernel.server.instance.arte.ArtesPanel;
import org.radixware.kernel.server.widgets.TraceView;
import org.radixware.kernel.starter.radixloader.RadixLoader;

/**
 * GUI presentation of a server instance
 *
 */
public final class InstanceView {

    private final Instance instance;
    private volatile InstanceController controller;
    private volatile TraceView traceList;
    private volatile JFrame frame;
    private volatile ArtesPanel artesPanel;
    private static final Dimension MIN_SIZE = new Dimension(300, 350);

    static InstanceView newInstanceView(final Instance model) {
        //use factory method to prevent unconstructed this escape
        final InstanceView view = new InstanceView(model);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                view.createFrame();
            }
        });
        return view;
    }

    private InstanceView(final Instance model) {
        this.instance = model;
        InstanceConfigStore.restoreTraceListSettings();
    }

    private void createFrame() {
        frame = new JFrame();
        instance.registerListener(new InstanceListener() {
            @Override
            public void stateChanged(final Instance unit, final InstanceState oldState, final InstanceState newState) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (instance.getState() == InstanceState.STARTED) {
                            String pidPart = "";
                            try {
                                pidPart = " (PID: " + org.radixware.kernel.starter.utils.SystemTools.getCurrentProcessPid() + ")";
                            } catch (Exception ex) {
                                //ignore
                            }
                            frame.setTitle(instance.getFullTitle() + " [" + RadixLoader.getInstance().getTopLayerUrisAsString() + "]" + pidPart);
                        } else if (instance.getState() == InstanceState.STOPPED) {
                            frame.setTitle(Messages.TITLE_INSTANCE);
                        }
                    }
                });

            }
        });

        //Controller
        frame.addWindowListener(controller = new InstanceController(this));

        //Window
        frame.setSize(MIN_SIZE);
        frame.setLocationRelativeTo(null);//center window   
        InstanceConfigStore.restoreMainWindowBounds(frame);
        frame.setTitle(Messages.TITLE_INSTANCE);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        //ContentPane
        frame.setContentPane(contentPane = new JPanel());
        contentPane.setLayout(new BorderLayout(5, 5));
        contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "minimize_on_esc");
        contentPane.getActionMap().put("minimize_on_esc", new AbstractAction() {
            private static final long serialVersionUID = 0L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.setExtendedState(JFrame.ICONIFIED);
            }
        });
        //Trace
        traceList = new TraceView(instance.getTrace());
        //tabs
        tabs = new JTabbedPane();
        tabs.addTab(Messages.TAB_TRACE, traceList);
        tabs.addTab(Messages.TAB_UNITS, ViewUnitsPanel.newViewUnitsPanel(instance));
        artesPanel = new ArtesPanel(instance);
        tabs.addTab(Messages.TAB_ARTE_POOL, artesPanel);
        contentPane.add(tabs);
        //Menu
        frame.setJMenuBar(new InstanceMenu(this));
        frame.setMinimumSize(MIN_SIZE);
        frame.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                int w = Math.max(frame.getWidth(), frame.getMinimumSize().width);
                int h = Math.max(frame.getHeight(), frame.getMinimumSize().height);
                frame.setSize(w, h);
            }

        });
    }

//model	
    public Instance getInstance() {
        return instance;
    }
//controller	

    InstanceController getController() {
        return controller;
    }
    volatile boolean disposing = false;

    public void dispose() {
        synchronized (disposeSem) {// ����� ���������� � ���������� ����� � ��������
            if (disposing) {
                return;
            }
            disposing = true;
        }
        if (traceList != null) {
            traceList.close();
        }
        if (artesPanel != null) {
            artesPanel.stop();
        }
        try {
            InstanceConfigStore.storeMainWindowBounds(frame);
            InstanceConfigStore.storeTraceListSettings();
            try {
                InstanceConfigStore.save();
            } catch (Throwable e) {
                JOptionPane.showMessageDialog(frame, Messages.ERR_CANT_SAVE_CFG + ExceptionTextFormatter.getExceptionMess(e), Messages.TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
            }
        } finally {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    frame.dispose();
                    Server.returnFromMain();
                }
            });
        }
    }
    private final Object disposeSem = new Object();
    private static final long serialVersionUID = 4521912377175732519L;
    private JPanel contentPane = null;  //  @jve:decl-index=0:visual-constraint="169,109"
    private JTabbedPane tabs = null;

    public JFrame getFrame() {
        return frame;
    }

    public void setVisible(final boolean b) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(b);
            }
        });
    }
}