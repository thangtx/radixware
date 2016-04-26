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

import java.text.MessageFormat;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.server.instance.Instance;

public final class SMSMailUnit extends MailUnit {

//    MessageFormat addressFormat;
//    MessageFormat subjectFormat;
    public SMSMailUnit(Instance instance, Long id, String title) {
        super(instance, id, title);
//        addressFormat = new MessageFormat(options.addressTemplate);
//        subjectFormat = new MessageFormat(options.subjectTemplate);
    }

    @Override
    public String optionsToString() {
        return "{\n\t"
                + PCMessages.SEND_PERIOD + " " + String.valueOf(options.sendPeriod) + "; \n\t"
                + PCMessages.RECV_PERIOD + " " + String.valueOf(options.recvPeriod) + "; \n\t"
                + PCMessages.SEND_ADDRESS + " " + options.sendAddress + "; \n\t"
                + PCMessages.RECV_ADDRESS + " " + options.recvAddress + "; \n\t"
                + PCMessages.EMAIL_LOGIN + " " + options.emailLogin + "; \n\t"
                + PCMessages.EMAIL_PASSWORD + " " + options.emailPassword + "; \n\t"
                + PCMessages.ADDRESS_TEMPLATE + " " + options.addressTemplate + "; \n\t"
                + PCMessages.SUBJECT_TEMPLATE + " " + options.subjectTemplate + "; \n\t"
                + "}";
    }

//    @Override
//    String convertAddress( String address,  String subject) {
//        Object[] args = {address, subject};
//        return addressFormat.format(args);
//    }
//    @Override
//    String convertSubject( String address,  String subject) {
//        Object[] args = {address, subject};
//        return subjectFormat.format(args);
//    }
    @Override
    public String getUnitTypeTitle() {
        return PCMessages.SMS_MAIL_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_SMSVIA_SMTP.getValue();
    }
}
