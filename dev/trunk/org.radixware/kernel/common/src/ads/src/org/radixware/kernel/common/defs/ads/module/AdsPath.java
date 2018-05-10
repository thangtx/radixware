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

package org.radixware.kernel.common.defs.ads.module;

import java.util.List;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsPath;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * Path object is a wrapper for id sequence uniquely describing definition
 * location
 *
 */
public class AdsPath extends DefinitionPath {

    public AdsPath(AdsDefinition source) {
        super(source);
    }

    public AdsPath(Definition source) {
        super(source);
    }

    public AdsPath(DdsTableDef source) {
        super(source);
    }

    public AdsPath(Module source) {
        super(source);
    }

    public AdsPath(Id[] ids) {
        super(ids);
    }

    public AdsPath(List<Id> ids) {
        super(ids);
    }

    public AdsPath(AdsPath source) {
        super(source);
    }

    /**
     * Tries to find definition component by given id
     */
    @Override
    protected SearchResult<? extends Definition> findComponent(Definition root, final Id id, Definition initialContext) {
        if (root instanceof AdsDefinition) {
            return ((AdsDefinition) root).findComponentDefinition(id);
        } else {
            if (root instanceof DdsTableDef) {
                final DdsTableDef table = (DdsTableDef) root;
                switch (id.getPrefix()) {
                    case DDS_COLUMN:
                        return table.getColumns().findById(id, ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE);
                    case DDS_TRIGGER:
                        return table.getTriggers().findById(id, ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE);
                    case DDS_INDEX:
                        final DdsPrimaryKeyDef pk = table.getPrimaryKey();
                        if (Utils.equals(pk.getId(), id)) {
                            return SearchResult.single(pk);
                        } else {
                            return table.getIndices().findById(id, ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE);
                        }
                }
            }
            return super.findComponent(root, id, initialContext);
        }
    }

    @Override
    protected DefinitionSearcher<? extends Definition> getSearcher(EDefinitionIdPrefix IdPrefix, Definition context) {
        switch (IdPrefix) {
            case DDS_TABLE:
                return AdsSearcher.Factory.newDdsTableSearcher(context);
            case DDS_REFERENCE:
                return AdsSearcher.Factory.newDdsReferenceSearcher(context);
            case DDS_SEQUENCE:
                return AdsSearcher.Factory.newDdsSequenceSearcher(context);
            case DDS_FUNCTION:
                return AdsSearcher.Factory.newDdsFunctionSearcher(context);
            case DDS_PACKAGE:
                return AdsSearcher.Factory.newDdsPackageSearcher(context);
            case DDS_TYPE:
                return AdsSearcher.Factory.newDdsTypeSearcher(context);
            case DDS_ACCESS_PARTITION_FAMILY:
                return AdsSearcher.Factory.newDdsApfSearcher(context);
            case IMAGE:
                return AdsSearcher.Factory.newImageSearcher(context);
            default:
                return AdsSearcher.Factory.newAdsDefinitionSearcher(context);
        }
    }

    public static SearchResult<Definition> resolve(Definition context, Id[] ids) {
        AdsPath path = new AdsPath(ids);
        return path.resolve(context);
    }
}
