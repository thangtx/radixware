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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.sqmleditor.SqmlEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.models.SqmlTreeModel;



public class SetTableAliasDialog extends ExplorerDialog {
    
    private final SqmlTreeModel sqmlTreeModel;
    private final List<ISqmlDefinition> currentItemPath;
    private ValStrEditor aliasEditor;
    private ValStrEditor tableEditor;
    private ISqmlTableDef selectedTable = null;
    
    public SetTableAliasDialog(final IClientEnvironment env, final SqmlTreeModel treeModel, final List<ISqmlDefinition> currentItemPath, final QWidget parent) {
        super(env, parent);
        this.sqmlTreeModel = treeModel;
        this.currentItemPath = currentItemPath;
        setUpUi();
    }

    private void setUpUi() {
        final MessageProvider msgProvider = getEnvironment().getMessageProvider();
        setWindowTitle(msgProvider.translate("SetTableAliasDialog", "Attaching Table"));
        setWindowIcon(ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_ADD_ALIAS));
        
        final QGridLayout grid = new QGridLayout();
        dialogLayout().addLayout(grid);
        
        final QLabel lblSelectTable = new QLabel(msgProvider.translate("SetTableAliasDialog", "Table:"));
        grid.addWidget(lblSelectTable, 0, 0);
        
        final QLabel lblAlias = new QLabel(msgProvider.translate("SetTableAliasDialog", "Alias:"));
        grid.addWidget(lblAlias, 1, 0);
        
        tableEditor = new ValStrEditor(getEnvironment(), this);
        tableEditor.addButton(createChooseTableButton());
        tableEditor.setReadOnly(true);
        tableEditor.valueChanged.connect(this, "onEditorChange(Object)");
        grid.addWidget(tableEditor, 0, 1);
        
        aliasEditor = new ValStrEditor(getEnvironment(), this);
        aliasEditor.valueChanged.connect(this, "onEditorChange(Object)");
        grid.addWidget(aliasEditor, 1, 1);
        
        addButtons(EnumSet.of(EDialogButtonType.OK,EDialogButtonType.CANCEL),true).get(EDialogButtonType.OK).setEnabled(false);
        dialogLayout().setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignTop));
    }
    
    private QToolButton createChooseTableButton() {
        final QToolButton button = new QToolButton();
        button.setText("...");
        button.clicked.connect(this, "actionChooseTable()");
        return button;
    }
    
    private void actionChooseTable() {
        final ChooseSqmlDefinitionDialog dlg = new ChooseSqmlDefinitionDialog(getEnvironment(), sqmlTreeModel, currentItemPath, false, this);
        final String title = getEnvironment().getMessageProvider().translate("SetTableAliasDialog", "Select Table");
        dlg.setWindowTitle(title);
        dlg.setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.TABLE));
        if(dlg.execDialog() == DialogResult.ACCEPTED) {
            selectedTable = (ISqmlTableDef) dlg.getCurrentItem();
            tableEditor.setValue(selectedTable.getFullName());
        }
    }
    
    public Id getSelectedId() {
        return (selectedTable == null) ? null : selectedTable.getId();
    }
    
    public String getAlias() {
        return aliasEditor.getValue();
    }
    
    private void onEditorChange(Object value) {
        final String tableValue = tableEditor.getValue();
        final String aliasValue = aliasEditor.getValue();
        final boolean nullVal = tableValue == null || aliasValue == null;
        final boolean cantAccept = nullVal || "".equals(aliasValue) || "".equals(tableValue);
        getButton(EDialogButtonType.OK).setEnabled(!cantAccept);
    }
    
}
