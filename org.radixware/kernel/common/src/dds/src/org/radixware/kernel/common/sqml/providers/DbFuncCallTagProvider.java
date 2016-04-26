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

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.defs.dds.providers.DdsFunctionProvider;


class DbFuncCallTagProvider extends DdsFunctionProvider {

    private final RadixObject context;

    public DbFuncCallTagProvider(RadixObject context) {
        this.context = context;
    }

    private static DdsPlSqlObjectDef findOwnerPlSqlObject(RadixObject radixObject) {
        for (RadixObject ro = radixObject; ro != null; ro = ro.getContainer()) {
            if (ro instanceof DdsPlSqlObjectDef) {
                return (DdsPlSqlObjectDef) ro;
            }
        }
        return null;
    }

    @Override
    public boolean isTarget(RadixObject radixObject) {
        if (radixObject instanceof DdsFunctionDef) {
            final DdsFunctionDef function = (DdsFunctionDef) radixObject;

            if (!function.isGeneratedInDb()) {
                return false;
            }

            // public functions allowed only in current package
            if (!function.isPublic()) {
                final DdsPlSqlObjectDef ownerPlSqlObject = function.getOwnerPlSqlObject();
                if (ownerPlSqlObject == null) {
                    return true;
                }
                final DdsPlSqlObjectDef requiredOwnerPlSqlObject = findOwnerPlSqlObject(context);
                if (requiredOwnerPlSqlObject == null) {
                    return true; // to call full sql of package
                }
                return requiredOwnerPlSqlObject == ownerPlSqlObject;
            }

//            if (!function.getPurityLevel().isWNDS()) {
//                final ISqmlEnvironment environment = (sqml != null ? sqml.getEnvironment() : null);
//                if (environment != null && !environment.isDbModificationAllowed()) {
//                    return false;
//                }
//            }

            return true;
        }

        return false;
    }
}
