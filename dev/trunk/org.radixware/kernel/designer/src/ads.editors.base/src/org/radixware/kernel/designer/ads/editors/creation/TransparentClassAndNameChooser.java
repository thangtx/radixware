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
 * TransparentClassAndNameChooser.java
 *
 * Created on May 6, 2009, 10:18:47 AM
 */
package org.radixware.kernel.designer.ads.editors.creation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.platform.PlatformLib;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.ads.common.dialogs.ChooseEnvComboBoxModel;
import org.radixware.kernel.designer.common.dialogs.choosetype.RadixPlatformClassPanel;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class TransparentClassAndNameChooser extends javax.swing.JPanel {

    private JButton pBtn;
    private final String NO_PUBLISHED = NbBundle.getMessage(TransparentClassAndNameChooser.class, "Creature-Step-ChooseTip");
    private ERuntimeEnvironmentType classEnv;
    private ChooseEnvComboBoxModel envModel = new ChooseEnvComboBoxModel();

    /** Creates new form TransparentClassAndNameChooser */
    public TransparentClassAndNameChooser() {
        initComponents();
        publishingField.setValue(NO_PUBLISHED);
        pBtn = publishingField.addButton();
        pBtn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        pBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                ERuntimeEnvironmentType usage = TransparentClassAndNameChooser.this.classEnv;

                String type = RadixPlatformClassPanel.choosePlatformCLass(TransparentClassAndNameChooser.this.context, usage);
                if (type != null && !type.isEmpty()) {
                    publishingField.setValue(type);
                    int point = type.lastIndexOf(".");
                    String classname = type.substring(point + 1, type.length());
                    nameField.setText(classname);
                    changeSupport.fireChange();
                }
            }
        });
        nameField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                changeSupport.fireChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeSupport.fireChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
               changeSupport.fireChange();
            }
        });

        pBtn.setEnabled(false);
        cbEnv.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                TransparentClassAndNameChooser.this.setUsageEnvironment(TransparentClassAndNameChooser.this.getEnvironment());
                TransparentClassAndNameChooser.this.changeSupport.fireChange();
            }
        });
    }
    private AdsModule context;

    public void open(AdsModule context, ERuntimeEnvironmentType usage) {
        this.context = context;
        this.classEnv = usage;
        pBtn.setEnabled(!context.isReadOnly());
    }

    public void open(AdsModule context, ERuntimeEnvironmentType usage, String publishedName, String customName) {
        this.context = context;
        this.classEnv = usage;
        publishingField.setValue(publishedName);
        nameField.setText(customName);
        pBtn.setEnabled(!context.isReadOnly());
    }

    public void setUsageEnvironment(ERuntimeEnvironmentType usage) {
        this.classEnv = usage;
    }

    public String getPublishedName() {
        return publishingField.getValue().toString();
    }

    public String getCustomName() {
        return nameField.getText();
    }

    public ERuntimeEnvironmentType getEnvironment() {
        return envModel.getCurrentEnv();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        publishingField = new org.radixware.kernel.common.components.ExtendableTextField();
        nameField = new javax.swing.JTextField();
        cbEnv = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();

        jLabel1.setText(org.openide.util.NbBundle.getMessage(TransparentClassAndNameChooser.class, "Creature-Step-Label")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(TransparentClassAndNameChooser.class, "Name-Label")); // NOI18N

        publishingField.setReadOnly(true);

        cbEnv.setModel(envModel);

        jLabel3.setText(org.openide.util.NbBundle.getMessage(TransparentClassAndNameChooser.class, "AdsTransparentWizard-Env-Tip")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(publishingField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                    .addComponent(cbEnv, 0, 231, Short.MAX_VALUE)
                    .addComponent(nameField, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbEnv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(publishingField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbEnv;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField nameField;
    private org.radixware.kernel.common.components.ExtendableTextField publishingField;
    // End of variables declaration//GEN-END:variables
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public final void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }
    private final StateManager stateManager = new StateManager(this);

    public boolean isComplete() {
        if (publishingField.getValue().equals(NO_PUBLISHED)) {
            stateManager.error(NbBundle.getMessage(TransparentClassAndNameChooser.class, "Creature-Step-Error"));
            return false;
        }

        final String name = nameField.getText();
        if (!RadixObjectsUtils.isCorrectName(name)) {
            stateManager.error(NbBundle.getMessage(TransparentClassAndNameChooser.class, "Creature-Step-NameError"));
            return false;
        }

        boolean isSuitable = isSuitableClass();
        if (!isSuitable) {
            return false;
        } else if (!checkPublishedClasses(publishingField.getValue().toString())) {
            stateManager.error(NbBundle.getMessage(TransparentClassAndNameChooser.class, "Creature-RepeatedNameError"));
            return false;
        }

        stateManager.ok();
        return true;
    }

    private boolean isSuitableClass() {
        String p = publishingField.getValue().toString();
        ERuntimeEnvironmentType usage = classEnv;
        PlatformLib lib = ((AdsSegment) context.getSegment()).getBuildPath().getPlatformLibs().getKernelLib(usage);
        PlatformLib commonLib = ((AdsSegment) context.getSegment()).getBuildPath().getPlatformLibs().getKernelLib(ERuntimeEnvironmentType.COMMON);
        RadixPlatformClass cl = lib.findPlatformClass(p);
        RadixPlatformClass commonCl = commonLib.findPlatformClass(p);
        if (cl == null && commonCl == null) {
            if (usage == ERuntimeEnvironmentType.SERVER) {
                stateManager.error(NbBundle.getMessage(TransparentClassAndNameChooser.class, "Creature-Step-UsageError_server"));
            } else {
                stateManager.error(NbBundle.getMessage(TransparentClassAndNameChooser.class, "Creature-Step-UsageError_explorer"));
            }
            return false;
        }
        return true;
    }

    private boolean checkPublishedClasses(String chosen) {
        ModuleDefinitions defs = context.getDefinitions();
        for (int i = 0; i <= defs.size() - 1; i++) {
            AdsDefinition d = defs.get(i);
            if (d instanceof AdsClassDef) {
                AdsClassDef cl = (AdsClassDef) d;
                if (cl.getTransparence() != null) {
                    String published = cl.getTransparence().getPublishedName();
                    if (published != null &&
                            !published.isEmpty()) {
                        if (published.equals(chosen)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
