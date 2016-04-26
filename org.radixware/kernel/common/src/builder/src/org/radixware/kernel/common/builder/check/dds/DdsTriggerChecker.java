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

package org.radixware.kernel.common.builder.check.dds;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.utils.Utils;


@RadixObjectCheckerRegistration
public class DdsTriggerChecker<T extends DdsTriggerDef> extends DdsDefinitionChecker<T> {

    public DdsTriggerChecker() {
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return DdsTriggerDef.class;
    }

    @Override
    public void check(T trigger, IProblemHandler problemHandler) {
        super.check(trigger, problemHandler);

        if (!trigger.isOverwrite()) {
            checkForDbNameDuplication(trigger, problemHandler);
        }

        for (DdsTriggerDef.ColumnInfo columnInfo : trigger.getColumnsInfo()) {
            final DdsColumnDef column = columnInfo.findColumn();
            if (column != null) {
                final IFilter<DdsColumnDef> columnForTriggerFilter = DdsVisitorProviderFactory.newColumnForTriggerFilter(trigger);
                if (!columnForTriggerFilter.isTarget(column)) {
                    error(trigger, problemHandler, "Illegal column in trigger: '" + column.getName());
                }
            } else {
                error(trigger, problemHandler, "Trigger column not found: " + String.valueOf(columnInfo.getColumnId()));
            }
        }

        // override
        if (trigger.isOverwrite()) {
            final DdsTriggerDef overwritten = trigger.findOverwritten();
            if (overwritten == null) {
                error(trigger, problemHandler, "Overwritten trigger not found");
            }
        }
    }
}
