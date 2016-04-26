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

package org.radixware.kernel.common.client.eas;

import java.math.BigDecimal;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.RadPresentationDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.meta.filters.RadUserFilter;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.EntityObjectsSelection;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.FormModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.PropertyDateTime;
import org.radixware.kernel.common.client.models.items.properties.PropertyInt;
import org.radixware.kernel.common.client.models.items.properties.PropertyNum;
import org.radixware.kernel.common.client.models.items.properties.PropertyRef;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.AppError;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.*;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;

import org.radixware.schemas.eas.*;


public final class RequestCreator {

    private RequestCreator() {
    }

    public static CreateSessionRq createSession(final String userName,
            final EAuthType authType,
            final X509Certificate[] userCertificates,
            final String stationName,
            final EIsoLanguage language,
            final EIsoCountry country,
            final ERuntimeEnvironmentType environmentType,
            final Id desiredExplorerRootId,
            final Long parentSessionId) {
        final CreateSessionRq request = CreateSessionRq.Factory.newInstance();
        request.setAuthType(authType);
        if (authType == EAuthType.PASSWORD) {
            request.setUser(userName);
        }
        request.setLanguage(language);
        request.setCountry(country);
        request.setEnvironment(environmentType);
        request.setStationName(stationName);
        if (userCertificates != null && userCertificates.length>0){
            final CreateSessionRq.UserCertificatesChain certChain = request.addNewUserCertificatesChain();
            try{
                for (X509Certificate certificate: userCertificates){
                    certChain.addItem(certificate.getEncoded());
                }
            }catch(CertificateEncodingException exception){
                throw new RadixError("Unable to open connection: certificate encoding failure", exception);
            }
        }
        if (desiredExplorerRootId != null) {
            request.addNewExplorerRoot().setId(desiredExplorerRootId);
        }
        if (RadixLoader.getInstance() == null) {
            throw new AppError("Radix loader not found. You must use radixware starter to start explorer ");
        }
        final CreateSessionRq.Platform platform = request.addNewPlatform();
        try {
            platform.setDefinitionsVer(RadixLoader.getInstance().getCurrentRevision());
        } catch (RadixLoaderException ex) {
            throw new RadixError("Error on opening session. Can't get version of current revision ", ex);
        }
        platform.setTopLayerUri(RadixLoader.getInstance().getTopLayerUrisAsString());
        platform.setRepositoryUri(RadixLoader.getInstance().getRepositoryUri());
        if (parentSessionId!=null){
            request.setParentSessionId(parentSessionId);
        }
        return request;
    }    

    public static SelectRq select(final GroupModel group,
            final int startIndex,
            final int rowCount,
            final boolean withSelectorAddons,
            final boolean withInstantiatableClasses) {
        final SelectRq selectRequest = SelectRq.Factory.newInstance();
        writeGroup(group, null, selectRequest);

        selectRequest.setStartIndex(startIndex);
        selectRequest.setCount(rowCount);
        if (withSelectorAddons) {
            selectRequest.setWithSelectorAddons(true);
        }
        if (withInstantiatableClasses){
            selectRequest.setWithInstantiatableClasses(true);
        }
        
        if (!group.getRowsWhenReadEntireObject().isEmpty()){
            final List<Integer> rowIndexes = new LinkedList<>();
            for (Integer row: group.getRowsWhenReadEntireObject()){
                rowIndexes.add(Integer.valueOf(row.intValue()+1));
            }
            selectRequest.setEntireObjectIndexes(rowIndexes);
        }
                
        final Set<Pid> entireObjectPids = group.getPidsWhenReadEntireObject();
        if (!entireObjectPids.isEmpty()){
            final List<String> entireObjectPidsAsStr = new LinkedList<>();
            for (Pid pid: entireObjectPids){
                entireObjectPidsAsStr.add(pid.toString());
            }
            selectRequest.setEntireObjectPids(entireObjectPidsAsStr);
        }
        return selectRequest;
    }

    public static GetObjectTitlesRq getObjectTitles(final Id tableId,
            final Id defaultPresentationId,
            final GetObjectTitlesRq.Objects objects,
            final DefManager defManager) {
        final GetObjectTitlesRq request = GetObjectTitlesRq.Factory.newInstance();
        request.addNewEntity().setId(tableId);
        if (defaultPresentationId != null) {
            //RADIX-708            
            final RadPresentationDef presentation;
            final org.radixware.schemas.eas.Context context = request.addNewContext();
            if (defaultPresentationId.getPrefix().equals(EDefinitionIdPrefix.EDITOR_PRESENTATION)) {
                presentation = defManager.getEditorPresentationDef(defaultPresentationId);
                final org.radixware.schemas.eas.Context.Editor xmlPresentation = context.addNewEditor();
                xmlPresentation.setClassId(presentation.getOwnerClassId());
                xmlPresentation.setPresentationId(defaultPresentationId);                                
            } else if (defaultPresentationId.getPrefix().equals(EDefinitionIdPrefix.SELECTOR_PRESENTATION)) {
                presentation = defManager.getSelectorPresentationDef(defaultPresentationId);
                final org.radixware.schemas.eas.Context.Selector xmlPresentation = context.addNewSelector();
                xmlPresentation.setClassId(presentation.getOwnerClassId());
                xmlPresentation.setPresentationId(defaultPresentationId);                
            } else {
                throw new DefinitionError("Can't find presentation #" + defaultPresentationId.toString());
            }
        }
        request.setObjects(objects);
        return request;
    }
    
    public static GetObjectTitlesRq getObjectTitles(final Id tableId,
            final IContext.Abstract defaultContext,
            final GetObjectTitlesRq.Objects objects,
            final DefManager defManager) {
        final GetObjectTitlesRq request = GetObjectTitlesRq.Factory.newInstance();
        request.addNewEntity().setId(tableId);
        if (defaultContext != null) {
            request.setContext(defaultContext.toXml());
        }
        request.setObjects(objects);
        return request;
    }    

    public static DeleteRq deleteMultipleObjects(final GroupModel group, final EntityObjectsSelection selection, final boolean cascade) {
        final DeleteRq request = DeleteRq.Factory.newInstance();
        writeGroup(group, selection, request);
        if (cascade) {
            request.setCascade(cascade);
        }
        return request;
    }
    
    public static ReadRq read(final Pid pid,
            final Id classId,
            final Collection<Id> presentationIds,
            final Collection<Id> propertyIds,
            final IContext.Entity entityContext,
            final boolean withAccessibleExplorerItems) {
        final ReadRq request = ReadRq.Factory.newInstance();
        request.setContext(entityContext.toXml());
        request.addNewClass1().setId(classId);
        request.setPID(pid.toString());
        request.addNewPresentations();
        for (Id presentationId : presentationIds) {
            request.getPresentations().addNewItem().setId(presentationId);
        }
        if (propertyIds != null) {
            final ReadRq.Properties props = request.addNewProperties();
            for (Id propertyId : propertyIds) {
                props.addNewItem().setId(propertyId);
            }
            request.setWithLOBValues(true);
        } else {
            request.setWithLOBValues(false);
        }
        if (withAccessibleExplorerItems){
            request.setWithAccessibleExplorerItems(true);
        }
        return request;
    }

    public static CommandRq command(final Id commandId,
            final Id propertyId,
            final XmlObject input,
            final Model sourceModel,
            final FormModel form) {
        final CommandRq request = CommandRq.Factory.newInstance();
        if (sourceModel instanceof FormModel) {
            final FormModel model = (FormModel) sourceModel;
            request.setForm(model.toXml());
            request.addNewClass1().setId(model.getDefinition().getId());
        } else if (sourceModel instanceof GroupModel) {
            writeGroup((GroupModel) sourceModel, ((GroupModel) sourceModel).getSelection(), request);
        } else if (sourceModel instanceof EntityModel) {
            final EntityModel entity = (EntityModel) sourceModel;
            final RadEditorPresentationDef presentation = entity.getEditorPresentationDef();
            request.setContext(((IContext.Entity) entity.getContext()).toXml());
            request.addNewPresentation().setId(presentation.getId());
            request.addNewClass1().setId(entity.getClassId());
            if (entity.isExists()) {
                request.setPID(entity.getPid().toString());
            }
            entity.writeToXml(request.addNewCurrentData(), true);
        }
        request.addNewCommand().setId(commandId);
        if (propertyId != null) {
            request.addNewProperty().setId(propertyId);
        }
        if (input != null) {
            if (input.getDomNode().getNodeName().equals("Input")) {
                request.setInput(input);
            } else {
                request.addNewInput().set(input);
            }
        }
        if (form != null) {
            request.setForm(form.toXml());
        }
        return request;
    }

    public static ContextlessCommandRq contextlessCommand(final Id commandId, final XmlObject input, final FormModel form) {
        final ContextlessCommandRq request = ContextlessCommandRq.Factory.newInstance();
        if (form != null) {
            request.setForm(form.toXml());
        }
        request.addNewCommand().setId(commandId);
        if (input != null) {
            if (input.getDomNode().getNodeName().equals("Input")) {
                request.setInput(input);
            } else {
                request.addNewInput().set(input);
            }
        }
        return request;
    }

    private static Id getClassIdByContext(final GroupModel group) {
        if (group.getContext() instanceof IContext.TableSelect) {
            final IContext.TableSelect context =
                    (IContext.TableSelect) group.getContext();
            return context.explorerItemDef.getModelDefinitionClassId();
        } else {
            return group.getSelectorPresentationDef().getOwnerClassId();
        }
    }

    private static void writeGroup(final GroupModel group, final EntityObjectsSelection selection, final Request request) {

        final FilterModel currentFilter = group.getCurrentFilter();
        final org.radixware.schemas.xscml.Sqml condition = group.getCondition();
        final RadSortingDef currentSorting = group.getCurrentSorting();

        org.radixware.schemas.eas.Filter filter = null;
        final RadSelectorPresentationDef presentation = group.getSelectorPresentationDef();
        final Id classId = getClassIdByContext(group);
        if (request instanceof SelectRq) {
            final SelectRq selectRequest = ((SelectRq) request);
            if (condition != null) {
                selectRequest.setCondition(condition);
            }
            if (currentFilter != null) {
                filter = selectRequest.addNewFilter();
            }
            if (currentSorting != null) {
                final org.radixware.schemas.eas.Sorting sorting = selectRequest.addNewSorting();
                if (currentSorting.isUserDefined()) {
                    final org.radixware.schemas.eas.Sorting.AdditionalSortingColumns columns = sorting.addNewAdditionalSortingColumns();
                    org.radixware.schemas.eas.Sorting.AdditionalSortingColumns.Item column;
                    for (RadSortingDef.SortingItem sortingItem : currentSorting.getSortingColumns()) {
                        column = columns.addNewItem();
                        column.setId(sortingItem.propId);
                        if (sortingItem.sortDesc) {
                            column.setOrder(EOrder.DESC);
                        }
                    }
                } else {
                    sorting.setId(currentSorting.getId());
                }
            }
            selectRequest.setContext(((IContext.Group) group.getContext()).toXml());
            selectRequest.addNewPresentation().setId(presentation.getId());

            if (classId != null) {
                selectRequest.addNewClass1().setId(classId);
            }            
            if (!group.getActiveProperties().isEmpty()) {
                group.writePropertiesToXml(selectRequest.addNewGroupProperties());                
            }
        } else if (request instanceof ObjectOrGroupRequest) {
            final ObjectOrGroupRequest groupRequest = ((ObjectOrGroupRequest) request);
            if (condition != null) {
                groupRequest.setCondition(condition);
            }
            if (currentFilter != null) {
                filter = groupRequest.addNewFilter();
            }
            groupRequest.setContext(((IContext.Group) group.getContext()).toXml());
            groupRequest.addNewPresentation().setId(group.getSelectorPresentationDef().getId());
            if (classId != null) {
                groupRequest.addNewClass1().setId(classId);
            }
            if (!group.getActiveProperties().isEmpty()) {
                group.writePropertiesToXml(groupRequest.addNewGroupProperties());
            }
            if (selection!=null){
                selection.writeToXml(groupRequest.addNewSelectedObjects());
            }
        } else if (request == null) {
            throw new NullPointerException();
        } else {
            throw new IllegalArgumentException(request.getClass().getSimpleName() + " is not suitable request");
        }

        if (filter != null) {
            if (currentFilter.isUserDefined()) {
                if (currentFilter.hasAncestor()) {
                    filter.setId(((RadUserFilter) currentFilter.getFilterDef()).baseFilterId);
                }
                filter.setAdditionalCondition(currentFilter.getUserCondition());
            } else {
                filter.setId(currentFilter.getId());
                if (currentFilter.getFilterDef().getLastUpdateTime() != null) {
                    filter.setLastUpdateTime(currentFilter.getFilterDef().getLastUpdateTime());
                }
            }

            final Collection<org.radixware.kernel.common.client.models.items.properties.Property> properties = currentFilter.getActiveProperties();
            if (properties != null && !properties.isEmpty()) {
                final org.radixware.schemas.eas.Filter.Parameters parameters = filter.addNewParameters();
                for (org.radixware.kernel.common.client.models.items.properties.Property property : properties) {
                    if (!property.isLocal()){
                        writeFilterParemeter(parameters.addNewItem(), property);
                    }
                }
            }
        }
    }

    private static void writeGroupProperties(final org.radixware.schemas.eas.PropertyList propertyList, final Collection<org.radixware.kernel.common.client.models.items.properties.Property> properties) {
        for (org.radixware.kernel.common.client.models.items.properties.Property property : properties) {
            if (!property.isLocal()){
                property.writeValue2Xml(propertyList.addNewItem());
            }
        }
    }

    private static void writeFilterParemeter(final org.radixware.schemas.eas.Filter.Parameters.Item parameter,
            final org.radixware.kernel.common.client.models.items.properties.Property property) {
        parameter.setId(property.getId());
        final java.lang.Object value = property.getValueObject();
        if (property instanceof PropertyInt) {
            if (value instanceof IKernelIntEnum) {
                parameter.setInt(((IKernelIntEnum) value).getValue());
            } else if (value instanceof Long) {
                parameter.setInt(((Long) value).longValue());
            } else {
                parameter.setNilInt();
            }
        } else if (property instanceof PropertyNum) {
            if (value != null) {
                parameter.setNum((BigDecimal) value);
            } else {
                parameter.setNilNum();
            }
        } else if (property instanceof PropertyDateTime) {
            parameter.setDateTime((Timestamp) value);//TWRBS-4011
        } else if (property instanceof PropertyRef) {
            if (value != null) {
                final Pid pid = ((Reference) value).getPid();
                if (pid != null) {
                    final org.radixware.schemas.eas.Filter.Parameters.Item.PID xPid;
                    xPid = parameter.addNewPID();
                    xPid.setEntityId(pid.getTableId());
                    xPid.setStringValue(XmlUtils.getSafeXmlString(pid.toString()));
                }
            } else {
                parameter.setNilPID();
            }
        } else if (value instanceof IKernelStrEnum) {
            parameter.setStr(((IKernelStrEnum) value).getValue());
        } else if (value instanceof IKernelCharEnum) {
            parameter.setStr(((IKernelCharEnum) value).getValue().toString());
        } else if (value == null) {
            parameter.setNilStr();
        } else {
            final EValType paramType = property.getDefinition().getType();
            final String valAsStr = 
                ValAsStr.toStr(value, ValueConverter.serverValType2ClientValType(paramType));
            parameter.setStr(XmlUtils.getSafeXmlString(valAsStr));
        }
    }
}
