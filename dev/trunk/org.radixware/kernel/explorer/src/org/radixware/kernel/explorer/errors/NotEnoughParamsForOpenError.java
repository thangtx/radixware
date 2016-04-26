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

package org.radixware.kernel.explorer.errors;

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.localization.DefaultMessageProvider;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.explorer.env.Application;

public final class NotEnoughParamsForOpenError extends Error implements IClientError {

    private static final long serialVersionUID = -499854144272489907L;
    final QWidget widget;

    public NotEnoughParamsForOpenError(final QWidget widget) {
        this.widget = widget;
    }

    @Override
    public String getTitle(MessageProvider mp) {
        return mp.translate("ExplorerError", "Can't open widget");
    }

    public String getMessage(MessageProvider mp) {
        return getLocalizedMessage();
    }

    @Override
    public String getMessage() {
        return getLocalizedMessage(DefaultMessageProvider.getInstance());
    }

    @Override
    public String getLocalizedMessage(MessageProvider mp) {
        final String msg = Application.translate("ExplorerError", "Not enough parameters to open widget\n\'%s\'");
        return String.format(msg, widget.objectName());
    }

    @Override
    public String getDetailMessage(MessageProvider mp) {
        return getLocalizedMessage(mp);
    }
}
