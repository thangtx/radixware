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

package org.radixware.kernel.designer.common.dialogs.utils;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;
import org.netbeans.editor.BaseDocument;


public class TextComponentUtils {

    private static final String UNDO_ACTION = "undo-action";
    private static final String REDO_ACTION = "redo-action";

    private static class UndoAction extends AbstractAction {

        public UndoAction() {
            super(UNDO_ACTION);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JTextComponent) {
                JTextComponent tc = (JTextComponent) e.getSource();
                if (tc.getDocument().getProperty(BaseDocument.UNDO_MANAGER_PROP) instanceof UndoManager) {
                    UndoManager manager = (UndoManager) tc.getDocument().getProperty(BaseDocument.UNDO_MANAGER_PROP);
                    if (tc.isEnabled() && tc.isEditable() && manager.canUndo()) {
                        manager.undo();
                    }
                }
            }
        }
    }

    private static class RedoAction extends AbstractAction {

        public RedoAction() {
            super(REDO_ACTION);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JTextComponent) {
                JTextComponent tc = (JTextComponent) e.getSource();
                if (tc.getDocument().getProperty(BaseDocument.UNDO_MANAGER_PROP) instanceof UndoManager) {
                    UndoManager manager = (UndoManager) tc.getDocument().getProperty(BaseDocument.UNDO_MANAGER_PROP);
                    if (tc.isEnabled() && tc.isEditable() && manager.canRedo()) {
                        manager.redo();
                    }
                }
            }
        }
    }

    public static void installUndoRedoAction(JTextComponent tc) {
        final UndoManager manager = new UndoManager();
        tc.getDocument().putProperty(BaseDocument.UNDO_MANAGER_PROP, manager);
        tc.getDocument().addUndoableEditListener(manager);

        UndoAction undoAction = new UndoAction();
        RedoAction redoAction = new RedoAction();

        String undoActionName = (String) undoAction.getValue(Action.NAME);
        String redoActionName = (String) redoAction.getValue(Action.NAME);

        tc.getInputMap().put(KeyStroke.getKeyStroke("ctrl Z"), undoActionName);
        tc.getInputMap().put(KeyStroke.getKeyStroke("ctrl Y"), redoActionName);

        tc.getActionMap().put(undoActionName, undoAction);
        tc.getActionMap().put(redoActionName, redoAction);

    }
}
