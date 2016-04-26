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

public class ServiceProcessClientFault extends ServiceProcessFault {
    private static final long serialVersionUID = 2932909321054946305L;

    public ServiceProcessClientFault(final String reason, final String mess, final Throwable cause, final String preprocessedCauseStack) {
        super(FAULT_CODE_CLIENT, reason, mess, cause, preprocessedCauseStack);
    }
    public ServiceProcessClientFault(final String reason, final String mess, final Throwable cause, final String preprocessedCauseStack, final SoapFormatter.FaultDetailWriter faultDetailWriter) {
        super(FAULT_CODE_CLIENT, reason, mess, cause, preprocessedCauseStack, faultDetailWriter);
    }
}
