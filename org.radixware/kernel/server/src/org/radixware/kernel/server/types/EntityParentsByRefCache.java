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

package org.radixware.kernel.server.types;

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;


class EntityParentsByRefCache {

	private Map<DdsReferenceDef, Entity> cache = null;

	final void clear() {
		if (cache != null) {
			cache.clear();
		}
	}

	final void cache(final DdsReferenceDef ref, final Entity parent) {
		if (/*ref.isByPrimaryKey() &&*/(parent == null || parent.isInDatabase(false))) {
			if (cache == null) {
				cache = new HashMap<DdsReferenceDef, Entity>();
			}
			cache.put(ref, parent);
		}
	}

	final boolean contains(final DdsReferenceDef ref) {
		return (cache != null) && cache.containsKey(ref);
	}

	final Entity get(final DdsReferenceDef ref) {
		if (cache != null) {
			return cache.get(ref);
		}
		return null;
	}
}
