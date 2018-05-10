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
package org.radixware.wps.settings;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.enums.EDateTimeStyle;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.rwt.Alignment;
import org.radixware.wps.rwt.GridBoxContainer;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.ValueEditor;

class DateTimeFormatsEditor extends SettingsWidget {

    public static enum EFormatType {

        DATE, TIME
    };
    final GridBoxContainer layout = new GridBoxContainer();
    private final Map<DateTimeFormatSettingWidget, Label> examplesBySettingsWidget = new HashMap<>();
    private final static EDateTimeStyle FORMAT_STYLES[]
            = {EDateTimeStyle.SHORT, EDateTimeStyle.MEDIUM, EDateTimeStyle.DEFAULT, EDateTimeStyle.LONG, EDateTimeStyle.FULL};
    private final List<SettingsWidget> formatSettings = new LinkedList<>();
    private final Timestamp currentTime;
    private final EFormatType formatType;

    public DateTimeFormatsEditor(WpsEnvironment env, final String groupName, final String subGroupName, final EFormatType formatType) {
        super(env, null, groupName, subGroupName, null);
        currentTime = env.getCurrentServerTime();
        this.formatType = formatType;
        setupUi();
    }

    private void setupUi() {
        final MessageProvider mp = getEnvironment().getMessageProvider();
        layout.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        Label lbFormatTitle = new Label(mp.translate("Settings Dialog", "Format:"));
        lbFormatTitle.setTextWrapDisabled(true);
        lbFormatTitle.setAlign(Alignment.CENTER);
        layout.add(lbFormatTitle, 0, 0, Alignment.MIDDLE);
        layout.setColSpan(0, 0, 2);
        Label lbExampleTilte = new Label(mp.translate("Settings Dialog", "Example:"));
        lbExampleTilte.setTextWrapDisabled(true);
        lbExampleTilte.setAlign(Alignment.CENTER);
        layout.add(lbExampleTilte, 0, 2, Alignment.MIDDLE);
        layout.setColSpan(0, 2, 1);
        for (int i = 0; i < FORMAT_STYLES.length; i++) {
            if (formatType == EFormatType.DATE) {
                addRow(i + 1, FORMAT_STYLES[i], null);
            } else {
                addRow(i + 1, null, FORMAT_STYLES[i]);
            }
        }
        this.add(layout);
    }

    private void addRow(final int row, final EDateTimeStyle dateStyle, final EDateTimeStyle timeStyle) {
        final EDateTimeStyle style = dateStyle == null || dateStyle == EDateTimeStyle.NONE ? timeStyle : dateStyle;
        final Label lbStyleTitle = new Label(getStyleTitle(style));
        lbStyleTitle.setTextWrapDisabled(true);
        lbStyleTitle.setSizePolicy(SizePolicy.MINIMUM_EXPAND, SizePolicy.MINIMUM_EXPAND);
        layout.add(lbStyleTitle, row, 0, Alignment.MIDDLE);
        layout.setColummnExpand(1, 100);
        final DateTimeFormatSettingWidget formatSetting
                = new DateTimeFormatSettingWidget((WpsEnvironment) getEnvironment(), this, group, subGroup, dateStyle, timeStyle);
        formatSettings.add(formatSetting);
        layout.add(formatSetting, row, 1, Alignment.MIDDLE);
        final Label lbExample = new Label();
        lbExample.setSizePolicy(SizePolicy.MINIMUM_EXPAND, SizePolicy.MINIMUM_EXPAND);
        lbExample.setTextWrapDisabled(true);
        examplesBySettingsWidget.put(formatSetting, lbExample);
        formatSetting.getValStrEditorController().addValueChangeListener(new ValueEditor.ValueChangeListener<String>() {

            @Override
            public void onValueChanged(String oldValue, String newValue) {
                final Label lbExample = examplesBySettingsWidget.get(formatSetting);
                if (lbExample != null) {
                    final String format = newValue == null ? formatSetting.getDefaultValue() : newValue;
                    updateExampleText(lbExample, format);
                }
            }
        });
        layout.add(lbExample, row, 2, Alignment.MIDDLE);
        updateExampleText(lbExample, formatSetting.getDefaultValue());
        layout.setCellWidthInPercent(row, 1, 100);
    }

    private void updateExampleText(final Label lbExample, final String format) {
        final EditMaskDateTime mask = new EditMaskDateTime(format, null, null);
        lbExample.setText(mask.toStr(getEnvironment(), currentTime));
    }

    private String getStyleTitle(final EDateTimeStyle style) {
        final MessageProvider mp = getEnvironment().getMessageProvider();
        switch (style) {
            case SHORT:
                return mp.translate("Settings Dialog", "Short:");
            case MEDIUM:
                return mp.translate("Settings Dialog", "Medium:");
            case DEFAULT:
                return mp.translate("Settings Dialog", "Default:");
            case LONG:
                return mp.translate("Settings Dialog", "Long:");
            case FULL:
                return mp.translate("Settings Dialog", "Full:");
            default:
                throw new IllegalArgumentException("Unsupported format " + style.name());
        }
    }

    @Override
    public void readSettings(WpsSettings src) {
        for (SettingsWidget settingsWidget: formatSettings){
            settingsWidget.readSettings(src);
        }
    }

    @Override
    public void writeSettings(WpsSettings dst) {
        for (SettingsWidget settingsWidget: formatSettings){
            settingsWidget.writeSettings(dst);
        }
    }

    @Override
    public void restoreDefaults() {
        for (SettingsWidget settingsWidget: formatSettings){
            settingsWidget.restoreDefaults();
        }
    }
}
