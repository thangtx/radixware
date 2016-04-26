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

package org.radixware.kernel.common.defs.ads.clazz.members;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsPropertyGroup;
import org.radixware.kernel.common.defs.ads.type.*;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EDdsTableExtOption;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;
import org.radixware.schemas.adsdef.PropertyDefinition;


public class AdsUserPropertyDef extends AdsTablePropertyDef {

    public static final class Factory {

        public static AdsUserPropertyDef newInstance() {
            return new AdsUserPropertyDef("newUserProperty");
        }

        public static AdsUserPropertyDef newTemporaryInstance(AdsPropertyGroup context) {
            AdsUserPropertyDef prop = newInstance();
            prop.setContainer(context);
            return prop;
        }
    }
    private boolean auditUpdate;
    private ParentDeletionOptions parentDeletionOptions = null;

    AdsUserPropertyDef(AbstractPropertyDefinition xProp) {
        super(xProp);
        if (xProp instanceof PropertyDefinition) {
            final PropertyDefinition xPropDef = (PropertyDefinition) xProp;
            auditUpdate = xPropDef.getAuditUpdate();
            if (xPropDef.isSetParentDeletionOptions()) {
                parentDeletionOptions = new ParentDeletionOptions(xPropDef.getParentDeletionOptions().getOnParentDeletion(), xPropDef.getParentDeletionOptions().getConfirmationRequired());
            }
        }
    }

    AdsUserPropertyDef(String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_USER_PROP), name);
    }

    AdsUserPropertyDef(AdsUserPropertyDef source, boolean forOverride) {
        super(source, forOverride);
    }

    @Override
    protected AdsPropertyDef createOvr(boolean forOverride) {
        return new AdsUserPropertyDef(this, forOverride);
    }

    public boolean isUserConstraintsEditable() {
        final AdsUserPropertyDef constraintsSource = getUserConstraintsSource();
        if (constraintsSource != this) {
            return false;
        }
        final IAdsTypedObject typedObject = getTypedObject();
        if (typedObject != null) {
            final AdsTypeDeclaration typeDecl = typedObject.getType();
            if (typeDecl != null) {
                final AdsType type = typeDecl.resolve(this).get();
                final DdsTableDef tableDef;
                if (type instanceof ParentRefType) {
                    tableDef = ((ParentRefType) type).getSource().findTable(this);
                } else if (type instanceof ArrRefType) {
                    tableDef = ((ArrRefType) type).getSource().findTable(this);
                } else {
                    tableDef = null;
                }
                return tableDef != null && tableDef.getExtOptions().contains(EDdsTableExtOption.CHECK_USER_CONSTRAINTS_ON_DELETION);
            }
        }
        return false;
    }

    public boolean isUserConstraintsAvailable() {
        final AdsUserPropertyDef constraintsSource = getUserConstraintsSource();
        if (constraintsSource != null) {
            return constraintsSource.isUserConstraintsEditable();
        }
        return false;
    }

    @Override
    protected void setContainer(RadixObject container) {
        super.setContainer(container);       
    }

    private AdsUserPropertyDef getUserConstraintsSource() {
        final AdsUserPropertyDef overwritten = (AdsUserPropertyDef) getHierarchy().findOverwritten().get();
        if (overwritten != null) {
            return overwritten.getUserConstraintsSource();
        }
        final AdsUserPropertyDef overriden = (AdsUserPropertyDef) getHierarchy().findOverridden().get();
        if (overriden != null) {
            return overriden;
        }
        return this;
    }

    @Override
    public void appendTo(PropertyDefinition xDef, ESaveMode saveMode) {
        if (auditUpdate) {
            xDef.setAuditUpdate(true);
        }

        if (saveMode == ESaveMode.NORMAL && parentDeletionOptions != null) {
            final PropertyDefinition.ParentDeletionOptions xOpts = xDef.addNewParentDeletionOptions();
            xOpts.setConfirmationRequired(parentDeletionOptions.isConfirmationRequired());
            xOpts.setOnParentDeletion(parentDeletionOptions.getDeleteMode());
        }
        super.appendTo(xDef, saveMode);
    }

    @Override
    public EPropNature getNature() {
        return EPropNature.USER;
    }

    public boolean isAuditUpdate() {
        return auditUpdate;
    }

    public void setAuditUpdate(boolean auditUpdate) {
        this.auditUpdate = auditUpdate;
        setEditState(EEditState.MODIFIED);
    }

    public ParentDeletionOptions getParentDeletionOptions() {
        final AdsUserPropertyDef optionsSource = getUserConstraintsSource();
        if (optionsSource == this) {
            return parentDeletionOptions;
        } else {
            return optionsSource.getParentDeletionOptions();
        }
    }

    public void setParentDeletionOptions(ParentDeletionOptions options) {
        if (!isUserConstraintsEditable()) {
            throw new IllegalStateException("User constraints are not editable in this object");
        }
        this.parentDeletionOptions = options;
        setEditState(EEditState.MODIFIED);
    }
}
