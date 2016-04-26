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

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;


public class EditMaskTimeInterval extends EditMask {

    public enum Scale {

        HOUR("Hour","ddd hh"),
        MINUTE("Minute","d hh:mm"),
        SECOND("Second","d hh:mm:ss"),
        MILLIS("Millisecond","d hh:mm:ss:zzz"),
        NONE("None","");
        final String name;
        final String defaultMask;

        Scale(String name, String mask) {
            this.name = name;
            defaultMask = mask;
        }

        @Override
        public String toString() {
            return name;
        }

        private static Scale fromXml(org.radixware.schemas.editmask.EditMaskTimeInterval.Scale.Enum xml) {
            if (xml.equals(org.radixware.schemas.editmask.EditMaskTimeInterval.Scale.HOUR)) {
                return HOUR;
            } else if (xml.equals(org.radixware.schemas.editmask.EditMaskTimeInterval.Scale.MILLIS)) {
                return MILLIS;
            } else if (xml.equals(org.radixware.schemas.editmask.EditMaskTimeInterval.Scale.MINUTE)) {
                return MINUTE;
            } else if (xml.equals(org.radixware.schemas.editmask.EditMaskTimeInterval.Scale.SECOND)) {
                return SECOND;
            } else {
                return NONE;
            }
        }

        public org.radixware.schemas.editmask.EditMaskTimeInterval.Scale.Enum toXml() {
            switch (this) {
                case HOUR:
                    return org.radixware.schemas.editmask.EditMaskTimeInterval.Scale.HOUR;
                case MINUTE:
                    return org.radixware.schemas.editmask.EditMaskTimeInterval.Scale.MINUTE;
                case SECOND:
                    return org.radixware.schemas.editmask.EditMaskTimeInterval.Scale.SECOND;
                case MILLIS:
                    return org.radixware.schemas.editmask.EditMaskTimeInterval.Scale.MILLIS;
                default:
                    return null;
            }
        }

        public long longValue() {
            switch (this) {
                case MILLIS:
                    return 1;
                case SECOND:
                    return 1000;
                case MINUTE:
                    return 60000;
                case HOUR:
                    return 3600000;
                default:
                    return 0;
            }
        }

        public String defaultMask(){
            return defaultMask;
        }
    }
    private String mask;
    private Long maxValue;
    private Long minValue;
    private Scale scale;

    EditMaskTimeInterval(RadixObject context, boolean virtual) {
        super(context, virtual);
        scale = Scale.NONE;
    }

    EditMaskTimeInterval(RadixObject context, org.radixware.schemas.editmask.EditMaskTimeInterval xDef, boolean virtual) {
        super(context, virtual);
        mask = xDef.isSetMask() ? xDef.getMask() : null;
        maxValue = xDef.isSetMaxValue() ? xDef.getMaxValue() : null;
        minValue = xDef.isSetMinValue() ? xDef.getMinValue() : null;
        scale = Scale.fromXml(xDef.getScale());
    }

    @Override
    public void appendTo(org.radixware.schemas.editmask.EditMask xDef) {
        org.radixware.schemas.editmask.EditMaskTimeInterval x = xDef.addNewTimeInterval();
        if (mask != null) {
            x.setMask(mask);
        }
        if (maxValue != null) {
            x.setMaxValue(maxValue);
        }
        if (minValue != null) {
            x.setMinValue(minValue);
        }
        if (scale.toXml() != null) {
            x.setScale(scale.toXml());
        }
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
        modified();
    }

    public Long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Long maxValue) {
        this.maxValue = maxValue;
        modified();
    }

    public Long getMinValue() {
        return minValue;
    }

    public void setMinValue(Long minValue) {
        this.minValue = minValue;
        modified();
    }

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
        modified();
    }

    @Override
    public boolean isCompatible(EValType valType) {
        if (valType != EValType.INT && valType != EValType.ARR_INT
                && valType != EValType.DATE_TIME && valType != EValType.ARR_DATE_TIME) {
            return false;
        }
        //if (valType == EValType.DATE_TIME || valType == EValType.ARR_DATE_TIME) {
//            return (scale == Scale.MILLIS);
//        }
        return true;
    }

    @Override
    public EEditMaskType getType() {
        return EEditMaskType.TIME_INTERVAL;
    }

    @Override
    public void applyDbRestrictions() {
        //ignore
    }

    @Override
    public boolean isDbRestrictionsAvailable() {
        return false;
    }
}
