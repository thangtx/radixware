/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.explorer.editors.editmask.refeditor;

import com.trolltech.qt.gui.QWidget;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.widgets.ListWidgetItem;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.valeditors.ValListItemEditor;
import org.radixware.kernel.explorer.models.SqmlTreeModel;
import org.radixware.schemas.editmask.EditMask;

final class RefSelectorPresentationIdSetting extends ValListItemEditor implements IEditMaskEditorSetting {

    IClientEnvironment environment;
    public final Signal1<RadSelectorPresentationDef> selectorPresentationChanged = new Signal1<>();

    public RefSelectorPresentationIdSetting(IClientEnvironment environment, QWidget parent, SqmlTreeModel model) {
        super(environment, parent, false, false);
        this.environment = environment;
        this.valueChanged.connect(this, "onSelectorChanged(Object)");
    }

    @Override
    public void addToXml(EditMask editMask) {
        List<ListWidgetItem> list = getValue();
        if (list != null && !list.isEmpty()) {
            List<Id> idList = new LinkedList<>();
            for (ListWidgetItem item : list) {
                idList.add((Id) item.getUserData());
            }
            editMask.getReference().setSelectorPresentationId(idList.get(0));
        }
    }

    @Override
    public void loadFromXml(EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskRef emr = editMask.getReference();
        Id selectorId = editMask.getReference().getSelectorPresentationId();
        ListWidgetItemExtended item = createListWidgetItemFromId(selectorId);
        if (item == null) {
            setValue(null);
        } else {
            List<ListWidgetItem> list = new LinkedList<>();
            list.add(item);
            setValue(list);
        }
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.REF_SELECTOR_PRESENTATION_ID;
    }

    @Override
    public void setDefaultValue() {
    }

    ListWidgetItemExtended createListWidgetItemFromId(Id id) {
        if (id != null) {
            ListWidgetItemExtended item = new ListWidgetItemExtended();
            try {
                RadSelectorPresentationDef radSelectorPresentationDef = environment.getDefManager().getSelectorPresentationDef(id);
                item.setText(radSelectorPresentationDef.getName());
                item.setValue(radSelectorPresentationDef);
                item.setExtendedTitle(radSelectorPresentationDef.getClassPresentation().getName());
            } catch (Exception ex) { //DefinitionNotFoundError
                item.setText(id.toString());
                item.setValidationResult(ValidationResult.Factory.newInvalidResult("No adc presentation with id \"" + item.getText() + "\" found"));
                addItem(item);
            }
            item.setUserData(id);
            return item;
        } else {
            return null;
        }
    }

    @SuppressWarnings("Unused")
    private void onSelectorChanged(Object newSelector) {
        RadSelectorPresentationDef radSelectorPresentationDef = null;
        List<ListWidgetItem> list = getValue();
        if (list != null && !list.isEmpty()) {
            radSelectorPresentationDef = (RadSelectorPresentationDef) list.get(0).getValue();
        }
        selectorPresentationChanged.emit(radSelectorPresentationDef);
    }
}
