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

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionLink;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.types.Id;

import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.ClassDefinition.Entity;


public abstract class AdsEntityBasedClassDef extends AdsClassDef {

    protected Id entityId;

    protected AdsEntityBasedClassDef(ClassDefinition xDef) {
        super(xDef);
        ClassDefinition.Entity e = xDef.getEntity();
        if (e != null) {
            this.entityId = e.getEntityId();
        } else {
            this.entityId = null;
        }
    }

    protected AdsEntityBasedClassDef(Id id, String name) {
        super(id, name);
        this.entityId = null;
    }

    protected AdsEntityBasedClassDef(AdsEntityBasedClassDef source) {
        super(source);
    }

    /**
     * Returns identifier of table associated with class
     */
    public Id getEntityId() {
        return entityId;
    }

    @Override
    public void appendTo(ClassDefinition xClass, ESaveMode saveMode) {
        super.appendTo(xClass, saveMode);
        Entity xDef = xClass.addNewEntity();
        appendTo(xClass, xDef, saveMode);
    }

    protected void appendTo(ClassDefinition xClass, Entity xDef, ESaveMode saveMode) {
        if (entityId != null) {
            xDef.setEntityId(entityId);
        }
    }

    /**
     * Returns reference to table the class is based on
     */
    public DdsTableDef findTable(RadixObject context) {
        AdsEntityClassDef root = findRootBasis();
        return root == null ? null : root.findTable(context);
    }

    /**
     * Return basis class for the class
     * <ul>
     * <li>For entity class returns null</li>
     * <li>For application class returns its superclass</li>
     * <li>For group class returns entity class</li>
     * <li>For presentation adapter class returns adopted class</li>
     * </ul>
     */
    public abstract AdsEntityObjectClassDef findBasis();

    private class RootBasisLink extends DefinitionLink<AdsEntityClassDef> {

        @Override
        protected AdsEntityClassDef search() {
            AdsEntityObjectClassDef basis = findBasis();
            if (basis == null) {
                if (getClassDefType() == EClassType.ENTITY) {
                    return (AdsEntityClassDef) AdsEntityBasedClassDef.this;
                } else {
                    return null;
                }
            } else {
                return basis.findRootBasis();
            }
        }
    }
    private final RootBasisLink rootBasisLink = new RootBasisLink();

    /**
     * Returns root class for classes based on entity
     */
    public final AdsEntityClassDef findRootBasis() {
        return rootBasisLink.find();
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        DdsTableDef table = findTable(this);
        if (table != null) {
            list.add(table);
        }
        AdsEntityObjectClassDef basis = findBasis();
        if (basis != null && basis != this) {
            list.add(basis);
        }
        AdsEntityClassDef root = findRootBasis();
        if (root != null && root != this) {
            list.add(root);
        }
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        return ERuntimeEnvironmentType.SERVER;
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return EnumSet.of(ERuntimeEnvironmentType.SERVER);
    }

    @Override
    public boolean isSaveable() {
        return true;
    }
}
