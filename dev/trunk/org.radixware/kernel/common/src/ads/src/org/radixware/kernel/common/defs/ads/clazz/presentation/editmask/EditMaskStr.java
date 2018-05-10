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

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditOptions;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.utils.Utils;


public class EditMaskStr extends EditMask {

    public enum ValidatorType {

        SIMPLE(0),
        INT(1),
        NUM(2),
        REGEX(3);
        private final int value;

        private ValidatorType(int value) {
            this.value = value;
        }

        static ValidatorType getForValue(int value) {
            switch (value) {
                case 0:
                    return SIMPLE;
                case 1:
                    return INT;
                case 2:
                    return NUM;
                case 3:
                    return REGEX;
                default:
                    throw new NoConstItemWithSuchValueError("Unknown validator type value", value);

            }
        }
    }

    public abstract class ValidatorDef {

        public abstract ValidatorType getType();

        public abstract void appendTo(org.radixware.schemas.editmask.EditMaskStr xDef);

        public abstract ValidatorDef copy();
    }

    public class DefaultValidatorDef extends ValidatorDef {

        private String mask;
        private boolean noBlanckChar;
        private boolean keepSeparators = true;

        DefaultValidatorDef(org.radixware.schemas.editmask.EditMaskStr xDef) {
            mask = xDef.getMask();

            noBlanckChar = xDef.getNoBlankCharacter();
            if (xDef.isSetKeepSeparators()) {
                keepSeparators = xDef.getKeepSeparators();
            }
        }

        DefaultValidatorDef() {
            mask = "";

            noBlanckChar = false;

            keepSeparators = true;
        }

        @Override
        public ValidatorDef copy() {
            DefaultValidatorDef def = new DefaultValidatorDef();
            def.mask = mask;
            def.noBlanckChar = noBlanckChar;
            def.keepSeparators = keepSeparators;
            return def;
        }

        public boolean isKeepSeparators() {
            return keepSeparators;
        }

        public void setKeepSeparators(boolean keepSeparators) {
            if (this.keepSeparators != keepSeparators) {
                this.keepSeparators = keepSeparators;
                setEditState(EEditState.MODIFIED);
            }
        }

        public String getMask() {
            return mask;
        }

        public void setMask(String mask) {
            if (!Utils.equals(this.mask, mask)) {
                this.mask = mask;
                setEditState(EEditState.MODIFIED);
            }
        }

        public boolean isNoBlanckChar() {
            return noBlanckChar;
        }

        public void setNoBlanckChar(boolean noBlanckChar) {
            if (noBlanckChar != this.noBlanckChar) {
                this.noBlanckChar = noBlanckChar;
                setEditState(EEditState.MODIFIED);
            }
        }

        @Override
        public ValidatorType getType() {
            return ValidatorType.SIMPLE;
        }

        @Override
        public void appendTo(org.radixware.schemas.editmask.EditMaskStr xDef) {
            xDef.setMask(mask);
            xDef.setNoBlankCharacter(noBlanckChar);
            if (!keepSeparators) {
                xDef.setKeepSeparators(keepSeparators);
            }
        }
    }

    public class IntValidatorDef extends ValidatorDef {

        private Long maxValue;
        private Long minValue;

        IntValidatorDef() {
        }

        @Override
        public ValidatorDef copy() {
            IntValidatorDef def = new IntValidatorDef();
            def.maxValue = maxValue == null ? null : Long.valueOf(maxValue.longValue());
            def.minValue = minValue == null ? null : Long.valueOf(minValue.longValue());
            return def;
        }

        IntValidatorDef(org.radixware.schemas.editmask.EditMaskStr xDef) {
            try {
                if (xDef.isSetMinValue()) {
                    minValue = new Long(xDef.getMinValue());
                }
            } catch (NumberFormatException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            try {
                if (xDef.isSetMaxValue()) {
                    maxValue = new Long(xDef.getMaxValue());
                }
            } catch (NumberFormatException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }

        public Long getMaxValue() {
            return maxValue;
        }

        public void setMaxValue(Long maxValue) {
            if (!Utils.equals(this.maxValue, maxValue)) {
                this.maxValue = maxValue;
                setEditState(EEditState.MODIFIED);
            }
        }

        public Long getMinValue() {
            return minValue;
        }

        public void setMinValue(Long minValue) {
            if (!Utils.equals(this.minValue, minValue)) {
                this.minValue = minValue;
                setEditState(EEditState.MODIFIED);
            }
        }

        @Override
        public ValidatorType getType() {
            return ValidatorType.INT;
        }

        @Override
        public void appendTo(org.radixware.schemas.editmask.EditMaskStr xDef) {
            if (minValue != null) {
                xDef.setMinValue(String.valueOf(minValue));
            }
            if (maxValue != null) {
                xDef.setMaxValue(String.valueOf(maxValue));
            }
        }
    }

    public class NumValidatorDef extends ValidatorDef {

        private BigDecimal maxValue;
        private BigDecimal minValue;
        private int precision = -1;

        NumValidatorDef(org.radixware.schemas.editmask.EditMaskStr xDef) {
            try {
                if (xDef.isSetMinValue()) {
                    minValue = new BigDecimal(xDef.getMinValue());
                }
            } catch (NumberFormatException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            try {
                if (xDef.isSetMaxValue()) {
                    maxValue = new BigDecimal(xDef.getMaxValue());
                }
            } catch (NumberFormatException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            if (xDef.isSetPrecision()) {
                this.precision = xDef.getPrecision();
            }
        }

        NumValidatorDef() {
        }

        @Override
        public ValidatorDef copy() {
            NumValidatorDef def = new NumValidatorDef();
            def.maxValue = maxValue == null ? null : new BigDecimal(maxValue.doubleValue());
            def.minValue = minValue == null ? null : new BigDecimal(minValue.doubleValue());
            def.precision = precision;
            return def;
        }

        public BigDecimal getMaxValue() {
            return maxValue;
        }

        public void setMaxValue(BigDecimal maxValue) {
            if (!Utils.equals(this.maxValue, maxValue)) {
                this.maxValue = maxValue;
                setEditState(EEditState.MODIFIED);
            }
        }

        public BigDecimal getMinValue() {
            return minValue;
        }

        public void setMinValue(BigDecimal minValue) {
            if (!Utils.equals(this.minValue, minValue)) {
                this.minValue = minValue;
                setEditState(EEditState.MODIFIED);
            }
        }

        public int getPrecision() {
            return precision;
        }

        public void setPrecision(int precision) {
            if (this.precision != precision) {
                this.precision = precision;
                setEditState(EEditState.MODIFIED);
            }
        }

        @Override
        public ValidatorType getType() {
            return ValidatorType.NUM;
        }

        @Override
        public void appendTo(org.radixware.schemas.editmask.EditMaskStr xDef) {
            if (minValue != null) {
                xDef.setMinValue(String.valueOf(minValue));
            }
            if (maxValue != null) {
                xDef.setMaxValue(String.valueOf(maxValue));
            }
            if (precision >= 0) {
                xDef.setPrecision(precision);
            }
        }
    }

    public class RegexValidatorDef extends ValidatorDef {

        private String regex = "";
        private boolean matchCase = true;

        public RegexValidatorDef(org.radixware.schemas.editmask.EditMaskStr xDef) {
            if (xDef.isSetMask()) {
                regex = xDef.getMask();
            } else {
                regex = "";
            }
            if (xDef.isSetCaseSensitive()) {
                matchCase = xDef.getCaseSensitive();
            }
        }

        RegexValidatorDef() {
        }

        public boolean isMatchCase() {
            return matchCase;
        }

        @Override
        public ValidatorDef copy() {
            RegexValidatorDef def = new RegexValidatorDef();
            def.matchCase = matchCase;
            def.regex = regex;
            return def;
        }

        public void setMatchCase(boolean matchCase) {
            if (matchCase != this.matchCase) {
                this.matchCase = matchCase;
                setEditState(EEditState.MODIFIED);
            }
        }

        public String getRegex() {
            return regex;
        }

        public void setRegex(String regex) {
            if (!Utils.equals(regex, this.regex)) {
                this.regex = regex;
                setEditState(EEditState.MODIFIED);
            }
        }

        @Override
        public ValidatorType getType() {
            return ValidatorType.REGEX;
        }

        @Override
        public void appendTo(org.radixware.schemas.editmask.EditMaskStr xDef) {
            if (regex != null) {
                xDef.setMask(regex);
            }
            if (!matchCase) {
                xDef.setCaseSensitive(false);
            }
        }
    }
    private boolean isPassword;
    private Integer maxLen;
    private boolean allowEmptyString = true;
    private ValidatorDef validator;

    EditMaskStr(RadixObject context, boolean virtual) {
        super(context, virtual);
        if (context instanceof EditOptions) {
            EditOptions opts = (EditOptions) context;
            if (opts.isNotNull()) {
                allowEmptyString = false;
            }
        }
        this.validator = new DefaultValidatorDef();
    }

    EditMaskStr(RadixObject context, org.radixware.schemas.editmask.EditMaskStr xDef, boolean virtual) {
        super(context, virtual);
        isPassword = xDef.getIsPassword();
        maxLen = xDef.isSetMaxLength() ? xDef.getMaxLength() : null;
        if (xDef.isSetAllowEmptyString()) {
            allowEmptyString = xDef.getAllowEmptyString();
        }
        if (xDef.isSetValidatorType()) {
            ValidatorType type = ValidatorType.getForValue(xDef.getValidatorType());
            switch (type) {
                case SIMPLE:
                    validator = new DefaultValidatorDef(xDef);
                    break;
                case INT:
                    validator = new IntValidatorDef(xDef);
                    break;
                case NUM:
                    validator = new NumValidatorDef(xDef);
                    break;
                case REGEX:
                    validator = new RegexValidatorDef(xDef);
                    break;
            }
        } else {
            validator = new DefaultValidatorDef(xDef);
        }
    }

    public void setValidator(ValidatorDef validator) {
        this.validator = validator.copy();
        setEditState(EEditState.MODIFIED);
    }

    @Override
    public void appendTo(org.radixware.schemas.editmask.EditMask xDef) {
        org.radixware.schemas.editmask.EditMaskStr x = xDef.addNewStr();
        x.setIsPassword(isPassword);

        if (maxLen != null) {
            x.setMaxLength(maxLen);
        }

        if (!allowEmptyString) {
            x.setAllowEmptyString(false);
        }
        if (validator.getType() != ValidatorType.SIMPLE) {
            x.setValidatorType(validator.getType().value);
        }
        validator.appendTo(x);
    }

    @Override
    public boolean isCompatible(EValType valType) {
        return (valType == EValType.BLOB || valType == EValType.CLOB || valType == EValType.BIN || valType == EValType.STR
                || valType == EValType.ARR_BLOB || valType == EValType.ARR_CLOB || valType == EValType.ARR_BIN || valType == EValType.ARR_STR);
    }

    @Override
    public EEditMaskType getType() {
        return EEditMaskType.STR;
    }

    public boolean isPassword() {
        return isPassword;
    }

    public Integer getMaxLen() {
        return maxLen;
    }

    public void setIsPassword(boolean isPassword) {
        this.isPassword = isPassword;
        modified();
    }

    public void setMaxLen(Integer maxLen) {
        this.maxLen = maxLen;
        modified();
    }

    public ValidatorDef getValidator() {
        return validator;
    }

    public void setValidatorType(ValidatorType type) {
        if (this.validator.getType() != type && type != null) {
            switch (type) {
                case INT:
                    this.validator = new IntValidatorDef();
                    break;
                case NUM:
                    this.validator = new NumValidatorDef();
                    break;
                case REGEX:
                    this.validator = new RegexValidatorDef();
                    break;
                default:
                    this.validator = new DefaultValidatorDef();
            }
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void applyDbRestrictions() {
        Integer dbMaxLen = getDbMaxLen();

        if (dbMaxLen != null) {
            this.setMaxLen(dbMaxLen > 1000 ? 1000 : dbMaxLen);
        }

        this.setAllowEmptyString(isDbAllowEmptyString());
    }

    public boolean isAllowEmptyString() {
        return allowEmptyString;
    }

    public void setAllowEmptyString(boolean allowEmptyString) {
        if (this.allowEmptyString != allowEmptyString) {
            this.allowEmptyString = allowEmptyString;
            modified();
        }
    }

    public boolean isDbAllowEmptyString() {
        DdsColumnDef col = findDdsColumn();
        if (col != null) {
            if (col.isNotNull()) {
                return false;
            }
        }
        return true;
    }

    public Integer getDbMaxLen() {
        int[] lp = getDbRestrictions();
        if (lp != null) {
            return Integer.valueOf(lp[0]);
        } else {
            return null;
        }

    }
}
