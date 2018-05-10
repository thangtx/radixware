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
package org.radixware.kernel.common.build.xbeans;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlValueOutOfRangeException;

public class XBeansChangeEvent {

    public enum Action {

        CREATE,
        DELETE,
        UPDATE,
    }
    private QName propertyName;
    private IXBeansChangeEmitter emitter;
    private XBeansChangeEvent cause;
    //private Object oldValue;
    private Object newValue;
    private Action action;
    private int index;

    public XBeansChangeEvent(QName propertyName, IXBeansChangeEmitter emitter, Object newValue, Action action, int index) {
        this.propertyName = propertyName;
        this.emitter = emitter;
//        this.oldValue = oldValue;
        this.newValue = newValue;
        this.action = action;
        this.index = index;
    }

    public XBeansChangeEvent(IXBeansChangeEmitter emitter, XBeansChangeEvent cause) {
        this.cause = cause;
        this.emitter = emitter;

    }

    public Action getAction() {
        return action;
    }

    public IXBeansChangeEmitter getEmitter() {
        return emitter;
    }

    public int getIndex() {
        return index;
    }

    public Object getNewValue() {
        if (newValue instanceof SimpleValue) {
            try {
                return ((SimpleValue) newValue).getObjectValue();
            } catch (XmlValueOutOfRangeException e) {
                return null;
            }
        }
        return newValue;
    }

//    public Object getOldValue() {
//        return oldValue;
//    }
    public QName getPropertyName() {
        return propertyName;
    }

    @Override
    public String toString() {
        if (cause == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(propertyName.getNamespaceURI()).append(":").append(propertyName.getLocalPart()).append("\n");
            //      sb.append("Old value:\n");
//        sb.append(oldValue);
            sb.append("\nNew value:\n");
            sb.append(newValue).append("\n");
            return sb.toString();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Hierarchy event, caused by\n");
            sb.append(cause.toString());
            return sb.toString();
        }
    }

    public XBeansChangeEvent getCause() {
        return cause;
    }

    public XBeansChangeEvent getInitialEvent() {
        XBeansChangeEvent e = this;
        while (e.cause != null) {
            e = e.cause;
        }
        return e;
    }
}
