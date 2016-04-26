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

package org.radixware.kernel.server.arte;

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.Id;


public abstract class LoadErrorsLog implements ILoadErrorsHandler {

    private static final boolean ALLOW_LOG_INSTEAD_OF_ERROR_ON_NOT_FOUND = System.getProperty("rdx.allow.skip.defnotfound.on.load") != null;
    //
    private final Map<Id, RadixError> loadErrors = new HashMap<>();
    private boolean tryLogInsteadOfExceptionOnNotFound = false;

    @Override
    public void registerLoadError(final Id defId, final RadixError err) {
        loadErrors.put(defId, err);
        logError(defId, err);
    }

    public void checkLoadError(final Id defId) throws RadixError {
        if (loadErrors != null) {
            final RadixError err = loadErrors.get(defId);
            if (err != null) {
                throw err;
            }
        }
    }

    public void setTryLogInsteadOfExceptionOnNotFound(boolean logInsteadOfExceptionOnNotFound) {
        this.tryLogInsteadOfExceptionOnNotFound = logInsteadOfExceptionOnNotFound;
    }
    
    @Override
    public void onDefNotFound(Id defId) {
        if (ALLOW_LOG_INSTEAD_OF_ERROR_ON_NOT_FOUND && tryLogInsteadOfExceptionOnNotFound) {
            logError(defId, new DefinitionNotFoundError(defId));
        } else {
            throw new DefinitionNotFoundError(defId);
        }
    }

    protected abstract void logError(final Id defId, final RadixError err);
}
