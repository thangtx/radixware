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

package org.radixware.kernel.explorer.iad.sane.gui;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QSlider;
import com.trolltech.qt.gui.QSpinBox;
import com.trolltech.qt.gui.QWidget;
import com.tuneology.sane.IntegerOption;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.iad.sane.SaneDevice;
import org.radixware.kernel.explorer.iad.sane.options.SaneOptionValueUtils;


final class SaneSliderIntOptionWidget extends SaneOptionWidget<IntegerOption, Integer>{
    
    private final QSlider slider = new QSlider(Qt.Orientation.Horizontal, this);
    private final QSpinBox spin = new QSpinBox(this);
    
    public SaneSliderIntOptionWidget(final SaneDevice device,final IntegerOption option, final IClientEnvironment environment, final QWidget parentWidget){
        super(device,option,environment,parentWidget);        
        spin.setAlignment(Qt.AlignmentFlag.AlignRight);
        refreshWidget();
        slider.valueChanged.connect(this, "onValueChanged(Integer)");
        spin.valueChanged.connect(this, "onValueChanged(Integer)");        
        slider.sliderReleased.connect(this,"onSliderReleased()");
        device.addChangeOptionValueListener(option, new SaneDevice.IChangeOptionValueListener() {
            @Override
            public void optionValueChanged() {
                rereadValue();
            }
        });
    }

    @Override
    public void putIntoLayout(final QGridLayout layout) {
        super.putIntoLayout(layout);
        final int row = layout.rowCount()-1;
        layout.addWidget(slider, row, 2);
        layout.addWidget(spin, row, 1);
    }

    @Override
    protected void refreshWidget() {
        if (!isHidden()){
            updateRange();
            rereadValue();            
        }
    }        

    @Override
    public void rereadValue() {
        final Integer value = device.getOptionIntegerValue(getOption());
        if (value!=null){
            if (slider.value()!=value.intValue()){
                slider.blockSignals(true);            
                slider.setValue(value.intValue());
                slider.blockSignals(false);
            }
            if (spin.value()!=value){
                spin.blockSignals(true);
                spin.setValue(value.intValue());
                spin.blockSignals(false);
            }            
        }
    };

    private void updateRange() {      
        final int minValue = SaneOptionValueUtils.getMinOptionIntValue(getOption());
        final int maxValue = SaneOptionValueUtils.getMaxOptionIntValue(getOption());
        final int step = SaneOptionValueUtils.getOptionIntValueQuant(getOption());
        slider.setRange(minValue, maxValue);
        spin.setRange(minValue, maxValue);
        slider.setSingleStep(step);
        spin.setSingleStep(step);
        spin.setSuffix(getUnitSuffix());
    }
    
    @SuppressWarnings("unused")
    private void onSliderReleased(){
        final int rest = (slider.value() - slider.minimum())%slider.singleStep();
        if (rest != 0) {
            if (rest > (slider.singleStep()/2)){
                slider.setValue(slider.value()+(slider.singleStep()-rest));
            }else{
                slider.setValue(slider.value()-rest);
            }
            spin.setValue(slider.value());
        }        
    }
    
    @SuppressWarnings("unused")
    private void onValueChanged(final Integer newValue){        
        if (newValue.intValue() != spin.value()) {
            if (writeValue(newValue.intValue())){
                spin.setValue(newValue.intValue());                
            }else{            
                rereadValue();
            }
        }else if (newValue.intValue() != slider.value()) {        
            if ((newValue - slider.minimum())%slider.singleStep() != 0) {                
                if (newValue.intValue() > spin.value()) {
                    slider.setValue(slider.value()+(slider.singleStep() - (newValue.intValue() - spin.value())));
                }else{
                    slider.setValue(slider.value() - (slider.singleStep() - (spin.value() - newValue.intValue())));
                }
            } else {
                if (writeValue(newValue.intValue())){
                    slider.setValue(newValue.intValue());                                        
                }else{
                    rereadValue();
                }
            }
        }        
    }
    
    private boolean writeValue(final int value){
        return device.setOptionIntegerValue(getOption(), value);
    }
}
