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

package org.radixware.kernel.common.client.errors;

import org.radixware.kernel.common.client.models.BrokenEntityModel;
import org.radixware.kernel.common.client.types.Pid;


public class UsageOfBrokenEntityError extends IllegalStateException {

    static final long serialVersionUID = -3124545459384125571L;
    private final Pid pid;

    public UsageOfBrokenEntityError(final BrokenEntityModel brokenEntity) {
        pid = brokenEntity.getPid() == null ? null : new Pid(brokenEntity.getPid());
    }

    public Pid getPid() {
        return pid;
    }
}
