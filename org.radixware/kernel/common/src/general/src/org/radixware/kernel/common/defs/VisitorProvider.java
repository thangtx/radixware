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

package org.radixware.kernel.common.defs;

/**
 * Visitor provider for {@linkplain IVisitor}.
 * Defines rules to filter objects.
 */
public abstract class VisitorProvider implements IFilter<RadixObject> {

    private volatile boolean cancelled = false;

    /**
     * @return true if specified Radix object can contain an object satisfied to this {@linkplain VisitorProvider}, false otherwise.
     */
    public boolean isContainer(RadixObject radixObject) {
        return true;
    }

    /**
     * Cancel jog.
     */
    public void cancel() {
        cancelled = true;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public boolean isClassContainer(Class c) {
        return true;
    }

}
