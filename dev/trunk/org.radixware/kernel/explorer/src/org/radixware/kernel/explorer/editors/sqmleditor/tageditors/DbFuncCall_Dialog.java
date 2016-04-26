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

package org.radixware.kernel.explorer.editors.sqmleditor.tageditors;

import com.trolltech.qt.core.Qt.ItemDataRole;
import com.trolltech.qt.core.Qt.ItemFlag;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.util.*;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitionsFilter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlFunctionDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlFunctionParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlPackageDef;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValSqmlDefEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.models.SqmlTreeModel;


public class DbFuncCall_Dialog extends ExplorerDialog{
    
    private ISqmlFunctionDef function;
    private Map<Id,String> parameters;
    private final boolean isReadOnly;
    private final QTableWidget paramsTable;
    private final QCheckBox chboxDefineParams;
    
    public DbFuncCall_Dialog(final IClientEnvironment environment, final ISqmlFunctionDef function, final Map<Id,String> parameters, final EDefinitionDisplayMode displayMode, final boolean isReadOnly, final QWidget parent){
        super(environment,parent);
        setAttribute(WidgetAttribute.WA_DeleteOnClose);
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.EXPLORER));
        setWindowTitle(environment.getMessageProvider().translate("DbFuncCall", "SQL Function Call Tag"));
        this.function = function;
        if (parameters!=null){
            this.parameters = new HashMap<>();
            this.parameters.putAll(parameters);
        }
        this.isReadOnly = isReadOnly;
        
        final MessageProvider messageProvider = environment.getMessageProvider();
        final QLayout layout = layout();
        // Function:
        final QLabel funcNameLabel = 
                new QLabel(
                        messageProvider.translate("DbFuncCall", "Function:"),
                        this);
        layout.addWidget(funcNameLabel);
        // Editor
        final SqmlTreeModel model = new SqmlTreeModel(environment,
                new ArrayList<ISqmlDefinition>(environment.getSqmlDefinitions().getPackages()),
                EnumSet.of(SqmlTreeModel.ItemType.FUNCTION));
        //model.setMarkDeprecatedItems(true);
        model.setDisplayMode(displayMode);
        
        final ValSqmlDefEditor editor = new ValSqmlDefEditor(environment, parent, model, true, isReadOnly);
        editor.setDefinitionDisplayMode(EDefinitionDisplayMode.SHOW_FULL_NAMES);
        editor.setDefinitionsFilter(new ISqmlDefinitionsFilter() {
            @Override
            public boolean isAccepted(final ISqmlDefinition definition, final ISqmlDefinition ownerDefinition) {
                if(definition instanceof ISqmlPackageDef) {
                    final ISqmlPackageDef def = (ISqmlPackageDef) definition;
                    return def.getFunctionsCount()>0;
                } 
                return true;
            }
        });
        layout.addWidget(editor);
        // Parameters label:
        chboxDefineParams = 
                new QCheckBox(
                        messageProvider.translate("DbFuncCall", "Define parameters"),
                        this);
        chboxDefineParams.setEnabled(!isReadOnly);
        layout.addWidget(chboxDefineParams);
        // Parameters list
        paramsTable = new QTableWidget(0, 2, this);
        layout.addWidget(paramsTable);
        chboxDefineParams.toggled.connect(paramsTable, "setEnabled(boolean)");
        paramsTable.setEnabled(chboxDefineParams.isChecked());
        paramsTable.horizontalHeader().setStretchLastSection(true);
        paramsTable.setHorizontalHeaderItem(0, new QTableWidgetItem(messageProvider.translate("DbFuncCall", "Name")));
        paramsTable.setHorizontalHeaderItem(1, new QTableWidgetItem(messageProvider.translate("DbFuncCall", "Value")));
        fillParameters(function);
        fillValues(parameters);
        if (isReadOnly){
            addButtons(EnumSet.of(EDialogButtonType.CLOSE),true);
        }else{
            addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true).get(EDialogButtonType.OK).setEnabled(function!=null);
        }
        editor.valueChanged.connect(this, "onValueChanged(Object)");
        editor.setDialogTitle(messageProvider.translate("DbFuncCall", "Select Function"));
        editor.setValue(function);
    }
    
    public DbFuncCall_Dialog(final IClientEnvironment environment, final EDefinitionDisplayMode displayMode, final boolean isReadOnly, final QWidget parent){
        this(environment,null,null,displayMode,isReadOnly,parent);
    }
    
    
    public ISqmlFunctionDef getSqmlFunction(){
        return function;
    }
    
    public Map<Id,String> getParameters(){
        return parameters;
    }
    
    @SuppressWarnings("unused")
    private void onValueChanged(final Object o) {
        if (getButton(EDialogButtonType.OK)!=null){
            getButton(EDialogButtonType.OK).setEnabled(o != null);
        }
        chboxDefineParams.setChecked(parameters != null && !isReadOnly);
        function = (ISqmlFunctionDef) o;
        fillParameters(function);
    }

    @Override
    public void accept() {
        if(parameters == null) { 
            parameters = new HashMap<>();
        }
        
        if(paramsTable.isEnabled()) {
            for(int i = 0; i < paramsTable.rowCount(); i++) {
                final Id id = (Id) paramsTable.item(i, 0).data(ItemDataRole.UserRole);
                final String value = paramsTable.item(i, 1).text();
                parameters.put(id, value);
            }
        } else {
            parameters = Collections.<Id, String>emptyMap();
        }

        super.accept();
    }
    
    private void fillParameters(final ISqmlFunctionDef func) {
        paramsTable.setRowCount(0);
        if(func == null || func.getParametersCount() == 0) { 
            return;
        }
        
        int rowCount = paramsTable.rowCount();
        for(ISqmlFunctionParameter p : func.getAllParameters()) {
            paramsTable.setRowCount(++rowCount);
            
            final QTableWidgetItem name = new QTableWidgetItem(p.getShortName());
            name.setIcon(ExplorerIcon.getQIcon(p.getIcon()));
            name.setFlags(ItemFlag.ItemIsEnabled, ItemFlag.ItemIsSelectable);
            name.setData(ItemDataRole.UserRole, p.getId());
            paramsTable.setItem(rowCount - 1, 0, name);
            
            final QTableWidgetItem value = new QTableWidgetItem();
            paramsTable.setItem(rowCount - 1, 1, value);
            value.setText(parameters == null ? p.getDefaultVal() : parameters.get(p.getId()));
        }
    }

    private void fillValues(final Map<Id, String> parameters) {
        if(parameters != null && !parameters.isEmpty()) {
            chboxDefineParams.setChecked(!isReadOnly);
            for(int i = 0; i < paramsTable.rowCount(); i++) {
                final Id id = (Id) paramsTable.item(i, 0).data(ItemDataRole.UserRole);
                final String value = parameters.get(id);
                paramsTable.item(i, 1).setText(value);
            }
        }
    }
    
}
