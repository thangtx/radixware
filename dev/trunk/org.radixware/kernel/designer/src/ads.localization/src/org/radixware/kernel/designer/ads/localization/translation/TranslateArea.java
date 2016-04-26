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
import org.radixware.kernel.designer.ads.localization.RowString;
import org.radixware.kernel.designer.ads.localization.source.MlsTablePanel;


public class TranslateArea {
    private List<LangPanel> langPanels;
    private LangPanel currentLangPanel;
    private ITranslationPanel panel;
    private JPanel translationPane;
    private boolean isTargetLangPanel;
    private List<EIsoLanguage> langs;
    private List<EIsoLanguage> sourceLangs;

    /** Creates new form TranslationPanel */
    public TranslateArea(final ITranslationPanel panel, final JPanel translationPane, final boolean isTargetLangPanel) {
        this.translationPane=translationPane;
        this.panel=panel;
        this.isTargetLangPanel=isTargetLangPanel;
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
        if(!langPanels.isEmpty())
            currentLangPanel=langPanels.get(0);
        drawBorder();
    }

    private void drawBorder(){
        if(!isTargetLangPanel)return ;
        for(int i=0;i<langPanels.size();i++){
            if(langPanels.get(i).equals(currentLangPanel)){
                langPanels.get(i).setBorder(true);
            }else{
                langPanels.get(i).setBorder(false);
            }
        }
    }

    public void goToNextTranslation(final LangPanel langPanel){
        int index=langPanels.indexOf(langPanel)+1;
        if(index<langPanels.size()){
            setCurrentLangPanel( index);
        }else{
            currentLangPanel=langPanels.get(0);
            panel.setNextRowSting();
        }
    }

    public void goToPreviousTranslation(final LangPanel langPanel){
        int index=langPanels.indexOf(langPanel)-1;
        if(index>=0){
            setCurrentLangPanel( index);
        }else{
            currentLangPanel=langPanels.get(langPanels.size()-1);
            panel.setPrevRowSting();
        }
    }
    
    public void goToNextUncheckedTranslation(final LangPanel langPanel){
        int index=langPanels.indexOf(langPanel)+1;
        while((index<langPanels.size())&&(!langPanels.get(index).getStatus())){
            index++;
        }
        if(index<langPanels.size()){
            setCurrentLangPanel( index);
        }else{
            currentLangPanel=langPanels.get(0);
            panel.setNextUncheckedRowSting();
        }

    }

    public void goToPreviousUncheckedTranslation(final LangPanel langPanel){
        int index=langPanels.indexOf(langPanel)-1;
        while((index>=0)&&(!langPanels.get(index).getStatus())){
            index--;
        }
        if(index>=0){
            setCurrentLangPanel( index);
        }else{
            currentLangPanel=langPanels.get(langPanels.size()-1);
            panel.setPrevUncheckedRowSting();
        }
    }

    public void setCursorOnNextTranslation(final LangPanel langPanel){
        if((!langPanel.getStatus())&& langPanel.canChangeStatus()){
            translationWasEdited(langPanel.getLanguage());
        }
        goToNextTranslation(langPanel);
    }

    //public void setCursorOnPrevTranslation(LangPanel langPanel){
    //    translationWasEdited(langPanel.getLanguage());
    //    goToPreviousTranslation(langPanel);
    //}

    private void setCurrentLangPanel(final int index){
        currentLangPanel=langPanels.get(index);
        currentLangPanel.setFocus();
        translationPane.scrollRectToVisible(currentLangPanel.getBounds());
        
    }

    public void updateScrollPanel(){
        panel.updateScrollPanel();
    }
    
    public JScrollPane getScrollPane() {
        return panel.getTranslationPanelScrollPane();
    }

    public void setRowString(final RowString rowString,final boolean setFocusOnTranslation,int selectUncheckedTranslation){
        for(int i=0;i<langPanels.size();i++){
           langPanels.get(i).setRowString(rowString,panel.isReadOnly());
           //langPanels.get(i).hightlightTranslation();
           if((selectUncheckedTranslation!=MlsTablePanel.NONE)&&(langPanels.get(i).getStatus())){
               currentLangPanel=langPanels.get(i);
               if(selectUncheckedTranslation==MlsTablePanel.UNCHECK_NEXT)
                    selectUncheckedTranslation=MlsTablePanel.NONE;
           }
        }
        if(setFocusOnTranslation){
            currentLangPanel.setFocus();
            translationPane.scrollRectToVisible(currentLangPanel.getBounds());
        }
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
        if(!isTargetLangPanel)
            panel.updateTargetLangsStatus(rowString);
     }

     public void save(){
        panel.save();
     }
}
