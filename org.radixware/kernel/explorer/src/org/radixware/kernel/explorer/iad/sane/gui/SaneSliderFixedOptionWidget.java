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
import com.trolltech.qt.gui.QDoubleSpinBox;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QSlider;
import com.trolltech.qt.gui.QWidget;
import com.tuneology.sane.FixedOption;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.iad.sane.SaneDevice;
import org.radixware.kernel.explorer.iad.sane.options.SaneOptionValueUtils;


final class SaneSliderFixedOptionWidget extends SaneOptionWidget<FixedOption, Double>{
    
    private final QSlider slider = new QSlider(Qt.Orientation.Horizontal, this);
    private final QDoubleSpinBox spin = new QDoubleSpinBox(this);
    
    private int iStep;
    private double dStep;
    private double minChange;
    
    public SaneSliderFixedOptionWidget(final SaneDevice device, final FixedOption option, final IClientEnvironment environment, final QWidget parentWidget){
        super(device,option,environment,parentWidget);
        spin.setMinimumWidth(spin.sizeHint().width());
        spin.setAlignment(Qt.AlignmentFlag.AlignRight);        
        refreshWidget();
        spin.valueChanged.connect(this,"onValueChanged(Double)");
        slider.valueChanged.connect(this,"onValueChanged(Integer)");
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
        layout.addWidget(slider, row,2);
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
        final Double value = device.getOptionDoubleValue(getOption());
        if (value!=null){
            if (Math.abs(spin.value() - value.doubleValue()) >= minChange){                
                if (Math.abs(value - spin.value()) > dStep) {
                    spin.blockSignals(true);
                    spin.setValue(value.doubleValue());
                    spin.blockSignals(false);
                }
                int ivalue = SaneOptionValueUtils.double2fixedValue(value.doubleValue());
                if (ivalue != slider.value()){
                    slider.blockSignals(true);
                    slider.setValue(ivalue);
                    slider.blockSignals(false);
                }
            }
        }
    }
    
    private void updateRange() {        
        final double dMaxValue = SaneOptionValueUtils.getMaxOptionDoubleValue(getOption());
        final double dMinValue = SaneOptionValueUtils.getMinOptionDoubleValue(getOption());
        dStep = SaneOptionValueUtils.getOptionDoubleValueQuant(getOption());
        final int iMaxValue = SaneOptionValueUtils.getMinOptionIntValue(getOption());
        final int iMinValue = SaneOptionValueUtils.getMaxOptionIntValue(getOption());
        iStep = SaneOptionValueUtils.getOptionIntValueQuant(getOption());
        slider.setRange(iMaxValue, iMinValue);
        spin.setRange(dMaxValue, dMinValue);
        slider.setSingleStep(iStep);
        spin.setSingleStep(dStep);
        
        int decimals=0;
        double tmp_step = dStep;
        while (tmp_step < 1) {
            tmp_step *= 10;
            decimals++;
            if (decimals > 5) break;
        }
        spin.setDecimals(decimals);        
        spin.setSuffix(getUnitSuffix());
        minChange = dStep/2;
    }
    
    @SuppressWarnings("unused")
    private void onValueChanged(final Double newValue){
        int ivalue = SaneOptionValueUtils.double2fixedValue(newValue.doubleValue());
        if (ivalue != slider.value()){            
            if (writeValue(newValue)){
                slider.setValue(ivalue);                
            }else{
                rereadValue();
            }
        }
    }
    
    @SuppressWarnings("unused")
    private void onValueChanged(final Integer newValue){
        double value = SaneOptionValueUtils.fixedValue2Double(newValue.intValue());
        if (Math.abs(value - spin.value()) > dStep) {
            if (writeValue(value)){
                spin.setValue(value);
            }else{                
                rereadValue();                
            }
        }        
    }
    
    @SuppressWarnings("unused")
    private void onSliderReleased(){
        int rest = (slider.value() - slider.minimum())%iStep;
        if (rest != 0) {
            if (rest > (iStep/2)) {
                slider.setValue(slider.value()+(iStep-rest));
            }else{
                slider.setValue(slider.value()-rest);
            }
            spin.setValue(SaneOptionValueUtils.fixedValue2Double(slider.value()));
        }        
    }
    
    private boolean writeValue(final double value){
        return device.setOptionDoubleValue(getOption(), value);
    }    
}
