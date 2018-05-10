/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.builder.api.IBuildEnvironment;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDetailColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsExpressionPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParentPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParentRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsServerSidePropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTablePropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ParentRefProperty;
import org.radixware.kernel.common.defs.ads.clazz.members.ParentReferenceInfo;
import org.radixware.kernel.common.defs.ads.clazz.members.ServerPresentationSupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyEditOptions;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyPresentation;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskDateTime;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskInt;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskNum;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskStr;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.SqmlDefinitionsWriter;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.schemas.sqmldef.*;


public final class SqmlDistributor {

    private final IBuildEnvironment buildEnv;
    private final BuildActionExecutor.EBuildActionType actionType;
    
    public SqmlDistributor(final IBuildEnvironment buildEnv, final BuildActionExecutor.EBuildActionType actionType){
        this.buildEnv = buildEnv;
        this.actionType = actionType;
    }
    
    public void execute(final Collection<Module> modules) {
        processTargets(modules);
    }
    
    public static void makeDefinitionsXml(final Module module, final IBuildEnvironment buildEnv, final BuildActionExecutor.EBuildActionType actionType) {
        SqmlDistributor distributor = new SqmlDistributor(buildEnv, actionType);
        distributor.execute(Collections.singleton(module));
    }
    
    public static void makeDefinitionsXml(final Module module) throws IOException{
        new ModuleProcessor(module).write();
    }
        
    private void cleanUpModules(final Collection<Module> modules) {
        final IProgressHandle handle = buildEnv.getBuildDisplayer().getProgressHandleFactory().createHandle("Cleanup SQML distribution...");
        try {

            handle.start(modules.size());


            final List<Module> processing = new ArrayList<>(modules.size());
            for (Module module : modules) {
                if (module.isReadOnly()) {
                    continue;
                }
                processing.add(module);
            }
            if (processing.isEmpty()) {
                return;
            }
            final int[] i = {0};
            final CountDownLatch waiter = new CountDownLatch(processing.size());
            final ExecutorService operationThreadPool = Executors.newFixedThreadPool(3);
            try {
                for (final Module module : processing) {
                    operationThreadPool.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                cleanUpModule(module);
                            } catch (Throwable e) {
                                Logger.getLogger(SqmlDistributor.class.getName()).log(Level.SEVERE, "Error on module SQML cleanup", e);
                            } finally {
                                waiter.countDown();
                                try {
                                    handle.progress(i[0]++);
                                } catch (Throwable e) {
                                    Logger.getLogger(SqmlDistributor.class.getName()).log(Level.FINE, "Error on module SQML cleanup", e);
                                }
                            }

                        }
                    });
                }
                try {
                    waiter.await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            } finally {
                operationThreadPool.shutdown();
            }
        } finally {
            handle.finish();
        }
    }
    
    private void cleanUpModule(final Module module) {
        buildEnv.getMutex().readAccess(new Runnable() {
            @Override
            public void run() {
                File moduleDir = module.getDirectory();
                if (moduleDir != null) {
                    File indexFile = new File(moduleDir, FileUtils.SQML_DEFINITIONS_XML_FILE_NAME);
                    FileUtils.deleteFile(indexFile);
                }
            }
        });
    }
    
    private boolean wasCancelled() {
        if (buildEnv.getFlowLogger().getCancellable() != null) {
            return buildEnv.getFlowLogger().getCancellable().wasCancelled();
        } else {
            return false;
        }
    }    
    
    private void updateModules(final Collection<Module> modules) {
        final IProgressHandle handle = buildEnv.getBuildDisplayer().getProgressHandleFactory().createHandle("Distribute " + modules.size() + " SQML modules...", new Cancellable() {
            @Override
            public boolean cancel() {
                if (buildEnv.getFlowLogger().getCancellable() != null) {
                    return buildEnv.getFlowLogger().getCancellable().cancel();
                } else {
                    return false;
                }
            }

            @Override
            public boolean wasCancelled() {
                return SqmlDistributor.this.wasCancelled();
            }
        });
        final long time = System.currentTimeMillis();
        try {         
            handle.start(modules.size());

            List<Module> processing = new ArrayList<>(modules.size());
            for (final Module module : modules) {
                if (wasCancelled()) {
                    return;
                }
                if (module.isReadOnly() || module.getDirectory() == null) {
                    continue;
                }
                processing.add(module);

            }
            if (processing.isEmpty()) {
                return;
            }
            final int[] i = {0};
            final CountDownLatch waiter = new CountDownLatch(processing.size());
            final ExecutorService operationThreadPool = Executors.newFixedThreadPool(5);

            for (final Module module : processing) {
                operationThreadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (wasCancelled()) {
                                return;
                            }
                            final SqmlDistributor.ModuleProcessor processor = new SqmlDistributor.ModuleProcessor(module);
                            buildEnv.getMutex().readAccess(new ProcessModuleTask(processor));
                        } catch(Throwable ex){
                            Logger.getLogger(SqmlDistributor.class.getName()).log(Level.FINE, "Processing module failure", ex);
                        }finally {
                            waiter.countDown();
                            try {
                                handle.progress(i[0]++);
                            } catch (Throwable e) {
                                Logger.getLogger(SqmlDistributor.class.getName()).log(Level.FINE, "Progress handle error", e);
                            }
                        }
                    }
                });
            }
            try {
                waiter.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            } finally {
                operationThreadPool.shutdown();
            }
        } finally {
            handle.finish();
            Logger.getLogger(getClass().getName()).log(Level.FINE, "SQML modules distribution time: {0}s",Long.valueOf((System.currentTimeMillis()-time)/1000));
        }        
    }
    
    private class ProcessModuleTask implements Runnable{
        
        private final ModuleProcessor processor;
        
        public ProcessModuleTask(final ModuleProcessor processor){
            this.processor = processor;
        }

        @Override
        public void run() {
            try {
                final String message = processor.write();
                if (message!=null && !message.isEmpty()){
                    buildEnv.getFlowLogger().message(message);
                }
            } catch (IOException ex) {
                buildEnv.getBuildProblemHandler().accept(RadixProblem.Factory.newError(processor.getModule(), "Can not create SQML definitions index file: " + ex.getMessage()));
            }
        }                
    }
        
    private static class ModuleProcessor extends SqmlDefinitionsWriter{

        public ModuleProcessor(final Module module) {
            super(module);
        }
        
        @Override
        protected boolean writeRadixObject(final RadixObject radixObject, final SqmlModule  xModule){
            if (radixObject instanceof AdsEnumDef){
                writeEnumDef((AdsEnumDef)radixObject, xModule.addNewEnumDef());
                return true;
            }else if (radixObject instanceof AdsDomainDef){
                writeDomainDef((AdsDomainDef)radixObject, xModule.addNewDomainDef());
                return true;
            }else if (radixObject instanceof AdsEntityObjectClassDef){
                writeClassDef((AdsEntityObjectClassDef)radixObject, xModule.addNewClassDef());
                return true;
            }
            return super.writeRadixObject(radixObject, xModule);
        }
        
        @Override
        protected boolean isSqmlDefinition(final RadixObject radixObject) {
            if (radixObject instanceof AdsDefinition) {                        
                final AdsDefinition def = ((AdsDefinition) radixObject);
                final EDefType defType = def.getDefinitionType();
                switch (defType) {
                    case CLASS:
                        return radixObject instanceof AdsEntityObjectClassDef;
                    case ENUMERATION:
                    case DOMAIN:
                        return def.isPublished();
                    default:
                        return false;
                }
            } else {
                return super.isSqmlDefinition(radixObject);
            }            
        }
                        
        @Override
        protected boolean isSqmlDefinitionsContainer(final RadixObject radixObject) {
            return radixObject instanceof AdsDomainDef ? false : super.isSqmlDefinitionsContainer(radixObject);
        }
        
        private void writeClassDef(final AdsEntityObjectClassDef classDef, final SqmlClassDef xClassDef){
            writeDefinition(classDef, xClassDef);
            final SqmlClassDef.Properties xProperties =  xClassDef.addNewProperties();
            final List<AdsPropertyDef> properties = classDef.getProperties().get(EScope.ALL);
            AdsPropertyDef finalProperty;           
            for (AdsPropertyDef property : properties) {
                if ( property.isPublished() && ((property instanceof AdsTablePropertyDef) || (property instanceof AdsExpressionPropertyDef)) ){
                    finalProperty = property;
                    //parent properties temporary restricted
                    if (finalProperty instanceof AdsParentPropertyDef) {
                        continue;
                    }
                    writeClassPropertyDef((AdsServerSidePropertyDef)property, xProperties.addNewProperty(), classDef.getId(), classDef.getQualifiedName());
                }
            }
            if ((classDef instanceof AdsApplicationClassDef) && ((AdsApplicationClassDef) classDef).isDetailPropsAllowed()){
                xClassDef.setHasDetails(true);
            }
            
            final List<AdsEntityObjectClassDef.DetailReferenceInfo> detailRefInfoList = classDef.getAllowedDetailRefs();
            final List<Id> detailTablesIds = new LinkedList<>();
            final List<Id> detailReferenceIds = new LinkedList<>();
            DdsReferenceDef reference;
            for (AdsEntityObjectClassDef.DetailReferenceInfo detailRefInfo : detailRefInfoList) {
               reference = detailRefInfo.findReference();
               if (reference != null) {
                   detailReferenceIds.add(reference.getId());
                   detailTablesIds.add(reference.getChildTableId());
               }
            }
            
            if (!detailTablesIds.isEmpty()){
                xClassDef.setAllowedDetails(detailTablesIds);
            }
            if (!detailReferenceIds.isEmpty()){
                xClassDef.setAllowedDetailRefs(detailReferenceIds);
            }
            final Id tableId = classDef.findTable(classDef).getId();
            if (!Id.Factory.changePrefix(classDef.getId(), EDefinitionIdPrefix.DDS_TABLE).equals(tableId)){
                xClassDef.setTableId(tableId);
            }
            if (isContextlessSelectorPresentationDefined(classDef)){
                final SqmlClassDef.ContextlessSelectorPresentations xPresentations = 
                    xClassDef.addNewContextlessSelectorPresentations();
                final List<AdsSelectorPresentationDef> selectorPresentations = 
                    new ArrayList<>(classDef.getPresentations().getSelectorPresentations().get(EScope.LOCAL_AND_OVERWRITE));
                Collections.sort(selectorPresentations, new Comparator<AdsSelectorPresentationDef>(){
                    @Override
                    public int compare(AdsSelectorPresentationDef pres1, AdsSelectorPresentationDef pres2) {
                        return pres1.getId().toString().compareTo(pres2.getId().toString());
                    }
                });
                for (AdsSelectorPresentationDef selectorPresentation : selectorPresentations) {
                    if (!selectorPresentation.getRestrictions().isDenied(ERestriction.CONTEXTLESS_USAGE)) {
                        writeDefinition(selectorPresentation, xPresentations.addNewPresentation());
                    }
                }
            }            
            final Id titleId = classDef.getTitleId();
            if (titleId!=null){                
                xClassDef.setTitleId(titleId);
            }
        }
        
        private static boolean isContextlessSelectorPresentationDefined(final AdsEntityObjectClassDef classDef){
            final List<AdsSelectorPresentationDef> selectorPresentations =
                        classDef.getPresentations().getSelectorPresentations().get(EScope.LOCAL_AND_OVERWRITE);
            for (AdsSelectorPresentationDef selectorPresentation : selectorPresentations) {
                if (!selectorPresentation.getRestrictions().isDenied(ERestriction.CONTEXTLESS_USAGE)){
                    return true;
                }
            }
            return false;
        }
        
        private void writeClassPropertyDef(final AdsServerSidePropertyDef propertyDef, final SqmlClassPropertyDef xProperty, final Id ownerClassId, final String ownerClassQualifiedName){
            writeDefinition(propertyDef, xProperty);
            final String qualifiedName = propertyDef.getQualifiedName();            
            if (!qualifiedName.equals(ownerClassQualifiedName+":"+propertyDef.getName())){
                xProperty.setQualifiedName(qualifiedName);
            }
            final AdsTypeDeclaration typeDecl = propertyDef.getValue().getType();
            xProperty.setValType(typeDecl.getTypeId());
            final AdsType type = typeDecl.resolve(propertyDef).get();
            final ServerPresentationSupport presentationSupport = propertyDef.getPresentationSupport();
            final PropertyPresentation presentation = presentationSupport==null ? null : presentationSupport.getPresentation();
            final PropertyEditOptions editOptions = presentation==null ? null : presentation.getEditOptions();            
            if (type instanceof AdsEnumType) {
                xProperty.setEnumId( ((AdsEnumType)type).getSource().getId() );
            } else {
                final org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMask mask = editOptions==null ? null : editOptions.getEditMask();                    
                if (mask!=null){
                    writeEditMask(mask, xProperty.addNewEditMask());
                }
            }
            if (editOptions!=null){                
                xProperty.setNotNull(editOptions.isNotNull());
            }
            if (propertyDef instanceof ParentRefProperty){
                final ParentReferenceInfo referenceInfo = ((ParentRefProperty) propertyDef).getParentReferenceInfo();
                final DdsReferenceDef referenceDef = referenceInfo == null ? null : referenceInfo.findParentReference();
                final DdsIndexDef indexDef = referenceDef == null ? null : referenceDef.findParentIndex();
                if (indexDef!=null){
                    writeTableIndex(indexDef, xProperty.addNewParentIndex(), false);
                }
                
                if (referenceDef != null) {
                    final DdsReferenceDef.ColumnsInfoItems columnsInfo = referenceDef.getColumnsInfo();
                    if (columnsInfo!=null && columnsInfo.size()>0){
                        final SqmlClassPropertyDef.ChildColumns childColumns = xProperty.addNewChildColumns();
                        SqmlClassPropertyDef.ChildColumns.Column childColumn;
                        for (DdsReferenceDef.ColumnsInfoItem item : columnsInfo) {
                            childColumn = childColumns.addNewColumn();
                            childColumn.setTableId(item.getChildColumn().getOwnerTable().getId());
                            childColumn.setId(item.getChildColumnId());
                        }
                    }
                }
            }
            if (propertyDef instanceof AdsDetailColumnPropertyDef) {
                xProperty.setDetailColumnId(((AdsDetailColumnPropertyDef) propertyDef).getColumnInfo().getColumnId());
            }
            AdsPropertyDef finalProperty = propertyDef;
            while (finalProperty instanceof AdsParentPropertyDef) {
                finalProperty = ((AdsParentPropertyDef) finalProperty).getParentInfo().findOriginalProperty();
            }            
            if (propertyDef instanceof AdsTablePropertyDef) {
                if (finalProperty instanceof AdsParentRefPropertyDef || finalProperty instanceof AdsUserPropertyDef){
                    xProperty.setOwnerTableId(ownerClassId);
                }else{
                    final DdsTableDef ownerTable = ((AdsTablePropertyDef) propertyDef).findTable();
                    final Id ownerTableId = ownerTable.getId();
                    if (!Id.Factory.changePrefix(ownerClassId, EDefinitionIdPrefix.DDS_TABLE).equals(ownerTableId)){
                        xProperty.setOwnerTableId(ownerTableId);
                    }
                }
            }else if (propertyDef instanceof AdsExpressionPropertyDef){
                xProperty.setOwnerTableId(ownerClassId);
            }else{
                xProperty.setInnateColumn(false);
            }
            
            if (finalProperty instanceof AdsParentRefPropertyDef) {
                final Id referencedTableId = 
                        ((AdsParentRefPropertyDef) finalProperty).findReferencedEntityClass().findTable(propertyDef).getId();
                xProperty.setReferencedTableId(referencedTableId);
            }            
                                    
            if (presentation!=null){
                final Id titleId = presentation.getTitleId();
                if (titleId!=null){
                    final AdsDefinition def = presentation.findTitleOwner();
                    if (def!=null){
                        xProperty.setTitleId(titleId);
                        if (!def.getId().equals(propertyDef.getId()) && !def.getId().equals(ownerClassId)){
                            xProperty.setTitleOwnerId(def.getId());
                        }
                    }
                }
            }
            if (!ownerClassId.equals(propertyDef.getOwnerClass().getId())){
                xProperty.setOwnerClassId(propertyDef.getOwnerClass().getId());
            }
        }
        
        private Id getTitleOwnerId(final PropertyPresentation presentation){
            final AdsClassDef clazz = presentation.getOwnerProperty().getOwnerClass();
            if (!clazz.isTopLevelDefinition()) {
                AdsDefinition def = clazz.findTopLevelDef();
                return def == null ? null : def.getId();
            } else {
                return clazz.getId();
            }
        }
        
        private Collection<PropertyPresentation> collectOverwrittenPresentations(final AdsPropertyDef propertyDef) {
            final ArrayList<PropertyPresentation> ppss = new ArrayList<>();
            AdsPropertyDef ovr = propertyDef.getHierarchy().findOverwritten().get();
            while (ovr != null) {
                if (ovr instanceof IAdsPresentableProperty) {
                    final ServerPresentationSupport support = ((IAdsPresentableProperty) ovr).getPresentationSupport();
                    if (support != null) {
                        ppss.add(support.getPresentation());
                    }
                }
                ovr = ovr.getHierarchy().findOverwritten().get();
            }
            return ppss;
        }
        
        private void writeEditMask(final org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMask mask, final EditMask xMask){
            if (mask instanceof EditMaskInt){                
                final Long dbMaxLen = ((EditMaskInt)mask).getDbMaxValue();
                if (dbMaxLen!=null){
                    xMask.addNewInt().setDbMaxValue(dbMaxLen);
                }
            }else if (mask instanceof EditMaskNum){
                final EditMask.Num xNum = xMask.addNewNum();
                final EditMaskNum maskNum = (EditMaskNum)mask;
                if (maskNum.getDbMaxValue()!=null){
                    xNum.setDbMaxValue(maskNum.getDbMaxValue());
                }
                if (maskNum.getPrecision()!=null){
                    xNum.setPrecision(maskNum.getPrecision());
                }                
            }else if (mask instanceof EditMaskStr){
                final Integer dbMaxLen = ((EditMaskStr)mask).getDbMaxLen();
                if (dbMaxLen!=null){
                    final EditMask.Str xStr = xMask.addNewStr();                 
                    xStr.setDbMaxLen(dbMaxLen); 
                }
            }else if (mask instanceof EditMaskTimeInterval){
                final EditMask.TimeInterval xTimeInterval = xMask.addNewTimeInterval();
                final String inputMask = ((EditMaskTimeInterval)mask).getMask();
                if (inputMask!=null){
                    xTimeInterval.setMask(inputMask);
                }
                xTimeInterval.setScale(((EditMaskTimeInterval)mask).getScale().name()); 
            }else if (mask instanceof EditMaskDateTime){
                final String inputMask = ((EditMaskDateTime)mask).getMask();
                if (inputMask!=null){
                    xMask.addNewDateTime().setMask(inputMask);
                }
            }
        }
        
        private void writeDomainDef(final AdsDomainDef domainDef, final SqmlDomainDef xDomainDef){
            writeDefinition(domainDef, xDomainDef);
            for (AdsDomainDef childDomain: domainDef.getChildDomains()){
                writeDomainDef(childDomain, xDomainDef.addNewSubDomain());
            }
            if (domainDef.getTitleId()!=null){
                xDomainDef.setTitleId(domainDef.getTitleId());
            }
        }
        
        private void writeEnumDef(final AdsEnumDef enumDef, final SqmlEnumDef xEnumDef){
            writeDefinition(enumDef, xEnumDef);
            xEnumDef.setValType(enumDef.getItemType());
            final List<AdsEnumItemDef> items = enumDef.getItems().get(EScope.LOCAL_AND_OVERWRITE);
            for (AdsEnumItemDef item: items){
                if (item.isPublished()){
                    writeEnumItem(item, xEnumDef.addNewItem());
                }
            }
            final Id titleId = enumDef.getTitleId();
            if (titleId!=null){
                xEnumDef.setTitleId(titleId);
            }
        }
        
        private void writeEnumItem(final AdsEnumItemDef item, final SqmlEnumItemDef xItem){
            writeDefinition(item, xItem);            
            xItem.setValAsStr(item.getValue().toString());
            final Id titleId = item.getTitleId();
            if (titleId!=null){
                xItem.setTitleId(titleId);
            }
        }
    }
    
    
    private void processTargets(final Collection<Module> modules) {        
        if (actionType == BuildActionExecutor.EBuildActionType.CLEAN || actionType == BuildActionExecutor.EBuildActionType.CLEAN_AND_BUILD) {
            cleanUpModules(modules);
            if (actionType == BuildActionExecutor.EBuildActionType.CLEAN) {
                return;
            }
        }
        updateModules(modules);
    }
    
    public Collection<Module> collectSqmlModules(final RadixObject[] targets){
        final Collection<Module> modules = new LinkedList<>();
        final IProgressHandle handle = buildEnv.getBuildDisplayer().getProgressHandleFactory().createHandle("Looking for sqml modules...");
        handle.start();
        try{
            for (RadixObject target : targets) {
                if (target instanceof AdsModule || target instanceof DdsModule) {
                    modules.add((Module)target);
                } else if (target instanceof AdsSegment || target instanceof DdsSegment) {
                    final List<Module> segmentModules = collectSegmentModules((Segment)target);
                    if (segmentModules==null){
                        return null;
                    }else{
                        modules.addAll(segmentModules);
                    }
                }else if (target instanceof Layer) {
                    final List<Module> layerModules = collectLayerModules((Layer)target);
                    if (layerModules==null){
                        return null;
                    }else{
                        modules.addAll(layerModules);
                    }                    
                } else if (target instanceof Branch) {
                    List<Layer> layers = ((Branch) target).getLayers().getInOrder();
                    List<Module> layerModules;
                    for (Layer l : layers) {
                        layerModules = collectLayerModules(l);
                        if (layerModules==null){
                            return null;
                        }else{
                            modules.addAll(layerModules);
                        }
                    }
                }
                if (wasCancelled()) {
                    return null;
                }                
            }
        }finally{
            handle.finish();
        }
        return modules;
    }
    
    private List<Module> collectLayerModules(final Layer layer){
        final List<Module> modules = new LinkedList<>();
        List<Module> segmentModules = collectSegmentModules(layer.getDds());
        if (segmentModules==null){
            return null;
        }else{
            modules.addAll(segmentModules);
        }                    
        segmentModules = collectSegmentModules(layer.getAds());
        if (segmentModules==null){
            return null;
        }else{
            modules.addAll(segmentModules);
        }        
        return modules;
    }
    
    private List<Module> collectSegmentModules(final Segment segment){
        final List<Module> modules = new LinkedList<>();
        for (Object  module: segment.getModules()) {
            if (module instanceof Module){
                modules.add((Module)module);
            }
            if (wasCancelled()) {
                return null;
            }
        }
        return modules;
    }

}