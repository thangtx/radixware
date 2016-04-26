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

package org.radixware.kernel.common.client.exceptions;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadPropertyDef;

import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.types.Id;

public final class InvalidPropertyValueException extends ModelPropertyException {

    private static final long serialVersionUID = -8651785069899772828L;
    private final Map<String, InvalidValueReason> reasons = new LinkedHashMap<>();

    public InvalidPropertyValueException(final Model model, final RadPropertyDef prop) {
        this(model, prop, InvalidValueReason.NO_REASON);
    }

    public InvalidPropertyValueException(final Model model, final RadPropertyDef prop, final String ValueAsStr) {
        this(model, prop, InvalidValueReason.Factory.createForInfeasibleValue(model.getEnvironment(), ValueAsStr));
    }

    public InvalidPropertyValueException(final Model model, final Id ownerId, final Id propertyId, final String valueAsStr, final String reason) {
        super(model, ownerId, propertyId);
        final InvalidValueReason ivReason;
        if (valueAsStr==null){
            ivReason = reason==null ? InvalidValueReason.NO_REASON : InvalidValueReason.Factory.createForInvalidValue(reason);
        }
        else{
            ivReason = InvalidValueReason.Factory.createForInfeasibleValue(environment, valueAsStr);
        }
        if (isOwnProperty()){
            reasons.put(getPropertyTitle(model, model.getDefinition().getPropertyDefById(propertyId)),ivReason);
        }else{
            reasons.put(getPropertyTitle(),ivReason);
        }
    }
    
    public InvalidPropertyValueException(final Model model, final RadPropertyDef prop, final InvalidValueReason reason){
        super(model, model.getDefinition().getId(), prop.getId());       
        reasons.put(getPropertyTitle(model,prop),reason);
    }
    
    private void addPropertyWithInvalidValue(final Model model, final RadPropertyDef propertyDef, final InvalidValueReason reason){
        reasons.put(getPropertyTitle(model,propertyDef),reason);
    }           

    @Override
    public String getTitle(final MessageProvider mp) {
        int countOfMandatoryProperties = 0;
        for (Map.Entry<String, InvalidValueReason> reasonEntry: reasons.entrySet()){
            if (reasonEntry.getValue()==InvalidValueReason.NOT_DEFINED){
                countOfMandatoryProperties++;
            }            
        }    
        if (countOfMandatoryProperties==reasons.size()){
            return reasons.size()>1 ? mp.translate("ExplorerException", "Values are not Defined") : mp.translate("ExplorerException", "Value is not Defined");
        }
        else{
            return reasons.size()>1 ? mp.translate("ExplorerException", "Invalid Values") : mp.translate("ExplorerException", "Invalid Value");
        }
    }    

    @Override
    public String getMessage(final MessageProvider mp) {
        final StringBuilder messageBuilder = new StringBuilder();
        InvalidValueReason reason;
        String reasonAsStr;
        final List<String> mandatoryProperties = new LinkedList<>();
        for (Map.Entry<String, InvalidValueReason> reasonEntry: reasons.entrySet()){
            reason = reasonEntry.getValue();
            reasonAsStr = getReasonAsString(mp, reason, reasonEntry.getKey());
            if (reason==InvalidValueReason.NOT_DEFINED){
                mandatoryProperties.add(reasonEntry.getKey());
            }
            else{
                if (messageBuilder.length()>0){
                    messageBuilder.append(".\n");
                }
                messageBuilder.append(reasonAsStr);                
            }
        }
        if (!mandatoryProperties.isEmpty()){
            if (messageBuilder.length()>0){
                messageBuilder.append(".\n");
            }            
            messageBuilder.append(getMessageForMandatoryProperties(mp, mandatoryProperties));
        }
        return messageBuilder.toString();
    }
    
    public static InvalidPropertyValueException createForProperties(final Model model, final Map<RadPropertyDef,InvalidValueReason> properties){
        InvalidPropertyValueException instance = null;
        for (Map.Entry<RadPropertyDef,InvalidValueReason> entry: properties.entrySet()){
            if (instance==null){
                instance = new InvalidPropertyValueException(model, entry.getKey(), entry.getValue());
            }
            else{
                instance.addPropertyWithInvalidValue(model, entry.getKey(), entry.getValue());
            }
        }
        return instance;
    }
}
