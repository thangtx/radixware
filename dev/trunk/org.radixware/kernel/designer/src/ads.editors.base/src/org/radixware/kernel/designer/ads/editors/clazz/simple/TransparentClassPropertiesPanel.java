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
 * TransparentClassPropertiesPanel.java
 *
 * Created on 16.09.2009, 14:41:11
 */

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel;
import org.radixware.kernel.designer.ads.common.dialogs.ChooseEnvComboBoxModel;
import org.radixware.kernel.designer.common.dialogs.components.TypeArgumentsEditor;


public class TransparentClassPropertiesPanel extends javax.swing.JPanel {

    /** Creates new form TransparentClassPropertiesPanel */
    public TransparentClassPropertiesPanel() {
        initComponents();
        setLayout(new BorderLayout());
        add(content, BorderLayout.NORTH);
        publishedViewer.setEditable(false);
        envBox.setModel(envModel);
        envBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!TransparentClassPropertiesPanel.this.isUpdate){
                    ERuntimeEnvironmentType selected = TransparentClassPropertiesPanel.this.envModel.getCurrentEnv();
                    TransparentClassPropertiesPanel.this.definition.setUsageEnvironment(selected); 
                }
            }

        });
    }

    private TypeArgumentsEditor argumentsEditor = new TypeArgumentsEditor();
    private javax.swing.JLabel publishedLabel = new javax.swing.JLabel(NbBundle.getMessage(MainPanel.class, "TransparentClass-NameTip"));
    private javax.swing.JTextField publishedViewer = new javax.swing.JTextField();
    private ChooseEnvComboBoxModel envModel = new ChooseEnvComboBoxModel();
    private javax.swing.JComboBox envBox = new javax.swing.JComboBox();
    private javax.swing.JLabel envLabel = new javax.swing.JLabel(NbBundle.getMessage(TransparentClassPropertiesPanel.class, "TypeArgumetnsOwner-EnvTip"));
    private javax.swing.JPanel content = new javax.swing.JPanel();
    private AccessEditPanel accessEditor = new AccessEditPanel();
    private javax.swing.JLabel accessLabel = new javax.swing.JLabel("Accessibility: ");

    private AdsClassDef definition;
    private boolean isUpdate = false;

    public void open(AdsClassDef definition){
        this.definition = definition;
        update();
    }

    public void update(){
        isUpdate = true;
        content.removeAll();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        content.setLayout(gbl);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(12, 0, 0, 12);
        gbl.setConstraints(publishedLabel, c);
        content.add(publishedLabel);

        c.gridx = 1;
        c.weightx = 1.0;
        c.insets = new Insets(12, 0, 0, 0);
        gbl.setConstraints(publishedViewer, c);
        content.add(publishedViewer);
        publishedViewer.setText(definition.getTransparence().getPublishedName());

        accessEditor.open(definition);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.0;
        c.insets = new Insets(12, 0, 12, 12);
        gbl.setConstraints(accessLabel, c);
        content.add(accessLabel);
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(12, 0, 12, 0);
        c.gridx = 1;
        gbl.setConstraints(accessEditor, c);
        content.add(accessEditor);

        c.fill = GridBagConstraints.HORIZONTAL;

        final EClassType classType = definition.getClassDefType();
        if (classType == EClassType.DYNAMIC ||
            classType == EClassType.INTERFACE ||
            classType == EClassType.EXCEPTION){
            c.gridx = 0;
            c.gridy = 2;
            c.insets = new Insets(0, 0, 0, 12);
            c.weightx = 0.0;
            gbl.setConstraints(envLabel, c);
            content.add(envLabel);

            envModel.setCurrentEnv(definition.getUsageEnvironment());
            c.gridx = 1;
            c.weightx = 1.0;
            c.insets = new Insets(0, 0, 0, 0);
            gbl.setConstraints(envBox, c);
            content.add(envBox);

            c.gridx = 0;
            c.gridy = 3;
            c.gridwidth = 2;
            c.insets = new Insets(12, 0, 12, 0);
            argumentsEditor.open(definition, definition.getTypeArguments(), definition.isReadOnly());
            
            javax.swing.JPanel argContent = new javax.swing.JPanel();
            GridBagLayout argGbl = new GridBagLayout();
            GridBagConstraints argC = new GridBagConstraints();
            argContent.setLayout(argGbl);
            argContent.setBorder(new TitledBorder(NbBundle.getMessage(TransparentClassPropertiesPanel.class, "TypeArgumentsOwner-ArgumentsTip")));

            argC.insets = new Insets(12, 12, 12, 12);
            argC.weightx = 1.0;
            argC.weighty = 1.0;
            argC.fill = GridBagConstraints.BOTH;
            argGbl.setConstraints(argumentsEditor, argC);
            argContent.add(argumentsEditor);
            gbl.setConstraints(argContent, c);
            content.add(argContent); 
        }
        isUpdate = false;
    }

    public void setReadonly(boolean readonly){
        argumentsEditor.setReadonly(readonly);
        envBox.setEnabled(!readonly); 
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
