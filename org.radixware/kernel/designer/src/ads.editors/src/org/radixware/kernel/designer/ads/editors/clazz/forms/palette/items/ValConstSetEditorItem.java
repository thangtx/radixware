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

import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.common.utils.PropertyStore;

public final class ValConstSetEditorItem extends ValEditorItem {

    public static final class ValConstSetEditorPropertyCollector extends ValEditorItem.ValEditorPropertyCollector {

        @Override
        public PropertyStore getBackgroundProperties(RadixObject node, Rectangle rect) {
            PropertyStore props = super.getBackgroundProperties(node, rect);

            int buttonsCount = ((AdsWidgetDef) node).getWidgets().size();

            props.set("buttonsCount", buttonsCount);
            props.set("editable", true);

            return props;
        }

        @Override
        public PropertyStore getWidgetProperties(RadixObject node, Rectangle rect) {
            PropertyStore props = super.getWidgetProperties(node, rect);

            AdsUIProperty.BooleanProperty enabled = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");
            AdsUIProperty.RectProperty geometry = (AdsUIProperty.RectProperty) AdsMetaInfo.getPropByName(AdsUIUtil.getQtClassName(node), "geometry", node);

            assert Utils.isNotNull(enabled, geometry) : "Some of properties are null";

            props.set("label", null);
            props.set("image", null);
            props.set("iconSize", null);
            props.set("enabled", enabled.value);
            props.set("editable", true);
            props.set("widgetHeight", geometry.height);
            props.set("halignment", EAlignment.AlignLeft);
            props.set("valignment", EAlignment.AlignVCenter);

            int buttonsCount = ((AdsWidgetDef) node).getWidgets().size();
            props.set("buttonsCount", buttonsCount);

            return props;
        }
    }

    public static final class ValConstSetEditorPainter extends ValEditorPainter {

        @Override
        public void paintWidget(Graphics2D graphics, Rectangle rect, PropertyStore props) {
            super.paintWidget(graphics, rect, props);

            Rectangle avalRect = props.get("freeAreaRect");
            ComboBoxItem.DEFAULT.getPainter().paintWidget(graphics, avalRect, props);
        }

        @Override
        public void paintBackground(Graphics2D graphics, Rectangle rect, PropertyStore props) {
            super.paintBackground(graphics, rect, props);

            Rectangle avalRect = props.get("freeAreaRect");
            ComboBoxItem.DEFAULT.getPainter().paintBackground(graphics, avalRect, props);
        }

    }

    public static final ValConstSetEditorItem DEFAULT = new ValConstSetEditorItem();

    
    public ValConstSetEditorItem() {
        super(Group.GROUP_INPUT_WIDGETS, NbBundle.getMessage(ValConstSetEditorItem.class, "Val_Const_Set_Editor"), AdsMetaInfo.VAL_CONST_SET_EDITOR_CLASS,
                new ValConstSetEditorPainter(), new ValConstSetEditorPropertyCollector());
        getPainter().setOwnerEditor(this);
    }

    @Override
    public ValConstSetEditorPainter getPainter() {
        return (ValConstSetEditorPainter) super.getPainter();
    }
}
