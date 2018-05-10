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
package org.radixware.kernel.common.defs.dds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDocGroup;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EMultilingualStringKind;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsLabelDef;
import org.radixware.kernel.common.defs.dds.DdsExtTableDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;

/**
 * Базовый класс для всех DDS дефиниций. Реализует идентификатор дефиниции.
 * Реализует подсчеты по умолчанию QualifiedName, Hint, пустые Description и
 * Name.
 *
 */
public abstract class DdsDefinition extends Definition implements ILocalizedDescribable, ILocalizedDef {

    protected byte initStatus;
    protected static final byte INIT_STATUS_RESERVERD_MAX = 0x10;
    private Id descriptionId;

    /**
     * For overwrite
     */
    protected DdsDefinition(final Id id, final String name) {
        super(id, name);
    }

    protected DdsDefinition(final EDefinitionIdPrefix idPrefix, final String name) {
        super(Id.Factory.newInstance(idPrefix), name);
    }

    protected DdsDefinition(org.radixware.schemas.commondef.Definition xDefinition) {
        super(xDefinition.getId());
    }

    protected DdsDefinition(org.radixware.schemas.commondef.NamedDefinition xNamedDefinition) {
        super(xNamedDefinition.getId(), xNamedDefinition.getName());
    }

    protected DdsDefinition(org.radixware.schemas.commondef.DescribedDefinition xDescribedDefinition) {
        super(xDescribedDefinition.getId(), xDescribedDefinition.getName(), xDescribedDefinition.getDescription());
        this.descriptionId = xDescribedDefinition.getDescriptionId();
    }

    @Override
    public String toString() {
        return "DdsDefinition{" + "initStatus=" + initStatus + ", descriptionId=" + descriptionId + "}: " + super.toString();
    }

    @Override
    public boolean needsDocumentation() {
        return false;
    }

    /**
     * *
     * Get owner model of the DDS definition.
     *
     * @return owner model or null if object is model of object is in clipboard
     * (for example).
     */
    public DdsModelDef getOwnerModel() {
        for (RadixObject owner = this.getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof DdsModelDef) {
                return (DdsModelDef) owner;
            }
        }
        return null;
    }

    /**
     * Get owner module of the definition.
     *
     * @return owner module or null if definition is in clipboard (for example).
     */
    @Override
    public final DdsModule getModule() {
        return (DdsModule) super.getModule();
    }

    /**
     * Returns id sequence containing all container ids from top level
     * definition to current
     */
    @Override
    public final Id[] getIdPath() {
        final List<Id> path = new ArrayList<>(2);
        Definition def = this;
        while (def != null) {
            path.add(def.getId());
            def = def.getOwnerDefinition();
            if (def instanceof DdsModelDef || def instanceof Module) {
                break;
            }
        }
        final Id[] pathArr = new Id[path.size()];
        for (int i = path.size() - 1, j = 0; i >= 0; i--, j++) {
            pathArr[j] = path.get(i);
        }
        return pathArr;
    }

    @Override
    public Id getDescriptionId() {
        return descriptionId;
    }

    @Override
    public void setDescriptionId(Id id) {
        if (id != descriptionId) {
            descriptionId = id;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public String getDescription(EIsoLanguage language) {
        if (descriptionId != null) {
            final IMultilingualStringDef string = findLocalizedString(getDescriptionId());
            return string == null ? null : string.getValue(language);
        } else {
            return null;
        }
    }

    @Override
    public String getLocalizedStringValue(EIsoLanguage language, Id stringId) {
        if (stringId == null) {
            return null;
        }
        ILocalizingBundleDef bundle = findExistingLocalizingBundle();
        if (bundle != null) {
            IMultilingualStringDef string = (IMultilingualStringDef) bundle.getStrings().findById(stringId, ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE).get();
            if (string != null) {
                return string.getValue(language);
            }
        }

        return null;
    }

    @Override
    public boolean setDescription(EIsoLanguage language, String description) {
        if (descriptionId != null) {
            final IMultilingualStringDef string = findLocalizedString(descriptionId);
            if (string != null) {
                string.setValue(language, description);
            }
            return true;
        }
        return false;
    }

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        ids.add(new MultilingualStringInfo(this) {
            @Override
            public String getContextDescription() {
                return "Desctription of " + getTypeTitle() + " " + getQualifiedName();
            }

            @Override
            public Id getId() {
                return descriptionId;
            }

            @Override
            public EAccess getAccess() {
                return EAccess.PUBLIC;
            }

            @Override
            public void updateId(Id newId) {
                descriptionId = newId;
            }

            @Override
            public boolean isPublished() {
                return true;
            }

            @Override
            public EMultilingualStringKind getKind() {
                return EMultilingualStringKind.DESCRIPTION;
            }
        });
    }

    @Override
    public ILocalizingBundleDef findLocalizingBundle() {
        DdsModelDef model = getOwnerModel();
        if (model != null) {
            return model.getStringBundle();
        }
        return null;
    }

    @Override
    public ILocalizingBundleDef findExistingLocalizingBundle() {
        return findLocalizingBundle();
    }

    @Override
    public EDocGroup getDocGroup() {
        return EDocGroup.NONE;
    }

    @Override
    public ERuntimeEnvironmentType getDocEnvironment() {
        return ERuntimeEnvironmentType.SERVER;
    }

    /**
     * Является дефиниция ребенком модуля
     */
    @Override
    public boolean isTopLevelDefinition() {
        final Definition ownerDefinition = getOwnerDefinition();
        if (ownerDefinition == null) {
            //throw new RadixObjectError("The level of definition can not be determined: " + this.getClass().getName());
            return false;
        }
        return ownerDefinition instanceof DdsModelDef;
    }

    @Override
    public boolean showApiBrowser() {
        return !(this instanceof DdsLabelDef
                || this instanceof DdsSequenceDef
                || this instanceof DdsReferenceDef
                || this instanceof DdsAccessPartitionFamilyDef
                || this instanceof DdsExtTableDef);
    }
}
