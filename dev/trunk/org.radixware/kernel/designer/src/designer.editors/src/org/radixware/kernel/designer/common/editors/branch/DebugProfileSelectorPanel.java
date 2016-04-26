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
 * DebugProfilePanel.java
 *
 * Created on Jan 27, 2012, 8:46:48 AM
 */
package org.radixware.kernel.designer.common.editors.branch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


final class DebugProfileSelectorPanel extends javax.swing.JPanel {

    public DebugProfileSelectorPanel() {
        initComponents();
        cboxProfileList.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                model.setProfileName(getSelectedProfileName());
            }
        });
    }
    private IProfileSelectorModel model;

    public void open(IProfileSelectorModel model) {
        this.model = model;
        if (model != null) {
            cboxProfileList.setModel(new ProfileListModel(model.getProfileNameList()));
            setCurrProfile(model.getProfileName());
        }
    }

    private void duplicateProfile() {
        String newProfileName = model.duplicateProfile();

        if (newProfileName != null && !newProfileName.isEmpty()) {
            cboxProfileList.addItem(newProfileName);
            cboxProfileList.setSelectedItem(newProfileName);
        }
    }

    private void removeProfile() {
        if (model.removeProfile()) {
            cboxProfileList.removeItem(getSelectedProfileName());
        }
    }

    public String getSelectedProfileName() {
        return (String) cboxProfileList.getSelectedItem();
    }

    void setCurrProfile(String name) {
        cboxProfileList.setSelectedItem(name);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        cboxProfileList = new javax.swing.JComboBox();
        btnDuplicate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));

        jLabel1.setText(org.openide.util.NbBundle.getMessage(DebugProfileSelectorPanel.class, "DebugProfileSelectorPanel.jLabel1.text")); // NOI18N

        btnDuplicate.setText(org.openide.util.NbBundle.getMessage(DebugProfileSelectorPanel.class, "DebugProfileSelectorPanel.btnDuplicate.text")); // NOI18N
        btnDuplicate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDuplicateActionPerformed(evt);
            }
        });

        btnDelete.setText(org.openide.util.NbBundle.getMessage(DebugProfileSelectorPanel.class, "DebugProfileSelectorPanel.btnDelete.text")); // NOI18N
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboxProfileList, 0, 193, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDuplicate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelete))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(cboxProfileList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnDelete)
                .addComponent(btnDuplicate))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnDuplicateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDuplicateActionPerformed
        duplicateProfile();
    }//GEN-LAST:event_btnDuplicateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        removeProfile();
    }//GEN-LAST:event_btnDeleteActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDuplicate;
    private javax.swing.JComboBox cboxProfileList;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

    private static final class ProfileListModel implements MutableComboBoxModel {

        private final EventListenerList listenerList = new EventListenerList();
        private final List<String> profiles;
        private String select;

        public ProfileListModel(List<String> profiles) {
            this.profiles = new ArrayList<String>(profiles);
        }

        @Override
        public void addElement(Object obj) {
            insertElementAt(obj, getSize());
        }

        @Override
        public void removeElement(Object obj) {
            removeElementAt(profiles.indexOf(obj));
        }

        @Override
        public void insertElementAt(Object obj, int index) {
            profiles.add(index, (String) obj);
            fireListDataChanged(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index));
        }

        @Override
        public void removeElementAt(int index) {
            profiles.remove(index);
            if (index > 0) {
                setSelectedItem(profiles.get(index - 1));
            } else if (!profiles.isEmpty()) {
                setSelectedItem(profiles.get(index));
            }
            fireListDataChanged(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index, index));
        }

        @Override
        public void setSelectedItem(Object item) {
            select = (String) item;
        }

        @Override
        public String getSelectedItem() {
            return select;
        }

        @Override
        public int getSize() {
            return profiles.size();
        }

        @Override
        public String getElementAt(int index) {
            return profiles.get(index);
        }

        @Override
        public void addListDataListener(ListDataListener l) {
            listenerList.add(ListDataListener.class, l);
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
            listenerList.remove(ListDataListener.class, l);
        }

        void fireListDataChanged(ListDataEvent event) {
            for (ListDataListener listener : listenerList.getListeners(ListDataListener.class)) {
                listener.contentsChanged(event);
            }
        }
    }
}

interface IProfileSelectorModel {

    List<String> getProfileNameList();

    String getProfileName();

    void setProfileName(String name);

    boolean removeProfile();

    String duplicateProfile();
}