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

import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.type.DefaultTypeSourceProviders.EntityBasedTypeSourceProvider;
import org.radixware.kernel.common.defs.ads.type.DefaultTypeSourceProviders.EntityTypeSourceProvider;
import org.radixware.kernel.common.defs.ads.type.DefaultTypeSourceProviders.EnumTypeSourceProvider;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;

/**
 * Interface for all Ads definitions that have a type
 *
 */
public interface IAdsTypedObject {

    public static class TypeProviderFactory {

        /**
         * Returns default visitor provider that collects all instances of
         * {@linkplain IAdsType} at context scope
         */
        public static final VisitorProvider getDefaultTypeSourceProvider(EValType toRefine, ERuntimeEnvironmentType env) {
            if (toRefine == null) {
                return null;
            }
            switch (toRefine) {
                case PARENT_REF:
                case OBJECT:
                case ARR_REF:
                    return AdsVisitorProviders.newEntityObjectTypeProvider(null);
                case INT:
                case ARR_INT:
                    return AdsVisitorProviders.newAdsEnumVisitorProvider(EValType.INT);
                case STR:
                case ARR_STR:
                    return AdsVisitorProviders.newAdsEnumVisitorProvider(EValType.STR);
                case CHAR:
                case ARR_CHAR:
                    return AdsVisitorProviders.newAdsEnumVisitorProvider(EValType.CHAR);
                case XML:
                    return AdsVisitorProviders.newXmlBasedTypesProvider(env);
                case USER_CLASS:
                    return AdsVisitorProviders.newClassBasedTypesProvider(env);
                default:
                    return null;
            }
        }

        /**
         * Returns visitor provider that collects all instances of
         * {@linkplain AdsEntityClassDef} at context scope. Usually used for
         * refinition of types {@linkplain EValType#ARR_REF},{@linkplain EValType#PARENT_REF},
         * {@linkplain EValType#OBJECT},{@linkplain EValType#ARR_OBJECT}
         */
        public static final VisitorProvider getEntityTypeSourceProvider(EValType typeToRefine) {
            return new EntityTypeSourceProvider(typeToRefine);
        }

        /**
         * Returns visitor provider that collects all instances of
         * {@linkplain AdsEntityClassDef} or {@linkplain AdsApplicationClassDef}
         * at context scope. Usually used for refinition of types {@linkplain EValType#ARR_REF},{@linkplain EValType#PARENT_REF},
         * {@linkplain EValType#OBJECT},{@linkplain EValType#ARR_OBJECT}
         */
        public static final VisitorProvider getEntityBasedTypeSourceProvider(EValType typeToRefine) {
            return new EntityBasedTypeSourceProvider(typeToRefine);
        }

        /**
         * Returns visitor provider that collects all instances of
         * {@linkplain AdsEnumDef} at context scope. Usually used for refinition
         * of types {@linkplain EValType#INT},{@linkplain EValType#CHAR},
         * {@linkplain EValTypeSTR}
         */
        public static final VisitorProvider getEnumTypeSourceProvider(EValType typeToRefine) {
            return new EnumTypeSourceProvider(typeToRefine);
        }

        public static final VisitorProvider newPropertyTypeProvider(EPropNature propNature) {
            return null;
        }

        public static final boolean defaultTypeRefineAllow(EValType toRefine, ERuntimeEnvironmentType env) {
            if (toRefine == null) {
                return false;
            }
            switch (toRefine) {
                case PARENT_REF:
                    return env == ERuntimeEnvironmentType.SERVER;
                case INT:
                case ARR_INT:
                case STR:
                case ARR_STR:
                case CHAR:
                case ARR_CHAR:

                case XML:
                    return true;
                default:
                    return false;
            }
        }
    }

    /**
     * Returns type declaration
     */
    public AdsTypeDeclaration getType();

    /**
     * Returns true if given value type identifier is applicable for the typed
     * object false otherwise
     */
    public boolean isTypeAllowed(EValType type);

    /*
     *Returns true if given simple type must be refined using definition based type
     */
    public boolean isTypeRefineAllowed(EValType type);

    /**
     * Returns visitor provider instance that helps to build list of
     * definitionsthat may be type for the typed object
     *
     * @param toRefine - simple type that should be refined with
     * definition-based type if null - no refinition will done
     */
    public VisitorProvider getTypeSourceProvider(EValType toRefine);
}
