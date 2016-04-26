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

import java.math.BigInteger;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.ETriadDelimeterType;
import org.radixware.kernel.common.enums.EValType;


public class EditMaskInt extends EditMask {

    private Long maxValue;
    private Byte minLength;
    private Long minValue;
    private byte numberBase;
    private String padChar;
    private long stepSize;
    private String triadDelimiter;
    private ETriadDelimeterType triadDelimeterType = ETriadDelimeterType.NONE;

    EditMaskInt(RadixObject context, boolean virtual) {
        super(context, virtual);
        numberBase = 10;
        stepSize = 1;
    }

    EditMaskInt(RadixObject context, org.radixware.schemas.editmask.EditMaskInt xDef, boolean virtual) {
        super(context, virtual);
        maxValue = xDef.isSetMaxValue() ? xDef.getMaxValue() : null;
        minLength = xDef.isSetMinLength() ? xDef.getMinLength() : null;
        minValue = xDef.isSetMinValue() ? xDef.getMinValue() : null;
        numberBase = xDef.getNumberBase();
        if (xDef.isSetPadChar()) {
            padChar = xDef.getPadChar();
        } else {
            padChar = null;
        }
        stepSize = xDef.getStepSize();
        triadDelimiter = xDef.isSetTriadDelimeter() ? xDef.getTriadDelimeter() : null;
        if (xDef.getTriadDelimeterType() != null) {
            triadDelimeterType = xDef.getTriadDelimeterType();
        } else {
            if (triadDelimiter != null && !triadDelimiter.isEmpty()) {
                triadDelimeterType = ETriadDelimeterType.SPECIFIED;
            } else {
                triadDelimeterType = ETriadDelimeterType.NONE;
            }
        }
    }

    @Override
    public void appendTo(org.radixware.schemas.editmask.EditMask xDef) {
        org.radixware.schemas.editmask.EditMaskInt x = xDef.addNewInt();
        if (maxValue != null) {
            x.setMaxValue(maxValue);
        }
        if (minLength != null) {
            x.setMinLength(minLength);
        }
        if (minValue != null) {
            x.setMinValue(minValue);
        }
        x.setNumberBase(numberBase);
        if (padChar != null) {
            x.setPadChar(padChar);
        }
        x.setStepSize(stepSize);
        if (triadDelimiter != null && !triadDelimiter.isEmpty()) {
            x.setTriadDelimeter(triadDelimiter);
            if (triadDelimeterType != null) {
                x.setTriadDelimeterType(triadDelimeterType);
            } else {
                x.setTriadDelimeterType(ETriadDelimeterType.NONE);
            }
        } else {
            if (triadDelimeterType != null && triadDelimeterType != ETriadDelimeterType.NONE) {
                x.setTriadDelimeterType(triadDelimeterType);
            }
        }
    }

    @Override
    public boolean isCompatible(EValType valType) {
        return valType == EValType.BOOL || valType == EValType.INT || valType == EValType.CHAR
                || valType == EValType.ARR_BOOL || valType == EValType.ARR_INT || valType == EValType.ARR_CHAR;
    }

    @Override
    public EEditMaskType getType() {
        return EEditMaskType.INT;
    }

    public Long getMaxValue() {
        return maxValue;
    }

    public Byte getMinLength() {
        return minLength;
    }

    public Long getMinValue() {
        return minValue;
    }

    public byte getNumberBase() {
        return numberBase;
    }

    public String getPadChar() {
        return padChar;
    }

    public long getStepSize() {
        return stepSize;
    }

    public String getTriadDelimiter() {
        return triadDelimiter;
    }

    public void setMaxValue(Long maxValue) {
        this.maxValue = maxValue;
        modified();
    }

    public void setMinLength(Byte minLength) {
        this.minLength = minLength;
        modified();
    }

    public void setMinValue(Long minValue) {
        this.minValue = minValue;
        modified();
    }

    public void setNumberBase(byte numberBase) {
        this.numberBase = numberBase;
        modified();

    }

    public void setPadChar(String padChar) {
        this.padChar = padChar;
        modified();
    }

    public void setStepSize(long stepSize) {
        this.stepSize = stepSize;
        modified();
    }

    public void setTriadDelimiter(String triadDelimiter) {
        this.triadDelimiter = triadDelimiter;
        modified();
    }

    public ETriadDelimeterType getTriadDelimeterType() {
        return triadDelimeterType;
    }

    public void setTriadDelimeterType(ETriadDelimeterType triadDelimeterType) {
        if (triadDelimeterType != this.triadDelimeterType && triadDelimeterType != null) {
            this.triadDelimeterType = triadDelimeterType;
            modified();
        }
    }

    @Override
    public void applyDbRestrictions() {
        Long dbMax = getDbMaxValue();
        if (dbMax != null) {
            setMaxValue(dbMax);
        }
        Long dbMin = getDbMinValue();
        if (dbMin != null) {
            setMinValue(dbMin);
        }
    }

    public Long getDbMaxValue() {
        int[] lp = getDbRestrictions();
        if (lp != null && lp[0] > 0) {
            char[] digits = new char[lp[0]];
            for (int i = 0; i < digits.length; i++) {
                digits[i] = '9';
            }
            BigInteger request = new BigInteger(String.valueOf(digits));
            BigInteger max_long = new BigInteger(String.valueOf(Long.MAX_VALUE));
            long result = request.min(max_long).longValue();
            return Long.valueOf(result);
        } else {
            return null;
        }
    }

    public Long getDbMinValue() {
        Long max = getDbMaxValue();
        if (max == null) {
            return null;
        } else {
            return Long.valueOf(-max.longValue());
        }
    }
}
