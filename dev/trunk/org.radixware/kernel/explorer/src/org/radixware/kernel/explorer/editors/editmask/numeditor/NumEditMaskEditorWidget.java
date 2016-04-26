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

package org.radixware.kernel.explorer.editors.editmask.numeditor;

import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.widgets.IEditMaskEditor;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.widgets.ExplorerFrame;


public final class NumEditMaskEditorWidget extends ExplorerFrame implements IEditMaskEditor {

    private final EnumMap<EEditMaskOption, IEditMaskEditorSetting> children = 
            new EnumMap<EEditMaskOption, IEditMaskEditorSetting>(EEditMaskOption.class);
    private final IClientEnvironment environment;
    public NumEditMaskEditorWidget(final IClientEnvironment environment, final QWidget parent) {
        super(environment,parent);
        this.environment = environment;
        setFrameShape(Shape.StyledPanel);
               
        final NumPrecisionSetting precision = new NumPrecisionSetting(environment, this);
        children.put(precision.getOption(), precision);
        final NumMinimumValueSetting minValue = new NumMinimumValueSetting(environment, this);
        children.put(minValue.getOption(), minValue);
        final NumMaximumValueSetting maxValue = new NumMaximumValueSetting(environment, this);
        children.put(maxValue.getOption(), maxValue);
        final NumScaleSetting scale = new NumScaleSetting(environment, this);
        children.put(scale.getOption(), scale);
        final NumDecimalDelimiterSetting decimDelimiter = new NumDecimalDelimiterSetting(environment, this);
        children.put(decimDelimiter.getOption(), decimDelimiter);
        final NumTriadDelimiterSetting triadDelimiter = new NumTriadDelimiterSetting(environment, this);
        children.put(triadDelimiter.getOption(), triadDelimiter);
        
        final QVBoxLayout layout = new QVBoxLayout();
        layout.setAlignment(new Alignment(AlignmentFlag.AlignTop, AlignmentFlag.AlignLeft));
        layout.addWidget(precision);
        layout.addWidget(minValue);
        layout.addWidget(maxValue);
        layout.addWidget(scale);
        layout.addWidget(decimDelimiter);
        layout.addWidget(triadDelimiter);
        this.setLayout(layout);
    }
    
    @Override
    public EditMask getEditMask() {
        final org.radixware.schemas.editmask.RadixEditMaskDocument xmlBean = 
                    org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.newInstance();
        final org.radixware.schemas.editmask.EditMask editMask = xmlBean.addNewRadixEditMask();
        editMask.addNewNum();
        final Collection<IEditMaskEditorSetting> childrenSettings = children.values();
        for(IEditMaskEditorSetting s : childrenSettings) {
            s.addToXml(editMask);
        }
        return EditMask.loadFrom(xmlBean);
    }

    @Override
    public void setEditMask(final EditMask editMask) {
        if(editMask instanceof EditMaskNum) {
            final org.radixware.schemas.editmask.RadixEditMaskDocument xmlBean = 
                    org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.newInstance();
            final org.radixware.schemas.editmask.EditMask em = xmlBean.addNewRadixEditMask();
            editMask.writeToXml(em);
            final Collection<IEditMaskEditorSetting> childrenSettings = children.values();
            for(IEditMaskEditorSetting s : childrenSettings) {
                s.loadFromXml(em);
            }
        } else {
            throw new IllegalArgumentException("Instance of EditMaskNum was expected");
        }
        
    }

    @Override
    public void setHiddenOptions(final EnumSet<EEditMaskOption> options) {
        for(EEditMaskOption o : options) {
            if(children.containsKey(o)) {
                children.get(o).hide();
            }
        }
    }

    @Override
    public void setVisibleOptions(final EnumSet<EEditMaskOption> options) {
        for(EEditMaskOption o : options) {
            if(children.containsKey(o)) {
                children.get(o).show();
            }
        }
    }

    @Override
    public void setEnabledOptions(final EnumSet<EEditMaskOption> options) {
        for(EEditMaskOption o : options) {
            if(children.containsKey(o)) {
                children.get(o).setEnabled(true);
            }
        }
    }

    @Override
    public void setDisabledOptions(final EnumSet<EEditMaskOption> options) {
        for(EEditMaskOption o : options) {
            if(children.containsKey(o)) {
                children.get(o).setDisabled(true);
            }
        }
    }

    @Override
    public boolean checkOptions() {
        final BigDecimal max = (BigDecimal) children.get(EEditMaskOption.NUM_MAXIMUM).getValue();
        final BigDecimal min = (BigDecimal) children.get(EEditMaskOption.NUM_MINIMUM).getValue();
        
        if(max.compareTo(min) < 0) { 
            final String msg = environment.getMessageProvider().translate("EditMask", "Incorrect input: maximum value is less than minimum value");
            environment.messageError(msg);
            children.get(EEditMaskOption.NUM_MINIMUM).setFocus();
            return false;
        }
        return true;
    }

}
