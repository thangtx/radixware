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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QFormLayout;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.enums.ETriadDelimeterType;
import org.radixware.kernel.explorer.editors.valeditors.ValListEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValNumEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.IExplorerSettings;


final class FormatSettingsWidget extends SettingsWidget implements ISettingsPage{
    
    private final static String GROUPING_SEPARATOR_SETTING_NAME=SettingNames.SYSTEM+"/"
        +SettingNames.FORMAT_SETTINGS+"/"
        +SettingNames.FormatSettings.NUMBER+"/"+SettingNames.FormatSettings.Number.GROUP_SEPARATOR;
    private final static String DECIMAL_MARK_SETTING_NAME=SettingNames.SYSTEM+"/"
        +SettingNames.FORMAT_SETTINGS+"/"
        +SettingNames.FormatSettings.NUMBER+"/"+SettingNames.FormatSettings.Number.DECIMAL_PART_SEPARATOR;
    
    private ValListEditor groupingSeparatorEditor;
    private char spaceChar = '\u00A0';
    private ValListEditor decimalMarkEditor;
    private ValNumEditor valNumExample;
    private DateTimeFormatsEditor dateFormatEditor;
    private DateTimeFormatsEditor timeFormatEditor;
    
    public FormatSettingsWidget(final IClientEnvironment environment, final QWidget parentWidget){
        super(environment, parentWidget, null, null, null);        
    }

    @Override
    public void open(final IClientEnvironment environment, 
                             final ISettingsProvider settingsProvider,
                             final List<SettingsWidget> settingWidgets) {
        setupUi();
        settingWidgets.add(this);
        readSettings(settingsProvider.getSettings());
    }
    
    private DecimalFormatSymbols getDefaultFormatSymbols(){
        final NumberFormat numberFormat = NumberFormat.getNumberInstance(getEnvironment().getLocale());
        if (numberFormat instanceof DecimalFormat) {
            final DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            final DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
            return symbols;
        }else{
            return new DecimalFormatSymbols(getEnvironment().getLocale());
        }
    }
    
    private boolean isGroupingUsed(){
        final NumberFormat numberFormat = NumberFormat.getNumberInstance(getEnvironment().getLocale());
        return numberFormat==null ? false : numberFormat.isGroupingUsed();
    }
    
    private void setupUi(){
        final MessageProvider mp = getEnvironment().getMessageProvider();
        final DecimalFormatSymbols defaultSymbols = getDefaultFormatSymbols();
        final char defaultGroupingSeparator = isGroupingUsed() ? defaultSymbols.getGroupingSeparator() : '\0';
        if (defaultGroupingSeparator==' '){
            spaceChar = ' ';
        }
        final char defaultDecimalMark = defaultSymbols.getDecimalSeparator();
        
        final QVBoxLayout mainLayout = new QVBoxLayout(this);
        mainLayout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignTop));
        {
            final QGroupBox gbNumberFormat = new QGroupBox(mp.translate("Settings Dialog", "Number Format:"), this);
            final QFormLayout layout = new QFormLayout(gbNumberFormat);
            {
                final List<EditMaskList.Item> groupSeparators = new LinkedList<>();
                groupSeparators.add(new EditMaskList.Item(",", ','));
                groupSeparators.add(new EditMaskList.Item(".", '.'));
                groupSeparators.add(new EditMaskList.Item(mp.translate("Settings Dialog", "Space"), spaceChar));
                groupSeparators.add(new EditMaskList.Item("\'", '\''));
                groupSeparators.add(new EditMaskList.Item("`", '`'));
                groupSeparators.add(new EditMaskList.Item(mp.translate("Settings Dialog", "None"), '\0'));
                boolean defaultSeparatorPresent = false;
                for (EditMaskList.Item item: groupSeparators){
                    if (Character.valueOf(defaultGroupingSeparator).equals(item.getValue())){
                        defaultSeparatorPresent = true;
                        break;
                    }
                }
                if (!defaultSeparatorPresent){
                    groupSeparators.add(new EditMaskList.Item(String.valueOf(defaultGroupingSeparator), defaultGroupingSeparator));
                }
                groupingSeparatorEditor = new ValListEditor(getEnvironment(), gbNumberFormat, groupSeparators);
                groupingSeparatorEditor.setMandatory(true);
                groupingSeparatorEditor.setValue(defaultGroupingSeparator);
                groupingSeparatorEditor.valueChanged.connect(this,"onChangeNumberFormat()");
                layout.addRow(mp.translate("Settings Dialog", "Thousands separator:"), groupingSeparatorEditor);
            }
            {
                final List<EditMaskList.Item> decimalMarks = new LinkedList<>();
                decimalMarks.add(new EditMaskList.Item(".", '.'));
                decimalMarks.add(new EditMaskList.Item(",", ','));
                boolean defaultMarkPresent = false;
                for (EditMaskList.Item item: decimalMarks){
                    if (Character.valueOf(defaultDecimalMark).equals(item.getValue())){
                        defaultMarkPresent = true;
                        break;
                    }
                }
                if (!defaultMarkPresent){
                    decimalMarks.add(new EditMaskList.Item(String.valueOf(defaultGroupingSeparator), defaultGroupingSeparator));
                }
                decimalMarkEditor = new ValListEditor(getEnvironment(), gbNumberFormat, decimalMarks);
                decimalMarkEditor.setMandatory(true);
                decimalMarkEditor.setValue(defaultDecimalMark);
                decimalMarkEditor.valueChanged.connect(this,"onChangeNumberFormat()");
                layout.addRow(mp.translate("Settings Dialog", "Decimal separator:"), decimalMarkEditor);
            }
            {                
                valNumExample = new ValNumEditor(getEnvironment(), gbNumberFormat, createEditMaskNum(), true, true);
                valNumExample.setValue(new BigDecimal("12345.6789"));
                layout.addRow(mp.translate("Settings Dialog", "Example:"), valNumExample);
            }
            mainLayout.addWidget(gbNumberFormat);
        }
        {
            final QGroupBox gbDateFormat = new QGroupBox(mp.translate("Settings Dialog", "Date Format:"), this);
            final QVBoxLayout layout = new QVBoxLayout(gbDateFormat);
            dateFormatEditor= new DateTimeFormatsEditor(getEnvironment(), 
                                                                                     gbDateFormat, 
                                                                                     SettingNames.FORMAT_SETTINGS,
                                                                                     SettingNames.FormatSettings.DATE, 
                                                                                     DateTimeFormatsEditor.EFormatType.DATE);
            layout.addWidget(dateFormatEditor);
            mainLayout.addWidget(gbDateFormat);
        }
        {
            final QGroupBox gbTimeFormat = new QGroupBox(mp.translate("Settings Dialog", "Time Format:"), this);
            final QVBoxLayout layout = new QVBoxLayout(gbTimeFormat);
            timeFormatEditor= new DateTimeFormatsEditor(getEnvironment(), 
                                                                                     gbTimeFormat, 
                                                                                     SettingNames.FORMAT_SETTINGS,
                                                                                     SettingNames.FormatSettings.TIME, 
                                                                                     DateTimeFormatsEditor.EFormatType.TIME);
            layout.addWidget(timeFormatEditor);
            mainLayout.addWidget(gbTimeFormat);
        }
        final QPushButton pbRestoreDefaults = new QPushButton(mp.translate("Settings Dialog", "Restore default formats"), this);
        mainLayout.addWidget(pbRestoreDefaults,0,Qt.AlignmentFlag.AlignLeft);
        pbRestoreDefaults.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CANCEL));
        pbRestoreDefaults.clicked.connect(this, "restoreDefaults()");
    }        
    
    private EditMaskNum createEditMaskNum(){
        final Character groupSeparator = (Character)groupingSeparatorEditor.getValue();
        final Character decimalMark = (Character)decimalMarkEditor.getValue();
        if (groupSeparator=='\0'){
            return new EditMaskNum(null, null, 1, ETriadDelimeterType.NONE, null, decimalMark, (byte)4);
        }else{
            return new EditMaskNum(null, null, 1, ETriadDelimeterType.SPECIFIED, groupSeparator, decimalMark, (byte)4);
        }
    }
    
    @SuppressWarnings("unused")
    private void onChangeNumberFormat(){
        valNumExample.setEditMask(createEditMaskNum());
        valNumExample.refresh();
    }

    @Override
    public void readSettings(final IExplorerSettings src) {
        final DecimalFormatSymbols defaultSymbols = getDefaultFormatSymbols();
        
        final String groupSeparator = src.readString(GROUPING_SEPARATOR_SETTING_NAME, null);
        if (groupSeparator==null || groupSeparator.isEmpty()){
            groupingSeparatorEditor.setValue(isGroupingUsed() ? defaultSymbols.getGroupingSeparator() : '\0');
        }else{
            final char groupSeparatorChar = groupSeparator.charAt(0);
            if (groupSeparatorChar==' ' || groupSeparatorChar=='\u00A0'){
                groupingSeparatorEditor.setValue(spaceChar);
            }else{
                groupingSeparatorEditor.setValue(groupSeparatorChar);
            }
        }
        
        final String decimalMark = src.readString(DECIMAL_MARK_SETTING_NAME, null);
        if (decimalMark==null || decimalMark.isEmpty()){
            decimalMarkEditor.setValue(defaultSymbols.getDecimalSeparator());
        }else{
            decimalMarkEditor.setValue(decimalMark.charAt(0));
        }
        
        dateFormatEditor.readSettings(src);
        timeFormatEditor.readSettings(src);
    }

    @Override
    public void writeSettings(final IExplorerSettings dst) {
        final DecimalFormatSymbols defaultSymbols = getDefaultFormatSymbols();
        
        final char groupSeparator = (Character)groupingSeparatorEditor.getValue();
        final char defaultGroupingSeparator = isGroupingUsed() ? defaultSymbols.getGroupingSeparator() : '\0';
        if (groupSeparator==defaultGroupingSeparator){
            dst.remove(GROUPING_SEPARATOR_SETTING_NAME);
        }else{
            dst.writeString(GROUPING_SEPARATOR_SETTING_NAME, String.valueOf(groupSeparator));
        }
        
        final char decimalMark = (Character)decimalMarkEditor.getValue();
        if (decimalMark==defaultSymbols.getDecimalSeparator()){
            dst.remove(DECIMAL_MARK_SETTING_NAME);
        }else{
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