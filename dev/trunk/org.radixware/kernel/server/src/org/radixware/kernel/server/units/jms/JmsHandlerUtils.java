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


package org.radixware.kernel.server.units.jms;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.jms.JMSException;
import javax.jms.Message;
import org.radixware.kernel.common.utils.Maps;
import org.radixware.schemas.jmshandler.Base;


public class JmsHandlerUtils {
 
    static void prepareRequest(final Base info, final Message msg) throws JMSException {
        if (msg == null)
            return;
        
        info.setCorrelationId(msg.getJMSCorrelationID());
        info.setDeliveryMode(Long.valueOf(msg.getJMSDeliveryMode()));
        info.setExpiration(msg.getJMSExpiration());
        info.setMessageId(msg.getJMSMessageID());
        info.setPriority(Long.valueOf(msg.getJMSPriority()));
        info.setRedelivered(msg.getJMSRedelivered());
        info.setTimestamp(msg.getJMSTimestamp());
        info.setType(msg.getJMSType());

        final Map<String, String> props = new HashMap<>();
        final Enumeration e = msg.getPropertyNames();
        while (e.hasMoreElements()) {
            final String name = (String)e.nextElement();
            final Object value = msg.getObjectProperty(name);
            props.put(name, value == null ? null : value.toString());
        }
        info.setProps(Maps.toXml(props));
    }
    
    static void prepareMessage(final Message msg, final Base info) throws JMSException {
        if (info == null)
            return;
        
        if (info.isSetCorrelationId())
            msg.setJMSCorrelationID(info.getCorrelationId());
        if (info.isSetDeliveryMode())
            msg.setJMSDeliveryMode(info.getDeliveryMode().intValue());
        if (info.isSetExpiration())
            msg.setJMSExpiration(info.getExpiration());
        if (info.isSetMessageId())
            msg.setJMSMessageID(info.getMessageId());
        if (info.isSetPriority())
            msg.setJMSPriority(info.getPriority().intValue());
        if (info.isSetRedelivered())
            msg.setJMSRedelivered(info.getRedelivered());
        if (info.isSetTimestamp())
            msg.setJMSTimestamp(info.getTimestamp());
        if (info.isSetType())
            msg.setJMSType(info.getType());
        
        final Map<String, String> props = Maps.fromXml(info.getProps());
        if (props != null) {
            for (Map.Entry<String, String> e : props.entrySet()) {
                msg.setStringProperty(e.getKey(), e.getValue());
            }
        }
    }    
}