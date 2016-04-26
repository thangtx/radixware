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

package org.radixware.kernel.server.exceptions;

import org.radixware.kernel.common.exceptions.RadixPrivateException;


public class UnsupportedUnitTypeException extends RadixPrivateException {
	private static final long serialVersionUID = -2822828798500614367L;

	public UnsupportedUnitTypeException(final Long unitType, final String unitTypeName) {
        super(generateMess(unitType, unitTypeName));
    }

	private static final String generateMess(final Long unitType, final String unitTypeName) {
		return "Unsupported unit type: \"" + unitTypeName + "\" ("+unitType.toString()+")"; 
	}
}
