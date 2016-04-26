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

package org.radixware.kernel.common.defs.ads.clazz.entity;

import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.IDependentId;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.schemas.adsdef.ClassDefinition;


public class AdsPresentationEntityAdapterClassDef extends AdsEntityBasedClassDef implements IDependentId {

    public static final String PLATFORM_ADAPTER_CLASS_NAME = "org.radixware.kernel.server.types.PresentationEntityAdapter";

    public static final class Factory {

        public static AdsPresentationEntityAdapterClassDef loadFrom(ClassDefinition classDef) {
            return new AdsPresentationEntityAdapterClassDef(classDef);
        }

        public static AdsPresentationEntityAdapterClassDef newInstance(AdsEntityObjectClassDef basis) {
            return new AdsPresentationEntityAdapterClassDef("NewPresentationEntityAdapterClass", basis);
        }
    }

    AdsPresentationEntityAdapterClassDef(ClassDefinition xDef) {
        super(xDef);
    }

    AdsPresentationEntityAdapterClassDef(String name, AdsEntityObjectClassDef basis) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_PRESENTATION_ENTITY_ADAPTER_CLASS), name);
        setBasis(basis);
    }

    AdsPresentationEntityAdapterClassDef(AdsEntityBasedClassDef source) {
        super(source);
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.PRESENTATION_ENTITY_ADAPTER;
    }

    /**
     * Associates this class with given entity object class
     *
     * @throws {@linkplain DefinitionError} if added into container
     */
    public boolean setBasis(AdsEntityObjectClassDef classDef) {
        if (classDef == null) {
            return false;
        }

        AdsPresentationEntityAdapterClassDef adapter = classDef.findPresentationAdapter();

        /**
         * multiple adapters not supprted
         */
        if (adapter != null) {
            return false;
        }

        final AdsEntityObjectClassDef targetBasis = classDef.findBasis();

        if (targetBasis == null) {
            if (!(classDef instanceof AdsEntityClassDef)) {
                return false;//error in search
            }
        } else {
            adapter = targetBasis.findPresentationAdapter();
            if (adapter == null)//no adapter in target's basis
            {
                return false;

            }
        }

        if (getContainer() != null) {
            throw new DefinitionError("Application class might be associated with entity during initial setup process only.", this);
        }

        this.setId(Id.Factory.changePrefix(classDef.getId(), EDefinitionIdPrefix.ADS_PRESENTATION_ENTITY_ADAPTER_CLASS));
        this.entityId = classDef.entityId;

//        if (adapter != null) {
//            this.getInheritance().setSuperClass(adapter);
//        }
        return true;
    }

    /**
     * Returns entity or application class which is adopted by th class
     */
    @Override
    public AdsEntityObjectClassDef findBasis() {
        final AdsModule module = getModule();
        if (module == null) {
            return null;
        } else {
            AdsEntityObjectClassDef res = findBasisImpl(module, EDefinitionIdPrefix.ADS_ENTITY_CLASS);
            if (res == null) {
                res = findBasisImpl(module, EDefinitionIdPrefix.ADS_APPLICATION_CLASS);
            }
            return res;
        }
    }

    private AdsEntityObjectClassDef findBasisImpl(AdsModule module, EDefinitionIdPrefix prefix) {
        final AdsDefinition def = AdsSearcher.Factory.newAdsDefinitionSearcher(this).findById(Id.Factory.changePrefix(getId(), prefix)).get();
        if (def instanceof AdsEntityObjectClassDef) {
            return (AdsEntityObjectClassDef) def;
        } else {
            return null;
        }
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_ENTITY_ADAPTER;
    }
}
