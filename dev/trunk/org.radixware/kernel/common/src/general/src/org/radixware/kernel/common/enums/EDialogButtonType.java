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

package org.radixware.kernel.common.enums;

import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;

public enum EDialogButtonType implements IKernelStrEnum {
//This enumeration field order is important,
    //it used to compute button order in dialog,
    //so do not change it without strong need

    OK("Ok"),
    YES("Yes"),
    NO("No"),
    OPEN("Open"),
    CLOSE("Close"),
    CANCEL("Cancel"),
    ABORT("Abort"),
    ALL("All"),
    HELP("Help"),
    IGNORE("Ignore"),
    NO_TO_ALL("No to All"),
    RETRY("Retry"),
    SAVE("Save"),
    SAVE_ALL("Save All"),
    SKIP("Skip"),
    YES_TO_ALL("Yes to All"),
    NEXT("Next"),
    REPORT("Report"),
    EXECUTE("Execute"),
    DISCARD("Discard"),
    APPLY("Apply"),
    RESET("Reset"),
    RESTORE_DEFAULTS("Restore Defaults"),
    BACK("Back"),
    NO_BUTTON("");
    private String val;

    private EDialogButtonType(final String val) {
        this.val = val;
    }

    @Override
    public String getValue() {
        return val;
    }

    @Override
    public String getName() {
        return null;
    }

    public static EDialogButtonType getForValue(final String val) {
        for (EDialogButtonType e : EDialogButtonType.values()) {
            if (e.getValue().equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EDialogButtonType has no item with value: " + String.valueOf(val), val);
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
