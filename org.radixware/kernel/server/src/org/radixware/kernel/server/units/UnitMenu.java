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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;


public class UnitMenu extends JMenuBar {
    
    private static final long serialVersionUID = 4770776268905462191L;
    protected final JMenu mainMenu;
    private final JMenuItem menuItemStart;
    private final JMenuItem menuItemStop;
    private final JMenuItem menuItemRestart;
    private final JMenuItem menuItemSrvTrcOptDlg;
    private final JMenuItem menuItemAbort;
    protected UnitView view;
    
    public UnitMenu(final UnitView view) {
        super();
        this.view = view;
        mainMenu = new JMenu(MenuText.MENU);
        this.add(mainMenu);
        menuItemStart = new JMenuItem(MenuText.START);
        menuItemStart.addActionListener(new StartActionListener());
        mainMenu.add(menuItemStart);
        menuItemStop = new JMenuItem(MenuText.STOP);
        menuItemStop.addActionListener(new StopActionListener());
        mainMenu.add(menuItemStop);
        menuItemRestart = new JMenuItem(MenuText.RESTART);
        menuItemRestart.addActionListener(new RestartActionListener());
        mainMenu.add(menuItemRestart);
        menuItemAbort = new JMenuItem(MenuText.ABORT);
        menuItemAbort.addActionListener(new AbortActionListener());
        mainMenu.add(menuItemAbort);
        mainMenu.addSeparator();
        menuItemSrvTrcOptDlg = new StartSrvTraceOptDlgMenuItem();
        mainMenu.add(menuItemSrvTrcOptDlg);
        
        mainMenu.addMenuListener(new MenuListener() {
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
        actualize();//NOPMD
    }
    
    protected void actualize() {
        final UnitState st = view.getUnit().getState();
        menuItemStop.setEnabled(st == UnitState.STARTED || st == UnitState.START_POSTPONED);
        menuItemRestart.setEnabled(st == UnitState.STARTED);
        menuItemStart.setEnabled(st == UnitState.STOPPED || st == UnitState.START_POSTPONED);
        menuItemSrvTrcOptDlg.setEnabled(st == UnitState.STARTED && view.getUnit().getDbConnection() != null);
        menuItemAbort.setEnabled(st != UnitState.STOPPED);
    }
    
    private final class AbortActionListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            view.getController().abort();
        }
    }
    
    private final class StartActionListener implements ActionListener {
        
        @Override
        public void actionPerformed(final ActionEvent e) {
            view.getController().start();
        }
    }
    
    private final class StopActionListener implements ActionListener {
        
        @Override
        public void actionPerformed(final ActionEvent e) {
            view.getController().stop();
        }
    }
    
    private final class RestartActionListener implements ActionListener {
        
        @Override
        public void actionPerformed(final ActionEvent e) {
            view.getController().restart();
        }
    }
    
    private final class StartSrvTraceOptDlgMenuItem extends JMenuItem {
        
        public StartSrvTraceOptDlgMenuItem() {
            super(MenuText.SRV_SQL_TRACE_CFG);
            addActionListener(new StartSrvTraceOptDlg());
        }
        
        private final class StartSrvTraceOptDlg implements ActionListener {
            
            @Override
            public void actionPerformed(final ActionEvent e) {
                view.getController().startServerSideTraceOptionsDialog();
            }
        }
    }
    
    private static final class MenuText {
        
        static {
            final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.units.mess.messages");
            
            MENU = bundle.getString("MNU_MENU");
            RESTART = bundle.getString("MNU_RESTART_UNIT");
            START = bundle.getString("MNU_START_UNIT");
            STOP = bundle.getString("MNU_STOP_UNIT");
            SRV_SQL_TRACE_CFG = bundle.getString("MNU_SRV_SQL_TRACE_CFG");
            ABORT = bundle.getString("MNU_ABORT_UNIT");
        }
        static final String MENU;
        static final String START;
        static final String STOP;
        static final String RESTART;
        static final String ABORT;
        static final String SRV_SQL_TRACE_CFG;
    }
}
