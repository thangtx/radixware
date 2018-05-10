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
 * TestPanel.java
 *
 * Created on 05.03.2009, 14:33:55
 */
package org.radixware.kernel.common.design.msdleditor.field.panel;

import org.radixware.kernel.common.msdl.MsdlPreprocessorAccessAas;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.common.msdl.fields.parser.ParseUtil;
import org.radixware.kernel.common.msdl.fields.parser.datasource.DataSourceByteBuffer;
import org.radixware.kernel.common.utils.XmlFormatter;


public class TestPanel extends javax.swing.JPanel {

    private static final String PREFERENCES_KEY = "MsdlEditor";
    private static final String TESTDIALOG_KEY = "TestDialog";
    private static final String TESTPARAM_KEY = "TestParam";
    private static final String ARRAY_KEY = "Array";
    private static final String XML_KEY = "Xml";
    private static final String IS_HEX_KEY = "IsHex";
    private static final String AAS_ADDRESS_KEY = "AasAddress";
    private static final String DIVIDER_POS = "DividerPos";
    AbstractFieldModel fieldModel;
    TestDialog owner;

    /** Creates new form TestPanel */
    public TestPanel() {
        initComponents();
        
        //RADIX-5111
       // hexEditor1.setIsMustConvertAtChange(false);
    }

    public void restoreCfg() {
        try {
            if (Preferences.userRoot().nodeExists(PREFERENCES_KEY)) {
                Preferences editorPreferences = Preferences.userRoot().node(PREFERENCES_KEY);
                if (editorPreferences.nodeExists(TESTDIALOG_KEY)) {
                    Preferences dialog = editorPreferences.node(TESTDIALOG_KEY);
                    if (dialog.nodeExists(TESTPARAM_KEY)) {
                        Preferences param = dialog.node(TESTPARAM_KEY);
                        hexEditor1.setAasAddress(param.get(AAS_ADDRESS_KEY, null));
                        String ns = fieldModel.getRootMsdlScheme().getNamespace();
                        if (ns != null && !ns.equals("")) {
                            String ns1 = ns.replace("//","_");
                            if (param.nodeExists(ns1)) {
                                Preferences scheme = param.node(ns1);
                                if (fieldModel.getContainer() instanceof RootMsdlScheme) {
                                    boolean isHex = scheme.getBoolean(IS_HEX_KEY, true);
                                    hexEditor1.setIsHex(isHex);
                                    hexEditor1.setValue(scheme.getByteArray(ARRAY_KEY, new byte[]{}));
                                    xmlEditor1.setValue(scheme.get(XML_KEY, ""));
                                    jSplitPane1.setDividerLocation(scheme.getInt(DIVIDER_POS, 90));
                                } else {
                                    if (scheme.nodeExists(fieldModel.getName())) {
                                        Preferences field = scheme.node(fieldModel.getName());
                                        boolean isHex = field.getBoolean(IS_HEX_KEY, true);
                                        hexEditor1.setIsHex(isHex);
                                        hexEditor1.setValue(field.getByteArray(ARRAY_KEY, new byte[]{}));
                                        xmlEditor1.setValue(field.get(XML_KEY, ""));
                                        jSplitPane1.setDividerLocation(field.getInt(DIVIDER_POS, 90));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (BackingStoreException ex) {
            JOptionPane.showMessageDialog(null, "Can't restore configuration: " + ex.getMessage());
        }
    }

    public void saveCfg() {
        Preferences editorPreferences = Preferences.userRoot().node(PREFERENCES_KEY);
        Preferences dialog = editorPreferences.node(TESTDIALOG_KEY);
        Preferences param = dialog.node(TESTPARAM_KEY);
        param.put(AAS_ADDRESS_KEY, hexEditor1.getAasAddress() != null ? hexEditor1.getAasAddress() : "");
        String ns = fieldModel.getRootMsdlScheme().getNamespace();
        if (ns != null && !ns.equals("")) {
            String ns1 = ns.replace("//","_");
            Preferences scheme = param.node(ns1);
            byte[] l = null;
            try {
                l = hexEditor1.getValue();
                if (l != null) {
                    if (fieldModel.getContainer() instanceof RootMsdlScheme) {
                        scheme.putByteArray(ARRAY_KEY, l);
                        String xml = xmlEditor1.getValue();
                        scheme.put(XML_KEY, xml);
                        scheme.putBoolean(IS_HEX_KEY, hexEditor1.getIsHex());
                        scheme.putInt(DIVIDER_POS, jSplitPane1.getDividerLocation());
                    } else {
                        Preferences field = scheme.node(fieldModel.getName());
                        field.putByteArray(ARRAY_KEY, l);
                        String xml = xmlEditor1.getValue();
                        field.put(XML_KEY, xml);
                        field.putBoolean(IS_HEX_KEY, hexEditor1.getIsHex());
                        field.putInt(DIVIDER_POS, jSplitPane1.getDividerLocation());
                    }
                }
            } catch (WrongFormatError ex) {
            }
        }
        try {
            Preferences.userRoot().flush();
        } catch (BackingStoreException ex) {
            JOptionPane.showMessageDialog(null, "Can't save configuration: " + ex.getMessage());
        }
    }

    public byte[] getArray() {
        return hexEditor1.getValue();
    }

    public void setArray(byte[] arr) {
        hexEditor1.setValue(arr);
    }

    public XmlObject getXml() throws XmlException {
        String xml = xmlEditor1.getValue();
        XmlObject res = XmlObject.Factory.parse(xml);
        return res;
    }

    public void setXml(XmlObject from) {
        try {
            xmlEditor1.setValue(XmlFormatter.format(from));
        } catch (IOException ex) {
            Logger.getLogger(TestPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void open(TestDialog owner, AbstractFieldModel field) {
        this.owner = owner;
        fieldModel = field;
        restoreCfg();
        xmlEditor1.resetStatus();
        SwingUtilities.getRootPane(this).setDefaultButton(jButtonClose);
    }
    
    private void initPreprocessorAccess() {
        try {
            if (fieldModel.getRootMsdlScheme().getPreprocessorClassGuid() != null && hexEditor1.getAasAddress() != null) {
                fieldModel.getRootMsdlScheme().setPreprocessorAccess(
                        new MsdlPreprocessorAccessAas(
                                fieldModel.getRootMsdlScheme().getPreprocessorClassGuid(),
                                hexEditor1.getAasAddress()));
            } else {
                fieldModel.getRootMsdlScheme().setPreprocessorAccess(null);
            }
        } catch (IOException ex) {
            Logger.getLogger(TestPanel.class.getName()).log(Level.SEVERE, "Error on create msdl preprocessor access.", ex);
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

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jButtonXmlToArray = new javax.swing.JButton();
        jButtonFromArrayToXml = new javax.swing.JButton();
        hexEditor1 = new org.radixware.kernel.common.design.msdleditor.field.panel.HexEditor();
        jPanel2 = new javax.swing.JPanel();
        xmlEditor1 = new org.radixware.kernel.common.design.msdleditor.field.panel.XmlEditor();
        jButtonClose = new javax.swing.JButton();

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jButtonXmlToArray.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/radixware/kernel/common/design/msdleditor/img/up.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/common/design/msdleditor/field/panel/Bundle"); // NOI18N
        jButtonXmlToArray.setText(bundle.getString("TestPanel.jButtonXmlToArray.text")); // NOI18N
        jButtonXmlToArray.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonXmlToArrayActionPerformed(evt);
            }
        });

        jButtonFromArrayToXml.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/radixware/kernel/common/design/msdleditor/img/down.png"))); // NOI18N
        jButtonFromArrayToXml.setText(bundle.getString("TestPanel.jButtonFromArrayToXml.text")); // NOI18N
        jButtonFromArrayToXml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFromArrayToXmlActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonFromArrayToXml)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonXmlToArray))
                    .addComponent(hexEditor1, javax.swing.GroupLayout.DEFAULT_SIZE, 635, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(hexEditor1, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonXmlToArray)
                    .addComponent(jButtonFromArrayToXml))
                .addContainerGap())
        );

        jSplitPane1.setTopComponent(jPanel1);

        jButtonClose.setText(bundle.getString("TestPanel.jButtonClose.text")); // NOI18N
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jButtonClose.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                Esc(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xmlEditor1, javax.swing.GroupLayout.DEFAULT_SIZE, 635, Short.MAX_VALUE)
                    .addComponent(jButtonClose, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xmlEditor1, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonClose, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        jSplitPane1.setRightComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 661, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        owner.savePos();
        saveCfg();
        owner.setVisible(false);
        owner.dispose();
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonFromArrayToXmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFromArrayToXmlActionPerformed
        XmlObject obj = XmlObject.Factory.newInstance();
        boolean fail = true;
        try {
            byte[] from = getArray();
            if (from == null) {
                JOptionPane.showMessageDialog(null, "String is empty");
                return;
            }
            DataSourceByteBuffer dsbf = new DataSourceByteBuffer(from);
            initPreprocessorAccess();
            fieldModel.getParser().parse(obj, dsbf);
            //RADIX-5111
            //JOptionPane.showMessageDialog(owner, "String sucessfully converted to xml");
            fail = false;
        } catch (SmioException ex) {
            showException(ex);
        } catch (IOException ex) {
            showException(ex);
        } catch (SmioError ex) {
            showException(ex);
        } catch (WrongFormatError ex) {
            showException(ex);
        } catch (Exception ex) {
            showException(ex);
        }
        finally {
            if(fail)
                xmlEditor1.setStatus(XmlEditor.EXmlEditorStates.EParseFailure);
            else
                xmlEditor1.setStatus(XmlEditor.EXmlEditorStates.EParseSuccess);
        }
        setXml(obj);
    }//GEN-LAST:event_jButtonFromArrayToXmlActionPerformed

    private void showException(Exception ex) {
        ExceptionDialog d = new ExceptionDialog(null,true);
        d.open(ex);
        d.pack();
        d.setLocationRelativeTo(null);
        d.setVisible(true);
    }

    private void jButtonXmlToArrayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonXmlToArrayActionPerformed
        boolean fail = true;
        try {
            xmlEditor1.resetStatus();
            initPreprocessorAccess();
            ByteBuffer res = fieldModel.getParser().merge(getXml());
            setArray(ParseUtil.extractByteBufferContent(res));
            fail = false;

        } catch (XmlException ex) {
            showException(ex);
        } catch (SmioException ex) {
            showException(ex);
        } catch (SmioError ex) {
            showException(ex);
        } catch (Exception ex) {
            showException(ex);
        }
        finally {
            if(fail)
                xmlEditor1.setStatus(XmlEditor.EXmlEditorStates.EMergeFailure);
            else
                xmlEditor1.setStatus(XmlEditor.EXmlEditorStates.EMergeSuccess);
        }
    }//GEN-LAST:event_jButtonXmlToArrayActionPerformed

    private void Esc(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Esc
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            owner.savePos();
            saveCfg();
            owner.setVisible(false);
            owner.dispose();
        }
    }//GEN-LAST:event_Esc
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.common.design.msdleditor.field.panel.HexEditor hexEditor1;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonFromArrayToXml;
    private javax.swing.JButton jButtonXmlToArray;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSplitPane jSplitPane1;
    private org.radixware.kernel.common.design.msdleditor.field.panel.XmlEditor xmlEditor1;
    // End of variables declaration//GEN-END:variables
}
