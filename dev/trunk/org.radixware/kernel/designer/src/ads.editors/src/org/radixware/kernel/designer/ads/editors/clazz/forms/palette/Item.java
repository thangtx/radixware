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
package org.radixware.kernel.designer.ads.editors.clazz.forms.palette;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.lang.ref.SoftReference;
import java.util.*;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.ui.*;
import org.radixware.kernel.common.defs.ads.ui.enums.EOrientation;
import org.radixware.kernel.common.defs.ads.ui.enums.EStandardButton;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtItemWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.ads.editors.clazz.forms.Settings;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.LayoutUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.*;

public class Item {

    private final static ArrayList<Item> items = new ArrayList<>();
    private final static ArrayList<Item> webItems = new ArrayList<>();
    private final static HashMap<String, Item> hash = new HashMap<>();
    private static final int MAX_HASH_SIZE = 0xFF;
    private static final Map<Id, SoftReference<AdsMultilingualStringDef>> hashString = new HashMap<>(MAX_HASH_SIZE);
    private static final Map<Id, SoftReference<RadixIcon>> hashIcon = new HashMap<>(MAX_HASH_SIZE);

    static void registerCustom(List<Item> customItems) {
        synchronized (items) {
            Set<Item> removeItems = new HashSet<>();
            List<Item> itemList = new ArrayList<>(items);
            for (Item item : itemList) {
                if (Group.GROUP_CUSTOM_WIDGETS.equals(item.getGroup())) {
                    removeItems.add(item);
                    hash.remove(item.getClazz());
                }
            }
            items.removeAll(removeItems);
            for (Item item : customItems) {
                hash.put(item.getClazz(), item);
            }
            items.addAll(customItems);
        }
    }

    static void registerCustomWeb(List<Item> customItems) {
        Set<Item> removeItems = new HashSet<>();
        synchronized (webItems) {
            for (Item item : new ArrayList<>(webItems)) {
                if (Group.GROUP_WEB_CUSTOM_WIDGETS.equals(item.getGroup())) {
                    removeItems.add(item);
                    hash.remove(item.getClazz());
                }
            }
            webItems.removeAll(removeItems);
            for (Item item : customItems) {
                hash.put(item.getClazz(), item);
            }
            webItems.addAll(customItems);
        }
    }

    public static void register(Item item) {
        synchronized (items) {
            if (item.getGroup() != null) {
                items.add(item);
            }
            hash.put(item.getClazz(), item);
        }
    }

    public static void registerWeb(Item item) {
        synchronized (webItems) {
            if (item.getGroup() != null) {
                webItems.add(item);
            }
            hash.put(item.getClazz(), item);
        }
    }

    public static Item getItem(RadixObject node) {
        int x;
        if (node == null) {
            x = 0;
        }
        if (AdsUIUtil.isUIRoot(node)) {
            return RootItem.DEFAULT;
        }
        if (node instanceof AdsLayout.SpacerItem) {
            EOrientation ori = EOrientation.Horizontal;
            AdsUIProperty.EnumValueProperty p = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "orientation");
            if (p != null) {
                ori = (EOrientation) p.value;
            }
            if (ori.equals(EOrientation.Horizontal)) {
                return HorizontalSpacerItem.DEFAULT;
            } else {
                return VerticalSpacerItem.DEFAULT;
            }
        }
        Item item = hash.get(AdsUIUtil.getUiClassName(node));
        if (item == null) {
            item = CustomItem.DEFAULT;
        }
        return item;
    }

    public static List<Item> getItems(ERuntimeEnvironmentType env) {
        return Collections.unmodifiableList(env == ERuntimeEnvironmentType.EXPLORER ? items : webItems);
    }

    protected static EIsoLanguage getLanguage() {
        return Settings.getLanguage();
    }

    private static AdsMultilingualStringDef getMlsById(RadixObject node, Id id) {
        if (id == null) {
            return null;
        }

        AdsMultilingualStringDef mls = null;
        SoftReference<AdsMultilingualStringDef> refMls = hashString.get(id);
        if (refMls != null) {
            mls = refMls.get();
        }

        if (mls == null) {
            AdsAbstractUIDef ui = AdsUIUtil.getUiDef(node);

            if (ui != null) {
                AdsLocalizingBundleDef bundle = ui.findExistingLocalizingBundle();
                if (bundle != null) {
                    mls = bundle.getStrings().getLocal().findById(id);
                }
                if (mls != null) {
                    if (hashString.size() >= MAX_HASH_SIZE) {
                        hashString.clear();
                    }
                    hashString.put(id, new SoftReference<>(mls));
                }
            }
        }
        return mls;
    }

    public static String getTextById(RadixObject node, Id id) {
        String text = null;
        AdsMultilingualStringDef mls = getMlsById(node, id);
        if (mls != null) {
            text = mls.getValue(getLanguage());
        }
        return (String) Utils.nvl(text, "");
    }

    public static void setTextById(RadixObject node, Id id, String text) {
        AdsMultilingualStringDef mls = getMlsById(node, id);
        if (mls != null) {
            mls.setValue(getLanguage(), text);
        }
    }

    public static RadixIcon getIconById(RadixObject node, Id id) {
        if (id == null) {
            return null;
        }

        RadixIcon icon = null;
        SoftReference<RadixIcon> refIcon = hashIcon.get(id);
        if (refIcon != null) {
            icon = refIcon.get();
        }

        if (icon == null) {
            EStandardButton sb;
            try {
                sb = EStandardButton.getForValue(id.toString().substring(3));
            } catch (NoConstItemWithSuchValueError e) {
                sb = null;
            }
            if (sb != null) {
                switch (sb) {
                    case Ok:
                        icon = RadixWareIcons.WIDGETS.STANDARD_OK;
                        break;
                    case Cancel:
                        icon = RadixWareIcons.WIDGETS.STANDARD_CANCEL;
                        break;
                    case Apply:
                        icon = RadixWareIcons.WIDGETS.STANDARD_APPLY;
                        break;
                    case Close:
                        icon = RadixWareIcons.WIDGETS.STANDARD_CLOSE;
                        break;
                    case Help:
                        icon = RadixWareIcons.WIDGETS.STANDARD_HELP;
                        break;
                    case No:
                        icon = RadixWareIcons.WIDGETS.STANDARD_NO;
                        break;
                    case Open:
                        icon = RadixWareIcons.WIDGETS.STANDARD_OPEN;
                        break;
                    case Save:
                        icon = RadixWareIcons.WIDGETS.STANDARD_SAVE;
                        break;
                    case Yes:
                        icon = RadixWareIcons.WIDGETS.STANDARD_YES;
                        break;
                }
            }

            if (sb == null && icon == null) {
                AdsAbstractUIDef ui = AdsUIUtil.getUiDef(node);
                AdsImageDef imageDef = AdsSearcher.Factory.newImageSearcher(ui).findById(id).get();
                if (imageDef != null) {
                    icon = imageDef.getIcon();
                }
            }

            if (icon != null) {
                if (hashIcon.size() >= MAX_HASH_SIZE) {
                    hashIcon.clear();
                }
                hashIcon.put(id, new SoftReference<>(icon));
            }
        }

        return icon;
    }

    /*
     * register items
     */
    static {
        Registry.initialize();
    }
    private Group group;
    private String title;
    private RadixIcon icon;
    private String clazz;

    protected Item(Group group, String title, RadixIcon icon, String clazz) {
        this.group = group;
        this.title = title;
        this.icon = icon;
        this.clazz = clazz;

    }

    public Item(Group group, String title, String clazz) {
        this(group, title, AdsDefinitionIcon.WIDGETS.calcIcon(clazz), clazz);
    }

    public RadixObject createObjectUI(RadixObject context) {
        AdsAbstractUIDef uidef = AdsUIUtil.getUiDef(context);
        if (uidef == null) {
            return null;
        } else {
            if (uidef.getUsageEnvironment() == ERuntimeEnvironmentType.EXPLORER) {
                if (group.equals(Group.GROUP_LAYOUTS)) {
                    AdsLayout layout = new AdsLayout(clazz);
                    AdsUIUtil.getUiProperties(layout).add(new AdsUIProperty.IntProperty("layoutLeftMargin", 0));
                    AdsUIUtil.getUiProperties(layout).add(new AdsUIProperty.IntProperty("layoutTopMargin", 0));
                    AdsUIUtil.getUiProperties(layout).add(new AdsUIProperty.IntProperty("layoutRightMargin", 0));
                    AdsUIUtil.getUiProperties(layout).add(new AdsUIProperty.IntProperty("layoutBottomMargin", 0));
                    return layout;
                }
                if (group.equals(Group.GROUP_SPACERS)) {
                    return new AdsLayout.SpacerItem();
                }
                if (group.equals(Group.GROUP_ITEM_WIDGETS)) {
                    return new AdsItemWidgetDef(clazz);
                }
                return new AdsWidgetDef(clazz);
            } else {
                if (AdsMetaInfo.RWT_UI_LIST.equals(clazz) || AdsMetaInfo.RWT_UI_GRID.equals(clazz)) {
                    return new AdsRwtItemWidgetDef(clazz);
                }
                return new AdsRwtWidgetDef(clazz);
            }
        }

    }

    public String getClazz() {
        return clazz;
    }

    public Group getGroup() {
        return group;
    }

    public String getTitle() {
        return title;
    }

    public RadixIcon getIcon() {
        return icon;
    }

    public Dimension adjustHintSize(RadixObject node, Dimension defaultSize) {
        return defaultSize;
    }

    public Rectangle adjustLayoutGeometry(RadixObject node, Rectangle defaultRect) {
        AdsLayout layout = null;
        if (node instanceof AdsWidgetDef) {
            layout = ((AdsWidgetDef) AdsUIUtil.currentWidget((AdsWidgetDef) node)).getLayout();
        }
        if (node instanceof AdsLayout) {
            layout = ((AdsLayout) node);
        }

        if (layout == null) {
            return defaultRect.getBounds();
        }

        Rectangle r = defaultRect.getBounds();
        int left = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutLeftMargin")).value;
        int top = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutTopMargin")).value;
        int right = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutRightMargin")).value;
        int bottom = ((AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(layout, "layoutBottomMargin")).value;

        r.x += Math.max(left, LayoutUtil.MIN_MARGIN);
        r.y += Math.max(top, LayoutUtil.MIN_MARGIN);
        r.width -= Math.max(left + right, LayoutUtil.MIN_MARGIN * 2);
        r.height -= Math.max(top + bottom, LayoutUtil.MIN_MARGIN * 2);

        return r;
    }

    public Rectangle adjustContentGeometry(RadixObject node, Rectangle rect) {
        return rect;
    }

    public void paintBackground(Graphics2D graphics, Rectangle rect, RadixObject node) {
        graphics.setColor(Color.WHITE);
        graphics.fill(rect);
    }

    public void paintBorder(Graphics2D graphics, Rectangle rect, RadixObject node) {
    }

    public void paintWidget(Graphics2D graphics, Rectangle rect, RadixObject node) {
    }
}
