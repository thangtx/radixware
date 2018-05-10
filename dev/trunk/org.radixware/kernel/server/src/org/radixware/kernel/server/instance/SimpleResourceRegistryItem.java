/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.instance;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import org.radixware.kernel.common.utils.SystemPropUtils;

/**
 *
 * @author dsafonov
 */
public abstract class SimpleResourceRegistryItem implements IResourceRegistryItem {
    
    private static final long DEAD_REPORT_DELAY = SystemPropUtils.getLongSystemProp("rdx.resource.dead.report.delay.nanos", 1000000l * 5000);
    private final String key;
    private final Closeable resource;
    private final String description;
    private final Callable<Boolean> holderAliveChecker;
    private final long creationNanoTime = System.nanoTime();
    private final AtomicLong holderDeadDetectionNanos = new AtomicLong(Long.MIN_VALUE);

    public SimpleResourceRegistryItem(String key, Closeable resource, String description, Callable<Boolean> holderAliveChecker) {
        this.key = key;
        this.resource = resource;
        this.description = description;
        this.holderAliveChecker = holderAliveChecker;
        if (resource == null) {
            throw new IllegalArgumentException("Resource can't be null");
        }
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isHolderAlive() {
        if (holderAliveChecker == null) {
            return true;
        }
        try {
            final boolean wasDead = holderDeadDetectionNanos.get() > Long.MIN_VALUE;
            
            final Boolean checkResult = wasDead ? null : holderAliveChecker.call();
            
            if (!wasDead && (checkResult != null && !checkResult)) {
                holderDeadDetectionNanos.compareAndSet(Long.MIN_VALUE, System.nanoTime());
            }
            
            return !wasDead || (System.nanoTime() < holderDeadDetectionNanos.get() + DEAD_REPORT_DELAY);
        } catch (Exception ex) {
            return true;
        }
    }

    @Override
    public void close() throws IOException {
        resource.close();
    }

    @Override
    public long getCreationNanoTime() {
        return creationNanoTime;
    }

}
