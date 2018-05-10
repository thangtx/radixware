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

package org.radixware.kernel.common.defs.ads.clazz.presentation.editmask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EEditMaskEnumCorrection;
import org.radixware.kernel.common.enums.EEditMaskEnumOrder;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ValTypes;


public class EditMaskEnum extends EditMask {

    private EEditMaskEnumCorrection correction;
    private EEditMaskEnumOrder orderBy;
    private ArrayList<Id> correctionItems = null;

    EditMaskEnum(RadixObject context, boolean virtual) {
        super(context, virtual);
        this.correction = EEditMaskEnumCorrection.NONE;
        this.orderBy = EEditMaskEnumOrder.BY_ORDER;
    }

    EditMaskEnum(RadixObject context, org.radixware.schemas.editmask.EditMaskEnum xDef, boolean virtual) {
        super(context, virtual);
        correction = xDef.getCorrection();
        if (correction == null) {
            correction = EEditMaskEnumCorrection.NONE;
        }
        if (xDef.isSetCorrectionItems()) {
            this.correctionItems = new ArrayList<Id>();
            correctionItems.addAll(xDef.getCorrectionItems());
        }
        orderBy = xDef.getOrderBy();
        if (orderBy == null) {
            orderBy = EEditMaskEnumOrder.BY_ORDER;
        }
    }

    @Override
    public void appendTo(org.radixware.schemas.editmask.EditMask xDef) {
        org.radixware.schemas.editmask.EditMaskEnum x = xDef.addNewEnum();
        if (correctionItems != null) {
            x.setCorrectionItems(new ArrayList<Id>(correctionItems));
        }
        x.setOrderBy(orderBy);
        x.setCorrection(correction);
    }

    @Override
    public boolean isCompatible(EValType valType) {
        return ValTypes.ENUM_ASSIGNABLE_TYPES.contains(valType);
    }

    @Override
    public EEditMaskType getType() {
        return EEditMaskType.ENUM;
    }

    public Id getEnumId() {
        AdsEnumDef enumDef = findEnum();
        if (enumDef != null) {
            return enumDef.getId();
        } else {
            return null;
        }
    }

    public AdsEnumDef findEnum() {
        AdsTypeDeclaration decl = null;
        Definition ownerDef = getOwnerDefinition();
        if (ownerDef instanceof AdsPropertyDef) {
            decl = ((AdsPropertyDef) ownerDef).getValue().getType();
        } else if (ownerDef instanceof AdsFilterDef.Parameter) {
            decl = ((AdsFilterDef.Parameter) ownerDef).getType();
        }
        if (decl != null) {
            AdsType type = decl.resolve((AdsDefinition) ownerDef).get();
            if (type instanceof AdsEnumType) {
                return ((AdsEnumType) type).getSource();
            }
        }
        return null;
    }

    public EEditMaskEnumCorrection getCorrection() {
        return correction;
    }

    public List<Id> getCorrectionItems() {
        if (correctionItems == null) {
            return Collections.emptyList();
        } else {
            return new ArrayList<Id>(correctionItems);
        }
    }

    public EEditMaskEnumOrder getOrderBy() {
        return orderBy;
    }

    public void setCorrection(EEditMaskEnumCorrection correction) {
        this.correction = correction;
    }

    public void setCorrectionItems(List<Id> correctionItems) {
        if (correctionItems == null || correctionItems.isEmpty()) {
            this.correctionItems = null;
        } else {
            this.correctionItems = new ArrayList<Id>(correctionItems);
        }
        modified();
    }

    public void setOrderBy(EEditMaskEnumOrder orderBy) {
        this.orderBy = orderBy;
        modified();
    }

    @Override
    public void applyDbRestrictions() {
        //ignore
    }

    @Override
    public boolean isDbRestrictionsAvailable() {
        return false;
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        if (correctionItems != null) {
            AdsEnumDef enumDef = findEnum();
            if (enumDef != null) {
                AdsEnumDef.Items items = enumDef.getItems();
                for (Id id : correctionItems) {
                    AdsEnumItemDef item = items.findById(id, ExtendableDefinitions.EScope.ALL).get();
                    if (item != null) {
                        list.add(item);
                    }
                }
            }
        }
    }
    
    
}
