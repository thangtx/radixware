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
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.widgets.ListWidgetItem;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.valeditors.ValListItemEditor;
import org.radixware.schemas.editmask.EditMask;
import org.radixware.schemas.types.MapStrStr;

public class RefSortingSetting extends ValListItemEditor implements IEditMaskEditorSetting {

    private Id filterId = null;
    private final IClientEnvironment env;

    public RefSortingSetting(IClientEnvironment env, QWidget parent) {
        super(env, parent, false, false);
        this.env = env;
    }

    @Override
    public void addToXml(EditMask editMask) {
        MapStrStr mss = MapStrStr.Factory.newInstance();
        MapStrStr.Entry entry = mss.addNewEntry();
        List<ListWidgetItem> list = getValue();
        if (list != null && !list.isEmpty()) {
            if (filterId != null) {
                entry.setKey(filterId.toString());
            }
            entry.setValue(((RadSortingDef) list.get(0).getValue()).getId().toString());
            editMask.getReference().setDefaultSortingIdByFilterId(mss);
        }
    }

    @Override
    public void loadFromXml(EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskRef emr = editMask.getReference();
        Id selectorId = emr.getSelectorPresentationId();
        if (selectorId != null) {
            filterId = emr.getDefaultFilterId();
            if (emr.isSetDefaultSortingIdByFilterId()) {
                for (MapStrStr.Entry entry : emr.getDefaultSortingIdByFilterId().getEntryList()) {
                    if ((entry.getKey() == null || entry.getKey().isEmpty()) && filterId == null || filterId != null && filterId.toString().equals(entry.getKey())) {
                        Id sortingId = Id.Factory.loadFrom(entry.getValue());
                        try {
                            RadSelectorPresentationDef radSelectorPresentationDef = env.getDefManager().getSelectorPresentationDef(selectorId);
                            RadSortingDef currentSorting = radSelectorPresentationDef.getClassPresentation().getSortingDefById(sortingId);
                            if (currentSorting == null) {
                                setValue(null);
                            } else {
                                ListWidgetItemExtended item = new ListWidgetItemExtended();
                                item.setText(currentSorting.getTitle());
                                item.setValue(currentSorting);
                                RadClassPresentationDef ownerClassDef = env.getDefManager().getClassPresentationDef(currentSorting.getOwnerClassId());
                                item.setExtendedTitle(ownerClassDef.getName());
                                List<ListWidgetItem> list = new LinkedList<>();
                                list.add(item);
                                setValue(list);
                            }
                        } catch (Exception ex) {
                            ListWidgetItemExtended item = new ListWidgetItemExtended();
                            item.setText(sortingId.toString());
                            item.setValue(null);
                            item.setUserData(sortingId);
                            item.setValidationResult(ValidationResult.Factory.newInvalidResult("No sorting with id \"" + item.getText() + "\" found"));
                            List<ListWidgetItem> list = new LinkedList<>();
                            addItem(item);
                            list.add(item);
                            setValue(list);
                        }
                    }
                }
            }
        } else {
            setValue(null);
        }
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.REF_SORTING_ID;
    }

    @Override
    public void setDefaultValue() {
    }

    void setFilterId(Id filterId) {
        this.filterId = filterId;
    }

    boolean check() {
        List<ListWidgetItem> list = getValue();
        if (list == null || list.isEmpty()) {
            return true;
        }
        for (ListWidgetItem item : list) {
            if (item.getValue() == null) {
                env.messageError("Editor Presentation Error", "No sorting with id \"" + item.getText() + "\" found");
                return false;
            }
        }
        return true;
    }
}
