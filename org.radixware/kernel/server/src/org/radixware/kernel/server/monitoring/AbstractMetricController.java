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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * This class provides common infrastructure for managing metrics in
 * multithreaded environment, where data come from multiple worker threads and
 * is written by one control thread (typically instance or unit thread)
 *
 */
public abstract class AbstractMetricController<T> {

    private final Object settingsSem = new Object();
    private final Object dataSem = new Object();
    private final List<RegisteredItem<T>> registeredData = new ArrayList<>();
    private AbstractStat stat = null;
    private final MetricDescription metricDescription;

    public AbstractMetricController(final MetricDescription metricDescription) {
        this.metricDescription = metricDescription;
    }
    
    protected AbstractStat getStat() {
        return stat;
    }
    
    /**
     * Register some measured data. It will be added to the queue of collected
     * data and lately transformed to statistic. ThreadSafe.
     *
     * @param item
     */
    public final void register(final RegisteredItem<T> item) {
        synchronized (dataSem) {
            doRegister(item);
        }
    }

    /**
     * Analog of {@linkplain  register(RegisteredItem item)}. ThreadSafe.
     *
     * @param items
     */
    public final void registerAll(final List<RegisteredItem<T>> items) {
        synchronized (dataSem) {
            for (final RegisteredItem item : items) {
                doRegister(item);
            }
        }
    }

    private void doRegister(final RegisteredItem<T> item) {
        registeredData.add(beforeRegister(item));
    }

    /**
     * Flush collected data. ThreadSafe.
     */
    public final List<MetricRecord> flush() {
        final List<MetricRecord> result;
        synchronized (dataSem) {
            synchronized (settingsSem) {
                process(registeredData);
                registeredData.clear();
                result = flushInternal();
            }
        }
        return result;
    }

    /**
     * Reread settings from database. Should be called only from control thread,
     * that must be an owner of the database connection
     *
     * @param dbQueries
     * @throws SQLException
     */
    public final void rereadSettings(final MonitoringDbQueries dbQueries) throws SQLException {
        final Runnable settingsUpdateTask = createSettingsUpdateTask(dbQueries);
        synchronized (settingsSem) {
            settingsUpdateTask.run();
        }
    }

    /**
     * Process item before registration. Called under synchronization on data
     * semaphore. Should return quickly.
     *
     * @param item
     */
    protected RegisteredItem<T> beforeRegister(RegisteredItem<T> item) {
        return item;
    }

    /**
     * Get values collected since last call. Called under synchronization on
     * both data and settings semaphore.
     *
     * @return
     */
    protected List<MetricRecord> flushInternal() {
        final List<MetricRecord> toWrite = new ArrayList<>();
        if (stat != null) {
            stat.processToTime(System.currentTimeMillis());
            toWrite.addAll(stat.popRecords());
            if (stat.getState() == EMetricState.DISABLED) {
                stat = null;
            }
        }
        return toWrite;
    }

    /**
     * Transform accumulated data to statistic. Called under synchronization on
     * both data and settings semaphore.
     *
     * @param collectedData
     */
    protected void process(final List<RegisteredItem<T>> collectedData) {
        if (collectedData == null) {
            return;
        }
        final List<RegisteredItem> uncheckedList = new ArrayList<>(collectedData.size());
        for (RegisteredItem item : collectedData) {
            uncheckedList.add(item);
        }
        processUnchecked(uncheckedList);
    }

    public void ensureStarted(final RegisteredItem<T> item) {
        if (!stat.isStarted()) {
            stat.start(item);
        }
    }

    protected void processUnchecked(final List<RegisteredItem> uncheckedData) {
        if (stat != null && !uncheckedData.isEmpty()) {
            ensureStarted(uncheckedData.get(0));
            stat.appendAll(uncheckedData);
        }
    }

    /**
     * Create task for updating settings (active stats, etc). Returned task will
     * be executed under settings semaphore, so all database operations should
     * be executed outside the task (i.e. in this method itself, not in the task
     * returend)
     *
     * @return
     */
    protected Runnable createSettingsUpdateTask(final MonitoringDbQueries dbQueries) throws SQLException {
        final MetricParameters parameters = dbQueries.readMetricParameters(metricDescription);
        return new Runnable() {
            @Override
            public void run() {
                if (stat != null) {
                    if (parameters == null) {
                        stat.setState(EMetricState.DISABLED);
                    } else {
                        stat.setParameters(parameters);
                    }
                } else if (parameters != null) {
                    stat = createStat(parameters);
                }
            }
        };
    }

    public MetricDescription getMetricDescription() {
        return metricDescription;
    }

    protected abstract AbstractStat createStat(final MetricParameters parameters);
}
