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

import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef.WidgetItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef.Items;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.enums.EViewMode;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawItemWidgets;

public class TreeItem extends Item {

    public static final TreeItem DEFAULT = new TreeItem();

    
   

    public TreeItem() {
        super(Group.GROUP_ITEM_WIDGETS, NbBundle.getMessage(TreeItem.class, "Tree"), AdsMetaInfo.TREE_WIDGET_CLASS);
    }

    /*  @Override
     public RadixObject createObjectUI() {
     WidgetItem item;
     AdsItemWidgetDef widget = (AdsItemWidgetDef)super.createObjectUI();

     Column col=new Column();
     col.setName("col1");
     widget.getColumns().add(col);

     col=new Column();
     col.setName("col2");
     widget.getColumns().add(col);

     item = new WidgetItem();
     item.setName("item1");
     WidgetItem subitem = new WidgetItem();
     subitem.setName("item2");
     item.getItems().add(subitem);
        
     subitem = new WidgetItem();
     subitem.setName("item2_col");
     item.getItems().add(subitem);
     widget.getItems().add(item);


     item = new WidgetItem();
     item.setName("item3");
     widget.getItems().add(item);

     item = new WidgetItem();
     item.setName("item5");
     item.column=0;
     widget.getItems().add(item);

     item = new WidgetItem();
     item.setName("item4");
     item.column=1;
     widget.getItems().add(item);
     //widget.getProperties().add(new AdsUIProperty.IntProperty("currentIndex", 0));
     return widget;
     }*/
    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        DrawItemWidgets.paintBackgroundWidgetsWithColumn(gr,r,node);
    }
    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        DrawItemWidgets.paint(gr, r, node, false);
    }
}
