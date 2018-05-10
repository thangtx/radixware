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
package org.radixware.kernel.server.exceptions;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.exceptions.RadixPrivateException;

public class PropNotLoadedException extends RadixPrivateException {

    private static final long serialVersionUID = -4536905888483198883L;
    public final Id propId;

    public PropNotLoadedException(final Id propId) {
        this("", propId);
    }

    public PropNotLoadedException(final String mess, final Id propId) {
        super(mess);
        this.propId = propId;
    }

    @Override
    public String getMessage() {
        final String superResult = super.getMessage();
        if (superResult != null && superResult.isEmpty()) {
            return "Property #" + propId + " is not loaded";
        }
        return superResult;
    }
}
