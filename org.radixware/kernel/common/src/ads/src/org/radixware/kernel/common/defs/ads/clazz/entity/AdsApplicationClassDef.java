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

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.defs.ads.radixdoc.EntityObjectClassRadixdoc;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;


public class AdsApplicationClassDef extends AdsEntityObjectClassDef {

    public static final int FORMAT_VERSION = IAdsPresentableClass.FORMAT_VERSION;

    @Override
    public long getFormatVersion() {
        return FORMAT_VERSION;
    }

    public static final class Factory {

        public static AdsApplicationClassDef loadFrom(ClassDefinition classDef) {
            return new AdsApplicationClassDef(classDef);
        }

        public static AdsApplicationClassDef newInstance(AdsEntityObjectClassDef basis) {
            return new AdsApplicationClassDef(basis, "NewApplicationOf" + basis.getName());
        }
    }

    AdsApplicationClassDef(ClassDefinition xDef) {
        super(xDef);
    }

    AdsApplicationClassDef(AdsEntityObjectClassDef basis, String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_APPLICATION_CLASS), name);
        this.setBasis(basis);
    }

    AdsApplicationClassDef(AdsEntityObjectClassDef source) {
        super(source);
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.APPLICATION;
    }

    /**
     * Make this class extends given entity class and associates it with given
     * class's table
     *
     * @throws {@linkplain DefinitionError} if added into container
     */
    public boolean setBasis(AdsEntityObjectClassDef classDef) {
        DdsTableDef table = classDef.findTable(this);
        if (table == null) {
            return false;
        }
        if (getContainer() != null) {
            throw new DefinitionError("Application class might be associated with entity during initial setup process only.", this);
        }
        this.entityId = classDef.entityId;

        if (classDef.getClientEnvironment() == ERuntimeEnvironmentType.EXPLORER
                || classDef.getClientEnvironment() == ERuntimeEnvironmentType.WEB) {
            this.setClientEnvironment(classDef.getClientEnvironment());
        }
        return this.getInheritance().setSuperClass(classDef);
    }

    @Override
    public DdsTableDef findTable(RadixObject context) {
        AdsEntityClassDef root = findRootBasis();
        return root == null ? null : root.findTable(context);
    }

    @Override
    public AdsEntityObjectClassDef findBasis() {
        AdsClassDef superclass = getInheritance().findSuperClass().get();
        if (superclass instanceof AdsEntityObjectClassDef) {
            return (AdsEntityObjectClassDef) superclass;
        } else {
            return null;
        }
    }
    public static final Id TEST_CASE_CLASS_ID = Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA");

    @Override
    public RadixIcon getIcon() {
        if (Utils.equals(TEST_CASE_CLASS_ID, getEntityId())) {
            return AdsDefinitionIcon.CLASS_TESTCASE;
        }

        return AdsDefinitionIcon.CLASS_APPLICATION;
    }

    @Override
    public boolean canChangeClientEnvironment() {
        AdsEntityObjectClassDef superClass = findBasis();
        if (superClass != null) {
            return (superClass.getClientEnvironment() != getClientEnvironment())
                    || getClientEnvironment() != ERuntimeEnvironmentType.EXPLORER && getClientEnvironment() != ERuntimeEnvironmentType.WEB;
        }
        return true;
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsClassDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new EntityObjectClassRadixdoc(getSource(), page, options);
            }
        };
    }
}
