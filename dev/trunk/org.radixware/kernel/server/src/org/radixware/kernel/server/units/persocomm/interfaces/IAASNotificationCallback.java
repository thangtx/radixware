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
package org.radixware.kernel.server.units.persocomm.interfaces;

import java.io.Closeable;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;

/**
 * <p>This interface used to notify about sending/receiving messages</p>
 */
public interface IAASNotificationCallback extends Closeable {
    /**
     * <p>Call notification after receiving message</p>
     * @param id received message id
     * @throws ServiceCallException
     * @throws ServiceCallTimeout
     * @throws ServiceCallFault
     * @throws InterruptedException 
     */
    void invokeAfterRecv(final Long id) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException;
    
    /**
     * <p>Trigger sending of messages in ARTE</p>
     * @throws ServiceCallException
     * @throws ServiceCallTimeout
     * @throws ServiceCallFault
     * @throws InterruptedException 
     */
    void invokeSend() throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException;
    
    /**
     * <p>Call notification after send message</p>
     * @param id message id was sent
     * @param error error message about sending. If null or empty - sending was completed successfully
     * @throws ServiceCallException
     * @throws ServiceCallTimeout
     * @throws ServiceCallFault
     * @throws InterruptedException 
     */
    void invokeAfterSend(final Long id, final String error) throws ServiceCallException, ServiceCallTimeout, ServiceCallFault, InterruptedException;
}
