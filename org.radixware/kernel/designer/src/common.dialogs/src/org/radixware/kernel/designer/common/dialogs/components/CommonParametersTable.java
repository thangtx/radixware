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

package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.LinkedList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.common.JavaSignatures;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib.ParameterNameChangeEvent;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;


public class CommonParametersTable extends TunedTable {

    private DefaultTableModel pModel;
    private CommonParametersEditorCellLib.NameCellEditor nameEditor;
    private CommonParametersEditorCellLib.TypeCellEditor typeEditor;
    private TunedTable.TunedComboCellEditor derivationEditor;
    private CommonParametersEditorCellLib.DescriptionCellEditor descriptionEditor;

    public void setTableModel(DefaultTableModel dataModel) {
        this.pModel = dataModel;
        super.setModel(dataModel);
    }

    public void closeCellEditors() {
        if (nameEditor.getComponent().isShowing()) {
            nameEditor.stopCellEditing();
        }
        if (typeEditor.getComponent().isShowing()) {
            typeEditor.stopCellEditing();
        }
        if (derivationEditor != null) {
            if (derivationEditor.getComponent().isShowing()) {
                derivationEditor.stopCellEditing();
            }
        }
        if (descriptionEditor != null &&
                descriptionEditor.isShowing()) {
            descriptionEditor.stopCellEditing();
        }
    }

    public void removeModelRow(int index) {
        pModel.removeRow(index);
    }

    public void addModelRow(Object[] content) {
        pModel.addRow(content);
    }

    public void insertModelRowToSelected(Object[] content){
        final int selected = getSelectedRow();
        if (selected < getRowCount() - 1 && selected > -1){
            pModel.insertRow(selected + 1, content);
            getSelectionModel().setSelectionInterval(selected + 1, selected + 1);
        } else {
            pModel.addRow(content);
            getSelectionModel().setSelectionInterval(getRowCount() - 1, getRowCount() - 1);
        }
    }

    public void moveRow(boolean up) {
        closeCellEditors();
        int row = getSelectedRow();

        CommonParametersEditorCellLib.StringCellValue nameitem = (CommonParametersEditorCellLib.StringCellValue) pModel.getValueAt(row, 0);
        CommonParametersEditorCellLib.TypePresentation typeitem = (CommonParametersEditorCellLib.TypePresentation) pModel.getValueAt(row, forGenerics ? 2 : 1);
        AdsTypeDeclaration.TypeArgument.Derivation derivation = forGenerics ? (AdsTypeDeclaration.TypeArgument.Derivation) pModel.getValueAt(row, 1) : null;
        String description = forGenerics ? null : pModel.getValueAt(row, 2).toString();
        pModel.removeRow(row);
        int newrow = up ? row - 1 : row + 1;
        if (forGenerics) {
            pModel.insertRow(newrow, new Object[]{nameitem, derivation, typeitem});
        } else {
            pModel.insertRow(newrow, new Object[]{nameitem, typeitem, new CommonParametersEditorCellLib.StringCellValue(description != null ? description : "") });
        }
        requestFocusInWindow();
        getSelectionModel().setSelectionInterval(newrow, newrow);
    }

    public void clearTable() {
        if (pModel != null) {
            while (pModel.getRowCount() != 0) {
                pModel.removeRow(0);
            }
            changeSupport.fireChange();
        }
    }

    public String[] getTableTitles() {
        if (pModel.getColumnCount() > 0) {
            String[] titles = new String[pModel.getColumnCount()];
            for (int i = 0, size = pModel.getColumnCount() - 1; i <= size; i++) {
                titles[i] = pModel.getColumnName(i);
            }
        }
        return null;
    }

    private boolean forGenerics = false;
    private boolean editGenericsNames = false;
    private AdsMethodProfileSettings profileSettings;

    public void setProfileSettings(AdsMethodProfileSettings settings){
        this.profileSettings = settings;
    }

    public void setEditGenericsNames(boolean edit){
        this.editGenericsNames = edit;
    }

    public void setupTableUI(final boolean readonlyGenericsNames,final boolean readonly, final boolean forGenerics, boolean isTransparent, boolean addNoneValue) {
        this.forGenerics = forGenerics;
        int columns = 3;
        String[] titles = new String[columns];
        titles[0] = NbBundle.getMessage(CommonParametersTable.class, "PP-Column-Name");
        titles[1] = NbBundle.getMessage(CommonParametersTable.class, "PP-Column-Type");
        titles[2] = forGenerics ? NbBundle.getMessage(CommonParametersTable.class, "PP-Column-Derivation")
                : NbBundle.getMessage(CommonParametersTable.class, "PP-Column-Description");

        pModel = new DefaultTableModel(titles, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0 && forGenerics && !readonly) {
                    AdsTypeDeclaration.TypeArgument.Derivation d = (AdsTypeDeclaration.TypeArgument.Derivation) getValueAt(row, 1);
                    return !d.equals(AdsTypeDeclaration.TypeArgument.Derivation.NONE);
                }
                return super.isCellEditable(row, column);
            }
        };
        setTableModel(pModel);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellSelectionEnabled(true);
        setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);

        if (profileSettings != null){
            final boolean checkedReadonly = profileSettings.layerReadonly ? true : !profileSettings.isTransparent && profileSettings.optionalReadonly;
            nameEditor = new CommonParametersEditorCellLib.NameCellEditor(getNameChangeSupport(), pModel, checkedReadonly);
            typeEditor = new CommonParametersEditorCellLib.TypeCellEditor(changeSupport, profileSettings, pModel);
            if (!forGenerics) {
                descriptionEditor = new CommonParametersEditorCellLib.DescriptionCellEditor(changeSupport, checkedReadonly, pModel);
            }
        } else  {
            boolean checkedReadonly = readonly ? !isTransparent : readonly;
            if (readonlyGenericsNames){
                checkedReadonly = true;
            }
            nameEditor = new CommonParametersEditorCellLib.NameCellEditor(getNameChangeSupport(), pModel, checkedReadonly);
            typeEditor = new CommonParametersEditorCellLib.TypeCellEditor(changeSupport, readonly, isTransparent, pModel);
            if (!forGenerics){
                descriptionEditor = new CommonParametersEditorCellLib.DescriptionCellEditor(changeSupport, checkedReadonly, pModel);
            }
        }

        getNameChangeSupport().addEventListener(new CommonParametersEditorCellLib.ParameterNameChangeListener() {

            @Override
            public void onEvent(ParameterNameChangeEvent e) {
                isComplete();
            }
        });

        getColumnModel().getColumn(0).setCellEditor(nameEditor);
        getColumnModel().getColumn(0).setCellRenderer(new CommonParametersEditorCellLib.LabelWithInsetsRenderer());
        if (forGenerics) {
            derivationEditor = new TunedTable.TunedComboCellEditor(this);
            if (addNoneValue) {
                derivationEditor.setComboValues(new Object[]{AdsTypeDeclaration.TypeArgument.Derivation.EXTENDS,
                            AdsTypeDeclaration.TypeArgument.Derivation.SUPER,
                            AdsTypeDeclaration.TypeArgument.Derivation.NONE});
            } else {
                derivationEditor.setComboValues(new Object[]{AdsTypeDeclaration.TypeArgument.Derivation.EXTENDS,
                            AdsTypeDeclaration.TypeArgument.Derivation.SUPER});
            }

            derivationEditor.addComboActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = getSelectedRow();
                    if (row > -1) {
                        AdsTypeDeclaration.TypeArgument.Derivation d = (AdsTypeDeclaration.TypeArgument.Derivation) derivationEditor.getCellEditorValue();
                        pModel.setValueAt(d, row, 1);
                        changeSupport.fireChange();
                    }
                }
            });
            getColumnModel().getColumn(1).setCellEditor(derivationEditor);
            getColumnModel().getColumn(1).setCellRenderer(new CommonParametersEditorCellLib.LabelWithInsetsRenderer());
        }
        int typeIndex = forGenerics ? 2 : 1;
        getColumnModel().getColumn(typeIndex).setCellEditor(typeEditor);
        getColumnModel().getColumn(typeIndex).setCellRenderer(new CommonParametersEditorCellLib.TypeCellRenderer());

        if (!forGenerics) {
            getColumnModel().getColumn(2).setCellEditor(descriptionEditor);
        }
    }

    public LinkedList<String> getCurrentNames() {
        LinkedList<String> names = new LinkedList<String>();
        for (int i = 0; i <= pModel.getRowCount() - 1; i++) {
            String n = ((CommonParametersEditorCellLib.StringCellValue) pModel.getValueAt(i, 0)).toString();
            names.add(n);
        }
        return names;
    }

    public String getCurrentRowName() {
        if (getSelectedRow() != -1) {
            String n = ((CommonParametersEditorCellLib.StringCellValue) pModel.getValueAt(getSelectedRow(), 0)).toString();
            return n;
        }
        return null;
    }

    public LinkedList<AdsTypeDeclaration> getCurrentTypes() {
        LinkedList<AdsTypeDeclaration> types = new LinkedList<AdsTypeDeclaration>();
        for (int i = 0; i <= pModel.getRowCount() - 1; i++) {
            AdsTypeDeclaration t = ((CommonParametersEditorCellLib.TypePresentation) pModel.getValueAt(i, forGenerics ? 2 : 1)).getType();
            types.add(t);
        }
        return types;
    }

    public AdsTypeDeclaration getCurrentRowType() {
        if (getSelectedRow() != -1) {
            int cols = pModel.getColumnCount();
            AdsTypeDeclaration t = ((CommonParametersEditorCellLib.TypePresentation) pModel.getValueAt(getSelectedRow(), cols - 1)).getType();
            return t;
        }
        return null;
    }

    public LinkedList<AdsTypeDeclaration.TypeArgument.Derivation> getCurrentDerivations() {
        LinkedList<AdsTypeDeclaration.TypeArgument.Derivation> derivations = new LinkedList<AdsTypeDeclaration.TypeArgument.Derivation>();
        for (int i = 0; i <= pModel.getRowCount() - 1; i++) {
            AdsTypeDeclaration.TypeArgument.Derivation d = (AdsTypeDeclaration.TypeArgument.Derivation) pModel.getValueAt(i, 1);
            derivations.add(d);
        }
        return derivations;
    }

    public AdsTypeDeclaration.TypeArgument.Derivation getCurrentRowDerivation() {
        if (getSelectedRow() != -1) {
            AdsTypeDeclaration.TypeArgument.Derivation d = (AdsTypeDeclaration.TypeArgument.Derivation) pModel.getValueAt(getSelectedRow(), 1);
            return d;
        }
        return null;
    }

    public String getCurrentDescription() {
        if (getSelectedRow() != -1) {
            String desc = pModel.getValueAt(getSelectedRow(), 2).toString();
            return desc;
        }
        return null;
    }

    public LinkedList<String> getCurrentDescriptions() {
        LinkedList<String> result = new LinkedList<String>();
        for (int i = 0; i <= pModel.getRowCount() - 1; i++){
            String desc =  pModel.getValueAt(i, 2).toString();
            result.add(desc);
        }
        return result;
    }
    static private String REPEATED_NAMES = NbBundle.getBundle(CommonParametersEditor.class).getString("PP-Repeated");
    static private String UNDEFINED_NAMES = NbBundle.getBundle(CommonParametersEditor.class).getString("PP-Undefined");
    private final StateManager stateManager = new StateManager(this);

    public boolean isComplete() {
        int undefined = checkUndefined();
        if (undefined != -1) {
            stateManager.error(UNDEFINED_NAMES + undefined);
            return false;
        } else {
            String repeated = checkRepeated();
            if (!repeated.isEmpty() && !repeated.equals("?")) {
                stateManager.error(REPEATED_NAMES + repeated);
                return false;
            } else {
                String incorrect = checkIncorrectName();
                if (!incorrect.isEmpty()) {
                    stateManager.error("Incorrect parameter name: " + incorrect);
                    return false;
                } else {
                    stateManager.ok();
                    return true;
                }
            }
        }
    }

    private String checkIncorrectName() {
        if (pModel != null) {
            for (int i = 0; i < pModel.getRowCount(); i++) {
                String strval = pModel.getValueAt(i, 0).toString();
                if (!JavaSignatures.isCorrectJavaIdentifier(strval) ||
                    !strval.matches("(\\w)*")){
                    if (forGenerics && strval.equals("?")){
                       return "";
                    }
                    return strval;
                }
            }
        }
        return "";
    }

    private int checkUndefined() {
        if (pModel != null) {
            int row = nameEditor.getCurrentRow();
            if (nameEditor.getComponent().isShowing()) {
                if (nameEditor.getCellEditorValue().toString().isEmpty()) {
                    return row + 1;
                }
                for (int i = 0; i < pModel.getRowCount(); i++) {
                    if (i != row &&
                            pModel.getValueAt(i, 0).toString().isEmpty()) {
                        return i + 1;
                    }
                }
            } else {
                for (int i = 0; i < pModel.getRowCount(); i++) {
                    if (pModel.getValueAt(i, 0).toString().isEmpty()) {
                        return i + 1;
                    }
                }
            }
        }
        return -1;
    }

    private String checkRepeated() {
        HashSet<String> names = new HashSet<String>();
        if (pModel != null) {
            int row = nameEditor.getCurrentRow();
            if (nameEditor.getComponent().isShowing()) {
                names.add(nameEditor.getCellEditorValue().toString());
                for (int i = 0; i < pModel.getRowCount(); i++) {
                    if (i != row &&
                            !names.add(pModel.getValueAt(i, 0).toString())) {
                        return pModel.getValueAt(i, 0).toString();
                    }
                }
            } else {
                for (int i = 0; i < pModel.getRowCount(); i++) {
                    if (!names.add(pModel.getValueAt(i, 0).toString())) {
                        return pModel.getValueAt(i, 0).toString();
                    }
                }
            }
        }
        return "";
    }
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    private CommonParametersEditorCellLib.ParametersNameChangeSupport parameterNameChangeSupport;

    CommonParametersEditorCellLib.ParametersNameChangeSupport getNameChangeSupport(){
        if (parameterNameChangeSupport == null){
            parameterNameChangeSupport = new CommonParametersEditorCellLib.ParametersNameChangeSupport();
        }
        return parameterNameChangeSupport;
    }

    public void addNameChangeListener(CommonParametersEditorCellLib.ParameterNameChangeListener listener){
        getNameChangeSupport().addEventListener(listener);
    }

    public void removeNameChangeListener(CommonParametersEditorCellLib.ParameterNameChangeListener listener){
        getNameChangeSupport().removeEventListener(listener);
    }
}
