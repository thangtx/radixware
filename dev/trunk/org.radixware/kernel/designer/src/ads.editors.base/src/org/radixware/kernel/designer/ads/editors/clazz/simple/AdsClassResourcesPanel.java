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

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;


public class AdsClassResourcesPanel extends javax.swing.JPanel {

    private class ResourceItem {

        private final AdsClassDef.Resources.Resource resource;

        public ResourceItem(AdsClassDef.Resources.Resource resource) {
            this.resource = resource;
        }

        @Override
        public String toString() {
            return resource.getName();
        }
    }

    private class ResourceListModel extends DefaultListModel<ResourceItem> {

        public void update() {
            int index = lstResources.getSelectedIndex();
            clear();
            if (clazz != null) {
                for (AdsClassDef.Resources.Resource res : clazz.getResources()) {
                    this.addElement(new ResourceItem(res));
                }
                if (index < 0) {
                    index = 0;
                }
                if (index >= this.getSize()) {
                    index = this.getSize()-1;
                }
                if (index >= 0) {
                    lstResources.setSelectedIndex(index);
                }
            }
            onSelectionChanged();
        }
    }
    /**
     * Creates new form AdsClassResourcesPanel
     */
    private AdsClassDef clazz;
    private ResourceListModel model = new ResourceListModel();
    private DocumentListener nameListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            sync();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            sync();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            sync();
        }

        private void sync() {
            ResourceItem item = getSelectedItem();
            if (item != null) {
                item.resource.setName(edName.getText());
            }
        }
    };
    private DocumentListener dataListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            sync();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            sync();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            sync();
        }

        private void sync() {
            ResourceItem item = getSelectedItem();
            if (item != null) {
                item.resource.setData(edText.getText());
            }
        }
    };

    public AdsClassResourcesPanel() {
        initComponents();
        model.update();
        lstResources.setModel(model);
        edName.getDocument().addDocumentListener(nameListener);
        edText.getDocument().addDocumentListener(dataListener);
        btAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clazz == null) {
                    return;
                }
                int index = 0;
                String name = "untitled";
                for (AdsClassDef.Resources.Resource res : clazz.getResources()) {
                    if (name.equals(res.getName())) {
                        index++;
                        name = "untitled" + String.valueOf(index);

                    }
                }


                final AdsClassDef.Resources.Resource res = clazz.getResources().new Resource(name);
                clazz.getResources().add(res);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        model.update();
                        for (int i = 0; i < model.getSize(); i++) {
                            if (model.getElementAt(i).resource == res) {
                                lstResources.setSelectedIndex(i);
                                onSelectionChanged();
                                break;
                            }
                        }
                    }
                });
            }
        });
        btRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ResourceItem item = getSelectedItem();
                if (item != null) {
                    clazz.getResources().remove(item.resource);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            model.update();
                        }
                    });
                }
            }
        });
        btAdd.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        btAdd.setToolTipText(btAdd.getText());
        btAdd.setText("");
        btRemove.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        btRemove.setToolTipText(btRemove.getText());
        btRemove.setText("");
        lstResources.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                onSelectionChanged();
            }
        });
        edText.setContentType("text/plain");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitter = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstResources = new javax.swing.JList();
        jToolBar1 = new javax.swing.JToolBar();
        btAdd = new javax.swing.JButton();
        btRemove = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        edName = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        edText = new javax.swing.JEditorPane();

        setLayout(new java.awt.BorderLayout());

        splitter.setDividerLocation(150);

        jPanel3.setPreferredSize(new java.awt.Dimension(300, 461));
        jPanel3.setLayout(new java.awt.BorderLayout());

        lstResources.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(lstResources);

        jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        org.openide.awt.Mnemonics.setLocalizedText(btAdd, org.openide.util.NbBundle.getMessage(AdsClassResourcesPanel.class, "AdsClassResourcesPanel.btAdd.text")); // NOI18N
        btAdd.setFocusable(false);
        jToolBar1.add(btAdd);

        org.openide.awt.Mnemonics.setLocalizedText(btRemove, org.openide.util.NbBundle.getMessage(AdsClassResourcesPanel.class, "AdsClassResourcesPanel.btRemove.text")); // NOI18N
        btRemove.setFocusable(false);
        jToolBar1.add(btRemove);

        jPanel3.add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        splitter.setLeftComponent(jPanel3);

        jPanel1.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(AdsClassResourcesPanel.class, "AdsClassResourcesPanel.jLabel1.text")); // NOI18N

        edName.setText(org.openide.util.NbBundle.getMessage(AdsClassResourcesPanel.class, "AdsClassResourcesPanel.edName.text")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(edName, javax.swing.GroupLayout.DEFAULT_SIZE, 862, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(edName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jScrollPane2.setViewportView(edText);

        jPanel1.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        splitter.setRightComponent(jPanel1);

        add(splitter, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btRemove;
    private javax.swing.JTextField edName;
    private javax.swing.JEditorPane edText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JList lstResources;
    private javax.swing.JSplitPane splitter;
    // End of variables declaration//GEN-END:variables

    public void open(AdsClassDef clazz) {
        this.clazz = clazz;
        update();
    }

    public void update() {
        model.update();

    }

    private void onSelectionChanged() {
        ResourceItem currentItem = updateEnabledState();
        try {
            edName.getDocument().removeDocumentListener(nameListener);
            edText.getDocument().removeDocumentListener(dataListener);
            if (currentItem != null) {
                edText.setText(currentItem.resource.getData());
                edName.setText(currentItem.resource.getName());
            } else {
                edText.setText("");
                edName.setText("");
            }
        } finally {
            edName.getDocument().addDocumentListener(nameListener);
            edText.getDocument().addDocumentListener(dataListener);
        }
    }

    private ResourceItem updateEnabledState() {
        boolean isReadOnly = clazz == null || clazz.isReadOnly();
        ResourceItem item = getSelectedItem();
        btAdd.setEnabled(!isReadOnly);
        btRemove.setEnabled(!isReadOnly && item != null);
        edText.setEditable(!isReadOnly && item != null);
        edName.setEditable(!isReadOnly && item != null);
        return item;
    }

    private ResourceItem getSelectedItem() {
        int index = lstResources.getSelectedIndex();
        if (index >= 0 && index < model.getSize()) {
            return model.get(index);
        } else {
            return null;
        }
    }
}
