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

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;
import java.util.Objects;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.PropertyStore;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;
import static org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item.getTextById;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.WidgetItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.IWidgetPainter;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.WidgetPropertyCollector;

public class ActionItem extends WidgetItem {

    public static final ActionItem DEFAULT = new ActionItem();

   
    public static class PropertyCollector extends WidgetPropertyCollector {

        @Override
        public PropertyStore getWidgetProperties(RadixObject node, Rectangle rect) {
            final PropertyStore properties = super.getWidgetProperties(node, rect);

            final AdsUIProperty textProperty = AdsUIUtil.getUiProperty(node, "text");
            final String text;
            if (textProperty instanceof AdsUIProperty.LocalizedStringRefProperty) {
                text = getTextById(node, ((AdsUIProperty.LocalizedStringRefProperty) textProperty).getStringId());
            } else if (textProperty instanceof AdsUIProperty.StringProperty) {
                text = ((AdsUIProperty.StringProperty) textProperty).value;
            } else {
                text = null;
            }

            if (node instanceof AdsRwtWidgetDef && ((AdsRwtWidgetDef) node).isActionWidget()) {
                properties.set("text", "");
            } else {
                properties.set("text", text);
            }

            final AdsUIProperty.ImageProperty iconProperty = (AdsUIProperty.ImageProperty) AdsUIUtil.getUiProperty(node, "icon");
            RadixIcon icon = null;
            if (iconProperty != null) {
                icon = getIconById(node, iconProperty.getImageId());
            }
            properties.set("icon", icon);

            properties.set("enabled", Boolean.TRUE);

            return properties;
        }
    }

    public static class Painter implements IWidgetPainter {

        private final IWidgetPainter nativ = new PushButtonItem.PushButtonPainter();

        @Override
        public void paintBackground(Graphics2D graphics, Rectangle rect, PropertyStore props) {
            nativ.paintBackground(graphics, rect, props);
        }

        @Override
        public void paintBorder(Graphics2D graphics, Rectangle rect, PropertyStore props) {
            nativ.paintBorder(graphics, rect, props);
        }

        @Override
        public void paintWidget(Graphics2D graphics, Rectangle rect, PropertyStore props) {
            nativ.paintWidget(graphics, rect, props);
        }
    }

    public ActionItem() {
        super(Group.GROUP_BUTTONS, NbBundle.getMessage(ActionItem.class, "Action"),
                AdsMetaInfo.ACTION_CLASS, new Painter(), new PropertyCollector());
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        final AdsWidgetDef wdg = (AdsWidgetDef) super.createObjectUI(context);

        wdg.getProperties().add(new AdsUIProperty.RectProperty("geometry", 0, 0, 30, 30));

        return wdg;
    }

    public static Dimension calcActionSize(FontMetrics fontMetrics, RadixIcon icon, String text) {

        int iconH = 0;
        int iconW = 0;
        if (icon != null) {
            iconH = icon.getIcon().getIconHeight();
            iconW = icon.getIcon().getIconWidth();
        }

        int textW = 0;
        if (text != null && !text.isEmpty()) {
            textW = fontMetrics.stringWidth(text) + 8;
        }

        final int height = Math.max(30, Math.max(iconH, fontMetrics.getHeight()));
        final int width = Math.max(30, textW + iconW);

        return new Dimension(width, height);
    }

    public static Dimension calcActionSize(FontMetrics fontMetrics, AdsWidgetDef action) {
        final AdsUIProperty.ImageProperty iconProp
                = (AdsUIProperty.ImageProperty) AdsUIUtil.getUiProperty(action, "icon");

        final AdsUIProperty textProp = AdsUIUtil.getUiProperty(action, "text");

        RadixIcon icon = null;
        if (iconProp != null) {
            icon = Item.getIconById(action, iconProp.getImageId());
        }

        final String text;
        if (textProp instanceof AdsUIProperty.LocalizedStringRefProperty) {
            text = getTextById(action, ((AdsUIProperty.LocalizedStringRefProperty) textProp).getStringId());
        } else if (textProp instanceof AdsUIProperty.StringProperty) {
            text = ((AdsUIProperty.StringProperty) textProp).value;
        } else {
            text = null;
        }

        return ActionItem.calcActionSize(fontMetrics, icon, text);
    }

    public static AdsWidgetDef findAction(Module module, final Id id) {
        final List<Definition> result = RadixObjectsUtils.collectAllInside(module, new VisitorProvider() {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (radixObject instanceof AdsWidgetDef
                        && Objects.equals(((AdsWidgetDef) radixObject).getId(), id)) {
                    return true;
                }
                return false;
            }
        });
        return !result.isEmpty() ? (AdsWidgetDef) result.get(0) : null;
    }
}
