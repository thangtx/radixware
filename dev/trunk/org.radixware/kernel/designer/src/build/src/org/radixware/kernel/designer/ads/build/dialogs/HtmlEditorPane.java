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

/*
 * HtmlEditorPane.java
 *
 * Created on 01.04.2010, 11:31:33
 */
package org.radixware.kernel.designer.ads.build.dialogs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.StyledEditorKit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import org.openide.util.actions.SystemAction;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.PasteAction;
import org.openide.actions.RedoAction;
import org.openide.actions.UndoAction;


public class HtmlEditorPane extends javax.swing.JPanel {

    private final Action boldAction = new StyledEditorKit.BoldAction();
    private final Action italicAction = new StyledEditorKit.ItalicAction();
    private final Action underlineAction = new StyledEditorKit.UnderlineAction();
    //private final Action cutAction = new DefaultEditorKit.CutAction();
    //private final Action copyAction = new DefaultEditorKit.CopyAction();
    //private final Action pasteAction = new DefaultEditorKit.PasteAction();
    private final HtmlEditoUndoAction undoAction = new HtmlEditoUndoAction();
    private final HtmlEditorRedoAction redoAction = new HtmlEditorRedoAction();
    private final UndoManager undo = new UndoManager();

    class HtmlEditoUndoAction extends AbstractAction {

        public HtmlEditoUndoAction() {
            super("");
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                undo.undo();
            } catch (CannotUndoException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, "Unable to undo", ex);
            }
            updateUndoState();
            redoAction.updateRedoState();
        }

        protected void updateUndoState() {
            if (undo.canUndo()) {
                setEnabled(true);
            } else {
                setEnabled(false);
            }
        }
    }

    class HtmlEditorRedoAction extends AbstractAction {

        public HtmlEditorRedoAction() {
            super("");
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                undo.redo();
            } catch (CannotRedoException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, "Unable to redo", ex);
            }
            updateRedoState();
            undoAction.updateUndoState();
        }

        protected void updateRedoState() {
            if (undo.canRedo()) {
                setEnabled(true);
            } else {
                setEnabled(false);
            }
        }
    }
    private static final int MARGIN = 5;
    private static final String UNDO_ACTION = "undo";
    private static final String REDO_ACTION = "redo";
//    private static final String CUT_ACTION = "cut";
//    private static final String COPY_ACTION = "copy";
//    private static final String PASTE_ACTION = "paste";
    private static final String BOLD_ACTION = "bold";
    private static final String ITALIC_ACTION = "italic";
    private static final String UNDERLINE_ACTION = "underline";

    /**
     * Creates new form HtmlEditorPane
     */
    public HtmlEditorPane() {
        initComponents();

        toolBar.setFloatable(false);

        final InputMap inputMap = editorPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        final ActionMap actionMap = editorPane.getActionMap();

        final KeyStroke undoKey = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK);
        inputMap.put(undoKey, UNDO_ACTION);
        actionMap.put(UNDO_ACTION, undoAction);
        undoBtn.setAction(undoAction);

        final KeyStroke redoKey = KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK);
        inputMap.put(redoKey, REDO_ACTION);
        actionMap.put(REDO_ACTION, redoAction);
        redoBtn.setAction(redoAction);

        final KeyStroke boldKey = KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK);
        inputMap.put(boldKey, BOLD_ACTION);
        actionMap.put(BOLD_ACTION, boldAction);
        boldBtn.addActionListener(boldAction);

        final KeyStroke italicKey = KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK);
        inputMap.put(italicKey, ITALIC_ACTION);
        actionMap.put(ITALIC_ACTION, italicAction);
        italicBtn.addActionListener(italicAction);

        final KeyStroke underlineKey = KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_DOWN_MASK);
        inputMap.put(underlineKey, UNDERLINE_ACTION);
        actionMap.put(UNDERLINE_ACTION, underlineAction);
        underlineBtn.addActionListener(underlineAction);

        cutBtn.setIcon(SystemAction.get(CutAction.class).getIcon());
        copyBtn.setIcon(SystemAction.get(CopyAction.class).getIcon());
        pasteBtn.setIcon(SystemAction.get(PasteAction.class).getIcon());

        undoBtn.setIcon(SystemAction.get(UndoAction.class).getIcon());
        redoBtn.setIcon(SystemAction.get(RedoAction.class).getIcon());

        editorPane.setContentType("text/html");

        editorPane.setMargin(new Insets(MARGIN, MARGIN, MARGIN, MARGIN));
        editorPane.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undo.addEdit(e.getEdit());
                undoAction.updateUndoState();
                redoAction.updateRedoState();
            }
        });


        colorBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseForeground();
            }
        });

        fontPanel.setSelectedFontFamily(editorPane.getFont().getFamily());
        fontPanel.setFontSize(editorPane.getFont().getSize());
        fontPanel.addFontFamilyChangeListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String selected = fontPanel.getSelectedFontFamily();
                final StyledEditorKit.FontFamilyAction fontFamilyAction = new StyledEditorKit.FontFamilyAction("Font", selected);
                fontFamilyAction.actionPerformed(e);
                final Font old = editorPane.getFont();
                editorPane.setFont(new Font(selected, old.getStyle(), old.getSize()));
            }
        });
        fontPanel.addFontSizeChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                final Integer value = fontPanel.getFontSize();
                final StyledEditorKit.FontSizeAction fontSizeAction = new StyledEditorKit.FontSizeAction("Font", value);
                fontSizeAction.actionPerformed(new ActionEvent(editorPane, 0, ""));
                final Font old = editorPane.getFont();
                editorPane.setFont(new Font(old.getFamily(), old.getStyle(), value));
            }
        });
    }

    public void open(String text) {
        editorPane.setText(text);
        undo.discardAllEdits();
        undoAction.updateUndoState();
        redoAction.updateRedoState();
    }

    public String getHtml() {
        return editorPane.getText();
    }

    private void chooseForeground() {
        Color chosen = JColorChooser.showDialog(this, "Choose color", editorPane.getForeground());
        StyledEditorKit.ForegroundAction colorAction = new StyledEditorKit.ForegroundAction("", chosen);
        colorAction.actionPerformed(new ActionEvent(editorPane, 0, ""));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toolBar = new javax.swing.JToolBar();
        fontPanel = new org.radixware.kernel.designer.ads.build.dialogs.FontToolbarPanel();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        boldBtn = new javax.swing.JButton();
        italicBtn = new javax.swing.JButton();
        underlineBtn = new javax.swing.JButton();
        colorBtn = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        cutBtn = new javax.swing.JButton();
        copyBtn = new javax.swing.JButton();
        pasteBtn = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        undoBtn = new javax.swing.JButton();
        redoBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        editorPane = new javax.swing.JTextPane();

        toolBar.setRollover(true);
        toolBar.add(fontPanel);
        toolBar.add(jSeparator3);

        boldBtn.setText(org.openide.util.NbBundle.getMessage(HtmlEditorPane.class, "HtmlEditorPane-BoldBtn")); // NOI18N
        boldBtn.setFocusable(false);
        boldBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        boldBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(boldBtn);

        italicBtn.setText(org.openide.util.NbBundle.getMessage(HtmlEditorPane.class, "HtmlEditorPane-ItalicBtn")); // NOI18N
        italicBtn.setFocusable(false);
        italicBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        italicBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(italicBtn);

        underlineBtn.setText(org.openide.util.NbBundle.getMessage(HtmlEditorPane.class, "HtmlEditorPane-UnderlineBtn")); // NOI18N
        underlineBtn.setFocusable(false);
        underlineBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        underlineBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(underlineBtn);

        colorBtn.setText("Color");
        colorBtn.setFocusable(false);
        colorBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        colorBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(colorBtn);
        toolBar.add(jSeparator1);

        cutBtn.setFocusable(false);
        cutBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cutBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(cutBtn);

        copyBtn.setFocusable(false);
        copyBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        copyBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(copyBtn);

        pasteBtn.setFocusable(false);
        pasteBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pasteBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(pasteBtn);
        toolBar.add(jSeparator2);

        undoBtn.setFocusable(false);
        undoBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        undoBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(undoBtn);

        redoBtn.setFocusable(false);
        redoBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        redoBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(redoBtn);

        jScrollPane1.setViewportView(editorPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(toolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton boldBtn;
    private javax.swing.JButton colorBtn;
    private javax.swing.JButton copyBtn;
    private javax.swing.JButton cutBtn;
    private javax.swing.JTextPane editorPane;
    private org.radixware.kernel.designer.ads.build.dialogs.FontToolbarPanel fontPanel;
    private javax.swing.JButton italicBtn;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JButton pasteBtn;
    private javax.swing.JButton redoBtn;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JButton underlineBtn;
    private javax.swing.JButton undoBtn;
    // End of variables declaration//GEN-END:variables
}
