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
package org.radixware.kernel.explorer.editors.monitoring.diagram;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QResizeEvent;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.XYPlot;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dashboard.DiagramSettings;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.enums.EDateTimeStyle;
import org.radixware.schemas.monitoringcommand.DiagramRs;

public abstract class DiagramWidget extends QWidget {

    private QWidget axisWidget;
    protected ImageWidget image;
    protected final QWidget parent;
    protected JFreeChart chart;
    protected double[] autoValScale;
    protected Color errorColor = new Color(255, 170, 170);
    protected Color warnColor = new Color(255, 204, 170);
    protected Color normalColor = Color.blue;
    protected ExecutorService asyncUpdateExec;
    private volatile boolean isUpdateProcessing = false;
    private JFreeChart lastNotProcessedChart;
    private QLabel begTimeLabel;
    private QLabel endTimeLabel;
    private QLabel timeLabel;

    protected Timestamp begTime;
    protected Timestamp endTime;
    private final IClientEnvironment env;

    protected DiagramWidget(final IClientEnvironment env, final QWidget parent) {
        super(parent);
        this.parent = parent;
        //this.setMinimumHeight(minheight);
        this.setSizePolicy(Policy.Expanding, Policy.Expanding);
        final QVBoxLayout mainlayout = new QVBoxLayout();
        mainlayout.setSpacing(0);
        mainlayout.setMargin(0);
        mainlayout.setContentsMargins(0, 0, 0, 0);
        //setStyleSheet("* {border: 1px solid red; }");
        this.setLayout(mainlayout);
        this.env = env; 
    }

    public void setAsyncExecutor(final ExecutorService exec) {
        asyncUpdateExec = exec;
    }

    public ExecutorService getAsyncExecutor() {
        return asyncUpdateExec;
    }

    public Color getErrorColor() {
        return errorColor;
    }

    public Color getWarningColor() {
        return warnColor;
    }

    public Color getNormalColor() {
        return normalColor;
    }

    public void setErrorColor(final Color color) {
        errorColor = color;
    }

    public void setWarningColor(final Color color) {
        warnColor = color;
    }

    public void setNormalColor(final Color color) {
        normalColor = color;
    }

    public void setErrorArea(final double lowErrorValue, final double highErrorValue) {
        setMarkers(lowErrorValue, highErrorValue, errorColor, chart.getXYPlot());
    }

    public void setWarnArea(final double lowWarnValue, final double highWarnValue) {
        setMarkers(lowWarnValue, highWarnValue, warnColor, chart.getXYPlot());
    }

    public void update(/*List<MetricValue>*/final DiagramRs vals, /*Timestamp begTime, Timestamp endTime,*/ final DiagramSettings metricSettings, final boolean isAsinc) {
        setNormalColor(new Color(metricSettings.getNormColor().getRGB()));
        setWarningColor(new Color(metricSettings.getWarningColor().getRGB()));
        setErrorColor(new Color(metricSettings.getErrorColor().getRGB()));
    }

    @Override
    protected void resizeEvent(final QResizeEvent qre) {
        super.resizeEvent(qre);
        if (this.width() > 0 && this.height() > 0 && begTime != null && endTime != null) {
            setDiagram(this.width(), this.height());
        }
    }

    public void setValueScale(final double minVal, final double maxVal) {
        final XYPlot plot = chart.getXYPlot();
        plot.getRangeAxis().setAutoRange(false);

        /*if(valScale==null){
         valScale=new double[]{minVal,maxVal};
         }else{
         valScale[0]=minVal;
         valScale[1]=maxVal;
         }*/
        plot.getRangeAxis().setRange(minVal, maxVal);
        //repaintDiagram();
    }

    //public void repaintDiagram(){
    //   setDiagram(parent.width(), parent.height()/3); 
    //}
    public void setAutoValueRange() {
        final XYPlot plot = chart.getXYPlot();
        plot.getRangeAxis().setAutoRange(true);
    }

    public void setTimeScale(final double begTime, final double endTime) {
        if (begTime < endTime) {
            final XYPlot plot = chart.getXYPlot();
            plot.getDomainAxis().setLowerBound(begTime);//Range(begTime, endTime);
            //repaintDiagram();
        }
    }

    public double getMaxVal() {
        final XYPlot plot = chart.getXYPlot();
        return plot.getRangeAxis().getRange().getUpperBound();
    }

    public double getMinVal() {
        final XYPlot plot = chart.getXYPlot();
        return plot.getRangeAxis().getRange().getLowerBound();
    }

    protected void setDiagram(final int width, final int height) {
        if (this.width() > 0 && this.height() > 0) {
            if (image != null) {
                final int axisWidgetHeight = axisWidget != null ? axisWidget.height() : 0;
                if (asyncUpdateExec != null) {
                    if (!isUpdateProcessing) {
                        try {
                            final Runnable createImageTask = new CreateImageTask((JFreeChart) chart.clone(), this.width(), this.height() - axisWidgetHeight);
                            if (!asyncUpdateExec.isShutdown() && !asyncUpdateExec.isTerminated()) {
                                asyncUpdateExec.submit(createImageTask);
                            }
                        } catch (CloneNotSupportedException ex) {
                            Logger.getLogger(DiagramWidget.class.getName()).log(Level.SEVERE, "Can't clone chart to create async update task", ex);
                        }
                    } else {
                        lastNotProcessedChart = chart;
                    }
                } else { //Non-async update
                    try {
                        final BufferedImage bufimage = chart.createBufferedImage(this.width(), this.height() - axisWidgetHeight);
                        image.setImage(bufimage);
                        createHorizontalAxisWidget(image.getAxisXCoordInPixels(), image.getBackgroundColor());
                    } catch (IOException ex) {
                        Logger.getLogger(DiagramWidget.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                final BufferedImage bufimage = chart.createBufferedImage(this.width(), this.height());
                try {
                    image = new ImageWidget(this, bufimage);
                    image.setSizePolicy(Policy.Expanding, Policy.Expanding);
                    image.setContentsMargins(0, 0, 0, 0);
                    image.setAlignment(AlignmentFlag.AlignRight);
                    layout().addWidget(image);
                    createHorizontalAxisWidget(image.getAxisXCoordInPixels(), image.getBackgroundColor());
                } catch (IOException ex) {
                    Logger.getLogger(DiagramWidget.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    protected void createHorizontalAxisWidget(int posAxisX, QColor colorBackground) {
        if (axisWidget == null) {
            axisWidget = new QWidget(this);
           
            axisWidget.setContentsMargins(0, 0, 0, 5); 
            axisWidget.setSizePolicy(Policy.Expanding, Policy.Minimum); 
            
            axisWidget.setAutoFillBackground(true);
            
            begTimeLabel = new QLabel(axisWidget);
            endTimeLabel = new QLabel(axisWidget);
            timeLabel = new QLabel(axisWidget);
            timeLabel.setMargin(posAxisX);
            timeLabel.setText("Time");

            begTimeLabel.setAlignment(AlignmentFlag.AlignLeft);
            endTimeLabel.setAlignment(AlignmentFlag.AlignRight);
            timeLabel.setAlignment(AlignmentFlag.AlignCenter);

            begTimeLabel.setSizePolicy(Policy.Minimum, Policy.Minimum);
            endTimeLabel.setSizePolicy(Policy.Minimum, Policy.Minimum);
            timeLabel.setSizePolicy(Policy.Expanding, Policy.Minimum);

            endTimeLabel.setContentsMargins(0, 0, 7, 0);
            timeLabel.setContentsMargins(0, 0, 0, 0);
            
            QHBoxLayout timeLayout = new QHBoxLayout();
            timeLayout.setContentsMargins(0, 0, 0, 0);
            axisWidget.setLayout(timeLayout);

            timeLayout.addWidget(begTimeLabel);
            timeLayout.addWidget(timeLabel);
            timeLayout.addWidget(endTimeLabel);
            layout().addWidget(axisWidget);
            
            QFont font = axisWidget.font().clone(); 
            font.setFamily(org.jfree.chart.axis.Axis.DEFAULT_TICK_LABEL_FONT.getFamily()); 
            
            begTimeLabel.setFont(font);
            timeLabel.setFont(font);
            endTimeLabel.setFont(font);
        }
        timeLabel.setAutoFillBackground(true);
        begTimeLabel.setAutoFillBackground(true);
        endTimeLabel.setAutoFillBackground(true);
        
        QPalette palette = new QPalette();
        palette.setColor(begTimeLabel.backgroundRole(), colorBackground);
        
        begTimeLabel.setPalette(palette);
        endTimeLabel.setPalette(palette);
        timeLabel.setPalette(palette);
        axisWidget.setPalette(palette);
        
        begTimeLabel.setText(new EditMaskDateTime(EDateTimeStyle.LONG, EDateTimeStyle.NONE, null, null).toStr(env, begTime));
        endTimeLabel.setText(new EditMaskDateTime(EDateTimeStyle.LONG, EDateTimeStyle.NONE, null, null).toStr(env, endTime));
      
        begTimeLabel.setContentsMargins(posAxisX, 0, 0, 0);
    }

    public static class AsyncImageUpdateEvent extends QEvent {

        private final BufferedImage awtImage;

        public AsyncImageUpdateEvent(final BufferedImage awtImage) {
            super(QEvent.Type.User);
            this.awtImage = awtImage;
        }

        BufferedImage getAwtImage() {
            return awtImage;
        }
    }

    public class CreateImageTask implements Runnable {

        private final JFreeChart chart;
        private final int width, height;

        public CreateImageTask(final JFreeChart chart, final int w, final int h) {
            this.chart = chart;
            width = w;
            height = h;
        }

        @Override
        public void run() {
            try {
                isUpdateProcessing = true;
                final BufferedImage bufimage = chart.createBufferedImage(width, height);
                QApplication.postEvent(DiagramWidget.this, new AsyncImageUpdateEvent(bufimage));
            } finally {
                isUpdateProcessing = false;
            }
        }
    }

    @Override
    protected void customEvent(final QEvent qevent) {
        if (qevent instanceof AsyncImageUpdateEvent) {
            qevent.accept();
            try {
                image.setImage(((AsyncImageUpdateEvent) qevent).getAwtImage());
                createHorizontalAxisWidget(image.getAxisXCoordInPixels(), image.getBackgroundColor());
            } catch (IOException ex) {
                Logger.getLogger(DiagramWidget.class.getName()).log(Level.SEVERE, "Can't load pixmap for diagram", ex);
            }

            if (lastNotProcessedChart != null) {
                try {
                    final Runnable createImageTask = new CreateImageTask((JFreeChart) lastNotProcessedChart.clone(), this.width(), this.height());
                    if (!asyncUpdateExec.isShutdown() && !asyncUpdateExec.isTerminated()) {
                        asyncUpdateExec.submit(createImageTask);
                    }
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(DiagramWidget.class.getName()).log(Level.SEVERE, "Can't clone chart to create async update task", ex);
                } finally {
                    lastNotProcessedChart = null;
                }
            }
        } else {
            super.customEvent(qevent);
        }
    }

    protected void setMarkers(final double val1, final double val2, final Color color, final XYPlot plot) {
        final Marker errorMark = createMarker(val1, val2, color);
        errorMark.setOutlinePaint(null);
        plot.addRangeMarker(errorMark);
    }

    public void clearMarkers() {
        final XYPlot plot = chart.getXYPlot();
        plot.clearRangeMarkers();

    }

    private Marker createMarker(final double val1, final double val2, final Color color) {
        final Marker errorMark = new IntervalMarker(val1, val2);
        errorMark.setPaint(color);
        errorMark.setAlpha(0.3f);
        return errorMark;
    }
}
