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

package org.radixware.kernel.common.defs.ads.radixdoc;

import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;


public class EntityObjectClassRadixdoc extends EntityBasedClassRadixdoc {

    public EntityObjectClassRadixdoc(AdsClassDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    protected void writeClassDefInfo(ContentContainer overview, Table overviewTable) {
        super.writeClassDefInfo(overview, overviewTable);
        AdsEntityObjectClassDef entityCl = (AdsEntityObjectClassDef) source;
        int num = 1;
        for (AdsEntityObjectClassDef.DetailReferenceInfo detailInfo : entityCl.getAllowedDetailRefs()) {
            getClassWriter().addAllStrRow(overviewTable, "Allowed Detail #" + num++, detailInfo.findReference().getQualifiedName());
        }
    }
}
