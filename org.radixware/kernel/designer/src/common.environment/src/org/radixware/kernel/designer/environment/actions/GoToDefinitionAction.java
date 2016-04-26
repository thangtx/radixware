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

import org.radixware.kernel.designer.environment.navigation.*;
import java.util.Collection;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.common.resources.RadixWareIcons;

/**
 * Action - go to definition (calls ChooseObjectDialog).
 */
public final class GoToDefinitionAction extends CallableSystemAction {

    private class BranchesVisitor extends RadixObject {

        @Override
        public void visitChildren(IVisitor visitor, VisitorProvider provider) {
            Collection<Branch> branches = RadixFileUtil.getOpenedBranches();
            for (Branch branch : branches) {
                branch.visit(visitor, provider);
            }
            super.visitChildren(visitor, provider);
        }
    }

    public GoToDefinitionAction() {
        updateEnabled();
    }

    private void updateEnabled() {
        boolean enabled = !RadixFileUtil.getOpenedBranches().isEmpty();
        setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        updateEnabled();
        return super.isEnabled();
    }

    @Override
    public void performAction() {
        BranchesVisitor branchesVisitor = new BranchesVisitor();
        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(branchesVisitor, new GoToDefinitionProvider());
        Definition definition = ChooseDefinition.chooseDefinition(cfg);
        if (definition != null) {
            DialogUtils.goToObject(definition);
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(GoToDefinitionAction.class, "CTL_GoToDefinitionAction");
    }

    @Override
    protected String iconResource() {
        return RadixWareIcons.ARROW.GO_TO_OBJECT.getResourceUri();
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
