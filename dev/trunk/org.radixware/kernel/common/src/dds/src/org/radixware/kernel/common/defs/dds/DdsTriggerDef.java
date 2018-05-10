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
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IOverwritable;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectInitializationPolicy;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDocGroup;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.radixdoc.RadixdocUtils;

/**
 * Metainformation about trigger in database.
 *
 */
public class DdsTriggerDef extends DdsDefinition implements IDdsTableItemDef, IOverwritable {

    /**
     * Trigger type.
     */
    public enum EType {

        /**
         * For user properties.
         */
        FOR_USER_PROPS,
        /**
         * For audit.
         */
        FOR_AUDIT,
        /**
         * For usual purpouses.
         */
        NONE
    }

    /**
     * Trigger aAtiation time.
     */
    public enum EActuationTime {

        /**
         * Before ETriggeringEvent
         */
        BEFORE,
        /**
         * After ETriggeringEvent
         */
        AFTER,
        /**
         * Instead of ETriggeringEvent
         */
        INSTEAD_OF
    }

    /**
     * Trigger event.
     */
    public static enum ETriggeringEvent {

        /**
         * During row deletion.
         */
        ON_DELETE("D"),
        /**
         * During row insertion.
         */
        ON_INSERT("I"),
        /**
         * During row updating.
         */
        ON_UPDATE("U");
        private final String value;

        private ETriggeringEvent(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    private EActuationTime actuationTime = EActuationTime.AFTER;

    /**
     * @return trigger actiation time.
     */
    public EActuationTime getActuationTime() {
        return actuationTime;
    }

    public void setActuationTime(EActuationTime actuationTime) {
        if (!Utils.equals(this.actuationTime, actuationTime)) {
            this.actuationTime = actuationTime;
            setEditState(EEditState.MODIFIED);
        }
    }
    private final Sqml body = new DdsSqml(this);

    /**
     * @return trigger body
     */
    public Sqml getBody() {
        return body;
    }

    /**
     * Metatinformation about trigger column.
     */
    public static class ColumnInfo extends DdsColumnInfo {

        protected ColumnInfo() {
            super();
        }

        @Override
        protected DdsTableDef findTable() {
            DdsTriggerDef ownerTrigger = getOwnerTrigger();
            if (ownerTrigger != null) {
                return ownerTrigger.getOwnerTable();
            } else {
                return null;
            }
        }

        public DdsTriggerDef getOwnerTrigger() {
            for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
                if (owner instanceof DdsTriggerDef) {
                    return (DdsTriggerDef) owner;
                }
            }
            return null;
        }

        public static final class Factory {

            private Factory() {
            }

            public static ColumnInfo newInstance() {
                return new ColumnInfo();
            }
        }
    }

    public class ColumnsInfo extends RadixObjects<ColumnInfo> {

        public ColumnsInfo(DdsTriggerDef owner) {
            super(owner);
        }

        public ColumnInfo add(Id columnId) {
            ColumnInfo columnInfo = ColumnInfo.Factory.newInstance();
            columnInfo.setColumnId(columnId);
            add(columnInfo);
            return columnInfo;
        }

        public ColumnInfo add(DdsColumnDef column) {
            return add(column.getId());
        }
    }
    private final ColumnsInfo columnsInfo = new ColumnsInfo(this);

    /**
     * @return metainformation about trigger columns. required only for
     * ON_UPDATE triggers.
     */
    public ColumnsInfo getColumnsInfo() {
        return columnsInfo;
    }
    private String dbName = "";

    /**
     * @return trigger database name.
     */
    @Override
    public String getDbName() {
        return dbName;
    }

    @Override
    public void setDbName(String dbName) {
        if (!Utils.equals(this.dbName, dbName)) {
            this.dbName = dbName;
            this.setEditState(EEditState.MODIFIED);
        }
    }
    private final EnumSet<ETriggeringEvent> triggeringEvents = EnumSet.noneOf(ETriggeringEvent.class);

    /**
     * @return trigger events.
     */
    public EnumSet<ETriggeringEvent> getTriggeringEvents() {
        return triggeringEvents;
    }
    private EType type = EType.NONE;

    /**
     * @return trigger type.
     */
    public EType getType() {
        return type;
    }

    public void setType(EType type) {
        if (!Utils.equals(this.type, type)) {
            this.type = type;
            setEditState(EEditState.MODIFIED);
        }
    }
    boolean autoDbName = true;

    @Override
    public boolean isAutoDbName() {
        return autoDbName;
    }

    public void setAutoDbName(boolean autoDbName) {
        if (!Utils.equals(this.autoDbName, autoDbName)) {
            this.autoDbName = autoDbName;
            setEditState(EEditState.MODIFIED);
        }
    }
    private boolean disabled = false;

    /**
     * @return true if trigger generated as disabled during creation, false
     * otherwise.
     */
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        if (!Utils.equals(this.disabled, disabled)) {
            this.disabled = disabled;
            setEditState(EEditState.MODIFIED);
        }
    }
    private boolean forEachRow = true;

    /**
     * @return if trigger assigned for each row, false otherwise.
     * @see Oracle documentation.
     */
    public boolean isForEachRow() {
        return forEachRow;
    }

    public void setForEachRow(boolean forEachRow) {
        if (!Utils.equals(this.forEachRow, forEachRow)) {
            this.forEachRow = forEachRow;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.TRIGGER;
    }

    protected DdsTriggerDef(final String name) {
        super(EDefinitionIdPrefix.DDS_TRIGGER, name);
    }

    protected DdsTriggerDef(final Id id, final String name) {
        super(id, name);
    }

    protected DdsTriggerDef(org.radixware.schemas.ddsdef.Trigger xTrigger) {
        super(xTrigger);

        if (xTrigger.isSetIsOverwrite()) {
            overwrite = xTrigger.getIsOverwrite();
        }

        this.dbName = xTrigger.getDbName();

        if (xTrigger.isSetAutoDbName()) {
            this.autoDbName = xTrigger.getAutoDbName();
        }

        if (xTrigger.isSetType()) {
            final org.radixware.schemas.ddsdef.Trigger.Type.Enum xmlType = xTrigger.getType();
            this.type = DdsTriggerDef.EType.valueOf(xmlType.toString());
        }

        final org.radixware.schemas.ddsdef.Trigger.ActuationTime.Enum xmlActuationTime = xTrigger.getActuationTime();
        this.actuationTime = DdsTriggerDef.EActuationTime.valueOf(xmlActuationTime.toString());

        final long triggeringEventMask = xTrigger.getTriggeringEventMask();
        if ((triggeringEventMask & 1) != 0) {
            this.triggeringEvents.add(DdsTriggerDef.ETriggeringEvent.ON_DELETE);
        }
        if ((triggeringEventMask & 2) != 0) {
            this.triggeringEvents.add(DdsTriggerDef.ETriggeringEvent.ON_INSERT);
        }
        if ((triggeringEventMask & 4) != 0) {
            this.triggeringEvents.add(DdsTriggerDef.ETriggeringEvent.ON_UPDATE);
        }

        if (xTrigger.isSetColumnIds()) {
            for (Object columnIdAsStr : xTrigger.getColumnIds()) {
                Id columnId = Id.Factory.loadFrom((String) columnIdAsStr);
                ColumnInfo columnInfo = ColumnInfo.Factory.newInstance();
                columnInfo.setColumnId(columnId);
                this.columnsInfo.add(columnInfo);
            }
        }

        if (xTrigger.isSetForEachRow()) {
            this.forEachRow = xTrigger.getForEachRow();
        }

        if (xTrigger.isSetDisabled()) {
            this.disabled = xTrigger.getDisabled();
        }

        if (xTrigger.isSetDeprecated()) {
            isDeprecated = xTrigger.getDeprecated();
        }
        if (!RadixObjectInitializationPolicy.get().isRuntime()) {
            final org.radixware.schemas.xscml.Sqml xBody = xTrigger.getBody();
            this.body.loadFrom(xBody);
        }
    }
    private boolean isDeprecated = false;

    @Override
    public boolean isDeprecated() {
        return isDeprecated || super.isDeprecated();
    }

    public void setDeprecated(boolean isDeprecated) {
        this.isDeprecated = isDeprecated;
        setEditState(EEditState.MODIFIED);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(super.toString());
        result.append("; DbName: ");
        result.append(dbName);
        result.append("; ActuationTime: ");
        result.append(actuationTime);
        result.append("; ForEachRow: ");
        result.append(forEachRow);
        result.append("; TriggeringEvents: ");
        result.append(triggeringEvents);

        return result.toString();
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsTriggerDef newInstance(final String name) {
            return new DdsTriggerDef(name);
        }

        public static DdsTriggerDef newInstance(final Id id, final String name) {
            return new DdsTriggerDef(id, name);
        }

        public static DdsTriggerDef loadFrom(org.radixware.schemas.ddsdef.Trigger xTrigger) {
            return new DdsTriggerDef(xTrigger);
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        body.visit(visitor, provider);
        columnsInfo.visit(visitor, provider);
    }

    @Override
    public DdsTableDef getOwnerTable() {
        return (DdsTableDef) getOwnerDefinition();
    }

    @Override
    public String calcAutoDbName() {
        String prefix = "T";

        switch (getActuationTime()) {
            case AFTER:
                prefix += "A";
                break;
            case BEFORE:
                prefix += "B";
                break;
            case INSTEAD_OF:
                prefix += "I";
                break;
            default:
                throw new IllegalStateException();
        }

        if (this.getTriggeringEvents().contains(ETriggeringEvent.ON_INSERT)) {
            prefix += ETriggeringEvent.ON_INSERT.getValue();
        }
        if (this.getTriggeringEvents().contains(ETriggeringEvent.ON_DELETE)) {
            prefix += ETriggeringEvent.ON_DELETE.getValue();
        }
        if (this.getTriggeringEvents().contains(ETriggeringEvent.ON_UPDATE)) {
            prefix += ETriggeringEvent.ON_UPDATE.getValue();
        }

        if (this.isForEachRow()) {
            prefix += 'R';
        }

        DdsTableDef table = getOwnerTable();
        String tableDbName = table.getDbName();

        return DbNameUtils.calcAutoDbName(prefix, tableDbName);

    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        for (ColumnInfo info : getColumnsInfo()) {
            DdsColumnDef column = info.findColumn();
            if (column != null) {
                list.add(column);
            }
        }
    }

    private class DdsTriggerClipboardSupport extends DdsClipboardSupport<DdsTriggerDef> {

        public DdsTriggerClipboardSupport() {
            super(DdsTriggerDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.Trigger xTrigger = org.radixware.schemas.ddsdef.Trigger.Factory.newInstance();
            DdsModelWriter.writeTrigger(DdsTriggerDef.this, xTrigger);
            return xTrigger;
        }

        @Override
        protected DdsTriggerDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.Trigger xTrigger = (org.radixware.schemas.ddsdef.Trigger) xmlObject;
            return DdsTriggerDef.Factory.loadFrom(xTrigger);
        }

        @Override
        protected Method getDecoderMethod() {
            try {
                return DdsTriggerDef.Factory.class.getDeclaredMethod("loadFrom", org.radixware.schemas.ddsdef.Trigger.class);
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            return null;
        }
//
//        @Override
//        protected boolean isIdChangeRequired(RadixObject copyRoot) {
//            final DdsTriggerDef trigger = DdsTriggerDef.this;
//            if (trigger.getType() == EType.FOR_AUDIT) {
//                return false;
//            }
//            return super.isIdChangeRequired(copyRoot);
//        }
    }

    @Override
    public ClipboardSupport<? extends DdsTriggerDef> getClipboardSupport() {
        return new DdsTriggerClipboardSupport();
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        sb.append("<br>Database name: " + getDbName());
    }

    @Override
    public boolean isGeneratedInDb() {
        final DdsTableDef table = getOwnerTable();
        return table.isGeneratedInDb();
    }

    @Override
    public boolean isReadOnly() {
        if (super.isReadOnly()) {
            return true;
        }

        if (getType() != EType.NONE) {
            return true;
        }

        return false;
    }

    @Override
    public void afterOverwrite() {
        // NOTHING
    }

    @Override
    public boolean allowOverwrite() {
        return true;
    }
    private boolean overwrite = false;

    @Override
    public boolean setOverwrite(boolean overwrite) {
        if (!Utils.equals(this.overwrite, overwrite)) {
            this.overwrite = overwrite;
            setEditState(EEditState.MODIFIED);
            return true;
        }
        return false;
    }
    // switch to public access

    @Override
    public boolean isOverwrite() {
        return overwrite;
    }

    public DdsTriggerDef findOverwritten() {
        final DdsTableDef table = getOwnerTable();
        if (table == null) {
            return null;
        }
        final DdsTableDef overwrittenTable = table.findOverwritten();
        if (overwrittenTable == null) {
            return null;
        }
        return overwrittenTable.getTriggers().findById(getId(), EScope.ALL).get();
    }

    /**
     * Solves the problem of the nameless triggers
     *
     * @return presentable name of trigger
     */
    public String getPresentableName() {
        String name = getName();
        if (name == null || name.isEmpty()) {
            return "<no name>";
        }
        return name;
    }
//    private boolean isAuditTrigger() {
//        final DdsTableDef ownerTable = getOwnerTable();
//        if (ownerTable == null) {
//            return false;
//        }
//
//        final String idPart = getOwnerTable().getId().toString().substring(3);
//        return Utils.equals(dbName, "atu" + idPart)
//                || Utils.equals(dbName, "ati" + idPart)
//                || Utils.equals(dbName, "atd" + idPart);
//    }
//
//    private boolean isUserPropTrigger() {
//        final DdsTableDef ownerTable = getOwnerTable();
//        if (ownerTable == null) {
//            return false;
//        }
//
//        final String idPart = getOwnerTable().getId().toString().substring(3);
//
//        return Utils.equals(dbName, "UPS_" + idPart)
//                || Utils.equals(dbName, "UPF_" + idPart)
//                || Utils.equals(dbName, "UPO_" + idPart);
//    }

    @Override
    public EDocGroup getDocGroup() {
        return EDocGroup.TRIGGER;
    }

    @Override
    public boolean needsDocumentation() {
        return getType() != EType.FOR_AUDIT;
    }

    private static Definition descriptionLocationForAudit; // кэш getDescriptionLocationForAudit()
    private static boolean wasFindDescriptionLocationForAudit; // для кэша getDescriptionLocationForAudit()
    
    /** возращаем класс в котором находится описание аудит триггеров (этого класса может и не быть) */
    private Definition getDescriptionLocationForAudit() {
        if (!wasFindDescriptionLocationForAudit) {
            wasFindDescriptionLocationForAudit = true;
            descriptionLocationForAudit = RadixdocUtils.getRadixdocOwnerDef(getLayer());
        }
        return descriptionLocationForAudit;                    
    }
    
    @Override
    public Id getDescriptionId() {
        if (getType() == EType.FOR_AUDIT && getDescriptionLocationForAudit() != null) {
            return Id.Factory.loadFrom("mlsNDNKDELXRRF5LKEEN4SC35L4GI");
        } else {
            return super.getDescriptionId();
        }
    }

    @Override
    public Definition getDescriptionLocation() {
        Definition descriptionLocationForAudit = getDescriptionLocationForAudit();
        if (getType() == EType.FOR_AUDIT && descriptionLocationForAudit != null) {
            return descriptionLocationForAudit;
        } else {
            return super.getDescriptionLocation();
        }
    }
}
