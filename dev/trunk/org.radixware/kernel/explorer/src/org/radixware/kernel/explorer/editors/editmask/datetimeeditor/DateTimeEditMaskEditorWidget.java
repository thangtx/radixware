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

import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.widgets.IEditMaskEditor;
import org.radixware.kernel.common.enums.EDateTimeStyle;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.widgets.ExplorerFrame;



public class DateTimeEditMaskEditorWidget extends ExplorerFrame implements IEditMaskEditor {
    private final EnumMap<EEditMaskOption, IEditMaskEditorSetting> widgets = 
            new EnumMap<>(EEditMaskOption.class);
    private final IClientEnvironment environment;
    private final DateStyleSetting dateStyle;
    private final TimeStyleSetting timeStyle;
    private final DateTimeMaskSetting mask;
    private final DateTimeMinimumValueSetting minVal;
    private final DateTimeMaximumValueSetting maxVal;
    
    @SuppressWarnings("LeakingThisInConstructor")
    public DateTimeEditMaskEditorWidget(final IClientEnvironment environment, final QWidget parent) {
        super(environment,parent);
        this.environment = environment;
        this.setFrameShape(Shape.StyledPanel);
        
        dateStyle = new DateStyleSetting(environment, this);
        widgets.put(dateStyle.getOption(), dateStyle);
        dateStyle.valueChanged.connect(this, "onDateStyleChange(org.radixware.kernel.common.enums.EDateTimeStyle)");
        timeStyle = new TimeStyleSetting(environment, this);
        widgets.put(timeStyle.getOption(), timeStyle);
        timeStyle.valueChanged.connect(this, "onTimeStyleChange(org.radixware.kernel.common.enums.EDateTimeStyle)");
        minVal = new DateTimeMinimumValueSetting(environment, this);
        widgets.put(minVal.getOption(), minVal);
        maxVal = new DateTimeMaximumValueSetting(environment, this);
        widgets.put(maxVal.getOption(), maxVal);
        mask = new DateTimeMaskSetting(environment, this);
        mask.toggled.connect(this, "onMaskToggle(Boolean)");
        widgets.put(mask.getOption(), mask);
                
        final QVBoxLayout layout = new QVBoxLayout();
        layout.setAlignment(new Alignment(AlignmentFlag.AlignTop, AlignmentFlag.AlignLeft));
        
        layout.addWidget(dateStyle);
        layout.addWidget(timeStyle);
        layout.addWidget(mask);
        minVal.setLabelWidth(maxVal.getLabelWidth());
        layout.addWidget(minVal);
        layout.addWidget(maxVal);
        
        
        this.setLayout(layout);
    }
    
    @Override
    public EditMask getEditMask() {
        final org.radixware.schemas.editmask.RadixEditMaskDocument xmlBeanInstance = 
                org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.newInstance();
        final org.radixware.schemas.editmask.EditMask editMask = 
                xmlBeanInstance.addNewRadixEditMask();
        editMask.addNewDateTime();
        final Set<EEditMaskOption> keySet = widgets.keySet();
        for(EEditMaskOption key : keySet) {
            widgets.get(key).addToXml(editMask);
        }
        
        return EditMask.loadFrom(xmlBeanInstance);
    }

    @Override
    public void setEditMask(final EditMask editMask) {
        if(editMask instanceof EditMaskDateTime) {
            final org.radixware.schemas.editmask.RadixEditMaskDocument xmlBeanInstance
                    = org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.newInstance();
            final org.radixware.schemas.editmask.EditMask em = 
                    xmlBeanInstance.addNewRadixEditMask();
            editMask.writeToXml(em);
            final Set<EEditMaskOption> keySet = widgets.keySet();
            for(EEditMaskOption key : keySet) {
                widgets.get(key).loadFromXml(em);
            }
        } else {
            throw new IllegalArgumentError("Instance of EditMaskDateTime expected");
        }
    }

    @Override
    public void setHiddenOptions(final EnumSet<EEditMaskOption> options) {
        for(EEditMaskOption key : options) {
            final IEditMaskEditorSetting setting = widgets.get(key);
            if(setting != null) {
                setting.hide();
            }
        }
    }

    @Override
    public void setVisibleOptions(final EnumSet<EEditMaskOption> options) {
        for(EEditMaskOption key : options) {
            final IEditMaskEditorSetting setting = widgets.get(key);
            if(setting != null) {
                setting.show();
            }
        }
    }

    @Override
    public void setEnabledOptions(final EnumSet<EEditMaskOption> options) {
        for(EEditMaskOption key : options) {
            final IEditMaskEditorSetting setting = widgets.get(key);
            if(setting != null) {
                setting.setEnabled(true);
            }
        }
    }

    @Override
    public void setDisabledOptions(final EnumSet<EEditMaskOption> options) {
        for(EEditMaskOption key : options) {
            final IEditMaskEditorSetting setting = widgets.get(key);
            if(setting != null) {
                setting.setDisabled(true);
            }
        }
    }

    @Override
    public boolean checkOptions() {
        if (timeStyle.getValue() == EDateTimeStyle.NONE && dateStyle.getValue() == EDateTimeStyle.NONE) {
            final String msg = environment.getMessageProvider().translate("EditMask", "Incorrect input: date or time style should be set");
            environment.messageError(msg);            
            return false;    
        }
        if (minVal.checkInput() && maxVal.checkInput()){
            final long max = maxVal.getValue().getTime();
            final long min = minVal.getValue().getTime();
            if(max < min) {
                final String msg = environment.getMessageProvider().translate("EditMask", "Incorrect input: maximum value is less than minimum value");
                environment.messageError(msg);            
                minVal.setFocus();
                return false;
            }       
            return true;
        }else{
            return false;
        }
    }
    
    private void onDateStyleChange(final org.radixware.kernel.common.enums.EDateTimeStyle style) {
        mask.setChecked(style == EDateTimeStyle.CUSTOM);
        if(style == EDateTimeStyle.CUSTOM) {
            timeStyle.setValue(style);
        } else if(timeStyle.getValue().equals(EDateTimeStyle.CUSTOM)) {
            timeStyle.setValue(EDateTimeStyle.DEFAULT);
        }
        
    } 
    
    private void onTimeStyleChange(final org.radixware.kernel.common.enums.EDateTimeStyle style) {
        mask.setChecked(style == EDateTimeStyle.CUSTOM);
        if(style == EDateTimeStyle.CUSTOM) {
            dateStyle.setValue(style);
        } else if(dateStyle.getValue().equals(EDateTimeStyle.CUSTOM)) {
            dateStyle.setValue(EDateTimeStyle.DEFAULT);
        }
        
    }
    
    private void onMaskToggle(final Boolean state) {
        EDateTimeStyle styleToSet = state ? EDateTimeStyle.CUSTOM : EDateTimeStyle.DEFAULT;
        dateStyle.setValue(styleToSet);
        timeStyle.setValue(styleToSet);
    }
}
