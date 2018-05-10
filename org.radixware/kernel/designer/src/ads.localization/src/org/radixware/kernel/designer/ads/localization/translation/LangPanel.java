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
 * LangPanel.java
 *
 * Created on Aug 12, 2009, 4:07:47 PM
 */
package org.radixware.kernel.designer.ads.localization.translation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.text.BadLocationException;

import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.PasteAction;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.ads.localization.RowString;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.localization.MultilingualEditorUtils;
import org.radixware.kernel.designer.common.dialogs.spellchecker.Spellchecker;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class LangPanel extends javax.swing.JPanel {
    private final List<EIsoLanguage> sourceLangs;
    private RowString rowString;
    private final TranslateArea panel;
    private final EIsoLanguage lang;
    private final ItemListener listener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            setAgreedTooltip();
            panel.translationWasEdited(lang);
            rowString.setAgreed(lang, isAgreed.isSelected());
        }
    };
    //private LangPanel lp=null;
    //private Style iconStyle;
    private final TranslateTextPaneUi translTextPanelUi;
    private final int defaultHeight = 60;
    private int lineCount = 1;
    private final String TRANSLATION_NOT_REQUIRED_TOLTIP = NbBundle.getMessage(LangPanel.class, "TRANSLATION_NOT_REQUIRED_TOOLTIP");
    private final JPopupMenu popupMenu = new JPopupMenu();
    /**
     * Creates new form LangPanel
     */
    public LangPanel(final RadixObject context, final EIsoLanguage lang, List<EIsoLanguage> source, final TranslateArea panel) {
        this.panel = panel;
        this.lang = lang;
        this.sourceLangs = source;
        initComponents();
        lbLang.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(final MouseEvent event) {
            }

            @Override
            public void mouseMoved(final MouseEvent event) {
                if ((rowString != null) && (canChangeStatus())) {
                    if (event.getX() <= 16) {
                        if (rowString.isNeedsCheck(LangPanel.this.lang)) {
                            lbLang.setIcon(RadixWareIcons.MLSTRING_EDITOR.TRANSLATION_NOT_CHECKED_BORDERED.getIcon(16, 16));
                        } else {
                            lbLang.setIcon(RadixWareIcons.MLSTRING_EDITOR.TRANSLATE_BORDERED.getIcon(16, 16));
                        }
                    } else {
                        lbLang.setIcon(rowString.getIcon(LangPanel.this.lang,LangPanel.this.sourceLangs));
                    }
                }
            }
        });

        lbLang.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(final MouseEvent event) {
            }

            @Override
            public void mousePressed(final MouseEvent event) {
            }

            @Override
            public void mouseReleased(final MouseEvent event) {
                if ((rowString != null) && (event.getButton() == MouseEvent.BUTTON1) && (event.getX() <= 16) && canChangeStatus()) {
                    LangPanel.this.setFocus();
                    if (changeStatus()){
                        LangPanel.this.panel.translationWasEdited(LangPanel.this.lang);
                    }
                }
            }

            @Override
            public void mouseEntered(final MouseEvent event) {
                /*if((rowString!=null)&&(e.getX()<=16)){
                 if(rowString.getCanChangeStatus(LangPanel.this.lang))
                 if(rowString.needsCheck(LangPanel.this.lang))
                 lbLang.setIcon(RadixWareIcons.WIDGETS.TRANSLATION_NOT_CHECKED_BORDERED.getIcon(16,16));
                 else
                 lbLang.setIcon(RadixWareIcons.WIDGETS.TRANSLATE_BORDERED.getIcon(16,16));
                 }*/
            }

            @Override
            public void mouseExited(final MouseEvent event) {
                if ((rowString != null) && (canChangeStatus())) {
                    lbLang.setIcon(rowString.getIcon(LangPanel.this.lang,LangPanel.this.sourceLangs));
                }
            }
        });

        translTextPanelUi = new TranslateTextPaneUi(edTranslation, this);


        lbLang.setText(lang.getName());
        setDefaultSize();
        addComponentListener(new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                updateSize();
            }
            
        });
        edTranslation.addMouseWheelListener(new MouseWheelListener() {
                @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                JScrollPane parentScroll = panel.getScrollPane();
                if (panel.getScrollPane() != null){
                    if (!jScrollPane1.getVerticalScrollBar().isVisible()){
                        parentScroll.dispatchEvent(e);
                    }
                }
                
            }
        });
        
        Spellchecker.register(edTranslation, lang, context);
        
        isAgreed.setVisible(false);
        isAgreed.setIcon(RadixWareIcons.MLSTRING_EDITOR.TRANSLATION_NOT_CHECKED.getIcon());
        isAgreed.setSelectedIcon(RadixWareIcons.DIALOG.OK.getIcon());
        isAgreed.setDisabledIcon(RadixWareIcons.MLSTRING_EDITOR.TRANSLATION_NOT_CHECKED_DISABLED.getIcon());
        isAgreed.setDisabledSelectedIcon(RadixWareIcons.MLSTRING_EDITOR.TRANSLAT_DISABLED.getIcon());
        isAgreed.setRolloverIcon(RadixWareIcons.MLSTRING_EDITOR.TRANSLATION_NOT_CHECKED_BORDERED.getIcon());
        isAgreed.setRolloverSelectedIcon(RadixWareIcons.MLSTRING_EDITOR.TRANSLATE_BORDERED.getIcon());
        
        isAgreed.addItemListener(listener);
        
                
        CutAction cutAction = SystemAction.get(CutAction.class);
        JMenuItem menuItem = DialogUtils.createMenuItem(cutAction);
        popupMenu.add(menuItem);
        CopyAction copyAction = SystemAction.get(CopyAction.class);
        menuItem = DialogUtils.createMenuItem(copyAction);
        popupMenu.add(menuItem);
        PasteAction pasteAction = SystemAction.get(PasteAction.class);
        menuItem =  DialogUtils.createMenuItem(pasteAction);
        popupMenu.add(menuItem);
        edTranslation.setComponentPopupMenu(popupMenu);
    }
    
    public void setAgreedTooltip() {
        if (isAgreed.isSelected()) {
            isAgreed.setToolTipText(org.openide.util.NbBundle.getMessage(LangPanel.class, "LangPanel.isAgreed.toolTipTex_Agree"));
        } else {
            isAgreed.setToolTipText(org.openide.util.NbBundle.getMessage(LangPanel.class, "LangPanel.isAgreed.toolTipTex_NotAgree"));
        }
    }
    
    public boolean checkString(boolean goNext){
        boolean result = true;
        final boolean oldState = rowString.isNeedsCheck(lang);
        if (oldState == true){
            result = changeTranslation(false);
            if (result) {
                panel.translationWasEdited(lang);
            }
        }
        if (result && goNext){
            panel.fireChange(MultilingualEditorUtils.GO_TO_NEXT);
        }
        return result;
    }
    
    public void setAgreedVisible(boolean visible){
        isAgreed.setVisible(visible);
    }

    public void updateSize() {
        final int newRowsCount = translTextPanelUi.getWrappedLineCount();
        if (lineCount != newRowsCount) {
            sizeChange(newRowsCount);
        }
    }

    public void translationWasEdited() {
        changeTranslation(true);
        panel.translationWasEdited(lang);
    }

    public void setCurrentLangPanel() {
        panel.setCurrentLangPanel(LangPanel.this);
    }

    public void scroll() throws BadLocationException {
        updateSize();
        final int caretPosition = edTranslation.getCaretPosition();
        final Rectangle caretRectangle = edTranslation.modelToView(caretPosition);
        if (caretRectangle != null) {
            final Rectangle rec = this.getBounds();
            caretRectangle.y = caretRectangle.y + rec.y + jScrollPane1.getBounds().y;
            panel.scroll(caretRectangle);
        }
    }


    private void sizeChange(final int line) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                lineCount = line;
                int height = defaultHeight + (translTextPanelUi.getFontSize() + 1) * (line - 1);
                setSize(new Dimension(getSize().width, height));
                setPreferredSize(new Dimension(getSize().width, height));
                panel.updateScrollPanel();
                revalidate();
            }
        });
        
    }
    

    private boolean changeStatus() {
        final boolean newState = !rowString.isNeedsCheck(lang);
        if ((!newState) && (!checkOnValidValue())) {
            return false;
        } else {
            setStatus(newState);
            return true;
        }
    }

    private boolean checkOnValidValue() {
        boolean result=true;
        if ((rowString.getValue(lang) == null) || (rowString.getValue(lang).equals(""))) {
            //String msg = "Can't change status of "+lang.getName()+" translation!\n"+"Translation on "+lang.getName()+" is not specified.";
            final String msg = "Can't change status of " + lang.getName() + " translation!\n"
                    + NbBundle.getMessage(LangPanel.class, "TRANSLATION_NOT_SPECIFIED", lang.getName());
            final NotifyDescriptor d = new NotifyDescriptor.Message(msg,
                    NotifyDescriptor.INFORMATION_MESSAGE);
            DialogDisplayer.getDefault().notify(d);
            result= false;
        }
        return result;
    }

    /*private boolean calcState(boolean needsCheck){
     boolean oldState=rowString.needsCheck(lang);
     if(!canChangeStatus()){
     if(!needsCheck)
     return oldState;
     }
     return needsCheck;
     }*/
    private boolean changeTranslation(final boolean needsCheck) {
        boolean res = false;
        final String str = edTranslation.getText();

        translTextPanelUi.setCanUndo(false);
        Highlight.hightlightTranslation(edTranslation);
        translTextPanelUi.setCanUndo(true);
        final boolean oldState = rowString.isNeedsCheck(lang);
        if (((!str.equals("")) && (rowString.getValue(lang) == null)) || 
                ((rowString.getValue(lang) != null) && (!str.equals(rowString.getValue(lang))))) {
            rowString.setValue(lang, str);
            res = true;
        }

        if (oldState != needsCheck) {
            if (setStatus(needsCheck)) res = true;
        }
        return res;
    }

    public boolean canChangeStatus() {
        return rowString.getCanChangeStatus(lang,sourceLangs);
    }

    private boolean setStatus(final boolean needsCheck) {
        if ((!edTranslation.isEditable()) || (!canChangeStatus()) || (!checkOnValidValue())) {
            return false;
        }

        rowString.setNeedCheck(lang, needsCheck);
        if (needsCheck) {
             panel.removePhraseFromPrompt(rowString);            
        } else {
            panel.addPhraseToPrompt(rowString);
        }
        isAgreed.setEnabled(!needsCheck);
        isAgreed.setSelected(rowString.isAgreed(lang));
        setAgreedTooltip();
        
        setIcon(rowString);

        panel.updateStatus(rowString);
        return true;
    }

    public boolean getStatus() {
        return rowString == null || rowString.isNeedsCheck(lang);
    }

    public void setTranslation(final String s) {
        edTranslation.setText(s);
        changeTranslation(true);
        panel.translationWasEdited(lang);
    }

    public void setRowString(final RowString rowString, final boolean readOnly) {
        this.rowString = rowString;
        setDefaultSize();
        isAgreed.removeItemListener(listener);
        edTranslation.setText(rowString.getValue(lang));
        if (rowString.isNeedTranslate(lang)) {
            //doc.setCharacterAttributes(0, s.length(), soutceTextArea.getStyle(StyleContext.DEFAULT_STYLE), true);
            translTextPanelUi.addImages(rowString.getValue(lang));
            setReadOnly(readOnly);
            isAgreed.setEnabled(!readOnly && rowString.getCanChangeStatus(lang, sourceLangs) && !rowString.isNeedsCheck(lang));
            isAgreed.setSelected(rowString.isAgreed(lang));
            setAgreedTooltip();
        } else {
            edTranslation.setToolTipText(TRANSLATION_NOT_REQUIRED_TOLTIP);
            //edTranslation.setText(TRANSLATION_NOT_REQUIRED);
            lbLang.setIcon(null);
            setReadOnly(true);
        }
        translTextPanelUi.clearUndoRedo();
        lineCount = translTextPanelUi.getWrappedLineCount();
        sizeChange(lineCount);
        setIcon(rowString);
        translTextPanelUi.setCanUndo(false);
        Highlight.hightlightTranslation(edTranslation);
        translTextPanelUi.setCanUndo(true);
        isAgreed.addItemListener(listener);
    }

    public RowString getRowString() {
        return rowString;
    }

    public void setIcon(final RowString rowString) {
        lbLang.setIcon(rowString.getIcon(lang,sourceLangs));
        lbLang.setToolTipText(rowString.getToolTip(lang,sourceLangs));
    }

    public void setFocus() {
        translTextPanelUi.setFocus();
        //lp=null;
        //edTranslation.requestFocus();
    }

    public final void setDefaultSize() {
        this.setPreferredSize(new Dimension(this.getSize().width, defaultHeight));
    }

    public void setReadOnly(final boolean readOnly) {
        edTranslation.setEditable(!readOnly);
        if (readOnly) {
            edTranslation.setForeground(Color.gray);
            edTranslation.setComponentPopupMenu(null);
        } else {
            edTranslation.setForeground(Color.black);
            edTranslation.setComponentPopupMenu(popupMenu);
        }
        lbLang.setEnabled(!readOnly);
        isAgreed.setEnabled(!readOnly);
    }
    
    public boolean isReadOnly(){
        return !edTranslation.isEditable();
    }

    public void clearPanel() {
        edTranslation.setText("");
        lbLang.setIcon(null);
    }

    public EIsoLanguage getLanguage() {
        return lang;
    }

    public void setBorder(final boolean selected) {
        translTextPanelUi.setBorder(selected);
    }

    public void save() {
        panel.save();
    }

    @Override
    public boolean isFocusOwner() {
        return super.isFocusOwner() || edTranslation.isFocusOwner();
    }
    
    

    /* private void hightlightTranslation(){
     Highlight h=new  Highlight();
     List<Integer[]> list=h.getHightlightPos(edTranslation.getText());
     MutableAttributeSet attrs = new SimpleAttributeSet();
     StyleConstants.setForeground(attrs, Color.blue);
     StyledDocument sdoc = edTranslation.getStyledDocument();
     sdoc.setCharacterAttributes(0,edTranslation.getText().length(), edTranslation.getStyle(StyleContext.DEFAULT_STYLE), false);
     for(Integer[] arr:list){
     sdoc.setCharacterAttributes(arr[0], arr[1], attrs, false);
     // n=arr[0]+arr[1];
     //sdoc.setCharacterAttributes(n,n+1, edTranslation.getStyle(StyleContext.DEFAULT_STYLE), false);
     }
     Highlight.hightlightTranslation(edTranslation);
     }*/
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbLang = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        edTranslation = new javax.swing.JTextPane();
        isAgreed = new javax.swing.JCheckBox();

        setBackground(java.awt.Color.white);
        setPreferredSize(new java.awt.Dimension(200, 40));

        lbLang.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/designer/ads/localization/translation/Bundle"); // NOI18N
        lbLang.setText(bundle.getString("LangPanel.lbLang.text")); // NOI18N

        jScrollPane1.setMinimumSize(new java.awt.Dimension(24, 22));
        jScrollPane1.setViewportView(edTranslation);

        isAgreed.setBackground(new java.awt.Color(255, 255, 255));
        isAgreed.setFont(new java.awt.Font("DejaVu Sans", 1, 13));
        isAgreed.setFocusPainted(false);
        isAgreed.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        isAgreed.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        isAgreed.setLabel(org.openide.util.NbBundle.getMessage(LangPanel.class, "LangPanel.isAgreed.label")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lbLang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addComponent(isAgreed))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbLang)
                    .addComponent(isAgreed))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane edTranslation;
    private javax.swing.JCheckBox isAgreed;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbLang;
    // End of variables declaration//GEN-END:variables
}
