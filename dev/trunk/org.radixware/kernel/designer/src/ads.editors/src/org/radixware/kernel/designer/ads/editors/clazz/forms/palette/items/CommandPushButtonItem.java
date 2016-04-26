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
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.enums.EToolButtonStyle;
import org.radixware.kernel.common.utils.PropertyStore;
import static org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item.register;

public class CommandPushButtonItem extends WidgetItem {

    public static final CommandPushButtonItem DEFAULT = new CommandPushButtonItem();

   

    public CommandPushButtonItem() {
        super(Group.GROUP_RADIX_WIDGETS, NbBundle.getMessage(CommandPushButtonItem.class, "Command_Button"), AdsMetaInfo.COMMAND_PUSH_BUTTON_CLASS);
    }

    @Override
    public Dimension adjustHintSize(RadixObject node, Dimension defaultSize) {
        return super.adjustHintSize(node, defaultSize);
    }

    @Override
    public void paintBackground(Graphics2D graphics, Rectangle rect, RadixObject node) {
        PropertyStore props = new PropertyStore();
        props.set("flat", Boolean.FALSE);
        props.set("default", Boolean.FALSE);

        boolean isChecked = false;
        AdsUIProperty.BooleanProperty checkable = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "checkable");
        if ((checkable != null) && (checkable.value == true)) {
            AdsUIProperty.BooleanProperty checked = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "checked");
            isChecked = checked.value;
        }
        props.set("checked", isChecked);

        paintBackground(graphics, rect, props);
    }

    @Override
    public void paintWidget(Graphics2D graphics, Rectangle rect, RadixObject node) {
        PropertyStore props = new PropertyStore();

        String label = "";
        AdsUIProperty.LocalizedStringRefProperty text = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(node, "text");
        if ((text != null) && (text.getStringId() != null)) {
            label = getTextById(node, text.getStringId());
        } else {
            AdsUIProperty.CommandRefProperty command_prop = (AdsUIProperty.CommandRefProperty) AdsUIUtil.getUiProperty(node, "command");
            if ((command_prop != null) && (command_prop.findCommand() != null)) {
                label = command_prop.findCommand().getTitle(getLanguage());
                if (label == null) {
                    label = NbBundle.getMessage(CheckBoxItem.class, "Title_Not_Specified");
                }
            }
        }
        props.set("text", label);

        //if((label==null)||(label.equals("")))label="Command Button";
        AdsUIProperty.BooleanProperty enabled = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");
        props.set("enabled", enabled.value);

        props.set("toolButtonStyle", EToolButtonStyle.ToolButtonTextBesideIcon);

        props.set("icon", null);
        props.set("iconSize", null);

        paintWidget(graphics, rect, props);
    }

    public void paintWidget(Graphics2D graphics, Rectangle rect, PropertyStore props) {
        PushButtonItem.DEFAULT.getPainter().paintWidget(graphics, rect, props);
    }

    public void paintBackground(Graphics2D graphics, Rectangle rect, PropertyStore props) {
        PushButtonItem.DEFAULT.getPainter().paintBackground(graphics, rect, props);
    }
}
