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


public class SaneOptionValueUtils {
    
    private static final int FIXED_MAX = 32767;
    private static final int FIXED_MIN = -32768;
    private static final double FIXED_QUANT = 0.0001;
    private static final int SANE_FIXED_SCALE_SHIFT = 16;    

    private SaneOptionValueUtils(){        
    }    
    
    public static double fixedValue2Double(final int value){
        return (double)value/(1<<SANE_FIXED_SCALE_SHIFT);        
    }
    
    public static int double2fixedValue(final double value){
        return (int)(value*(1<<SANE_FIXED_SCALE_SHIFT));
    }
    
    public static int getMaxOptionIntValue(final OptionDescriptor option){
        final com.tuneology.sane.SaneRange range = option.getRange();        
        if (range==null){
            final Object[] values = option.getElements();
            if (values!=null && values.length>0 && values[values.length-1] instanceof Integer){
                return ((Integer)values[values.length-1]).intValue();
            }else{
                if (option instanceof IntegerOption){
                    return Integer.MAX_VALUE;
                }else if (option instanceof FixedOption){
                    return FIXED_MAX;
                }else if (option instanceof BooleanOption){
                    return 1;
                }else {
                    final String message = "Unable to determine maximum value of \'%1$s\' option \'%2$s\'";
                    throw new IllegalArgumentException(String.format(message, option.getClass().getSimpleName(), option.getName()));
                }
            }         
        }else{
            return range.getMax();
        }
    }
    
    public static int getMinOptionIntValue(final OptionDescriptor option){
        final com.tuneology.sane.SaneRange range = option.getRange();        
        if (range==null){
            final Object[] values = option.getElements();
            if (values!=null && values.length>0 && values[0] instanceof Integer){
                return ((Integer)values[0]).intValue();
            }else{
                if (option instanceof IntegerOption){
                    return Integer.MIN_VALUE;
                }else if (option instanceof FixedOption){
                    return FIXED_MIN;
                }else if (option instanceof BooleanOption){
                    return 0;
                }else {
                    final String message = "Unable to determine minimum value of \'%1$s\' option \'%2$s\'";
                    throw new IllegalArgumentException(String.format(message, option.getClass().getSimpleName(), option.getName()));
                }
            }
        }else{
            return range.getMin();
        }
    }
    
    public static double getMaxOptionDoubleValue(final OptionDescriptor option){
        final int value = getMaxOptionIntValue(option);
        return option instanceof FixedOption ? fixedValue2Double(value) : value;
    }
    
    public static double getMinOptionDoubleValue(final OptionDescriptor option){
        final int value = getMinOptionIntValue(option);
        return option instanceof FixedOption ? fixedValue2Double(value) : value;
    }
    
    public static int getOptionIntValueQuant(final OptionDescriptor option){
        final com.tuneology.sane.SaneRange range = option.getRange();
        return range==null ? 1 : range.getQuant();
    }
    
    public static double getOptionDoubleValueQuant(final OptionDescriptor option){
        final com.tuneology.sane.SaneRange range = option.getRange();
        return range==null ? FIXED_QUANT : fixedValue2Double(range.getQuant());
    }
}
