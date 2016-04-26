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

package org.radixware.kernel.common.defs.dds;

import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Utils;

/**
 * External table - link to {@link DdsTableDef} of current or dependent module.
 * Used to draw references on diagram.
 *
 */
public class DdsExtTableDef extends DdsDefinition implements IPlacementSupport {

    /**
     * @return source table name or source table identifier (as string) if
     * source table is not found.
     */
    @Override
    public String getName() {
        final DdsTableDef table = findTable();
        if (table != null) {
            return table.getName();
        } else {
            return String.valueOf(tableId);
        }
    }
    private Id tableId = null;

    /**
     * Get source table identifier.
     */
    public Id getTableId() {
        return tableId;
    }

    /**
     * Set source table identifier.
     */
    public void setTableId(Id tableId) {
        if (!Utils.equals(this.tableId, tableId)) {
            this.tableId = tableId;
            setEditState(EEditState.MODIFIED);
        }
    }
    private final DdsDefinitionPlacement placement;

    @Override
    public DdsDefinitionPlacement getPlacement() {
        return placement;
    }

    /**
     * Find source table.
     *
     * @return source table or null if not found.
     * @throws NullPointerException if external table is not in module.
     */
    public DdsTableDef findTable() {
        final DdsModule module = getModule();
        if (module != null) {
            return module.getDdsTableSearcher().findById(getTableId()).get();
        } else {
            return null;
        }
    }

    /**
     * Find source table.
     *
     * @return source table.
     * @throws NullPointerException if external table is not in module.
     * @throws DefinitionNotFoundError if not found.
     */
    public DdsTableDef getTable() {
        DdsTableDef table = findTable();
        if (table == null) {
            throw new DefinitionNotFoundError(tableId);
        } else {
            return table;
        }
    }

    /**
     * Use factory.
     */
    protected DdsExtTableDef() {
        super(EDefinitionIdPrefix.DDS_EXT_TABLE, "");
        this.placement = DdsDefinitionPlacement.Factory.newInstance(this);
    }

    protected DdsExtTableDef(DdsTableDef sourceTable) {
        this();
        this.tableId = sourceTable.getId();
    }

    /**
     * Use factory.
     */
    protected DdsExtTableDef(org.radixware.schemas.ddsdef.ExtTable xExtTable) {
        super(xExtTable);

        this.tableId = Id.Factory.loadFrom(xExtTable.getTableId());
        this.placement = DdsDefinitionPlacement.Factory.loadFrom(this, xExtTable.getPlacement());
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.EXT_TABLE;
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsExtTableDef newInstance() {
            return new DdsExtTableDef();
        }

        public static DdsExtTableDef newInstance(DdsTableDef sourceTable) {
            return new DdsExtTableDef(sourceTable);
        }

        public static DdsExtTableDef loadFrom(org.radixware.schemas.ddsdef.ExtTable xExtTable) {
            return new DdsExtTableDef(xExtTable);
        }
    }

    @Override
    public String toString() {
        return super.toString()
                + "; SourceTableId: " + String.valueOf(tableId);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        DdsTableDef table = findTable();
        if (table != null) {
            list.add(table);
        }
    }

    private class DdsExtTableClipboardSupport extends DdsClipboardSupport<DdsExtTableDef> {

        public DdsExtTableClipboardSupport() {
            super(DdsExtTableDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.ExtTable xExtTable = org.radixware.schemas.ddsdef.ExtTable.Factory.newInstance();
            DdsModelWriter.writeExtTable(DdsExtTableDef.this, xExtTable);
            return xExtTable;
        }

        @Override
        protected DdsExtTableDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.ExtTable xExtTable = (org.radixware.schemas.ddsdef.ExtTable) xmlObject;
            return DdsExtTableDef.Factory.loadFrom(xExtTable);
        }

        @Override
        protected Method getDecoderMethod() {
            try {
                return DdsExtTableDef.Factory.class.getDeclaredMethod("loadFrom", org.radixware.schemas.ddsdef.ExtTable.class);
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            return null;
        }
    }

    @Override
    public ClipboardSupport<? extends DdsExtTableDef> getClipboardSupport() {
        return new DdsExtTableClipboardSupport();
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        DdsTableDef table = findTable();
        if (table != null) {
            sb.append("<br>Source table: " + table.getQualifiedName());
        }
    }
    public static final String EXT_TABLE_TYPE_TITLE = "Shortcut to Table";
    public static final String EXT_TABLE_TYPES_TITLE = "Shortcuts to tables";

    @Override
    public String getTypeTitle() {
        return EXT_TABLE_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return EXT_TABLE_TYPES_TITLE;
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.CALC;
    }
}
