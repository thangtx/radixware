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

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef.Profile;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPresentationSlotMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIConnection;
import org.radixware.kernel.common.defs.ads.ui.AdsUIConnection.Parameter;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.designer.common.dialogs.RadixObjectRenamePanel;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;


public class ConnectionPanel extends JPanel implements PropertyChangeListener {

    private final PropertyEnv env;
    private final ConnectionEditor editor;
    private ListModel model;
    private final AdsAbstractUIDef uiDef;

    /**
     * Creates new form ConnectionPanel
     */
    public ConnectionPanel(ConnectionEditor editor, final AdsAbstractUIDef uiDef,PropertyEnv env) {
        this.env = env;
        this.editor = editor;
        this.uiDef = uiDef;
        env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
        env.addPropertyChangeListener(this);

        initComponents();

        final List<AdsPresentationSlotMethodDef> handlers = new ArrayList<AdsPresentationSlotMethodDef>();

        final AdsUIConnection c = getConnection();
        
        final AdsModelClassDef classModel = AdsUIUtil.getModelByUI(uiDef, false);
        if (classModel != null) {
            loop:
            for (AdsMethodDef m : classModel.getMethods().getLocal()) {
                if (!(m instanceof AdsPresentationSlotMethodDef)) {
                    continue;
                }
                final Profile profile = m.getProfile();
                if (AdsTypeDeclaration.VOID != profile.getReturnValue().getType()) {
                    continue;
                }
                final List<AdsTypeDeclaration> mt = new ArrayList<AdsTypeDeclaration>();
                for (MethodParameter p : profile.getParametersList()) {
                    mt.add(p.getType());
                }
                final List<AdsTypeDeclaration> st = new ArrayList<AdsTypeDeclaration>();
                for (Parameter p : c.getSignalParams(getWidget())) {
                    st.add(p.getType());
                }

                if (mt.size() != st.size()) {
                    continue;
                } else {
                    for (int i = 0; i < mt.size(); i++) {
                        if (!mt.get(i).equalsTo(uiDef, st.get(i))) {
                            continue loop;
                        }
                    }
                }

                handlers.add((AdsPresentationSlotMethodDef) m);
            }
        }
        model = new ListModel(handlers);

        listHandlers.setModel(model);
        listHandlers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent ev) {
                if (ev.getClickCount() == 2) {
                    int idx = listHandlers.getSelectedIndex();
                    model.rename(idx);
                    update();
                }
            }
        });
        listHandlers.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                update();
            }
        });
        listHandlers.setSelectedIndex(model.getSize() > 0 ? 0 : -1);
        listHandlers.requestFocusInWindow();
        update();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPin = new javax.swing.JScrollPane();
        listHandlers = new javax.swing.JList();
        btnRename = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.inactiveCaption));
        setMinimumSize(new java.awt.Dimension(250, 230));
        setPreferredSize(new java.awt.Dimension(250, 230));
        setRequestFocusEnabled(false);

        listHandlers.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollPin.setViewportView(listHandlers);

        btnRename.setIcon(RadixWareDesignerIcon.EDIT.EDIT.getIcon());
        btnRename.setToolTipText(org.openide.util.NbBundle.getMessage(ConnectionPanel.class, "Connection.btnRename.text")); // NOI18N
        btnRename.setMaximumSize(new java.awt.Dimension(32, 32));
        btnRename.setMinimumSize(new java.awt.Dimension(32, 32));
        btnRename.setPreferredSize(new java.awt.Dimension(32, 32));
        btnRename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenameActionPerformed(evt);
            }
        });

        btnAdd.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        btnAdd.setToolTipText(org.openide.util.NbBundle.getMessage(ConnectionPanel.class, "Connection.btnAdd.text")); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnDel.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        btnDel.setToolTipText(org.openide.util.NbBundle.getMessage(ConnectionPanel.class, "Connection.btnRemove.text")); // NOI18N
        btnDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPin, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRename, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPin, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnRename, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnRenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenameActionPerformed
        // TODO add your handling code here:
        int idx = listHandlers.getSelectedIndex();
        model.rename(idx);
        update();
}//GEN-LAST:event_btnRenameActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        int idx = listHandlers.getSelectedIndex();
        model.add(idx);
        listHandlers.setSelectedIndex(model.getSize() - 1/*Math.max(idx, 0)*/);
        update();
}//GEN-LAST:event_btnAddActionPerformed

    private void btnDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelActionPerformed
        // TODO add your handling code here:
        int idx = listHandlers.getSelectedIndex();
        model.del(idx);
        listHandlers.setSelectedIndex(Math.min(idx, model.getSize() - 1));
        update();
}//GEN-LAST:event_btnDelActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnRename;
    private javax.swing.JList listHandlers;
    private javax.swing.JScrollPane scrollPin;
    // End of variables declaration//GEN-END:variables

    private void update() {
        int idx = listHandlers.getSelectedIndex();
        btnAdd.setEnabled(true);
        btnDel.setEnabled(idx > 0);
        btnRename.setEnabled(idx > 0);
    }

    private AdsUIItemDef getWidget() {
        return ((UIConnectionSupport) editor.getSource()).getWidget();
    }

    private AdsUIConnection getConnection() {
        return (AdsUIConnection) editor.getValue();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (PropertyEnv.PROP_STATE.equals(evt.getPropertyName()) && evt.getNewValue() == PropertyEnv.STATE_VALID) {
            final AdsUIConnection conn = getConnection();
            final AdsUIItemDef w = getWidget();
            int idx = listHandlers.getSelectedIndex();
            if (idx < 0 || idx >= model.getSize()) {
                return;
            }
            
            final AdsPresentationSlotMethodDef m = model.getHandlers().get(idx);
            AdsUIConnection c = uiDef.getConnections().get(w, conn.getSignalName());
            if (m == null) {
                if (c != null) {
                    c.delete();
                }
            } else {
                if (c == null) {
                    c = conn.duplicate();
                    c.setSenderId(w.getId());
                    uiDef.getConnections().add(c);
                }
                c.setSlotId(m.getId());
            }
            editor.setValue(c);
            try {
                ((UIConnectionSupport) editor.getSource()).setValue(c);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    // list model
    private class ListModel extends AbstractListModel {

        private List<AdsPresentationSlotMethodDef> handlers;

        ListModel(List<AdsPresentationSlotMethodDef> handlers) {
            this.handlers = handlers;
            handlers.add(0, null);
        }

        @Override
        public Object getElementAt(int i) {
            if (handlers.get(i) == null) {
                return "<none>";
            }
            return handlers.get(i).getName();
        }

        @Override
        public int getSize() {
            return handlers.size();
        }

        public List<AdsPresentationSlotMethodDef> getHandlers() {
            return handlers;
        }

        void rename(int idx) {
            if (idx > 0 && idx < getSize()) {
                final AdsPresentationSlotMethodDef m = handlers.get(idx);
                if (m != null) {
                    RadixObjectRenamePanel.renameRadixObject(m);
                }
                fireContentsChanged(this, idx, idx);
            }
        }

        void add(int idx) {            
            final AdsModelClassDef classModel = AdsUIUtil.getModelByUI(uiDef, true);
            if (classModel == null) {
                return;
            }

            final AdsPresentationSlotMethodDef method = AdsPresentationSlotMethodDef.Factory.newInstance(getConnection(), getWidget());
            classModel.getMethods().getLocal().add(method);

            handlers.add(method);
            fireIntervalAdded(this, handlers.size() - 1, handlers.size() - 1);
        }

        void del(int idx) {
            if (idx >= 0 && idx < handlers.size() && handlers.size() > 1) {
                AdsPresentationSlotMethodDef handler = handlers.get(idx);
                handlers.remove(handler);
                handler.delete();
                fireIntervalRemoved(this, idx, idx);
            }
        }
    }
}
