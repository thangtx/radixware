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

package org.radixware.kernel.designer.ads.editors.clazz.report.chart;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.windows.TopComponent;


public class ChartSeriesDialog extends TopComponent implements ChangeListener {
    private DialogDescriptor dd;
    private final ChangeSupportPanel itemPanel;
    private  boolean ok;

    public ChartSeriesDialog(final ChangeSupportPanel itemPanel,final String title) {
        this.ok = false;

        initComponents();
        setLayout(new java.awt.BorderLayout());
        this.itemPanel = itemPanel;

        this.add(this.itemPanel, BorderLayout.CENTER);
        this.itemPanel.changeSupport.addChangeListener(this);

        setName(title);
    }

    private void initComponents() {
        setAutoscrolls(true);
        setDisplayName("null");
        setMinimumSize(new java.awt.Dimension(526, 164));
    }

    public void invokeModalDialog() {
        dd = new DialogDescriptor(this,getName());
        dd.setHelpCtx(null);
        dd.setModal(true);

        dd.setButtonListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent evt) {
                ok = evt.getSource() == DialogDescriptor.OK_OPTION;
            }
        });

        final Dialog infoDialog = DialogDisplayer.getDefault().createDialog(dd);
        infoDialog.setMinimumSize(new java.awt.Dimension(526, 164));

        infoDialog.addWindowListener(
                new WindowAdapter() {

                    @Override
                    public void windowActivated(final WindowEvent e) {
                        dd.setValid(itemPanel.isComplete());
                    }
                });

        infoDialog.setVisible(true);
    }

    public boolean isOK() {
        return ok;
    }

    @Override
    public void stateChanged(final ChangeEvent e) {
        dd.setValid(this.itemPanel.isComplete());
    }    
    
    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }   
}
