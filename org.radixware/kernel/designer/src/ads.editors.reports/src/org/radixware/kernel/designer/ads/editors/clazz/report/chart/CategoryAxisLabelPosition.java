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
 * CategoryAxisPosition.java
 *
 * Created on Jun 7, 2012, 3:29:32 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.report.chart;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartAxis;
import org.radixware.kernel.common.enums.EReportChartAxisLabelPositions;


public class CategoryAxisLabelPosition extends  AbstractAxisLabelPositionPanel{
    private AdsReportChartAxis axis;
    
    /** Creates new form CategoryAxisPosition */
    public CategoryAxisLabelPosition() {
        initComponents();      
        rbStandard.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
               if(axis!=null&& rbStandard.isSelected()){
                   axis.setAxisLabelPosition(EReportChartAxisLabelPositions.STANDARD);
               } 
            }
        });
        rbDown_45.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
               if(axis!=null && rbDown_45.isSelected()){
                   axis.setAxisLabelPosition(EReportChartAxisLabelPositions.DOWN_45);
               } 
            }
        });        
        rbDown_90.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
               if(axis!=null && rbDown_90.isSelected()){
                   axis.setAxisLabelPosition(EReportChartAxisLabelPositions.DOWN_90);
               } 
            }
        });
        rbUp_45.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
               if(axis!=null&& rbUp_45.isSelected()){
                   axis.setAxisLabelPosition(EReportChartAxisLabelPositions.UP_45);
               } 
            }
        });        
        rbUp_90.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
               if(axis!=null&& rbUp_90.isSelected()){
                   axis.setAxisLabelPosition(EReportChartAxisLabelPositions.UP_90);
               } 
            }
        });
    }
    
    private void setPanelEnabled(final boolean enabled){
        rbStandard.setEnabled(enabled);
        rbUp_45.setEnabled(enabled); 
        rbUp_90.setEnabled(enabled); 
        rbDown_90.setEnabled(enabled); 
        rbDown_45.setEnabled(enabled); 
        this.setEnabled(enabled);
    }
    
    @Override
    public void setAxis(final AdsReportChartAxis axis){
        this.axis=axis;
         setPanelEnabled(axis!=null);
         if(axis!=null){
             switch(axis.getAxisLabelPosition()){
                 case STANDARD:
                     rbStandard.setSelected(true);
                     break;
                 case DOWN_45:
                     rbDown_45.setSelected(true);
                     break;
                 case DOWN_90:
                     rbDown_90.setSelected(true);
                     break;
                 case UP_45:
                     rbUp_45.setSelected(true);
                     break;
                 case UP_90:
                     rbUp_90.setSelected(true);
                     break;
                 default:
                     break;
             }
         }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        rbStandard = new javax.swing.JRadioButton();
        rbUp_45 = new javax.swing.JRadioButton();
        rbUp_90 = new javax.swing.JRadioButton();
        rbDown_45 = new javax.swing.JRadioButton();
        rbDown_90 = new javax.swing.JRadioButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CategoryAxisLabelPosition.class, "CategoryAxisLabelPosition.border.title"))); // NOI18N

        buttonGroup1.add(rbStandard);
        rbStandard.setText(org.openide.util.NbBundle.getMessage(CategoryAxisLabelPosition.class, "CategoryAxisLabelPosition.rbStandard.text")); // NOI18N

        buttonGroup1.add(rbUp_45);
        rbUp_45.setText(org.openide.util.NbBundle.getMessage(CategoryAxisLabelPosition.class, "CategoryAxisLabelPosition.rbUp_45.text")); // NOI18N

        buttonGroup1.add(rbUp_90);
        rbUp_90.setText(org.openide.util.NbBundle.getMessage(CategoryAxisLabelPosition.class, "CategoryAxisLabelPosition.rbUp_90.text")); // NOI18N
        rbUp_90.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        buttonGroup1.add(rbDown_45);
        rbDown_45.setText(org.openide.util.NbBundle.getMessage(CategoryAxisLabelPosition.class, "CategoryAxisLabelPosition.rbDown_45.text")); // NOI18N

        buttonGroup1.add(rbDown_90);
        rbDown_90.setText(org.openide.util.NbBundle.getMessage(CategoryAxisLabelPosition.class, "CategoryAxisLabelPosition.rbDown_90.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbStandard)
                    .addComponent(rbUp_45)
                    .addComponent(rbUp_90)
                    .addComponent(rbDown_45)
                    .addComponent(rbDown_90))
                .addContainerGap(192, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(rbStandard)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbUp_45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbUp_90)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbDown_45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbDown_90)
                .addContainerGap(16, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton rbDown_45;
    private javax.swing.JRadioButton rbDown_90;
    private javax.swing.JRadioButton rbStandard;
    private javax.swing.JRadioButton rbUp_45;
    private javax.swing.JRadioButton rbUp_90;
    // End of variables declaration//GEN-END:variables
}