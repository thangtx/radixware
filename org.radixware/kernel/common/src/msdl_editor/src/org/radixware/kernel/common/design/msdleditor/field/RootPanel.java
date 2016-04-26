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
 * RootPanel.java
 *
 * Created on 19.03.2009, 10:53:01
 */
package org.radixware.kernel.common.design.msdleditor.field;

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.RenameEvent;
import org.radixware.kernel.common.defs.RadixObject.RenameListener;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.design.msdleditor.AbstractEditItem;
import org.radixware.kernel.common.design.msdleditor.DefaultLayout;
import org.radixware.kernel.common.design.msdleditor.field.panel.ChangeNamespaceDialog;
import org.radixware.kernel.common.design.msdleditor.field.panel.TestDialog;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.msdl.EFieldType;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.MsdlStructureField;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.common.msdl.XmlObjectMessagingXsdCreator;
import org.radixware.kernel.common.msdl.fields.StructureFieldModel;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExecAppByFileName;

public class RootPanel extends AbstractEditItem implements RenameListener {

    private static final String PREFERENCES_KEY = "MsdlEditor";
    private static final String FIELD_DIVIDER_POS = "FieldDividerPos";

    private RootMsdlScheme rootMsdlScheme;
    private TestDialog testDialog = null;
    private JPanel preprocessorClassPanel = null;
    private JLabel preprocessorLabel = null;
    JButton buttonEdit = null;
    private boolean opened;

    /**
     * Creates new form RootPanel
     */
    public RootPanel() {
        initComponents();
        buttonEdit = namespace.addButton("...");
        buttonEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame frm = (Frame) Window.getOwnerlessWindows()[0];
                ChangeNamespaceDialog d = new ChangeNamespaceDialog(frm, true, rootMsdlScheme.getNamespace());
                d.pack();
                d.setLocationRelativeTo(null);
                d.setVisible(true);
                if (d.result != null && !d.result.equals(rootMsdlScheme.getNamespace())) {
                    namespace.setValue(d.result);
                    rootMsdlScheme.setNamespace(d.result);
                }
                d.dispose();
            }
        });
        titledPiecePanel1.setTitle("Message Format");
    }

    private void doDefaultLayout() {
        ArrayList<JLabel> l = new ArrayList<JLabel>();
        l.add(jLabelDefinitionName);
        l.add(jLabelNamespace);
        if (preprocessorLabel != null) {
            l.add(preprocessorLabel);
        }
        ArrayList<JComponent> e = new ArrayList<JComponent>();
        e.add(definitionName);
        e.add(namespace);
        if (preprocessorClassPanel != null) {
            e.add(preprocessorClassPanel);
        }
        DefaultLayout.doLayout(jPanel2, l, e, true);
    }

    private void restoreCfg() {
        try {
            if (Preferences.userRoot().nodeExists(PREFERENCES_KEY)) {
                Preferences editorPreferences = Preferences.userRoot().node(PREFERENCES_KEY);
                int pos = editorPreferences.getInt(FIELD_DIVIDER_POS, 300);
                jSplitPane1.setDividerLocation(pos);
            }
        } catch (BackingStoreException ex) {
            JOptionPane.showMessageDialog(null, "Can't restore configuration: " + ex.getMessage());
        }
    }

    public void open(final RootMsdlScheme rootMsdlScheme, JPanel preprocessorClassPanel) {
        super.open(rootMsdlScheme);
        this.preprocessorClassPanel = preprocessorClassPanel;
        if (preprocessorClassPanel != null) {
            add(preprocessorClassPanel);
            preprocessorLabel = new javax.swing.JLabel();
            preprocessorLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/field/panel/Bundle");
            preprocessorLabel.setText(bundle.getString("RootHeaderPanel.jLabel1.text")); // NOI18N
            preprocessorLabel.setFont(jLabelNamespace.getFont());
        }

        doDefaultLayout();
        restoreCfg();
        this.rootMsdlScheme = rootMsdlScheme;

        rootMsdlScheme.addRenameListener(this);
        structurePanel1.open(rootMsdlScheme.getStructureFieldModel(), this);
        titledPiecePanel1.open(rootMsdlScheme.getStructureFieldModel(), rootMsdlScheme.getStructureFieldModel().getField());
        update();
    }

    @Override
    public void update() {
        opened = false;
        definitionName.setText(rootMsdlScheme.getName());
        namespace.setTextFieldValue(rootMsdlScheme.getNamespace());
        structurePanel1.update();
        titledPiecePanel1.update();
        opened = true;
        super.update();
        jButtonTest.setEnabled(true);
        jButtonXsdScheme.setEnabled(true);
    }

    public void setDbfViewMode(boolean dbfMode) {
        titledPiecePanel1.setVisible(!dbfMode);
        jButtonTest.setVisible(!dbfMode);
        if (dbfMode) {
            StructureFieldModel model = rootMsdlScheme.getStructureFieldModel();
            List<MsdlStructureField> list = model.getFields().list();
            ArrayList<MsdlField> deleted = new ArrayList<MsdlField>();
            for (MsdlField cur : list) {
                if (cur.getType() != EFieldType.STR && cur.getType() != EFieldType.NUM
                        && cur.getType() != EFieldType.BOOLEAN && cur.getType() != EFieldType.DATETIME) {
                    deleted.add(cur);
                }
            }
            for (MsdlField cur : deleted) {
                cur.delete();
            }
        }
    }

    public TestDialog getTestDialog() {
        if (testDialog == null) {
            Frame frm = (Frame) Window.getOwnerlessWindows()[0];
            testDialog = new TestDialog(frm, true);
            testDialog.pack();
            testDialog.setLocationRelativeTo(null);
        }
        return testDialog;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButtonXsdScheme = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabelDefinitionName = new javax.swing.JLabel();
        definitionName = new javax.swing.JTextField();
        jLabelNamespace = new javax.swing.JLabel();
        namespace = new org.radixware.kernel.common.components.ExtendableTextField();
        jButtonTest = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        structurePanel1 = new org.radixware.kernel.common.design.msdleditor.field.panel.StructurePanel();
        titledPiecePanel1 = new org.radixware.kernel.common.design.msdleditor.piece.TitledPiecePanel();

        setMinimumSize(new java.awt.Dimension(0, 300));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/field/panel/Bundle"); // NOI18N
        jButtonXsdScheme.setText(bundle.getString("RootHeaderPanel.jButtonXsdScheme.text")); // NOI18N
        jButtonXsdScheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonXsdSchemeActionPerformed(evt);
            }
        });

        jLabelDefinitionName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelDefinitionName.setText(bundle.getString("RootHeaderPanel.jLabelDefinitionName.text")); // NOI18N

        definitionName.setEditable(false);
        definitionName.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                definitionNameCaretUpdate(evt);
            }
        });
        definitionName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                definitionNameKeyReleased(evt);
            }
        });

        jLabelNamespace.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelNamespace.setText(bundle.getString("RootHeaderPanel.jLabelNamespace.text")); // NOI18N

        namespace.setEditable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelNamespace, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                    .addComponent(jLabelDefinitionName, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(namespace, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                    .addComponent(definitionName, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDefinitionName, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(definitionName, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNamespace, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(namespace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34))
        );

        jButtonTest.setText(bundle.getString("RootHeaderPanel.jButtonTest.text")); // NOI18N
        jButtonTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTestActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonTest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonXsdScheme))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonXsdScheme)
                .addGap(4, 4, 4)
                .addComponent(jButtonTest))
        );

        jSplitPane1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jSplitPane1PropertyChange(evt);
            }
        });

        structurePanel1.setMinimumSize(new java.awt.Dimension(292, 415));
        jSplitPane1.setLeftComponent(structurePanel1);
        jSplitPane1.setRightComponent(titledPiecePanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void definitionNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_definitionNameKeyReleased
        if (!opened) {
            return;
        }
        rootMsdlScheme.setName(definitionName.getText());
        getMsdlField().setModified();
}//GEN-LAST:event_definitionNameKeyReleased

    private void jButtonTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTestActionPerformed
        getTestDialog().open(rootMsdlScheme.getStructureFieldModel(), rootMsdlScheme.getQualifiedName());
}//GEN-LAST:event_jButtonTestActionPerformed


    private void jButtonXsdSchemeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonXsdSchemeActionPerformed
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        SchemaDocument sd = null;
        File f = null;
        try {
            f = File.createTempFile("msdl_editor", ".xsd");
        } catch (IOException ex) {
            Logger.getLogger(RootPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            AdsMsdlSchemeDef def = null;
            for (RadixObject obj = rootMsdlScheme.getContainer(); obj != null; obj = obj.getContainer()) {
                if (obj instanceof AdsMsdlSchemeDef) {
                    def = (AdsMsdlSchemeDef) obj;
                    break;
                }
            }
            if (def == null) {
                return;
            }
            final AdsMsdlSchemeDef ownerDef = def;
            sd = XmlObjectMessagingXsdCreator.createXSD(rootMsdlScheme.getMessage(), ownerDef.getId().toString(), new XmlObjectMessagingXsdCreator.DefinitionTypeResolver() {
                @Override
                public String getEnumTypeName(String enumId) {
                    Id id = Id.Factory.loadFrom(enumId);
                    AdsEnumDef enumeration = AdsSearcher.Factory.newAdsEnumSearcher(ownerDef.getModule()).findById(id).get();
                    if (enumeration != null) {
                        if (enumeration.getItemType() == EValType.CHAR) {
                            return "Char";
                        } else {
                            return "Str";
                        }
                    } else {
                        return "Str";
                    }
                }

                @Override
                public String getMsdlSchemeNs(String schemeId) {
                    Id id = Id.Factory.loadFrom(schemeId);
                    Definition enumeration = AdsSearcher.Factory.newAdsDefinitionSearcher(ownerDef.getModule()).findById(id).get();
                    if (enumeration instanceof AdsMsdlSchemeDef) {
                        return ((AdsMsdlSchemeDef) enumeration).getTargetNamespace();
                    } else {
                        return null;
                    }
                }

            });
        } catch (XmlException ex) {
            Logger.getLogger(RootPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            sd.save(f);
        } catch (IOException ex) {
            Logger.getLogger(RootPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        ExecAppByFileName.exec(f.getAbsolutePath());
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

//     Don't work under Linux
//        try {
//            setCursor(new Cursor(Cursor.WAIT_CURSOR));
//            File f = File.createTempFile("msdl_editor",".xsd");
//            SchemaDocument sd = XmlObjectMessagingXsdCreator.createXSD(rootMsdlScheme.getMessage(), "");
//            sd.save(f);
//            ExecAppByFileName.open(f);
//            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//        } catch (DesktopNotSupported ex) {
//            Logger.getLogger(RootPanel.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (XmlException ex) {
//            Logger.getLogger(RootPanel.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(RootPanel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
}//GEN-LAST:event_jButtonXsdSchemeActionPerformed

    private void jSplitPane1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jSplitPane1PropertyChange
        if (evt.getPropertyName().equalsIgnoreCase("dividerLocation")) {
            int newValue = ((Integer) evt.getNewValue()).intValue();
            Preferences editorPreferences = Preferences.userRoot().node(PREFERENCES_KEY);
            editorPreferences.putInt(FIELD_DIVIDER_POS, newValue);
            try {
                Preferences.userRoot().flush();
            } catch (BackingStoreException ex) {
                JOptionPane.showMessageDialog(null, "Can't save configuration: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_jSplitPane1PropertyChange

    private void definitionNameCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_definitionNameCaretUpdate
        if (!opened) {
            return;
        }
        rootMsdlScheme.setName(definitionName.getText());
    }//GEN-LAST:event_definitionNameCaretUpdate

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField definitionName;
    private javax.swing.JButton jButtonTest;
    private javax.swing.JButton jButtonXsdScheme;
    private javax.swing.JLabel jLabelDefinitionName;
    private javax.swing.JLabel jLabelNamespace;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSplitPane jSplitPane1;
    private org.radixware.kernel.common.components.ExtendableTextField namespace;
    private org.radixware.kernel.common.design.msdleditor.field.panel.StructurePanel structurePanel1;
    private org.radixware.kernel.common.design.msdleditor.piece.TitledPiecePanel titledPiecePanel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onEvent(RenameEvent e) {
        if (!opened) {
            return;
        }
        opened = false;
        definitionName.setText(e.radixObject.getName());
        opened = true;
    }

}
