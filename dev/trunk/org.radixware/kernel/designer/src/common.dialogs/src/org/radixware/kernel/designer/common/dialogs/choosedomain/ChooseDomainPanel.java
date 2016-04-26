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

package org.radixware.kernel.designer.common.dialogs.choosedomain;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Set;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXTable.BooleanEditor;
import org.jdesktop.swingx.JXTreeTable;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.common.types.Id;


public class ChooseDomainPanel extends JPanel {

    private boolean isMultipleSelectionAllowed;
    private boolean fillValuesFromContext;
    private RadixObject context;
    private JXTreeTable treeTable;

    public ChooseDomainPanel(RadixObject context, boolean isMultipleSelectionAllowed, boolean fillValuesFromContext, Action onEnter) {
        this.isMultipleSelectionAllowed = isMultipleSelectionAllowed;
        this.fillValuesFromContext = fillValuesFromContext;
        this.context = context;
        initTreeTable();
        setLayout(new BorderLayout());
        JScrollPane jsp = new JScrollPane(treeTable);
        add(jsp, BorderLayout.CENTER);
        setMinimumSize(new Dimension(300, 400));
        setPreferredSize(new Dimension(300, 400));
        treeTable.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), onEnter);
        if (onEnter != null) {
            treeTable.getActionMap().put(onEnter, onEnter);
        }
    }

    private void initTreeTable() {
        treeTable = new JXTreeTable();
        treeTable.setRootVisible(false);
        treeTable.setShowsRootHandles(true);
        treeTable.setTreeCellRenderer(new DomainsTreeCellRenderer());
        treeTable.setDefaultEditor(Boolean.class, new BooleanEditor());
        treeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        treeTable.setToggleClickCount(0);
        treeTable.setFillsViewportHeight(true);
        treeTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                TreePath path = treeTable.getPathForLocation(e.getX(), e.getY());
                if (path != null) {
                    int row = treeTable.getRowForPath(path);
                    treeTable.getSelectionModel().setLeadSelectionIndex(row);
                    if (e.getClickCount() == 2) {
                        Component c = treeTable.getComponentAt(e.getX(), e.getY());
                        if (treeTable.isExpanded(path)) {
                            treeTable.collapsePath(path);
                        } else {
                            treeTable.expandPath(path);
                        }
                    }
                }
            }
        });
        resetTreeTable();
    }

    private void resetTreeTable() {
        treeTable.setTreeTableModel(new AdsDefinitionDomainsTreeTableModel(context, isMultipleSelectionAllowed, fillValuesFromContext));
        if (isMultipleSelectionAllowed) {
            final TableColumn checkboxesColumn = treeTable.getColumnModel().getColumn(AdsDefinitionDomainsTreeTableModel.CHECKBOX_COLUMN_INDEX);
            checkboxesColumn.setPreferredWidth(60);
            checkboxesColumn.setMaxWidth(60);
            checkboxesColumn.setMinWidth(60);
        }
        treeTable.expandAll();
    }

    public Set<AdsDomainDef> getSelectedDomains() {
        if (treeTable.getCellEditor() != null) {
            treeTable.getCellEditor().stopCellEditing();
        }
        if (isMultipleSelectionAllowed) {
            final Set<AdsDomainDef> selectedDomains = ((AdsDefinitionDomainsTreeTableModel) treeTable.getTreeTableModel()).getSelectedDomains();
            return selectedDomains;
        } else {
            int selectedRow = treeTable.getSelectedRow();
            AdsDomainDef domainDef = (AdsDomainDef) treeTable.getModel().getValueAt(selectedRow, 0);
            return Collections.singleton(domainDef);
        }
    }

    public Set<Id> getSelectedDomainIds() {
        if (treeTable.getCellEditor() != null) {
            treeTable.getCellEditor().stopCellEditing();
        }
        if (isMultipleSelectionAllowed) {
            final Set<Id> selectedDomains = ((AdsDefinitionDomainsTreeTableModel) treeTable.getTreeTableModel()).getSelectedDomainIds();
            return selectedDomains;
        } else {
            int selectedRow = treeTable.getSelectedRow();
            Object obj = (AdsDomainDef) treeTable.getModel().getValueAt(selectedRow, 0);
            if (obj instanceof AdsDomainDef) {
                return Collections.singleton(((AdsDomainDef) obj).getId());
            } else if (obj instanceof Id) {
                return Collections.singleton((Id) obj);
            } else {
                throw new IllegalStateException("Can not obtain value from model");
            }
        }
    }

    public void setReadOnly(boolean readOnly) {
        if (treeTable.getTreeTableModel() instanceof AdsDefinitionDomainsTreeTableModel) {
            AdsDefinitionDomainsTreeTableModel model = (AdsDefinitionDomainsTreeTableModel) treeTable.getTreeTableModel();
            model.setReadOnly(readOnly);
        }
    }

    public boolean isReadOnly() {
        if (treeTable.getTreeTableModel() instanceof AdsDefinitionDomainsTreeTableModel) {
            AdsDefinitionDomainsTreeTableModel model = (AdsDefinitionDomainsTreeTableModel) treeTable.getTreeTableModel();
            return model.isReadOnly();
        }
        return false;
    }
}
