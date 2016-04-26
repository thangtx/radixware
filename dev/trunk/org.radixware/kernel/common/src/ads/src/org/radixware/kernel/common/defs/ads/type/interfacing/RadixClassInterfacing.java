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

package org.radixware.kernel.common.defs.ads.type.interfacing;

import java.util.HashSet;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsInterfaceClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;


public final class RadixClassInterfacing {

    private final AdsClassDef base;

    public RadixClassInterfacing(AdsClassDef base) {
        this.base = base;
    }

    public boolean isSuperFor(Definition definition) {

        if (definition == base) {
            return true;
        }

        if (definition instanceof AdsClassDef) {
            AdsClassDef classDef = (AdsClassDef) definition;
            if (base instanceof AdsInterfaceClassDef) {
                return isDescendant(base, classDef);
            } else {
                return classDef.getInheritance().isSubclassOf(base);
            }
        }

        return false;
    }
    private Set<AdsClassDef> used = new HashSet<>();

    private boolean isDescendant(AdsClassDef base, AdsClassDef child) {

        if (child == null || used.contains(child)) {
            return false;
        }

        if (base == child) {
            return true;
        }

        for (AdsTypeDeclaration baseInterface : child.getInheritance().getInerfaceRefList(EScope.LOCAL_AND_OVERWRITE)) {
            AdsClassType resolve = (AdsClassType) baseInterface.resolve(child).get();
            if (resolve != null) {

                AdsClassDef source = resolve.getSource();
                if (isDescendant(base, source)) {
                    return true;
                }
                used.add(source);
            }
        }

        return isDescendant(base, child.getInheritance().findSuperClass().get());
    }
}
