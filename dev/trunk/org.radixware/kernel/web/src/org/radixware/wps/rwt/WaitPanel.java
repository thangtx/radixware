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

import org.radixware.kernel.common.html.Div;




public final class WaitPanel extends UIObject {

    private int counter = 0;

    WaitPanel() {
        super(new Div());
        html.setCss("position", "absolute");
        html.setCss("z-index", "99999999");
        html.setCss("top", "0");
        html.setCss("left", "0");
        html.setCss("width", "100% ");
        html.setCss("height", "100%");
    }

    int counter() {
        return counter;
    }

    void inc() {
        counter++;
    }

    void dec() {
        if (counter > 0) {
            counter--;
        }
    }
}