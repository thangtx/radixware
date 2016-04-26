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

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Collections;
import java.util.List;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.enums.EOrientation;
import org.radixware.kernel.common.defs.ads.ui.enums.EStandardButton;
import org.radixware.kernel.common.defs.ads.ui.enums.EToolButtonStyle;
import org.radixware.kernel.common.defs.ads.ui.enums.UIEnum;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtUIDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.PropertyStore;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;
import static org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item.register;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;

public class DialogButtonBoxItem extends Item {

    public static final DialogButtonBoxItem DEFAULT = new DialogButtonBoxItem();

   

    private static boolean isButtonBoxVisible = true;

    public DialogButtonBoxItem() {
        super(Group.GROUP_BUTTONS, NbBundle.getMessage(DialogButtonBoxItem.class, "Dialog_Button_Box"), AdsMetaInfo.DIALOG_BUTTON_BOX_CLASS);
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        AdsWidgetDef widget = (AdsWidgetDef) super.createObjectUI(context);
        widget.getProperties().add(AdsMetaInfo.getPropByName(AdsMetaInfo.DIALOG_BUTTON_BOX_CLASS, "standardButtons", context));
        return widget;
    }

    @Override
    public void paintBackground(Graphics2D graphics, Rectangle rect, RadixObject node) {
        paint(graphics, rect, node, true);
    }

    @Override
    public void paintWidget(Graphics2D graphics, Rectangle rect, RadixObject node) {
        paint(graphics, rect, node, false);
    }

    public static void paint(Graphics2D graphics, Rectangle rect, RadixObject node, boolean paintBack) {
        AdsUIProperty.EnumValueProperty orientation = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "orientation");

        AdsUIProperty.BooleanProperty centerButtons = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "centerButtons");
        AdsUIProperty.SetProperty standard_buttons = (AdsUIProperty.SetProperty) AdsUIUtil.getUiProperty(node, "standardButtons");
        AdsUIProperty.IdListProperty action_buttons = (AdsUIProperty.IdListProperty) AdsUIUtil.getUiProperty(node, "actions");
        AdsUIProperty.BooleanProperty enabled = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");

        UIEnum[] buttons = standard_buttons.getValues();
        List<Id> actions = action_buttons == null ? Collections.<Id>emptyList() : action_buttons.getIds();
        if (buttons.length == 1 && buttons[0] == EStandardButton.NoButton && !actions.isEmpty()) {
            buttons = new UIEnum[0];
        }

        int size = buttons.length + actions.size();
        Rectangle btn_rect = rect.getBounds();
        //System.out.println("btn_rect " + rect.getBounds().toString());
        // RadixObject button=PushButtonItem.DEFAULT.createObjectUI(node);
        // AdsMetaInfo.getPropByName(null, null, node)

        AdsUIProperty.RectProperty defaultSize = (AdsUIProperty.RectProperty) AdsMetaInfo.getPropByName(AdsMetaInfo.PUSH_BUTTON_CLASS, "geometry", node);
        setRectSize(btn_rect, rect.width, rect.height, defaultSize.width, defaultSize.height, size, orientation == null ? EOrientation.Horizontal : (EOrientation) orientation.value);

        orderButton(buttons);
        for (int i = 0; i < buttons.length; i++) {
            //AdsWidgetDef node_button=(AdsWidgetDef) PushButtonItem.DEFAULT.createObjectUI(node);
            //node_button.setContainer(AdsUIUtil.getUiDef(node));
            setRectPos(btn_rect, rect, i, size, orientation == null ? EOrientation.Horizontal : (EOrientation) orientation.value, centerButtons == null ? false : centerButtons.value, getButtonIndex((EStandardButton) buttons[i]));
            if (paintBack) {
                paintPushButtonBackground(graphics, btn_rect);
            } else {
                paintPushButton(graphics, btn_rect, node, enabled == null ? true : enabled.value, (EStandardButton) buttons[i]);
            }
        }
        int i = buttons.length;
        if (!actions.isEmpty()) {
            AdsRwtUIDef uiDef = (AdsRwtUIDef) AdsUIUtil.getUiDef(node);
            for (Id id : actions) {
                setRectPos(btn_rect, rect, i, size, orientation == null ? EOrientation.Horizontal : (EOrientation) orientation.value, centerButtons == null ? false : centerButtons.value, 0);
                if (paintBack) {
                    paintPushButtonBackground(graphics, btn_rect);
                } else {
                    AdsRwtWidgetDef target = uiDef.getWidget().findWidgetById(id);
                    if (target != null && target.isActionWidget()) {
                        AdsUIProperty.ImageProperty icon = (AdsUIProperty.ImageProperty) AdsUIUtil.getUiProperty(target, "icon");
                        RadixIcon image = null;
                        if (icon != null) {
                            image = getIconById(node, icon.getImageId());
                        } else {
                            image = RadixWareDesignerIcon.WIDGETS.ARROW_UP;
                        }
                        AdsUIProperty prop = AdsUIUtil.getUiProperty(target, "text");
                        String label = "<untitled>";
                        if (prop instanceof AdsUIProperty.LocalizedStringRefProperty) {
                            Id stringId = ((AdsUIProperty.LocalizedStringRefProperty) prop).getStringId();
                            if (stringId != null) {
                                label = getTextById(node, stringId);
                            }
                        }
                        paintPushButton(graphics, btn_rect, node, enabled == null ? true : enabled.value, label, image);
                    }
                    i++;
                }
            }
        }
    }

    private static void setRectSize(Rectangle btn_rect, int width, int height, int default_width, int default_height, int size, EOrientation orientation) {
        if (orientation == EOrientation.Horizontal) {
            btn_rect.width = width / size - 2;
            if (btn_rect.width > default_width) {
                btn_rect.width = default_width;
            }
            btn_rect.height = default_height;
            if (height < default_height) {
                btn_rect.height = height;
            }
        } else if (orientation == EOrientation.Vertical) {
            btn_rect.height = height / size - 2;
            if (btn_rect.height > default_height) {
                btn_rect.height = default_height;
            }
            btn_rect.width = width;
        }
    }

    private static Rectangle setRectPos(Rectangle btn_rect, Rectangle rect, int index, int size, EOrientation orientation, boolean centerButtons, int button_type) {
        if (orientation == EOrientation.Horizontal) {
            if (centerButtons) {
                btn_rect.x = rect.x + rect.width / 2 - (index + 1 - size / 2) * (btn_rect.width + 2);
                btn_rect.y = rect.y + (rect.height - btn_rect.height) / 2;
            } else {
                btn_rect.y = rect.y + (rect.height - btn_rect.height) / 2;
                if (button_type >= 15) {//привязываем к правому краю
                    btn_rect.x = rect.x + (size - index - 1) * (btn_rect.width + 2);
                } else {
                    btn_rect.x = rect.x + rect.width - (index + 1) * (btn_rect.width + 2);
                }
            }
        } else if (orientation == EOrientation.Vertical) {
            if (centerButtons) {
                btn_rect.x = rect.x;
                btn_rect.y = rect.y + rect.height / 2 - (size / 2 - index) * (btn_rect.height + 2);//rect.y + rect.height/2 - (index+1-size/2)*(btn_rect.height+2);
            } else {
                btn_rect.x = rect.x;
                if (button_type >= 15) {//привязываем к нижнему краю
                    btn_rect.y = rect.y + rect.height - (size - index) * (btn_rect.height + 2);
                } else {
                    btn_rect.y = rect.y + index * (btn_rect.height + 2);
                }
            }
        }
        return btn_rect;
    }

    private static void paintPushButtonBackground(Graphics2D gr, Rectangle rect) {
        PropertyStore props = new PropertyStore();
        props.set("flat", Boolean.FALSE);
        props.set("default", Boolean.FALSE);
        props.set("checked", Boolean.FALSE);

        PushButtonItem.DEFAULT.getPainter().paintBackground(gr, rect, props);
    }

    private static void paintPushButton(Graphics2D graphics, Rectangle rect, RadixObject nodeButton, boolean enabled, EStandardButton standard_button) {
        PropertyStore props = new PropertyStore();

        props.set("toolButtonStyle", EToolButtonStyle.ToolButtonTextBesideIcon);
        props.set("enabled", enabled);
        props.set("text", standard_button.getName());

//        AdsUIProperty.SizeProperty iconSize = (AdsUIProperty.SizeProperty) AdsUIUtil.getUiProperty(node, "iconSize");
        props.set("iconSize", null);
        //setProps(nodeButton,standard_button,enabled);
        //PushButtonItem.DEFAULT.paint(Graphics2D gr, Rectangle r, RadixObject node,String label,EToolButtonStyle toolButtonStyle,RadixIcon image)
        RadixIcon image = Item.getIconById(nodeButton, standard_button.getIconId());
        props.set("icon", image);

        Shape clip = graphics.getClip();
        graphics.clipRect(rect.x, rect.y, rect.width, rect.height);
        PushButtonItem.DEFAULT.getPainter().paintWidget(graphics, rect, props);
        graphics.setClip(clip);

    }

    private static void paintPushButton(Graphics2D graphics, Rectangle rect, RadixObject nodeButton, boolean enabled, String actionTitle, RadixIcon actionIcon) {
        PropertyStore props = new PropertyStore();

        props.set("toolButtonStyle", EToolButtonStyle.ToolButtonTextBesideIcon);
        props.set("enabled", enabled);
        props.set("text", actionTitle);

//        AdsUIProperty.SizeProperty iconSize = (AdsUIProperty.SizeProperty) AdsUIUtil.getUiProperty(node, "iconSize");
        props.set("iconSize", null);
        //setProps(nodeButton,standard_button,enabled);
        //PushButtonItem.DEFAULT.paint(Graphics2D gr, Rectangle r, RadixObject node,String label,EToolButtonStyle toolButtonStyle,RadixIcon image)
//        RadixIcon image = Item.getIconById(nodeButton, standard_button.getIconId());
        props.set("icon", actionIcon);

        Shape clip = graphics.getClip();
        graphics.clipRect(rect.x, rect.y, rect.width, rect.height);
        PushButtonItem.DEFAULT.getPainter().paintWidget(graphics, rect, props);
        graphics.setClip(clip);

    }

    /*private RadixObject setProps(RadixObject nodeButton,EStandardButton standard_buttons,boolean enabled){
     AdsMultilingualStringDef d=AdsMultilingualStringDef.Factory.newInstance();
     d.setValue(EIsoLanguage.ENGLISH, standard_buttons.getName());
     AdsUIProperty.LocalizedStringRefProperty propText=new AdsUIProperty.LocalizedStringRefProperty("text",d.getId().toString());
     AdsUIUtil.getUiDef(nodeButton).findLocalizingBundle().getStrings().getLocal().add(d);
     AdsUIUtil.setUiProperty(nodeButton, propText);
    
     AdsUIProperty.BooleanProperty propEnable = (AdsUIProperty.BooleanProperty)AdsUIUtil.getUiProperty(nodeButton, "enabled");
     propEnable.value=enabled;
     AdsUIUtil.setUiProperty(nodeButton, propEnable);
    
     AdsUIProperty.ImageProperty  propIcon=new AdsUIProperty.ImageProperty("icon", standard_buttons.getIconId().toString());
     AdsUIUtil.setUiProperty(nodeButton, propIcon);
    
     return nodeButton;
     }*/
    private static void orderButton(UIEnum[] buttons) {
        for (int i = 0; i < buttons.length - 1; i++) {
            for (int j = i + 1; j < buttons.length; j++) {
                if (getButtonIndex((EStandardButton) buttons[i]) > getButtonIndex((EStandardButton) buttons[j])) {
                    UIEnum b = buttons[j];
                    buttons[j] = buttons[i];
                    buttons[i] = b;
                }
            }
        }
    }

    private static int getButtonIndex(EStandardButton button) {
        if (button == EStandardButton.Abort) {
            return 5;
        } else if (button == EStandardButton.Apply) {
            return 14;
        } else if (button == EStandardButton.Cancel) {
            return 7;
        } else if (button == EStandardButton.Close) {
            return 6;
        } else if (button == EStandardButton.Discard) {
            return 13;
        } else if (button == EStandardButton.Help) {
            return 17;
        } else if (button == EStandardButton.Ignore) {
            return 12;
        } else if (button == EStandardButton.No) {
            return 2;
            //}else if(button==EStandardButton.NoButton){
            //   return 5;
        } else if (button == EStandardButton.NoToAll) {
            return 3;
        } else if (button == EStandardButton.Ok) {
            return 4;
        } else if (button == EStandardButton.Open) {
            return 10;
        } else if (button == EStandardButton.Reset) {
            return 15;
        } else if (button == EStandardButton.RestoreDefaults) {
            return 16;
        } else if (button == EStandardButton.Retry) {
            return 11;
        } else if (button == EStandardButton.Save) {
            return 8;
        } else if (button == EStandardButton.SaveAll) {
            return 9;
        } else if (button == EStandardButton.Yes) {
            return 0;
        } else if (button == EStandardButton.YesToAll) {
            return 1;
        }
        return 0;
    }
//    public static void paintWidget(Graphics2D graphics, Rectangle rect, PropertyStore props) {
//        PushButtonItem.paintWidget(graphics, rect, props);
//    }
//
//    public static void paintBackground(Graphics2D graphics, Rectangle rect, PropertyStore props) {
//        PushButtonItem.paintBackground(graphics, rect, props);
//    }
}
