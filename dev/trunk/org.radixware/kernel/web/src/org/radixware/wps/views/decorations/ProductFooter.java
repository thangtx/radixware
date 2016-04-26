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

package org.radixware.wps.views.decorations;

import org.radixware.kernel.common.html.Div;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.UIObject.SizePolicy;



public class ProductFooter extends ProductDecoration {

    public ProductFooter() {
        super(new Div());
        html.setInnerText("Compass Plus 2011");
        setPreferredHeight(20);
        html.addClass("rwt-product-footer");
        setSizePolicy(SizePolicy.EXPAND, UIObject.SizePolicy.PREFERRED);
    }
}
