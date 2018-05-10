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

package org.radixware.kernel.designer.ads.localization.translation;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.Utilities;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.PasteAction;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.localization.MultilingualEditorUtils;
import org.radixware.kernel.designer.ads.localization.RowString;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class TranslateTextPaneUi {

    private final javax.swing.JTextPane edTranslation;
    private final Border border;
    private Style iconStyle;
    private final int fontSize;
    private LangPanel lp = null;
    //private UndoableEditListener docListener;
    private final UndoManager manager;
    private boolean canUndo = true;
    private boolean canCaretUpdate = true;

    public void setCanUndo(final boolean canUndo) {
        this.canUndo = canUndo;
    }

    public TranslateTextPaneUi(final javax.swing.JTextPane edTranslation, final LangPanel langPanel) {
        this.edTranslation = edTranslation;
        //hightLightPos=new ArrayList<Integer[]>();


        //edTranslation.setHighlighter(null);
        border = edTranslation.getBorder();
        edTranslation.setOpaque(true);
        edTranslation.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(final FocusEvent event) {
                langPanel.setCurrentLangPanel();
            }

            @Override
            public void focusLost(final FocusEvent event) {
            }
        });
        edTranslation.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(final CaretEvent event) {
                if (canCaretUpdate) {
                    langPanel.updateSize();
                }
            }
        });
        
        final AbstractAction actCheckAndGo = new AbstractAction(MultilingualEditorUtils.CHECK_STRING_AND_GO) {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                langPanel.checkString(true);
            }
        };
        
        edTranslation.getActionMap().put(MultilingualEditorUtils.CHECK_STRING_AND_GO, actCheckAndGo);
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK, true);
        edTranslation.getInputMap().put(keyStroke, MultilingualEditorUtils.CHECK_STRING_AND_GO);
        
        final AbstractAction actCheck = new AbstractAction(MultilingualEditorUtils.CHECK_STRING) {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                langPanel.checkString(false);
            }
        };
        
        edTranslation.getActionMap().put(MultilingualEditorUtils.CHECK_STRING, actCheck);
        keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.ALT_DOWN_MASK, true);
        edTranslation.getInputMap().put(keyStroke, MultilingualEditorUtils.CHECK_STRING);

        edTranslation.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(final KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_ENTER && !event.isControlDown() && !event.isAltDown() && !event.isShiftDown()){
                    event.consume();
                    insertImageInCurPos();
                } else if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_S) {
                    langPanel.save();
                }
                lp = langPanel;
            }

            @Override
            public void keyReleased(final KeyEvent event) {
                if ((lp == null) || (!lp.equals(langPanel)) || !((javax.swing.JTextPane) event.getSource()).isEditable()) {
                    return;
                }
                RowString rowString = langPanel.getRowString();
                if (rowString == null) {
                    return;
                }
                String val = rowString.getValue(langPanel.getLanguage());
                val = val == null ? "" : val;
                if (!edTranslation.getText().equals(val)) {
                    langPanel.translationWasEdited();
                }

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            langPanel.scroll();
                        } catch (BadLocationException ex) {
                            Logger.getLogger(LangPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                
            }
        });
        

        fontSize = edTranslation.getFontMetrics(edTranslation.getFont()).getHeight();
        final Style defaultStyle = edTranslation.getStyle(StyleContext.DEFAULT_STYLE);
        final StyledDocument doc = (StyledDocument) edTranslation.getDocument();
        iconStyle = doc.addStyle("StyleName", defaultStyle);

        //Icon sourceNotCheckedIcon=RadixWareDesignerIcon.WIDGETS.NEW_LINE1.getIcon((fontSize-2)/*3*/, fontSize-2);

        final Icon srcUncheckIcon = RadixWareIcons.MLSTRING_EDITOR.NEW_LINE.getIcon((fontSize - 2) * 3, fontSize - 2);

        StyleConstants.setFontSize(iconStyle, edTranslation.getFont().getSize());
        StyleConstants.setIcon(iconStyle, srcUncheckIcon);
        //Icon sourceNotCheckedIcon=RadixWareIcons.WIDGETS.NEW_LINE.getIcon(fontSize*3, fontSize);
        //JLabel l=new JLabel(sourceNotCheckedIcon);
        //l.setAlignmentY(0.75f);
        //StyleConstants.setComponent(iconStyle, l);

        manager = new UndoManager();

        final UndoableEditListener docListener = new UndoableEditListener() {
            @Override
            public void undoableEditHappened(final UndoableEditEvent evt) {

                if (canUndo) {
                    manager.addEdit(evt.getEdit());
                }
            }
        };
        edTranslation.getDocument().addUndoableEditListener(docListener);

        edTranslation.getActionMap().put("Undo", new UndoAction(manager));
        edTranslation.getActionMap().put("Redo", new RedoAction(manager));
        edTranslation.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");
        edTranslation.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
    }

    /*public void createHightlight(){
     Highlighter h = edTranslation.getHighlighter();
     try {
     DefaultHighlighter.DefaultHighlightPainter redPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.red);

     h.addHighlight(0, 4, redPainter);
     } catch (BadLocationException ble) {
     }
     }

     public void removeHighlights() {
     Highlighter hilite = edTranslation.getHighlighter();
     Highlighter.Highlight[] hilites = hilite.getHighlights();

     for (int i = 0; i < hilites.length; i++) {
     hilite.removeHighlight(hilites[i]);
     }
     }*/
    public void clearUndoRedo() {
        //if(docListener!=null){
        //    edTranslation.getDocument().removeUndoableEditListener(docListener);
        manager.discardAllEdits();
        //}
        //docListener=new UndoableEditListener() {
        //   public void undoableEditHappened(UndoableEditEvent evt) {
        //     manager.addEdit(evt.getEdit());
        //   }
        //  };
        // edTranslation.getDocument().addUndoableEditListener(docListener);

    }

    private void insertImageInCurPos() {
        final int pos = edTranslation.getCaretPosition();
        addImage(pos);
    }

    private void addImage(final int pos) {
        try {
            final StyledDocument doc = (StyledDocument) edTranslation.getDocument();
            doc.insertString(pos, "\n", iconStyle);
        } catch (BadLocationException ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
    }

    public void addImages(final String text) {
        canCaretUpdate = false;
        if (text != null) {
            int count = text.indexOf('\n', 0);
            while ( count!= -1) {
                edTranslation.setSelectionStart(count);
                edTranslation.setSelectionEnd(count + 1);
                edTranslation.replaceSelection("");
                addImage(count);
                count = text.indexOf('\n', count+1);
            }
        }
        canCaretUpdate = true;
    }

    public void setBorder(final boolean selected) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (selected) {
                    edTranslation.setBorder(new javax.swing.border.BevelBorder(BevelBorder.RAISED));
                    edTranslation.validate();
                    edTranslation.repaint();           
                } else {
                    edTranslation.setBorder(border); 
                }
            }
        });

    }

    public int getFontSize() {
        return fontSize;
    }

    public int getWrappedLineCount() {
        final int length = edTranslation.getDocument().getLength();
        int offset = 0;
        int wrapLineCount = 0;
        try {
            while (offset < length) {
                int end = Utilities.getRowEnd(edTranslation, offset);
                if (end < 0) {
                    break;
                }
                end = Math.min(end + 1, length);
                final String line = edTranslation.getDocument().getText(offset, end - offset);
                offset = end;

                wrapLineCount++;
                if ((line.endsWith("\n")) && (offset == length)) {
                    wrapLineCount++;
                }
            }
        } catch (BadLocationException | NullPointerException ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }

        return wrapLineCount == 0 ? 1 : wrapLineCount;
    }

    public void setFocus() {
        lp = null;
        edTranslation.requestFocus();
    }

    // The Undo action
    public class UndoAction extends AbstractAction {
        private final UndoManager manager;
        
        public UndoAction(final UndoManager manager) {
            super();
            this.manager = manager;
        }

        @Override
        public void actionPerformed(final ActionEvent evt) {
            try {
                manager.undo();
            } catch (CannotUndoException e) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
       
    }

    // The Redo action
    public class RedoAction extends AbstractAction {
        private final UndoManager manager;
        
        public RedoAction(final UndoManager manager) {
            super();
            this.manager = manager;
        }

        @Override
        public void actionPerformed(final ActionEvent evt) {
            try {
                manager.redo();
            } catch (CannotRedoException e) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
        
    }
}
