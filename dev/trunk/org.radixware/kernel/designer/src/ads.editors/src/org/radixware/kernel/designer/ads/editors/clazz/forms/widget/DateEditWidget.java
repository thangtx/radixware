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

import java.util.List;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.EditProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;


public class DateEditWidget extends BaseWidget {

    public DateEditWidget(GraphSceneImpl scene, AdsWidgetDef node) {
        super(scene, node);
    }

    @Override
    protected List<WidgetAction> getInitialActions(GraphSceneImpl scene, RadixObject node) {
        List<WidgetAction> actions = super.getInitialActions(scene, node);
        actions.add(ActionFactory.createEditAction (new EditProvider() {
            @Override
            public void edit (Widget widget) {
            }
        }));
        return actions;
    }
}
