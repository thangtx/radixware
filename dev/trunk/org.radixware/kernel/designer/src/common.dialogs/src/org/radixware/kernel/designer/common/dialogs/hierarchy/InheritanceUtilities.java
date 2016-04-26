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

package org.radixware.kernel.designer.common.dialogs.hierarchy;

import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;


public class InheritanceUtilities {

    public static AdsClassDef getSuperClass(AdsClassDef classDef) {
        if (classDef.getInheritance().getSuperClassRef() == null) {
            return null;
        }
        AdsType type = classDef.getInheritance().getSuperClassRef().resolve(classDef).get();
        if (type instanceof AdsClassType) {
            return ((AdsClassType) type).getSource();
        }
        return null;
    }

    public static AdsClassDef getOverwrittenClass(AdsClassDef classDef) {
        if (classDef.isOverwrite()) {
            return classDef.getHierarchy().findOverwritten().get();
        }
        return null;
    }

    public static AdsClassDef getSuperOrOverwrittenClass(AdsClassDef classDef) {
        AdsClassDef superClassDef = getSuperClass(classDef);
        if (superClassDef != null) {
            return superClassDef;
        }
        return getOverwrittenClass(classDef);
    }
}
