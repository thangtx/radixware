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

package org.radixware.kernel.common.client.errors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * Класс-обертка над ServiceCallFault для случая
 * когда getFaultString().equals(ExceptionEnum.OBJECT_NOT_FOUND.toString())
 * Содержит человеко-читаемое сообщение об ошибке.
 * Позволяет определить относится ли данное исключение к заданной модели сущности.
 */
public final class ObjectNotFoundError extends ServiceCallFault implements IClientError {

    static final long serialVersionUID = -1116447636085612794L;
    private final Pid pid;
    private final Collection<Pid> searchInExplorerTree = new ArrayList<Pid>(2);
    private boolean inKnownContext;
    private String message;
    private String detailMessage;

    public ObjectNotFoundError(final EntityModel entity) {
        super("Client", org.radixware.schemas.eas.ExceptionEnum.OBJECT_NOT_FOUND.toString(), null);
        pid = entity.getPid();
        inKnownContext = true;
        final String messageTemplate = entity.getEnvironment().getMessageProvider().translate("ExplorerError", "Object \'%s\' does not exist");
        message = String.format(messageTemplate, entity.getTitle());
        detailMessage = message;
    }
    
    public ObjectNotFoundError(final IClientEnvironment environment, final Pid pid) {
        super("Client", org.radixware.schemas.eas.ExceptionEnum.OBJECT_NOT_FOUND.toString(), null);
        this.pid = pid;
        inKnownContext = false;
        
        final RadClassPresentationDef classDef;
        try {
            classDef = environment.getDefManager().getClassPresentationDef(pid.getTableId());
        } catch (DefinitionError error) {
            {
                final String messageTemplate = environment.getMessageProvider().translate("ExplorerError", "Object with PID \'%s\' does not exist in table #%s");
                detailMessage = String.format(messageTemplate, pid.toString(), pid.getTableId().toString());
            }
            {
                final String messageTemplate = environment.getMessageProvider().translate("ExplorerError", "Object \'%s\' does not exist");
                message = String.format(messageTemplate, pid.toString());
            }
            return;
        }        
        message = String.format(environment.getMessageProvider().translate("ExplorerError", "Object \'%s\' does not exist"), pid.toString());
        final String messageTemplate = environment.getMessageProvider().translate("ExplorerError", "Object with PID \'%s\' does not exist for class #%s");
        detailMessage = String.format(messageTemplate, pid.toString(), classDef.getId().toString());
    }    

    public ObjectNotFoundError(final IClientEnvironment environment, final ServiceCallFault source) {
        super(source.getFaultCode(), source.getFaultString(), source.getDetail());
        pid = parse(source.getMessage());
        searchInExplorerTree.add(pid);
        inKnownContext = false;
        final RadClassPresentationDef classDef;
        try {
            classDef = environment.getDefManager().getClassPresentationDef(pid.getTableId());
        } catch (DefinitionError error) {
            {
                final String messageTemplate = environment.getMessageProvider().translate("ExplorerError", "Object with PID \'%s\' does not exist in table #%s");
                detailMessage = String.format(messageTemplate, pid.toString(), pid.getTableId().toString());
            }
            {
                final String messageTemplate = environment.getMessageProvider().translate("ExplorerError", "Object \'%s\' does not exist");
                message = String.format(messageTemplate, pid.toString());
            }
            return;
        }
        String classTitle;
        if (classDef.hasObjectTitle()) {
            classTitle = classDef.getObjectTitle();
        } else if (classDef.hasGroupTitle()) {
            classTitle = classDef.getGroupTitle();
        } else if (!classDef.getClassTitle().isEmpty()) {
            classTitle = classDef.getClassTitle();
        } else {
            classTitle = "";
        }
        if (!classTitle.isEmpty()) {
            {
                final String messageTemplate = environment.getMessageProvider().translate("ExplorerError", "%s \'%s\' does not exist");
                message = String.format(messageTemplate, classTitle, pid.toString());
            }
            {
                if (classDef.hasGroupTitle()) {
                    classTitle = classDef.getGroupTitle();
                } else if (!classDef.getClassTitle().isEmpty()) {
                    classTitle = classDef.getClassTitle();
                }
                final String messageTemplate = environment.getMessageProvider().translate("ExplorerError", "Object with PID \'%s\' does not exist for class \'%s\' #%s");
                detailMessage = String.format(messageTemplate, pid.toString(), classTitle, classDef.getId().toString());
            }
        } else {
            message = String.format(environment.getMessageProvider().translate("ExplorerError", "Object \'%s\' does not exist"), pid.toString());
            final String messageTemplate = environment.getMessageProvider().translate("ExplorerError", "Object with PID \'%s\' does not exist for class #%s");
            detailMessage = String.format(messageTemplate, pid.toString(), classDef.getId().toString());
        }
    }

    public void setContextEntity(final EntityModel contextEntity) {
        if (contextEntity != null) {
            if (Utils.equalsNotNull(contextEntity.getPid(), pid)) {
                final String messageTemplate = contextEntity.getEnvironment().getMessageProvider().translate("ExplorerError", "Object \'%s\' does not exist");
                message = String.format(messageTemplate, contextEntity.getTitle());
                inKnownContext = true;
            } else if (contextEntity.isNew() && Utils.equalsNotNull(contextEntity.getSrcPid(), pid)) {
                message = contextEntity.getEnvironment().getMessageProvider().translate("ExplorerError", "Source object does not exist");
                inKnownContext = true;
            } else if (contextEntity.getContext() instanceof IContext.ReferencedChoosenEntityEditing) {
                final String messageTemplate = contextEntity.getEnvironment().getMessageProvider().translate("ExplorerError", "Owner of object \'%s\' does not exist");
                message = String.format(messageTemplate, contextEntity.getTitle());
                searchInExplorerTree.add(contextEntity.getPid());
                inKnownContext = true;
            }
        }
    }

    public void setContextReference(final IClientEnvironment environment, final Reference ref) {
        if (inContextOf(ref)) {
            final String messageTemplate = environment.getMessageProvider().translate("ExplorerError", "Object \'%s\' does not exist");
            message = String.format(messageTemplate, ref.getTitle());
            inKnownContext = true;
        }
    }

    private static Pid parse(final String message) {
        /*detailMessage example:
         * tblX5TD7JDVVHWDBROXAAIT4AGD7E
         * PID=867
         *  */
        final StringTokenizer lines = new StringTokenizer(message, "\n");
        final Id entityId = Id.Factory.loadFrom(lines.nextToken());
        final String objectPidAsStr = lines.nextToken().substring(4);
        return new Pid(entityId, objectPidAsStr);
    }

    public boolean inContextOf(final Model model) {
        return ((model instanceof EntityModel) && inContextOfEntityModel((EntityModel) model))
                || ((model instanceof GroupModel) && inContextOfGroupModel((GroupModel) model));
    }

    private boolean inContextOfEntityModel(final EntityModel entity) {
        if (entity != null) {
            if (Utils.equalsNotNull(entity.getPid(), pid)) {
                return true;
            }
            if (entity.isNew() && Utils.equalsNotNull(entity.getSrcPid(), pid)) {
                return true;
            }
            if (entity.getContext() instanceof IContext.ReferencedChoosenEntityEditing) {
                final IContext.ReferencedChoosenEntityEditing context = (IContext.ReferencedChoosenEntityEditing) entity.getContext();
                return Utils.equalsNotNull(context.ownerPid, pid);
            }
        }
        return false;
    }

    public boolean inContextOf(final Reference ref) {
        return ref != null && Utils.equalsNotNull(ref.getPid(), pid);
    }

    public boolean inContextOfGroupModel(final GroupModel group) {
        if (Utils.equalsNotNull(group.getSelectorPresentationDef().getTableId(), pid == null ? null : pid.getTableId())) {
            final int idx = group.findEntityByPid(pid);
            if (idx > -1) {
                try {
                    setContextEntity(group.getEntity(idx));
                } catch (ServiceClientException ex) {
                    // getEntity(idx) was already loaded here - never thrown
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                } catch (InterruptedException ex) {
                    // getEntity(idx) was already loaded here - never thrown
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);                    
                } catch (BrokenEntityObjectException exception){
                    return false;
                }
                
            }
            return true;
        } else if (group.getContext() instanceof IContext.TableSelect) {
            final IContext.TableSelect ctx = (IContext.TableSelect) group.getContext();
            if (inContextOf(ctx.getHolderEntityModel())) {
                setContextEntity(ctx.getHolderEntityModel());
                return true;
            }
        } else if (group.getContext() instanceof IContext.ParentSelect) {
            final IContext.ParentSelect ctx = (IContext.ParentSelect) group.getContext();
            if (inContextOf(ctx.childEntity)) {
                setContextEntity(ctx.childEntity);
            }
        }
        return false;
    }

    public boolean inKnownContext() {
        return inKnownContext;
    }

    //getMessage() не стал перекрывать т.к. ServiceCallFault исключение
    //может обрабатываться в прикладном коде, который ожидает оригинальный
    //формат сообщения
    public String getMessageToShow() {
        return message;
    }

    public Pid getPid() {
        return pid;
    }

    public Collection<Pid> getPidsToSearchInExplorerTree() {
        return Collections.unmodifiableCollection(searchInExplorerTree);
    }

    @Override
    public String getTitle(MessageProvider mp) {
        return mp.translate("ExplorerError", "Object does not exist");
    }

    @Override
    public String getDetailMessage(MessageProvider mp) {
        return detailMessage;
    }

    @Override
    public String getLocalizedMessage(MessageProvider messageProvider) {
        return getDetailMessage(messageProvider);
    }
}
