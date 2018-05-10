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

import com.trolltech.qt.QSignalEmitter;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.enums.EDateTimeStyle;
import org.radixware.kernel.explorer.env.IExplorerSettings;


final class DateTimeFormatsEditor extends SettingsWidget{
    
    public static enum EFormatType{DATE,TIME};
    
    private final static EDateTimeStyle FORMAT_STYLES[] = 
        {EDateTimeStyle.SHORT,EDateTimeStyle.MEDIUM,EDateTimeStyle.DEFAULT,EDateTimeStyle.LONG,EDateTimeStyle.FULL};
    
    private final QGridLayout layout = new QGridLayout(this);
    private final List<SettingsWidget> formatSettings = new LinkedList<>();
    private final Map<DateTimeFormatSettingWidget,QLabel> examplesBySettingsWidget = new HashMap<>();    
    private final Timestamp currentTime;
    private final EFormatType formatType;

    public DateTimeFormatsEditor(final IClientEnvironment environment, 
                                                  final QWidget parentWidget,
                                                  final String groupName,
                                                  final String subGroupName,
                                                  final EFormatType formatType){
        super(environment, parentWidget, groupName, subGroupName, null);
        currentTime = environment.getCurrentServerTime();
        this.formatType = formatType;
        setupUi();
    }
    
    private void setupUi(){
        final MessageProvider mp = getEnvironment().getMessageProvider();
        layout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignTop));
        final QLabel lbFormatTitle = new QLabel(mp.translate("Settings Dialog", "Format:"), this);
        layout.addWidget(lbFormatTitle, 0, 0, 1, 2, Qt.AlignmentFlag.AlignCenter);
        final QLabel lbExampleTilte = new QLabel(mp.translate("Settings Dialog", "Example:"), this);
        layout.addWidget(lbExampleTilte, 0, 2, 1, 1, Qt.AlignmentFlag.AlignCenter);
        for (int i=0; i<FORMAT_STYLES.length; i++){
            if (formatType==EFormatType.DATE){
                addRow(i+1, FORMAT_STYLES[i], null);
            }else{
                addRow(i+1, null, FORMAT_STYLES[i]);
            }
        }
    }
    
    private void addRow(final int row, final EDateTimeStyle dateStyle, final EDateTimeStyle timeStyle){
        final EDateTimeStyle style = dateStyle==null || dateStyle==EDateTimeStyle.NONE ? timeStyle : dateStyle;
        final QLabel lbStyleTitle = new QLabel(getStyleTitle(style), this);
        layout.addWidget(lbStyleTitle, row, 0);
        final DateTimeFormatSettingWidget formatSetting = 
            new DateTimeFormatSettingWidget(getEnvironment(), this, group, subGroup, dateStyle, timeStyle);
        formatSettings.add(formatSetting);
        layout.addWidget(formatSetting, row, 1);
        final QLabel lbExample = new QLabel(this);
        examplesBySettingsWidget.put(formatSetting, lbExample);
        formatSetting.valueChanged.connect(this,"formatChanged(String)");
        layout.addWidget(lbExample, row, 2);
        updateExampleText(lbExample, formatSetting.getDefaultValue());
    }
    
    @SuppressWarnings("unused")
    private void formatChanged(final String newFormat){
        final QSignalEmitter signalSender = QObject.signalSender();
        if (signalSender instanceof DateTimeFormatSettingWidget){
            final DateTimeFormatSettingWidget settingWidget = (DateTimeFormatSettingWidget)signalSender;
            final QLabel lbExample = examplesBySettingsWidget.get(settingWidget);            
            if (lbExample!=null){
                final String format = newFormat==null ? settingWidget.getDefaultValue() : newFormat;
                updateExampleText(lbExample, format);
            }            
        }
    }
    
    private void updateExampleText(final QLabel lbExample, final String format){
        final EditMaskDateTime mask = new EditMaskDateTime(format, null, null);
        lbExample.setText(mask.toStr(getEnvironment(), currentTime));
    }
    
    private String getStyleTitle(final EDateTimeStyle style){
        final MessageProvider mp = getEnvironment().getMessageProvider();
        switch(style){
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
                throw new IllegalArgumentException("Unsupported format "+style.name());
        }
    }

    @Override
    public void readSettings(final IExplorerSettings src) {
        for (SettingsWidget settingsWidget: formatSettings){
            settingsWidget.readSettings(src);
        }
    }

    @Override
    public void writeSettings(final IExplorerSettings dst) {
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
