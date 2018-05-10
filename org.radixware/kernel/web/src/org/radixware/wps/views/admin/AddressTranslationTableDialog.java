/*
 * Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.wps.views.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.eas.AddressTranslationTable;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.FormBox;
import org.radixware.wps.rwt.Grid;
import org.radixware.wps.rwt.Grid.Row;
import org.radixware.wps.rwt.IGrid;
import org.radixware.wps.rwt.Splitter;
import org.radixware.wps.rwt.ToolBar;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.ValueEditor;
import org.radixware.wps.rwt.VerticalBox;
import org.radixware.wps.views.RwtAction;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;

public class AddressTranslationTableDialog extends Dialog {

    private final Iterator<Map.Entry<String, AddressTranslationTable.TranslationTable>> iterator;
    private final ValStrEditorController nameEditorController;
    private final ValStrEditorController originalInetAddressEditorController;
    private final ValStrEditorController newInetAddressEditorController;
    private final Grid table = new Grid();
    private final File file;
    private final MessageProvider mp;

    public AddressTranslationTableDialog(Iterator<Map.Entry<String, AddressTranslationTable.TranslationTable>> iterator, File file) {
        super();
        this.iterator = iterator;
        this.file = file;
        table.setSelectionMode(Grid.SelectionMode.ROW);
        nameEditorController = new ValStrEditorController(getEnvironment());
        originalInetAddressEditorController = new ValStrEditorController(getEnvironment());
        newInetAddressEditorController = new ValStrEditorController(getEnvironment());
        mp = getEnvironment().getMessageProvider();
        setupUI();
    }

    private void setupUI() {
        setWindowTitle(mp.translate("AdminPanel", "Address translation table"));
        VerticalBox mainLayout = new VerticalBox();
        final FormBox formBox = new FormBox();
        setWidth(800);
        setHeight(600);
        ToolBar toolBar = new ToolBar();
        final RwtAction createNewItemAction = new RwtAction(getEnvironment(), ClientIcon.CommonOperations.CREATE, mp.translate("AdminPanel", "Create"));
        createNewItemAction.setToolTip(mp.translate("AdminPanel", "Create Translation"));
        final RwtAction deleteItemAcion = new RwtAction(getEnvironment(), ClientIcon.CommonOperations.DELETE, mp.translate("AdminPanel", "Delete"));
        deleteItemAcion.setToolTip(mp.translate("AdminPanel", "Delete Translation"));
        createNewItemAction.addActionListener(new Action.ActionListener() {

            @Override
            public void triggered(Action action) {
                CreateTranslationDialog createTranslationDlg = new CreateTranslationDialog(table);
                if (createTranslationDlg.execDialog() == DialogResult.ACCEPTED) {
                    Row row = table.addRow();
                    row.getCell(0).setValue(createTranslationDlg.getName());
                    row.getCell(1).setValue(createTranslationDlg.getOriginalInetAddress());
                    row.getCell(2).setValue(createTranslationDlg.getNewInetAddress());
                    formBox.getHtml().setCss("display", "block");
                    Grid.Cell prevCell = table.getCurrentCell();
                    table.setCurrentCell(row.getCell(prevCell == null ? 0 : prevCell.getCellIndex()));
                    deleteItemAcion.setEnabled(true);
                }
            }
        });
        toolBar.addAction(createNewItemAction);

        deleteItemAcion.addActionListener(new Action.ActionListener() {

            @Override
            public void triggered(Action action) {
                String str = mp.translate("AdminPanel", "Do you really want to delete translation for SCP %s?");
                Row currentRow = table.getCurrentRow();
                if (getEnvironment().messageConfirmation(mp.translate("AdminPanel", "Delete translation"), String.format(str, currentRow.getCell(0).getValue()))) {
                    int rowIndex = table.getRowIndex(currentRow);
                    int currentCellIndex = table.getCurrentCell().getCellIndex();
                    if (rowIndex < table.getRowCount() - 1) {
                        Grid.Cell newCell = table.getRow(rowIndex + 1).getCell(currentCellIndex);
                        table.setCurrentCell(newCell);
                    } else if (rowIndex != 0) {
                        Grid.Cell newCell = table.getRow(rowIndex - 1).getCell(currentCellIndex);
                        table.setCurrentCell(newCell);
                    } else {
                        formBox.getHtml().setCss("display", "none");
                        deleteItemAcion.setEnabled(false);
                    }
                    table.removeRow(currentRow);
                }
            }
        });
        toolBar.addAction(deleteItemAcion);
        mainLayout.add(toolBar);
        add(mainLayout);
        addCloseAction(CLOSE_ACTION_OK, DialogResult.ACCEPTED);
        addCloseAction(CLOSE_ACTION_CANCEL, DialogResult.REJECTED);
        Splitter mainSplitter = new Splitter();
        mainSplitter.setOrientation(Splitter.Orientation.VERTICAL);
        mainSplitter.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        table.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        table.addColumn("SCP");
        table.addColumn(mp.translate("AdminPanel", "Original address"));
        table.addColumn(mp.translate("AdminPanel", "New address"));
        table.getColumn(0).setSizePolicy(IGrid.EColumnSizePolicy.STRETCH);
        table.getColumn(1).setSizePolicy(IGrid.EColumnSizePolicy.BY_CONTENT);
        table.getColumn(2).setSizePolicy(IGrid.EColumnSizePolicy.BY_CONTENT);
        while (iterator.hasNext()) {
            Map.Entry<String, AddressTranslationTable.TranslationTable> entry = iterator.next();
            AddressTranslationTable.TranslationTable translationTable = entry.getValue();
            Iterator<Map.Entry<InetSocketAddress, InetSocketAddress>> tableIter = translationTable.getIterator();
            while (tableIter.hasNext()) {
                Entry<InetSocketAddress, InetSocketAddress> inetSocketEntry = tableIter.next();
                Grid.Row row = table.addRow();
                row.getCell(0).setValue(entry.getKey());
                InetAddress inetAddress = inetSocketEntry.getKey().getAddress();
                row.getCell(1).setValue((inetAddress == null ? inetSocketEntry.getKey().getHostName() : inetAddress.getHostName()) + ":" + inetSocketEntry.getKey().getPort());
                InetSocketAddress newSocketAddr = inetSocketEntry.getValue();
                row.getCell(2).setValue(newSocketAddr == null ? null : inetSocketEntry.getValue().getAddress().getHostName() + ":" + inetSocketEntry.getValue().getPort());
            }
        }
        mainSplitter.add(table);
        formBox.addLabledEditor("SCP:", (UIObject) nameEditorController.getValEditor());
        formBox.addLabledEditor(mp.translate("AdminPanel", "Original address:"), (UIObject) originalInetAddressEditorController.getValEditor());
        originalInetAddressEditorController.setMandatory(true);
        formBox.addLabledEditor(mp.translate("AdminPanel", "New address:"), (UIObject) newInetAddressEditorController.getValEditor());
        mainSplitter.add(formBox);
        mainLayout.add(mainSplitter);
        table.addCurrentRowListener(new Grid.CurrentRowListener() {

            @Override
            public void currentRowChanged(Grid.Row oldRow, Grid.Row newRow) {
                if (newRow != null) {
                    nameEditorController.setValue((String) newRow.getCell(0).getValue());
                    originalInetAddressEditorController.setValue((String) newRow.getCell(1).getValue());
                    newInetAddressEditorController.setValue((String) newRow.getCell(2).getValue());
                }
            }

            @Override
            public boolean beforeChangeCurrentRow(Grid.Row oldRow, Grid.Row newRow) {
                return true;
            }
        });

        if (table.getRowCount() != 0) {
            table.setCurrentCell(table.getRow(0).getCell(0));
        } else {
            formBox.getHtml().setCss("display", "none");
            deleteItemAcion.setEnabled(false);
        }

        nameEditorController.addValueChangeListener(new ValueEditor.ValueChangeListener<String>() {

            @Override
            public void onValueChanged(String oldValue, String newValue) {
                table.getCurrentRow().getCell(0).setValue(newValue);
            }
        });

        originalInetAddressEditorController.addValueChangeListener(new ValueEditor.ValueChangeListener<String>() {

            @Override
            public void onValueChanged(String oldValue, String newValue) {
                if (newValue == null || newValue.isEmpty()) {
                    getEnvironment().messageError(mp.translate("AdminPanel", "Original address should not be empty"), 
                            mp.translate("AdminPanel", "Please enter Original address"));
                } else {
                    table.getCurrentRow().getCell(1).setValue(newValue);
                }
            }
        });

        newInetAddressEditorController.addValueChangeListener(new ValueEditor.ValueChangeListener<String>() {

            @Override
            public void onValueChanged(String oldValue, String newValue) {
                table.getCurrentRow().getCell(2).setValue(newValue);
            }
        });
    }

    @Override
    protected DialogResult onClose(String action, DialogResult actionResult) {
        if (actionResult == DialogResult.ACCEPTED) {
            try {
                if (!validate()) {
                    return null;
                }
            } catch (IllegalArgumentException ex) {
                return null;
            }

            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
                for (int i = 0; i < table.getRowCount(); i++) {
                    Row row = table.getRow(i);
                    Object newAddress = row.getCell(2).getValue();
                    StringBuilder sb = new StringBuilder();
                    sb.append((String) row.getCell(0).getValue()).append(", ");
                    sb.append(row.getCell(1).getValue().toString());
                    sb.append(", ");
                    if (newAddress != null) {
                        sb.append(newAddress.toString());
                    }
                    pw.println(sb.toString());
                }
            } catch (IOException ex) {
                Logger.getLogger(AddressTranslationTableDialog.class.getName()).log(Level.SEVERE, null, ex);
            } finally {

            }
        }
        return super.onClose(action, actionResult); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean validate() throws IllegalArgumentException {
        List<String> list = new LinkedList<>();
        String invalidValueStr = mp.translate("AdminPanel", "Invalid value");
        for (int i = 0; i < table.getRowCount(); i++) {
            Row currentRow = table.getRow(i);
            try {
                ValueFormatter.parseInetSocketAddress((String) currentRow.getCell(1).getValue());
            } catch (IllegalArgumentException ex) {
                table.setCurrentCell(currentRow.getCell(1));
                getEnvironment().messageException(invalidValueStr, String.format(mp.translate("AdminPanel", "Scp with name \"%1s\" has invalid original address: \"%2s\""), 
                                                currentRow.getCell(0).getValue(), currentRow.getCell(1).getValue()), ex);
                throw ex;
            }
            try {
                ValueFormatter.parseInetSocketAddress((String) currentRow.getCell(2).getValue());
            } catch (IllegalArgumentException ex) {
                table.setCurrentCell(currentRow.getCell(2));
                 getEnvironment().messageException(invalidValueStr, String.format(mp.translate("AdminPanel", "Scp with name \"%1s\" has invalid new address: \"%2s\""), 
                                                currentRow.getCell(0).getValue(), currentRow.getCell(2).getValue()), ex);
                throw ex;
            }
            String currentStr = (String)currentRow.getCell(0).getValue() + (String)currentRow.getCell(1).getValue();
            list.add(currentStr);
            for (int j = list.size() - 2; j >= 0; j--) {
                if (currentStr.equals(list.get(j))) {
                    table.setCurrentCell(currentRow.getCell(0));
                    getEnvironment().messageError(invalidValueStr, mp.translate("AdminPanel", "Table should not contain records with equal SCP and original address"));
                    return false;
                }
            }
        }
        return true;
    }
}
