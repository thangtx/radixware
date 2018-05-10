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
package org.radixware.kernel.designer.common.editors.layer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.doc.AdsDocMapDef;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Branch.Layers;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.common.dialogs.ExtensionsPanel;
import org.radixware.kernel.designer.common.dialogs.LanguagesSelector;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.ExpandableTextFieldPanel;
import org.radixware.kernel.designer.common.dialogs.components.selector.ItemClusterizator;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

class PropertiesPanel extends javax.swing.JPanel {

    private Layer layer = null;
    private List<Layer.Extension> extensions = null;
    private StateManager stateManager = new StateManager(this);

    /**
     * Creates new form PropertiesPanel
     */
    public PropertiesPanel() {
        initComponents();

    }

    private String listBaseLayers() {
        final List<Layer> prevLayers = layer.listBaseLayers();
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Layer l : prevLayers) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(l.getURI());
        }
        return sb.toString();
    }
    RootParagraphNavigator paragraphNavigator;
    RootParagraphNavigator rolesNavigator;

    public void open(Layer l) {
        this.layer = l;
        this.extensions = l.getExtensions();

        final boolean readonly = l.isReadOnly();
        nameValue.setEditable(!readonly);
        titleValue.setEditable(!readonly);
        copyrightValue.setEditable(!readonly);
        selectLanguagesBtn.setEnabled(!readonly);
        selectRootDocMapDefBtn.setEnabled(!readonly);
        comboBoxMainDocLanguag.setEnabled(!readonly);

        paragraphNavigator = new RootParagraphNavigator(layer, true);
        rolesNavigator = new RootParagraphNavigator(layer, false);

        cmdDefaultRestriction.setEnabled(!readonly);
        nameValue.setCurrentName(l.getName());
        titleValue.setText(l.getTitle());   //any string
        ownerBranchValue.setText(l.getBranch().getName());
        copyrightValue.setText(l.getCopyright()); //any string
        releaseValue.setText(l.getReleaseNumber());   //read-only

        prevLayerUriValue.setText(listBaseLayers()); //read-only
        URIValue.setText(l.getURI()); //string, java package's format
        URIValue.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent evt) {
                changeSupport.fireChange();
            }
        });

        nameValue.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                changeSupport.fireChange();
            }
        });

        btChooseBaseLayers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ChooseBaseLayersPanel panel = new ChooseBaseLayersPanel(layer);
                        panel.open(layer.getBranch(), layer);
                        ModalDisplayer displayer = new ModalDisplayer(panel, "Choose Base Layers");
                        if (displayer.showModal()) {
                            layer.clearBaseLayers();
                            for (Layer l : panel.selection()) {
                                layer.addBaseLayer(l);
                            }
                            prevLayerUriValue.setText(listBaseLayers()); //read-only
                        }
                    }
                });

            }
        });
        btChooseBaseLayers.setEnabled((!layer.isReadOnly() || (layer.listBaseLayers().isEmpty() && !"org.radixware".equals(layer.getURI()))) && !layer.isLocalizing());

        ((ExpandableTextFieldPanel) dbNameRestrictionPanel).setText(l.getDbObjectNamesRestriction());
        ((ExpandableTextFieldPanel) dbNameRestrictionPanel).setReadOnly(readonly);

        txtAccessibleRoots.setText(paragraphNavigator.createModel().toString());
        txtAccessibleRoles.setText(rolesNavigator.createModel().toString());

        refreshLanguagesField();
        refreshExtensionsField();
        refreshMainDocLanguageField();
        refreshRootDocMapDefId();
        isComplete();
    }

    private static boolean URINameIsValid(final String str) {
        return RadixObjectsUtils.isCorrectURL(str);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        selectLanguagesBtn = new javax.swing.JButton();
        lblUri = new javax.swing.JLabel();
        lblBaseLayers = new javax.swing.JLabel();
        prevLayerUriValue = new javax.swing.JTextField();
        lblTitle = new javax.swing.JLabel();
        URIValue = new javax.swing.JTextField();
        lblRelease = new javax.swing.JLabel();
        copyrightValue = new javax.swing.JTextField();
        lblCopyright = new javax.swing.JLabel();
        lblLanguages = new javax.swing.JLabel();
        languagesValue = new javax.swing.JTextField();
        titleValue = new javax.swing.JTextField();
        lblEnclosingBranch = new javax.swing.JLabel();
        releaseValue = new javax.swing.JTextField();
        ownerBranchValue = new javax.swing.JTextField();
        lblName = new javax.swing.JLabel();
        nameValue = new org.radixware.kernel.designer.common.dialogs.components.NameEditPanel();
        lblExtensions = new javax.swing.JLabel();
        selectExtensionsBtn = new javax.swing.JButton();
        extensionsValue = new javax.swing.JTextField();
        btChooseBaseLayers = new javax.swing.JButton();
        lblDbNameRestriction = new javax.swing.JLabel();
        cmdDefaultRestriction = new javax.swing.JButton();
        dbNameRestrictionPanel = new ExpandableTextFieldPanel();
        cmdAccessibleRoots = new javax.swing.JButton();
        txtAccessibleRoots = new javax.swing.JTextField();
        lblAccessibleRoots = new javax.swing.JLabel();
        javax.swing.JLabel spacer = new javax.swing.JLabel();
        lblAccessibleRoles = new javax.swing.JLabel();
        txtAccessibleRoles = new javax.swing.JTextField();
        cmdAccessibleRoles = new javax.swing.JButton();
        lbMainDoclLanguage = new javax.swing.JLabel();
        selectRootDocMapDefBtn = new javax.swing.JButton();
        rootDocMapDefValue = new javax.swing.JTextField();
        lbRootDocMapDef = new javax.swing.JLabel();
        comboBoxMainDocLanguag = new javax.swing.JComboBox();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setLayout(new java.awt.GridBagLayout());

        selectLanguagesBtn.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.selectLanguagesBtn.text")); // NOI18N
        selectLanguagesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLanguagesBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(selectLanguagesBtn, gridBagConstraints);

        lblUri.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.lblUri.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(lblUri, gridBagConstraints);

        lblBaseLayers.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.lblBaseLayers.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(lblBaseLayers, gridBagConstraints);

        prevLayerUriValue.setEditable(false);
        prevLayerUriValue.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.prevLayerUriValue.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        add(prevLayerUriValue, gridBagConstraints);

        lblTitle.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.lblTitle.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(lblTitle, gridBagConstraints);

        URIValue.setEditable(false);
        URIValue.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.URIValue.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        add(URIValue, gridBagConstraints);

        lblRelease.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.lblRelease.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(lblRelease, gridBagConstraints);

        copyrightValue.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.copyrightValue.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        add(copyrightValue, gridBagConstraints);

        lblCopyright.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.lblCopyright.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(lblCopyright, gridBagConstraints);

        lblLanguages.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.lblLanguages.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(lblLanguages, gridBagConstraints);

        languagesValue.setEditable(false);
        languagesValue.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.languagesValue.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        add(languagesValue, gridBagConstraints);

        titleValue.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.titleValue.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        add(titleValue, gridBagConstraints);

        lblEnclosingBranch.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.lblEnclosingBranch.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(lblEnclosingBranch, gridBagConstraints);

        releaseValue.setEditable(false);
        releaseValue.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.releaseValue.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        add(releaseValue, gridBagConstraints);

        ownerBranchValue.setEditable(false);
        ownerBranchValue.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.ownerBranchValue.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        add(ownerBranchValue, gridBagConstraints);

        lblName.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.lblName.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(lblName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        add(nameValue, gridBagConstraints);

        lblExtensions.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.lblExtensions.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(lblExtensions, gridBagConstraints);

        selectExtensionsBtn.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.selectExtensionsBtn.text")); // NOI18N
        selectExtensionsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectExtensionsBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(selectExtensionsBtn, gridBagConstraints);

        extensionsValue.setEditable(false);
        extensionsValue.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.extensionsValue.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        add(extensionsValue, gridBagConstraints);

        btChooseBaseLayers.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.btChooseBaseLayers.text")); // NOI18N
        btChooseBaseLayers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btChooseBaseLayersActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(btChooseBaseLayers, gridBagConstraints);

        lblDbNameRestriction.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.lblDbNameRestriction.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(lblDbNameRestriction, gridBagConstraints);

        cmdDefaultRestriction.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.cmdDefaultRestriction.text")); // NOI18N
        cmdDefaultRestriction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDefaultRestrictionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(cmdDefaultRestriction, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        add(dbNameRestrictionPanel, gridBagConstraints);

        cmdAccessibleRoots.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.cmdAccessibleRoots.text")); // NOI18N
        cmdAccessibleRoots.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAccessibleRootsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(cmdAccessibleRoots, gridBagConstraints);

        txtAccessibleRoots.setEditable(false);
        txtAccessibleRoots.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.txtAccessibleRoots.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        add(txtAccessibleRoots, gridBagConstraints);

        lblAccessibleRoots.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.lblAccessibleRoots.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(lblAccessibleRoots, gridBagConstraints);

        spacer.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.spacer.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        add(spacer, gridBagConstraints);

        lblAccessibleRoles.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.lblAccessibleRoles.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(lblAccessibleRoles, gridBagConstraints);

        txtAccessibleRoles.setEditable(false);
        txtAccessibleRoles.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.txtAccessibleRoles.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        add(txtAccessibleRoles, gridBagConstraints);

        cmdAccessibleRoles.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.cmdAccessibleRoles.text")); // NOI18N
        cmdAccessibleRoles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAccessibleRolesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(cmdAccessibleRoles, gridBagConstraints);

        lbMainDoclLanguage.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.lbMainDoclLanguage.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(lbMainDoclLanguage, gridBagConstraints);

        selectRootDocMapDefBtn.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.selectRootDocMapDefBtn.text")); // NOI18N
        selectRootDocMapDefBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectRootDocMapDefBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(selectRootDocMapDefBtn, gridBagConstraints);

        rootDocMapDefValue.setEditable(false);
        rootDocMapDefValue.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.rootDocMapDefValue.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        add(rootDocMapDefValue, gridBagConstraints);

        lbRootDocMapDef.setText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.lbRootDocMapDef.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(lbRootDocMapDef, gridBagConstraints);

        comboBoxMainDocLanguag.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxMainDocLanguag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxMainDocLanguagActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        add(comboBoxMainDocLanguag, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

private void selectLanguagesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectLanguagesBtnActionPerformed
    List<EIsoLanguage> languages = new ArrayList(layer.getLanguages());
    if (LanguagesSelector.configureLanguages(languages)) {
        layer.setLanguages(languages);
    }
    refreshLanguagesField();
    refreshMainDocLanguageField();
}//GEN-LAST:event_selectLanguagesBtnActionPerformed

private void selectExtensionsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectExtensionsBtnActionPerformed
    List<Layer.Extension> list = new ArrayList<>(extensions.size());
    for (Layer.Extension e : extensions) {
        list.add(layer.new Extension(e.getTitle(), e.getFile(), e.getInstaller()));
    }
    if (ExtensionsPanel.show(layer, list)) {
        extensions = list;
    }
    refreshExtensionsField();
    // TODO add your handling code here:
}//GEN-LAST:event_selectExtensionsBtnActionPerformed

    private void btChooseBaseLayersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btChooseBaseLayersActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btChooseBaseLayersActionPerformed

    private void cmdDefaultRestrictionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDefaultRestrictionActionPerformed
        ((ExpandableTextFieldPanel) dbNameRestrictionPanel).setText(DbNameUtils.generateDefaultRestriction(layer, false));
    }//GEN-LAST:event_cmdDefaultRestrictionActionPerformed

    private void cmdAccessibleRootsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAccessibleRootsActionPerformed
        setAccessibleRoots(true);
    }//GEN-LAST:event_cmdAccessibleRootsActionPerformed

    private void cmdAccessibleRolesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAccessibleRolesActionPerformed
        setAccessibleRoots(false);
    }//GEN-LAST:event_cmdAccessibleRolesActionPerformed

    private void selectRootDocMapDefBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectRootDocMapDefBtnActionPerformed
        Id oldRootMapId = layer.getRootDocMapDefId();

        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(layer, new AdsVisitorProvider() {
            @Override
            public boolean isTarget(RadixObject object) {
                return (object instanceof AdsDocMapDef);
            }
        }
        );

        Definition def = ChooseDefinition.chooseDefinition(cfg);
        if (def != null && !def.getId().equals(oldRootMapId)) {
            layer.setRootDocMapDefId(def.getId());
            refreshRootDocMapDefId();
        }
    }//GEN-LAST:event_selectRootDocMapDefBtnActionPerformed

    private void comboBoxMainDocLanguagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxMainDocLanguagActionPerformed
        EIsoLanguage oldLang = layer.getMainDocLanguage();
        JComboBox cb = (JComboBox) evt.getSource();
        EIsoLanguage lang = (EIsoLanguage) cb.getSelectedItem();
        if (!lang.equals(oldLang)) {
            layer.setMainDocLanguage(lang);
        }
    }//GEN-LAST:event_comboBoxMainDocLanguagActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField URIValue;
    private javax.swing.JButton btChooseBaseLayers;
    private javax.swing.JButton cmdAccessibleRoles;
    private javax.swing.JButton cmdAccessibleRoots;
    private javax.swing.JButton cmdDefaultRestriction;
    private javax.swing.JComboBox comboBoxMainDocLanguag;
    private javax.swing.JTextField copyrightValue;
    private javax.swing.JPanel dbNameRestrictionPanel;
    private javax.swing.JTextField extensionsValue;
    private javax.swing.JTextField languagesValue;
    private javax.swing.JLabel lbMainDoclLanguage;
    private javax.swing.JLabel lbRootDocMapDef;
    private javax.swing.JLabel lblAccessibleRoles;
    private javax.swing.JLabel lblAccessibleRoots;
    private javax.swing.JLabel lblBaseLayers;
    private javax.swing.JLabel lblCopyright;
    private javax.swing.JLabel lblDbNameRestriction;
    private javax.swing.JLabel lblEnclosingBranch;
    private javax.swing.JLabel lblExtensions;
    private javax.swing.JLabel lblLanguages;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblRelease;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUri;
    private org.radixware.kernel.designer.common.dialogs.components.NameEditPanel nameValue;
    private javax.swing.JTextField ownerBranchValue;
    private javax.swing.JTextField prevLayerUriValue;
    private javax.swing.JTextField releaseValue;
    private javax.swing.JTextField rootDocMapDefValue;
    private javax.swing.JButton selectExtensionsBtn;
    private javax.swing.JButton selectLanguagesBtn;
    private javax.swing.JButton selectRootDocMapDefBtn;
    private javax.swing.JTextField titleValue;
    private javax.swing.JTextField txtAccessibleRoles;
    private javax.swing.JTextField txtAccessibleRoots;
    // End of variables declaration//GEN-END:variables

    private void refreshLanguagesField() {
        //update listing of languages
        final int len = layer.getLanguages().size();
        if (len > 0) {

            final java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();

            final EIsoLanguage[] langs = new EIsoLanguage[len];
            layer.getLanguages().toArray(langs);
            int i = 0;
            for (; i < len - 1; ++i) {
                stringBuilder.append(langs[i].getName()).append(", ");
            }
            stringBuilder.append(langs[i].getName());

            languagesValue.setText(stringBuilder.toString());
        } else {
            languagesValue.setText("");
        }
    }

    private void refreshMainDocLanguageField() {
        final List<EIsoLanguage> languages = layer.getLanguages();
        // TODO: !!! нужно обработать ситуацию когда язык который был выбран ранее пробпал из списка.
        comboBoxMainDocLanguag.setModel(new javax.swing.DefaultComboBoxModel(languages.toArray()));
        comboBoxMainDocLanguag.setSelectedItem(layer.getMainDocLanguage());
    }

    private void refreshRootDocMapDefId() {
        final Id idMap = layer.getRootDocMapDefId();
        final Branch branch = layer.getBranch();
        Layers layers = branch.getLayers();

        // ищим во всех слоях нужную map 
        for (Layer item : layers) {
            AdsDocMapDef map = (AdsDocMapDef) item.find(new AdsVisitorProvider() {
                public boolean isTarget(RadixObject object) {
                    return object instanceof AdsDocMapDef && (((AdsDocMapDef) object).getId() == idMap);
                }

            }
            );
            if (map != null) {
                rootDocMapDefValue.setText(map.getQualifiedName());
                break;
            }
        }
    }

    private void refreshExtensionsField() {
        final int len = extensions.size();
        final StringBuilder text = new StringBuilder();
        for (int i = 0; i < len; i++) {
            final String title = String.valueOf(extensions.get(i).getTitle());
            text.append(title);
            if (i != len - 1) {
                text.append(", ");
            }
        }
        extensionsValue.setText(text.toString());
    }
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public final void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    protected final void fireChangeEvent() {
        changeSupport.fireChange();
    }

    protected void applySetup() {
        layer.setName(nameValue.getCurrentName());
        layer.setCopyright(copyrightValue.getText());
        layer.setURI(URIValue.getText());
        layer.setTitle(titleValue.getText());
        layer.setDbObjectNamesRestriction(((ExpandableTextFieldPanel) dbNameRestrictionPanel).getText());
        try {
            layer.setExtensions(extensions);
        } catch (IOException ex) {
            stateManager.error(ex.toString());
        }
    }

    public boolean isComplete() {

        if (!URINameIsValid(URIValue.getText())) {
            stateManager.error(NbBundle.getMessage(PropertiesPanel.class, "Error-Invalid-Layer-URI"));
            return false;
        }

        if (!nameValue.isComplete()) {
            return false;
        }

        stateManager.ok();
        return true;
    }

    private void setAccessibleRoots(boolean forParagraphs) {
        final ItemClusterizator.IClusterizatorModel<RootDefinitionItem> model = forParagraphs ? paragraphNavigator.createModel() : rolesNavigator.createModel();
        final List<RootDefinitionItem> cluster = ItemClusterizator.cluster(model);
        if (cluster != null) {

            final List<Layer.RootDefinition> roots = new ArrayList<>();
            for (final RootDefinitionItem item : cluster) {
                roots.add(new Layer.RootDefinition(item.paragraph.getLayer().getURI(), item.id));
            }

            if (forParagraphs) {
                layer.getAccessibleRoots().setRoots(roots);
                txtAccessibleRoots.setText(model.toString());
            } else {
                layer.getAccessibleRoles().setRoots(roots);
                txtAccessibleRoles.setText(model.toString());
            }

        }
        if (forParagraphs) {
            layer.getAccessibleRoots().setInherited(model.isInherit());
        } else {
            layer.getAccessibleRoles().setInherited(model.isInherit());
        }
    }
}
