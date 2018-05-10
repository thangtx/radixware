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

import com.trolltech.qt.gui.QFormLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitionsFilter;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValRefEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValSqmlDefEditor;
import org.radixware.kernel.explorer.models.SqmlTreeModel;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;

public class TypifiedValue_Dialog extends ValPropEdit_Dialog {

    private final ValSqmlDefEditor sqmlDefEditor;  
    private final boolean isReadOnly;
    private final QFormLayout formLayout = new QFormLayout();
    private ValEditor valueEditor;
    private Object value;

    @SuppressWarnings({"unchecked"})
    public TypifiedValue_Dialog(final IClientEnvironment environment, 
                                              final ISqmlColumnDef prop,
                                              final Object val,
                                              final EDefinitionDisplayMode showMode, 
                                              final boolean isReadOnly,
                                              final QWidget parentWidget) {
        super(environment, parentWidget, prop, "SqmlTypifiedValueDialog", false);
        final EnumSet<SqmlTreeModel.ItemType> itemTypes = EnumSet.of(SqmlTreeModel.ItemType.PROPERTY, SqmlTreeModel.ItemType.MODULE_INFO);
        final SqmlTreeModel sqmlModel = new SqmlTreeModel(environment, null, itemTypes);        
        sqmlModel.setMarkDeprecatedItems(true);
        sqmlModel.setDisplayMode(showMode);        
        sqmlDefEditor = new ValSqmlDefEditor(environment, this, sqmlModel, true, isReadOnly);
        sqmlDefEditor.setDefinitionDisplayMode(showMode);
        sqmlDefEditor.setDisplayStringProvider(new SqmlDefDisplayProvider(showMode));        
        sqmlDefEditor.setDefinitionsFilter(new ISqmlDefinitionsFilter() {
            @Override
            public boolean isAccepted(final ISqmlDefinition definition, final ISqmlDefinition ownerDefinition) {
                if (definition instanceof ISqmlColumnDef){
                    return !((ISqmlColumnDef)definition).getType().isArrayType();
                }else{
                    return true;
                }
            }
        });
        this.isReadOnly = isReadOnly;        

        createUI();        
        setValue(val);
    }

    @SuppressWarnings("unchecked")
    private void setValue(final Object val) {
        if (valueEditor instanceof ValRefEditor && val instanceof Pid){
            final Pid pid = (Pid) val;
            Reference reference;
            final String objTitle;
            try{
                objTitle = pid.getDefaultEntityTitle(getEnvironment().getEasSession());
                reference = new Reference(pid, objTitle);
            }catch(ObjectNotFoundError ex){
                getEnvironment().getTracer().debug(ex);
                reference = new Reference(pid, null, pid.toString());
            } catch(ServiceClientException ex){
                getEnvironment().getTracer().error(ex);
                reference = new Reference(pid, null, pid.toString());
            } catch (InterruptedException ex){
                reference = new Reference(pid, pid.toString());
            }
            valueEditor.setValue(reference);
        }else{
            valueEditor.setValue(val);
        }
        value = val;
    }

    private void createUI() {
        final MessageProvider mp = getEnvironment().getMessageProvider();
        setWindowTitle(mp.translate("SqmlEditor", "Edit Value"));
        final String dialogTitle = isReadOnly ? mp.translate("SqmlEditor", "Column") : mp.translate("SqmlEditor", "Select Column");
        sqmlDefEditor.setDialogTitle(dialogTitle);        
        sqmlDefEditor.setObjectName("editLineProp");        
        
        final QLabel lbPropName = new QLabel(mp.translate("SqmlEditor", "Property:"), this);
        final QLabel lbValue = new QLabel(mp.translate("SqmlEditor", "Value:"), this);
        final ExplorerTextOptions labelTextOptions = getLabelTextOptions();
        labelTextOptions.applyTo(lbPropName);
        labelTextOptions.applyTo(lbValue);
                
        formLayout.addRow(lbPropName, sqmlDefEditor);
        formLayout.addRow(lbValue, (QWidget)null);
        dialogLayout().addLayout(formLayout);        
        
        sqmlDefEditor.valueChanged.connect(this,"onChangeColumn()");
        sqmlDefEditor.setValue(getProperty());
        
        final EnumSet<EDialogButtonType> buttons;
        if (isReadOnly) {
            buttons = EnumSet.of(EDialogButtonType.CLOSE);
        } else {
            buttons = EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL);
        }        
        addButtons(buttons, true);
    }

    @SuppressWarnings("unused")
    private void onChangeColumn(){
        final ISqmlColumnDef newColumn = (ISqmlColumnDef)sqmlDefEditor.getValue();
        if (newColumn==null){
            sqmlDefEditor.setValue(getProperty());
            return;
        }
        setProperty(newColumn);
        if (valueEditor!=null){
            formLayout.removeWidget(valueEditor);
            valueEditor.setParent(null);
            valueEditor.disposeLater();
        }        
        valueEditor = createValEditor(this);
        valueEditor.setReadOnly(isReadOnly);
        formLayout.setWidget(1, QFormLayout.ItemRole.FieldRole, valueEditor);
        value = null;
    }    

    @Override
    public void accept() {
        if (!valueEditor.checkInput()){
            return;
        }
        final Object val = valueEditor.getValue();
        if (val instanceof Reference){
            if (((Reference)val).isBroken()){
                return;
            }else{
                value = ((Reference)val).getPid();
            }            
        }else{
            value = val;
        }
        super.accept();
    }

    public Object getValue(){
        return value;
    }
}