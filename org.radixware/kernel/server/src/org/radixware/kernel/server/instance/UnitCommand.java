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

package org.radixware.kernel.server.instance;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.utils.XmlObjectProcessor;
import org.radixware.kernel.server.units.Unit;


public class UnitCommand {

    private final XmlObject request;
    private final Unit unit;
    private final long createTimeMillis;

    public UnitCommand(XmlObject request, final Unit unit, final long createTimeMillis) {
        this.request = request;
        this.unit = unit;
        this.createTimeMillis = createTimeMillis;
    }

    public XmlObject getRequestContainer() {
        return request;
    }

    public <T extends XmlObject> T getRequest(Class<T> requestClass) {
        return (T) XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(), request, requestClass);
    }

    public Unit getUnit() {
        return unit;
    }

    public long getCreateTimeMillis() {
        return createTimeMillis;
    }

    public String getTraceId() {
        return String.valueOf(System.identityHashCode(this));
    }
}
