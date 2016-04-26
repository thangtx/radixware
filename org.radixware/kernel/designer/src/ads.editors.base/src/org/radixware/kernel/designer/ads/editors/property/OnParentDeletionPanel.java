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

package org.radixware.kernel.designer.ads.editors.property;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.ads.clazz.members.ParentDeletionOptions;
import org.radixware.kernel.common.enums.EDeleteMode;


public class OnParentDeletionPanel extends JPanel {

    private EDeleteMode deleteMode;
    private final ChangeSupport changeSupport = new ChangeSupport(this);
    private boolean shouldFireEvents = true;
    private final ButtonGroup buttonGroup;
    private final JCheckBox cbConfirm;

    public OnParentDeletionPanel() {
        setBorder(BorderFactory.createTitledBorder("On parent deletion"));
        setLayout(new MigLayout());
        buttonGroup = new ButtonGroup();
        final ActionListener actionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JRadioButton) {
                    deleteMode = (EDeleteMode) ((JRadioButton) e.getSource()).getClientProperty(EDeleteMode.class);
                    updateConfirmCheckBox();
                }
                if (shouldFireEvents) {
                    changeSupport.fireChange();
                }
            }
        };
        for (EDeleteMode mode : EDeleteMode.values()) {
            if (mode == EDeleteMode.CASCADE) {
                continue;//delete cascade is not supported yet
            }
            final JRadioButton radioButton = new JRadioButton();
            switch (mode) {
                case NONE:
                    radioButton.setText("None");
                    break;
                case REMOVE_VALUE:
                    radioButton.setText("Remove value");
                    break;
                case RESTRICT:
                    radioButton.setText("Restrict");
                    break;
                case SET_NULL:
                    radioButton.setText("Set null");
                    break;
            }
            radioButton.putClientProperty(EDeleteMode.class, mode);
            radioButton.addActionListener(actionListener);
            buttonGroup.add(radioButton);
            add(radioButton);
        }
        cbConfirm = new JCheckBox("Confirm children modification");
        cbConfirm.addActionListener(actionListener);
        add(cbConfirm);

    }

    private void updateConfirmCheckBox() {
        cbConfirm.setEnabled(deleteMode == EDeleteMode.SET_NULL || deleteMode == EDeleteMode.REMOVE_VALUE);
    }

    public void addChangeListener(final ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    public ParentDeletionOptions getOptions() {
        return new ParentDeletionOptions(deleteMode == null ? EDeleteMode.NONE : deleteMode, cbConfirm.isSelected());
    }

    public void setOptions(ParentDeletionOptions options) {
        shouldFireEvents = false;
        try {
            cbConfirm.setSelected(options == null ? false : options.isConfirmationRequired());
            this.deleteMode = options == null ? null : options.getDeleteMode();
            updateConfirmCheckBox();
            buttonGroup.clearSelection();
            for (Component comp : getComponents()) {
                if (comp instanceof JRadioButton
                        && (((JRadioButton) comp).getClientProperty(EDeleteMode.class) == deleteMode
                        || (deleteMode == null && ((JRadioButton) comp).getClientProperty(EDeleteMode.class) == EDeleteMode.NONE))) {
                    ((JRadioButton) comp).setSelected(true);
                    return;
                }
            }
        } finally {
            shouldFireEvents = true;
        }
    }

    public void setEditable(final boolean editable) {
        for (Component component : getComponents()) {
            if (component instanceof JRadioButton || component instanceof JCheckBox) {
                ((JComponent) component).setEnabled(editable);
            }
        }
    }
}
