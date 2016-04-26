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

package org.radixware.kernel.designer.ads.editors.refactoring.replace;

import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.designer.ads.editors.refactoring.RefactoringWizard;


public abstract class ReplaceAction extends CookieAction {

    @Override
    public String getName() {
        return "Replace";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
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
                final AdsDefinition member = node.getLookup().lookup(AdsClassMember.class);

                if (member instanceof AdsMethodDef || member instanceof AdsPropertyDef) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            RefactoringWizard.showModal(new ReplaceProcessor(member));
                        }
                    });
                }
            }
        }
    }
}
