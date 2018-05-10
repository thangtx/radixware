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

public final class InvalidPropertyValueTypeException extends WebDrvException{

    private static final long serialVersionUID = 6457377354763545228L;
    
    public InvalidPropertyValueTypeException(final String propName, final Object value) {
        super(EWebDrvErrorCode.INVALID_ARGUMENT, propName+" property has invalid type "+value.getClass().getName());
    }
    
    
}
