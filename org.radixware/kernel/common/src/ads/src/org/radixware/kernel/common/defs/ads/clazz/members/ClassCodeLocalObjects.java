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

package org.radixware.kernel.common.defs.ads.clazz.members;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;


public class ClassCodeLocalObjects<T extends RadixObject> extends RadixObjects<T> {

    public ClassCodeLocalObjects(String name) {
        super(name);
    }

    public ClassCodeLocalObjects() {
    }

    public ClassCodeLocalObjects(RadixObject container) {
        super(container);
    }

    @Override
    public boolean isReadOnly() {
        if (super.isReadOnly()) {
            return true;
        }
        AdsClassDef clazz = getOwnerClass();
        if (clazz == null) {
            return true;
        }
        return !clazz.isCodeEditable();
    }

    public AdsClassDef getOwnerClass() {
        RadixObject c = getContainer();
        while (c != null) {
            if (c instanceof AdsClassDef) {
                return (AdsClassDef) c;
            }
            c = c.getContainer();
        }
        return null;
    }
}
