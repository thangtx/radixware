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

import org.radixware.kernel.common.client.localization.DefaultMessageProvider;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.exceptions.ClientException;

public final class SettingPropertyValueError extends RuntimeException implements IClientError {

    private static final long serialVersionUID = -8866013573820756152L;
    private final String propertyName;
    private final String propertyInfo;

    public SettingPropertyValueError(Property property, Throwable cause) {
        super(cause);
        propertyName = property.getDefinition().getName();
        propertyInfo = property.getDefinition().toString();
    }

    @Override
    public String getTitle(MessageProvider mp) {
        return mp.translate("ExplorerError", "Setting Property Value Error");
    }

    @Override
    public String getMessage() {
        return getLocalizedMessage(DefaultMessageProvider.getInstance());
    }

    @Override
    public String getDetailMessage(MessageProvider mp) {
        return getMessage() + ":\n" + propertyInfo;
    }

    @Override
    public String getLocalizedMessage(MessageProvider messageProvider) {
        final String msg = messageProvider.translate("ExplorerError", "Error on setting new value to property \'%s\':\n%s");
        return String.format(msg, propertyName, ClientException.getExceptionReason(messageProvider, getCause()));
    }
}
