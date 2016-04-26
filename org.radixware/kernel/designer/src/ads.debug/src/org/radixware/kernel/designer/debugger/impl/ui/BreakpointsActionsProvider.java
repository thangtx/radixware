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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.spi.viewmodel.Models;
import org.netbeans.spi.viewmodel.NodeActionsProvider;
import org.netbeans.spi.viewmodel.NodeActionsProviderFilter;
import org.netbeans.spi.viewmodel.UnknownTypeException;
import org.openide.text.NbDocument;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.debugger.breakpoints.JmlBreakpoint;
import org.radixware.kernel.designer.debugger.breakpoints.RadixBreakpoint;


public class BreakpointsActionsProvider implements NodeActionsProviderFilter {

    private static final Action GO_TO_ACTION = Models.createAction(
            "Go to source", // NOI18N
            new Models.ActionPerformer() {

        @Override
        public boolean isEnabled(Object node) {
            return node instanceof RadixBreakpoint;
        }

        @Override
        public void perform(Object[] nodes) {
            if (nodes.length == 1 && nodes[0] instanceof RadixBreakpoint) {
                goTo((RadixBreakpoint) nodes[0]);
            }
        }
    },
            Models.MULTISELECTION_TYPE_EXACTLY_ONE);

    @Override
    public void performDefaultAction(NodeActionsProvider original, Object node) throws UnknownTypeException {
        //
    }

    @Override
    public Action[] getActions(NodeActionsProvider original, Object node) throws UnknownTypeException {
        if (node instanceof RadixBreakpoint) {
            Action[] actions = original.getActions(node);
            Action[] self = new Action[actions.length + 1];
            System.arraycopy(actions, 0, self, 1, actions.length);
            self[0] = GO_TO_ACTION;
            return self;
        } else {
            return original.getActions(node);
        }
    }

    private static void goTo(final RadixBreakpoint bp) {
       
        final RadixObject ro = bp.getRadixObject();
        if (ro != null) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    DialogUtils.goToObject(ro, new OpenInfo(ro, Lookups.fixed()));
                    if (bp instanceof JmlBreakpoint) {
                        final Jml jml = ((JmlBreakpoint) bp).getJml();
                        try {

                            final JTextComponent c = EditorManagerProxy.findComponent(jml);
                            if (c != null) {
                                c.getCaret().setDot(NbDocument.findLineOffset((StyledDocument) c.getDocument(), ((JmlBreakpoint) bp).getLineNumber()));
                            }
                        } catch (Throwable ex) {
                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                    }
                }
            });
        }
    }
}
