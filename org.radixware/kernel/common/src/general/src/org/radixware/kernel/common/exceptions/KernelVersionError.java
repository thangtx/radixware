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

public class KernelVersionError extends RadixError {
	private final Long loadedVer;
	private final Long actualVer;

	public KernelVersionError(long loadedVersion, long actualVersion) {
        super("Kernel runtime is not actual. Loaded version: " + String.valueOf(loadedVersion) + ", actual: " + String.valueOf(actualVersion));
		this.loadedVer = Long.valueOf(loadedVersion);
		this.actualVer = Long.valueOf(actualVersion);
    }

	public Long getLoadedVersion() {
		return loadedVer;
	}

	public Long getActualVersion() {
		return actualVer;
	}
}
