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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.EditorPageItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.EmbeddedEditorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.EmbeddedSelectorItem;


public class RwtEmbeddedSelectorItem extends Item {

    public static final RwtEmbeddedSelectorItem DEFAULT = new RwtEmbeddedSelectorItem();

    public RwtEmbeddedSelectorItem() {
        super(Group.GROUP_WEB_RADIX_WIDGETS, NbBundle.getMessage(EmbeddedSelectorItem.class, "Embedded_Selector"), AdsMetaInfo.RWT_EMBEDDED_SELECTOR);
    }

    @Override
    public Dimension adjustHintSize(RadixObject node, Dimension defaultSize) {
        return super.adjustHintSize(node, defaultSize);
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        EditorPageItem.DEFAULT.paintBackground(gr, r, node, Color.WHITE, new Color(164,203,255));
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        String label = NbBundle.getMessage(EmbeddedSelectorItem.class, "Embedded_Selector");
        AdsUIProperty.EmbeddedSelectorOpenParamsProperty openParams = (AdsUIProperty.EmbeddedSelectorOpenParamsProperty) AdsUIUtil.getUiProperty(node, "openParams");
        AdsPropertyDef prop = openParams.findProperty();
        if (prop != null)/*&&(prop instanceof AdsPropertyDef)) {
        AdsPropertyDef sProp = (AdsPropertyDef)prop;
        label = sProp.getPresentation().getTitle(getLanguage());*/ {
            label = prop.getName();
        } else {
            AdsExplorerItemDef explorerItem = openParams.findExplorerItem();
            if (explorerItem != null) {
                label = explorerItem.getName();
            }
        }
        EditorPageItem.DEFAULT.paint(gr, r, label);
    }
}
