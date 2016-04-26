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

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil.IVisitorUI;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;


public class UserWidget extends BaseWidget {

    public UserWidget(GraphSceneImpl scene, RadixObject node) {
        super(scene, node);
        init(scene, node);
    }

    public void init(final GraphSceneImpl scene, RadixObject node) {

        AdsUIUtil.visitUI(node, new IVisitorUI() {

            @Override
            public void visit(RadixObject node, boolean active) {
                String classUI = AdsUIUtil.getUiClassName(node);
                if (!classUI.startsWith("wdc")) {
                    BaseWidget wg = BaseWidget.Factory.newInstance(scene, node);
                    wg.restoreGeometry();
                    wg.setVisible(active);
                    addChild(wg, node);
                }
            }
        }, true);

    }

    @Override
    protected void paintWidget() {
        super.paintWidget();
    }


}
