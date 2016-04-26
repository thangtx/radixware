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

package org.radixware.kernel.server.trace;

import org.radixware.kernel.common.trace.TraceProfile;

public final class TraceTarget {
	private TraceProfile profile;
	/**
	 * if null - write to RDX_Event
	 */
	final TraceBuffer buffer;
	
	TraceTarget(final TraceProfile profile, final TraceBuffer buffer){
		this.profile = profile;
		this.buffer  = buffer;
	}

	public void setProfile(final TraceProfile profile) {
		this.profile = profile;
	}
	public TraceProfile  getProfile() {
		return profile;
	}
}
