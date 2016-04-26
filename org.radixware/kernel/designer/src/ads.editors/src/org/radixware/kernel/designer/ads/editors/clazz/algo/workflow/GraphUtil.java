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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow;

import java.awt.Image;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsBaseObject;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.palette.Item;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class GraphUtil {
    
    public static final Image IMAGE_FINISH = RadixWareIcons.WORKFLOW.WIDGET_FINISH.getImage(38);
    public static final Image IMAGE_FORK   = RadixWareIcons.WORKFLOW.WIDGET_FORK.getImage(62, 17);
    public static final Image IMAGE_MERGE  = RadixWareIcons.WORKFLOW.WIDGET_MERGE.getImage(62, 17);
    public static final Image IMAGE_RETURN = RadixWareIcons.WORKFLOW.WIDGET_RETURN.getImage(38);
    public static final Image IMAGE_START  = RadixWareIcons.WORKFLOW.WIDGET_START.getImage(38);
    public static final Image IMAGE_TERM   = RadixWareIcons.WORKFLOW.WIDGET_TERM.getImage(38);
    public static final Image IMAGE_THROW  = RadixWareIcons.WORKFLOW.WIDGET_THROW.getImage(38);
    
    public static Image getImage(AdsBaseObject node) {
        Item item = Item.getItem(node);
        assert item != null;
        return item.getSmalImage();
    }
}
