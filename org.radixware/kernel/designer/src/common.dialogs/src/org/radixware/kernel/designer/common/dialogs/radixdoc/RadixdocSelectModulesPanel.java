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
package org.radixware.kernel.designer.common.dialogs.radixdoc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.tree.TreePath;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Pair;

public final class RadixdocSelectModulesPanel extends javax.swing.JPanel implements java.beans.Customizer {

    private Object bean;
    private final RadixObject root;
    private final List<Pair<String, Id>> exludedModules;
    private final List<String> includedLayers;
    private final boolean needUds;
    private final boolean needLocalizingLayers;
            
    private JButton okButtonFromDialog;    

    public RadixdocSelectModulesPanel(RadixObject root) {
        this(root, null, null, false, false);        
    }

    public RadixdocSelectModulesPanel(RadixObject root, List<Pair<String, Id>> exludedModules, List<String> includedLayers, boolean needUds) {
        this(root, exludedModules, includedLayers, needUds, true);
    }
    
    public RadixdocSelectModulesPanel(RadixObject root, List<Pair<String, Id>> exludedModules, List<String> includedLayers, boolean needUds, boolean needLocalizingLayers) {
        this.root = root;
        this.exludedModules = exludedModules;
        this.includedLayers = includedLayers;
        this.needUds = needUds;
        this.needLocalizingLayers = needLocalizingLayers;
        initComponents();
    }

    public void setOkButtonFromDialog(JButton okButtonFromDialog) {
        this.okButtonFromDialog = okButtonFromDialog;
        this.okButtonFromDialog.setEnabled(false);
    }

    public void updateDialogButtonsState() {
        if (okButtonFromDialog != null) {
            okButtonFromDialog.setEnabled(modulesTree.getSelectionPaths() != null);
        }
    }

    private List<? extends Module> getAllModulesFromSegment(Segment<? extends Module> segment) {
        final List<Module> modules = new LinkedList<>();
        loop:
        for (Module module : segment.getModules()) {
            if (module != null) {
                if (exludedModules != null) {
                    for (Pair<String, Id> entry : exludedModules) {
                        if (module.getLayer().getURI().equals(entry.getFirst()) && entry.getSecond() == module.getId()) {
                            continue loop;
                        }
                    }
                }
                modules.add(module);
            }
        }
        return modules;
    }

    public List<Module> getSelectedModules() {
        List<Module> selectedModules = new ArrayList();
        for (TreePath path : modulesTree.getSelectionPaths()) {
            if (path.getLastPathComponent() instanceof Module) {
                selectedModules.add((Module) path.getLastPathComponent());
            } else if (path.getLastPathComponent() instanceof Segment) {
                Segment selectedSegment = (Segment) path.getLastPathComponent();
                selectedModules.addAll(getAllModulesFromSegment(selectedSegment));
            } else if (path.getLastPathComponent() instanceof Layer) {
                Layer selectedLayer = (Layer) path.getLastPathComponent();
                selectedModules.addAll(getAllModulesFromSegment(selectedLayer.getDds()));
                selectedModules.addAll(getAllModulesFromSegment(selectedLayer.getAds()));
            } else if (path.getLastPathComponent() instanceof Branch) {
                Branch selectedBranch = (Branch) path.getLastPathComponent();                
                for (Layer l : selectedBranch.getLayers()) {
                    if (includedLayers != null && !includedLayers.contains(l.getURI())) {
                        continue;
                    }
                    if (l.isLocalizing() && !needLocalizingLayers) {
                        continue;
                    }
                    selectedModules.addAll(getAllModulesFromSegment(l.getDds()));
                    selectedModules.addAll(getAllModulesFromSegment(l.getAds()));
                }
            }
        }
        return selectedModules;
    }

    @Override
    public void setObject(Object bean) {
        this.bean = bean;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        modulesScrollPane = new javax.swing.JScrollPane();
        modulesTree = new javax.swing.JTree();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setAutoscrolls(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.W_RESIZE_CURSOR));
        setLayout(new java.awt.GridBagLayout());

        modulesScrollPane.setViewportView(modulesTree);
        modulesScrollPane.setBorder(null);

        modulesTree.setBackground(new java.awt.Color(240, 240, 240));
        RadixdocTreeModulesModel treeModel = new RadixdocTreeModulesModel(root, exludedModules, includedLayers, needUds, needLocalizingLayers);
        modulesTree.setModel(treeModel);
        RadixdocCheckTreeSelectionModel selectionModel = new RadixdocCheckTreeSelectionModel(treeModel);
        modulesTree.setSelectionModel(selectionModel);
        RadixdocCheckTreeManager checkTreeManager = new RadixdocCheckTreeManager(modulesTree,selectionModel, this);
        modulesTree.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        modulesTree.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        modulesTree.setExpandsSelectedPaths(false);
        modulesTree.setFocusable(false);
        modulesTree.setLargeModel(false);
        modulesTree.setMaximumSize(new java.awt.Dimension(800, 600));
        modulesTree.setNextFocusableComponent(modulesScrollPane);
        modulesTree.setScrollsOnExpand(false);
        modulesTree.setToggleClickCount(1);
        modulesTree.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                modulesTreeAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        modulesScrollPane.setViewportView(modulesTree);
        setPreferredSize(null);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(modulesScrollPane, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void modulesTreeAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_modulesTreeAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_modulesTreeAncestorAdded

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane modulesScrollPane;
    private javax.swing.JTree modulesTree;
    // End of variables declaration//GEN-END:variables
}