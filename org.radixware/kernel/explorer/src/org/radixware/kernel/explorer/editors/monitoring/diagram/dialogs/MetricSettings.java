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

package org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs;

import com.trolltech.qt.gui.QColor;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.utils.XmlColor;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget;
import org.radixware.schemas.monitoringSettings.MonitoringMetricSettings;


public class MetricSettings extends AbstractMetricSettings{
        private EntityModel metricState;     
        private QColor normColor;
        private QColor warningColor;
        private QColor errorColor;
        private String metricName;
        private long   stateId;
        private String kind;
        public MetricHistWidget.EDiagramType diagramType;
        
        public MetricSettings(EntityModel metricState){
            super();
            setMetricState(metricState);
        }
        
        public long getStateId() {
//            if (metricState == null) {
//                return -1;
//            }
//            return Long.parseLong(metricState.getProperty(Id.Factory.loadFrom("colE6T7IPRVTFFVDKYLOSFZCLMCTI")).getValueAsString());
            return stateId;
        }
        
        public void setStateId(long id) {
            this.stateId = id;
        }
        
        public void loadFromXml(MonitoringMetricSettings settings){
            metricName=settings.isSetMetricName() ? settings.getMetricName(): "";
            title=settings.isSetTitle() ? settings.getTitle():metricName;
            normColor=settings.isSetNormalColor() ? new QColor( XmlColor.parseColor(settings.getNormalColor()).getRGB()) : MetricHistWidget.defaultNornalColor;
            warningColor=settings.isSetWarningColor() ? new QColor( XmlColor.parseColor(settings.getWarningColor()).getRGB()):MetricHistWidget.defaultWarningColor;
            errorColor=settings.isSetErrorColor() ? new QColor( XmlColor.parseColor(settings.getErrorColor()).getRGB()):MetricHistWidget.defaultErrorColor;
         }      
        
        public MetricSettings(){
            super();
        }
        
        public MetricSettings(MetricSettings ms){
            this(ms.getMetricState());
            title=ms.getTitle();
            metricName=ms.getMetricName();    
            normColor=ms.getNormColor().clone();
            warningColor=ms.getWarningColor().clone();
            errorColor=ms.getErrorColor().clone();
        }
        
        public EntityModel getMetricState(){
            return metricState;
        }
        
        public final void setMetricState(EntityModel metricState){
            if(metricState!=null && !metricState.equals(this.metricState)){
                this.metricState=metricState;
            }
        } 
        
        public String getMetricName(){
            return metricName==null ? title : metricName;
        }
        public void setMetricName(String metricName){
            this.metricName=metricName;
        }
        
        public QColor getNormColor(){
            return normColor==null ? MetricHistWidget.defaultNornalColor : normColor;
        }
        public void setNormColor(QColor color){
            normColor=color;
        }
        
        public QColor getWarningColor(){
            return warningColor==null ? MetricHistWidget.defaultWarningColor : warningColor;
        }
        public void setWarningColor(QColor color){
            warningColor=color;
        }
        public QColor getErrorColor(){
            return errorColor==null ? MetricHistWidget.defaultErrorColor : errorColor;
        }
        public void setErrorColor(QColor color){
            errorColor=color;
        }  

        public String getKind() {
            return kind;
//            if (metricState == null) {
//                return null;
//            }
//            return metricState.getProperty(Id.Factory.loadFrom("colDVZEATMUZJDRTHRHWF6HXQ276Q")).getValueAsString();
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public void setDiagramType(MetricHistWidget.EDiagramType diagramType) {
            this.diagramType = diagramType;
        }

        public MetricHistWidget.EDiagramType getDiagramType() {
            return diagramType;     
        }
}
