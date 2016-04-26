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
package org.radixware.kernel.server.widgets;

import java.awt.AWTKeyStroke;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.Icon;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.server.trace.ServerTrace;
import org.radixware.kernel.server.trace.ViewTraceItem;
import org.radixware.kernel.common.utils.CircularBuffer;
import org.radixware.kernel.server.trace.TraceBuffer;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.trace.TraceProfile;

public class TraceView extends JPanel implements TraceSettings.SettingsListener {

    private static final long serialVersionUID = -1020935492401290652L;
    private static final int DEBUG_TRACE_BUF_SIZE = 1024;
    private static final int ABOVE_DEBUG_TRACE_BUF_SIZE = 200;
    private static final int MAX_WIDTH_HINT_MINIMIZE = 5000;
    private static final int MAX_WIDTH_HINT_RESIZE = MAX_WIDTH_HINT_MINIMIZE + 1;
    private final TraceToolBar toolBar;

    private final class TableChangedListener implements TableModelListener {

        private final TraceScrollPane traceScrollPane;
        private boolean firstTime = true;
        private boolean autoupdate = false;

        public TableChangedListener(final TraceScrollPane traceScrollPane) {
            this.traceScrollPane = traceScrollPane;
            traceScrollPane.getVerticalScrollBar().getModel().addChangeListener(new ChangeListener() {
                private int lastMax = 0;
                private int lastValue = 0;

                @Override
                public void stateChanged(ChangeEvent e) {
                    if (traceScrollPane.getVerticalScrollBar().getValue() < lastValue && (lastMax <= traceScrollPane.getVerticalScrollBar().getMaximum())) {
                        autoupdate = false;
                    } else if (isScrolledDown()) {
                        autoupdate = true;
                    }
                    lastValue = traceScrollPane.getVerticalScrollBar().getValue();
                    lastMax = traceScrollPane.getVerticalScrollBar().getMaximum();

                }
            });
        }

        private boolean isScrolledDown() {
            return traceScrollPane.getVerticalScrollBar().getValue() == traceScrollPane.getVerticalScrollBar().getMaximum() - traceScrollPane.getVerticalScrollBar().getVisibleAmount();
        }

        @Override
        public void tableChanged(TableModelEvent e) {
            boolean viewWasAtTheBottom = isScrolledDown() || firstTime;
            firstTime = false;
            traceScrollPane.getTraceTable().tableChanged(e);
            autoupdate = autoupdate || viewWasAtTheBottom;
            if (autoupdate) {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        traceScrollPane.getVerticalScrollBar().setValue(Integer.MAX_VALUE);
                    }
                });

            }
        }
    }

    public final class TraceModel implements TableModel, TraceBuffer {

        private final ServerTrace traceImpl;
        private static final int COLUMN_COUNT = 5;
        private final String[] header = {Messages.COL_SEVERITY, Messages.COL_DATE, Messages.COL_TIME,
            Messages.COL_SOURCE, Messages.COL_MESSAGE
        };
        private final List<TableModelListener> listeners;
        private final CircularBuffer<TraceItem> fromDebug;
        private final CircularBuffer<TraceItem> fromEvent;
        private final CircularBuffer<TraceItem> fromWarning;
        private final CircularBuffer<TraceItem> fromError;
        private final CircularBuffer<TraceItem> fromAlarm;
        private CircularBuffer<TraceItem> fromCurrent;
        private EEventSeverity severityFilter;
        private EEventSeverity currentMaxSeverity;
        private final SimpleDateFormat dateFormat;
        private final SimpleDateFormat timeFormat;

        public TraceModel(final ServerTrace traceImpl) {
            this.traceImpl = traceImpl;
            fromDebug = new CircularBuffer<TraceItem>(DEBUG_TRACE_BUF_SIZE);
            fromEvent = new CircularBuffer<TraceItem>(ABOVE_DEBUG_TRACE_BUF_SIZE);
            fromWarning = new CircularBuffer<TraceItem>(ABOVE_DEBUG_TRACE_BUF_SIZE);
            fromError = new CircularBuffer<TraceItem>(ABOVE_DEBUG_TRACE_BUF_SIZE);
            fromAlarm = new CircularBuffer<TraceItem>(ABOVE_DEBUG_TRACE_BUF_SIZE);
            fromCurrent = fromDebug;
            listeners = new CopyOnWriteArrayList<TableModelListener>();
            severityFilter = EEventSeverity.DEBUG;
            currentMaxSeverity = EEventSeverity.NONE;
            dateFormat = new SimpleDateFormat("dd/MM/yy");
            timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        }

        public void onTraceProfileOverriden() {
            toolBar.traceProfileButton.setIcon(new ImageIcon(TraceView.class.getResource("img/trace_profile_ovr.png")));
            toolBar.traceProfileButton.setToolTipText(Messages.BTN_TRACE_PROFILE_OVR);
        }

        private String getTraceProfile() {
            return traceImpl.getProfiles().getGuiTraceProfile();
        }

        private void setTraceProfile(final String profile) {
            traceImpl.overrideGuiProfile(profile);
            onTraceProfileOverriden();
        }

        @Override
        public void put(final TraceItem item) {
            if (item.severity == EEventSeverity.DEBUG) {
                fromDebug.add(item);
                if (currentMaxSeverity == EEventSeverity.NONE) {
                    fireSeverityChanged(currentMaxSeverity, EEventSeverity.DEBUG);
                    currentMaxSeverity = EEventSeverity.DEBUG;
                }
            } else if (item.severity == EEventSeverity.EVENT) {
                fromDebug.add(item);
                fromEvent.add(item);
                if (currentMaxSeverity == EEventSeverity.NONE || currentMaxSeverity == EEventSeverity.DEBUG) {
                    fireSeverityChanged(currentMaxSeverity, EEventSeverity.EVENT);
                    currentMaxSeverity = EEventSeverity.EVENT;
                }
            } else if (item.severity == EEventSeverity.WARNING) {
                fromDebug.add(item);
                fromEvent.add(item);
                fromWarning.add(item);
                if (currentMaxSeverity == EEventSeverity.NONE || currentMaxSeverity == EEventSeverity.DEBUG
                        || currentMaxSeverity == EEventSeverity.EVENT) {
                    fireSeverityChanged(currentMaxSeverity, EEventSeverity.WARNING);
                    currentMaxSeverity = EEventSeverity.WARNING;
                }
            } else if (item.severity == EEventSeverity.ERROR) {
                fromDebug.add(item);
                fromEvent.add(item);
                fromWarning.add(item);
                fromError.add(item);
                if (currentMaxSeverity != EEventSeverity.ERROR && currentMaxSeverity != EEventSeverity.ALARM) {
                    fireSeverityChanged(currentMaxSeverity, EEventSeverity.ERROR);
                    currentMaxSeverity = EEventSeverity.ERROR;
                }
            } else if (item.severity == EEventSeverity.ALARM) {
                fromDebug.add(item);
                fromEvent.add(item);
                fromWarning.add(item);
                fromError.add(item);
                fromAlarm.add(item);
                if (currentMaxSeverity != EEventSeverity.ALARM) {
                    fireSeverityChanged(currentMaxSeverity, EEventSeverity.ALARM);
                    currentMaxSeverity = EEventSeverity.ALARM;
                }
            }
            SwingUtilities.invokeLater(
                    new Runnable() {
                        @Override
                        public void run() {
                            for (TableModelListener listener : listeners) {
                                if (listener instanceof TableChangedListener) {
                                    listener.tableChanged(new TableModelEvent(TraceModel.this));
                                }
                            }
                        }
                    });
        }

        public TraceItem getTraceItemAt(final int row) {
            if (row >= fromCurrent.size() || row < 0) {
                return null;
            }
            return fromCurrent.get(row);
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        @Override
        public int getRowCount() {
            return fromCurrent.size();
        }

        @Override
        public boolean isCellEditable(final int row, final int column) {
            return false;
        }

        @Override
        public void setValueAt(final Object value, final int row, final int column) {
            //do nothing
        }

        @Override
        public Object getValueAt(final int row, final int column) {
            if (row < 0 || column < 0 || row >= fromCurrent.size() || column >= COLUMN_COUNT) {
                return null;
            }
            final TraceItem traceItem = fromCurrent.get(row);
            switch (column) {
                case 0:
                    return traceItem.severity;
                case 1:
                    return dateFormat.format(new Date(traceItem.time));
                case 2:
                    return timeFormat.format(new Date(traceItem.time));
                case 3:
                    return traceItem.source;
                case 4:
                    return traceItem.getMess();
                case 5:
                    return (traceItem instanceof ViewTraceItem) ? ((ViewTraceItem) traceItem).context : null;
                default:
                    return null;
            }
        }

        @Override
        public String getColumnName(final int column) {
            return header[column];
        }

        @Override
        public Class<?> getColumnClass(final int column) {
            return String.class;
        }

        @Override
        public void addTableModelListener(final TableModelListener l) {
            listeners.add(l);
        }

        @Override
        public void removeTableModelListener(final TableModelListener l) {
            listeners.remove(l);
        }

        public void clear() {
            fromDebug.clear();
            fromEvent.clear();
            fromWarning.clear();
            fromError.clear();
            fromAlarm.clear();
            fireSeverityChanged(currentMaxSeverity, EEventSeverity.NONE);
            currentMaxSeverity = EEventSeverity.NONE;
            SwingUtilities.invokeLater(
                    new Runnable() {
                        @Override
                        public void run() {
                            for (TableModelListener listener : listeners) {
                                if (listener instanceof TableChangedListener) {
                                    listener.tableChanged(new TableModelEvent(TraceModel.this));
                                }
                            }
                        }
                    });
        }

        public void setSeverityFilter(final EEventSeverity filter) {
            severityFilter = filter;
            if (severityFilter == EEventSeverity.DEBUG) {
                fromCurrent = fromDebug;
            } else if (severityFilter == EEventSeverity.EVENT) {
                fromCurrent = fromEvent;
            } else if (severityFilter == EEventSeverity.WARNING) {
                fromCurrent = fromWarning;
            } else if (severityFilter == EEventSeverity.ERROR) {
                fromCurrent = fromError;
            } else {
                fromCurrent = fromAlarm;
            }
            SwingUtilities.invokeLater(
                    new Runnable() {
                        @Override
                        public void run() {
                            for (TableModelListener listener : listeners) {
                                if (listener instanceof TableChangedListener) {
                                    listener.tableChanged(new TableModelEvent(TraceModel.this));
                                }
                            }
                        }
                    });
        }

        public EEventSeverity getSeverityFilter() {
            return severityFilter;
        }

        public EEventSeverity getTraceMaxSeverity() {
            return currentMaxSeverity;
        }
    }

    private static final class RowRenderer implements TableCellRenderer {

        private final JLabel label;
        private final Icon debugIcon;
        private final Icon eventIcon;
        private final Icon warningIcon;
        private final Icon errorIcon;
        private final Icon alarmIcon;

        public RowRenderer() {
            label = new JLabel();
            label.setBorder(new EmptyBorder(1, 4, 1, 1));
            label.setOpaque(true);
            debugIcon = TraceSettings.getIconForSeverity(EEventSeverity.DEBUG);
            eventIcon = TraceSettings.getIconForSeverity(EEventSeverity.EVENT);
            warningIcon = TraceSettings.getIconForSeverity(EEventSeverity.WARNING);
            errorIcon = TraceSettings.getIconForSeverity(EEventSeverity.ERROR);
            alarmIcon = TraceSettings.getIconForSeverity(EEventSeverity.ALARM);
        }

        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
                final boolean hasFocus, final int row, final int column) {
            if (value instanceof EEventSeverity) {
                label.setText(null);
                final EEventSeverity severity = (EEventSeverity) value;
                if (severity == EEventSeverity.DEBUG) {
                    label.setIcon(debugIcon);
                } else if (severity == EEventSeverity.EVENT) {
                    label.setIcon(eventIcon);
                } else if (severity == EEventSeverity.WARNING) {
                    label.setIcon(warningIcon);
                } else if (severity == EEventSeverity.ERROR) {
                    label.setIcon(errorIcon);
                } else {
                    label.setIcon(alarmIcon);
                }
                if (row != table.getSelectedRow()) {
                    if (row % 2 == 0) {
                        label.setBackground(TraceSettings.currentSettings.bgColor1);
                    } else {
                        label.setBackground(TraceSettings.currentSettings.bgColor2);
                    }
                } else {
                    label.setBackground(TraceSettings.currentSettings.bgSelColor);
                }
            } else {
                label.setIcon(null);
                label.setText(value != null ? value.toString() : "");
                label.setFont(TraceSettings.currentSettings.traceFont);
                if (row != table.getSelectedRow()) {
                    if (row % 2 == 0) {
                        label.setBackground(TraceSettings.currentSettings.bgColor1);
                    } else {
                        label.setBackground(TraceSettings.currentSettings.bgColor2);
                    }
                    final TraceItem traceItem = ((TraceModel) (table.getModel())).getTraceItemAt(row);
                    if (traceItem.severity == EEventSeverity.DEBUG) {
                        label.setForeground(TraceSettings.currentSettings.debugColor);
                    } else if (traceItem.severity == EEventSeverity.EVENT) {
                        label.setForeground(TraceSettings.currentSettings.eventColor);
                    } else if (traceItem.severity == EEventSeverity.WARNING) {
                        label.setForeground(TraceSettings.currentSettings.warningColor);
                    } else if (traceItem.severity == EEventSeverity.ERROR) {
                        label.setForeground(TraceSettings.currentSettings.errorColor);
                    } else {
                        label.setForeground(TraceSettings.currentSettings.alarmColor);
                    }
                } else {
                    label.setBackground(TraceSettings.currentSettings.bgSelColor);
                    label.setForeground(TraceSettings.currentSettings.selColor);
                }
            }
            return label;
        }
    }

    private final class TraceScrollPane extends JScrollPane {

        private static final long serialVersionUID = -2023521065360536794L;

        private final class TraceTable extends JTable {

            private static final long serialVersionUID = -8583372105494292679L;

            public TraceTable(final TraceModel traceModel) {
                super(traceModel);
                setDefaultRenderer(getColumnClass(0), new RowRenderer());
                setShowHorizontalLines(false);
                setShowVerticalLines(false);
                setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                setCellSelectionEnabled(true);
                setRowHeight(30);
                setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
                final Set<AWTKeyStroke> forwardKeys = getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
                final Set<AWTKeyStroke> newForwardKeys = new HashSet<AWTKeyStroke>(forwardKeys);
                newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
                setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
                getTableHeader().setToolTipText(Messages.HINT_SHOW_HIDE_COLUMNS);
                addMouseListener(
                        new MouseAdapter() {
                            @Override
                            public void mouseClicked(final MouseEvent e) {
                                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                                    final int row = TraceTable.this.rowAtPoint(e.getPoint());
                                    if (row < 0 || row >= TraceTable.this.getRowCount()) {
                                        return;
                                    }
                                    traceViewer.show(traceModel.getTraceItemAt(row));
                                }
                            }
                        });
                final ActionListener actionListener = new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent actionEvent) {
                        final TraceItem item = traceModel.getTraceItemAt(TraceTable.this.getSelectedRow());
                        if (item != null) {
                            traceViewer.show(item);
                        }
                    }
                };
                final KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
                registerKeyboardAction(actionListener, enter, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
                setName("SelectOnFinding");
                addPropertyChangeListener(getName(),
                        new PropertyChangeListener() {
                            @Override
                            public void propertyChange(final PropertyChangeEvent e) {
                                final int row = ((Integer) e.getNewValue()).intValue();
                                TraceTable.this.setRowSelectionInterval(row, row);
                                TraceTable.this.setColumnSelectionInterval(0, 0);
                                final int max = TraceTable.this.getHeight();
                                final int extent = TraceScrollPane.this.getVerticalScrollBar().getModel().getExtent();
                                final int value = Math.round((float) row * (max - extent) / (TraceTable.this.getRowCount() - 1 > 0 ? TraceTable.this.getRowCount() - 1 : 1));
                                TraceScrollPane.this.getVerticalScrollBar().getModel().setRangeProperties(value, extent, 0, max, false);
                            }
                        });
                settingsChanged();
            }

            public void settingsChanged() {
                setRowHeight(TraceSettings.getRowHeightForCurrentFont());
                updateColumnWidths();
                repaint();
            }

            private void updateColumnWidths() {
                for (int i = 0; i < columnModel.getColumnCount(); i++) {
                    boolean resizable = TraceSettings.getIsMinimizeColumnWidth() ? i == 4 : true;
                    columnModel.getColumn(i).setResizable(resizable);
                    columnModel.getColumn(i).setMinWidth(30);
                    if (resizable) {
                        columnModel.getColumn(i).setMaxWidth(MAX_WIDTH_HINT_RESIZE);
                    } else {
                        columnModel.getColumn(i).setMaxWidth(MAX_WIDTH_HINT_MINIMIZE);
                    }
                }
            }

            @Override
            public Component prepareRenderer(final TableCellRenderer renderer, final int rowIndex, final int vColIndex) {
                final Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
                if (c instanceof JComponent) {
                    final JComponent jc = (JComponent) c;
                    jc.setToolTipText(null);
                    final Object value = getValueAt(rowIndex, vColIndex);
                    if (value instanceof EEventSeverity) {
                        final EEventSeverity severity = (EEventSeverity) value;
                        if (severity == EEventSeverity.DEBUG) {
                            jc.setToolTipText(Messages.TLP_DEBUG);
                        } else if (severity == EEventSeverity.EVENT) {
                            jc.setToolTipText(Messages.TLP_EVENT);
                        } else if (severity == EEventSeverity.WARNING) {
                            jc.setToolTipText(Messages.TLP_WARNING);
                        } else if (severity == EEventSeverity.ERROR) {
                            jc.setToolTipText(Messages.TLP_ERROR);
                        } else {
                            jc.setToolTipText(Messages.TLP_ALARM);
                        }
                    }
                }
                final TableColumn column = getColumnModel().getColumn(vColIndex);
                if (!column.getResizable()) {
                    int width = (int) (c.getPreferredSize().width * 1.1);
                    width = Math.max(column.getMaxWidth() == MAX_WIDTH_HINT_MINIMIZE ? 0 : column.getMaxWidth(), width);
                    column.setMaxWidth(width);
                    column.setMinWidth(width);
                }

                return c;
            }
        }
        private final TraceTable traceTable;

        public TraceScrollPane(final TraceModel traceModel) {
            super();
            traceTable = new TraceTable(traceModel);
            setViewportView(traceTable);
        }

        public TraceTable getTraceTable() {
            return traceTable;
        }
    }

    public void clearTrace() {
        traceModel.clear();
    }

    private final class TraceToolBar extends JToolBar {

        private static final long serialVersionUID = -7589098362994516155L;
        private final JButton traceProfileButton;

        public TraceToolBar() {
            super();
            setAlignmentX(JToolBar.LEFT_ALIGNMENT);
            setFloatable(false);

            final JButton clearButton = new JButton(TraceSettings.loadIcon(this.getClass(), "img/clear.png"));
            clearButton.setToolTipText(Messages.HINT_CLEAR);
            clearButton.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(final ActionEvent e) {
                            traceModel.clear();
                        }
                    });
            clearButton.setFocusable(false);
            add(clearButton);

            final JButton settingsButton = new JButton(TraceSettings.loadIcon(this.getClass(), "img/colors.png"));
            settingsButton.setToolTipText(Messages.HINT_SETTINGS);
            settingsButton.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            settingsDialog.show();
                        }
                    });
            settingsButton.setFocusable(false);
            add(settingsButton);

            final JButton searchButton = new JButton(TraceSettings.loadIcon(this.getClass(), "img/find.png"));
            searchButton.setToolTipText(Messages.BTN_FIND);
            searchButton.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            searchDialog.show();
                        }
                    });
            searchButton.setFocusable(false);
            add(searchButton);

            final JButton findNextButton = new JButton(TraceSettings.loadIcon(this.getClass(), "img/find_next.png"));
            findNextButton.setToolTipText(Messages.BTN_FIND_NEXT);
            findNextButton.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(final ActionEvent e) {
                            searchDialog.findNext();
                        }
                    });
            findNextButton.setFocusable(false);
            add(findNextButton);

            traceProfileButton = new JButton(TraceSettings.loadIcon(this.getClass(), "img/trace_profile.png"));
            traceProfileButton.setToolTipText(Messages.BTN_TRACE_PROFILE);
            traceProfileButton.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(final ActionEvent ev) {
                            final String curProfile = traceModel.getTraceProfile();
                            final String newProfile = editProfile(curProfile, TraceView.this);
                            if (newProfile != null && !curProfile.equals(newProfile)) {
                                traceModel.setTraceProfile(newProfile);
                            }
                        }
                    });
            traceProfileButton.setFocusable(false);
            add(traceProfileButton);

            addSeparator(new Dimension(11, 30));
            JLabel label = new JLabel(Messages.LBL_FILTER);
            add(label);
            addSeparator(new Dimension(3, 30));

            final EEventSeverity[] options = {EEventSeverity.DEBUG, EEventSeverity.EVENT,
                EEventSeverity.WARNING, EEventSeverity.ERROR, EEventSeverity.ALARM
            };
            final String[] optionTitles = {Messages.FLT_DEBUG, Messages.FLT_EVENT, Messages.FLT_WARNING,
                Messages.FLT_ERROR, Messages.FLT_ALARM
            };
            final JComboBox comboBox = new JComboBox(optionTitles);
            label.setLabelFor(comboBox);
            comboBox.setSelectedIndex(0);
            comboBox.addItemListener(
                    new ItemListener() {
                        @Override
                        public void itemStateChanged(final ItemEvent e) {
                            if (options[comboBox.getSelectedIndex()] == traceModel.getSeverityFilter()) {
                                return;
                            }
                            traceModel.setSeverityFilter(options[comboBox.getSelectedIndex()]);
                        }
                    });
            add(comboBox);
        }
    }
    private final TraceModel traceModel;
    private final TraceScrollPane traceScrollPane;
    private final TraceItemViewer traceViewer;
    private final SettingsDialog settingsDialog;
    private final SearchDialog searchDialog;
    private final TraceStatusBar traceStatusBar;
    private final List<TraceViewListener> listeners;

    public JToolBar getToolBar() {
        return toolBar;
    }

    public TraceView(final ServerTrace traceImpl) {
        super(new BorderLayout());
        listeners = new CopyOnWriteArrayList<TraceViewListener>();
        traceModel = new TraceModel(traceImpl);
        traceScrollPane = new TraceScrollPane(traceModel);
        traceModel.addTableModelListener(new TableChangedListener(traceScrollPane));
        traceStatusBar = new TraceStatusBar();
        toolBar = new TraceToolBar();
        add(toolBar, BorderLayout.NORTH);
        add(traceScrollPane, BorderLayout.CENTER);
        add(traceStatusBar, BorderLayout.SOUTH);
        registerListener(traceStatusBar);
        TraceSettings.addListener(this);
        traceModel.clear();

        traceViewer = new TraceItemViewer(this);
        settingsDialog = new SettingsDialog(this);
        searchDialog = new SearchDialog(this);

        traceImpl.registerView(traceModel);
        final String overridenGuiProfile = traceImpl.getOverridenGuiProfile();
        if (overridenGuiProfile != null) {
            traceModel.onTraceProfileOverriden();
        }
    }
    
    public void setStatus(final String status) {
        traceStatusBar.setStatus(status);
    }

    public final void registerListener(final TraceViewListener listener) {
        listeners.add(listener);
    }

    public final void unregisterListener(final TraceViewListener listener) {
        listeners.remove(listener);
    }

    private final void fireSeverityChanged(final EEventSeverity oldSeverity, final EEventSeverity newSeverity) {
        for (TraceViewListener listener : listeners) {
            listener.severityChanged(this, oldSeverity, newSeverity);
        }
    }

    public void settingsChanged() {
        traceScrollPane.getTraceTable().settingsChanged();
    }

    public JTable getTraceTable() {
        return traceScrollPane.getTraceTable();
    }

    public EEventSeverity getTraceMaxSeverity() {
        return traceModel.getTraceMaxSeverity();
    }

    public static final class Messages {

        static {
            final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.widgets.mess.messages");

            COL_SEVERITY = bundle.getString("COL_SEVERITY");
            COL_DATE = bundle.getString("COL_DATE");
            COL_TIME = bundle.getString("COL_TIME");
            COL_SOURCE = bundle.getString("COL_SOURCE");
            COL_MESSAGE = bundle.getString("COL_MESSAGE");
            COL_CONTEXT = bundle.getString("COL_CONTEXT");
            HINT_SHOW_HIDE_COLUMNS = bundle.getString("HINT_SHOW_HIDE_COLUMNS");
            TITLE_COLUMNS_VISIBILITY = bundle.getString("TITLE_COLUMNS_VISIBILITY");
            BTN_CLOSE = bundle.getString("BTN_CLOSE");
            LBL_SHOW_SEVERITY = bundle.getString("LBL_SHOW_SEVERITY");
            LBL_SHOW_DATE = bundle.getString("LBL_SHOW_DATE");
            LBL_SHOW_TIME = bundle.getString("LBL_SHOW_TIME");
            LBL_SHOW_SOURCE = bundle.getString("LBL_SHOW_SOURCE");
            LBL_SHOW_MESSAGE = bundle.getString("LBL_SHOW_MESSAGE");
            LBL_SHOW_CONTEXT = bundle.getString("LBL_SHOW_CONTEXT");
            TITLE_WARNING_MESSAGE = bundle.getString("TITLE_WARNING_MESSAGE");
            ERR_NO_VISIBLE_COLS = bundle.getString("ERR_NO_VISIBLE_COLS");
            LBL_FILTER = bundle.getString("LBL_FILTER");
            HINT_CLEAR = bundle.getString("HINT_CLEAR");
            HINT_SETTINGS = bundle.getString("HINT_SETTINGS");
            FLT_ERROR = bundle.getString("FLT_ERROR");
            FLT_ALARM = bundle.getString("FLT_ALARM");
            FLT_WARNING = bundle.getString("FLT_WARNING");
            FLT_EVENT = bundle.getString("FLT_EVENT");
            FLT_DEBUG = bundle.getString("FLT_DEBUG");
            BTN_FIND = bundle.getString("BTN_FIND");
            BTN_FIND_NEXT = bundle.getString("BTN_FIND_NEXT");
            TLP_ERROR = bundle.getString("TLP_ERROR");
            TLP_ALARM = bundle.getString("TLP_ALARM");
            TLP_WARNING = bundle.getString("TLP_WARNING");
            TLP_EVENT = bundle.getString("TLP_EVENT");
            TLP_DEBUG = bundle.getString("TLP_DEBUG");
            BTN_TRACE_PROFILE = bundle.getString("BTN_TRACE_PROFILE");
            BTN_TRACE_PROFILE_OVR = bundle.getString("BTN_TRACE_PROFILE_OVR");
            TITLE_TRACE_PROFILE = bundle.getString("TITLE_TRACE_PROFILE");
        }
        static final String COL_SEVERITY;
        static final String COL_DATE;
        static final String COL_TIME;
        static final String COL_SOURCE;
        static final String COL_MESSAGE;
        static final String COL_CONTEXT;
        static final String HINT_SHOW_HIDE_COLUMNS;
        static final String TITLE_COLUMNS_VISIBILITY;
        static final String BTN_CLOSE;
        static final String BTN_FIND;
        static final String BTN_FIND_NEXT;
        static final String LBL_SHOW_SEVERITY;
        static final String LBL_SHOW_CONTEXT;
        static final String LBL_SHOW_DATE;
        static final String LBL_SHOW_TIME;
        static final String LBL_SHOW_SOURCE;
        static final String LBL_SHOW_MESSAGE;
        static final String TITLE_WARNING_MESSAGE;
        static final String ERR_NO_VISIBLE_COLS;
        public static final String HINT_CLEAR;
        static final String HINT_SETTINGS;
        static final String LBL_FILTER;
        static final String FLT_ERROR;
        static final String FLT_ALARM;
        static final String FLT_WARNING;
        static final String FLT_EVENT;
        static final String FLT_DEBUG;
        static final String TLP_ERROR;
        static final String TLP_ALARM;
        static final String TLP_WARNING;
        static final String TLP_EVENT;
        static final String TLP_DEBUG;
        public static final String BTN_TRACE_PROFILE;
        static final String BTN_TRACE_PROFILE_OVR;
        static final String TITLE_TRACE_PROFILE;
    }

    public void close() {
        TraceSettings.removeListener(this);
    }

    public static String editProfile(final String curProfile, final JComponent parentDialog) {
        String newProfile = curProfile;
        Object result;
        while ((result = JOptionPane.showInputDialog(parentDialog, null, TraceView.Messages.TITLE_TRACE_PROFILE, JOptionPane.PLAIN_MESSAGE, null, null, newProfile)) != null) {
            newProfile = (String) result;
            if (!curProfile.equals(newProfile)) {
                try {
                    //let's check and normalize
                    return new TraceProfile(newProfile).toString();
                } catch (RuntimeException e) {
                    JOptionPane.showMessageDialog(parentDialog, e.getMessage(), TraceView.Messages.TITLE_WARNING_MESSAGE, JOptionPane.ERROR_MESSAGE);
                }
            } else {
                return null;
            }
        }
        return null;
    }
}
