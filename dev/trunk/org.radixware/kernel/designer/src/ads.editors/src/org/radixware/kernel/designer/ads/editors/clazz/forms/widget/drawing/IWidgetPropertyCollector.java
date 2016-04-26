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
 * 11/22/11 1:15 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing;

import org.radixware.kernel.common.utils.PropertyStore;
import java.awt.Rectangle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;


interface IWidgetPropertyCollector {

    PropertyStore getGeneralProperties(RadixObject node, Rectangle rect);

    PropertyStore getBackgroundProperties(RadixObject node, Rectangle rect);

    PropertyStore getBorderProperties(RadixObject node, Rectangle rect);

    PropertyStore getWidgetProperties(RadixObject node, Rectangle rect);

    PropertyStore getAdjustProperties(RadixObject node, Rectangle rect);

//    PropertyStore getWidgetProperties(BaseWidget widget);
//
//    PropertyStore getBackgroundProperties(BaseWidget widget);
//
//    PropertyStore getBorderProperties(BaseWidget widget);
//
//    PropertyStore getGeneralProperties(BaseWidget widget);
}
