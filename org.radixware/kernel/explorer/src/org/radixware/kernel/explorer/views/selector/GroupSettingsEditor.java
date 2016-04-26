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

package org.radixware.kernel.explorer.views.selector;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.ContextMenuPolicy;
import com.trolltech.qt.core.Qt.KeyboardModifier;
import com.trolltech.qt.gui.QAbstractItemView.DragDropMode;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.models.groupsettings.GroupSettings;
import org.radixware.kernel.common.client.models.groupsettings.IGroupSetting;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;

import org.radixware.kernel.explorer.views.MainWindow;


abstract class GroupSettingsEditor<T extends IGroupSetting> extends MainWindow {

    final private GroupSettings<T> addons;
    final private GroupSettingsTree<T> tree;
    final protected QAction editSettingAction = new QAction(this);
    final protected QAction createSettingAction = new QAction(this);
    final protected QAction createSettingInGroupAction = new QAction(this);
    final protected QAction createSettingGroupAction = new QAction(this);
    final protected QAction inheritSettingAction = new QAction(this);
    final protected QAction removeAction = new QAction(this);
    final protected QAction moveUpAction = new QAction(this);
    final protected QAction moveDownAction = new QAction(this);
    final public Signal1<IGroupSetting> currentSettingChanged = new Signal1<>();
    final public Signal1<IGroupSetting> applySetting = new Signal1<>();
    private QToolButton btnCreateSetting;
    private IClientEnvironment environment;

    protected final IClientEnvironment getEnvironment() {
        return environment;
    }

    public GroupSettingsEditor(final IClientEnvironment environment, final QWidget parent, final GroupSettings<T> addons) {
        super(parent);
        this.environment = environment;
        this.addons = addons;
        final EnumSet<GroupSettingsTree.ShowMode> showMode =
                EnumSet.of(GroupSettingsTree.ShowMode.SHOW_EMPTY_GROUPS, GroupSettingsTree.ShowMode.SHOW_INVALID_SETTINGS);
        tree = new GroupSettingsTree<T>(environment, null/*centralWidget*/, addons, showMode) {

            @Override
            protected QTreeWidgetItem createItemForSetting(QTreeWidgetItem parent, IGroupSetting setting) {
                final QTreeWidgetItem item = super.createItemForSetting(parent, setting);
                if (setting.isValid() && !setting.isUserDefined()) {
                    final QFont font = new QFont(item.font(0));
                    font.setBold(true);
                    item.setFont(0, font);
                }
                return item;
            }
        };
        tree.setObjectName("settingsTree");
        setCentralWidget(tree);
        tree.setDragDropMode(DragDropMode.InternalMove);
        tree.setDragEnabled(true);

        final QToolBar toolbar = addToolBar("settings tool bar");
        toolbar.setObjectName("settingsToolbar");
        toolbar.setFloatable(false);
        toolbar.setMovable(false);

        final ExplorerSettings settings = (ExplorerSettings) getEnvironment().getConfigStore();
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.SELECTOR_GROUP);
        settings.beginGroup(SettingNames.Selector.COMMON_GROUP);
        try {
            final QSize iconSize = settings.readQSize(SettingNames.Selector.Common.ICON_SIZE_IN_SELECTOR_TOOLBARS);
            toolbar.setIconSize(iconSize);
        } finally {
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }

        setupToolBar(toolbar);
    }

    protected void setupToolBar(final QToolBar toolbar) {
        editSettingAction.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.EDIT));
        editSettingAction.setToolTip(getEnvironment().getMessageProvider().translate("SelectorAddons", "Edit"));
        editSettingAction.triggered.connect(this, "editCurrentSetting()");

        setupAction(createSettingAction);
        createSettingAction.triggered.connect(this, "createCustomSetting()");
        toolbar.addAction(createSettingAction);
        btnCreateSetting = (QToolButton) toolbar.widgetForAction(createSettingAction);
        btnCreateSetting.setAutoRaise(true);

        setupAction(createSettingInGroupAction);
        createSettingInGroupAction.triggered.connect(this, "createCustomSettingInGroup()");

        setupAction(createSettingGroupAction);
        createSettingGroupAction.triggered.connect(this, "createSettingGroup()");
        toolbar.addAction(createSettingGroupAction);
        setupAction(inheritSettingAction);
        inheritSettingAction.triggered.connect(this, "inheritSetting()");
        toolbar.addAction(inheritSettingAction);
        setupAction(removeAction);
        removeAction.triggered.connect(this, "removeCurrent()");
        toolbar.addAction(removeAction);
        toolbar.addSeparator();
        setupAction(moveUpAction);
        moveUpAction.triggered.connect(this, "moveUpCurrent()");
        toolbar.addAction(moveUpAction);
        setupAction(moveDownAction);
        moveDownAction.triggered.connect(this, "moveDownCurrent()");
        toolbar.addAction(moveDownAction);

        setContextMenuPolicy(ContextMenuPolicy.NoContextMenu);
        tree.currentItemChanged.connect(this, "currentItemChanged()");
        tree.doubleClicked.connect(editSettingAction, "trigger()");
        setMinimumWidth(toolbar.sizeHint().width());
    }

    @Override
    protected void keyPressEvent(QKeyEvent keyEvent) {
        final int key = keyEvent.key(), modifiers = keyEvent.modifiers().value();
        final int no_modifiers = KeyboardModifier.NoModifier.value(),
                ctrl_modifier = KeyboardModifier.ControlModifier.value(),
                pad_modifier = KeyboardModifier.KeypadModifier.value(),
                ctrl_pad_modifier = ctrl_modifier + pad_modifier;

        if (key == Qt.Key.Key_Return.value() || key == Qt.Key.Key_Enter.value() || key == Qt.Key.Key_F2.value()) {
            if (tree.currentItem() != null && tree.isGroupItem(tree.currentItem())) {
                tree.editItem(tree.currentItem());
            } else {
                editCurrentSetting();
            }
        } else if (createSettingInGroupAction.isEnabled() && ((key == Qt.Key.Key_Insert.value() && modifiers == no_modifiers)
                || (key == Qt.Key.Key_Plus.value() && modifiers == pad_modifier))) {
            createSettingInGroupAction.trigger();
        } else if (keyEvent.matches(QKeySequence.StandardKey.New) && createSettingInGroupAction.isEnabled()) {
            createSettingInGroupAction.trigger();
        } else if (createSettingAction.isEnabled() && ((key == Qt.Key.Key_Insert.value() && modifiers == no_modifiers)
                || (key == Qt.Key.Key_Plus.value() && modifiers == pad_modifier))) {
            createSettingAction.trigger();
        } else if (keyEvent.matches(QKeySequence.StandardKey.New) && createSettingAction.isEnabled()) {
            createSettingAction.trigger();
        } else if (createSettingGroupAction.isEnabled() && (((key == Qt.Key.Key_Insert.value() || key == Qt.Key.Key_G.value()) && modifiers == ctrl_modifier)
                || (key == Qt.Key.Key_Plus.value() && modifiers == ctrl_pad_modifier))) {
            createSettingGroupAction.trigger();
        } else if (key == Qt.Key.Key_I.value() && modifiers == ctrl_modifier && inheritSettingAction.isEnabled()) {
            inheritSettingAction.trigger();
        } else if (removeAction.isEnabled() && ((key == Qt.Key.Key_Delete.value() && modifiers == no_modifiers)
                || (key == Qt.Key.Key_Minus.value() && modifiers == pad_modifier))) {
            removeAction.trigger();
        } else if (keyEvent.matches(QKeySequence.StandardKey.Delete) && removeAction.isEnabled()) {
            removeAction.trigger();
        } else if (moveUpAction.isEnabled() && key == Qt.Key.Key_Up.value() && modifiers == ctrl_modifier) {
            moveUpAction.trigger();
        } else if (moveDownAction.isEnabled() && key == Qt.Key.Key_Down.value() && modifiers == ctrl_modifier) {
            moveDownAction.trigger();
        } else if (tree.currentItem() != null && key == Qt.Key.Key_Up.value() && modifiers == no_modifiers) {
            final QTreeWidgetItem item = tree.itemAbove(tree.currentItem());
            if (item != null) {
                tree.setCurrentItem(item);
            }
        } else if (tree.currentItem() != null && key == Qt.Key.Key_Down.value() && modifiers == no_modifiers) {
            final QTreeWidgetItem item = tree.itemBelow(tree.currentItem());
            if (item != null) {
                tree.setCurrentItem(item);
            }
        } else {
            super.keyPressEvent(keyEvent);
        }
    }

    protected final void refreshTree() {
        tree.refill();        
        if (tree.topLevelItemCount() > 0) {
            tree.setCurrentItem(tree.topLevelItem(0));
        } else {
            refreshActions();
        }
    }

    @SuppressWarnings("unused")
    private void currentItemChanged() {
        refreshActions();
        currentSettingChanged.emit(tree.getCurrentSetting());
    }
    
    public final void setCurrentSetting(T setting){
        tree.setCurrent(setting);
    }

    @SuppressWarnings("unused")
    private void doubleClicked() {
        editCurrentSetting();
    }

    public QIcon getSettingIcon() {
        return tree.getSettingIcon();
    }

    public void setSettingIcon(QIcon addonIcon) {
        tree.setSettingIcon(addonIcon);
    }

    public QIcon getSettingsGroupIcon() {
        return tree.getSettingsGroupIcon();
    }

    public void setSettingsGroupIcon(QIcon icon) {
        tree.setSettingsGroupIcon(icon);
    }

    public String getCustomSettingsGroupTitle() {
        return tree.getCustomSettingsGroupTitle();
    }

    public void setCustomSettingsGroupTitle(String title) {
        tree.setCustomSettingsGroupTitle(title);
    }

    protected abstract void setupAction(QAction action);

    protected abstract String getNewSettingName();

    protected abstract boolean confirmToRemoveSetting(final String name);

    protected abstract boolean confirmToRemoveGroup(final String name);

    public void refreshActions() {

        if (environment.getGroupSettingsStorage().isReadonly()) {
            editSettingAction.setDisabled(true);
            createSettingAction.setDisabled(true);
            createSettingInGroupAction.setDisabled(true);
            createSettingGroupAction.setDisabled(true);
            inheritSettingAction.setDisabled(true);
            removeAction.setDisabled(true);
            moveUpAction.setDisabled(true);
            moveDownAction.setDisabled(true);
        } else {
            createSettingGroupAction.setEnabled(true);
            final IGroupSetting currentSetting = getCurrentSetting();
            final QTreeWidgetItem treeItem = tree.currentItem();
            final boolean isPredefinedSetting = currentSetting != null && !currentSetting.isUserDefined();
            final boolean isCustomSetting = currentSetting != null && currentSetting.isUserDefined();
            final boolean isGroup = treeItem != null && tree.isGroupItem(treeItem);
            btnCreateSetting.removeAction(createSettingAction);
            btnCreateSetting.removeAction(createSettingInGroupAction);

            if (addons.canCreateNew()) {
                createSettingAction.setEnabled(true);
                if (isGroup) {
                    createSettingInGroupAction.setVisible(true);
                    createSettingInGroupAction.setEnabled(true);
                    btnCreateSetting.setDefaultAction(createSettingInGroupAction);
                    btnCreateSetting.addAction(createSettingAction);
                    btnCreateSetting.setAutoRaise(true);
                    btnCreateSetting.setPopupMode(QToolButton.ToolButtonPopupMode.MenuButtonPopup);
                } else {
                    createSettingInGroupAction.setVisible(false);
                    createSettingInGroupAction.setEnabled(false);
                    btnCreateSetting.setDefaultAction(createSettingAction);
                    btnCreateSetting.setPopupMode(QToolButton.ToolButtonPopupMode.InstantPopup);
                }
            } else {
                btnCreateSetting.setDefaultAction(createSettingAction);
                btnCreateSetting.setPopupMode(QToolButton.ToolButtonPopupMode.InstantPopup);
                createSettingAction.setEnabled(false);
                createSettingInGroupAction.setEnabled(false);
            }
            editSettingAction.setEnabled(addons.canCreateNew() && isCustomSetting && currentSetting.isValid());
            inheritSettingAction.setEnabled(addons.canCreateNew() && isPredefinedSetting);
            removeAction.setEnabled(isCustomSetting || isGroup);
            moveUpAction.setEnabled(treeItem != null && !isFirstTreeItem(treeItem));
            moveDownAction.setEnabled(treeItem != null && !isLastTreeItem(treeItem));
        }
    }

    public final IGroupSetting getCurrentSetting() {
        return tree.getCurrentSetting();
    }

    private boolean isFirstTreeItem(final QTreeWidgetItem treeItem) {
        return treeItem != null
                && treeItem.parent() == null
                && tree.indexOfTopLevelItem(treeItem) == 0;
    }

    private boolean isLastTreeItem(final QTreeWidgetItem treeItem) {
        return treeItem != null
                && treeItem.parent() == null
                && tree.indexOfTopLevelItem(treeItem) == tree.topLevelItemCount() - 1;
    }

    public T createCustomSetting() {
        return createCustomSettingImpl(tree.currentItem() == null ? null : tree.currentItem().parent(), null);
    }

    public T createCustomSettingInGroup() {
        final GroupSettings.Group currentGroup = tree.getCurrentGroup();
        final QTreeWidgetItem parentItem;
        if (currentGroup == null) {
            parentItem = null;
        } else if (tree.isGroupItem(tree.currentItem())) {
            parentItem = tree.currentItem();
        } else {
            parentItem = tree.currentItem().parent();
        }
        return createCustomSettingImpl(parentItem, currentGroup);
    }

    private T createCustomSettingImpl(final QTreeWidgetItem parentItem, GroupSettings.Group currentGroup) {
        String settingName = getNewSettingName();
        for (int i = 1; addons.isSettingWithNameExist(settingName); i++) {
            settingName = getNewSettingName() + " " + i;
        }
        final int index;
        if (tree.currentIndex() == null || (parentItem != null && parentItem == tree.currentItem())) {
            index = 0;
        } else {
            index = tree.currentIndex().row() + 1;
        }
        final T setting = addons.create(settingName, null, currentGroup, index, this);
        if (setting != null) {
            final QTreeWidgetItem newItem = tree.createItemForSetting(parentItem, setting);
            insertItemWithIndexCorrection(tree, newItem, index, parentItem);
            if (parentItem==null){
                tree.markPositionChanged(index);
            }
            tree.setCurrentItem(newItem);
            return setting;
        }
        return null;
    }

    protected void addCustomSetting(final T setting) {
        if (setting != null) {
            final GroupSettings.Group currentGroup = tree.getCurrentGroup();
            final int index;
            final QTreeWidgetItem parentItem;
            if (tree.currentIndex() == null || tree.isGroupItem(tree.currentItem())) {
                index = 0;
                parentItem = tree.currentItem();
            } else {
                index = tree.currentIndex().row() + 1;
                parentItem = tree.currentItem().parent();
            }
            addons.add(setting, currentGroup, index);
            final QTreeWidgetItem newItem = tree.createItemForSetting(parentItem, setting);
            insertItemWithIndexCorrection(tree, newItem, index, parentItem);
            if (parentItem==null){
                tree.markPositionChanged(index);
            }            
            tree.setCurrentItem(newItem);
        }
    }

    private static void insertItemWithIndexCorrection(final QTreeWidget tree, final QTreeWidgetItem itemToInsert, final int index, final QTreeWidgetItem parentItem) {
        final int actualIndex;
        if (parentItem == null) {
            tree.insertTopLevelItem(index, itemToInsert);
            actualIndex = tree.indexOfTopLevelItem(itemToInsert);
        } else {
            parentItem.insertChild(index, itemToInsert);
            actualIndex = parentItem.indexOfChild(itemToInsert);
        }
        if (index != actualIndex) {//Qt bug?
            if (parentItem == null) {
                tree.takeTopLevelItem(actualIndex);
                tree.insertTopLevelItem(index, itemToInsert);                
            } else {
                parentItem.takeChild(actualIndex);
                parentItem.insertChild(index, itemToInsert);
            }
        }
    }

    public GroupSettings.Group createSettingGroup() {
        String groupName = getEnvironment().getMessageProvider().translate("SelectorAddons", "New Group");
        for (int i = 1; addons.isGroupExist(groupName); i++) {
            groupName = getEnvironment().getMessageProvider().translate("SelectorAddons", "New Group") + " " + i;
        }
        final int index;
        if (tree.currentIndex() == null) {
            index = 0;
        } else {
            index = tree.currentIndex().parent() == null ? tree.currentIndex().row() + 1 : tree.currentIndex().parent().row() + 1;
        }
        final GroupSettings.Group newGroup = addons.addGroup(groupName);
        final QTreeWidgetItem item = tree.createItemForGroup(null, newGroup);
        insertItemWithIndexCorrection(tree, item, index, null);
        tree.markPositionChanged(index);        
        tree.setCurrentItem(item);
        return newGroup;
    }

    public T inheritSetting() {
        final IGroupSetting currentSetting = tree.getCurrentSetting();
        if (currentSetting != null) {
            String settingName = getNewSettingName();
            for (int i = 1; addons.isSettingWithNameExist(settingName); i++) {
                settingName = getNewSettingName() + " " + i;
            }
            final int index = tree.currentIndex() == null ? 0 : tree.currentIndex().row() + 1;
            final T setting = addons.create(settingName, currentSetting, tree.getCurrentGroup(), index, this);
            if (setting != null) {
                final QTreeWidgetItem parentItem;
                if (tree.getCurrentGroup() == null) {
                    parentItem = null;
                } else {
                    parentItem = tree.currentItem().parent();
                }
                final QTreeWidgetItem newItem = tree.createItemForSetting(parentItem, setting);
                insertItemWithIndexCorrection(tree, newItem, index, parentItem);
                if (parentItem==null){
                    tree.markPositionChanged(index);
                }
                tree.setCurrentItem(newItem);
                return setting;
            }
        }
        return null;
    }

    public boolean removeCurrent() {
        final QTreeWidgetItem curItem = tree.currentItem();
        if (curItem != null) {
            if (tree.isGroupItem(curItem)) {
                final GroupSettings.Group removingGroup = tree.getCurrentGroup();
                if (confirmToRemoveGroup(removingGroup.getName())) {
                    int row = tree.currentIndex().row();
                    final List<QTreeWidgetItem> predefinedChilds = curItem.takeChildren();
                    T setting;
                    for (int i = predefinedChilds.size() - 1; i >= 0; i--) {
                        setting = tree.getSettingForItem(predefinedChilds.get(i));
                        if (setting.isUserDefined()) {
                            addons.remove(setting.getId());
                            predefinedChilds.remove(i);
                        }
                    }
                    tree.takeTopLevelItem(row);
                    addons.removeGroup(removingGroup.getName());
                    row = row == 0 ? 0 : row - 1;
                    tree.insertTopLevelItems(row, predefinedChilds);
                    tree.setCurrentItem(tree.topLevelItem(row));
                    return true;
                }
            } else {
                final IGroupSetting removingSetting = tree.getSettingForItem(curItem);
                if (removingSetting.isUserDefined() && confirmToRemoveSetting(removingSetting.getName())) {
                    final QModelIndex indexAbove = tree.indexAbove(tree.currentIndex());
                    final int row = tree.currentIndex().row();
                    addons.remove(removingSetting.getId());
                    final QTreeWidgetItem parentItem = curItem.parent();
                    tree.removeItemWidget(curItem, 0);
                    if (parentItem != null) {
                        parentItem.takeChild(row);
                    } else {
                        tree.takeTopLevelItem(row);
                    }
                    if (indexAbove != null) {
                        tree.setCurrentIndex(indexAbove);
                    } else {
                        refreshActions();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean moveUpCurrent() {
        final QTreeWidgetItem curItem = tree.currentItem();
        if (curItem != null && !isFirstTreeItem(curItem)) {
            final QTreeWidgetItem parentItem = curItem.parent();
            final int itemRow = tree.currentIndex().row();
            final boolean isExpanded = curItem.isExpanded();
            tree.setUpdatesEnabled(false);
            tree.blockSignals(true);
            try {
                if (parentItem != null) {//элемент внутри группы
                    final int groupRow = tree.indexOfTopLevelItem(parentItem);
                    if (itemRow == 0) {//выходит из группы
                        parentItem.takeChild(0);
                        tree.insertTopLevelItem(groupRow, curItem);
                        tree.markPositionChanged(groupRow);
                    } else {
                        parentItem.takeChild(itemRow);
                        parentItem.insertChild(itemRow - 1, curItem);
                    }
                } else {
                    final QTreeWidgetItem prevItem = tree.topLevelItem(itemRow - 1);
                    tree.takeTopLevelItem(itemRow);
                    if (tree.isGroupItem(prevItem) && !tree.isGroupItem(curItem)) {//Элемент добавляется в группу
                        prevItem.addChild(curItem);
                        prevItem.setExpanded(true);
                    } else {
                        tree.insertTopLevelItem(itemRow - 1, curItem);
                        tree.markPositionChanged(itemRow);
                    }
                }
            } finally {
                tree.blockSignals(false);
                tree.setUpdatesEnabled(true);
            }
            tree.setCurrentItem(curItem);
            if (isExpanded) {
                curItem.setExpanded(true);
            }
            return true;
        }
        return false;
    }

    public boolean moveDownCurrent() {
        final QTreeWidgetItem curItem = tree.currentItem();
        if (curItem != null && !isLastTreeItem(curItem)) {
            final QTreeWidgetItem parentItem = curItem.parent();
            final int itemRow = tree.currentIndex().row();
            final boolean isExpanded = curItem.isExpanded();
            tree.setUpdatesEnabled(false);
            tree.blockSignals(true);
            try {
                if (parentItem != null) {//элемент внутри группы
                    final int groupRow = tree.indexOfTopLevelItem(parentItem);
                    if (itemRow == parentItem.childCount() - 1) {//выходит из группы
                        parentItem.takeChild(itemRow);
                        tree.insertTopLevelItem(groupRow + 1, curItem);
                        tree.markPositionChanged(groupRow + 1);
                    } else {
                        parentItem.takeChild(itemRow);
                        parentItem.insertChild(itemRow + 1, curItem);
                    }
                } else {
                    final QTreeWidgetItem nextItem = tree.topLevelItem(itemRow + 1);
                    tree.takeTopLevelItem(itemRow);
                    if (tree.isGroupItem(nextItem) && !tree.isGroupItem(curItem)) {//Элемент добавляется в группу
                        nextItem.insertChild(0, curItem);
                        nextItem.setExpanded(true);
                    } else {
                        tree.insertTopLevelItem(itemRow + 1, curItem);
                        tree.markPositionChanged(itemRow+1);
                    }
                }
            } finally {
                tree.blockSignals(false);
                tree.setUpdatesEnabled(true);
            }
            tree.setCurrentItem(curItem);
            if (isExpanded) {
                curItem.setExpanded(true);
            }
            return true;
        }
        return false;
    }
    
    public boolean canApplySetting(final IGroupSetting setting){
        return setting!=null;
    }

    public void editCurrentSetting() {
        if (editSettingAction.isEnabled()) {
            final IGroupSetting setting = tree.getCurrentSetting();
            final String oldSettingName = setting.getName();            
            final Collection<String> existingSettings = new LinkedList<>(addons.getAllSettingTitles());
            existingSettings.remove(oldSettingName);            
            final DialogResult result = setting.openEditor(environment, (IWidget) this, existingSettings, true);
            if (result != DialogResult.REJECTED) {
                if (!oldSettingName.equals(setting.getName())) {
                    tree.currentItem().setText(0, setting.getName());
                }
                currentSettingChanged.emit(setting);
                if (result == DialogResult.APPLY) {
                    applySetting.emit(setting);
                }
            }
        }
    }

    @Override
    protected void closeEvent(QCloseEvent closeEvent) {
        tree.close();
        super.closeEvent(closeEvent);
    }
}
