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

package org.radixware.kernel.designer.common.dialogs.chooseobject;

import javax.swing.JList;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.resources.icons.RadixIcon;

/**
 * Choose Radix object item visual renderer.
 a-kiliyevich
 */
class ItemQualifiedNameRenderer extends AbstractItemRenderer {

    public ItemQualifiedNameRenderer(JList list) {
        super(list);
    }

    
    @Override
    public String getObjectName(Object object) {
        if (object instanceof RadixObject) {
            final RadixObject radixObject = (RadixObject) object;
            return radixObject.getQualifiedName();
        } else {
            return String.valueOf(object);
        }
    }

    @Override
    public RadixIcon getObjectIcon(Object object) {
        if (object instanceof RadixObject) {
            final RadixObject radixObject = (RadixObject) object;
            return radixObject.getIcon();
        } else {
            return null;
        }
    }

    @Override
    public String getObjectLocation(Object object) {
        return "";
    }

    @Override
    public RadixIcon getObjectLocationIcon(Object object) {
        return null;
    }
}
