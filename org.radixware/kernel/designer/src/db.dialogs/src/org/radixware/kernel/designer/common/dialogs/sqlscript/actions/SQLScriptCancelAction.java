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

package org.radixware.kernel.designer.common.dialogs.sqlscript.actions;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.Lookup;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.actions.AbstractRadixAction;
import org.radixware.kernel.designer.common.dialogs.actions.AbstractRadixContextAwareAction;

import org.radixware.kernel.designer.common.dialogs.sqlscript.SQLScriptExecutionSession;


public class SQLScriptCancelAction extends AbstractRadixContextAwareAction {

    public static final String SQLSCRIPT_CANCEL_ACTION = "sqlscript-cancel-action";

    public SQLScriptCancelAction() {
        super(SQLSCRIPT_CANCEL_ACTION, "org/radixware/kernel/designer/common/dialogs/sqlscript/resources/stopsql.png");
    }

    @Override
    public Action createAction(Lookup actionContext) {
        final SQLScriptExecutionSession session = actionContext.lookup(SQLScriptExecutionSession.class);
        if (session != null) {
            return new SQLScriptCancelActionImpl(session);
        }
        return new UnavailableRunSqlScriptActionImpl();
    }

    private static class SQLScriptCancelActionImpl extends AbstractRadixAction {

        private final SQLScriptExecutionSession session;
        private final ChangeListener listener;

        public SQLScriptCancelActionImpl(final SQLScriptExecutionSession session) {
            putValue("iconBase", RadixWareDesignerIcon.DELETE.DELETE.getIcon());
            putValue(NAME, SQLSCRIPT_CANCEL_ACTION);
            this.session = session;
            this.listener = new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (session.isActive() != isEnabled()) {
                        if (SwingUtilities.isEventDispatchThread()) {
                            setEnabled(session.isActive());
                        } else {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    setEnabled(session.isActive());
                                }
                            });
                        }
                    }
                }
            };
            session.addWeakListener(listener);
            listener.stateChanged(null);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (session.isActive()) {
                session.end();
            }
        }

        @Override
        protected String getMimeTypeForSettings() {
            return "text/x-sql";
        }
    }
}
