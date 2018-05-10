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

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.ads.localization.MultilingualEditorUtils.SelectionInfo;
import org.radixware.kernel.designer.ads.localization.RowString;
import org.radixware.kernel.designer.ads.localization.phrase_book.PhraseBookTranslationPanel;
import org.radixware.kernel.designer.ads.localization.source.MlsTablePanel;


public class TranslateArea {
    private List<LangPanel> langPanels;
    private LangPanel currentLangPanel;
    private ITranslationPanel panel;
    private JPanel translationPane;
    private boolean isTranslatedPanel;
    private List<EIsoLanguage> langs;
    private List<EIsoLanguage> sourceLangs;

    /** Creates new form TranslationPanel */
    public TranslateArea(final ITranslationPanel panel, final JPanel translationPane, final boolean isTranslatedPanel) {
        this.translationPane=translationPane;
        this.panel=panel;
        this.isTranslatedPanel = isTranslatedPanel;
        createUI();
        this.translationPane.validate();
        this.translationPane.repaint();
    }   
    
    public void open(List<EIsoLanguage> langs, List<EIsoLanguage> sourceLangs){
        this.langs = langs;
        this.sourceLangs = sourceLangs;
    }
    
    private void createUI(){        
        translationPane.setBorder(new RoundedBorder(10,6));
        //translationPane.setBackground(Color.WHITE);
        translationPane.setPreferredSize(null);
        ///BoxLayout l=new BoxLayout(translationPane, BoxLayout.Y_AXIS);
        translationPane.setLayout(new BoxLayout(translationPane, BoxLayout.Y_AXIS));
        langPanels=new ArrayList<>();
    }

    
    public void update(RadixObject context){
        RowString rowString=null;
        if(context==null && currentLangPanel!=null &&currentLangPanel.getRowString()!=null)
            rowString=currentLangPanel.getRowString();
        final int size=langs.size();
        langPanels.clear();
        translationPane.removeAll();
        translationPane.setPreferredSize(null);
        for(int i=0;i<size;i++){
            context=context==null && rowString!=null ? (RadixObject)rowString.getMlString(langs.get(i)) : context;
            LangPanel langPanel=new LangPanel(context, langs.get(i), sourceLangs, this);
            langPanel.setBorder(new EmptyBorder(/*10*/0, 10, 10/*15*/, 15/*20*/));
            translationPane.add(langPanel);
            langPanels.add(langPanel);
        }
        selectFirstPanel();
        drawBorder();
    }

    private void drawBorder(){
        if(!isTranslatedPanel)return ;
        for(int i=0;i<langPanels.size();i++){
            if(langPanels.get(i).equals(currentLangPanel)){
                langPanels.get(i).setBorder(true);
            }else{
                langPanels.get(i).setBorder(false);
            }
        }
    }
 
    public boolean selectFirstPanel(){
        if(!langPanels.isEmpty()){
            return setCurrentLangPanel(0);
        }
        return false;
    }
    
    public boolean selectFirstUncheckedPanel(){
        if(!langPanels.isEmpty()){
            return goToNextUncheckedTranslation(0);
        }
        return false;
    }
    
    public boolean selectLastPanel(){
        if(!langPanels.isEmpty()){
            return setCurrentLangPanel(langPanels.size() - 1);
        }
        return false;
    }
    
    public boolean selectLastUncheckedPanel(){
        if(!langPanels.isEmpty()){
            return goToPreviousUncheckedTranslation(langPanels.size() - 1);
        }
        return false;
    }
    
    public boolean selectFirstEditablePanel(){
        if(!langPanels.isEmpty()){
            return goToNextEditableTranslation(0);
        }
        return false;
    }
    
    public boolean selectLastEditablePanel(){
        if(!langPanels.isEmpty()){
            return goToPreviousEditableTranslation(langPanels.size() - 1);
        }
        return false;
    }

    public boolean goToNextTranslation(){
        LangPanel langPanel = getCurrentLangPanel();
        int index=langPanels.indexOf(langPanel)+1;
        while(index < langPanels.size()){
            if (setCurrentLangPanel(index)){
               return true; 
            }
            index++;
        }
        
        return false;
    }
    
    public boolean goToPreviousTranslation(){
        LangPanel langPanel = getCurrentLangPanel();
        int index=langPanels.indexOf(langPanel)-1;
        while (index >= 0){
           if (setCurrentLangPanel(index)) {
               return true;
           }
           index--;
        }
        return false;
    }
    
    public boolean goToNextUncheckedTranslation(int index){
        while(index<langPanels.size()){
            if(langPanels.get(index).getStatus()){
                if (setCurrentLangPanel(index)){
                    return true;
                }
            }
            index++;
        }
        return false;
    }
    
    public boolean goToNextUncheckedTranslation(){
        LangPanel langPanel = getCurrentLangPanel();
        int index=langPanels.indexOf(langPanel) + 1;
        return goToNextUncheckedTranslation(index);
    }
    
    private boolean goToPreviousUncheckedTranslation(int index){
        while(index >= 0){
            if (langPanels.get(index).getStatus()){
                if (setCurrentLangPanel(index)){
                    return true;
                }
            }
            index--;
        }
        return false;
    }
    
    public boolean goToPreviousUncheckedTranslation(){
        LangPanel langPanel = getCurrentLangPanel();
        int index = langPanels.indexOf(langPanel)-1;
        return goToPreviousUncheckedTranslation(index);
    }
    
    public boolean goToPreviousEditableTranslation(){
        LangPanel langPanel = getCurrentLangPanel();
        int index = langPanels.indexOf(langPanel) - 1;
        return goToPreviousEditableTranslation(index);
    }
    
    private boolean goToPreviousEditableTranslation(int index){
        while(index >= 0){
            if (!langPanels.get(index).isReadOnly()){
                if (setCurrentLangPanel(index)){
                    return true;
                }
            }
            index--;
        }
        return false;
    }
    
    public boolean goToNextEditableTranslation(){
        LangPanel langPanel = getCurrentLangPanel();
        int index = langPanels.indexOf(langPanel) + 1;
        return goToNextEditableTranslation(index);
    }
    
    private boolean goToNextEditableTranslation(int index){
        while(index < langPanels.size()){
            if(!langPanels.get(index).isReadOnly()){
                if (setCurrentLangPanel(index)){
                    return true;
                }
            }
            index++;
        }
        return false;
    }

    
    
    private boolean setCurrentLangPanel(final int index){
        currentLangPanel=langPanels.get(index);
        setFocus();
        return true;
    }
    
    public void setFocus() {
        if (currentLangPanel != null){
            currentLangPanel.setFocus();
            translationPane.scrollRectToVisible(currentLangPanel.getBounds());
        }
    }

    public void updateScrollPanel(){
        panel.updateScrollPanel();
    }
    
    public JScrollPane getScrollPane() {
        return panel.getTranslationPanelScrollPane();
    }

    public void setRowString(final RowString rowString){
        for (LangPanel langPanel : langPanels) {
            if (!(panel instanceof PhraseBookTranslationPanel)) {
                langPanel.setAgreedVisible(true);
            }
            langPanel.setRowString(rowString, panel.isReadOnly());
        }
    }
    
    public boolean checkAll(){
        for (LangPanel langPanel : langPanels) {
            if (!langPanel.checkString(false)){
                return false;
            }
        }
        return true;
    }

    public void clearPanel(final boolean setFocusOnTranslation){
        for(int i=0;i<langPanels.size();i++){
           langPanels.get(i).clearPanel();
           langPanels.get(i).setDefaultSize();
        }
        translationPane.validate();
        translationPane.repaint();
    }

    public void scroll(final Rectangle rect){
        rect.y+=translationPane.getBounds().y;
        panel.scroll(rect);
    }


    public void setReadOnly(final boolean readOnly){
        clearPanel(false);
        for(int i=0;i<langPanels.size();i++){
           langPanels.get(i).setReadOnly(readOnly);
        }
    }

    public EIsoLanguage getCurrentTranslateLang(){
        if(currentLangPanel!=null)
            return currentLangPanel.getLanguage();
        return null;
    }

    public void translationWasEdited(final EIsoLanguage lang){
        panel.translationWasEdited(lang);
        panel.checkWarnings(currentLangPanel.getRowString());
    }

    public void setTranslationFromPraseList(final String translation){
        if(currentLangPanel!=null)
            currentLangPanel.setTranslation(translation);
    }

    public void setCurrentLangPanel(final LangPanel curPanel){
         currentLangPanel=curPanel;
         panel.updatePhrasesPanel();
         drawBorder();
         panel.checkWarnings(currentLangPanel.getRowString());
     }

     public LangPanel getCurrentLangPanel(){
         return currentLangPanel;
     }

     public void addPhraseToPrompt(final RowString rowString){
         panel.addPhraseToPrompt(rowString);
     }

     public void removePhraseFromPrompt(final RowString rowString){
         panel.removePhraseFromPrompt(rowString);
     }

     public void updateStatus(final RowString rowString){
        for(int i=0;i<langPanels.size();i++){
           langPanels.get(i).setIcon(rowString);
        }
        if(!isTranslatedPanel)
            panel.updateTargetLangsStatus(rowString);
     }

     public void save(){
        panel.save();
     }
     
    public void fireChange(String key){
        panel.fireChange(key);
    }
}
