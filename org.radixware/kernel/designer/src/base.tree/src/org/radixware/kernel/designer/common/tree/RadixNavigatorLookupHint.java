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

package org.radixware.kernel.designer.common.tree;

import org.netbeans.spi.navigator.NavigatorLookupHint;
import org.radixware.kernel.designer.common.tree.RadixNavigatorPanel;

class RadixNavigatorLookupHint implements NavigatorLookupHint {

    private RadixNavigatorLookupHint() {
    }

    @Override
    public String getContentType() {
        return "Radix/" + RadixNavigatorPanel.class.getSimpleName(); // registered in layer.xml
    }
    private static final RadixNavigatorLookupHint INSTANCE = new RadixNavigatorLookupHint();

    public static RadixNavigatorLookupHint getInstance() {
        return INSTANCE;
    }
}
