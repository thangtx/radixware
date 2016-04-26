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

import java.util.Collection;
import java.util.EnumSet;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;

import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef.MultilingualStringInfo;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EAccess;

import org.radixware.kernel.common.enums.ECompatibleTypesForBool;

import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;

public class EditMaskBool extends EditMask implements ILocalizedDef {

    private EnumSet<EValType> compatibleTypes = EnumSet.of(EValType.BOOL, EValType.ARR_BOOL,
            EValType.ARR_INT, EValType.INT, EValType.NUM, EValType.STR,
            EValType.ARR_STR, EValType.ARR_NUM);
    private String strTrueValue;
    private String strFalseValue;
    private ECompatibleTypesForBool valueType;
    private Id falseTitleId;
    private Id trueTitleId;
    private String trueTitle;
    private String falseTitle;
    private boolean valueTitleVisible;

    public EditMaskBool(RadixObject context, boolean virtual) {
        super(context, virtual);
        valueType = ECompatibleTypesForBool.DEFAULT;
        strTrueValue = "true";//by default
        falseTitle = "";
        trueTitle = "";
        strFalseValue = "false";//by default
        valueTitleVisible = false;
    }

    public EditMaskBool(RadixObject context, org.radixware.schemas.editmask.EditMaskBool xDef, boolean virtual) {
        super(context, virtual);
        strTrueValue = xDef.getTrueValue();
        strFalseValue = xDef.getFalseValue();
        valueType = xDef.getValueType();
        trueTitleId = xDef.getTrueTitleId();
        falseTitleId = xDef.getFalseTitleId();
        trueTitle = xDef.getTrueTitle();
        falseTitle = xDef.getFalseTitle();
        valueTitleVisible = xDef.getValueTitleVisible();
        //validateInitialValues();
    }

    public final void setFalseValue(String val) {
        this.strFalseValue = ValAsStr.toStr(val, getEvalType());
        modified();
    }

    public final void setTrueValue(String val) {
        this.strTrueValue = ValAsStr.toStr(val, getEvalType());
        modified();
    }

    private EValType getEvalType() {
        EValType type;
        switch (valueType) {
            case DEFAULT:
                type = EValType.BOOL;
                break;
            case INT:
                type = EValType.INT;
                break;
            case REALNUM:
                type = EValType.NUM;
                break;
            case STR:
                type = EValType.STR;
                break;
            default:
                type = EValType.BOOL;
                break;
        }
        return type;
    }

    public Object getTrueValue() {
        EValType type = getEvalType();
        Object val = null;
        if (isCompatible(type)) {
            switch (valueType) {
                case DEFAULT:
                    if ("1".equals(strTrueValue) || "true".equals(strTrueValue) || "Boolean.TRUE".equals(strTrueValue)) {
                        val = Boolean.TRUE;
                    }
                    break;
                case INT:
                    val = ValAsStr.fromStr(strTrueValue, EValType.INT);
                    break;
                case REALNUM:
                    val = ValAsStr.fromStr(strTrueValue, EValType.NUM);
                    break;
                case STR:
                    val = strTrueValue;
                    break;
                default:
                    val = ValAsStr.fromStr(strTrueValue, type);
            }
        } else {
            throw new IllegalArgumentException("Type " + type + " is not compatible to EditMaskBool.");
        }
        return val;
    }

    public Object getFalseValue() {
        EValType type = getEvalType();
        Object val = null;
        if (isCompatible(type)) {
            switch (valueType) {
                case DEFAULT:
                    if ("0".equals(strFalseValue) || "false".equals(strFalseValue) || "Boolean.FALSE".equals(strFalseValue)) {
                        val = Boolean.FALSE;
                    }
                    break;
                case INT:
                    val = ValAsStr.fromStr(strFalseValue, EValType.INT);
                    break;
                case REALNUM:
                    val = ValAsStr.fromStr(strFalseValue, EValType.NUM);
                    break;
                case STR:
                    val = strFalseValue;
                    break;
                default:
                    val = ValAsStr.fromStr(strFalseValue, type);
            }
        } else {
            throw new IllegalArgumentException("Type " + type + " is not compatible to EditMaskBool.");
        }
        return val;
    }

    @Override
    public void appendTo(org.radixware.schemas.editmask.EditMask xDef) {
        org.radixware.schemas.editmask.EditMaskBool x = xDef.addNewBoolean();

        if (strFalseValue != null && strTrueValue != null) {
            if (!strFalseValue.equals(strTrueValue)) {
                x.setFalseValue(strFalseValue);
                x.setTrueValue(strTrueValue);
            } else {
                throw new IllegalArgumentException("True value and false value must be different in EditMaskBool.");
            }
        } else {
            x.setFalseValue(strFalseValue);//exception will be already thrown if equal
            x.setTrueValue(strTrueValue);
        }

        if (valueType == null) {
            x.setValueType(ECompatibleTypesForBool.DEFAULT);
        } else {
            x.setValueType(valueType);
        }
        if (falseTitleId != null) {
            x.setTrueTitleId(trueTitleId);
        }
        if (trueTitleId != null) {
            x.setFalseTitleId(falseTitleId);
        }
        x.setValueTitleVisible(valueTitleVisible);
        x.setTrueTitle(trueTitle);
        x.setFalseTitle(falseTitle);
        if (trueTitleId != null) {
            x.setTrueTitleId(trueTitleId);
        }
        if (falseTitleId != null) {
            x.setFalseTitleId(falseTitleId);
        }

    }

    @Override
    public boolean isCompatible(EValType valType) {
        if (compatibleTypes.contains(valType)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public EEditMaskType getType() {
        return EEditMaskType.BOOL;
    }

    @Override
    public void applyDbRestrictions() {
        //ignore
    }

    @Override
    public boolean isDbRestrictionsAvailable() {
        return false;
    }

    public String getTrueValueAsStr() {
        return strTrueValue;
    }

    public String getFalseValueAsStr() {
        return strFalseValue;
    }

    public void setTrueTitle(final String title) {
        this.trueTitle = title;
        modified();
    }

    public void setFalseTitle(final String title) {
        this.falseTitle = title;
        modified();
    }

    public String getTrueTitle() {
        if (trueTitle == null) {
            if (trueTitleId != null) {
                return null;
            }
        }
        return trueTitle;
    }

    public String getFalseTitle() {
        if (falseTitle == null) {
            if (falseTitleId != null) {
                return null;
            }
        }
        return falseTitle;
    }

    public Id getTrueTitleId() {
        return trueTitleId;
    }

    public Id getFalseTitleId() {
        return falseTitleId;
    }

    public void setTrueTitleId(Id id) {
        if (id != trueTitleId) {
            trueTitleId = id;
            modified();
        }
    }

    public void setFalseTitleId(Id id) {
        if (id != falseTitleId) {
            falseTitleId = id;
            modified();
        }
    }

    public boolean isValueTitleVisible() {
        return this.valueTitleVisible;
    }

    public void setValueTitleVisible(boolean visible) {
        this.valueTitleVisible = visible;
        modified();
    }

    public ECompatibleTypesForBool getCompatibleType() {
        if (this.valueType != null) {
            return this.valueType;
        } else {
            this.valueType = ECompatibleTypesForBool.DEFAULT;
            return ECompatibleTypesForBool.DEFAULT;
        }
    }

    public void setCompatibleType(ECompatibleTypesForBool valType) {
        this.valueType = valType;
        modified();
    }

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        ids.add(new MultilingualStringInfo(this) {
            @Override
            public String getContextDescription() {
                return "Title for boolean edit mask true value.";
            }

            @Override
            public Id getId() {
                return trueTitleId;
            }

            @Override
            public EAccess getAccess() {
                AdsDefinition def = getOwnerDef();
                return def == null ? EAccess.DEFAULT : def.getAccessMode();
            }

            @Override
            public void updateId(Id newId) {
                trueTitleId = newId;
            }

            @Override
            public boolean isPublished() {
                AdsDefinition def = getOwnerDef();
                return def == null ? false : def.isPublished();
            }
        });
        ids.add(new MultilingualStringInfo(this) {
            @Override
            public String getContextDescription() {
                return "Title for boolean edit mask false value.";
            }

            @Override
            public Id getId() {
                return falseTitleId;
            }

            @Override
            public EAccess getAccess() {
                AdsDefinition def = getOwnerDef();
                return def == null ? EAccess.DEFAULT : def.getAccessMode();
            }

            @Override
            public void updateId(Id newId) {
                falseTitleId = newId;
            }

            @Override
            public boolean isPublished() {
                AdsDefinition def = getOwnerDef();
                return def == null ? false : def.isPublished();
            }
        });
    }

    @Override
    public AdsMultilingualStringDef findLocalizedString(Id stringId) {
        return getOwnerDef().findLocalizedString(stringId);
    }
}
