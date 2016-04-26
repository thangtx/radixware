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

package org.radixware.kernel.designer.ads.editors.clazz.forms.palette;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;
import java.awt.Image;
import org.radixware.kernel.common.resources.icons.RadixIcon;


public class ItemNode extends AbstractNode {
    
    private Item item;
    
    public ItemNode(Item key) {
        super(Children.LEAF, Lookups.fixed(new Object[] { key } ));
        this.item = key;
    }
    
    @Override
    public Image getIcon(int i) {
        RadixIcon icon = item.getIcon();
        return icon != null ? icon.getImage(24) : null;
    }
    
    @Override
    public String getDisplayName() {
        return item.getTitle();
    }
    
}