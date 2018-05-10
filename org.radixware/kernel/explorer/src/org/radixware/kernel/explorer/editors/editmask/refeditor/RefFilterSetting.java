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
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.filters.RadFilterDef;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.widgets.ListWidgetItem;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.valeditors.ValListItemEditor;
import org.radixware.schemas.editmask.EditMask;

public class RefFilterSetting extends ValListItemEditor implements IEditMaskEditorSetting {

    private final IClientEnvironment env;
    public final Signal1<RadFilterDef> filterChanged = new Signal1<>();

    public RefFilterSetting(IClientEnvironment env, QWidget parent) {
        super(env, parent, false, false);
        this.env = env;
        setupUI();
    }

    private void setupUI() {
        this.valueChanged.connect(this, "onFilterChanged(Object)");
    }

    @Override
    public void addToXml(EditMask editMask) {
        if (getValue() == null || getValue().isEmpty()) {
            editMask.getReference().setDefaultFilterId(null);
            editMask.getReference().setDefaultFilterDefined(false);
        } else {
            editMask.getReference().setDefaultFilterId(((RadFilterDef) getValue().get(0).getValue()).getId());
            editMask.getReference().setDefaultFilterDefined(true);
        }
    }

    @Override
    public void loadFromXml(EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskRef emr = editMask.getReference();
        Id selectorId = emr.getSelectorPresentationId();
        RadSelectorPresentationDef radSelectorPresentationDef = null;
        try {
            radSelectorPresentationDef = env.getDefManager().getSelectorPresentationDef(selectorId);
        } catch (Exception ex) {
            //do nothing
        }
        if (emr.getDefaultFilterDefined()) {
            Id defaultFilterId = emr.getDefaultFilterId();
            if (radSelectorPresentationDef != null) {
                try {
                    RadFilterDef currentFilter = radSelectorPresentationDef.getClassPresentation().getFilterDefById(defaultFilterId);
                    if (currentFilter == null) {
                        setValue(null);
                    } else {
                        ListWidgetItemExtended item = new ListWidgetItemExtended();
                        item.setText(currentFilter.getTitle());
                        item.setValue(currentFilter);
                        item.setUserData(defaultFilterId);
                        RadClassPresentationDef ownerClassDef = env.getDefManager().getClassPresentationDef(currentFilter.getOwnerClassId());
                        if (ownerClassDef != null) {
                            item.setExtendedTitle(ownerClassDef.getName());
                        }
                        List<ListWidgetItem> list = new LinkedList<>();
                        list.add(item);
                        setValue(list);
                    }
                } catch (Exception ex) {
                    ListWidgetItemExtended item = createErrorWidgetItem(defaultFilterId);
                    List<ListWidgetItem> list = new LinkedList<>();
                    addItem(item);
                    list.add(item);
                    setValue(list);
                }
            } else {
                ListWidgetItemExtended item = createErrorWidgetItem(defaultFilterId);
                List<ListWidgetItem> list = new LinkedList<>();
                addItem(item);
                list.add(item);
                setValue(list);
            }
        } else {
            setValue(null);
        }
    }

    boolean check() {
        List<ListWidgetItem> list = getValue();
        if (list == null || list.isEmpty()) {
            return true;
        }
        for (ListWidgetItem item : list) {
            if (item.getValue() == null) {
                env.messageError("Editor Presentation Error", "No filter with id \"" + item.getText() + "\" found");
                return false;
            }
        }
        return true;
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.REF_FILTER_ID;
    }

    @Override
    public void setDefaultValue() {
    }

    @SuppressWarnings("Unused")
    private void onFilterChanged(Object newFilter) {
        RadFilterDef radFilterDef = null;
        List<ListWidgetItem> list = getValue();
        if (list != null && !list.isEmpty()) {
            radFilterDef = (RadFilterDef) list.get(0).getValue();
        }
        filterChanged.emit(radFilterDef);
    }

    @Override
    protected ValidationResult validateListItem(ListWidgetItem item) {
        if (item.getValue() == null) {
            return ValidationResult.Factory.newInvalidResult("No filter with id \"" + item.getText() + "\" found");
        } else {
            return ValidationResult.ACCEPTABLE;
        }
    }

    private ListWidgetItemExtended createErrorWidgetItem(Id id) {
        if (id != null) {
            ListWidgetItemExtended item = new ListWidgetItemExtended();
            item.setText(id.toString());
            item.setValue(null);
            item.setUserData(id);
            item.setValidationResult(ValidationResult.Factory.newInvalidResult("No filter with id \"" + item.getText() + "\" found"));
            return item;
        } else {
            return null;
        }
    }
}
