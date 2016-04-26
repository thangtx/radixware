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

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityBasedClassDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;


public abstract class AdsTablePropertyDef extends AdsServerSidePropertyDef {

    protected AdsTablePropertyDef(Id id, String name) {
        super(id, name);
    }

    protected AdsTablePropertyDef(AbstractPropertyDefinition xProp) {
        super(xProp);
    }

    protected AdsTablePropertyDef(AdsTablePropertyDef source, boolean forOverride) {
        super(source, forOverride);
    }

    public DdsTableDef findTable() {
        AdsClassDef cd = getOwnerClass();
        if (cd instanceof AdsEntityBasedClassDef) {
            return ((AdsEntityBasedClassDef) cd).findTable(this);
        } else {
            return null;
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
//        DdsTableDef table = this.findTable();
//        if (table != null) {
//            list.add(table);
//        }

    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        if (super.isSuitableContainer(collection)) {
            for (Definition def = collection.getOwnerDefinition(); def != null; def = def.getOwnerDefinition()) {
                if (def instanceof AdsClassDef) {
                    final EClassType ct = ((AdsClassDef) def).getClassDefType();
                    return ct == EClassType.APPLICATION || ct == EClassType.ENTITY;
                }
            }
            return false;
        } else {
            return false;
        }
    }
}
