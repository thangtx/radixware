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

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.radixware.kernel.server.dbq.SelectQuery.Result;
import org.radixware.schemas.eas.ObjectList.Rows.Item;
import org.radixware.schemas.eas.Presentation;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.ESelectorRowStyle;

import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.server.dbq.SelectQuery;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.dbq.SelectParentQuery;
import org.radixware.kernel.server.exceptions.FilterParamNotDefinedException;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.kernel.server.types.EntityPropVals;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.PresentationEntityAdapter;
import org.radixware.kernel.server.types.Restrictions;
import org.radixware.kernel.server.types.presctx.PresentationContext;
import org.radixware.schemas.eas.Actions;
import org.radixware.schemas.eas.Definition;
import org.radixware.schemas.eas.EditorPages;
import org.radixware.schemas.eas.ExceptionEnum;
import org.radixware.schemas.eas.ObjectList;
import org.radixware.schemas.eas.PresentableObject;
import org.radixware.schemas.eas.PropertyList;
import org.radixware.schemas.eas.SelectMess;
import org.radixware.schemas.eas.SelectRq;
import org.radixware.schemas.eas.SelectRs;
import org.radixware.schemas.easWsdl.SelectDocument;

public class SelectRequest extends SessionRequest {

    public SelectRequest(final ExplorerAccessService presenter) {
        super(presenter);
    }
    
    private SelectDocument process(final SelectRq rqParams) throws ServiceProcessFault, InterruptedException {
        final RadClassDef classDef;
        final Definition classXml = rqParams.getClass1();
        if (classXml != null) {
            final Id classId = classXml.getId();
            try {
                classDef = getArte().getDefManager().getClassDef(classId);
            } catch (DefinitionNotFoundError e) {
                throw EasFaults.newDefWithIdNotFoundFault("Class", rqParams.getDomNode().getNodeName(), classId);
            }
        } else {
            throw EasFaults.newParamRequiedFault("Class", rqParams.getDomNode().getNodeName());
        }
        final Id grpClassId = classDef.getId();
        final RadClassDef grpClassDef = getArte().getDefManager().getClassDef(grpClassId);
        final PresentationOptions presOptions = getPresentationOptions(rqParams, grpClassDef, true, true, rqParams.getPresentation());
        getArte().switchToReadonlyTransaction(); // to prevent commiting of childEntity changes before saving in editor        
        long startIdx = rqParams.isSetStartIndex() ? rqParams.getStartIndex() : 1;
        int count = rqParams.isSetCount() ? rqParams.getCount() : 10;
        final boolean isRowsDataRequested = count > 0;//it may be request just for read common selector addons and disabled actions
        final Group group = getGroup(rqParams, grpClassDef, presOptions, !isRowsDataRequested, isRowsDataRequested);
        final PresentationContext presCtx = getPresentationContext(getArte(), presOptions.context, group.entityGroup);
        final SelectDocument resSel = SelectDocument.Factory.newInstance();
        final SelectRs rsStruct = resSel.addNewSelect().addNewSelectRs();
        final ObjectList dsXml = rsStruct.addNewDataSet();
        final ObjectList.Rows dsRowsXml = dsXml.addNewRows();
        final Id presentationId = group.entityGroup.getPresentation().getId();
        final EntityGroup.EntityFilter filter = group.entityGroup.getEntityFilter();
        if (isRowsDataRequested) {
            SelectQuery.Result rs = select(group, startIdx, count, presOptions);
            int skippedCount = 0;
            int filteredCount = 0;
            long itemIdx = 0;
            do {
                if (!rs.rows.isEmpty()) {
                    skippedCount = 0;
                    filteredCount = 0;
                    final Class<?> selectionClass = getArte().getDefManager().getClass(group.entityGroup.getSelectionClassId());
                    for (EntityPropVals propVals : rs.rows) {
                        Pid pid = null;
                        Item rowXml = null;
                        try {
                            pid = new Pid(getArte(), group.entityGroup.getDdsMeta(), propVals.asMap());
                            final Entity curEntity = getArte().getEntityObject(pid, propVals, false);
                            final PresentationEntityAdapter curPresEntAdapter = getArte().getPresentationAdapter(curEntity);
                            curPresEntAdapter.setPresentationContext(presCtx);
                            if (!selectionClass.isAssignableFrom(curEntity.getClass())) {
                                throw new ServiceProcessServerFault(ExceptionEnum.APPLICATION_ERROR.toString(), "Selected object is not instance of selection class #" + group.entityGroup.getSelectionClassId(), null, null);
                            }
                            //RADIX-2028: for modal selectors only editor presentations registered in selector presentation can be used
                            //final RadEditorPresentationDef curPres = getActualEditorPresentation(curEntity, (request instanceof SelectParentMess || request instanceof SelectFormParentMess) ? curEntity.getPresentationMeta().getEditorPresentations() : context.selectorPresentation.getEditorPresentations());
                            final RadEditorPresentationDef curPres = getActualEditorPresentation(curPresEntAdapter, presOptions.selectorPresentation.getEditorPresentations(), false);
                            if (curPres == null) {//no rights
                                skippedCount++;
                                continue; //skip this record
                            }
                            if (filter != null && filter.omitEntity(curEntity)) {
                                filteredCount++;
                                continue; //skip this record                                
                            }
                            //end of RADIX-2028 FIX
                            itemIdx++;
                            final boolean writeEntireObject = (rqParams.isSetEntireObjectIndexes() && rqParams.getEntireObjectIndexes().contains(Long.valueOf(itemIdx)))
                                    || (rqParams.isSetEntireObjectPids() && rqParams.getEntireObjectPids().contains(pid.toString()));

                            rowXml = dsRowsXml.addNewItem();                            
                            final PresentableObject  objectXml = PresentableObject.Factory.newInstance();
                            objectXml.setPID(pid.toString());
                            final Presentation presXml = objectXml.addNewPresentation();
                            presXml.setId(curPres.getId());
                            presXml.setClassId(curPres.getClassPresentation().getClassId());

                            objectXml.setTitle(curEntity.calcTitle(curPres.getObjectTitleFormat()));
                            objectXml.setClassId(curEntity.getRadMeta() != null ? curEntity.getRadMeta().getId() : RadClassDef.getEntityClassIdByTableId(curEntity.getDdsMeta().getId()));

                            final ESelectorRowStyle rowStyle;
                            final ColorScheme colorScheme = presenter.getColorScheme(getArte(), grpClassDef, presentationId);
                            if (colorScheme == null) {
                                rowStyle = curPresEntAdapter.calcSelectorRowStyle();
                            } else {
                                rowStyle = colorScheme.apply(curEntity);
                            }

                            if (rowStyle != null) {
                                objectXml.setRowStyle(rowStyle);
                            }

                            final Actions disActXml = objectXml.addNewDisabledActions();
                            if (writeDisbaledObjActions(disActXml, curPresEntAdapter, curPres, presOptions, group.entityGroup) == 0) {
                                objectXml.unsetDisabledActions();
                            }

                            final PropertyList propsXml = objectXml.addNewProperties();
                            final Collection<RadPropDef> columns = presOptions.selectorPresentation.getUsedPropDefs(curEntity.getPresentationMeta());
                            if (writeEntireObject) {
                                for (RadPropDef propDef : curPres.getUsedPropDefs(curEntity.getPresentationMeta())) {                                    
                                    addPropXml(propsXml, curPresEntAdapter, presOptions.selectorPresentation, presOptions.context, propDef, columns.contains(propDef));
                                }

                                final Restrictions restr = curPres.getTotalRestrictions(curEntity);
                                final Restrictions additionalRestrictions = curPresEntAdapter.getAdditionalRestrictions(curPres);
                                final Restrictions totalRestrictions;
                                if (additionalRestrictions==Restrictions.ZERO){
                                    totalRestrictions = restr;
                                }else{
                                    totalRestrictions = Restrictions.Factory.sum(restr, additionalRestrictions);
                                }
                                final EditorPages enadledEditorPages = objectXml.addNewEnabledEditorPages();
                                if (totalRestrictions.getIsAccessRestricted() || totalRestrictions.getIsViewRestricted()) {
                                    enadledEditorPages.setAll(false);
                                } else if (!totalRestrictions.getIsAllEditPagesRestricted()) {
                                    enadledEditorPages.setAll(true);
                                } else {
                                    enadledEditorPages.setAll(false);
                                    final Collection<Id> allowedEditPages = totalRestrictions.getAllowedEditPages();
                                    for (Id id : allowedEditPages) {
                                        final EditorPages.Item item = enadledEditorPages.addNewItem();
                                        item.setId(id);
                                    }
                                }                                
                                writeAccessibleExplorerItems(curEntity, curPres, additionalRestrictions, objectXml.addNewAccessibleExplorerItems());
                            } else {
                                for (RadPropDef col : columns) {
                                    addPropXml(propsXml, curPresEntAdapter, presOptions.selectorPresentation, presOptions.context, col, true);
                                }
                            }

                            rowXml.setObject(objectXml);
                        } catch (Throwable exception) {//RADIX-1641
                            if (rowXml == null) {
                                rowXml = dsRowsXml.addNewItem();
                            }
                            final Item.Exception exceptionXml = rowXml.addNewException();
                            if (pid != null) {
                                exceptionXml.setPID(pid.toString());
                            }
                            exceptionXml.setMessage(exception.getMessage());
                            exceptionXml.setClass1(exception.getClass().getName());
                            exceptionXml.setStack(ExceptionTextFormatter.exceptionStackToString(exception));
                        }
                    }
                }
                if ((skippedCount > 0 || filteredCount > 0) && rs.hasMore && !getArte().needBreak()) {
                    if (skippedCount>0){
                        getArte().getTrace().put(Messages.MLS_ID_EAS_SKIPPED_BY_ACS_RECORDS, Collections.singletonList(String.valueOf(skippedCount)));
                    }
                    if (filteredCount>0){
                        getArte().getTrace().put(Messages.MLS_ID_EAS_FILTERED_RECORDS, Collections.singletonList(String.valueOf(filteredCount)));
                    }
                    startIdx += count;
                    count = skippedCount + filteredCount;
                    rs = select(group, startIdx, count, presOptions);
                } else {
                    break;
                }
            } while (true);
            rsStruct.setMore(rs.hasMore);
        } else {
            rsStruct.setMore(true);
        }
        if (group.filter != null && (!rqParams.isSetFilter() || !group.filter.getId().equals(rqParams.getFilter().getId()))) {
            rsStruct.addNewFilter().setId(group.filter.getId());
        }
        if (group.sorting != null && (!rqParams.isSetSorting() || !group.sorting.getId().equals(rqParams.getSorting().getId()))) {
            rsStruct.addNewSorting().setId(group.sorting.getId());
        }
        if (rqParams.isSetWithSelectorAddons() && rqParams.getWithSelectorAddons()) {
            final CommonSelectorFilters commonFilters = presenter.getCommonSelectorFilters();
            try {
                final Map<Id, CommonSelectorFilter> filtersMap = commonFilters.get(getArte(), grpClassDef, presentationId);
                final org.radixware.schemas.eas.CommonFilters rsFilters = rsStruct.addNewCommonFilters();
                for (Map.Entry<Id, CommonSelectorFilter> entry : filtersMap.entrySet()) {
                    entry.getValue().addToXml(rsFilters.addNewItem());
                }
                final ColorScheme colorSchemes = presenter.getColorScheme(getArte(), grpClassDef, presentationId);
                if (colorSchemes != null) {
                    final org.radixware.schemas.eas.ColorScheme rsColorScheme = rsStruct.addNewColorScheme();
                    colorSchemes.addToXml(rsColorScheme);
                }
            } catch (SQLException exception) {
                getArte().getTrace().put(
                        Messages.MLS_ID_EAS_UNABLE_TO_LOAD_COMMON_FILTERS,
                        new ArrStr(grpClassDef.getEntityId().toString(),
                                presentationId.toString(),
                                ExceptionTextFormatter.exceptionStackToString(exception)));
            }
        }
        if (rqParams.isSetWithInstantiatableClasses() && rqParams.getWithInstantiatableClasses()) {
            final org.radixware.schemas.eas.InstantiatableClasses instantiatableClasses
                    = rsStruct.addNewInstantiatableClasses();
            if (presOptions.context != null
                    && presOptions.context.getCreationEditorPresentationIds() != null
                    && !presOptions.context.getCreationEditorPresentationIds().isEmpty()) {
                InstantiatableClassesCollector.collectInstantiatableClasses(classDef.getPresentation(), group.entityGroup, presOptions, instantiatableClasses);
            }
        }
        try {
            final Actions disActXml = rsStruct.addNewDisabledActions();
            if (writeDisbaledGrpActions(disActXml, group.entityGroup, presOptions) == 0) {
                rsStruct.unsetDisabledActions();
            }
        } catch (Throwable e) {
            throw EasFaults.exception2Fault(getArte(), e, "Can't check group command is disabled");
        }
        return resSel;
    }

    @Override
    public final XmlObject process(final XmlObject request) throws ServiceProcessFault, InterruptedException {        
        SelectDocument document = null;
        try{
            document = process(getRqParams(request));
        }finally{
            postProcess(request, document==null ? null : document.getSelect().getSelectRs());
        }
        return document;
    }

    @Override
    public void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {
        super.prepare(rqXml);
        prepare(getRqParams(rqXml));
    }

    private static SelectRq getRqParams(final XmlObject request) {
        return ((SelectMess) request).getSelectRq();
    }

    private Result select(final Group group, final long startIdx, final int count, final PresentationOptions context) throws InterruptedException, ServiceProcessFault {
        Result rs;
        SelectQuery qry = null;
        try {
            if (context.context instanceof PropContext) {
                qry = getArte().getDefManager().getDbQueryBuilder().buildSelectParent(group.entityGroup);
                rs = ((SelectParentQuery) qry).selectParent(group.entityGroup, startIdx, count, ((PropContext) context.context).getChildInstForParentTitleRef());
            } else {
                qry = getArte().getDefManager().getDbQueryBuilder().buildSelect(group.entityGroup);
                rs = qry.select(group.entityGroup, startIdx, count, context.context != null ? context.context.getContextObjectPid() : null);
            }
        } catch (FilterParamNotDefinedException e) {
            final String preprocessedExStack = getArte().getTrace().exceptionStackToString(e);
            throw new ServiceProcessClientFault(ExceptionEnum.MISSING_FILTER_PARAM.toString(), e.getMessage(), e, preprocessedExStack);
        } catch (RuntimeException e) {
            throw EasFaults.exception2Fault(getArte(), e, "\"Select\" query raised exception");
        } finally {
            if (qry != null) {
                qry.free();
            }
        }
        return rs;
    }
}
