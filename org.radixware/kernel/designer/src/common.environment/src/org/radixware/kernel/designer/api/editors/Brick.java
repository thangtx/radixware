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

package org.radixware.kernel.designer.api.editors;

import java.awt.GridBagConstraints;
import org.radixware.kernel.common.defs.RadixObject;


public abstract class Brick<T extends RadixObject> extends ApiEditorModel<T> {

    private final String tag;
    public GridBagConstraints constraints;

    public Brick(T source) {
        this(source, null, null);
    }

    public Brick(T source, String tag) {
        this(source, null, tag);
    }

    public Brick(T source, GridBagConstraints constraints, String tag) {
        super(source);
        this.constraints = constraints;
        this.tag = tag;
    }

    public boolean isEditor() {
        return false;
    }

    public String getTag() {
        return tag;
    }
}
