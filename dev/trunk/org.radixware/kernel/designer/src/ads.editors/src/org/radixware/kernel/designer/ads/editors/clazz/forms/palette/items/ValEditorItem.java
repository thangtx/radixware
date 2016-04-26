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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.defs.ads.ui.enums.EFrameShadow;
import org.radixware.kernel.common.defs.ads.ui.enums.EFrameShape;
import org.radixware.kernel.common.defs.ads.ui.enums.EToolButtonStyle;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.IWidgetPainter;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.WidgetPropertyCollector;
import org.radixware.kernel.common.utils.PropertyStore;


public abstract class ValEditorItem extends FrameItem {

    public static class ValEditorPropertyCollector extends FramePropertyCollector {

        @Override
        public PropertyStore getGeneralProperties(RadixObject node, Rectangle rect) {
            PropertyStore props = super.getGeneralProperties(node, rect);

            Rectangle clearButtonRect = getClearButtonRect(node, rect);
            props.set("clearButtonRect", clearButtonRect);

            Rectangle freeAreaRect = getAvailableFreeAreaRect(node, rect);
            props.set("freeAreaRect", freeAreaRect);

            Rectangle valueAreaRect = getAvailableValueAreaRect(node, rect);
            props.set("valueAreaRect", valueAreaRect);

            int minFreeAreaWidth = getMinFreeAreaWidth(node, rect.width);
            props.set("minFreeAreaWidth", minFreeAreaWidth);

            props.set("clearButton", clearButtonEnabled(node));

            return props;
        }

        @Override
        public PropertyStore getBackgroundProperties(RadixObject node, Rectangle rect) {
            PropertyStore props = super.getBackgroundProperties(node, rect);

            AdsUIProperty.BooleanProperty isFrame = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "frame");
            props.set("frame", isFrame.value);

            return props;
        }

        @Override
        public PropertyStore getWidgetProperties(RadixObject node, Rectangle rect) {
            PropertyStore props = super.getWidgetProperties(node, rect);

            AdsUIProperty.BooleanProperty isFrame = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "frame");
            AdsUIProperty.BooleanProperty isEnabled = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");
            AdsUIProperty.BooleanProperty isReadOnly = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "readOnly");
            AdsUIProperty.BooleanProperty isNotNull = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "notNull");

            assert Utils.isNotNull(isNotNull, isFrame, isEnabled, isReadOnly) : "Some required properties are null";

            props.set("value", null);
            props.set("frame", isFrame.value);
            props.set("enabled", isEnabled.value);
            props.set("readOnly", isReadOnly.value);
            props.set("notNull", isNotNull.value);

            return props;
        }

        public Rectangle getAvailableValueAreaRect(RadixObject node, Rectangle rect) {
            return getAvailableFreeAreaRect(node, rect);
        }

        public Rectangle getAvailableFreeAreaRect(RadixObject node, Rectangle rect) {
            int buttonCount = toAdsWidgetDef(node).getWidgets().size();
            if (clearButtonEnabled(node)) {
                ++buttonCount;
            }

            int currWidth = rect.width - buttonCount * DEFAULT_BUTTON_WIDTH + (buttonCount - 1);
            int minFreeWidth = getMinFreeAreaWidth(node, rect.width);

            currWidth = currWidth < minFreeWidth ? minFreeWidth : currWidth;
            return new Rectangle(rect.x, rect.y, currWidth, rect.height);
        }

        public int getMinFreeAreaWidth(RadixObject node, int widhetWidth) {
            return 20;
        }

        public Rectangle getClearButtonRect(RadixObject node, Rectangle rect) {
            int buttonCount = toAdsWidgetDef(node).getWidgets().size();
            int freeAreaWidth = getAvailableFreeAreaRect(node, rect).width;
            int offset = freeAreaWidth - getMinFreeAreaWidth(node, rect.width);

            if (offset > 0) {
                offset = DEFAULT_BUTTON_WIDTH;
            } else {
                offset = rect.width - (DEFAULT_BUTTON_WIDTH - 1) * (buttonCount) - freeAreaWidth;
            }
            Rectangle buttonRect = new Rectangle(rect.x + rect.width - offset, rect.y, DEFAULT_BUTTON_WIDTH, rect.height);
            return buttonRect;
        }

        public boolean clearButtonEnabled(RadixObject node) {
            AdsUIProperty.BooleanProperty isReadOnly = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "readOnly");
            AdsUIProperty.BooleanProperty isNotNull = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "notNull");

            assert Utils.isNotNull(isNotNull, isReadOnly) : "Some properties are null";

            AdsUIProperty valueProp = toAdsWidgetDef(node).getProperties().getByName("value");

            return valueProp != null && !isReadOnly.value && !isNotNull.value;
        }
    }

    public static class ValEditorPainter extends FramePainter {

        protected ValEditorItem ownerEditor;

        public final void setOwnerEditor(ValEditorItem ownerEditor) {
            this.ownerEditor = ownerEditor;
        }

        @Override
        public void paintBorder(Graphics2D graphics, Rectangle rect, PropertyStore props) {
        }

        @Override
        public void paintWidget(Graphics2D graphics, Rectangle rect, PropertyStore props) {
            boolean isFrame = PropertyStore.getBool(props.get("frame"));
            paintFrame(graphics, rect, isFrame);

            Rectangle textRect = props.get("valueAreaRect");

            String value = props.get("value", String.class);
            paintTextValue(graphics, textRect, value, true, EAlignment.AlignLeft, EAlignment.AlignVCenter);

            boolean clearButton = PropertyStore.getBool(props.get("clearButton"));
            if (clearButton) {
                Rectangle buttonRect = props.get("clearButtonRect");
                paintClearButton(graphics, buttonRect);
            }
        }

        @Override
        public void paintBackground(Graphics2D graphics, Rectangle rect, PropertyStore props) {
            boolean isFrame = PropertyStore.getBool(props.get("frame"));
            graphics.setColor(DrawUtil.COLOR_WINDOW);
            if (isFrame) {
                graphics.fillRoundRect(rect.x, rect.y, rect.width, rect.height, DrawUtil.DEFAULT_ARC_WIDTH, DrawUtil.DEFAULT_ARC_HEIGHT);
            } else {
                graphics.fillRect(rect.x, rect.y, rect.width, rect.height);
            }
        }

        protected void paintFrame(Graphics2D graphics, Rectangle frameRect, boolean frame) {

            if (frame) {
                DrawUtil.drawPlainRoundRect(graphics, frameRect, DrawUtil.COLOR_DARK, 1, DrawUtil.COLOR_BASE);
            } else {
                DrawUtil.drawPlainRect(graphics, frameRect, DrawUtil.COLOR_BASE, 1, DrawUtil.COLOR_BASE);
            }
        }

        protected void paintTextValue(Graphics2D graphics, Rectangle rect, String value, boolean enabled, EAlignment halign, EAlignment valign) {
            final int IDENT = 4;

            Rectangle rec = rect.getBounds();
            rec.x = rec.x + IDENT;
            rec.width = rec.width - IDENT * 2;

            if (value == null) {
                value = "<not defined>";
            }

            Shape clip = graphics.getClip();
            graphics.clipRect(rec.x, rec.y, rec.width, rec.height);
            DrawUtil.drawText(graphics, rec, halign, valign, enabled, value);
            graphics.setClip(clip);
        }

        protected void paintClearButton(Graphics2D graphics, Rectangle rect) {
            PropertyStore props = new PropertyStore();
            props.set("text", "");
            props.set("enabled", true);
            props.set("icon", RadixWareIcons.DELETE.CLEAR);
            props.set("iconSize", new Dimension(16, 16));
            props.set("toolButtonStyle", EToolButtonStyle.ToolButtonIconOnly);

            Rectangle rectangle = rect.getBounds();
            rectangle.width = 20;

            IWidgetPainter painter = ToolButtonItem.DEFAULT.getPainter();

            painter.paintBackground(graphics, rectangle, props);
            painter.paintWidget(graphics, rectangle, props);
        }
    }
    public static final int DEFAULT_BUTTON_WIDTH = 20;

    protected ValEditorItem(Group group, String title, String clazz, IWidgetPainter painter, WidgetPropertyCollector propertyCollector) {
        super(group, title, clazz, painter, propertyCollector);
    }

    @Override
    public AdsWidgetDef createObjectUI(RadixObject context) {
        AdsWidgetDef wdg = super.createObjectUI(context);

        ((AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(wdg, "frameShape")).value = EFrameShape.StyledPanel;
        ((AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(wdg, "frameShadow")).value = EFrameShadow.Sunken;

        wdg.getProperties().add(new AdsUIProperty.BooleanProperty("notNull", false));
        wdg.getProperties().add(new AdsUIProperty.BooleanProperty("readOnly", false));
        wdg.getProperties().add(new AdsUIProperty.BooleanProperty("frame", true));

        return wdg;
    }

    @Override
    public ValEditorPainter getPainter() {
        return (ValEditorPainter) super.getPainter();
    }

    protected static AdsWidgetDef toAdsWidgetDef(RadixObject node) {
        assert node instanceof AdsWidgetDef : "Node is't instance of AdsWidgetDef";

        return (AdsWidgetDef) node;
    }

//    protected static ValEditorWidget toValEditorWidget(Widget widget) {
//        assert widget instanceof ValEditorWidget : "Widget is't instance of ValEditorWidget";
//
//        return (ValEditorWidget) widget;
//    }
}
