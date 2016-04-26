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

package org.radixware.kernel.common.defs.ads.clazz.members;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EPropInitializationPolicy;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.PropertyDefinition;
import org.radixware.schemas.adsdef.PropertyDefinition.ValInheritance;


public class ValueInheritanceRules extends RadixObject {

    public static final class Factory {

        public static final ValueInheritanceRules loadFrom(AdsPropertyDef context, PropertyDefinition.ValInheritance xInheritance, EPropInitializationPolicy initPolicy) {
            if (xInheritance == null) {
                if (context.getNature() == EPropNature.USER) {
                    return new ValueInheritanceRules(context, initPolicy);
                } else {
                    return new ValueInheritanceRules(context);
                }
            } else {

                return new ValueInheritanceRules(context, xInheritance.getMarkVal(), xInheritance);
            }
        }

        public static final ValueInheritanceRules newInstance(AdsPropertyDef context) {
            return new ValueInheritanceRules(context);
        }

        public static final ValueInheritanceRules newCopy(AdsPropertyDef context, ValueInheritanceRules source) {
            return new ValueInheritanceRules(context, source);
        }
    }
    private ValAsStr mark = null;
    private boolean inheritable = false;
    private List<InheritancePath> pathes = null;
    private EPropInitializationPolicy initPolicy = EPropInitializationPolicy.DEFAULT;

    public static class InheritancePath {

        public static final class Factory {

            public static final InheritancePath newInstance(AdsPropertyDef context) {
                return new InheritancePath(context);
            }
        }

        public class PathItem {

            public final Id referenceId;
            public final AdsPropertyDef property;
            public final AdsClassDef clazz;

            public PathItem(Id referenceId, AdsPropertyDef property, AdsClassDef clazz) {
                this.referenceId = referenceId;
                this.property = property;
                this.clazz = clazz;
            }
        }

        public class ResolvedPath {

            public final List<PathItem> items;
            public final boolean isResolved;
            public final AdsPropertyDef finalProperty;

            public ResolvedPath(List<PathItem> items, boolean isResolved, AdsPropertyDef finalProp) {
                this.items = new ArrayList<PathItem>(items);
                this.isResolved = isResolved;
                this.finalProperty = finalProp;
            }
        }
        private Id propertyId;
        private List<Id> referenceIds = null;
        private AdsPropertyDef ownerProperty;

        private InheritancePath(AdsPropertyDef ownerProperty, PropertyDefinition.ValInheritance.Pathes.Path path) {
            this.ownerProperty = ownerProperty;
            this.propertyId = path.getPropertyId();
            if (path.getReferences() != null && !path.getReferences().isEmpty()) {
                referenceIds = new ArrayList<Id>(path.getReferences());
            }
        }

        private InheritancePath(AdsPropertyDef ownerProperty) {
            this.ownerProperty = ownerProperty;
            this.propertyId = null;
            referenceIds = null;
        }

        public Id getPropertyId() {
            return propertyId;
        }

        public void setPropertyId(Id id) {
            this.propertyId = id;
            ownerProperty.setEditState(EEditState.MODIFIED);
        }

        public List<Id> getReferenceIds() {
            if (referenceIds == null) {
                return Collections.emptyList();
            } else {
                return new ArrayList<Id>(referenceIds);
            }
        }

        public void setReferenceIds(List<Id> ids) {
            if (ids == null || ids.isEmpty()) {
                referenceIds = null;
            } else {
                this.referenceIds = new ArrayList<Id>(ids);
            }
            ownerProperty.setEditState(EEditState.MODIFIED);
        }

        public void collectDependences(List<Definition> list) {
            ResolvedPath path = resolvePath(new ArrayList<DdsReferenceDef>(2));
            for (PathItem item : path.items) {
                if (item.property != null) {
                    list.add(item.property);
                }
            }
            if (path.finalProperty != null) {
                list.add(path.finalProperty);
            }
        }

        public ResolvedPath resolvePath(Collection<DdsReferenceDef> primaryKeyOccurences) {
            ArrayList<PathItem> infos = new ArrayList<PathItem>();
            boolean incomplete = false;
            AdsClassDef clazz = ownerProperty.getOwnerClass();
            final Id columnId;
            final Id tableId;
            if (ownerProperty instanceof ColumnProperty) {
                DdsColumnDef c = ((ColumnProperty) ownerProperty).getColumnInfo().findColumn();
                if (c != null) {
                    columnId = c.getId();
                    tableId = c.getOwnerTable().getId();
                } else {
                    columnId = null;
                    tableId = null;
                }
            } else {
                columnId = null;
                tableId = null;
            }
            if (referenceIds != null) {
                AdsClassDef context = clazz;
                if (clazz != null) {
                    for (Id id : referenceIds) {
                        AdsPropertyDef prop = clazz.getProperties().findById(id, EScope.ALL).get();
                        if (columnId != null && prop instanceof AdsParentRefPropertyDef) {
                            AdsParentRefPropertyDef ref = (AdsParentRefPropertyDef) prop;
                            DdsReferenceDef ddsRef = ref.getParentReferenceInfo().findParentReference();

                            if (ddsRef != null && ddsRef.getChildTableId() == tableId) {
                                for (DdsReferenceDef.ColumnsInfoItem item : ddsRef.getColumnsInfo()) {
                                    if (item.getChildColumnId() == columnId) {
                                        primaryKeyOccurences.add(ddsRef);
                                    }
                                }
                            }
                        }
                        if (prop == null) {
                            incomplete = true;
                            break;
                        }
                        infos.add(new PathItem(id, prop, clazz));

                        AdsType type = prop.getValue().getType().resolve(context).get();
                        if (type instanceof AdsClassType) {
                            clazz = ((AdsClassType) type).getSource();
                        }
                        if (clazz == null) {
                            incomplete = true;
                            break;
                        }
                    }
                }
            }
            AdsPropertyDef fp = null;
            if (!incomplete) {
                fp = clazz.getProperties().findById(propertyId, EScope.ALL).get();
                incomplete = fp == null;
            }
            return new ResolvedPath(infos, !incomplete, fp);
        }
    }

    private ValueInheritanceRules(AdsPropertyDef context) {
        setContainer(context);
    }

    private ValueInheritanceRules(AdsPropertyDef context, EPropInitializationPolicy policy) {
        setContainer(context);
        this.initPolicy = policy == null ? EPropInitializationPolicy.DEFAULT : policy;
    }

    private ValueInheritanceRules(AdsPropertyDef property, String mark) {
        this(property);
        this.inheritable = false;
        this.mark = mark == null ? null : ValAsStr.Factory.loadFrom(mark);
        this.initPolicy = EPropInitializationPolicy.DEFAULT;
    }

    private ValueInheritanceRules(AdsPropertyDef property, ValueInheritanceRules source) {
        this(property);
        this.inheritable = source.inheritable;
        this.initPolicy = source.getInitializationPolicy();
        this.mark = source.mark == null ? null : ValAsStr.Factory.loadFrom(source.mark.toString());
        this.pathes = source.pathes == null ? null : new ArrayList<InheritancePath>(source.pathes);
    }

    private ValueInheritanceRules(AdsPropertyDef property, String mark, PropertyDefinition.ValInheritance xInh) {
        this(property);
        this.inheritable = true;
        this.mark = ValAsStr.Factory.loadFrom(mark);
        if (xInh.isSetInitializationPolicy()) {
            this.initPolicy = xInh.getInitializationPolicy();
        }
        PropertyDefinition.ValInheritance.Pathes xPathes = xInh.getPathes();
        if (xPathes != null) {
            List<PropertyDefinition.ValInheritance.Pathes.Path> pathList = xPathes.getPathList();
            if (pathList != null && !pathList.isEmpty()) {
                pathes = new ArrayList<InheritancePath>();
                for (PropertyDefinition.ValInheritance.Pathes.Path path : pathList) {
                    pathes.add(new InheritancePath(property, path));
                }
            }
        }

    }

    public void appendTo(PropertyDefinition xDef) {
        if (inheritable) {
            ValInheritance xInh = xDef.addNewValInheritance();
            if (mark != null) {
                xInh.setMarkVal(mark.toString());
            }
            if (pathes != null && !pathes.isEmpty()) {
                ValInheritance.Pathes xPathes = xInh.addNewPathes();
                for (InheritancePath p : pathes) {
                    ValInheritance.Pathes.Path xPath = xPathes.addNewPath();
                    if (p.propertyId != null) {
                        xPath.setPropertyId(p.propertyId);
                    }
                    if (p.referenceIds != null) {
                        xPath.setReferences(p.referenceIds);
                    }
                }
            }
            if (initPolicy != null && initPolicy != EPropInitializationPolicy.DEFAULT) {
                xInh.setInitializationPolicy(initPolicy);
            }
        } else if (getOwnerProperty() != null && getOwnerProperty().getNature() == EPropNature.USER) {
            if (initPolicy != null && initPolicy != EPropInitializationPolicy.DEFAULT) {
                xDef.setInitializationPolicy(initPolicy);
            }
        }
    }

    public List<InheritancePath> getPathes() {
        if (pathes == null) {
            return Collections.emptyList();
        } else {
            return new ArrayList<InheritancePath>(pathes);
        }
    }

    public void addPath(InheritancePath path) {
        if (path.ownerProperty != this.getOwnerProperty()) {
            throw new RadixObjectError("Invalid property context");
        }
        if (pathes == null) {
            pathes = new ArrayList<InheritancePath>();
        }
        pathes.add(path);
        setEditState(EEditState.MODIFIED);
    }

    public void removePath(InheritancePath path) {
        if (pathes != null) {
            pathes.remove(path);
            if (pathes.isEmpty()) {
                pathes = null;
            }
        }
        setEditState(EEditState.MODIFIED);
    }

    public void movePathUp(InheritancePath path) {
        movePath(path, -1);
    }

    public void movePathDn(InheritancePath path) {
        movePath(path, 1);
    }

    private void movePath(InheritancePath path, int dir) {
        if (pathes == null) {
            return;

        }
        int index = pathes.indexOf(path);
        if (index < 0) {
            return;
        }

        int targetIndex = index + dir;

        if (targetIndex < 0 || targetIndex >= pathes.size()) {
            return;
        }

        InheritancePath other = pathes.get(targetIndex);

        pathes.set(index, other);
        pathes.set(targetIndex, path);
        setEditState(EEditState.MODIFIED);

    }

    public void clearPath() {
        pathes = null;
    }

    public boolean getInheritable() {
        return inheritable;
    }

    public boolean setInheritable(boolean inheritable) {
        this.inheritable = inheritable;
        setEditState(EEditState.MODIFIED);
        return true;
    }

    public ValAsStr getInheritanceMark() {
        return mark;
    }

    public boolean setInheritanceMark(ValAsStr mark) {
        this.mark = mark;
        setEditState(EEditState.MODIFIED);
        return true;
    }

    public AdsPropertyDef getOwnerProperty() {
        return (AdsPropertyDef) getContainer();
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        if (pathes != null) {
            for (InheritancePath path : pathes) {
                path.collectDependences(list);
            }
        }
    }

    public EPropInitializationPolicy getInitializationPolicy() {
        return initPolicy == null ? initPolicy.DEFAULT : initPolicy;
    }

    public void setInitializationPolicy(EPropInitializationPolicy policy) {
        initPolicy = policy;
        setEditState(EEditState.MODIFIED);
    }
}
