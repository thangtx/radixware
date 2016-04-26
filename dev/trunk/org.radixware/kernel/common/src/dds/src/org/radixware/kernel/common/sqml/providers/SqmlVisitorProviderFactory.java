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

package org.radixware.kernel.common.sqml.providers;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;


public class SqmlVisitorProviderFactory {

    private SqmlVisitorProviderFactory() {
    }

    public static VisitorProvider newSequenceDbNameTagProvider() {
        return DdsVisitorProviderFactory.newSequenceProvider();
    }

    public static VisitorProvider newTableForIdTagProvider() {
        return DdsVisitorProviderFactory.newTableProvider();
    }

    public static VisitorProvider newTableSqlNameTagProvider() {
        return DdsVisitorProviderFactory.newGeneratedTableProvider();
    }

    public static VisitorProvider newIndexDbNameTagProvider() {
        return DdsVisitorProviderFactory.newGeneratedIndexProvider();
    }

    public static VisitorProvider newColumnForIdTagProvider() {
        return DdsVisitorProviderFactory.newColumnProvider();
    }

    public static VisitorProvider newConstValueTagProvider() {
        return VisitorProviderFactory.createEnumItemVisitorProvider();
    }

    public static VisitorProvider newTypifiedValueTagProvider() {
        return DdsVisitorProviderFactory.newColumnProvider();
    }

    // ================= own ==================
    public static VisitorProvider newPropSqlNameTagProvider(Sqml sqml, PropSqlNameTag.EOwnerType ownerType) {
        assert sqml != null;
        assert ownerType != null;
        return sqml.getEnvironment().getPropProvider(ownerType);
    }

    public static VisitorProvider newDbFuncCallTagProvider(RadixObject context) {
        return new DbFuncCallTagProvider(context);
    }

    public static VisitorProvider newIdTagProvider(final Class<? extends Definition> template) {
        return newIdTagProvider(template, null);
    }

    public static VisitorProvider newIdTagProvider(final Class<? extends Definition> template, IFilter<RadixObject> filter) {
        if (template == DdsColumnDef.class) {
            return DdsVisitorProviderFactory.newColumnProvider();
        } else if (template == DdsTableDef.class) {
            return DdsVisitorProviderFactory.newTableProvider();
        } else {
            return new IdTagProvider(template, filter);
        }
    }

    public static VisitorProvider newDbNameTagProvider(final Class<? extends Definition> template) {
        return new IdTagProvider(template, null);
    }

    public static IFilter<DdsReferenceDef> newJoinTagFilter(final DdsTableDef childTable) {
        return new IFilter<DdsReferenceDef>() {
            @Override
            public boolean isTarget(DdsReferenceDef ref) {
                return ref.isGeneratedInDb();
            }
        };
    }
}
