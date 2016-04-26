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

package org.radixware.kernel.designer.common.editors.jml.vtag;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagId;


public class VJmlTagId extends VJmlTag<JmlTagId> {

    @Override
    public String getTokenName() {
        Jml jml = getTag().getOwnerJml();
        if (jml != null){
            Definition def = getTag().resolve(jml.getOwnerDefinition());
            if (def instanceof DdsAccessPartitionFamilyDef 
                    || def instanceof AdsEditorPresentationDef
                     || def instanceof AdsContextlessCommandDef){
                return "tag-id-1";
            }

            if(def instanceof DdsTableDef || def instanceof AdsCommandDef){
                return "tag-id-2";
            }
        }    
        return "tag-id";
    }

    public VJmlTagId(JmlTagId tag) {
        super(tag);
    }
}
