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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.RectangularSelectProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomDialogDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomFormDialogDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtUIDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.LayoutProcessor;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.ResizeAction;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.ResizeBorder;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.SelectDecorator;
import org.radixware.kernel.designer.ads.editors.clazz.forms.props.UIPropertySupport;


public class RootWidget extends BaseWidget {

    private static final Color BORDER_COLOR = new Color(185, 209, 234);
    private Insets customInsets;

    public RootWidget(GraphSceneImpl scene, AdsUIItemDef node) {
        super(scene, node);
        setBorder(BorderFactory.createCompositeBorder(
                BorderFactory.createLineBorder(1, BORDER_COLOR),
                BorderFactory.createLineBorder(1, BORDER_COLOR),
                BorderFactory.createLineBorder(1, BORDER_COLOR),
                BorderFactory.createLineBorder(1, BORDER_COLOR),
                BorderFactory.createLineBorder(1, BORDER_COLOR)));

        setLayoutProcessor(LayoutProcessor.Factory.newInstance(this, node));
    }

    @Override
    protected Insets getInsets() {
        if (customInsets == null) {
            return getBorder().getInsets();
        } else {
            Insets insets = getBorder().getInsets();
            insets = new Insets(insets.top, insets.left, insets.bottom, insets.right);
            insets.left += customInsets.left;
            insets.top += customInsets.top;
            insets.bottom += customInsets.bottom;
            insets.right += customInsets.right;
            return insets;
        }
    }

    @Override
    protected List<WidgetAction> getInitialActions(GraphSceneImpl scene, RadixObject node) {
        return Arrays.asList(
                scene.createSelectAction(),
                ActionFactory.createPopupMenuAction(new WidgetPopupMenu()),
                new ResizeAction(ResizeAction.ROOT_WIDGET_CONTROL_POINT_RESOLVER),
                SelectDecorator.createRectangularSelectAction(scene, scene.getInterLayer(),
                new RectangularSelectProvider() {

                    @Override
                    public void performSelection(Rectangle sceneSelection) {
                        GraphSceneImpl scene = getSceneImpl();
                        boolean entirely = sceneSelection.width > 0;
                        int w = sceneSelection.width;
                        int h = sceneSelection.height;
                        Rectangle rect = new Rectangle(w >= 0 ? 0 : w, h >= 0 ? 0 : h, w >= 0 ? w : -w, h >= 0 ? h : -h);
                        rect.translate(sceneSelection.x, sceneSelection.y);

                        HashSet<Object> set = new HashSet<Object>();
                        Set<?> objects = scene.getObjects();
                        for (Object object : objects) {
                            Widget widget = scene.findWidget(object);
                            if (widget == null) {
                                continue;
                            }
                            Rectangle widgetRect = widget.convertLocalToScene(widget.getBounds());
                            if (entirely) {
                                if (rect.contains(widgetRect)) {
                                    set.add(object);
                                }
                            } else {
                                if (rect.intersects(widgetRect)) {
                                    set.add(object);
                                }
                            }
                        }

                        set.add(getNode());

                        Iterator<Object> iterator = set.iterator();
                        scene.setFocusedObject(iterator.hasNext() ? iterator.next() : null);
                        scene.userSelectionSuggested(set, false);
                    }
                }));
    }

    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
    }

    @Override
    public boolean isHitAt(Point localLocation) {
        Rectangle bounds = getBounds();
        Insets insets = getInsets();
        bounds = new Rectangle(
                bounds.x + insets.left,
                bounds.y + insets.top,
                bounds.width - insets.left - insets.right,
                bounds.height - insets.top - insets.bottom);
        return isVisible() && (bounds.contains(localLocation) || ResizeAction.ROOT_WIDGET_CONTROL_POINT_RESOLVER.resolveControlPoint(this, localLocation) != null);
    }

    @Override
    public void restoreGeometry() {
        RadixObject node = getNode();
        AdsUIProperty.RectProperty geom = (AdsUIProperty.RectProperty) AdsUIUtil.getUiProperty(node, "geometry");
        if (geom != null) {
            if (this.customInsets != null) {
                geom.width += customInsets.right + customInsets.left;
                geom.height += customInsets.bottom + customInsets.top;
            } else {
                if (node instanceof AdsRwtWidgetDef) {
                    AdsRwtUIDef def = (AdsRwtUIDef) AdsUIUtil.getUiDef(node);
                    if ((def instanceof AdsRwtCustomDialogDef || def instanceof AdsRwtCustomFormDialogDef) && !(def instanceof AdsRwtCustomWidgetDef)) {
                        customInsets = new Insets(0, 0, 30, 0);
                    }

                }
            }
            setGeometry(0, 0, geom.width, geom.height);
        }
    }

    @Override
    public void saveGeometry() {
        RadixObject node = getNode();
        AdsUIProperty.RectProperty geom = (AdsUIProperty.RectProperty) AdsUIUtil.getUiProperty(node, "geometry");
        if (geom == null) {
            return;
        }

        Rectangle r = getGeometry();
        geom.width = r.width;
        geom.height = r.height;

        if (geom.getContainer() == null) {
            AdsUIUtil.setUiProperty(node, geom);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof UIPropertySupport) {
            restoreGeometry();
        }
        super.propertyChange(evt);
    }

    @Override
    protected Cursor getCursorAt(Point localLocation) {

        Rectangle bounds = getBounds();
        int thic = ResizeBorder.THICKNESS;

        int dy = localLocation.y - bounds.y;
        int dx = localLocation.x - bounds.x;
        int iy, ix, hh = bounds.height / 2 - thic / 2, hw = bounds.width / 2 - thic / 2;

        boolean hit = false;

        iy = dy / hh;
        dy %= hh;
        if (dy >= 0 && dy < thic) {
            hit = true;
        }

        ix = dx / hw;
        dx %= hw;
        if (dx >= 0 && dx < thic) {
            hit = true;
        }

        if (iy == 0) {
            iy = 1;
        }
        if (ix == 0) {
            ix = 1;
        }

        return hit ? getResizeCursor(ix, iy) : Cursor.getDefaultCursor();
    }
}
