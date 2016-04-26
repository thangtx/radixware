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

package org.radixware.kernel.designer.common.editors.module.images;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.radixware.kernel.common.enums.ERadixIconType;


public class ChooseTypeDialog extends TopComponent {

    private final ChooseTypePanel chooseTypePanel;
    private DialogDescriptor dialogDescriptor = null;
    private Dialog dialog = null;
    private ERadixIconType imageType = null;
    private static Rectangle bounds = null;

    protected ChooseTypeDialog() {
        chooseTypePanel = new ChooseTypePanel();
        setLayout(new BorderLayout());
        add(chooseTypePanel, BorderLayout.CENTER);
    }

    public ERadixIconType chooseType() {
        dialogDescriptor = new DialogDescriptor(this, NbBundle.getMessage(ChooseTypeDialog.class, "Choose_Type"));
        dialogDescriptor.setOptions(new Object[] { getOkCancelButtonBar() } );
        dialogDescriptor.setHelpCtx(null);
        dialogDescriptor.setValid(true);
        dialog = DialogDisplayer.getDefault().createDialog(dialogDescriptor);
        dialog.setPreferredSize(new Dimension(320, 240));
        dialog.setResizable(false);
        if (bounds != null)
            dialog.setBounds(bounds);
        dialog.setVisible(true);
        bounds = dialog.getBounds();
        if (dialogDescriptor.getValue().equals(DialogDescriptor.OK_OPTION)) {
            dialog.dispose();
            return imageType;
        }
        dialog.dispose();
        return null;
    }

    private JPanel getOkCancelButtonBar() {
        JButton okButton = new JButton("   OK   ") {

            @Override
            public boolean isDefaultButton() {
                return true;
            }
        };
        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                imageType = chooseTypePanel.getImageType();
                dialogDescriptor.setValue(DialogDescriptor.OK_OPTION);
                dialog.setVisible(false);
            }
        });

        JButton cancelButton = new JButton(NbBundle.getBundle(ChooseTypeDialog.class).getString("Cancel"));
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dialogDescriptor.setValue(DialogDescriptor.CANCEL_OPTION);
                dialog.setVisible(false);
            }
        });

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
//        panel.setBorder(new EmptyBorder(0, 0, 6, 6));
        panel.add(okButton);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(cancelButton);
        return panel;
    }

    public static class Factory {

        public static ChooseTypeDialog newInstance() {
            return new ChooseTypeDialog();
        }

    }

}
