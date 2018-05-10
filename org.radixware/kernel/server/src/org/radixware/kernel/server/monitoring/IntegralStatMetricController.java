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


public class IntegralStatMetricController extends AbstractMetricController<Integer> {

    private static final RegisteredItem<Integer> INCREMENT_REQUEST = new RegisteredItem<>(-1, -1);
    private static final RegisteredItem<Integer> DECREMENT_REQUEST = new RegisteredItem<>(-1, -1);
    
    private int currentCount = 0;

    public IntegralStatMetricController(final MetricDescription metricDescription) {
        super(metricDescription);
    }

    public void increment() {
        register(INCREMENT_REQUEST);
    }

    public void decrement() {
        register(DECREMENT_REQUEST);
    }

    public void set(final int count) {
        register(new RegisteredItem<>(System.currentTimeMillis(), count));
    }

    public int getCurrentCount() {
        return currentCount;
    }

    @Override
    protected AbstractStat createStat(MetricParameters parameters) {
        return new IntegralCountStat(parameters);
    }
    
    @Override
    protected RegisteredItem<Integer> beforeRegister(final RegisteredItem<Integer> item) {
        if (item == INCREMENT_REQUEST) {
            return new RegisteredItem<>(System.currentTimeMillis(), ++currentCount);
        } else if (item == DECREMENT_REQUEST) {
            return new RegisteredItem<>(System.currentTimeMillis(), --currentCount);
        } else {
            if (item != null) {
                currentCount = item.val;
            }
            return item;
        }
    }
}
