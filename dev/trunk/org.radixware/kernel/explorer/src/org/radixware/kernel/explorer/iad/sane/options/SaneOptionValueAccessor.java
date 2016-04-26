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

package org.radixware.kernel.explorer.iad.sane.options;

import com.tuneology.sane.BooleanOption;
import com.tuneology.sane.FixedOption;
import com.tuneology.sane.IntegerOption;
import com.tuneology.sane.OptionDescriptor;
import com.tuneology.sane.SaneException;
import com.tuneology.sane.StringOption;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.explorer.iad.sane.SaneDevice;


public abstract class SaneOptionValueAccessor{
    
    private final String optionName;
    private final IClientEnvironment environment;
    private List<SaneDevice.IChangeOptionValueListener> changeValueListeners;
    
    SaneOptionValueAccessor(final OptionDescriptor optionDescriptor, final IClientEnvironment env){
        optionName = optionDescriptor.getName();
        this.environment = env;
    }
    
    public static class Factory{
        private Factory(){      
        }
        
        public static SaneOptionValueAccessor getInstance(final OptionDescriptor option, 
                                                          final IClientEnvironment environment){
            if (option instanceof IntegerOption){
                return new SaneIntOptionValueAccessor((IntegerOption)option, environment);
            }else if (option instanceof FixedOption){
                return new SaneFixedOptionValueAccessor((FixedOption)option, environment);
            }else if (option instanceof BooleanOption){
                return new SaneBooleanOptionValueAccessor((BooleanOption)option, environment);
            }else if (option instanceof StringOption){
                return new SaneStringOptionValueAccessor((StringOption)option, environment);
            }else{
                final String message = "Unable to create value accessor fo %1$s option \'%2$s\'";
                throw new IllegalArgumentException(String.format(message, option.getClass().getSimpleName(), option.getName()));
            }
        }
    }

    public abstract Integer getIntValue();
    public abstract int setIntValue(int value);

    public abstract Double getDoubleValue();
    public abstract int setDoubleValue(double value);
        
    public abstract String getStringValue();
    public abstract int setStringValue(final String value);
    
    public abstract void clearCache();
            
    protected final void traceReadValueException(final SaneException exception){
        final String messageTemplate = 
            environment.getMessageProvider().translate("IAD", "Failed to read value of \'%1$s\' option");
        final String message = String.format(messageTemplate, optionName);
        environment.getTracer().error(message,exception, EEventSource.IAD);
    }
    
    protected final void traceWriteValueException(final SaneException exception, final Object value){
        final String messageTemplate = 
            environment.getMessageProvider().translate("IAD", "Failed to change value of \'%1$s\' option to \'%2$s\'");
        final String message = String.format(messageTemplate, optionName, String.valueOf(value));
        environment.getTracer().error(message,exception, EEventSource.IAD);
    }
    
    protected final void traceReadValue(final int[] values){
        final String messageTemplate = 
            environment.getMessageProvider().translate("IAD", "Value of \'%1$s\' option is %2$s");
        final StringBuilder valAsStr = new StringBuilder();
        if (values==null){
            valAsStr.append("null");
        }else if (values.length==0){
            valAsStr.append("{}");
        }else if (values.length==1){
            valAsStr.append(values[0]);
        }else{
            valAsStr.append("{");
            for (int i=0; i<values.length; i++){
                if (i>0){
                    valAsStr.append(", ");
                }
                valAsStr.append(String.valueOf(values[i]));
            }
            valAsStr.append("}");
        }        
        final String message = String.format(messageTemplate, optionName, valAsStr.toString());
        trace(message);        
    }
    
    protected final void traceReadValue(final boolean[] values){
        final String messageTemplate = 
            environment.getMessageProvider().translate("IAD", "Value of \'%1$s\' option is %2$s");
        final StringBuilder valAsStr = new StringBuilder();
        if (values==null){
            valAsStr.append("null");
        } else if (values.length==0){
            valAsStr.append("{}");
        }else if (values.length==1){
            valAsStr.append(values[0]==true ? "true" : "false");
        }else{
            valAsStr.append("{");
            for (int i=0; i<values.length; i++){
                if (i>0){
                    valAsStr.append(", ");
                }
                valAsStr.append(String.valueOf(values[i]==true ? "true" : "false"));
            }
            valAsStr.append("}");
        }        
        final String message = String.format(messageTemplate, optionName, valAsStr.toString());
        trace(message);        
    }
    
    protected final void traceReadValue(final String value){
        final String messageTemplate = 
            environment.getMessageProvider().translate("IAD", "Value of \'%1$s\' option is \'%2$s\'");
        final String message = String.format(messageTemplate, optionName, String.valueOf(value));
        trace(message);
    }
    
    protected final void traceWriteValue(final int value, final int result){
        traceWriteValue(String.valueOf(value), result);
    }
    
    protected final void traceWriteValue(final String value, final int result){
        final String messageTemplate = 
            environment.getMessageProvider().translate("IAD", "Value of \'%1$s\' option was changed to %2$s. Operation result is %3$s");
        final String message = String.format(messageTemplate, optionName, value, String.valueOf(result));
        trace(message);
    }
    
    private void trace(final String message){
        environment.getTracer().put(EEventSeverity.DEBUG,message,EEventSource.IAD);
    }
    
    public final void addChangeOptionValueListener(SaneDevice.IChangeOptionValueListener listener){
        if (listener!=null){
            if (changeValueListeners==null){
                changeValueListeners = new LinkedList<>();
                changeValueListeners.add(listener);
            }else if (!changeValueListeners.contains(listener)){
                changeValueListeners.add(listener);
            }
        }
    }
    
    public final void removeChangeValueListener(SaneDevice.IChangeOptionValueListener listener){
        if (changeValueListeners!=null){
            changeValueListeners.remove(listener);
        }
    }
    
    protected final void notifyValueChanged(){
        if (changeValueListeners!=null){
            final List<SaneDevice.IChangeOptionValueListener> listeners = new LinkedList<>(changeValueListeners);
            for (SaneDevice.IChangeOptionValueListener listener: listeners){
                listener.optionValueChanged();
            }
        }
    }
}
