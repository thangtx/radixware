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

package org.radixware.kernel.designer.common.dialogs.results;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JToolBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import org.openide.actions.CopyAction;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class ResultsToolbar extends javax.swing.JPanel implements IResultsFilter {

    private static final String SEARCH = "Search...";
    private final ResultsTree tree;
    private TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            updateButtonsState();
        }
    };
    private static final Lookup.Template<RadixObject> lookupTemplate = new Lookup.Template<RadixObject>(RadixObject.class);
    private Lookup.Result<RadixObject> curLookupResult = null;
    private final LookupListener lookupResultListener = new LookupListener() {

        @Override
        public void resultChanged(LookupEvent ev) {
            onSelectedObjectsChanged();
        }
    };
    private Collection<? extends RadixObject> selectedRadixObjects = null;

    protected void fireFilterChanged() {
        tree.onFilterChanged();
    }

    private void onSelectedObjectsChanged() {
        if (curLookupResult == null) { // component deactivated
            return;
        }

        Collection<? extends RadixObject> tmp = curLookupResult.allInstances();
        if (tmp.isEmpty()) {
            return;
        }
        selectedRadixObjects = tmp;
//        if (selectedRadixObjects.isEmpty()) {
//            return;
//        }

        fireFilterChanged();
    }

    private void goToObjectButtonActionPerformed() {
        tree.goToLastSelectedObject();
    }

    private void copyButtonActionPerformed() {
        tree.copyToClipboard();
    }

    private void filterBySelectedObjectsButtonActionPerformed() {
//        if (megaFeatureSelectButton.isSelected()) {
//            curLookupResult.addLookupListener(lookupResultListener);
//            onSelectedObjectsChanged();
//        } else {
//            curLookupResult.removeLookupListener(lookupResultListener);
//            selectedRadixObjects = null;
//        }
        fireFilterChanged();
    }

    private void filterBySelectedModulesButtonActionPerformed() {
        fireFilterChanged();
    }

//    private void selectDDSSegmentActionPerformed() {
//        changeSupport.fireEvent(new RadixEvent());
//    }
//
//    private void selectADSSegmentActionPerformed() {
//        changeSupport.fireEvent(new RadixEvent());
//    }
    private void collapseButtonActionPerformed() {
        for (ResultsTree.Item item : tree.getRoot().getChildren()) {
            item.setExpanded(false);
        }
    }

    private void clearButtonActionPerformed() {
        tree.clear();
    }

    private void searchTextChanged() {
        fireFilterChanged();
    }

    public ResultsToolbar() {
        tree = null;
        initComponents();
    }

    /** Creates new form ResultsToolbar */
    public ResultsToolbar(ResultsTree tree) {
        this.tree = tree;
        initComponents();

        searchTextField.setText(SEARCH);

        updateButtonsState();
        tree.addTreeSelectionListener(treeSelectionListener);

        curLookupResult = Utilities.actionsGlobalContext().lookup(lookupTemplate);
        curLookupResult.addLookupListener(lookupResultListener);
    }

    @Override
    public String getText() {
        if (searchTextField.getText().equals(SEARCH)) {
            return "";
        }
        return searchTextField.getText();
    }

    @Override
    public Collection<? extends RadixObject> getRootObjects() {
        if (selectedRadixObjects == null) {
            return null;
        } else if (filterBySelectedModulesButton.isSelected()) {
            final Set<RadixObject> result = new HashSet<RadixObject>();
            for (RadixObject radixObject : selectedRadixObjects) {
                final Module module = radixObject.getModule();
                result.add(module != null ? module : radixObject); // branch, layer, segment
            }
            return result;
        } else if (filterBySelectedObjectsButton.isSelected()) {
            return selectedRadixObjects;
        } else {
            return null;
        }
    }

    private void updateButtonsState() {
        final boolean enabled = tree.getLastSelectedUserObject() != null;
        openButton.setEnabled(enabled);
        copyButton.setEnabled(enabled);
    }

    protected JToolBar getAdditionalToolBar() {
        return additionalToolBar;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        baseToolBar = new javax.swing.JToolBar(){
            public Dimension getMaximumSize(){
                Dimension dim = super.getMaximumSize();
                dim.height = 24;
                return dim;
            }

            public Dimension getMinimumSize(){
                Dimension dim = super.getMinimumSize();
                dim.height = 24;
                return dim;
            }

            public Dimension getPreferredSize(){
                Dimension dim = super.getPreferredSize();
                dim.height = 24;
                return dim;
            }
        };
        openButton = new javax.swing.JButton(new AbstractAction("Open", RadixWareIcons.ARROW.GO_TO_OBJECT.getIcon()){

            public void actionPerformed(ActionEvent evt){
                goToObjectButtonActionPerformed();
            }
        })
        ;
        copyButton = new javax.swing.JButton(new AbstractAction("Copy", SystemAction.get(CopyAction.class).getIcon()){

            public void actionPerformed(ActionEvent evt){
                copyButtonActionPerformed();
            }
        })
        ;
        jSeparator1 = new javax.swing.JToolBar.Separator();
        filterBySelectedObjectsButton = new javax.swing.JToggleButton(new AbstractAction("Filter by objects", RadixWareIcons.CHECK.FILTER_BY_OBJECT.getIcon())
            {
                public void actionPerformed(ActionEvent evt){
                    filterBySelectedObjectsButtonActionPerformed();
                }
            }
        );
        filterBySelectedModulesButton = new javax.swing.JToggleButton(new AbstractAction("Filter by modules", RadixWareIcons.CHECK.FILTER_BY_MODULE.getIcon())
            {
                public void actionPerformed(ActionEvent evt){
                    filterBySelectedModulesButtonActionPerformed();
                }
            }
        );
        jSeparator2 = new javax.swing.JToolBar.Separator();
        additionalToolBar = new javax.swing.JToolBar(){
            public Dimension getMaximumSize(){
                Dimension dim = super.getMaximumSize();
                dim.height = 24;
                return dim;
            }

            public Dimension getMinimumSize(){
                Dimension dim = super.getMinimumSize();
                dim.height = 24;
                return dim;
            }

            public Dimension getPreferredSize(){
                Dimension dim = super.getPreferredSize();
                dim.height = 24;
                return dim;
            }
        };
        jToolBar1 = new javax.swing.JToolBar();
        collapseButton = new javax.swing.JButton(new AbstractAction("Collapse",  RadixWareIcons.TREE.COLLAPSE.getIcon())
            {
                public void actionPerformed(ActionEvent evt){
                    collapseButtonActionPerformed();
                }
            });
            clearButton = new javax.swing.JButton(new AbstractAction("Clear",  RadixWareIcons.DELETE.CLEAR.getIcon())
                {
                    public void actionPerformed(ActionEvent evt){
                        clearButtonActionPerformed();
                    }
                });
                jPanel2 = new javax.swing.JPanel();
                searchTextField = new javax.swing.JTextField();

                jPanel1.setMaximumSize(new java.awt.Dimension(32767, 24));
                jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));

                baseToolBar.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
                baseToolBar.setFloatable(false);
                baseToolBar.setAlignmentY(1.0F);
                baseToolBar.setMaximumSize(new java.awt.Dimension(32767, 24));
                baseToolBar.setMinimumSize(new java.awt.Dimension(0, 24));

                openButton.setText(org.openide.util.NbBundle.getMessage(ResultsToolbar.class, "ResultsToolbar.openButton.text")); // NOI18N
                openButton.setToolTipText(org.openide.util.NbBundle.getMessage(ResultsToolbar.class, "ResultsToolbar.openButton.toolTipText")); // NOI18N
                openButton.setAlignmentY(1.0F);
                openButton.setFocusable(false);
                openButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
                openButton.setMaximumSize(new java.awt.Dimension(24, 24));
                openButton.setMinimumSize(new java.awt.Dimension(24, 24));
                openButton.setPreferredSize(new java.awt.Dimension(24, 24));
                openButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                baseToolBar.add(openButton);

                copyButton.setText(org.openide.util.NbBundle.getMessage(ResultsToolbar.class, "ResultsToolbar.copyButton.text")); // NOI18N
                copyButton.setToolTipText(org.openide.util.NbBundle.getMessage(ResultsToolbar.class, "ResultsToolbar.copyButton.toolTipText")); // NOI18N
                copyButton.setAlignmentY(1.0F);
                copyButton.setFocusable(false);
                copyButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                copyButton.setMaximumSize(new java.awt.Dimension(24, 24));
                copyButton.setMinimumSize(new java.awt.Dimension(24, 24));
                copyButton.setPreferredSize(new java.awt.Dimension(24, 24));
                copyButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                baseToolBar.add(copyButton);

                jSeparator1.setAlignmentX(0.0F);
                jSeparator1.setAlignmentY(1.0F);
                jSeparator1.setSeparatorSize(new java.awt.Dimension(5, 24));
                baseToolBar.add(jSeparator1);

                filterBySelectedObjectsButton.setText(org.openide.util.NbBundle.getMessage(ResultsToolbar.class, "ResultsToolbar.filterBySelectedObjectsButton.text")); // NOI18N
                filterBySelectedObjectsButton.setToolTipText(org.openide.util.NbBundle.getMessage(ResultsToolbar.class, "ResultsToolbar.filterBySelectedObjectsButton.toolTipText")); // NOI18N
                filterBySelectedObjectsButton.setAlignmentY(1.0F);
                filterBySelectedObjectsButton.setFocusable(false);
                filterBySelectedObjectsButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
                filterBySelectedObjectsButton.setMaximumSize(new java.awt.Dimension(24, 24));
                filterBySelectedObjectsButton.setMinimumSize(new java.awt.Dimension(24, 24));
                filterBySelectedObjectsButton.setPreferredSize(new java.awt.Dimension(24, 24));
                filterBySelectedObjectsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                baseToolBar.add(filterBySelectedObjectsButton);

                filterBySelectedModulesButton.setText(org.openide.util.NbBundle.getMessage(ResultsToolbar.class, "ResultsToolbar.filterBySelectedModulesButton.text")); // NOI18N
                filterBySelectedModulesButton.setToolTipText(org.openide.util.NbBundle.getMessage(ResultsToolbar.class, "ResultsToolbar.filterBySelectedModulesButton.toolTipText")); // NOI18N
                filterBySelectedModulesButton.setAlignmentY(1.0F);
                filterBySelectedModulesButton.setFocusable(false);
                filterBySelectedModulesButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                filterBySelectedModulesButton.setMaximumSize(new java.awt.Dimension(24, 24));
                filterBySelectedModulesButton.setMinimumSize(new java.awt.Dimension(24, 24));
                filterBySelectedModulesButton.setPreferredSize(new java.awt.Dimension(24, 24));
                filterBySelectedModulesButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                baseToolBar.add(filterBySelectedModulesButton);

                jSeparator2.setAlignmentX(0.0F);
                jSeparator2.setAlignmentY(1.0F);
                jSeparator2.setSeparatorSize(new java.awt.Dimension(3, 24));
                baseToolBar.add(jSeparator2);

                jPanel1.add(baseToolBar);

                additionalToolBar.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
                additionalToolBar.setFloatable(false);
                additionalToolBar.setAlignmentY(1.0F);
                additionalToolBar.setMaximumSize(new java.awt.Dimension(32767, 24));
                additionalToolBar.setMinimumSize(new java.awt.Dimension(0, 24));
                additionalToolBar.setPreferredSize(null);
                jPanel1.add(additionalToolBar);

                jToolBar1.setBorder(null);
                jToolBar1.setFloatable(false);
                jToolBar1.setMaximumSize(new java.awt.Dimension(48, 24));
                jToolBar1.setMinimumSize(new java.awt.Dimension(48, 24));
                jToolBar1.setOpaque(false);

                collapseButton.setText(org.openide.util.NbBundle.getMessage(ResultsToolbar.class, "ResultsToolbar.collapseButton.text")); // NOI18N
                collapseButton.setToolTipText(org.openide.util.NbBundle.getMessage(ResultsToolbar.class, "ResultsToolbar.collapseButton.toolTipText")); // NOI18N
                collapseButton.setAlignmentX(1.0F);
                collapseButton.setAlignmentY(1.0F);
                collapseButton.setFocusable(false);
                collapseButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                collapseButton.setMaximumSize(new java.awt.Dimension(24, 24));
                collapseButton.setMinimumSize(new java.awt.Dimension(24, 24));
                collapseButton.setPreferredSize(new java.awt.Dimension(24, 24));
                collapseButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                jToolBar1.add(collapseButton);

                clearButton.setText(org.openide.util.NbBundle.getMessage(ResultsToolbar.class, "ResultsToolbar.clearButton.text")); // NOI18N
                clearButton.setToolTipText(org.openide.util.NbBundle.getMessage(ResultsToolbar.class, "ResultsToolbar.clearButton.toolTipText")); // NOI18N
                clearButton.setAlignmentX(1.0F);
                clearButton.setAlignmentY(1.0F);
                clearButton.setFocusable(false);
                clearButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                clearButton.setMaximumSize(new java.awt.Dimension(24, 24));
                clearButton.setMinimumSize(new java.awt.Dimension(24, 24));
                clearButton.setPreferredSize(new java.awt.Dimension(24, 24));
                clearButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                jToolBar1.add(clearButton);

                jPanel2.setLayout(new java.awt.GridBagLayout());

                searchTextField.setColumns(10);
                searchTextField.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        searchTextFieldMouseClicked(evt);
                    }
                });
                searchTextField.getDocument().addDocumentListener(new DocumentListener() {

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        searchTextChanged();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        searchTextChanged();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        searchTextChanged();
                    }
                });
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.weightx = 1.0;
                jPanel2.add(searchTextField, gridBagConstraints);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                );
                layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );
            }// </editor-fold>//GEN-END:initComponents

    private void searchTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchTextFieldMouseClicked
        if (searchTextField.getText().equals(SEARCH)) {
            searchTextField.setText("");
        }
}//GEN-LAST:event_searchTextFieldMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar additionalToolBar;
    private javax.swing.JToolBar baseToolBar;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton collapseButton;
    private javax.swing.JButton copyButton;
    private javax.swing.JToggleButton filterBySelectedModulesButton;
    private javax.swing.JToggleButton filterBySelectedObjectsButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton openButton;
    private javax.swing.JTextField searchTextField;
    // End of variables declaration//GEN-END:variables
}
