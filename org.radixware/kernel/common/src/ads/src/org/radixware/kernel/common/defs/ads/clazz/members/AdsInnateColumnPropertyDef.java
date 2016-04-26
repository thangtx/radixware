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

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.IDependentId;
import org.radixware.kernel.common.defs.ads.clazz.AdsPropertyGroup;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;

/**
 * Property wrope for database table column
 */
public class AdsInnateColumnPropertyDef extends AdsTablePropertyDef implements ColumnProperty, IDependentId {

    public static final class Factory extends AdsPropertyDef.Factory {

        public static final AdsInnateColumnPropertyDef newInstance(DdsColumnDef column) {
            return new AdsInnateColumnPropertyDef(column);
        }

        public static final AdsInnateColumnPropertyDef newTemporaryInstance(AdsPropertyGroup context) {
            AdsInnateColumnPropertyDef c = newInstance(null);
            c.setContainer(context);
            return c;
        }
    }
    private final ColumnInfo columnInfo;

    protected AdsInnateColumnPropertyDef(DdsColumnDef column) {
        super(column == null ? Id.Factory.newInstance(EDefinitionIdPrefix.DDS_COLUMN) : column.getId(), column == null ? "newInnateProperty" : column.getName());
        this.columnInfo = new ColumnInfo(this, column);
    }

    protected AdsInnateColumnPropertyDef(AdsInnateColumnPropertyDef source, boolean forOverride) {
        super(source, forOverride);
        this.columnInfo = new ColumnInfo(this, source.columnInfo);
    }

    protected AdsInnateColumnPropertyDef(AbstractPropertyDefinition xProp) {
        super(xProp);
        this.columnInfo = new ColumnInfo(this, this.getId());
    }

    @Override
    protected AdsPropertyDef createOvr(boolean forOverride) {
        return new AdsInnateColumnPropertyDef(this, forOverride);
    }

    @Override
    public boolean isDescriptionInheritable() {
        return true;
    }

    @Override
    public Definition getDescriptionLocation(){
        Definition owner = super.getDescriptionLocation();
        if (isDescriptionInherited() && owner == this){
            if (!isOverride() && !isOverwrite()){
                DdsColumnDef column = columnInfo.findColumn();
                if (column != null){
                    return column;
                }
            }
        }
        return owner;
    }
    

    @Override
    public EPropNature getNature() {
        return EPropNature.INNATE;
    }

    @Override
    public ColumnInfo getColumnInfo() {
        return columnInfo;
    }

    boolean changeColumn(Id newColumnId) {
        if (getOwnerClass().getProperties().getLocal().findById(newColumnId) != null) {
            return false;
        }
        RadixObject container = getContainer();
        setContainer(null);
        setId(newColumnId);
        setContainer(container);
        return true;
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        DdsColumnDef column = this.columnInfo.findColumn();
        if (column != null) {
            list.add(column);
        }
    }

    @Override
    public String getTypeTitle() {
        return "Column-Based Property";
    }

    @Override
    public String getTypesTitle() {
        return "Column-Based Properties";
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        getColumnInfo().appendAdditionalToolTip(sb);
    }
//    /**
//     * Associates the property with table column
//     * @throws {@linkplain NullPointerException} if given column is null
//     * @throws {@linkplain DefinitionError} if property already has an owner
//     * @return <code>true</code> if property sucsessfully associated with column
//     */
//    public boolean associateWith(DdsColumnDef column) {
//        if (getOwner() != null) {
//            throw new DefinitionError("Property might be associated with column during initial setup process only.", this);
//        }
//        if (columnInfo.associateWith(column)) {
//            this.setId(column.getId());
//            return true;
//        } else {
//            return false;
//        }
//    }
//    @Override
//    public void afterCreateAndAppend() {
//        super.afterCreateAndAppend();
//        this.value.setType(columnInfo.calculatePropertyType());
//        setEditState(EEditState.MODIFIED);
//    }
}
