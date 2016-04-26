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
 * TitleModalEditor.java
 *
 * Created on 17.07.2009, 11:44:11
 */
package org.radixware.kernel.designer.ads.editors.classcatalogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef.ClassReference;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef.Topic;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.schemas.adsdef.LocalizingBundleDefinition;


public class TitleModalEditor extends javax.swing.JPanel {

    class TitleDisplayer extends ModalDisplayer {

        TitleDisplayer(TitleModalEditor panel) {
            super(panel);
            panel.setMinimumSize(new Dimension(200, 150));
            setTitle(NbBundle.getMessage(TitleModalEditor.class, "TitleEditor-DialogTitle"));
            javax.swing.JButton closeBtn = new javax.swing.JButton(NbBundle.getMessage(TitleModalEditor.class, "TitleEditor-CloseBtn"));
            closeBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    TitleDisplayer.this.getDialog().setVisible(false);
                }
            });
            getDialogDescriptor().setOptions(new Object[]{closeBtn});
        }

        @Override
        protected void apply() {
            if (handleInfo != null){
                IMultilingualStringDef stringDef = handleInfo.getAdsMultilingualStringDef();
                if (stringDef != null) {
                    if (item instanceof ClassReference){
                        ((ClassReference) item).inheritTitle(isTitleInherited);
                        if (!isTitleInherited){
                            if (!onCreation)
                                ((ClassReference) item).setTitleId(stringDef.getId());
                            else
                                newTitleId = stringDef.getId();
                        }
                    } else {
                        ((Topic) item).setTitleId(stringDef.getId());
                    }
                } else {
                    if (item instanceof ClassReference){
                        ((ClassReference) item).inheritTitle(isTitleInherited);
                    } else if (item instanceof Topic){
                        ((Topic) item).setTitleId(null);
                    }
                }
            }
        }
    }

    boolean editTitle(RadixObject rdx, AdsDefinition context, boolean onCreation) {
        if (rdx != null) {
            open(rdx, context, onCreation);
            TitleDisplayer displayer = new TitleDisplayer(this);
            return displayer.showModal();
        } else {
            throw new RadixError("Undefined class catalog item!");
        }
    }
    private javax.swing.JCheckBox inheritBox = new javax.swing.JCheckBox(NbBundle.getMessage(TitleModalEditor.class, "TitleEditor-InheritBox"));
    private LocalizingEditorPanel titleEditor = new LocalizingEditorPanel();
    private GridBagLayout gbl = new GridBagLayout();
    private GridBagConstraints constraints = new GridBagConstraints();
    private javax.swing.JPanel content = new javax.swing.JPanel();

    /** Creates new form TitleModalEditor */
    public TitleModalEditor() {
        initComponents();
        setLayout(new BorderLayout());
        content.setLayout(gbl);
        add(content, BorderLayout.NORTH);

        ActionListener inheritListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                TitleModalEditor.this.onInheritanceChange();
            }
        };
        inheritBox.addActionListener(inheritListener);
    }
    private RadixObject item;
    private AdsDefinition context;
    private boolean isTitleInherited = false;
    private boolean onCreation = false;
    private Id newTitleId;

    public Id getNewTitleId(){
        return newTitleId;
    }

    public boolean isTitleInherited(){
        return isTitleInherited;
    }

    private void onInheritanceChange() {
//        if (!onCreation)
//            ((ClassReference) item).inheritTitle(inheritBox.isSelected());
        isTitleInherited = inheritBox.isSelected();
        updateTitleEditor();
        newTitleId = handleInfo.getTitleId();
    }

    public void open(RadixObject item, AdsDefinition context, boolean onCreation) {
        this.item = item;
        this.context = context;
        this.onCreation = onCreation;
        content.removeAll();
        boolean isRef = item instanceof ClassReference;
        if (isRef) {
            isTitleInherited = ((ClassReference) item).isTitleInherited();

            constraints.fill = GridBagConstraints.NONE;
            constraints.anchor = GridBagConstraints.WEST;
            constraints.insets = new Insets(10, 10, 0, 10);
            gbl.setConstraints(inheritBox, constraints);
            content.add(inheritBox);

            inheritBox.setSelected(isTitleInherited);

            constraints.gridy = 1;
        }
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.weighty = 0.0;
        gbl.setConstraints(titleEditor, constraints);
        content.add(titleEditor);

        updateTitleEditor();
        this.newTitleId = handleInfo.getTitleId();
    }

    HandleInfo handleInfo;
    private void updateTitleEditor() {
        handleInfo = new HandleInfo() {

            @Override
            public AdsDefinition getAdsDefinition() {
                return context;
            }

            @Override
            public Id getTitleId() {
                if (item instanceof Topic) {
                    return ((Topic) item).getTitleId();
                } else {
                    if (isTitleInherited) {
                        AdsClassDef ref = AdsSearcher.Factory.newAdsClassSearcher(context).findById(((ClassReference) item).getClassId()).get();
                        return ref != null ? ref.getTitleId() : null;
                    } else {
                        return ((ClassReference) item).getTitleId();
                    }
                }
            }

            private AdsMultilingualStringDef clone;
            @Override
            public AdsMultilingualStringDef getAdsMultilingualStringDef() {
                if (clone == null){
                    if (item instanceof Topic){
                        AdsMultilingualStringDef current = context.findLocalizedString(getTitleId());
                        AdsLocalizingBundleDef bundle = context.findLocalizingBundle();
                        if (current != null){
                            clone = current.cloneString(bundle);
                        } else {
                            clone = AdsMultilingualStringDef.Factory.newInstance();
                            if (bundle != null){
                                bundle.getStrings().getLocal().add(clone);
                            }
                        }

                    } else {
                        ClassReference asRef = (ClassReference) item;
                        if (isTitleInherited){
                            AdsClassDef ref = AdsSearcher.Factory.newAdsClassSearcher(context).findById(asRef.getClassId()).get();
                            if (ref != null){
                                clone = ref.findLocalizedString(ref.getTitleId());
                            }
                        } else {
                            AdsMultilingualStringDef current = context.findLocalizedString(asRef.getTitleId());
                            AdsLocalizingBundleDef bundle = context.findLocalizingBundle();
                            if (current != null) {
                                clone = current.cloneString(bundle);
                            } else {
                                clone = AdsMultilingualStringDef.Factory.newInstance();
                                if (bundle != null){
                                    bundle.getStrings().getLocal().add(clone);
                                }
                            }
                        }
                    }
                }
                return clone;
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                //do nothing
            }

            @Override
            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                this.getAdsMultilingualStringDef().setValue(language, newStringValue);
            }

            @Override
            protected boolean isBundleResettable() {
                return item instanceof Topic;
            }

        };
        titleEditor.open(handleInfo);

        if (item instanceof ClassReference) {
            titleEditor.setReadonly(isTitleInherited);
        }
    }

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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
