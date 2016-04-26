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

package org.radixware.kernel.common.client.views;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.exceptions.ExceptionMessage;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.AbstractBatchOperationResult;
import org.radixware.kernel.common.client.models.EntityModel;


public final class BatchCopyResult extends AbstractBatchOperationResult<EBatchCopyRejectionReason> {

    private static final class Record extends AbstractBatchOperationResult.Record<EBatchCopyRejectionReason> {

        private final EntityModel newObject;

        public Record(final EBatchCopyRejectionReason reason, final String message) {
            super(reason, message, null);
            this.newObject = null;
        }

        public Record(final AbstractBatchOperationResult.ExceptionInfo exceptionInfo, final String message) {
            super(EBatchCopyRejectionReason.UNEXPECTED_EXCEPTION, message, exceptionInfo);
            this.newObject = null;
        }

        public Record(final EntityModel newObject) {
            super(null, null, null);
            this.newObject = newObject;
        }

        public EntityModel getNewObject() {
            return newObject;
        }
    }    

    public BatchCopyResult() {
        super(false, false);
    }

    private void addCopyRejection(final String pidAsStr, final EBatchCopyRejectionReason reason, final String message) {
        addRecord(pidAsStr, new Record(reason, message));
    }

    private void addCopyRejection(final String pidAsStr, final ExceptionInfo exceptionInfo, final String message) {
        addRecord(pidAsStr, new Record(exceptionInfo, message));
    }

    public void addNewObject(final EntityModel sourceObject, final EntityModel newObject) {
        addRecord(sourceObject.getPid().toString(), new Record(newObject));
    }

    public void addCopyRestricted(final EntityModel sourceModel) {
        final MessageProvider mp = sourceModel.getEnvironment().getMessageProvider();
        final String reasonTemplate = mp.translate("Selector", "Unable to copy object \'%1$s\' because copy operation is not accessible for this object");
        final String reason = String.format(reasonTemplate, sourceModel.getTitle());
        addCopyRejection(sourceModel.getPid().toString(), EBatchCopyRejectionReason.COPY_RESTRICTED, reason);
    }

    public void addCopyCancelledByUser(final EntityModel sourceModel) {
        final MessageProvider mp = sourceModel.getEnvironment().getMessageProvider();
        final String reasonTemplate = mp.translate("Selector", "Creation copy of \'%1$s\' object was cancelled by user");
        final String reason = String.format(reasonTemplate, sourceModel.getTitle());
        addCopyRejection(sourceModel.getPid().toString(), EBatchCopyRejectionReason.CANCELLED_BY_USER, reason);
    }

    public void addCopyCancelledProgrammatically(final EntityModel sourceModel) {
        final MessageProvider mp = sourceModel.getEnvironment().getMessageProvider();
        final String reasonTemplate = mp.translate("Selector", "Creation copy of \'%1$s\' object was cancelled");
        final String reason = String.format(reasonTemplate, sourceModel.getTitle());
        addCopyRejection(sourceModel.getPid().toString(), EBatchCopyRejectionReason.CANCELLED_PROGRAMMATICALLY, reason);
    }

    public void addException(final EntityModel sourceModel, final Throwable exception) {
        final MessageProvider mp = sourceModel.getEnvironment().getMessageProvider();
        final String reasonTemplate = mp.translate("Selector", "Failed to copy object \'%1$s\': %2$s");
        final String exceptionClass = exception.getClass().getName();
        final ExceptionMessage exceptionMessage = new ExceptionMessage(sourceModel.getEnvironment(), exception);
        final String message = exceptionMessage.hasDialogMessage() ? exceptionMessage.getDialogMessage() : exceptionClass;
        final String reason = String.format(reasonTemplate, sourceModel.getTitle(), message);
        final ExceptionInfo exInfo = new ExceptionInfo(exceptionClass, message, exceptionMessage.getTrace());
        addCopyRejection(sourceModel.getPid().toString(), exInfo, reason);
    }

    public int getNumberOfCopies() {
        return getNumberOfProcessedObjects();
    }

    public List<EntityModel> getNewObjects() {
        final List<EntityModel> result = new LinkedList<>();
        final List<AbstractBatchOperationResult.Record<EBatchCopyRejectionReason>> records = getRecords();
        for (AbstractBatchOperationResult.Record<EBatchCopyRejectionReason> record : records) {
            final EntityModel newObject = ((Record) record).getNewObject();
            if (newObject != null) {
                result.add(newObject);
            }
        }
        return result;
    }

}