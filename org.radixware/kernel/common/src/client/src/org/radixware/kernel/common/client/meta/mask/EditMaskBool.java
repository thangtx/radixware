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
package org.radixware.kernel.common.client.meta.mask;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Objects;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.defs.value.ValAsStr;

import org.radixware.kernel.common.enums.ECompatibleTypesForBool;

import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.Id;

public class EditMaskBool extends org.radixware.kernel.common.client.meta.mask.EditMask {

    private static final EnumSet<EValType> SUPPORTED_VALTYPES
            = EnumSet.of(EValType.BOOL, EValType.ARR_BOOL, EValType.ARR_INT, EValType.INT,
                    EValType.NUM, EValType.STR, EValType.ARR_STR,
                    EValType.ARR_NUM);
    private String strTrueValue;
    private String strFalseValue;
    private ECompatibleTypesForBool valueType;
    private Id trueTitleId;
    private Id falseTitleId;
    private String trueTitle;
    private String falseTitle;
    private Id titleOwnerId;
    private boolean valueTitleVisible;

    public EditMaskBool() {
        this("true", "false",
                ECompatibleTypesForBool.DEFAULT, "", "", false);
    }

    protected EditMaskBool(final org.radixware.schemas.editmask.EditMaskBool editMask) {
        super();

        this.strTrueValue = editMask.getTrueValue();
        this.strFalseValue = editMask.getFalseValue();
        this.valueType = editMask.getValueType();
        this.titleOwnerId = editMask.getTitleOwnerId();
        this.trueTitle = editMask.getTrueTitle();        
        this.trueTitleId = editMask.getTrueTitleId();
        this.falseTitleId = editMask.getFalseTitleId();
        this.falseTitle = editMask.getFalseTitle();
        this.valueTitleVisible = editMask.getValueTitleVisible();

    }

    public EditMaskBool(final EditMaskBool source) {
        super();
        this.strTrueValue = source.strTrueValue;
        this.strFalseValue = source.strFalseValue;
        this.valueType = source.valueType;
        this.titleOwnerId = source.titleOwnerId;
        this.falseTitleId = source.falseTitleId;
        this.trueTitleId = source.trueTitleId;
        this.valueTitleVisible = source.valueTitleVisible;
        this.falseTitle = source.falseTitle;
        this.trueTitle = source.trueTitle;

    }

    public EditMaskBool(
            final String trueval,
            final String falseval,
            final ECompatibleTypesForBool valType,
            final String trueTitle,
            final String falseTitle,
            final boolean titleVisible) {
        super();

        this.strTrueValue = trueval;
        this.strFalseValue = falseval;
        this.valueType = valType;
        this.trueTitle = trueTitle;
        this.falseTitle = falseTitle;
        this.valueTitleVisible = titleVisible;

    }

    public EditMaskBool(
            final String trueval,
            final String falseval,
            final ECompatibleTypesForBool valType,
            final Id trueTitleId,
            final Id falseTitleId,
            final boolean titleVisible) {
        super();

        this.strTrueValue = trueval;
        this.strFalseValue = falseval;
        this.valueType = valType;
        this.trueTitleId = trueTitleId;
        this.falseTitleId = falseTitleId;
        this.valueTitleVisible = titleVisible;
    }

    public EditMaskBool(
            final String trueval,
            final String falseval,
            final ECompatibleTypesForBool valType,
            final String trueTitle,
            final String falseTitle,
            final Id titleOwnerId,
            final Id trueTitleId,
            final Id falseTitleId,
            final boolean titleVisible) {
        super();

        this.strTrueValue = trueval;
        this.strFalseValue = falseval;
        this.valueType = valType;
        this.trueTitleId = trueTitleId;
        this.falseTitleId = falseTitleId;
        this.trueTitle = trueTitle;
        this.falseTitle = falseTitle;
        this.titleOwnerId = titleOwnerId;
        this.valueTitleVisible = titleVisible;
    }

    public void setFalseValue(final String val) {
        strFalseValue = ValAsStr.toStr(val, getEvalType());
        afterModify();
    }

    public void setTrueValue(final String val) {
        strTrueValue = ValAsStr.toStr(val, getEvalType());
        afterModify();
    }

    private EValType getEvalType() {
        EValType type = EValType.BOOL;
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
        final EValType type = getEvalType();
        Object val = null;
        if (getSupportedValueTypes().contains(type)) {
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
                    break;
            }
        } else {
            throw new IllegalArgumentException("Type " + type + " is not compatible to EditMaskBool.");
        }
        return val;
    }

    public Object getFalseValue() {
        final EValType type = getEvalType();
        Object val = null;
        if (getSupportedValueTypes().contains(type)) {
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
                    break;
            }
        } else {
            throw new IllegalArgumentException("Type " + type + " is not compatible to EditMaskBool.");
        }
        return val;
    }

    public void setValueType(final ECompatibleTypesForBool valType) {
        this.valueType = valType;
        afterModify();
    }

    public ECompatibleTypesForBool getValueType() {
        return valueType;
    }

    public String getTrueValueAsStr() {
        return strTrueValue;
    }

    public String getFalseValueAsStr() {
        return strFalseValue;
    }

    public void setTrueTitle(final String title) {
        this.trueTitle = title;
        afterModify();
    }

    public void setFalseTitle(final String title) {
        this.falseTitle = title;
        afterModify();

    }

    public String getTrueTitle(final DefManager manager) {
        if (trueTitle == null && trueTitleId != null) {
            return manager.getMlStringValue(titleOwnerId, trueTitleId);
        }
        return trueTitle;
    }

    public String getFalseTitle(final DefManager manager) {
        if (falseTitle == null && falseTitleId != null) {
            return manager.getMlStringValue(titleOwnerId, falseTitleId);
        }
        return falseTitle;
    }

    public Id getTitleOwnerId() {
        return this.titleOwnerId;
    }

    public void setTitleOwnerId(final Id ownerId) {
        this.titleOwnerId = ownerId;
        afterModify();
    }

    public boolean getValueTitleVisible() {
        return valueTitleVisible;
    }

    public void setValueTitleVisible(final boolean visible) {
        this.valueTitleVisible = visible;
    }

    @Override
    public ValidationResult validate(final IClientEnvironment environment, final Object value) {
        if (value == null) {
            return ValidationResult.ACCEPTABLE;
        }
        if (value instanceof Arr) {
            return validateArray(environment, (Arr) value);
        }
        if (value instanceof Long || value instanceof Boolean || value instanceof String || value instanceof BigDecimal) {
            return ValidationResult.ACCEPTABLE;
        }
        if (!value.equals(getTrueValue()) && !value.equals(getFalseValue())) {
            return ValidationResult.ACCEPTABLE;
        }
        return ValidationResult.Factory.newInvalidResult(InvalidValueReason.Factory.createForInvalidValueType(environment));
    }

    @Override
    public String toStr(final IClientEnvironment environment, final Object o) {
        if (o == null) {
            return getNoValueStr(environment.getMessageProvider());
        } else if (o instanceof Arr) {
            if (o.equals(getTrueValue()) && trueTitle != null) {
                return trueTitle;
            } else if (o.equals(getFalseValue()) && falseTitle != null) {
                return falseTitle;
            }
            return arrToStr(environment, (Arr) o);
        } else if (o instanceof String) {
            if (valueTitleVisible && trueTitle != null) {
                if (o.equals("true")) {
                    return trueTitle;
                } else if (o.equals("false") && falseTitle != null) {
                    return falseTitle;
                }
            }
        }

        return o.toString();
    }

    @Override
    public EEditMaskType getType() {
        return EEditMaskType.BOOL;
    }

    @Override
    public EnumSet<EValType> getSupportedValueTypes() {
        return SUPPORTED_VALTYPES;
    }

    @Override
    public void writeToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        org.radixware.schemas.editmask.EditMaskBool editMaskBoolean = editMask.addNewBoolean();
        if (strFalseValue != null) {
            editMaskBoolean.setFalseValue(strFalseValue);
        }
        if (strTrueValue != null) {
            editMaskBoolean.setTrueValue(strTrueValue);
        }
        if (valueType != null) {
            editMaskBoolean.setValueType(this.valueType);
        }
        if (trueTitleId != null) {
            editMaskBoolean.setTrueTitleId(trueTitleId);
        }
        if (falseTitleId != null) {
            editMaskBoolean.setFalseTitleId(falseTitleId);
        }
        if (titleOwnerId != null) {
            editMaskBoolean.setTitleOwnerId(titleOwnerId);
        }
        editMaskBoolean.setTrueTitle(trueTitle);
        editMaskBoolean.setFalseTitle(falseTitle);
        editMaskBoolean.setValueTitleVisible(valueTitleVisible);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EditMaskBool other = (EditMaskBool) obj;

        if (!Objects.equals(this.strFalseValue, other.strFalseValue)) {
            return false;
        }
        if (!Objects.equals(this.strTrueValue, other.strTrueValue)) {
            return false;
        }
        if (!Objects.equals(this.valueType, other.valueType)) {
            return false;
        }
        if (!Objects.equals(this.titleOwnerId, other.titleOwnerId)) {
            return false;
        }        
        if (!Objects.equals(this.trueTitleId, other.trueTitleId)) {
            return false;
        }
        if (!Objects.equals(this.falseTitleId, other.falseTitleId)) {
            return false;
        }
        if (!Objects.equals(this.trueTitle, other.trueTitle)) {
            return false;
        }
        if (!Objects.equals(this.falseTitle, other.falseTitle)) {
            return false;
        }
        if (this.valueTitleVisible != other.valueTitleVisible) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.strTrueValue);
        hash = 29 * hash + Objects.hashCode(this.strFalseValue);
        hash = 29 * hash + Objects.hashCode(this.trueTitleId);
        hash = 29 * hash + Objects.hashCode(this.falseTitleId);
        hash = 29 * hash + Objects.hashCode(this.titleOwnerId);
        hash = 29 * hash + Objects.hashCode(this.trueTitle);
        hash = 29 * hash + Objects.hashCode(this.falseTitle);
        hash = 29 * hash + (this.valueTitleVisible ? 1 : 0);
        hash = 29 * hash + super.hashCode();
        return hash;
    }
}
