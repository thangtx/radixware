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
 * PublishingPropertyPanel.java
 *
 * Created on May 18, 2012, 4:19:26 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.members.transparent;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.resources.RadixWareIcons;


final class PublishingClassMemberPanel extends javax.swing.JPanel {

    private final EClassMemberType classMemberType;
    private JTextField txtName;

    public PublishingClassMemberPanel(EClassMemberType classMemberType) {
        initComponents();

        this.classMemberType = classMemberType;

        initChooseMemberPanel();
    }

    private void initChooseMemberPanel() {
        chooseMemberPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        chooseMemberPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipadx = 4;
        chooseMemberPanel.add(new JLabel(NbBundle.getMessage(PublishingClassMemberPanel.class,
            "PublishingClassMemberPanel-Description") + " " + classMemberType + ":"), constraints);

        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 1;
        final String initValue = "<" + NbBundle.getMessage(PublishingClassMemberPanel.class,
            "PublishingClassMemberPanel-NameLabel") + " " + classMemberType + ">";
        txtName = new JTextField(initValue);
        txtName.setEnabled(false);
        txtName.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1 && SwingUtilities.isLeftMouseButton(e)) {
                    choose();
                }
            }

        });
        chooseMemberPanel.add(txtName, constraints);

        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.weighty = 1;
        final int size = 25;
        final JButton btnChoose = new JButton(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        btnChoose.setPreferredSize(new Dimension(size, size));
        btnChoose.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                choose();
            }

        });
        chooseMemberPanel.add(btnChoose, constraints);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chooseMemberPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());
        add(chooseMemberPanel, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel chooseMemberPanel;
    // End of variables declaration//GEN-END:variables
    private AdsClassDef ownerClass;

    public void open(AdsClassDef ownerClass) {
        this.ownerClass = ownerClass;
    }

    public ClassMemberItem getSelectedMember() {
        return selectedMember;
    }

    private final ChangeSupport changeSupport = new ChangeSupport(this);

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    private ClassMemberItem selectedMember;

    private void setValue(ClassMemberItem value) {
        selectedMember = value;
        if (selectedMember != null) {
            txtName.setText(selectedMember.getInfo().getName());
            txtName.setToolTipText(selectedMember.getInfo().getHtmlName());
            changeSupport.fireChange();
        }
    }

    private void choose() {
        if (ownerClass != null) {
            final PuplishClassMemberDisplayer displayer = new PuplishClassMemberDisplayer();
            displayer.open(ownerClass, classMemberType);

            if (displayer.showModal()) {
                setValue(displayer.getItem());
            }
        }
    }

}
