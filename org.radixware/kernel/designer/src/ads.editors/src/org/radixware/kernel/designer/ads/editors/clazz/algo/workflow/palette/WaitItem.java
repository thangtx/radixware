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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.palette;

import java.util.Arrays;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.EditorDialog.EditorPanel;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.*;


class WaitItem extends AppItem {

    public static final String CLASS_NAME = AdsAppObject.WAIT_CLASS_NAME;

    public WaitItem() {
        super(CLASS_NAME, Group_.GROUP_ALGBLOCKS, NbBundle.getMessage(AppItem.class, "Wait"), AdsDefinitionIcon.WORKFLOW.WAIT);
    }

    @Override
    public EditorPanel<AdsAppObject> getPanel(AdsAppObject node) {
        return new TabbedPanel(node,
                Arrays.asList(
                (EditorDialog.EditorPanel)new PreExecutePanel(node),
                (EditorDialog.EditorPanel)new PostExecutePanel(node),
                (EditorDialog.EditorPanel)new WaitPanel(node)
                ));
    }

    @Override
    public void sync(AdsAppObject node) {
    }
}