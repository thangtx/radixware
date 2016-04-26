/*
* Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.types;

import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;


public final class UnacceptableInput {        
    
    private final InvalidValueReason reason;
    private final String inputText;
    private final IClientEnvironment environment;    
    
    public UnacceptableInput(final IClientEnvironment environment, final InvalidValueReason validationResult, final String text){
        this.reason = validationResult;
        this.inputText = text;
        this.environment = environment;
    }
    
    public void showMessage(final String title, final String firstMessageLine){
        final MessageProvider messageProvider = environment.getMessageProvider();
        final String dialogTitle = title==null || title.isEmpty() ? messageProvider.translate("Value", "Value is Invalid") : title;
        final String message;
        if (firstMessageLine==null || firstMessageLine.isEmpty()){
            message = reason.getMessage(messageProvider, InvalidValueReason.EMessageType.Value);
                
        }else{
            message = firstMessageLine+"\n"+
                reason.getMessage(messageProvider, InvalidValueReason.EMessageType.Value);
        }
        environment.messageError(dialogTitle, message);        
    }
    
    public void showMessage(){
        showMessage(null, null);
    }
    
    public String getMessageText(final InvalidValueReason.EMessageType messageType){
        final MessageProvider mp = environment.getMessageProvider();
        return reason.getMessage(mp, messageType==null ? InvalidValueReason.EMessageType.Value : messageType);
    }
    
    public String getMessageText(){
        return getMessageText(null);
    }

    public InvalidValueReason getReason() {
        return reason;
    }

    public String getText() {
        return inputText;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.reason);
        hash = 37 * hash + Objects.hashCode(this.inputText);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UnacceptableInput other = (UnacceptableInput) obj;
        return this.reason==other.reason 
               && Objects.equals(this.inputText, other.inputText);
    }

    @Override
    public String toString() {
        return reason.toString()+"; inputText=\'"+inputText+"\'";
    }
    
    
        
}
