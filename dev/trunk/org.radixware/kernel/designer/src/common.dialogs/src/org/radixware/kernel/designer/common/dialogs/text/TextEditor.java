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

package org.radixware.kernel.designer.common.dialogs.text;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;
import org.netbeans.api.editor.EditorActionRegistration;
import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.BaseKit;
import org.netbeans.editor.EditorUI;
import org.netbeans.editor.Utilities;
import org.netbeans.modules.editor.NbEditorKit;
import org.netbeans.modules.editor.NbEditorKit.NbBuildPopupMenuAction;
import org.openide.DialogDescriptor;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class TextEditor extends JPanel implements Lookup.Provider {

    // class to enable cut/copy/paste actions in popup menu
    @EditorActionRegistration(name = NbEditorKit.buildPopupMenuAction, shortDescription = NbEditorKit.buildPopupMenuAction)
    public static class ScmlBuildPopupMenuAction extends NbBuildPopupMenuAction {

        public ScmlBuildPopupMenuAction() {
            super();
            putValue(NAME, NbEditorKit.buildPopupMenuAction);
        }

        @Override
        protected void addAction(JTextComponent component, JPopupMenu popupMenu, Action action) {
            JMenuItem item = null;
            BaseKit kit = Utilities.getKit(component);
            if ("Cu&t".equals(action.getValue(Action.NAME))) {
                item = new JMenuItem(kit.getActionByName(NbEditorKit.cutAction));
            } else if ("Cop&y".equals(action.getValue(Action.NAME))) {
                item = new JMenuItem(kit.getActionByName(NbEditorKit.copyAction));
            } else if ("&Paste".equals(action.getValue(Action.NAME))) {
                item = new JMenuItem(kit.getActionByName(NbEditorKit.pasteAction));
            }
            if (item != null) {
                if (action.getValue(Action.ACCELERATOR_KEY) != null) {
                    item.setAccelerator((KeyStroke) action.getValue(Action.ACCELERATOR_KEY));
                }
                String name = (String) action.getValue(Action.NAME);
                item.setText(name.replace("&", ""));
                popupMenu.add(item);
            } else {
                super.addAction(component, popupMenu, action);
            }
        }
    }
    private final JEditorPane textPane;
    private final UndoManager undoManager;
    private final Lookup lookup;
    private String initialText;
    private final KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 27) {// ESC
                if (!Utils.equals(initialText, getText())) {
                    e.consume();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    };
    private EditorUI editorUi = null;
    private final DocumentListener documentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            fireChanged();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            fireChanged();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            fireChanged();
        }
    };
    private EventListenerList changeListeners = new EventListenerList();

    public TextEditor() {
        this("text/plain", true);
    }

    public TextEditor(String type, boolean editable) {
        super(new BorderLayout());
        setMinimumSize(new Dimension(300, 200));
        setPreferredSize(new Dimension(800, 600));
        textPane = new JEditorPane();
        textPane.setContentType(type);
        textPane.addKeyListener(keyListener);
        textPane.setEditable(editable);

        //lookup must be inited before getting toolbar component
        lookup = Lookups.fixed(textPane);

        editorUi = Utilities.getEditorUI(textPane);
        if (editorUi == null) {
            textPane.setEditorKit(new NbEditorKit());
            editorUi = Utilities.getEditorUI(textPane);
        }
        editorUi.setLineNumberEnabled(false);
        final JComponent component = editorUi.getExtComponent();
        undoManager = new UndoManager();
        undoManager.setLimit(1000);
        textPane.getDocument().addUndoableEditListener(undoManager);
        textPane.getDocument().putProperty(BaseDocument.UNDO_MANAGER_PROP, undoManager);
        textPane.getDocument().addDocumentListener(documentListener);
        this.add(component);

        final JToolBar toolbar = editorUi.getToolBarComponent();
        this.add(toolbar, BorderLayout.NORTH);

        setActionMap(textPane.getActionMap());
    }

    public void setEditable(boolean isEditable) {
        textPane.setEditable(isEditable);
    }

    public String getText() {
        return textPane.getText();
    }

    public void setText(String text) {
        this.initialText = text;
        textPane.setText(text);
        textPane.setEnabled(true);
        textPane.setCaretPosition(0);
        undoManager.discardAllEdits();
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    public static String editTextModal(String text, final String title, String contextType, boolean editing, Object[] options) {
        final TextEditor textEditor = new TextEditor(contextType, editing);
        textEditor.setText(text);
        final ModalDisplayer dg = new ModalDisplayer(textEditor, title, options);
        if (dg.showModal()) {
            return textEditor.getText();
        } else {
            return text;
        }
    }

    public static String editTextModal(String text, final String title) {
        return editTextModal(text, title, "text/plain", true, new Object[]{DialogDescriptor.OK_OPTION, DialogDescriptor.CANCEL_OPTION});
    }

    public void addChangeListener(ChangeListener l) {
        changeListeners.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeListeners.remove(ChangeListener.class, l);
    }

    private void fireChanged() {
        final ChangeEvent event = new ChangeEvent(this);
        for (ChangeListener l : changeListeners.getListeners(ChangeListener.class)) {
            l.stateChanged(event);
        }
    }
}
