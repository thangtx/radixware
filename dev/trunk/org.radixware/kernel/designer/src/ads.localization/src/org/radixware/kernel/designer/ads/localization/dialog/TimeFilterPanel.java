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

package org.radixware.kernel.designer.ads.localization.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Timestamp;
import javax.swing.JLabel;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.designer.ads.editors.common.adsvalasstr.AdsValAsStrEditor;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeEvent;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeListener;


public class TimeFilterPanel extends javax.swing.JPanel {

    /**
     * Creates new form TimeFilterPanel
     */
    public TimeFilterPanel() {
        initComponents();           
    }

    
    public void open(final Timestamp timeFrom,final Timestamp timeTo){
        edTimeFrom.open(getInitialValueController());
        edTimeFrom.setEditable(false);
        edTimeFrom.setValue(createAdsValAsStr(timeFrom));
        
        edTimeTo.open(getInitialValueController());
        edTimeTo.setEditable(false);    
        edTimeTo.setValue(createAdsValAsStr(timeTo));
    }
    
    public void addTimeFromValueChangeListener(ValueChangeListener valueChangeListener){
        edTimeFrom.addValueChangeListener(valueChangeListener);
    }
    
    public void removeTimeFromValueChangeListener(ValueChangeListener valueChangeListener){
        edTimeFrom.removeValueChangeListener(valueChangeListener);
    }
      
    public void addTimeToValueChangeListener(ValueChangeListener valueChangeListener){
        edTimeTo.addValueChangeListener(valueChangeListener);
    }
    
    public void removeTimeToValueChangeListener(ValueChangeListener valueChangeListener){
        edTimeTo.removeValueChangeListener(valueChangeListener);
    }
    
    private AdsValAsStr createAdsValAsStr(final Timestamp time){
        AdsValAsStr sdsValAsStr;
        if(time==null){
            sdsValAsStr= AdsValAsStr.NULL_VALUE;
        }else{           
            final ValAsStr val=ValAsStr.Factory.newInstance(time, EValType.DATE_TIME);            
            sdsValAsStr=AdsValAsStr.Factory.newInstance(val);           
        }
        return sdsValAsStr;
    }
        
    public Timestamp getTimeFrom(){
        if(edTimeFrom.getValue() == null || !edTimeFrom.isSetValue()  || edTimeFrom.getValue().toString().equals("null")){
            return null;
        }
        final String str=edTimeFrom.getValue().toString();
        return Timestamp.valueOf(str.replaceAll("T", " "));
    }
    
    public Timestamp getTimeTo(){
        if(edTimeTo.getValue() == null || !edTimeTo.isSetValue()  || edTimeTo.getValue().toString().equals("null")){
            return null;
        }
        final String str=edTimeTo.getValue().toString();
        return Timestamp.valueOf(str.replaceAll("T", " "));
    }
    
    public boolean isEmpty() {
        return getTimeFrom() == null && getTimeTo() == null;
    }
    
     public boolean isFilerValid(){
        final Timestamp timeFrom=getTimeFrom();
        final Timestamp timeTo=getTimeTo();
        return !(timeFrom!=null && timeTo!=null && timeFrom.after(timeTo));
    }
        
    public final AdsValAsStr.IValueController getInitialValueController() {
        return new AdsValAsStr.IValueController() {

            @Override
            public boolean isValueTypeAvailable(final AdsValAsStr.EValueType type) {
                if(type==AdsValAsStr.EValueType.JML){
                    return false;
                }
                return true;
            }

            @Override
            public AdsTypeDeclaration getContextType() {
                return AdsTypeDeclaration.Factory.newInstance(EValType.DATE_TIME);
            }

            @Override
            public Definition getContextDefinition() {
                return null;
            }

            @Override
            public void setValue(final AdsValAsStr value) {
                //System.out.println(value);
            }

            @Override
            public AdsValAsStr getValue() {
                return null;
            }

            @Override
            public String getValuePresentation() {
                return AdsValAsStr.DefaultPresenter.getAsString(this);
            }
        };
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lbFrom = new javax.swing.JLabel();
        edTimeFrom = new org.radixware.kernel.designer.ads.editors.common.adsvalasstr.AdsValAsStrEditor();
        lbTo = new javax.swing.JLabel();
        edTimeTo = new org.radixware.kernel.designer.ads.editors.common.adsvalasstr.AdsValAsStrEditor();

        setAlignmentX(0.0F);
        setAlignmentY(0.0F);
        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(lbFrom, org.openide.util.NbBundle.getMessage(TimeFilterPanel.class, "TimeFilterPanel.lbFrom.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lbFrom, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(edTimeFrom, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(lbTo, org.openide.util.NbBundle.getMessage(TimeFilterPanel.class, "TimeFilterPanel.lbTo.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(lbTo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(edTimeTo, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.ads.editors.common.adsvalasstr.AdsValAsStrEditor edTimeFrom;
    private org.radixware.kernel.designer.ads.editors.common.adsvalasstr.AdsValAsStrEditor edTimeTo;
    private javax.swing.JLabel lbFrom;
    private javax.swing.JLabel lbTo;
    // End of variables declaration//GEN-END:variables
}
