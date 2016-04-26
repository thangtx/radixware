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

package org.radixware.kernel.server.dbq;

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.types.Id;


final class QueryBatchOptions {
	private final Map<Id, Integer> batchSizebyTabId = new HashMap<Id, Integer>();

	void clear(){
		batchSizebyTabId.clear();
	}

	void setBatchSize(final Id tableId, final int size){
		batchSizebyTabId.put(tableId, Integer.valueOf(size));
	}

	int getBatchSize(final Id tableId){
		final Integer size = batchSizebyTabId.get(tableId);
		if (size != null)
			return size.intValue();
		return 1;
	}
}
