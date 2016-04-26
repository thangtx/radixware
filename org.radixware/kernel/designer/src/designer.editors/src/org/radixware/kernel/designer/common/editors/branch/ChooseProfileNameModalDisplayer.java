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

package org.radixware.kernel.designer.common.editors.branch;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.DialogDescriptor;
import org.openide.util.NbBundle;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


final class ChooseProfileNameModalDisplayer extends ModalDisplayer {

    public interface INameValidator {

        boolean valid(String name);
    }

    public ChooseProfileNameModalDisplayer(INameValidator nameValidator) {
        super(new ProfileNamePanel(nameValidator));

        getDialogDescriptor().setValid(false);
        getComponent().open(getDialogDescriptor());
        
        String title = NbBundle.getMessage(ChooseProfileNameModalDisplayer.class, "ChooseProfileNameModalDisplayer.Title");
        setTitle(title);
    }

    public String getProfileName() {
        return getComponent().getProfileName();
    }

    @Override
    public ProfileNamePanel getComponent() {
        return (ProfileNamePanel) super.getComponent();
    }

    private static final class ProfileNamePanel extends JPanel {

        StateManager stateManager;
        JTextField txtProfileName;
        INameValidator nameValidator;
        StateDisplayer displayer = new StateDisplayer();
        DialogDescriptor descriptor;

        ProfileNamePanel(INameValidator nameValidator) {
            super();

            this.stateManager = new StateManager(this);
            this.nameValidator = nameValidator;

            stateManager.ok();

            setLayout(new BorderLayout());

            JPanel namePanel = new JPanel();
            namePanel.setBorder(BorderFactory.createEmptyBorder(10, 4, 4, 4));
            namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));

            String lblProfileNameText = NbBundle.getMessage(ChooseProfileNameModalDisplayer.class, "LabelProfileName.Text");
            JLabel lblProfileName = new JLabel(lblProfileNameText);
            lblProfileName.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
            namePanel.add(lblProfileName);

            txtProfileName = new JTextField();
            namePanel.add(txtProfileName);

            add(namePanel, BorderLayout.PAGE_START);
            
            displayer.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
            add(displayer, BorderLayout.PAGE_END);

            txtProfileName.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    changedUpdate(e);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    changedUpdate(e);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    if (ProfileNamePanel.this.nameValidator.valid(txtProfileName.getText())) {
                        stateManager.ok();
                        descriptor.setValid(true);
                    } else {
                        String errMessage;
                        if (txtProfileName.getText().isEmpty()) {
                            errMessage = NbBundle.getMessage(ChooseProfileNameModalDisplayer.class, "StateDisplayer.ProfileName.EmptyName");
                        } else {
                            errMessage = NbBundle.getMessage(ChooseProfileNameModalDisplayer.class, "StateDisplayer.ProfileName.InvalidName");
                        }
                        stateManager.error(errMessage);
                        descriptor.setValid(false);
                    }
                }
            });
        }
        
        @Override
        public void requestFocus() {
            txtProfileName.requestFocus();
        }
        
        private void open(DialogDescriptor descriptor) {
            this.descriptor = descriptor;
        }

        private String getProfileName() {
            return txtProfileName.getText();
        }
    }
}