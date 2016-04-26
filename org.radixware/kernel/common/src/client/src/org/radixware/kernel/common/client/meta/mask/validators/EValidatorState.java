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

package org.radixware.kernel.common.client.meta.mask.validators;

import java.util.List;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;


public enum EValidatorState implements IKernelIntEnum{

    ACCEPTABLE(0,"Acceptable"),
    INTERMEDIATE(1,"Intermediate"),
    INVALID(2,"Invalid");


    private final Long val;
    private final String title;

    private EValidatorState(final long val, final String title){
        this.val = val;
        this.title = title;
    }

    public String getName() {
        return title;
    }

    public Long getValue() {
        return val;
    }

    public boolean isInDomain(Id id) {
        return false;
    }

    public boolean isInDomains(List<Id> ids) {
        return false;
    }

}
