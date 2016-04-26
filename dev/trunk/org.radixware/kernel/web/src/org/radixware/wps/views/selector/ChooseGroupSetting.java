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

package org.radixware.wps.views.selector;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EFontStyle;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.groupsettings.GroupSettings;
import org.radixware.kernel.common.client.models.groupsettings.IGroupSetting;
import org.radixware.wps.rwt.DropDownListDelegate;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.Panel;
import org.radixware.wps.rwt.ValueEditor.ValueChangeListener;
import org.radixware.wps.text.WpsFont;


public class ChooseGroupSetting<T extends IGroupSetting> extends Panel {
    
    private static final Color RESENTLY_USED_ITEM_FOREGROUND = new Color(128, 0, 128);

    private final class DropDownSettingsListDelegate extends DropDownListDelegate<T> {

        private final static int MIN_SETTINGS_COUNT = 5;       
        private final DropDownListItem<T> nullItem;
        private final GroupSettings<T> addons;

        public DropDownSettingsListDelegate(final String noItemText, final GroupSettings<T> addons) {
            nullItem = new DropDownListItem<>(noItemText, null);
            this.addons = addons;
            setDisplayCurrentItemInDropDownList(true);
        }

        @Override
        protected List<DropDownListItem<T>> getItems() {
            List<DropDownListItem<T>> result = new LinkedList<>();

            if (!addons.isObligatory() && !ChooseGroupSetting.this.isMandatory()) {
                result.add(nullItem);
            }
            int numberOfVisibleSettings = 0;
            for (GroupSettings.SettingItem settingItem: addons.getItemsByOrder()){
                if (settingItem.settingId!=null){
                    final T setting = addons.findById(settingItem.settingId);
                    if (setting!=null && ChooseGroupSetting.this.isSettingVisible(setting)){
                        numberOfVisibleSettings++;
                    }
                }
            }
            if (numberOfVisibleSettings > MIN_SETTINGS_COUNT) {
                final List<T> lastUsed = new ArrayList<>(addons.getLastUsed());
                for (int i=lastUsed.size()-1; i>=0; i--) {                        
                    if (!ChooseGroupSetting.this.isSettingVisible(lastUsed.get(i))) {
                        lastUsed.remove(i);
                    }
                }
                if (!lastUsed.isEmpty()) {
                    final MessageProvider mp = ChooseGroupSetting.this.getEnvironment().getMessageProvider();                    
                    result.add(createSeparatorItem(mp.translate("SelectorAddons", "Recently Used")));
                    for (T addon : lastUsed) {
                        result.add(createRecentlyUsedItem(addon));
                    }
                    result.add(createSeparatorItem(mp.translate("SelectorAddons", "All")));
                }                
            }

            final List<GroupSettings.SettingItem> items = addons.getItemsByOrder();
            for (GroupSettings.SettingItem item : items) {
                T setting = addons.findById(item.settingId);
                if (setting != null && ChooseGroupSetting.this.isSettingVisible(setting)) {
                    result.add(new DropDownListItem<>(setting.getTitle(), setting, setting.getIcon()));
                }
            }
            return result;
        }
        
        private DropDownListItem<T> createRecentlyUsedItem(final T addon){
            final DropDownListItem<T> item = new DropDownListItem<>(addon.getTitle(),addon);            
            item.setForeground(RESENTLY_USED_ITEM_FOREGROUND);
            return item;
        }
        
        private DropDownListItem<T> createSeparatorItem(final String title){
            final DropDownListItem<T> item = new DropDownListItem<>(title,null);
            final WpsFont font = item.getFont().changeStyle(EFontStyle.ITALIC);
            item.setFont(font);
            item.setEnabled(false);
            item.setBorderColor(Color.GRAY);
            item.getHtml().setCss("border-bottom-width", "1px");
            item.getHtml().setCss("border-bottom-style", "solid");
            return item;
        }
    }

    private class ComboBox extends InputBox<T> {

        public ComboBox(final GroupSettings<T> addons, final String openSettingsManagerTitle) {
            super(new DisplayController<T>() {

                @Override
                public String getDisplayValue(T value, boolean isFocused, boolean isReadOnly) {
                    if (value == null) {
                        return noItemText;
                    } else {
                        return value.getTitle();
                    }
                }
            }, null);
            addDropDownDelegate(new DropDownSettingsListDelegate(noItemText, addons));
        }
    }
    //private IGroupSetting currentAddon;
    private T currentAddon;
    private final ComboBox comboBox;    
    private String noItemText;
    private boolean blockChangesListener;

    public ChooseGroupSetting(final IClientEnvironment environment, final GroupSettings<T> addons, final String openSettingsManagerTitle, final String label) {
        super();
        noItemText = environment.getMessageProvider().translate("Selector", "<none>");
        comboBox = new ComboBox(addons, openSettingsManagerTitle);
        Panel.Table.Row row = this.getTable().addRow();
        Table.Row.Cell labelCell = row.addCell();
        labelCell.setComponent(new Label(label));
        labelCell.setHfit(true);
        Table.Row.Cell chooserCell = row.addCell();
        chooserCell.setComponent(comboBox);
        comboBox.setVCoverage(100);
        comboBox.addValueChangeListener(new ValueChangeListener<T>() {

            @Override
            public void onValueChanged(T oldValue, T newValue) {
                currentAddon = newValue;
                if (!blockChangesListener){
                    groupSettingChanged(newValue);
                }
            }
        });
        refresh();
    }

    protected void groupSettingChanged(T setting) {
    }

    public T getCurrentAddon() {
        return currentAddon;
    }

    public void setCurrentAddon(T addon) {
        this.currentAddon = addon;
        refresh();
    }

    protected boolean isSettingVisible(final T setting) {
        return true;
    }

    protected boolean canOpenSettingsManager() {
        return true;
    }

    public void setNoItemText(final String text) {
        noItemText = text;
    }

    protected boolean isMandatory() {
        return false;
    }

    public final void refresh() {
        if (comboBox.getValue() != currentAddon) {
            blockChangesListener = true;
            try{
                comboBox.setValue(currentAddon);
            }
            finally{
                blockChangesListener = false;
            }
        }
    }

    protected void close() {
        currentAddon = null;
        comboBox.setValue(null);
    }
}