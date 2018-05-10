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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance.Font;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EReportChartCellType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.editors.clazz.report.AdsReportFontPanel;
import org.radixware.kernel.designer.common.dialogs.components.ComponentTitledBorder;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;


public class AdsReportChartAppearance extends javax.swing.JPanel {
    private final LocalizingEditorPanel chartTitle= new LocalizingEditorPanel();
    private final ChartAppearancePanel appearancePanel=new ChartAppearancePanel();
    private final JCheckBox cbFontForTitle = new JCheckBox(NbBundle.getMessage(AdsReportChartAppearance.class, "AdsReportChartAppearance.cbUseCellFont.text"));
    private final JCheckBox cbFontForAxes = new JCheckBox(NbBundle.getMessage(AdsReportChartAppearance.class, "AdsReportChartAppearance.cbUseCellFont.text"));
    private final JCheckBox cbFontForLegend = new JCheckBox(NbBundle.getMessage(AdsReportChartAppearance.class, "AdsReportChartAppearance.cbUseCellFont.text"));
    private final AdsReportFontPanel titleFontPanel;
    private final AdsReportFontPanel axisFontPanel;
    private final AdsReportFontPanel legendFontPanel;
    private final ForegroundAlphaPanel foregrAlphapanel;
    private final MinPlotSizePanel minPlotSizePanel;
    private AdsReportChartCell cell;
    private boolean updating=false;

    /** Creates new form AdsReportChartAppearance */
    @SuppressWarnings("deprecation")
    public AdsReportChartAppearance() {
        super();
        initComponents();

        final javax.swing.JPanel content = new javax.swing.JPanel();
        final GridBagLayout gbl = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        content.setLayout(gbl);
        c.insets = new Insets(0, 2, 10, 2);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        String title=NbBundle.getMessage(AdsReportChartAppearance.class, "AdsReportChartAppearance.chartTitle.text");
        chartTitle.setTitle(title);
        gbl.setConstraints(chartTitle, c);
        content.add(chartTitle);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 6, 0);        
        gbl.setConstraints(appearancePanel, c);
        content.add(appearancePanel);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(0, 0, 8, 0);
        title=NbBundle.getMessage(AdsReportChartAppearance.class, "AdsReportChartAppearance.titleFontPanel.text");
        titleFontPanel=new AdsReportFontPanel(null,title);
        javax.swing.JPanel fontPanel = createFontPanel(titleFontPanel,cbFontForTitle);
        cbFontForTitle.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if(cell != null && !updating){
                   cell.setTitleFontInherited(cbFontForTitle.isSelected());
                   titleFontPanel.setFont(cell.getTitleFont(),cbFontForTitle.isSelected());
                }
            }
        });
        gbl.setConstraints(fontPanel, c);
        content.add(fontPanel);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(0, 0, 8, 0);
        title=NbBundle.getMessage(AdsReportChartAppearance.class, "AdsReportChartAppearance.axisFontPanel.text");
        axisFontPanel=new AdsReportFontPanel(null,title);
        fontPanel = createFontPanel(axisFontPanel,cbFontForAxes);
        cbFontForAxes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if(cell !=null && !updating){
                    cell.setAxesFontInherited(cbFontForAxes.isSelected());
                    axisFontPanel.setFont(cell.getAxesFont(),cbFontForAxes.isSelected());
                }
            }
        });
        gbl.setConstraints(fontPanel, c);
        content.add(fontPanel);

        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(0, 0, 6, 0);
        title=NbBundle.getMessage(AdsReportChartAppearance.class, "AdsReportChartAppearance.legendFontPanel.text");
        legendFontPanel=new AdsReportFontPanel(null,title);
        fontPanel = createFontPanel(legendFontPanel,cbFontForLegend);
        cbFontForLegend.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if(cell !=null && !updating){
                   cell.setLegendFontInherited(cbFontForLegend.isSelected());
                   legendFontPanel.setFont(cell.getLegendFont(),cbFontForLegend.isSelected());
                }
            }
        });
        gbl.setConstraints(fontPanel, c);
        content.add(fontPanel);

        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(10, 10, 0, 0);
        foregrAlphapanel = new ForegroundAlphaPanel();
        gbl.setConstraints(foregrAlphapanel, c);
        content.add(foregrAlphapanel);
        
        c.gridx = 0;
        c.gridy = 6;
        c.insets = new Insets(10, 0, 0, 0);
        minPlotSizePanel = new MinPlotSizePanel();
        gbl.setConstraints(minPlotSizePanel, c);
        
        content.add(minPlotSizePanel);        
        this.setLayout(new BorderLayout());
        JScrollPane scroller = new JScrollPane();        
        scroller.setViewportBorder(BorderFactory.createEmptyBorder());
        scroller.setViewportView(content);
        this.add(scroller, BorderLayout.CENTER);
    }

    @SuppressWarnings("deprecation")
    private javax.swing.JPanel createFontPanel(final AdsReportFontPanel innerFontPanel,final JCheckBox cbUseCellFont){
        final javax.swing.JPanel fontPanel = new javax.swing.JPanel();
        ComponentTitledBorder border = new ComponentTitledBorder(cbUseCellFont, fontPanel, new TitledBorder(""));
        fontPanel.setBorder(border);

        cbUseCellFont.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                final boolean inheritFont=cbUseCellFont.isSelected();
                innerFontPanel.setPanelEnabled(!inheritFont);
                if (!inheritFont  && !updating) {
                    innerFontPanel.apply();
                }
            }
        });
        fontPanel.setLayout(new BorderLayout());
        fontPanel.add(innerFontPanel,BorderLayout.NORTH);
        return fontPanel;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    private final ChangeSupport changeSupport = new ChangeSupport(this);

    public void removeChangeListener(final ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    public void fireChange() {
        changeSupport.fireChange();
    }

    public void addChangeListener(final ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public void open(final AdsReportChartCell cell,final IReportWidgetContainer  band,final AdsReportClassDef report,final Map<Id,IMultilingualStringDef> mlStrings) {
        updating =true;
        this.cell=cell;
        appearancePanel.open(cell);
        foregrAlphapanel.setCell(cell);
        minPlotSizePanel.open(cell);

        updateFonts(band);

        cbFontForTitle.setSelected(cell.isTitleFontInherited());
        cbFontForLegend.setSelected(cell.isLegendFontInherited());
        if(cell.getChartType()==EReportChartCellType.PIE){
            cbFontForAxes.setSelected(true);
            cbFontForAxes.setEnabled(false);
        }else{
           cbFontForAxes.setSelected(cell.isAxesFontInherited());
        }
        chartTitle.open(new ChartEditorHandleInfo(report,mlStrings) {

        @Override
        public Id getTitleId() {
            return cell!=null ? cell.getTitleId():null;
        }

        @Override
        protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
            if (cell != null) {
               if (multilingualStringDef == null) {
                   cell.setTitleId(null);                   
               }else{
                   cell.setTitleId(multilingualStringDef.getId());
               }
           }
       }

        /* @Override
        public AdsMultilingualStringDef getAdsMultilingualStringDef(){
           AdsMultilingualStringDef res=super.getAdsMultilingualStringDef();
           if(res==null && mlStrings!=null){
               res=mlStrings.get(getTitleId());
           }
           return  res;
       }

        @Override
        public AdsDefinition getAdsDefinition() {
            return report;
        }

        @Override
        protected void addAdsMultilingualStringDef(AdsMultilingualStringDef adsMultilingualStringDef) {
            if(adsMultilingualStringDef!=null){
                if(mlStrings!=null) {
                    mlStrings.put(adsMultilingualStringDef.getId(), adsMultilingualStringDef);
                }else {
                    super.addAdsMultilingualStringDef(adsMultilingualStringDef);
                }
            }
        }

        @Override
        public void removeAdsMultilingualStringDef() {
            AdsMultilingualStringDef stringDef = this.getAdsMultilingualStringDef();
            if(stringDef != null && mlStrings!=null && mlStrings.containsKey(stringDef.getId())){
                mlStrings.remove(stringDef.getId());
            }
            super.removeAdsMultilingualStringDef();
        }

        @Override
        protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                getAdsMultilingualStringDef().setValue(language, newStringValue);
        }*/
        });
        updating =false;
    }

    public void updateFonts(final IReportWidgetContainer  band){
            final Font titleFont=calcFont(cell.getTitleFont(),  band, cell, cell.isTitleFontInherited());
            titleFontPanel.setFont(titleFont,true);
            final Font axisFont=calcFont(cell.getAxesFont(),  band, cell,cell.isAxesFontInherited());
            axisFontPanel.setFont(axisFont,true);
            final Font legendFont=calcFont(cell.getLegendFont(),  band, cell,cell.isLegendFontInherited());
            legendFontPanel.setFont(legendFont,true);
    }

    private Font calcFont(final Font font, final IReportWidgetContainer band,final AdsReportChartCell cell, final boolean isInherited){
            if(isInherited && cell.isFontInherited() && cell.getOwnerBand()==null ){
                return band.getFont();
            }
            return font;
        }

    boolean isComplete() {
        //if (nameEditPanel1.isComplete()) {
        //    return true;
        //} else {
        //    return false;
        //}

        return true;
    }

}
