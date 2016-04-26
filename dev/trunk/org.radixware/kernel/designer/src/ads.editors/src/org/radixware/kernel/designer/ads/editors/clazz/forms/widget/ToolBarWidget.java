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

package org.radixware.kernel.designer.ads.editors.clazz.forms.widget;

import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.ToolBarLayoutProcessor;


public class ToolBarWidget extends BaseWidget {

    public ToolBarWidget(GraphSceneImpl scene, AdsWidgetDef node) {
        super(scene, node);

        setLayoutProcessor(new ToolBarLayoutProcessor(this, (AdsWidgetDef) AdsUIUtil.currentWidget(node)));
    }

    @Override
    public boolean isContainer() {
        return true;
    }

    @Override
    boolean isContainerFor(String clazz) {
        if (AdsMetaInfo.SPACER_CLASS.equals(clazz)) {
            return false;
        }
        return super.isContainerFor(clazz);
    }

    @Override
    public boolean canChangeLayout() {
        return false;
    }
}
