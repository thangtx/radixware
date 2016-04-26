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

package org.radixware.kernel.designer.common.dialogs.wizards.newobject;

import java.awt.Component;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;

public final class CreationWizardStartStepPanel extends JPanel {

    private class CategoryListItem {

        private ObjectListModel model = null;
        private ICreatureGroup context;

        CategoryListItem(ICreatureGroup context) {
            this.context = context;
        }

        @Override
        public String toString() {
            return context.getDisplayName();
        }

        private synchronized ObjectListModel getObjectModel() {
            if (model == null) {
                model = new ObjectListModel(this);
            }
            return model;
        }
    }

    private class CategoryListModel extends DefaultListModel {

        CategoryListModel() {
            super();
            for (int i = 0; i < step.categories.length; i++) {
                if (step.categories[i] != null) {
                    this.addElement(new CategoryListItem(step.categories[i]));
                }
            }

        }
    }

    private class ObjectListItem {

        private ICreature creature;

        private ObjectListItem(ICreature creature) {
            this.creature = creature;
        }

        @Override
        public String toString() {
            return this.creature.getDisplayName();
        }
    }

    private class ObjectListModel extends DefaultListModel {

        ObjectListModel(CategoryListItem categoryItem) {
            List<ICreature> creatures = categoryItem.context.getCreatures();

            if (creatures != null) {
                for (ICreature c : creatures) {
                    if (c != null) {
                        this.addElement(new ObjectListItem(c));
                    }
                }
            }
        }

        private int findCreature(ICreature c) {
            for (int i = 0; i < this.size(); i++) {
                ObjectListItem item = (ObjectListItem) this.get(i);
                if (item.creature == c) {
                    return i;
                }
            }
            return -1;
        }
    }
    private int lastSelectedCategory = 0;

    void open(final CreationWizardStartStep step) {
        this.step = step;
        this.current = step.getCurrent();
        this.lstCategory.setModel(new CategoryListModel());
        this.lstObjectType.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                int index = lstObjectType.getSelectedIndex();


                if (index >= 0 && index < lstObjectType.getModel().getSize()) {
                    final ObjectListItem item = (ObjectListItem) lstObjectType.getModel().getElementAt(index);
                    if (item != null) {
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                step.setCurrent(item.creature);
                                String description = item.creature.getDescription();
                                if (description != null) {
                                    edDescription.setText(description);
                                } else {
                                    edDescription.setText("");
                                }

                            }
                        });
                    }
                }
            }
        });
        this.lstCategory.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = lstCategory.getSelectedIndex();


                if (index >= 0 && index < lstCategory.getModel().getSize()) {
                    CategoryListItem item = (CategoryListItem) lstCategory.getModel().getElementAt(index);
                    if (item != null) {
                        lstObjectType.setModel(item.getObjectModel());
                        if (item.getObjectModel().getSize() > 0) {
                            SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    int index = 0;
                                    if (current != null) {
                                        index = ((ObjectListModel) lstObjectType.getModel()).findCreature(current);
                                    } else {
                                        index = 0;
                                    }
                                    if (index >= 0 && index < lstObjectType.getVisibleRowCount()) {
                                        lstObjectType.setSelectedIndex(index);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
        if (lstCategory.getModel().getSize() > 0) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    lstCategory.setSelectedIndex(0);
                }
            });

        }
    }
    private CreationWizardStartStep step;
    private ICreature current = null;
    private ChangeSupport changeSupport = new ChangeSupport(this);

    /** Creates new form StartupPanelVisual */
    CreationWizardStartStepPanel(CreationWizardStartStep step) {
        initComponents();
        this.step = step;

        final DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
        lstObjectType.setCellRenderer(new ListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {


                JLabel label = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ObjectListItem) {
                    ObjectListItem item = (ObjectListItem) value;
                    RadixIcon icon = item.creature.getIcon();
                    if (icon != null) {
                        label.setIcon(icon.getIcon(16, 16));
                    }

                }

                return label;
            }
        });

    }

    @Override
    public String getName() {
        return NbBundle.getMessage(CreationWizardStartStepPanel.class, "Startup-Panel-Name");
    }

    public void addChangeListener(ChangeListener l) {
        this.changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        this.changeSupport.removeChangeListener(l);
    }

    synchronized ICreature getCurrent() {
        return current;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        objectPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lbCategory = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstCategory = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstObjectType = new javax.swing.JList();
        lbType = new javax.swing.JLabel();
        descriptionPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        edDescription = new javax.swing.JTextArea();
        lbDescription = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout(4, 4));

        objectPanel.setPreferredSize(new java.awt.Dimension(400, 200));
        objectPanel.setLayout(new java.awt.GridLayout(1, 0, 4, 4));

        org.openide.awt.Mnemonics.setLocalizedText(lbCategory, org.openide.util.NbBundle.getMessage(CreationWizardStartStepPanel.class, "CreationWizardStartStepPanel.lbCategory.text")); // NOI18N

        lstCategory.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(lstCategory);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbCategory, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(lbCategory)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
        );

        objectPanel.add(jPanel1);

        lstObjectType.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lstObjectType);

        org.openide.awt.Mnemonics.setLocalizedText(lbType, org.openide.util.NbBundle.getMessage(CreationWizardStartStepPanel.class, "CreationWizardStartStepPanel.lbType.text")); // NOI18N
        lbType.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lbType.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
            .addComponent(lbType, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(lbType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
        );

        objectPanel.add(jPanel2);

        add(objectPanel, java.awt.BorderLayout.CENTER);

        descriptionPanel.setPreferredSize(new java.awt.Dimension(400, 120));

        edDescription.setColumns(20);
        edDescription.setEditable(false);
        edDescription.setLineWrap(true);
        jScrollPane3.setViewportView(edDescription);

        org.openide.awt.Mnemonics.setLocalizedText(lbDescription, org.openide.util.NbBundle.getMessage(CreationWizardStartStepPanel.class, "CreationWizardStartStepPanel.lbDescription.text")); // NOI18N

        javax.swing.GroupLayout descriptionPanelLayout = new javax.swing.GroupLayout(descriptionPanel);
        descriptionPanel.setLayout(descriptionPanelLayout);
        descriptionPanelLayout.setHorizontalGroup(
            descriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(descriptionPanelLayout.createSequentialGroup()
                .addComponent(lbDescription)
                .addContainerGap(321, Short.MAX_VALUE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
        );
        descriptionPanelLayout.setVerticalGroup(
            descriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(descriptionPanelLayout.createSequentialGroup()
                .addComponent(lbDescription)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
        );

        add(descriptionPanel, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel descriptionPanel;
    private javax.swing.JTextArea edDescription;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbCategory;
    private javax.swing.JLabel lbDescription;
    private javax.swing.JLabel lbType;
    private javax.swing.JList lstCategory;
    private javax.swing.JList lstObjectType;
    private javax.swing.JPanel objectPanel;
    // End of variables declaration//GEN-END:variables
}

