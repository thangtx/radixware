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

package org.radixware.kernel.common.client.meta.mask.validators;

import java.util.EnumMap;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;


public class InvalidValueReason {
    
    public static enum EMessageType{
        Value,
        PropertyValue,
        ParameterValue,
        ThisPropertyValue,
        ThisParameterValue
    }

    public final static InvalidValueReason NO_REASON = new InvalidValueReason(null);
    public final static InvalidValueReason WRONG_FORMAT = new InvalidValueReason(null);
    public final static InvalidValueReason NOT_DEFINED = new InvalidValueReason(null);

    private final EnumMap<EMessageType, String> messages = new EnumMap<EMessageType, String>(EMessageType.class);
    

    private InvalidValueReason(final EnumMap<EMessageType, String> messages){
        if (messages!=null)
            this.messages.putAll(messages);
    }
    
    public static class Factory{

        private Factory(){
        }

        public static InvalidValueReason createForInvalidValue(final String reason){
            final EnumMap<EMessageType, String> messages = new EnumMap<EMessageType, String>(EMessageType.class);
            messages.put(EMessageType.Value, reason);
            return new InvalidValueReason(messages);
        }

        public static InvalidValueReason createForInvalidValueType(final IClientEnvironment environment){
            final MessageProvider mp = environment.getMessageProvider();
            final EnumMap<EMessageType, String> messages = new EnumMap<EMessageType, String>(EMessageType.class);            
            messages.put(EMessageType.Value, mp.translate("Value", "Value type is invalid"));
            messages.put(EMessageType.PropertyValue, mp.translate("Value", "Value of property \'%1$s\' in \'%2$s\' has invalid type"));
            messages.put(EMessageType.ParameterValue, mp.translate("Value", "Value of parameter \'%1$s\' in \'%2$s\' has invalid type"));
            messages.put(EMessageType.ThisPropertyValue, mp.translate("Value", "Value of property \'%1$s\' has invalid type"));
            messages.put(EMessageType.ThisParameterValue, mp.translate("Value", "Value of parameter \'%1$s\' has invalid type"));
            return new InvalidValueReason(messages);
        }
        
        public static InvalidValueReason createForDeprecatedValue(final IClientEnvironment environment){
            final MessageProvider mp = environment.getMessageProvider();
            final EnumMap<EMessageType, String> messages = new EnumMap<EMessageType, String>(EMessageType.class);
            messages.put(EMessageType.Value, mp.translate("Value", "Deprecated value"));
            messages.put(EMessageType.PropertyValue, mp.translate("Value", "Value of property \'%1$s\' in \'%2$s\' is deprecated"));
            messages.put(EMessageType.ParameterValue, mp.translate("Value", "Value of parameter \'%1$s\' in \'%2$s\' is deprecated"));
            messages.put(EMessageType.ThisPropertyValue, mp.translate("Value", "Value of property \'%1$s\' is deprecated"));
            messages.put(EMessageType.ThisParameterValue, mp.translate("Value", "Value of parameter \'%1$s\' is deprecated"));
            return new InvalidValueReason(messages);
        }        
        
        public static InvalidValueReason createForValueWithNonprintableChars(final IClientEnvironment environment){
            final MessageProvider mp = environment.getMessageProvider();
            final EnumMap<EMessageType, String> messages = new EnumMap<EMessageType, String>(EMessageType.class);            
            messages.put(EMessageType.Value, mp.translate("Value", "Value contains non-printing characters that will be omitted if you change this field"));
            messages.put(EMessageType.PropertyValue, mp.translate("Value", "Value of property \'%1$s\' in \'%2$s\' contains non-printing characters that will be omitted if you change this field"));
            messages.put(EMessageType.ParameterValue, mp.translate("Value", "Value of parameter \'%1$s\' in \'%2$s\' contains non-printing characters that will be omitted if you change this field"));
            messages.put(EMessageType.ThisPropertyValue, mp.translate("Value", "Value of property \'%1$s\' contains non-printing characters that will be omitted if you change this field"));
            messages.put(EMessageType.ThisParameterValue, mp.translate("Value", "Value of parameter \'%1$s\' contains non-printing characters that will be omitted if you change this field"));
            return new InvalidValueReason(messages);
        }

        public static InvalidValueReason createForTooBigValue(final IClientEnvironment environment, final String maxValueAsStr){
            final MessageProvider mp = environment.getMessageProvider();
            final EnumMap<EMessageType, String> messages = new EnumMap<EMessageType, String>(EMessageType.class);            
            messages.put(EMessageType.Value, String.format(mp.translate("Value", "Value is too big. The highest acceptable value is %1$s"), maxValueAsStr));
            messages.put(EMessageType.PropertyValue, 
                         String.format(mp.translate("Value", "Value of property \'%1$s\' in \'%2$s\' is too big. The highest acceptable value is %3$s"), "%1$s", "%2$s", maxValueAsStr));
            messages.put(EMessageType.ParameterValue, 
                         String.format(mp.translate("Value", "Value of parameter \'%1$s\' in \'%2$s\' is too big. The highest acceptable value is %3$s"), "%1$s", "%2$s", maxValueAsStr));
            messages.put(EMessageType.ThisPropertyValue, 
                         String.format(mp.translate("Value", "Value of property \'%1$s\' is too big. The highest acceptable value is %2$s"), "%1$s", maxValueAsStr));
            messages.put(EMessageType.ThisParameterValue, 
                         String.format(mp.translate("Value", "Value of parameter \'%1$s\' is too big. The highest acceptable value is %2$s"), "%1$s", maxValueAsStr));
            return new InvalidValueReason(messages);
        }

        public static InvalidValueReason createForTooSmallValue(final IClientEnvironment environment, final String minValueAsStr){
            final MessageProvider mp = environment.getMessageProvider();
            final EnumMap<EMessageType, String> messages = new EnumMap<EMessageType, String>(EMessageType.class);            
            messages.put(EMessageType.Value, String.format(mp.translate("Value", "Value is too small. The lowest acceptable value is %1$s"),minValueAsStr));
            messages.put(EMessageType.PropertyValue, 
                         String.format(mp.translate("Value", "Value of property \'%1$s\' in \'%2$s\' is too small. The lowest acceptable value is %3$s"), "%1$s", "%2$s", minValueAsStr));
            messages.put(EMessageType.ParameterValue, 
                         String.format(mp.translate("Value", "Value of parameter \'%1$s\' in \'%2$s\' is too small. The lowest acceptable value is %3$s"), "%1$s", "%2$s", minValueAsStr));
            messages.put(EMessageType.ThisPropertyValue, 
                         String.format(mp.translate("Value", "Value of property \'%1$s\' is too small. The lowest acceptable value is %2$s"), "%1$s", minValueAsStr));
            messages.put(EMessageType.ThisParameterValue, 
                         String.format(mp.translate("Value", "Value of parameter \'%1$s\' is too small. The lowest acceptable value is %2$s"), "%1$s", minValueAsStr));
            return new InvalidValueReason(messages);
        }

        public static InvalidValueReason createForOutOfRange(final IClientEnvironment environment, final String minValueAsStr, final String maxValueAsStr){
            final MessageProvider mp = environment.getMessageProvider();
            final EnumMap<EMessageType, String> messages = new EnumMap<EMessageType, String>(EMessageType.class);            
            messages.put(EMessageType.Value, String.format(mp.translate("Value", "Value must be between %1$s and %2$s"),minValueAsStr, maxValueAsStr));
            messages.put(EMessageType.PropertyValue, 
                            String.format(mp.translate("Value", "Value of property \'%1$s\' in \'%2$s\' must be between %3$s and %4$s"), "%1$s", "%2$s", minValueAsStr, maxValueAsStr));
            messages.put(EMessageType.ParameterValue, 
                            String.format(mp.translate("Value", "Value of parameter \'%1$s\' in \'%2$s\' must be between %3$s and %4$s"), "%1$s", "%2$s", minValueAsStr, maxValueAsStr));
            messages.put(EMessageType.ThisPropertyValue, 
                            String.format(mp.translate("Value", "Value of property \'%1$s\' must be between %2$s and %3$s"), "%1$s", minValueAsStr, maxValueAsStr));
            messages.put(EMessageType.ThisParameterValue, 
                            String.format(mp.translate("Value", "Value of parameter \'%1$s\' must be between %2$s and %3$s"), "%1$s", minValueAsStr, maxValueAsStr));
            return new InvalidValueReason(messages);
        }
        
        public static InvalidValueReason createForInfeasibleValue(final IClientEnvironment environment, final String valueAsStr){
            final MessageProvider mp = environment.getMessageProvider();
            final EnumMap<EMessageType, String> messages = new EnumMap<EMessageType, String>(EMessageType.class);            
            messages.put(EMessageType.Value, String.format(mp.translate("Value", "Value cannot be equal to %1$s"),valueAsStr));
            final String safeValueAsStr = valueAsStr==null || valueAsStr.isEmpty() ? valueAsStr : valueAsStr.replace("%", "%%");
            messages.put(EMessageType.PropertyValue, 
                         String.format(mp.translate("Value", "Value of property \'%1$s\' in \'%2$s\' cannot be equal to %3$s"), "%1$s", "%2$s", safeValueAsStr));
            messages.put(EMessageType.ParameterValue, 
                         String.format(mp.translate("Value", "Value of parameter \'%1$s\' in \'%2$s\' cannot be equal to %3$s"), "%1$s", "%2$s", safeValueAsStr));
            messages.put(EMessageType.ThisPropertyValue, 
                         String.format(mp.translate("Value", "Value of property \'%1$s\' cannot be equal to %2$s"), "%1$s", safeValueAsStr));
            messages.put(EMessageType.ThisParameterValue, 
                         String.format(mp.translate("Value", "Value of parameter \'%1$s\' cannot be equal to %2$s"), "%1$s", safeValueAsStr));
            return new InvalidValueReason(messages);
        }
    }

    @Override
    public String toString(){
        return messages.get(EMessageType.Value);
    }
    
    public String getMessage(final MessageProvider mp, EMessageType messageType){
        if (this==NO_REASON){
            switch (messageType){
                case ParameterValue:
                    return mp.translate("Value", "Parameter \'%1$s\' in \'%2$s\' has invalid value");
                case PropertyValue:
                    return mp.translate("Value", "Property \'%1$s\' in \'%2$s\' has invalid value");
                case ThisParameterValue:
                    return mp.translate("Value", "Parameter \'%1$s\' has invalid value");
                case ThisPropertyValue:
                    return mp.translate("Value", "Property \'%1$s\' has invalid value");
                default:
                    return mp.translate("Value", "Invalid value");
            }
        }
        else if (this==WRONG_FORMAT){
            switch (messageType){
                case ParameterValue:
                    return mp.translate("Value", "Value of parameter \'%1$s\' in \'%2$s\' has wrong format");
                case PropertyValue:
                    return mp.translate("Value", "Value of property \'%1$s\' in \'%2$s\' has wrong format");
                case ThisParameterValue:
                    return mp.translate("Value", "Value of parameter \'%1$s\' has wrong format");
                case ThisPropertyValue:
                    return mp.translate("Value", "Value of property \'%1$s\' has wrong format");
                default:
                    return mp.translate("Value", "Value has wrong format");
            }            
        }
        else if (this==NOT_DEFINED){
            switch (messageType){
                case ParameterValue:
                    return mp.translate("Value", "Value of parameter \'%1$s\' in \'%2$s\' must be defined");
                case PropertyValue:
                    return mp.translate("Value", "Value of property \'%1$s\' in \'%2$s\' must be defined");
                case ThisParameterValue:
                    return mp.translate("Value", "Value of parameter \'%1$s\' must be defined");
                case ThisPropertyValue:
                    return mp.translate("Value", "Value of property \'%1$s\' must be defined");
                default:
                    return mp.translate("Value", "Value must be defined");
            }            
        }
        if (messages.get(messageType)==null){
            final String reason = toString();
            if (reason!=null && !reason.isEmpty()){
                switch (messageType){
                    case ParameterValue:
                        return String.format(mp.translate("Value", "Parameter \'%1$s\' in \'%2$s\' has invalid value: %3$s"), "%1$s", "%2$s", reason.replace("%", "%%"));
                    case PropertyValue:
                        return String.format(mp.translate("Value", "Property \'%1$s\' in \'%2$s\' has invalid value: %3$s"), "%1$s", "%2$s", reason.replace("%", "%%"));
                    case ThisParameterValue:
                        return String.format(mp.translate("Value", "Parameter \'%1$s\' has invalid value: %2$s"), "%1$s", reason.replace("%", "%%"));
                    case ThisPropertyValue:
                        return String.format(mp.translate("Value", "Property \'%1$s\' has invalid value: %2$s"), "%1$s", reason.replace("%", "%%"));
                    default:
                        return reason;
                }                    
            }
            else{
                return NO_REASON.getMessage(mp, messageType);
            }
        }
        return messages.get(messageType);
    }
}
