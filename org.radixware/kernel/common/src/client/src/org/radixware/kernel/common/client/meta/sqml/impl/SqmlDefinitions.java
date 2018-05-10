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

package org.radixware.kernel.common.client.meta.sqml.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ReleaseRepository;
import org.radixware.kernel.common.client.meta.sqml.ISqmlBrokenDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitions;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDomainDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEventCodeDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlFunctionDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlPackageDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsServerSidePropertyDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsPackageDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProvider;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.meta.RadMlStringBundleDef;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.types.Id;


@Deprecated //use org.radixware.kernel.common.client.meta.sqml.impl.SqmlDefinitions instead
public class SqmlDefinitions implements ISqmlDefinitions {

    private final Branch branch;
    private final Map<Id, ISqmlEnumDef> enumsById = new HashMap<>(64);
    private final Map<Id, ISqmlTableDef> tablesById = new HashMap<>(512);
    private final Map<Id, ISqmlFunctionDef> functionsById = new HashMap<>(256);
    private final Map<Id, ISqmlPackageDef> packagesById = new HashMap<>(128);
    private final Map<Id, ISqmlTableDef> appClassesById = new HashMap<>(32);
    private final Map<String, ISqmlDefinition> definitionsByIdPath = new HashMap<>(64);
    private final List<ISqmlPackageDef> packages = new ArrayList<>();
    private final List<ISqmlDomainDef> domains = new ArrayList<>();
    private final Map<Id, ISqmlEventCodeDef> eventCodes = new HashMap<>();
    private final List<Id> unknown = new ArrayList<>();
    private boolean tablesWasCallected, enumsWasCollected, packagesWasCollected, domainsWasCollected;
    private VisitorProvider currentVisitor;
    private final Object currentVisitorSemaphore = new Object();
    private final IClientEnvironment environment;

    public SqmlDefinitions(final IClientEnvironment environment, final Branch branch) {
        this.branch = branch;
        this.environment = environment;
    }

    private static class AdsDefinitionSearcher extends AdsVisitorProvider.AdsTopLevelDefVisitorProvider {

        private final Id definitionId;
        private final boolean publishedOnly;
        private final Class<? extends AdsDefinition> definitionClass;

        public AdsDefinitionSearcher(final Class<? extends AdsDefinition> definitionClass, final Id definitionId, final boolean publishedOnly) {
            super();
            this.definitionClass = definitionClass;
            this.publishedOnly = publishedOnly;
            this.definitionId = definitionId;
        }

        public AdsDefinitionSearcher(final Class<? extends AdsDefinition> definitionClass) {
            this(definitionClass, null, true);
        }

        @Override
        public boolean isTarget(final RadixObject radixObject) {
            return definitionClass.isInstance(radixObject)
                    && (!publishedOnly || ((AdsDefinition) radixObject).isPublished())//RADIX-5007
                    && (definitionId == null || ((AdsDefinition) radixObject).getId().equals(definitionId));
        }
    }

    private static class DdsDefinitionSearcher extends DdsVisitorProvider {

        private final Id definitionId;
        private final Class<? extends DdsDefinition> definitionClass;

        public DdsDefinitionSearcher(final Class<? extends DdsDefinition> definitionClass, final Id definitionId) {
            super();
            this.definitionId = definitionId;
            this.definitionClass = definitionClass;
        }

        public DdsDefinitionSearcher(final Class<? extends DdsDefinition> definitionClass) {
            this(definitionClass, null);
        }

        @Override
        public boolean isTarget(final RadixObject radixObject) {
            return definitionClass.isInstance(radixObject)
                    && (definitionId == null || ((DdsDefinition) radixObject).getId().equals(definitionId));
        }
    }
    private final IVisitor packagesCollector = new IVisitor() {
        @Override
        public void accept(RadixObject radixObject) {
            if (radixObject instanceof DdsPackageDef) {
                final ISqmlPackageDef packageDef = new SqmlPackageDefImpl(environment, (DdsPackageDef) radixObject);
                final List<ISqmlFunctionDef> functions = packageDef.getAllFunctions();
                for (ISqmlFunctionDef function : functions) {
                    functionsById.put(function.getId(), function);
                }
                packages.add(packageDef);
            }
        }
    };
    private final DdsVisitorProvider packagesVisitor = new DdsVisitorProvider() {
        @Override
        public boolean isTarget(RadixObject radixObject) {
            return (radixObject instanceof DdsPackageDef);
        }
    };
    private final IVisitor tablesCollector = new IVisitor() {
        @Override
        public void accept(final RadixObject radixObject) {
            if (radixObject instanceof AdsEntityClassDef) {
                final AdsEntityClassDef adsClass = (AdsEntityClassDef) radixObject;
                final DdsTableDef table = adsClass.findTable(adsClass);
                if (table == null) {
                    final String message = environment.getMessageProvider().translate("TraceMessage", "Cannot find table for class '%s' #%s");
                    environment.getTracer().warning(String.format(message, adsClass.getQualifiedName(), adsClass.getId().toString()));
                } else {
                    final Id tableId = table.getId();
                    if (!tablesById.containsKey(tableId)) {
                        tablesById.put(tableId, new SqmlTableDefImpl(environment, adsClass));
                    }
                }
            } else if (radixObject instanceof DdsTableDef) {
                final DdsTableDef ddsTable = (DdsTableDef) radixObject;
                if (!tablesById.containsKey(ddsTable.getId())) {
                    tablesById.put(ddsTable.getId(), new SqmlTableDefImpl(environment, ddsTable));
                }
            }
        }
    };
    private final IVisitor domaindCollector = new IVisitor() {
        @Override
        public void accept(RadixObject radixObject) {
            if (radixObject instanceof AdsDomainDef) {
                domains.add(new SqmlDomainDefImpl(environment, (AdsDomainDef) radixObject));
            }
        }
    };
    private final IVisitor enumsCollector = new IVisitor() {
        @Override
        public void accept(final RadixObject radixObject) {
            if (radixObject instanceof AdsEnumDef) {
                final AdsEnumDef adsEnum = (AdsEnumDef) radixObject;
                final Id enumId = adsEnum.getId();
                if (!enumsById.containsKey(enumId)) {
                    enumsById.put(enumId, new SqmlEnumDefImpl(environment, adsEnum));
                }
            }
        }
    };

    @Override
    public ISqmlTableDef findTableById(final Id tableId) {
        //Пытаюсь сначала найти сущность, т.к. у нее набор свойств может быть больше.
        final ISqmlTableDef tableDef = findEntityClassById(tableId);
        if (tableDef == null && tableId.getPrefix() == EDefinitionIdPrefix.DDS_TABLE) {
            //Если сущность найти не удалось - ищу таблицу
            return findDdsTableById(tableId);
        }
        return tableDef;
    }

    @Override
    public Collection<ISqmlTableDef> getTables() {
        if (!tablesWasCallected) {
            final AdsDefinitionSearcher entitySearcher = new AdsDefinitionSearcher(AdsEntityObjectClassDef.class, null, false);//get all
            final DdsDefinitionSearcher tableSearcher = new DdsDefinitionSearcher(DdsTableDef.class);//get all
            //Сначала собираем все сущности
            synchronized (currentVisitorSemaphore) {
                currentVisitor = entitySearcher;
            }
            try {
                branch.visit(tablesCollector, entitySearcher);
                synchronized (currentVisitorSemaphore) {
                    if (currentVisitor.isCancelled()) {
                        tablesById.clear();
                        throw new CancellationException("Tables loading was cancelled");
                    }
                }
            } finally {
                synchronized (currentVisitorSemaphore) {
                    currentVisitor = null;
                }
            }
            //Затем все таблицы
            synchronized (currentVisitorSemaphore) {
                currentVisitor = tableSearcher;
            }
            try {
                branch.visit(tablesCollector, tableSearcher);
                synchronized (currentVisitorSemaphore) {
                    if (currentVisitor.isCancelled()) {
                        tablesById.clear();
                        throw new CancellationException("Tables loading was cancelled");
                    }
                }
            } finally {
                synchronized (currentVisitorSemaphore) {
                    currentVisitor = null;
                }
            }
            tablesWasCallected = true;
        }
        return tablesById.values();
    }

    @Override
    public Collection<ISqmlEnumDef> getEnums() {
        if (!enumsWasCollected) {
            branch.visit(enumsCollector, new AdsDefinitionSearcher(AdsEnumDef.class));
            enumsWasCollected = true;
        }
        return enumsById.values();
    }

    @Override
    public Collection<ISqmlDomainDef> getDomains() {
        if (!domainsWasCollected) {
            branch.visit(domaindCollector, new AdsDefinitionSearcher(AdsDomainDef.class));
            domainsWasCollected = true;
        }
        return domains;
    }

    private ISqmlTableDef findEntityClassById(final Id definitionId) {
        final Id entityClassId, tableId;
        if (definitionId.getPrefix() == EDefinitionIdPrefix.DDS_TABLE) {
            tableId = definitionId;
            entityClassId = Id.Factory.changePrefix(definitionId, EDefinitionIdPrefix.ADS_ENTITY_CLASS);
        } else {
            entityClassId = definitionId;
            if (definitionId.getPrefix() == EDefinitionIdPrefix.ADS_ENTITY_CLASS) {
                tableId = Id.Factory.changePrefix(definitionId, EDefinitionIdPrefix.DDS_TABLE);
            } else {
                tableId = definitionId;
            }
        }
        if (tablesById.containsKey(tableId)) {
            return tablesById.get(tableId);
        }
        if (!appClassesById.containsKey(tableId)) {
            final RadixObject adsEntity = findAdsDefinitionById(new AdsDefinitionSearcher(AdsEntityObjectClassDef.class, entityClassId, false));
            if (adsEntity instanceof AdsEntityClassDef) {
                final ISqmlTableDef sqmlTable = new SqmlTableDefImpl(environment, (AdsEntityClassDef) adsEntity);
                tablesById.put(tableId, sqmlTable);
                return sqmlTable;
            } else if (adsEntity != null) {
                final ISqmlTableDef sqmlTable = new SqmlTableDefImpl(environment, (AdsEntityObjectClassDef) adsEntity);
                appClassesById.put(tableId, sqmlTable);
                return sqmlTable;
            }
        }
        return appClassesById.get(tableId);

    }

    private ISqmlTableDef findDdsTableById(final Id tableId) {
        if (!tablesById.containsKey(tableId)) {
            if (unknown.contains(tableId)) {
                return null;
            }
            RadixObject ddsTable;
            ddsTable = branch.find(new DdsDefinitionSearcher(DdsTableDef.class, tableId));
            if (ddsTable != null) {
                final ISqmlTableDef sqmlTable = new SqmlTableDefImpl(environment, (DdsTableDef) ddsTable);
                tablesById.put(tableId, sqmlTable);
                return sqmlTable;
            } else {
                final String message = environment.getMessageProvider().translate("TraceMessage", "Cannot find table #%s");
                environment.getTracer().warning(String.format(message, tableId.toString()));
            }
            unknown.add(tableId);
            return null;
        }
        return tablesById.get(tableId);
    }

    private RadixObject findAdsDefinitionById(final VisitorProvider searcher) {
        return branch.find(searcher);
    }

    @Override
    public ISqmlEnumDef findEnumById(final Id enumId) {
        if (!enumsById.containsKey(enumId)) {
            if (unknown.contains(enumId)) {
                return null;
            }
            final RadixObject adsEnum = findAdsDefinitionById(new AdsDefinitionSearcher(AdsEnumDef.class, enumId, true));
            if (adsEnum != null) {
                final ISqmlEnumDef enumDef = new SqmlEnumDefImpl(environment, (AdsEnumDef) adsEnum);
                enumsById.put(enumId, enumDef);
                return enumDef;
            }
            unknown.add(enumId);
            return null;
        }
        return enumsById.get(enumId);
    }

    @Override
    public ISqmlFunctionDef findFunctionById(final Id functionId) {
        if (!functionsById.containsKey(functionId)) {
            if (unknown.contains(functionId)) {
                return null;
            }
            final RadixObject ddsFunction = branch.find(new DdsDefinitionSearcher(DdsFunctionDef.class, functionId));
            if (ddsFunction != null) {
                final ISqmlFunctionDef function = new SqmlFunctionDefImpl(environment, (DdsFunctionDef) ddsFunction);
                functionsById.put(functionId, function);
                return function;
            }
            unknown.add(functionId);
            return null;
        }
        return functionsById.get(functionId);
    }

    @Override
    public ISqmlPackageDef findPackageById(final Id packageId) {
        if (!packagesById.containsKey(packageId)) {
            if (unknown.contains(packageId)) {
                return null;
            }
            final RadixObject ddsPackage = branch.find(new DdsDefinitionSearcher(DdsPackageDef.class, packageId));
            if (ddsPackage != null) {
                final ISqmlPackageDef packageDef = new SqmlPackageDefImpl(environment, (DdsPackageDef) ddsPackage);
                packagesById.put(packageId, packageDef);
                return packageDef;
            }
            unknown.add(packageId);
            return null;
        }
        return packagesById.get(packageId);
    }

    @Override
    public Collection<ISqmlPackageDef> getPackages() {
        if (!packagesWasCollected) {
            branch.visit(packagesCollector, packagesVisitor);
            packagesWasCollected = true;
        }
        return Collections.unmodifiableCollection(packages);
    }

    private Definition findDefinition(final Definition owner, final Id definitionId) {
        if (owner == null) {
            Definition result =
                    (Definition) branch.find(new DdsVisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return radixObject instanceof Definition ? ((Definition) radixObject).getId().equals(definitionId) : false;
                }
            });
            if (result == null) {
                result = (Definition) branch.find(new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject radixObject) {
                        return radixObject instanceof Definition ? ((Definition) radixObject).getId().equals(definitionId) : false;
                    }
                });
            }
            return result;
        } else {
            return (Definition) owner.find(new VisitorProvider() {
                @Override
                public boolean isContainer(RadixObject object) {
                    return true;
                }

                @Override
                public boolean isTarget(RadixObject object) {
                    return object instanceof Definition ? ((Definition) object).getId().equals(definitionId) : false;
                }
            });
        }
    }

    @Override
    public ISqmlDefinition findDefinitionByIdPath(final List<Id> ids) {
        if (ids != null) {
            final StringBuilder keyBuilder = new StringBuilder();
            for (Id id : ids) {
                keyBuilder.append(id.toString());
            }
            final String key = keyBuilder.toString();
            if (!definitionsByIdPath.containsKey(key)) {
                Definition definition = null, childDefinition;
                Id lostDefinitionId = null;
                for (Id id : ids) {
                    childDefinition = findDefinition(definition, id);
                    if (childDefinition == null) {
                        lostDefinitionId = id;
                        break;
                    } else {
                        definition = childDefinition;
                    }
                }
                if (definition == null) {
                    definitionsByIdPath.put(key, null);
                } else if (lostDefinitionId == null) {
                    final SqmlDefinitionImpl sqmlDefiniton;
                    if (definition instanceof DdsTableDef) {
                        sqmlDefiniton = new SqmlTableDefImpl(environment, (DdsTableDef) definition);
                    } else if (definition instanceof AdsEntityObjectClassDef) {
                        sqmlDefiniton = new SqmlTableDefImpl(environment, (AdsEntityObjectClassDef) definition);
                    } else if (definition instanceof DdsColumnDef) {
                        sqmlDefiniton = new SqmlColumnDefImpl(environment, (DdsColumnDef) definition, ((DdsColumnDef) definition).getOwnerTable().getId());
                    } else if (definition instanceof AdsServerSidePropertyDef) {
                        final AdsServerSidePropertyDef propertyDef = (AdsServerSidePropertyDef) definition;
                        sqmlDefiniton = new SqmlColumnDefImpl(environment, propertyDef, propertyDef.getOwnerClass().getId());
                    } else if (definition instanceof AdsDomainDef) {
                        sqmlDefiniton = new SqmlDomainDefImpl(environment, (AdsDomainDef) definition);
                    } else {
                        sqmlDefiniton = new SqmlDefinitionImpl(environment, definition) {
                            @Override
                            public String getTitle() {
                                return getShortName();
                            }
                        };
                    }
                    definitionsByIdPath.put(key, sqmlDefiniton);
                } else {
                    definitionsByIdPath.put(key, new SqmlBrokenDefinitionImpl(environment, definition, lostDefinitionId));
                }
            }
            return definitionsByIdPath.get(key);
        }
        return null;
    }

    @Override
    public ISqmlBrokenDefinition createBrokenDefinition(Id id, String typeName) {
        return new SqmlBrokenDefinitionImpl(environment, null, id, typeName);
    }

    /**
     *
     * @return
     */
    @Override
    public Collection<ISqmlEventCodeDef> getEventCodes() {
        if (eventCodes.isEmpty()) {
            readEventCodes();
        }
        return Collections.unmodifiableCollection(eventCodes.values());
    }

    @Override
    public ISqmlEventCodeDef findEventCodeById(Id id) {
        if (eventCodes.isEmpty()) {
            readEventCodes();
        }
        return eventCodes.get(id);
    }

    public void cancelLoading() {
        synchronized (currentVisitorSemaphore) {
            if (currentVisitor != null) {
                currentVisitor.cancel();
            }
        }
    }

    /**
     * Saves event codes to cache
     */
    private void readEventCodes() {
        final Collection<ReleaseRepository.DefinitionInfo> bundles = environment.getDefManager().getRepository().getDefinitions(EDefType.LOCALIZING_BUNDLE);
        for (ReleaseRepository.DefinitionInfo b : bundles) {
            final RadMlStringBundleDef bundle = environment.getDefManager().getMlStringBundleDef(b.id);
            for (Id eid : bundle.getEventCodesIds()) {
                eventCodes.put(eid, new SqmlEventCodeImpl(environment, bundle, eid));
            }
        }
    }
}