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

package org.radixware.kernel.designer.common.dialogs.sqlscript;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.radixware.kernel.common.sqlscript.parser.SQLScriptValue;
import org.radixware.kernel.common.sqlscript.parser.SQLScriptValue.Type;
import org.radixware.kernel.common.sqlscript.parser.spi.SQLDialogHandler;
import org.radixware.kernel.common.utils.Reference;

import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class DesignerDialogHandler extends SQLDialogHandler {

    @Override
    public SQLScriptValue getScriptValue(final Type type, final String varType, final String defaultValue, final boolean prompt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean getLoginData(Reference<String> user, Reference<String> password) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public void printMessage(String prompt) {
        DialogUtils.messageInformation(prompt);
    }

    @SuppressWarnings("fallthrough")
    @Override
    public Action handleError(String msg, boolean askRetry, boolean canIgnore) {
        if (!askRetry && !canIgnore) {
            DialogUtils.messageError(msg);
            return Action.ABORT;
        }
        final List<String> options = new ArrayList<String>();
        options.add("Abort");
        if (askRetry) {
            options.add("Retry");
        }
        if (canIgnore) {
            options.add("Ignore");
        }
        final int result = JOptionPane.showOptionDialog(null, msg, "Error", 0, JOptionPane.ERROR_MESSAGE, null, options.toArray(), options.get(options.size() - 1));
        switch (result) {
            case 0:
                return Action.ABORT;
            case 1:
                if (askRetry) {
                    return Action.RETRY;
                }
                if (canIgnore) {
                    return Action.IGNORE;
                }
                throw new IllegalStateException("Illegal option selected");
            case 2:
                if (canIgnore && askRetry) {
                    return Action.IGNORE;
                }
            default:
                if (canIgnore) {
                    return Action.IGNORE;
                } else {
                    return Action.ABORT;
                }
        }
    }

    @Override
    public void showErrorMessage(Throwable exception) {
        DialogUtils.messageError(exception);
    }
}
