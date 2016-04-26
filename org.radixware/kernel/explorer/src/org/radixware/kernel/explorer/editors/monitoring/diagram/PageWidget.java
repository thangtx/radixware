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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;
import java.util.ArrayList;
import java.util.List;


public class PageWidget extends QWidget {
    private MetricHistWidget metricHistWidget;
    private boolean isDragging =false;       
    private final List<AbstaractMetricView> metricViews;
        
    List<AbstaractMetricView> getMetricViews(){
            return metricViews;
    }    
    
    PageWidget(MetricHistWidget metricHistWidget,QScrollArea parent){
        super(parent);
        this.metricHistWidget=metricHistWidget;
        metricViews=new ArrayList<>();
    }
    
    public void setIsDragging (boolean b){
        isDragging=b;
    }
    
    @Override
    protected void mouseReleaseEvent(QMouseEvent qme) {
        super.mouseReleaseEvent(qme);
        isDragging=false;
        metricHistWidget.clearSelections();
        repaint();    
    }
    
   /* @Override
    protected void mouseMoveEvent(QMouseEvent qme) {
        super.mouseMoveEvent(qme);
        isDragging=true;
        System.out.println("mouseMoveEvent");
    }*/
    
     @Override
    protected void paintEvent(QPaintEvent qpe) {             
        if(isDragging){             
            QPainter p = new QPainter(this);
            p.setPen(new QPen(QColor.gray,1, Qt.PenStyle.DotLine));
            final int width = width() ;
            final int height = height() ;
            final int deltaX=width/GridLayout.gridSize;
            final int deltaY=height/GridLayout.gridSize;
            for(int i=0;i<16;i++){
                p.drawLine(i*deltaX, 0,i*deltaX,height);
                p.drawLine(0, i*deltaY,width,i*deltaY);
            }
        }
        super.paintEvent(qpe);   
    }
}
