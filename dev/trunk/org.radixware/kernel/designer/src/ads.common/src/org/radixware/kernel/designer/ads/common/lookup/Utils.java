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

package org.radixware.kernel.designer.ads.common.lookup;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.types.Id;


class Utils {

    protected static List<Id> familyClassIds(AdsEntityObjectClassDef clazz) {
        ArrayList<Id> ids = new ArrayList<Id>();
        ids.add(clazz.getId());
        AdsTypeDeclaration decl = clazz.getInheritance().getSuperClassRef();
        AdsEntityObjectClassDef current = clazz;
        while (decl != null) {
            AdsType sct = decl.resolve(clazz).get();
            if (sct != null) {
                if (sct instanceof AdsClassType) {
                    AdsClassDef sc = ((AdsClassType) sct).getSource();
                    if (sc == current) {
                        break;
                    }
                    if (sc instanceof AdsEntityObjectClassDef) {
                        ids.add(sc.getId());
                        current = (AdsEntityObjectClassDef) sc;
                        decl = sc.getInheritance().getSuperClassRef();
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return ids;
    }
}
