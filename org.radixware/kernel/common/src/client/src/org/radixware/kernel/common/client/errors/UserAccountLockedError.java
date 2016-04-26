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

import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.exceptions.ServiceCallFault;


public class UserAccountLockedError  extends EasError implements IAlarm, IClientError{

    static final long serialVersionUID = 6790343627684559647L;

    public UserAccountLockedError(final String message, final ServiceCallFault fault) {
        super(message,fault);
    }

    @Override
    public String getTitle(final MessageProvider messageProvider) {
        return messageProvider.translate("ClientSessionException", "Connection Problem");
    }

    @Override
    public String getLocalizedMessage(final MessageProvider messageProvider) {
        return getLocalizedMessage();
    }

    @Override
    public String getDetailMessage(final MessageProvider messageProvider) {
        return null;
    }
}
