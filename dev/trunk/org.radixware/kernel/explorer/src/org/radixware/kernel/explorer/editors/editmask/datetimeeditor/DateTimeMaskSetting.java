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

package org.radixware.kernel.explorer.editors.editmask.datetimeeditor;

import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.utils.html.HtmlTableBuilder;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.schemas.editmask.EditMask;


final class DateTimeMaskSetting extends QWidget implements IEditMaskEditorSetting {
    private QCheckBox checkBox;
    private ValStrEditor valueEditor;
    
    public final Signal1<Boolean> toggled = new Signal1<>();
    
    public DateTimeMaskSetting(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        setUpUi(environment);
        setTooltipText(environment);
        setDefaultValue();
        checkBox.clicked.connect(toggled);
    }
    
    @Override
    public void addToXml(final EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskDateTime editMaskDateTime = 
                editMask.getDateTime();
        if(checkBox.isChecked()) {
            editMaskDateTime.setMask(valueEditor.getValue());
        }
    }

    @Override
    public void loadFromXml(final EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskDateTime editMaskDateTime = 
                editMask.getDateTime();
        if(editMaskDateTime.isSetMask()) {
            valueEditor.setValue(editMaskDateTime.getMask());
            checkBox.setChecked(true);
        } else {
            setDefaultValue();
            checkBox.setChecked(false);
        }
        
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.DATETIME_QTMASK;
    }
    
    @Override
    public void setDefaultValue() {
        valueEditor.setValue("dd.MM.yyyy hh:mm:ss");
    }

    @Override
    public String getValue() {
        return valueEditor.getValue();
    }
    
    private void onCheck() {
        switch(checkBox.checkState()) {
            case Checked:
                valueEditor.setEnabled(true);
                break;
            case Unchecked:
                setDefaultValue();
                valueEditor.setDisabled(true);
                break;
            default:
                break;
        }
    }

    private void setUpUi(final IClientEnvironment environment) {
        checkBox = new QCheckBox(this);
        final String checkBoxTitle = environment.getMessageProvider().translate(
                "EditMask", 
                "Use Qt date/time mask:");
        checkBox.setText(checkBoxTitle);
        checkBox.setChecked(false);
        checkBox.stateChanged.connect(this, "onCheck()");
        valueEditor = new ValStrEditor(environment, this);
        valueEditor.setDisabled(true);
        
                
        final QVBoxLayout layout = new QVBoxLayout();
        layout.setMargin(0);
        layout.addWidget(checkBox);
        layout.addWidget(valueEditor);
        this.setLayout(layout);
    }

    private void setTooltipText(final IClientEnvironment environment) {
        final HtmlTableBuilder tooltip = new HtmlTableBuilder();
        final MessageProvider mp = environment.getMessageProvider();
        
        tooltip.putTwoCellHeader(mp.translate("EditMask", "Expression"),
                mp.translate("EditMask", "Output"));
                
        //table
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
        
        valueEditor.setToolTip(tooltip.toString());
    }
    
    public void setChecked(final boolean flag) {
        checkBox.setChecked(flag);
    }
}
