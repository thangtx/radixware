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

package org.radixware.kernel.designer.common.editors;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.enums.EIsoLanguage;

/**
 *Dialog for creating definitions items
 */
final public class NewItemDialog extends TopComponent implements ChangeListener {

    private final NewItemPanel itemPanel;
    private DialogDescriptor dd;
    private IEnumDef enumDefinition;
    private boolean isOK;
    private final boolean edit;

    public NewItemDialog(AdsEnumDef enumDefinition, AdsEnumItemDef original, final boolean isHexMode) {
        this(enumDefinition, original, isHexMode, false);
    }
    
    public NewItemDialog(AdsEnumDef enumDefinition, AdsEnumItemDef original, final boolean isHexMode, boolean edit) {
        this.enumDefinition = enumDefinition;
        this.isOK = false;

        initComponents();
        setLayout(new java.awt.BorderLayout());
        this.itemPanel = new NewItemPanel(enumDefinition, original, isHexMode, edit);

        this.add(this.itemPanel, BorderLayout.CENTER);
        this.itemPanel.changeSupport.addChangeListener(this);

        this.edit = edit;
        
        setName(edit ? NbBundle.getMessage(NewItemDialog.class, "EditItemDialog") : NbBundle.getMessage(NewItemDialog.class, "NewItemDialog"));
        setToolTipText(NbBundle.getMessage(NewItemDialog.class, "HINT_NewItemDialog"));
    }

    private void initComponents() {

        setAutoscrolls(true);
        setDisplayName("null");
        setMinimumSize(new java.awt.Dimension(526, 164));
        setSize(526, 164);
    }

    public IEnumDef.IItem getCreatedItem() {
        return itemPanel.getCreatedItem();
    }

    public Map<EIsoLanguage, String> getTitles(EIsoLanguage[] languages){
        return itemPanel.getTitles(languages);
    }

    @Override
    public void componentOpened() {
        //do nothing
    }

    @Override
    public void componentClosed() {
        //do nothing
    }

    public void invokeModalDialog() {

        dd = new DialogDescriptor(this, edit ? NbBundle.getMessage(NewItemDialog.class, "EditItemDialog.title") : NbBundle.getMessage(NewItemDialog.class, "NewItemDialog.title"));
        dd.setHelpCtx(null);
        dd.setModal(true);

        dd.setButtonListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                isOK = evt.getSource() == DialogDescriptor.OK_OPTION;
            }
        });

        Dialog infoDialog = DialogDisplayer.getDefault().createDialog(dd);
        infoDialog.setMinimumSize(new java.awt.Dimension(526, 164));

        infoDialog.addWindowListener(
                new WindowAdapter() {

                    @Override
                    public void windowActivated(WindowEvent e) {
                        dd.setValid(itemPanel.isComplete());
                    }
                });

        infoDialog.setVisible(true);
    }

    public boolean isOK() {
        return isOK;
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        dd.setValid(this.itemPanel.isComplete());
    }
}
