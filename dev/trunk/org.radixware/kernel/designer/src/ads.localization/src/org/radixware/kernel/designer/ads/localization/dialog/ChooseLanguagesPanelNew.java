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
 * NewJPanel.java
 *
 * Created on Jul 15, 2010, 4:33:20 PM
 */
package org.radixware.kernel.designer.ads.localization.dialog;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.ads.localization.MultilingualEditor;


public class ChooseLanguagesPanelNew extends JPanel {

    private final LanguagesListPanel sourseLangsList;
    private final LanguagesListPanel targetLangsList;

    /**
     * Creates new form NewJPanel
     */
    public ChooseLanguagesPanelNew(final List<EIsoLanguage> sourceLangs, final List<EIsoLanguage> translLangs) {
        initComponents();
        final JPanel userLangPanel = new JPanel();
        userLangPanel.setLayout(new javax.swing.BoxLayout(userLangPanel, javax.swing.BoxLayout.Y_AXIS));
        final JPanel sourceLangPanel = new JPanel();
        sourceLangPanel.setLayout(new javax.swing.BoxLayout(sourceLangPanel, javax.swing.BoxLayout.X_AXIS));
        final JPanel targetLangPanel = new JPanel();
        targetLangPanel.setLayout(new javax.swing.BoxLayout(targetLangPanel, javax.swing.BoxLayout.X_AXIS));

        final List<EIsoLanguage> newSourseLangs = createNewLangList(sourceLangs);
        final List<EIsoLanguage> newTargetLangs = createNewLangList(translLangs);
        sourseLangsList = new LanguagesListPanel(newSourseLangs, "Languages to translate from:");
        final LanguagesListPanel layerLangsList = new LanguagesListPanel(getFilteredLangs(newSourseLangs, newTargetLangs), "Product languages:");
        final ButtonsPanel btnPanel1 = new ButtonsPanel(this, sourseLangsList.getJList(), layerLangsList.getJList());
        targetLangsList = new LanguagesListPanel(newTargetLangs, "Languages to translate to:");
        final ButtonsPanel btnPanel2 = new ButtonsPanel(this, targetLangsList.getJList(), layerLangsList.getJList());

        sourseLangsList.setSelection();
        layerLangsList.setSelection();
        targetLangsList.setSelection();

        sourceLangPanel.add(sourseLangsList);
        sourceLangPanel.add(btnPanel1);
        targetLangPanel.add(targetLangsList);
        targetLangPanel.add(btnPanel2);
        userLangPanel.add(sourceLangPanel);
        userLangPanel.add(targetLangPanel);

        this.add(userLangPanel);
        this.add(layerLangsList);
    }

    private List<EIsoLanguage> createNewLangList(final List<EIsoLanguage> oldLangList) {
        final List<EIsoLanguage> newLangList = new ArrayList<EIsoLanguage>();
        for (EIsoLanguage lang : oldLangList) {
            newLangList.add(lang);
        }
        return newLangList;
    }

    public void check() {
        if (this.getParent() instanceof ChooseLanguagesDialog) {
            ((ChooseLanguagesDialog) this.getParent()).check();
        }
    }

    /*private List<EIsoLanguage> getFilteredTargetLangs(List<EIsoLanguage> sourceLangs,List<EIsoLanguage> translLangs) {
     List<EIsoLanguage> langs=new ArrayList<EIsoLanguage>();
     List<EIsoLanguage> allLangs=getAllLangs();
     for(EIsoLanguage lang:translLangs)
     if((!sourceLangs.contains(lang))&&(allLangs.contains(lang)))
     langs.add(lang);
     return langs;
     }*/
    private List<EIsoLanguage> getFilteredLangs(final List<EIsoLanguage> sourceLangs, final List<EIsoLanguage> translLangs) {
        final List<EIsoLanguage> allLangs = MultilingualEditor.getAllLangs();
        for (EIsoLanguage lang1 : sourceLangs) {
            if (allLangs.contains(lang1)) {
                allLangs.remove(lang1);
            }
        }
        for (EIsoLanguage lang1 : translLangs) {
            if (allLangs.contains(lang1)) {
                allLangs.remove(lang1);
            }
        }
        return allLangs;
    }

    /* private List<EIsoLanguage> getAllLangs() {
     List<EIsoLanguage> langs = new ArrayList<EIsoLanguage>();
     Collection<Branch> openBranches = RadixFileUtil.getOpenedBranches();
     for(Branch branch : openBranches){
     for(final Layer layer : branch.getLayers()){
     int size=layer.getLanguages().size();
     final EIsoLanguage[] langsArr = new EIsoLanguage[size];
     layer.getLanguages().toArray(langsArr);
     for(int i=0;i<size;i++){
     if(!langs.contains(langsArr[i]))
     langs.add(langsArr[i]);
     }
     }
     }
     return langs;
     }*/
    public List<EIsoLanguage> getTargetLangs() {
        return targetLangsList.getSelectedLangs();
    }

    public List<EIsoLanguage> getSourceLangs() {
        return sourseLangsList.getSelectedLangs();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setAlignmentY(1.0F);
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
