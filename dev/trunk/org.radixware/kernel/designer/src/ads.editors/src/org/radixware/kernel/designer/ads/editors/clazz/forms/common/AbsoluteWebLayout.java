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
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.netbeans.api.visual.layout.Layout;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.TabItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;


public class AbsoluteWebLayout implements Layout {

    @Override
    public void layout(Widget widget) {
        justify(widget);
    }

    @Override
    public boolean requiresJustification(Widget widget) {
        return true;

    }

    @Override
    public void justify(Widget widget) {
        GraphSceneImpl scene = (GraphSceneImpl) widget.getScene();
        synchronized (LayoutProcessor.LOCK) {
            BaseWidget w = (BaseWidget) widget;

            Rectangle r = w.getInnerGeometry();

            AdsRwtWidgetDef rw = (AdsRwtWidgetDef) w.getNode();

            List<AdsRwtWidgetDef> roots = rw.getWidgets().list();
            boolean first = true;

            for (AdsRwtWidgetDef root : roots) {
                List<AdsRwtWidgetDef> widgets;
                if (AdsMetaInfo.RWT_TAB_SET.equals(AdsUIUtil.getUiClassName(rw))) {
                    widgets = root.getWidgets().list();
                    if (first) {
                        Point offset = w.offsetPoint();
                        r.y -= offset.y;
                        r.height += offset.y;
                        r.x -= offset.x;
                        r.width += offset.x;
                    }
                    first = false;
                } else if (AdsMetaInfo.RWT_UI_GROUP_BOX.equals(AdsUIUtil.getUiClassName(rw))) {
                    widgets = new ArrayList<AdsRwtWidgetDef>();
                    widgets.add(root);
                    if (first) {
                        Point offset = w.offsetPoint();
                        r.y -= offset.y;
                        r.height += offset.y;
                        r.x -= offset.x;
                        r.width += offset.x;
                    }
                    first = false;
                } else if (AdsMetaInfo.RWT_UI_DIALOG.equals(AdsUIUtil.getQtClassName(rw)) || AdsMetaInfo.RWT_CUSTOM_DIALOG.equals(AdsUIUtil.getQtClassName(rw)) || AdsMetaInfo.RWT_PROP_EDITOR_DIALOG.equals(AdsUIUtil.getQtClassName(rw))) {
                    widgets = new ArrayList<AdsRwtWidgetDef>();
                    widgets.add(root);
                    if (first) {
                        r.height -= 30;

                    }
                    first = false;
                } else {
                    widgets = new ArrayList<AdsRwtWidgetDef>();
                    widgets.add(root);
                }
                Set<AdsRwtWidgetDef> processedv = new HashSet<AdsRwtWidgetDef>();
                Set<AdsRwtWidgetDef> processedh = new HashSet<AdsRwtWidgetDef>();
                Set<AdsRwtWidgetDef> visited = new HashSet<AdsRwtWidgetDef>();
                for (AdsRwtWidgetDef rwt : widgets) {
                    if (processedv.contains(rwt) && processedh.contains(rwt)) {
                        continue;
                    }
                    BaseWidget cw = (BaseWidget) scene.findWidget(rwt);

                    if (cw == null) {
                        continue;
                    }

                    AdsUIProperty.RectProperty prop = (AdsUIProperty.RectProperty) AdsUIUtil.getUiProperty(rwt, "geometry");
                    Rectangle g = prop.getRectangle();

                    //  g.x += r.x;
                    //  g.y += r.y;
                    AdsUIProperty.AnchorProperty anchor = (AdsUIProperty.AnchorProperty) rwt.getProperties().getByName("anchor");
                    if (anchor != null) {
                        visited.clear();

                        applyHorizontalAnchors(anchor, rwt, cw, r.x, r.width, r.y, visited, processedv);

                        visited.clear();


                        applyVerticalAnchors(anchor, rwt, cw, r.y, r.height, visited, processedh);


//                        if (anchor.getLeft() != null) {
//                            g.x = Math.round(r.width * anchor.getLeft().part) + anchor.getLeft().offset + r.x;
//                        }
//                        if (anchor.getTop() != null) {
//                            g.y = +Math.round(r.height * anchor.getTop().part) + anchor.getTop().offset + r.y;
//                        }
//                        if (anchor.getRight() != null) {
//                            g.width = Math.round(r.width * anchor.getRight().part) + anchor.getRight().offset - g.x + r.x;
//                        }
//                        if (anchor.getBottom() != null) {
//                            g.height = Math.round(r.height * anchor.getBottom().part) + anchor.getBottom().offset - g.y + r.y;
//                        }
                    } else {
                        g.x += r.x;
                        g.y += r.y;
                        cw.setGeometry(g);
                    }
                    cw.revalidate();
                }
            }
        }
        scene.repaint();
    }

    private void applyHorizontalAnchors(AdsUIProperty.AnchorProperty anchor, AdsRwtWidgetDef widget, BaseWidget cw, int rx, int rw, int ry, Set<AdsRwtWidgetDef> set, Set<AdsRwtWidgetDef> processed) {
        AdsUIProperty.RectProperty prop = (AdsUIProperty.RectProperty) AdsUIUtil.getUiProperty(widget, "geometry");
        Rectangle g = prop.getRectangle();

        try {
            if (processed.contains(widget)) {
                return;
            }
            if (set.contains(widget)) {
                return;
            }
            set.add(widget);
            if (anchor.getLeft() != null) {
                AdsUIProperty.AnchorProperty.Anchor a = anchor.getLeft();
                if (a.refId == null) {
                    g.x = Math.round(rw * anchor.getLeft().part) + anchor.getLeft().offset;
                } else {
                    AdsRwtWidgetDef ref = widget.getOwnerWidget().findWidgetById(a.refId);
                    if (ref != null) {
                        AdsUIProperty.AnchorProperty anchor2 = (AdsUIProperty.AnchorProperty) ref.getProperties().getByName("anchor");

                        BaseWidget rcw = (BaseWidget) ((GraphSceneImpl) cw.getScene()).findWidget(ref);
                        if (rcw == null) {
                            return;
                        }
                        if (anchor2 != null && !processed.contains(ref)) {
                            applyHorizontalAnchors(anchor2, ref, rcw, rx, rw, ry, set, processed);
                        }
                        Rectangle geometry = rcw.getGeometry();
                        g.x = Math.round(geometry.width * anchor.getLeft().part) + anchor.getLeft().offset + geometry.x - rx;
                    }
                }
            }
            if (anchor.getRight() != null) {
                AdsUIProperty.AnchorProperty.Anchor a = anchor.getRight();
                if (a.refId == null) {
                    g.width = Math.round(rw * anchor.getRight().part) + anchor.getRight().offset - g.x;
                } else {
                    AdsRwtWidgetDef ref = widget.getOwnerWidget().findWidgetById(a.refId);
                    if (ref != null) {
                        AdsUIProperty.AnchorProperty anchor2 = (AdsUIProperty.AnchorProperty) ref.getProperties().getByName("anchor");
                        BaseWidget rcw = (BaseWidget) ((GraphSceneImpl) cw.getScene()).findWidget(ref);
                        if (rcw == null) {
                            return;
                        }
                        if (anchor2 != null && !processed.contains(ref)) {
                            applyHorizontalAnchors(anchor2, ref, rcw, rx, rw, ry, set, processed);
                        }
                        Rectangle geometry = rcw.getGeometry();
                        g.width = Math.round(geometry.width * anchor.getRight().part) + anchor.getRight().offset - g.x + geometry.x - rx;
                    }
                }
            }

            g.x += rx;
            g.y += ry;
            //g.width -= rx;            
            Rectangle ystore = cw.getGeometry();

            cw.setGeometry(g);
            g.x -= rx;
            g.y -= ry;
            //g.width += rx;
            prop.x = g.x;
            prop.width = g.width;
        } finally {
            processed.add(widget);
            set.remove(widget);
        }
    }

    private void applyVerticalAnchors(AdsUIProperty.AnchorProperty anchor, AdsRwtWidgetDef widget, BaseWidget cw, int ry, int rh, Set<AdsRwtWidgetDef> set, Set<AdsRwtWidgetDef> processed) {
        AdsUIProperty.RectProperty prop = (AdsUIProperty.RectProperty) AdsUIUtil.getUiProperty(widget, "geometry");
        Rectangle g = prop.getRectangle();

        try {
            if (processed.contains(widget)) {
                return;
            }
            if (set.contains(widget)) {
                return;
            }
            set.add(widget);
            if (anchor.getTop() != null) {
                AdsUIProperty.AnchorProperty.Anchor a = anchor.getTop();
                if (a.refId == null) {
                    g.y = +Math.round(rh * anchor.getTop().part) + anchor.getTop().offset;
                } else {

                    AdsRwtWidgetDef ref = widget.getOwnerWidget().findWidgetById(a.refId);
                    if (ref != null) {
                        AdsUIProperty.AnchorProperty anchor2 = (AdsUIProperty.AnchorProperty) ref.getProperties().getByName("anchor");

                        BaseWidget rcw = (BaseWidget) ((GraphSceneImpl) cw.getScene()).findWidget(ref);
                        if (rcw == null) {
                            return;
                        }
                        if (anchor2 != null && !processed.contains(ref)) {
                            applyVerticalAnchors(anchor2, ref, rcw, ry, rh, set, processed);
                        }
                        Rectangle geometry = rcw.getGeometry();
                        g.y = Math.round(geometry.height * anchor.getTop().part) + anchor.getTop().offset + geometry.y - ry;
                    }
                }
            } 
            if (anchor.getBottom() != null) {
                AdsUIProperty.AnchorProperty.Anchor a = anchor.getBottom();
                if (a.refId == null) {
                    g.height = Math.round(rh * anchor.getBottom().part) + anchor.getBottom().offset - g.y;
                } else {
                    AdsRwtWidgetDef ref = widget.getOwnerWidget().findWidgetById(a.refId);
                    if (ref != null) {
                        AdsUIProperty.AnchorProperty anchor2 = (AdsUIProperty.AnchorProperty) ref.getProperties().getByName("anchor");
                        BaseWidget rcw = (BaseWidget) ((GraphSceneImpl) cw.getScene()).findWidget(ref);
                        if (rcw == null) {
                            return;
                        }
                        if (anchor2 != null && !processed.contains(ref)) {
                            applyVerticalAnchors(anchor2, ref, rcw, ry, rh, set, processed);
                        }
                        Rectangle geometry = rcw.getGeometry();
                        g.height = Math.round(geometry.height * anchor.getBottom().part) + anchor.getBottom().offset - g.y + geometry.y - ry;
                    }
                    //g.width = Math.round(geometry.width * anchor.getRight().part) + anchor.getRight().offset - g.x + geometry.x;
                }
            }



            g.y += ry;
            // g.height -= ry;
            Rectangle xstore = cw.getGeometry();
            g.x = xstore.x;
            g.width = xstore.width;
            cw.setGeometry(g);
            g.y -= ry;
            //   g.height += ry;
            prop.y = g.y;
            prop.height = g.height;
        } finally {
            processed.add(widget);
            set.remove(widget);
        }
    }
}
