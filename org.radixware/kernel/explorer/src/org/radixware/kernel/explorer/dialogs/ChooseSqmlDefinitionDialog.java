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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.CaseSensitivity;
import com.trolltech.qt.core.Qt.SortOrder;
import com.trolltech.qt.gui.QAbstractItemView.ScrollHint;
import com.trolltech.qt.gui.QAbstractItemView.SelectionMode;
import com.trolltech.qt.gui.*;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.IChooseDefFromList;
import org.radixware.kernel.explorer.models.SqmlTreeModel;


public final class ChooseSqmlDefinitionDialog extends ExplorerDialog implements IChooseDefFromList {

    private final QTreeView tree = new QTreeView(this);
    private final boolean isReadOnly;
    private final SqmlTreeModelProxy proxyModel;

    /**
     *@param definitionsModel - Уже заполненная модель с настроенным SqmlTreeModel.ItemType и EDisplayMode
     *@param filter - Дополнительные условия на отображаемые дефиниции.
     */
    public ChooseSqmlDefinitionDialog(final IClientEnvironment environment, final SqmlTreeModel definitionsModel, final ISqmlDefinitionsFilter filter, final List<ISqmlDefinition> currentItemPath, final boolean isReadOnly, final QWidget parent) {
        super(environment, parent, "ChooseSqmlDefinitionDialog");

        proxyModel = new SqmlTreeModelProxy(definitionsModel, filter);
        proxyModel.setFilterCaseSensitivity(CaseSensitivity.CaseInsensitive);
        proxyModel.setFilterKeyColumn(0);
        proxyModel.setFilterRole(SqmlTreeModel.FILTER_ROLE);
        proxyModel.sort(0, SortOrder.AscendingOrder);

        tree.setObjectName("tree");
        tree.setItemDelegate(new QItemDelegate(tree));
        //SqmlDefinitionsTreeItemDelegate delegate = new SqmlDefinitionsTreeItemDelegate();
        //tree.setItemDelegateForColumn(0, delegate);
        tree.setIconSize(new QSize(20, 20));
        tree.setModel(proxyModel);
        tree.setHeaderHidden(true);
        if (isReadOnly) {
            tree.setSelectionMode(SelectionMode.NoSelection);
        }
        this.isReadOnly = isReadOnly;

        setupUi();
        setCurrentItemPath(currentItemPath);
        if (tree.currentIndex() == null && proxyModel.index(0, 0) != null) {
            tree.setCurrentIndex(proxyModel.index(0, 0));
        }
        setCurItem(tree.currentIndex());
    }

    public ChooseSqmlDefinitionDialog(final IClientEnvironment environment, final SqmlTreeModel definitionsModel, final List<ISqmlDefinition> currentItemPath, final boolean isReadOnly, final QWidget parent) {
        this(environment, definitionsModel, null, currentItemPath, isReadOnly, parent);
    }

    private void setupUi() {
        this.setMinimumSize(600, 400);

        tree.activated.connect(this, "onItemClick(QModelIndex)");
        tree.clicked.connect(this, "onItemClick(QModelIndex)");
        tree.doubleClicked.connect(this, "onItemDoubleClick(QModelIndex)");
        tree.expanded.connect(this, "onItemExpand(QModelIndex)");
        tree.collapsed.connect(this, "onItemCollapsed()");


        final QVBoxLayout tabLayout = new QVBoxLayout();
        final QLabel label = new QLabel(getEnvironment().getMessageProvider().translate("SqmlEditor", "Find") + ":");
        label.setObjectName("label");
        label.setEnabled(!isReadOnly);
        final QLineEdit edit = new QLineEdit() {
            @Override
            protected void keyPressEvent(final QKeyEvent qke) {
                if (qke.key() == Qt.Key.Key_Up.value() || qke.key() == Qt.Key.Key_Down.value() || qke.key() == Qt.Key.Key_Left.value() || qke.key() == Qt.Key.Key_Right.value()) {
                    QApplication.postEvent(tree, new QKeyEvent(qke.type(), qke.key(), qke.modifiers()));
                } else if (qke.key() == Qt.Key.Key_Return.value()) {
                    if (tree.currentIndex() != null) {
                        onItemDoubleClick(tree.currentIndex());
                    }
                } else {
                    super.keyPressEvent(qke);
                }
            }
        };
        edit.setObjectName("edit");
        edit.textChanged.connect(this, "onTextChange(String)");
        edit.setEnabled(!isReadOnly);
        
        final QHBoxLayout layout = new QHBoxLayout();
        layout.addWidget(label);
        layout.addWidget(edit);

        tabLayout.addLayout(layout);
        tabLayout.addWidget(tree);
        dialogLayout().addLayout(tabLayout);
        final EnumSet<EDialogButtonType> buttons;
        if (isReadOnly) {
            buttons = EnumSet.of(EDialogButtonType.CLOSE);
        } else {
            buttons = EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL);
        }
        addButtons(buttons, true);
        if (!isReadOnly){
            edit.setFocus();
        }
    }
    
    private void setCurrentItemPath(final List<ISqmlDefinition> definitions) {
        final SqmlTreeModel sourceModel = (SqmlTreeModel) proxyModel.sourceModel();
        QModelIndex index = null;
        if (definitions != null) {
            for (ISqmlDefinition definition : definitions) {
                if (definition != null) {
                    index = sourceModel.findDefinitionIndex(definition, index);
                }
                if (index == null) {
                    break;
                }
                tree.expand(proxyModel.mapFromSource(index));
            }
        }
        proxyModel.invalidate();
        index = index == null ? proxyModel.index(0, 0) : proxyModel.mapFromSource(index);
        if (index != null) {
            tree.setCurrentIndex(index);
            tree.scrollTo(index, ScrollHint.PositionAtCenter);
        }else if (proxyModel.index(0, 0) != null) {
            tree.setCurrentIndex(proxyModel.index(0, 0));
            setCurItem(proxyModel.index(0, 0));
        }else{
            setCurItem(null);
        }        
        tree.resizeColumnToContents(0);
        tree.update();
    }

    private boolean isCurrentItemAcceptable() {
        final SqmlTreeModel treeModel = (SqmlTreeModel) proxyModel.sourceModel();
        final EnumSet<SqmlTreeModel.ItemType> itemTypes = treeModel.getVisibleDefinitionTypes();
        final ISqmlDefinition currentDefinition = getCurrentItem();
        if (currentDefinition instanceof ISqmlTableDef) {
            return !itemTypes.contains(SqmlTreeModel.ItemType.PROPERTY)
                    && !itemTypes.contains(SqmlTreeModel.ItemType.ENUIM_ITEM)
                    && !itemTypes.contains(SqmlTreeModel.ItemType.INDEX);
        } else if (currentDefinition instanceof ISqmlEnumDef) {
            return !itemTypes.contains(SqmlTreeModel.ItemType.ENUIM_ITEM);
        } else if (currentDefinition instanceof ISqmlPackageDef){
            return !itemTypes.contains(SqmlTreeModel.ItemType.FUNCTION);
        } else {
            return currentDefinition != null;
        }
    }

    @Override
    public void onItemClick(final QModelIndex modelIndex) {
        setCurItem(modelIndex);
    }

    @Override
    public void onItemDoubleClick(final QModelIndex modelIndex) {
        if (setCurItem(modelIndex)) {
            accept();
        }
    }

    public void onItemExpand(final QModelIndex modelIndex) {
        tree.update(modelIndex);
        tree.resizeColumnToContents(0);
    }

    @SuppressWarnings("unused")
    private void onItemCollapsed() {
        tree.resizeColumnToContents(0);
    }

    @SuppressWarnings("unused")
    private void onTextChange(final String text) {
        proxyModel.setFilterCaseSensitivity(CaseSensitivity.CaseInsensitive);
        proxyModel.invalidate();
        proxyModel.setFilterWildcard(text);
        if (proxyModel.index(0, 0) != null) {
            tree.setCurrentIndex(proxyModel.index(0, 0));
            setCurItem(proxyModel.index(0, 0));
        }else if (getButton(EDialogButtonType.OK)!=null){
            getButton(EDialogButtonType.OK).setEnabled(false);
        }
    }

    @Override
    public boolean setCurItem(final QModelIndex modelIndex) {
        if (isReadOnly) {
            return false;
        }
        final boolean isAcceptable = isCurrentItemAcceptable();
        getButton(EDialogButtonType.OK).setEnabled(isAcceptable);
        return isAcceptable;
    }

    public ISqmlDefinition getCurrentItem() {
        final SqmlTreeModel treeModel = (SqmlTreeModel) proxyModel.sourceModel();
        return treeModel.definition(proxyModel.mapToSource(tree.currentIndex()));
    }

    @Override
    public boolean setSelectedDefinition(final AdsDefinition def) {
        return false;
    }
}