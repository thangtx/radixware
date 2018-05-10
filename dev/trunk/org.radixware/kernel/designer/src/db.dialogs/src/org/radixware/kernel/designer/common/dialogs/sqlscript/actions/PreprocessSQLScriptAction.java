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
import static javax.swing.Action.NAME;
import org.netbeans.api.db.explorer.DatabaseConnection;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;
import org.radixware.kernel.common.sqlscript.parser.SQLPreprocessor;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.actions.AbstractRadixAction;
import org.radixware.kernel.designer.common.dialogs.actions.AbstractRadixContextAwareAction;
import org.radixware.kernel.designer.common.dialogs.sqlscript.DesignerVariablesProvider;
import org.radixware.kernel.designer.common.dialogs.sqlscript.ISQLScriptProvider;
import org.radixware.kernel.designer.common.dialogs.sqlscript.SelectedConnectionsManager;
import org.radixware.kernel.designer.common.dialogs.sqmlnb.DatabaseParameters;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class PreprocessSQLScriptAction extends AbstractRadixContextAwareAction {

    private static final String PREPROCESS_SQL_SCRIPT_ACTION = "preprocess-sqlscript-action";

    public PreprocessSQLScriptAction() {
        super(PREPROCESS_SQL_SCRIPT_ACTION, "org/radixware/kernel/designer/common/dialogs/sqlscript/resources/viewsql.png");
    }

    @Override
    public Action createAction(Lookup actionContext) {
        final SelectedConnectionsManager selectedConnectionManager = actionContext.lookup(SelectedConnectionsManager.class);
        if (selectedConnectionManager == null) {
            return new UnavailableRunSqlScriptActionImpl();
        }

        String scriptName = "sql";
        final DataObject dataObject = actionContext.lookup(DataObject.class);
        if (dataObject != null) {
            scriptName = dataObject.getName();
        }

        ISQLScriptProvider.Factory factory = actionContext.lookup(ISQLScriptProvider.Factory.class);
        if (factory == null && dataObject != null) {
            factory = dataObject.getLookup().lookup(ISQLScriptProvider.Factory.class);
        }

        if (factory == null) {
            return new UnavailableRunSqlScriptActionImpl();
        }
        return new PreprocessSQLScriptActionImpl(factory.create(actionContext), scriptName, selectedConnectionManager);

    }

    private static class PreprocessSQLScriptActionImpl extends AbstractRadixAction {

        private final SelectedConnectionsManager manager;
        private final ISQLScriptProvider scriptProvider;
        private final String name;

        public PreprocessSQLScriptActionImpl(final ISQLScriptProvider scriptProvider, final String scriptName, final SelectedConnectionsManager connectionsManager) {
            putValue("iconBase", RadixWareDesignerIcon.EDIT.VIEW_SQL.getIcon());
            putValue(NAME, PREPROCESS_SQL_SCRIPT_ACTION);
            this.manager = connectionsManager;
            this.scriptProvider = scriptProvider;
            this.name = scriptName;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final DatabaseConnection connection;
            if (manager.getSelectedConnections() == null || manager.getSelectedConnections().isEmpty()) {
                connection = null;
            } else {
                connection = manager.getSelectedConnections().get(0);
            }
            final DatabaseParameters connectionParameters = BaseRunSQLScriptAction.prepareConnectionParameters(connection);
            if (connectionParameters == null) {
                return;
            }
            final SQLPreprocessor sqlPreprocessor = new SQLPreprocessor(scriptProvider.getScript(), name, new DesignerVariablesProvider(connection, connectionParameters));
            try {
                final String preprocessedScript = sqlPreprocessor.preprocess(SQLPreprocessor.PreprocessBehavior.PT_REPLACE_UNUSED_BLOCKS_TO_COMMENT, null);
                if (preprocessedScript != null) {
                    DialogUtils.showText(preprocessedScript, "Preprocessed " + name, "sql");
                }
            } catch (Exception ex) {
                DialogUtils.messageError(ex);
            }

        }

        @Override
        protected String getMimeTypeForSettings() {
            return "text/x-sql";
        }
    }
}
