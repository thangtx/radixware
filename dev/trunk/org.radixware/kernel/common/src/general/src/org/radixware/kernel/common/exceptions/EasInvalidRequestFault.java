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

import org.radixware.schemas.eas.ExceptionEnum;


public class EasInvalidRequestFault extends ServiceProcessClientFault {

    private static final long serialVersionUID = 7019054403973456455L;
    private static final String DOUBLE_NEW_LINE = "\n\n";

    public EasInvalidRequestFault(final String mess) {
        super(ExceptionEnum.INVALID_REQUEST.toString(), mess, null, null);
    }

    public EasInvalidRequestFault(final String mess, final boolean alwaysShowDetails) {
        super(ExceptionEnum.INVALID_REQUEST.toString(), (alwaysShowDetails && mess.indexOf(DOUBLE_NEW_LINE) < 0 ? mess + DOUBLE_NEW_LINE : mess), null, null);
    }

    public EasInvalidRequestFault(final String mess, final Throwable cause, final String preprocessedCauseStack) {
        super(ExceptionEnum.INVALID_REQUEST.toString(), mess, cause, preprocessedCauseStack);
    }

    public EasInvalidRequestFault(final String mess, final Throwable cause, final String preprocessedCauseStack, final boolean alwaysShowDetails) {
        super(ExceptionEnum.INVALID_REQUEST.toString(), (alwaysShowDetails && mess.indexOf(DOUBLE_NEW_LINE) < 0 ? mess + DOUBLE_NEW_LINE : mess), cause, preprocessedCauseStack);
    }
}
