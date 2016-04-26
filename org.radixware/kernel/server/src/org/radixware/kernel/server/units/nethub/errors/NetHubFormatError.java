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

package org.radixware.kernel.server.units.nethub.errors;

import org.radixware.kernel.common.utils.ExceptionTextFormatter;


public class NetHubFormatError extends NetHubError {
    public NetHubFormatError(final String mess) {
        super(mess);
    }
    public NetHubFormatError(Throwable e) {
        super(ExceptionTextFormatter.getExceptionMess(e), e);
    }
    public NetHubFormatError(final String mess, final Throwable cause) {
        super(mess, cause);
    }
}