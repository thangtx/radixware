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

package org.radixware.kernel.explorer.editors.editmask.streditor;

import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.widgets.IEditMaskEditor;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.widgets.ExplorerFrame;
import org.radixware.schemas.editmask.RadixEditMaskDocument;


public class StrEditMaskEditorWidget extends ExplorerFrame implements IEditMaskEditor {
    private final QVBoxLayout mainLayout;
    private final EnumMap<EEditMaskOption, IEditMaskEditorSetting> widgets = 
            new EnumMap<EEditMaskOption, IEditMaskEditorSetting>(EEditMaskOption.class);
    private StrValueControlWidget valueControl;
    private StrMaximumLengthSetting maxLength;
    private int index;
    private final IClientEnvironment environment;
    /**
     * Creates widget for string editing
     * @param environment
     * @param parent 
     */
    public StrEditMaskEditorWidget(final IClientEnvironment environment, final QWidget parent) {
        super(environment,parent);
        setFrameShape(Shape.StyledPanel);
        mainLayout = new QVBoxLayout();
        this.environment = environment;
        setUpUi(environment, false);
        this.setLayout(mainLayout);
    }
    
    public StrEditMaskEditorWidget(final IClientEnvironment environment, final QWidget parent, final boolean isChar) {
        super(environment,parent);
        setFrameShape(Shape.StyledPanel);
        mainLayout = new QVBoxLayout();
        this.environment = environment;
        setUpUi(environment, isChar);
        this.setLayout(mainLayout);
    }
    
    @Override
    public EditMask getEditMask() {
        final RadixEditMaskDocument xmlBeanInstance = 
                RadixEditMaskDocument.Factory.newInstance();
        final org.radixware.schemas.editmask.EditMask editMask = xmlBeanInstance.addNewRadixEditMask();
        editMask.addNewStr();
        final Set<EEditMaskOption> keySet = widgets.keySet();
        for(EEditMaskOption key : keySet) {
            widgets.get(key).addToXml(editMask);
        }
        valueControl.addToXml(editMask);
        
        return EditMask.loadFrom(xmlBeanInstance);
    }

    @Override
    public void setEditMask(final EditMask editMask) {
        if(editMask instanceof EditMaskStr) {
            final org.radixware.schemas.editmask.RadixEditMaskDocument xmlBeanInstance
                    = org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.newInstance();
            final org.radixware.schemas.editmask.EditMask em = 
                    xmlBeanInstance.addNewRadixEditMask();
            editMask.writeToXml(em);
            
            final Set<EEditMaskOption> keySet = widgets.keySet();
            for(EEditMaskOption key : keySet) {
                widgets.get(key).loadFromXml(em);
            }
            valueControl.loadFromXml(em);
            final org.radixware.schemas.editmask.EditMaskStr editMaskStr = em.getStr();
            if(editMaskStr.isSetMaxLength()) {
                onMaxLengthToggled();
            }
            if(editMaskStr.isSetValidatorType() && (editMaskStr.getValidatorType() == 0 ||
                    editMaskStr.getValidatorType() == 3)) {
                onValidatorsSet(editMaskStr.getValidatorType());
            }
        } else {
            throw new IllegalArgumentError("Instance of EditMaskStr expected");
        }
    }

    @Override
    public void setHiddenOptions(final EnumSet<EEditMaskOption> options) {
        for(EEditMaskOption key : options) {
            final IEditMaskEditorSetting s = widgets.get(key);
            if(s != null) {
                s.hide();
            }
        }
        valueControl.setHiddenOptions(options);
    }

    @Override
    public void setVisibleOptions(final EnumSet<EEditMaskOption> options) {
        for(EEditMaskOption key : options) {
            final IEditMaskEditorSetting s = widgets.get(key);
            if(s != null) {
                s.show();
            }
        }
        valueControl.setVisibleOptions(options);
    }

    @Override
    public void setEnabledOptions(final EnumSet<EEditMaskOption> options) {
        for(EEditMaskOption key : options) {
            final IEditMaskEditorSetting s = widgets.get(key);
            if(s != null) {
                s.setEnabled(true);
            }
        }
        valueControl.setEnabledOptions(options);
    }

    @Override
    public void setDisabledOptions(final EnumSet<EEditMaskOption> options) {
        for(EEditMaskOption key : options) {
            final IEditMaskEditorSetting s = widgets.get(key);
            if(s != null) {
                s.setDisabled(true);
            }
        }
        valueControl.setDisabledOptions(options);
    }

    @Override
    public boolean checkOptions() {
        final boolean check = valueControl.checkOptions();
        final String numeralOptionsErrorMsg = environment.getMessageProvider()
                .translate("EditMask", "Incorrect input: maximum value is less than minimum value.");
        final String regexpErrorMsg = environment.getMessageProvider()
                .translate("EditMask", "Incorrect input: regular expression can't be empty.");
        
        if(!check) {
            switch(index) {
                case StrValueControlWidget.INT:
                    environment.messageError(numeralOptionsErrorMsg);
                    valueControl.getSetting(EEditMaskOption.STR_VC_INTEGER_MINIMUM).setFocus();
                    break;
                case StrValueControlWidget.NUM: 
                    environment.messageError(numeralOptionsErrorMsg);
                    valueControl.getSetting(EEditMaskOption.STR_VC_NUMBER_MINIMUM).setFocus();
                    break;
                case StrValueControlWidget.REGEXP:
                    environment.messageError(regexpErrorMsg);
                    valueControl.getSetting(EEditMaskOption.STR_VC_REGEXP_PATTERN).setFocus();
                    break;
            }
        }
        
        return check;
    }

    private void setUpUi(final IClientEnvironment environment, final boolean isChar) {
        final StrIsPasswordSetting isPassword = new StrIsPasswordSetting(environment, this);
        widgets.put(EEditMaskOption.STR_IS_PASSWORD, isPassword);
        final StrAllowEmptyStringSetting allowEmpty = new StrAllowEmptyStringSetting(environment, this);
        widgets.put(EEditMaskOption.STR_ALLOW_EMPTY, allowEmpty);
        
        maxLength = new StrMaximumLengthSetting(environment, this);
        widgets.put(EEditMaskOption.STR_MAX_LENGTH, maxLength);
        valueControl = new StrValueControlWidget(environment, this);
        if(isChar) {
            maxLength.setDisabled(isChar);
            maxLength.setValue(1L);
            valueControl.setIsChar(isChar);
        } else {
            maxLength.getStateChangedSignal().connect(this, "onMaxLengthToggled()");
            valueControl.getIndexSignal().connect(this, "onValidatorsSet(int)");
            valueControl.getIndexSignal().emit(StrValueControlWidget.DEFAULT);
        }
        ((StrDefaultQtMaskSetting) valueControl.getSetting(EEditMaskOption.STR_VC_DEFAULT_QTMASK))
                    .valueChanged.connect(this, "onQtMaskEdit(Object)");
        mainLayout.addWidget(isPassword);
        mainLayout.addWidget(allowEmpty);
        mainLayout.addWidget(maxLength);
        mainLayout.addWidget(valueControl);

    }
    
    public void onMaxLengthToggled() {
        switch(maxLength.getState()) {
            case Checked:
                valueControl.onMaxLengthSet(true);
                break;
            case Unchecked:
                valueControl.onMaxLengthSet(false);
                break;
            default: break;
        }
    }
    
    public void onValidatorsSet(final int i) {
        this.index = i;
        maxLength.setEnabled(i != StrValueControlWidget.REGEXP); // disable when current option is regexp
    }
    
    @SuppressWarnings("unused")
    private void onQtMaskEdit(final Object o) {
        if(o!=null) {
            final String value = (String) o;
            final boolean flag = value.isEmpty();
            maxLength.setEnabled(flag);
            if(!flag) maxLength.setState(CheckState.Unchecked);
        }
    }
    
}
