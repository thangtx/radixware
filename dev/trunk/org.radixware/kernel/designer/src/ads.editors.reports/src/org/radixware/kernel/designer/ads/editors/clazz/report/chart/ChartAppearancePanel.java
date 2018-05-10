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
 * AdsReportChartAppearance.java
 *
 * Created on Jun 1, 2012, 11:38:20 AM
 */
package org.radixware.kernel.designer.ads.editors.clazz.report.chart;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.border.TitledBorder;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell;
import org.radixware.kernel.common.enums.EReportChartCellType;
import org.radixware.kernel.common.enums.EReportChartLegendPosition;
import org.radixware.kernel.designer.common.dialogs.components.ComponentTitledBorder;


public class ChartAppearancePanel extends javax.swing.JPanel {
    private final JCheckBox axisSpaceCheckBox = new JCheckBox(NbBundle.getMessage(ChartAppearancePanel.class, "ChartAppearancePanel.axisSpaceCheckBox.text"));
    private final JCheckBox legendCheckBox = new JCheckBox(NbBundle.getMessage(ChartAppearancePanel.class, "ChartAppearancePanel.showLegendCheckBox.text"));
    private AdsReportChartCell cell;
   
    
    /** Creates new form AdsReportChartAppearance */
    @SuppressWarnings("deprecation")
    public ChartAppearancePanel() {
        initComponents();       
        ComponentTitledBorder border = new ComponentTitledBorder(axisSpaceCheckBox, axisSpacePanel, new TitledBorder(""));
        axisSpacePanel.setBorder(border);        
        axisSpaceCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (cell!=null) {
                    setAxisSpacePanelEnabled(axisSpaceCheckBox.isSelected());
                    cell.setAutoAxisSpace(!axisSpaceCheckBox.isSelected());
               }
            }
        });
       
        border = new ComponentTitledBorder(legendCheckBox, legendPanel, new TitledBorder(""));
        legendPanel.setBorder(border);
        
        legendCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if(cell!=null){
                    if(legendCheckBox.isSelected()){
                        rbBottomLegend.setSelected(true);
                        setLegendPanelEnabled(true);
                    }else{                        
                        cell.setLegendPosition(EReportChartLegendPosition.NO_LEGEND);
                        setLegendPanelEnabled(false);
                        buttonGroup2.clearSelection();
                    }
                }
            }
        });        
        rbBottomLegend.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if(cell!=null && rbBottomLegend.isSelected()){
                   cell.setLegendPosition(EReportChartLegendPosition.BOTTOM);
                }
            }
        });        
        rbTopLegend.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if(cell!=null && rbTopLegend.isSelected()){
                   cell.setLegendPosition(EReportChartLegendPosition.TOP);
                }
            }
        });
        rbRightLegend.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if(cell!=null && rbRightLegend.isSelected()){
                   cell.setLegendPosition(EReportChartLegendPosition.RIGHT);
                }
            }
        });
        rbLeftLegend.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if(cell!=null && rbLeftLegend.isSelected()){
                   cell.setLegendPosition(EReportChartLegendPosition.LEFT);
                }
            }
        });
        
        cbAxisGridX.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if(cell!=null){
                    cell.setXAxisGridVisible(cbAxisGridX.isSelected());
                }
            }
        });        
        cdAxisGridY.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if(cell!=null){
                 cell.setYAxisGridVisible(cdAxisGridY.isSelected());
                }
            }
        });
        
        rbHorizontalOrientation.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if(cell!=null && rbHorizontalOrientation.isSelected()){
                    cell.setIsHorizontalOrientation(rbHorizontalOrientation.isSelected());
                }
            }
        });
        rbVerticalOrientation.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if(cell!=null && rbVerticalOrientation.isSelected()){
                    cell.setIsHorizontalOrientation(!rbVerticalOrientation.isSelected());
                }
            }
        });      
    }
    
    private void setLegendPanelEnabled(final boolean enabled){
        rbBottomLegend.setEnabled(enabled);        
        rbLeftLegend.setEnabled(enabled);
        rbRightLegend.setEnabled(enabled);
        rbTopLegend.setEnabled(enabled);
    }
    
    private void setAxisSpacePanelEnabled(final boolean enabled){               
        rightAxisSpaceSpinner.setEnabled(enabled);
        topAxisSpaceSpinner.setEnabled(enabled);
        bottomAxisSpaceSpinner.setEnabled(enabled);
        leftAxisSpaceSpinner.setEnabled(enabled);
        lbBottomAxisSpace.setEnabled(enabled);
        lbLeftAxisSpace.setEnabled(enabled);
        lbRightAxisSpace.setEnabled(enabled);
        lbTopAxisSpace.setEnabled(enabled);
    }
    
    private void setPanelEnabled(final boolean enabled){
        //chartOrientationPanel.setEnabled(enabled);
        rbHorizontalOrientation.setEnabled(enabled);
        rbVerticalOrientation.setEnabled(enabled);        
        
        legendCheckBox.setEnabled(enabled);
        rbBottomLegend.setEnabled(enabled);        
        rbLeftLegend.setEnabled(enabled);
        rbRightLegend.setEnabled(enabled);
        rbTopLegend.setEnabled(enabled);
        
        //gridPanel.setEnabled(enabled);
        cbAxisGridX.setEnabled(enabled);
        cdAxisGridY.setEnabled(enabled);        
        
        axisSpaceCheckBox.setEnabled(enabled);        
        setAxisSpacePanelEnabled(enabled);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        legendPanel = new javax.swing.JPanel();
        rbBottomLegend = new javax.swing.JRadioButton();
        rbTopLegend = new javax.swing.JRadioButton();
        rbLeftLegend = new javax.swing.JRadioButton();
        rbRightLegend = new javax.swing.JRadioButton();
        axisSpacePanel = new javax.swing.JPanel();
        topAxisSpaceSpinner = new javax.swing.JSpinner();
        lbTopAxisSpace = new javax.swing.JLabel();
        lbLeftAxisSpace = new javax.swing.JLabel();
        lbBottomAxisSpace = new javax.swing.JLabel();
        lbRightAxisSpace = new javax.swing.JLabel();
        leftAxisSpaceSpinner = new javax.swing.JSpinner();
        bottomAxisSpaceSpinner = new javax.swing.JSpinner();
        rightAxisSpaceSpinner = new javax.swing.JSpinner();
        gridPanel = new javax.swing.JPanel();
        cbAxisGridX = new javax.swing.JCheckBox();
        cdAxisGridY = new javax.swing.JCheckBox();
        chartOrientationPanel = new javax.swing.JPanel();
        rbHorizontalOrientation = new javax.swing.JRadioButton();
        rbVerticalOrientation = new javax.swing.JRadioButton();

        setMinimumSize(new java.awt.Dimension(400, 350));
        setPreferredSize(new java.awt.Dimension(400, 350));
        setLayout(new java.awt.GridBagLayout());

        legendPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ChartAppearancePanel.class, "ChartAppearancePanel.legendPanel.border.title"))); // NOI18N

        buttonGroup2.add(rbBottomLegend);
        rbBottomLegend.setText(org.openide.util.NbBundle.getMessage(ChartAppearancePanel.class, "ChartAppearancePanel.rbBottomLegend.text")); // NOI18N

        buttonGroup2.add(rbTopLegend);
        rbTopLegend.setText(org.openide.util.NbBundle.getMessage(ChartAppearancePanel.class, "ChartAppearancePanel.rbTopLegend.text")); // NOI18N

        buttonGroup2.add(rbLeftLegend);
        rbLeftLegend.setText(org.openide.util.NbBundle.getMessage(ChartAppearancePanel.class, "ChartAppearancePanel.rbLeftLegend.text")); // NOI18N

        buttonGroup2.add(rbRightLegend);
        rbRightLegend.setText(org.openide.util.NbBundle.getMessage(ChartAppearancePanel.class, "ChartAppearancePanel.rbRightLegend.text")); // NOI18N

        javax.swing.GroupLayout legendPanelLayout = new javax.swing.GroupLayout(legendPanel);
        legendPanel.setLayout(legendPanelLayout);
        legendPanelLayout.setHorizontalGroup(
            legendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(legendPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(legendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbTopLegend)
                    .addComponent(rbLeftLegend)
                    .addComponent(rbRightLegend)
                    .addComponent(rbBottomLegend))
                .addContainerGap(144, Short.MAX_VALUE))
        );
        legendPanelLayout.setVerticalGroup(
            legendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(legendPanelLayout.createSequentialGroup()
                .addComponent(rbBottomLegend)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbTopLegend)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbLeftLegend)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbRightLegend)
                .addGap(0, 127, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        add(legendPanel, gridBagConstraints);

        axisSpacePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ChartAppearancePanel.class, "ChartAppearancePanel.axisSpacePanel.border.title"))); // NOI18N

        topAxisSpaceSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                topAxisSpaceSpinnerStateChanged(evt);
            }
        });

        lbTopAxisSpace.setText(org.openide.util.NbBundle.getMessage(ChartAppearancePanel.class, "ChartAppearancePanel.lbTopAxisSpace.text")); // NOI18N

        lbLeftAxisSpace.setText(org.openide.util.NbBundle.getMessage(ChartAppearancePanel.class, "ChartAppearancePanel.lbLeftAxisSpace.text")); // NOI18N

        lbBottomAxisSpace.setText(org.openide.util.NbBundle.getMessage(ChartAppearancePanel.class, "ChartAppearancePanel.lbBottomAxisSpace.text")); // NOI18N

        lbRightAxisSpace.setText(org.openide.util.NbBundle.getMessage(ChartAppearancePanel.class, "ChartAppearancePanel.lbRightAxisSpace.text")); // NOI18N

        leftAxisSpaceSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                leftAxisSpaceSpinnerStateChanged(evt);
            }
        });

        bottomAxisSpaceSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                bottomAxisSpaceSpinnerStateChanged(evt);
            }
        });

        rightAxisSpaceSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rightAxisSpaceSpinnerStateChanged(evt);
            }
        });

        javax.swing.GroupLayout axisSpacePanelLayout = new javax.swing.GroupLayout(axisSpacePanel);
        axisSpacePanel.setLayout(axisSpacePanelLayout);
        axisSpacePanelLayout.setHorizontalGroup(
            axisSpacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(axisSpacePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(axisSpacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbTopAxisSpace)
                    .addComponent(lbLeftAxisSpace)
                    .addComponent(lbBottomAxisSpace)
                    .addComponent(lbRightAxisSpace))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(axisSpacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rightAxisSpaceSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                    .addComponent(leftAxisSpaceSpinner, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(topAxisSpaceSpinner, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bottomAxisSpaceSpinner))
                .addContainerGap())
        );
        axisSpacePanelLayout.setVerticalGroup(
            axisSpacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(axisSpacePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(axisSpacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbTopAxisSpace)
                    .addComponent(topAxisSpaceSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(axisSpacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbLeftAxisSpace)
                    .addComponent(leftAxisSpaceSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(axisSpacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbBottomAxisSpace)
                    .addComponent(bottomAxisSpaceSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(axisSpacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbRightAxisSpace)
                    .addComponent(rightAxisSpaceSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        add(axisSpacePanel, gridBagConstraints);

        gridPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ChartAppearancePanel.class, "ChartAppearancePanel.gridPanel.border.title"))); // NOI18N

        cbAxisGridX.setText(org.openide.util.NbBundle.getMessage(ChartAppearancePanel.class, "ChartAppearancePanel.cbAxisGridX.text")); // NOI18N

        cdAxisGridY.setText(org.openide.util.NbBundle.getMessage(ChartAppearancePanel.class, "ChartAppearancePanel.cdAxisGridY.text")); // NOI18N

        javax.swing.GroupLayout gridPanelLayout = new javax.swing.GroupLayout(gridPanel);
        gridPanel.setLayout(gridPanelLayout);
        gridPanelLayout.setHorizontalGroup(
            gridPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gridPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gridPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbAxisGridX, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cdAxisGridY))
                .addContainerGap(128, Short.MAX_VALUE))
        );
        gridPanelLayout.setVerticalGroup(
            gridPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gridPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbAxisGridX)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cdAxisGridY)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(gridPanel, gridBagConstraints);

        chartOrientationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ChartAppearancePanel.class, "ChartAppearancePanel.chartOrientationPanel.border.title"))); // NOI18N

        buttonGroup1.add(rbHorizontalOrientation);
        rbHorizontalOrientation.setText(org.openide.util.NbBundle.getMessage(ChartAppearancePanel.class, "ChartAppearancePanel.rbHorizontalOrientation.text")); // NOI18N

        buttonGroup1.add(rbVerticalOrientation);
        rbVerticalOrientation.setText(org.openide.util.NbBundle.getMessage(ChartAppearancePanel.class, "ChartAppearancePanel.rbVerticalOrientation.text")); // NOI18N

        javax.swing.GroupLayout chartOrientationPanelLayout = new javax.swing.GroupLayout(chartOrientationPanel);
        chartOrientationPanel.setLayout(chartOrientationPanelLayout);
        chartOrientationPanelLayout.setHorizontalGroup(
            chartOrientationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chartOrientationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(chartOrientationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbHorizontalOrientation)
                    .addComponent(rbVerticalOrientation))
                .addContainerGap(128, Short.MAX_VALUE))
        );
        chartOrientationPanelLayout.setVerticalGroup(
            chartOrientationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, chartOrientationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbHorizontalOrientation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbVerticalOrientation)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(chartOrientationPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void topAxisSpaceSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_topAxisSpaceSpinnerStateChanged
        if(cell!=null){
            cell.setTopAxisSpacePx((Integer)topAxisSpaceSpinner.getValue());
        }
    }//GEN-LAST:event_topAxisSpaceSpinnerStateChanged

    private void leftAxisSpaceSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_leftAxisSpaceSpinnerStateChanged
        if(cell!=null){
            cell.setLeftAxisSpacePx((Integer)leftAxisSpaceSpinner.getValue());
        }
    }//GEN-LAST:event_leftAxisSpaceSpinnerStateChanged

    private void bottomAxisSpaceSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_bottomAxisSpaceSpinnerStateChanged
        if(cell!=null){
            cell.setBottomAxisSpacePx((Integer)bottomAxisSpaceSpinner.getValue());
        }
    }//GEN-LAST:event_bottomAxisSpaceSpinnerStateChanged

    private void rightAxisSpaceSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rightAxisSpaceSpinnerStateChanged
        if(cell!=null){
            cell.setRightAxisSpacePx((Integer)rightAxisSpaceSpinner.getValue());
        }
    }//GEN-LAST:event_rightAxisSpaceSpinnerStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel axisSpacePanel;
    private javax.swing.JSpinner bottomAxisSpaceSpinner;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBox cbAxisGridX;
    private javax.swing.JCheckBox cdAxisGridY;
    private javax.swing.JPanel chartOrientationPanel;
    private javax.swing.JPanel gridPanel;
    private javax.swing.JLabel lbBottomAxisSpace;
    private javax.swing.JLabel lbLeftAxisSpace;
    private javax.swing.JLabel lbRightAxisSpace;
    private javax.swing.JLabel lbTopAxisSpace;
    private javax.swing.JSpinner leftAxisSpaceSpinner;
    private javax.swing.JPanel legendPanel;
    private javax.swing.JRadioButton rbBottomLegend;
    private javax.swing.JRadioButton rbHorizontalOrientation;
    private javax.swing.JRadioButton rbLeftLegend;
    private javax.swing.JRadioButton rbRightLegend;
    private javax.swing.JRadioButton rbTopLegend;
    private javax.swing.JRadioButton rbVerticalOrientation;
    private javax.swing.JSpinner rightAxisSpaceSpinner;
    private javax.swing.JSpinner topAxisSpaceSpinner;
    // End of variables declaration//GEN-END:variables


      public void open(final AdsReportChartCell cell){          
          setPanelEnabled(cell!=null);
          if(cell!=null){
              if(cell.getChartType()==EReportChartCellType.PIE){
                  rbHorizontalOrientation.setEnabled(false);
                  rbVerticalOrientation.setEnabled(false);
                  cbAxisGridX.setEnabled(false);
                  cdAxisGridY.setEnabled(false);
                  axisSpaceCheckBox.setEnabled(false);
                  setAxisSpacePanelEnabled(false);
              }else{
                cbAxisGridX.setSelected(cell.isXAxisGridVisible());
                cdAxisGridY.setSelected(cell.isYAxisGridVisible());
                if(cell.isHorizontalOrientation()){
                    rbHorizontalOrientation.setSelected(true);
                }else{
                    rbVerticalOrientation.setSelected(true);
                }                
                axisSpaceCheckBox.setSelected(!cell.isAutoAxisSpace());
                topAxisSpaceSpinner.setValue(cell.getTopAxisSpacePx());
                bottomAxisSpaceSpinner.setValue(cell.getBottomAxisSpacePx());
                leftAxisSpaceSpinner.setValue(cell.getLeftAxisSpacePx());
                rightAxisSpaceSpinner.setValue(cell.getRightAxisSpacePx());
                setAxisSpacePanelEnabled(axisSpaceCheckBox.isSelected());
               
              }
              switch(cell.getLegendPosition()){
                    case NO_LEGEND:
                        setLegendPanelEnabled(false);
                        legendCheckBox.setSelected(false);                     
                        break;
                    case BOTTOM:
                        legendCheckBox.setSelected(true);  
                        rbBottomLegend.setSelected(true);
                        setLegendPanelEnabled(true);
                        break;
                    case TOP:
                        legendCheckBox.setSelected(true);
                        rbTopLegend.setSelected(true);
                        setLegendPanelEnabled(true);
                        break;
                    case LEFT:
                        legendCheckBox.setSelected(true);
                        rbLeftLegend.setSelected(true);
                        setLegendPanelEnabled(true);
                        break;
                    case RIGHT:
                        legendCheckBox.setSelected(true);
                        rbRightLegend.setSelected(true);
                        setLegendPanelEnabled(true);
                        break;
              }
          }
          this.cell=cell;
      } 
}
