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

package org.radixware.kernel.designer.ads.editors.clazz.report;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.windows.TopComponent;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsExportCsvColumn;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;


public class CsvExportColumnDialog extends TopComponent implements ChangeListener {
    private DialogDescriptor dd;
    private final CsvExportColumnPanel itemPanel;
    private boolean ok;
    private Dialog infoDialog;

    public CsvExportColumnDialog(final AdsReportClassDef report,final List<AdsPropertyDef> selectedPropIds) {
        this.ok = false;

        initComponents();
        setLayout(new java.awt.BorderLayout());
        this.itemPanel = new CsvExportColumnPanel(report,selectedPropIds);

        this.add(this.itemPanel, BorderLayout.NORTH);
        this.itemPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent e) {
                CsvExportColumnDialog.this.validate();
                CsvExportColumnDialog.this.repaint();
                if(infoDialog!=null){
                    final int height=itemPanel.getHeight()+30;
                    infoDialog.setSize(526, height);
                    infoDialog.setPreferredSize(new Dimension(526, height));
                    infoDialog.revalidate();
                    infoDialog.repaint();
                }
            } 
        });

        setName("Add export column for CSV"/*NbBundle.getMessage(NewCsvExportColumnDialog.class, "NewItemDialog")*/);
        //setToolTipText(""/*NbBundle.getMessage(NewCsvExportColumnDialog.class, "HINT_NewItemDialog")*/);
    }


    private void initComponents() {

        setAutoscrolls(true);
        setDisplayName("null");
        setMinimumSize(new java.awt.Dimension(526, 164));
        setSize(526, 164);
    }

    public void invokeModalDialog() {

        dd = new DialogDescriptor(this, "Add export column for CSV"/*NbBundle.getMessage(NewCsvExportColumnDialog.class, "NewItemDialog.title")*/);
        dd.setHelpCtx(null);
        dd.setModal(true);

        dd.setButtonListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                ok = evt.getSource() == DialogDescriptor.OK_OPTION;
            }
        });

        infoDialog = DialogDisplayer.getDefault().createDialog(dd);
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

    public AdsExportCsvColumn getSelectedItem(){
       return  itemPanel.getSelectedItem();
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
