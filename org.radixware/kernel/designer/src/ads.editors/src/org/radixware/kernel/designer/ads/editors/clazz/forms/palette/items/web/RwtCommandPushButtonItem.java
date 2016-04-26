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

package org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web;

import java.awt.Dimension;
import java.awt.Rectangle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.enums.EToolButtonStyle;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.PushButtonItem.PushButtonPainter;
import org.radixware.kernel.common.utils.PropertyStore;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.WidgetItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.WidgetPropertyCollector;


public class RwtCommandPushButtonItem extends WidgetItem {

    public static final RwtCommandPushButtonItem DEFAULT = new RwtCommandPushButtonItem();

    private static class CommandButtonPropertyStore extends WidgetPropertyCollector {

        CommandButtonPropertyStore() {
        }

        @Override
        public PropertyStore getGeneralProperties(RadixObject node, Rectangle rect) {
            final PropertyStore properties = new PropertyStore();
            AdsRwtWidgetDef w = (AdsRwtWidgetDef) node;
            AdsUIProperty.CommandRefProperty command = (AdsUIProperty.CommandRefProperty) w.getProperties().getByName("command");
            properties.set("toolButtonStyle", EToolButtonStyle.ToolButtonTextBesideIcon);
            if (command == null || command.findCommand() == null) {
                properties.set("label", "<No command>");
                properties.set("image", null);
                properties.set("iconSize", new Dimension(13, 13));

            } else {
                AdsCommandDef cmd = command.findCommand();
                properties.set("label", cmd.getName());
                AdsImageDef image = AdsSearcher.Factory.newImageSearcher(cmd).findById(cmd.getPresentation().getIconId()).get();
                properties.set("image", image == null ? null : image.getIcon());
                properties.set("iconSize", new Dimension(13, 13));
            }
            return properties;
        }
    }

    public RwtCommandPushButtonItem() {
        super(Group.GROUP_WEB_RADIX_WIDGETS, "Command Push Button", AdsMetaInfo.RWT_COMMAND_PUSH_BUTTON, new PushButtonPainter(), new CommandButtonPropertyStore());
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        AdsRwtWidgetDef widget = (AdsRwtWidgetDef) super.createObjectUI(context);
        return widget;
    }
}
