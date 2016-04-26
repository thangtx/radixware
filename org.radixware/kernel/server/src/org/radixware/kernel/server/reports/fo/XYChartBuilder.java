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

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.data.xy.XYSeries;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartAxis;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.server.reports.ChartDataSet;
import org.radixware.kernel.server.reports.ChartDataSet.DataSetItem;
import org.radixware.kernel.server.types.Report;


public class XYChartBuilder extends ChartBuilder{    
    
    public XYChartBuilder(final AdsReportChartCell cell, final Report report,final Set<ChartDataSet> chartDataSet){
        super(cell,report,chartDataSet);
    }
    
    @Override
    protected Plot createPlot(){
        final XYPlot plot=new XYPlot();
        plot.setForegroundAlpha((float)cell.getForegroundAlpha());
        plot.setOrientation(PlotOrientation.VERTICAL);        
        plot.setDomainGridlinesVisible(cell.isXAxisGridVisible()); 
        plot.setRangeGridlinesVisible(cell.isYAxisGridVisible());
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD); 
        if(cell.isHorizontalOrientation()){
            final PlotOrientation orientation=PlotOrientation.HORIZONTAL;
            plot.setOrientation(orientation);
        }
        if(!cell.isAutoAxisSpace())
            setAxisSpase(cell,plot);
        
        createAxes(plot);  
        createDatasets(plot);        
        return plot;
    }

    private void createAxes(XYPlot plot){
        int index=0;
        EValType type=null;
        for(AdsReportChartAxis axis:cell.getDomainAxes()){   
            for(ChartDataSet chartData:chartDataSet){
                if(chartData.getDomainAxisIndex()==axis.getIndex()){
                    type=chartData.getDomainPropDef().getValType();
                }
            }
            final ValueAxis domainAxis=createValueAxis(axis,cell.getAxesFont(),type);  
            domainAxis.setVisible(axis.isVisible());
            plot.setDomainAxis(index,domainAxis);
            if(axis.isRightOrTop()){
                plot.setDomainAxisLocation(index, AxisLocation.TOP_OR_RIGHT);
            }else{
                plot.setDomainAxisLocation(index, AxisLocation.BOTTOM_OR_LEFT);
            }            
            index++;
        }
        index=0;
        for(AdsReportChartAxis axis:cell.getRangeAxes()){ 
            for(ChartDataSet chartData:chartDataSet){
                if(chartData.getRangeAxisIndex()==axis.getIndex()){
                    type=chartData.getRangePropDef().getValType();
                }
            }
            final ValueAxis rangeAxis=createValueAxis( axis,cell.getAxesFont(),type);
            rangeAxis.setVisible(axis.isVisible());
            plot.setRangeAxis(index,rangeAxis); 
            if(axis.isRightOrTop()){
                plot.setRangeAxisLocation(index, AxisLocation.TOP_OR_RIGHT);
            }else{
                plot.setRangeAxisLocation(index, AxisLocation.BOTTOM_OR_LEFT);
            }             
            index++;
        }           
    }
    
    private void createDatasets(XYPlot plot){
        int index=0;   
        for(ChartDataSet dataSet:chartDataSet){
           plot.setDataset(index,createDataSet(dataSet,cell));
           final XYItemRenderer renderer=(XYItemRenderer)createRenderer(dataSet);

           plot.setRenderer(index,renderer);
           if(dataSet.getIsSeriesItemLabelShow()){
               renderer.setBaseItemLabelsVisible(true);
               renderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
           }
           final int domainAxisIndex=dataSet.getDomainAxisIndex();
           final int rangeAxisIndex=dataSet.getRangeAxisIndex();
           calcAxisTitle( dataSet.getDomainPropDef(),plot.getDomainAxis(domainAxisIndex));           
           calcAxisTitle( dataSet.getRangePropDef(),plot.getRangeAxis(rangeAxisIndex));
           
           plot.mapDatasetToDomainAxis(index, domainAxisIndex);
           plot.mapDatasetToRangeAxis(index, rangeAxisIndex);           
           index++;
        }
    }
    
    private TableXYDataset createDataSet(final ChartDataSet chartDataSet,final AdsReportChartCell cell){
        TableXYDataset dataset;
        final int axisIndex=chartDataSet.getDomainAxisIndex();
        final AdsReportChartAxis axis=cell.getDomainAxes().get(axisIndex);
        if(axis.isDateAxis()){
            dataset = createDateDataSet(chartDataSet);
        }else{
            dataset = createXYDataSet(chartDataSet);
        }
        return dataset;
    }
    
    private TableXYDataset createXYDataSet(ChartDataSet chartDataSet){
        final DefaultTableXYDataset dataset = new DefaultTableXYDataset();
        final Map<Object,List<DataSetItem>> dataSetMap=chartDataSet.getDataSetMap();
        for(Object seies:dataSetMap.keySet()){
            final List<DataSetItem> d=dataSetMap.get(seies);
            final String seriesName=calcSeriesTitle(seies,chartDataSet);            
            final XYSeries series = new XYSeries(seriesName,true,false);   
            for(DataSetItem item:d){
                if( item.getY()!=null && item.getX()!=null ){ 
                    EValType valtype=chartDataSet.getDomainPropDef().getValType();
                    final Number x=getValue( item.getX(), valtype);  
                    valtype=chartDataSet.getRangePropDef().getValType();
                    final Number y=getValue( item.getY(), valtype);
                    if(x!=null && y!=null){
                        series.add(x, y);
                    }                    
                }
            }
            dataset.addSeries(series);
        }
        return dataset;
    }
    
    private TableXYDataset createDateDataSet(ChartDataSet chartDataSet){
        final TimeTableXYDataset data= new TimeTableXYDataset ();
        final Map<Object,List<DataSetItem>> dataSetMap=chartDataSet.getDataSetMap();
        for(Object seies:dataSetMap.keySet()){
            final List<DataSetItem> d=dataSetMap.get(seies);
            final String seriesName=calcSeriesTitle(seies,chartDataSet);
            for(DataSetItem item:d){
                if( item.getY()!=null && item.getX()!=null ){
                    final EValType valtype=chartDataSet.getRangePropDef().getValType();
                    final Number y=getValue( item.getY(), valtype);
                    final Timestamp x=(Timestamp)item.getX();
                    if(y!=null && x!=null){ 
                        final Date date=new Date(x.getTime());
                        data.add(new Millisecond(date), y,seriesName,true);
                    }
                }
            }
        }
        return data;
    }    
    
    protected void setAxisSpase(final AdsReportChartCell cell,final XYPlot plot){
        final AxisSpace space=new AxisSpace();       
        space.setTop(cell.getTopAxisSpacePx());
        space.setLeft(cell.getLeftAxisSpacePx());
        space.setBottom(cell.getBottomAxisSpacePx());
        space.setRight(cell.getRightAxisSpacePx());
        plot.setFixedRangeAxisSpace(space);
    }
}