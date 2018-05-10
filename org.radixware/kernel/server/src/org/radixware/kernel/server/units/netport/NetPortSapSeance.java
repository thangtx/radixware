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

package org.radixware.kernel.server.units.netport;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.aio.ServiceServerSeance;
import org.radixware.schemas.netporthandler.ExceptionEnum;

/**
 *
 * @author dsafonov
 */
public class NetPortSapSeance {
    public final ServiceServerSeance seance;
    public final XmlObject xmlRq;
    public final boolean keepConnect;
    public final NetPortHandlerUnit unit;

    public NetPortSapSeance(final NetPortHandlerUnit unit, final ServiceServerSeance seance, final XmlObject rqDoc, final boolean keepConnect) {
        this.seance = seance;
        this.xmlRq = rqDoc;
        this.keepConnect = keepConnect;
        this.unit = unit;
    }

    public void response(final XmlObject rsDoc) {
        seance.response(rsDoc, keepConnect);
    }

    public void response(final Throwable e) {
        if (e instanceof ServiceProcessFault) {
            seance.response((ServiceProcessFault) e, keepConnect);
        } else {
            final String exceptionStack = ExceptionTextFormatter.exceptionStackToString(e);
            seance.response(new ServiceProcessServerFault(ExceptionEnum.SERVER_EXCEPTION.toString(), "Unhandled exception: " + exceptionStack, e, exceptionStack), false);
        }
    }

    public void responseReceiveTimeout() {
        seance.response(new ServiceProcessServerFault(ExceptionEnum.RECEIVE_TIMEOUT.toString(), "Receive timeout", null, null), false);
    }

    public void responseConnectTimeout() {
        seance.response(new ServiceProcessServerFault(ExceptionEnum.CONNECT_TIMEOUT.toString(), "Connect timeout", null, null), false);
    }
    
    public void responseCaptureTimeout() {
        seance.response(new ServiceProcessServerFault(ExceptionEnum.CAPTURE_TIMEOUT.toString(), "Capture timeout", null, null), false);
    }

}
