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
package org.radixware.kernel.server.units.netport;

import java.awt.BorderLayout;
import java.awt.AWTKeyStroke;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.JButton;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.server.units.ServerItemView;
import org.radixware.kernel.server.units.UnitState;
import org.radixware.kernel.server.widgets.TableWithSeverityColumnRenderer;
import org.radixware.kernel.server.widgets.TraceSettings;
import org.radixware.kernel.server.widgets.TraceView;

class ChannelsPanel extends JPanel {

    private static final long serialVersionUID = 8130083921612990094L;
    private static final long TIC_MILLIS = 500; //�����, �� ������� �������� �����, � �������������

    private final class TableChangedListener implements TableModelListener {

        private int selectedRow = -1;

        @Override
        public void tableChanged(TableModelEvent e) {
            selectedRow = scrollPane.getArteInstancesTable().getSelectedRow();
            scrollPane.getArteInstancesTable().tableChanged(e);
            if (selectedRow != -1 && selectedRow < scrollPane.getArteInstancesTable().getRowCount()) {
                scrollPane.getArteInstancesTable().setRowSelectionInterval(selectedRow, selectedRow);
            }
        }
    }

    private static final class ListenersTableModel implements TableModel {

        private static final int COLUMN_COUNT = 4;
        private static final String[] HEADER = {Messages.COL_SEVERITY, Messages.COL_ID, Messages.COL_NAME, Messages.COL_ACTIVE_SEANCES};
        private final List<TableModelListener> tabListeners;
        private final NetPortHandlerUnit unit;
        private volatile NetChannel[] channelsSnapshot;

        public ListenersTableModel(final NetPortHandlerUnit unit) {
            this.unit = unit;
            tabListeners = new ArrayList<>();
            channelsSnapshot = unit.getNetChannelsSnapshot();
        }

        public void update() {
            SwingUtilities.invokeLater(
                    new Runnable() {
                        @Override
                        public void run() {
                            channelsSnapshot = unit.getNetChannelsSnapshot();
                            for (TableModelListener listener : tabListeners) {
                                if (listener instanceof TableChangedListener) {
                                    listener.tableChanged(new TableModelEvent(ListenersTableModel.this));
                                }
                            }
                        }
                    });
        }

        private NetChannel getChannelAt(int row) {
            try {
                return channelsSnapshot[row];
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
            return channelsSnapshot.length;
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
            if (column < 0 || column >= getColumnCount()) {
                return null;
            }
            final NetChannel list = getChannelAt(row);
            if (list == null) {
                return null;
            }
            switch (column) {
                case 0:
                    final ServerItemView v = list.getView();
                    if (v == null) {
                        return null;
                    } else {
                        return v.getTraceMaxSeverity();
                    }
                case 1:
                    return list.id;

                case 2:
                    return list.getTitle();
                case 3:
                    return String.valueOf(list.getActiveSeanceCount());
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
            tabListeners.add(l);
        }

        @Override
        public void removeTableModelListener(TableModelListener l) {
            tabListeners.remove(l);
        }
    }

    private final class NetListenerPopupMenu extends JPopupMenu implements ActionListener, PopupMenuListener {

        private static final long serialVersionUID = -8112879207273503016L;
        private static final String HIDE_COMMAND = "hide";
        private static final String SHOW_COMMAND = "show";
        private final JMenuItem hide;
        private final JMenuItem show;

        public NetListenerPopupMenu() {
            super();

            hide = new JMenuItem(Messages.MENU_HIDE);
            hide.setActionCommand(HIDE_COMMAND);
            hide.addActionListener(this);
            add(hide);

            show = new JMenuItem(Messages.MENU_SHOW);
            show.setActionCommand(SHOW_COMMAND);
            show.addActionListener(this);
            add(show);

            addPopupMenuListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            NetChannel netListener = tableModel.getChannelAt(scrollPane.getArteInstancesTable().getSelectedRow());
            String command = e.getActionCommand();
            if (command.equals(HIDE_COMMAND)) {
                final ServerItemView v = netListener.getView();
                if (v != null) {
                    v.setVisible(false);
                }
            } else if (command.equals(SHOW_COMMAND)) {
                final ServerItemView v = netListener.getView();
                if (v != null) {
                    v.setVisible(true);
                }
            }
        }

        @Override
        public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
            NetChannel netListener = tableModel.getChannelAt(scrollPane.getArteInstancesTable().getSelectedRow());
            final ServerItemView v = netListener.getView();
            if (v == null) {
                hide.setEnabled(false);
                show.setEnabled(false);
            } else if (v.isVisible()) {
                hide.setEnabled(true);
                show.setEnabled(false);
            } else {
                show.setEnabled(true);
                hide.setEnabled(false);
            }
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

    private final class NetListenersScrollPane extends JScrollPane {

        private static final long serialVersionUID = -849965925488834757L;
        private final NetListenersTable table;

        private final class NetListenersTable extends JTable {

            private static final long serialVersionUID = 1552549028233029619L;
            private final static int DEFAULT_ROW_HEIGHT = 26;
            private final NetListenerPopupMenu popup;

            public NetListenersTable(final ListenersTableModel model) {
                super(model);
                setDefaultRenderer(getColumnClass(0), new TableWithSeverityColumnRenderer());
                setShowHorizontalLines(false);
                setShowVerticalLines(false);
                setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                setCellSelectionEnabled(true);
                updateSettings();
                TraceSettings.addListener(new TraceSettings.SettingsListener() {
                    @Override
                    public void settingsChanged() {
                        updateSettings();
                    }
                });
                getColumnModel().getColumn(0).setMinWidth(DEFAULT_ROW_HEIGHT);
                getColumnModel().getColumn(0).setPreferredWidth(DEFAULT_ROW_HEIGHT);
//				int width = ListenersPanel.this.getWidth() - DEFAULT_ROW_HEIGHT;
//				for (int i = 1; i < getColumnCount(); i++)
//					getColumnModel().getColumn(i).setPreferredWidth(width / (getColumnCount() - 1));
                setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
                Set<AWTKeyStroke> forwardKeys = getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
                Set<AWTKeyStroke> newForwardKeys = new HashSet<AWTKeyStroke>(forwardKeys);
                newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
                setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
                popup = new NetListenerPopupMenu();
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            int row = NetListenersTable.this.rowAtPoint(e.getPoint());
                            int column = NetListenersTable.this.columnAtPoint(e.getPoint());
                            if (row >= 0 && row < NetListenersTable.this.getRowCount()
                                    && column >= 0 && column < NetListenersTable.this.getColumnCount()) {
                                NetListenersTable.this.setRowSelectionInterval(row, row);
                                popup.show(e.getComponent(), e.getX(), e.getY());
                            }
                        } else if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                            int row = NetListenersTable.this.rowAtPoint(e.getPoint());
                            int column = NetListenersTable.this.columnAtPoint(e.getPoint());
                            if (row >= 0 && row < NetListenersTable.this.getRowCount()
                                    && column >= 0 && column < NetListenersTable.this.getColumnCount()) {
                                final NetChannel list = model.getChannelAt(row);
                                if (list != null) {
                                    final ServerItemView v = list.getView();
                                    if (v != null) {
                                        v.setVisible(!list.getView().isVisible());
                                    }
                                }
                            }
                        }
                    }
                });
                ActionListener actionListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (NetListenersTable.this.getSelectedRow() != -1) {
                            popup.show(NetListenersTable.this, 0, 0);
                        }
                    }
                };
                KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
                registerKeyboardAction(actionListener, enter, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
                Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    Object value = getValueAt(rowIndex, 0);
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
                    }
                    c.setFont(TraceSettings.getCurrentSettings().traceFont);
                }
                return c;
            }

            private void updateSettings() {
                setRowHeight(Math.max(DEFAULT_ROW_HEIGHT, TraceSettings.getRowHeightForCurrentFont()));
            }
        }

        public NetListenersScrollPane(final ListenersTableModel model) {
            super();
            table = new NetListenersTable(model);
            setViewportView(table);
        }

        public JTable getArteInstancesTable() {
            return table;
        }
    }

    public void stop() {
        if (thread != null) {
            thread.interrupt();
        }
        thread = null;
    }

    public void start() {
        if (thread == null || thread.getState() == Thread.State.TERMINATED) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        tableModel.update();
                        try {
                            Thread.sleep(TIC_MILLIS);
                        } catch (InterruptedException e) {
                            tableModel.update();
                            return;
                        }
                    }
                }
            }, "Arte Instances Thread of " + unit.getFullTitle());
            thread.setPriority(Thread.NORM_PRIORITY - 1);
            thread.start();
        }
    }
    private final NetListenersScrollPane scrollPane;
    private final ListenersTableModel tableModel;
    private Thread thread = null;
    private final NetPortHandlerUnit unit;

    ChannelsPanel(final NetPortHandlerUnit unit) {
        super(new BorderLayout());
        this.unit = unit;
        tableModel = new ListenersTableModel(unit);
        scrollPane = new NetListenersScrollPane(tableModel);
        tableModel.addTableModelListener(new TableChangedListener());
        add(scrollPane, BorderLayout.CENTER);
        addComponentListener(new ComponentAdapter() {
//			@Override
//			public void componentResized(ComponentEvent e) {
//				int w0 = scrollPane.getArteInstancesTable().getColumnModel().getColumn(0).getWidth();
//				int width = ListenersPanel.this.getWidth() - w0;
//				int colCount = scrollPane.getArteInstancesTable().getColumnCount();
//				for (int i = 1; i < colCount; i++)
//					scrollPane.getArteInstancesTable().getColumnModel().getColumn(i).setPreferredWidth(width / (colCount - 1));
//			}
            @Override
            public void componentShown(ComponentEvent e) {
                if (unit.getState() == UnitState.STARTED) {
                    start();
                }
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                stop();
            }
        });

        final JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        initToolbar(toolbar);
        add(toolbar, BorderLayout.NORTH);
    }

    private void initToolbar(final JToolBar toolbar) {
        final JButton clearTraceButton = new JButton(TraceSettings.loadIcon(TraceSettings.class, "img/clear.png"));
        clearTraceButton.setToolTipText(TraceView.Messages.HINT_CLEAR);
        clearTraceButton.setFocusable(false);
        clearTraceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (NetChannel channel : unit.getNetChannelsSnapshot()) {
                    ServerItemView channelView = channel.getViewIfCreated();
                    if (channelView != null && channelView.getTraceList() != null) {
                        channelView.getTraceList().clearTrace();
                    }
                }
            }
        });
        toolbar.add(clearTraceButton);

        final JButton traceProfileButton = new JButton(TraceSettings.loadIcon(TraceSettings.class, "img/trace_profile.png"));
        traceProfileButton.setToolTipText(TraceView.Messages.BTN_TRACE_PROFILE);
        traceProfileButton.addActionListener(
                new ActionListener() {

                    private String lastOverriddenProfie = "Event";

                    @Override
                    public void actionPerformed(final ActionEvent ev) {
                        final String newProfile = TraceView.editProfile(lastOverriddenProfie, ChannelsPanel.this);
                        if (newProfile != null) {
                            lastOverriddenProfie = newProfile;
                            for (NetChannel channel : unit.getNetChannelsSnapshot()) {
                                channel.getTrace().overrideGuiProfile(newProfile);
                            }
                        }
                    }
                });
        traceProfileButton.setFocusable(false);
        toolbar.add(traceProfileButton);
    }

    private static final class Messages {

        static {
            final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.units.arte.mess.messages");

            COL_ID = bundle.getString("COL_ID");
            COL_NAME = bundle.getString("COL_NAME");
            COL_SEVERITY = bundle.getString("COL_SEVERITY");
            COL_ACTIVE_SEANCES = bundle.getString("COL_ACTIVE_SEANCES");
            //COL_STATE = bundle.getString("COL_STATE");
            //MES_BUSY = bundle.getString("MES_BUSY");
            //MES_FREE = bundle.getString("MES_FREE");
            MENU_HIDE = bundle.getString("MENU_HIDE");
            MENU_SHOW = bundle.getString("MENU_SHOW");
            //MENU_STOP = bundle.getString("MENU_STOP");
            TLP_ERROR = bundle.getString("TLP_ERROR");
            TLP_ALARM = bundle.getString("TLP_ALARM");
            TLP_WARNING = bundle.getString("TLP_WARNING");
            TLP_EVENT = bundle.getString("TLP_EVENT");
            TLP_DEBUG = bundle.getString("TLP_DEBUG");
            TLP_NONE = bundle.getString("TLP_NONE");
        }
        static final String COL_ID;
        static final String COL_NAME;
        static final String COL_SEVERITY;
        static final String COL_ACTIVE_SEANCES;
        //static final String COL_STATE;
        //static final String MES_BUSY;
        //static final String MES_FREE;
        static final String MENU_HIDE;
        static final String MENU_SHOW;
        //static final String MENU_STOP;
        static final String TLP_ERROR;
        static final String TLP_ALARM;
        static final String TLP_WARNING;
        static final String TLP_EVENT;
        static final String TLP_DEBUG;
        static final String TLP_NONE;
    }
}
