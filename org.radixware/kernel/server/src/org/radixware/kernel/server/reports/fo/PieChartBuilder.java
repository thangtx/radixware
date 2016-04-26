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

package org.radixware.kernel.server.reports.fo;

import java.awt.Font;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.util.TableOrder;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.server.reports.ChartDataSet;
import org.radixware.kernel.server.reports.ChartDataSet.DataSetItem;
import org.radixware.kernel.server.types.Report;


public class PieChartBuilder extends ChartBuilder{
    
    public PieChartBuilder(final AdsReportChartCell cell, final Report report,final Set<ChartDataSet> chartDataSet){
        super(cell,report,chartDataSet);
    }

    @Override
     protected JFreeChart createChart(){  
         boolean is3dChart=cell.getChartSeries().is3D();
         JFreeChart chart;
         if(is3dChart){
            chart = ChartFactory.createMultiplePieChart3D(null, createMultyPieDataSet(), TableOrder.BY_COLUMN,true,  false,false);
         }else{
            chart = ChartFactory.createMultiplePieChart(null, createMultyPieDataSet(), TableOrder.BY_COLUMN,true,  false,false); 
         }
         MultiplePiePlot plot =(MultiplePiePlot)chart.getPlot();
         JFreeChart subchart = plot.getPieChart();        
         PiePlot p = (PiePlot) subchart.getPlot();
         p.setForegroundAlpha((float)cell.getForegroundAlpha());
         p.setBackgroundPaint(null);
         p.setOutlineStroke(null);
         p.setLabelGenerator(new StandardPieSectionLabelGenerator("{2}"));
         Font newFont=createFont(cell.getLegendFont());
         subchart.getTitle().setFont(newFont);       
         p.setLabelFont(newFont); 
         return chart;
    }
    
    /* private DefaultPieDataset createPieDataSet(){
        DefaultPieDataset pieDataset = new DefaultPieDataset();        
        if(chartDataSet!=null && chartDataSet.iterator().hasNext()){
            ChartDataSet  dataset=chartDataSet.iterator().next();            
            Map<Object,List<DataSetItem>> dataSetMap=dataset.getDataSetMap();
            for(Object seies:dataSetMap.keySet()){
                List<DataSetItem> d=dataSetMap.get(seies);
                for(DataSetItem item:d){
                    if(item.getY()!=null && item.getX()!=null){
                        EValType valtype=dataset.getRangePropDef().getValType();
                        Number y=getValue( item, valtype);
                        String x= item.getX().toString();
                        if(y!=null){
                            pieDataset.setValue(x, y);
                        }
                    }
                }
            }
        }
        return pieDataset;
    } */ 
     
    private CategoryDataset createMultyPieDataSet(){
        DefaultCategoryDataset pieDataset = new DefaultCategoryDataset();        
        for(ChartDataSet dataSet:chartDataSet){
           Map<Object,List<DataSetItem>> dataSetMap=dataSet.getDataSetMap();
            for(Object seies:dataSetMap.keySet()){
                List<DataSetItem> d=dataSetMap.get(seies);
                String seriesName=calcSeriesTitle(seies,dataSet);
                for(DataSetItem item:d){
                    if(item.getY()!=null && item.getX()!=null){
                        EValType valtype=dataSet.getRangePropDef().getValType();
                        Number y=getValue( item.getY(), valtype);
                        String x= item.getX().toString();
                        if(y!=null){
                            pieDataset.addValue(y,  x, seriesName);

                        }
                    }
                }
            }
        } 
        return pieDataset;
    }
    
    @Override
     protected String calcSeriesTitle(Object series,ChartDataSet dataset){
        String seriesName; 
        if(series!=null){
             seriesName= series.toString();
        }else{
             seriesName=calcTitle(dataset.getSeriesTitleId());
        }
        return seriesName==null? "": seriesName; 
    }

    @Override
    protected Plot createPlot() {
        return null;
    }
    
}
