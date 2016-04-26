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

package org.radixware.kernel.designer.ads.editors.clazz.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import org.radixware.kernel.common.defs.ads.ui.AdsUIActionDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;


class RwtWidgetActionsList extends javax.swing.JPanel {

    private List<AdsUIActionDef> actions = new LinkedList<AdsUIActionDef>();
    private ActionListModel model = new ActionListModel();
    private Map<AdsUIActionDef, ActionSettings> map = new HashMap<AdsUIActionDef, ActionSettings>();
    private boolean isReadOnly;
    private AdsRwtWidgetDef widget;

    private static class ActionSettings {

        private AdsUIActionDef action;
        private String name;
        private String title;

        public ActionSettings(AdsUIActionDef action) {
            this.action = action;
            this.name = action.getName();
        }

        private void apply() {
            action.setName(name);
        }
    }

    private class ActionItem {

        private final AdsUIActionDef action;

        public ActionItem(AdsUIActionDef a) {
            this.action = a;
        }

        @Override
        public String toString() {
            ActionSettings settings = map.get(action);
            if (settings == null) {
                return settings.name;
            }
            return action.getName();
        }
    }

    private class ActionListModel extends DefaultListModel {

        public ActionListModel() {
        }

        public void update() {
            this.clear();
            for (AdsUIActionDef a : actions) {
                this.add(getSize(), new ActionItem(a));
            }
            fireContentsChanged(this, 0, getSize());
        }
    }

    /** Creates new form RwtWidgetActionsList */
    public RwtWidgetActionsList(AdsRwtWidgetDef widget) {
        initComponents();
        this.widget = widget;
        isReadOnly = widget.isReadOnly();
        for (AdsUIActionDef a : widget.getActions()) {
            actions.add(a);
            map.put(a, new ActionSettings(a));
        }
        lstActions.setModel(model);
        btAddAction.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addAction();
            }
        });
        btRemoveAction.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                removeAction();
            }
        });
        if (model.getSize() > 0) {
            lstActions.setSelectedIndex(0);

        }
        selectionChanged();
    }

    private AdsUIActionDef getSelectedAction() {
        int index = lstActions.getSelectedIndex();
        if (index >= 0 && index < actions.size()) {
            return actions.get(index);
        } else {
            return null;
        }
    }

    private void updateEnabled() {

        btAddAction.setEnabled(!isReadOnly);
        AdsUIActionDef current = getSelectedAction();

        btRemoveAction.setEnabled(!isReadOnly && current != null);
        int currentIndex = current == null ? -1 : actions.indexOf(current);
        btMoveDown.setEnabled(!isReadOnly && currentIndex < actions.size() - 1);
        btMoveUp.setEnabled(!isReadOnly && currentIndex > 0);

        if (current != null) {
            edName.setEnabled(true);
            edTitle.setEnabled(true);
            edName.setEditable(!isReadOnly);
            edTitle.setEditable(!isReadOnly);
        } else {
            edName.setEnabled(false);
            edTitle.setEnabled(false);
        }
    }

    private void updateValues() {
        AdsUIActionDef current = getSelectedAction();
        if (current != null) {
            ActionSettings settings = map.get(current);
            edName.setText(settings.name);
            edTitle.setText(settings.title);
        } else {
            edName.setText("");
            edTitle.setText("");
        }
    }

    private void addAction() {
        AdsUIActionDef action = new AdsUIActionDef(Id.Factory.newInstance(EDefinitionIdPrefix.WIDGET), "newAction");
        actions.add(action);
        map.put(action, new ActionSettings(action));
        updateList(action);
    }

    private void removeAction() {
        AdsUIActionDef action = getSelectedAction();
        if (action != null) {
            actions.remove(action);
            map.remove(action);
            updateList(null);
        }
    }

    private void updateList(AdsUIActionDef current) {
        int selection = lstActions.getSelectedIndex();
        if (current == null) {
            if (selection >= 0 && selection < actions.size()) {
                current = actions.get(selection);
            } else if (!actions.isEmpty()) {
                current = actions.get(actions.size() - 1);
            }
        }
        model.update();
        if (current != null) {
            int index = actions.indexOf(current);
            if (index >= 0 && index < model.getSize()) {
                lstActions.setSelectedIndex(index);
            }
        }
        selectionChanged();
    }

    private void selectionChanged() {
        updateEnabled();
        updateValues();
    }

    public void apply() {
        for (AdsUIActionDef a : actions) {
            if (a.getOwnerDef() == null) {
                widget.getActions().add(a);
            }
            ActionSettings s = map.get(a);
            if (s != null) {
                s.apply();
            }
        }
        for (AdsUIActionDef a : widget.getActions()) {
            if (!actions.contains(a)) {
                widget.getActions().remove(a);
            }
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

        jScrollPane1 = new javax.swing.JScrollPane();
        lstActions = new javax.swing.JList();
        jToolBar1 = new javax.swing.JToolBar();
        btAddAction = new javax.swing.JButton();
        btRemoveAction = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btMoveUp = new javax.swing.JButton();
        btMoveDown = new javax.swing.JButton();
        pDetails = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        edName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        edTitle = new javax.swing.JTextField();

        jScrollPane1.setViewportView(lstActions);

        jToolBar1.setBorder(null);
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setBorderPainted(false);

        btAddAction.setText(org.openide.util.NbBundle.getMessage(RwtWidgetActionsList.class, "RwtWidgetActionsList.btAddAction.text")); // NOI18N
        btAddAction.setFocusable(false);
        btAddAction.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btAddAction.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btAddAction);

        btRemoveAction.setText(org.openide.util.NbBundle.getMessage(RwtWidgetActionsList.class, "RwtWidgetActionsList.btRemoveAction.text")); // NOI18N
        btRemoveAction.setFocusable(false);
        btRemoveAction.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btRemoveAction.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btRemoveAction);
        jToolBar1.add(jSeparator1);

        btMoveUp.setText(org.openide.util.NbBundle.getMessage(RwtWidgetActionsList.class, "RwtWidgetActionsList.btMoveUp.text")); // NOI18N
        btMoveUp.setFocusable(false);
        btMoveUp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btMoveUp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btMoveUp);

        btMoveDown.setText(org.openide.util.NbBundle.getMessage(RwtWidgetActionsList.class, "RwtWidgetActionsList.btMoveDown.text")); // NOI18N
        btMoveDown.setFocusable(false);
        btMoveDown.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btMoveDown.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btMoveDown);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(RwtWidgetActionsList.class, "RwtWidgetActionsList.jLabel1.text")); // NOI18N

        edName.setText(org.openide.util.NbBundle.getMessage(RwtWidgetActionsList.class, "RwtWidgetActionsList.edName.text")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(RwtWidgetActionsList.class, "RwtWidgetActionsList.jLabel2.text")); // NOI18N

        edTitle.setText(org.openide.util.NbBundle.getMessage(RwtWidgetActionsList.class, "RwtWidgetActionsList.edTitle.text")); // NOI18N

        javax.swing.GroupLayout pDetailsLayout = new javax.swing.GroupLayout(pDetails);
        pDetails.setLayout(pDetailsLayout);
        pDetailsLayout.setHorizontalGroup(
            pDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pDetailsLayout.createSequentialGroup()
                .addGroup(pDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(edTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                    .addComponent(edName, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)))
        );
        pDetailsLayout.setVerticalGroup(
            pDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pDetailsLayout.createSequentialGroup()
                .addGroup(pDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(edName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(edTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(199, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAddAction;
    private javax.swing.JButton btMoveDown;
    private javax.swing.JButton btMoveUp;
    private javax.swing.JButton btRemoveAction;
    private javax.swing.JTextField edName;
    private javax.swing.JTextField edTitle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JList lstActions;
    private javax.swing.JPanel pDetails;
    // End of variables declaration//GEN-END:variables
}
