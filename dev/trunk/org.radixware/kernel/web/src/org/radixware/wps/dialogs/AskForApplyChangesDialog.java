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

package org.radixware.wps.dialogs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IAskForApplyChangesDialog;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.views.ModificationsList;
import org.radixware.kernel.common.client.views.ModificationsList.ModifiedObject;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IButton.ClickHandler;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.rwt.*;


public class AskForApplyChangesDialog extends Dialog implements IAskForApplyChangesDialog{
    
    private final IClientEnvironment environment;
    private final TableLayout modificationsTable = new TableLayout();    
    private final Map<CheckBox, ModifiedObject> modifiedObjectByCheckBox = new HashMap<CheckBox, ModifiedObject>();
    
    public AskForApplyChangesDialog (final IClientEnvironment environment, final IDialogDisplayer displayer, final ModificationsList modificationsList){
        super(displayer, environment.getMessageProvider().translate("ExplorerDialog","Confirm to Apply Changes"));
        this.environment = environment;
        setupUi(modificationsList);
    }
    
    private void setupUi(final ModificationsList modificationsList){
        final MessageProvider mp = getEnvironment().getMessageProvider();
        setWindowIcon(environment.getApplication().getImageManager().getIcon(ClientIcon.Editor.SAVE));
        final VerticalBoxContainer container = new VerticalBoxContainer();
        {                            
            final Label lbHeader  = new Label(mp.translate("ExplorerDialog", "The following objects have been modified."));
            lbHeader.setTextWrapDisabled(true);
            lbHeader.setAlign(Alignment.CENTER);            
            lbHeader.getHtml().setCss("font-weight", "bold");
            lbHeader.getHtml().setCss("font-size", "medium");
            container.add(lbHeader);
        }
        {
            final Label lbHeader  = new Label(mp.translate("ExplorerDialog", "Do you want to save them before closing?"));
            lbHeader.setTextWrapDisabled(true);
            lbHeader.setAlign(Alignment.CENTER);            
            lbHeader.getHtml().setCss("font-weight", "bold");
            lbHeader.getHtml().setCss("font-size", "medium");
            container.add(lbHeader);
        }
        {
            final List<ModifiedObject> modifiedObjects = modificationsList.getModifiedObjectsList();
            for (ModifiedObject modifiedObject: modifiedObjects){
                if (modifiedObject.hasUnsavedData()){
                    addModifiedObject(modifiedObject);
                }
            }
            modificationsTable.getHtml().setCss("margin-top", "10px");
            container.add(modificationsTable);
            container.setAutoSize(modificationsTable, true);
        }
        add(container);
        {
            final IPushButton button = addButton(EDialogButtonType.YES);
            button.setObjectName("btn_save_selected");
            button.setTitle(mp.translate("ExplorerDialog", "&Save Selected"));
            button.setIcon(environment.getApplication().getImageManager().getIcon(ClientIcon.Editor.NEED_FOR_SAVE));
            button.setDefault(true);                        
        }
        {
            final IPushButton button = addButton(EDialogButtonType.IGNORE);
            button.setObjectName("btn_do_not_save");
            button.setTitle(mp.translate("ExplorerDialog", "Do &Not Save"));
            button.setIcon(environment.getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.CANCEL));
            button.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(final IButton source) {
                    uncheckItems();
                }
            });
        }
        {
            final IPushButton button = addButton(EDialogButtonType.CANCEL);
            button.setObjectName("btn_do_not_close");
            button.setTitle(mp.translate("ExplorerDialog", "Do Not &Close"));
            button.setIcon(environment.getApplication().getImageManager().getIcon(ClientIcon.Dialog.BUTTON_CANCEL));            
        }
        getHtml().setCss("max-width", "360px");
        setMinimumWidth(360);        
        setMinimumHeight(175);
    }
    
    private void addModifiedObject(final ModificationsList.ModifiedObject modifiedObject){
        final TableLayout.Row row = modificationsTable.addRow();
        final TableLayout.Row.Cell cell = row.addCell();
        final CheckBox cbModifiedObject = new CheckBox();
        cbModifiedObject.setIcon(modifiedObject.getIcon());
        cbModifiedObject.setTitle(modifiedObject.getTitle());
        cbModifiedObject.setSelected(true);
        cell.add(cbModifiedObject);
        modifiedObjectByCheckBox.put(cbModifiedObject, modifiedObject);
    }

    @Override
    public List<ModifiedObject> getItemsToApplyChanges() {
        final List<ModifiedObject> result = new LinkedList<ModifiedObject>();
        final List<TableLayout.Row> rows = modificationsTable.getRows();
        for (TableLayout.Row row: rows){
            final CheckBox cbModifiedObject = (CheckBox)row.getCell(0).getChildren().get(0);
            if (cbModifiedObject.isSelected()){
                result.add(modifiedObjectByCheckBox.get(cbModifiedObject));
            }
        }
        return result;
    }
    
    private void uncheckItems(){
        final List<TableLayout.Row> rows = modificationsTable.getRows();
        for (TableLayout.Row row: rows){
            ((CheckBox)row.getCell(0).getChildren().get(0)).setSelected(false);
        }
    }
}
