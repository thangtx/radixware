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
package org.radixware.kernel.server.units.mq.interfaces;

import org.radixware.kernel.server.units.mq.MqMessage;

public interface IMqQueueManipulator<PartitionType> {
    /**
     * <p>Pause processing for the given data source</p>
     * @param parameter data source to pause
     */
    void pause(final PartitionType parameter);

    /**
     * <p>Resume processing for the given data source</p>
     * @param parameter  data source to resume
     */
    void resume(final PartitionType parameter);
    
    /**
     * <p>Pause polling for the all data sources</p>
     */
    void pausePolling();
    
    /**
     * <p>Resume polling for the all data sources</p>
     */
    void resumePolling();
    
    boolean canPausePartition();
    
    boolean canPausePolling();
    
    boolean canRestoreOffset();
    
    void rememberOffset(MqMessage mess);
    
    boolean isOffsetRemembered(MqMessage mess);
    
}
