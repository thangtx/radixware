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
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QWidget;
import com.tuneology.sane.OptionDescriptor;
import com.tuneology.sane.Sane;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.iad.sane.SaneDevice;


abstract class AbstractSaneListOptionWidget<T extends OptionDescriptor,E> extends SaneOptionWidget<T,E>{
    
    private final QComboBox combo = new QComboBox(this);    
    
    public AbstractSaneListOptionWidget(final SaneDevice device,
                                        final T option,
                                        final IClientEnvironment environment, 
                                        final QWidget parentWidget){
        super(device,option,environment,parentWidget);
        setLabelText(localizer.getLocalizedString(option.getTitle()));
        setSizePolicy(QSizePolicy.Policy.Ignored, QSizePolicy.Policy.Fixed);
        refreshWidget();
        combo.currentIndexChanged.connect(this,"currentIndexChanged(Integer)");
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
        layout.addWidget(combo, row, 1);
        layout.addWidget(new QWidget(this), row, 2);
    }

    @Override
    protected final void setLabelText(final String text){
        super.setLabelText(getTitleWithUnitName(text));
    }

    @Override
    protected final void refreshWidget() {
        if (!isHidden()){
            updateItems();
            rereadValue();
        }
    }        
    
    @SuppressWarnings("unchecked")
    private void updateItems(){
        combo.blockSignals(true);
        try{
            final String saveValue = combo.currentText();
            combo.clear();
            final Object[] elements = getOption().getElements();
            if (elements!=null){
                String itemTitle;
                for (Object element: elements){
                    itemTitle = localizer.getLocalizedString(val2Str((E)element));
                    combo.addItem(itemTitle,element);
                    if (itemTitle.equals(saveValue)){
                        combo.setCurrentIndex(combo.count()-1);
                    }
                }
            }
        }finally{
            combo.blockSignals(false);
        }
    }

    @Override
    public final void rereadValue() {
        if (!isHidden()){
            final E value = readValue();
            if (value!=null){
                combo.blockSignals(true);
                try{
                    setValue(value);
                }finally{
                    combo.blockSignals(false);
                }
            }
        }
    }
    
    private String getTitleWithUnitName(final String title){
        switch(getOption().getUnit())
        {
            case Sane.UNIT_PIXEL:                
                return String.format(getEnvironment().getMessageProvider().translate("IAD", "%1$s (Pixels)"),title);
            case Sane.UNIT_BIT:
                return String.format(getEnvironment().getMessageProvider().translate("IAD", "%1$s (Bits)"),title);
            case Sane.UNIT_MM:
                return String.format(getEnvironment().getMessageProvider().translate("IAD", "%1$s (Millimeters)"),title);
            case Sane.UNIT_DPI:
                return String.format(getEnvironment().getMessageProvider().translate("IAD", "%1$s (Dots Per Inch)"),title);
            case Sane.UNIT_PERCENT:
                return String.format(getEnvironment().getMessageProvider().translate("IAD", "%1$s (Percentage)"),title);
            case Sane.UNIT_MICROSECOND:
                return String.format(getEnvironment().getMessageProvider().translate("IAD", "%1$s (Microseconds)"),title);
            default:
                return title;
        }        
    }
    
    @SuppressWarnings({"unused","unchecked"})
    private void currentIndexChanged(final Integer index){
        if (!writeValue((E)combo.itemData(index,Qt.ItemDataRole.UserRole))){
            rereadValue();
        }
    }
    
    protected void setValue(final E value) {
        if (value!=null){
            for (int i=0; i<combo.count(); i++){
                if (Objects.equals(combo.itemData(i,Qt.ItemDataRole.UserRole), value)){
                    combo.setCurrentIndex(i);
                    break;
                }
            }
        }
    }
    
    protected abstract E readValue();
    
    protected abstract boolean writeValue(E value);    
    
    protected abstract String val2Str(final E value);
}
