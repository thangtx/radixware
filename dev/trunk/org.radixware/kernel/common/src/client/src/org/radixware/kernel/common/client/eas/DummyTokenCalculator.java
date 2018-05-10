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

package org.radixware.kernel.common.client.eas;

import org.radixware.kernel.common.auth.PasswordHash;
import org.radixware.kernel.common.client.IClientEnvironment;


class DummyTokenCalculator implements ITokenCalculator {

    public final static DummyTokenCalculator INSTANCE = new DummyTokenCalculator();

    private DummyTokenCalculator() {
    }

    @Override
    public SecurityToken calcToken(final byte[] inToken) {
        return new SecurityToken(inToken);
    }

    @Override
    public byte[] createEncryptedHashForNewPassword(final PasswordHash newPwdHash) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public void dispose() {
    }

    @Override
    public ITokenCalculator copy(final IClientEnvironment environment) {
        return this;
    }
}
