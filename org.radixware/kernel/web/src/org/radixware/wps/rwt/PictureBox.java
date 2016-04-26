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
import java.awt.Dimension;
import org.radixware.wps.icons.WpsIcon;


public class PictureBox extends UIObject {

    private WpsIcon icon;

    public PictureBox() {
        super(new Html("img"));
        setWidth(20);
        setHeight(20);
    }

    public final WpsIcon getIcon() {
        return icon;
    }

    public final void setIcon(WpsIcon icon) {
        this.icon = icon;
        if (icon != null) {
            final Dimension d = icon.getOriginalSize();
            if (d != null) {
                super.setWidth(d.width);
                super.setHeight(d.height);
            }
            this.html.setAttr("src", icon.getURI(this));
        }

    }

    @Override
    public final void setHeight(int h) {
        super.setHeight(h);
        setIcon(icon);
    }

    @Override
    public final void setWidth(int w) {
        super.setWidth(w);
        setIcon(icon);
    }
}
