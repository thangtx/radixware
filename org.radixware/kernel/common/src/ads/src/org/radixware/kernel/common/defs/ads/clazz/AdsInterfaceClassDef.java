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

package org.radixware.kernel.common.defs.ads.clazz;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.schemas.adsdef.ClassDefinition;


public class AdsInterfaceClassDef extends AdsDynamicClassDef {

    public static final class Factory {

        public static AdsInterfaceClassDef loadFrom(ClassDefinition classDef) {
            return new AdsInterfaceClassDef(classDef);
        }

        public static AdsInterfaceClassDef newInstance(ERuntimeEnvironmentType env) {
            return new AdsInterfaceClassDef("NewInterfaceClass", env);
        }
    }

    protected AdsInterfaceClassDef(String name, ERuntimeEnvironmentType env) {
        super(EDefinitionIdPrefix.ADS_INTERFACE_CLASS, name, env);
    }

    public AdsInterfaceClassDef(ClassDefinition xDef) {
        super(xDef);
    }

    public AdsInterfaceClassDef(AdsInterfaceClassDef source) {
        super(source);
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.INTERFACE;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_INTERFACE;
    }

    @Override
    public String getTypeTitle() {
        return environment.getName() + " Interface";
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        if (super.isSuitableContainer(collection)) {
            final Definition owner = collection.getOwnerDefinition();
            if (owner instanceof AdsClassDef) {
                return !((AdsClassDef) owner).isInner();
            }
            return true;
        }
        return false;
    }
}
