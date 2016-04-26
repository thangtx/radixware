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

package org.radixware.kernel.designer.ads.editors.clazz.forms.widget;

import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.util.List;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.EditProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.dialog.EditorDialog;
import org.radixware.kernel.designer.ads.editors.clazz.forms.dialog.TabPanel;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.TabItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.props.UIPropertySupport;


public class TabWidget extends PaginalWidget {

    public TabWidget(GraphSceneImpl scene, AdsWidgetDef node) {
        super(scene, node);
    }

    public TabWidget(GraphSceneImpl scene, AdsRwtWidgetDef node) {
        super(scene, node);
    }

    @Override
    protected List<WidgetAction> getInitialActions(GraphSceneImpl scene, RadixObject node) {
        List<WidgetAction> actions = super.getInitialActions(scene, node);
        actions.add(ActionFactory.createEditAction(new EditProvider() {

            @Override
            public void edit(Widget widget) {
                if (notifyEdited()) {
                    repaint();
                }
            }
        }));
        return actions;
    }

    protected boolean notifyEdited() {
        AdsUIItemDef w = (AdsUIItemDef) getNode();
        EditorDialog.execute(new TabPanel(((GraphSceneImpl) getScene()).getUI(),w, this));

        return true;
    }

    @Override
    protected void notifySelected(Point localLocation) {
        RadixObject node = getNode();
        TabItem item = (TabItem) Item.getItem(node);
        assert item != null : "item cann't be null";

        Insets insets = getBorder().getInsets();
        Rectangle bounds = getBounds();

        Graphics2D gr = getScene().getGraphics();
        Rectangle r = new Rectangle(
                bounds.x + insets.left,
                bounds.y + insets.top,
                bounds.width - insets.right - insets.left,
                bounds.height - insets.top - insets.bottom);

        int idx = item.getTabIndex(gr, r, node, localLocation.x, localLocation.y);
        if (idx >= 0) {
            AdsUIProperty.IntProperty p = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(node, "currentIndex");
            p.value = idx;

            AdsUIUtil.fire(node, p, this);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof UIPropertySupport || evt.getSource() instanceof TabPanel || evt.getSource() == this) {
            AdsUIProperty prop = (AdsUIProperty) evt.getNewValue();
            if (prop.getName().equals("currentIndex")) {
                super.propertyChange(evt);
                return;
            }
        }
        super.propertyChange(evt);
    }

    @Override
    public void onEvent(final ContainerChangedEvent e) {
        super.onEvent(e);
    }

    @Override
    public Point offsetPoint() {
        if (getNode() instanceof AdsRwtWidgetDef) {
            return new Point(-5, -21);
        } else {
            return new Point(0, 0);
        }
    }
}
