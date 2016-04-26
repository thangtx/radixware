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

/*
 * 11/22/11 1:17 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing;

import java.awt.Rectangle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.utils.PropertyStore;


public class WidgetPropertyCollector implements IWidgetPropertyCollector {

    @Override
    public PropertyStore getWidgetProperties(RadixObject node, Rectangle rect) {
        return getGeneralProperties(node, rect);
    }

    @Override
    public PropertyStore getBackgroundProperties(RadixObject node, Rectangle rect) {
        return getGeneralProperties(node, rect);
    }

    @Override
    public PropertyStore getBorderProperties(RadixObject node, Rectangle rect) {
        return getGeneralProperties(node, rect);
    }

    @Override
    public PropertyStore getGeneralProperties(RadixObject node, Rectangle rect) {
        return new PropertyStore();
    }

    @Override
    public PropertyStore getAdjustProperties(RadixObject node, Rectangle rect) {
        return getGeneralProperties(node, rect);
    }

    public final PropertyStore getAllProperties(RadixObject node, Rectangle rect) {
        final PropertyStore generalProperties = getGeneralProperties(node, rect);
        generalProperties.merge(getWidgetProperties(node, rect));
        generalProperties.merge(getBackgroundProperties(node, rect));
        generalProperties.merge(getBorderProperties(node, rect));
        generalProperties.merge(getAdjustProperties(node, rect));
        return generalProperties;
    }
}
