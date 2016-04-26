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

package org.radixware.kernel.designer.ads.editors.clazz.property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDetailColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDetailRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParentPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty;
import org.radixware.kernel.common.defs.ads.common.AdsFilters;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionSequence;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;

/**
 * Common link panel for properties using other properties as some basic
 * determinant:
 * <ul>
 * <li>{@linkplain AdsInnateColumnPropertyDef Innate Column Property}</li>
 * <li>{@linkplain AdsInnateRefPropertyDef Innate Ref Property}</li>
 * <li>{@linkplain AdsDetailColumnPropertyDef Detail Column Property}</li>
 * <li>{@linkplain AdsDetailRefPropertyDef Detail Ref Property}</li>
 * <li>{@linkplain AdsParentPropertyDef Parent Property}</li>
 * <li>{@linkplain AdsPropertyPresentationPropertyDef Property Presentation}</li>
 * </ul>
 *
 */
public class PublishingPropertyRefPanel extends DefinitionLinkEditPanel {

    private final StateManager stateManager = new StateManager(this);

    private class ChooseParentPropCfg extends ChooseDefinitionCfg {
        
        ChooseParentPropCfg(AdsClassDef clazz) {
            super(clazz.getProperties().get(EScope.ALL));
            setTypesTitle("Choose Final Property or Next Reference in Class " + clazz.getQualifiedName(contextClass));
        }

        ChooseParentPropCfg() {
            super(contextClass.getProperties().get(EScope.ALL, new IFilter<AdsPropertyDef>() {
                @Override
                public boolean isTarget(AdsPropertyDef object) {
                    return object.getValue().getType().getTypeId() == EValType.PARENT_REF || object.getValue().getType().resolve(contextClass).get() instanceof AdsClassType.EntityObjectType;
                }
            }));
            setTypeTitle("Choose Parent Reference in Class " + contextClass.getName());
        }
    }

    private class ChooseParentPropCfgs extends ChooseDefinitionSequence.ChooseDefinitionCfgs {
        
        public ChooseParentPropCfgs() {
            super(new ChooseParentPropCfg());
        }

        @Override
        public String getDisplayName() {
            return "Choose Parent Property";
        }

        @Override
        protected boolean hasNextConfig(Definition choosenDef) {
            if (choosenDef instanceof AdsPropertyDef) {
                AdsPropertyDef prop = (AdsPropertyDef) choosenDef;
//                if (prop.getValue().getType().getTypeId() == EValType.PARENT_REF) {
                    AdsType type = prop.getValue().getType().resolve(contextClass).get();
                    if (type instanceof AdsClassType.EntityObjectType) {
                        return true;
                    }
//                }
            }
            return false;
        }

        @Override
        protected boolean isFinalTarget(Definition choosenDef) {            
            if (choosenDef instanceof AdsPropertyDef) {
                AdsPropertyDef prop = (AdsPropertyDef) choosenDef;
                if (contextClass.getProperties().findById(prop.getId(), EScope.ALL).get() == prop) {
                    if (prop.getValue().getType().getTypeId() == EValType.PARENT_REF){
                        return false;
                    } else {
                        AdsType type = prop.getValue().getType().resolve(contextClass).get();
                        if (type instanceof AdsClassType.EntityObjectType) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }

        @Override
        protected ChooseDefinitionCfg nextConfig(Definition choosenDef) {
            if (choosenDef instanceof AdsPropertyDef) {
                AdsPropertyDef prop = (AdsPropertyDef) choosenDef;
//                if (prop.getValue().getType().getTypeId() == EValType.PARENT_REF || prop.getValue().getType().getTypeId() == EValType.USER_CLASS) {
                    AdsType type = prop.getValue().getType().resolve(contextClass).get();
                    if (type instanceof AdsClassType.EntityObjectType) {
                        return new ChooseParentPropCfg(((AdsClassType.EntityObjectType) type).getSource());
                    }
//                }
            }
            return null;
        }
    }

    private class ChooseDetailRefsCfg extends ChooseDefinitionCfg {

        public ChooseDetailRefsCfg() {
            super(contextTable.collectIncomingReferences(new IFilter<DdsReferenceDef>() {
                @Override
                public boolean isTarget(DdsReferenceDef reference) {
                    return reference.getType() == DdsReferenceDef.EType.MASTER_DETAIL
                            && contextEntityClass.isDetailAllowed(reference.getId()) && isVisibleReference(reference);
                }
            }));
            setTypesTitle("Choose Detail Reference");
        }
    }

    private boolean isVisibleReference(DdsReferenceDef ref) {
        Layer thisLayer = contextClass.getModule().getSegment().getLayer();
        Layer refLayer = ref.getModule().getSegment().getLayer();

        return thisLayer == refLayer || thisLayer.isHigherThan(refLayer);
    }

    private class ChooseDetailColumn extends ChooseDefinitionCfg {

        public ChooseDetailColumn(List<DdsColumnDef> columns, String tableName) {
            super(columns);
            setTypesTitle("Choose Column of Table" + tableName);
        }

        public ChooseDetailColumn(DdsTableDef detailTable) {
            this(detailTable.getColumns().get(EScope.ALL), detailTable.getQualifiedName());
        }
    }

    private class ChooseDetailParentRef extends ChooseDefinitionCfg {

        public ChooseDetailParentRef(DdsTableDef detailTable) {
            super(detailTable.collectOutgoingReferences(new IFilter<DdsReferenceDef>() {
                @Override
                public boolean isTarget(DdsReferenceDef object) {
                    if (object.getType() != DdsReferenceDef.EType.MASTER_DETAIL && isVisibleReference(object)) {
                        DdsTableDef table = object.findParentTable(property);
                        if (table != null) {
                            return AdsUtils.isEntityClassExists(table);
                        }
                    }
                    return false;
                }
            }));
        }
    }

    private class ChooseDetailColumnOrRefCfgs extends ChooseDefinitionSequence.ChooseDefinitionCfgs {

        ChooseDetailColumnOrRefCfgs() {
            super(new ChooseDetailRefsCfg());
        }

        /**
         * Search unused columns.
         */
        private List<DdsColumnDef> findColumns(DdsTableDef detailTable) {
            if (detailTable == null || contextEntityClass == null) {
                return Collections.EMPTY_LIST;
            }

            final Set<Id> ids = new HashSet<>();
            for (final AdsPropertyDef prop : contextEntityClass.getProperties().get(EScope.ALL)) {
                if (prop instanceof AdsDetailColumnPropertyDef) {
                    final AdsDetailColumnPropertyDef detailProp = (AdsDetailColumnPropertyDef) prop;
                    ids.add(detailProp.getColumnInfo().getColumnId());
                }
            }

            final List<DdsColumnDef> columns = new ArrayList<>();
            final List<DdsColumnDef> allColumns = detailTable.getColumns().get(EScope.ALL);
            for (final DdsColumnDef column : allColumns) {
                if (!ids.contains(column.getId())) {
                    columns.add(column);
                }
            }
            return columns;
        }

        @Override
        protected ChooseDefinitionCfg nextConfig(Definition choosenDef) {
            if (choosenDef instanceof DdsReferenceDef) {
                final DdsTableDef table = ((DdsReferenceDef) choosenDef).findChildTable(property);
                if (property instanceof AdsDetailColumnPropertyDef) {
                    return new ChooseDetailColumn(findColumns(table), table.getQualifiedName());
                } else {
                    return new ChooseDetailParentRef(table);
                }
            }
            return null;
        }

        @Override
        protected boolean hasNextConfig(Definition choosenDef) {
            if (choosenDef instanceof DdsColumnDef) {
                return false;
            }
            if (choosenDef instanceof DdsReferenceDef) {
                DdsReferenceDef ref = (DdsReferenceDef) choosenDef;
                if (ref.getType() == DdsReferenceDef.EType.MASTER_DETAIL && ref.getParentTableId() == contextTable.getId()) {
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        }

        @Override
        protected boolean isFinalTarget(Definition choosenDef) {
            return !hasNextConfig(choosenDef);
        }

        @Override
        public String getDisplayName() {
            return "Choose Detail Column or Parent Reference";
        }
    }

    private class ChooseInnateColumnCfg extends ChooseDefinitionCfg {

        public ChooseInnateColumnCfg() {
            super(contextTable.getColumns().get(EScope.ALL, new IFilter<DdsColumnDef>() {
                @Override
                public boolean isTarget(DdsColumnDef object) {
                    return contextClass.getProperties().findById(object.getId(), EScope.LOCAL).get() == null;
                }
            }));
            setTypesTitle("Choose Column of Table " + contextTable.getQualifiedName());
        }
    }

    private class ChooseParentRefCfg extends ChooseDefinitionCfg {

        public ChooseParentRefCfg() {
            super(contextTable.collectOutgoingReferences(new IFilter<DdsReferenceDef>() {
                @Override
                public boolean isTarget(DdsReferenceDef object) {
                    return object.getType() != DdsReferenceDef.EType.MASTER_DETAIL && isVisibleReference(object);
                }
            }));
            setTypesTitle("Choose Outgoing Reference of Table " + contextTable.getQualifiedName());
        }
    }

    private class ChooseInnateColumnOrRefCfgs extends ChooseDefinitionSequence.ChooseDefinitionCfgs {

        public ChooseInnateColumnOrRefCfgs() {
            super(property instanceof AdsInnateColumnPropertyDef ? new ChooseInnateColumnCfg() : new ChooseParentRefCfg());
        }

        @Override
        protected ChooseDefinitionCfg nextConfig(Definition choosenDef) {
            return null;
        }

        @Override
        protected boolean hasNextConfig(Definition choosenDef) {
            return false;
        }

        @Override
        protected boolean isFinalTarget(Definition choosenDef) {
            return choosenDef instanceof DdsColumnDef;
        }

        @Override
        public String getDisplayName() {
            return "Choose Table Column";
        }
    }

    private class ChooseServerPropertyCfg extends ChooseDefinitionCfg {

        public ChooseServerPropertyCfg() {
            super(list2collection((AdsPropertyPresentationPropertyDef) property));
            setTypesTitle("Choose Corresponding Property");
        }
    }

    private static Collection<? extends AdsDefinition> list2collection(AdsPropertyPresentationPropertyDef property) {
        if (property == null) {
            return Collections.emptyList();
        }
        IModelPublishableProperty.Provider provider = property.findModelPropertyProvider();
        if (provider == null) {
            return Collections.emptyList();
        }
        IModelPublishableProperty.Support support = provider.getModelPublishablePropertySupport();
        if (support == null) {
            return Collections.emptyList();
        }

        return list2collection(support.list(property.getClientEnvironment(), EScope.ALL, AdsFilters.newAvailablePublishablePropertyFilter(property)));
    }

    private static Collection<? extends AdsDefinition> list2collection(List objects) {
        if (objects == null || objects.isEmpty()) {
            return Collections.emptySet();
        } else {
            ArrayList<AdsDefinition> defs = new ArrayList<AdsDefinition>(objects.size());
            for (Object obj : objects) {
                defs.add((AdsDefinition) obj);
            }
            return defs;
        }
    }

    private class ChooseServerPropertyCfgs extends ChooseDefinitionSequence.ChooseDefinitionCfgs {

        public ChooseServerPropertyCfgs() {
            super(new ChooseServerPropertyCfg());
        }

        @Override
        public String getDisplayName() {
            return "Choose Server Side Property";
        }

        @Override
        protected boolean hasNextConfig(Definition choosenDef) {
            return false;
        }

        @Override
        protected boolean isFinalTarget(Definition choosenDef) {
            return choosenDef instanceof AdsPropertyDef;
        }

        @Override
        protected ChooseDefinitionCfg nextConfig(Definition choosenDef) {
            return null;
        }
    }
    private AdsClassDef contextClass;
    private AdsEntityObjectClassDef contextEntityClass;
    private DdsTableDef contextTable;
    private AdsPropertyDef property;

    public PublishingPropertyRefPanel() {
    }

    public void open(AdsPropertyDef property) {
        if (property == null) {
            throw new NullPointerException("Property must not be null");
        }
        this.contextClass = property.getOwnerClass();
        this.property = property;
        //this.setEnabled(!property.isReadOnly());
        ChooseDefinitionSequence.ChooseDefinitionCfgs cfgs = null;
        Definition referencedObject = null;
        Id referencedObjectId = null;
        switch (property.getNature()) {
            case PARENT_PROP:
                cfgs = new ChooseParentPropCfgs();
                referencedObject = ((AdsParentPropertyDef) property).getParentInfo().findOriginalProperty();
                referencedObjectId = ((AdsParentPropertyDef) property).getParentInfo().getOriginalPropertyId();
                break;
            case DETAIL_PROP:
                this.contextEntityClass = (AdsEntityObjectClassDef) contextClass;
                this.contextTable = contextEntityClass.findTable(property);
                if (this.contextTable != null) {
                    cfgs = new ChooseDetailColumnOrRefCfgs();
                    if (property instanceof AdsDetailColumnPropertyDef) {
                        referencedObject = ((AdsDetailColumnPropertyDef) property).getColumnInfo().findColumn();
                        referencedObjectId = ((AdsDetailColumnPropertyDef) property).getColumnInfo().getColumnId();
                    } else {
                        referencedObject = ((AdsDetailRefPropertyDef) property).getParentReferenceInfo().findParentReference();
                        referencedObjectId = ((AdsDetailRefPropertyDef) property).getParentReferenceInfo().getParentReferenceId();
                    }
                }
                break;
            case INNATE:
                this.contextEntityClass = (AdsEntityObjectClassDef) contextClass;
                this.contextTable = contextEntityClass.findTable(property);
                if (this.contextTable != null) {
                    cfgs = new ChooseInnateColumnOrRefCfgs();
                    if (property instanceof AdsInnateColumnPropertyDef) {
                        referencedObject = ((AdsInnateColumnPropertyDef) property).getColumnInfo().findColumn();
                        referencedObjectId = ((AdsInnateColumnPropertyDef) property).getColumnInfo().getColumnId();
                    } else {
                        referencedObject = ((AdsInnateRefPropertyDef) property).getParentReferenceInfo().findParentReference();
                        referencedObjectId = ((AdsInnateRefPropertyDef) property).getParentReferenceInfo().getParentReferenceId();
                    }
                }
                break;
            case PROPERTY_PRESENTATION:
                cfgs = new ChooseServerPropertyCfgs();
                final AdsPropertyPresentationPropertyDef pp = (AdsPropertyPresentationPropertyDef) property;
                referencedObject = (Definition) pp.findServerSideProperty();
                referencedObjectId = null;
                break;
            default:
                throw new IllegalStateException("Unsupported Property Nature: " + property.getNature().name());
        }
        super.open(cfgs, referencedObject, referencedObjectId);
    }

    public boolean isComplete() {
        switch (property.getNature()) {
            case PARENT_PROP:
                final AdsParentPropertyDef parentProp = (AdsParentPropertyDef) property;
                if (parentProp.getParentInfo().findOriginalProperty() != null) {
                    stateManager.ok();
                    return true;
                } else {
                    if (parentProp.getParentInfo().getOriginalPropertyId() != null) {
                        stateManager.error("Parent property not found");
                    } else {
                        stateManager.error("Parent property not specified");
                    }
                    return false;
                }
            case PROPERTY_PRESENTATION:
                final AdsPropertyPresentationPropertyDef propPresentation = (AdsPropertyPresentationPropertyDef) property;
                if (propPresentation.isLocal()) {
                    stateManager.ok();
                    return true;
                } else {
                    if (propPresentation.findServerSideProperty() != null) {
                        stateManager.ok();
                        return true;
                    } else {
                        stateManager.error("Server side property not found");
                        return false;
                    }
                }
            case DETAIL_PROP:
                if (property instanceof AdsDetailColumnPropertyDef) {
                    final AdsDetailColumnPropertyDef detailColumnProp = (AdsDetailColumnPropertyDef) property;
                    if (detailColumnProp.getColumnInfo().findColumn() != null) {
                        stateManager.ok();
                        return true;
                    } else {
                        if (detailColumnProp.getDetailReferenceInfo().getDetailReferenceId() != null) {
                            if (detailColumnProp.getDetailReferenceInfo().findDetailReference() == null) {
                                stateManager.error("Detail reference not found");
                                return false;
                            }
                        } else {
                            stateManager.error("Detail reference not specified");
                            return false;
                        }
                        if (detailColumnProp.getColumnInfo().getColumnId() != null) {
                            stateManager.error("Detail column property not found");
                            return false;
                        } else {
                            stateManager.error("Detail column not specified");
                        }
                        return false;
                    }

                } else if (property instanceof AdsDetailRefPropertyDef) {
                    final AdsDetailRefPropertyDef detailRefProp = (AdsDetailRefPropertyDef) property;
                    if (detailRefProp.getParentReferenceInfo().findParentReference() != null) {
                        stateManager.ok();
                        return true;
                    } else {
                        if (detailRefProp.getDetailReferenceInfo().getDetailReferenceId() != null) {
                            if (detailRefProp.getDetailReferenceInfo().findDetailReference() == null) {
                                stateManager.error("Detail reference not found");
                                return false;
                            }
                        } else {
                            stateManager.error("Detail reference not specified");
                            return false;
                        }
                        if (detailRefProp.getParentReferenceInfo().getParentReferenceId() != null) {
                            stateManager.error("Parent reference not found");
                        } else {
                            stateManager.error("Parent reference not specified");
                        }
                        return false;
                    }
                }
                break;
            case INNATE:
                if (property instanceof AdsInnateColumnPropertyDef) {
                    final AdsInnateColumnPropertyDef detailColumnProp = (AdsInnateColumnPropertyDef) property;
                    if (detailColumnProp.getColumnInfo().findColumn() != null) {
                        stateManager.ok();
                        return true;
                    } else {
                        if (detailColumnProp.getColumnInfo().getColumnId() != null) {
                            stateManager.error("Table column property not found");
                            return false;
                        } else {
                            stateManager.error("Table column not specified");
                        }
                        return false;
                    }

                } else if (property instanceof AdsInnateRefPropertyDef) {
                    final AdsInnateRefPropertyDef detailRefProp = (AdsInnateRefPropertyDef) property;
                    if (detailRefProp.getParentReferenceInfo().findParentReference() != null) {
                        stateManager.ok();
                        return true;
                    } else {
                        if (detailRefProp.getParentReferenceInfo().getParentReferenceId() != null) {
                            stateManager.error("Parent reference not found");
                        } else {
                            stateManager.error("Parent reference not specified");
                        }
                        return false;
                    }
                }

                break;
        }
        return false;
    }

    @Override
    protected void onChange() {
        final List<Definition> path = getLastSelectedDefinitionSequence();

        switch (property.getNature()) {
            case PARENT_PROP:
                if (!path.isEmpty()) {
                    final AdsParentPropertyDef parentProp = (AdsParentPropertyDef) property;
                    final ArrayList<Id> parentPath = new ArrayList<Id>();
                    for (int i = 0, len = path.size() - 1; i
                            < len; i++) {
                        parentPath.add(path.get(i).getId());
                    }

                    parentProp.getParentInfo().getParentPath().setRefPropIds(parentPath);
                    parentProp.getParentInfo().setOriginalPropertyId(getDefinitionId());
                }

                break;
            case DETAIL_PROP:
                if (path.size() == 2) {
                    if (property instanceof AdsDetailColumnPropertyDef) {
                        final AdsDetailColumnPropertyDef prop = (AdsDetailColumnPropertyDef) property;
                        prop.getDetailReferenceInfo().setDetailReferenceId(path.get(0).getId());
                        prop.getColumnInfo().setColumnId(path.get(1).getId());
                    } else if (property instanceof AdsDetailRefPropertyDef) {
                        final AdsDetailRefPropertyDef prop = (AdsDetailRefPropertyDef) property;
                        prop.getDetailReferenceInfo().setDetailReferenceId(path.get(0).getId());
                        prop.getParentReferenceInfo().setParentReferenceId(path.get(1).getId());
                    }

                }
                break;
            case INNATE:
                if (path.size() == 1) {
                    if (property instanceof AdsInnateColumnPropertyDef) {
                        final AdsInnateColumnPropertyDef prop = (AdsInnateColumnPropertyDef) property;
                        prop.getColumnInfo().setColumnId(path.get(0).getId());
                    } else if (property instanceof AdsInnateRefPropertyDef) {
                        final AdsInnateRefPropertyDef prop = (AdsInnateRefPropertyDef) property;
                        prop.getParentReferenceInfo().setParentReferenceId(path.get(0).getId());
                    }

                }
                break;
            case PROPERTY_PRESENTATION:
                if (path.size() == 1) {
                    final AdsPropertyPresentationPropertyDef propPresentation = (AdsPropertyPresentationPropertyDef) property;
                    propPresentation.setPublishedPropertyId(path.get(0).getId());
                }
                break;

        }
    }

    /**
     * Returns text for label attached to the component suffxed with ':'
     * according to currently opened object Example for Parent Propert will
     * return 'Parent Property:' IMPORTANT: Do not call this method before
     * {@linkplain  #open(org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef) open()}
     * method invoked
     */
    public String getLabelText() {
        switch (property.getNature()) {
            case DETAIL_PROP:

                if (property instanceof AdsDetailColumnPropertyDef) {
                    return "Detail Column:";
                } else {
                    return "Detail Reference:";
                }
            case INNATE:
                if (property instanceof AdsInnateColumnPropertyDef) {

                    return "Table Column:";
                } else {
                    return "Parent Reference:";
                }
            case PROPERTY_PRESENTATION:
                return "Published Property:";
            case PARENT_PROP:
                return "Parent Property:";
            default:
                return "<Unsupported Propetty Nature>";
        }
    }

    public String getAutoName() {
        final Definition def = getDefinition();
        if (def != null) {
            switch (property.getNature()) {
                case DETAIL_PROP:
                    if (property instanceof AdsDetailColumnPropertyDef) {
                        return def.getName();
                    } else {
                        assert def instanceof DdsReferenceDef;
                        final DdsTableDef table = ((DdsReferenceDef) def).findParentTable(property);
                        if (table != null) {
                            char[] content = table.getName().toCharArray();
                            content[0] = Character.toLowerCase(content[0]);

                            return String.valueOf(content);//RadixObjectsUtils.underlinedName2CamelCasedName(table.getDbName());tab
                        }
                        return def.getName();
                    }
                case INNATE:
                    if (property instanceof AdsInnateColumnPropertyDef) {
                        return def.getName();
                    } else {
                        assert def instanceof DdsReferenceDef;
                        final DdsTableDef table = ((DdsReferenceDef) def).findParentTable(property);
                        if (table != null) {
                            char[] content = table.getName().toCharArray();
                            content[0] = Character.toLowerCase(content[0]);

                            return String.valueOf(content);//RadixObjectsUtils.underlinedName2CamelCasedName(table.getDbName());tab
                        }
                        return def.getName();
                    }
                default:
                    return def.getName();
            }
        } else {
            return "";
        }
    }
}
