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
 * 10/3/11 2:24 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.enums.EOrientation;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.PropertyStore;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.ToolBarLayout;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.ToolBarLayoutProcessor;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.WidgetItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.IWidgetPainter;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.WidgetPropertyCollector;

public class ToolBarItem extends WidgetItem {

    public static final ToolBarItem DEFAULT = new ToolBarItem();


    private static class Painter implements IWidgetPainter {

        @Override
        public void paintBackground(Graphics2D graphics, Rectangle rect, PropertyStore props) {
            graphics.setColor(DrawUtil.COLOR_BUTTON);
            graphics.fillRect(rect.x, rect.y, rect.width, rect.height);
        }

        @Override
        public void paintBorder(Graphics2D graphics, Rectangle rect, PropertyStore props) {
            graphics.setColor(DrawUtil.COLOR_DARK);
            graphics.drawRoundRect(rect.x, rect.y, rect.width - 1, rect.height - 1,
                    DrawUtil.DEFAULT_ARC_WIDTH, DrawUtil.DEFAULT_ARC_HEIGHT);
        }

        @Override
        public void paintWidget(Graphics2D graphics, Rectangle rect, PropertyStore props) {

            final Object actions = props.get("actions");
            if (actions instanceof List<?>) {

                final EOrientation orientation = props.get("orientation");
                final IWidgetPainter actionPainter = new ActionItem.Painter();

                Point start = ToolBarLayout.getStartPoint(rect, orientation);
                final List<PropertyStore> actionList = (List<PropertyStore>) actions;
                for (final PropertyStore actionProps : actionList) {
                    final Dimension size = ActionItem.calcActionSize(graphics.getFontMetrics(DrawUtil.DEFAULT_FONT),
                            actionProps.<RadixIcon>get("icon"), actionProps.<String>get("text"));

                    final Rectangle actionRect;
                    if (orientation == EOrientation.Horizontal) {
                        actionRect = ToolBarLayout.calcHorizontalRect(start, rect, size);
                        start.x += actionRect.width;
                    } else {
                        actionRect = ToolBarLayout.calcVerticalRect(start, rect, size);
                        start.y += actionRect.height + ToolBarLayoutProcessor.INTERVAL;
                    }

                    actionPainter.paintBackground(graphics, actionRect, actionProps);
                    actionPainter.paintWidget(graphics, actionRect, actionProps);
                }
            }
        }
    }

    private static class PropertyCollector extends WidgetPropertyCollector {

        @Override
        public PropertyStore getWidgetProperties(RadixObject node, Rectangle rect) {
            final PropertyStore properties = super.getWidgetProperties(node, rect);

            final AdsUIProperty.IdListProperty actionsProp = (AdsUIProperty.IdListProperty) AdsUIUtil.getUiProperty(node, "actions");
            if (actionsProp != null) {

                final List<PropertyStore> actions = new ArrayList<>();
                for (final Id id : actionsProp.getIds()) {
                    final AdsWidgetDef action = ActionItem.findAction(node.getModule(), id);

                    if (action != null) {
                        actions.add(new ActionItem.PropertyCollector().getWidgetProperties(action, rect));
                    }
                }
                properties.set("actions", actions);
            }

            properties.set("orientation", getOrientation(node));

            return properties;
        }
    }

    public ToolBarItem() {
        super(Group.GROUP_CONTAINERS, NbBundle.getMessage(ToolBarItem.class, "Tool_Bar"),
                AdsMetaInfo.TOOL_BAR_CLASS, new Painter(), new PropertyCollector());
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        super.paintWidget(gr, r, node);
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        super.paintBackground(gr, r, node);
    }

    public static EOrientation getOrientation(RadixObject action) {
        final AdsUIProperty property = AdsUIUtil.getUiProperty(action, "orientation");
        if (property instanceof AdsUIProperty.EnumValueProperty) {
            return (EOrientation) ((AdsUIProperty.EnumValueProperty) property).value;
        }
        return null;
    }
}
