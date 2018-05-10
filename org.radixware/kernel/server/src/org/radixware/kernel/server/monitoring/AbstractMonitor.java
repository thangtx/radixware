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

package org.radixware.kernel.server.monitoring;

import java.sql.Connection;
import java.sql.SQLException;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.server.trace.ServerTrace;

/**
 * Abstract Monitor
 */
public abstract class AbstractMonitor {

    private final MetricRecordWriter writer;
    private final MonitoringDbQueries dbQueries;

    protected AbstractMonitor() {
        this.writer = null;
        this.dbQueries = null;
    }
    
    public AbstractMonitor(final Connection connection, final ServerTrace serverTrace, final String eventSource) {
        this.dbQueries = new MonitoringDbQueries(connection, serverTrace, eventSource);
        this.writer = new MetricRecordWriter(connection, dbQueries, serverTrace.newTracer(EEventSource.SYSTEM_MONITORING.getValue()));
        this.writer.addFlushListener(new MetricRecordWriter.FlushListener() {

            @Override
            public void beforeFlush() {
                AbstractMonitor.this.beforeWriterFlush();
            }
        });
    }

    protected MonitoringDbQueries getDbQueries() {
        return dbQueries;
    }

    public void flush() throws SQLException {
        writer.flush();
    }

    public void setConnection(final Connection connection) {
        writer.setConnection(connection);
        dbQueries.setConnection(connection);
    }

    public MetricRecordWriter getWriter() {
        return writer;
    }

    public abstract void rereadSettings() throws SQLException;

    public abstract void beforeWriterFlush();
}
