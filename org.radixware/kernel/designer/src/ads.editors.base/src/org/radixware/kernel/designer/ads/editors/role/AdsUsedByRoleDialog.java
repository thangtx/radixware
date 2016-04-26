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

package org.radixware.kernel.designer.ads.editors.role;

import java.awt.Dimension;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.designer.ads.editors.role.AdsUsedByRoleAccessAndCommandsPanel;
import org.radixware.kernel.designer.ads.editors.role.AdsUsedByRoleAccessPanel;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public class AdsUsedByRoleDialog extends ModalDisplayer {

    RadixObject object;

    @Override
    public void apply() {
        if (object instanceof AdsEditorPresentationDef
                || object instanceof AdsSelectorPresentationDef) {
            AdsUsedByRoleAccessAndCommandsPanel panel = (AdsUsedByRoleAccessAndCommandsPanel) getComponent();
            panel.apply();
        } else {
            AdsUsedByRoleAccessPanel panel = (AdsUsedByRoleAccessPanel) getComponent();
            panel.apply();
        }
    }

    @Override
    public boolean showModal() {
        return super.showModal();
    }

    public AdsUsedByRoleDialog(RadixObject object) {
        super(object instanceof AdsEditorPresentationDef || object instanceof AdsSelectorPresentationDef ? new AdsUsedByRoleAccessAndCommandsPanel(object) : new AdsUsedByRoleAccessPanel(object),
                "Access to Definition");
        this.object = object;
        this.getComponent().setMinimumSize(new Dimension(200, 200));
    }
}
