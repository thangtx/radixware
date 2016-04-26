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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.Lookup;
import org.radixware.kernel.designer.common.dialogs.actions.AbstractRadixAction;
import org.radixware.kernel.designer.common.dialogs.actions.AbstractRadixContextAwareAction;
import org.radixware.kernel.designer.common.dialogs.sqlscript.SQLScriptExecutionSession;


public class SQLScriptMakeStepAction extends AbstractRadixContextAwareAction {

    public static final String SQLSCRIPT_MAKE_STEP_ACTION = "sqlscript-make-step-action";

    public SQLScriptMakeStepAction() {
        super(SQLSCRIPT_MAKE_STEP_ACTION, "org/radixware/kernel/designer/common/dialogs/sqlscript/resources/stepsql.png");
    }

    @Override
    public Action createAction(Lookup actionContext) {
        final SQLScriptExecutionSession session = actionContext.lookup(SQLScriptExecutionSession.class);
        if (session == null) {
            return new UnavailableRunSqlScriptActionImpl();
        }
        return new SQLScriptMakeStepActionImpl(session);
    }

    private static class SQLScriptMakeStepActionImpl extends AbstractRadixAction {

        private final SQLScriptExecutionSession session;
        private final ChangeListener sessionListener;

        public SQLScriptMakeStepActionImpl(final SQLScriptExecutionSession session) {
            putValue("iconBase", "org/radixware/kernel/designer/common/dialogs/resources/runsql.png");
            putValue(NAME, SQLSCRIPT_MAKE_STEP_ACTION);
            this.session = session;
            sessionListener = new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    boolean enabled = false;
                    if (session.isActive()) {
                        if (session.getExecutors().get(0).getMonitor().getPauseObject().isStepMode()) {
                            enabled = true;
                        }
                    }
                    if (enabled != isEnabled()) {
                        setEnabled(enabled);
                    }
                }
            };
            session.addWeakListener(sessionListener);
            sessionListener.stateChanged(null);
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            if (session.isActive() && session.getExecutors().get(0).getMonitor().getPauseObject().isStepMode()) {
                session.getExecutors().get(0).getMonitor().getPauseObject().allowNextStep();
            }
        }

        @Override
        protected String getMimeTypeForSettings() {
            return "text/x-sql";
        }
    }
}
