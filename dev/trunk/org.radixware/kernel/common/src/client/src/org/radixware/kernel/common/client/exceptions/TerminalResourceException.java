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

import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.localization.DefaultMessageProvider;
import org.radixware.kernel.common.client.localization.MessageProvider;

public final class TerminalResourceException extends Exception implements IClientError {

    private static final long serialVersionUID = -1557996819818256443L;
    private final String reason;
    private final String id;
    private final ResourceKind kind;

    public static enum ResourceKind {
        File,
        MessageDialog,
        Process;
    }

    public TerminalResourceException(ResourceKind kind, String id) {
        this.id = id;
        this.kind = kind;
        this.reason = null;
    }

    public TerminalResourceException(final String localizedMsg) {
        this.id = null;
        this.kind = null;
        reason = localizedMsg;
    }

    public TerminalResourceException(final String localizedMsg, Throwable ex) {
        super(ex);
        this.id = null;
        this.kind = null;
        reason = localizedMsg;
    }

    private static String getResourceKindTitle(final MessageProvider msgProvider, final ResourceKind kind){
        switch (kind){
            case File:
                return msgProvider.translate("ExplorerException", "File");
            case MessageDialog:
                return msgProvider.translate("ExplorerException", "Message dialog");
            case Process:
                return msgProvider.translate("ExplorerException", "Process");
            default:
                return kind.name();
        }
    }

    @Override
    public String getLocalizedMessage() {
        return getLocalizedMessage(DefaultMessageProvider.getInstance());
    }

    @Override
    public String getLocalizedMessage(MessageProvider provider) {
        if (reason != null || id == null || kind == null) {
            return reason;
        } else {
            final String msg = provider.translate("ExplorerException", "%s resource #%s was not found");
            return String.format(msg, getResourceKindTitle(provider, kind), id);
        }
    }

    @Override
    public String getMessage() {
        return getLocalizedMessage();
    }

    @Override
    public String getDetailMessage(MessageProvider provider) {
        return getLocalizedMessage(provider);
    }

    @Override
    public String getTitle(MessageProvider provider) {
        return provider.translate("ExplorerError", "Explorer Resource Exception");
    }
}
