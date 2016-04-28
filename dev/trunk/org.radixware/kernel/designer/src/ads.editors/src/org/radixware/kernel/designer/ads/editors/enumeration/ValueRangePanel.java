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
 * ValueRangePanel.java
 *
 * Created on May 5, 2009, 5:11:16 PM
 */

package org.radixware.kernel.designer.ads.editors.enumeration;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ads.enumeration.ValueRange;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;


public class ValueRangePanel extends StateAbstractDialog.StateAbstractPanel {

    protected EValType eValType;

    /** Creates new form ValueRangePanel */
    public ValueRangePanel(EValType eValType) {
        super();
        this.eValType = eValType;
        initComponents();

        minValueTextField.setDefaultValue(eValType);
        maxValueTextField.setDefaultValue(eValType);
        ChangeListener listener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                check();
            }
        };

        minValueTextField.addChangeListener(listener);
        maxValueTextField.addChangeListener(listener);

        check();
    }

    @Override
    public final void check() {

        final String fromStr = minValueTextField.getValue().toString();
        final String toStr = maxValueTextField.getValue().toString();

        final ValAsStr from =  ValAsStr.Factory.loadFrom(fromStr);
        final ValAsStr to = ValAsStr.Factory.loadFrom(toStr);

        boolean result = false;

        if (from != null && to != null){
            if (eValType == EValType.INT){
                Object fromObj = from.toObject(eValType);
                Object toObj = to.toObject(eValType);

                if (fromObj == null || toObj == null){
                    result = false;
                } else {
                    result = (Long) fromObj <=  (Long) toObj;
                }
            }else if (eValType == EValType.CHAR){
                result = fromStr.length() == 1 && toStr.length() == 1 && (Character) from.toObject(eValType) <= (Character) to.toObject(eValType);
            }else if (eValType == EValType.STR){
                final Object fromObject = from.toObject(eValType);
                final Object toObject = to.toObject(eValType);

                result = fromObject != null && toObject != null &&
                    ( (String) fromObject).compareTo( (String) toObject) <= 0;
            }else{
                stateManager.error("Wrong EValType " + eValType.toString());
                changeSupport.fireChange();
                return;
            }
        }

        if (!result){
            stateManager.error("Wrong value range");
        }else{
            stateManager.ok();
        }

        changeSupport.fireChange();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        minValueLabel = new javax.swing.JLabel();
        maxValueLabel = new javax.swing.JLabel();
        stateDisplayer1 = new org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer();
        minValueTextField = new org.radixware.kernel.designer.common.dialogs.components.ValAsStrEditPanel();
        maxValueTextField = new org.radixware.kernel.designer.common.dialogs.components.ValAsStrEditPanel();

        setMinimumSize(new java.awt.Dimension(10, 80));

        minValueLabel.setText(org.openide.util.NbBundle.getMessage(ValueRangePanel.class, "ValueRangePanel.minValueLabel.text")); // NOI18N

        maxValueLabel.setText(org.openide.util.NbBundle.getMessage(ValueRangePanel.class, "ValueRangePanel.maxValueLabel.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(maxValueLabel)
                    .addComponent(minValueLabel))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(stateDisplayer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(minValueTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                        .addComponent(maxValueTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)))
                .addGap(9, 9, 9))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(minValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(minValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maxValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maxValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stateDisplayer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    public ValueRange getCreatedValueRange(){
         return new ValueRange(ValAsStr.Factory.newInstance(minValueTextField.getValue().toString(), eValType), ValAsStr.Factory.newInstance(maxValueTextField.getValue().toString(), eValType));
    }

    @Override
    public boolean requestFocusInWindow() {
        return minValueTextField.requestFocusInWindow();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel maxValueLabel;
    org.radixware.kernel.designer.common.dialogs.components.ValAsStrEditPanel maxValueTextField;
    private javax.swing.JLabel minValueLabel;
    org.radixware.kernel.designer.common.dialogs.components.ValAsStrEditPanel minValueTextField;
    private org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer stateDisplayer1;
    // End of variables declaration//GEN-END:variables

}