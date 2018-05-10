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
package org.radixware.kernel.utils.traceview.window;

import org.radixware.kernel.utils.traceview.TraceEvent;
import org.radixware.kernel.utils.traceview.TraceViewSettings;
import org.radixware.kernel.utils.traceview.utils.TraceViewUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import javax.swing.AbstractCellEditor;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.radixware.kernel.common.dialogs.datetimepicker.DateAndTimePicker;
import org.radixware.kernel.utils.traceview.TraceEvent.IndexedDate;
import org.radixware.kernel.utils.traceview.TraceViewSettings.EEventColumn;
import org.radixware.kernel.utils.traceview.TraceViewSettings.EIcon;
import org.radixware.kernel.utils.traceview.TraceViewSettings.ESeverity;
import org.radixware.kernel.utils.traceview.utils.IEventParserListener;

public class TablePanel extends JPanel implements IEventParserListener {

    private List<TraceEvent> data = null;
    private final File file;
    private JTable fictiveTableForObtainingTableDimensions;
    private final JProgressBar progressBar;
    private final JPanel loadingPanel;
    private final JLabel eventCountLabel;
    private TabSettings settings;
    private final JButton applyButton = new JButton("Apply");
    private JPanel tablePanel;
    private List<String> source;
    private JTable table;
    private JScrollPane scrollPane;
    private JScrollPane tableScrollPane;
    private ArrayList<String> arrayListName;
    private WindowMode window;
    //private ArrayList<IndexedDate> listOfLabels = new ArrayList<>();
    private int currentIndxLabel;

    private void keyEnterSettings(final JTable table) {
        InputMap im = table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        InputMap imParent = im.getParent();
        imParent.remove(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        im.setParent(imParent);
        im.remove(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        table.setInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, im);

        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    MessageDialog message = new MessageDialog((String) table.getValueAt(table.getSelectedRow(), 4), table, table.getSelectedRow());
                }
            }
        });
    }

    private TableRowSorter createRowSorter(JTable table) {
        TableRowSorter sorter = new TableRowSorter(table.getModel());

        sorter.setSortable(0, false);
        sorter.setSortable(1, true);
        sorter.setSortable(2, false);
        sorter.setSortable(3, false);
        sorter.setSortable(4, false);

        sorter.setComparator(1, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                return Integer.compare(((TraceEvent.IndexedDate) o1).getIndex(), ((TraceEvent.IndexedDate) o2).getIndex());
            }
        });

        table.setRowSorter(sorter);
        return sorter;
    }

    private static List<RowFilter<Object, Object>> createFilters() {
        List<RowFilter<Object, Object>> filters = new ArrayList<>(5);
        filters.add(RowFilter.regexFilter(""));
        filters.add(RowFilter.regexFilter(""));
        filters.add(RowFilter.regexFilter(""));
        filters.add(RowFilter.regexFilter(""));
        filters.add(RowFilter.regexFilter(""));
        filters.add(RowFilter.regexFilter(""));
        return filters;
    }

    private void settingsColumn(int index, JTable table) {
        TableColumn column = table.getColumnModel().getColumn(index);
        column.setMinWidth(0);
        column.setMaxWidth(100000);
        if (index == EEventColumn.SEVERITY.getIndex()) {
            column.setPreferredWidth(80);
        } else if (index == EEventColumn.DATE.getIndex()) {
            column.setPreferredWidth(185);
        } else {
            column.setPreferredWidth((index == EEventColumn.MESSAGE.getIndex()) ? 1000 : TraceViewUtils.getPreferredWidthColumn(index, fictiveTableForObtainingTableDimensions));
        }
    }

    private Date getMaxDate() {
        Date dateMax = data.get(0).getIndexedDate().getDate();
        for (int i = 1; i < data.size(); i++) {
            if (data.get(i).getIndexedDate().getDate().after(dateMax)) {
                dateMax = data.get(i).getIndexedDate().getDate();
            }
        }
        return dateMax;
    }

    public File getFile() {
        return file;
    }

    private Date getMinDate() {
        Date dateMin = data.get(0).getIndexedDate().getDate();
        for (int i = 1; i < data.size(); i++) {
            if (data.get(i).getIndexedDate().getDate().before(dateMin)) {
                dateMin = data.get(i).getIndexedDate().getDate();
            }
        }
        return dateMin;
    }

    private JPanel getFilterPanelTop(final TableRowSorter sorter, final List<RowFilter<Object, Object>> filters) {
        JPanel filterPanelTop = new JPanel();

        final DateAndTimePicker datePickerMin = new DateAndTimePicker(DateAndTimePicker.EOperatingMode.DATE_AND_TIME_MILLISECONDS, TraceViewSettings.DATE_TIME_PICKER_IMAGE_DISTRIBUTOR);
        final DateAndTimePicker datePickerMax = new DateAndTimePicker(DateAndTimePicker.EOperatingMode.DATE_AND_TIME_MILLISECONDS, TraceViewSettings.DATE_TIME_PICKER_IMAGE_DISTRIBUTOR);

        final Date dateMin = getMinDate();
        final Date dateMax = getMaxDate();

        datePickerMin.setDate(settings.getFirstTime() == -1 ? dateMin : new Date(settings.getFirstTime()));
        datePickerMax.setDate(settings.getLastTime() == -1 ? dateMax : new Date(settings.getLastTime()));

        final JTextField findField = new JTextField(settings.getFindText(), 25);
        JButton resetButton = new JButton("Reset");

        JButton resetAllButton = new JButton("Reset All");

        resetAllButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                resetAll();
            }
        });

        ActionListener findInMessageListener = new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyActionPerformed(datePickerMin.getDateAndTime(), datePickerMax.getDateAndTime(), findField, sorter, filters);
            }
        };

        resetButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                datePickerMin.setDate(dateMin);
                datePickerMax.setDate(dateMax);

                findField.setText("");

                applyActionPerformed(datePickerMin.getDateAndTime(), datePickerMax.getDateAndTime(), findField, sorter, filters);
            }
        });

        applyButton.addActionListener(findInMessageListener);
        findField.addActionListener(findInMessageListener);

        JButton pasteMinDate = new JButton(EIcon.PASTE_FROM_CLIPBOARD.getIcon());
        JButton pasteMaxDate = new JButton(EIcon.PASTE_FROM_CLIPBOARD.getIcon());

        pasteMinDate.setPreferredSize(new Dimension(20, 20));
        pasteMinDate.setMaximumSize(new Dimension(20, 20));
        pasteMinDate.setBorderPainted(false);
        pasteMinDate.setContentAreaFilled(false);

        pasteMaxDate.setPreferredSize(new Dimension(20, 20));
        pasteMaxDate.setMaximumSize(new Dimension(20, 20));
        pasteMaxDate.setBorderPainted(false);
        pasteMaxDate.setContentAreaFilled(false);

        pasteMinDate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    datePickerMin.setDate(TraceViewSettings.DATE_FORMAT_PLAIN_TEXT.get().parse(Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString()));
                } catch (UnsupportedFlavorException | IOException | ParseException ex) {
                }
            }
        });

        pasteMaxDate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    datePickerMax.setDate(TraceViewSettings.DATE_FORMAT_PLAIN_TEXT.get().parse(Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString()));
                } catch (UnsupportedFlavorException | IOException | ParseException ex) {
                }
            }
        });

        filterPanelTop.add(new JLabel("From: "));
        filterPanelTop.add(datePickerMin);
        filterPanelTop.add(pasteMinDate);
        filterPanelTop.add(new JLabel(" To: "));
        filterPanelTop.add(datePickerMax);
        filterPanelTop.add(pasteMaxDate);
        filterPanelTop.add(new JLabel(" Find"));
        filterPanelTop.add(findField);
        filterPanelTop.add(applyButton);
        filterPanelTop.add(resetButton);
        filterPanelTop.add(resetAllButton);

        return filterPanelTop;
    }

    private void applyActionPerformed(final Date firstDate, final Date lastDate, JTextField findField, TableRowSorter sorter, List<RowFilter<Object, Object>> filters) {
        
        if (firstDate != null) {
            filters.set(1, new RowFilter() {

                @Override
                public boolean include(RowFilter.Entry entry) {
                    return ((TraceEvent.IndexedDate) entry.getValue(1)).getDate().after(new Date(firstDate.getTime() - 1));
                }

            });
            settings.setFirstTime(firstDate.getTime());
        }

        if (lastDate != null) {
            filters.set(5, new RowFilter() {

                @Override
                public boolean include(RowFilter.Entry entry) {
                    return ((TraceEvent.IndexedDate) entry.getValue(1)).getDate().before(new Date(lastDate.getTime() + 1));
                }
            });
            settings.setLastTime(lastDate.getTime());
        }

        if ("".equals(findField.getText())) {
            filters.set(4, (RowFilter.regexFilter("", EEventColumn.MESSAGE.getIndex())));
        } else {
            StringBuilder sbuilder = new StringBuilder();
            for (int i = 0; i < findField.getText().length(); i++) {
                if (findField.getText().charAt(i) == '*') {
                    sbuilder.append(".*");
                } else if (findField.getText().charAt(i) == '?') {
                    sbuilder.append(".");
                } else if (findField.getText().charAt(i) == '\\') {
                    if (i != findField.getText().length() - 1) {
                        switch (findField.getText().charAt(i + 1)) {
                            case '*':
                                sbuilder.append("\\*");
                                i++;
                                break;
                            case '?':
                                sbuilder.append("\\?");
                                i++;
                                break;
                            default:
                                sbuilder.append("\\\\");
                                break;
                        }
                    } else {
                        sbuilder.append("\\\\");
                    }

                } else if (((Character) findField.getText().charAt(i)).toString().matches("\\W")) {
                    sbuilder.append("\\").append(findField.getText().charAt(i));
                } else {
                    sbuilder.append(findField.getText().charAt(i));
                }
            }
            try {
                RowFilter<Object, Object> filter = RowFilter.regexFilter("(?msi)" + sbuilder.toString(), EEventColumn.MESSAGE.getIndex());
                if (findField.getText().startsWith("[") && findField.getText().endsWith("]")) {
                    List<RowFilter<Object, Object>> columnAndContextFilter = new ArrayList<>(6);
                    columnAndContextFilter.add(filter);
                    columnAndContextFilter.add(RowFilter.regexFilter("(?msi)" + sbuilder.toString(), EEventColumn.CONTEXT.getIndex()));
                    filters.set(4, RowFilter.orFilter(columnAndContextFilter));
                } else {
                    filters.set(4, filter);
                }
            } catch (PatternSyntaxException e) {
                JOptionPane.showMessageDialog(null, "Wrong find expression: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }

        settings.setFindText(findField.getText());
        sorter.setRowFilter(RowFilter.andFilter(filters));
    }

    private static Color getForegroundColorBySeverity(ESeverity sev) {
        switch (sev) {
            case DEBUG:
                return TraceViewSettings.COLOR_DEBUG;
            case EVENT:
                return TraceViewSettings.COLOR_EVENT;
            case WARNING:
                return TraceViewSettings.COLOR_WARNING;
            default:
                return TraceViewSettings.COLOR_DEFAULT;
        }
    }

    private JTable createTable() {
        JTable table = new JTable(new DataTraceModel(data)) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                IndexedDate id = (TraceEvent.IndexedDate) getValueAt(row, 1);
                if (isRowSelected(row)) {
                    component.setBackground(TraceViewSettings.COLOR_BACKGROUND_SELECTED_ROW);
                    component.setForeground(TraceViewSettings.COLOR_FOREGROUND_SELECTED_ROW);
                } else if (settings.listOfLabels.contains(id)) {
                    component.setBackground(Color.YELLOW);
                    component.setForeground(Color.BLACK);
                } else {
                    component.setBackground(row % 2 == 0 ? TraceViewSettings.COLOR_EVEN_LINE : TraceViewSettings.COLOR_NOT_EVEN_LINE);
                    component.setForeground(getForegroundColorBySeverity(data.get(convertRowIndexToModel(row)).getSeverity()));
                }
                return component;
            }
        };

        table.setGridColor(Color.WHITE);
        keyEnterSettings(table);
        table.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
        table.setRowHeight((int) (table.getRowHeight() * 1.1));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(true);

        for (EEventColumn column : EEventColumn.values()) {
            settingsColumn(column.getIndex(), table);
        }
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int r = (table.getRowCount() > 100)? 100 : table.getRowCount();
        for (int column = 0; column < 2; column++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < r; row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
                Component c = table.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                //  We've exceeded the maximum width, no need to check other rows
                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }
            tableColumn.setPreferredWidth(preferredWidth);
            tableColumn.setMinWidth(preferredWidth);
        }
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.getColumnModel().getColumn(EEventColumn.SEVERITY.getIndex()).setCellRenderer(new MySeverityRenderer());
        table.getColumnModel().getColumn(EEventColumn.DATE.getIndex()).setCellRenderer(new MyDateRenderer());
        table.getColumnModel().getColumn(EEventColumn.SOURCE.getIndex()).setCellEditor(new SourceCellEditor());
        table.getColumnModel().getColumn(EEventColumn.CONTEXT.getIndex()).setCellEditor(new SourceCellEditor());
        table.getColumnModel().getColumn(EEventColumn.MESSAGE.getIndex()).setCellEditor(new MessageCellEditor());

        return table;
    }

    public TablePanel(File file, TabSettings settings, WindowMode window) {
        this.file = file;
        this.settings = settings;
        this.window = window;

        progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setValue(0);
        progressBar.setString("0%");
        progressBar.setStringPainted(true);

        eventCountLabel = new JLabel("Event processed: 0");
        eventCountLabel.setHorizontalAlignment(JLabel.CENTER);

        loadingPanel = new JPanel();
        loadingPanel.setLayout(new GridLayout(2, 1));
        loadingPanel.add(progressBar);
        loadingPanel.add(eventCountLabel);

        setLayout(new GridBagLayout());
        add(loadingPanel);
    }

    private void resetAll() {
        remove(tablePanel);
        settings = new TabSettings();
        tablePanel = createTablePanel();
        add(tablePanel);
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel();
        table = createTable();
        TableRowSorter sorter = createRowSorter(table);
        List<RowFilter<Object, Object>> filters = createFilters();
        JPanel filterPanelTop = getFilterPanelTop(sorter, filters);
        FilterPanelRight filterPanelRight = new FilterPanelRight(source, sorter, filters, settings, arrayListName);

        scrollPane = new JScrollPane(filterPanelRight);
        scrollPane.getVerticalScrollBar().setUnitIncrement(100);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        table.getTableHeader().setComponentPopupMenu(new JPopupMenuHeader(table, fictiveTableForObtainingTableDimensions, settings, window));
        table.setComponentPopupMenu(new JPopupMenuTable(table, filterPanelRight));
        tableScrollPane = new JScrollPane(table);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableScrollPane, scrollPane);
        splitPane.setResizeWeight(1.0);
        setPreferredSize(new Dimension(1600, 800));

        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(filterPanelTop, BorderLayout.NORTH);
        tablePanel.add(splitPane, BorderLayout.CENTER);
        return tablePanel;
    }

    public void addLabel() {
        int selected = table.getSelectedRow();
        if (!settings.listOfLabels.contains((TraceEvent.IndexedDate) table.getValueAt(selected, 1))) {
            settings.listOfLabels.add((TraceEvent.IndexedDate) table.getValueAt(selected, 1));
            settings.addNewBookmark(((TraceEvent.IndexedDate) table.getValueAt(selected, 1)).toString());
        } else {
            settings.listOfLabels.remove((TraceEvent.IndexedDate) table.getValueAt(selected, 1));
            settings.removeBookmark(((TraceEvent.IndexedDate) table.getValueAt(selected, 1)).toString());
        }
        currentIndxLabel = settings.listOfLabels.size() - 1;
    }

    public void moveToNextLabel() {
        IndexedDate currentIndexedDate;
        if (!settings.listOfLabels.isEmpty()) {
            if (currentIndxLabel < settings.listOfLabels.size() - 1) {
                currentIndxLabel++;
                currentIndexedDate = settings.listOfLabels.get(currentIndxLabel);
            } else {
                currentIndexedDate = settings.listOfLabels.get(settings.listOfLabels.size() - 1);
                currentIndxLabel = settings.listOfLabels.size() - 1;
            }
            moveToBookmark(currentIndexedDate);
        }
    }

    public void moveToPreviousLabel() {
        IndexedDate currentIndexedDate;
        if (!settings.listOfLabels.isEmpty()) {
            if (currentIndxLabel > 0) {
                currentIndxLabel--;
                currentIndexedDate = settings.listOfLabels.get(currentIndxLabel);
            } else {
                currentIndexedDate = settings.listOfLabels.get(0);
                currentIndxLabel = 0;
            }
            moveToBookmark(currentIndexedDate);
        }
    }

    public void moveToBookmark(IndexedDate indexedDate) {
        for (int i = 0; i < table.getRowCount(); i++) {
            if (((TraceEvent.IndexedDate) table.getValueAt(i, 1)) == indexedDate) {
                Rectangle rect = table.getCellRect(i, 1, true);
                table.scrollRectToVisible(rect);
                table.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    public void setData(final HashMap<String, Object> dataMap) {
        data = (List<TraceEvent>) dataMap.get(TraceViewSettings.DATA);
        fictiveTableForObtainingTableDimensions = new JTable(new DataTraceModel((TraceEvent) dataMap.get(TraceViewSettings.FICTIVE_ELEMENT)));
        source = (List<String>) dataMap.get(TraceViewSettings.UNIQUE_SOURCES);
        arrayListName = (ArrayList<String>) dataMap.get(TraceViewSettings.CONTEXT_ARRAY);

        tablePanel = createTablePanel();

        remove(loadingPanel);
        setLayout(new BorderLayout());
        add(tablePanel);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                applyButton.doClick();
                tableScrollPane.getVerticalScrollBar().setValue(settings.getTableScrollBarValue());
                scrollPane.getVerticalScrollBar().setValue(settings.getFilterPanelScrollBarValue());

                tableScrollPane.getViewport().addChangeListener(new ChangeListener() {

                    @Override
                    public void stateChanged(ChangeEvent e) {
                        settings.setTableScrollBarValue(tableScrollPane.getVerticalScrollBar().getValue());
                    }
                });

                scrollPane.getViewport().addChangeListener(new ChangeListener() {

                    @Override
                    public void stateChanged(ChangeEvent e) {
                        settings.setFilterPanelScrollBarValue(scrollPane.getVerticalScrollBar().getValue());
                    }
                });

                TablePanel.this.invalidate();
                TablePanel.this.revalidate();
            }
        });
    }

    @Override
    public void setProgress(final int progressInPercentage, final int eventProcessed) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                progressBar.setValue(progressInPercentage);
                progressBar.setString(progressInPercentage + "%");
                eventCountLabel.setText("Event processed: " + eventProcessed);
            }
        });
    }

    public TabSettings getTabSettings() {
        return settings;
    }

    public JTable getTable() {
        return table;
    }

    private class MyDateRenderer extends DefaultTableCellRenderer {

        @Override
        public void setValue(Object value) {
            setText((value == null) ? "" : TraceViewSettings.DATE_FORMAT_GUI.get().format(((TraceEvent.IndexedDate) value).getDate()));
        }
    }

    private class MySeverityRenderer extends DefaultTableCellRenderer {

        @Override
        public void setValue(Object value) {
            setText(ESeverity.getSeverity(value.toString()).toString());
        }
    }
    
    private class SourceCellEditor extends AbstractCellEditor implements TableCellEditor  {
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
            return null;
        }
        @Override
        public Object getCellEditorValue() {
            return null;
        }
        @Override
        public boolean isCellEditable(EventObject e) {
            return false;
        }
    }

    private class MessageCellEditor extends AbstractCellEditor implements TableCellEditor {

        JTextField component = new JTextField();

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
            component.setText((String) value);
            MessageDialog message = new MessageDialog((String) value, table, rowIndex);
            return null;
        }

        @Override
        public Object getCellEditorValue() {
            return component.getText();
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            if (e instanceof MouseEvent) {
                return ((MouseEvent) e).getClickCount() >= 2;
            }
            return true;
        }
    }
}
