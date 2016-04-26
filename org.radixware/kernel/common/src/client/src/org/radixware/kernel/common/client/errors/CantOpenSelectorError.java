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
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.exceptions.ServiceCallFault;

public final class CantOpenSelectorError extends RuntimeException implements IClientError {

    /**
     *
     */
    private static final long serialVersionUID = 6630029890250654797L;
    private final String modelInfo;
    private final String definitionInfo;
    private final String reason;
    private final boolean isAccessViolationError;

    public CantOpenSelectorError(GroupModel basedOn, Throwable ex) {
        super(ex);
        modelInfo = basedOn.getTitle();
        definitionInfo = basedOn.getDefinition().toString();
        if (ex instanceof AccessViolationError) {
            isAccessViolationError = true;
            reason = basedOn.getEnvironment().getMessageProvider().translate("ExplorerError", "insufficient privileges");
        } else if (ex instanceof ServiceCallFault) {
            ServiceCallFault fault = (ServiceCallFault) ex;
            if (fault.getCauseExMessage() != null && !fault.getCauseExMessage().isEmpty()) {
                reason = fault.getCauseExMessage();
            } else if (fault.getMessage() != null && !fault.getMessage().isEmpty()) {
                reason = fault.getMessage();
            } else {
                reason = fault.getFaultString();
            }
            isAccessViolationError = false;
        } else {
            if (ex.getMessage() != null && !ex.getMessage().isEmpty()) {
                reason = ex.getMessage();
            } else {
                reason = ex.getClass().getName();
            }
            isAccessViolationError = false;
        }
    }

    @Override
    public String getTitle(MessageProvider mp) {
        return mp.translate("ExplorerError", "Can't Open Selector");
    }

    @Override
    public String getMessage() {
        return getLocalizedMessage(DefaultMessageProvider.getInstance());
    }

    @Override
    public String getLocalizedMessage(MessageProvider mp) {
        final String msg = mp.translate("ExplorerError", "Can't open selector of \'%1$s\':\n%2$s");
        return String.format(msg, modelInfo, reason);
    }

    @Override
    public String getDetailMessage(MessageProvider mp) {
        if (isAccessViolationError) {
            return "";
        } else {
            return getLocalizedMessage(mp) + ":\n"
                    + mp.translate("ExplorerError", "Definition:") + " \"" + definitionInfo + "\"\n"
                    + mp.translate("ExplorerError", "Model:") + " \"" + modelInfo + "\"";
        }
    }
}
