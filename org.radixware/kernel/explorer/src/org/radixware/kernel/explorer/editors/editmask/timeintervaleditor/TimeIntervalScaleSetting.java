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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval.Scale;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;


final class TimeIntervalScaleSetting extends QWidget implements IEditMaskEditorSetting {
    private final QComboBox cbTimeUnitSelector = new QComboBox(this);
    public final Signal1<Scale> scaleChanged = new Signal1<Scale>();
    
    public TimeIntervalScaleSetting(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        
        final MessageProvider msgProvider = environment.getMessageProvider();
        final String labelText = msgProvider.translate("EditMask", "Scale:");
        final QHBoxLayout layout = new QHBoxLayout();
        layout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignLeft));
        final QLabel label = new QLabel(labelText, this);
        layout.setMargin(0);
        layout.addWidget(label);
        layout.addWidget(cbTimeUnitSelector);
        
        final Scale[] scales = Scale.values();
        String itemText = msgProvider.translate("EditMask", "Hour");
        cbTimeUnitSelector.addItem(itemText, scales[0]);
        itemText = msgProvider.translate("EditMask", "Minute");
        cbTimeUnitSelector.addItem(itemText, scales[1]);
        itemText = msgProvider.translate("EditMask", "Second");
        cbTimeUnitSelector.addItem(itemText, scales[2]);
        itemText = msgProvider.translate("EditMask", "Millisecond");
        cbTimeUnitSelector.addItem(itemText, scales[3]);
        itemText = msgProvider.translate("EditMask", "None");
        cbTimeUnitSelector.addItem(itemText, scales[4]);
        
        
        this.setLayout(layout);
        cbTimeUnitSelector.currentIndexChanged.connect((QWidget)this, "onIndexChanged(Integer)");
        setDefaultValue();
    }

    @Override
    public void addToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final Scale scale = (Scale) cbTimeUnitSelector.itemData(cbTimeUnitSelector.currentIndex());
        editMask.getTimeInterval().setScale(scale.toXml());
    }

    @Override
    public void loadFromXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskTimeInterval emti = editMask.getTimeInterval();
        final int index = cbTimeUnitSelector.findData(Scale.valueOf(emti.getScale().toString()));
        cbTimeUnitSelector.setCurrentIndex(index);
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.TIMEINTERVAL_SCALE;
    }

    @Override
    public void setDefaultValue() {
        cbTimeUnitSelector.setCurrentIndex(0);
    }

    @Override
    public Scale getValue() {
        return (Scale) cbTimeUnitSelector.itemData(cbTimeUnitSelector.currentIndex());
    }
    
    public void setValue(final Scale scale) {
        final int index = cbTimeUnitSelector.findData(scale);
        if(index >= 0) {
            cbTimeUnitSelector.setCurrentIndex(index);
        }
    }
    
        
    @SuppressWarnings("unused")
    private void onIndexChanged(final Integer index) {
        final Scale currentScale = (Scale) cbTimeUnitSelector.itemData(index);
        scaleChanged.emit(currentScale);
    }
}
