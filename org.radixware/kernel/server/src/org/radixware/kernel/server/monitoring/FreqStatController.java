/*
* Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
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

/**
 *
 * @author dsafonov
 */
public class FreqStatController extends AbstractMetricController<Double> {

    public FreqStatController(MetricDescription metricDescription) {
        super(metricDescription);
    }
    
    public void tick() {
        tick(1);
    }
    
    public void tick(long times) {
        register(new RegisteredItem<>(System.currentTimeMillis(), times * 1.));
    }
    
    @Override
    protected AbstractStat createStat(MetricParameters parameters) {
        return new FrequencyPerSecondStat(parameters, System.currentTimeMillis());
    }
    

}
