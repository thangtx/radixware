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

package org.radixware.kernel.explorer.editors.editmask.inteditor;

import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.widgets.IEditMaskEditor;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.widgets.ExplorerFrame;
import org.radixware.schemas.editmask.RadixEditMaskDocument;


public final class IntEditMaskEditorWidget extends ExplorerFrame implements IEditMaskEditor{
    private final IClientEnvironment environment;
    private QVBoxLayout mainLayout;
    private final EnumMap<EEditMaskOption, IEditMaskEditorSetting> widgets 
            = new EnumMap<EEditMaskOption, IEditMaskEditorSetting>(EEditMaskOption.class);
    
    public IntEditMaskEditorWidget(final IClientEnvironment environment, final QWidget parent) {
        super(environment,parent);
        this.environment = environment;
        setUpUi(environment);
        this.setLayout(mainLayout);
        this.setFrameShape(Shape.StyledPanel);
    }
    
    private void setUpUi(final IClientEnvironment environment) {
        mainLayout = new QVBoxLayout();
        mainLayout.setAlignment(new Alignment(AlignmentFlag.AlignTop));
        
        final IntNumberBaseSetting numberBase = new IntNumberBaseSetting(environment, this);
        widgets.put(numberBase.getOption(), numberBase); 
        //Minimum length box setting
        final IntPadCharacterSetting padChar = new IntPadCharacterSetting(environment, null);
        widgets.put(padChar.getOption(), padChar);
        final IntMinimumLengthSetting minimumLengthBox = new IntMinimumLengthSetting(environment, this, padChar);
        widgets.put(minimumLengthBox.getOption(), minimumLengthBox);
        
        //minimumLengthBox.addPadChar(padChar);
        //Minimum value box setting
        final IntMinimumValueSetting minimumValueBox = new IntMinimumValueSetting(environment, this);
        widgets.put(minimumValueBox.getOption(), minimumValueBox);
        //Maximum value box setting
        final IntMaximumValueSetting maximumValueBox = new IntMaximumValueSetting(environment, this);
        widgets.put(maximumValueBox.getOption(), maximumValueBox);
        //Step size setting
        final IntStepSizeSetting stepSize = new IntStepSizeSetting(environment, this);
        widgets.put(stepSize.getOption(), stepSize);
        //Triad delimiter box setting
        final IntTriadDelimiterSetting triadDelimiter = new IntTriadDelimiterSetting(environment, this);
        widgets.put(triadDelimiter.getOption(), triadDelimiter);
        
        mainLayout.addWidget(numberBase);
        mainLayout.addWidget(minimumLengthBox);
        mainLayout.addWidget(minimumValueBox);
        mainLayout.addWidget(maximumValueBox);
        mainLayout.addWidget(stepSize);
        mainLayout.addWidget(triadDelimiter);
        mainLayout.update();
    }
    
    @Override
    public EditMask getEditMask() {
        final RadixEditMaskDocument xmlBeanInstance = 
                RadixEditMaskDocument.Factory.newInstance();
        final org.radixware.schemas.editmask.EditMask editMask = xmlBeanInstance.addNewRadixEditMask();
        editMask.addNewInt();
        final Set<EEditMaskOption> keySet = widgets.keySet();
        for(EEditMaskOption key : keySet) {
            widgets.get(key).addToXml(editMask);
        }
        return EditMask.loadFrom(xmlBeanInstance);
    }

    @Override
    public void setEditMask(final EditMask editMask) {
        if(editMask instanceof EditMaskInt) {
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
            throw new IllegalArgumentError("Instance of EditMaskInt expected");
        }
    }

    @Override
    public void setHiddenOptions(final EnumSet<EEditMaskOption> options) {
        IEditMaskEditorSetting setting = null;
        for(EEditMaskOption key : options) {
            setting = widgets.get(key);
            if(setting != null) {
                setting.hide();
            }
        }
    }

    @Override
    public void setVisibleOptions(final EnumSet<EEditMaskOption> options) {
        IEditMaskEditorSetting setting = null;
        for(EEditMaskOption key : options) {
            setting = widgets.get(key);
            if(setting != null) {
                setting.show();
            }
        }
    }

    @Override
    public void setEnabledOptions(final EnumSet<EEditMaskOption> options) {
        IEditMaskEditorSetting setting = null;
        for(EEditMaskOption key : options) {
            setting = widgets.get(key);
            if(setting != null) {
                setting.setEnabled(true);
            }
        }
    }

    @Override
    public void setDisabledOptions(final EnumSet<EEditMaskOption> options) {
        IEditMaskEditorSetting setting = null;
        for(EEditMaskOption key : options) {
            setting = widgets.get(key);
            if(setting != null) {
                setting.setDisabled(true);
            }
        }
    }

    @Override
    public boolean checkOptions() {
        boolean result = true;
        final long max = (Long)widgets.get(EEditMaskOption.INT_MAX_VALUE).getValue();
        final long min = (Long)widgets.get(EEditMaskOption.INT_MIN_VALUE).getValue();
        if( max < min ) {
            final String msg = environment.getMessageProvider().translate("EditMask", "Incorrect input: maximum value is less than minimum value");
            widgets.get(EEditMaskOption.INT_MIN_VALUE).setFocus();
            environment.messageError(msg);
            result = false;
        }
        
        return result;
    }
}
