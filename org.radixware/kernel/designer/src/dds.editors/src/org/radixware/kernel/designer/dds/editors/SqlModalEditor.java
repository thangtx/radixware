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

package org.radixware.kernel.designer.dds.editors;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.undo.UndoManager;
import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.EditorUI;
import org.netbeans.editor.Utilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.designer.common.dialogs.sqlscript.DefaultSQLScriptProviderFactory;
import org.radixware.kernel.designer.common.dialogs.sqlscript.SQLScriptExecutionSession;
import org.radixware.kernel.designer.common.dialogs.sqlscript.SelectedConnectionsManager;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public class SqlModalEditor {

    public static interface ICfg {

        public String getSql();

        public void setSql(String sql);

        public String getTitle();

        public JPanel getAdditionalPanel();

        public boolean canCloseEditor();

        public void showClosingProblems();
    }

    public static class SqlEditor extends JPanel implements Lookup.Provider {

        private final JEditorPane sqlPanel;
        private final UndoManager undoRedoManager;
        private final Lookup lookup;
        private final ICfg cfg;

        public SqlEditor(final ICfg cfg) {
            super(new BorderLayout());
            this.cfg = cfg;
            setMinimumSize(new Dimension(300, 200));
            setPreferredSize(new Dimension(800, 600));

            sqlPanel = new JEditorPane();
            sqlPanel.setContentType("text/x-sql");
            //lookup must be inited before getting toolbar component
            lookup = Lookups.fixed(new SelectedConnectionsManager(), new SQLScriptExecutionSession(), sqlPanel, new DefaultSQLScriptProviderFactory());

            EditorUI eui = Utilities.getEditorUI(sqlPanel);
            if (eui != null) {
                JComponent editorWrapperComponent = eui.getExtComponent();
                this.add(editorWrapperComponent, BorderLayout.CENTER);

                JToolBar toolbar = eui.getToolBarComponent();
                this.add(toolbar, BorderLayout.NORTH);

                final JPanel additionalPanel = cfg.getAdditionalPanel();
                if (additionalPanel != null) {
                    this.add(additionalPanel, BorderLayout.SOUTH);
                }
            }

            undoRedoManager = new UndoManager();
            undoRedoManager.setLimit(1000);
            sqlPanel.getDocument().addUndoableEditListener(undoRedoManager);
            sqlPanel.getDocument().putProperty(BaseDocument.UNDO_MANAGER_PROP, undoRedoManager);
        }

        public void setSql(String sql) {
            sqlPanel.setText(sql);
            sqlPanel.setEditable(true);
            sqlPanel.setEnabled(true);

            undoRedoManager.discardAllEdits();
        }

        public String getSql() {
            return sqlPanel.getText();
        }

        @Override
        public Lookup getLookup() {
            return lookup;
        }

        public boolean canClose() {
            return cfg.canCloseEditor();
        }
    }

    public static boolean editSqlOnAwtThread(final ICfg cfg) {
        final SqlEditor sqlPanel = new SqlEditor(cfg);
        final String sql = cfg.getSql();
        sqlPanel.setSql(sql);
        final String title = cfg.getTitle();
        final ModalDisplayer md = new ModalDisplayer(sqlPanel, title) {

            @Override
            protected boolean canClose() {
                return cfg.canCloseEditor();
            }

            @Override
            protected void showClosingProblems() {
                cfg.showClosingProblems();
            }
        };
        if (md.showModal()) {
            final String newSql = sqlPanel.getSql();
            cfg.setSql(newSql);
            return true;
        } else {
            return false;
        }
    }

    public static boolean editSql(final ICfg cfg) {
        try {
            final boolean[] resultHolder = {false};
            if (SwingUtilities.isEventDispatchThread()) {
                resultHolder[0] = editSqlOnAwtThread(cfg);
            } else {
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        resultHolder[0] = editSqlOnAwtThread(cfg);
                    }
                });
            }
            return resultHolder[0];
        } catch (InterruptedException ex) {
            return false;
        } catch (InvocationTargetException ex) {
            DialogUtils.messageError(ex);
            return false;
        }
    }
}
