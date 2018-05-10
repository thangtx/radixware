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

import java.io.Closeable;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.units.mq.MqDbQueries;
import org.radixware.kernel.server.units.mq.MqMessage;
import org.radixware.schemas.messagequeue.MqProcessRs;

/**
 * <p>
 * This interface describes queue consumer</p>
 */
public interface IMqQueueConsumer<MessageType extends MqMessage<?,DataSourceId>,DataSourceId> extends Closeable, IMqQueueManipulator<DataSourceId> {
    
    public final long SLEEP_BEFORE_FORCE_WAKEUP_MILLIS = SystemPropUtils.getLongSystemProp("rdx.mq.consumer.sleep.before.forced.close.millis", 2000);
    
    /**
     * <p>Polls the queue </p>
     *
     * @return true if at least one message was received
     * @throws Exception
     */
    boolean poll() throws Exception;

    public void onResponse(MessageType message, MqProcessRs xProcessRs);

    public void onFailToSendAasRequest(MessageType message, Exception ex);

    public void onAasException(MessageType message, Exception ex);
    
    public void requestStopAsync();
    
    public void maintenance();
    
    public String debugKey();
    
    MqDbQueries.MqUnitOptions getOptions();
    
    public boolean isClosed();
}
