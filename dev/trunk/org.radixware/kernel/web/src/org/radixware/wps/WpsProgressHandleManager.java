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

package org.radixware.wps;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.progress.ProgressHandleManager;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.views.IProgressHandle.Cancellable;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.exceptions.AppError;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.html.IHtmlContext;
import org.radixware.kernel.common.html.IHtmlUser;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.Events;
import org.radixware.wps.rwt.PushButton;
import org.radixware.wps.rwt.RootPanel;
import org.radixware.wps.views.RwtAction;


public class WpsProgressHandleManager implements ProgressHandleManager {
    
    private final static class ActionWrapper{
        
        private final RwtAction action;
        private final String cancelProgressText;
        
        public ActionWrapper(final RwtAction action, final String cancelProgressText){
            this.action=action;
            this.cancelProgressText = cancelProgressText;
        }
        
        public RwtAction getAction(){
            return action;
        }
        
        public String getCancelProgressText(){
            return cancelProgressText;
        }
    }
    
    private final static class CancelAction extends RwtAction{
        
        public CancelAction(final IClientEnvironment environment){
            super(environment);
            setText(environment.getMessageProvider().translate("Wait Dialog", "Cancel"));
            setTextShown(true);
            setObjectName("cancel");
        }
    }
    
    private final static class ActionButton extends PushButton implements RwtAction.IActionPresenter {

        private final RwtAction action;

        private ActionButton(final ActionWrapper actionWrapper) {
            this.action = actionWrapper.getAction();
            action.addActionPresenter(this);
            actionStateChanged(action);
            html.setAttr("onclick", "$RWT.waitWindow.onButtonClick");
            final String cancelProgressText = actionWrapper.getCancelProgressText();
            if (cancelProgressText!=null && !cancelProgressText.isEmpty()){
                getHtml().setAttr("cancel-progress-text", cancelProgressText);
            }
        }

        @Override
        public final void actionStateChanged(final Action a) {
            this.setIcon(a.getIcon());
            this.setToolTip(a.getToolTip());
            this.setVisible(a.isVisible());
            this.setEnabled(a.isEnabled());
            this.setText(a.getText());
            final String actionName = a.getObjectName();
            if (actionName != null && !actionName.isEmpty()) {
                setObjectName("rx_pbtn_" + actionName);
            }            
        }
    }    
    
    private final static class ButtonBox extends Div{                
        
        private final Map<Action,ActionButton> buttonsByAction = new HashMap<>();
        private final Map<String,Action> actionsById = new HashMap<>();
        private final List<ActionWrapper> actions = new LinkedList<>();        
        private final IHtmlContext htmlContext = new IHtmlContext() {

            @Override
            public IHtmlUser getExplicitChild() {
                return null;
            }

            @Override
            public Html getHtml() {
                return null;
            }
        };
                
        private void ButtonBox(){
        }
        
        private void addAction(final ActionWrapper actionWrapper){
            final RwtAction action = actionWrapper.getAction();
            final ActionButton button = new ActionButton(actionWrapper);
            buttonsByAction.put(action, button);
            actionsById.put(button.getHtmlId(), action);
            actions.add(actionWrapper);
            add(button.getHtml());
        }
        
        public boolean setActions(final List<ActionWrapper> actions){
            if (this.actions.equals(actions)){
                return false;
            }else{
                for (PushButton btn: buttonsByAction.values()){
                    remove(btn.getHtml());
                }
                buttonsByAction.clear();
                actionsById.clear();
                this.actions.clear();
                for (ActionWrapper action: actions){
                    addAction(action);
                }
                return true;
            }
        }
        
        public void clearActions(){
            actions.clear();
        }
        
        public String getChanges(){
            final StringBuilder html = new StringBuilder();
            saveChanges(htmlContext, html);
            return html.toString();
        }
        
        public Action getActionByButtonId(final String buttonId){
            return actionsById.get(buttonId);
        }
    }

    final class ProgressHandleInfo {

        private String text;
        private boolean forced;
        private String title;
        private int value;
        private int maxValue;
        private String command;
        private List<ActionWrapper> actions;
        
        public void saveChanges(final HttpResponseContent response) throws IOException {
            final StringBuilder builder = new StringBuilder();            
            builder.append("<waitbox command = \"");
            builder.append(command);
            builder.append('\"');
            response.writeResponseXmlAttrs(builder, false);
            boolean needToCloseTag = false;
            if (!"ping".equals(command) && !"hide".equals(command)) {
                if (text != null) {
                    builder.append(" text=\"");
                    builder.append(text);
                    builder.append('\"');
                }
                if (forced && "show".equals(command)) {
                    builder.append(" forced=\"true\"");
                }
                if (title != null) {
                    builder.append(" title=\"");
                    builder.append(title);
                    builder.append('\"');
                }
                if (maxValue > 0) {
                    builder.append(" value=\"");
                    builder.append(value);                    
                    
                    builder.append("\" max-value=\"");
                    builder.append(maxValue);
                    builder.append('\"');
                }
                if (actions!=null && !actions.isEmpty()){
                    builder.append(" buttonsPresent=\"true\"");
                }
                if (actions!=null && getButtonBox().setActions(actions)){
                    final String buttonBoxHtml = getButtonBox().getChanges();
                    if (buttonBoxHtml!=null && !buttonBoxHtml.isEmpty()){
                        builder.append(">\n\t<buttons>\n");
                        builder.append(buttonBoxHtml);
                        builder.append("\n\t</buttons>\n");
                        needToCloseTag = true;
                    }
                }
                WpsProgressHandleManager.this.lastUpdateTime = System.currentTimeMillis();
            }
            if (needToCloseTag){
                builder.append("\n</waitbox>");
            }else{
                builder.append("/>");
            }

            try {
                response.writeString(builder.toString());                
            } catch (IOException ex) {
                throw ex;
            }
        }

        public void showDialog() {
            command = "show";
            updateState();
        }

        public void hideDialog() {
            command = "hide";
            updateState();
        }

        public void ping() {
            command = "ping";
            updateState();
        }
        
        public void update() {
            command = "update";
            updateState();
        }        

        public void updateState() {
            title = WpsProgressHandleManager.this.env.getMessageProvider().translate("Wait Dialog", "Please wait...");
            final WpsProgressHandle activeHandle = (WpsProgressHandle)getActive();
            if (activeHandle != null) {
                text = activeHandle.getText();
                value = activeHandle.getValue();
                maxValue = activeHandle.getMaximumValue();
                forced = activeHandle.forcedShow();
                final List<ActionWrapper> handleActions = activeHandle.getActions();
                if (handleActions==null || handleActions.isEmpty()){
                    actions = null;
                }else{
                    actions = new LinkedList<>(handleActions);
                }
            }
        }
        
        void merge(final ProgressHandleInfo other){
            if ("ping".equals(command)){
                command = other.command;
            }
        }
    }    

    public static class WpsProgressHandle implements IProgressHandle {

        WpsProgressHandleManager manager;
        private boolean isActive = false;
        private final Object semaphore = new Object();
        private int maxValue = 0;
        private int value = 0;
        private String text;
        private String title;
        private boolean wasCancelled = false;
        private boolean isCancellable = false;
        private boolean forcedShow = false;
        private Action cancelAction;
        private final List<ActionWrapper> actions = new LinkedList<>();

        public WpsProgressHandle(final WpsProgressHandleManager manager) {
            this.manager = manager;        }

        @Override
        public void startProgress(final String text, boolean canCancel) {
            startProgress(text, canCancel, false);
        }

        @Override
        public void startProgress(String text, boolean canCancel, boolean forcedShow) {
            isActive = true;
            manager.buttonBox.clearActions();
            setText(text);
            this.forcedShow = forcedShow;
            if (canCancel){
                if (cancelAction==null){
                    final String cancellingText = 
                        manager.getEnvironment().getMessageProvider().translate("Wait Dialog", "Cancelling operation...");
                    addAction(new CancelAction(manager.getEnvironment()), cancellingText, true);
                }
            }else if (cancelAction!=null){
                removeAction(cancelAction);
            }
            this.isCancellable = canCancel;
            manager.startHandle(this);
        }

        void reset() {
            setText("");
            setMaximumValue(-1);
            setValue(0);
            isActive = false;
            manager.buttonBox.clearActions();
            actions.clear();
            manager.updateHandles();            
        }

        @Override
        public boolean isActive() {
            return isActive;
        }

        @Override
        public void cancel() {
            synchronized(semaphore){
                wasCancelled = true;
                if (cancelAction!=null){
                    cancelAction.trigger();
                }
            }            
        }

        @Override
        public boolean wasCanceled() {
            synchronized(semaphore){
                return wasCancelled;
            }
        }

        @Override
        public void finishProgress() {
            isActive = false;
            manager.stopHandle(this);
            manager.buttonBox.clearActions();
        }

        @Override
        public void setTitle(String title) {
            this.title = title;
            manager.updateActiveHandle();
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public void setText(String text) {
            this.text = text;
            manager.updateActiveHandle();
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public void setMaximumValue(int value) {
            maxValue = value;
            manager.updateActiveHandle();
        }

        @Override
        public int getMaximumValue() {
            return maxValue;

        }

        @Override
        public void setValue(int value) {
            this.value = Math.min(value, maxValue);
            manager.updateActiveHandle();
        }

        @Override
        public void incValue() {
            if (value + 1 <= maxValue) {
                value++;
            }
            manager.updateActiveHandle();
        }

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public boolean canCancel() {
            return isCancellable;
        }

        public boolean forcedShow() {
            return forcedShow;
        }
        
        public void addAction(final Action action, final String cancellingProgressText, final boolean isCancelAction){
            actions.add(new ActionWrapper((RwtAction)action, cancellingProgressText));
            if (isCancelAction){
                isCancellable = true;
                cancelAction = action;
            }
            manager.updateActiveHandle();
        }
        
        public void removeAction(final Action action){
            for (int i=actions.size()-1; i>=0; i--){
                if (actions.get(i).getAction()==action){
                    actions.remove(i);
                }
            }
            if (cancelAction==action){
                cancelAction = null;
                isCancellable = false;
            }
            manager.updateActiveHandle();
        }
        
        List<ActionWrapper> getActions(){
            return actions;
        }
        
        void processRequest(final HttpQuery query){
            final List<HttpQuery.EventSet> events = query.getEvents();
            Action action;
            for (HttpQuery.EventSet event: events){
                if ("wait-box".equals(event.getEventWidgetId()) 
                    && Events.isActionEvent(event.getEventName())
                    && "button".equals(Events.getActionName(event))){
                    action = manager.getButtonBox().getActionByButtonId(event.getEventParam());
                    if (action!=null){
                        if (action==cancelAction){
                            cancel();
                        }else{
                            action.trigger();
                        }
                        break;
                    }
                }
            }
        }
    }
    
    
    private Stack<WpsProgressHandle> activeHandles;
    private final WpsEnvironment env;
    private final ButtonBox buttonBox = new ButtonBox();
    private boolean selfCheckEnabled = true;
    private int blockCount = 0;
    private long lastUpdateTime;
    private final ArrayList<String> blockProgressTraceCall = new ArrayList<>();
    private final SimpleDateFormat traceCallTimeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    private final RwtAction standardCancelAction;

    public WpsProgressHandleManager(final WpsEnvironment env) {
        this.env = env;
        standardCancelAction = new CancelAction(env);
        standardCancelAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(final Action action) {
                WpsProgressHandleManager.this.env.getEasSession().breakRequest();
            }
        });        
    }

    void updateHandles() {
        checkOverlayState();
    }
    
    void updateActiveHandle(){
        if (getActive() != null) {
            ProgressHandleInfo info = new ProgressHandleInfo();
            info.update();            
            env.context.updateProgressManagerState(info);
        }
    }

    void startHandle(WpsProgressHandle handle) {
        if (activeHandles == null) {
            activeHandles = new Stack<>();
        }
        activeHandles.push(handle);
        checkOverlayState();
    }

    void stopHandle(WpsProgressHandle handle) {
        boolean wasChanges = false;
        if (activeHandles != null && activeHandles.contains(handle)) {
            do {
                WpsProgressHandle h = activeHandles.pop();
                wasChanges = true;
                if (h == handle) {
                    break;
                }
            } while (!activeHandles.isEmpty());
        }
        if (wasChanges) {
            checkOverlayState();
        }
    }

    private void checkOverlayState() {
        if (getActive() == null) {
            hideOverlay();
        } else {
            showOverlay();
        }
    }

    public RootPanel findRoot() {
        return env.getMainWindow();
    }

    private void hideOverlay() {
        ProgressHandleInfo info = new ProgressHandleInfo();
        info.hideDialog();
        env.context.updateProgressManagerState(info);
    }

    private void showOverlay() {
        if (!isBlocked()) {
            ProgressHandleInfo info = new ProgressHandleInfo();
            info.showDialog();
            env.context.updateProgressManagerState(info);
        }
    }

    @Override
    public void blockProgress() {
        blockProgress(null);
    }

    private boolean isBlocked() {
        return blockCount > 0;
    }

    void blockProgress(Dialog cause) {
        blockCount++;
        blockTraceCall("blocking progress");
        if (blockCount == 1 && getActive() != null) {
            ProgressHandleInfo info = new ProgressHandleInfo();
            info.hideDialog();
            env.context.updateProgressManagerState(info);
        }

    }

    @Override
    public void unblockProgress() {
        unblockProgress(null);
    }

    void unblockProgress(Dialog cause) {
        if (blockCount > 0) {
            blockCount--;
            blockTraceCall("unblocking progress");
            if (blockCount == 0 && getActive() != null) {
                final ProgressHandleInfo info = new ProgressHandleInfo();
                info.showDialog();                
                env.context.updateProgressManagerState(info);
            }
        }
    }
    
    private void blockTraceCall(final String message){
        final String time = message+" at "+traceCallTimeFormat.format(new Date(System.currentTimeMillis()));
        blockProgressTraceCall.add(ClientException.exceptionStackToString(new AppError(time)));
        if (blockProgressTraceCall.size()>64){
            blockProgressTraceCall.remove(0);
        }
    }
    
    void disableSelfCheck(){
        selfCheckEnabled = false;
    }
    
    void cancellAll(){        
        while (!activeHandles.isEmpty()){
            IProgressHandle handle = activeHandles.pop();
            handle.cancel();
        }
    }
        
    public void assertProgressUnblocked(final String message){
        if (blockCount > 0 && selfCheckEnabled){
            final StringBuilder traceMessageBuilder = new StringBuilder(message);
            for (String traceCall: blockProgressTraceCall){
                traceMessageBuilder.append('\n');
                traceMessageBuilder.append(traceCall);
            }
            env.getTracer().error(traceMessageBuilder.toString());
            blockCount = 0;            
            if (getActive() != null) {
                final ProgressHandleInfo info = new ProgressHandleInfo();
                info.showDialog();
                env.context.updateProgressManagerState(info);
            }
        }
        blockProgressTraceCall.clear();
    }    

    public void ping() {
        if (!isBlocked()) {
            final long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdateTime >= WpsEnvironment.PING_PERIOD){
                final ProgressHandleInfo info = new ProgressHandleInfo();
                info.ping();
                env.context.updateProgressManagerState(info);
            }
        }
    }

    @Override
    public IProgressHandle getActive() {
        return activeHandles == null || activeHandles.isEmpty() ? null : activeHandles.peek();
    }

    @Override
    public IProgressHandle newProgressHandle() {
        return new WpsProgressHandle(this);
    }

    @Override
    public IProgressHandle newProgressHandle(final Cancellable onCancelHandler) {
        final WpsProgressHandle progressHandle = new WpsProgressHandle(this);
        final RwtAction action = new CancelAction(env);
        action.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(final Action action) {
                onCancelHandler.onCancel();
            }
        });
        final String cancellingText = 
            env.getMessageProvider().translate("Wait Dialog", "Cancelling operation...");
        progressHandle.addAction(action, cancellingText, true);
        return progressHandle;
    }

    @Override
    public IProgressHandle newStandardProgressHandle() {
        final WpsProgressHandle progressHandle = new WpsProgressHandle(this);
        final String cancellingText = 
            env.getMessageProvider().translate("Wait Dialog", "Cancelling request...");
        progressHandle.addAction(standardCancelAction, cancellingText, true);
        return progressHandle;
    }
    
    private ButtonBox getButtonBox(){
        return buttonBox;
    }
    
    private IClientEnvironment getEnvironment(){
        return env;
    }
}
