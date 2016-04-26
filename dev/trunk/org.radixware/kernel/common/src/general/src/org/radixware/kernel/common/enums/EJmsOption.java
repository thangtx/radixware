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

package org.radixware.kernel.common.enums;

import java.util.List;
import javax.naming.Context;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;


public enum EJmsOption implements IKernelStrEnum {

    JMS_LOGIN("radix.jms.login"),
    JMS_PASSWORD("radix.jms.password"),
    JNDI_INITIAL_CONTEXT_FACTORY(Context.INITIAL_CONTEXT_FACTORY),
    JNDI_PROVIDER_URL(Context.PROVIDER_URL),
    CONNECTION_FACTORY_JNDI_NAME("radix.jms.connectionFactory.jndi.name"),
    CONNECTION_FACTORY_CLASS_NAME("radix.jms.connectionFactoryClass"),
    INBOUND_DESTINATION_JNDI_NAME("radix.jms.inbound.jndi.name"),
    OUTBOUND_DESTINATION_JNDI_NAME("radix.jms.outbound.jndi.name"),
    INBOUND_QUEUE_NAME("radix.jms.inbound.queue.name"),
    OUTBOUND_QUEUE_NAME("radix.jms.outbound.queue.name");
    private final String value;

    private EJmsOption(String val) {
        this.value = val;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }

    public static EJmsOption getForValue(final String str) {
        for (EJmsOption type : EJmsOption.values()) {
            if (type.getValue().equals(str)) {
                return type;
            }
        }
        throw new NoConstItemWithSuchValueError("EJmsProperty has no item with value " + str, str);
    }

    @Override
    public String getName() {
        return null;
    }
}
