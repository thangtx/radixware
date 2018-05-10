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

package org.radixware.kernel.common.client.meta.sqml.defs;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ReleaseRepository;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDetailTableReference;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDomainDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEventCodeDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlFunctionDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlPackageDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.meta.RadMlStringBundleDef;
import org.radixware.kernel.common.types.Id;


final class SqmlRepository {
    
    private final IClientEnvironment environment;
    
    private final Map<Id, ISqmlEnumDef> enumById = new HashMap<>(1024);
    private final Map<Id, ISqmlDomainDef> domainById = new HashMap<>(512);
    private final Map<Id, ISqmlPackageDef> packageById = new HashMap<>(128);
    private final Map<Id, ISqmlFunctionDef> functionById = new HashMap<>(512);
    private final Map<Id, ISqmlTableDef> tableById = new HashMap<>(2048);
    private final Map<Id, ISqmlTableDef> classById = new HashMap<>(2048);
    private final Map<Id, ISqmlEventCodeDef> eventCodeById = new HashMap<>(512);
    private final Map<Id,List<ISqmlDetailTableReference>> detailReferencesByMasterTableId = new HashMap<>(256);
    
    public SqmlRepository(final IClientEnvironment env){
        environment = env;
    }
    
    public void addEnum(final SqmlEnumDefImpl enumDef){
        enumById.put(enumDef.getId(), enumDef);
    }
    
    public void addDomain(final SqmlDomainDefImpl domainDef){        
        domainById.put(domainDef.getId(), domainDef);
    }
    
    public void addPackage(final SqmlPackageDefImpl packageDef){
        packageById.put(packageDef.getId(), packageDef);
        for (ISqmlFunctionDef function: packageDef.getAllFunctions()){
            functionById.put(function.getId(), function);
        }
    }
    
    public void addTable(final SqmlTableDef tableDef){
        final Id tableId = tableDef.getId();
        final ISqmlTableDef sqmlDef = tableById.get(tableId);
        if (sqmlDef instanceof SqmlClassDef){
            ((SqmlClassDef)sqmlDef).linkWithTable(tableDef);
        }else{
            tableById.put(tableId, tableDef);
        }
    }
    
    public void addDetailReference(final Id masterTableId, final SqmlDetailTableReferenceImpl reference){
        List<ISqmlDetailTableReference> references = detailReferencesByMasterTableId.get(masterTableId);
        if (references==null){
            references = new LinkedList<>();            
            detailReferencesByMasterTableId.put(masterTableId, references);
        }
        references.add(reference);
    }
    
    public void addClass(final SqmlClassDef classDef){
        if (classDef.getId().getPrefix()==EDefinitionIdPrefix.ADS_APPLICATION_CLASS){
            classById.put(classDef.getId(), classDef);
        }else{
            final Id tableId = Id.Factory.changePrefix(classDef.getId(), EDefinitionIdPrefix.DDS_TABLE);
            final ISqmlTableDef sqmlDef = tableById.get(tableId);
            if (sqmlDef instanceof SqmlClassDef){
                final SqmlTableDef tableDef = ((SqmlClassDef)sqmlDef).getLinkedTable();
                if (tableDef!=null){
                    classDef.linkWithTable(tableDef);
                }
            }else if (sqmlDef instanceof SqmlTableDef){
                classDef.linkWithTable((SqmlTableDef)sqmlDef);                
            }
            tableById.put(tableId, classDef);
        }
    }    

    public ISqmlEnumDef findEnumById(final Id enumId){
        return enumById.get(enumId);
    }
    
    public ISqmlDomainDef findDomainById(final Id domainId){
        return domainById.get(domainId);
    }
    
    public ISqmlPackageDef findPackageById(final Id packageId){
        return packageById.get(packageId);
    }
    
    public ISqmlFunctionDef findFunctionById(final Id functionId){
        return functionById.get(functionId);
    }
    
    public ISqmlTableDef findTableById(final Id tableId){
        if (tableId.getPrefix()==EDefinitionIdPrefix.ADS_ENTITY_CLASS){
            return tableById.get(Id.Factory.changePrefix(tableId, EDefinitionIdPrefix.DDS_TABLE));
        }else if (tableId.getPrefix()==EDefinitionIdPrefix.ADS_APPLICATION_CLASS){
            return classById.get(tableId);
        }else{
            return tableById.get(tableId);
        }
    }
    
    public ISqmlEventCodeDef findEventCodeById(final Id eventCodeId){
        if (eventCodeById.isEmpty()){
            readEventCodes();
        }
        return eventCodeById.get(eventCodeId);
    }

    public Collection<ISqmlEnumDef> getEnums(){
        return enumById.values();
    }
    
    public Collection<ISqmlDomainDef> getDomains(){
        return domainById.values();
    }
    
    public Collection<ISqmlPackageDef> getPackages(){
        return packageById.values();
    }
    
    public Collection<ISqmlFunctionDef> getFunctions(){
        return functionById.values();
    }
    
    public Collection<ISqmlTableDef> getTables(){
        return tableById.values();
    }
    
    public Collection<ISqmlEventCodeDef> getEventCodes(){
        if (eventCodeById.isEmpty()){
            readEventCodes();
        }
        return eventCodeById.values();
    }
    
    public List<ISqmlDetailTableReference> getDetailReferencesForMasterTable(final Id tableId){
        final List<ISqmlDetailTableReference> references = detailReferencesByMasterTableId.get(tableId);
        return references==null ? Collections.<ISqmlDetailTableReference>emptyList() : references;
    }
    
    private void readEventCodes() {
        final Collection<ReleaseRepository.DefinitionInfo> bundles = environment.getDefManager().getRepository().getDefinitions(EDefType.LOCALIZING_BUNDLE);
        for (ReleaseRepository.DefinitionInfo b : bundles) {
            final RadMlStringBundleDef bundle = environment.getDefManager().getMlStringBundleDef(b.id);
            for (Id eid : bundle.getEventCodesIds()) {
                eventCodeById.put(eid, new SqmlEventCodeImpl(bundle, eid));
            }
        }
    }
    
    public void clear(){
        enumById.clear();
        domainById.clear();
        packageById.clear();
        functionById.clear();
        tableById.clear();
        eventCodeById.clear();
        detailReferencesByMasterTableId.clear();
    }
}
