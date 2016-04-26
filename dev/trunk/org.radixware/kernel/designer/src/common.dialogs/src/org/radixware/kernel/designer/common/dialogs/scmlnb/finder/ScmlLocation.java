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

package org.radixware.kernel.designer.common.dialogs.scmlnb.finder;

import java.lang.ref.WeakReference;
import org.radixware.kernel.common.scml.Scml;

/**
 * Defines location at the string representation of Scml.
 */
public class ScmlLocation {

    private WeakReference<Scml> weakScmlRef;
    private int scmlOffset;
    private int additionalOffset;

    public ScmlLocation(Scml scml, int scmlOffset, int additionalOffset) {
        weakScmlRef = new WeakReference<Scml>(scml);
        this.scmlOffset = scmlOffset;
        this.additionalOffset = additionalOffset;
    }

    public int getAdditionalOffset() {
        return additionalOffset;
    }

    public int getScmlOffset() {
        return scmlOffset;
    }

    public Scml getScml() {
        return weakScmlRef.get();
    }
}
