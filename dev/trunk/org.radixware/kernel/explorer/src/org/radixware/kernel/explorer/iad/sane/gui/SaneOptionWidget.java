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
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;
import com.tuneology.sane.BooleanOption;
import com.tuneology.sane.ButtonOption;
import com.tuneology.sane.FixedOption;
import com.tuneology.sane.IntegerOption;
import com.tuneology.sane.OptionDescriptor;
import com.tuneology.sane.Sane;
import com.tuneology.sane.StringOption;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.explorer.iad.sane.SaneDevice;


abstract class SaneOptionWidget<T extends OptionDescriptor,E> extends QWidget{
    
    public static class Factory{
        
        private Factory(){            
        }
        
        public static SaneOptionWidget createInstance(final SaneDevice device, final OptionDescriptor option, final IClientEnvironment environment, final QWidget parent){
            final Object[] elements = option.getElements();            
            if (option instanceof BooleanOption){
                return new SaneBooleanOptionWidget(device,(BooleanOption)option, environment, parent);
            }
            final boolean isList = elements!=null && elements.length>0;
            if (option instanceof IntegerOption){
                if (isList){
                    return new SaneListIntOptionWidget(device,(IntegerOption)option, environment, parent);
                }else{
                    return new SaneSliderIntOptionWidget(device,(IntegerOption)option, environment, parent);
                }
            }
            if (option instanceof FixedOption){
                return new SaneSliderFixedOptionWidget(device,(FixedOption)option, environment, parent);
            }
            if (option instanceof ButtonOption){
                return new SaneButtonOptionWidget(device,(ButtonOption)option, environment, parent);
            }
            if (option instanceof StringOption){                
                if (isList){
                    return new SaneListStringOptionWidget(device,(StringOption)option, environment, parent);
                }else{
                    return new SaneStringOptionWidget(device,(StringOption)option, environment, parent);
                }
            }
            final String message = "Unable to create handle for option \'%1$s\': unknown option class \'%2$s\'";
            environment.getTracer().put(EEventSeverity.DEBUG, String.format(message,option.getTitle(),option.getClass().getName()), EEventSource.IAD);
            return null;
        }
    }
    
    private T optionDesctiptor;
    protected final SaneDevice device;
    private final IClientEnvironment environment;
    protected final SaneLocalization localizer;
    private final QLabel label = new QLabel(this);    
    
    SaneOptionWidget(final SaneDevice device,
                     final T option,
                     final IClientEnvironment environment, 
                     final QWidget parent){
        super(parent);        
        optionDesctiptor = option;
        localizer = new SaneLocalization(environment);
        this.device = device;
        this.environment = environment;
        final String title = localizer.getLocalizedString(optionDesctiptor.getTitle());
        if (title!=null && !title.isEmpty()){
            label.setText(title+":");
        }
        updateState();
        if (optionDesctiptor.getDescription()!=null){
            setToolTip(localizer.getLocalizedString(optionDesctiptor.getDescription()));
        }
    }
    
    protected void setLabelText(final String text){        
        label.setText(text!=null && !text.isEmpty() ? text+":" : "");
    }
    
    public void putIntoLayout(final QGridLayout layout){
        final int row = layout.rowCount();
        layout.addWidget(label, row, 0, Qt.AlignmentFlag.AlignRight);
    }
    
    private void updateState(){
        if (((optionDesctiptor.getCap() & Sane.CAP_SOFT_DETECT) == 0) ||
            ((optionDesctiptor.getCap() & Sane.CAP_INACTIVE) != 0) ||
            ((optionDesctiptor.getSize() == 0) && (optionDesctiptor instanceof ButtonOption==false))){    
            setVisible(false);
        }else if (isSoftSelect()) {
            setVisible(true);
            setDisabled(false);            
        }else{
            setVisible(true);
            setDisabled(true);            
        }
    }
    
    @SuppressWarnings({"unused", "unchecked"})
    public final void refresh(final OptionDescriptor option){
        optionDesctiptor = (T)option;
        updateState();
        refreshWidget();
    }
    
    public final boolean needsPolling(){
        return optionNeedsForPolling(optionDesctiptor);
    }
    
    protected final boolean isSoftSelect(){
        return (optionDesctiptor.getCap() & Sane.CAP_SOFT_SELECT) != 0;
    }
    
    public final T getOption(){
        return optionDesctiptor;
    }
    
    protected final IClientEnvironment getEnvironment(){
        return environment;
    }
    
    public abstract void rereadValue();
    
    protected abstract void refreshWidget();

    
    protected final String getUnitSuffix(){
        switch (optionDesctiptor.getUnit()){            
            case Sane.UNIT_PIXEL:       
                return getEnvironment().getMessageProvider().translate("IAD", "Pixel");
            case Sane.UNIT_BIT:
                return getEnvironment().getMessageProvider().translate("IAD", "Bit");
            case Sane.UNIT_MM:
                return getEnvironment().getMessageProvider().translate("IAD", "mm");
            case Sane.UNIT_DPI:
                return getEnvironment().getMessageProvider().translate("IAD", "DPI");
            case Sane.UNIT_PERCENT:
                return getEnvironment().getMessageProvider().translate("IAD", "%");
            case Sane.UNIT_MICROSECOND:
                return getEnvironment().getMessageProvider().translate("IAD", "mcs");
            default:
                return "";
        }
    }
        
    public static boolean optionNeedsForPolling(final OptionDescriptor option){
        final int caps = option.getCap();
        return (caps & Sane.CAP_SOFT_DETECT)!=0 && (caps & Sane.CAP_SOFT_SELECT)==0;
    }
}