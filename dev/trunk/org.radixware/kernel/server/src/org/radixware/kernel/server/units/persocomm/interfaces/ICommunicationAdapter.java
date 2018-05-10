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

package org.radixware.kernel.server.units.persocomm.interfaces;

import java.io.Closeable;
import java.sql.SQLException;
import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.kernel.server.units.persocomm.MessageSendResult;
import org.radixware.kernel.server.units.persocomm.MessageWithMeta;
import org.radixware.schemas.personalcommunications.MessageDocument;

public interface ICommunicationAdapter extends Closeable {

    /**
     *
     * @param messageWithMeta
     * @return MessageSendResult instance on success
     * @throws DPCSendException on non-success
     */
    MessageSendResult sendMessage(MessageWithMeta messageWithMeta) throws DPCSendException;

    /**
     *
     * @return
     * @throws DPCRecvException
     * @throws java.sql.SQLException
     */
    MessageDocument receiveMessage() throws DPCRecvException, SQLException;

    public boolean isPersistent();

}
