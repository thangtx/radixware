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

import java.awt.Rectangle;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.common.utils.PropertyStore;

public class ValTimeIntervalEditorItem extends ValEditorItem {

    public static final class ValTimeIntervalEditorPropertyCollector extends ValEditorItem.ValEditorPropertyCollector {

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

    public static final class ValTimeIntervalEditorPainter extends ValEditorPainter {

    }

    public static final ValTimeIntervalEditorItem DEFAULT = new ValTimeIntervalEditorItem();

  

    public ValTimeIntervalEditorItem() {
        super(Group.GROUP_INPUT_WIDGETS, NbBundle.getMessage(ValTimeIntervalEditorItem.class, "Val_Time_Interval_Editor"), AdsMetaInfo.VAL_TIMEINTERVAL_EDITOR_CLASS,
                new ValTimeIntervalEditorPainter(), new ValTimeIntervalEditorPropertyCollector());

        getPainter().setOwnerEditor(this);
    }

    @Override
    public final ValTimeIntervalEditorPainter getPainter() {
        return (ValTimeIntervalEditorPainter) super.getPainter();
    }
}
