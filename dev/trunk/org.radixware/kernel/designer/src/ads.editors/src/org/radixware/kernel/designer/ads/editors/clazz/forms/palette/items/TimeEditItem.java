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
package org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items;

import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;

public class TimeEditItem extends Item {

    public static final TimeEditItem DEFAULT = new TimeEditItem();

    

    public TimeEditItem() {
        super(Group.GROUP_INPUT_WIDGETS, NbBundle.getMessage(TimeEditItem.class, "Time_Edit"), AdsMetaInfo.TIME_EDIT_CLASS);
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        DateTimeEditItem.DEFAULT.paintBackground(gr, r, node, false);
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        DateTimeEditItem.DEFAULT.paint(gr, r, node, false);
    }
}
