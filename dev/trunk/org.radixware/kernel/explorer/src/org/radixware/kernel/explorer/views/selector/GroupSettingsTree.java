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
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QDragMoveEvent;
import com.trolltech.qt.gui.QDropEvent;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.groupsettings.GroupSettings;
import org.radixware.kernel.common.client.models.groupsettings.IGroupSetting;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.explorer.env.ImageManager;


public class GroupSettingsTree<T extends IGroupSetting> extends QTreeWidget {   

    private static final class GroupSettingsDelegate extends QItemDelegate {

        final private static String SEPARATOR_ITEM_TYPE = "separator";
        final private static int ITEM_MARGIN = 2;
        final int lineHeight;        
        final QAbstractItemView ownerView;

        public GroupSettingsDelegate(final int separatorHeight, final QAbstractItemView owner) {
            super(owner);
            this.lineHeight = separatorHeight;
            ownerView = owner;
        }

        public static QTreeWidgetItem createSeparatorItem(final QTreeWidget tree, final String text) {
            final QTreeWidgetItem separatorItem = new QTreeWidgetItem(tree);
            separatorItem.setText(0, text);
            separatorItem.setData(0, Qt.ItemDataRole.AccessibleDescriptionRole, SEPARATOR_ITEM_TYPE);
            final QFont font = new QFont(separatorItem.font(0));
            font.setItalic(true);
            font.setPointSize(font.pointSize() - 1);
            separatorItem.setFont(0, font);
            separatorItem.setFlags(new Qt.ItemFlags(Qt.ItemFlag.NoItemFlags));
            return separatorItem;
        }

        private static boolean isSeparator(final QModelIndex index) {
            return SEPARATOR_ITEM_TYPE.equals(index.data(Qt.ItemDataRole.AccessibleDescriptionRole));
        }

        @Override
        public void paint(QPainter painter, QStyleOptionViewItem style, QModelIndex index) {
            if (isSeparator(index)) {
                final QRect separatorRect = style.rect().clone();
                final QRect itemRect = style.rect().clone();
                itemRect.setHeight(itemRect.height() - lineHeight - ITEM_MARGIN);
                style.setRect(itemRect);
                super.paint(painter, style, index);//paint item text as usual
                //paint separator line
                separatorRect.setTop(separatorRect.top() + itemRect.height());
                separatorRect.setHeight(lineHeight);
                separatorRect.setWidth(ownerView.viewport().width());
                style.setRect(separatorRect);
                ownerView.style().drawPrimitive(QStyle.PrimitiveElement.PE_IndicatorToolBarSeparator, style, painter, ownerView);
            } else {
                super.paint(painter, style, index);
            }
        }

        @Override
        public QSize sizeHint(QStyleOptionViewItem style, QModelIndex index) {
            final QSize sizeHint = super.sizeHint(style, index);
            if (isSeparator(index)) {
                sizeHint.setHeight(sizeHint.height() + lineHeight + ITEM_MARGIN);
            }
            return sizeHint;
        }
    };
    
    private final static Qt.ItemFlags SETTING_ITEM_FLAGS =
            new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsDragEnabled);
    private final static Qt.ItemFlags SETTING_GROUP_FLAGS =
            new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsEditable, Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsDragEnabled, Qt.ItemFlag.ItemIsDropEnabled);
    private final static String SETTING_ITEM_DESCRIBTION = "setting";
    private final static String SETTING_GROUP_DESCRIBTION = "group";
    private int rowPositionChangedFrom = -1;
    
    public static enum ShowMode {

        SHOW_LAST_USED, SHOW_EMPTY_GROUPS, SHOW_INVALID_SETTINGS
    };
    private static final QBrush LAST_USED_FOREGROUND = new QBrush(QColor.darkMagenta);
    private static final QBrush INVALID_SETTING_FOREGROUND = new QBrush(QColor.red);
    private static final QBrush SERVICE_ITEM_FOREGROUND = new QBrush(QColor.darkBlue);
    private static final int MIN_SETTINGS_COUNT = 5;
    private final EnumSet<ShowMode> showMode;
    final public GroupSettings<T> addons;
    protected QIcon addonIcon, groupIcon;
    private boolean wasModified;

    public QIcon getSettingIcon() {
        return addonIcon;
    }

    public void setSettingIcon(final QIcon addonIcon) {
        this.addonIcon = addonIcon;
    }

    public QIcon getSettingsGroupIcon() {
        return groupIcon;
    }

    public void setSettingsGroupIcon(final QIcon icon) {
        this.groupIcon = icon;
    }
    
    private String customSettingsGroupTitle;

    public String getCustomSettingsGroupTitle() {
        return customSettingsGroupTitle;
    }

    public void setCustomSettingsGroupTitle(final String title) {
        customSettingsGroupTitle = title;
    }
    private final IClientEnvironment environment;

    @SuppressWarnings("LeakingThisInConstructor")
    public GroupSettingsTree(final IClientEnvironment environment, final QWidget parent, final GroupSettings<T> addons, final EnumSet<ShowMode> mode) {
        super(parent);        
        this.environment = environment;
        this.addons = addons;        
        header().setVisible(false);
        setMinimumHeight(128);
        setSortingEnabled(false);
        showMode = mode == null ? EnumSet.allOf(ShowMode.class) : EnumSet.copyOf(mode);
        customSettingsGroupTitle = environment.getMessageProvider().translate("SelectorAddons", "User settings");
        final int separatorHeight;
        if (parent==null){
            separatorHeight = style().pixelMetric(QStyle.PixelMetric.PM_DefaultFrameWidth, null, this);
        }else{
            separatorHeight = parent.style().pixelMetric(QStyle.PixelMetric.PM_DefaultFrameWidth, null, parent);
        }        
        setItemDelegate(new GroupSettingsDelegate(separatorHeight, this));
    }

    public void refill() {
        blockSignals(true);
        try {
            clear();
            int numberOfVisibleSettings = 0;
            for (GroupSettings.SettingItem settingItem: addons.getItemsByOrder()){
                if (settingItem.settingId!=null){
                    final T setting = addons.findById(settingItem.settingId);
                    if (setting!=null && settingIsVisible(setting)){
                        numberOfVisibleSettings++;
                    }
                }
            }
            if (showMode.contains(ShowMode.SHOW_LAST_USED) && numberOfVisibleSettings > MIN_SETTINGS_COUNT) {
                final List<T> lastUsed = new ArrayList<>(addons.getLastUsed());
                for (int i=lastUsed.size()-1; i>=0; i--) {
                    if (!settingIsVisible(lastUsed.get(i))) {
                        lastUsed.remove(i);
                    }
                }
                if (!lastUsed.isEmpty()) {
                    addTopLevelItem(GroupSettingsDelegate.createSeparatorItem(this, environment.getMessageProvider().translate("SelectorAddons", "Recently Used")));
                    QTreeWidgetItem lastUsedSettingItem;
                    for (T addon : lastUsed) {
                        lastUsedSettingItem = createItemForSetting(null, addon);
                        lastUsedSettingItem.setForeground(0, LAST_USED_FOREGROUND);
                        addTopLevelItem(lastUsedSettingItem);
                    }
                    addTopLevelItem(GroupSettingsDelegate.createSeparatorItem(this, environment.getMessageProvider().translate("SelectorAddons", "All")));
                }
            }
            final List<GroupSettings.SettingItem> items = addons.getItemsByOrder();
            GroupSettings.Group group;
            T setting;
            QTreeWidgetItem groupItem;
            List<Id> settingIds;
            boolean isEmptyGroup;
            for (GroupSettings.SettingItem item : items) {
                if (item.groupName != null) {
                    group = addons.findGroupByName(item.groupName);
                    isEmptyGroup = true;
                    settingIds = group.getSettingsByOrder();
                    for (Id settingId : settingIds) {
                        setting = addons.findById(settingId);
                        if (setting != null && settingIsVisible(setting)) {
                            isEmptyGroup = false;
                            break;
                        }
                    }
                    if (!isEmptyGroup || showMode.contains(ShowMode.SHOW_EMPTY_GROUPS)) {
                        groupItem = createItemForGroup(null, group);
                        settingIds = group.getSettingsByOrder();
                        for (Id settingId : settingIds) {
                            setting = addons.findById(settingId);
                            if (setting != null && settingIsVisible(setting)) {
                                createItemForSetting(groupItem, setting);
                            }
                        }
                        addTopLevelItem(groupItem);
                    }
                } else if (item.settingId != null) {
                    setting = addons.findById(item.settingId);
                    if (setting != null && settingIsVisible(setting)) {
                        addTopLevelItem(createItemForSetting(null, setting));
                    }
                }
            }
            expandToDepth(2);
        } finally {
            blockSignals(false);
        }
        wasModified = false;
    }

    protected boolean settingIsVisible(final T setting) {
        return (!setting.isUserDefined() || addons.canCreateNew())
                && (showMode.contains(ShowMode.SHOW_INVALID_SETTINGS) || setting.isValid());
    }

    protected QTreeWidgetItem createServiceItem(final String text) {
        final QTreeWidgetItem item = new QTreeWidgetItem(this);
        item.setText(0, text);
        item.setForeground(0, SERVICE_ITEM_FOREGROUND);
        final QFont font = item.font(0);
        font.setBold(true);
        item.setFont(0, font);
        return item;
    }

    protected QTreeWidgetItem createItemForSetting(final QTreeWidgetItem parent, final IGroupSetting setting) {
        final QTreeWidgetItem item = parent == null ? new QTreeWidgetItem(this) : new QTreeWidgetItem(parent);
        item.setText(0, setting.hasTitle() ? setting.getTitle() : setting.getName());
        item.setData(0, Qt.ItemDataRole.UserRole, setting.getId().toString());
        item.setData(0, Qt.ItemDataRole.AccessibleDescriptionRole, SETTING_ITEM_DESCRIBTION);
        item.setFlags(SETTING_ITEM_FLAGS);
        final QBrush foreground = getItemForeground(setting);
        if (foreground!=null) {
            item.setForeground(0, foreground);
        }
        item.setIcon(0, getSettingIcon()==null ? ImageManager.getQIcon(setting.getIcon()) : getSettingIcon());
        return item;
    }
    
    protected QBrush getItemForeground(final IGroupSetting setting){
        return setting.isValid() ? null : INVALID_SETTING_FOREGROUND;        
    }

    protected QTreeWidgetItem createItemForGroup(final QTreeWidgetItem parent, GroupSettings.Group group) {
        final QTreeWidgetItem item = parent == null ? new QTreeWidgetItem(this) : new QTreeWidgetItem(parent);
        item.setText(0, group.getName());
        item.setIcon(0, groupIcon);
        item.setData(0, Qt.ItemDataRole.UserRole, group.getName());
        item.setData(0, Qt.ItemDataRole.AccessibleDescriptionRole, SETTING_GROUP_DESCRIBTION);
        item.setFlags(SETTING_GROUP_FLAGS);
        return item;
    }

    public final void setCurrent(final IGroupSetting addon) {
        final QModelIndex idx = findGroupSetting(addon);
        if (idx != null) {
            scrollTo(idx, QAbstractItemView.ScrollHint.PositionAtCenter);
            setCurrentIndex(idx);
        }
    }

    public final void setCurrent(final GroupSettings.Group group) {
        final QTreeWidgetItem item = findItemForGroupSettings(group);
        if (item != null) {
            setCurrentItem(item);
        }
    }

    public final void expand(final GroupSettings.Group group) {
        final QTreeWidgetItem item = findItemForGroupSettings(group);
        if (item != null) {
            expandItem(item);
        }
    }

    public IGroupSetting getCurrentSetting() {
        return currentItem() == null ? null : getSettingForItem(currentItem());
    }

    public GroupSettings.Group getCurrentGroup() {
        final QTreeWidgetItem currentItem = currentItem();
        if (currentItem != null) {
            if (isGroupItem(currentItem)) {
                return getSettingsGroupForItem(currentItem);
            } else {
                final QTreeWidgetItem parentItem = currentItem.parent();
                return parentItem == null ? null : getSettingsGroupForItem(parentItem);
            }
        }
        return null;
    }

    private QTreeWidgetItem findItemForGroupSettings(final GroupSettings.Group group) {
        if (group != null) {
            String groupName;
            for (int i = topLevelItemCount() - 1; i >= 0; i--) {
                if (isGroupItem(topLevelItem(i))) {
                    groupName = (String) topLevelItem(i).data(0, Qt.ItemDataRole.UserRole);
                    if (group.getName().equals(groupName)) {
                        return topLevelItem(i);
                    }
                }
            }
        }
        return null;
    }

    private QModelIndex findGroupSetting(final IGroupSetting addon) {
        QModelIndex result;
        for (int i = 0; i < topLevelItemCount(); i++) {
            if (getSettingForItem(topLevelItem(i)) == addon) {
                return indexFromItem(topLevelItem(i));
            }
            result = findGroupSetting(topLevelItem(i), addon);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private QModelIndex findGroupSetting(final QTreeWidgetItem item, final IGroupSetting addon) {
        QTreeWidgetItem child;
        QModelIndex result;
        final int rowCount = item.childCount();
        for (int i = 0; i < rowCount; i++) {
            child = item.child(i);
            if (getSettingForItem(child) == addon) {
                return indexFromItem(child);
            }
            if (child.childCount() > 0) {
                result = findGroupSetting(child, addon);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
    
    public final void markPositionChanged(final int lastRow){
        if (lastRow>rowPositionChangedFrom)
            rowPositionChangedFrom = lastRow;
    }    

    @Override
    protected void commitData(final QWidget widget) {
        if (widget instanceof QLineEdit) {
            final String newName = ((QLineEdit) widget).text();
            if (newName != null && !newName.isEmpty() && currentItem() != null && isGroupItem(currentItem())) {
                final GroupSettings.Group currentGroup = getSettingsGroupForItem(currentItem());
                GroupSettings.Group group;
                QTreeWidgetItem item;
                for (int i = topLevelItemCount() - 1; i >= 0; i--) {
                    item = topLevelItem(i);
                    if (isGroupItem(item)) {
                        group = getSettingsGroupForItem(item);
                        if (group != null && group != currentGroup && Utils.equals(group.getName(), currentGroup.getName())) {//NOPMD
                            final String title = environment.getMessageProvider().translate("SelectorAddons", "Can't rename group");
                            final String message = environment.getMessageProvider().translate("SelectorAddons", "Group with such name is already exist");
                            environment.messageInformation(title, message);
                            return;
                        }
                    }
                }
                currentGroup.setName(newName);
                super.commitData(widget);
                currentItem().setData(0, Qt.ItemDataRole.UserRole, newName);
                wasModified = true;
            }
        }
    }

    public final boolean isGroupItem(final QTreeWidgetItem item) {
        final String itemDescribtion = (String) item.data(0, Qt.ItemDataRole.AccessibleDescriptionRole);
        return SETTING_GROUP_DESCRIBTION.equals(itemDescribtion);
    }

    public final T getSettingForItem(final QTreeWidgetItem item) {
        final String itemDescribtion = (String) item.data(0, Qt.ItemDataRole.AccessibleDescriptionRole);
        if (SETTING_ITEM_DESCRIBTION.equals(itemDescribtion)) {
            final String idAsStr = (String) item.data(0, Qt.ItemDataRole.UserRole);
            return addons.findById(Id.Factory.loadFrom(idAsStr));
        }
        return null;
    }

    public final T getSettingForIndex(final QModelIndex index) {
        return index != null ? getSettingForItem(itemFromIndex(index)) : null;
    }

    public final GroupSettings.Group getSettingsGroupForItem(final QTreeWidgetItem item) {
        final String itemDescribtion = (String) item.data(0, Qt.ItemDataRole.AccessibleDescriptionRole);
        if (SETTING_GROUP_DESCRIBTION.equals(itemDescribtion)) {
            return addons.findGroupByName((String) item.data(0, Qt.ItemDataRole.UserRole));
        }
        return null;
    }

    @Override
    protected void rowsInserted(QModelIndex parentIndex, int startRow, int endRow) {
        super.rowsInserted(parentIndex, startRow, endRow);
        if (parentIndex!=null && parentIndex.parent()==null){
            rowPositionChangedFrom = Math.max(rowPositionChangedFrom, endRow);
        }
        wasModified = true;
    }

    @Override
    protected void dragMoveEvent(final QDragMoveEvent moveEvent) {
        final QTreeWidgetItem currentItem = currentItem();
        if (currentItem != null) {
            final QTreeWidgetItem itemUnderCursor = itemAt(moveEvent.pos());
            if (itemUnderCursor != null && isGroupItem(currentItem)
                    && (isGroupItem(itemUnderCursor) || itemUnderCursor.parent() != null)) {
                moveEvent.setDropAction(Qt.DropAction.IgnoreAction);
                moveEvent.setAccepted(true);
            }
        }
        super.dragMoveEvent(moveEvent);
    }

    @Override
    protected void dropEvent(final QDropEvent dropEvent) {        
        final QTreeWidgetItem currentItem = currentItem();
        if (currentItem != null) {
            wasModified = true;
            final boolean isGroup = isGroupItem(currentItem);
            final boolean needToExpand = currentItem.isExpanded();
            final IGroupSetting currentSetting = isGroup ? null : getSettingForItem(currentItem);
            final GroupSettings.Group currentGroup = isGroup ? getSettingsGroupForItem(currentItem) : null;
            if (currentItem()!=null && currentItem().parent()==null){
                markPositionChanged(currentIndex().row());//mark before drop
            }            
            super.dropEvent(dropEvent);
            if (currentSetting == null) {
                setCurrent(currentGroup);
                if (needToExpand) {
                    expand(currentGroup);
                }
            } else {
                setCurrent(currentSetting);
                if (currentItem()!=null && currentItem().parent()==null){
                    markPositionChanged(currentIndex().row());//mark after drop
                }
            }            
        }
    }

    @Override
    protected void rowsAboutToBeRemoved(QModelIndex parentIndex, int startRow, int endRow) {
        super.rowsAboutToBeRemoved(parentIndex, startRow, endRow);        
        if (parentIndex==null && endRow>=rowPositionChangedFrom){
            for (int i=startRow; i<=endRow; i++){
                if (i>=rowPositionChangedFrom){
                    rowPositionChangedFrom--;
                }
            }
        }
        wasModified = true;
    }

    private void saveItems() {
        final List<GroupSettings.SettingItem> items = new ArrayList<GroupSettings.SettingItem>();
        QTreeWidgetItem item;
        for (int i = 0, itemsCount = topLevelItemCount(); i < itemsCount; i++) {
            item = topLevelItem(i);
            if (isGroupItem(item)) {
                final GroupSettings.Group group = getSettingsGroupForItem(item);
                group.clear();
                for (int j = 0, childCount = item.childCount(); j < childCount; j++) {
                    group.add(getSettingForItem(item.child(j)).getId());
                }
                items.add(new GroupSettings.SettingItem(group.getName()));
            } else if (i<=rowPositionChangedFrom){
                items.add(new GroupSettings.SettingItem(getSettingForItem(item).getId()));
            }
        }
        addons.setItemsOrder(items);
        addons.saveAll();
        
    }

    @Override
    protected void closeEvent(final QCloseEvent closeEvent) {
        if (wasModified) {
            saveItems();
        } else if (addons.wasModified()) {
            addons.saveAll();
        }

        super.closeEvent(closeEvent);
    }
}