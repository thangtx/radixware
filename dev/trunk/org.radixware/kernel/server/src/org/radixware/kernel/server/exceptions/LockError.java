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

import java.sql.SQLException;


public class LockError extends DatabaseError{
	private static final long serialVersionUID = -5965380532607108981L;
	
	public LockError(String mess, Throwable cause) {
        super(mess, cause);
    }
    public LockError(SQLException cause) {
    	super(cause);
    }
}
