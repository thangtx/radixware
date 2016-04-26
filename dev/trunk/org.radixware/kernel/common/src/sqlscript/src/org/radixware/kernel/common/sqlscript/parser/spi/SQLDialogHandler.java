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

package org.radixware.kernel.common.sqlscript.parser.spi;

import org.radixware.kernel.common.sqlscript.parser.SQLScriptValue;
import org.radixware.kernel.common.sqlscript.parser.SQLScriptValue.Type;
import org.radixware.kernel.common.utils.Reference;


public abstract class SQLDialogHandler {

    public enum Action {

        RETRY, ABORT, IGNORE
    };    

    public abstract SQLScriptValue getScriptValue(Type type, String varType, String defaultValue, boolean prompt);

    public abstract boolean getLoginData(Reference<String> user, Reference<String> password);

    public abstract void printMessage(String prompt);
    
    public abstract void showErrorMessage(final Throwable exception);

    /**
     * Dialog with error message.
     * @param msg message to print
     * @param askRetry show retry button or not
     * @param canIgnore show ignore button or not
     * @return selected Action
     */
    public abstract Action handleError(String msg, boolean askRetry, boolean canIgnore);

    /**
     * This method calls {@link SQLDialogHandler#oracleHandleError(msg, true, true)}
     */
    public Action handleError(final String msg) {
        return handleError(msg, true, true);
    }
}
