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
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.ETriadDelimeterType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.Utils;


public class EditMaskNum extends EditMask {

    private BigDecimal maxValue;
    private BigDecimal minValue;
    private Byte precision;
    private long scale;
    private String triadDelimiter;
    private ETriadDelimeterType triadDelimeterType = ETriadDelimeterType.DEFAULT;
    private String decimalDelimiter = null;

    EditMaskNum(RadixObject context, boolean virtual) {
        super(context, virtual);
        scale = 1;
    }

    EditMaskNum(RadixObject context, org.radixware.schemas.editmask.EditMaskNum xDef, boolean virtual) {
        super(context, virtual);
        maxValue = xDef.isSetMaxValue() ? xDef.getMaxValue() : null;
        minValue = xDef.isSetMinValue() ? xDef.getMinValue() : null;
        precision = xDef.isSetPrecision() ? xDef.getPrecision() : null;
        scale = xDef.getScale();
        triadDelimiter = xDef.isSetTriadDelimeter() ? xDef.getTriadDelimeter() : null;


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

        if (xDef.isSetDecimalDelimeter()) {
            decimalDelimiter = xDef.getDecimalDelimeter();
        }
    }

    @Override
    public void appendTo(org.radixware.schemas.editmask.EditMask xDef) {
        org.radixware.schemas.editmask.EditMaskNum x = xDef.addNewNum();
        if (maxValue != null) {
            x.setMaxValue(maxValue);
        }
        if (minValue != null) {
            x.setMinValue(minValue);
        }
        if (precision != null) {
            x.setPrecision(precision);
        }
        x.setScale(scale);
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
        if (decimalDelimiter != null) {
            x.setDecimalDelimeter(decimalDelimiter);
        }
    }

    @Override
    public boolean isCompatible(EValType valType) {
        return (valType == EValType.NUM || valType == EValType.ARR_NUM);
    }

    @Override
    public EEditMaskType getType() {
        return EEditMaskType.NUM;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public Byte getPrecision() {
        return precision;
    }

    public long getScale() {
        return scale;

    }

    public String getTriadDelimiter() {
        return triadDelimiter;
    }

    public void setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
        modified();
    }

    public void setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
        modified();

    }

    public void setPrecision(Byte precision) {
        this.precision = precision;
        modified();
    }

    public void setScale(long scale) {
        this.scale = scale;
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

    public String getDecimalDelimiter() {
        return decimalDelimiter;
    }

    public void setDecimalDelimiter(String decimalDelimiter) {
        if (!Utils.equals(decimalDelimiter, this.decimalDelimiter)) {
            this.decimalDelimiter = decimalDelimiter;
            modified();
        }
    }

    /**
     * Updates edit mask precision
     */
    @Override
    public void applyDbRestrictions() {
        int[] lp = getDbRestrictions();
        if (lp != null) {
            if (lp[1] <= 0) {
                this.setPrecision(null);
            } else {
                this.setPrecision((byte) lp[1]);
            }
            if (lp[0] <= 0) {
                this.setMaxValue(null);
                this.setMinValue(null);
            } else {
                String number = getDigets(lp);
                this.setMaxValue(new BigDecimal(number));
                this.setMinValue(new BigDecimal("-" + number));
            }
        }
    }

    public BigDecimal getDbMaxValue() {
        int[] lp = getDbRestrictions();
        if (lp != null) {
            if (lp[0] <= 0) {
                return null;
            }
            return new BigDecimal(getDigets(lp));
        } else {
            return null;
        }
    }
    
    private String getDigets(final int[] lp) {
        char[] digits;
        if (lp[1] > 0 && lp[1] < lp[0]) {
            digits = new char[lp[0] + 1];
            
            for (int i = 0; i < digits.length; i++) {
                if (i == (lp[0] - lp[1])) {
                    digits[i] = '.';
                } else {
                    digits[i] = '9';
                }
            }
        } else {
            digits = new char[lp[0]];

            for (int i = 0; i < digits.length; i++) {
                digits[i] = '9';
            }
        }
        
        return String.valueOf(digits);
    }

    public Byte getDbPrecision() {
        int[] lp = getDbRestrictions();
        if (lp != null) {
            return Byte.valueOf((byte) lp[1]);
        } else {
            return null;
        }
    }
}
