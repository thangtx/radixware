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
package org.radixware.kernel.server.units.persocomm;

import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.schemas.personalcommunications.MessageDocument;

public final class ServiceBusUnit extends PCUnit {

    public ServiceBusUnit(final Instance instance, final Long id, final String title) {
        super(instance, id, title);
    }

    @Override
    public String optionsToString() {
        return "{\n\t"
                + PCMessages.SEND_PERIOD + " " + String.valueOf(options.sendPeriod) + "; \n\t"
                + "}";
    }

    @Override
    protected void recvMessages() throws DPCRecvException {
    }

    @Override
    protected void sendMessages() throws DPCSendException {
        try {
            aasClient.invokeSend();
        } catch (Exception ex) {
            throw new DPCSendException("Unable to trigger messages sending", ex);
        }
    }

    @Override
    protected void send(final MessageDocument m, final Long id) throws DPCSendException {
    }

    @Override
    protected void checkOptions() throws Exception {
    }

    @Override
    public String getUnitTypeTitle() {
        return PCMessages.SERVICEBUS_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_SERVICE_BUS.getValue();
    }
}
