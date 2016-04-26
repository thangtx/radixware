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

package org.radixware.kernel.designer.ads.build.actions;

import javax.swing.Action;
import javax.swing.KeyStroke;
import org.radixware.kernel.common.builder.BuildActionExecutor.EBuildActionType;


public class CleanAndBuildAction extends AbstractBuildAction {

    public CleanAndBuildAction() {
        super();
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("shift F11"));
    }

    @Override
    protected EBuildActionType getBuildActionType() {
        return EBuildActionType.CLEAN_AND_BUILD;
    }

    @Override
    public String getName() {
        return "Clean and Build";
    }

    @Override
    protected String iconResource() {
        return "org/netbeans/modules/project/ui/resources/rebuildProject.png";
    }
}
