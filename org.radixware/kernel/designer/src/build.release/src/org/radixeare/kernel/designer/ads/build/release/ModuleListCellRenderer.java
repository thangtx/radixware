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

package org.radixeare.kernel.designer.ads.build.release;

import javax.swing.JList;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.chooseobject.AbstractItemRenderer;


class ModuleListCellRenderer extends AbstractItemRenderer {

    public ModuleListCellRenderer(JList list) {
        super(list);
    }

    @Override
    public String getObjectName(Object object) {
        return ((AdsModule) object).getName();
    }

    @Override
    public String getObjectLocation(Object object) {
        return ((AdsModule) object).getSegment().getLayer().getName();
    }

    @Override
    public RadixIcon getObjectIcon(Object object) {
        return ((AdsModule) object).getIcon();
    }

    @Override
    public RadixIcon getObjectLocationIcon(Object object) {
        return ((AdsModule) object).getSegment().getLayer().getIcon();
    }
}
