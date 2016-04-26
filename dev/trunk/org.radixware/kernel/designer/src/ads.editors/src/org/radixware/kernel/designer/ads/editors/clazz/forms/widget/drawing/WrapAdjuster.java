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

package org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing;

import java.awt.Point;
import java.awt.Rectangle;
import org.radixware.kernel.common.utils.PropertyStore;


public abstract class WrapAdjuster implements IAdjuster {

    private final IAdjuster source;

    public WrapAdjuster(IAdjuster spurce) {
        this.source = spurce != null ? spurce : IAdjuster.EMPTY_ADJUSTER;
    }

    @Override
    public final Rectangle adjust(Rectangle rect, PropertyStore props) {
        return adjustImpl(source.adjust(rect, props), rect, props);
    }

    @Override
    public final Point getOffset(Rectangle rect, Point point, PropertyStore props) {
        return getOffsetImpl(rect, source.getOffset(rect, point, props), point, props);
    }

    protected Point getOffsetImpl(Rectangle rect, Point point, Point sourcePoint, PropertyStore props) {
        return point;
    }
    protected abstract Rectangle adjustImpl(Rectangle rect, Rectangle sourceRect, PropertyStore props);

    public final IAdjuster getSource() {
        return source;
    }
}
