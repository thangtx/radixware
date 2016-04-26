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

package org.radixware.kernel.common.client.models;

import org.radixware.kernel.common.client.enums.EDeleteRejectionReason;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.types.Pid;


public final class BatchDeleteResult extends AbstractBatchOperationResult<EDeleteRejectionReason> {
            
    public final static BatchDeleteResult CANCELLED_RPOGRAMMATICALLY = new BatchDeleteResult(true, false);
    public final static BatchDeleteResult CANCELLED_BY_USER = new BatchDeleteResult(false, true);
    public final static BatchDeleteResult EMPTY = new BatchDeleteResult(false, false);

    private BatchDeleteResult(final boolean cancelledProgrammativally, final boolean cancelledByUser){
        super(cancelledProgrammativally,cancelledByUser);
    }
    
    BatchDeleteResult(){
        this(false,false);
    }
    
    void setNumberOfDeletedObjects(final int number){
        setNumberOfProcessedObjects(number);
    }
    
    private void addDeleteRejection(final String pidAsStr, final EDeleteRejectionReason reason, final String message){
        addRecord(pidAsStr, new AbstractBatchOperationResult.Record<>(reason, message, null));
    }     
    
    void addBrokenObject(final BrokenEntityModel entityModel){
        final String reason = 
            entityModel.getEnvironment().getMessageProvider().translate("Selector", "Unable to delete broken object \'%1$s\'");
        final String pidAsStr = entityModel.getPid().toString();
        addDeleteRejection(pidAsStr, EDeleteRejectionReason.BROKEN_OBJECT, String.format(reason, pidAsStr));
    }
    
    void addDeleteRestricted(final EntityModel entityModel){        
        addDeleteRestricted(entityModel.getPid().toString(), entityModel.getTitle(), entityModel.getEnvironment().getMessageProvider());
    }
    
    void addDeleteRestricted(final String pidAsStr, final String title, final MessageProvider mp){
        final String reasonTemplate = 
            mp.translate("Selector", "Unable to delete object \'%1$s\' because delete operation is not accessible for this object");
        final String reason = String.format(reasonTemplate, title);
        addDeleteRejection(pidAsStr, EDeleteRejectionReason.DELETE_RESTRICTED, reason);
    }
    
    void addCascadeDeleRestricted(final String pidAsStr, final String title, final String childTitle, final MessageProvider mp){
        final String reasonTemplate = 
            mp.translate("Selector", "Unable to delete object \'%1$s\' because because it is used by \'%2$s\' object");
        final String reason = String.format(reasonTemplate, title, childTitle);
        addDeleteRejection(pidAsStr, EDeleteRejectionReason.DELETE_CASCADE_RESTRICTED, reason);
    }    
    
    void addException(final String pidAsStr, final String title, final String exceptionClass, final String exceptionMessage, final String exceptionStack, final MessageProvider mp){
        final String reasonTemplate = 
            mp.translate("Selector", "Failed to delete object \'%1$s\': %2$s");
        final String message;
        if (exceptionMessage==null || exceptionMessage.isEmpty()){
            message = exceptionClass;
        }else{
            message = exceptionMessage;
        }
        final ExceptionInfo exInfo = new ExceptionInfo(exceptionClass, exceptionMessage, exceptionStack);        
        addRecord(pidAsStr, new AbstractBatchOperationResult.Record<>(EDeleteRejectionReason.UNEXPECTED_EXCEPTION, String.format(reasonTemplate, title, message), exInfo));
    }
    
    public int getNumberOfDeletedObjects(){
        return getNumberOfProcessedObjects();
    }
    
    public boolean objectWasDeleted(final Pid pid){
        return objectWasProcessed(pid);
    }    
}
