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

package org.radixware.kernel.common.defs.ads.enumeration;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.schemas.adsdef.EnumDefinition;


public class ValueRange extends RadixObject {

    private ValAsStr from = null;
    private ValAsStr to = null;

    private ValueRange() {
    }

    public ValueRange(ValAsStr from, ValAsStr to) {
        this.from = from;
        this.to = to;
    }

    ValueRange(EnumDefinition.ValueRanges.Range xDef) {
        if (xDef != null) {
            this.from = ValAsStr.Factory.loadFrom(xDef.getFrom());
            this.to = ValAsStr.Factory.loadFrom(xDef.getTo());
        }
    }

    public void setFrom(ValAsStr from) {
        this.from = from;
    }

    public void setTo(ValAsStr to) {
        this.to = to;
    }

    public ValAsStr getFrom() {
        return from;
    }

    public ValAsStr getTo() {
        return to;
    }

    void appendTo(EnumDefinition.ValueRanges.Range xDef) {
        if (this.from != null) {
            xDef.setFrom(this.from.toString());
        }
        if (this.to != null) {
            xDef.setTo(this.to.toString());
        }
    }

    private AdsEnumDef getOwnerEnum() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsEnumDef) {
                return (AdsEnumDef) owner;
            }
        }
        return null;
    }

    public boolean checkValueInRange(ValAsStr value) {
        Object fromObj = from != null ? from.toObject(getOwnerEnum().getItemType()) : null;
        Object toObj = to != null ? to.toObject(getOwnerEnum().getItemType()) : null;
        Object valObj = value != null ? value.toObject(getOwnerEnum().getItemType()) : null;
        switch (getOwnerEnum().getItemType()) {
            case INT:
                if (valObj instanceof Long) {
                    if (fromObj instanceof Long) {
                        if (((Long) valObj).longValue() < ((Long) fromObj).longValue()) {
                            return false;
                        }
                    }
                    if (toObj instanceof Long) {
                        if (((Long) valObj).longValue() > ((Long) toObj).longValue()) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            case CHAR:
                if (valObj instanceof Character) {
                    if (fromObj instanceof Character) {
                        if (((Character) valObj).charValue() < ((Character) fromObj).charValue()) {
                            return false;
                        }
                    }
                    if (toObj instanceof Character) {
                        if (((Character) valObj).charValue() > ((Character) toObj).charValue()) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    return false;
                }

            case STR:
                if (valObj instanceof String) {
                    if (fromObj instanceof String) {
                        if ((((String) valObj).compareTo((String) fromObj)) < 0) {
                            return false;
                        }
                    }
                    if (toObj instanceof String) {
                        if ((((String) valObj).compareTo((String) toObj)) > 0) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            default:
                return true;
        }
    }
}
