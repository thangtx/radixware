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
import org.radixware.kernel.common.defs.ads.clazz.AdsPropertyGroup;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.localization.LocalizedDescribableRef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;
import org.radixware.schemas.adsdef.PropertyDefinition;

/**
 * Property wrope for database column of child table of database reference
 */
public class AdsDetailColumnPropertyDef extends AdsTablePropertyDef implements DetailProperty, ColumnProperty {

    @Override
    protected AdsPropertyDef createOvr(boolean forOverride) {
        return new AdsDetailColumnPropertyDef(this, forOverride);
    }

    public static final class Factory {

        public static AdsDetailColumnPropertyDef newInstance(DdsReferenceDef ref, DdsColumnDef column) {
            return new AdsDetailColumnPropertyDef(ref, column);
        }

        public static AdsDetailColumnPropertyDef newTemporaryInstance(AdsPropertyGroup context) {
            AdsDetailColumnPropertyDef prop = new AdsDetailColumnPropertyDef(null, null);
            prop.setContainer(context);
            return prop;
        }
    }
    private final DetailReferenceInfo referenceInfo;
    private final ColumnInfo columnInfo;

    AdsDetailColumnPropertyDef(AbstractPropertyDefinition xProp) {
        super(xProp);
        this.referenceInfo = new DetailReferenceInfo(this, xProp);
        this.columnInfo = new ColumnInfo(this, xProp instanceof PropertyDefinition ? ((PropertyDefinition) xProp).getOriginalPropertyId() : null);
    }

    AdsDetailColumnPropertyDef(DdsReferenceDef ref, DdsColumnDef column) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.DDS_COLUMN), column == null ? "newDetailColumnProperty" : column.getName());
        this.referenceInfo = new DetailReferenceInfo(this, ref);
        this.columnInfo = new ColumnInfo(this, column);
    }

    AdsDetailColumnPropertyDef(AdsDetailColumnPropertyDef source, boolean forOverride) {
        super(source, forOverride);
        this.referenceInfo = new DetailReferenceInfo(this, source.referenceInfo);
        this.columnInfo = new ColumnInfo(this, source.columnInfo);
    }

    @Override
    public EPropNature getNature() {
        return EPropNature.DETAIL_PROP;
    }

    @Override
    public DetailReferenceInfo getDetailReferenceInfo() {
        return referenceInfo;
    }

    @Override
    public ColumnInfo getColumnInfo() {
        return columnInfo;
    }

    @Override
    public boolean isDescriptionInheritable() {
        return true;
    }
    
    @Override
    public Definition getDescriptionLocation(boolean inherited){
        Definition owner = super.getDescriptionLocation(inherited);
        if (inherited && owner == this) {
            DdsColumnDef column = columnInfo.findColumn();
            if (column != null) {
                describableRef = new LocalizedDescribableRef(column);
                return column;
            }
        }
        return owner;
    }

    @Override
    public void appendTo(PropertyDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        columnInfo.appendTo(xDef);
        referenceInfo.appendTo(xDef);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        DdsReferenceDef ref = this.referenceInfo.findDetailReference();
        if (ref != null) {
            list.add(ref);
            DdsColumnDef column = this.columnInfo.findColumn();
            if (column != null) {
                list.add(column);
            }
        }
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        getColumnInfo().appendAdditionalToolTip(sb);
        getDetailReferenceInfo().appendAdditionalToolTip(sb);
    }
}
