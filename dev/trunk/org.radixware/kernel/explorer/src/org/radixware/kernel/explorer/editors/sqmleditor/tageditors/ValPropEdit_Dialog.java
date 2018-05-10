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

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValBoolEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValRefEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.widgets.propeditors.IDisplayStringProvider;


abstract class ValPropEdit_Dialog extends ExplorerDialog {
    
    final static class SqmlDefDisplayProvider implements IDisplayStringProvider{
        
        private final EDefinitionDisplayMode showMode;
        private final String tableAlias;                
        
        public SqmlDefDisplayProvider(final EDefinitionDisplayMode displayMode){
            this(displayMode, null);
        }
        
        public SqmlDefDisplayProvider(final EDefinitionDisplayMode displayMode, final String tableAlias){
            showMode = displayMode;
            this.tableAlias = tableAlias;
        }

        @Override
        public String format(final EditMask mask, final Object value, final String defaultDisplayString) {
            if (value instanceof ISqmlColumnDef){
                final ISqmlColumnDef column = (ISqmlColumnDef)value;
                final ISqmlTableDef ownerTable = column.getOwnerTable();
                final String tableDisplayString;
                if (tableAlias!=null && !tableAlias.isEmpty()){
                    tableDisplayString = tableAlias;
                } else if (ownerTable.hasAlias()){
                    tableDisplayString = ownerTable.getAlias();
                }else{
                    tableDisplayString = ownerTable.getDisplayableText(showMode);
                }
                final String columnDisplayString;
                if (showMode== EDefinitionDisplayMode.SHOW_FULL_NAMES){
                    columnDisplayString = column.getShortName();
                }else{
                    columnDisplayString = column.getDisplayableText(showMode);
                }
                return tableDisplayString+"."+columnDisplayString;
            }else if (value instanceof ISqmlTableDef){
                if (tableAlias!=null && !tableAlias.isEmpty()){
                    return tableAlias;
                }
                final ISqmlTableDef table = (ISqmlTableDef)value;
                return table.hasAlias() ? table.getAlias() : defaultDisplayString;
            }else{
                return defaultDisplayString;
            }
        }
        
    }

    private final boolean isMandatory;
    private ISqmlColumnDef prop;
    
    public ValPropEdit_Dialog(final IClientEnvironment environment, final QWidget parent, final ISqmlColumnDef prop, final String dialogName) {
        this(environment, parent, prop, dialogName, false);
    }    

    public ValPropEdit_Dialog(final IClientEnvironment environment, final QWidget parent, final ISqmlColumnDef prop, final String dialogName, final boolean isMandatory) {
        super(environment, parent, dialogName);
        this.isMandatory = isMandatory;        
        this.prop = prop;
        setAutoSize(true);
        setDisposeAfterClose(true);
    }

    protected final ValEditor createValEditor(final QWidget parentWidget) {
        final EValType type = prop.getType();
        final ValEditor editor;

        switch(type){
            case ANY:{
                editor = new ValStrEditor(getEnvironment(), parentWidget);
                break;
            }
            case BOOL:{
                editor = new ValBoolEditor(getEnvironment(), parentWidget);
                break;
            }
            default:
                editor = ValEditor.createForValType(getEnvironment(), type, prop.getEditMask(), isMandatory, false, parentWidget);
        }
        if (type == EValType.PARENT_REF) {
            try {
                if (prop.getSelectorPresentationId() != null && prop.getSelectorPresentationClassId() != null) {
                    ((ValRefEditor) editor).setSelectorPresentation(prop.getSelectorPresentationClassId(), prop.getSelectorPresentationId());
                }
            } catch (DefinitionError e) {
                getEnvironment().processException(e);
            }
        }
        if (type.isArrayType()) {
            editor.setVisible(false);
        }
        editor.setObjectName("valEditor");
        return editor;
    }
    
    protected final ExplorerTextOptions getLabelTextOptions(){
        final EnumSet<ETextOptionsMarker> markers = 
            EnumSet.of(ETextOptionsMarker.LABEL, ETextOptionsMarker.EDITABLE_VALUE, ETextOptionsMarker.REGULAR_VALUE);
        return (ExplorerTextOptions)getEnvironment().getTextOptionsProvider().getOptions(markers, null);
    }
        
    protected final void setProperty(final ISqmlColumnDef prop){
        this.prop = prop;
    }

    public final ISqmlColumnDef getProperty() {
        return prop;
    }
    
    @Override
    protected QSize layoutMinimumSize(final QSize size){
        size.setHeight(layout().sizeHint().height());
        size.setWidth(layout().sizeHint().width()+80);
        return size;
    }
}