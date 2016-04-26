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

package org.radixware.kernel.designer.debugger.impl.ui;

import javax.swing.Action;
import org.netbeans.spi.debugger.ContextProvider;
import org.netbeans.spi.viewmodel.Models;
import org.netbeans.spi.viewmodel.NodeActionsProvider;
import org.netbeans.spi.viewmodel.UnknownTypeException;
import org.radixware.kernel.designer.debugger.impl.CallStackFrame;


public class CallStackActionProvider implements NodeActionsProvider {

    private final Action MAKE_CURRENT_ACTION = Models.createAction("Make current", new Models.ActionPerformer() {

        @Override
        public boolean isEnabled(Object node) {
            if (node instanceof CallStackFrame) {
                CallStackFrame w = (CallStackFrame) node;
                return !w.isCurrent();
            }
            return false;
        }

        @Override
        public void perform(Object[] nodes) {
            if (nodes.length == 1 && (nodes[0] instanceof CallStackFrame)) {
                CallStackFrame w = (CallStackFrame) nodes[0];
                w.setCurrent();
            }
        }
    }, Models.MULTISELECTION_TYPE_EXACTLY_ONE);

    public CallStackActionProvider(ContextProvider lookupProvider) {
    }

    @Override
    public Action[] getActions(Object node) throws UnknownTypeException {
        if (node instanceof CallStackFrame) {
            return new Action[]{MAKE_CURRENT_ACTION};
        } else {
            return new Action[0];
        }
    }

    @Override
    public void performDefaultAction(Object node) throws UnknownTypeException {
        if (node instanceof CallStackFrame) {
            ((CallStackFrame) node).setCurrent();
            ((CallStackFrame) node).tryToOpenContext();            
        }
    }
}
