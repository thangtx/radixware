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
package org.radixware.kernel.server.monitoring;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.server.types.ProfileStatisticEntry;

/**
 *
 * @author dsafonov
 */
public class DurationStatMetricController extends AbstractMetricController<Long> {

    public DurationStatMetricController(MetricDescription metricDescription) {
        super(metricDescription);
    }

    @Override
    public void ensureStarted(RegisteredItem<Long> item) {
        if (getStat() != null && !getStat().isStarted()) {
            ((DurationStat) getStat()).start(new RegisteredItem<>(item.regTimeMillis, new ProfileStatisticEntry("", "", item.val * 1000000l, 1, item.val * 1000000l, item.val * 1000000l)));
        }
    }
    
    public void start() {
        ensureStarted(new RegisteredItem<>(System.currentTimeMillis(), 0L));
    }
    
    public void register(long regTimeMillis, long durationMillis) {
        register(new RegisteredItem<>(regTimeMillis, durationMillis));
    }
    
    public void register(long durationMillis) {
        register(System.currentTimeMillis(), durationMillis);
    }

    @Override
    protected void process(List<RegisteredItem<Long>> collectedData) {
        if (collectedData == null) {
            return;
        }
        final List<RegisteredItem> preprocessedList = new ArrayList<>(collectedData.size());
        for (RegisteredItem<Long> item : collectedData) {
            preprocessedList.add(new RegisteredItem<>(item.regTimeMillis, new ProfileStatisticEntry("", "", item.val * 1000000l, 1, item.val * 1000000l, item.val * 1000000l)));
        }
        super.processUnchecked(preprocessedList);
    }

    @Override
    protected AbstractStat createStat(MetricParameters parameters) {
        return new DurationStat(parameters, System.currentTimeMillis());
    }

}
