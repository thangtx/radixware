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
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.exceptions.ServiceCallFault;

public final class CantOpenEditorError extends RuntimeException implements IClientError {

    /**
     *
     */
    private static final long serialVersionUID = -752815723231334758L;
    private final String modelInfo;
    private final String definitionInfo;
    private final String reason;

    public CantOpenEditorError(final EntityModel basedOn, final Throwable ex) {
        super(ex);
        modelInfo = basedOn.getTitle();
        definitionInfo = basedOn.getDefinition().toString();
        if (ex instanceof ServiceCallFault) {
            ServiceCallFault fault = (ServiceCallFault) ex;
            if (fault.getCauseExMessage() != null && !fault.getCauseExMessage().isEmpty()) {
                reason = fault.getCauseExMessage();
            } else if (fault.getMessage() != null && !fault.getMessage().isEmpty()) {
                reason = fault.getMessage();
            } else {
                reason = fault.getFaultString();
            }
        } else {
            if (ex.getMessage() != null && !ex.getMessage().isEmpty()) {
                reason = ex.getMessage();
            } else {
                reason = ex.getClass().getName();
            }
        }
    }

    public CantOpenEditorError(final EntityModel basedOn, final String reason) {
        modelInfo = basedOn.getTitle();
        definitionInfo = "";
        this.reason = reason;
    }

    @Override
    public String getTitle(MessageProvider mp) {
        return mp.translate("ExplorerError", "Can't Open Editor");
    }

    @Override
    public String getLocalizedMessage(MessageProvider mp) {
        final String msg = mp.translate("ExplorerError", "Can't open editor of \'%1$s\':\n%2$s");
        return String.format(msg, modelInfo, reason);
    }

    @Override
    public String getMessage() {
        return getLocalizedMessage(DefaultMessageProvider.getInstance());
    }

    @Override
    public String getDetailMessage(MessageProvider mp) {
        if (definitionInfo == null || definitionInfo.isEmpty()) {
            return "";
        } else {
            return getLocalizedMessage(mp) + ":\n"
                    + mp.translate("ExplorerError", "Definition:") + " \"" + definitionInfo + "\"\n"
                    + mp.translate("ExplorerError", "Model:") + " \"" + modelInfo + "\"\n";
        }
    }
}