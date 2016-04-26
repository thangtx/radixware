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
 * PhraseBookPanel.java
 *
 * Created on Aug 26, 2009, 3:33:15 PM
 */

package org.radixware.kernel.designer.ads.localization.phrase_book;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.localization.AdsPhraseBookDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.ads.localization.RowString;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsPhraseBookEditor  extends RadixObjectEditor<AdsPhraseBookDef> {

   private MlstringPanel topPanel;
   private PhraseBookTranslationPanel bottomPanel;
   private ExtendsPanel extendsPanel;
   private DescriptionPanel descriptionPanel;

   private AdsPhraseBookDef phraseBook;
   private List<RowString> mlStrings;
   private List<IMultilingualStringDef> editMlStrings;
   private List<EIsoLanguage> langList;
   final Map<EIsoLanguage,Boolean> langsMap = new HashMap<>();

   javax.swing.JPanel jPanel1 ;
   javax.swing.JScrollPane translationScrollPane;

    /** Creates new form PhraseBookPanel */
    public AdsPhraseBookEditor(final AdsPhraseBookDef phraseBook) {
        super(phraseBook);
        initComponents();
       /* this.addKeyListener(new KeyAdapter(){
             @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == KeyEvent.VK_S)&&(e.isControlDown())){
                   //save();
                 }
            }
        });*/

        this.phraseBook=phraseBook;
        final Layer layer=phraseBook.getModule().getSegment().getLayer();
        langList = new ArrayList<>(layer.getLanguages());
        for(EIsoLanguage lang:langList){
            langsMap.put(lang, layer.isReadOnly());
        }
        editMlStrings=new ArrayList<>();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        descriptionPanel=new DescriptionPanel();
        descriptionPanel.setAlignmentX(0.0f);
        descriptionPanel.open(phraseBook);
        descriptionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        extendsPanel=new ExtendsPanel(this.phraseBook);
        extendsPanel.setAlignmentX(0.0f);
        extendsPanel.setBorder(new EmptyBorder(10, 10, 20, 10));

        bottomPanel= new PhraseBookTranslationPanel(this,langList);
        
        jPanel1 = new javax.swing.JPanel();
        //jPanel1.setBackground(java.awt.Color.white);
        jPanel1.setPreferredSize(null);
        jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.Y_AXIS));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bottomPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(bottomPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE)
                .addContainerGap(0, Short.MAX_VALUE))//196
        );
        jPanel1.add(bottomPanel);

        translationScrollPane= new javax.swing.JScrollPane();
        translationScrollPane.setViewportView(jPanel1);

        topPanel=new MlstringPanel(this);

        JSplitPane splitPane=new JSplitPane();
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(topPanel);
        splitPane.setBottomComponent(translationScrollPane);
        splitPane.setAlignmentX(0.0f);
        splitPane.setResizeWeight(0.4);
        splitPane.setBorder(new EmptyBorder(0, 10, 10, 10));

        this.add(descriptionPanel);
        //this.add(extendsPanel);
        this.add(splitPane);

        update();
    }

    @Override
     public boolean open(final OpenInfo info) {
        final RadixObject radixObject = info.getTarget();
        if (radixObject instanceof IMultilingualStringDef){
            topPanel.setCurrentRowString((IMultilingualStringDef)radixObject);
        }
        update();         
        return super.open(info);
    }

    @Override
    public final void update() {
        mlStrings=new ArrayList<>();
        final AdsLocalizingBundleDef mlBundle=phraseBook.findExistingLocalizingBundle();
        if((mlBundle!=null)&&(mlBundle.getStrings()!=null)){
            List<AdsMultilingualStringDef> list=mlBundle.getStrings().getLocal().list();
            //list.addAll(getSuperPhraseBookStrings());//??????
            for(IMultilingualStringDef mlstring : list){
                 RowString row=new  RowString(mlstring,false);
                 if(editMlStrings.contains(mlstring))
                       row.setWasEdit(true);
                 mlStrings.add(row);
                 phraseBook.addString(mlstring);
            }
        }

        bottomPanel.update(mlBundle);
        topPanel.update(mlStrings, langList);
        extendsPanel.update();
    }

    public void setReadonly(final boolean readonly) {
        descriptionPanel.setReadonly(readonly);
        extendsPanel.setReadOnly(readonly);       
        bottomPanel.setReadOnly(readonly);
        topPanel.setReadOnly(readonly);
    }

   /* private List<AdsMultilingualStringDef> getSuperPhraseBookStrings(){
        List<AdsMultilingualStringDef> list=new ArrayList();
        if(phraseBook.getSuperPhraseBook()!=null){
             AdsLocalizingBundleDef superMlBundle=phraseBook.getSuperPhraseBook().findExistingLocalizingBundle();
             if(superMlBundle!=null)
                    list.addAll(superMlBundle.getStrings().get(EScope.LOCAL));
        }
        return list;
    }*/
    
    public void updateScrollPanel() {
        if(translationScrollPane!=null){
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    translationScrollPane.validate();
                    translationScrollPane.repaint();
                }
            });
            
        }
    }

    public void scroll(final Rectangle rect) {
        jPanel1.validate();
        jPanel1.repaint();
        if(!jPanel1.getVisibleRect().contains(rect)){
            jPanel1.scrollRectToVisible(rect);
        }
    }

    public void setRowString(final RowString rowString, final boolean setFocusOnTranslation) {
        bottomPanel.setMlString(rowString,setFocusOnTranslation);
    }

    public List<IMultilingualStringDef> getEditedStringList() {
         return editMlStrings;
    }

    public void translationWasEdited() {
         topPanel.updateStringRow();
         phraseBook.setEditState(RadixObject.EEditState.MODIFIED);
    }

    public void setNextString() {
          topPanel.setNextString();
    }

    public RowString addMlString(final boolean isEventCode) {
        final AdsMultilingualStringDef newMlString;
        if(isEventCode)
            newMlString=AdsMultilingualStringDef.Factory.newEventCodeInstance();
        else
            newMlString=AdsMultilingualStringDef.Factory.newInstance();
        final AdsLocalizingBundleDef mlBundle=phraseBook.findLocalizingBundle();
        mlBundle.getStrings().getLocal().add(newMlString);
        phraseBook.addString(newMlString);
        final RowString row=new  RowString(newMlString,false);
        mlStrings.add(row);
        return row;
    }

    public void removeMlString(final RowString rowString) {
        mlStrings.remove(rowString);
        AdsLocalizingBundleDef mlBundle=phraseBook.findExistingLocalizingBundle();
        for(IMultilingualStringDef mlString :rowString.getMlStrings()){
            if((mlString instanceof AdsMultilingualStringDef) && mlBundle.getStrings().getLocal().contains((AdsMultilingualStringDef)mlString)){
                mlBundle.getStrings().getLocal().remove((AdsMultilingualStringDef)mlString);
                phraseBook.removeString(mlString);
            }
        }
    }

    public void save(){
        editMlStrings.clear();
        for(RowString str:mlStrings)
            if(str.getWasEdit())
                str.setWasEdit(false);
        topPanel.update();
    }

    /* @Override
    public boolean open(OpenInfo info) {
        return super.open(info);
    }*/


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsPhraseBookDef> {

        @Override
        public RadixObjectEditor<AdsPhraseBookDef> newInstance(AdsPhraseBookDef uiDef) {
            return new AdsPhraseBookEditor(uiDef);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
