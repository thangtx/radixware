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

package org.radixware.kernel.explorer.utils;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.explorer.types.ICachableWidget;


public final class LeakedWidgetsDetector {
    
    private List<WidgetInfo> topLevelWidgets;
    private final boolean isDevelopmentMode; 
    private boolean isEnabled;
    
    private final static class WidgetInfo{
        
        private final long nativeId;
        private final String widgetClassName;
        private final String objectName;
        
        public WidgetInfo(final QWidget widget){
            nativeId = widget.nativeId();
            widgetClassName = widget.getClass().getName();
            objectName = widget.objectName();
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + (int) (this.nativeId ^ (this.nativeId >>> 32));
            hash = 37 * hash + Objects.hashCode(this.widgetClassName);
            return hash;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final WidgetInfo other = (WidgetInfo) obj;
            if (this.nativeId != other.nativeId) {
                return false;
            }
            if (!Objects.equals(this.widgetClassName, other.widgetClassName)) {
                return false;
            }
            return true;
        }
        
        @Override
        public String toString(){
            if (objectName==null || objectName.isEmpty()){
                return widgetClassName;
            }else{
                return widgetClassName+" ("+objectName+")";
            }
        }

        public String getClassName() {
            return widgetClassName;
        }

        public String getObjectName() {
            return objectName;
        }                
    }
    
    private final static LeakedWidgetsDetector INSTANCE = new LeakedWidgetsDetector();

    private LeakedWidgetsDetector(){
        isDevelopmentMode = RunParams.isDevelopmentMode();
    }
    
    public static LeakedWidgetsDetector getInstance(){
        return INSTANCE;
    }
    
    public void init(){
        if (isDevelopmentMode){
            topLevelWidgets = getTopLevelWidgetsInfo();
        }
    }
    
    public boolean isEnabled(){
        return isDevelopmentMode && isEnabled;
    }
    
    public void setEnabled(final boolean enabled){
        if (isDevelopmentMode && enabled!=isEnabled){
            isEnabled = enabled;
            if (isEnabled){
                topLevelWidgets = getTopLevelWidgetsInfo();
            }
        }
    }
    
    private List<WidgetInfo> getTopLevelWidgetsInfo(){
        final List<WidgetInfo> widgetsInfo = new LinkedList<>();
        final List<QWidget> widgets = QApplication.topLevelWidgets();
        for (QWidget widget: widgets){
            if (widget!=null
                && widget.nativeId()!=0
                && widget.parent()==null
                && (widget instanceof ICachableWidget==false || !((ICachableWidget)widget).isInCache())
               ){
                widgetsInfo.add(new WidgetInfo(widget));
            }
        }
        return widgetsInfo;
    }
    
    private List<WidgetInfo> findNewTopLevelWidgets(){
        final List<WidgetInfo> newWidgets = new LinkedList<>();
        final List<WidgetInfo> currentWidgets = getTopLevelWidgetsInfo();
        for (int i=topLevelWidgets.size()-1; i>=0; i--){
            if (!currentWidgets.contains(topLevelWidgets.get(i))){
                topLevelWidgets.remove(i);
            }
        }
        for (WidgetInfo widgetInfo: currentWidgets){
            if (!topLevelWidgets.contains(widgetInfo)){
                newWidgets.add(widgetInfo);
            }
        }
        return newWidgets;
    }
    
    private void printWidgetsInfo(final StringBuilder stringBuilder, final List<WidgetInfo> widgetsInfo){
        boolean first = true;
        for (WidgetInfo widgetInfo: widgetsInfo){
            if (first){
                first = false;
            }else{
                stringBuilder.append('\n');
            }
            stringBuilder.append(widgetInfo.toString());
        }
    }
    
    @SuppressWarnings("SleepWhileInLoop")
    public void findLeakedWidgets(final IClientEnvironment environment, final String sourceTitle){
        if (isEnabled() && topLevelWidgets!=null){
            List<WidgetInfo> newWidgets = findNewTopLevelWidgets();
            if (!newWidgets.isEmpty()){
                int tryCount=0;
                do{
                    tryCount++;
                    System.gc();//NOPMD
                    try{
                        Thread.sleep(50);
                    }catch(InterruptedException exception){
                    }
                    QApplication.sendPostedEvents(null, QEvent.Type.DeferredDelete.value());
                    newWidgets = findNewTopLevelWidgets();
                }while(!newWidgets.isEmpty() && tryCount<=3);
            }
            if (!newWidgets.isEmpty()){
                final MessageProvider mp = environment.getMessageProvider();
                if (newWidgets.size()==1){
                    final WidgetInfo widgetInfo = newWidgets.get(0);
                    final String objectName = widgetInfo.getObjectName();
                    final String messageTemplate;                    
                    final String message;
                    if (objectName==null || objectName.isEmpty()){
                        if(sourceTitle==null || sourceTitle.isEmpty()){                            
                            messageTemplate = 
                                mp.translate("TraceMessage", "The instance of \'%1$s\' class was not disposed.");
                            message = String.format(messageTemplate,widgetInfo.getClassName());
                        }else{
                            messageTemplate = 
                                mp.translate("TraceMessage", "The instance of \'%1$s\' class was not disposed in \'%2$s\'.");
                            message = String.format(messageTemplate,widgetInfo.getClassName(),sourceTitle);
                        }
                    }else{
                        if(sourceTitle==null || sourceTitle.isEmpty()){
                            messageTemplate = 
                                mp.translate("TraceMessage", "The instance of \'%1$s\' class with name \'%2$s\' was not disposed.");
                            message = String.format(messageTemplate,widgetInfo.getClassName(),widgetInfo.getObjectName());                            
                        }else{
                            messageTemplate = 
                                mp.translate("TraceMessage", "The instance of \'%1$s\' class with name \'%2$s\' was not disposed in \'%3$s\'.");
                            message = String.format(messageTemplate,widgetInfo.getClassName(),widgetInfo.getObjectName(),sourceTitle);
                        }
                    }
                    environment.getTracer().warning(message);
                }else{
                    final StringBuilder messageBuilder = new StringBuilder();
                    if (sourceTitle==null || sourceTitle.isEmpty()){
                        messageBuilder.append(mp.translate("TraceMessage", "Following instances was not disposed:"));
                    }else{
                        final String messageTemplate = 
                            mp.translate("TraceMessage", "Following instances was not disposed in \'%1$s\':");
                        messageBuilder.append(String.format(messageTemplate, sourceTitle));
                    }
                    messageBuilder.append('\n');
                    printWidgetsInfo(messageBuilder, newWidgets);
                    environment.getTracer().warning(messageBuilder.toString());
                }
                topLevelWidgets.addAll(newWidgets);
            }
        }
    }
}
