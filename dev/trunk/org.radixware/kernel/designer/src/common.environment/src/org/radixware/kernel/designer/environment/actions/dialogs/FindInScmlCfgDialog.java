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

package org.radixware.kernel.designer.environment.actions.dialogs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.designer.common.dialogs.scmlnb.finder.IFindInSourcesCfg;
import org.radixware.kernel.designer.common.dialogs.scmlnb.finder.IFinder;
import org.radixware.kernel.designer.common.dialogs.scmlnb.finder.IOccurrence;
import org.radixware.kernel.designer.common.dialogs.scmlnb.finder.ScmlFinderFactory;
import org.radixware.kernel.designer.common.dialogs.scmlnb.finder.TagTextFactory;
import org.radixware.kernel.designer.common.dialogs.utils.IAcceptor;

import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.editors.DefaultTagTextFactory;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;


public class FindInScmlCfgDialog extends javax.swing.JPanel {

    private static ConfigData previousConfigData;
    private final TagTextFactory tagTextFactory = new DefaultTagTextFactory();

    /**
     * Creates new form TextInScmlCfgDialog
     */
    public FindInScmlCfgDialog() {
        initComponents();

        rbSelection.setSelected(true);
        cbSearchInTitles.setSelected(true);
        cbSearchInXml.setSelected(true);

        cbRegularExpression.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent e) {
                cbWholeWords.setEnabled(!cbRegularExpression.isSelected());
            }
        });

        restoreConfigData();

        if (rbSelection.isSelected() && getSearchRoots().isEmpty()) {
            rbOpenBranches.setSelected(true);
        } else {
            final StringBuilder sb = new StringBuilder();
            for (RadixObject root : getSearchRoots()) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(root.getName());
            }
            rbSelection.setText(rbSelection.getText() + " (" + sb.toString() + ")");
        }
    }

    public IFindInSourcesCfg getCfg() {
        saveConfigData();
        final String serachStrng = tfFindString.getText();
        final boolean matchCase = cbMatchCase.isSelected();
        final boolean wholeWords = cbWholeWords.isSelected() && !cbRegularExpression.isSelected();
        final boolean trueRegex = cbRegularExpression.isSelected();
        final List<IFinder> activeFinders = new ArrayList<>();
        activeFinders.add(ScmlFinderFactory.createRegexSubstringFinder(serachStrng, tagTextFactory, matchCase, wholeWords, trueRegex));
        if (cbSearchInTitles.isSelected()) {
            activeFinders.add(ScmlFinderFactory.createTitleFinder(serachStrng, matchCase, wholeWords, trueRegex));
        }
        activeFinders.add(ScmlFinderFactory.createEventCodePropFinderfinal(serachStrng, matchCase, wholeWords, trueRegex));
        if(cbSearchInXml.isSelected()) {
            activeFinders.add(ScmlFinderFactory.createXmlSchemeFinder(serachStrng, matchCase, wholeWords, trueRegex));
        }
        final List<RadixObject> roots = getSearchRoots();
        return new DefaultCfg(roots, activeFinders);
    }

    private List<RadixObject> getSearchRoots() {
        final List<RadixObject> roots = new LinkedList<RadixObject>();
        if (rbSelection.isSelected()) {
            for (Node node : WindowManager.getDefault().getRegistry().getActivatedNodes()) {
                final RadixObject root = node.getLookup().lookup(RadixObject.class);
                if (root != null) {
                    roots.add(root);
                }
            }
        } else if (rbOpenBranches.isSelected()) {
            for (Branch branch : RadixFileUtil.getOpenedBranches()) {
                roots.add(branch);
            }
        }
        return roots;
    }

    private void saveConfigData() {
        previousConfigData = new ConfigData(
                cbMatchCase.isSelected(),
                cbWholeWords.isSelected(),
                cbRegularExpression.isSelected(),
                tfFindString.getText(),
                rbSelection.isSelected(),
                cbSearchInTitles.isSelected(),
                cbSearchInXml.isSelected());
    }

    private void restoreConfigData() {
        if (previousConfigData == null) {
            return;
        }
        cbMatchCase.setSelected(previousConfigData.matchCase);
        cbWholeWords.setSelected(previousConfigData.wholeWords);
        cbRegularExpression.setSelected(previousConfigData.trueRegex);
        cbSearchInTitles.setSelected(previousConfigData.searchInTitles);
        cbSearchInXml.setSelected(previousConfigData.searchInXml);
        tfFindString.setText(previousConfigData.pattern);
        rbSelection.setSelected(previousConfigData.inSelection);
        tfFindString.selectAll();
    }

    private static class ConfigData {

        private final boolean matchCase;
        private final boolean wholeWords;
        private final boolean trueRegex;
        private final String pattern;
        private final boolean inSelection;
        private final boolean searchInTitles;
        private final boolean searchInXml;

        public ConfigData(boolean matchCase, boolean wholeWords, boolean trueRegex, String pattern, boolean selection, boolean searchInTitles, boolean searchInXml) {
            this.matchCase = matchCase;
            this.wholeWords = wholeWords;
            this.trueRegex = trueRegex;
            this.pattern = pattern;
            this.inSelection = selection;
            this.searchInTitles = searchInTitles;
            this.searchInXml = searchInXml;
        }

    }

    public static IFindInSourcesCfg createCfg() {
        final FindInScmlCfgDialog dialog = new FindInScmlCfgDialog();
        final ModalDisplayer md = new ModalDisplayer(dialog, NbBundle.getMessage(FindInScmlCfgDialog.class, "CTL_FindInScmlCfgDialog"));
        dialog.tfFindString.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateOkState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateOkState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateOkState();
            }

            private void updateOkState() {
                md.getDialogDescriptor().setValid(dialog.tfFindString.getText().length() > 0);
            }
        });
        md.getDialogDescriptor().setValid(dialog.tfFindString.getText().length() > 0);
        if (md.showModal()) {
            return dialog.getCfg();
        }
        return null;
    }

    @Override
    public void requestFocus() {
        tfFindString.requestFocusInWindow();
    }

    private static class DefaultCfg implements IFindInSourcesCfg {

        private final List<RadixObject> roots;
        private final IFinder proxyFinder;

        public DefaultCfg(final List<RadixObject> roots, final List<IFinder> finders) {
            this.roots = roots;
            proxyFinder = new IFinder() {
                @Override
                public List<IOccurrence> list(RadixObject radixObject) {
                    final List<IOccurrence> result = new ArrayList<>();
                    for (IFinder finder : finders) {
                        if (finder.accept(radixObject)) {
                            List<IOccurrence> finderResult = finder.list(radixObject);
                            if (finderResult != null) {
                                result.addAll(finderResult);
                            }
                        }
                    }
                    return result;
                }

                @Override
                public boolean accept(RadixObject candidate) {
                    for (IFinder finder : finders) {
                        if (finder.accept(candidate)) {
                            return true;
                        }
                    }
                    return false;
                }
            };
        }

        @Override
        public IFinder getFinder(RadixObject radixObject) {
            return proxyFinder;
        }

        @Override
        public List<RadixObject> getRoots() {
            return roots;
        }

        @Override
        public IAcceptor<RadixObject> getAcceptor() {
            return proxyFinder;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgScope = new javax.swing.ButtonGroup();
        lbFindString = new javax.swing.JLabel();
        tfFindString = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        cbWholeWords = new javax.swing.JCheckBox();
        cbMatchCase = new javax.swing.JCheckBox();
        cbRegularExpression = new javax.swing.JCheckBox();
        cbSearchInTitles = new javax.swing.JCheckBox();
        cbSearchInXml = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        rbOpenBranches = new javax.swing.JRadioButton();
        rbSelection = new javax.swing.JRadioButton();

        setMinimumSize(new java.awt.Dimension(454, 225));

        lbFindString.setText(org.openide.util.NbBundle.getMessage(FindInScmlCfgDialog.class, "FindInScmlCfgDialog.lbFindString.text")); // NOI18N

        tfFindString.setText(org.openide.util.NbBundle.getMessage(FindInScmlCfgDialog.class, "FindInScmlCfgDialog.tfFindString.text")); // NOI18N

        jLabel2.setForeground(new java.awt.Color(142, 142, 142));
        jLabel2.setText(org.openide.util.NbBundle.getMessage(FindInScmlCfgDialog.class, "FindInScmlCfgDialog.jLabel2.text")); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(FindInScmlCfgDialog.class, "FindInScmlCfgDialog.jPanel1.border.title"))); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(200, 120));

        cbWholeWords.setMnemonic('W');
        cbWholeWords.setText(org.openide.util.NbBundle.getMessage(FindInScmlCfgDialog.class, "FindInScmlCfgDialog.cbWholeWords.text")); // NOI18N

        cbMatchCase.setMnemonic('M');
        cbMatchCase.setText(org.openide.util.NbBundle.getMessage(FindInScmlCfgDialog.class, "FindInScmlCfgDialog.cbMatchCase.text")); // NOI18N

        cbRegularExpression.setMnemonic('E');
        cbRegularExpression.setText(org.openide.util.NbBundle.getMessage(FindInScmlCfgDialog.class, "FindInScmlCfgDialog.cbRegularExpression.text")); // NOI18N

        cbSearchInTitles.setText(org.openide.util.NbBundle.getMessage(FindInScmlCfgDialog.class, "FindInScmlCfgDialog.cbSearchInTitles.text")); // NOI18N

        cbSearchInXml.setText(org.openide.util.NbBundle.getMessage(FindInScmlCfgDialog.class, "FindInScmlCfgDialog.cbSearchInXml.text")); // NOI18N
        cbSearchInXml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSearchInXmlActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbWholeWords)
                    .addComponent(cbMatchCase)
                    .addComponent(cbRegularExpression)
                    .addComponent(cbSearchInTitles)
                    .addComponent(cbSearchInXml))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(cbWholeWords)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbMatchCase)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbRegularExpression)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbSearchInTitles)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbSearchInXml)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(FindInScmlCfgDialog.class, "FindInScmlCfgDialog.jPanel2.border.title"))); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(200, 120));

        bgScope.add(rbOpenBranches);
        rbOpenBranches.setMnemonic('H');
        rbOpenBranches.setText(org.openide.util.NbBundle.getMessage(FindInScmlCfgDialog.class, "FindInScmlCfgDialog.rbOpenBranches.text")); // NOI18N

        bgScope.add(rbSelection);
        rbSelection.setMnemonic('S');
        rbSelection.setText(org.openide.util.NbBundle.getMessage(FindInScmlCfgDialog.class, "FindInScmlCfgDialog.rbSelection.text")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbOpenBranches)
                    .addComponent(rbSelection))
                .addContainerGap(121, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(rbOpenBranches)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbSelection)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbFindString)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(tfFindString, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbFindString)
                    .addComponent(tfFindString, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbSearchInXmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSearchInXmlActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSearchInXmlActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgScope;
    private javax.swing.JCheckBox cbMatchCase;
    private javax.swing.JCheckBox cbRegularExpression;
    private javax.swing.JCheckBox cbSearchInTitles;
    private javax.swing.JCheckBox cbSearchInXml;
    private javax.swing.JCheckBox cbWholeWords;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lbFindString;
    private javax.swing.JRadioButton rbOpenBranches;
    private javax.swing.JRadioButton rbSelection;
    private javax.swing.JTextField tfFindString;
    // End of variables declaration//GEN-END:variables
}
