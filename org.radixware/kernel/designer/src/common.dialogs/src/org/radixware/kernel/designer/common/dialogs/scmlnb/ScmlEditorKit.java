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

package org.radixware.kernel.designer.common.dialogs.scmlnb;

import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.RandomAccess;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.TextAction;
import org.netbeans.api.editor.EditorActionRegistration;
import org.netbeans.api.editor.EditorActionRegistrations;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.editor.BaseAction;
import org.netbeans.editor.BaseCaret;
import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.BaseKit;
import org.netbeans.editor.Utilities;
import org.netbeans.editor.ext.ExtKit;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkProviderExt;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkType;
import org.netbeans.lib.editor.util.swing.DocumentUtilities;
import org.netbeans.modules.editor.NbEditorKit;
import org.openide.util.actions.Presenter;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.TagBounds;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.TagHyperlinkProvider;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.TagMapper;
import org.radixware.kernel.designer.common.dialogs.xml.XmlHyperlinkProvider;

/**
 * EditorKit for Scml.
 *
 */
public class ScmlEditorKit extends NbEditorKit {

    @Override
    protected void addSystemActionMapping(String editorActionName, Class systemActionClass) {
        super.addSystemActionMapping(editorActionName, systemActionClass);
    }
    public static final String scmlToolBarCutAction = "scml-toolbar-cut-action";
    public static final String scmlToolBarCopyAction = "scml-toolbar-copy-action";
    public static final String scmlToolBarPasteAction = "scml-toolbar-paste-action";
    protected static boolean ctrlDown;

    static {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.isControlDown()) {
                    ctrlDown = true;
                } else {
                    ctrlDown = false;
                }
                return false;
            }
        });
    }

    protected @Override
    Action[] createActions() {
        Action[] superActions = super.createActions();
        Action[] scmlActions = new Action[]{
            new ScmlCutAction(),
            new ScmlCopyAction(),
            new ScmlPasteAction(true),
            new ScmlPasteAction(false),
            new ScmlDeleteCharAction(deletePrevCharAction, false),
            new ScmlDeleteCharAction(deleteNextCharAction, true),
            new FakeToggleToolbarAction(),
            new ScmlUndoAction(),
            new ScmlRedoAction(),
            new ScmlNextWordAction(nextWordAction),
            new ScmlNextWordAction(selectionNextWordAction),
            new ScmlPreviousWordAction(previousWordAction),
            new ScmlPreviousWordAction(selectionPreviousWordAction),
            new org.radixware.kernel.designer.common.dialogs.findsupport.visual.ReplaceAction()
        };

        return TextAction.augmentList(superActions, scmlActions);
    }

    public static class ScmlCutAction extends BaseKit.CutAction implements Presenter.Toolbar {

        ScmlCutAction() {
            super();
        }

        @Override
        public void actionPerformed(final ActionEvent evt, final JTextComponent target) {
            if (!target.isEditable() || !target.isEnabled()) {
                target.getToolkit().beep();
                return;
            }
            TagMapper tm = TagMapper.getInstance(target.getDocument());
            if (tm == null) {
                super.actionPerformed(evt, target);
            } else {
                int p0 = target.getSelectionStart();
                int p1 = target.getSelectionEnd();

                NavigableSet<TagBounds> bounds = tm.getBounds(p0, p1);

                if (bounds.size() > 0) {

                    p0 = Math.min(p0, bounds.first().getBeginOffset());
                    p1 = Math.max(p1, bounds.last().getEndOffset());

                    target.setSelectionStart(p0);
                    target.setSelectionEnd(p1);


                    for (TagBounds tb : bounds) {
                        tb.unlockText();
                    }

                    super.actionPerformed(evt, target);
                } else {
                    super.actionPerformed(evt, target);
                }
            }
        }

        @Override
        public Component getToolbarPresenter() {
            JButton bt = new JButton(this);
            bt.setText(null);
            bt.setIcon(SystemAction.get(org.openide.actions.CutAction.class).getIcon());
            return bt;
        }
    }

    public static class ScmlCopyAction extends BaseKit.CopyAction implements Presenter.Toolbar {

        @Override
        public void actionPerformed(ActionEvent evt, JTextComponent target) {
            if (target != null) {
                if (target.getSelectionStart() == target.getSelectionEnd()) {
                    TagMapper tm = TagMapper.getInstance(target.getDocument());
                    if (tm != null) {
                        TagBounds tb = tm.findContainingBounds(target.getCaretPosition());
                        if (tb != null && tb.getEndOffset() != target.getCaretPosition()) {
                            target.select(tb.getBeginOffset(), tb.getEndOffset());
                        }
                    }
                }
                target.copy();
            }
        }

        @Override
        public Component getToolbarPresenter() {
            JButton bt = new JButton(this);
            bt.setText(null);
            bt.setIcon(SystemAction.get(org.openide.actions.CopyAction.class).getIcon());
            return bt;
        }
    }

    public static class ScmlPasteAction extends BaseKit.PasteAction implements Presenter.Toolbar {

        public ScmlPasteAction(boolean formated) {
            super(formated);
        }

        @Override
        public Component getToolbarPresenter() {
            JButton bt = new JButton(this) {
            };
            bt.setText(null);
            bt.setIcon(SystemAction.get(org.openide.actions.PasteAction.class).getIcon());
            return bt;
        }
    }

    public static class ScmlDeleteCharAction extends ExtDeleteCharAction {

        public ScmlDeleteCharAction(String nm, boolean nextChar) {
            super(nm, nextChar);
        }

        @Override
        public void actionPerformed(ActionEvent evt, final JTextComponent target) {
            if (target != null) {
                if (!target.isEditable() || !target.isEnabled()) {
                    target.getToolkit().beep();
                    return;
                }

                final BaseDocument doc = (BaseDocument) target.getDocument();
                final Caret caret = target.getCaret();
                final int dot = caret.getDot();
                final int mark = caret.getMark();
                final TagMapper tm = TagMapper.getInstance(doc);

                doc.runAtomicAsUser(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            DocumentUtilities.setTypingModification(doc, true);
                            if (dot != mark) { // remove selection
                                int p0 = Math.min(dot, mark);
                                int p1 = p0 + Math.abs(dot - mark);
                                NavigableSet<TagBounds> bounds = tm.getBounds(p0, p1);
                                if (bounds.size() > 0) {
                                    p0 = Math.min(p0, bounds.first().getBeginOffset());
                                    p1 = Math.max(p1, bounds.last().getEndOffset());
                                    for (TagBounds tb : bounds) {
                                        tb.unlockText();
                                    }
                                }
                                doc.remove(p0, p1 - p0);
                            } else {
                                if (nextChar) { // remove next char
                                    //XXX who cares whether one char was deleted or more?
                                    TagBounds tb = tm.findContainingBounds(dot);
                                    if (tb != null) {
                                        tb.unlockText();
                                        int p0 = tb.getBeginOffset();
                                        int p1 = tb.getEndOffset();
                                        doc.remove(p0, p1 - p0);
                                    } else {
                                        doc.remove(dot, 1);
                                    }
                                } else if (dot > 0) { // remove previous char

                                    TagBounds tb = tm.findContainingBounds(dot - 1);
                                    if (tb != null) {
                                        tb.unlockText();
                                        int p0 = tb.getBeginOffset();
                                        int p1 = tb.getEndOffset();
                                        doc.remove(p0, p1 - p0);
                                    } else {
                                        doc.remove(dot - 1, 1);
                                    }
                                } else {
                                    target.getToolkit().beep();
                                }
                            }
                        } catch (BadLocationException e) {
                            target.getToolkit().beep();
                        } catch (Exception ex) {
                            //added for debugging. It is known, that this method generates exception
                            //in some cases, but it's hard to reproduce and it is NOT CAUGHT anywhere,
                            //so we must log it here.
                            ex.printStackTrace();
                            target.getToolkit().beep();
                        } finally {
                            DocumentUtilities.setTypingModification(doc, false);
                        }
                    }
                });
            }
        }

        public boolean getNextChar() {
            return nextChar;
        }
    }

    @EditorActionRegistrations(value = {
        @EditorActionRegistration(name = buildToolTipAction, shortDescription = buildToolTipAction),
    //    @EditorActionRegistration(name = buildToolTipAction, shortDescription = buildToolTipAction, mimeType = "text/x-jml"),
   //     @EditorActionRegistration(name = buildToolTipAction, shortDescription = buildToolTipAction, mimeType = "text/x-sqml")
    })
    public static class BuildScmlTooltipAction extends ExtKit.BuildToolTipAction {
        
        private String getTooltip(JTextComponent target, int offs){
            String tooltip = null;
            String mimeType = ((ScmlEditorPane) target).getContentType();
            //Get the TagHyperlinkProvider instance
            MimePath mimePath = MimePath.parse(mimeType);
            Collection<? extends HyperlinkProviderExt> providers = MimeLookup.getLookup(mimePath).lookupAll(HyperlinkProviderExt.class);
            for (HyperlinkProviderExt provider : providers){
               if (provider.isHyperlinkPoint(target.getDocument(), offs, HyperlinkType.GO_TO_DECLARATION)){
                    tooltip = provider.getTooltipText(target.getDocument(), offs, HyperlinkType.GO_TO_DECLARATION);
                    break;
               }
            }

            return tooltip;
        } 

        //Super action creates tooltip with text returned by this function
        @Override
        protected String buildText(JTextComponent target) {
            if (target instanceof ScmlEditorPane) {
                TagMapper tm = TagMapper.getInstance(target.getDocument());
                Point p = target.getMousePosition();
                if (p != null) {
                    int offs = target.viewToModel(p);
                    TagBounds tb = tm.findContainingBounds(offs);
                    String tooltip = null;
                    
                    if (tb != null) {
                        tooltip = tb.getTooltip();
                        //special tooltip when ctrl is pressed
                        if (ctrlDown) {
                           tooltip = getTooltip(target,offs); 
                        }
                    } else {
                            tooltip = getTooltip(target,offs);
                    }
                    if (tooltip != null && tooltip.length() > 0) {
                            return tooltip;
                    }
                }
            }
            return super.buildText(target);
        }
    }

    //Due to some specialities of ScmlEditor it's toolbar must always be visible.
    public static class FakeToggleToolbarAction extends ToggleToolbarAction {

        @Override
        public JMenuItem getPopupMenuItem(JTextComponent target) {
            JMenuItem item = new JMenuItem();
            item.setVisible(false);
            return item;
        }
    }

    public static class ScmlUndoAction extends NbUndoAction {

        @Override
        public void actionPerformed(final ActionEvent evt, final JTextComponent target) {
            if (target instanceof ScmlEditorPane) {
                final ScmlDocument sdoc = ((ScmlEditorPane) target).getScmlDocument();
                if (sdoc != null && target.isEnabled() && target.isEditable() && sdoc.getUndoRedo().canUndo()) {
                    sdoc.runAtomic(new Runnable() {

                        @Override
                        public void run() {
                            sdoc.getUndoRedo().undo();
                            target.setCaretPosition(sdoc.getLastChangeEndOffset());
                        }
                    });

                } else {
                    target.getToolkit().beep();
                }
            } else {
                super.actionPerformed(evt, target);
            }
        }
    }

    public static class ScmlRedoAction extends NbRedoAction {

        @Override
        public void actionPerformed(final ActionEvent evt, final JTextComponent target) {
            if (target instanceof ScmlEditorPane) {
                final ScmlDocument sdoc = ((ScmlEditorPane) target).getScmlDocument();
                if (sdoc != null && target.isEnabled() && target.isEditable() && sdoc.getUndoRedo().canRedo()) {
                    sdoc.runAtomic(new Runnable() {

                        @Override
                        public void run() {
                            sdoc.getUndoRedo().redo();
                            target.setCaretPosition(sdoc.getLastChangeEndOffset());
                        }
                    });


                } else {
                    target.getToolkit().beep();
                }
            } else {
                super.actionPerformed(evt, target);
            }
        }
    }

    @EditorActionRegistrations({
        @EditorActionRegistration(name = forwardAction, shortDescription = forwardAction),
        @EditorActionRegistration(name = selectionForwardAction, shortDescription = selectionForwardAction),
//        @EditorActionRegistration(name = forwardAction, shortDescription = forwardAction, mimeType = "text/x-jml"),
//        @EditorActionRegistration(name = selectionForwardAction, shortDescription = selectionForwardAction, mimeType = "text/x-jml"),
//        @EditorActionRegistration(name = forwardAction, shortDescription = forwardAction, mimeType = "text/x-sqml"),
//        @EditorActionRegistration(name = selectionForwardAction, shortDescription = selectionForwardAction, mimeType = "text/x-sqml")
    })
    public static class ScmlForwardAction extends ForwardAction {

        @Override
        public void actionPerformed(ActionEvent evt, JTextComponent target) {
            if (target != null) {
                Caret caret = target.getCaret();
                try {
                    int pos;
                    boolean select = selectionForwardAction.equals(getValue(Action.NAME));
                    if (!select && Utilities.isSelectionShowing(caret)) {
                        pos = target.getSelectionEnd();
                        if (pos != caret.getDot()) {
                            pos--;
                        } else {
                            // clear the selection, but do not move the cursor
                            caret.setDot(pos);
                            return;
                        }
                    } else {
                        pos = caret.getDot();
                    }
                    int dot = target.getUI().getNextVisualPositionFrom(target,
                            pos, Position.Bias.Forward, SwingConstants.EAST, null);
                    TagMapper tagMapper = TagMapper.getInstance(target.getDocument());
                    if (tagMapper != null && tagMapper.insideTagBounds(dot)) {
                        dot = tagMapper.findContainingBounds(dot).getEndOffset();
                    }
                    if (select) {
                        caret.moveDot(dot);
                    } else {
                        caret.setDot(dot);
                    }
                } catch (BadLocationException ex) {
                    target.getToolkit().beep();
                }
            }
        }
    }

    @EditorActionRegistrations({
        @EditorActionRegistration(name = backwardAction, shortDescription = backwardAction),
        @EditorActionRegistration(name = selectionBackwardAction, shortDescription = backwardAction)
    })
    public static class ScmlBackwardAction extends BackwardAction {

        @Override
        public void actionPerformed(ActionEvent evt, JTextComponent target) {
            if (target != null) {
                Caret caret = target.getCaret();
                try {
                    int pos;
                    boolean select = selectionBackwardAction.equals(getValue(Action.NAME));
                    if (!select && Utilities.isSelectionShowing(caret)) {
                        pos = target.getSelectionStart();
                        if (pos != caret.getDot()) {
                            pos++;
                        } else {
                            // clear the selection, but do not move the cursor
                            caret.setDot(pos);
                            return;
                        }
                    } else {
                        pos = caret.getDot();
                    }
                    int dot = target.getUI().getNextVisualPositionFrom(target,
                            pos, Position.Bias.Backward, SwingConstants.WEST, null);
                    TagMapper tagMapper = TagMapper.getInstance(target.getDocument());
                    if (tagMapper != null && tagMapper.insideTagBounds(dot)) {
                        dot = tagMapper.findContainingBounds(dot).getBeginOffset();
                    }
                    if (select) {
                        caret.moveDot(dot);
                    } else {
                        caret.setDot(dot);
                    }
                } catch (BadLocationException ex) {
                    target.getToolkit().beep();
                }
            }
        }
    }

    public static class ScmlNextWordAction extends BaseAction {

        protected static final long serialVersionUID = -5909906947175434032L;

        ScmlNextWordAction(String name) {
            super(name, MAGIC_POSITION_RESET | ABBREV_RESET | UNDO_MERGE_RESET | WORD_MATCH_RESET | CLEAR_STATUS_TEXT);
        }

        @Override
        public void actionPerformed(ActionEvent evt, JTextComponent target) {
            if (target != null) {
                Caret caret = target.getCaret();
                try {
                    int dot = caret.getDot();
                    int newDot = Utilities.getNextWord(target, dot);
                    if (target instanceof ScmlEditorPane) {
                        ScmlEditorPane pane = (ScmlEditorPane) target;
                        TagMapper tm = pane.getScmlDocument().getTagMapper();
                        if (tm != null) {
                            TagBounds tb = tm.findContainingBounds(dot);
                            if (tb != null && tb.getEndOffset() != dot) {
                                newDot = tb.getEndOffset();
                            } else {
                                tb = tm.findContainingBounds(newDot);
                                if (tb != null && tb.getEndOffset() > newDot && tb.getBeginOffset() != newDot) {
                                    newDot = tb.getEndOffset();
                                }
                            }
                        }
                    }
                    boolean select = selectionNextWordAction.equals(getValue(Action.NAME));
                    if (caret instanceof BaseCaret) {
                        BaseCaret bCaret = (BaseCaret) caret;
                        if (select) {
                            bCaret.moveDot(newDot);
                        } else {
                            bCaret.setDot(newDot, false);
                        }
                    } else {
                        if (select) {
                            caret.moveDot(newDot);
                        } else {
                            caret.setDot(newDot);
                        }
                    }
                } catch (BadLocationException ex) {
                    target.getToolkit().beep();
                }
            }
        }
    }

    public static class ScmlPreviousWordAction extends BaseAction {

        protected static final long serialVersionUID = -5465143382669785799L;

        ScmlPreviousWordAction(String name) {
            super(name, MAGIC_POSITION_RESET | ABBREV_RESET | UNDO_MERGE_RESET | WORD_MATCH_RESET | CLEAR_STATUS_TEXT);
        }

        @Override
        public void actionPerformed(ActionEvent evt, JTextComponent target) {
            if (target != null) {
                Caret caret = target.getCaret();
                try {
                    int dot = caret.getDot();
                    int newDot = Utilities.getPreviousWord(target, dot);
                    if (target instanceof ScmlEditorPane) {
                        ScmlEditorPane pane = (ScmlEditorPane) target;
                        TagMapper tm = pane.getScmlDocument().getTagMapper();
                        if (tm != null) {
                            TagBounds tb = tm.findContainingBounds(newDot);
                            if (tb != null) {
                                if (tb.getEndOffset() < dot) {
                                    newDot = tb.getEndOffset();
                                } else if (tb.getBeginOffset() < newDot && tb.getEndOffset() != newDot) {
                                    newDot = tb.getBeginOffset();
                                }
                            }
                        }
                    }
                    boolean select = selectionPreviousWordAction.equals(getValue(Action.NAME));
                    if (caret instanceof BaseCaret) {
                        BaseCaret bCaret = (BaseCaret) caret;
                        if (select) {
                            bCaret.moveDot(newDot);
                        } else {
                            bCaret.setDot(newDot, false);
                        }
                    } else {
                        if (select) {
                            caret.moveDot(newDot);
                        } else {
                            caret.setDot(newDot);
                        }
                    }
                } catch (BadLocationException ex) {
                    target.getToolkit().beep();
                }
            }
        }
    }

    @Override
    public String getContentType() {
        return "text/x-scml";
    }
}
