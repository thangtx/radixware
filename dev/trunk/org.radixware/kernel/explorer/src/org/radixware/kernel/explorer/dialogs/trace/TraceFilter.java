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

import com.trolltech.qt.QSignalEmitter.Signal0;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QWidget;
import java.sql.Timestamp;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.explorer.env.trace.ExplorerTraceItem;


public class TraceFilter extends QObject {

    public final Signal0 filterChanged = new Signal0();
    public final Signal0 saveTrace = new Signal0();
    public final Signal0 clearTrace = new Signal0();
    public final Signal0 showFindDialog = new Signal0();
    public final Signal0 findNext = new Signal0();
    //private TraceLevelTreeWidget traceLevelTreeWidget;
    private final TraceFilterToolBar traceFilterToolBar;
    private long begTime = -1;
    private long endTime = -1;
    final IClientEnvironment environment;

    public TraceFilter(final IClientEnvironment environment, final QWidget parentWidget) {
        super(parentWidget);
        this.environment = environment;
        //traceLevelTreeWidget = new TraceLevelTreeWidget(environment);
        traceFilterToolBar = new TraceFilterToolBar(environment,parentWidget);
        traceFilterToolBar.severityChanged.connect(filterChanged);
        traceFilterToolBar.saveTrace.connect(saveTrace);
        traceFilterToolBar.clearTrace.connect(clearTrace);
        traceFilterToolBar.begTimeChanged.connect(this, "setBegTime(Timestamp)");
        traceFilterToolBar.endTimeChanged.connect(this, "setEndTime(Timestamp)");
        traceFilterToolBar.showFindDialog.connect(showFindDialog);
        traceFilterToolBar.findNext.connect(findNext);
    }

    @SuppressWarnings("unused")
    private void setBegTime(final Timestamp time) {
        begTime = time == null ? -1 : time.getTime();
        filterChanged.emit();
    }

    @SuppressWarnings("unused")
    private void setEndTime(final Timestamp time) {
        endTime = time == null ? -1 : time.getTime();
        filterChanged.emit();
    }
/*
    public TraceLevelTreeWidget getTraceLevelView() {
        return traceLevelTreeWidget;
    }
*/
    public TraceFilterToolBar getFilterToolBar() {
        return traceFilterToolBar;
    }
/*
    public void disableDetail() {
        traceLevelTreeWidget.disable();
    }

    public void enableDetail() {
        traceLevelTreeWidget.enable();
    }
*/
    public EEventSeverity getSeverity() {
        return traceFilterToolBar.getSeverity();
    }

    boolean isAllowed(final ExplorerTraceItem traceItem) {
        if (traceItem.getSeverity().getValue() < traceFilterToolBar.getSeverity().getValue()) {
            return false;
        }
        if ((begTime != -1 && traceItem.getTime() < begTime) || (endTime != -1 && traceItem.getTime() > endTime)) {
            return false;
        }
        return true;
    }
}
