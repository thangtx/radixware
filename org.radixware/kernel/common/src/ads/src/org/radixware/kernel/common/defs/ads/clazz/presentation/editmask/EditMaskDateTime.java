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

import java.sql.Timestamp;
import java.util.Calendar;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EDateTimeStyle;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.Utils;


public class EditMaskDateTime extends EditMask {

    private String mask;
    private Calendar minValue;
    private Calendar maxValue;
    private EDateTimeStyle dateStyle;
    private EDateTimeStyle timeStyle;

    EditMaskDateTime(RadixObject context, org.radixware.schemas.editmask.EditMaskDateTime xDef, boolean virtual) {
        super(context, virtual);
        this.mask = xDef.isSetMask() ? xDef.getMask() : null;
        this.minValue = xDef.getMinValue();
        this.maxValue = xDef.getMaxValue();
        dateStyle = xDef.getDateStyle();
        timeStyle = xDef.getTimeStyle();

        if (dateStyle == null || timeStyle == null) {
            if (this.mask == null || this.mask.isEmpty()) {
                dateStyle = timeStyle = EDateTimeStyle.DEFAULT;
            } else {
                dateStyle = timeStyle = EDateTimeStyle.CUSTOM;
            }
        } 
    }

    EditMaskDateTime(RadixObject context, boolean virtual) {
        super(context, virtual);
        this.mask = "";
        this.minValue = null;
        this.maxValue = null;
        dateStyle = timeStyle = EDateTimeStyle.DEFAULT;
    }

    @Override
    public void appendTo(org.radixware.schemas.editmask.EditMask xDef) {
        org.radixware.schemas.editmask.EditMaskDateTime x = xDef.addNewDateTime();
        if (mask != null) {
            x.setMask(mask);
        }
        if (minValue != null) {
            x.setMinValue(minValue);
        }
        if (maxValue != null) {
            x.setMaxValue(maxValue);
        }
        if (dateStyle != null) {
            x.setDateStyle(dateStyle);
        }
        if (timeStyle != null) {
            x.setTimeStyle(timeStyle);
        }
    }

    @Override
    public boolean isCompatible(EValType valType) {
        return valType == EValType.DATE_TIME || valType == EValType.ARR_DATE_TIME;
    }

    @Override
    public EEditMaskType getType() {
        return EEditMaskType.DATE_TIME;
    }

    public String getMask() {
        return mask;
    }

    public Calendar getMinValue() {
        return minValue;
    }

    public Calendar getMaxValue() {
        return maxValue;
    }

    public Timestamp getMinValueStamp() {
        if (minValue == null) {
            return null;
        }
        return new Timestamp(minValue.getTimeInMillis());
    }

    public Timestamp getMaxValueStamp() {
        if (maxValue == null) {
            return null;
        }
        return new Timestamp(maxValue.getTimeInMillis());
    }

    public void setMask(String mask) {
        if (!Utils.equals(mask, this.mask)) {
            this.mask = mask;
            if (this.mask != null && !this.mask.isEmpty()) {
                this.dateStyle = this.timeStyle = EDateTimeStyle.CUSTOM;
            }
            modified();
        }
    }

    public EDateTimeStyle getDateStyle() {
        return dateStyle;
    }

    public void setDateStyle(EDateTimeStyle dateStyle) {
        if (this.dateStyle != dateStyle) {
            this.dateStyle = dateStyle;
            if (this.dateStyle != EDateTimeStyle.CUSTOM) {
                this.mask = null;
                if (this.timeStyle == EDateTimeStyle.CUSTOM) {
                    this.timeStyle = EDateTimeStyle.DEFAULT;
                }
            } else {
                this.timeStyle = EDateTimeStyle.CUSTOM;
            }
            modified();
        }
    }

    public EDateTimeStyle getTimeStyle() {
        return timeStyle;
    }

    public void setTimeStyle(EDateTimeStyle timeStyle) {
        if (this.timeStyle != timeStyle) {
            this.timeStyle = timeStyle;
            if (this.timeStyle != EDateTimeStyle.CUSTOM) {
                this.mask = null;
                if (this.dateStyle == EDateTimeStyle.CUSTOM) {
                    this.dateStyle = EDateTimeStyle.DEFAULT;
                }
            } else {
                this.dateStyle = EDateTimeStyle.CUSTOM;
            }

            modified();
        }
    }

    public void setMaxValue(Calendar maxValue) {
        this.maxValue = maxValue;
        modified();
    }

    public void setMinValue(Calendar minValue) {
        this.minValue = minValue;
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
}
