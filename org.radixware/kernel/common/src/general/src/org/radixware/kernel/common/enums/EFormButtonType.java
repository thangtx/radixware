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

public enum EFormButtonType implements IKernelStrEnum {

    ABORT("Abort"),
    ALL("All"),
    CANCEL("Cancel"),
    CLOSE("Close"),
    HELP("Help"),
    IGNORE("Ignore"),
    NO("No"),
    OK("Ok"),
    OPEN("Open"),
    RETRY("Retry"),
    SAVE("Save"),
    SKIP("Skip"),
    YES("Yes"),
    NEXT("Next"),
    PREVIOUS("Previous"),
    EXECUTE("Execute");
    private String val;

    private EFormButtonType(final String val) {
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

    public static EFormButtonType getForValue(final String val) {
        for (EFormButtonType e : EFormButtonType.values()) {
            if (e.getValue().equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EDialogButtonType has no item with value: " + String.valueOf(val),val);

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
