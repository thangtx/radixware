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

package org.radixware.kernel.designer.common.dialogs.choosetype;

import java.awt.Component;
import javax.swing.JList;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.chooseobject.AbstractItemRenderer;


class RadixPlatformClassRenderer extends AbstractItemRenderer {
    private Object item;

    public RadixPlatformClassRenderer(javax.swing.JList list) {
        super(list);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
        this.item = value;
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
        return c;
    }

    @Override
    public String getObjectName(Object object) {
        return item.toString();
    }

    @Override
    public RadixIcon getObjectIcon(Object object) {
        if (item instanceof RadixPlatformClassListItem) {
            return ((RadixPlatformClassListItem) item).getIcon();
        }
        return RadixWareIcons.METHOD.VOID;
    }

    @Override
    public String getObjectLocation(Object object) {
        if (item instanceof RadixPlatformClassListItem) {
            return ((RadixPlatformClassListItem) item).getOwnerName();
        }
        return "";
    }

    @Override
    public RadixIcon getObjectLocationIcon(Object object) {
        if (item instanceof RadixPlatformClassListItem) {
            return ((RadixPlatformClassListItem) item).getOwnerIcon();
        }
        return RadixWareIcons.METHOD.VOID;
    }

}
