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

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.IdTag;


public class IdVTag<T extends IdTag> extends SqmlVTag<T> {

    public IdVTag(T tag) {
        super(tag);
    }

    @Override
    public String getTokenName() {
        Definition def = getTag().findTarget();
        if (def instanceof DdsAccessPartitionFamilyDef || def instanceof AdsEditorPresentationDef){
            return "tag-id-1";
        }
        
        if(def instanceof DdsTableDef){
            return "tag-id-2";
        }
            
        return "tag-id";
    }

    @Override
    protected void printTitle(CodePrinter cp) {
        final T tag = getTag();
        final Definition def = tag.findTarget();
        cp.print("id[");
        if (def != null) {
            cp.print(def.getQualifiedName());
        } else {
            cp.printError();
        }
        cp.print("]");
    }
}