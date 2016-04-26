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

package org.radixware.kernel.common.defs.ads.clazz.entity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionLink;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas.AccessArea.Partition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityPresentations;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.radixdoc.EntityClassRadixdoc;
import org.radixware.kernel.common.defs.ads.rights.SystemPresentationBuilder;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.enums.EAccessAreaType;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.RadixObjectsUtils;

import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.ClassDefinition.Entity;
import org.radixware.schemas.radixdoc.Page;


public class AdsEntityClassDef extends AdsEntityObjectClassDef {

    public static final int FORMAT_VERSION = IAdsPresentableClass.FORMAT_VERSION;

    @Override
    public long getFormatVersion() {
        return FORMAT_VERSION;
    }
    public static final Id PREDEFINED_ID = Id.Factory.loadFrom("pdcEntity____________________");
    public static final String PLATFORM_CLASS_NAME = "org.radixware.kernel.server.types.Entity";

    public static final class Factory {

        public static AdsEntityClassDef loadFrom(ClassDefinition classDef) {
            return new AdsEntityClassDef(classDef);
        }

        public static AdsEntityClassDef newInstance(DdsTableDef table) {
            return new AdsEntityClassDef(table, "NewEntityClass");
        }
    }

    public class AccessAreas extends RadixObjects<AccessAreas.AccessArea> {

        public static final int MAX_AREAS_COUNT = 5;

        private AccessAreas() {
            super(AdsEntityClassDef.this);
            this.type = EAccessAreaType.NONE;
            this.inheritReferenceId = null;
        }

        private AccessAreas(AccessAreas source, boolean forOverwrite) {
            super(AdsEntityClassDef.this);
            this.type = forOverwrite ? EAccessAreaType.NOT_OVERRIDDEN : source.getType();
            this.inheritReferenceId = source.inheritReferenceId;
        }

        @Override
        public String getName() {
            return "Access Area Set";
        }

        public AccessArea newAccessArea(String name) {
            return new AccessArea(name);
        }

        public Id getInheritReferenceId() {
            return this.inheritReferenceId;
        }

        public boolean setInheritReferenceId(Id id) {
            if (type != EAccessAreaType.INHERITED) {
                return false;
            } else {
                this.inheritReferenceId = id;
                setEditState(EEditState.MODIFIED);
                return true;
            }
        }

        public DdsReferenceDef findInheritReference() {
            return AdsSearcher.Factory.newDdsReferenceSearcher(AdsEntityClassDef.this).findById(inheritReferenceId).get();
        }

        public EAccessAreaType getType() {
            return type;
        }

        public boolean setType(EAccessAreaType type) {
            if (type == EAccessAreaType.NOT_OVERRIDDEN) {
                if (AdsEntityClassDef.this.getHierarchy().findOverwritten().get() == null) {
                    return false;
                }
            }
            this.type = type;
            setEditState(EEditState.MODIFIED);

            return true;
        }

        public class AccessArea extends RadixObject {

            public class Partitions extends RadixObjects<Partition> {

                private Partitions() {
                    super(AccessArea.this);
                }
            }

            public Partitions getPartitions() {
                return partitions;
            }

            @Override
            public boolean setName(String name) {
                if (super.setName(name)) {
                    setEditState(EEditState.MODIFIED);
                    return true;
                }
                return false;
            }

            @Override
            public RadixIcon getIcon() {
                return AdsDefinitionIcon.ACCESS_AREA;
            }

            public Partition newPartition(DdsAccessPartitionFamilyDef family, AdsInnateColumnPropertyDef property, List<DdsReferenceDef> references) {
                return new Partition(family, property, references);
            }

            public class Partition extends RadixObject {

                private List<Id> referenceIds;
                private Id familyId;
                private Id propertyId;

                private Partition(ClassDefinition.Entity.AccessAreas.AccessArea.Partition p) {
                    this.familyId = p.getFamilyId();
                    this.propertyId = p.getPropId();
                    if (p.getRefIds() != null && !p.getRefIds().isEmpty()) {
                        this.referenceIds = new ArrayList<Id>(p.getRefIds());
                    }
                }

                private Partition(DdsAccessPartitionFamilyDef apf, AdsInnateColumnPropertyDef property, List<DdsReferenceDef> references) {
                    this.familyId = apf.getId();
                    this.propertyId = property.getId();
                    if (references != null && !references.isEmpty()) {
                        this.referenceIds = new ArrayList<Id>();
                        for (DdsReferenceDef ref : references) {
                            this.referenceIds.add(ref.getId());
                        }
                    }
                }

                //by BAO
                @Override
                public String getName() {
                    final DdsAccessPartitionFamilyDef ddsAccessPartitionFamilyDef = findApf();
                    return ddsAccessPartitionFamilyDef == null ? "Wrong id " + familyId : ddsAccessPartitionFamilyDef.getName();
                }
                //

                public void appendTo(ClassDefinition.Entity.AccessAreas.AccessArea.Partition p) {
                    p.setFamilyId(familyId);
                    p.setPropId(propertyId);
                    if (this.referenceIds != null) {
                        p.setRefIds(referenceIds);
                    }
                }

                public Id getFamilyId() {
                    return familyId;
                }

                public void setFamilyId(Id id) {
                    this.familyId = id;
                    setEditState(EEditState.MODIFIED);
                }

                public Id getPropertyId() {
                    return propertyId;
                }

                public List<Id> getReferenceIds() {
                    if (referenceIds == null) {
                        return Collections.emptyList();
                    } else {
                        return new ArrayList<Id>(referenceIds);
                    }
                }

                @Override
                public RadixIcon getIcon() {
                    return AdsDefinitionIcon.ACCESS_PARTITION;
                }

                public AdsEntityClassDef findReferencedClass() {
                    if (referenceIds != null) {
                        Id classId = null;
                        DefinitionSearcher<DdsReferenceDef> searcher = AdsSearcher.Factory.newDdsReferenceSearcher(AdsEntityClassDef.this);


                        for (Id id : referenceIds) {
                            DdsReferenceDef ref = searcher.findById(id).get();
                            if (ref != null) {
                                classId = Id.Factory.changePrefix(ref.getParentTableId(), EDefinitionIdPrefix.ADS_ENTITY_CLASS);
                            }
                        }
                        if (classId != null) {
                            AdsClassDef clazz = AdsSearcher.Factory.newAdsClassSearcher(AdsEntityClassDef.this).findById(classId).get();
                            if (clazz instanceof AdsEntityClassDef) {
                                return (AdsEntityClassDef) clazz;
                            } else {
                                return null;
                            }
                        } else {
                            return null;
                        }

                    } else {
                        return (AdsEntityClassDef) this.getOwnerDefinition();
                    }
                }

                public AdsPropertyDef findReferencedProperty() {
                    AdsEntityClassDef clazz = findReferencedClass();
                    if (clazz != null) {
                        return clazz.getProperties().findById(propertyId, EScope.ALL).get();
                    } else {
                        return null;
                    }
                }

                public DdsAccessPartitionFamilyDef findApf() {
                    //return AdsSearcher.Factory.newDdsApfSearcher(getOwnerClass()).findById(familyId);
                    Layer layer = getOwnerClass().getModule().getSegment().getLayer();
                    return AdsUtils.findTopLevelApfById(layer, familyId);
                }

                @Override
                public void collectDependences(List<Definition> list) {
                    super.collectDependences(list);
                    if (referenceIds != null) {
                        DefinitionSearcher<DdsReferenceDef> searcher = AdsSearcher.Factory.newDdsReferenceSearcher(AdsEntityClassDef.this);
                        for (Id id : referenceIds) {
                            DdsReferenceDef ref = searcher.findById(id).get();
                            if (ref != null) {
                                list.add(ref);
                            }
                        }
                    }
                    DdsAccessPartitionFamilyDef apf = AdsSearcher.Factory.newDdsApfSearcher(AdsEntityClassDef.this).findById(familyId).get();
                    if (apf != null) {
                        list.add(apf);
                    }
                    AdsPropertyDef prop = findReferencedProperty();
                    if (prop != null) {
                        list.add(prop);
                    }
                }
            }
            private Partitions partitions = new Partitions();

            private AccessArea(ClassDefinition.Entity.AccessAreas.AccessArea area) {
                super(area.getName());
                List<ClassDefinition.Entity.AccessAreas.AccessArea.Partition> xPartitions = area.getPartitionList();

                for (ClassDefinition.Entity.AccessAreas.AccessArea.Partition p : xPartitions) {
                    this.partitions.add(new Partition(p));
                }
            }

            private AccessArea(String name) {
                super(name);
            }

            public AccessAreas getOwnerAreas() {
                return (AccessAreas) getContainer();
            }

            public AdsEntityClassDef getOwnerClass() {
                return getOwnerAreas().getOwnerClass();
            }

            public void appendTo(ClassDefinition.Entity.AccessAreas.AccessArea xArea) {
                xArea.setName(this.getName());

                for (Partition p : this.partitions) {
                    p.appendTo(xArea.addNewPartition());
                }
            }
        }
        private EAccessAreaType type;
        private Id inheritReferenceId;

        private AccessAreas(ClassDefinition.Entity.AccessAreas xDef) {
            super(AdsEntityClassDef.this);
            if (xDef != null && xDef.getAccessAreaList() != null) {
                List<ClassDefinition.Entity.AccessAreas.AccessArea> xAreas = xDef.getAccessAreaList();

                for (ClassDefinition.Entity.AccessAreas.AccessArea area : xAreas) {
                    this.add(new AccessArea(area));
                }
                this.inheritReferenceId = xDef.getInheritReferenceId();
                if (xDef.getType() == ClassDefinition.Entity.AccessAreas.Type.OWN) {
                    this.type = EAccessAreaType.OWN;
                } else if (xDef.getType() == ClassDefinition.Entity.AccessAreas.Type.INHERITED) {
                    this.type = EAccessAreaType.INHERITED;
                } else if (xDef.getType() == ClassDefinition.Entity.AccessAreas.Type.NOT_OVERRIDEN) {
                    this.type = EAccessAreaType.NOT_OVERRIDDEN;
                } else {
                    this.type = EAccessAreaType.NONE;
                }
            } else {
                this.type = EAccessAreaType.NONE;
                this.inheritReferenceId = null;
            }
        }

        public AdsEntityClassDef getOwnerClass() {
            return (AdsEntityClassDef) getContainer();
        }

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);
            DdsReferenceDef ref = AdsSearcher.Factory.newDdsReferenceSearcher(AdsEntityClassDef.this).findById(inheritReferenceId).get();
            if (ref != null) {
                list.add(ref);
            }
        }

        private void appendTo(ClassDefinition.Entity e) {
            ClassDefinition.Entity.AccessAreas xDef = e.addNewAccessAreas();
            for (AccessArea a : this) {
                a.appendTo(xDef.addNewAccessArea());
            }
            if (this.inheritReferenceId != null) {
                xDef.setInheritReferenceId(this.inheritReferenceId);
            }
            switch (type) {
                case INHERITED:
                    xDef.setType(ClassDefinition.Entity.AccessAreas.Type.INHERITED);
                    break;
                case NONE:
                    xDef.setType(ClassDefinition.Entity.AccessAreas.Type.NONE);
                    break;
                case NOT_OVERRIDDEN:
                    xDef.setType(ClassDefinition.Entity.AccessAreas.Type.NOT_OVERRIDEN);
                    break;
                case OWN:
                    xDef.setType(ClassDefinition.Entity.AccessAreas.Type.OWN);
                    break;
            }
        }
    }
    private final AccessAreas accessAreas;

    AdsEntityClassDef(ClassDefinition xDef) {
        super(xDef);
        ClassDefinition.Entity e = xDef.getEntity();
        if (e != null) {
            this.accessAreas = new AccessAreas(e.getAccessAreas());
        } else {
            this.accessAreas = new AccessAreas();
        }

    }

    public AccessAreas getAccessAreas() {
        return accessAreas;
    }

    AdsEntityClassDef(DdsTableDef table, String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_ENTITY_CLASS), name);
        this.associateWithTable(table);
        this.accessAreas = new AccessAreas();
    }

    AdsEntityClassDef(AdsEntityClassDef source, boolean forOverwrite) {
        super(source);
        this.accessAreas = new AccessAreas(source.getAccessAreas(), forOverwrite);
    }

    @Override
    protected void appendTo(ClassDefinition xClass, Entity xDef, ESaveMode saveMode) {
        super.appendTo(xClass, xDef, saveMode);
        this.accessAreas.appendTo(xDef);
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.ENTITY;
    }

    /**
     * Associates the class with given dds table
     */
    private void associateWithTable(DdsTableDef tableDef) {
        if (tableDef == null) {
            throw new NullPointerException();
        }

        if (getContainer() != null) {
            throw new DefinitionError("Link with table must be configured throw setup process only.", this);
        }
        this.entityId = tableDef.getId();
        setId(Id.Factory.changePrefix(tableDef.getId(), EDefinitionIdPrefix.ADS_ENTITY_CLASS));
    }

    private class TableLink extends DefinitionLink<DdsTableDef> {

        WeakReference<Layer> lastContextLayer;

        public TableLink(Layer layer) {
            lastContextLayer = new WeakReference<>(layer);
        }

        public Layer getLayer() {
            return lastContextLayer.get();
        }

        @Override
        protected DdsTableDef search() {
            AdsModule module = getModule();
            if (module == null) {
                return null;
            }
            return AdsSearcher.Factory.newDdsTableSearcher(AdsEntityClassDef.this).findById(getEntityId()).get();
        }
    }
    private final Object tableLinkLock = new Object();
    private TableLink tableLink;

    @Override
    public DdsTableDef findTable(RadixObject context) {
        if (context == null || context.getLayer() == null) {
            context = this;
        }
        synchronized (tableLinkLock) {
            if (tableLink == null || tableLink.getLayer() != context.getLayer()) {
                tableLink = new TableLink(context.getLayer());
            }

        }
        return tableLink.find();
    }

    @Override
    public AdsEntityObjectClassDef findBasis() {
        return null;
    }

    private class EntityGroupLink extends DefinitionLink<AdsEntityGroupClassDef> {

        @Override
        protected AdsEntityGroupClassDef search() {
            AdsModule module = getModule();
            if (module == null) {
                return null;
            } else {
                AdsDefinition def = AdsSearcher.Factory.newAdsDefinitionSearcher(AdsEntityClassDef.this).findById(Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.ADS_ENTITY_GROUP_CLASS)).get();
                if (def instanceof AdsEntityGroupClassDef) {
                    return (AdsEntityGroupClassDef) def;
                } else {
                    return null;
                }
            }
        }
    }
    private final EntityGroupLink groupLink = new EntityGroupLink();

    public AdsEntityGroupClassDef findEntityGroup() {
        return groupLink.find();
    }

    @Override
    public EntityPresentations getPresentations() {
        return (EntityPresentations) super.getPresentations();
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_ENTITY;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        this.accessAreas.visit(visitor, provider);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        if (getId().equals(SystemPresentationBuilder.USER2ROLE_ID) || getId().equals(SystemPresentationBuilder.USERGROUP2ROLE_ID)) {
            Collection<Definition> apfCollection = RadixObjectsUtils.collectAllAround(this, DdsVisitorProviderFactory.newAccessPartitionFamilyProvider());
            list.addAll(apfCollection);
        }
    }

    @Override
    public AdsType getType(final EValType typeId, final String extStr) {
        if (AdsClassType.EntityGroupModelType.TYPE_SUFFIX.equals(extStr)) {
            return new AdsClassType.EntityGroupModelType(this);
        } else {
            return super.getType(typeId, extStr);
        }
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsClassDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new EntityClassRadixdoc(getSource(), page, options);
            }
        };
    }
}
