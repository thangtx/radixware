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
 * 11/17/11 8:36 AM
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.radixware.kernel.common.utils.PropertyStore;


public interface IWidgetPainter {
    void paintBackground(Graphics2D graphics, Rectangle rect, PropertyStore props);
    void paintBorder(Graphics2D graphics, Rectangle rect, PropertyStore props);
    void paintWidget(Graphics2D graphics, Rectangle rect, PropertyStore props);
}

