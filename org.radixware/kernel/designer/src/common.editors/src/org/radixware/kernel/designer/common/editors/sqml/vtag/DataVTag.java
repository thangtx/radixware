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

package org.radixware.kernel.designer.common.editors.sqml.vtag;

import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsIncludeObject;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.DataTag;


public class DataVTag<T extends DataTag> extends SqmlVTag<T> {

    public DataVTag(T tag) {
        super(tag);
    }

    @Override
    public String getTokenName() {
        return "tag-data";
    }

    @Override
    protected void printTitle(CodePrinter cp) {
        final T tag = getTag();
        final AdsDefinition def = (AdsDefinition)tag.findTarget();
        if (def != null) {
            if (def instanceof AdsAppObject.Prop || def instanceof AdsIncludeObject.Param) {
                cp.print(def.getOwnerDefinition().getName() + "." + def.getName());
            } else if (def instanceof AdsAlgoClassDef.Param) {
                cp.print(def.getOwnerDef().getName() + "::" + def.getName());
            } else
                cp.print(def.getName());
        } else {
            cp.printError();
        }
    }    
}