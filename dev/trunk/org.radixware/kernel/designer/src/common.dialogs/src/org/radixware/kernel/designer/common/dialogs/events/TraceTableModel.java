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

package org.radixware.kernel.designer.common.dialogs.events;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.utils.CircularBuffer;


public class TraceTableModel extends AbstractTableModel {

    public interface MaxSeverityChangeListener {
        void maxSeverityChanged();
    }

    public static EEventSeverity getSeverityByLevel(Level level) {
        if (level.equals(Level.FINE) || level.equals(Level.FINER) || level.equals(Level.FINEST))
            return EEventSeverity.DEBUG;
        if (level.equals(Level.CONFIG) || level.equals(Level.INFO))
            return EEventSeverity.EVENT;
        if (level.equals(Level.WARNING))
            return EEventSeverity.WARNING;
        if (level.equals(Level.SEVERE))
            return EEventSeverity.ERROR;
        return EEventSeverity.NONE;
    }
    
    private static final int MAX_TRACE_BUF_SIZE = 1024;
    private static final String[] columnHeaders = {
        NbBundle.getBundle(TraceTableModel.class).getString("COL_SEVERITY"),
        NbBundle.getBundle(TraceTableModel.class).getString("COL_NUMBER"),
        NbBundle.getBundle(TraceTableModel.class).getString("COL_DATE"),
        NbBundle.getBundle(TraceTableModel.class).getString("COL_TIME"),
        NbBundle.getBundle(TraceTableModel.class).getString("COL_LOGGER"),
        NbBundle.getBundle(TraceTableModel.class).getString("COL_MESSAGE")
    };
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    private final ArrayList<MaxSeverityChangeListener> maxSeverityChangeListeners = new ArrayList<MaxSeverityChangeListener>();
    private final CircularBuffer<LogRecord> initialRecords = new CircularBuffer<LogRecord>(MAX_TRACE_BUF_SIZE);
    private final CircularBuffer<LogRecord> visibleRecords = new CircularBuffer<LogRecord>(MAX_TRACE_BUF_SIZE);
    private volatile EEventSeverity severityFilter;
    private volatile String stringFilter;
    private volatile EEventSeverity maxSeverity;

    public TraceTableModel() {
        severityFilter = EEventSeverity.DEBUG;
        stringFilter = "";
        maxSeverity = EEventSeverity.NONE;
    }

    public synchronized EEventSeverity getMaxSeverity() {
        return maxSeverity;
    }

    private synchronized void updateData() {
        synchronized (visibleRecords) {
            visibleRecords.clear();
            synchronized (initialRecords) {
                for (int i = 0; i < initialRecords.size(); ++i) {
                    if (match(initialRecords.get(i))) {
                        visibleRecords.add(initialRecords.get(i));
                    }
                }
            }
        }
    }

    public synchronized void putLogRecord(LogRecord record) {
        EEventSeverity severity = getSeverityByLevel(record.getLevel());
        if (severity.equals(EEventSeverity.NONE))
            return;
        initialRecords.add(record);
        if (match(record)) {
            visibleRecords.add(record);
            if (getRowCount() < MAX_TRACE_BUF_SIZE)
                fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
            else
                fireTableDataChanged();
        }
        if (maxSeverity.equals(EEventSeverity.NONE) || maxSeverity.compareTo(severity) < 0) {
            maxSeverity = severity;
            fireMaxSeverityChanged();
        }
    }

    public synchronized LogRecord getLogRecordAt(int row) {
        if (row >= getRowCount() || row < 0)
            return null;
        return visibleRecords.get(row);
    }

    @Override
    public int getColumnCount() {
        return columnHeaders.length;
    }

    @Override
    public int getRowCount() {
        return visibleRecords.size();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
    }

    @Override
    public Object getValueAt(int row, int column) {
        LogRecord record = getLogRecordAt(row);
        if (record == null)
            return null;
        switch (column) {
            case 0:
                return getSeverityByLevel(record.getLevel());
            case 1:
                return new Long(record.getSequenceNumber());
            case 2:
                return dateFormat.format(new Date(record.getMillis()));
            case 3:
                return timeFormat.format(new Date(record.getMillis()));
            case 4:
                return record.getLoggerName();
            case 5:
                return record.getMessage();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnHeaders[column];
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
                return EEventSeverity.class;
            case 1:
                return Long.class;
            case 2:
            case 3:
            case 4:
            case 5:
                return String.class;
            default:
                return Object.class;
        }
    }

    public synchronized void clear() {
        int cnt = getRowCount();
        initialRecords.clear();
        visibleRecords.clear();
        if (cnt > 0)
            fireTableRowsDeleted(0, cnt - 1);
        maxSeverity = EEventSeverity.NONE;
        fireMaxSeverityChanged();
    }

    public synchronized void setSeverityFilter(EEventSeverity severity) {
        severityFilter = severity;
        updateData();
        fireTableDataChanged();
    }

    public synchronized void setStringFilter(String str) {
        stringFilter = str.trim().toLowerCase();
        updateData();
        fireTableDataChanged();
    }

    private synchronized boolean match(LogRecord logRecord) {
        if (getSeverityByLevel(logRecord.getLevel()).compareTo(severityFilter) < 0)
            return false;
        if (stringFilter.equals(""))
            return true;
        if (logRecord.getLoggerName() != null && logRecord.getLoggerName().toLowerCase().indexOf(stringFilter) != -1)
            return true;
        if (logRecord.getMessage() != null && logRecord.getMessage().toLowerCase().indexOf(stringFilter) != -1)
            return true;
        return false;
    }

    public void addMaxSeverityChangeListener(MaxSeverityChangeListener listener) {
        maxSeverityChangeListeners.add(listener);
    }

    public void removeMaxSeverityChangeListener(MaxSeverityChangeListener listener) {
        maxSeverityChangeListeners.remove(listener);
    }

    private void fireMaxSeverityChanged() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                for (MaxSeverityChangeListener listener : maxSeverityChangeListeners) {
                    listener.maxSeverityChanged();
                }
            }
        });
    }

}
