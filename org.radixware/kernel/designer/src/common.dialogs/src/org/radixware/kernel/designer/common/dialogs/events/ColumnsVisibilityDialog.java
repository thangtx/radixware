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

package org.radixware.kernel.designer.common.dialogs.events;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;


public class ColumnsVisibilityDialog extends TopComponent {

    private static ColumnsVisibilityDialog columnsVisibilityDialog = null;
    private static DialogDescriptor dialogDescriptor;
    private static Dialog dialog;
    private static Point location = null;

    private final ColumnsVisibilityPanel columnsVisibilityPanel;
    private final JButton okButton;
    private final JButton cancelButton;

    protected ColumnsVisibilityDialog(TraceTable traceTable) {

        okButton = new JButton("   OK   ") {
            @Override
            public boolean isDefaultButton() {
                return true;
            }
        };
        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                columnsVisibilityPanel.apply();
                dialogDescriptor.setValue(DialogDescriptor.OK_OPTION);
                dialog.setVisible(false);
            }
        });

        cancelButton = new JButton(NbBundle.getBundle(ColumnsVisibilityDialog.class).getString("Cancel"));
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                columnsVisibilityPanel.restore();
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

        columnsVisibilityPanel = new ColumnsVisibilityPanel(this, traceTable);
        this.setLayout(new BorderLayout());
        this.add(columnsVisibilityPanel, BorderLayout.CENTER);

    }

    private JPanel getButtonBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
//        panel.setBorder(new EmptyBorder(0, 0, 6, 6));
        panel.add(okButton);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(cancelButton);
        return panel;
    }

    public static void show(TraceTable traceTable) {
        if (columnsVisibilityDialog == null)
            columnsVisibilityDialog = new ColumnsVisibilityDialog(traceTable);
        dialogDescriptor = new DialogDescriptor(columnsVisibilityDialog, NbBundle.getMessage(ColumnsVisibilityDialog.class, "COL_VISIBILITY"));
        dialogDescriptor.setOptions(new Object[] { columnsVisibilityDialog.getButtonBar() } );
        dialogDescriptor.setHelpCtx(null);
        dialogDescriptor.setValid(true);
        dialog = DialogDisplayer.getDefault().createDialog(dialogDescriptor);
        dialog.pack();
        dialog.setResizable(false);
        dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowActivated(WindowEvent e) {
                columnsVisibilityDialog.columnsVisibilityPanel.restore();
                super.windowActivated(e);
            }
        });
        if (location != null)
            dialog.setLocation(location);
        dialog.setVisible(true);
        location = dialog.getLocation();
        dialog.dispose();
    }

    public void setOkButtonEnabled(boolean enabled) {
        okButton.setEnabled(enabled);
    }

}
