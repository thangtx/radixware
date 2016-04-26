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

package org.radixware.kernel.common.defs.ads.clazz.presentation;

import java.util.Collection;
import org.radixware.kernel.common.defs.ads.AdsDefinition;

import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef.MultilingualStringInfo;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EPropertyVisibility;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;


public final class AdsPropertyPresentationAttributes extends PropertyUsage implements AdsEditorPresentationDef.IPropertyPresentationAttributesView, ILocalizedDef {
    private Id titleId;
    private EPropertyVisibility visibility;
    private EEditPossibility editPossibility;
    private Boolean presentable;
    private Boolean mandatory;

    public AdsPropertyPresentationAttributes(final Id propId) {
        this(propId, null, null, null, null, null);
    }

    public AdsPropertyPresentationAttributes(final Id propId, final Boolean presentable, final EEditPossibility editPossibility, final EPropertyVisibility visibility, final Boolean mandatory, final Id titleId) {
        super(propId);
        this.presentable = presentable;
        this.editPossibility = editPossibility;
        this.visibility = visibility;
        this.mandatory = mandatory;
        this.titleId = titleId;
    }

    @Override
    public EEditPossibility getEditPossibility() {
        return editPossibility;
    }

    @Override
    public Boolean getMandatory() {
        return mandatory;
    }

    @Override
    public Boolean getPresentable() {
        return presentable;
    }

    @Override
    public EPropertyVisibility getVisibility() {
        return visibility;
    }

    @Override
    public Id getTitleId() {
        return titleId;
    }

    public void setTitleId(Id titleId) {
        if (this.titleId != titleId) {
            this.titleId = titleId;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void setVisibility(EPropertyVisibility visibility) {
        if (this.visibility != visibility) {
            this.visibility = visibility;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void setEditPossibility(EEditPossibility editPossibility) {
        if (this.editPossibility != editPossibility) {
            this.editPossibility = editPossibility;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void setPresentable(Boolean presentable) {
        if (this.presentable != presentable) {
            this.presentable = presentable;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void setMandatory(Boolean mandatory) {
        if (this.mandatory != mandatory) {
            this.mandatory = mandatory;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PropertiyId").append(String.valueOf(getPropertyId())).append("\n");
        sb.append("Presentable: ").append(String.valueOf(getPresentable())).append("\n");
        sb.append("Mandatory: ").append(String.valueOf(getMandatory())).append("\n");
        sb.append("Visibility: ").append(String.valueOf(getVisibility())).append("\n");
        sb.append("EditPossibility: ").append(String.valueOf(getEditPossibility())).append("\n");
        sb.append("TitleId: ").append(String.valueOf(getTitleId())).append("\n");
        return sb.toString();
    }

    @Override
    public AdsClassDef getOwnerClass() {
        return RadixObjectsUtils.findContainer(this, AdsClassDef.class);
    }

    public boolean isEmpty() {
        return presentable == null && editPossibility == null && visibility == null && mandatory == null && titleId == null;
    }

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        if (titleId != null) {
            ids.add(new MultilingualStringInfo(this) {
                @Override
                public String getContextDescription() {
                    return "Property Presentation Title";
                }

                @Override
                public Id getId() {
                    return AdsPropertyPresentationAttributes.this.getTitleId();
                }

                @Override
                public EAccess getAccess() {
                    return EAccess.PUBLIC;
                }

                @Override
                public void updateId(Id newId) {
                    AdsPropertyPresentationAttributes.this.setTitleId(titleId);
                }

                @Override
                public boolean isPublished() {
                    return true;
                }
            });
        }
    }

    @Override
    public AdsMultilingualStringDef findLocalizedString(Id stringId) {
        if (stringId == null) {
            return null;
        }
        final AdsDefinition ownDef = RadixObjectsUtils.findContainer(this, AdsDefinition.class);
        if (ownDef != null) {
            return ownDef.findLocalizedString(getTitleId());
        }
        return null;
    }

    public AdsMultilingualStringDef findTitle() {
        return findLocalizedString(titleId);
    }
}
