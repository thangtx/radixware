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

package org.radixware.kernel.explorer.editors.editmask.timeintervaleditor;

import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval;
import org.radixware.kernel.common.client.widgets.IEditMaskEditor;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval.Scale;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.widgets.ExplorerFrame;


public class TimeIntervalEditMaskEditorWidget extends ExplorerFrame implements IEditMaskEditor{
    private final EnumMap<EEditMaskOption, IEditMaskEditorSetting> children = 
            new EnumMap<EEditMaskOption, IEditMaskEditorSetting>(EEditMaskOption.class);
    private final IClientEnvironment environment;
    
    public TimeIntervalEditMaskEditorWidget(final IClientEnvironment environment, final QWidget parent) {
        super(environment,parent);
        this.environment = environment;
        this.setFrameShape(Shape.StyledPanel);
        final QVBoxLayout layout = new QVBoxLayout();
        
        layout.setAlignment(new Alignment(AlignmentFlag.AlignTop));
        //layout.addStretch();
        
        final TimeIntervalScaleSetting scale = new TimeIntervalScaleSetting(environment, this);
        children.put(scale.getOption(), scale);
        layout.addWidget(scale);
        final TimeIntervalQtMaskSetting qtMask = new TimeIntervalQtMaskSetting(environment, this);
        children.put(qtMask.getOption(), qtMask);
        layout.addWidget(qtMask);
        scale.scaleChanged.connect(qtMask, "updateScale(org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval$Scale)");
        
        final TimeIntervalMinimumValueSetting minValue = new TimeIntervalMinimumValueSetting(environment, this);
        children.put(minValue.getOption(), minValue);
        layout.addWidget(minValue);
        scale.scaleChanged.connect(minValue, "updateLabelText(org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval$Scale)");
        final TimeIntervalMaximumValueSetting maxValue = new TimeIntervalMaximumValueSetting(environment, this);
        children.put(maxValue.getOption(), maxValue);
        layout.addWidget(maxValue);
        scale.scaleChanged.connect(maxValue, "updateLabelText(org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval$Scale)");
        
        scale.scaleChanged.emit(scale.getValue());
        this.setLayout(layout);
        
    }
    
    @Override
    public EditMask getEditMask() {
        final org.radixware.schemas.editmask.RadixEditMaskDocument xmlBean = 
                org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.newInstance();
        final org.radixware.schemas.editmask.EditMask editMask = xmlBean.addNewRadixEditMask();
        editMask.addNewTimeInterval();
        
        final Collection<IEditMaskEditorSetting> settings = children.values();
        for(IEditMaskEditorSetting s : settings) {
            s.addToXml(editMask);
        }
        
        return EditMask.loadFrom(xmlBean);
        
    }

    @Override
    public void setEditMask(final EditMask editMask) {
        if(editMask instanceof EditMaskTimeInterval) {
            final org.radixware.schemas.editmask.RadixEditMaskDocument xmlBean = 
                    org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.newInstance();
            final org.radixware.schemas.editmask.EditMask em = xmlBean.addNewRadixEditMask();
            editMask.writeToXml(em);
            final Collection<IEditMaskEditorSetting> childrenSettings = children.values();
            for(IEditMaskEditorSetting s : childrenSettings) {
                s.loadFromXml(em);
            }
        } else {
            throw new IllegalArgumentException("Instance of EditMaskTimeInterval was expected");
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
        final Long max = (Long) children.get(EEditMaskOption.TIMEINTERVAL_MAXIMUM).getValue();
        final Long min = (Long) children.get(EEditMaskOption.TIMEINTERVAL_MINIMUM).getValue();
        if(max.compareTo(min) < 0) {
            final String msg = environment.getMessageProvider().translate("EditMask", "Incorrect input: maximum value is less than minimum value");
            environment.messageError(msg);
            children.get(EEditMaskOption.TIMEINTERVAL_MINIMUM).setFocus();
            return false;
        }       
        return true;
    }
}
