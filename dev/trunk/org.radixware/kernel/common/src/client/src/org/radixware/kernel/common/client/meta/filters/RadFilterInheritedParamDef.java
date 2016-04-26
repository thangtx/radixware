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

package org.radixware.kernel.common.client.meta.filters;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameterPersistentValue;
import org.radixware.kernel.common.client.views.IPropEditorDialog;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


class RadFilterInheritedParamDef extends RadFilterParamDef implements ISqmlParameter {

    private final RadFilterParamDef baseParam;
    private boolean wasModified;
    private ISqmlParameterPersistentValue persistentValue;

    public RadFilterInheritedParamDef(final RadFilterParamDef baseFilterParamDef) {
        super(
                baseFilterParamDef.getId(),
                baseFilterParamDef.getName(), //собственное имя
                null, //если перекрыт заголовок - идентификатор строки
                baseFilterParamDef.getOwnerClassId(), //идентификатор класса (aec), в котором определен фильтр
                null,//tableId
                baseFilterParamDef.getBasePropertyId(),
                baseFilterParamDef.getType(),
                null,//constSetId
                baseFilterParamDef.getInitialVal(),
                //Editing
                baseFilterParamDef.isMandatory(),
                baseFilterParamDef.storeHistory(),
                baseFilterParamDef.customDialog(),
                null,//customDialogId
                baseFilterParamDef.isCustomEditOnly(),
                null,//editMask
                null,//null string id
                baseFilterParamDef.isMemo(),
                //Parent selector
                baseFilterParamDef.getParentSelectorPresentationClassId(),
                baseFilterParamDef.getParentSelectorPresentationId());
        baseParam = baseFilterParamDef;
    }

    public RadFilterInheritedParamDef copy() {
        final RadFilterInheritedParamDef result = new RadFilterInheritedParamDef(baseParam);
        if (persistentValue != null) {
            result.persistentValue = persistentValue.copy();
        }
        return result;
    }

    @Override
    public boolean isPredefined() {
        return true;
    }

    @Override
    protected RadPropertyDef getTargetProperty() {
        return baseParam.getTargetProperty();
    }

    @Override
    public String getTitle() {
        return baseParam.getTitle();
    }

    @Override
    public boolean hasTitle() {
        return baseParam.hasTitle();
    }

    @Override
    public RadEnumPresentationDef getConstSet() {
        return baseParam.getConstSet();
    }

    @Override
    public IPropEditorDialog getPropEditorDialog(IClientEnvironment env) {
        return baseParam.getPropEditorDialog(env);
    }

    @Override
    public EditMask getEditMask() {
        return baseParam.getEditMask();
    }

    @Override
    public EEditPossibility getEditPossibility() {
        return EEditPossibility.ALWAYS;
    }

    @Override
    public String getNullString() {
        return baseParam.getNullString();
    }

    @Override
    public boolean customDialog() {
        return baseParam.customDialog();
    }

    @Override
    public boolean isCustomEditOnly() {
        return baseParam.isCustomEditOnly();
    }

    @Override
    public String getHint() {
        return baseParam.getHint();
    }

    @Override
    public Id getReferencedTableId() {
        return baseParam.getReferencedTableId();
    }

    @Override
    public RadSelectorPresentationDef getParentSelectorPresentation() {
        return baseParam.getParentSelectorPresentation();
    }

    @Override
    public String getDescription() {
        return baseParam.getDescription();
    }

    @Override
    public Id getEnumId() {
        return baseParam.getEnumId();
    }

    @Override
    public Id getParentSelectorPresentationClassId() {
        return baseParam.getParentSelectorPresentationClassId();
    }

    @Override
    public Id getParentSelectorPresentationId() {
        return baseParam.getParentSelectorPresentationId();
    }

    @Override
    public boolean canHavePersistentValue() {
        return true;
    }

    @Override
    public void setPersistentValue(final ISqmlParameterPersistentValue value) {
        if (!Utils.equals(value, persistentValue)) {
            updateChangeValueTimestamp();
            wasModified = true;
        }
        persistentValue = value;
    }

    @Override
    public ISqmlParameterPersistentValue getPersistentValue() {
        return persistentValue;
    }

    public boolean wasModified() {
        return wasModified;
    }

    @Override
    public boolean isDeprecated() {
        return baseParam.isDeprecated();
    }
}
