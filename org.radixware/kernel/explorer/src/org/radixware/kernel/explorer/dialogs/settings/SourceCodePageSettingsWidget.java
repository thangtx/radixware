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

package org.radixware.kernel.explorer.dialogs.settings;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.IExplorerSettings;


final class SourceCodePageSettingsWidget extends SettingsWidget {
    
    public final static String DEFAULT = "DFLT";
    private final ArrayList<SettingsWidget> settingsArrayList = new ArrayList<>();
    private boolean isDefault;
    private final SourceCodePageSettingsWidget srcEditorSettingsWidget;
    public final Signal1<SourceCodePageSettingsWidget> updateSrcEditorSettingsSignal = new Signal1<SourceCodePageSettingsWidget>();

    public SourceCodePageSettingsWidget(final IClientEnvironment environment, final QWidget parent, final String group, final String subgroup, final String name, SourceCodePageSettingsWidget srcEditorSettingsWidget) {
        super(environment, parent, group, subgroup, name);
        this.srcEditorSettingsWidget=srcEditorSettingsWidget;
        final QVBoxLayout gridLayout = new QVBoxLayout();
        gridLayout.setAlignment(new Alignment(AlignmentFlag.AlignLeft, AlignmentFlag.AlignTop));

        isDefault=srcEditorSettingsWidget==null;//name.equals(SettingNames.SourceCode.Syntax.DEFAULT);
        if(!isDefault){
            String desc=Application.translate("Settings Dialog", "Default");
            CheckBox cbDefault=new CheckBox(environment, parent, group, subgroup,  name + "/" + DEFAULT,  desc);
            gridLayout.addWidget(cbDefault);
            settingsArrayList.add(cbDefault);
        }        
        PropertySettingsWidget w12 = new PropertySettingsWidget(environment, this, group, subgroup, name,/*SettingNames.SourceCode.Syntax.COMMENTS*/ Application.translate("Settings Dialog", "Readonly Properties Value"), true);
        if(isDefault){
            w12.updateBgrnColorSignal.connect(this,"changeBackgroundColor(QColor,String)" );
            w12.updateFgrnColorSignal.connect(this,"changeForegraundColor(QColor,String)" );
            w12.updateFontSignal.connect(this,"changeFont(QFont,String)" );
        }
        gridLayout.addWidget(w12);
        settingsArrayList.add(w12);
        this.setLayout(gridLayout);
    }
    
    private void changeForegraundColor(QColor c,String s){
        updateSrcEditorSettingsSignal.emit(this);
    }
    
    private void changeBackgroundColor(QColor c,String s){
        updateSrcEditorSettingsSignal.emit(this);
    }
    
    private void changeFont(QFont f,String s){
        updateSrcEditorSettingsSignal.emit(this);
    }
    
    private void cbDefaultChanged(Integer state){
        boolean enabled=Qt.CheckState.Unchecked.value()==state;      
        for (SettingsWidget w : settingsArrayList) {
            if(w instanceof PropertySettingsWidget){
                if(!enabled){
                    QFont font=srcEditorSettingsWidget.getPropertySettingsWidget().getFont();
                    QColor frgColor=srcEditorSettingsWidget.getPropertySettingsWidget().getForegroundColor();
                    QColor bgrColor=srcEditorSettingsWidget.getPropertySettingsWidget().getBackgroundColor();
                    change(frgColor, bgrColor,  font);
                }            
                w.setEnabled(enabled);
            }            
        }        
    }
    
    public void change( QColor frgColor,QColor bgrColor, QFont font){
        if(getCheckBoxSettingsWidget()!=null){
            Qt.CheckState checkState=getCheckBoxSettingsWidget().bSaveCheckBox.checkState();
            if(checkState==Qt.CheckState.Checked){
                getPropertySettingsWidget().setBackgroundColor(bgrColor);
                getPropertySettingsWidget().setForegroundColor(frgColor);
                getPropertySettingsWidget().setSettingsFont(font);
            }
        }
    }

    @Override
    public void restoreDefaults() {
        for (SettingsWidget w : settingsArrayList) {
            w.restoreDefaults();
        }
    }

    @Override
    public void readSettings(IExplorerSettings src) {
        getPropertySettingsWidget().readSettings(src);
        CheckBoxSettingsWidget checkBox=getCheckBoxSettingsWidget();
        if(checkBox!=null)
            checkBox.readSettings(src);
    }

    @Override
    public void writeSettings(IExplorerSettings dst) {
        for (SettingsWidget w : settingsArrayList) {
            w.writeSettings(dst);
        }
    }

    public PropertySettingsWidget getPropertySettingsWidget() {
        if(isDefault){
             return (PropertySettingsWidget) settingsArrayList.get(0);
        }
        return (PropertySettingsWidget) settingsArrayList.get(1);
    }
    
     public CheckBoxSettingsWidget getCheckBoxSettingsWidget() {
         if(isDefault){
             return null;
        }
        return (CheckBoxSettingsWidget) settingsArrayList.get(0);
    }
     
     class CheckBox extends CheckBoxSettingsWidget{
         
         CheckBox(final IClientEnvironment environment, final QWidget parent, final String gr, final String sub, final String n, final String descr){
             super(environment, parent, gr, sub, n, descr); 

         }
         
        @Override
         protected void onChangeCheckBox() {
            cbDefaultChanged(bSaveCheckBox.checkState().value());
        }
     }
}
