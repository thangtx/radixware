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

package org.radixware.wps.rwt;

import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.utils.Utils;


public class PushButton extends ButtonBase implements IPushButton {

    private boolean isDefault;

    public PushButton() {
        this(null);
    }

    public PushButton(String caption) {
        super(new Html("button"));
        html.setCss("vertical-align", "middle");
        html.setCss("text-align", "center");
        html.addClass("rwt-push-button");
        setIconWidth(13);
        setIconHeight(13);
        setMinimumWidth(100);
        html.setCss("width", "auto");//to avoid extra padding in IE
        setMinimumHeight(20);
        if (caption != null) {
            setText(caption);
        }
        setTextWrapDisabled(true);//TWRBS-3989
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
        Dialog dialog = null;
        for (UIObject parent = getParent(); parent != null && dialog == null; parent = parent.getParent()) {
            if (parent instanceof Dialog) {
                dialog = (Dialog) parent;
            }
        }
        if (dialog != null) {
            if (isDefault && getText() != null) {
                dialog.setDefaultAction(getText());
            } else if (Utils.equalsNotNull(dialog.getDefaultAction(), getText())) {
                dialog.setDefaultAction((String) null);
            }
        }
    }
}
