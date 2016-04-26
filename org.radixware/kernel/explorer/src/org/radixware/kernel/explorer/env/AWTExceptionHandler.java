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

package org.radixware.kernel.explorer.env;

import org.radixware.kernel.common.client.exceptions.ClientException;

public class AWTExceptionHandler {

    public static void register() {
        System.setProperty("sun.awt.exception.handler", AWTExceptionHandler.class.getName());
    }

    public void handle(final Throwable ex) {
        final String message = Application.getInstance().getMessageProvider().translate("ExplorerError", "Exception occurred during event dispatching: %s\n%s");
        final String reason = ClientException.getExceptionReason(Application.getInstance().getMessageProvider(), ex);
        final String stack = ClientException.exceptionStackToString(ex);
        Application.getInstance().getTracer().error(String.format(message, reason, stack));
    }
}
