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

import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityGroupPresentations;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.radixdoc.EntityBasedClassRadixdoc;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.ClassDefinition.Entity;
import org.radixware.schemas.radixdoc.Page;


public class AdsEntityGroupClassDef extends AdsEntityBasedClassDef implements IAdsPresentableClass {

    public static final String PLATFORM_CLASS_NAME = "org.radixware.kernel.server.types.EntityGroup";
    public static final Id PREDEFINED_ID = Id.Factory.loadFrom("pdcEntityGroup_______________");

    public static final class Factory {

        public static AdsEntityGroupClassDef loadFrom(ClassDefinition classDef) {
            return new AdsEntityGroupClassDef(classDef);
        }

        public static AdsEntityGroupClassDef newInstance(AdsEntityClassDef clazz) {
            return new AdsEntityGroupClassDef("NewEntityGroupClass", clazz);
        }
    }
    private final EntityGroupPresentations presentations;

    AdsEntityGroupClassDef(ClassDefinition xDef) {
        super(xDef);
        this.presentations = (EntityGroupPresentations) EntityGroupPresentations.Factory.loadFrom(this, xDef.getPresentations());
    }

    AdsEntityGroupClassDef(String name, AdsEntityClassDef clazz) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_ENTITY_GROUP_CLASS), name);
        this.presentations = (EntityGroupPresentations) EntityGroupPresentations.Factory.newInstance(this);
        setBasis(clazz);
    }

    AdsEntityGroupClassDef(AdsEntityGroupClassDef source) {
        super(source);
        this.presentations = (EntityGroupPresentations) EntityGroupPresentations.Factory.newInstance(this);
    }

    /**
     * Associates this class with given entity class
     *
     * @throws {@linkplain DefinitionError} if added into container
     */
    public boolean setBasis(AdsEntityClassDef classDef) {
        if (classDef == null) {
            return false;
        }
        if (classDef.findEntityGroup() != null) {
            return false;
        }

        if (getContainer() != null) {
            throw new DefinitionError("Application class might be associated with entity during initial setup process only.", this);
        }
        this.entityId = classDef.entityId;

        this.setId(Id.Factory.changePrefix(classDef.getId(), EDefinitionIdPrefix.ADS_ENTITY_GROUP_CLASS));
        return true;
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.ENTITY_GROUP;
    }

    @Override
    public AdsEntityObjectClassDef findBasis() {
        AdsModule module = getModule();
        if (module == null) {
            return null;
        } else {
            AdsDefinition def = AdsSearcher.Factory.newAdsDefinitionSearcher(this).findById(getBasisId()).get();
            if (def instanceof AdsEntityObjectClassDef) {
                return (AdsEntityObjectClassDef) def;
            } else {
                return null;
            }
        }
    }

    public Id getBasisId() {
        return Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.ADS_ENTITY_CLASS);
    }

    @Override
    public EntityGroupPresentations getPresentations() {
        return presentations;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_ENTITY_GROUP;
    }

    @Override
    public SearchResult<? extends AdsDefinition> findComponentDefinition(Id id) {
        try {
            EDefinitionIdPrefix prefix = id.getPrefix();
            if (prefix == null) {
                return super.findComponentDefinition(id);
            }
            switch (prefix) {
                case COMMAND:
                    return getPresentations().getCommands().findById(id, EScope.ALL);
                default:
                    return super.findComponentDefinition(id);
            }
        } catch (NoConstItemWithSuchValueError e) {
            return super.findComponentDefinition(id);
        }
    }

    @Override
    protected void appendTo(ClassDefinition xClass, Entity xDef, ESaveMode saveMode) {
        super.appendTo(xClass, xDef, saveMode);
        presentations.appendTo(xClass.addNewPresentations(), saveMode);
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsClassDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new EntityBasedClassRadixdoc(getSource(), page, options);
            }
        };
    }
}
