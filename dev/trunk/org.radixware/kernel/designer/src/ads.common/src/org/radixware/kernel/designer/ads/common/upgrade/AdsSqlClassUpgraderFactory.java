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

package org.radixware.kernel.designer.ads.common.upgrade;

import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.upgrade.IRadixObjectUpgrader;


public class AdsSqlClassUpgraderFactory extends AdsClassUpgraderFactory {

    public AdsSqlClassUpgraderFactory() {
    }

    @Override
    public int getActualVersion() {
        return AdsSqlClassDef.FORMAT_VERSION;
    }

    @Override
    public boolean isSupportedClassType(EClassType classType) {
        return classType == EClassType.SQL_CURSOR || classType == EClassType.SQL_PROCEDURE || classType == EClassType.SQL_STATEMENT || classType == EClassType.REPORT;
    }

    public IRadixObjectUpgrader createUpgrader(int toVersion) {
        switch (toVersion) {
            case 1:
                return new AdsSqlClassUpgrader1();
            case 2:
                return new AdsSqlClassUpgrader2();
            case 3:
                return new AdsSqlClassUpgrader3();
            case 4:
                return new AdsSqlClassUpgrader4();
            case 5:
                return new AdsSqlClassUpgrader5();
            case 6:
                return new AdsPresentableClassUpgrader6();
            case 7:
                return new AdsSqlClassUpgrader7();
//            case 8:
//                return new AdsSqlClassUpgrader8();
        }
        return null;
    }
}
