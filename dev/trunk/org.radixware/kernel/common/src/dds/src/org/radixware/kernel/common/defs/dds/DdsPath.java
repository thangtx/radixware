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

package org.radixware.kernel.common.defs.dds;

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionPath;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


public class DdsPath extends DefinitionPath {

    public DdsPath(DefinitionPath source) {
        super(source);
    }

    public DdsPath(List<Id> ids) {
        super(ids);
    }

    public DdsPath(Id[] ids) {
        super(ids);
    }

    public DdsPath(Definition source) {
        super(source);
    }

    @Override
    protected DefinitionSearcher<? extends Definition> getSearcher(EDefinitionIdPrefix IdPrefix, Definition context) {
        if (context == null) {
            return null;
        }

        final Module module = context.getModule();
        if (module != null) {
            return module.getDefinitionSearcher();
        } else {
            return null;
        }
    }

    @Override
    protected SearchResult< ? extends Definition> findComponent(Definition root, final Id id, Definition initialContext) {
        if (id == null) {
            return SearchResult.empty();
        }

        if (root instanceof DdsTableDef) {
            final DdsTableDef table = (DdsTableDef) root;
            switch (id.getPrefix()) {
                case DDS_COLUMN:
                    return table.getColumns().findById(id, EScope.LOCAL_AND_OVERWRITE);
                case DDS_TRIGGER:
                    return table.getTriggers().findById(id, EScope.LOCAL_AND_OVERWRITE);
                case DDS_INDEX:
                    final DdsPrimaryKeyDef pk = table.getPrimaryKey();
                    if (Utils.equals(pk.getId(), id)) {
                        return SearchResult.single(pk);
                    } else {
                        return table.getIndices().findById(id, EScope.LOCAL_AND_OVERWRITE);
                    }
            }
        }

        return super.findComponent(root, id, initialContext);
    }

    public static SearchResult<Definition> resolve(DdsDefinition context, Id[] ids) {
        DdsPath path = new DdsPath(ids);
        return path.resolve(context);
    }
}
