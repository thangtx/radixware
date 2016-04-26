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
 * 11/10/11 12:53 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.enums.EToolButtonStyle;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.common.utils.PropertyStore;

public class ValDataTimeEditorItem extends ValEditorItem {

    public static final class ValDataTimeEditorPropertyCollector extends ValEditorItem.ValEditorPropertyCollector {

        @Override
        public Rectangle getAvailableValueAreaRect(RadixObject node, Rectangle rect) {
            Rectangle avalRect = super.getAvailableValueAreaRect(node, rect);
            avalRect.width -= DEFAULT_BUTTON_WIDTH;
            int minWidth = getMinFreeAreaWidth(node, rect.width);

            if (avalRect.width < minWidth) {
                avalRect.width = minWidth;
            }

            return avalRect;
        }
    }

    public static final class ValDataTimeEditorPainter extends ValEditorPainter {

        @Override
        public void paintWidget(Graphics2D graphics, Rectangle rect, PropertyStore props) {
            super.paintWidget(graphics, rect, props);

            Rectangle avalRect = props.get("freeAreaRect");
            paintCalendarButton(graphics, getCalendarButtonRect(avalRect));
        }

        private Rectangle getCalendarButtonRect(Rectangle avalRect) {
            assert avalRect != null;

            Rectangle rect = avalRect.getBounds();
            rect.x += avalRect.width - DEFAULT_BUTTON_WIDTH;
            rect.width = DEFAULT_BUTTON_WIDTH;

            return rect;
        }

        private void paintCalendarButton(Graphics2D graphics, Rectangle rect) {
            PropertyStore props = new PropertyStore();
            props.set("text", null);
            props.set("enabled", Boolean.TRUE);
            props.set("icon", RadixWareIcons.EDIT.EDIT);
            props.set("iconSize", new Dimension(16, 16));
            props.set("toolButtonStyle", EToolButtonStyle.ToolButtonIconOnly);

            props.set("default", false);
            props.set("enabled", true);
            props.set("checked", false);

            PushButtonItem.DEFAULT.getPainter().paintBackground(graphics, rect, props);
            PushButtonItem.DEFAULT.getPainter().paintWidget(graphics, rect, props);
        }
    }
    public static final ValDataTimeEditorItem DEFAULT = new ValDataTimeEditorItem();

   
    public ValDataTimeEditorItem() {
        super(Group.GROUP_INPUT_WIDGETS, NbBundle.getMessage(ValDataTimeEditorItem.class, "Val_Date_Time_Editor"),
                AdsMetaInfo.VAL_DATE_TIME_EDITOR_CLASS, new ValDataTimeEditorPainter(), new ValDataTimeEditorPropertyCollector());

        getPainter().setOwnerEditor(this);
    }

    @Override
    public final ValDataTimeEditorPainter getPainter() {
        return (ValDataTimeEditorPainter) super.getPainter();
    }
}
