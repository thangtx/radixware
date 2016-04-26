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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.types.Pid;


public abstract class AbstractBatchOperationResult<T> {
    
    public final static class ExceptionInfo{
        
        private final String exceptionClass;
        private final String message;
        private final String stack;
        
        public ExceptionInfo (final String exClass, final String exMess, final String exStack){
            exceptionClass = exClass;
            message = exMess;
            stack = exStack;
        }
        
        public ExceptionInfo(final Throwable exception){
            exceptionClass = exception.getClass().getName();
            message = exception.getMessage();
            stack = ClientException.exceptionStackToString(exception);
        }

        public String getExceptionClass() {
            return exceptionClass;
        }

        public String getExceptionMessage() {
            return message;
        }

        public String getExceptionStackTrace() {
            return stack;
        }
    }
    
    protected static class Record<T>{
        
        private final T reason;
        private final String message;
        private final ExceptionInfo exception;
        
        public Record(final T reason, final String message, final ExceptionInfo exception){
            this.reason = reason;
            this.message = message;
            this.exception = exception;
        }
        
        public final T getRejectionReason() {
            return reason;
        }
        
        public final String getMessage() {
            return message;
        }

        public final ExceptionInfo getExceptionInfo() {
            return exception;
        }
        
        public final boolean wasException(){
            return exception!=null;
        }
    }
    
    private final boolean cancelledProgrammatically;
    private final boolean cancelledByUser;
    private int numberOfProcessedObjects;
    private int numberOfRejectedObjects;
    private Map<String,Record<T>> recordsByPid;
    
    protected AbstractBatchOperationResult(final boolean cancelledByUser, final boolean cancelledProgrammatically){
        this.cancelledByUser = cancelledByUser;
        this.cancelledProgrammatically = cancelledProgrammatically;
    }
            
    public AbstractBatchOperationResult(){
        this(false,false);
    }
    
    protected final void setNumberOfProcessedObjects(final int number){
        numberOfProcessedObjects = number;
    }
    
    protected final int getNumberOfProcessedObjects(){
        return numberOfProcessedObjects;
    }
    
    protected final void addRecord(final String pidAsStr, final Record<T> record){
        if (record!=null && pidAsStr!=null){
            if (recordsByPid==null){
                recordsByPid = new LinkedHashMap<>();
            }
            recordsByPid.put(pidAsStr, record);
            if (record.getRejectionReason()!=null){
                numberOfRejectedObjects++;
            }
        }
    }
        
    public final boolean wasCancelledByUser(){
        return cancelledByUser;
    }
    
    public final boolean wasCancelledProgrammatically(){
        return cancelledProgrammatically;
    }
    
    protected final boolean objectWasProcessed(final Pid pid){
        final Record record = getRecordByPid(pid);
        return record==null || record.getRejectionReason()==null;
    }
    
    public final List<String> getRejectedObjectPids(){
        if (recordsByPid==null){
            return Collections.emptyList();
        }
        final List<String> result = new LinkedList<>();
        for (Map.Entry<String,Record<T>> entry: recordsByPid.entrySet()){
            if (entry.getValue().getRejectionReason()!=null){
                result.add(entry.getKey());
            }
        }
        return result;
    }
    
    public final List<String> getRejectionMessages(){
        if (recordsByPid==null){
            return Collections.emptyList();
        }
        final List<String> result = new LinkedList<>();
        for (Map.Entry<String,Record<T>> entry: recordsByPid.entrySet()){
            if (entry.getValue().getRejectionReason()!=null){
                result.add(entry.getValue().getMessage());
            }
        }
        return result;
    }
    
    protected final List<Record<T>> getRecords(){
        if (recordsByPid==null){
            return Collections.emptyList();
        }
        return new LinkedList<>(recordsByPid.values());
    }
    
    public final String getRejectionMessage(final Pid pid){
        final Record record = getRecordByPid(pid);
        return record==null || record.getRejectionReason()==null ? null : record.getMessage();
    }
    
    public final String getRejectionMessage(final String pidAsString){
        final Record record = getRecordByPid(pidAsString);
        return record==null || record.getRejectionReason()==null ? null : record.getMessage();        
    }
    
    public final T getRejectionReason(final Pid pid){
        final Record<T> record = getRecordByPid(pid);
        return record==null ? null : record.getRejectionReason();
    }
    
    public final T getRejectionReason(final String pidAsString){
        final Record<T> record = getRecordByPid(pidAsString);
        return record==null ? null : record.getRejectionReason();
    }    
    
    public final ExceptionInfo getExceptionInfo(final Pid pid){
        final Record record = getRecordByPid(pid);
        return record==null ? null : record.getExceptionInfo();
    }
    
    public final ExceptionInfo getExceptionInfo(final String pidAsString){
        final Record record = getRecordByPid(pidAsString);
        return record==null ? null : record.getExceptionInfo();
    }
        
    public final int getNumberOfRejections(){
        return numberOfRejectedObjects;
    }
    
    private Record<T> getRecordByPid(final String pidAsStr){
        return recordsByPid==null ? null : recordsByPid.get(pidAsStr);
    }
    
    private Record<T> getRecordByPid(final Pid pid){
        return getRecordByPid(pid.toString());
    }    
    
}
