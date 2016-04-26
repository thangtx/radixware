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

package org.radixware.kernel.explorer.exceptions;

import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.localization.MessageProvider;


public abstract class XmlEditorException extends Exception implements IClientError {

    static final long serialVersionUID = -7318141608534673454L;
    private final String title;

    protected XmlEditorException(final String title, final String message) {
        super(message);
        this.title = title;
    }

    protected XmlEditorException(final String title, final String message, final Throwable cause) {
        super(message, cause);
        this.title = title;
    }

    @Override
    public String getTitle(MessageProvider mp) {
        return title;
    }

    @Override
    public String getDetailMessage(MessageProvider mp) {
        return getMessage();
    }

    @Override
    public String getLocalizedMessage(MessageProvider messageProvider) {
        return getMessage();
    }
}
