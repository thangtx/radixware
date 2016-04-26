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
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.enums.EToolButtonStyle;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.utils.PropertyStore;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;

public class ValFilePathEditorItem extends ValEditorItem {

    public static final ValFilePathEditorItem DEFAULT = new ValFilePathEditorItem();

   
    public ValFilePathEditorItem() {
        super(Group.GROUP_INPUT_WIDGETS, NbBundle.getMessage(ValFilePathEditorItem.class, "Val_File_Path_Editor"),
                AdsMetaInfo.VAL_FILE_PATH_EDITOR_CLASS, new ValFilePathEditorPainter(),
                new ValFilePathEditorPropertyCollector());
        getPainter().setOwnerEditor(this);
    }

    @Override
    public final ValFilePathEditorPainter getPainter() {
        return (ValFilePathEditorPainter) super.getPainter();

    }

    public static final class ValFilePathEditorPropertyCollector extends ValEditorItem.ValEditorPropertyCollector {

        @Override
        public PropertyStore getWidgetProperties(RadixObject node, Rectangle rect) {
            PropertyStore props = super.getWidgetProperties(node, rect);

            AdsUIProperty.StringProperty valueProp = (AdsUIProperty.StringProperty) toAdsWidgetDef(node).getProperties().getByName("value");

            if (valueProp != null) {
                props.set("value", valueProp.value);
            }
            return props;
        }
    }

    public static final class ValFilePathEditorPainter extends ValEditorPainter {

        @Override
        public void paintWidget(Graphics2D graphics, Rectangle rect, PropertyStore props) {
            super.paintWidget(graphics, rect, props);

            Rectangle avalRect = props.get("freeAreaRect");
            paintOpenFileButton(graphics, getOpenFileButtonRect(avalRect));
        }

        private Rectangle getOpenFileButtonRect(Rectangle avalRect) {
            assert avalRect != null;

            Rectangle rect = avalRect.getBounds();
            rect.x += avalRect.width - DEFAULT_BUTTON_WIDTH;
            rect.width = DEFAULT_BUTTON_WIDTH;

            return rect;
        }

        private void paintOpenFileButton(Graphics2D graphics, Rectangle rect) {
            PropertyStore props = new PropertyStore();
            props.set("text", null);
            props.set("enabled", Boolean.TRUE);
            props.set("icon", RadixWareIcons.FILE.LOAD_IMAGE);
            props.set("iconSize", new Dimension(16, 16));
            props.set("toolButtonStyle", EToolButtonStyle.ToolButtonIconOnly);

            props.set("default", false);
            props.set("enabled", true);
            props.set("checked", false);

            PushButtonItem.DEFAULT.getPainter().paintBackground(graphics, rect, props);
            PushButtonItem.DEFAULT.getPainter().paintWidget(graphics, rect, props);
        }
    }
}
