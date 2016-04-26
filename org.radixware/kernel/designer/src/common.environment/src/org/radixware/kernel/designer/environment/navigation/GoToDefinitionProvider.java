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

package org.radixware.kernel.designer.environment.navigation;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;

/**
 * Visitor provider for GoToDefinitionAction.
 */
public class GoToDefinitionProvider extends VisitorProvider {

    @Override
    public boolean isTarget(RadixObject radixObject) {
        if (!(radixObject instanceof Definition)) {
            return false;
        }

        Definition definition = (Definition) radixObject;

        return (definition instanceof DdsTableDef) ||
                (definition instanceof DdsSequenceDef) ||
                (definition instanceof DdsAccessPartitionFamilyDef) ||
                (definition instanceof DdsPlSqlObjectDef) ||
                (definition instanceof AdsClassDef) ||
                (definition instanceof AdsEnumDef);
    }
}
