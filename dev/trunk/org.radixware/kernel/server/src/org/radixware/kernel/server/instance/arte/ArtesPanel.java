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
package org.radixware.kernel.server.instance.arte;

import java.awt.BorderLayout;
import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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

import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.utils.DebugUtils;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.instance.InstanceConfigStore;
import org.radixware.kernel.server.instance.InstanceState;
import org.radixware.kernel.server.instance.InstanceView;
import org.radixware.kernel.server.monitoring.ArteWaitStats;
import org.radixware.kernel.server.units.ServerItemView;
import org.radixware.kernel.server.widgets.TraceSettings;
import org.radixware.kernel.server.widgets.TableWithSeverityColumnRenderer;
import org.radixware.kernel.server.widgets.TraceView;

public class ArtesPanel extends JPanel {

    private static final long serialVersionUID = 4961007823381517945L;
    private static final long TIC_MILLIS = 1000;

    private final class TableChangedListener implements TableModelListener {

        private int selectedRow = -1;

        @Override
        public void tableChanged(final TableModelEvent e) {
            selectedRow = arteInstancesScrollPane.getArteInstancesTable().getSelectedRow();
            arteInstancesScrollPane.getArteInstancesTable().tableChanged(e);
            if (selectedRow != -1 && selectedRow < arteInstancesScrollPane.getArteInstancesTable().getRowCount()) {
                arteInstancesScrollPane.getArteInstancesTable().setRowSelectionInterval(selectedRow, selectedRow);
            }
        }
    }

    private static final class ArteInstancesModel implements TableModel {

        private static final int COLUMN_COUNT = 4;
        private final String[] HEADER = {Messages.COL_SEVERITY, Messages.COL_SEQ_NUM, Messages.COL_STATE, Messages.COL_USAGE};
        private final List<TableModelListener> listeners;
        private final Instance instance;
        private List<ArteInstance> snapshot = new ArrayList<>();

        public ArteInstancesModel(final Instance instance) {
            this.instance = instance;
            listeners = new ArrayList<TableModelListener>();
        }

        public void update() {
            final InstanceView instanceView = instance.getViewIfCreated();
            if (instanceView != null && !instanceView.isDisposing()) {
                SwingUtilities.invokeLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                final ArtePool artePool = instance.getArtePool();
                                if (artePool != null) {
                                    snapshot = new ArrayList<>(instance.getArtePool().getInstances(true));
                                } else {
                                    snapshot = Collections.emptyList();
                                }
                                for (TableModelListener listener : listeners) {
                                    if (listener instanceof TableChangedListener) {
                                        listener.tableChanged(new TableModelEvent(ArteInstancesModel.this));
                                    }
                                }
                            }
                        });
            }
        }

        public ArteInstance getArteHandlerAt(final int row) {
            try {
                return snapshot.get(row);
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        @Override
        public int getRowCount() {
            return snapshot.size();
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
            if (column < 0 || column >= getColumnCount()) {
                return null;
            }
            final ArteInstance arteInstance = getArteHandlerAt(row);
            if (arteInstance == null) {
                return null;
            }
            switch (column) {
                case 0:
                    final ServerItemView v = arteInstance.getView();
                    if (v == null) {
                        return null;
                    } else {
                        return v.getTraceMaxSeverity();
                    }
                case 1:
                    return String.valueOf(arteInstance.getSeqNumber());
                case 2: {
                    try {
                        switch (arteInstance.getState()) {
                            case BUSY:
                                final IArteRequest request = arteInstance.getRequest();
                                if (request != null) {
                                    return MessageFormat.format(Messages.MES_BUSY_BY, request.getUnit().getTitle());
                                }
                                return Messages.MES_BUSY;
                            case FREE:
                                return Messages.MES_FREE;
                            case INIT:
                                return Messages.MES_INIT;
                            default:
                                return "?";
                        }
                    } catch (Exception t) {
                        return t.getMessage();
                    }
                }
                case 3: {
//                    final String workTimePercent = Byte.toString(arteInstance.getStatistic().getWorkTimePercent());
//                    return workTimePercent + "%";
                    return arteInstance;
                }
                default:
                    return null;
            }
        }

        @Override
        public String getColumnName(final int column) {
            return HEADER[column];
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
    }

    private final class ArtePopupMenu extends JPopupMenu implements ActionListener, PopupMenuListener {

        private static final long serialVersionUID = 8602385325374899199L;
        private static final String HIDE_COMMAND = "hide";
        private static final String SHOW_COMMAND = "show";
        private static final String STOP_COMMAND = "stop";
        private final JMenuItem hide;
        private final JMenuItem show;
        private final JMenuItem stop;

        public ArtePopupMenu() {
            super();

            hide = new JMenuItem(Messages.MENU_HIDE);
            hide.setActionCommand(HIDE_COMMAND);
            hide.addActionListener(this);
            add(hide);

            show = new JMenuItem(Messages.MENU_SHOW);
            show.setActionCommand(SHOW_COMMAND);
            show.addActionListener(this);
            add(show);

            stop = new JMenuItem(Messages.MENU_STOP);
            stop.setActionCommand(STOP_COMMAND);
            stop.addActionListener(this);
            add(stop);

            addPopupMenuListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final ArteInstance arteInstance = arteInstancesModel.getArteHandlerAt(arteInstancesScrollPane.getArteInstancesTable().getSelectedRow());
            if (arteInstance == null) {
                return;
            }
            final String command = e.getActionCommand();
            if (command.equals(HIDE_COMMAND)) {
                final ServerItemView v = arteInstance.getView();
                if (v != null) {
                    v.setVisible(false);
                }
            } else if (command.equals(SHOW_COMMAND)) {
                final ServerItemView v = arteInstance.getView();
                if (v != null) {
                    v.setVisible(true);
                }
            } else if (command.equals(STOP_COMMAND)) {
                try {
                    if (!arteInstance.stop(5000)) {
                        JOptionPane.showMessageDialog(ArtesPanel.this, ArteMessages.ARTE_IS_NOT_STOPPED, ArteMessages.TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Throwable t) {
                    LogFactory.getLog(ArtesPanel.class).error("Error while stopping " + arteInstance.getTitle(), t);
                }
            }
        }

        @Override
        public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
            final ArteInstance arteInstance = arteInstancesModel.getArteHandlerAt(arteInstancesScrollPane.getArteInstancesTable().getSelectedRow());
            try {
                if (arteInstance.isStopped()) {
                    stop.setEnabled(false);
                } else {
                    stop.setEnabled(true);
                }
            } catch (Throwable t) {
                DebugUtils.suppressException(t);
                stop.setEnabled(true);
            }
            final ServerItemView v = arteInstance.getView();
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
        public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
            //do nothing
        }

        @Override
        public void popupMenuCanceled(final PopupMenuEvent e) {
            //do nothing
        }
    }

    private final class ArteInstancesScrollPane extends JScrollPane {

        private static final long serialVersionUID = 3617720068279691360L;
        private final ArteInstancesTable arteInstancesTable;

        private final class ArteInstancesTable extends JTable {

            private static final long serialVersionUID = 1552549028233029619L;
            private static final int DEFAULT_ROW_HEIGHT = 26;
            private final ArtePopupMenu popup;
            private final ArteLoadCellRenderer loadCellRenderer = new ArteLoadCellRenderer();

            public ArteInstancesTable(final ArteInstancesModel model) {
                super(model);
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
                getColumnModel().getColumn(3).setCellRenderer(loadCellRenderer);
                getColumnModel().getColumn(1).setPreferredWidth(100);
                setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
                getColumnModel().getColumn(3).setPreferredWidth(300);
                final Set<AWTKeyStroke> forwardKeys = getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
                final Set<AWTKeyStroke> newForwardKeys = new HashSet<AWTKeyStroke>(forwardKeys);
                newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
                setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
                popup = new ArtePopupMenu();
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(final MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            final int row = ArteInstancesTable.this.rowAtPoint(e.getPoint());
                            final int column = ArteInstancesTable.this.columnAtPoint(e.getPoint());
                            if (row >= 0 && row < ArteInstancesTable.this.getRowCount()
                                    && column >= 0 && column < ArteInstancesTable.this.getColumnCount()) {
                                ArteInstancesTable.this.setRowSelectionInterval(row, row);
                                popup.show(e.getComponent(), e.getX(), e.getY());
                            }
                        } else if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                            final int row = ArteInstancesTable.this.rowAtPoint(e.getPoint());
                            final int column = ArteInstancesTable.this.columnAtPoint(e.getPoint());
                            if (row >= 0 && row < ArteInstancesTable.this.getRowCount()
                                    && column >= 0 && column < ArteInstancesTable.this.getColumnCount()) {
                                final ArteInstance handler = model.getArteHandlerAt(row);
                                if (handler != null) {
                                    final ServerItemView v = handler.getView();
                                    if (v != null) {
                                        v.setVisible(!handler.getView().isVisible());
                                    }
                                }
                            }
                        }
                    }
                });
                final ActionListener actionListener = new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        if (ArteInstancesTable.this.getSelectedRow() != -1) {
                            popup.show(ArteInstancesTable.this, 0, 0);
                        }
                    }
                };
                final KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
                registerKeyboardAction(actionListener, enter, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
                loadCellRenderer.setGrapMode(InstanceConfigStore.restoreArtesUsageVisual());
                final JPopupMenu headerPopup = new JPopupMenu();
                final JCheckBoxMenuItem cbGraphicalUsage = new JCheckBoxMenuItem(ArteMessages.USAGE_GRAPH);
                cbGraphicalUsage.setSelected(loadCellRenderer.grapMode);
                headerPopup.add(cbGraphicalUsage);
                cbGraphicalUsage.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        loadCellRenderer.setGrapMode(cbGraphicalUsage.isSelected());
                        InstanceConfigStore.storeArtesUsageVisual(cbGraphicalUsage.isSelected());
                    }
                });
                getTableHeader().addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent me) {
                        if (SwingUtilities.isRightMouseButton(me)) {
                            headerPopup.show(getTableHeader(), me.getX(), me.getY());
                        }
                    }
                });
            }

            private void updateSettings() {
                setRowHeight(Math.max(TraceSettings.getRowHeightForCurrentFont(), DEFAULT_ROW_HEIGHT));
            }

            @Override
            public Component prepareRenderer(final TableCellRenderer renderer, final int rowIndex, final int vColIndex) {
                final Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
                if (c instanceof JComponent) {
                    final JComponent jc = (JComponent) c;
                    final Object value = getValueAt(rowIndex, 0);
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
        }

        public ArteInstancesScrollPane(final ArteInstancesModel model) {
            super();
            arteInstancesTable = new ArteInstancesTable(model);
            setViewportView(arteInstancesTable);
        }

        public JTable getArteInstancesTable() {
            return arteInstancesTable;
        }
    }

    public void stop() {
        if (thread != null) {
            thread.interrupt();
            try {
                thread.join(5000);
            } catch (InterruptedException ex) {
                LogFactory.getLog(ArtesPanel.class).warn("Interrupted while waiting for ArtesPanel termination", ex);
            }
        }
        thread = null;
    }

    public void start() {
        if (thread == null || thread.getState() == Thread.State.TERMINATED) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    final InstanceView instanceView = instance.getViewIfCreated();
                    while (instanceView != null && !instanceView.isDisposing()) {
                        arteInstancesModel.update();
                        try {
                            Thread.sleep(TIC_MILLIS);
                        } catch (InterruptedException e) {
                            arteInstancesModel.update();
                            return;
                        }
                    }
                }
            }, "ARTE Instances GUI Updater Thread of " + instance.getFullTitle());
            thread.setPriority(Thread.NORM_PRIORITY - 1);
            thread.setDaemon(true);
            thread.start();
        }
    }
    private final ArteInstancesScrollPane arteInstancesScrollPane;
    private final ArteInstancesModel arteInstancesModel;
    private volatile Thread thread = null;
    private final Instance instance;

    public ArtesPanel(final Instance instance) {
        super(new BorderLayout());
        this.instance = instance;
        arteInstancesModel = new ArteInstancesModel(instance);
        arteInstancesScrollPane = new ArteInstancesScrollPane(arteInstancesModel);
        arteInstancesModel.addTableModelListener(new TableChangedListener());
        add(arteInstancesScrollPane, BorderLayout.CENTER);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(final ComponentEvent e) {
                start();
            }

            @Override
            public void componentHidden(final ComponentEvent e) {
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
                final ArtePool artePool = instance.getArtePool();
                if (artePool != null) {
                    for (ArteInstance arteInstance : artePool.getInstances(false)) {
                        final ServerItemView view = arteInstance.getView();
                        if (view != null) {
                            final TraceView traceView = view.getTraceList();
                            if (traceView != null) {
                                traceView.clearTrace();
                            }
                        }
                    }
                }
            }
        });
        toolbar.add(clearTraceButton);

        final JButton traceProfileButton = new JButton(TraceSettings.loadIcon(TraceSettings.class, "img/trace_profile.png"));
        traceProfileButton.setToolTipText(TraceView.Messages.BTN_TRACE_PROFILE);
        traceProfileButton.addActionListener(
                new ActionListener() {
                    private String lastOverriddenProfile = null;
                    private boolean isProfileOverriden = false;

                    @Override
                    public void actionPerformed(final ActionEvent ev) {
                        if (!isProfileOverriden) {
                            lastOverriddenProfile = null;
                            boolean differentProfiles = false;
                            if (instance.getArtePool() != null) {
                                for (ArteInstance arteInstance : instance.getArtePool().getInstances(false)) {
                                    if (lastOverriddenProfile == null) {
                                        lastOverriddenProfile = arteInstance.getTrace().getProfiles().getGuiTraceProfile();
                                    } else {
                                        differentProfiles = !lastOverriddenProfile.equals(arteInstance.getTrace().getProfiles().getGuiTraceProfile());
                                        break;
                                    }
                                }
                            }
                            if (lastOverriddenProfile == null) {
                                lastOverriddenProfile = Messages.MES_WAIT_FOR_ARTE;
                            } else if (differentProfiles) {
                                lastOverriddenProfile = Messages.MES_DIFFERENT_PROFILES;
                            }
                        }
                        final String newProfile = TraceView.editProfile(lastOverriddenProfile, ArtesPanel.this);
                        if (newProfile != null) {
                            isProfileOverriden = true;
                            lastOverriddenProfile = newProfile;
                            for (ArteInstance arteInstance : instance.getArtePool().getInstances(false)) {
                                arteInstance.getTrace().overrideGuiProfile(newProfile);
                            }
                        }
                    }
                });
        traceProfileButton.setFocusable(false);
        toolbar.add(traceProfileButton);
    }

    private static class ArteLoadCellRenderer implements TableCellRenderer {

        private boolean grapMode;
        private final Map<ArteInstance, RendererInfo> arteToChart = new WeakHashMap<>();
        private static final int COUNT = 60;

        public void setGrapMode(boolean grapMode) {
            this.grapMode = grapMode;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof ArteInstance) {
                final ArteInstance arteInst = (ArteInstance) value;
                final List<ArteWaitsHistoryItem> history = arteInst.getWaitsHistory();
                RendererInfo info = arteToChart.get(arteInst);
                if (info == null) {
                    DefaultCategoryDataset dataset = new MyDataset();
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < COUNT; j++) {
                            dataset.addValue((Number) (i == 3 ? 100 : 0), i, j);
                        }
                    }
                    JFreeChart chart = ChartFactory.createStackedBarChart(
                            null,
                            "time",
                            "percent",
                            dataset,
                            PlotOrientation.VERTICAL,
                            false,
                            true,
                            false);
                    info = new RendererInfo(chart);
                    ((CategoryPlot) chart.getPlot()).getRenderer().setSeriesPaint(0, new Color(54, 219, 39));
                    ((CategoryPlot) chart.getPlot()).getRenderer().setSeriesPaint(1, new Color(249, 246, 57));
                    ((CategoryPlot) chart.getPlot()).getRenderer().setSeriesPaint(2, new Color(150, 251, 255));
                    ((CategoryPlot) chart.getPlot()).getRenderer().setSeriesPaint(3, new Color(220, 224, 232));
                    ((CategoryPlot) chart.getPlot()).getRenderer().setSeriesPaint(4, new Color(255, 255, 255));
                    ((CategoryPlot) chart.getPlot()).getRenderer().setBaseToolTipGenerator(new CategoryToolTipGenerator() {

                        @Override
                        public String generateToolTip(CategoryDataset cd, int i, int i1) {
                            final Number val = cd.getValue(i, i1);
                            switch (i) {
                                case 0:
                                    return "<html><div style='margin:0 -3 0 -3; padding: 0 3 0 3; background:#050505;'><font color=#36db27>CPU: " + val + "</font></div></html>";
                                case 1:
                                    return "<html><div style='margin:0 -3 0 -3; padding: 0 3 0 3; background:#050505;'><font color=#f9f639>DB: " + val + "</font></div></html>";
                                case 2:
                                    return "<html><div style='margin:0 -3 0 -3; padding: 0 3 0 3; background:#050505;'><font color=#96fbff>EXT: " + val + "</font></div></html>";
                                case 3:
                                    return "<html><div style='margin:0 -3 0 -3; padding: 0 3 0 3; background:#050505;'><font color=#dce0e8>OTHER: " + val + "</font></div></html>";
                                case 4:
                                    return "<html><div style='margin:0 -3 0 -3; padding: 0 3 0 3; background:#050505;'><font color=#ffffff>IDLE: " + val + "</font></div></html>";
                                default:
                                    return "UNKNOWN";
                            }
                        }
                    });
                    ((CategoryPlot) chart.getPlot()).getDomainAxis().setVisible(false);
                    ((CategoryPlot) chart.getPlot()).getDomainAxis().setUpperMargin(0);
                    ((CategoryPlot) chart.getPlot()).getDomainAxis().setLowerMargin(0);
                    ((CategoryPlot) chart.getPlot()).getDomainAxis().setCategoryMargin(0);
                    ((CategoryPlot) chart.getPlot()).getRangeAxis().setVisible(false);
                    ((CategoryPlot) chart.getPlot()).getRangeAxis().setUpperMargin(0);
                    ((CategoryPlot) chart.getPlot()).getRangeAxis().setLowerMargin(0);
                    ((CategoryPlot) chart.getPlot()).setDomainGridlinesVisible(false);
                    ((CategoryPlot) chart.getPlot()).setRangeGridlinesVisible(false);
                    ((CategoryPlot) chart.getPlot()).setRangeCrosshairVisible(false);
                    ((CategoryPlot) chart.getPlot()).setInsets(new RectangleInsets(4, 0, 4, 0));
                    ((CategoryPlot) chart.getPlot()).setOutlineVisible(false);
                    arteToChart.put(arteInst, info);
                }

                if (grapMode) {

                    int existingIdx = history.size() - 1;
                    while (existingIdx >= 0) {
                        if (info.lastItem != null && info.lastItem.getTimestampMillis() >= history.get(existingIdx).getTimestampMillis()) {
                            break;
                        }
                        existingIdx--;
                    }
                    for (int i = existingIdx + 1; i < history.size(); i++) {
                        info.lastItem = history.get(i);
                        final MyDataset dataset = (MyDataset) info.chart.getCategoryPlot().getDataset();
                        dataset.updatedEnabled = false;
                        dataset.addValue((Number) (100. * info.lastItem.getWaitStats().getCpuNanos() / info.lastItem.getWaitStats().totalNanos()), 0, info.lastItem.getTimestampMillis());
                        dataset.addValue((Number) (100. * info.lastItem.getWaitStats().getDbNanos() / info.lastItem.getWaitStats().totalNanos()), 1, info.lastItem.getTimestampMillis());
                        dataset.addValue((Number) (100. * info.lastItem.getWaitStats().getExtNanos() / info.lastItem.getWaitStats().totalNanos()), 2, info.lastItem.getTimestampMillis());
                        dataset.addValue((Number) (100. * info.lastItem.getWaitStats().getOtherNanos() / info.lastItem.getWaitStats().totalNanos()), 3, info.lastItem.getTimestampMillis());
                        dataset.addValue((Number) (100. * info.lastItem.getWaitStats().getIdleNanos() / info.lastItem.getWaitStats().totalNanos()), 4, info.lastItem.getTimestampMillis());
                        dataset.updatedEnabled = true;
                        dataset.removeColumn(0);
                    }
                    return info.chartPanel;
                } else {
                    ArteWaitStats stats = history.isEmpty() ? new ArteWaitStats(0, 0, 0, 0, 0) : history.get(history.size() - 1).getWaitStats();
                    final String text = String.format("cpu:%02d db:%02d ext:%02d other:%02d idle:%02d",
                            (int) Math.min(100. * stats.getCpuNanos() / stats.totalNanos(), 99),
                            (int) Math.min(100. * stats.getDbNanos() / stats.totalNanos(), 99),
                            (int) Math.min(100. * stats.getExtNanos() / stats.totalNanos(), 99),
                            (int) Math.min(100. * stats.getOtherNanos() / stats.totalNanos(), 99),
                            (int) Math.min(100. * stats.getIdleNanos() / stats.totalNanos(), 99)
                    );
                    return info.textRenderer.getTableCellRendererComponent(table, text, isSelected, hasFocus, row, column);
                }
            } else {
                return null;
            }
        }

        private static final class MyDataset extends DefaultCategoryDataset {

            private boolean updatedEnabled;

            @Override
            protected void fireDatasetChanged() {
                if (updatedEnabled) {
                    super.fireDatasetChanged();
                }
            }

        }

        private static final class RendererInfo {

            private final JFreeChart chart;
            private ArteWaitsHistoryItem lastItem;
            private final ChartPanel chartPanel;
            private final TableWithSeverityColumnRenderer textRenderer = new TableWithSeverityColumnRenderer();

            public RendererInfo(final JFreeChart chart) {
                this.chart = chart;
                this.chartPanel = new ChartPanel(chart);
                chartPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            }

        }

    }

    private static final class Messages {

        static {
            final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.instance.arte.mess.messages");

            COL_NAME = bundle.getString("COL_NAME");
            COL_SEQ_NUM = bundle.getString("COL_SEQ_NUM");
            COL_SEVERITY = bundle.getString("COL_SEVERITY");
            COL_USAGE = bundle.getString("COL_USAGE");
            COL_STATE = bundle.getString("COL_STATE");
            MES_BUSY = bundle.getString("MES_BUSY");
            MES_FREE = bundle.getString("MES_FREE");
            MES_INIT = bundle.getString("MES_INIT");
            MES_CAPTURED = bundle.getString("MES_CAPTURED");
            MENU_HIDE = bundle.getString("MENU_HIDE");
            MENU_SHOW = bundle.getString("MENU_SHOW");
            MENU_STOP = bundle.getString("MENU_STOP");
            TLP_ERROR = bundle.getString("TLP_ERROR");
            TLP_ALARM = bundle.getString("TLP_ALARM");
            TLP_WARNING = bundle.getString("TLP_WARNING");
            TLP_EVENT = bundle.getString("TLP_EVENT");
            TLP_DEBUG = bundle.getString("TLP_DEBUG");
            TLP_NONE = bundle.getString("TLP_NONE");
            MES_BUSY_BY = bundle.getString("MES_BUSY_BY");
            MES_WAIT_FOR_ARTE = bundle.getString("MES_WAIT_FOR_ARTE");
            MES_DIFFERENT_PROFILES = bundle.getString("MES_DIFFERENT_PROFILES");
        }
        static final String COL_NAME;
        static final String COL_SEQ_NUM;
        static final String COL_SEVERITY;
        static final String COL_USAGE;
        static final String COL_STATE;
        static final String MES_BUSY;
        static final String MES_BUSY_BY;
        static final String MES_FREE;
        static final String MES_INIT;
        static final String MENU_HIDE;
        static final String MENU_SHOW;
        static final String MENU_STOP;
        static final String TLP_ERROR;
        static final String TLP_ALARM;
        static final String TLP_WARNING;
        static final String TLP_EVENT;
        static final String TLP_DEBUG;
        static final String TLP_NONE;
        static final String MES_CAPTURED;
        static final String MES_WAIT_FOR_ARTE;
        static final String MES_DIFFERENT_PROFILES;
    }
}
