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
 * @deprecated
 *
 * This class provides common infrastructure for managing metrics in
 * multithreaded environment, where data come from multiple worker threads and
 * is written by one control thread (typically instance or unit thread)
 *
 */
public abstract class AbstractMetricController<T> {

    private final Object settingsSem;
    private final Object dataSem;
    private final List<RegisteredItem<T>> registeredData = new ArrayList<>();

    public AbstractMetricController() {
        this(new Object(), new Object());
    }

    /**
     * Use provided objects for synchronization. Useful in cases when you want
     * two or more controllers use the same objects for syncronization
     *
     * @param dataSem
     * @param settingsSem
     */
    public AbstractMetricController(final Object dataSem, final Object settingsSem) {
        this.settingsSem = settingsSem;
        this.dataSem = dataSem;
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
        synchronized (dataSem) {
            synchronized (settingsSem) {
                process(registeredData);
                registeredData.clear();
                return flushInternal();
            }
        }
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
    protected abstract RegisteredItem<T> beforeRegister(RegisteredItem<T> item);

    /**
     * Get values collected since last call. Called under synchronization on
     * both data and settings semaphore.
     *
     * @return
     */
    protected abstract List<MetricRecord> flushInternal();

    /**
     * Transform accumulated data to statistic. Called under synchronization on
     * both data and settings semaphore.
     *
     * @param collectedData
     */
    protected abstract void process(List<RegisteredItem<T>> collectedData);

    /**
     * Create task for updating settings (active stats, etc). Returned task will
     * be executed under settings semaphore, so all database operations should
     * be executed outside the task (i.e. in this method itself, not in the task
     * returend)
     *
     * @return
     */
    protected abstract Runnable createSettingsUpdateTask(final MonitoringDbQueries dbQueries) throws SQLException;
}
