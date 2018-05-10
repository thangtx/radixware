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

import com.trolltech.qt.QNoNativeResourcesException;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.types.ICachableWidget;


public final class LeakedWidgetsDetector {
    
    public interface ITracableWidget{
        String getCreationStackTrace();
    }
    
    private final static class WidgetInfo{
        
        private final long nativeId;
        private final String widgetClassName;
        private final String objectDescription;
        private final String creationStackTrace;
        private final boolean isPopup;
        
        public WidgetInfo(final QWidget widget){
            nativeId = widget.nativeId();
            widgetClassName = widget.getClass().getName();
            isPopup = widget.windowFlags().isSet(Qt.WindowType.Popup);
            final StringBuilder descriptionBuilder = new StringBuilder();
            printPropName("name",widget.objectName(),descriptionBuilder);
            printPropName("toolTip",widget.toolTip(),descriptionBuilder);
            printPropName("statusTip",widget.statusTip(),descriptionBuilder);
            if (widget instanceof QAbstractButton){
                printPropName("text", ((QAbstractButton)widget).text(), descriptionBuilder);
            }
            if (widget instanceof QMenu){
                final QMenu menu = (QMenu)widget;
                printPropName("title", menu.title(), descriptionBuilder);
                final List<QAction> actions = menu.actions();
                if (actions!=null && !actions.isEmpty()){
                    if (descriptionBuilder.length()>0){
                        descriptionBuilder.append(", ");
                    }
                    descriptionBuilder.append("actions: [");
                    final StringBuilder actionDescriptionBuilder = new StringBuilder();
                    boolean firstAction = true;
                    for (QAction action: actions){
                        if (action.nativeId()!=0){
                            if (firstAction){
                                firstAction = false;
                            }else{
                                actionDescriptionBuilder.setLength(0);
                                descriptionBuilder.append(", ");
                            }
                            descriptionBuilder.append('{');
                            printActionDescription(action, actionDescriptionBuilder);
                            descriptionBuilder.append(actionDescriptionBuilder.toString());
                            descriptionBuilder.append('}');
                        }
                    }
                    descriptionBuilder.append(']');
                }
            }
            if (descriptionBuilder.length()>0){
                descriptionBuilder.insert(0, "{ ");
                descriptionBuilder.append('}');
                objectDescription = descriptionBuilder.toString();
            }else{
                objectDescription = null;
            }
            if (widget instanceof ITracableWidget){
                creationStackTrace =  ((ITracableWidget)widget).getCreationStackTrace();
            }else{
                creationStackTrace  = widget.getCreationStackTrace();
            }
        }
        
        private static void printActionDescription(final QAction action, final StringBuilder out){            
            printPropName("text",action.text(),out);
            printPropName("toolTip",action.toolTip(),out);
            printPropName("statusTip",action.statusTip(),out);
            printPropName("iconText",action.iconText(),out);
        }
        
        private static void printPropName(final String propName, final String propVal, final StringBuilder out){
            if (propVal!=null && !propVal.isEmpty()){
                if (out.length()>0){
                    out.append(", ");
                }
                out.append(propName);
                out.append(": \'");
                out.append(propVal);
                out.append('\'');
            }
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
            if (objectDescription==null || objectDescription.isEmpty()){
                return widgetClassName;
            }else{
                return widgetClassName+" ("+objectDescription+")";
            }
        }

        public String getClassName() {
            return widgetClassName;
        }

        public String getObjectDescription() {
            return objectDescription;
        }
        
        public String getCreationStackTrace(){
            return creationStackTrace;
        }
        
        public boolean isPopup(){
            return isPopup;
        }
    }    
        
    private static final class TopLevelWidgets{
        
        private final List<WidgetInfo> widgets = new ArrayList<>();
        private final String contextDescription;
        
        private TopLevelWidgets(final String contextDescription){
            this.contextDescription = contextDescription;
        }
        
        public static TopLevelWidgets find(final String contextDesc){
            final TopLevelWidgets instance = new TopLevelWidgets(contextDesc);
            final List<QWidget> widgets = QApplication.topLevelWidgets();
            for (QWidget widget: widgets){
                if (widget!=null
                    && widget.nativeId()!=0
                    && widget.parent()==null
                    && (widget instanceof ICachableWidget==false || !((ICachableWidget)widget).isInCache())
                    && (widget instanceof ExplorerDialog==false || !widget.isVisible())
                   ){
                    try{
                        instance.add(new WidgetInfo(widget));
                    }catch(QNoNativeResourcesException exception){

                    }
                }
            }
            return instance;
        }
        
        public void add(WidgetInfo widgetInfo){
            widgets.add(widgetInfo);
        }
        
        public void remove(WidgetInfo widgetInfo){
            widgets.remove(widgetInfo);
        }
        
        public void add(final TopLevelWidgets other){
            widgets.addAll(other.widgets);
        }        
        
        public boolean isEmpty(){
            return widgets.isEmpty();
        }
        
        public void removeComplementOf(final TopLevelWidgets other){
            for (int i=widgets.size()-1; i>=0; i--){
                if (!other.widgets.contains(widgets.get(i))){
                    widgets.remove(i);
                }
            }
        }
        
        public TopLevelWidgets getCompelmentOf(final TopLevelWidgets other, final boolean ignorePopup){
            final TopLevelWidgets complement = new TopLevelWidgets(other.contextDescription);
            for (WidgetInfo widgetInfo: other.widgets){
                if (!widgets.contains(widgetInfo) && (!widgetInfo.isPopup() || !ignorePopup)){
                    complement.add(widgetInfo);
                }
            }
            return complement;
        }
                
        private String print(final MessageProvider mp){
            if (widgets.size()==1){
                final StringBuilder messageBuilder = new StringBuilder();
                final WidgetInfo widgetInfo = widgets.get(0);
                final String objectDescription = widgetInfo.getObjectDescription();
                final String messageTemplate;                    
                final String message;
                if (objectDescription==null || objectDescription.isEmpty()){
                    if(contextDescription==null || contextDescription.isEmpty()){                            
                        messageTemplate = 
                            mp.translate("TraceMessage", "The instance of \'%1$s\' class was not disposed.");
                        message = String.format(messageTemplate,widgetInfo.getClassName());
                    }else{
                        messageTemplate = 
                            mp.translate("TraceMessage", "The instance of \'%1$s\' class was not disposed in \'%2$s\'.");
                        message = String.format(messageTemplate,widgetInfo.getClassName(), contextDescription);
                    }
                }else{
                    if (contextDescription==null || contextDescription.isEmpty()){
                        messageTemplate = 
                            mp.translate("TraceMessage", "The instance of \'%1$s\' (%2$s) was not disposed.");
                        message = String.format(messageTemplate,widgetInfo.getClassName(),objectDescription);                            
                    }else{
                        messageTemplate = 
                            mp.translate("TraceMessage", "The instance of \'%1$s\' class (%2$s) was not disposed in \'%3$s\'.");
                        message = String.format(messageTemplate,widgetInfo.getClassName(),objectDescription, contextDescription);
                    }
                }
                messageBuilder.append(message);
                final String trace = widgetInfo.getCreationStackTrace();
                if (trace!=null && !trace.isEmpty()){
                    messageBuilder.append('\n');
                    messageBuilder.append(mp.translate("TraceMessage", "Creation stack trace:"));
                    messageBuilder.append('\n');
                    messageBuilder.append(trace);
                }
                return messageBuilder.toString();
            }else if (!widgets.isEmpty()){                
                final StringBuilder messageBuilder = new StringBuilder();
                if (contextDescription==null || contextDescription.isEmpty()){
                    messageBuilder.append(mp.translate("TraceMessage", "Following instances was not disposed:"));
                }else{
                    final String messageTemplate = 
                        mp.translate("TraceMessage", "Following instances was not disposed in \'%1$s\':");
                    messageBuilder.append(String.format(messageTemplate, contextDescription));
                }
                messageBuilder.append('\n');
                print(mp, messageBuilder);
                return messageBuilder.toString();
            }else{
                return "";
            }
        }
        
        private void print(final MessageProvider mp, final StringBuilder stringBuilder){
            boolean first = true;
            for (WidgetInfo widgetInfo: widgets){
                if (first){
                    first = false;
                }else{
                    stringBuilder.append('\n');
                }
                stringBuilder.append(widgetInfo.toString());
                final String trace = widgetInfo.getCreationStackTrace();
                if (trace!=null && !trace.isEmpty()){
                    stringBuilder.append('\n');
                    stringBuilder.append(mp.translate("TraceMessage", "Creation stack trace:"));
                    stringBuilder.append('\n');
                    stringBuilder.append(trace);
                }
            }            
        }
    }
    
    private final static class DialogInfo{
        
        private final TopLevelWidgets topLevelWidgets;
        private final String dialogTitle;

        public DialogInfo(final TopLevelWidgets topLevelWidgets, final String dialogTitle) {
            this.topLevelWidgets = topLevelWidgets;
            this.dialogTitle = dialogTitle;
        }

        public TopLevelWidgets getTopLevelWidgets() {
            return topLevelWidgets;
        }

        public String getDialogTitle() {
            return dialogTitle;
        }                
        
    }

    
    private static final String ENABLE_DETECTOR_PROP_NAME = "org.radixware.kernel.explorer.detect-leaked-widgets";
    
    private TopLevelWidgets topLevelWidgets;           
    
    private final Stack<DialogInfo> dialogs = new Stack<>();        
        
    private final boolean isActive;
    private boolean isEnabled;
    
    private final static LeakedWidgetsDetector INSTANCE = new LeakedWidgetsDetector();

    private LeakedWidgetsDetector(){
        final boolean isDevelopmentMode = RunParams.isDevelopmentMode();
        final boolean isSysPropDefined = System.getProperty(ENABLE_DETECTOR_PROP_NAME)!=null;
        isActive = isDevelopmentMode || isSysPropDefined;
        if (isSysPropDefined){
            isEnabled = true;
        }        
    }        
    
    public static LeakedWidgetsDetector getInstance(){
        return INSTANCE;
    }
    
    public void init(){
        if (isActive){
            doInit();
        }
    }
    
    private void doInit(){
        topLevelWidgets = TopLevelWidgets.find("MainWindow");
    }
    
    public void registerTopLevelWidget(final QWidget w){
        if (isActive && topLevelWidgets!=null){
            addTopLevelWidget(w);
        }
    }
    
    public boolean isEnabled(){
        return isActive && isEnabled;
    }
    
    public void setEnabled(final boolean enabled){
        if (isActive && enabled!=isEnabled){
            isEnabled = enabled;
            if (isEnabled){
                doInit();
            }else{
                topLevelWidgets = null;
            }
        }
    }
    
    public void beforeExecDialog(final IClientEnvironment environment, final QDialog dialog){
        if (isEnabled()){
            final StringBuilder dialogDescriptionBuilder = new StringBuilder();
            final String dialogTitle = dialog.windowTitle();
            if (dialogTitle!=null && !dialogTitle.isEmpty()){
                dialogDescriptionBuilder.append('\"');
                dialogDescriptionBuilder.append(dialogTitle);
                dialogDescriptionBuilder.append("\" (");
            }
            dialogDescriptionBuilder.append(dialog.getClass().getName());
            final String objectName = dialog.objectName();
            if (objectName!=null && !objectName.isEmpty()){
                dialogDescriptionBuilder.append(", ");
                dialogDescriptionBuilder.append(objectName);
            }
            if (dialogTitle!=null && !dialogTitle.isEmpty()){
                dialogDescriptionBuilder.append(" )");
            }
            final String contextDescription = dialogDescriptionBuilder.toString();
            dialogs.push(new DialogInfo(TopLevelWidgets.find(contextDescription), contextDescription));
        }
    }
    
    public void afterExecDialog(final IClientEnvironment environment){
        if (isEnabled()){
            final DialogInfo dialog = dialogs.pop();
            findLeakedWidgets(environment, dialog.getDialogTitle(), true, dialog.getTopLevelWidgets());
        }
    }
    
    private TopLevelWidgets getCurrentTopLevelWidgets(){
        return dialogs.isEmpty() ? topLevelWidgets : dialogs.peek().getTopLevelWidgets();
    }
        
    private TopLevelWidgets findNewTopLevelWidgets(final String title, 
                                                                                 final boolean ignorePopup,
                                                                                 final TopLevelWidgets currentTopLevelWidgets){
        final TopLevelWidgets currentWidgets = TopLevelWidgets.find(title);
        currentTopLevelWidgets.removeComplementOf(currentWidgets);
        return currentTopLevelWidgets.getCompelmentOf(currentWidgets, ignorePopup);
    }            
    
    public void findLeakedWidgets(final IClientEnvironment environment, final String sourceTitle){
        findLeakedWidgets(environment, sourceTitle, false, getCurrentTopLevelWidgets());
    }
            
    @SuppressWarnings("SleepWhileInLoop")
    private TopLevelWidgets findLeakedWidgets(final IClientEnvironment environment, 
                                                                       final String sourceTitle, 
                                                                       final boolean ignorePopup,
                                                                       final TopLevelWidgets topLevelWidgets){
        TopLevelWidgets newWidgets = null;
        if (isEnabled() && topLevelWidgets!=null){
            newWidgets = findNewTopLevelWidgets(sourceTitle, ignorePopup, topLevelWidgets);
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
                    newWidgets = findNewTopLevelWidgets(sourceTitle, ignorePopup, topLevelWidgets);
                }while(!newWidgets.isEmpty() && tryCount<=3);
            }
            final String message = newWidgets.print(environment.getMessageProvider());
            if (message!=null && !message.isEmpty()){
                environment.getTracer().warning(message);
                addNewTopLevelWidgets(newWidgets);                
            }
        }
        return newWidgets;
    }
    
    private void addTopLevelWidget(final QWidget widget){
        final WidgetInfo widgetInfo = new WidgetInfo(widget);
        topLevelWidgets.add(widgetInfo);
        for (DialogInfo dialog: dialogs){
            dialog.getTopLevelWidgets().add(widgetInfo);
        }
    }
    
    private void addNewTopLevelWidgets(final TopLevelWidgets newWidgets){
        topLevelWidgets.add(newWidgets);
        for (DialogInfo dialog: dialogs){
            dialog.getTopLevelWidgets().add(newWidgets);
        }
    }
}
