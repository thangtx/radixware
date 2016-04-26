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

package org.radixware.kernel.common.defs.ads.ui.enums;

import java.util.HashMap;
import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.Id;


public enum ESizePolicy implements UIEnum {

    Fixed("Fixed"),
    Minimum("Minimum"),
    Maximum("Maximum"),
    Preferred("Preferred"),
    MinimumExpanding("MinimumExpanding"),
    Expanding("Expanding"),
    Ignored("Ignored");

    private final static String PACKAGE = "com.trolltech.qt.gui.QSizePolicy.Policy";
    private final String value;

    private ESizePolicy(final String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getQualifiedValue() {
        return PACKAGE + "." + getValue();
    }

    @Override
    public String getQualifiedEnum() {
        return PACKAGE;
    }

    @Override
    public String getName() {
        return value;
    }

    public static ESizePolicy getForValue(final String value) {
        String v = value.startsWith(PACKAGE + ".") ? value.substring(PACKAGE.length() + 1) : value;
        for (ESizePolicy val: ESizePolicy.values()) {
            if (val.getValue().equals(v)) {
                return val;
            }
        }
        throw new NoConstItemWithSuchValueError("ESizePolicy has no item with value: " + String.valueOf(value),value);
    }

    private static HashMap<ESizePolicy, Integer> ws = new HashMap<ESizePolicy, Integer>();
    static {
        ws.put(Fixed, 0);
        ws.put(Maximum, 1);
        ws.put(Minimum, 2);
        ws.put(Preferred, 3);
        ws.put(Ignored, 4);
        ws.put(Expanding, 5);
        ws.put(MinimumExpanding, 6);
    }

    public static ESizePolicy max(ESizePolicy sp1, ESizePolicy sp2) {
        return ws.get(sp1) > ws.get(sp2) ? sp1 : sp2;
    }

    public static ESizePolicy min(ESizePolicy sp1, ESizePolicy sp2) {
        return ws.get(sp1) < ws.get(sp2) ? sp1 : sp2;
    }

    public static ESizePolicy grid_max(ESizePolicy sp1, ESizePolicy sp2) {
        ESizePolicy max = max(sp1, sp2);
        if (max.equals(Expanding) || max.equals(MinimumExpanding))
            return max;
        ESizePolicy min = min(sp1, sp2);
        if (min.equals(Fixed) || min.equals(Maximum))
            return min;
        return max;
    }
    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
}
