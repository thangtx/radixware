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

package org.radixware.kernel.designer.dds.editors.table;

import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsUniqueConstraintDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.dds.script.defs.DdsUniqueConstraintScriptGenerator;


class DdsUniqueConstraintsUpdater {

    private static void update(final DdsUniqueConstraintDef oldUc, final DdsUniqueConstraintDef newUc) {
        if (oldUc.isGeneratedInDb() && newUc.isGeneratedInDb()) {
            final DdsUniqueConstraintScriptGenerator ucScriptGenerator = DdsUniqueConstraintScriptGenerator.Factory.newInstance();
            if (ucScriptGenerator.isModifiedToDrop(oldUc, newUc)) {
                // change ID of old index unique constraint.
                final DdsUniqueConstraintDef uc = newUc.getClipboardSupport().copy();
                final DdsIndexDef index = newUc.getOwnerIndex();
                index.setUniqueConstraint(uc);
            }
        }
    }

    private static void update(final DdsIndexDef oldIndex, final DdsIndexDef newIndex) {
        if (oldIndex.isSecondaryKey() && newIndex.isSecondaryKey()) {
            update(oldIndex.getUniqueConstraint(), newIndex.getUniqueConstraint());
        }
    }

    public static void update(final DdsTableDef oldTable, final DdsTableDef newTable) {
        update(oldTable.getPrimaryKey(), newTable.getPrimaryKey());

        for (DdsIndexDef newIndex : newTable.getIndices().getLocal()) {
            final Id indexId = newIndex.getId();
            final DdsIndexDef oldIndex = oldTable.getIndices().findById(indexId, EScope.LOCAL).get();
            if (oldIndex != null) {
                update(oldIndex, newIndex);
            }
        }
    }
}
