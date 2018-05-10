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

import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.utils.OptionsGroup;

public class SmsMailUnit extends MailUnit {

    public SmsMailUnit(Instance instance, Long id, String title) {
        super(instance, id, title);
    }

    @Override
    public OptionsGroup optionsGroup(final Options options) {
        return new OptionsGroup()
                .add(PCMessages.SEND_PERIOD, options.sendPeriod)
                .add(PCMessages.RECV_PERIOD, options.recvPeriod)
                .add(PCMessages.SEND_ADDRESS, options.sendAddress)
                .add(PCMessages.RECV_ADDRESS, options.recvAddress)
                .add(PCMessages.EMAIL_LOGIN, options.emailLogin)
                .add(PCMessages.EMAIL_PASSWORD, options.emailPassword)
                .add(PCMessages.ADDRESS_TEMPLATE, options.addressTemplate)
                .add(PCMessages.SUBJECT_TEMPLATE, options.subjectTemplate);
    }

    @Override
    public String getUnitTypeTitle() {
        return PCMessages.SMS_MAIL_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_SMSVIA_SMTP.getValue();
    }
}
