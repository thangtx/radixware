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

package org.radixware.kernel.server.utils;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.server.types.Pid;



public final class TransactionLock extends Object{
	private final List<Pid> lockedPids = new ArrayList<Pid>(); 
	
	public final void unregisterAllLocks(){
            lockedPids.clear();
	}
	
	public final void registerLock(final Pid pid){
            lockedPids.add(pid);
	}
	
	public boolean isLocked(final Pid pid){
            return lockedPids.contains(pid);
	}

}
