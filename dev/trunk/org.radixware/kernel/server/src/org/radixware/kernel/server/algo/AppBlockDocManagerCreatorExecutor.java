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

package org.radixware.kernel.server.algo;

import org.radixware.kernel.server.types.Algorithm;

public class AppBlockDocManagerCreatorExecutor extends AppBlockFormCreatorExecutor {

	static final String DWF_DOCMANAGER_CLASS_ID = "acl4GEVMJ72XTNRDF5NABIFNQAABA";

	// return 0 - wait, 1 - timeout если задан	
	static public int invoke(final Algorithm algo) throws Exception {
		//TODO сгенерировать форму
		return 0;
	}
	public static int resume(final Algorithm algo) {
		//TODO таймаут или редактирование завершено
		return 0;
	}
}