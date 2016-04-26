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
 * 11/15/11 2:35 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.common.utils.PropertyStore;

public class ValIntEditorItem extends ValNumEditorItem {

    public static final class ValIntEditorPropertyCollector extends ValEditorItem.ValEditorPropertyCollector {

        @Override
        public PropertyStore getWidgetProperties(RadixObject node, Rectangle rect) {
            PropertyStore props = super.getWidgetProperties(node, rect);

            AdsWidgetDef def = (AdsWidgetDef) node;
            AdsUIProperty.LongProperty valueProp = (AdsUIProperty.LongProperty) def.getProperties().getByName("value");

            if (valueProp != null) {
                String strVal = String.valueOf(valueProp.value);
                props.set("value", strVal);
            }

            Rectangle spinButtonRect = getSpinButtonRect(getAvailableFreeAreaRect(node, rect));
            props.set("spinButtonRect", spinButtonRect);

            return props;
        }

        @Override
        public Rectangle getAvailableValueAreaRect(RadixObject node, Rectangle rect) {

            Rectangle freeRect = getAvailableFreeAreaRect(node, rect);
            freeRect.width -= SPIN_WIDTH;

            int minWidth = getMinFreeAreaWidth(node, rect.width);
            if (freeRect.width < minWidth) {
                freeRect.width = minWidth;
            }

            return freeRect;
        }

        @Override
        public Rectangle getAvailableFreeAreaRect(RadixObject node, Rectangle rect) {
            int offset = 3;
            int buttonCount = toAdsWidgetDef(node).getWidgets().size();

            if (clearButtonEnabled(node)) {
                ++buttonCount;
            }

            if (buttonCount > 0) {
                offset = 0;
            }

            Rectangle freeArea = super.getAvailableFreeAreaRect(node, rect);
            freeArea.width -= offset;

            return freeArea;
        }

        @Override
        public int getMinFreeAreaWidth(RadixObject node, int widhetWidth) {
            return 40;
        }

        private Rectangle getSpinButtonRect(Rectangle freeRect) {
            return new Rectangle(freeRect.x + freeRect.width - SPIN_WIDTH, freeRect.y, SPIN_WIDTH, freeRect.height);
        }
    }

    public static class ValIntEditorPainter extends ValEditorPainter {

        @Override
        public void paintWidget(Graphics2D graphics, Rectangle rect, PropertyStore props) {
            super.paintWidget(graphics, rect, props);

            boolean isEnabled = PropertyStore.getBool(props.get("enabled"));

            Rectangle spinButtonRect = props.get("spinButtonRect");
            drawSpinButtons(graphics, spinButtonRect, isEnabled);
        }

        private void drawSpinButtons(Graphics2D graphics, Rectangle rect, boolean enabled) {
            final int ARC_WH = 6;

            Rectangle spinRect = rect.getBounds();

            graphics.setColor(DrawUtil.COLOR_BUTTON);
            graphics.fillRoundRect(spinRect.x, spinRect.y, spinRect.width, spinRect.height - 1, ARC_WH, ARC_WH);
            graphics.setColor(DrawUtil.COLOR_DARK);
            graphics.drawRoundRect(spinRect.x, spinRect.y, spinRect.width, spinRect.height - 1, ARC_WH, ARC_WH);

            int x = spinRect.x, y = spinRect.y + spinRect.height / 2;
            graphics.setColor(DrawUtil.COLOR_DARK);
            graphics.drawLine(x, y, x + SPIN_WIDTH, y);

            final int SPIN_ICON_WIDTH = 8;
            final int SPIN_ICON_HEIGHT = 6;

            x = spinRect.x + (spinRect.width - SPIN_ICON_WIDTH) / 2 + 1;
            y = spinRect.y + (spinRect.height / 2 + SPIN_ICON_HEIGHT) / 2 + 1;

            Polygon polygon = new Polygon();
            polygon.addPoint(x, y);
            polygon.addPoint(x + SPIN_ICON_WIDTH / 2, y - SPIN_ICON_HEIGHT);
            polygon.addPoint(x + SPIN_ICON_WIDTH, y);
            polygon.addPoint(x, y);

            graphics.setColor(Color.BLACK);
            graphics.fillPolygon(polygon);

            y = spinRect.y + (spinRect.height / 2 * 3 - SPIN_ICON_HEIGHT) / 2;

            polygon = new Polygon();
            polygon.addPoint(x, y);
            polygon.addPoint(x + SPIN_ICON_WIDTH, y);
            polygon.addPoint(x + SPIN_ICON_WIDTH / 2, y + SPIN_ICON_HEIGHT);
            polygon.addPoint(x, y);

            graphics.fillPolygon(polygon);
        }
    }

    public static final int SPIN_WIDTH = 15;
    public static final ValIntEditorItem DEFAULT = new ValIntEditorItem();

 

    public ValIntEditorItem() {
        super(Group.GROUP_INPUT_WIDGETS, NbBundle.getMessage(ValIntEditorItem.class, "Val_Int_Editor"), AdsMetaInfo.VAL_INT_EDITOR_CLASS,
                new ValIntEditorPainter(), new ValIntEditorPropertyCollector());

        getPainter().setOwnerEditor(this);
    }

    @Override
    public ValIntEditorPainter getPainter() {
        return (ValIntEditorPainter) super.getPainter();
    }
}
