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

import java.text.DateFormat;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.LocaleManager;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.enums.EDateTimeStyle;
import org.radixware.kernel.common.enums.EIsoCountry;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.html.Table;
import org.radixware.kernel.common.html.ToolTip;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.ValueEditor.ValueChangeListener;
import org.radixware.wps.rwt.VerticalBox;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;

class DateTimeFormatSettingWidget extends SettingsWidget {

    private final ValStrEditorController formatEditor;
    private final EDateTimeStyle dateStyle;
    private final EDateTimeStyle timeStyle;
    private final VerticalBox vb = new VerticalBox();

    public DateTimeFormatSettingWidget(final WpsEnvironment environment,
            final UIObject parent,
            final String group,
            final String subGroup,
            final EDateTimeStyle dateStyle,
            final EDateTimeStyle timeStyle) {
        super(environment, parent, group, subGroup, getSettingName(dateStyle, timeStyle));
        this.dateStyle = dateStyle;
        this.timeStyle = timeStyle;
        final EditMaskStr mask = new EditMaskStr();
        mask.setNoValueStr(getDefaultValue());
        formatEditor = new ValStrEditorController(environment);
        ((UIObject) formatEditor.getValEditor()).setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        formatEditor.setEditMask(mask);
        final List<String> predefinedValues = createPredefinedValues();
        formatEditor.setPredefinedValues(predefinedValues);
        formatEditor.addValueChangeListener(new ValueChangeListener<String>() {

            @Override
            public void onValueChanged(String oldValue, String newValue) {
                DateTimeFormatSettingWidget.this.onFormatEditingFinished();
            }
        });
        formatEditor.setHtmlToolTip(getTooltipText(dateStyle, timeStyle, environment)); 
        formatEditor.setMandatory(true);
        vb.add((UIObject) formatEditor.getValEditor());
        this.add(vb);
    }

    public static String getSettingName(final EDateTimeStyle dateStyle, final EDateTimeStyle timeStyle) {
        if (dateStyle != null && dateStyle != EDateTimeStyle.NONE) {
            return dateStyle.getName().toLowerCase();
        } else if (timeStyle != null && timeStyle != EDateTimeStyle.NONE) {
            return timeStyle.getName().toLowerCase();
        } else {
            throw new IllegalArgumentException("One of dateStyle or timeStyle must be defined");
        }
    }

    private final List<String> createPredefinedValues() {
        final List<String> predefinedValues = new LinkedList<>();
        final List<String> valuesForCurrentLanguage
                = createPredefinedValues(getEnvironment().getLanguage());
        final String defaultValue = getDefaultValue();
        predefinedValues.add(defaultValue);
        for (String value : valuesForCurrentLanguage) {
            if (!predefinedValues.contains(value)) {
                predefinedValues.add(value);
            }
        }
        if (getEnvironment().getLanguage() != EIsoLanguage.ENGLISH) {
            final List<String> valuesForEnglishLanguage
                    = createPredefinedValues(EIsoLanguage.ENGLISH);
            for (String value : valuesForEnglishLanguage) {
                if (!predefinedValues.contains(value)) {
                    predefinedValues.add(value);
                }
            }
        }
        return predefinedValues;
    }

    public final String getDefaultValue() {
        return getQtFormat(dateStyle, timeStyle, getEnvironment().getLocale());
    }

    private final List<String> createPredefinedValues(final EIsoLanguage language) {
        final List<String> predefinedValues = new LinkedList<>();
        final EnumSet<EIsoCountry> countries = LocaleManager.getCountriesForLanguage(language);
        for (EIsoCountry country : countries) {
            final Locale locale = new Locale(language.getValue(), country.getAlpha2Code());
            final String qtFormat = getQtFormat(dateStyle, timeStyle, locale);
            predefinedValues.add(qtFormat);
        }
        return predefinedValues;
    }

    public void addListener(ValueChangeListener<String> listener) {
        formatEditor.addValueChangeListener(listener);
    }

    private final void onFormatEditingFinished() {
        final String format = formatEditor.getValue();
        if (format != null && format.isEmpty()) {
            formatEditor.setValue(getDefaultValue());
        }
    }

    private String getCurrentFormat() {
        final String format = formatEditor.getValue();
        if (dateStyle == null || dateStyle == EDateTimeStyle.NONE) {
            return EditMaskDateTime.stripDateFormat(format);
        } else if (timeStyle == null || timeStyle == EDateTimeStyle.NONE) {
            return EditMaskDateTime.stripTimeFormat(format);
        } else {
            return format;
        }
    }

    private static String getQtFormat(final EDateTimeStyle dateStyle,
            final EDateTimeStyle timeStyle,
            final java.util.Locale locale) {
        final DateFormat format;
        if (dateStyle != null && dateStyle != EDateTimeStyle.NONE) {
            format = DateFormat.getDateInstance(dateStyle.getJavaDateTimeStyle(), locale);
        } else if (timeStyle != null && timeStyle != EDateTimeStyle.NONE) {
            format = DateFormat.getTimeInstance(timeStyle.getJavaDateTimeStyle(), locale);
        } else {
            return "";
        }
        final String javaFormat = ((java.text.SimpleDateFormat) format).toPattern();
        return EditMaskDateTime.javaFormat2QtFormat(locale, javaFormat);
    }

    public ValStrEditorController getValStrEditorController() {
        return this.formatEditor;
    }
    
    private static ToolTip getTooltipText(final EDateTimeStyle dateStyle, final EDateTimeStyle timeStyle, final IClientEnvironment environment) {
        final MessageProvider mp = environment.getMessageProvider();
        ToolTip toolTip = new ToolTip();
        Table table = new Table();
        
        //table
        if (dateStyle==null || dateStyle==EDateTimeStyle.NONE){
            table.addRow("h", mp.translate("EditMask", "The hour without a leading zero (0 to 23 or 1 to 12 if AM/PM display)"));
            table.addRow("hh", mp.translate("EditMask", "The hour with a leading zero (00 to 23 or 01 to 12 if AM/PM display)"));
            table.addRow("H", mp.translate("EditMask", "The hour without a leading zero (0 to 23, even with AM/PM display)"));
            table.addRow("HH", mp.translate("EditMask", "The hour with a leading zero (00 to 23, even with AM/PM display)"));
            table.addRow("m", mp.translate("EditMask", "The minute without a leading zero (0 to 59)"));
            table.addRow("mm", mp.translate("EditMask", "The minute with a leading zero (00 to 59)"));
            table.addRow("s", mp.translate("EditMask", "The second without a leading zero (0 to 59)"));
            table.addRow("ss", mp.translate("EditMask", "The second with a leading zero (00 to 59)"));
            table.addRow("z", mp.translate("EditMask", "The milliseconds without leading zeroes (0 to 999)"));
            table.addRow("zzz", mp.translate("EditMask", "The milliseconds with leading zeroes (000 to 999)"));
            table.addRow("AP or A", mp.translate("EditMask", "Interpret as an AM/PM time. AP must be either \"AM\" or \"PM\"."));
            table.addRow("ap or a", mp.translate("EditMask", "Interpret as an AM/PM time. ap must be either \"am\" or \"pm\"."));
            table.addRow("T", mp.translate("EditMask", "Time zone."));
            table.addRow("Z", mp.translate("EditMask", "Time zone in RFC 822 format."));
            table.addRow("X", mp.translate("EditMask", "Time zone in ISO 8601 format."));
        }else{
            table.addRow("d", mp.translate("EditMask", "The day as number without a leading zero (1 to 31)."));
            table.addRow("dd", mp.translate("EditMask", "The day as number with a leading zero (01 to 31)."));
            table.addRow("ddd", mp.translate("EditMask","The abbreviated localized day name (e.g. 'Mon' to 'Sun')."));
            table.addRow("dddd", mp.translate("EditMask", "The long localized day name (e.g. 'Monday' to 'Sunday')."));
            table.addRow("M", mp.translate("EditMask", "The month as number without a leading zero (1-12)."));
            table.addRow("MM", mp.translate("EditMask", "The month as number with a leading zero (01-12)."));
            table.addRow("MMM", mp.translate("EditMask", "The abbreviated localized month name (e.g. 'Jan' to 'Dec')."));
            table.addRow("MMMM", mp.translate("EditMask", "The long localized month name (e.g. 'January' to 'December')."));
            table.addRow("yy", mp.translate("EditMask", "The year as two digit number (00-99)"));
            table.addRow("yyyy", mp.translate("EditMask", "The year as four digit number"));
        }
        toolTip.add(table);
        return toolTip;
    }

    @Override
    public void readSettings(WpsSettings src) {
        final String format = src.readString(getSettingCfgName(), null);
        if (format == null || format.isEmpty()) {
            formatEditor.setValue(getDefaultValue());
        } else {
            formatEditor.setValue(format);
        }
    }

    @Override
    public void writeSettings(WpsSettings dst) {
        final String format = getCurrentFormat();
        if (format == null || format.isEmpty() || format.equals(getDefaultValue())) {
            dst.remove(getSettingCfgName());
        } else {
            dst.writeString(getSettingCfgName(), format);
        }
    }

    @Override
    public void restoreDefaults() {
        formatEditor.setValue(getDefaultValue());
    }
}
