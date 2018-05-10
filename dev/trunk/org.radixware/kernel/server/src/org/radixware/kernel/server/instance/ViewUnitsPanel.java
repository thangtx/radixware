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

import java.awt.AWTKeyStroke;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.JButton;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.apache.commons.logging.LogFactory;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.server.units.UnitState;
import org.radixware.kernel.server.units.UnitView;
import org.radixware.kernel.server.units.Unit;
import org.radixware.kernel.server.widgets.TraceSettings;
import org.radixware.kernel.server.widgets.TableWithSeverityColumnRenderer;
import org.radixware.kernel.server.widgets.TraceView;

class ViewUnitsPanel extends JPanel {

    private static final long serialVersionUID = 8762713745209381431L;
    private static final long SLEEP_INTERVAL = 500; //�����, �� ������� �������� �����, � �������������

    private final class TableChangedListener implements TableModelListener {

        private int selectedRow = -1;

        @Override
        public void tableChanged(TableModelEvent e) {
            selectedRow = unitsScrollPane.getUnitsTable().getSelectedRow();
            unitsScrollPane.getUnitsTable().tableChanged(e);
            if (selectedRow != -1 && selectedRow < unitsScrollPane.getUnitsTable().getRowCount()) {
                unitsScrollPane.getUnitsTable().setRowSelectionInterval(selectedRow, selectedRow);
            }
        }
    }

    private static final class UnitsModel implements TableModel {

        private static final int COLUMN_COUNT = 5;
        private final static String[] HEADER = {Messages.COL_SEVERITY, Messages.COL_ID, Messages.COL_TYPE, Messages.COL_NAME, Messages.COL_STATE};
        private final List<TableModelListener> listeners;
        private final Instance instanceModel;
        private List<Unit> unitsSnapshot = new ArrayList<>();

        public UnitsModel(final Instance instanceModel) {
            this.instanceModel = instanceModel;
            listeners = new ArrayList<>();
        }

        public void update() {
            final List<Unit> newSnapshot = new ArrayList<>();
            newSnapshot.add(null);
            List<List<Unit>> unitsByPriorities = instanceModel.getUnitsByPriorities();
            if (unitsByPriorities != null) {
                for (List<Unit> list : unitsByPriorities) {
                    if (!newSnapshot.isEmpty() && newSnapshot.get(newSnapshot.size() - 1) != null) {
                        newSnapshot.add(null);
                    }
                    if (list != null) {
                        newSnapshot.addAll(list);
                    }
                }
            }
            if (newSnapshot.size() == 1) {
                newSnapshot.clear();
            }
            InstanceView instanceView = instanceModel.getViewIfCreated();
            if (instanceView != null && !instanceView.isDisposing()) {
                SwingUtilities.invokeLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                unitsSnapshot.clear();
                                unitsSnapshot.addAll(newSnapshot);
                                for (TableModelListener listener : listeners) {
                                    if (listener instanceof TableChangedListener) {
                                        listener.tableChanged(new TableModelEvent(UnitsModel.this));
                                    }
                                }
                            }
                        });
            }
        }

        public Unit getUnitModelAt(int row) {
            try {
                return unitsSnapshot.get(row);
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        @Override
        public int getRowCount() {
            return unitsSnapshot.size();
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public void setValueAt(Object value, int row, int column) {
            //do nothing
        }

        @Override
        public Object getValueAt(int row, int column) {
            Unit unitModel = getUnitModelAt(row);
            if (unitModel == null) {
                return null;
            }
            switch (column) {
                case 0:
                    if (unitModel.getView().getTraceList() != null) {
                        return unitModel.getView().getTraceList().getTraceMaxSeverity();
                    }
                    return null;
                case 1:
                    return String.valueOf(unitModel.getId());
                case 2:
                    return unitModel.getUnitTypeTitle();
                case 3:
                    return unitModel.getTitle();
                case 4: {
                    switch (unitModel.getState()) {
                        case STARTED:
                            return Messages.ST_UNIT_STARTED;
                        case STOPPED:
                            return Messages.ST_UNIT_STOPPED;
                        case START_POSTPONED:
                            return Messages.ST_UNIT_START_POSTPONED;
                        case STARTING:
                            return Messages.ST_UNIT_STARTING;
                        case STOPPING:
                            return Messages.ST_UNIT_STOPPING;
                        default:
                            return null;
                    }
                }
                default:
                    return null;
            }
        }

        @Override
        public String getColumnName(int column) {
            return HEADER[column];
        }

        @Override
        public Class<?> getColumnClass(int column) {
            return String.class;
        }

        @Override
        public void addTableModelListener(TableModelListener l) {
            listeners.add(l);
        }

        @Override
        public void removeTableModelListener(TableModelListener l) {
            listeners.remove(l);
        }
    }

    private final class UnitsPopupMenu extends JPopupMenu implements ActionListener, PopupMenuListener {

        private static final long serialVersionUID = -7744503234127375528L;
        private static final String SHOW_COMMAND = "show";
        private static final String LAUNCH_COMMAND = "launch";
        private static final String STOP_COMMAND = "stop";
        private static final String UNLOAD_COMMAND = "unload";
        private static final String RESTART_COMMAND = "restart";
        private static final String ABORT_COMMAND = "abort";
        private final JMenuItem showing;
        private final JMenuItem launching;
        private final JMenuItem stopping;
        private final JMenuItem unloading;
        private final JMenuItem abort;
        private final JMenuItem restart;

        public UnitsPopupMenu() {
            super();

            showing = new JMenuItem();
            showing.setActionCommand(SHOW_COMMAND);
            showing.addActionListener(this);
            add(showing);

            launching = new JMenuItem(Messages.MENU_START);
            launching.setActionCommand(LAUNCH_COMMAND);
            launching.addActionListener(this);
            add(launching);

            stopping = new JMenuItem(Messages.MENU_STOP);
            stopping.setActionCommand(STOP_COMMAND);
            stopping.addActionListener(this);
            add(stopping);

            restart = new JMenuItem(Messages.MENU_RESTART);
            restart.setActionCommand(RESTART_COMMAND);
            restart.addActionListener(this);
            add(restart);

            abort = new JMenuItem(Messages.MENU_ABORT);
            abort.setActionCommand(ABORT_COMMAND);
            abort.addActionListener(this);
            add(abort);

            unloading = new JMenuItem(Messages.MENU_UNLOAD);
            unloading.setActionCommand(UNLOAD_COMMAND);
            unloading.addActionListener(this);
            add(unloading);

            addPopupMenuListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final Unit unit = unitsModel.getUnitModelAt(unitsScrollPane.getUnitsTable().getSelectedRow());
            String command = e.getActionCommand();
            if (command.equals(SHOW_COMMAND)) {
                unit.getView().setVisible(!unit.getView().isVisible());
            } else if (command.equals(LAUNCH_COMMAND)) {
                unit.getView().getController().start();
            } else if (command.equals(UNLOAD_COMMAND)) {
                new Thread("Unit stop and interrtupt " + System.currentTimeMillis()) {
                    @Override
                    public void run() {
                        instanceModel.stopAndUnloadUnit(unit, Messages.COMMAND_FROM_GUI);
                    }
                }.start();
            } else if (command.equals(RESTART_COMMAND)) {
                if (unit.getState() == UnitState.STARTED) {
                    unit.getView().getController().restart();
                }
            } else if (command.equals(STOP_COMMAND)) {
                unit.getView().getController().stop();
            } else if (command.equals(ABORT_COMMAND)) {
                unit.getView().getController().abort();
            }
        }

        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            final Unit unit = unitsModel.getUnitModelAt(unitsScrollPane.getUnitsTable().getSelectedRow());
            if (unit == null) {
                showing.setEnabled(false);
                launching.setEnabled(false);
                restart.setEnabled(false);
                abort.setEnabled(false);
                stopping.setEnabled(false);
                unloading.setEnabled(false);
                return;
            } else {
                showing.setEnabled(true);
                unloading.setEnabled(true);
            }
            if (unit.getView().isVisible()) {
                showing.setText(Messages.MENU_HIDE);
            } else {
                showing.setText(Messages.MENU_SHOW);
            }
            stopping.setEnabled(unit.getState() == UnitState.STARTED || unit.getState() == UnitState.STARTING || unit.getState() == UnitState.START_POSTPONED);
            launching.setEnabled(unit.getState() == UnitState.STOPPED || unit.getState() == UnitState.START_POSTPONED);
            restart.setEnabled(unit.getState() == UnitState.STARTED);
            abort.setEnabled(unit.getState() != UnitState.STOPPED);
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            //do nothing
        }

        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
            //do nothing
        }
    }

    private final class UnitsScrollPane extends JScrollPane {

        private static final long serialVersionUID = -7155978988827355397L;
        private final UnitsTable unitsTable;

        private final class UnitsTable extends JTable {

            private static final long serialVersionUID = -1624070425201983351L;
            private final UnitsPopupMenu popupMenu;
            private static final int DEFAULT_ROW_HEIGHT = 26;

            public UnitsTable(final UnitsModel model) {
                super(model);
                popupMenu = new UnitsPopupMenu();
                setDefaultRenderer(getColumnClass(0), new TableWithSeverityColumnRenderer());
                setShowHorizontalLines(false);
                setShowVerticalLines(false);
                setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                setCellSelectionEnabled(true);
                TraceSettings.addListener(new TraceSettings.SettingsListener() {
                    @Override
                    public void settingsChanged() {
                        updateSettings();
                        repaint();
                    }
                });
                updateSettings();
                getColumnModel().getColumn(0).setMinWidth(DEFAULT_ROW_HEIGHT);
                getColumnModel().getColumn(0).setPreferredWidth(DEFAULT_ROW_HEIGHT);
                getColumnModel().getColumn(1).setPreferredWidth(100);
//				int width = ViewUnitsPanel.this.getWidth() - DEFAULT_ROW_HEIGHT - 106;
//				for (int i = 2; i < getColumnCount(); i++) {
//					getColumnModel().getColumn(i).setPreferredWidth(width / (getColumnCount() - 2));
//				}
                setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
                Set<AWTKeyStroke> forwardKeys = getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
                Set<AWTKeyStroke> newForwardKeys = new HashSet<AWTKeyStroke>(forwardKeys);
                newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
                setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            int row = UnitsTable.this.rowAtPoint(e.getPoint());
                            int column = UnitsTable.this.columnAtPoint(e.getPoint());
                            if (row >= 0 && row < UnitsTable.this.getRowCount()
                                    && column >= 0 && column < UnitsTable.this.getColumnCount()) {
                                UnitsTable.this.setRowSelectionInterval(row, row);
                                popupMenu.show(e.getComponent(), e.getX(), e.getY());
                            }
                        } else if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                            int row = UnitsTable.this.rowAtPoint(e.getPoint());
                            int column = UnitsTable.this.columnAtPoint(e.getPoint());
                            if (row >= 0 && row < model.getRowCount()
                                    && column >= 0 && column < model.getColumnCount()) {
                                Unit unit = model.getUnitModelAt(row);
                                if (unit != null) {
                                    UnitView unitView = unit.getView();
                                    if (unitView != null) {
                                        unitView.setVisible(!unitView.isVisible());
                                    }
                                }
                            }
                        }
                    }
                });
                ActionListener actionListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (UnitsTable.this.getSelectedRow() != -1) {
                            popupMenu.show(UnitsTable.this, 0, 0);
                        }
                    }
                };
                KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
                registerKeyboardAction(actionListener, enter, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
            }

            private void updateSettings() {
                setRowHeight(Math.max(TraceSettings.getRowHeightForCurrentFont(), DEFAULT_ROW_HEIGHT));
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
                Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
                Object value = getValueAt(rowIndex, vColIndex);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;

                    if (value instanceof EEventSeverity) {
                        EEventSeverity severity = (EEventSeverity) value;
                        if (severity == EEventSeverity.DEBUG) {
                            jc.setToolTipText(Messages.TLP_DEBUG);
                        } else if (severity == EEventSeverity.EVENT) {
                            jc.setToolTipText(Messages.TLP_EVENT);
                        } else if (severity == EEventSeverity.WARNING) {
                            jc.setToolTipText(Messages.TLP_WARNING);
                        } else if (severity == EEventSeverity.ERROR) {
                            jc.setToolTipText(Messages.TLP_ERROR);
                        } else if (severity == EEventSeverity.ALARM) {
                            jc.setToolTipText(Messages.TLP_ALARM);
                        } else {
                            jc.setToolTipText(Messages.TLP_NONE);
                        }
                    } else {
                        if (value != null) {
                            jc.setToolTipText(value.toString());
                        } else {
                            jc.setToolTipText(null);
                            if (vColIndex == 0 && value == null) {
                                int idx = 0;
                                for (int i = 0; i < rowIndex; i++) {
                                    if (getValueAt(i, 0) == null) {
                                        idx++;
                                    }
                                }
                                if (c instanceof JLabel) {
                                    ((JLabel) c).setText((idx + 1) + ")");
                                }
                            }
                        }
                    }

                    c.setFont(TraceSettings.getCurrentSettings().traceFont);
                }

                return c;
            }
        }

        public UnitsScrollPane(final UnitsModel model) {
            super();
            unitsTable = new UnitsTable(model);
            setViewportView(unitsTable);
        }

        public JTable getUnitsTable() {
            return unitsTable;
        }
    }

    public void stop() {
        if (thread != null) {
            thread.interrupt();
            try {
                thread.join(5000);
            } catch (InterruptedException ex) {
                LogFactory.getLog(ViewUnitsPanel.class).warn("Interrupted while waiting for ViewUnitsPanel termination", ex);
            }
            thread = null;
        }

    }

    public void start() {
        if (thread == null || thread.getState() == Thread.State.TERMINATED) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    final InstanceView view = instanceModel.getViewIfCreated();
                    while (view != null && !view.isDisposing()) {
                        unitsModel.update();
                        try {
                            Thread.sleep(SLEEP_INTERVAL);
                        } catch (InterruptedException e) {
                            unitsModel.update();
                            return;
                        }
                    }
                }
            }, "View Units Thread of " + instanceModel.getFullTitle());
            thread.setDaemon(true);
            thread.start();
        }
    }
    private final UnitsScrollPane unitsScrollPane;
    private final UnitsModel unitsModel;
    private volatile Thread thread = null;
    private final Instance instanceModel;

    static ViewUnitsPanel newViewUnitsPanel(final Instance instanceModel) {
        //use factory method to prevent unconstructed this escape
        final ViewUnitsPanel panel = new ViewUnitsPanel(instanceModel);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                panel.start();
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                panel.stop();
            }
        });
        return panel;
    }

    private ViewUnitsPanel(final Instance instanceModel) {
        super(new BorderLayout());
        this.instanceModel = instanceModel;
        unitsModel = new UnitsModel(instanceModel);
        unitsScrollPane = new UnitsScrollPane(unitsModel);
        unitsModel.addTableModelListener(new TableChangedListener());
        final JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        add(toolbar, BorderLayout.NORTH);
        initToolbar(toolbar);
        add(unitsScrollPane, BorderLayout.CENTER);
    }

    private void initToolbar(final JToolBar toolbar) {
        final JButton clearTraceButton = new JButton(TraceSettings.loadIcon(TraceSettings.class, "img/clear.png"));
        clearTraceButton.setToolTipText(TraceView.Messages.HINT_CLEAR);
        clearTraceButton.setFocusable(false);
        clearTraceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Unit u : instanceModel.getUnits()) {
                    final UnitView view = u.getView();
                    if (view != null && view.getTraceList() != null) {
                        view.getTraceList().clearTrace();
                    }
                }
            }
        });
        toolbar.add(clearTraceButton);

        final JButton traceProfileButton = new JButton(TraceSettings.loadIcon(TraceSettings.class, "img/trace_profile.png"));
        traceProfileButton.setToolTipText(TraceView.Messages.BTN_TRACE_PROFILE);
        traceProfileButton.addActionListener(
                new ActionListener() {

                    private String lastOverriddenProfile = "Event";

                    @Override
                    public void actionPerformed(final ActionEvent ev) {
                        final String newProfile = TraceView.editProfile(lastOverriddenProfile, ViewUnitsPanel.this);
                        if (newProfile != null) {
                            lastOverriddenProfile = newProfile;
                            for (Unit u : instanceModel.getUnits()) {
                                u.getTrace().overrideGuiProfile(newProfile);
                            }
                        }
                    }
                });
        traceProfileButton.setFocusable(false);
        toolbar.add(traceProfileButton);
    }

    private static final class Messages {

        static {
            final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.instance.mess.messages");

            COMMAND_FROM_GUI = bundle.getString("COMMAND_FROM_GUI");
            COL_ID = bundle.getString("COL_ID");
            COL_TYPE = bundle.getString("COL_TYPE");
            COL_NAME = bundle.getString("COL_NAME");
            COL_SEVERITY = bundle.getString("COL_SEVERITY");
            COL_STATE = bundle.getString("COL_STATE");
            MENU_HIDE = bundle.getString("MENU_HIDE");
            MENU_SHOW = bundle.getString("MENU_SHOW");
            MENU_STOP = bundle.getString("MENU_STOP");
            MENU_START = bundle.getString("MENU_START");
            MENU_RESTART = bundle.getString("MENU_RESTART");
            MENU_ABORT = bundle.getString("MENU_ABORT");
            MENU_UNLOAD = bundle.getString("MENU_UNLOAD");
            TLP_ERROR = bundle.getString("TLP_ERROR");
            TLP_ALARM = bundle.getString("TLP_ALARM");
            TLP_WARNING = bundle.getString("TLP_WARNING");
            TLP_EVENT = bundle.getString("TLP_EVENT");
            TLP_DEBUG = bundle.getString("TLP_DEBUG");
            TLP_NONE = bundle.getString("TLP_NONE");
            ST_UNIT_STOPPED = bundle.getString("ST_UNIT_STOPPED");
            ST_UNIT_STARTING = bundle.getString("ST_UNIT_STARTING");
            ST_UNIT_STARTED = bundle.getString("ST_UNIT_STARTED");
            ST_UNIT_STOPPING = bundle.getString("ST_UNIT_STOPPING");
            ST_UNIT_START_POSTPONED = bundle.getString("ST_UNIT_START_POSTPONED");
        }
        static final String COMMAND_FROM_GUI;
        static final String COL_ID;
        static final String COL_TYPE;
        static final String COL_NAME;
        static final String COL_SEVERITY;
        static final String COL_STATE;
        static final String MENU_HIDE;
        static final String MENU_SHOW;
        static final String MENU_STOP;
        static final String MENU_START;
        static final String MENU_UNLOAD;
        static final String MENU_RESTART;
        static final String MENU_ABORT;
        static final String TLP_ERROR;
        static final String TLP_ALARM;
        static final String TLP_WARNING;
        static final String TLP_EVENT;
        static final String TLP_DEBUG;
        static final String TLP_NONE;
        static final String ST_UNIT_STOPPED;
        static final String ST_UNIT_STARTING;
        static final String ST_UNIT_STARTED;
        static final String ST_UNIT_START_POSTPONED;
        static final String ST_UNIT_STOPPING;
    }
}
