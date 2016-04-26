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

package org.radixware.kernel.designer.environment.actions;

import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.finder.FindInSourcesResults;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class FindTextInScmlResultsAction  extends CallableSystemAction {
    public FindTextInScmlResultsAction() {
        setIcon(RadixWareIcons.TREE.DEPENDENCIES.getIcon());
    }

    @Override
    public void performAction() {
        FindInSourcesResults.findInstance().open();
        FindInSourcesResults.findInstance().requestActive();
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(FindTextInScmlResultsAction.class, "CTL_FindUsagesResultsAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
