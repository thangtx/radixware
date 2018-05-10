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

package org.radixware.kernel.common.client.types;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameterPersistentValue;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.items.properties.PropertyValue;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


public class FilterParameterPersistentValue implements ISqmlParameterPersistentValue {

    private Object value;
    private final Id editorPresentationId;
    private final boolean isReadOnly;
    private final EValType type;

    public FilterParameterPersistentValue(final EntityModel entity, final boolean isReadOnly) {
        value = new Reference(entity.getPid(), entity.getTitle());
        type = EValType.PARENT_REF;
        editorPresentationId = entity.getEditorPresentationDef().getId();
        this.isReadOnly = isReadOnly;
    }

    public FilterParameterPersistentValue(final Reference ref, final Id editorPresentationId, final boolean isReadOnly) {
        value = new Reference(ref);
        type = EValType.PARENT_REF;
        this.editorPresentationId = editorPresentationId;
        this.isReadOnly = isReadOnly;
    }
    
    public FilterParameterPersistentValue(final ArrRef references, final Id editorPresentationId, final boolean isReadOnly) {
        value = new ArrRef(references);
        type = EValType.ARR_REF;
        this.editorPresentationId = editorPresentationId;
        this.isReadOnly = isReadOnly;
    }

    public FilterParameterPersistentValue(final Object value, final EValType valType, final boolean isReadOnly) {
        this.value = copyValue(value, valType);
        type = valType;
        this.editorPresentationId = null;
        this.isReadOnly = isReadOnly;
    }

    public FilterParameterPersistentValue(final FilterParameterPersistentValue source) {
        type = source.type;
        editorPresentationId = source.editorPresentationId;
        value = copyValue(source.value, type);
        isReadOnly = source.isReadOnly;
    }

    private FilterParameterPersistentValue(final Pid pid, final Id editorPresentationId, final boolean isReadOnly) {
        value = pid;
        type = EValType.PARENT_REF;
        this.editorPresentationId = editorPresentationId;
        this.isReadOnly = isReadOnly;
    }
    
    private FilterParameterPersistentValue(final ArrPid value, final Id editorPresentationId, final boolean isReadOnly){
        this.value = value;
        type = EValType.ARR_REF;
        this.editorPresentationId = editorPresentationId;
        this.isReadOnly = isReadOnly;
    }

    public static FilterParameterPersistentValue loadFromXml(final IClientEnvironment environment, final ISqmlParameter parameter,
            final org.radixware.schemas.groupsettings.FilterParameterValue paramValue, final boolean isReadOnly) {
        final String valueAsString = paramValue.isSetValueAsStr() ? paramValue.getValueAsStr() : paramValue.getValAsStr();
        if (valueAsString == null) {
            return new FilterParameterPersistentValue(null, parameter.getType(), isReadOnly);
        }
        if (paramValue.getType()!=null && parameter.getType()!=paramValue.getType()){
            return null;//parameter type was changed - ignoring this value
        }
        if (parameter.getType() == EValType.PARENT_REF) {
            final Pid pid = new Pid(parameter.getReferencedTableId(), valueAsString);
            Id editorPresentationId = paramValue.getEditorPresentationId();
            if (editorPresentationId != null) {
                try {
                    environment.getDefManager().getEditorPresentationDef(editorPresentationId);
                } catch (DefinitionError error) {
                    //Editor presentation was not found by stored id - try to use default
                    editorPresentationId = null;
                }
            }
            return new FilterParameterPersistentValue(pid, editorPresentationId, isReadOnly);
        } else if (parameter.getType() == EValType.ARR_REF) {
            final ArrStr arrPidAsStr = ArrStr.fromValAsStr(valueAsString);
            final Id tableId = parameter.getReferencedTableId();
            final ArrPid value = new ArrPid();
            for (String pidAsStr: arrPidAsStr){
                value.add(pidAsStr==null ? null : new Pid(tableId,pidAsStr));
            }
            Id editorPresentationId = paramValue.getEditorPresentationId();
            if (editorPresentationId != null) {
                try {
                    environment.getDefManager().getEditorPresentationDef(editorPresentationId);
                } catch (DefinitionError error) {
                    //Editor presentation was not found by stored id - try to use default
                    editorPresentationId = null;
                }
            }
            return new FilterParameterPersistentValue(value, editorPresentationId, isReadOnly);
        } else {
            final EValType valType = parameter.getType();
            try {
                return new FilterParameterPersistentValue(ValAsStr.fromStr(valueAsString, valType), valType, isReadOnly);
            } catch (WrongFormatError error) {
                final String message = environment.getMessageProvider().translate("TraceMessage", "Can't restore persistent value of %s #%s parameter: %s");
                final String reason = ClientException.getExceptionReason(environment.getMessageProvider(), error);
                environment.getTracer().warning(String.format(message, parameter.getShortName(), parameter.getId(), reason));
                return null;
            }
        }
    }

    @Override
    public Object getValObject() {
        return copyValue(value, type);
    }

    @Override
    public boolean isReadOnly() {
        return isReadOnly;
    }

    @Override
    public boolean isValid(final IClientEnvironment environment) {
        if (value instanceof Pid) {
            final Pid pid = (Pid) value;
            final Reference rawValue = new Reference(pid);
            try {                
                value = rawValue.actualizeTitle(environment, pid.getTableId(), editorPresentationId);
            } catch (ServiceCallFault fault) {
                value = new Reference(pid, pid.toString(), pid.toString());
                if (org.radixware.schemas.eas.ExceptionEnum.OBJECT_NOT_FOUND.toString().equals(fault.getFaultString())) {
                    final String message = environment.getMessageProvider().translate("TraceMessage", "Failed to actualize filter parameter value: object %s was not found in table #%s");
                    environment.getTracer().warning(String.format(message, pid.toString(), pid.getTableId()));
                } else {
                    final String reason = ClientException.getExceptionReason(environment.getMessageProvider(), fault);
                    final String stack = ClientException.exceptionStackToString(fault);
                    final String message = environment.getMessageProvider().translate("TraceMessage", "Failed to actualize filter parameter value: %s\n%s");
                    environment.getTracer().warning(String.format(message, reason, stack));
                }
                return false;
            } catch (ServiceClientException exception) {
                value = new Reference(pid, pid.toString(), pid.toString());
                final String reason = ClientException.getExceptionReason(environment.getMessageProvider(), exception);
                final String stack = ClientException.exceptionStackToString(exception);
                final String message = environment.getMessageProvider().translate("TraceMessage", "Failed to actualize filter parameter value: %s\n%s");
                environment.getTracer().warning(String.format(message, reason, stack));
                return false;
            } catch (InterruptedException exception) {
                return false;
            }
        }else if (value instanceof ArrPid){
            final ArrRef arrRef = ((ArrPid)value).toArrRef();
            final Id tableId = findTableId((ArrPid)value);
            if (tableId==null){
                return true;
            }
            try{
                value = arrRef.actualizeTitles(environment, tableId, editorPresentationId);
            }catch (ServiceClientException exception) {
                final String reason = ClientException.getExceptionReason(environment.getMessageProvider(), exception);
                final String stack = ClientException.exceptionStackToString(exception);
                final String message = environment.getMessageProvider().translate("TraceMessage", "Failed to actualize filter parameter value: %s\n%s");
                environment.getTracer().warning(String.format(message, reason, stack));
                return false;                
            }catch (InterruptedException exception) {
                return false;
            }
        }
        if (value instanceof Reference) {
            return !((Reference) value).isBroken();
        }else if (value instanceof ArrRef){
            for (Reference reference: (ArrRef)value){
                if (reference!=null && reference.isBroken()){
                    return false;
                }
            }
        }
        return true;
    }
    
    private static Id findTableId(final ArrPid arr){
        for (Pid pid: arr){
            if (pid!=null){
                return pid.getTableId();
            }
        }
        return null;
    }

    @Override
    public String getValAsStr() {
        if (value instanceof Pid) {
            return ((Pid) value).toString();
        } else if (value instanceof Reference) {
            final Reference ref = (Reference) value;
            return ref.getPid() == null ? null : ref.getPid().toString();
        } else if (value instanceof ArrRef){
            final ArrStr arrPidAsStr = new ArrStr();
            for (Reference ref: (ArrRef)value){
                arrPidAsStr.add(ref==null || ref.getPid()==null ? null : ref.getPid().toString());
            }
            return arrPidAsStr.toString();
        } else if (value == null) {
            return null;
        }
        return ValAsStr.toStr(value, type);
    }

    @Override
    public Id getEditorPresentationId() {
        return editorPresentationId;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof FilterParameterPersistentValue) {
            final FilterParameterPersistentValue persistentValue = (FilterParameterPersistentValue) obj;
            return type == persistentValue.type
                    && Utils.equals(value, persistentValue.value)
                    && Utils.equals(editorPresentationId, persistentValue.editorPresentationId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.value != null ? this.value.hashCode() : 0);
        hash = 97 * hash + (this.editorPresentationId != null ? this.editorPresentationId.hashCode() : 0);
        hash = 97 * hash + (this.type != null ? this.type.hashCode() : 0);
        return hash;
    }

    @Override
    public ISqmlParameterPersistentValue copy() {
        return new FilterParameterPersistentValue(this);
    }
    
    private static Object copyValue(final Object value, final EValType type){
        if (value==null || value instanceof Pid) {
            return value;
        } else if (value instanceof ArrPid){
            return new ArrPid((ArrPid)value);
        }else {
            return PropertyValue.copyValue(value, type);
        }        
    }
}
