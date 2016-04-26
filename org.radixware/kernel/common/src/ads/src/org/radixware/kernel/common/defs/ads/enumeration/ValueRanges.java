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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.schemas.adsdef.EnumDefinition;


public class ValueRanges extends RadixObjects<ValueRange> {

    private String regexp;

    ValueRanges(AdsEnumDef owner) {
        super(owner);
    }

    ValueRanges(AdsEnumDef owner, EnumDefinition.ValueRanges xDef) {
        this(owner);
        if (xDef != null) {
            if (xDef.getRangeList() != null) {
                for (EnumDefinition.ValueRanges.Range xRange : xDef.getRangeList()) {
                    add(new ValueRange(xRange));
                }
            }
            if (xDef.isSetPattern()) {
                regexp = xDef.getPattern().isEmpty() ? null : xDef.getPattern();
            }

        }
    }

    void appendTo(EnumDefinition xDef) {
        EnumDefinition.ValueRanges xRanges = xDef.addNewValueRanges();
        for (ValueRange range : this) {
            range.appendTo(xRanges.addNewRange());
        }
        if (regexp != null) {
            xRanges.setPattern(regexp);
        }
    }

    private AdsEnumDef getOwnerEnum() {
        return (AdsEnumDef) getContainer();
    }

    /**
     * @return true if value to ranges relationship is valid
     */
    public boolean checkValueAgainstRanges(ValAsStr value, String[] message, boolean checkOwnItem) {

        boolean atLeastOneRange = false;
        for (ValueRange range : this) {
            if (range.checkValueInRange(value)) {
                if (checkOwnItem) {
                    message[0] = " range '" + range.getFrom().toString() + "' - '" + range.getTo().toString() + "'";
                    return false;
                } else {
                    atLeastOneRange = true;
                }
            }
        }

        if (!isEmpty() && !checkOwnItem && !atLeastOneRange) {
            message[0] = " value ranges";
            return false;
        }

        if (regexp != null && !regexp.isEmpty()) {
            if (getOwnerEnum().getItemType() == EValType.STR) {
                final Object val = value.toObject(EValType.STR);
                if (val instanceof String) {
                    final String asStr = (String) val;
                    try {
                        if (Pattern.matches(regexp, asStr)) {
                            if (!checkOwnItem) {
                                message[0] = " regular expression";
                                return false;
                            }
                        } else {
                            if (checkOwnItem) {
                                message[0] = " regular expression";
                                return false;
                            }
                        }
                    } catch (PatternSyntaxException ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }
            }
        }
        return true;
    }

    public String getRegexp() {
        return regexp == null ? null : regexp;
    }

    public void setRegexp(String regexp) {
        if (regexp == null || regexp.isEmpty()) {
            this.regexp = null;
        } else {
            this.regexp = regexp;
        }
        setEditState(EEditState.MODIFIED);
    }
}
