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

package org.radixware.kernel.designer.ads.editors.refactoring.pullup;

import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.IAdsClassMember;
import org.radixware.kernel.designer.ads.editors.refactoring.RefactoringWizard;


public abstract class PullUpAction extends CookieAction {

    @Override
    public String getName() {
        return "Pull Up";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }

    @Override
    public boolean isEnabled() {
        final Node[] activatedNodes = this.getActivatedNodes();
        if (activatedNodes.length > 0) {
            final Node node = activatedNodes[0];

            if (node != null) {
                final AdsDefinition member = node.getLookup().lookup(AdsDefinition.class);

                if (member instanceof IAdsClassMember) {
                    return !member.isReadOnly();
                }
            }
        }
        
        return false;
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected final void performAction(Node[] activatedNodes) {
        if (activatedNodes.length > 0) {
            final Node node = activatedNodes[0];

            if (node != null) {
                final AdsDefinition member = node.getLookup().lookup(AdsDefinition.class);

                if (member instanceof IAdsClassMember) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            RefactoringWizard.showModal(new PullUpProcessor(member));
                        }
                    });
                }
            }
        }
    }
}
