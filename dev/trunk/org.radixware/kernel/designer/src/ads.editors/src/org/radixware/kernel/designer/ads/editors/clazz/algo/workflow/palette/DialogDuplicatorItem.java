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
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject;
import org.radixware.kernel.common.utils.RadixResourceBundle;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.*;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.EditorDialog.EditorPanel;


class DialogDuplicatorItem extends CommonFormItem {

    public static final String CLASS_NAME = AdsAppObject.DIALOG_DUPLICATOR_CLASS_NAME;

    public DialogDuplicatorItem() {
        super(CLASS_NAME, Group_.GROUP_APPBLOCKS, RadixResourceBundle.getMessage(DialogDuplicatorItem.class, "DialogDuplicator"), AdsDefinitionIcon.WORKFLOW.DIALOG_DUPLICATOR);
    }

    @Override
    public EditorPanel<AdsAppObject> getPanel(AdsAppObject node) {
        DialogDuplicatorUserPropPanel userPropPanel = new DialogDuplicatorUserPropPanel(node);
        return new TabbedPanel(node,
                Arrays.asList(
                (EditorDialog.EditorPanel)new PreExecutePanel(node),
                (EditorDialog.EditorPanel)new OverdueExecutePanel(node),
                (EditorDialog.EditorPanel)new PostExecutePanel(node),
                (EditorDialog.EditorPanel)new DialogDuplicatorCommonPanel(node),
                (EditorDialog.EditorPanel)new DialogDuplicatorPanel(node, userPropPanel),
                (EditorDialog.EditorPanel)new OverridePanel(node),
                (EditorDialog.EditorPanel)userPropPanel,
                (EditorDialog.EditorPanel)new FormDelaysPanel(node)
                ));
    }

    @Override
    public void sync(AdsAppObject node) {
        super.sync(node);
    }
}
