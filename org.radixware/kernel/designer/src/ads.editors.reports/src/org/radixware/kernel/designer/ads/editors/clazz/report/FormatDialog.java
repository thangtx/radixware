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
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportFormat;
import org.radixware.kernel.common.enums.EValType;


public class FormatDialog  extends TopComponent  {
    //private DialogDescriptor dd;
    private final FormattedCellPanel formatPanel;
    private boolean ok;
    private Dialog infoDialog;
    
    public FormatDialog(final boolean isReadOnly) {
        this.ok = false;
        initComponents();
        setLayout(new java.awt.BorderLayout());
        this.formatPanel = new FormattedCellPanel(isReadOnly);
        formatPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent e) {        
                FormatDialog.this.validate();
                FormatDialog.this.repaint();
                if(infoDialog!=null){
                    final int height=formatPanel.getHeight()+50;
                    infoDialog.setSize(526, height);
                    infoDialog.setPreferredSize(new Dimension(526, height));
                    infoDialog.revalidate();
                    infoDialog.repaint();
                }
            } 
        });
        this.setBorder(new EmptyBorder(10,10,10,10));
        this.setAutoscrolls(true);
        final JScrollPane jsp = new JScrollPane(formatPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jsp.getViewport().setBorder(null);
        jsp.setViewportBorder(null);
        jsp.setBorder(null);
        add(jsp, BorderLayout.NORTH);
        
        //this.add(this.formatPanel, BorderLayout.CENTER);

        setName(NbBundle.getMessage(FormatDialog.class, "FormatDialog.title"));
    }
    
    void open(final AdsReportFormat format,final EValType type){
        formatPanel.open(format,type);
    }
    
    private void initComponents() {
        setAutoscrolls(true);
        setDisplayName("null");
        setMinimumSize(new java.awt.Dimension(526, 100));
        setSize(526, 100);
    }
     
     public void invokeModalDialog() {
        DialogDescriptor dd = new DialogDescriptor(this, NbBundle.getMessage(FormatDialog.class, "FormatDialog.title"));
        dd.setHelpCtx(null);
        dd.setModal(true);

        dd.setButtonListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent evt) {
                ok = evt.getSource() == DialogDescriptor.OK_OPTION;
            }
        });

        infoDialog = DialogDisplayer.getDefault().createDialog(dd);
        infoDialog.setMinimumSize(new java.awt.Dimension(526, 100));

        /*infoDialog.addWindowListener(
                new WindowAdapter() {

                    @Override
                    public void windowActivated(WindowEvent e) {
                        dd.setValid(formatPanel.isComplete());
                    }
                });*/

        infoDialog.setVisible(true);
    }     

    public boolean isOK() {
        return ok;
    }    
}
