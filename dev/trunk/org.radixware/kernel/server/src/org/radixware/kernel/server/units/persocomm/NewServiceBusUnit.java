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
package org.radixware.kernel.server.units.persocomm;

import java.sql.SQLException;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.units.persocomm.interfaces.IDatabaseConnectionAccess;
import org.radixware.kernel.server.units.persocomm.interfaces.IExtendedRadixTrace;
import org.radixware.kernel.server.utils.OptionsGroup;

public class NewServiceBusUnit extends NewPCUnit {

    public NewServiceBusUnit(final Instance instModel, final Long id, final String title) {
        super(instModel, id, title);
    }

    public NewServiceBusUnit(final Instance instModel, final Long id, final String title, final IDatabaseConnectionAccess dbca, final IExtendedRadixTrace trace) {
        super(instModel, id, title, dbca, trace);
    }

    @Override
    public CommunicationAdapter getCommunicationAdapter(CommunicationMode mode) throws DPCRecvException, DPCSendException {
        return null;
    }

    @Override
    public OptionsGroup optionsGroup(final Options options) {
        return new OptionsGroup().add(PCMessages.SEND_PERIOD, options.sendPeriod);
    }

    @Override
    public boolean supportsTransmitting() {
        return true;
    }

    @Override
    public boolean supportsReceiving() {
        return false;
    }

    @Override
    public String getUnitTypeTitle() {
        return PCMessages.SERVICEBUS_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_SERVICE_BUS.getValue();
    }

    @Override
    protected void checkOptions(Options options) throws Exception {
    }

    @Override
    protected int sendMessages() throws DPCSendException, SQLException {
        try {
            getPersoCommClient().invokeSend();
            return -1;
        } catch (ServiceCallException | ServiceCallTimeout | ServiceCallFault | InterruptedException ex) {
            throw new DPCSendException("Unable to trigger sending pipeline", ex);
        }
    }
}
