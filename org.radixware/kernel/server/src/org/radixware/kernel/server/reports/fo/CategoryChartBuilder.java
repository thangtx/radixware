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

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartAxis;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell;
import org.radixware.kernel.common.enums.EReportChartSeriesType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.server.reports.ChartDataSet;
import org.radixware.kernel.server.reports.ChartDataSet.DataSetItem;
import org.radixware.kernel.server.types.Report;


public class CategoryChartBuilder extends ChartBuilder{   
    
    public CategoryChartBuilder(final AdsReportChartCell cell, final Report report,final Set<ChartDataSet> chartDataSet){
        super(cell,report,chartDataSet);
    }
    
    @Override
    protected Plot createPlot(){
        CategoryPlot plot=new CategoryPlot();
        plot.setForegroundAlpha((float)cell.getForegroundAlpha());
        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setDomainGridlinesVisible(cell.isXAxisGridVisible()); 
        plot.setRangeGridlinesVisible(cell.isYAxisGridVisible());
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        if(cell.isHorizontalOrientation()){
            PlotOrientation orientation=PlotOrientation.HORIZONTAL;
            plot.setOrientation(orientation);
        }
        if(!cell.isAutoAxisSpace())
            setAxisSpase(cell, plot);         
          
        createAxes(plot);
        createDatasets(plot);              
        return plot;
    } 
    
    private void createAxes(CategoryPlot plot){
        int index=0;        
        for(AdsReportChartAxis axis:cell.getDomainAxes()){
            CategoryAxis categoryAxis=createCategoryAxis(axis,cell.getAxesFont()); 
            categoryAxis.setLowerMargin(0.0);
            categoryAxis.setUpperMargin(0.0);
            categoryAxis.setVisible(axis.isVisible());
            if(axis.isRightOrTop()){
                plot.setDomainAxisLocation(index, AxisLocation.TOP_OR_RIGHT);
            }else{
                plot.setDomainAxisLocation(index, AxisLocation.BOTTOM_OR_LEFT);
            } 
            plot.setDomainAxis(index,categoryAxis);
            index++;
        }
        index=0;  
        EValType type=null;
        for(AdsReportChartAxis axis:cell.getRangeAxes()){
            for(ChartDataSet chartData:chartDataSet){
                if(chartData.getRangeAxisIndex()==axis.getIndex()){
                    type=chartData.getRangePropDef().getValType();
                }
            }
            ValueAxis rangeAxis=createValueAxis(axis, cell.getAxesFont(), type); 
            rangeAxis.setVisible(axis.isVisible());
            if(axis.isRightOrTop()){
                plot.setRangeAxisLocation(index, AxisLocation.TOP_OR_RIGHT);
            }else{
                plot.setRangeAxisLocation(index, AxisLocation.BOTTOM_OR_LEFT);
            }             
            plot.setRangeAxis(index,rangeAxis);
            index++;
        }
    }
    
    private void createDatasets(CategoryPlot plot){
       int index=0;
       for(ChartDataSet dataSet:chartDataSet){
            plot.setDataset(index,createCategoryDataset(dataSet));
            CategoryItemRenderer renderer=(CategoryItemRenderer)createRenderer(dataSet);
            plot.setRenderer(index,renderer);
            if(dataSet.getIsSeriesItemLabelShow()){
               renderer.setBaseItemLabelsVisible(true);
               renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            }
            int domainAxisIndex=dataSet.getDomainAxisIndex();
            int rangeAxisIndex=dataSet.getRangeAxisIndex();
            calcAxisTitle( dataSet.getDomainPropDef(),plot.getDomainAxis(domainAxisIndex));            
            calcAxisTitle( dataSet.getRangePropDef(),plot.getRangeAxis(rangeAxisIndex));
            
            plot.mapDatasetToRangeAxis(index, rangeAxisIndex);
            plot.mapDatasetToDomainAxis(index, domainAxisIndex);
            
            if(dataSet.getSeriesType()==EReportChartSeriesType.AREA || 
               dataSet.getSeriesType()==EReportChartSeriesType.AREA_STACKED){
                plot.getDomainAxis(dataSet.getDomainAxisIndex()).setCategoryMargin(0);
            }
            index++;
       }  
    }
    
    private DefaultCategoryDataset createCategoryDataset(ChartDataSet chartDataSet){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<Object,List<DataSetItem>> dataSetMap=chartDataSet.getDataSetMap();
        for(Object seies:dataSetMap.keySet()){
            List<DataSetItem> d=dataSetMap.get(seies);
            //java.util.Collections.sort(d, new CategoryComporator());
            String seriesName=calcSeriesTitle(seies,chartDataSet);
            for(DataSetItem item:d){
                if(item.getY()!=null && item.getX()!=null){
                    EValType valtype=chartDataSet.getRangePropDef().getValType();
                    Number y=getValue(item.getY(), valtype);
                    String x= item.getX().toString();
                    if(y!=null){
                        dataset.addValue(y, seriesName, x);
                    }
                }
            }
        }
        return dataset;
    } 
    
    
    protected void setAxisSpase(final AdsReportChartCell cell,final CategoryPlot plot){
        AxisSpace space=new AxisSpace();       
        space.setTop(cell.getTopAxisSpacePx());
        space.setLeft(cell.getLeftAxisSpacePx());
        space.setBottom(cell.getBottomAxisSpacePx());
        space.setRight(cell.getRightAxisSpacePx());
        plot.setFixedRangeAxisSpace(space);
    }
}
