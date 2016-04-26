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

package org.radixware.kernel.common.client.exceptions;

import org.radixware.kernel.common.client.localization.MessageProvider;

public final class NoInstantiatableClassesException extends RuntimeException {

    static final long serialVersionUID = -9121327292473118364L;
    private MessageProvider mp;

    public NoInstantiatableClassesException(MessageProvider mp) {
        this.mp = mp;
    }

    @Override
    public String getMessage() {
        //return mp.translate("ExplorerError", "Can't determine class of object to be created");
        return mp.translate("ExplorerError", "It is impossible to create objects in this context");
    }
}