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

package org.radixware.kernel.common.builder.check.ads.clazz;

import java.util.HashMap;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.types.Id;


class ClassCache {

    HashMap<Id, AdsClassDef> id2clazz = new HashMap<Id, AdsClassDef>();

    ClassCache(Definition context) {
    }

    public AdsClassDef findById(Definition context, final Id id) {

        if (id2clazz.containsKey(id)) {
            return id2clazz.get(id);
        } else if (context.isInBranch()) {
            RadixObject obj = context.getModule().getSegment().getLayer().getBranch().find(new VisitorProvider() {

                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return radixObject instanceof AdsClassDef && ((AdsClassDef) radixObject).getId() == id;
                }
            });
            if (obj instanceof AdsClassDef) {
                AdsClassDef clazz = (AdsClassDef) obj;
                id2clazz.put(id, clazz);
                return clazz;
            } else {
                id2clazz.put(id, null);
                return null;
            }
        } else {
            return null;
        }
    }
}
