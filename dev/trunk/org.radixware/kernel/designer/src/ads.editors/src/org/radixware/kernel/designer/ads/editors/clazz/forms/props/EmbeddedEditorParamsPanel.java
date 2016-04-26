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

package org.radixware.kernel.designer.ads.editors.clazz.forms.props;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsSelectorExplorerItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.ads.common.lookup.AdsEditorPresentationLookupSupport;
import org.radixware.kernel.designer.ads.common.lookup.AdsExplorerItemLookupSupport;


final class EmbeddedEditorParamsPanel extends JPanel implements PropertyChangeListener {

    private PropertyEnv env;
    private EmbeddedEditorParamsEditor editor;
    private AdsPropertyDef p = null;
    private AdsExplorerItemDef explorerItem = null;
    private AdsEntityObjectClassDef c = null;
    private AdsEditorPresentationDef ep = null;

    EmbeddedEditorParamsPanel(EmbeddedEditorParamsEditor editor, PropertyEnv env) {
        this.env = env;
        this.editor = editor;

        env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
        env.addPropertyChangeListener(this);

        initComponents();

        AdsUIProperty.EmbeddedEditorOpenParamsProperty prop = getProperty();

        p = prop.findProperty();
        explorerItem = prop.findExplorerItem();

        c = prop.findClass();
        ep = prop.findEditorPresentation();

        JButton button = textProperty.addButton();
        button.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdsClassDef ownerClass = AdsUIUtil.getOwnerClassDef(getNode());

                List<Definition> list = new ArrayList<Definition>();
                if (ownerClass != null) {
                    for (AdsDefinition def : ownerClass.getProperties().get(EScope.ALL)) {
                        list.add(def);
                    }
                }

                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(list);
                p = (AdsPropertyDef) ChooseDefinition.chooseDefinition(cfg);
                if (p != null) {
                    explorerItem = null;
                    c = null;
                    ep = null;
                }

                update();
            }
        });

        button = textProperty.addButton();
        button.setIcon(RadixWareIcons.DELETE.CLEAR.getIcon());
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p = null;
                update();
            }
        });

        button = textExplorerItem.addButton();
        button.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(availableExplorerItems());
                explorerItem = (AdsExplorerItemDef) ChooseDefinition.chooseDefinition(cfg);

                if (explorerItem != null) {
                    p = null;
                    c = null;
                    ep = null;
                }

                update();
            }
        });

        button = textExplorerItem.addButton();
        button.setIcon(RadixWareIcons.DELETE.CLEAR.getIcon());
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                explorerItem = null;
                update();
            }
        });

        button = textEditorPresentation.addButton();
        button.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                VisitorProvider provider = new AdsVisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject object) {
                        return object instanceof AdsEditorPresentationDef;
                    }
                };

                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(getNode(), provider);
                ep = (AdsEditorPresentationDef) ChooseDefinition.chooseDefinition(cfg);
                if (ep != null) {
                    p = null;
                    explorerItem = null;
                    c = ep.getOwnerClass();
                } else {
                    c = null;
                }

                update();
            }
        });

        button = textEditorPresentation.addButton();
        button.setIcon(RadixWareIcons.DELETE.CLEAR.getIcon());
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c = null;
                ep = null;
                update();
            }
        });

        update();

        textProperty.setEnabled(!getNode().isReadOnly());
        textExplorerItem.setEnabled(!getNode().isReadOnly());
        textEditorPresentation.setEnabled(!getNode().isReadOnly());
    }

    private Collection<AdsExplorerItemDef> availableExplorerItems() {
        final AdsExplorerItemLookupSupport.IFilter filter = new AdsExplorerItemLookupSupport.IFilter() {
            @Override
            public boolean accept(AdsExplorerItemDef explorerItem) {
                return !(explorerItem instanceof AdsSelectorExplorerItemDef);
            }
        };

        final AdsEditorPresentationDef editorPresentation = RadixObjectsUtils.findContainer(getNode(), AdsEditorPresentationDef.class);
        if (editorPresentation != null) {
            return AdsEditorPresentationLookupSupport.getAvailableEmbeddedExplorerItems(editorPresentation, filter);
        } else {
            final AdsParagraphExplorerItemDef paragraphExplorerItem = RadixObjectsUtils.findContainer(getNode(), AdsParagraphExplorerItemDef.class);
            if (paragraphExplorerItem != null) {
                return AdsExplorerItemLookupSupport.getAvailableEmbeddedExplorerItems(
                        paragraphExplorerItem.getExplorerItems().getChildren().get(EScope.ALL), filter);
            }
        }

        return Collections.<AdsExplorerItemDef>emptyList();
    }

    private AdsUIProperty.EmbeddedEditorOpenParamsProperty getProperty() {
        return (AdsUIProperty.EmbeddedEditorOpenParamsProperty) editor.getValue();
    }

    private RadixObject getNode() {
        return ((UIPropertySupport) editor.getSource()).getNode();
    }

    public void update() {
        AdsUIProperty.EmbeddedEditorOpenParamsProperty prop = getProperty();
        textProperty.setValue(p != null ? p.getQualifiedName() : NbBundle.getMessage(getClass(), "Empty.text"));
        textExplorerItem.setValue(explorerItem != null ? explorerItem.getQualifiedName() : NbBundle.getMessage(getClass(), "Empty.text"));
        textEditorPresentation.setValue(c != null && ep != null ? c.getQualifiedName() + " - " + ep.getName() : NbBundle.getMessage(getClass(), "Empty.text"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textProperty = new ExtendableTextField(true);
        labelProperty = new javax.swing.JLabel();
        textEditorPresentation = new ExtendableTextField(true);
        labelExplorerItem = new javax.swing.JLabel();
        textExplorerItem = new ExtendableTextField(true);
        labelEditorPresentation = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.inactiveCaption));
        setMinimumSize(new java.awt.Dimension(200, 140));
        setPreferredSize(new java.awt.Dimension(320, 260));

        labelProperty.setText(org.openide.util.NbBundle.getMessage(EmbeddedEditorParamsPanel.class, "EmbeddedEditorParamsPanel.labelProperty.text")); // NOI18N

        labelExplorerItem.setText(org.openide.util.NbBundle.getMessage(EmbeddedEditorParamsPanel.class, "EmbeddedEditorParamsPanel.labelExplorerItem.text")); // NOI18N

        labelEditorPresentation.setText(org.openide.util.NbBundle.getMessage(EmbeddedEditorParamsPanel.class, "EmbeddedEditorParamsPanel.labelEditorPresentation.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelEditorPresentation)
                    .addComponent(labelExplorerItem)
                    .addComponent(labelProperty))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textProperty, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(textExplorerItem, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(textEditorPresentation, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textProperty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelProperty))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textExplorerItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelExplorerItem))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textEditorPresentation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelEditorPresentation))
                .addContainerGap(124, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelEditorPresentation;
    private javax.swing.JLabel labelExplorerItem;
    private javax.swing.JLabel labelProperty;
    private org.radixware.kernel.common.components.ExtendableTextField textEditorPresentation;
    private org.radixware.kernel.common.components.ExtendableTextField textExplorerItem;
    private org.radixware.kernel.common.components.ExtendableTextField textProperty;
    // End of variables declaration//GEN-END:variables

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (PropertyEnv.PROP_STATE.equals(evt.getPropertyName()) && evt.getNewValue() == PropertyEnv.STATE_VALID) {
            AdsUIProperty.EmbeddedEditorOpenParamsProperty prop = getProperty();

            prop.setPropertyId(p != null ? p.getId() : null);
            prop.setExplorerItemId(explorerItem != null ? explorerItem.getId() : null);
            prop.setEditorPresentation(c != null ? c.getId() : null, ep != null ? ep.getId() : null);

            editor.setValue(prop);
            try {
                ((UIPropertySupport) editor.getSource()).setValue(prop);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
