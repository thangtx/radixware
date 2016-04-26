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

/*
 * 10/4/11 5:42 PM
 */
package org.radixware.kernel.common.defs.ads.ui.enums;

import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.Id;


public enum EReactionToIntermediateInput implements UIEnum {

    NONE,
    SHOW_WARNING,
    DISCARD_INPUT;
    
    private final static String ENUM = "org.radixware.kernel.explorer.editors.valeditors.ValEditor.EReactionToIntermediateInput";

    @Override
    public String getQualifiedValue() {
        return ENUM + "." + getValue();
    }

    @Override
    public String getQualifiedEnum() {
        return ENUM;
    }

    @Override
    public String getName() {
        return toString();
    }

    @Override
    public String getValue() {
        return toString();
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }

    public static EReactionToIntermediateInput getForValue(final String value) {
        String v = value.startsWith(ENUM + ".") ? value.substring(ENUM.length() + 1) : value;
        for (EReactionToIntermediateInput val : EReactionToIntermediateInput.values()) {
            if (val.getValue().equals(v)) {
                return val;
            }
        }
        throw new NoConstItemWithSuchValueError("EReactionToIntermediateInput has no item with value: " + String.valueOf(value), value);
    }
}