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
package org.radixware.kernel.designer.environment.merge;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import org.openide.actions.CopyAction;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;

public abstract class CopyMergePanel extends JPanel {

    protected CopyMergeDialog dialog;

    protected void setDialog(final CopyMergeDialog dialog) {
        this.dialog = dialog;
    }

//    protected abstract CopyMergeTable getTable();
    protected abstract CopyMergeTable recreateTable();

    protected abstract void openTable(final AbstractMergeChangesOptions options);

    protected abstract void mergeToAnotherBranch();

    protected void setOptions(final AbstractMergeChangesOptions options) {
//        this.options = options;

        if (dialog != null) {
            dialog.setTitle("Merge Changes from \'" + options.getFromBranchShortName() + "' to '" + options.getToBranchShortName() + "\'");
        }

        this.removeAll();
        //table = new AdsCopyMergeTable();
        final CopyMergeTable table = recreateTable();
        final JScrollPane jScrollPane = new JScrollPane();
        this.setLayout(new BorderLayout());

        final JToolBar toolBar = new JToolBar();
        toolBar.setBorderPainted(true);
        toolBar.setFloatable(false);

//        final JButton btAdd = new JButton();
//        toolBar.add(btAdd, 0);
//        btAdd.setIcon(RadixWareIcons.CREATE.ADD.getIcon());
//        btAdd.setToolTipText("Add Definitions");
//        btAdd.setFocusable(false);
//        btAdd.addActionListener(new java.awt.event.ActionListener() {
//            @Override
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                table.addDefinitions();
//            }
//        });
        {

            final JButton btCopyAll = new JButton();
            toolBar.add(btCopyAll, 0);
            btCopyAll.setFocusable(false);
            btCopyAll.setText("Copy All");
            btCopyAll.setIcon(SystemAction.get(CopyAction.class).getIcon());
            btCopyAll.setToolTipText("Copy All Allowed Definitions");
            btCopyAll.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    try {
                        table.copyAll();
                    } catch (Exception ex) {
                        MergeUtils.messageError(ex);
                    }
                }
            });
        }

        {
            final JButton btMergeToAnotherBranch = new JButton();
            toolBar.add(btMergeToAnotherBranch, 0);
            btMergeToAnotherBranch.setFocusable(false);
            btMergeToAnotherBranch.setText("Merge to Another Branch");
            btMergeToAnotherBranch.setIcon(RadixWareIcons.SUBVERSION.UPDATE.getIcon());
            btMergeToAnotherBranch.setToolTipText("Merge to Another Branch");
            btMergeToAnotherBranch.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    mergeToAnotherBranch();
                }
            });
        }

        this.add(toolBar, BorderLayout.NORTH);

        this.add(jScrollPane, BorderLayout.CENTER);
        jScrollPane.add(table);
        jScrollPane.setViewportView(table);

        openTable(options);

        if (dialog != null) {
            final int x = dialog.getDialog().getSize().width;
            final int y = dialog.getDialog().getSize().height;

            dialog.getDialog().resize(x - 1, y);
            dialog.getDialog().resize(x + 1, y);
        }

    }

}
