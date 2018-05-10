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
package org.radixware.kernel.designer.ads.editors.clazz.report;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportColumnDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer;
import org.radixware.kernel.common.types.Id;

public class AdsReportCellGeneralSettingPanel extends JPanel {

    private static final String UNDEFINED = "<undefined>";

    private final AdsReportCell cell;

    private final AdsReportWidgetNamePanel namePanel;

    public AdsReportCellGeneralSettingPanel(AdsReportCell cell) {
        this.cell = cell;
        this.namePanel = new AdsReportWidgetNamePanel(cell);

        initComponents();
    }

    private void initComponents() {
        this.setLayout(new MigLayout("ins 0", "[shrink][grow]", "[shrink][shrink][shrink][shrink]"));

        this.add(namePanel, "shrinky, growx, span 2, wrap");

        this.add(new JLabel("Associated column:"), "shrinkx, shrinky, gapx 10");
        this.add(createColumnsComboBox(), "shrinky, growx, gapx 0 10, wrap");

        this.add(new JLabel("Snap to cell:"), "shrinkx, shrinky, gapx 10");
        this.add(createCellsComboBox(), "shrinky, growx, gapx 0 10, wrap");

        this.add(createChangeTopOnMovingCheckBox(), "shrinky, growx, gapx 10, span 2");
    }

    public void updateNameEditor() {
        namePanel.open();
    }

    private JComboBox<ColumnsComboBoxItem> createColumnsComboBox() {
        final JComboBox<ColumnsComboBoxItem> result;

        AdsReportClassDef report = cell.getOwnerReport();

        if (report.getColumns().isEmpty()) {
            result = new JComboBox<>();
            result.setEnabled(false);
            return result;
        }

        ColumnsComboBoxItem initialValue = new ColumnsComboBoxItem(null);

        ColumnsComboBoxItem[] columns = new ColumnsComboBoxItem[report.getColumns().size() + 1];
        columns[0] = initialValue;
        for (int i = 1; i < columns.length; i++) {
            columns[i] = new ColumnsComboBoxItem(report.getColumns().get(i - 1));
            if (columns[i].columnId == cell.getAssociatedColumnId()) {
                initialValue = columns[i];
            }
        }
        Arrays.sort(columns);

        result = new JComboBox<>(columns);
        result.setSelectedItem(initialValue);
        result.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ColumnsComboBoxItem selectedValue = (ColumnsComboBoxItem) result.getSelectedItem();
                cell.setAssociatedColumnId(selectedValue.columnId);
            }
        });

        return result;
    }

    private JComboBox<CellsComboBoxItem> createCellsComboBox() {
        final JComboBox<CellsComboBoxItem> result;

        List<AdsReportCell> cellsList = new ArrayList<>();
        addCellsToList(cell.getOwnerBand(), cellsList);

        if (cellsList.isEmpty()) {
            result = new JComboBox<>();
            result.setEnabled(false);
            return result;
        }

        CellsComboBoxItem undefinedCell = new CellsComboBoxItem(null);
        CellsComboBoxItem initialValue = undefinedCell;
        CellsComboBoxItem[] cells = new CellsComboBoxItem[cellsList.size()];

        for (int i = 0; i < cells.length; i++) {
            cells[i] = new CellsComboBoxItem(cellsList.get(i));
            if (cells[i].cell.getId() == cell.getLeftCellId()) {
                initialValue = cells[i];
            }
        }
        Arrays.sort(cells);

        result = new JComboBox<>(cells);
        result.insertItemAt(undefinedCell, 0);
        result.setSelectedItem(initialValue);
        result.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IReportWidgetContainer container = cell.getOwnerWidget();
                
                AdsReportCell leftCell = (AdsReportCell) container.findWidgetById(cell.getLeftCellId());                                                
                CellsComboBoxItem selectedValue = (CellsComboBoxItem) result.getSelectedItem();                
                if (selectedValue.cell == leftCell) {
                    return;
                }
                
                if (leftCell != null) {
                    leftCell.removeRightCellId(cell.getId());
                }
                
                if (selectedValue.cell != null) {
                    cell.setLeftCellId(selectedValue.cell.getId());
                    if (selectedValue.cell.getRightCellIdList() == null || !selectedValue.cell.getRightCellIdList().contains(cell.getId())) {
                        selectedValue.cell.addRightCellId(cell.getId());
                    }
                } else {
                    cell.setLeftCellId(null);
                }
            }
        });

        AutoCompleteDecorator.decorate(result);

        return result;
    }

    private JCheckBox createChangeTopOnMovingCheckBox() {
        final JCheckBox result = new JCheckBox("Prohibit changing vertical position of cell after shift");
        result.setBorder(null);

        result.setSelected(!cell.isChangeTopOnMoving());
        result.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cell.setChangeTopOnMoving(!result.isSelected());
            }
        });

        return result;
    }

    private void addCellsToList(IReportWidgetContainer container, List<AdsReportCell> cellsList) {
        for (AdsReportWidget widget : container.getWidgets()) {
            if (widget instanceof AdsReportCell && widget != cell) {
                cellsList.add((AdsReportCell) widget);
            } else if (widget instanceof IReportWidgetContainer) {
                addCellsToList((IReportWidgetContainer) widget, cellsList);
            }
        }
    }

    private static class ColumnsComboBoxItem implements Comparable<ColumnsComboBoxItem> {

        public final Id columnId;
        private final String displayVal;

        public ColumnsComboBoxItem(AdsReportColumnDef column) {
            columnId = column == null ? null : column.getId();
            displayVal = column == null ? UNDEFINED : column.getName();
        }

        @Override
        public String toString() {
            return displayVal;
        }

        @Override
        public int compareTo(ColumnsComboBoxItem o) {
            return displayVal.compareTo(o.displayVal);
        }
    }

    private static class CellsComboBoxItem implements Comparable<CellsComboBoxItem> {

        public final AdsReportCell cell;

        public CellsComboBoxItem(AdsReportCell cell) {
            this.cell = cell;
        }

        @Override
        public String toString() {
            return cell == null ? UNDEFINED : cell.getName();
        }

        @Override
        public int compareTo(CellsComboBoxItem o) {
            return toString().compareTo(o.toString());
        }
    }
}
