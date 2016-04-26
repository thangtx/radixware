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


package org.radixware.kernel.designer.api.editors.components;

import javax.swing.JComponent;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.api.editors.SimpleBrick;


public class DefinitionBrick<T extends RadixObject> extends SimpleBrick<T> {

    public DefinitionBrick(T source, JComponent component) {
        super(source, component, null, null, null);
    }

    @Override
    public boolean isEditor() {
        return true;
    }
}
