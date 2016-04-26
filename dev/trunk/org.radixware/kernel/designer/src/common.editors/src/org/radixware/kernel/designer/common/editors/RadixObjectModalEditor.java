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

package org.radixware.kernel.designer.common.editors;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;


public abstract class RadixObjectModalEditor<T extends RadixObject> extends RadixObjectEditor<T> {

    public RadixObjectModalEditor(T radixObject) {
        super(radixObject);
    }

    /**
     * Apply changed after OK button pressed
     */
    protected abstract void apply();
    private boolean complete = false;

    /**
     * @return true if OK buton enabled, false otherwise
     */
    // final - to use setComplete
    public final boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        if (this.complete != complete) {
            this.complete = complete;
            completeSupport.fireEvent(new RadixEvent());
        }
    }
    private final RadixEventSource completeSupport = new RadixEventSource();

    public RadixEventSource getCompleteSupport() {
        return completeSupport;
    }

    public String getTitle() {
        final RadixObject radixObject = getRadixObject();
        return radixObject.getTypeTitle() + " '" + radixObject.getName() + "'";
    }

    /**
     * If true and radix object is editable - OK & Cancel buttons will be displayed.
     * If false and radix object is editable - Close button will be displayed.
     * If radix object is not editable, function will be ignored and only Cancen button will be displayed.
     * @return true if changes can be cancelled by Cancel button click, false otherwise (changes applied at once).
     */
    public boolean isCancelable() {
        return true;
    }
}
