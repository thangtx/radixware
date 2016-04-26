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

import java.awt.event.ActionEvent;
import javax.lang.model.type.UnknownTypeException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.netbeans.spi.debugger.ContextProvider;
import org.netbeans.spi.viewmodel.Models;
import org.netbeans.spi.viewmodel.NodeActionsProvider;
import org.radixware.kernel.designer.debugger.impl.KernelSourcePath;
import org.radixware.kernel.designer.debugger.impl.KernelSourcePathProvider;


public class SourcesActionProvider implements NodeActionsProvider {

    private final Action ADD_ROOT = new AbstractAction("Add source root") {

        @Override
        public void actionPerformed(ActionEvent e) {
            KernelSourcePathProvider.getInstance().addSourceRoot();
        }
    };
    private final Action REMOVE_ROOT = Models.createAction("Remove source root", new Models.ActionPerformer() {

        @Override
        public boolean isEnabled(Object node) {
            if (node instanceof KernelSourcePath) {
                return true;
            }
            return false;
        }

        @Override
        public void perform(Object[] nodes) {
            for (Object node : nodes) {
                if (node instanceof KernelSourcePath) {
                    KernelSourcePathProvider.getInstance().removeSourceRoot((KernelSourcePath) node);
                }
            }
        }
    }, Models.MULTISELECTION_TYPE_ANY);

    public SourcesActionProvider(ContextProvider lookupProvider) {
    }

    @Override
    public Action[] getActions(Object node) throws UnknownTypeException {
        return new Action[]{ADD_ROOT, REMOVE_ROOT};
    }

    @Override
    public void performDefaultAction(Object node) throws UnknownTypeException {
    }
}
