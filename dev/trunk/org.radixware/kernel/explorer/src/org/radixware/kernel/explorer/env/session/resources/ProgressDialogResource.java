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

package org.radixware.kernel.explorer.env.session.resources;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.resources.IProgressDialogResource;
import org.radixware.kernel.common.client.eas.resources.IProgressMonitor;
import org.radixware.kernel.explorer.env.UserInputFilter;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.schemas.eas.ProgressDialogSetRq;


class ProgressDialogResource extends QDialog implements IProgressDialogResource {
    
    public final static class UpdateSizeEvent extends QEvent{        
        public UpdateSizeEvent(){
            super(QEvent.Type.User);
        }
    }

    private final static int START_DELAY = 3000;
    private final static int STOP_DELAY = 500;
    private final QObject eventFilter = new UserInputFilter(this);
    private final List<ProgressWidget> processes = new ArrayList<>(8);
    private final QVBoxLayout layout = new QVBoxLayout(this);
    private final QVBoxLayout processLayout = new QVBoxLayout(null);
    private final QPushButton btnCancel;
    private final ProgressTraceWidget progressTrace;
    private int startTimerId = -1, stopTimerId = -1;
    private final IClientEnvironment environment;

    ProgressDialogResource(final IClientEnvironment environment) {
        super((QWidget)environment.getMainWindow());
        this.environment = environment;
        btnCancel = new QPushButton(environment.getMessageProvider().translate("Wait Dialog", "Cancel"), this);
        progressTrace = new ProgressTraceWidget(environment, this);
        setObjectName("Progress Dialog Resource");
        setupUi();
        hide();
    }

    private void setupUi() {        
        final Qt.WindowFlags flags = new Qt.WindowFlags();
        flags.set(new Qt.WindowType[]{Qt.WindowType.Dialog,
                    Qt.WindowType.WindowTitleHint,
                    Qt.WindowType.WindowSystemMenuHint});

        setWindowFlags(flags);
        setModal(true);
        this.setCursor(new QCursor(Qt.CursorShape.ArrowCursor));

        btnCancel.setObjectName("btnCancel");
        btnCancel.clicked.connect(this, "onCancel()");
        btnCancel.setVisible(false);
        progressTrace.setVisible(false);
        layout.addLayout(processLayout);
        layout.addWidget(progressTrace);
        layout.addWidget(btnCancel, 0, Qt.AlignmentFlag.AlignHCenter);
        setLayout(layout);
    }

    @Override
    public long startProcess(final org.radixware.schemas.eas.ProgressDialogStartProcessRq request) {
        final boolean unblockRedraw;
        if (stopTimerId > 0) {
            killTimer(stopTimerId);
            stopTimerId = -1;
            removeLast();         
            setUpdatesEnabled(false);
            unblockRedraw = true;
        }else{
            unblockRedraw = false;
        }
        try{
            final boolean showTrace = request.isSetShowTrace() ? request.getShowTrace() : false;
            final boolean showPercent = request.isSetShowPercent() ? request.getShowPercent() : false;
            final String caption = request.isSetCaption() ? request.getCaption() : null;
            final ProgressWidget process = new ProgressWidget(this, showPercent, showTrace, caption);
            process.setText(request.getTitle());
            process.setCanCancel(request.getCancellable());
            processLayout.addWidget(process);
            if (process.canCancel()) {
                btnCancel.setEnabled(true);
                btnCancel.setVisible(true);
            }
            if (showTrace) {
                progressTrace.setVisible(true);
            }
            if (isHidden()) {
                if (startTimerId == -1) {
                    setWindowTitle(caption == null ? environment.getMessageProvider().translate("Wait Dialog", "Please Wait") : caption);
                    QApplication.instance().installEventFilter(eventFilter);
                    startTimerId = startTimer(START_DELAY);
                } else if (caption != null) {
                    setWindowTitle(caption);
                }
            } else {
                if (caption != null) {
                    setWindowTitle(caption);
                }
                updateGeometry();
                updateSizeLater();
            }
            processes.add(process);
            progressTrace.traceStartProgress(process.getText());
            return process.getProgressId();
        }finally{
            if (unblockRedraw){
                setUpdatesEnabled(true);
            }
        }
    }

    @Override
    public void forceShowIfActive() {
        if (startTimerId > 0) {
            killTimer(startTimerId);
            startTimerId = -1;
            start();
        }
    }

    @Override
    protected void timerEvent(final QTimerEvent event) {
        if (event.timerId() == startTimerId) {
            killTimer(startTimerId);
            startTimerId = -1;
            start();
        } else if (event.timerId() == stopTimerId) {
            killTimer(stopTimerId);
            stopTimerId = -1;
            finish();
        }
        super.timerEvent(event);
    }

    private void start() {
        QApplication.instance().removeEventFilter(eventFilter);
        environment.getProgressHandleManager().blockProgress();
        show();
        updateSize();
    }

    private void finish() {
        removeLast();
        closeDialog();
    }

    private void closeDialog() {
        setWindowTitle(environment.getMessageProvider().translate("Wait Dialog", "Please Wait"));
        setFixedWidth(fontMetrics().width(windowTitle()) + 120);
        hide();
        progressTrace.deleteAll();
        environment.getProgressHandleManager().unblockProgress();
    }

    @Override
    public boolean updateProcess(final IProgressMonitor process, final ProgressDialogSetRq request) {
        if (process.wasCanceled()) {
            return false;
        }
        if (request.isSetTitle()) {
            process.setText(request.getTitle());
        }
        if (request.isSetProgress()) {
            process.setValue(Math.round(request.getProgress()));
        }
        if (request.isSetCancellable()) {
            process.setCanCancel(request.getCancellable());
            updateCancelState();
        }
        updateSize();
        return true;
    }

    @Override
    public boolean addTrace(final org.radixware.schemas.eas.Trace trace) {
        final ProgressWidget process = getLastProcess();
        if (process == null) {
            return false;
        }
        if (process.wasCanceled()) {
            return false;
        }
        progressTrace.add(trace);
        return true;
    }

    @Override
    public boolean finishProcess() {
        if (processes.size() > 0) {
            progressTrace.traceFinishProgress(getLastProcess().getText());
            if (processes.size() == 1) {
                if (isHidden()) {
                    if (startTimerId > -1) {
                        QApplication.instance().removeEventFilter(eventFilter);
                        killTimer(startTimerId);
                        startTimerId = -1;
                    }
                    removeLast();
                } else {
                    stopTimerId = startTimer(STOP_DELAY);
                }
            } else {
                removeLast();
                updateGeometry();
                updateSizeLater();
            }
            return true;
        } else {
            return false;
        }
    }

    private void removeLast() {
        final ProgressWidget process = getLastProcess();
        if (process != null) {
            process.setVisible(false);
            processLayout.removeWidget(process);
            process.disposeLater();
            processes.remove(process);

            if (process.getWindowTitle() != null) {
                int processWithTitle = -1;
                for (int i = processes.size() - 1; i >= 0; i--) {
                    if (processes.get(i).getWindowTitle() != null) {
                        processWithTitle = i;
                        break;
                    }
                }
                if (processWithTitle >= 0) {
                    setWindowTitle(processes.get(processWithTitle).getWindowTitle());
                } else {
                    setWindowTitle(environment.getMessageProvider().translate("Wait Dialog", "Please Wait"));
                }
            }
            updateCancelState();
            if (process.showTrace()) {
                for (ProgressWidget pw : processes) {
                    if (pw.showTrace()) {
                        return;
                    }             
                }    
                progressTrace.deleteAll();
                progressTrace.setVisible(false);
            }              
        }
    }

    private void updateCancelState() {
        for (int i = processes.size() - 1; i >= 0; i--) {
            if (processes.get(i).canCancel()) {
                btnCancel.setVisible(true);
                btnCancel.setEnabled(!processes.get(i).wasCanceled());
                return;
            }
        }
        btnCancel.setVisible(false);
    }

    private void updateSizeLater() {
        QApplication.postEvent(this, new UpdateSizeEvent());
    }

    private void updateSize() {
        if (!isHidden()) {
            final String title = windowTitle();
            layout.activate();
            processLayout.activate();
            final int width = Math.max(sizeHint().width(), fontMetrics().width(title) + 120);
            if (progressTrace.isVisible()) {
                setMinimumSize(Math.max(width(), width), sizeHint().height());
                setMaximumSize(WidgetUtils.MAXIMUM_SIZE, WidgetUtils.MAXIMUM_SIZE);
            } else {
                setFixedSize(Math.max(width(), width), sizeHint().height());
            }
        }
    }

    @SuppressWarnings("unused")
    private void onCancel() {
        for (int i = processes.size() - 1; i >= 0; i--) {
            processes.get(i).cancel();
            if (processes.get(i).canCancel()) {
                return;
            }
        }
        btnCancel.setDisabled(true);
    }

    @Override
    public void clear() {
        if (startTimerId > -1) {
            QApplication.instance().removeEventFilter(eventFilter);
            killTimer(startTimerId);
            startTimerId = -1;
        }
        if (stopTimerId > -1) {
            killTimer(stopTimerId);
            stopTimerId = -1;
        }
        if (!processes.isEmpty()) {
            closeDialog();
            while (processes.size() > 0) {
                removeLast();
            }
        }
    }

    private ProgressWidget getLastProcess() {
        return processes.isEmpty() ? null : processes.get(processes.size() - 1);
    }

    @Override
    public ProgressWidget findProcess(final long processId) {
        for (ProgressWidget process : processes) {
            if (process.getProgressId() == processId) {
                return process;
            }
        }
        return null;
    }

    @Override
    public void done(int result) {
        if (processes.isEmpty()) {
            super.done(result);
        }
    }

    @Override
    protected void customEvent(final QEvent qevent) {
        if (qevent instanceof UpdateSizeEvent){
            qevent.accept();
            updateSize();
        }else{
            super.customEvent(qevent);
        }
    }
    
    
}
