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
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


public class FilterParameterPersistentValue implements ISqmlParameterPersistentValue {

    private Object value;
    private final Id editorPresentationId;
    private final EValType type;

    public FilterParameterPersistentValue(final EntityModel entity) {
        value = new Reference(entity.getPid(), entity.getTitle());
        type = EValType.PARENT_REF;
        editorPresentationId = entity.getEditorPresentationDef().getId();
    }

    public FilterParameterPersistentValue(final Reference ref, final Id editorPresentationId) {
        value = new Reference(ref);
        type = EValType.PARENT_REF;
        this.editorPresentationId = editorPresentationId;
    }

    public FilterParameterPersistentValue(final Object value, final EValType valType) {
        this.value = value;
        type = valType;
        this.editorPresentationId = null;
    }

    public FilterParameterPersistentValue(final FilterParameterPersistentValue source) {
        type = source.type;
        editorPresentationId = source.editorPresentationId;
        if (source.value instanceof Pid) {
            value = source.value;
        } else {
            value = PropertyValue.copyValue(source.value, type);
        }
    }

    private FilterParameterPersistentValue(final Pid pid, final Id editorPresentationId) {
        value = pid;
        type = EValType.PARENT_REF;
        this.editorPresentationId = editorPresentationId;
    }

    public static FilterParameterPersistentValue loadFromXml(IClientEnvironment environment, final ISqmlParameter parameter,
            final org.radixware.schemas.groupsettings.CustomFilter.PersistentValues.PersistentValue paramValue) {
        if (paramValue.getValAsStr() == null) {
            return new FilterParameterPersistentValue(null, parameter.getType());
        } else if (parameter.getType() == EValType.PARENT_REF) {
            final Pid pid = new Pid(parameter.getReferencedTableId(), paramValue.getValAsStr());
            Id editorPresentationId = paramValue.getEditorPresentationId();
            if (editorPresentationId != null) {
                try {
                    environment.getDefManager().getEditorPresentationDef(editorPresentationId);
                } catch (DefinitionError error) {
                    //Editor presentation was not found by stored id - try to use default
                    editorPresentationId = null;
                }
            }
            return new FilterParameterPersistentValue(pid, editorPresentationId);
        } else {
            final EValType valType = parameter.getType();
            try {
                return new FilterParameterPersistentValue(ValAsStr.fromStr(paramValue.getValAsStr(), valType), valType);
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
        return value;
    }

    @Override
    public boolean isValid(IClientEnvironment environment) {
        if (value instanceof Pid) {
            final Pid pid = (Pid) value;
            try {
                final String title;
                if (editorPresentationId != null) {
                    title = pid.getEntityTitleInPresentation(environment.getEasSession(), editorPresentationId);
                } else {
                    title = pid.getDefaultEntityTitle(environment.getEasSession());
                }
                value = new Reference(pid, title);
            } catch (ServiceCallFault fault) {
                value = new Reference(pid, pid.toString(), pid.toString());
                if (org.radixware.schemas.eas.ExceptionEnum.OBJECT_NOT_FOUND.toString().equals(fault.getFaultString())) {
                    final String message = environment.getMessageProvider().translate("TraceMessage", "Can't actualize filter parameter value: object %s was not found in table #%s");
                    environment.getTracer().warning(String.format(message, pid.toString(), pid.getTableId()));
                } else {
                    final String reason = ClientException.getExceptionReason(environment.getMessageProvider(), fault);
                    final String stack = ClientException.exceptionStackToString(fault);
                    final String message = environment.getMessageProvider().translate("TraceMessage", "Can't actualize filter parameter value: %s\n%s");
                    environment.getTracer().warning(String.format(message, reason, stack));
                }
                return false;
            } catch (ServiceClientException exception) {
                value = new Reference(pid, pid.toString(), pid.toString());
                final String reason = ClientException.getExceptionReason(environment.getMessageProvider(), exception);
                final String stack = ClientException.exceptionStackToString(exception);
                final String message = environment.getMessageProvider().translate("TraceMessage", "Can't actualize filter parameter value: %s\n%s");
                environment.getTracer().warning(String.format(message, reason, stack));
                return false;
            } catch (InterruptedException exception) {
                return false;
            }
        }
        if (value instanceof Reference) {
            return !((Reference) value).isBroken();
        }
        return true;
    }

    @Override
    public String getValAsStr() {
        if (value instanceof Pid) {
            return ((Pid) value).toString();
        } else if (value instanceof Reference) {
            final Reference ref = (Reference) value;
            return ref.getPid() == null ? null : ref.getPid().toString();
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
}
