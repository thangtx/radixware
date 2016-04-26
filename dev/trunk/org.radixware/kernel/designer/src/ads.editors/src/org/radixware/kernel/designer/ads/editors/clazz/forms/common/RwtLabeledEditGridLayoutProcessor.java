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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty.IntProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.RwtLabeledEditGridLayout.ExtRect;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;

public class RwtLabeledEditGridLayoutProcessor extends LayoutProcessor {

    private final AdsRwtWidgetDef widget;

    public RwtLabeledEditGridLayoutProcessor(AdsRwtWidgetDef widget, BaseWidget wg) {
        super(wg);
        this.widget = widget;
        wg.setLayout(new RwtLabeledEditGridLayout());
    }

    @Override
    public void locate(DragDropLocator locator, Point localPoint) {
        try {
            Rectangle r = wg.getGeometry();
            Point check = new Point(localPoint);
            check.x += r.x;
            check.y += r.y;
            ExtRect[][] rects = RwtLabeledEditGridLayout.computeRectangles(widget, r);
            RwtLabeledEditGridLayout.ExtRect target = preferredLocation(check, r, rects);
            List<Point> points = new LinkedList<Point>();

            points.add(new Point(target.x, target.y));
            points.add(new Point(target.x + target.width, target.y));
            if (target.widget == null) {
                points.add(new Point(target.x + target.width, target.y + target.height));
                points.add(new Point(target.x, target.y + target.height));
                points.add(new Point(target.x, target.y));
            }

            locator.locate(points);
        } catch (Throwable ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        } finally {
            wg.getScene().revalidate();
        }
    }

    private RwtLabeledEditGridLayout.ExtRect preferredLocation(Point p, Rectangle src, RwtLabeledEditGridLayout.ExtRect[][] rectangles) {
        for (int r = 0; r < rectangles.length; r++) {
            for (int c = 0; c < rectangles[r].length; c++) {
                RwtLabeledEditGridLayout.ExtRect b = rectangles[r][c];
                if ((p.x >= b.x
                        && p.y >= b.y
                        && p.x <= b.x + b.width
                        && p.y <= b.y + b.height)
                        && b.widget == null) {
                    return b;
                }
            }
        }


        RwtLabeledEditGridLayout.ExtRect[] check = rectangles[rectangles.length - 1];
        for (int c = 0; c < check.length; c++) {
            RwtLabeledEditGridLayout.ExtRect b = check[c];
            if (p.x >= b.x && p.x <= b.x + b.width && b.widget == null) {
                return b;
            }
        }
        return check[0];
    }

    @Override
    public RadixObject add(RadixObject node, Point localPoint) {
        try {

            Rectangle rect = wg.getGeometry();
            Point check = new Point(localPoint);
            check.x += rect.x;
            check.y += rect.y;


            AdsRwtWidgetDef w = null;
            if (node instanceof AdsRwtWidgetDef) {
                w = (AdsRwtWidgetDef) node;
            }

            if (w != null) {

                RwtLabeledEditGridLayout.ExtRect[][] rectangles = RwtLabeledEditGridLayout.computeRectangles(widget, rect);
                RwtLabeledEditGridLayout.ExtRect target = preferredLocation(check, rect, rectangles);

                //sync columns,rows,and spans

                for (int r = 0; r < rectangles.length; r++) {
                    for (int c = 0; c < rectangles[r].length; c++) {
                        RwtLabeledEditGridLayout.ExtRect cell = rectangles[r][c];
                        if (cell.widget != null) {
                            if (c > 0 && rectangles[r][c - 1].widget == cell.widget) {
                                IntProperty intProp = (IntProperty) AdsUIUtil.getUiProperty(cell.widget, "colSpan");
                                intProp.value++;
                                AdsUIUtil.setUiProperty(cell.widget, intProp);
                            } else {
                                IntProperty prop = (IntProperty) AdsUIUtil.getUiProperty(cell.widget, "gridColumn");
                                prop.value = cell.col;
                                AdsUIUtil.setUiProperty(cell.widget, prop);

                                prop = (IntProperty) AdsUIUtil.getUiProperty(cell.widget, "gridRow");
                                prop.value = cell.row;
                                AdsUIUtil.setUiProperty(cell.widget, prop);

                                prop = (IntProperty) AdsUIUtil.getUiProperty(cell.widget, "colSpan");
                                prop.value = 1;
                                AdsUIUtil.setUiProperty(cell.widget, prop);

                            }
                        }
                    }
                }

                if (target.widget != null) {//


                    for (int r = target.row; r < rectangles.length; r++) {
                        for (int c = 0; c < rectangles[r].length; c++) {
                            RwtLabeledEditGridLayout.ExtRect cell = rectangles[r][c];
                            if (cell.widget != null) {
                                IntProperty intProp = (IntProperty) AdsUIUtil.getUiProperty(cell.widget, "gridRow");
                                intProp.value++;
                                AdsUIUtil.setUiProperty(cell.widget, intProp);
                            }
                        }
                    }
                }

                IntProperty prop = (IntProperty) AdsUIUtil.getUiProperty(w, "gridColumn");
                prop.value = target.col;
                AdsUIUtil.setUiProperty(w, prop);

                prop = (IntProperty) AdsUIUtil.getUiProperty(w, "gridRow");
                prop.value = target.row;
                AdsUIUtil.setUiProperty(w, prop);



                widget.getWidgets().add(w);
                wg.revalidate();
                wg.getScene().revalidate();
            }
            return w;
        } finally {
            wg.getScene().revalidate();
        }
    }

    @Override
    public RadixObject remove(RadixObject node) {
        try {
            AdsRwtWidgetDef w = (AdsRwtWidgetDef) node;
            if (w != null && widget.equals(w.getOwnerDefinition())) {
                node.delete();
                wg.revalidate();
                wg.getScene().revalidate();
                return w;
            }

            return null;
        } finally {
            wg.getScene().revalidate();
        }
    }
}
