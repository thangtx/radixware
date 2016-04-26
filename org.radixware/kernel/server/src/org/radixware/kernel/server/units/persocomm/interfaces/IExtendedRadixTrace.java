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

import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.trace.IRadixTrace;
import org.radixware.kernel.server.trace.ServerTrace;
import org.radixware.kernel.server.units.persocomm.tools.MultiLangStringWrapper;

/**
 * <p>This interface is used to isolate the PCUnit class from RadixWare environment for testing purposes</p>
 */
public interface IExtendedRadixTrace extends IRadixTrace {
    /**
     * <p>Set the flood period for the radix trace</p>
     * @param floodKey 
     * @param periodMillis 
     */
    void setFloodPeriod(final String floodKey, final long periodMillis);

    /**
     * <p>
     * @param severity
     * @param localizedMessage
     * @param mlsId
     * @param mlsArgs
     * @param source
     * @param isSensetive 
     */
    void put(final EEventSeverity severity, final String localizedMessage, final MultiLangStringWrapper mlsId, final List<String> mlsArgs, final String source, final boolean isSensetive);

    /**
     * 
     * @param floodKey
     * @param severity
     * @param localizedMessage
     * @param mlsId
     * @param mlsArgs
     * @param source
     * @param millisOrMinusOne
     * @param isSensitive
     * @param targetDestinations 
     */
    void putFloodControlled(final String floodKey, EEventSeverity severity, final String localizedMessage, final MultiLangStringWrapper mlsId, final List<String> mlsArgs, final String source, final long millisOrMinusOne, final boolean isSensitive, final Collection<ServerTrace.ETraceDestination> targetDestinations);

    /**
     * 
     * @param eEventSeverity
     * @param string
     * @param object
     * @param object0
     * @param eEventSource
     * @param b 
     */
    public void put(EEventSeverity eEventSeverity, String string, Object object, Object object0, EEventSource eEventSource, boolean b);
}
