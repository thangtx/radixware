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

package org.radixware.kernel.designer.dds.editors.table;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.conventions.DbNameConventions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsExtTableDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.defs.dds.utils.DbTypeUtils;
import org.radixware.kernel.common.enums.EDeleteMode;
import org.radixware.kernel.common.enums.EOrder;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.types.Id;


class UseAsUserPropertiesObjectGenerator {

    private DdsColumnDef findOrCreateColumn(final DdsTableDef table, final String name, final int len) {
        final DdsColumnDef findColumn = table.getColumns().findByDbName(name.toUpperCase());
        if (findColumn != null) {
            return findColumn;
        }
        final DdsColumnDef column = DdsColumnDef.Factory.newInstance(name);
        column.setDbName(name.toUpperCase());
        column.setValType(EValType.STR);
        column.setLength(len);
        column.setNotNull(true);
        table.getColumns().getLocal().add(column);
        column.setDbType(DbTypeUtils.calcAutoDbType(EValType.STR, len, 1, null, table.getModule()));
        return column;
    }

    private DdsIndexDef findOrCreateIndex(final DdsTableDef table, final DdsColumnDef... cols) {

        final DdsIndexDef findIndex = (DdsIndexDef) table.getIndices().find(new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (radixObject instanceof DdsIndexDef) {
                    final DdsIndexDef idx = (DdsIndexDef) radixObject;

                    if (idx.getColumnsInfo() != null && idx.getColumnsInfo().size() == cols.length) {
                        for (int i = 0; i < cols.length; i++) {
                            if (!Objects.equals(idx.getColumnsInfo().get(i).getColumnId(), cols[i].getId())) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        if (findIndex != null) {
            return findIndex;
        }

        final DdsIndexDef index = DdsIndexDef.Factory.newInstance("UserPropertyObjectUniqueIndex");
        index.getDbOptions().add(DdsIndexDef.EDbOption.UNIQUE);
        index.setAutoDbName(true);
        for (final DdsColumnDef col : cols) {
            index.getColumnsInfo().add(col, EOrder.ASC);
        }
        table.getIndices().getLocal().add(index);
        DbNameUtils.updateAutoDbNames(index);

        return index;
    }
    public static final String RDX_UPVALREF = "RDX_UPVALREF";

    private DdsTableDef findUpValRef(final DdsTableDef source) {
        for (final Layer layer : source.getBranch().getLayers()) {
            if (Layer.ORG_RADIXWARE_LAYER_URI.equals(layer.getURI())) {
                final DdsSegment dds = (DdsSegment) layer.getDds();
                final DdsModule userProps = dds.getModules().findById(Id.Factory.loadFrom("mdl3646HMWKSNWDRKKZAIDCWI44IC"));
                if (userProps != null) {

                    try {
                        final DdsModelDef model = userProps.getModelManager().getModel();
                        return (DdsTableDef) model.getTables().find(new VisitorProvider() {
                            @Override
                            public boolean isTarget(RadixObject radixObject) {
                                if (radixObject instanceof DdsTableDef) {
                                    final DdsTableDef table = (DdsTableDef) radixObject;

                                    if (RDX_UPVALREF.equals(table.getDbName())) {
                                        return true;
                                    }
                                }
                                return false;
                            }
                        });
                    } catch (IOException ex) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    private DdsReferenceDef findOrCreateReference(final DdsTableDef parent, final DdsTableDef child, final DdsColumnDef... childCols) {

        final DdsReferenceDef findRef = (DdsReferenceDef) child.getOwnerModel().getReferences().find(new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (radixObject instanceof DdsReferenceDef) {
                    final DdsReferenceDef ref = (DdsReferenceDef) radixObject;
                    
                    if (!Objects.equals(ref.getChildTableId(), child.getId())) {
                        return false;
                    }
                    
                    if (!Objects.equals(ref.getParentTableId(), parent.getId())) {
                        return false;
                    }
                    
                    final List<DdsReferenceDef.ColumnsInfoItem> columnsInfo = ref.getColumnsInfo().list();
                    if (columnsInfo.size() != childCols.length) {
                        return false;
                    }
                    final List<DdsIndexDef.ColumnInfo> pkInfos = parent.getPrimaryKey().getColumnsInfo().list();
                    
                    for (int i = 0; i < childCols.length; i++) {
                        if (!Objects.equals(childCols[i].getId(), columnsInfo.get(i).getChildColumnId())) {
                            return false;
                        }

                        if (!Objects.equals(pkInfos.get(i).getColumnId(), columnsInfo.get(i).getParentColumnId())) {
                            return false;
                        }
                    }
                    
                    return true;
                    
                }
                return false;
            }
        });
        
        if (findRef != null) {
            return findRef;
        }

        final DdsReferenceDef ref = DdsReferenceDef.Factory.newInstance();
        ref.setAutoDbName(true);
        ref.setType(DdsReferenceDef.EType.LINK);
        ref.setDeleteMode(EDeleteMode.CASCADE);
        ref.setParentUnuqueConstraintId(parent.getPrimaryKey().getUniqueConstraint().getId());
        ref.setChildTableId(child.getId());
        ref.setParentTableId(parent.getId());

        final DdsExtTableDef extUpValRef = findOrCreateExtUpValRef(child, parent.getId());
        ref.setExtParentTableId(extUpValRef.getId());

        final List<DdsIndexDef.ColumnInfo> columnInfos = parent.getPrimaryKey().getColumnsInfo().list();
        for (int i = 0; i < columnInfos.size(); i++) {
            ref.getColumnsInfo().add(childCols[i], columnInfos.get(i).findColumn());
        }

        child.getOwnerModel().getReferences().add(ref);
        DbNameUtils.updateAutoDbNames(ref);
        return ref;
    }

    private DdsExtTableDef findOrCreateExtUpValRef(final DdsTableDef source, final Id upValRefId) {

        final DdsExtTableDef findExt = (DdsExtTableDef) source.getOwnerModel().getExtTables().find(new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (radixObject instanceof DdsExtTableDef) {
                    final DdsExtTableDef ext = (DdsExtTableDef) radixObject;
                    if (Objects.equals(ext.getTableId(), upValRefId)) {
                        return true;
                    }
                }
                return false;
            }
        });

        if (findExt != null) {
            return findExt;
        }

        final DdsExtTableDef extUpValRef = DdsExtTableDef.Factory.newInstance();
        extUpValRef.setTableId(upValRefId);

        source.getOwnerModel().getExtTables().add(extUpValRef);
        return extUpValRef;
    }

    void generate(final DdsTableDef table) {
        final DdsColumnDef upDefId = findOrCreateColumn(table, DbNameConventions.PROP_DEF_ID_DB_COL_NAME, 100);
        final DdsColumnDef upOwnerEntityId = findOrCreateColumn(table, DbNameConventions.VAL_OWNER_ENTITY_ID_DB_COL_NAME, 100);
        final DdsColumnDef upOwnerPid = findOrCreateColumn(table, DbNameConventions.VAL_OWNER_PID_DB_COL_NAME, 200);

        final DdsIndexDef index = findOrCreateIndex(table, upDefId, upOwnerEntityId, upOwnerPid);
        final DdsTableDef upValRef = findUpValRef(table);

        final DdsReferenceDef ref = findOrCreateReference(upValRef, table, upDefId, upOwnerEntityId, upOwnerPid);

        table.getModule().getDependences().actualize();
    }
}
