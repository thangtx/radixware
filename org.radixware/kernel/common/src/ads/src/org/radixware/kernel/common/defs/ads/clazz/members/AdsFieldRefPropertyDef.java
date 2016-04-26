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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;
import org.radixware.schemas.adsdef.PropertyDefinition;


public class AdsFieldRefPropertyDef extends AdsFieldPropertyDef {

    public static final class Factory {

        private Factory() {
            super();
        }

        public static AdsFieldRefPropertyDef newInstance() {
            return new AdsFieldRefPropertyDef("newFieldRefProperty");
        }

        public static AdsFieldRefPropertyDef newTemporaryInstance(final RadixObject container) {
            final AdsFieldRefPropertyDef ref = new AdsFieldRefPropertyDef("newFieldRefProperty");
            ref.setContainer(container);
            return ref;
        }
    }

    public static class RefMapItem extends RadixObject {

        public static final class Factory {

            private Factory() {
                super();
            }

            public static RefMapItem newInstance() {
                return new RefMapItem();
            }
        }
        private Id columnId;
        private Id fieldId;

        protected RefMapItem(final PropertyDefinition.FieldToColumnMap.Item item) {
            this.columnId = Id.Factory.loadFrom(item.getColumnId());
            this.fieldId = Id.Factory.loadFrom(item.getFieldId());
        }

        protected RefMapItem() {
            this.columnId = null;
            this.fieldId = null;
        }

        public Id getColumnId() {
            return columnId;
        }

        public Id getFieldId() {
            return fieldId;
        }

        public void setColumnId(final Id id) {
            columnId = id;
            setEditState(EEditState.MODIFIED);
        }

        public void setFieldId(final Id id) {
            fieldId = id;
            setEditState(EEditState.MODIFIED);
        }
    }

    public static class FieldToColumnMap extends RadixObjects<RefMapItem> {

        private FieldToColumnMap(final AdsFieldRefPropertyDef owner, final PropertyDefinition.FieldToColumnMap map) {
            super(owner);
            if (map != null) {
                final List<PropertyDefinition.FieldToColumnMap.Item> xItems = map.getItemList();
                if (xItems != null && !xItems.isEmpty()) {
                    for (PropertyDefinition.FieldToColumnMap.Item item : xItems) {
                        add(new RefMapItem(item));
                    }
                }
            }
        }

        @Override
        public void collectDependences(final List<Definition> list) {
            final DdsTableDef table = ((AdsFieldRefPropertyDef) getOwnerDefinition()).findReferencedTable();
            if (table != null) {
                for (RefMapItem item : this) {
                    final DdsColumnDef column = table.getColumns().findById(item.getColumnId(), EScope.ALL).get();
                    if (column != null) {
                        list.add(column);
                    }
                }
            }
        }

        protected FieldToColumnMap(final AdsFieldRefPropertyDef owner) {
            super(owner);
        }

        @Override
        public String getName() {
            return "FieldToColumnMap";
        }

        public RefMapItem findItemByColumnId(Id id) {
            for (RefMapItem item : this) {
                if (item.getColumnId() == id) {
                    return item;
                }
            }
            return null;
        }

        private void appendTo(final PropertyDefinition.FieldToColumnMap xDef) {
            for (RefMapItem item : this) {
                final PropertyDefinition.FieldToColumnMap.Item xItem = xDef.addNewItem();
                if (item.getColumnId() != null) {
                    xItem.setColumnId(item.getColumnId().toString());
                }
                if (item.getFieldId() != null) {
                    xItem.setFieldId(item.getFieldId().toString());
                }
            }
        }
    }
    private final transient FieldToColumnMap fieldToColumnMap;

    protected AdsFieldRefPropertyDef(final AbstractPropertyDefinition xProp) {
        super(xProp);
        if (xProp instanceof PropertyDefinition) {
            this.fieldToColumnMap = new FieldToColumnMap(this, ((PropertyDefinition) xProp).getFieldToColumnMap());//NOPMD
        } else {
            this.fieldToColumnMap = new FieldToColumnMap(this);
        }
    }

    protected AdsFieldRefPropertyDef(final String name) {
        super(name);
        this.fieldToColumnMap = new FieldToColumnMap(this);
    }

    public FieldToColumnMap getFieldToColumnMap() {
        return fieldToColumnMap;
    }

    public DdsTableDef findReferencedTable() {
        final AdsType type = getValue().getType().resolve(this).get();
        if (type instanceof AdsClassType) {
            final AdsClassDef clazz = ((AdsClassType) type).getSource();
            if (clazz instanceof AdsEntityClassDef) {
                return ((AdsEntityClassDef) clazz).findTable(this);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void collectDependences(final List<Definition> list) {
        super.collectDependences(list);
        final DdsTableDef table = findReferencedTable();
        if (table != null) {
            list.add(table);
        }
    }

    @Override
    public void visitChildren(final IVisitor visitor, final VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        fieldToColumnMap.visit(visitor, provider);
    }

    @Override
    public void appendTo(final PropertyDefinition xDef, final ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (!fieldToColumnMap.isEmpty()) {
            fieldToColumnMap.appendTo(xDef.addNewFieldToColumnMap());
        }
    }

    @Override
    public EPropNature getNature() {
        return EPropNature.FIELD_REF;
    }

    @Override
    public boolean isTransfereble() {
        return true;
    }

    public DdsIndexDef findUsedIndex() {
        DdsTableDef table = findReferencedTable();
        if (table == null) {
            return null;
        }
        List<DdsIndexDef> indices = new LinkedList<>();
        indices.add(table.getPrimaryKey());
        for (DdsIndexDef index : table.getIndices().get(EScope.ALL)) {
            if (!indices.contains(index)) {
                indices.add(index);
            }
        }
        final Set<Id> columnIds = new HashSet<>();
        for (RefMapItem item : this.fieldToColumnMap) {
            columnIds.add(item.getColumnId());
        }        
        for (DdsIndexDef index : indices) {
            boolean completed = true;
            for (DdsIndexDef.ColumnInfo info : index.getColumnsInfo()) {
                if (!columnIds.contains(info.getColumnId())) {
                    completed = false;
                }
            }
            if (completed) {
                return index;
            }
        }
        return null;
    }
}
