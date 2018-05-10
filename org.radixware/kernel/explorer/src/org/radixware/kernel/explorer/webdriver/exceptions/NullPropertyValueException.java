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

package org.radixware.kernel.explorer.webdriver.exceptions;

public class NullPropertyValueException extends WebDrvException{
    
    private static final long serialVersionUID = 7426065444676481165L;

    public NullPropertyValueException(final String propName) {
        super(EWebDrvErrorCode.INVALID_ARGUMENT, propName+" property was not defined");
    }    
}
