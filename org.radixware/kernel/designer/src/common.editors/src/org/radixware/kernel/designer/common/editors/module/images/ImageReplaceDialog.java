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
import java.awt.event.KeyEvent;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.designer.common.dialogs.components.state.IStateDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;


class ImageReplaceDialog extends TopComponent implements ChangeListener {

    private final ImageReplacePanel replacePanel;
    private DialogDescriptor dialogDescriptor = null;
    private Dialog dialog = null;
    private JButton okButton = null;
    private static Rectangle bounds = null;

    protected ImageReplaceDialog(AdsImageDef adsImage) {
        StateDisplayer stateDisplayer = new StateDisplayer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(0, 12, 0, 0));
        panel.add(stateDisplayer, BorderLayout.CENTER);
        IStateDisplayer.Locator.register(stateDisplayer, this);

        replacePanel = new ImageReplacePanel(adsImage);
        replacePanel.addChangeListener(this);
        setLayout(new BorderLayout());
        add(replacePanel, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
        setName(NbBundle.getMessage(ImageReplaceDialog.class, "Replace_Image"));
    }

    public static ImageReplaceDialog getInstanceFor(AdsImageDef adsImage) {
        return new ImageReplaceDialog(adsImage);
    }

    private JPanel getButtonBar() {
        okButton = new JButton("   OK   ") {
            @Override
            public boolean isDefaultButton() {
                return true;
            }
        };
        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                replacePanel.apply();
                dialogDescriptor.setValue(DialogDescriptor.OK_OPTION);
                dialog.setVisible(false);
            }
        });
        okButton.setEnabled(false);

        JButton cancelButton = new JButton(NbBundle.getBundle(ImageConfigureDialog.class).getString("Cancel"));
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dialogDescriptor.setValue(DialogDescriptor.CANCEL_OPTION);
                dialog.setVisible(false);
            }
        });

        ActionListener actionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (okButton.isEnabled())
                    okButton.doClick();
            }
        };
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        this.registerKeyboardAction(actionListener, enter, JComponent.WHEN_IN_FOCUSED_WINDOW);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
//        panel.setBorder(new EmptyBorder(0, 0, 6, 6));
        panel.add(okButton);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(cancelButton);
        return panel;
    }

    public void executeDialog() {
        dialogDescriptor = new DialogDescriptor(this, NbBundle.getMessage(ImageReplaceDialog.class, "Replace_Image"));
        dialogDescriptor.setOptions(new Object[] { getButtonBar() } );
        dialogDescriptor.setHelpCtx(null);
        dialogDescriptor.setValid(true);
        dialog = DialogDisplayer.getDefault().createDialog(dialogDescriptor);
        dialog.setMinimumSize(new Dimension(420, 340));
        if (bounds != null)
            dialog.setBounds(bounds);
        dialog.setVisible(true);
        bounds = dialog.getBounds();
        dialog.dispose();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        super.componentClosed();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        boolean isValid = replacePanel.isComplete();
        if (okButton != null)
            okButton.setEnabled(isValid);
        if (dialogDescriptor != null)
            dialogDescriptor.setValid(isValid);
    }

}
