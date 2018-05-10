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
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.widgets.IListWidget;
import org.radixware.kernel.common.client.widgets.ListWidgetItem;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.valeditors.ValListItemEditor;
import org.radixware.kernel.explorer.widgets.propeditors.IDisplayStringProvider;
import org.radixware.schemas.editmask.EditMask;

public class RefEditorPresentationIdsSetting extends ValListItemEditor implements IEditMaskEditorSetting {

    private final IClientEnvironment env;

    public RefEditorPresentationIdsSetting(IClientEnvironment env, QWidget parent) {
        super(env, parent, false, false);
        this.env = env;
        this.setFeatures(EnumSet.of(IListWidget.EFeatures.FILTERING, IListWidget.EFeatures.MULTI_SELECT, IListWidget.EFeatures.SELECTION_LABEL));
        setDisplayStringProvider(new IDisplayStringProvider() {

            @Override
            public String format(org.radixware.kernel.common.client.meta.mask.EditMask mask, Object value, String defaultDisplayString) {
                List<ListWidgetItem> itemsList = RefEditorPresentationIdsSetting.this.getValue();
                MessageProvider mp = RefEditorPresentationIdsSetting.this.env.getMessageProvider();
                if (itemsList == null || itemsList.isEmpty()) {
                    return mp.translate("EditMaskRef", "No editor presentations were selected");
                } else {
                    return mp.translate("EditMaskRef", "Selected editor presentations: ") + itemsList.size();
                }
            }
        });
    }

    @Override
    public void addToXml(EditMask editMask) {
        List<ListWidgetItem> list = getValue();
        if (list != null && !list.isEmpty()) {
            List<Id> idList = new LinkedList<>();
            for (ListWidgetItem item : list) {
                idList.add((Id) item.getUserData());
            }
            editMask.getReference().setEditorPresentationIds(idList);
        }
    }

    @Override
    public void loadFromXml(EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskRef emr = editMask.getReference();
        if (emr.getEditorPresentationIds() != null && !emr.getEditorPresentationIds().isEmpty()) {
            List<Id> editorPresentationsIdList = emr.getEditorPresentationIds();
            List<ListWidgetItem> itemsList = new LinkedList<>();
            for (Id id : editorPresentationsIdList) {
                itemsList.add(createListWidgetItemFromId(id));
            }
            setValue(itemsList);
        } else {
            setValue(null);
        }
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.REF_EDITOR_PRESENTATION_ID;
    }

    @Override
    public void setDefaultValue() {
    }

    boolean check() {
        List<ListWidgetItem> list = getValue();
        if (list == null || list.isEmpty()) {
            return true;
        }
        for (ListWidgetItem item : list) {
            if (item.getValue() == null) {
                env.messageError("Editor Presentation Error", "No adc presentation with id \"" + item.getText() + "\" found");
                return false;
            }
        }
        return true;
    }

    @Override
    protected ValidationResult validateListItem(ListWidgetItem item) {
            if (item.getValue() == null) {
                return ValidationResult.Factory.newInvalidResult("No adc presentation with id \"" + item.getText() + "\" found");
            } else {
                return ValidationResult.ACCEPTABLE;
            }
    }
    
    

    ListWidgetItem createListWidgetItemFromId(Id id) { 
        ListWidgetItemExtended item = new ListWidgetItemExtended();
        try {
            RadEditorPresentationDef radEditorPresentationDef = env.getDefManager().getEditorPresentationDef(id);
            item.setText(radEditorPresentationDef.getName());
            item.setValue(radEditorPresentationDef);
            item.setExtendedTitle(radEditorPresentationDef.getClassPresentation().getName());
        } catch (Exception ex) { //DefinitionNotFoundError
            item.setText(id.toString());
            item.setValidationResult(ValidationResult.Factory.newInvalidResult("No adc presentation with id \"" + item.getText() + "\" found"));
            addItem(item);
        }
        item.setUserData(id);
        return item;
    }
    
}
