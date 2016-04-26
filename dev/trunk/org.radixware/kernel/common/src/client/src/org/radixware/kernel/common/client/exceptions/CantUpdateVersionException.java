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

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.localization.MessageProvider;


public class CantUpdateVersionException extends RuntimeException implements IClientError {

    static final long serialVersionUID = -3813374675631531822L;
    private final String reason;

    public CantUpdateVersionException(final String reason) {
        this.reason = reason;
    }

    @Override
    public String getTitle(MessageProvider mp) {
        return mp.translate("ExplorerException", "Can\'t load new definitions");
    }

    @Override
    public String getLocalizedMessage() {
        return reason;
    }

    @Override
    public String getDetailMessage(MessageProvider mp) {
        return getTitle(mp) + ": " + getLocalizedMessage(mp);
    }

    @Override
    public String getLocalizedMessage(MessageProvider messageProvider) {
        return reason;
    }

    public final void showMessage(IClientEnvironment environment) {
        if (getLocalizedMessage() != null) {
            final String title = environment.getMessageProvider().translate("ExplorerMessage", "Can't Update Version");
            final String message = environment.getMessageProvider().translate("ExplorerMessage", "Can't update definitions version: \n%s");
            environment.messageInformation(title, String.format(message, getLocalizedMessage()));
        }
    }
}
