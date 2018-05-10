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

package org.radixware.kernel.explorer.editors.sqmleditor.tageditors;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QBoxLayout;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QSortFilterProxyModel;
import com.trolltech.qt.gui.QStandardItem;
import com.trolltech.qt.gui.QStandardItemModel;
import com.trolltech.qt.gui.QTreeView;
import com.trolltech.qt.gui.QWidget;
import java.util.*;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEventCodeDef;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class EventCodeChoiceDialog extends ExplorerDialog {

    private static class EventCodeProxyModel extends QSortFilterProxyModel {

        public EventCodeProxyModel(final QAbstractItemModel sourceModel) {
            super();
            setSourceModel(sourceModel);
            setFilterCaseSensitivity(Qt.CaseSensitivity.CaseInsensitive);
        }

        @Override
        protected boolean filterAcceptsRow(final int sourceRow, final QModelIndex sourceParent) {
            final QModelIndex idx = sourceModel().index(sourceRow, 0, sourceParent);
            if (sourceModel().hasChildren(idx)) {
                return true;
            }

            return super.filterAcceptsRow(sourceRow, sourceParent);
        }

        public ISqmlEventCodeDef definition(final QModelIndex index) {
            return (ISqmlEventCodeDef) mapToSource(index).data(Qt.ItemDataRole.UserRole);
        }
    }
    private final static int COL = 0;
    private final static int LIMIT = 100;
    private final Collection<ISqmlEventCodeDef> eventCodeDefs;
    private ISqmlDefinition selectedEventCode = null;
    private QTreeView codesList = null;
    private EventCodeProxyModel proxyModel;

    public EventCodeChoiceDialog(final IClientEnvironment env, final QWidget parent, final Collection<ISqmlEventCodeDef> eventCodeDefs) {
        super(env, parent);
        this.eventCodeDefs = eventCodeDefs;
        setWindowTitle(env.getMessageProvider().translate("EventCodeChooseDialog", "Select Event Code"));
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.EVENT_CODE));
        setUpUi();
    }

    private void setUpUi() {
        // Create search line
        final QHBoxLayout searchBox = new QHBoxLayout();
        searchBox.setMargin(0);
        final QLabel searchLabel = new QLabel(getEnvironment().getMessageProvider().translate("EventCodeChooseDialog", "Find")+":", (QWidget)this);
        searchBox.addWidget(searchLabel);
        final ValStrEditor search = new ValStrEditor(getEnvironment(), (QWidget) this, new EditMaskStr(), false, false);
        search.valueChanged.connect(this, "onSearch(Object)");
        searchBox.addWidget(search);
        ((QBoxLayout)layout()).addLayout(searchBox);

        // Create and fill tree
        final String noSource = "[" + getEnvironment().getMessageProvider().translate("EventCodeChooseDialog", "No source") + "]";
        final Map<String, List<ISqmlEventCodeDef>> defsUnderCategories = new HashMap<>();
        final QStandardItemModel treeModel = new QStandardItemModel();

        codesList = new QTreeView();
        codesList.header().setVisible(false);
        codesList.header().setStretchLastSection(true);
        codesList.setExpandsOnDoubleClick(false);
        codesList.setSortingEnabled(true);
        for (ISqmlEventCodeDef e : eventCodeDefs) {
            if (e.getEventCode().isEmpty()) {
                continue;
            }
            final String eventSource = e.getEventSource() == null ? noSource : e.getEventSource();
            List<ISqmlEventCodeDef> currentList = defsUnderCategories.get(eventSource);
            if (currentList == null) {
                currentList = new LinkedList<>();
                defsUnderCategories.put(eventSource, currentList);
            }
            currentList.add(e);
        }

        final List<QStandardItem> sources = new LinkedList<>();
        for (String s : defsUnderCategories.keySet()) {
            final QStandardItem sourceItem = new QStandardItem();
            sourceItem.setData(s, Qt.ItemDataRole.DisplayRole);
            sources.add(sourceItem);
        }

        for (QStandardItem source : sources) {

            final List<ISqmlEventCodeDef> eventCodes = defsUnderCategories.get((String) source.data(Qt.ItemDataRole.DisplayRole));
            for (int i = 0; i < eventCodes.size(); i++) {
                final ISqmlEventCodeDef eventCodeDef = eventCodes.get(i);
                final QStandardItem item = new QStandardItem();
                item.setData(toPlainText(eventCodeDef.getEventCode()), Qt.ItemDataRole.DisplayRole);
                item.setData(eventCodeDef, Qt.ItemDataRole.UserRole);
                final ClientIcon icon = ExplorerIcon.TraceLevel.getIconBySeverity(eventCodeDef.getEventSeverity());
                if(icon != null) {
                    item.setIcon(ExplorerIcon.getQIcon(icon));
                }
                source.setChild(i, COL, item);
            }
        }
        treeModel.insertColumn(COL, sources);
        treeModel.setColumnCount(1);
        proxyModel = new EventCodeProxyModel(treeModel);
        proxyModel.setFilterKeyColumn(0);
        codesList.setModel(proxyModel);
        codesList.sortByColumn(COL, Qt.SortOrder.AscendingOrder);
        codesList.clicked.connect(this, "onClick(QModelIndex)");
        codesList.doubleClicked.connect(this, "onDoubleClick(QModelIndex)");
        layout().addWidget(codesList);
        // Create dialog buttons
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
    }

    public ISqmlDefinition getSelectedEventCode() {
        return selectedEventCode;
    }

    @SuppressWarnings("unused")
    private void onClick(final QModelIndex idx) {
        if (codesList.model().hasChildren(idx)) {
            this.selectedEventCode = null;
        } else {
            this.selectedEventCode = proxyModel.definition(idx);
        }
    }

    @SuppressWarnings("unused")
    private void onDoubleClick(final QModelIndex idx) {
        if (codesList.model().hasChildren(idx)) {
            final boolean isExpanded = codesList.isExpanded(idx);
            if (isExpanded) {
                codesList.collapse(idx);
            } else {
                codesList.expand(idx);
            }
        } else {
            this.selectedEventCode = proxyModel.definition(idx);
            accept();
        }
    }

    private String toPlainText(final String string) {
        final String newString = string.replace("\n", " ");
        if (LIMIT <= newString.length()) {
            return newString.substring(0, LIMIT - 1).concat("...");
        } else {
            return newString;
        }

    }

    @SuppressWarnings("unused")
    private void onSearch(final Object val) {
        // Restoring hidden rows, because each new edit may be contained by any row
        for (int i = 0; i < codesList.model().rowCount(); i++) {
            if (codesList.isRowHidden(i, codesList.rootIndex())) {
                codesList.setRowHidden(i, codesList.rootIndex(), false);
            }
        }
        // Filtering data
        final String value = (String) val;
        proxyModel.setFilterWildcard(value);
        if (value.isEmpty()) {
            codesList.collapseAll();
        } else {
            // Hiding the rows which are empty after filtering
            for (int i = 0; i < codesList.model().rowCount(); i++) {
                final QModelIndex idx = codesList.model().index(i, COL, codesList.rootIndex());
                if (!codesList.model().hasChildren(idx)) {
                    codesList.setRowHidden(i, codesList.rootIndex(), true);
                }
            }
            codesList.expandAll();
        }
    }
}
