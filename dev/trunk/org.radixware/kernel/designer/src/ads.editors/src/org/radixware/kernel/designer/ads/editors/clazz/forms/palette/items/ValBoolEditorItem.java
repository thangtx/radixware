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
 * 11/10/11 12:44 PM
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
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.common.utils.PropertyStore;

public class ValBoolEditorItem extends ValEditorItem {

    public static final class ValBoolEditorPropertyCollector extends ValEditorItem.ValEditorPropertyCollector {

        @Override
        public PropertyStore getWidgetProperties(RadixObject node, Rectangle rect) {
            PropertyStore props = super.getWidgetProperties(node, rect);

            AdsUIProperty.BooleanValueProperty valueProp = (AdsUIProperty.BooleanValueProperty) AdsUIUtil.getUiProperty(node, "value");
            props.set("value", valueProp.value);

            return props;
        }

        @Override
        public int getMinFreeAreaWidth(RadixObject node, int widhetWidth) {

            AdsUIProperty.BooleanValueProperty valueProp = (AdsUIProperty.BooleanValueProperty) AdsUIUtil.getUiProperty(node, "value");
            boolean setValue = valueProp != null && valueProp.value != null;
            int minWidth = super.getMinFreeAreaWidth(node, widhetWidth);
            return setValue ? minWidth : minWidth + 90;
        }

        @Override
        public boolean clearButtonEnabled(RadixObject node) {
            return false;
        }
    }

    public static final class ValBoolEditorPainter extends ValEditorPainter {

        @Override
        public void paintWidget(Graphics2D graphics, Rectangle rect, PropertyStore props) {
            Boolean value = props.get("value");
            paintCheckBox(graphics, rect, value, true);
        }

        private void paintCheckBox(Graphics2D graphics, Rectangle rect, Boolean value, boolean enabled) {
            int width = 12, indentX = 6 + rect.x,
                    indentY = (rect.height - width) / 2 + rect.y,
                    dVal = 3, widthVal = 7, dCheck = 1;

            graphics.setColor(Color.WHITE);
            graphics.fillRect(indentX, indentY, width, width);
            graphics.setColor(Color.GRAY);
            graphics.drawRect(indentX, indentY, width, width);

            if (value == null) {
                graphics.setColor(Color.LIGHT_GRAY);
                graphics.fillRect(indentX + dVal, indentY + dVal, widthVal, widthVal);

                Rectangle textRect = new Rectangle(indentX + width, rect.y, rect.width - width - 6, rect.height);
                paintTextValue(graphics, textRect, "<not difined>", true, EAlignment.AlignLeft, EAlignment.AlignVCenter);
            } else if (value) {
                int x, y;
                Polygon check = new Polygon();

                x = indentX;
                y = indentY + width / 2;
                check.addPoint(x, y);

                x = indentX + width / 2;
                y = indentY + width;
                check.addPoint(x, y);

                x = indentX + width;
                y = indentY;
                check.addPoint(x, y);

                x = indentX + width / 2;
                y = indentY + (int) (width / 4.0 * 3);
                check.addPoint(x, y);

                graphics.setColor(Color.BLACK);
                graphics.fillPolygon(check);
            }
        }
    }
    public static final ValBoolEditorItem DEFAULT = new ValBoolEditorItem();

   

    public ValBoolEditorItem() {
        super(Group.GROUP_INPUT_WIDGETS, NbBundle.getMessage(ValBoolEditorItem.class, "Val_Bool_Editor"), AdsMetaInfo.VAL_BOOL_EDITOR_CLASS,
                new ValBoolEditorPainter(), new ValBoolEditorPropertyCollector());
        getPainter().setOwnerEditor(this);
    }

    @Override
    public AdsWidgetDef createObjectUI(RadixObject context) {
        AdsWidgetDef wdg = super.createObjectUI(context);
        ((AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(wdg, "frame")).value = false;
        return wdg;
    }

    @Override
    public final ValBoolEditorPainter getPainter() {
        return (ValBoolEditorPainter) super.getPainter();
    }
}
