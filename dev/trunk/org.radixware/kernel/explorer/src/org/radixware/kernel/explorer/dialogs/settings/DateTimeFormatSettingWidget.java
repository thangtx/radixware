/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.dialogs.settings;

import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.sql.Timestamp;
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
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.env.IExplorerSettings;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.utils.html.HtmlTableBuilder;


final class DateTimeFormatSettingWidget extends SettingsWidget{
    
    public final com.trolltech.qt.QSignalEmitter.Signal1<String> valueChanged = new com.trolltech.qt.QSignalEmitter.Signal1<>();
    
    private final ValStrEditor formatEditor;
    private final EDateTimeStyle dateStyle;
    private final EDateTimeStyle timeStyle;
    
    public DateTimeFormatSettingWidget(final IClientEnvironment environment, 
                                                              final QWidget parent, 
                                                              final String group,
                                                              final String subGroup,
                                                              final EDateTimeStyle dateStyle,
                                                              final EDateTimeStyle timeStyle
    ){
        super(environment, parent, group, subGroup, getSettingName(dateStyle, timeStyle));
        this.dateStyle = dateStyle;
        this.timeStyle = timeStyle;
        final EditMaskStr mask = new EditMaskStr();        
        mask.setNoValueStr(getDefaultValue());
        final QVBoxLayout layout = WidgetUtils.createVBoxLayout(this);
        formatEditor = new ValStrEditor(environment, this, mask, true, false);        
        formatEditor.valueChanged.connect(this,"onFormatChanged()");
        formatEditor.editingFinished.connect(this,"onFormatEditingFinished()");
        final List<String> predefinedValues = createPredefinedValues();
        formatEditor.setPredefinedValues(predefinedValues);
        formatEditor.setToolTip(getTooltipText(dateStyle, timeStyle, environment));
        layout.addWidget(formatEditor);
        setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Fixed);
    }
    
    public final String getDefaultValue(){
        return getQtFormat(dateStyle, timeStyle, getEnvironment().getLocale());
    }
    
    private final List<String> createPredefinedValues(){        
        final List<String> predefinedValues = new LinkedList<>();
        final List<String> valuesForCurrentLanguage = 
                createPredefinedValues(getEnvironment().getLanguage());
        final String defaultValue = getDefaultValue();
        predefinedValues.add(defaultValue);
        for (String value: valuesForCurrentLanguage){            
            if (!predefinedValues.contains(value)){
                predefinedValues.add(value);                
            }
        }
        if (getEnvironment().getLanguage()!=EIsoLanguage.ENGLISH){
            final List<String> valuesForEnglishLanguage = 
                    createPredefinedValues(EIsoLanguage.ENGLISH);
            for (String value: valuesForEnglishLanguage){            
                if (!predefinedValues.contains(value)){
                    predefinedValues.add(value);
                }
            }            
        }
        return predefinedValues;
    }
    
    private final List<String> createPredefinedValues(final EIsoLanguage language){
        final List<String> predefinedValues = new LinkedList<>();
        final EnumSet<EIsoCountry> countries = LocaleManager.getCountriesForLanguage(language);
        for (EIsoCountry country: countries){
            final Locale locale = new Locale(language.getValue(), country.getAlpha2Code());
            final String qtFormat = getQtFormat(dateStyle, timeStyle, locale);
            predefinedValues.add(qtFormat);
        }
        return predefinedValues;
    }
    
    public static String getFormattedTime(final IClientEnvironment environment, final String qtFormat, final Timestamp time){
        final EditMaskDateTime mask = new EditMaskDateTime(qtFormat, null, null);
        return mask.toStr(environment, time);
    }
    
    @SuppressWarnings("unused")
    private final void onFormatChanged(){
        valueChanged.emit(getCurrentFormat());
    }
    
    private String getCurrentFormat(){
        final String format = formatEditor.getValue();
        if (dateStyle==null || dateStyle==EDateTimeStyle.NONE){
            return EditMaskDateTime.stripDateFormat(format);
        }else if (timeStyle==null || timeStyle==EDateTimeStyle.NONE){
            return EditMaskDateTime.stripTimeFormat(format);
        }else{
            return format;
        } 
    }
    
    @SuppressWarnings("unused")
    private final void onFormatEditingFinished(){
        final String format = formatEditor.getValue();
        if (format!=null && format.isEmpty()){
            formatEditor.setValue(getDefaultValue());
        }        
    }
        
    public static String getSettingName(final EDateTimeStyle dateStyle, final EDateTimeStyle timeStyle){
        if (dateStyle!=null && dateStyle!=EDateTimeStyle.NONE){
            return dateStyle.getName().toLowerCase();
        }else if (timeStyle!=null && timeStyle!=EDateTimeStyle.NONE){
            return timeStyle.getName().toLowerCase();
        }else{
            throw new IllegalArgumentException("One of dateStyle or timeStyle must be defined");
        }        
    }
    
    private static String getQtFormat(final EDateTimeStyle dateStyle,
                                              final EDateTimeStyle timeStyle, 
                                              final java.util.Locale locale){        
        final DateFormat format;
        if (dateStyle!=null && dateStyle != EDateTimeStyle.NONE) {
            format = DateFormat.getDateInstance(dateStyle.getJavaDateTimeStyle(), locale);
        } else if (timeStyle != null && timeStyle!=EDateTimeStyle.NONE) {
            format = DateFormat.getTimeInstance(timeStyle.getJavaDateTimeStyle(), locale);
        }else{
            return "";
        }
        final String javaFormat = ((java.text.SimpleDateFormat)format).toPattern();
        return EditMaskDateTime.javaFormat2QtFormat(locale, javaFormat);
    }    
    

    @Override
    public void readSettings(final IExplorerSettings src) {
        final String format = src.readString(getSettingCfgName(),null);
        if (format==null || format.isEmpty()){
            formatEditor.setValue(getDefaultValue());
        }else{
            formatEditor.setValue(format);
        }
    }

    @Override
    public void writeSettings(final IExplorerSettings dst) {
        final String format = getCurrentFormat();
        if (format==null || format.isEmpty() || format.equals(getDefaultValue())){
            dst.remove(getSettingCfgName());            
        }else{
            dst.writeString(getSettingCfgName(), format);
        }
    }

    @Override
    public void restoreDefaults() {
        formatEditor.setValue(getDefaultValue());
    }
    
    private static String getTooltipText(final EDateTimeStyle dateStyle, final EDateTimeStyle timeStyle, final IClientEnvironment environment) {
        final HtmlTableBuilder tooltip = new HtmlTableBuilder();
        final MessageProvider mp = environment.getMessageProvider();
        
        tooltip.putTwoCellHeader(mp.translate("EditMask", "Expression"),
                mp.translate("EditMask", "Output"));
                
        //table
        if (dateStyle==null || dateStyle==EDateTimeStyle.NONE){
            tooltip.putTwoCellRow("h", mp.translate("EditMask", "The hour without a leading zero (0 to 23 or 1 to 12 if AM/PM display)"));
            tooltip.putTwoCellRow("hh", mp.translate("EditMask", "The hour with a leading zero (00 to 23 or 01 to 12 if AM/PM display)"));
            tooltip.putTwoCellRow("H", mp.translate("EditMask", "The hour without a leading zero (0 to 23, even with AM/PM display)"));
            tooltip.putTwoCellRow("HH", mp.translate("EditMask", "The hour with a leading zero (00 to 23, even with AM/PM display)"));
            tooltip.putTwoCellRow("m", mp.translate("EditMask", "The minute without a leading zero (0 to 59)"));
            tooltip.putTwoCellRow("mm", mp.translate("EditMask", "The minute with a leading zero (00 to 59)"));
            tooltip.putTwoCellRow("s", mp.translate("EditMask", "The second without a leading zero (0 to 59)"));
            tooltip.putTwoCellRow("ss", mp.translate("EditMask", "The second with a leading zero (00 to 59)"));
            tooltip.putTwoCellRow("z", mp.translate("EditMask", "The milliseconds without leading zeroes (0 to 999)"));
            tooltip.putTwoCellRow("zzz", mp.translate("EditMask", "The milliseconds with leading zeroes (000 to 999)"));
            tooltip.putTwoCellRow("AP or A", mp.translate("EditMask", "Interpret as an AM/PM time. AP must be either \"AM\" or \"PM\"."));
            tooltip.putTwoCellRow("ap or a", mp.translate("EditMask", "Interpret as an AM/PM time. ap must be either \"am\" or \"pm\"."));
            tooltip.putTwoCellRow("T", mp.translate("EditMask", "Time zone."));
            tooltip.putTwoCellRow("Z", mp.translate("EditMask", "Time zone in RFC 822 format."));
            tooltip.putTwoCellRow("X", mp.translate("EditMask", "Time zone in ISO 8601 format."));            
        }else{
            tooltip.putTwoCellRow("d", mp.translate("EditMask", "The day as number without a leading zero (1 to 31)."));
            tooltip.putTwoCellRow("dd", mp.translate("EditMask", "The day as number with a leading zero (01 to 31)."));
            tooltip.putTwoCellRow("ddd", mp.translate("EditMask","The abbreviated localized day name (e.g. 'Mon' to 'Sun')."));
            tooltip.putTwoCellRow("dddd", mp.translate("EditMask", "The long localized day name (e.g. 'Monday' to 'Sunday')."));
            tooltip.putTwoCellRow("M", mp.translate("EditMask", "The month as number without a leading zero (1-12)."));
            tooltip.putTwoCellRow("MM", mp.translate("EditMask", "The month as number with a leading zero (01-12)."));
            tooltip.putTwoCellRow("MMM", mp.translate("EditMask", "The abbreviated localized month name (e.g. 'Jan' to 'Dec')."));
            tooltip.putTwoCellRow("MMMM", mp.translate("EditMask", "The long localized month name (e.g. 'January' to 'December')."));
            tooltip.putTwoCellRow("yy", mp.translate("EditMask", "The year as two digit number (00-99)"));
            tooltip.putTwoCellRow("yyyy", mp.translate("EditMask", "The year as four digit number"));
        }
        
        return tooltip.toString();
    }
    
}
