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

import java.util.List;
import java.util.Arrays;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.utils.RadixResourceBundle;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.*;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.EditorDialog.EditorPanel;


class EditorFormCreatorItem extends CommonFormItem {

    public static final String CLASS_NAME = AdsAppObject.EDITOR_FORM_CLASS_NAME;

    public EditorFormCreatorItem() {
        super(CLASS_NAME, Group_.GROUP_APPBLOCKS, RadixResourceBundle.getMessage(EditorFormCreatorItem.class, "EditorFormCreator"), AdsDefinitionIcon.WORKFLOW.EDITOR_FORM_CREATOR);
    }

    private static final List<ERestriction> RESTRICTIONS = Arrays.asList(
            ERestriction.ANY_COMMAND,
            ERestriction.CREATE,
            ERestriction.DELETE,
            ERestriction.UPDATE
            );

    @Override
    public EditorPanel<AdsAppObject> getPanel(AdsAppObject node) {
        return new TabbedPanel(node,
                Arrays.asList(
                (EditorDialog.EditorPanel)new PreExecutePanel(node),
                (EditorDialog.EditorPanel)new OverdueExecutePanel(node),
                (EditorDialog.EditorPanel)new PostExecutePanel(node),
                (EditorDialog.EditorPanel)new FormCommonPanel(node),
                (EditorDialog.EditorPanel)new FormEditorPanel(node),
                (EditorDialog.EditorPanel)new RestrictionsPanel(node, RESTRICTIONS),
                (EditorDialog.EditorPanel)new FormDelaysPanel(node)
                ));
    }

    @Override
    public void sync(AdsAppObject node) {
        super.sync(node);
    }
}