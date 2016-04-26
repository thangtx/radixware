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

package org.radixware.kernel.explorer.dialogs.trace;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QFile;
import com.trolltech.qt.core.QIODevice.OpenModeFlag;
import com.trolltech.qt.core.QTextStream;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QShowEvent;
import com.trolltech.qt.gui.QSpinBox;
import com.trolltech.qt.gui.QVBoxLayout;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.trace.AbstractTraceBuffer;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.explorer.dialogs.ExplorerMessageBox;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.trace.ExplorerTraceBuffer;
import org.radixware.kernel.explorer.env.trace.ExplorerTraceItem;


import org.radixware.kernel.explorer.widgets.ExplorerWidget;


public class TraceView extends ExplorerWidget {

    private static class TraceItemInBufferEvent extends QEvent{        
        public TraceItemInBufferEvent(){
            super(QEvent.Type.User);            
        }
    }

    private class TraceBufferListener implements  AbstractTraceBuffer.TraceBufferListener<ExplorerTraceItem>{

        @Override
        public void newItemInBuffer(final ExplorerTraceItem traceItem) {
            TraceView.this.newTraceItemInBuffer(traceItem);
        }

        @Override
        public void maxSeverityChanged(final EEventSeverity eventSeverity) {
            //do not process
        }

        @Override
        public void cleared() {
            clearTraceList();
        }

    }
    
    private final List<ExplorerTraceItem> traceBuffer = new LinkedList<>();
    private final Object traceBufferSemaphore = new Object();        

    //private final TraceBuffer traceBuffer;
    private final TraceListWidget traceListWidget;
    private QCheckBox continueTracing;
    private ItemCountSpinBox spinBox;
    private final TraceFilter traceFilter;

    @SuppressWarnings("LeakingThisInConstructor")
    public TraceView(final IClientEnvironment environment, final ExplorerTraceBuffer traceBuffer) {
        super(environment);        
        traceFilter = new TraceFilter(environment,this);
        traceListWidget = new TraceListWidget(environment, traceFilter, this);

        setupUi();
        traceBuffer.addTraceBufferListener(new TraceBufferListener());
        if (getEnvironment().getTracer().getBuffer().getMaxSize()!=0 && continueTracing.isChecked()) {
            traceListWidget.addTraceItems(traceBuffer.asList());
        }
        traceFilter.saveTrace.connect(this, "saveTrace()");
        traceFilter.clearTrace.connect(traceBuffer, "clear()");
        traceFilter.showFindDialog.connect(traceListWidget, "showFindDialog()");
        traceFilter.findNext.connect(traceListWidget, "findNext()");
    }
    
    private void newTraceItemInBuffer(final ExplorerTraceItem traceItem){
        synchronized(traceBufferSemaphore){
            traceBuffer.add(traceItem);
            if (traceBuffer.size()==1){//traceBuffer was empty
                QApplication.postEvent(this, new TraceItemInBufferEvent());
            }
        }
    }    

    public QCheckBox getCheckBox() {
        return continueTracing;
    }

    private void clearTraceList() {
        traceListWidget.clear();
    }

    public void refreshControls() {
        spinBox.setValue(getEnvironment().getTracer().getBuffer().getMaxSize());
        traceFilter.getFilterToolBar().updateEventSeverityItems(-1);
    }

    public boolean getContinueTracing() {
        return continueTracing.isChecked();
    }

    private void setupUi() {
        final QVBoxLayout vBoxLayout = new QVBoxLayout();
        this.setLayout(vBoxLayout);
        vBoxLayout.setMargin(0);
        vBoxLayout.setWidgetSpacing(0);
           
        spinBox = new ItemCountSpinBox(getEnvironment().getTracer(),this);
        spinBox.itemCountChanged.connect(this, "maxItemCountChanged()");

        continueTracing = new QCheckBox(this);
        continueTracing.setText(Application.translate("TraceDialog", "Continue tracing in &background"));
        final String val = getEnvironment().getConfigStore().value(SettingNames.SYSTEM + "/TraceDialog/ContinueTracing", "1").toString();
        continueTracing.setChecked("1".equals(val));
        
        final QHBoxLayout toolBarLayout = new QHBoxLayout();
        toolBarLayout.setMargin(0);
        toolBarLayout.setWidgetSpacing(0);
        toolBarLayout.addWidget(continueTracing);
        toolBarLayout.addWidget(spinBox);
        toolBarLayout.addWidget(traceFilter.getFilterToolBar());    
        
        vBoxLayout.addLayout(toolBarLayout);
        vBoxLayout.addWidget(traceListWidget);   
    }

    @SuppressWarnings("unused")
    private void maxItemCountChanged() {
        traceListWidget.setMaxSize(getEnvironment().getTracer().getBuffer().getMaxSize());
    }

    public QSpinBox getSpinBox() {
        return spinBox;
    }

    public EEventSeverity getTraceFilterSeverity() {
        return traceFilter.getSeverity();
    }

    @SuppressWarnings("unused")
    private void saveTrace() {
        final String title = Application.translate("TraceDialog", "Save to file");
        final String filter = Application.translate("TraceDialog", "Log files (%s)");
        final String fileName = QFileDialog.getSaveFileName(this, title, "",
                new QFileDialog.Filter(String.format(filter, "*.log")));
        if (fileName.isEmpty()) {
            return;
        }
        final QFile file = new QFile(fileName);
        if (!file.open(OpenModeFlag.WriteOnly)) {
            ExplorerMessageBox.critical(this, Application.translate("TraceDialog", "Could not open file"),
                    Application.translate("TraceDialog", "Could not open file") + " " + fileName);
            return;
        }
        final QTextStream textStream = new QTextStream(file);
        traceListWidget.writeData(textStream);
        textStream.flush();
        file.close();
    }

    @Override
    protected void showEvent(final QShowEvent event) {
        if (event.type() == QEvent.Type.Show) {
            traceListWidget.setFocus();
        }
        super.showEvent(event);
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof TraceItemInBufferEvent){
            event.accept();
            final List<ExplorerTraceItem> traceItems = new LinkedList<>();
            synchronized(traceBufferSemaphore){
                traceItems.addAll(traceBuffer);
                traceBuffer.clear();
            }
            traceListWidget.addTraceItems(traceItems);
        }else{
            super.customEvent(event);
        }
    }
}
