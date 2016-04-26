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
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;
import org.radixware.kernel.common.client.env.progress.ProgressHandleManager;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.views.IProgressHandle.Cancellable;
import org.radixware.kernel.common.exceptions.AppError;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.RootPanel;


public class WpsProgressHandleManager implements ProgressHandleManager {

    class ProgressHandleInfo {

        private String text;
        private boolean forced;
        private String title;
        private int value;
        private int maxValue;
        private String command;

        public void saveChanges(OutputStream stream) throws IOException {
            StringBuilder builder = new StringBuilder();
            builder.append("<waitbox command = \"");
            builder.append(command);
            if (!"ping".equals(command)) {
                if (text != null) {
                    builder.append("\" text=\"");
                    builder.append(text);
                }
                if (forced && "show".equals(command)) {
                    builder.append("\" forced=\"true");
                }
                if (title != null) {
                    builder.append("\" title=\"");
                    builder.append(title);
                }
                if (maxValue > 0) {
                    builder.append("\" value=\"");
                    builder.append(value);

                    builder.append("\" max-value=\"");
                    builder.append(maxValue);
                }
            }
            builder.append("\"/>");

            try {
                FileUtils.writeString(stream, builder.toString(), FileUtils.XML_ENCODING);
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

        public void updateState() {
            title = WpsProgressHandleManager.this.env.getMessageProvider().translate("Wait Dialog", "Please wait...");
            IProgressHandle activeHandle = getActive();
            if (activeHandle != null) {
                text = activeHandle.getText();
                //   title = activeHandle.getText();
                value = activeHandle.getValue();
                maxValue = activeHandle.getMaximumValue();
                forced = ((ProgressHandleImpl) activeHandle).forcedShow();
            }

        }
    }

    private static class ProgressHandleImpl implements IProgressHandle {

        WpsProgressHandleManager manager;
        private boolean isActive = false;
        private int maxValue = 0;
        private int value = 0;
        private String text;
        private String title;
        private boolean wasCancelled = false;
        private boolean isCancellable = false;
        private boolean forcedShow = false;

        public ProgressHandleImpl(WpsProgressHandleManager manager) {
            this.manager = manager;
        }

        @Override
        public void startProgress(String text, boolean canCancel) {
            startProgress(text, canCancel, false);
        }

        @Override
        public void startProgress(String text, boolean canCancel, boolean forcedShow) {
            isActive = true;
            manager.startHandle(this);
            this.isCancellable = canCancel;
            this.forcedShow = forcedShow;
            setText(text);
        }

        void reset() {
            setText("");
            setMaximumValue(-1);
            setValue(0);
            isActive = false;
            manager.updateHandles();
        }

        @Override
        public boolean isActive() {
            return isActive;
        }

        @Override
        public void cancel() {
            wasCancelled = true;
            isActive = false;
            manager.updateHandles();
        }

        @Override
        public boolean wasCanceled() {
            return wasCancelled;
        }

        @Override
        public void finishProgress() {
            isActive = false;
            manager.stopHandle(this);
        }

        @Override
        public void setTitle(String title) {
            this.title = title;
            manager.updateHandles();
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public void setText(String text) {
            this.text = text;
            manager.updateHandles();
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public void setMaximumValue(int value) {
            maxValue = value;
            manager.updateHandles();
        }

        @Override
        public int getMaximumValue() {
            return maxValue;

        }

        @Override
        public void setValue(int value) {
            this.value = Math.min(value, maxValue);
            manager.updateHandles();
        }

        @Override
        public void incValue() {
            if (value + 1 <= maxValue) {
                value++;
            }
            manager.updateHandles();
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
    }
    private Stack<ProgressHandleImpl> activeHandles;
    private final WpsEnvironment env;
    //  private WaitBox waitBox;
    private int blockCount = 0;
    private final ArrayList<String> blockProgressTraceCall = new ArrayList<>();
    private final SimpleDateFormat traceCallTimeFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    //
    public WpsProgressHandleManager(WpsEnvironment env) {
        this.env = env;
    }

    void updateHandles() {
        checkOverlayState();
    }

    void startHandle(ProgressHandleImpl handle) {
        if (activeHandles == null) {
            activeHandles = new Stack<>();
        }
        activeHandles.push(handle);
        checkOverlayState();
    }

    void stopHandle(ProgressHandleImpl handle) {
        boolean wasChanges = false;
        if (activeHandles != null && activeHandles.contains(handle)) {
            do {
                ProgressHandleImpl h = activeHandles.pop();
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
        
    public void assertProgressUnblocked(final String message){
        if (blockCount > 0){
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
            final ProgressHandleInfo info = new ProgressHandleInfo();
            info.ping();
            env.context.updateProgressManagerState(info);
        }
    }

    @Override
    public IProgressHandle getActive() {
        return activeHandles == null || activeHandles.isEmpty() ? null : activeHandles.peek();
    }

    @Override
    public IProgressHandle newProgressHandle() {
        return new ProgressHandleImpl(this);
    }

    @Override
    public IProgressHandle newProgressHandle(Cancellable onCancelHandler) {
        return newProgressHandle();
    }

    @Override
    public IProgressHandle newStandardProgressHandle() {
        return newProgressHandle();
    }
}
