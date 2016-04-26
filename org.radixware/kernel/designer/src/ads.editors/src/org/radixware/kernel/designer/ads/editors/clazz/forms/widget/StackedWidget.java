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
 * 10/21/11 3:21 PM
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
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.dialog.EditorDialog;
import org.radixware.kernel.designer.ads.editors.clazz.forms.dialog.WidgetPanel;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.StackedWidgetItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.props.UIPropertySupport;


public class StackedWidget extends PaginalWidget {

    public StackedWidget(GraphSceneImpl scene, AdsWidgetDef node) {
        super(scene, node);
    }

    @Override
    protected List<WidgetAction> getInitialActions(GraphSceneImpl scene, RadixObject node) {
        final List<WidgetAction> actions = super.getInitialActions(scene, node);
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
        final AdsWidgetDef widget = (AdsWidgetDef) getNode();
        return EditorDialog.execute(new WidgetPanel(((GraphSceneImpl) getScene()).getUI(),widget, this));
    }

    @Override
    protected void notifySelected(Point localLocation) {
    }

    @Override
    public void notifyClicked(Point localLocation) {
        final AdsWidgetDef node = (AdsWidgetDef) getNode();
        final StackedWidgetItem item = (StackedWidgetItem) Item.getItem(node);
        assert item != null : "item cann't be null";

        final Insets insets = getBorder().getInsets();
        final Rectangle bounds = getBounds();

        final Graphics2D gr = getScene().getGraphics();
        final Rectangle r = new Rectangle(
            bounds.x + insets.left,
            bounds.y + insets.top,
            bounds.width - insets.right - insets.left,
            bounds.height - insets.top - insets.bottom);

        int ins = item.insideSwitcher(gr, r, localLocation);
        if (ins != 0) {
            final int size = node.getWidgets().size();
            final AdsUIProperty.IntProperty p = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(node, "currentIndex");
            p.value = p.value + ins >= size ? 0 : (p.value + ins < 0 ? size - 1 : p.value + ins);
            AdsUIUtil.fire(node, p, this);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof UIPropertySupport || evt.getSource() instanceof WidgetPanel || evt.getSource() == this) {
            final AdsUIProperty prop = (AdsUIProperty) evt.getNewValue();
            switch (prop.getName()) {
                case "currentIndex":
                    super.propertyChange(evt);

                    final AdsWidgetDef node = (AdsWidgetDef) getNode();

                    final AdsUIProperty.StringProperty byName = (AdsUIProperty.StringProperty)AdsUIUtil.getUiProperty(node, "currentPageName");
                    byName.value = AdsUIUtil.currentWidget(node).getName();
                    AdsUIUtil.fire(node, byName);

                    return;
                case "currentPageName":
                    AdsUIUtil.currentWidget((AdsWidgetDef) getNode()).setName(((AdsUIProperty.StringProperty) prop).value);
                    return;
            }
        }

        super.propertyChange(evt);
    }
}
