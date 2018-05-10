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

package org.radixware.kernel.explorer.editors.filterparameditor;

import org.radixware.kernel.explorer.dialogs.*;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QRegExp;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.CaseSensitivity;
import com.trolltech.qt.core.Qt.SortOrder;
import com.trolltech.qt.gui.QAbstractItemView.ScrollHint;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QSortFilterProxyModel;
import com.trolltech.qt.gui.QTreeView;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlSelectorPresentationDef;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;


final class ChooseSelectorPresentationDialog extends ExplorerDialog {

    private final QTreeView tree = new QTreeView(this);
    private final ChooseSelectorPresentationModel model;
    private final QSortFilterProxyModel proxyModel = new QSortFilterProxyModel();

    public ChooseSelectorPresentationDialog(final IClientEnvironment environment, final Id classId, final Id presentationId, final QWidget parent) {
        super(environment, parent, "ChooseSqmlDefinitionDialog");

        model = new ChooseSelectorPresentationModel(environment);

        proxyModel.setSourceModel(model);
        proxyModel.setFilterCaseSensitivity(CaseSensitivity.CaseInsensitive);
        proxyModel.setFilterKeyColumn(0);
        proxyModel.setFilterRole(ChooseSelectorPresentationModel.FILTER_ROLE);
        proxyModel.sort(0, SortOrder.AscendingOrder);

        tree.setObjectName("tree");
        tree.setIconSize(new QSize(20, 20));
        tree.setModel(proxyModel);
        tree.setHeaderHidden(true);
        tree.resizeColumnToContents(0);

        setupUi();
        if (classId != null && presentationId != null) {
            findCurrentPresentation(classId, presentationId);
        }
    }

    protected void setupUi() {
        this.setMinimumSize(600, 400);

        tree.activated.connect(this, "onItemClick(QModelIndex)");
        tree.clicked.connect(this, "onItemClick(QModelIndex)");
        tree.doubleClicked.connect(this, "onItemDoubleClick(QModelIndex)");
        tree.expanded.connect(this, "onItemExpand(QModelIndex)");
        tree.collapsed.connect(this, "onItemCollapsed()");

        final QVBoxLayout tabLayout = new QVBoxLayout();
        final QLabel label = new QLabel(Application.translate("SqmlEditor", "Find") + " : ");
        label.setObjectName("label");
        final QLineEdit edit = new QLineEdit();
        edit.setObjectName("edit");
        edit.textChanged.connect(this, "onTextChange(String)");
        final QHBoxLayout layout = new QHBoxLayout();
        layout.addWidget(label);
        layout.addWidget(edit);

        tabLayout.addLayout(layout);
        tabLayout.addWidget(tree);
        dialogLayout().addLayout(tabLayout);
        
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
        if (proxyModel.index(0, 0) != null) {
            setCurItem(proxyModel.index(0, 0));
        }else{
            setCurItem(null);
        }
        
        setWindowTitle(Application.translate("SqmlEditor", "Selector Presentation"));
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.SELECTOR));
    }

    private void findCurrentPresentation(final Id classId, final Id presentationId) {
        final int classesCount = model.rowCount();
        boolean classFound = false;
        QModelIndex index = null, childIndex;
        for (int row = 0; row < classesCount; row++) {
            index = model.index(row, 0);
            if (model.definition(index).getId().equals(classId)) {
                final QModelIndex classIndex = proxyModel.mapFromSource(index);
                tree.expand(classIndex);
                classFound = true;
                setCurItem(classIndex);
                tree.scrollTo(classIndex, ScrollHint.PositionAtCenter);
                break;
            }
        }
        if (classFound) {
            final int presentationsCount = model.rowCount(index);
            for (int row = 0; row < presentationsCount; row++) {
                childIndex = model.index(row, 0, index);
                if (model.definition(childIndex).getId().equals(presentationId)) {
                    final QModelIndex presentationIndex = proxyModel.mapFromSource(childIndex);
                    tree.expand(presentationIndex);
                    setCurItem(presentationIndex);
                    tree.scrollTo(presentationIndex, ScrollHint.PositionAtCenter);
                    break;
                }
            }
            proxyModel.invalidate();
            tree.resizeColumnToContents(0);
            tree.update();
        }
    }

    private boolean isCurrentItemAcceptable() {
        return getCurrentItem() != null;
    }

    @SuppressWarnings("unused")
    private void onItemClick(final QModelIndex modelIndex) {
        setCurItem(modelIndex);
    }

    @SuppressWarnings("unused")
    private void onItemDoubleClick(final QModelIndex modelIndex) {
        if (setCurItem(modelIndex)) {
            accept();
        }
    }

    @SuppressWarnings("unused")
    private void onItemExpand(final QModelIndex modelIndex) {
        tree.update(modelIndex);
        tree.resizeColumnToContents(0);
    }

    @SuppressWarnings("unused")
    private void onItemCollapsed() {
        tree.resizeColumnToContents(0);
    }

    @SuppressWarnings("unused")
    private void onTextChange(final String text) {
        final QRegExp exp = new QRegExp();
        exp.setPattern(text);
        proxyModel.setFilterCaseSensitivity(CaseSensitivity.CaseInsensitive);
        proxyModel.invalidate();
        proxyModel.setFilterWildcard(text);
        if (proxyModel.index(0, 0) != null) {
            setCurItem(proxyModel.index(0, 0));
        }
    }

    private boolean setCurItem(final QModelIndex index) {
        tree.setCurrentIndex(index);
        final boolean isAcceptable = isCurrentItemAcceptable();
        getButton(EDialogButtonType.OK).setEnabled(isAcceptable);
        return isAcceptable;
    }

    public ISqmlSelectorPresentationDef getCurrentItem() {
        return model.presentation(proxyModel.mapToSource(tree.currentIndex()));
    }
}