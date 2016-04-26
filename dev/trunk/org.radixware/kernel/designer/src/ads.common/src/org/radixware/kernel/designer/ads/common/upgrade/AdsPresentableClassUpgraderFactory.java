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

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.upgrade.IRadixObjectUpgrader;
import org.w3c.dom.Element;


public class AdsPresentableClassUpgraderFactory extends AdsClassUpgraderFactory {

    @Override
    public boolean isSupportedClassType(EClassType classType) {
        switch (classType) {
            case ENTITY:
            case APPLICATION:
            case FORM_HANDLER:
                return true;
            default:
                return false;
        }
    }

    public int getActualVersion() {
        return IAdsPresentableClass.FORMAT_VERSION;
    }

    public IRadixObjectUpgrader createUpgrader(int toVersion) {
        switch (toVersion) {
//            case 7:
//                return new AdsPresentableClassUpgrader7();
            case 6:
                return new AdsPresentableClassUpgrader6();
            default:
                return new IRadixObjectUpgrader() {

                    public void firstStep(Element root) {
                        //ignore
                    }

                    public void finalStep(RadixObject radixObject) {
                        //ignore
                    }
                };
        }
    }
}
