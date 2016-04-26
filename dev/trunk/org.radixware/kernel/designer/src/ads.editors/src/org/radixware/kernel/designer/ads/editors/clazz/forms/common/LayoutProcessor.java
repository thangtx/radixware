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

package org.radixware.kernel.designer.ads.editors.clazz.forms.common;

import java.awt.Point;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsLayout;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;


public abstract class LayoutProcessor {

    public static final Object LOCK = new Object();

    public static class Factory {

        public static LayoutProcessor newInstance(BaseWidget widget, AdsUIItemDef w) {
            if (w instanceof AdsWidgetDef) {
                AdsWidgetDef node = (AdsWidgetDef) w;
                if (node.getLayout() != null) {
                    AdsLayout layout = node.getLayout();
                    if (layout.getClassName().equals(AdsMetaInfo.HORIZONTAL_LAYOUT_CLASS)) {
                        return new HorizontalLayoutProcessor(widget, node);
                    } else if (layout.getClassName().equals(AdsMetaInfo.VERTICAL_LAYOUT_CLASS)) {
                        return new VerticalLayoutProcessor(widget, node);
                    } else if (layout.getClassName().equals(AdsMetaInfo.GRID_LAYOUT_CLASS)) {
                        return new GridLayoutProcessor(widget, node);
                    } else {
                        assert false : "unknown layout " + layout.getClassName();
                    }
                }
                if (node.getClassName().equals(AdsMetaInfo.SPLITTER_CLASS) || node.getClassName().equals(AdsMetaInfo.ADVANCED_SPLITTER_CLASS)) {
                    return new SplitterLayoutProcessor(widget, node);
                } else if (AdsUIUtil.isValEditr(node)) {
                    return new ValEditorLayoutProcessor(widget, node);
                }
                return new AbsoluteLayoutProcessor(widget, node);
            } else {
                AdsRwtWidgetDef node = (AdsRwtWidgetDef) w;
                if (node == null) {
                    return null;
                }

                if (node.getClassName().equals(AdsMetaInfo.RWT_LABELED_EDIT_GRID) || node.getClassName().equals(AdsMetaInfo.RWT_PROPERTIES_GRID)) {
                    return new RwtLabeledEditGridLayoutProcessor(node, widget);
                }
                if (node.getClassName().equals(AdsMetaInfo.RWT_VERTICAL_BOX_CONTAINER) || node.getClassName().equals(AdsMetaInfo.RWT_HORIZONTAL_BOX_CONTAINER)) {
                    return new BoxWebLayoutProcessor(widget, node);
                }
                if (node.getClassName().equals(AdsMetaInfo.RWT_SPLITTER)) {
                    return new SplitterLayoutProcessor(widget, node);
                }
                if (AdsMetaInfo.RWT_UI_PANEL.equals(node.getClassName())) {
                    return new PanelWebLayoutProcessor(widget, node);
                }

                if (AdsMetaInfo.RWT_UI_GRID_BOX_CONTAINER.equals(node.getClassName())) {
                    return new GridBoxWebLayoutProcessor(widget, node);
                }
                return new AbsoluteWebLayoutProcessor(widget, node);
            }
        }
    }
    protected BaseWidget wg;

    protected LayoutProcessor(BaseWidget wg) {
        this.wg = wg;
    }

    public abstract void locate(DragDropLocator locator, Point localPoint);

    public abstract RadixObject add(RadixObject node, Point localPoint);

    public abstract RadixObject remove(RadixObject node);

    protected void saveGeometry(AdsWidgetDef widget) { // to remove geometry if necessary
        GraphSceneImpl scene = wg.getSceneImpl();
        if (widget.getLayout() != null) {
            AdsLayout.Items items = widget.getLayout().getItems();
            for (AdsLayout.Item item : items) {
                RadixObject o = AdsUIUtil.getItemNode(item);
                BaseWidget wgt = (BaseWidget) scene.findWidget(o);
                if (wgt != null) {
                    wgt.saveGeometry();
                }
            }
        } else {
            for (AdsWidgetDef w : widget.getWidgets()) {
                BaseWidget wgt;
                if (AdsUIUtil.getUiClassName(w).equals(AdsMetaInfo.WIDGET_CLASS) && w.getLayout() != null) {
                    wgt = (BaseWidget) scene.findWidget(w.getLayout());
                } else {
                    wgt = (BaseWidget) scene.findWidget(w);
                }
                if (wgt != null) {
                    wgt.saveGeometry();
                }
            }
        }
    }
}
