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

package org.radixware.kernel.server.arte.services.eas;

import java.util.Collection;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.enums.EDefType;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.AppError;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.DeleteCascadeConfirmationRequiredException;
import org.radixware.kernel.server.exceptions.DeleteCascadeRestrictedException;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;
import org.radixware.kernel.server.exceptions.InvalidPropertyValueError;
import org.radixware.kernel.server.exceptions.LoadOldVersionFromDirError;
import org.radixware.kernel.server.exceptions.PropertyIsMandatoryError;
import org.radixware.kernel.server.exceptions.UniqueConstraintViolation;
import org.radixware.schemas.eas.ExceptionEnum;
import org.radixware.schemas.eas.SessionRestorePolicy;
import org.radixware.schemas.eas.UserSessionsDocument;

final class EasFaults {

    private EasFaults() {
    }

    ;
//Std faults

	static ServiceProcessFault exception2Fault(final Arte arte, final Throwable e, final String defPresenterComment) throws InterruptedException {
        if (e instanceof InterruptedException) {
            throw (InterruptedException) e;
        }
        return ex2Flt(arte, e, defPresenterComment);
    }

    static ServiceProcessFault exception2Fault(final Arte arte, final RuntimeException e, final String defPresenterComment) {
        return ex2Flt(arte, e, defPresenterComment);
    }

    private static ServiceProcessFault ex2Flt(final Arte arte, final Throwable e, final String defPresenterComment) {
        if (e instanceof ServiceProcessFault) {
            return (ServiceProcessFault) e;
        }

        String mess = ExceptionTextFormatter.getExceptionMess(e);
        ExceptionEnum.Enum clientFaultReason = null, srvFaultReason = null;
        if (e instanceof EntityObjectNotExistsError) {
            clientFaultReason = ExceptionEnum.OBJECT_NOT_FOUND;
            final EntityObjectNotExistsError ex = (EntityObjectNotExistsError) e;
            mess = String.valueOf(ex.getTable().getId().toString()) + "\n" + String.valueOf(ex.getKeyPres());
        }
        if (e instanceof PropertyIsMandatoryError) {
            return newPropMandatoryFault(((PropertyIsMandatoryError) e).classId, ((PropertyIsMandatoryError) e).propId);
        }
        if (e instanceof InvalidPropertyValueError) {
            clientFaultReason = ExceptionEnum.INVALID_PROPERTY_VALUE;
            final InvalidPropertyValueError ex = (InvalidPropertyValueError) e;
            mess = (ex).getClassId() + "\n" + ex.getPropId() + (ex.getReasonMess() == null ? "" : "\n" + ex.getReasonMess());
        } else if (e instanceof UniqueConstraintViolation) {
            clientFaultReason = ExceptionEnum.UNIQUE_CONSTRAINT_VIOLATION;
            final UniqueConstraintViolation v = (UniqueConstraintViolation) e;
            final StringBuilder messBuilder = new StringBuilder(v.getTable().getId().toString());            
            Collection<Id> parentRefs;
            for (DdsIndexDef.ColumnInfo c : v.getKey().getColumnsInfo()) {
                messBuilder.append("\n");
                messBuilder.append(c.getColumnId());
                parentRefs = v.getParentRefPropIdsForIndexColumn(c.getColumnId());
                for (Id parentRefId: parentRefs){
                    messBuilder.append(" ");
                    messBuilder.append(parentRefId.toString());
                }                
            }
            mess = messBuilder.toString();
        } else if (e instanceof DefinitionNotFoundError
                || e instanceof NoConstItemWithSuchValueError) {
            clientFaultReason = ExceptionEnum.DEFINITION_NOT_FOUND;
        } else if (e instanceof LoadOldVersionFromDirError) {
            srvFaultReason = ExceptionEnum.UNSUPPORTED_DEFINITION_VERSION;
        } else if (e instanceof DeleteCascadeRestrictedException) {
            clientFaultReason = ExceptionEnum.SUBOBJECTS_FOUND;
            mess = e.getMessage();
        } else if (e instanceof DeleteCascadeConfirmationRequiredException) {
            clientFaultReason = ExceptionEnum.CONFIRM_SUBOBJECTS_DELETE;
            mess = e.getMessage();
        } else if (e instanceof IllegalUsageError) {
            clientFaultReason = ExceptionEnum.APPLICATION_ERROR;
        } else if (e instanceof AppError) {
            clientFaultReason = ExceptionEnum.APPLICATION_ERROR;
        } else if (e instanceof XmlException) {
            clientFaultReason = ExceptionEnum.FORMAT_ERROR;
        } else if (e instanceof RuntimeException
                || e instanceof Error) {
            srvFaultReason = ExceptionEnum.SERVER_MALFUNCTION;
        } else {
            srvFaultReason = ExceptionEnum.SERVER_EXCEPTION;
        }
        final String preprocessedExStack = arte.getTrace().exceptionStackToString(e);
        if (clientFaultReason != null) {
            arte.getTrace().put(EEventSeverity.DEBUG, "Client fault caused by: " + arte.getTrace().exceptionStackToString(e), EEventSource.EAS);
            return new ServiceProcessClientFault(clientFaultReason.toString(), mess, e, preprocessedExStack);
        }
        if (srvFaultReason != null) {
            EEventSeverity severity = EEventSeverity.WARNING;
            if (Arte.get() != null && Arte.get().suppressDbForciblyCloseErrors()) {
                severity = EEventSeverity.DEBUG;
            }
            arte.getTrace().put(severity, "Server fault caused by: " + arte.getTrace().exceptionStackToString(e), EEventSource.EAS);
            return new ServiceProcessServerFault(srvFaultReason.toString(), mess, e, preprocessedExStack);
        } else {//Others
            arte.getTrace().put(EEventSeverity.WARNING, "Can't find corresponding faultstring for " + arte.getTrace().exceptionStackToString(e), EEventSource.EAS);
            return new ServiceProcessServerFault(ExceptionEnum.SERVER_EXCEPTION.toString(), defPresenterComment + ": " + mess, e, preprocessedExStack);
        }
    }

    static final ServiceProcessClientFault newDefWithIdNotFoundFault(final String defTokenName, final String defTokenPlace, final Id id) {
        return new ServiceProcessClientFault(ExceptionEnum.DEFINITION_NOT_FOUND.toString(), defTokenPlace + "." + defTokenName + " with \"ID\" = \"" + id + "\" not found", null, null);
    }

    static final ServiceProcessClientFault newDefWithNameNotFoundFault(final String defTokenName, final String defTokenPlace, final String name) {
        return new ServiceProcessClientFault(ExceptionEnum.DEFINITION_NOT_FOUND.toString(), defTokenPlace + "." + defTokenName + " with \"Name\" = \"" + name + "\" not found", null, null);
    }

    static final ServiceProcessClientFault newWrongTranWordFault(final String tranWord_) {
        return new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "Wrong transaction directive \"" + tranWord_ + "\"", null, null);
    }

    static final ServiceProcessClientFault newAccessViolationFault(final Arte arte, final Id mlsId, final String detail) {
        return newAccessViolationFault(arte, MultilingualString.get(arte, Messages.MLS_OWNER_ID, mlsId) + (detail == null ? "" : " " + detail));
    }

    static final ServiceProcessClientFault newAccessViolationFault(final Arte arte, final String mess) {
        //RADIX-4402: "Explorer Access Service prevented access violation: Message: %1, User: %2, Station: %3."
        arte.getTrace().put(
                Messages.MLS_ID_EAS_PREVENTED_ACCESS_VIOLATION,
                new ArrStr(
                mess,
                arte.getUserName(),
                arte.getStationName() + " (" + arte.getArteSocket().getRemoteAddress() + ")"));
        return new ServiceProcessClientFault(ExceptionEnum.ACCESS_VIOLATION.toString(), mess, null, null);
    }

    static final ServiceProcessClientFault newDefinitionAccessViolationFault(final Arte arte, final Id mlsId, final String detail, final EDefType defType, final Id[] idPath) {
        final String mess =
                MultilingualString.get(arte, Messages.MLS_OWNER_ID, mlsId) + (detail == null ? "" : " " + detail);
        arte.getTrace().put(
                Messages.MLS_ID_EAS_PREVENTED_ACCESS_VIOLATION,
                new ArrStr(
                mess,
                arte.getUserName(),
                arte.getStationName() + " (" + arte.getArteSocket().getRemoteAddress() + ")"));
        final StringBuilder faultMessageBuilder = new StringBuilder(String.valueOf(defType.getValue()));
        faultMessageBuilder.append("\n");
        for (int i = 0; i < idPath.length; i++) {
            if (i > 0) {
                faultMessageBuilder.append(" ");
            }
            faultMessageBuilder.append(idPath[i].toString());
        }
        faultMessageBuilder.append("\n");
        faultMessageBuilder.append(mess);
        return new ServiceProcessClientFault(ExceptionEnum.DEFINITION_ACCESS_VIOLATION.toString(), faultMessageBuilder.toString(), null, null);
    }
    
    static ServiceProcessClientFault newLogonTimeRestrictionViolationFault(){
        return new ServiceProcessClientFault(ExceptionEnum.LOGON_TIME_RESTRICTION_VIOLATION.toString(), null, null, null);
    }
    
    static ServiceProcessClientFault newTemporaryPasswordExpiredFault(){
        return new ServiceProcessClientFault(ExceptionEnum.TEMPORARY_PASSWORD_EXPIRED.toString(), null, null, null);
    }    

    static ServiceProcessClientFault newInvalidPassword() {
        return new ServiceProcessClientFault(ExceptionEnum.INVALID_PASSWORD.toString(), "Invalid password", null, null);
    }

    static ServiceProcessClientFault newSessionDoesNotExist(final SessionRestorePolicy.Enum sessionRestorePolicy) {
        return new ServiceProcessClientFault(ExceptionEnum.SESSION_DOES_NOT_EXIST.toString(), sessionRestorePolicy.toString(), null, null);
    }
    
    static ServiceProcessClientFault newSessionsLimitExceedFault(final UserSessionsDocument userSessions){
        return new ServiceProcessClientFault(ExceptionEnum.SESSIONS_LIMIT_EXCEED.toString(), userSessions.xmlText(), null, null);
    }

    static Exception newCantSaveObjPropValUntilOwnerSaved() {
        return new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "Can't store property's object value util owner is stored in database", null, null);
    }

    static final ServiceProcessClientFault newParamRequiedFault(final String param, final String place) {
        return new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "\"" + param + "\" is required in \"" + place + "\"", null, null);
    }

    static final ServiceProcessClientFault newWrongParamValFault(final String param, final String val) {
        return new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(), "\"" + param + "\" value can't be \"" + val + "\"", null, null);
    }

    static final ServiceProcessClientFault newPropMandatoryFault(final Id classId, final Id propId) {
        return new ServiceProcessClientFault(ExceptionEnum.PROPERTY_IS_MANDATORY.toString(), classId + "\n" + propId, null, null);
    }
}
