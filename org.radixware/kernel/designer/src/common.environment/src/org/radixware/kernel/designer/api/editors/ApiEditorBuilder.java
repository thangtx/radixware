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

import org.radixware.kernel.common.defs.RadixObject;


public abstract class ApiEditorBuilder<T extends RadixObject> {

    private final BrickFactory componentFactory;
    private final T source;

    public ApiEditorBuilder(T object, BrickFactory componentFactory) {
        this.componentFactory = componentFactory;
        this.source = object;
    }

    public BrickFactory getBrickFactory() {
        return componentFactory;
    }

    public final T getSource() {
        return source;
    }

    public abstract ApiEditorModel buildModel();
}
