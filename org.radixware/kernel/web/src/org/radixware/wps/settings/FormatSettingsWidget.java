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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.ETriadDelimeterType;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.rwt.Alignment;
import org.radixware.wps.rwt.FormBox;
import org.radixware.wps.rwt.GroupBox;
import org.radixware.wps.rwt.PushButton;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.ValueEditor;
import org.radixware.wps.rwt.VerticalBox;
import org.radixware.wps.views.editors.valeditors.ValListEditorController;
import org.radixware.wps.views.editors.valeditors.ValNumEditorController;

class FormatSettingsWidget extends SettingsWidget {

    private ValListEditorController<Character> groupingSeparatorEditor;
    private ValListEditorController<Character> decimalMarkEditor;
    private ValNumEditorController valNumExample;
    private DateTimeFormatsEditor dateFormatEditor;
    private DateTimeFormatsEditor timeFormatEditor;
    private char spaceChar = '\u00A0';

    private final static String GROUPING_SEPARATOR_SETTING_NAME = SettingNames.SYSTEM + "/"
            + SettingNames.FORMAT_SETTINGS + "/"
            + SettingNames.FormatSettings.NUMBER + "/" + SettingNames.FormatSettings.Number.GROUP_SEPARATOR;
    private final static String DECIMAL_MARK_SETTING_NAME = SettingNames.SYSTEM + "/"
            + SettingNames.FORMAT_SETTINGS + "/"
            + SettingNames.FormatSettings.NUMBER + "/" + SettingNames.FormatSettings.Number.DECIMAL_PART_SEPARATOR;

    public FormatSettingsWidget(WpsEnvironment env) {
        super(env, null, null, null, null);
        this.setHeight(550);
        setupUi();
    }

    private DecimalFormatSymbols getDefaultFormatSymbols() {
        final NumberFormat numberFormat = NumberFormat.getNumberInstance(getEnvironment().getLocale());
        if (numberFormat instanceof DecimalFormat) {
            final DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            final DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
            return symbols;
        } else {
            return new DecimalFormatSymbols(getEnvironment().getLocale());
        }
    }

    private boolean isGroupingUsed() {
        final NumberFormat numberFormat = NumberFormat.getNumberInstance(getEnvironment().getLocale());
        return numberFormat == null ? false : numberFormat.isGroupingUsed();
    }

    private void setupUi() {
        final MessageProvider mp = getEnvironment().getMessageProvider();
        final DecimalFormatSymbols defaultSymbols = getDefaultFormatSymbols();
        final char defaultGroupingSeparator = isGroupingUsed() ? defaultSymbols.getGroupingSeparator() : '\0';
        if (defaultGroupingSeparator==' '){
            spaceChar = ' ';
        }
        final char defaultDecimalMark = defaultSymbols.getDecimalSeparator();

        final VerticalBox vb = new VerticalBox();
        vb.setSizePolicy(SizePolicy.MINIMUM_EXPAND, SizePolicy.MINIMUM_EXPAND);
        final FormBox numberFormatForm = new FormBox();
        numberFormatForm.getHtml().setCss("overflow-y", "hidden");
        
        final GroupBox gbNumberFormat = new GroupBox();
        gbNumberFormat.setTitle(mp.translate("Settings Dialog", "Number Format:"));
        gbNumberFormat.setTitleAlign(Alignment.LEFT);
        gbNumberFormat.setHSizePolicy(SizePolicy.MINIMUM_EXPAND);

        final GroupBox gbDateFormat = new GroupBox();
        gbDateFormat.setTitleAlign(Alignment.LEFT);
        gbDateFormat.setHSizePolicy(SizePolicy.MINIMUM_EXPAND);
        gbDateFormat.setTitle(mp.translate("Settings Dialog", "Date Format:"));

        final GroupBox gbTimeFormat = new GroupBox();
        gbTimeFormat.setTitleAlign(Alignment.LEFT);
        gbTimeFormat.setHSizePolicy(SizePolicy.MINIMUM_EXPAND);
        gbTimeFormat.setTitle(mp.translate("Settings Dialog", "Time Format:"));

        {
            final List<EditMaskList.Item> groupSeparators = new LinkedList<>();
            groupSeparators.add(new EditMaskList.Item(",", ','));
            groupSeparators.add(new EditMaskList.Item(".", '.'));            
            groupSeparators.add(new EditMaskList.Item(mp.translate("Settings Dialog", "Space"), spaceChar));
            groupSeparators.add(new EditMaskList.Item("\'", '\''));
            groupSeparators.add(new EditMaskList.Item("`", '`'));
            groupSeparators.add(new EditMaskList.Item(mp.translate("Settings Dialog", "None"), '\0'));
            boolean defaultSeparatorPresent = false;
            for (EditMaskList.Item item : groupSeparators) {
                if (Character.valueOf(defaultGroupingSeparator).equals(item.getValue())) {
                    defaultSeparatorPresent = true;
                    break;
                }
            }
            if (!defaultSeparatorPresent) {
                groupSeparators.add(new EditMaskList.Item(String.valueOf(defaultGroupingSeparator), defaultGroupingSeparator));
            }
            groupingSeparatorEditor = new ValListEditorController<>(getEnvironment(), new EditMaskList(groupSeparators));
            groupingSeparatorEditor.setMandatory(true);
            groupingSeparatorEditor.setValue(defaultGroupingSeparator);
            groupingSeparatorEditor.addValueChangeListener(new ValueEditor.ValueChangeListener<Character>() {

                @Override
                public void onValueChanged(Character oldValue, Character newValue) {
                    FormatSettingsWidget.this.onChangeNumberFormat();
                }
            });
            numberFormatForm.addLabledEditor(mp.translate("Settings Dialog", "Thousands separator:"), (UIObject) groupingSeparatorEditor.getValEditor());
        }
        {
            final List<EditMaskList.Item> decimalMarks = new LinkedList<>();
            decimalMarks.add(new EditMaskList.Item(".", '.'));
            decimalMarks.add(new EditMaskList.Item(",", ','));
            boolean defaultMarkPresent = false;
            for (EditMaskList.Item item : decimalMarks) {
                if (Character.valueOf(defaultDecimalMark).equals(item.getValue())) {
                    defaultMarkPresent = true;
                    break;
                }
            }
            if (!defaultMarkPresent) {
                decimalMarks.add(new EditMaskList.Item(String.valueOf(defaultGroupingSeparator), defaultGroupingSeparator));
            }
            decimalMarkEditor = new ValListEditorController<>(getEnvironment(), new EditMaskList(decimalMarks));
            decimalMarkEditor.setMandatory(true);
            decimalMarkEditor.setValue(defaultDecimalMark);
            decimalMarkEditor.addValueChangeListener(new ValueEditor.ValueChangeListener<Character>() {

                @Override
                public void onValueChanged(Character oldValue, Character newValue) {
                    FormatSettingsWidget.this.onChangeNumberFormat();
                }
            });
            numberFormatForm.addLabledEditor(mp.translate("Settings Dialog", "Decimal separator:"), (UIObject) decimalMarkEditor.getValEditor());
        }
        {
            valNumExample = new ValNumEditorController(getEnvironment());
            valNumExample.setValue(new BigDecimal("12345.6789"));
            valNumExample.setReadOnly(true);
            numberFormatForm.addLabledEditor(mp.translate("Settings Dialog", "Example:"), (UIObject) valNumExample.getValEditor());
            onChangeNumberFormat();
        }

        gbNumberFormat.add(numberFormatForm);

        {
            dateFormatEditor = new DateTimeFormatsEditor((WpsEnvironment) getEnvironment(), SettingNames.FORMAT_SETTINGS, SettingNames.FormatSettings.DATE, DateTimeFormatsEditor.EFormatType.DATE);
            gbDateFormat.add(dateFormatEditor);
        }
        {
            timeFormatEditor = new DateTimeFormatsEditor((WpsEnvironment) getEnvironment(), SettingNames.FORMAT_SETTINGS, SettingNames.FormatSettings.TIME, DateTimeFormatsEditor.EFormatType.TIME);
            gbTimeFormat.add(timeFormatEditor);
        }
        vb.addSpace(10);
        vb.add(gbNumberFormat);
        vb.addSpace(10);
        vb.add(gbDateFormat);
        vb.addSpace(10);
        vb.add(gbTimeFormat);
        vb.addSpace(10);
        PushButton restoreDefaultsBtn = new PushButton();
        restoreDefaultsBtn.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                FormatSettingsWidget.this.restoreDefaults();
            }
        });
        restoreDefaultsBtn.setIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.CANCEL));
        restoreDefaultsBtn.setText(getEnvironment().getMessageProvider().translate("Settings Dialog", "Restore default formats"));
        vb.add(restoreDefaultsBtn);
        vb.getHtml().setCss("padding", "5px");
        this.add(vb);
    }

    private EditMaskNum createEditMaskNum() {
        final Character groupSeparator = groupingSeparatorEditor.getValue();
        final Character decimalMark = decimalMarkEditor.getValue();
        if (groupSeparator == '\0') {
            return new EditMaskNum(null, null, 1, ETriadDelimeterType.NONE, null, decimalMark, (byte) 4);
        } else {
            return new EditMaskNum(null, null, 1, ETriadDelimeterType.SPECIFIED, groupSeparator, decimalMark, (byte) 4);
        }
    }

    @SuppressWarnings("unused")
    private void onChangeNumberFormat() {
        valNumExample.setEditMask(createEditMaskNum());
        valNumExample.refresh();
    }

    @Override
    public void readSettings(WpsSettings src) {
        final DecimalFormatSymbols defaultSymbols = getDefaultFormatSymbols();

        final String groupSeparator = src.readString(GROUPING_SEPARATOR_SETTING_NAME, null);
        if (groupSeparator == null || groupSeparator.isEmpty()) {
            groupingSeparatorEditor.setValue(isGroupingUsed() ? defaultSymbols.getGroupingSeparator() : '\0');
        } else {
            final char groupSeparatorChar = groupSeparator.charAt(0);
            if (groupSeparatorChar==' ' || groupSeparatorChar=='\u00A0'){
                groupingSeparatorEditor.setValue(spaceChar);
            }else{
                groupingSeparatorEditor.setValue(groupSeparatorChar);
            }
        }

        final String decimalMark = src.readString(DECIMAL_MARK_SETTING_NAME, null);
        if (decimalMark == null || decimalMark.isEmpty()) {
            decimalMarkEditor.setValue(defaultSymbols.getDecimalSeparator());
        } else {
            decimalMarkEditor.setValue(decimalMark.charAt(0));
        }

        dateFormatEditor.readSettings(src);
        timeFormatEditor.readSettings(src);
    }

    @Override
    public void writeSettings(WpsSettings dst) {
        final DecimalFormatSymbols defaultSymbols = getDefaultFormatSymbols();

        final char groupSeparator = groupingSeparatorEditor.getValue();
        final char defaultGroupingSeparator = isGroupingUsed() ? defaultSymbols.getGroupingSeparator() : '\0';
        if (groupSeparator == defaultGroupingSeparator) {
            dst.remove(GROUPING_SEPARATOR_SETTING_NAME);
        } else {
            dst.writeString(GROUPING_SEPARATOR_SETTING_NAME, String.valueOf(groupSeparator));
        }

        final char decimalMark = decimalMarkEditor.getValue();
        if (decimalMark == defaultSymbols.getDecimalSeparator()) {
            dst.remove(DECIMAL_MARK_SETTING_NAME);
        } else {
            dst.writeString(DECIMAL_MARK_SETTING_NAME, String.valueOf(decimalMark));
        }

        dateFormatEditor.writeSettings(dst);
        timeFormatEditor.writeSettings(dst);
    }

    @Override
    public void restoreDefaults() {
        final DecimalFormatSymbols defaultSymbols = getDefaultFormatSymbols();
        groupingSeparatorEditor.setValue(isGroupingUsed() ? defaultSymbols.getGroupingSeparator() : '\0');
        decimalMarkEditor.setValue(defaultSymbols.getDecimalSeparator());

        dateFormatEditor.restoreDefaults();
        timeFormatEditor.restoreDefaults();
    }

    @Override
    public boolean validate() {
        if (groupingSeparatorEditor.getValue().equals(decimalMarkEditor.getValue())) {
            MessageProvider mp = getEnvironment().getMessageProvider();
            getEnvironment().messageWarning(mp.translate("Settings Dialog", "Input error"), mp.translate("Settings Dialog", "Thousands separator value and decimal separator value should not be equal"));
            return false;
        } else {
            return true;
        }
    }
    
    
}
