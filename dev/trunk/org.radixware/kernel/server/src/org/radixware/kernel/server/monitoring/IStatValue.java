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


public interface IStatValue extends IMetricValue {

    /**
     * @return min value on the interval. Can be null if there were no values.
     */
    public Double getMin();

    /**
     * 
     * @return max value on the interval. Can be null if there were no values.
     */
    public Double getMax();

    /**
     * 
     * @return average value on the interval. If there were no values, 0 should
     * be returned
     */
    public double getAvg();
}
