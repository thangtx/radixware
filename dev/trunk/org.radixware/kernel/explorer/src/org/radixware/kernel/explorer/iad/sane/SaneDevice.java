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

package org.radixware.kernel.explorer.iad.sane;

import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QWidget;
import com.tuneology.sane.ButtonOption;
import com.tuneology.sane.OptionDescriptor;
import com.tuneology.sane.Sane;
import com.tuneology.sane.SaneException;
import com.tuneology.sane.SaneParameters;
import com.tuneology.sane.SaneRange;
import com.tuneology.sane.SaneScanner;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.explorer.iad.ImageAcquiringDevice;
import org.radixware.kernel.explorer.iad.sane.options.SaneOptionValueAccessor;


public final class SaneDevice extends ImageAcquiringDevice{
    
    public static interface IOptionsListener{
        void needToRefreshOptions();
        void needToRereadOptionValues();
    }
    
    public static interface IChangeOptionValueListener{
        void optionValueChanged();
    }
    
    private final IClientEnvironment environment;    
    private final com.tuneology.sane.SaneDevice devDescriptor;
    private final Map<OptionDescriptor, SaneOptionValueAccessor> valueAccessorsForOptions = new HashMap<>();
    private List<IOptionsListener> optionsListeners;
    private SaneScanner device;
        
    SaneDevice(final com.tuneology.sane.SaneDevice descriptor, final IClientEnvironment environment){
        devDescriptor = descriptor;
        this.environment = environment;
    }

    @Override
    public String getId() {
        return devDescriptor.name;
    }

    @Override
    public String getDescription() {
        return String.format("%1$s %2$s %3$s", devDescriptor.type, devDescriptor.vendor, devDescriptor.model);
    }
        
    @Override
    public QImage getImage(final boolean showImageConfirmationDialog, final QWidget parentWidget){
        final SaneDeviceDialog dialog = new SaneDeviceDialog(this, true, showImageConfirmationDialog, environment, parentWidget);
        try{
            if (dialog.exec()==QDialog.DialogCode.Accepted.value()){
                final List<QImage> scannedImages = dialog.getScannedImages();
                return scannedImages==null || scannedImages.isEmpty() ? null : scannedImages.get(0);
            }else{
                return null;
            }
        }finally{
            release();
        }
        
    }        
    
    private void openDevice() throws SaneException{
        device = Sane.open(getId());
        final String message = environment.getMessageProvider().translate("IAD", "Device \'%1$s\' was opened.");
        trace(String.format(message, devDescriptor.name));
    }
    
    public OptionDescriptor[] getOptions() throws SaneException{
        if (device==null){
            openDevice();
        }
        {
            final String message = environment.getMessageProvider().translate("IAD", "Reading options for \'%1$s\'...");
            trace(String.format(message, devDescriptor.name));
        }
        final OptionDescriptor[] options = device.get_option_descriptors();
        final StringBuilder traceMessage = new StringBuilder();
        if (options==null || options.length==0){
            traceMessage.append(environment.getMessageProvider().translate("IAD", "No options for \'%1$s\' was detected"));
        }else{
            final String message = environment.getMessageProvider().translate("IAD", "Following options for \'%1$s\' was detected:");
            traceMessage.append(String.format(message, devDescriptor.name));
            for (int i=0; i<options.length; i++){
                traceMessage.append("\n\t");
                traceMessage.append(options[i].getName());
                traceMessage.append(":{\n\t\t");
                traceMessage.append(environment.getMessageProvider().translate("IAD", "Type:"));
                traceMessage.append("\t");
                traceMessage.append(options[i].getClass().getSimpleName());                
                traceMessage.append("\n\t\t");
                traceMessage.append(environment.getMessageProvider().translate("IAD", "Unit:"));
                traceMessage.append("\t");
                final int unit = options[i].getUnit();
                traceMessage.append(String.valueOf(unit));
                final String unitName = getUnitName(unit);
                if (!unitName.isEmpty()){
                    traceMessage.append(" (");
                    traceMessage.append(unitName);
                    traceMessage.append(")\n\t\t");
                }
                traceMessage.append(environment.getMessageProvider().translate("IAD", "Value size:"));
                traceMessage.append("\t");
                traceMessage.append(options[i].getSize());
                traceMessage.append("\n\t\t");
                traceMessage.append(environment.getMessageProvider().translate("IAD", "Capability flags:"));
                traceMessage.append("\t");
                traceMessage.append(options[i].getCap());
                traceMessage.append("\n\t\t");
                final SaneRange range = options[i].getRange();
                traceMessage.append(environment.getMessageProvider().translate("IAD", "Range:"));
                if (range==null){
                    traceMessage.append("\t");
                    traceMessage.append(environment.getMessageProvider().translate("IAD", "none"));                    
                }else{
                    traceMessage.append(environment.getMessageProvider().translate("IAD", "Range:"));
                    traceMessage.append("{\n\t\t\t");
                    traceMessage.append(environment.getMessageProvider().translate("IAD", "maximum value:"));
                    traceMessage.append("\t");
                    traceMessage.append(range.getMax());
                    traceMessage.append("\n\t\t\t");
                    traceMessage.append(environment.getMessageProvider().translate("IAD", "minimum value:"));
                    traceMessage.append("\t");
                    traceMessage.append(range.getMin());
                    traceMessage.append("\n\t\t\t");
                    traceMessage.append(environment.getMessageProvider().translate("IAD", "quant:"));
                    traceMessage.append("\t");
                    traceMessage.append(range.getQuant());
                    traceMessage.append("\n\t\t}");
                }
                traceMessage.append("\n\t\t");
                final Object[] elements = options[i].getElements();
                traceMessage.append(environment.getMessageProvider().translate("IAD", "Values list:"));
                traceMessage.append("\t");
                if (elements==null || elements.length==0){                    
                    traceMessage.append(environment.getMessageProvider().translate("IAD", "none"));                    
                }else{
                    traceMessage.append("{");
                    for (int j=0; j<elements.length; j++){
                        if (j>0){
                            traceMessage.append(",");
                        }
                        traceMessage.append("\'");
                        traceMessage.append(String.valueOf(elements[j]));
                        traceMessage.append("\'");
                    }
                    traceMessage.append("}");
                }
                traceMessage.append("\n\t\t");
                traceMessage.append(environment.getMessageProvider().translate("IAD", "Title:"));
                traceMessage.append("\t\'");
                traceMessage.append(options[i].getTitle());
                traceMessage.append("\'\n\t\t");
                traceMessage.append(environment.getMessageProvider().translate("IAD", "Description:"));
                traceMessage.append("\t\'");
                traceMessage.append(options[i].getDescription());
                traceMessage.append("\'\n\t}");
            }
        }
        trace(traceMessage.toString());
        return options;
    }
    
    private String getUnitName(final int unit){
        switch (unit){            
            case Sane.UNIT_PIXEL:       
                return environment.getMessageProvider().translate("IAD", "Pixels");
            case Sane.UNIT_BIT:
                return environment.getMessageProvider().translate("IAD", "Bits");
            case Sane.UNIT_MM:
                return environment.getMessageProvider().translate("IAD", "Millimeters");
            case Sane.UNIT_DPI:
                return environment.getMessageProvider().translate("IAD", "Dots per inch");
            case Sane.UNIT_PERCENT:
                return environment.getMessageProvider().translate("IAD", "Percents");
            case Sane.UNIT_MICROSECOND:
                return environment.getMessageProvider().translate("IAD", "Microseconds");
            default:
                return "";
        }        
    }
    
    public SaneParameters getFrameParameters() throws SaneException{
        if (device==null){
            openDevice();
        }        
        {
            final String message = environment.getMessageProvider().translate("IAD", "Reading scan parameters for \'%1$s\'...");
            trace(String.format(message,devDescriptor.name));
        }        
        final SaneParameters parameters = device.get_parameters();
        final StringBuilder traceMessage = new StringBuilder();
        traceMessage.append(environment.getMessageProvider().translate("IAD", "Following scan paramateters was received:"));
        traceMessage.append("\n\t");
        {
            final String message = environment.getMessageProvider().translate("IAD", "Bytes per line:\t%1$s");
            traceMessage.append(String.format(message, parameters.bytes_per_line));
        }        
        traceMessage.append("\n\t");
        {
            final String message = environment.getMessageProvider().translate("IAD", "Pixels per line:\t%1$s");
            traceMessage.append(String.format(message, parameters.pixels_per_line));
        }
        traceMessage.append("\n\t");
        {
            final String message = environment.getMessageProvider().translate("IAD", "Number of lines:\t%1$s");
            traceMessage.append(String.format(message, parameters.lines));
        }
        traceMessage.append("\n\t");
        {
            final String message = environment.getMessageProvider().translate("IAD", "Color depth:\t%1$s");
            traceMessage.append(String.format(message, parameters.depth));
        }
        traceMessage.append("\n\t");
        {
            final String message = environment.getMessageProvider().translate("IAD", "Frame format:\t%1$s (%2$s)");
            traceMessage.append(String.format(message, parameters.format, getFrameFormat(parameters.format)));
        }
        traceMessage.append("\n\t");
        if (parameters.last_frame){
            traceMessage.append(environment.getMessageProvider().translate("IAD", "Last frame:\tyes"));
        }else{
            traceMessage.append(environment.getMessageProvider().translate("IAD", "Last frame:\tno"));
        }
        trace(traceMessage.toString());
        return parameters;
    }
    
    private String getFrameFormat(final int format){
        switch (format){
            case Sane.FRAME_GRAY:       
                return "\'GRAY\'";
            case Sane.FRAME_RGB:
                return "\'RGB\'";
            case Sane.FRAME_RED:
                return "\'RED\'";
            case Sane.FRAME_GREEN:
                return "\'GREEN\'";
            case Sane.FRAME_BLUE:
                return "\'BLUE\'";
            default:
                return "Unknown";
        }        
    }
    
    public void close(){
        if (device!=null){
            device.close();            
            optionsListeners = null;
            valueAccessorsForOptions.clear();
            final String message = environment.getMessageProvider().translate("IAD", "Device \'%1$s\' was closed.");
            trace(String.format(message, devDescriptor.name));            
            device = null;
        }
    }
    
    public void startScan() throws SaneException{
        if (device==null){
            openDevice();
        }
        device.start();
        final String message = environment.getMessageProvider().translate("IAD", "Scanning progress was initiated at device \'%1$s\'");
        trace(String.format(message, devDescriptor.name));
    }
    
    public void cancelScan(){
        if (device!=null){
            device.cancel();
            final String message = environment.getMessageProvider().translate("IAD", "Scanning progress was canceled at device \'%1$s\'");
            trace(String.format(message, devDescriptor.name));
        }
    }
    
    public int read(final byte[] buffer) throws SaneException{
        {
            final String message = environment.getMessageProvider().translate("IAD", "Reading data from device \'%1$s\'...");
            trace(String.format(message, devDescriptor.name));
        }
        final int result = device.read(buffer, 0, buffer.length);
        {
            final String message = environment.getMessageProvider().translate("IAD", "%1$s bytes was received");
            trace(String.format(message, result));
        }
        return result;
    }
    
    private void trace(final String message){
        environment.getTracer().put(EEventSeverity.DEBUG, message, EEventSource.IAD);
    }
    
    private SaneOptionValueAccessor getOptionValueAccessor(final OptionDescriptor option){
        SaneOptionValueAccessor valueAccessor = valueAccessorsForOptions.get(option);
        if (valueAccessor==null){
            valueAccessor = SaneOptionValueAccessor.Factory.getInstance(option, environment);
            valueAccessorsForOptions.put(option, valueAccessor);
        }
        return valueAccessor;
    }
    
    public Integer getOptionIntegerValue(final OptionDescriptor option){
        return getOptionValueAccessor(option).getIntValue();
    }
    
    public boolean setOptionIntegerValue(final OptionDescriptor option, final int value){
        final int result = getOptionValueAccessor(option).setIntValue(value);
        if (result<0){
            return false;
        }else{
            afterWriteOptionValue(result);
            return true;
        }
    }
    
    public Double getOptionDoubleValue(final OptionDescriptor option){
        return getOptionValueAccessor(option).getDoubleValue();
    }
    
    public boolean setOptionDoubleValue(final OptionDescriptor option, final double value){
        final int result = getOptionValueAccessor(option).setDoubleValue(value);
        if (result<0){
            return false;
        }else{
            afterWriteOptionValue(result);
            return true;
        }
    }
    
    public String getOptionStringValue(final OptionDescriptor option){
        return getOptionValueAccessor(option).getStringValue();
    }
    
    public boolean setOptionStringValue(final OptionDescriptor option, final String value){
        final int result = getOptionValueAccessor(option).setStringValue(value);
        if (result<0){
            return false;
        }else{
            afterWriteOptionValue(result);
            return true;
        }
    }
    
    public boolean pressButton(final ButtonOption option){
        final int result;
        try{
            result = option.pressButton();
        }catch(SaneException exception){
            final String messageTemplate = 
                environment.getMessageProvider().translate("IAD", "Failed to change value of \'%1$s\' option");
            final String message = String.format(messageTemplate, option.getName());
            environment.getTracer().error(message,exception, EEventSource.IAD);
            return false;
        }
        final String messageTemplate = 
            environment.getMessageProvider().translate("IAD", "Button \'%1$s\' was pressed");
        final String message = String.format(messageTemplate, option.getName());
        trace(message);
        afterWriteOptionValue(result);
        return true;
    }
    
    private void afterWriteOptionValue(final int writeResult){
        if (needForRefreshOptions(writeResult)){
            clearCachedOptionValues();
            notifyRefreshOptions();
        }else if (needForRereadOptionValues(writeResult)){
            clearCachedOptionValues();
            notifyRereadOptionValues();
        }
    }
    
    private void clearCachedOptionValues(){
        for (SaneOptionValueAccessor valueAccessor: valueAccessorsForOptions.values()){
            valueAccessor.clearCache();
        }
    }
    
    private static boolean needForRefreshOptions(final int setValueResult){
        return (setValueResult & Sane.INFO_RELOAD_OPTIONS)!=0;
    }
    
    private static boolean needForRereadOptionValues(final int setValueResult){
        return (setValueResult & Sane.INFO_RELOAD_PARAMS)!=0;
    }    
    
    public void addOptionsListener(final IOptionsListener listener){
        if (listener!=null){
            if (optionsListeners==null){
                optionsListeners = new LinkedList<>();
                optionsListeners.add(listener);
            }else if (!optionsListeners.contains(listener)){
                optionsListeners.add(listener);
            }
        }
    }
    
    public void removeOptionsListener(final IOptionsListener listener){
        if (optionsListeners!=null){
            optionsListeners.remove(listener);
        }
    }
    
    private void notifyRefreshOptions(){
        if (optionsListeners!=null){
            final List<IOptionsListener> listeners = new LinkedList<>(optionsListeners);
            for (IOptionsListener listener: listeners){
                listener.needToRefreshOptions();
            }
        }
    }
    
    private void notifyRereadOptionValues(){
        if (optionsListeners!=null){
            final List<IOptionsListener> listeners = new LinkedList<>(optionsListeners);
            for (IOptionsListener listener: listeners){
                listener.needToRereadOptionValues();
            }
        }
    }   
    
    public void addChangeOptionValueListener(final OptionDescriptor option, final IChangeOptionValueListener listener){
        getOptionValueAccessor(option).addChangeOptionValueListener(listener);
    }
    
    public void removeChangeOptionValueListener(final OptionDescriptor option, final IChangeOptionValueListener listener){
        getOptionValueAccessor(option).removeChangeValueListener(listener);
    }        
}
