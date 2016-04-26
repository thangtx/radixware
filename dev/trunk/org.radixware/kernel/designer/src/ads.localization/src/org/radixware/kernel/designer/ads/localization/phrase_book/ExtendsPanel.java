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
 * Extends.java
 *
 * Created on Aug 28, 2009, 6:05:47 PM
 */
package org.radixware.kernel.designer.ads.localization.phrase_book;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.localization.AdsPhraseBookDef;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class ExtendsPanel extends javax.swing.JPanel {

    private final JButton btnSetExtends;
    private final JButton btnRemoveExtends;
    private final JButton btnGoToExtends;
    private final JButton btnSelectInExplorer;
    private AdsPhraseBookDef phraseBook = null;
    private boolean isReadonly = false;

    //private Id extendsId;
    /**
     * Creates new form Extends
     */
    public ExtendsPanel(final AdsPhraseBookDef phraseBook) {
        this.phraseBook = phraseBook;
        initComponents();
        btnSetExtends = extendableTextField.addButton();
        btnSetExtends.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        btnSetExtends.setToolTipText(NbBundle.getMessage(ExtendsPanel.class, "CHOOSE_SUPER"));
        btnSetExtends.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ExtendsPanel.this.setExtends();
            }
        });

        btnRemoveExtends = extendableTextField.addButton();
        btnRemoveExtends.setToolTipText(NbBundle.getMessage(ExtendsPanel.class, "RESET_SUPER"));
        btnRemoveExtends.setIcon(RadixWareIcons.DELETE.CLEAR.getIcon());
        btnRemoveExtends.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ExtendsPanel.this.resetExtends();
            }
        });

        btnGoToExtends = extendableTextField.addButton();
        btnGoToExtends.setToolTipText(NbBundle.getMessage(ExtendsPanel.class, "OPEN"));
        btnGoToExtends.setIcon(RadixWareIcons.ARROW.GO_TO_OBJECT.getIcon());
        btnGoToExtends.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ExtendsPanel.this.gotoObject();
            }
        });

        btnSelectInExplorer = extendableTextField.addButton();
        btnSelectInExplorer.setToolTipText(NbBundle.getMessage(ExtendsPanel.class, "SELECT_IN_TREE"));
        btnSelectInExplorer.setIcon(RadixWareIcons.TREE.SELECT_IN_TREE.getIcon());
        btnSelectInExplorer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                /*final AdsPhraseBookDef superPhraseBook =findSuperPhraseBook();
                 assert (superPhraseBook != null);
                 NodesManager.selectInProjects(superPhraseBook);*/
            }
        });

        btnGoToExtends.setEnabled(false);
        btnSelectInExplorer.setEnabled(false);
    }

    private void gotoObject() {
        /*final AdsPhraseBookDef superPhraseBook = findSuperPhraseBook();
         EditorsManager.getDefault().open(superPhraseBook);*/
    }

    private void resetExtends() {
        //phraseBook.setSuperPhraseBook(null);
        extendableTextField.setValue("");
        btnGoToExtends.setEnabled(false);
        btnSelectInExplorer.setEnabled(false);
    }

    private void setExtends() {
        final AdsPhraseBookDef superPhraseBook = getSuperPhraseBook();
        if (superPhraseBook == null) {
            return;
        }

       // if (superPhraseBook != null) {
            extendableTextField.setValue(superPhraseBook.getQualifiedName(phraseBook));
            btnSelectInExplorer.setEnabled(true);
            btnGoToExtends.setEnabled(true);
        //} else {
       //     btnSelectInExplorer.setEnabled(false);
       //     btnGoToExtends.setEnabled(false);
       // }
    }

    private AdsPhraseBookDef getSuperPhraseBook() {
        final BranchesVisitor branchesVisitor = new BranchesVisitor();
        final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(branchesVisitor, new GoToDefinitionProvider());
        return (AdsPhraseBookDef) ChooseDefinition.chooseDefinition(cfg);
    }

    /*public void open(AdsPhraseBookDef phraseBook) {
     this.phraseBook = phraseBook;
     update();
     }*/
    public void update() {
        if (phraseBook != null) {
            this.isReadonly = phraseBook.isReadOnly();
            extendableTextField.setReadOnly(isReadonly);

            /*final boolean hasPhraseBook = phraseBook.getSuperPhraseBook() != null;

             Id extendsId=phraseBook.getSuperPhraseBook();
             if(extendsId!=null){
             extendableTextField.setValue(findSuperPhraseBook().getQualifiedName(findSuperPhraseBook()));
             }

             btnGoToExtends.setEnabled(hasPhraseBook);
             btnSelectInExplorer.setEnabled(hasPhraseBook);

             btnRemoveExtends.setEnabled(!isReadonly && hasPhraseBook);
             btnSetExtends.setEnabled(!isReadonly);*/
        }
    }

    public void setReadOnly(final boolean readonly) {
        this.isReadonly = readonly;
        extendableTextField.setReadOnly(readonly);
        //update();
    }

    private class BranchesVisitor extends RadixObject {

        @Override
        public void visitChildren(final IVisitor visitor, final VisitorProvider provider) {
            /*Collection<Branch> branches = RadixFileUtil.getOpenedBranches();
             for (Branch branch : branches) {
             branch.visit(visitor, provider);
             }*/
            final Branch branch = phraseBook.getModule().getSegment().getLayer().getBranch();
            branch.visit(visitor, provider);
            super.visitChildren(visitor, provider);
        }
    }

    private class GoToDefinitionProvider extends VisitorProvider {

        @Override
        public boolean isTarget(final RadixObject radixObject) {
            return ((radixObject instanceof AdsPhraseBookDef) && (!radixObject.equals(phraseBook)));
        }
    }

    /* private AdsPhraseBookDef findSuperPhraseBook(){
     Branch branch=phraseBook.getModule().getSegment().getLayer().getBranch();
     return (AdsPhraseBookDef)branch.find(new PhraseBookProvider(phraseBook.getSuperPhraseBook()));
     }*/
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        extendableTextField = new org.radixware.kernel.common.components.ExtendableTextField();

        jLabel1.setText(org.openide.util.NbBundle.getMessage(ExtendsPanel.class, "ExtendsPanel.jLabel1.text_1")); // NOI18N

        extendableTextField.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(extendableTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(extendableTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel1))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.common.components.ExtendableTextField extendableTextField;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
