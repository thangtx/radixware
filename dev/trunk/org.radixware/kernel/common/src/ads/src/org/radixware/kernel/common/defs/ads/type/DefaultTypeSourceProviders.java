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

package org.radixware.kernel.common.defs.ads.type;

import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EValType;


class DefaultTypeSourceProviders {

    /**
     * Default type source visitor provider
     * Allows all objects implementing interface {@linkplain AdsType} to be choosen for typyfing
     */
    static class DefaultTypeSourceProvider extends VisitorProvider {

        protected final EValType valType;

        public DefaultTypeSourceProvider(EValType valType) {
            this.valType = valType;
        }

        /**
         * returns true is object is branch layer segment or module
         */
        @Override
        public boolean isContainer(RadixObject object) {
            if (object instanceof Module) {
                return object instanceof AdsModule;
            }
            return super.isContainer(object);
        }

        /**
         * Returns object instanceof AdsType;
         */
        @Override
        public boolean isTarget(RadixObject object) {
            return object instanceof IAdsTypeSource && !(object instanceof AdsScopeCommandDef);
        }
    }

    static class EntityTypeSourceProvider extends DefaultTypeSourceProvider {

        public EntityTypeSourceProvider(EValType valType) {
            super(valType);
        }

        /**
         * Returns object instanceof AdsEntityClassDef
         */
        @Override
        public boolean isTarget(RadixObject object) {
            return object instanceof AdsEntityClassDef;
        }
    }

    static class EntityBasedTypeSourceProvider extends DefaultTypeSourceProvider {

        public EntityBasedTypeSourceProvider(EValType valType) {
            super(valType);
        }

        /**
         * Returns object instanceof AdsEntityClassDef
         */
        @Override
        public boolean isTarget(RadixObject object) {
            return object instanceof AdsEntityClassDef || object instanceof AdsApplicationClassDef;
        }
    }

    /**
     * Standard type source provider for int,char or string typed objects
     * Provides list of Ads enumeration definitions with given type 
     */
    static class EnumTypeSourceProvider extends DefaultTypeSourceProvider {

        public EnumTypeSourceProvider(EValType valType) {
            super(valType);
        }

        /**
         * Returns object instanceof AdsEnumDef with item type of provider's val type
         */
        @Override
        public boolean isTarget(RadixObject object) {
            if (object instanceof AdsEnumDef) {
                AdsEnumDef e = (AdsEnumDef) object;
                return e.getItemType() == valType;
            } else {
                return false;
            }
        }
    }
}
