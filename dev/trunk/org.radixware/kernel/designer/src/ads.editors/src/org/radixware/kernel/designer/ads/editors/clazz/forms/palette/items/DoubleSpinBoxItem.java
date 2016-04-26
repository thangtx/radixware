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

import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;

public class DoubleSpinBoxItem extends Item {

    public static final DoubleSpinBoxItem DEFAULT = new DoubleSpinBoxItem();

   

    public DoubleSpinBoxItem() {
        super(Group.GROUP_INPUT_WIDGETS, NbBundle.getMessage(DoubleSpinBoxItem.class, "Double_Spin_Box"), AdsMetaInfo.DOUBLE_SPIN_BOX_CLASS);
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        SpinBoxItem.DEFAULT.paintBackground(gr, r, node);
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        AdsUIProperty.DoubleProperty value = (AdsUIProperty.DoubleProperty) AdsUIUtil.getUiProperty(node, "value");

        AdsUIProperty.DoubleProperty minimum = (AdsUIProperty.DoubleProperty) AdsUIUtil.getUiProperty(node, "minimum");
        boolean isCurValMin = (value.value <= minimum.value);

        AdsUIProperty.DoubleProperty maximum = (AdsUIProperty.DoubleProperty) AdsUIUtil.getUiProperty(node, "maximum");
        boolean isCurValMax = (value.value >= maximum.value);

        String label = getLabel(node, Double.valueOf(value.value).toString());
        SpinBoxItem.DEFAULT.paint(gr, r, node, label, isCurValMin, isCurValMax);
    }

    private String getLabel(RadixObject node, String strValue) {
        AdsUIProperty.LocalizedStringRefProperty suffix_prop = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(node, "suffix");
        String suffix = getTextById(node, suffix_prop.getStringId());

        AdsUIProperty.LocalizedStringRefProperty prefix_prop = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(node, "prefix");
        String prefix = getTextById(node, prefix_prop.getStringId());

        AdsUIProperty.IntProperty decimals = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(node, "decimals");
        if (decimals.value == 0) {
            strValue = strValue.substring(0, strValue.indexOf("."));
        } else {
            if (strValue.indexOf(".") != -1) {
                String s = strValue.substring(strValue.indexOf(".") + 1, strValue.length());
                if (s.length() < decimals.value) {
                    for (int i = 0; i < decimals.value - s.length(); i++) {
                        strValue += "0";
                    }
                } else {
                    strValue = strValue.substring(0, strValue.indexOf(".") + decimals.value + 1);
                }
            } else {
                strValue += ".";
                for (int i = 0; i < decimals.value; i++) {
                    strValue += "0";
                }
            }
        }
        return prefix + strValue + suffix;
    }
}
