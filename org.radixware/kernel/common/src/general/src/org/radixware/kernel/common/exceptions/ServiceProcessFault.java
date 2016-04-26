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

package org.radixware.kernel.common.exceptions;

import org.radixware.kernel.common.utils.SoapFormatter;

public class ServiceProcessFault extends RadixError {

    private static final long serialVersionUID = -8846050937725808363L;
    public final String code; // FAULT_CODE_XXX;
    public final String reason;
    public final String preprocessedCauseStack;
    private final SoapFormatter.FaultDetailWriter faultDetailWriter;
    public static final String FAULT_CODE_CLIENT = "Client";
    public static final String FAULT_CODE_SERVER = "Server";
    public static final String FAULT_CODE_SERVER_BUSY = FAULT_CODE_SERVER + ".Busy";
    public static final String FAULT_CODE_SERVER_SHUTDOWN = FAULT_CODE_SERVER + ".Shutdown";

    public ServiceProcessFault(final String code, final String reason, final String mess, final Throwable cause, final String preprocessedCauseStack) {
        this(code, reason, mess, cause, preprocessedCauseStack, new SoapFormatter.DefaultFaultDetailWriter(mess, cause, preprocessedCauseStack));
    }

    public ServiceProcessFault(final String code, final String reason, final String mess, final Throwable cause, final String preprocessedCauseStack, final SoapFormatter.FaultDetailWriter faultDetailWriter) {
        super(mess, cause);
        this.code = code;
        this.reason = reason;
        this.preprocessedCauseStack = preprocessedCauseStack;
        this.faultDetailWriter = faultDetailWriter;
    }

    public SoapFormatter.FaultDetailWriter getFaultDetailWriter() {
        return faultDetailWriter;
    }
}