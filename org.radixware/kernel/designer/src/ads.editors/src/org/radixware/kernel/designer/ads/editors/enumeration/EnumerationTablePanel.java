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
package org.radixware.kernel.designer.ads.editors.enumeration;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.openide.actions.CopyAction;
import org.openide.util.*;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.enums.EEnumDefinitionItemViewFormat;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib.StringCellEditor;
import org.radixware.kernel.designer.common.dialogs.components.UpdateLocker;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsages;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsagesCfg;
import org.radixware.kernel.designer.common.dialogs.utils.SearchFieldAdapter;
import org.radixware.kernel.designer.common.editors.*;
import org.radixware.kernel.designer.common.general.utils.SwingUtils;

public class EnumerationTablePanel extends javax.swing.JPanel {

    private AdsEnumDef enumDef;
    private EIsoLanguage[] languagesArray;
    private EnumerationTableModel model;
    private SortOrder sortOrder = SortOrder.UNSORTED;
    private Map<Integer, SortOrder> column2SortOrder = new HashMap<>();
    private final String SEARCH_DEFAULT = NbBundle.getMessage(EnumerationTablePanel.class, "ItemTablePanel-SearchDefault");
    private boolean isInnerSortingEnabled = true;
    private boolean isHexMode = false;

    /**
     * Creates new form EnumerationTablePanel
     */
    public EnumerationTablePanel() {
        initComponents();

        label.setText("");

        ActionListener btnListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(addButton)) {
                    EnumerationTablePanel.this.onAddButtonPressed();
                } else if (e.getSource().equals(removeButton)) {
                    EnumerationTablePanel.this.onRemoveButtonPressed();
                } else if (e.getSource().equals(overwriteButton)) {
                    EnumerationTablePanel.this.onOverwriteButtonPressed();
                } else if (e.getSource().equals(deprecateButton)) {
                    EnumerationTablePanel.this.onDeprecateButtonPressed();
                } else if (e.getSource().equals(upButton)) {
                    EnumerationTablePanel.this.onUpButtonPressed();
                } else if (e.getSource().equals(downButton)) {
                    EnumerationTablePanel.this.onDownButtonPressed();
                } else if (e.getSource().equals(fixButton)) {
                    EnumerationTablePanel.this.onFixButtonPressed();
                } else if (e.getSource().equals(usedByButton)) {
                    EnumerationTablePanel.this.onUsedByButtonPressed();
                } else if (e.getSource().equals(duplicateBtn)) {
                    EnumerationTablePanel.this.onDuplicateButtonPressed();
                } else if (e.getSource().equals(cmdEdit)) {
                    EnumerationTablePanel.this.onEditButtonPressed();
                }
            }
        };
        addButton.addActionListener(btnListener);
        removeButton.addActionListener(btnListener);
        upButton.addActionListener(btnListener);
        downButton.addActionListener(btnListener);
        overwriteButton.addActionListener(btnListener);
        deprecateButton.addActionListener(btnListener);
        fixButton.addActionListener(btnListener);
        usedByButton.addActionListener(btnListener);
        duplicateBtn.addActionListener(btnListener);
        cmdEdit.addActionListener(btnListener);

        searchField.setText(SEARCH_DEFAULT);
        DocumentListener searchListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!searchField.getText().equals(SEARCH_DEFAULT)) {
                    EnumerationTablePanel.this.search();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!searchField.getText().equals(SEARCH_DEFAULT)) {
                    EnumerationTablePanel.this.search();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (!searchField.getText().equals(SEARCH_DEFAULT)) {
                    EnumerationTablePanel.this.search();
                }
            }
        };
        searchField.getDocument().addDocumentListener(searchListener);

        FocusAdapter searchFocusAdapter = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (EnumerationTablePanel.this.searchField.getText().equals(SEARCH_DEFAULT)) {
                    EnumerationTablePanel.this.searchField.setText("");
                }
            }
        };
        searchField.addFocusListener(searchFocusAdapter);

        MouseAdapter searchMouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (EnumerationTablePanel.this.searchField.getText().equals(SEARCH_DEFAULT)) {
                    EnumerationTablePanel.this.searchField.setText("");
                }
            }
        };
        searchField.addMouseListener(searchMouseAdapter);

        ActionListener hexBoxListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!updateLocker.inUpdate()) {
                    EnumerationTablePanel.this.onHexModeChange();
                }
            }
        };
        hexBox.addActionListener(hexBoxListener);
    }
    private static RequestProcessor EDITOR_RP = new RequestProcessor(EnumerationTablePanel.class.getName());

    private void search() {
        final String text = searchField.getText().toLowerCase();
        if (text.length() == 0) {
            label.setText("");
            model.clearHiddenIndexes();
            table.setEnabled(true);
            table.getSelectionModel().setSelectionInterval(0, 0);
            model.reload();
            updateUpAndDownButtonsState();
        } else {
            label.setText("Searching...");

            table.setEnabled(false);
            EDITOR_RP.post(new Runnable() {
                @Override
                public void run() {

                    model.clearHiddenIndexes();

                    List<AdsEnumItemDef> hidden = new ArrayList<>();
                    Collection<AdsEnumItemDef> allItems = model.getStrictItemsCollection();

                    for (AdsEnumItemDef item : allItems) {
                        if (item != null) {
                            if (!SearchFieldAdapter.isFitingToken(text, item.getName().toLowerCase())) {
                                hidden.add(item);
                            }
                        }
                    }

                    for (AdsEnumItemDef hItem : hidden) {
                        model.addHiddenItem(hItem);
                    }
                    Mutex.EVENT.readAccess(new Runnable() {
                        @Override
                        public void run() {
                            if (model.getRowCount() > 0) {
                                table.setRowSelectionInterval(0, 0);
                                updateRemoveAndEditButtonState();
                            } else {
                                removeButton.setEnabled(false);
                            }

                            label.setText("");
                            table.setEnabled(true);

                            model.fireTableDataChanged();
                            table.getSelectionModel().setSelectionInterval(0, 0);
                            updateUpAndDownButtonsState();

                        }
                    });

                }
            });
        }

    }

    private void onHexModeChange() {
        this.isHexMode = this.hexBox.isSelected();

        enumDef.setItemsViewFormat(isHexMode ? EEnumDefinitionItemViewFormat.HEXADECIMAL : null);

        setupTable();
    }

    private void onUsedByButtonPressed() {
        final AdsEnumItemDef selected = getSelectedItem();
        if (selected != null) {
            RequestProcessor.getDefault().post(new Runnable() {
                @Override
                public void run() {
                    final FindUsagesCfg cfg = new FindUsagesCfg(selected);
                    FindUsages.search(cfg);
                }
            });
        }
    }

    private void onFixButtonPressed() {
        enumDef.getViewOrder().fixup();
        onOpening();
    }

    private void addEnumItem(AdsEnumItemDef original) {
        final NewItemDialog itemDialog = new NewItemDialog(enumDef, original, isHexMode);
        itemDialog.invokeModalDialog();
        if (itemDialog.isOK()) {
            final AdsEnumItemDef createdItem = (AdsEnumItemDef) itemDialog.getCreatedItem();

//            Map<EIsoLanguage, String> titles = itemDialog.getTitles(languagesArray);
//            AdsLocalizingBundleDef bundle = createdItem.findLocalizingBundle();
//            if (bundle != null) {
//                AdsMultilingualStringDef stringDef = null;
//                if (createdItem.getTitleId() != null) {
//                    stringDef = createdItem.findLocalizedString(createdItem.getTitleId());
//                }
//                if (stringDef == null) {
//                    stringDef = AdsMultilingualStringDef.Factory.newInstance();
//                    bundle.getStrings().getLocal().add(stringDef);
//                    createdItem.setTitleId(stringDef.getId());
//                }
//                for (EIsoLanguage l : titles.keySet()) {
//                    stringDef.setValue(l, titles.get(l));
//                }
//            }
            int selected = table.getSelectedRow();
            int actualSelected = model.getActualIndex(selected);
            if (actualSelected < model.getViewItemsCount()) {
                model.addItem(actualSelected + 1, createdItem);
            } else {
                model.addItem(createdItem);
            }

//            final String itemsName = createdItem.getName();
//            final int row = model.getRowByViewItem(createdItem);
//
//            for (Integer column : model.getColumnsLanguagesIndexes()) {
//                model.setValueAt(itemsName, row, column);
//            }
//
//            enumDef.findExistingLocalizingBundle().getStrings().getLocal().add(stringDef);
//            createdItem.setTitleId(stringDef.getId());
            final int newRowPosition = table.convertRowIndexToView(model.getRowByViewItem(createdItem));
            table.setRowSelectionInterval(newRowPosition, newRowPosition);

            updateUpAndDownButtonsState();
            updateRemoveAndEditButtonState();
            table.scrollToVisible(newRowPosition, 0);
        }
    }

    private void onDuplicateButtonPressed() {
        int selection = table.getSelectedRow();
        if (selection > -1 && selection < model.getRowCount()) {
            addEnumItem(model.getViewItemByRow(selection));
        }
    }

    private void onEditButtonPressed() {
        int selection = table.getSelectedRow();
        if (selection > -1 && selection < model.getRowCount()) {
            final AdsEnumItemDef enumItem = model.getViewItemByRow(selection);

            final NewItemDialog itemDialog = new NewItemDialog(enumDef, enumItem, isHexMode, true);

            itemDialog.invokeModalDialog();

            if (itemDialog.isOK()) {
                final AdsEnumItemDef createdItem = (AdsEnumItemDef) itemDialog.getCreatedItem();

                NewItemPanel.install(enumDef, enumItem, createdItem, isHexMode);

                final int newRowPosition = table.convertRowIndexToView(model.getRowByViewItem(enumItem));
                table.setRowSelectionInterval(newRowPosition, newRowPosition);
                model.fireTableRowsUpdated(selection, selection);

                updateUpAndDownButtonsState();
                updateRemoveAndEditButtonState();
                table.scrollToVisible(newRowPosition, 0);
            }
        }
    }

    private void onAddButtonPressed() {
        addEnumItem(null);
    }

    private void onRemoveButtonPressed() {
        String frameHeader = NbBundle.getMessage(EnumerationTablePanel.class, "ItemTablePanel-RemoveDialog-Header");
        String frameQuest = "Remove selected items?";
        //NbBundle.getMessage(EnumerationTablePanel.class, "ItemTablePanel-RemoveDialog-Quest");
        if (JOptionPane.showConfirmDialog(new JFrame(), frameQuest, frameHeader, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

//            final int index = table.getSelectedRow();
//            if (index > -1 && index < model.getRowCount()) {
//                model.removeRow(index);
//
//                final int rowsCount = model.getRowCount();
//                if (rowsCount > 0) {
//                    if (index == rowsCount) {
//                        table.setRowSelectionInterval(index - 1, index - 1);
//                    } else {
//                        table.setRowSelectionInterval(index, index);
//                    }
//
//                }
//                updateRemoveButtonState();
//                updateUpAndDownButtonsState();
//                updateDeprecateButtonState();
//                updateOvewriteButtonState();
//                table.requestFocusInWindow();
//            }
            int arr[] = table.getSelectedRows();
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                boolean isSelect = false;
                for (int j : arr) {
                    if (j == i) {
                        isSelect = true;
                        break;
                    }
                }
                if (isSelect) {
                    model.removeRow(i);
                }
            }
            updateRemoveAndEditButtonState();
            updateUpAndDownButtonsState();
            updateDeprecateButtonState();
            updateOvewriteButtonState();
            table.requestFocusInWindow();

        }
    }

    private void onUpButtonPressed() {
        final AdsEnumItemDef curItem = getSelectedItem();
        if (curItem != null) {
            if (sortOrder.equals(SortOrder.DESCENDING)) {
                enumDef.getViewOrder().moveDn(curItem);
            } else {
                enumDef.getViewOrder().moveUp(curItem);
            }

            model.reload();
            final int newRowPosition = table.convertRowIndexToView(model.getRowByViewItem(curItem));
            table.setRowSelectionInterval(newRowPosition, newRowPosition);
            updateUpAndDownButtonsState();
            table.scrollToVisible(newRowPosition, 0);
        }
    }

    private void onDownButtonPressed() {
        final AdsEnumItemDef curItem = getSelectedItem();
        if (curItem != null) {
            if (sortOrder.equals(SortOrder.DESCENDING)) {
                enumDef.getViewOrder().moveUp(curItem);
            } else {
                enumDef.getViewOrder().moveDn(curItem);
            }
            model.reload();
            final int newRowPosition = table.convertRowIndexToView(model.getRowByViewItem(curItem));
            table.setRowSelectionInterval(newRowPosition, newRowPosition);
            updateUpAndDownButtonsState();
            table.scrollToVisible(newRowPosition, 0);
        }

    }

    private void onOverwriteButtonPressed() {
        if (!updateOverwrite) {
            final AdsEnumItemDef curItem = getSelectedItem();
            if (curItem != null) {
                final int index = model.getRowByViewItem(curItem);
                if (index != -1) {
                    boolean state = overwriteButton.isSelected();
                    searchField.setText("");
                    if (state) {
                        model.overwriteItem(curItem);
                        overwriteButton.setToolTipText(NbBundle.getMessage(EnumerationTablePanel.class, "ItemTableButtons-Deoverwrite"));
                    } else {
                        model.deoverwriteItem(curItem);
                        overwriteButton.setToolTipText(NbBundle.getMessage(EnumerationTablePanel.class, "ItemTableButtons-Overwrite"));
                    }
                    final int newRowPosition = table.convertRowIndexToView(index);
                    table.setRowSelectionInterval(newRowPosition, newRowPosition);
                    table.scrollToVisible(newRowPosition, 0);
                }
            }
        }
    }

    private void onDeprecateButtonPressed() {
        if (!updateDeprecate) {
            final AdsEnumItemDef curItem = getSelectedItem();
            if (curItem != null) {
                assert (enumDef.getItems().list(EScope.LOCAL).contains(curItem) && !curItem.isOverwrite());
                boolean state = deprecateButton.isSelected();
                searchField.setText("");
                if (state) {
                    assert (!curItem.isDeprecated());
                    curItem.setDeprecated(true);
                    deprecateButton.setIcon(RadixWareIcons.JAVA.DEPRECATE.getIcon());
                    deprecateButton.setToolTipText(NbBundle.getMessage(EnumerationTablePanel.class, "ItemTableButtons-Undeprecate"));
                } else {
                    assert (curItem.isDeprecated());
                    curItem.setDeprecated(false);
                    deprecateButton.setIcon(RadixWareIcons.JAVA.UNDEPRECATE.getIcon());
                    deprecateButton.setToolTipText(NbBundle.getMessage(EnumerationTablePanel.class, "ItemTableButtons-Deprecate"));
                }
                final int newRowPosition = table.convertRowIndexToView(model.getRowByViewItem(curItem));
                table.setRowSelectionInterval(newRowPosition, newRowPosition);
                table.scrollToVisible(newRowPosition, 0);
            }
        }
    }

    private UpdateLocker updateLocker = new UpdateLocker();

    public void open(final AdsEnumDef enumDef) {
        this.enumDef = enumDef;
        onOpening();
    }

    private void onOpening() {
        try {
            updateLocker.enterUpdate();
            setupTable();
            update();
            table.requestFocusInWindow();
        } finally {
            updateLocker.leavUpdate();
        }
    }
    private AdsEnumItemDef lastSelectedItem;
    private boolean isFixed = true;

    public void update() {
        this.hexBox.setVisible(enumDef.getItemType().equals(EValType.INT));

        final Layer layer = enumDef.getModule().getSegment().getLayer();
        languagesArray = new EIsoLanguage[layer.getLanguages().size()];
        layer.getLanguages().toArray(languagesArray);

        usedByButton.setEnabled(getSelectedItem() != null);

        isFixed = enumDef.getViewOrder().isCorrect();
        fixButton.setEnabled(!isFixed);

        //table.setEnabled(!enumDef.isReadOnly());
        final boolean hasElements = model.getRowCount() > 0;
        if (hasElements) {
            if (lastSelectedItem != null) {
                int indexInModel = model.getRowByViewItem(lastSelectedItem);
                int indexInTable = table.convertRowIndexToView(indexInModel);

                //additional checking because of non-constant items count
                if (table.getRowCount() >= indexInTable) {
                    table.setRowSelectionInterval(indexInTable, indexInTable);
                } else {
                    table.setRowSelectionInterval(0, 0);
                }
            } else {
                table.setRowSelectionInterval(0, 0);
            }
        }

        if (enumDef.isReadOnly()) {
            addButton.setEnabled(false);
            removeButton.setEnabled(false);
            cmdEdit.setEnabled(false);
        } else {
            addButton.setEnabled(enumDef.isExtendable() && isFixed);
            updateRemoveAndEditButtonState();
        }

        updateUpAndDownButtonsState();
        updateDeprecateButtonState();
        updateOvewriteButtonState();
    }

    private void setupTable() {
        isHexMode = enumDef.getItemsViewFormat() == EEnumDefinitionItemViewFormat.HEXADECIMAL;
        this.hexBox.setSelected(isHexMode);

        model = new EnumerationTableModel(enumDef);
        table.setModel(model);

        column2SortOrder.clear();
        column2SortOrder.put(EnumerationTableModel.NAME_COLUMN, SortOrder.UNSORTED);
        column2SortOrder.put(EnumerationTableModel.VALUE_COLUMN, SortOrder.UNSORTED);

        final Layer layer = enumDef.getModule().getSegment().getLayer();
        for (int i = 0, n = layer.getLanguages().size(); i < n; i++) {
            column2SortOrder.put(EnumerationTableModel.VALUE_COLUMN + i + 1, SortOrder.UNSORTED);
        }

        column2SortOrder.put(this.model.DOMAIN_COLUMN, SortOrder.UNSORTED);

        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        final ColorCellRenderer colorCellRenderer = new ColorCellRenderer(isHexMode);
        table.setDefaultRenderer(Object.class, colorCellRenderer);
        table.setDefaultRenderer(Integer.class, colorCellRenderer);
        table.setDefaultRenderer(EValType.class, colorCellRenderer);
        table.setDefaultRenderer(Icon.class, new IconCellRenderer());
        table.setDefaultRenderer(String.class, colorCellRenderer);

        final TableColumnModel tableColumnModel = table.getColumnModel();
        javax.swing.table.TableColumn indexColumn = tableColumnModel.getColumn(EnumerationTableModel.INDEX_COLUMN);
        indexColumn.setPreferredWidth(30);

        final boolean readonly = enumDef.isReadOnly();

        tableColumnModel.getColumn(EnumerationTableModel.NAME_COLUMN).setCellEditor(new StringCellEditor(null, model, readonly));
        tableColumnModel.getColumn(model.ICON_COLUMN).setCellEditor(new IconExtendableTableCellEditor(enumDef, model));
        tableColumnModel.getColumn(model.DOMAIN_COLUMN).setCellEditor(getDomainsCellEditor());

        final EValType itemType = enumDef.getItemType();
        if (itemType == EValType.CHAR) {
            tableColumnModel.getColumn(EnumerationTableModel.VALUE_COLUMN).setCellEditor(new CharCellEditor(model));
        } else if (itemType == EValType.INT) {
            tableColumnModel.getColumn(EnumerationTableModel.VALUE_COLUMN).setCellEditor(new LongCellEditor(isHexMode));
        } else if (itemType == EValType.STR) {
            MultiLinedPopupCellEditor multiCellEditor = new MultiLinedPopupCellEditor(model);
            tableColumnModel.getColumn(EnumerationTableModel.VALUE_COLUMN).setCellEditor(multiCellEditor);
        }

        for (int i = EnumerationTableModel.VALUE_COLUMN + 1, size = model.DOMAIN_COLUMN - 1; i <= size; i++) {
            tableColumnModel.getColumn(i).setCellEditor(new StringCellEditor(null, model, model.getColumnLanguage(i), enumDef, readonly));
        }
        table.setRowSelectionAllowed(true);
        table.setAutoCreateColumnsFromModel(false);
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        MouseAdapter tableMouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EnumerationTablePanel.this.updateRemoveAndEditButtonState();
                EnumerationTablePanel.this.updateUpAndDownButtonsState();
                EnumerationTablePanel.this.updateOvewriteButtonState();
                EnumerationTablePanel.this.updateDeprecateButtonState();
            }
        };
        table.addMouseListener(tableMouseAdapter);

        TableColumnModelListener tableColumnModelListener = new TableColumnModelListener() {
            @Override
            public void columnAdded(TableColumnModelEvent e) {
            }

            @Override
            public void columnRemoved(TableColumnModelEvent e) {
            }

            @Override
            public void columnMoved(TableColumnModelEvent e) {
            }

            @Override
            public void columnSelectionChanged(ListSelectionEvent e) {
            }

            @Override
            public void columnMarginChanged(ChangeEvent e) {
                Preferences preferences = org.radixware.kernel.common.utils.Utils.findOrCreatePreferences(CONFIG_STR);
                if (preferences != null && isMustSaveColumnWidth) {
                    TableColumn col;
                    for (int i = 0; i < tableColumnModel.getColumnCount(); i++) {
                        col = tableColumnModel.getColumn(i);
                        preferences.putInt("ColumnWidth" + String.valueOf(i), col.getWidth());
                    }
                }
            }
        };
        table.getColumnModel().addColumnModelListener(tableColumnModelListener);

        MouseAdapter headerMouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                final AdsEnumItemDef curItem = EnumerationTablePanel.this.getSelectedItem();
                EnumerationTablePanel.this.onTableHeaderClickEvent(e);
                if (curItem != null) {
                    final int newRowPosition = table.convertRowIndexToView(model.getRowByViewItem(curItem));
                    table.setRowSelectionInterval(newRowPosition, newRowPosition);
                }

                EnumerationTablePanel.this.updateUpAndDownButtonsState();

            }
        };
        table.getTableHeader().addMouseListener(headerMouseAdapter);
        table.getColumnModel().getColumn(EnumerationTableModel.NAME_COLUMN).setHeaderRenderer(new HeaderRenderer());
        table.getColumnModel().getColumn(EnumerationTableModel.VALUE_COLUMN).setHeaderRenderer(new HeaderRenderer());
        table.getColumnModel().getColumn(model.DOMAIN_COLUMN).setHeaderRenderer(new HeaderRenderer());
        for (int col = EnumerationTableModel.VALUE_COLUMN + 1; col < model.DOMAIN_COLUMN; col++) {
            table.getColumnModel().getColumn(col).setHeaderRenderer(new HeaderRenderer());
        }

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                EnumerationTablePanel.this.onSelectionChange();
            }
        });

        Preferences preferences = null;
        try {
            preferences = org.radixware.kernel.common.utils.Utils.findPreferences(CONFIG_STR);
        } catch (BackingStoreException ex) {
            Exceptions.printStackTrace(ex);
        }
        if (preferences != null) {
            isMustSaveColumnWidth = false;
            final TableColumnModel tableColumnModel2 = table.getColumnModel();
            TableColumn col;
            for (int i = 0; i < tableColumnModel2.getColumnCount(); i++) {
                col = tableColumnModel2.getColumn(i);
                col.setPreferredWidth(preferences.getInt("ColumnWidth" + String.valueOf(i), col.getPreferredWidth()));
                col.setWidth(preferences.getInt("ColumnWidth" + String.valueOf(i), col.getWidth()));
            }
            isMustSaveColumnWidth = true;
        }
    }
    private static String CONFIG_STR = "AnsEnumEditorConfigurations";
    private boolean isMustSaveColumnWidth = true;

    public void setSelectedItem(AdsEnumItemDef item) {
        if (item != null) {
            int indexInModel = model.getRowByViewItem(item);
            if (indexInModel != -1) {
                SwingUtils.setRowSelection(table, indexInModel);
            }
        }

    }

    public AdsEnumItemDef getSelectedItem() {
        final int selectedRow = table.getSelectedRow();
        return selectedRow == -1
                ? null
                : model.getViewItemByRow(selectedRow);
    }

    public void onExtendabilityChange(boolean enabled) {

        if (enabled) {
            enumDef.setExtendable(true);
            addButton.setEnabled(isFixed && !enumDef.isReadOnly());
            if (table.getRowCount() > 0) {
                table.clearSelection();
            }
        } else {
            enumDef.setExtendable(false);
            addButton.setEnabled(false);
            if (table.getRowCount() > 0) {
                table.clearSelection();
            }
            onOpening();
        }
        updateRemoveAndEditButtonState();
    }

    public void onSynchronizeAction(ActionEvent e) {
        model.synchronizeAdsDefinitionsItems();
        if (table.getRowCount() > 0) {
            final AdsEnumItemDef selected = getSelectedItem();
            final int newRowForSelected = selected != null ? model.getRowByViewItem(selected) : -1;
            if (newRowForSelected != -1) {
                final int newRowInTableForSelected = table.convertRowIndexToView(newRowForSelected);
                table.setRowSelectionInterval(newRowInTableForSelected, newRowInTableForSelected);
            } else {
                table.setRowSelectionInterval(0, 0);
            }
        }

        onOpening();
    }

    private void onSelectionChange() {
        lastSelectedItem = getSelectedItem();

//        RADIX-5735
//        if (table.isEditing()) {
//            table.getCellEditor().stopCellEditing();
//        }
        isFixed = enumDef.getViewOrder().isCorrect();
        fixButton.setEnabled(!isFixed);
        addButton.setEnabled(isFixed && !enumDef.isReadOnly() && enumDef.isExtendable());

        usedByButton.setEnabled(lastSelectedItem != null);
        duplicateBtn.setEnabled(lastSelectedItem != null);
        cmdEdit.setEnabled(lastSelectedItem != null);

        updateDeprecateButtonState();
        updateOvewriteButtonState();
        updateRemoveAndEditButtonState();
        updateUpAndDownButtonsState();
    }

    private void onTableHeaderClickEvent(MouseEvent e) {
        final int column = table.columnAtPoint(e.getPoint());
        //if (column > -1 && column <= EnumerationTableModel.VALUE_COLUMN) {
        if (column > -1 && column <= model.DOMAIN_COLUMN) {
            if (column == EnumerationTableModel.INDEX_COLUMN) {
                isInnerSortingEnabled = true;
                model.reloadWithDefaultComparator();
                sortOrder = SortOrder.UNSORTED;
                for (Integer key : column2SortOrder.keySet()) {
                    column2SortOrder.put(key, sortOrder);
                }
            } else {
                isInnerSortingEnabled = false;
                if (sortOrder.equals(SortOrder.UNSORTED)) {
                    sortOrder = SortOrder.DESCENDING;
                } else {
                    if (sortOrder.equals(SortOrder.ASCENDING)) {
                        sortOrder = SortOrder.DESCENDING;
                    } else {
                        sortOrder = SortOrder.ASCENDING;
                    }
                }
                column2SortOrder.put(column, sortOrder);
                for (Integer key : column2SortOrder.keySet()) {
                    if (!key.equals(column)) {
                        column2SortOrder.put(key, SortOrder.UNSORTED);
                    }
                }
                model.sort(column, sortOrder);
            }
        } else {
            sortOrder = SortOrder.UNSORTED;
            isInnerSortingEnabled = true;
        }
    }

    private DomainsCellEditor getDomainsCellEditor() {
//        ExtendableTableCellEditor domainsTableCellEditor = new ExtendableTableCellEditor(true);
//        javax.swing.JButton chooseDomainsButton = domainsTableCellEditor.addButton();
//        chooseDomainsButton.setEnabled(true);
//        chooseDomainsButton.setIcon(RadixWareIcons.ARROW.GO_TO_OBJECT.getIcon(13, 13));
//        chooseDomainsButton.setToolTipText("Configure Domains");
//
//        ActionListener chooseButton = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                final int curRowIndex = table.getSelectedRow();
//                final AdsEnumItemDef context = model.getViewItemByRow(curRowIndex);
//                AdsDefinitionDomainsEditor editor = new AdsDefinitionDomainsEditor();
//                editor.open(context, null);
//                if (editor.showModal()) {
//                    table.getCellEditor().stopCellEditing();
//                }
//            }
//        };
//        chooseDomainsButton.addActionListener(chooseButton);

        return new DomainsCellEditor();
    }

    private static class DomainsCellEditor extends AbstractCellEditor implements TableCellEditor {

        private final EnumItemDomainsEditor editor;
        private JTable table;

        public DomainsCellEditor() {
            editor = new EnumItemDomainsEditor(new Runnable() {
                @Override
                public void run() {
                    if (table != null) {
                        table.getCellEditor().stopCellEditing();
                    }
                }
            });
        }

        @Override
        public Object getCellEditorValue() {
            return editor.getItem();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.table = table;

            final EnumerationTableModel enumerationTableModel = (EnumerationTableModel) table.getModel();
            editor.open(enumerationTableModel.getViewItemByRow(row));

            return editor;
        }
    }

    private void updateRemoveAndEditButtonState() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            final int rowIndexInModel = table.convertRowIndexToModel(selectedRow);
            final boolean isEnabled = model.isItemRemovable(rowIndexInModel) && !enumDef.isReadOnly()
                    && isFixed;
            removeButton.setEnabled(isEnabled);
            cmdEdit.setEnabled(isEnabled);

        } else {
            removeButton.setEnabled(false);
            cmdEdit.setEnabled(false);
        }
    }

    private void updateUpAndDownButtonsState() {
        final boolean readonly = enumDef.isReadOnly();

        if (isInnerSortingEnabled) {
            final int curRow = table.getSelectedRow();
            final int count = model.getRowCount();
            final boolean possibility = !readonly && curRow != -1 && count > 1;

            upButton.setEnabled(possibility
                    && curRow > 0
                    && isFixed);
            downButton.setEnabled(possibility
                    && curRow < (count - 1)
                    && isFixed);
        } else {
            upButton.setEnabled(false);
            downButton.setEnabled(false);
        }
    }
    private boolean updateDeprecate = false;

    private void updateDeprecateButtonState() {
        updateDeprecate = true;
        final boolean readonly = enumDef.isReadOnly();
        final AdsEnumItemDef selectedItem = getSelectedItem();
        if (selectedItem != null) {
            if (!selectedItem.isOverwrite() && enumDef.getItems().list(EScope.LOCAL).contains(selectedItem)) {
                if (selectedItem.isDeprecated()) {
                    deprecateButton.setSelected(true);
                    deprecateButton.setEnabled(!readonly);
                } else {
                    deprecateButton.setSelected(false);
                    deprecateButton.setEnabled(!readonly);
                }
            } else {
                deprecateButton.setSelected(false);
                deprecateButton.setEnabled(false);
            }
        } else {
            deprecateButton.setSelected(false);
            deprecateButton.setEnabled(false);
        }
        deprecateButton.setToolTipText(deprecateButton.isSelected() ? NbBundle.getMessage(EnumerationTablePanel.class, "ItemTableButtons-Undeprecate") : NbBundle.getMessage(EnumerationTablePanel.class, "ItemTableButtons-Deprecate"));

        updateDeprecate = false;
    }
    private boolean updateOverwrite = false;

    private void updateOvewriteButtonState() {
        updateOverwrite = true;
        final boolean readonly = enumDef.isReadOnly();
        final AdsEnumItemDef selectedItem = getSelectedItem();
        if (selectedItem != null) {
            if (selectedItem.getOwnerEnum().equals(enumDef)) {
                if (selectedItem.isOverwrite()) {
                    overwriteButton.setSelected(true);
                    overwriteButton.setEnabled(!readonly);
                } else {
                    overwriteButton.setSelected(false);
                    overwriteButton.setEnabled(selectedItem.getEditPossibility().contains(AdsEnumItemDef.EditPossibility.OVERWRITE) && !readonly);
                }
            } else {
                overwriteButton.setSelected(false);
                overwriteButton.setEnabled(!readonly);
            }
        } else {
            overwriteButton.setSelected(false);
            overwriteButton.setEnabled(false);
        }

        overwriteButton.setToolTipText(overwriteButton.isSelected() ? NbBundle.getMessage(EnumerationTablePanel.class, "ItemTableButtons-Deoverwrite") : NbBundle.getMessage(EnumerationTablePanel.class, "ItemTableButtons-Overwrite"));

        updateOverwrite = false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new org.radixware.kernel.designer.common.dialogs.components.TunedTable();
        fixButton = new javax.swing.JButton();
        label = new javax.swing.JLabel();
        hexBox = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        cmdEdit = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        overwriteButton = new javax.swing.JToggleButton();
        deprecateButton = new javax.swing.JToggleButton();
        usedByButton = new javax.swing.JButton();
        duplicateBtn = new javax.swing.JButton();

        jLabel1.setText(org.openide.util.NbBundle.getMessage(EnumerationTablePanel.class, "ItemTablePanel-SearchTip")); // NOI18N

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(table);

        fixButton.setIcon(RadixWareDesignerIcon.EDIT.FIX.getIcon(13, 13));
        fixButton.setText(org.openide.util.NbBundle.getMessage(EnumerationTablePanel.class, "ItemTableButtons-Fix")); // NOI18N
        fixButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        label.setText(org.openide.util.NbBundle.getMessage(EnumerationTablePanel.class, "SearchTip")); // NOI18N

        hexBox.setText(org.openide.util.NbBundle.getMessage(EnumerationTablePanel.class, "ItemTablePanel-HexLabel")); // NOI18N

        jPanel1.setLayout(new java.awt.GridBagLayout());

        cmdEdit.setIcon(RadixWareIcons.EDIT.EDIT.getIcon(13, 13));
        cmdEdit.setText(org.openide.util.NbBundle.getMessage(EnumerationTablePanel.class, "EnumerationTablePanel.cmdEdit.text")); // NOI18N
        cmdEdit.setToolTipText(org.openide.util.NbBundle.getMessage(EnumerationTablePanel.class, "EnumerationTablePanel.cmdEdit.toolTipText")); // NOI18N
        cmdEdit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel1.add(cmdEdit, gridBagConstraints);

        addButton.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon(13, 13));
        addButton.setText(org.openide.util.NbBundle.getMessage(EnumerationTablePanel.class, "ItemTableButtons-Add")); // NOI18N
        addButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel1.add(addButton, gridBagConstraints);

        removeButton.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon(13, 13));
        removeButton.setText(org.openide.util.NbBundle.getMessage(EnumerationTablePanel.class, "ItemTableButtons-Remove")); // NOI18N
        removeButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel1.add(removeButton, gridBagConstraints);

        upButton.setIcon(RadixWareDesignerIcon.ARROW.MOVE_UP.getIcon(13, 13));
        upButton.setText(org.openide.util.NbBundle.getMessage(EnumerationTablePanel.class, "ItemTableButtons-Up")); // NOI18N
        upButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel1.add(upButton, gridBagConstraints);

        downButton.setIcon(RadixWareDesignerIcon.ARROW.MOVE_DOWN.getIcon(13, 13));
        downButton.setText(org.openide.util.NbBundle.getMessage(EnumerationTablePanel.class, "ItemTableButtons-Down")); // NOI18N
        downButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel1.add(downButton, gridBagConstraints);

        overwriteButton.setIcon(RadixWareDesignerIcon.ARROW.GO_TO_OVERWRITE.getIcon(13, 13));
        overwriteButton.setText(org.openide.util.NbBundle.getMessage(EnumerationTablePanel.class, "ItemTableButtons-Overwrite")); // NOI18N
        overwriteButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel1.add(overwriteButton, gridBagConstraints);

        deprecateButton.setIcon(RadixWareDesignerIcon.JAVA.DEPRECATE.getIcon(13, 13));
        deprecateButton.setText(org.openide.util.NbBundle.getMessage(EnumerationTablePanel.class, "ItemTableButtons-Deprecate")); // NOI18N
        deprecateButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel1.add(deprecateButton, gridBagConstraints);

        usedByButton.setIcon(RadixWareDesignerIcon.TREE.DEPENDENCIES.getIcon(13, 13));
        usedByButton.setText(org.openide.util.NbBundle.getMessage(EnumerationTablePanel.class, "ItemTableButtons-UsedBy")); // NOI18N
        usedByButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel1.add(usedByButton, gridBagConstraints);

        duplicateBtn.setIcon(SystemAction.get(CopyAction.class).getIcon());
        duplicateBtn.setText(org.openide.util.NbBundle.getMessage(EnumerationTablePanel.class, "ItemTableButtons-Duplicate")); // NOI18N
        duplicateBtn.setToolTipText(org.openide.util.NbBundle.getMessage(EnumerationTablePanel.class, "ItemTableToolTips-DuplicateBtn")); // NOI18N
        duplicateBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel1.add(duplicateBtn, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchField, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fixButton)
                    .addComponent(label)
                    .addComponent(hexBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hexBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(fixButton))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JToggleButton deprecateButton;
    private javax.swing.JButton downButton;
    private javax.swing.JButton duplicateBtn;
    private javax.swing.JButton fixButton;
    private javax.swing.JCheckBox hexBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel label;
    private javax.swing.JToggleButton overwriteButton;
    private javax.swing.JButton removeButton;
    private javax.swing.JTextField searchField;
    private org.radixware.kernel.designer.common.dialogs.components.TunedTable table;
    private javax.swing.JButton upButton;
    private javax.swing.JButton usedByButton;
    // End of variables declaration//GEN-END:variables

    class HeaderRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component s = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            Component headerComponent = table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (headerComponent instanceof javax.swing.JLabel) {
                if (column == EnumerationTableModel.NAME_COLUMN
                        || column == EnumerationTableModel.VALUE_COLUMN
                        || column == model.DOMAIN_COLUMN
                        || column > EnumerationTableModel.VALUE_COLUMN && column < model.DOMAIN_COLUMN) {
                    javax.swing.JLabel asLabel = (javax.swing.JLabel) headerComponent;
                    SortOrder order = column2SortOrder.get(column);
                    if (order == null) {
                        order = sortOrder;
                    }
                    if (order != null) {
                        if (order.equals(SortOrder.ASCENDING)) {
                            asLabel.setIcon(RadixWareIcons.ENUMERATION.REVERT_SORT.getIcon(10, 10));
                        } else if (order.equals(SortOrder.DESCENDING)) {
                            asLabel.setIcon(RadixWareIcons.ENUMERATION.DIRECT_SORT.getIcon(10, 10));
                        } else {
                            asLabel.setIcon(null);
                        }
                    }
                }
                return headerComponent;
            }
            return s;
        }
    }
}
