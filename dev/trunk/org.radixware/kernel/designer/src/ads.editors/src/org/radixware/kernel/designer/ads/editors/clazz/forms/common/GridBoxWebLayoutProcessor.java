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
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.GridBoxWebLayout.CellRect;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;

public class GridBoxWebLayoutProcessor extends LayoutProcessor {

    private AdsRwtWidgetDef widget;

    public GridBoxWebLayoutProcessor(BaseWidget wg, AdsRwtWidgetDef widget) {

        super(wg);
        this.widget = widget;
        wg.setLayout(new GridBoxWebLayout());
    }

    @Override
    public void locate(DragDropLocator locator, Point localPoint) {

        try {
            Rectangle r = wg.getGeometry();
            Point check = new Point(localPoint);

            check.x += r.x;
            check.y += r.y;

            CellRect[][] rectangles = GridBoxWebLayout.computeRectangles(widget, r);
            GridBoxWebLayout.CellRect target = getNewLocation(check, r, rectangles);
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

    //get preffered location for new cell
    private GridBoxWebLayout.CellRect getNewLocation(Point localPoint, Rectangle src, GridBoxWebLayout.CellRect[][] rectangles) {
        for (int r = 0; r < rectangles.length; r++) {
            for (int c = 0; c < rectangles[r].length; c++) {

                GridBoxWebLayout.CellRect b = rectangles[r][c];
                if (localPoint.x >= b.x
                        && localPoint.y >= b.y
                        && localPoint.x <= b.x + b.width
                        && localPoint.y <= b.y + b.height && b.widget==null) {
                    
                    return b;
                }
            }
        }
        GridBoxWebLayout.CellRect[] check = rectangles[rectangles.length - 1];
        for (int c = 0; c < check.length; c++) {
            GridBoxWebLayout.CellRect b = check[c];
            if (localPoint.x >= b.x && localPoint.x <= b.x + b.width
                    && localPoint.y >= b.y && localPoint.y <= b.y + b.height && b.widget==null) {
                
                return b;
            }
        }
        
        return null;
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
                GridBoxWebLayout.CellRect[][] rectangles = GridBoxWebLayout.computeRectangles(widget,rect);
                GridBoxWebLayout.CellRect target = getNewLocation(check, rect, rectangles);
             if (target!=null){
                for (int r = target.cellRow; r < rectangles.length; r++) {
                    for (int c = target.cellCol; c < rectangles[r].length; c++) {

                        GridBoxWebLayout.CellRect cell = rectangles[r][c];
                
                    if (cell.widget != null) {

                                if (c > 0 && rectangles[r][c - 1].widget != cell.widget) {//>0
                                    IntProperty cintProp = (IntProperty) AdsUIUtil.getUiProperty(cell.widget, "col");
                                    AdsUIUtil.setUiProperty(cell.widget, cintProp);

                                }

                                if (r > 0 && rectangles[r - 1][c].widget != cell.widget) {//>0
                                    IntProperty intProp = (IntProperty) AdsUIUtil.getUiProperty(cell.widget, "row");
                                    AdsUIUtil.setUiProperty(cell.widget, intProp);

                                }

                                if (c > 0 && rectangles[r][c - 1].widget == cell.widget) {
                                    IntProperty csintProp = (IntProperty) AdsUIUtil.getUiProperty(cell.widget, "colSpan");

                                    AdsUIUtil.setUiProperty(cell.widget, csintProp);
                                    if (target.widget != null) {//prevent non-empty cells from adding new elements
                                        //add new element in the position, move remain cells to the right    
                                    for (int row = target.cellRow; row < rectangles.length; row++) {
                                        for (int col = target.cellCol; col < rectangles[r].length; col++) {
                                                GridBoxWebLayout.CellRect newcell = rectangles[row][col];
                                                if (newcell.widget != null) {
                                                    IntProperty intProp = (IntProperty) AdsUIUtil.getUiProperty(newcell.widget, "col");
                                                    intProp.value++;
                                                    AdsUIUtil.setUiProperty(newcell.widget, intProp);
                                                }
                                            }
                                        }
                                    }
                                }

                                if (r > 0 && rectangles[r - 1][c].widget == cell.widget) {
                                    IntProperty rsintProp = (IntProperty) AdsUIUtil.getUiProperty(cell.widget, "rowSpan");
                                    AdsUIUtil.setUiProperty(cell.widget, rsintProp);

                                    if (target.widget != null) {//prevent non-empty cells from adding new elements
                                        //add new element in the position, move remain cells down    
                                    for (int row = target.cellRow; row < rectangles.length; row++) {
                                        for (int col = target.cellCol; col < rectangles[r].length; col++) {
                                                GridBoxWebLayout.CellRect newcell = rectangles[row][col];
                                                if (newcell.widget != null) {
                                                    IntProperty intProp = (IntProperty) AdsUIUtil.getUiProperty(newcell.widget, "row");
                                                    intProp.value++;
                                                    AdsUIUtil.setUiProperty(newcell.widget, intProp);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
            
                IntProperty prop = (IntProperty) AdsUIUtil.getUiProperty(w, "row");
                prop.value = target.cellRow;
                AdsUIUtil.setUiProperty(w, prop);

                prop = (IntProperty) AdsUIUtil.getUiProperty(w, "col");
                prop.value = target.cellCol;
                AdsUIUtil.setUiProperty(w, prop);

                prop = (IntProperty) AdsUIUtil.getUiProperty(w, "colSpan");
                prop.value = GridBoxWebLayout.getColSpan(w);
                AdsUIUtil.setUiProperty(w, prop);

                prop = (IntProperty) AdsUIUtil.getUiProperty(w, "rowSpan");
                prop.value = GridBoxWebLayout.getRowSpan(w);
                AdsUIUtil.setUiProperty(w, prop);

                widget.getWidgets().add(w);
                wg.revalidate();
                wg.getScene().revalidate();
            }
            }
            return w;
        } finally {

            wg.getScene().revalidate();
        }
    }

    @Override
    public RadixObject remove(RadixObject node) {
        RadixObject owner = wg.getNode();
        try {
            if (owner.isParentOf(node)) {
                node.delete();
                wg.getSceneImpl().revalidate();
                return node;
            } else {
                return null;
            }
        } finally {
            wg.getScene().revalidate();
        }
    }
}