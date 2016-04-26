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
 * SourceTextPanel.java
 *
 * Created on Oct 8, 2009, 2:27:47 PM
 */
package org.radixware.kernel.designer.ads.localization.translation;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.ads.localization.RowString;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.spellchecker.Spellchecker;


public class SourceTextPanel extends javax.swing.JPanel {

    private javax.swing.JLabel lbIcon;
    private javax.swing.JTextPane soutceTextArea;
    private EIsoLanguage lang;
    private RowString rowString;
    private final String NEW_LINE_STYLE = "NewLineIconStyle";
    final List<EIsoLanguage> sourceLangs;
    //private String LANGUAGE_STYLE="LanguageStyle";

    /**
     * Creates new form SourceTextPanel
     */
    public SourceTextPanel(final RadixObject context, final EIsoLanguage lang, final List<EIsoLanguage> sourceLangs) {
        this.lang = lang;
        this.sourceLangs = sourceLangs;
        initComponents();

        createUi();
        final int fontSize = soutceTextArea.getFontMetrics(soutceTextArea.getFont()).getHeight();
        final Icon newLineIcon = RadixWareIcons.MLSTRING_EDITOR.NEW_LINE.getIcon((fontSize - 2) * 3, fontSize - 2);
        createStyles("NewLineIconStyle", newLineIcon, null, true);
        //createStyles("LanguageStyle",null,Color.blue,true);


        Spellchecker.register(soutceTextArea, lang, context);
    }

    private Style createStyles(final String styleName, final Icon icon, final Color fontColor, final boolean bold) {
        final Style defaultStyle = soutceTextArea.getStyle(StyleContext.DEFAULT_STYLE);
        final StyledDocument doc = (StyledDocument) soutceTextArea.getDocument();
        final Style iconStyle = doc.addStyle(styleName, defaultStyle);
        StyleConstants.setFontSize(iconStyle, soutceTextArea.getFont().getSize());
        StyleConstants.setBold(iconStyle, bold);
        if (icon != null) {
            StyleConstants.setIcon(iconStyle, icon);
        }
        if (fontColor != null) {
            StyleConstants.setForeground(iconStyle, fontColor);
        }
        return iconStyle;
    }

    private void createUi() {
        this.setPreferredSize(null);
        lbIcon = new javax.swing.JLabel();
        lbIcon.setBackground(Color.white);
        lbIcon.setEnabled(false);
        lbIcon.setVerticalAlignment(javax.swing.JLabel.TOP);
        lbIcon.setVerticalTextPosition(javax.swing.JLabel.TOP);
        lbIcon.setAlignmentY(0);
        lbIcon.setMaximumSize(new Dimension(16, 16));

        soutceTextArea = new javax.swing.JTextPane();
        soutceTextArea.setBackground(Color.white);
        soutceTextArea.setOpaque(true);
        soutceTextArea.setBorder(null);
        soutceTextArea.setEditable(false);
        soutceTextArea.setFont(new java.awt.Font("DejaVu Sans", 3, 13));
        soutceTextArea.setBorder(new EmptyBorder(2, 0, 5, 0));
        soutceTextArea.setAutoscrolls(false);
        final javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane1.setBorder(null);
        jScrollPane1.setViewportBorder(null);
        jScrollPane1.setViewportView(soutceTextArea);
        this.add(jScrollPane1);
        //this.add(soutceTextArea);
    }

    public void setSourceValue(final RowString rowString) {
        this.rowString = rowString;
        updatePanel();
    }

    public void updatePanel() {
        if (rowString == null) {
            lbIcon.setIcon(null);
            soutceTextArea.setText("");
        } else {           
            lbIcon.setIcon(rowString.getIcon(lang,sourceLangs));
            setSourceText(rowString);
        }
    }

    private void setSourceText(final RowString rowString) {
        final StyledDocument doc = (StyledDocument) soutceTextArea.getDocument();
        final String str = rowString.getValue(lang);
        String strLanguage;

        if (str == null || "".equals(str)) {
            final String msg = NbBundle.getMessage(SourceTextPanel.class, "TRANSLATION_NOT_DEFINED", lang.getName());
            strLanguage = " (" + msg + ")";
            soutceTextArea.setText("");
        } else {
            final String strCheck = rowString.needsCheck(lang) ? ", unchecked" : "";
            strLanguage = " (" + lang.getName() + strCheck + ")";
            if (rowString.needsCheck(lang)) {
                soutceTextArea.setToolTipText(NbBundle.getMessage(SourceTextPanel.class, "SRC_NOT_CHECKED"));
                soutceTextArea.setForeground(Color.gray);
            } else {
                soutceTextArea.setToolTipText(null);
                soutceTextArea.setForeground(Color.black);
            }
            soutceTextArea.setEditable(true);
            soutceTextArea.setText(str);
            doc.setCharacterAttributes(0, str.length(), soutceTextArea.getStyle(StyleContext.DEFAULT_STYLE), true);
            addNewLineImages();
            Highlight.hightlightTranslation(soutceTextArea);
            soutceTextArea.setEditable(false);

        }
        //setText(soutceTextArea.getText().length(),LANGUAGE_STYLE,strLanguage);

        // The component must first be wrapped in a style
        final Style style = doc.addStyle("styleName", null);
        final JLabel label = new JLabel(strLanguage);
        label.setFont(new java.awt.Font("DejaVu Sans", 3, 13));
        label.setForeground(Color.blue);
        label.setAlignmentY(0.75f);
        StyleConstants.setComponent(style, label);
        try {
            // Insert the component at the end of the text
            doc.insertString(doc.getLength(), "ignored text", style);
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    private void addNewLineImages() {
        
        final String text = soutceTextArea.getText();
        int count = text.indexOf('\n', 0);
        while (count != -1) {
            soutceTextArea.setSelectionStart(count);
            soutceTextArea.setSelectionEnd(count + 1);
            soutceTextArea.replaceSelection("");
            setText(count, NEW_LINE_STYLE, "\n");
            count = text.indexOf('\n', count+1);
        }
    }

    /*public void addImages(String text){
     int n=0;
     if(text==null)return;
     while((n=text.indexOf("\n",n))!=-1){
     edTranslation.setSelectionStart(n);
     edTranslation.setSelectionEnd(n+1);
     edTranslation.replaceSelection("");
     addImage(n);
     n+=1;
     }
     }


     private void addImage(int pos){
     try {
     StyledDocument doc = (StyledDocument)edTranslation.getDocument();


     doc.insertString(pos, "\n", iconStyle);

     } catch (BadLocationException e) {
     }
     }*/
    private void setText(final int pos, final String styleName, final String str) {
        try {
            final StyledDocument doc = (StyledDocument) soutceTextArea.getDocument();
            final Style style = soutceTextArea.getStyle(styleName);
            doc.insertString(pos, str, style);
        } catch (BadLocationException ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
    }

    /*private void hightlightTranslation(){
     Highlight.hightlightTranslation(soutceTextArea);
     List<Integer[]> list=h.getHightlightPos(soutceTextArea.getText());
     MutableAttributeSet attrs = new SimpleAttributeSet();
     StyleConstants.setForeground(attrs, Color.blue);
     StyledDocument sdoc = soutceTextArea.getStyledDocument();
     sdoc.setCharacterAttributes(0,soutceTextArea.getText().length(), soutceTextArea.getStyle(StyleContext.DEFAULT_STYLE), false);
     for(Integer[] arr:list){
     sdoc.setCharacterAttributes(arr[0], arr[1], attrs, false);
     // n=arr[0]+arr[1];
     //sdoc.setCharacterAttributes(n,n+1, edTranslation.getStyle(StyleContext.DEFAULT_STYLE), false);
     }
     }*/
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(java.awt.Color.white);
        setPreferredSize(new java.awt.Dimension(0, 0));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
