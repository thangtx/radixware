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

package org.radixware.kernel.server.meta.presentations;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.types.Restrictions;

public abstract class RadSelectorExplorerItemDef extends RadExplorerItemDef {

    private final Id selectorPresentationId;
    private final RadConditionDef condition;
    private final Restrictions restrictions;
    private final Id classCatalogId;
    private final Id classId;
    private RadClassDef selClasDef = null;
    RadClassPresentationDef.ClassCatalog classCat = null;
    private RadSelectorPresentationDef selPres = null;

    public RadSelectorExplorerItemDef(
            final Id id,
            final Id ownerDefId,
            final Id classId,
            final Id selectorPresentationId,
            final RadConditionDef condition,
            final Restrictions restrictions,
            final Id classCatalogId // if null - inherited from selector presentation
            ) {
        super(id, ownerDefId);
        this.classId = classId;
        this.restrictions = restrictions != null ? restrictions : Restrictions.ZERO;
        this.condition = condition;
        this.selectorPresentationId = selectorPresentationId;
        this.classCatalogId = classCatalogId;
    }

    @Override
    public void link() {
        super.link();
        getSelectionClassDef();
        getSelectorPresentation();
        getClassCatalog();
        getClassCatalogId();
    }

    public RadClassDef getSelectionClassDef() {
        if (selClasDef == null) {
            selClasDef = getRelease().getClassDef(classId);
        }
        return selClasDef;
    }

    public final RadSelectorPresentationDef getSelectorPresentation() {
        if (selPres == null) {
            selPres = getSelectionClassDef().getPresentation().getSelectorPresentationById(getSelectorPresentationId());
        }
        return selPres;
    }

    public final RadClassPresentationDef.ClassCatalog getClassCatalog() {
        if (classCat == null && getClassCatalogId() != null) {
            classCat = getSelectionClassDef().getPresentation().getClassCatalogById(getClassCatalogId());
        }
        return classCat;
    }

    @Override
    public final boolean isCommandEnabled(final Id cmdId) {
        return !restrictions.getIsCommandRestricted(cmdId);
    }

    public Id getClassCatalogId() {
        if (classCatalogId == null) {
            return getSelectorPresentation().getClassCatalogId();
        }
        return classCatalogId;
    }

    /**
     * @return the selectorPresentationId
     */
    public Id getSelectorPresentationId() {
        return selectorPresentationId;
    }

    /**
     * @return the condition
     */
    public RadConditionDef getCondition() {
        return condition;
    }

    /**
     * @return the restrictions
     */
    @Override
    public Restrictions getRestrictions() {
        return restrictions;
    }
}
