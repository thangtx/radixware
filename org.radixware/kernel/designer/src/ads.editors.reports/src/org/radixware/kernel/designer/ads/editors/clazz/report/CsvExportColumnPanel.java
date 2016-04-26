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

/*
 * CsvEcportColumnPanel.java
 *
 * Created on Nov 18, 2011, 4:53:28 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.report;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsExportCsvColumn;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportFormat;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;


public class CsvExportColumnPanel extends javax.swing.JPanel {
    private final javax.swing.JLabel propertyLabel = new javax.swing.JLabel(NbBundle.getMessage(CsvExportColumnPanel.class, "CsvExportColumnPanel-PropertyLabel")+":");
    private final javax.swing.JLabel extNameLabel = new javax.swing.JLabel(NbBundle.getMessage(CsvExportColumnPanel.class, "CsvExportColumnPanel-ExternalName-Label")+":");
    private final DefinitionLinkEditPanel propTextField = new DefinitionLinkEditPanel();
    private final javax.swing.JTextField extNameTextField = new javax.swing.JTextField();
    private final javax.swing.JPanel content = new javax.swing.JPanel();
    private final FormattedCellPanel formatPanel ;
    private final StateManager stateManager;
    private final AdsReportFormat format;
    private ChangeSupport changeSupport = new ChangeSupport(this);
    
    /** Creates new form CsvEcportColumnPanel */
    public CsvExportColumnPanel(final AdsReportClassDef report,final List<AdsPropertyDef> selectedProps) {
        this.stateManager = new StateManager(content);
        
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        content.setLayout(gbl);

        setupVerifiers();

        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;

        gbl.setConstraints(propertyLabel, c);
        content.add(propertyLabel);

        c.insets = new Insets(10, 0, 10, 10);
        c.gridx = 1;
        c.weightx = 1.0;

        gbl.setConstraints(propTextField, c);
        content.add(propTextField);
        propTextField.setComboMode();
        propTextField.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(format!=null && formatPanel!=null){
                    AdsPropertyDef prop=(AdsPropertyDef)propTextField.getDefinition();
                    formatPanel.open(format, prop.getValue().getType().getTypeId());
                }
            } 
        });
        
        List<AdsPropertyDef> list=new ArrayList<>();
        List<AdsPropertyDef> allProps =report.getProperties().get(EScope.LOCAL);
        for(AdsPropertyDef prop:allProps){
            if((prop.getNature()==EPropNature.DYNAMIC || prop.getNature()==EPropNature.FIELD ||
                prop.getNature()==EPropNature.SQL_CLASS_PARAMETER ) && !selectedProps.contains(prop)){
                list.add(prop);
            }
        }
        RadixObjectsUtils.sortByName(list);
        propTextField.setComboBoxValues(list, false);
        
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 10, 10, 10);
        c.weightx = 0.0;
        gbl.setConstraints(extNameLabel, c);
        content.add(extNameLabel);

        c.insets = new Insets(0, 0, 10, 10);
        c.gridx = 1;
        c.weightx = 1.0;
        gbl.setConstraints(extNameTextField, c);
        content.add(extNameTextField);
        
        
        format=new AdsReportFormat();
        formatPanel=new  FormattedCellPanel(format, null,  report.isReadOnly());
        formatPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                changeSupport.fireChange();
            } 
        });
        c.insets = new Insets(0, 10, 10, 10);
        c.gridy = 2;
        c.gridx = 0;
        c.weightx = 1.0;
        c.gridwidth = 2;
        gbl.setConstraints(formatPanel, c);
        content.add(formatPanel);
                
        if(!list.isEmpty()){
            propTextField.open(list.get(0), list.get(0).getId());
            AdsPropertyDef prop=(AdsPropertyDef)propTextField.getDefinition();
            if(prop!=null)
               formatPanel.open(format, prop.getValue().getType().getTypeId());
        }
        setLayout(new BorderLayout());
        JScrollPane jsp = new JScrollPane(content,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jsp.getViewport().setBorder(null);
        jsp.setViewportBorder(null);
        jsp.setBorder(null);
        add(jsp, BorderLayout.NORTH);
    }
    
    public AdsExportCsvColumn getSelectedItem(){
        AdsPropertyDef prop=(AdsPropertyDef)propTextField.getDefinition();
        if(prop!=null){
            String extName=extNameTextField.getText();
            return new AdsExportCsvColumn (prop.getId(), extName, format);
        }
        return null;
    }
    
        
    private void setupVerifiers() {

        propTextField.setInputVerifier(new InputVerifier() {

            @Override
            public boolean verify(JComponent input) {
                return propTextField.getDefinitionId()!=null;
            }
        });
    }

    public final void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }
    
     public boolean isComplete() {
            stateManager.ok();
            return true;

    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
