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

package org.radixware.kernel.designer.environment.project;

import javax.swing.Action;
import org.netbeans.spi.project.ActionProvider;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.designer.ads.build.actions.AbstractBuildAction;
import org.radixware.kernel.designer.ads.build.actions.CompileDefinitionAction;
import org.radixware.kernel.designer.debugger.RadixDebugger;


public class RadixActionProvider implements ActionProvider {

    @Override
    public String[] getSupportedActions() {
        if (branch != null) {
            return new String[]{
                        ActionProvider.COMMAND_COMPILE_SINGLE,
                        ActionProvider.COMMAND_DEBUG};
        } else {
            return new String[]{
                        ActionProvider.COMMAND_COMPILE_SINGLE};
        }
    }

    public RadixActionProvider() {
        this.branch = null;
    }
    private final Branch branch;

    RadixActionProvider(Branch branch) {
        this.branch = branch;
    }

    @Override
    public void invokeAction(final String command, final Lookup context) throws IllegalArgumentException {
        if (ActionProvider.COMMAND_DEBUG.equals(command)) {
            if (branch != null) {
                RadixDebugger.debug(branch);
            }
        } else if (ActionProvider.COMMAND_COMPILE_SINGLE.equals(command)) {
            final Node node = context.lookup(Node.class);
            if (node != null) {
                for (Action action : node.getActions(true)) {
                    if (action instanceof CompileDefinitionAction) {
                        final CompileDefinitionAction compileDefinitionAction = (CompileDefinitionAction) action;
                        compileDefinitionAction.performAction(node);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public boolean isActionEnabled(final String command, final Lookup context) throws IllegalArgumentException {
        //System.out.println(command);
        switch (command) {
            case ActionProvider.COMMAND_DEBUG:
                return branch != null;
            case ActionProvider.COMMAND_COMPILE_SINGLE:
                final Node node = context.lookup(Node.class);
                if (node != null) {
                    final AbstractBuildAction.CompileCookie compileCookie = node.getLookup().lookup(AbstractBuildAction.CompileCookie.class);
                    return compileCookie != null;
                } else {
                    return false;
                }
            default:
                throw new IllegalArgumentException("Command not supported: '" + String.valueOf(command) + "'");
        }
    }
}
