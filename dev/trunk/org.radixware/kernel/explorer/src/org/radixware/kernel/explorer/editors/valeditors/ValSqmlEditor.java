/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.editors.valeditors;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.SqmlEditorDialog;
import org.radixware.kernel.explorer.widgets.propeditors.IDisplayStringProvider;
import org.radixware.schemas.xscml.Sqml;


public final class ValSqmlEditor extends ValEditor<Sqml>{
    
    private Id contextClassId;
    private Map<String, Id>aliases;
    private ISqmlParameters parameters;
    private boolean isImportAccessible;
    private boolean isExportAccessible;
    private boolean isTranslateSqmlEnabled;
            
    private final QToolButton editBtn;
    
    public ValSqmlEditor(final IClientEnvironment environment, final QWidget parent, final boolean mandatory, final boolean readonly) {
        this(environment, parent, null, mandatory, readonly);
    }
    
    public ValSqmlEditor(final IClientEnvironment environment, final QWidget parent, final Id contextClassId) {
        this(environment, parent, contextClassId, true, false);
    }    
    
    public ValSqmlEditor(final IClientEnvironment environment, final QWidget parent) {
        this(environment, parent, null, true, false);
    }    
    
    public ValSqmlEditor(final IClientEnvironment environment, final QWidget parent, final Id contextClassId, final boolean mandatory, final boolean readonly) {
        super(environment, parent, new EditMaskNone(), mandatory, readonly);
        {
            final QAction action = new QAction(this);
            action.triggered.connect(this, "editSqml()");            
            action.setText("...");
            action.setObjectName("edit");
            editBtn = addButton(null, action);
            editBtn.setText("...");
        }
        this.contextClassId = contextClassId;
        getLineEdit().setReadOnly(true);
        updateEditButton();
    }
    
    private void updateEditButton(){
        if (isReadOnly()){
            editBtn.setToolTip(getEnvironment().getMessageProvider().translate("SqmlEditor", "View"));
        }else{
            editBtn.setToolTip(getEnvironment().getMessageProvider().translate("SqmlEditor", "Edit"));
        }
    }
    
    public final void setSelectButtonIcon(final QIcon icon) {
        editBtn.setText(icon == null ? "..." : null);
        editBtn.setIcon(icon);
    }

    public final void setSelectButtonHint(final String hint) {
        editBtn.setToolTip(hint);
    }    

    @Override
    protected String getStringToShow(final Object value) {
        final String defaultString;
        if (value == null) {
            defaultString = getEditMask().getNoValueStr(getEnvironment().getMessageProvider());
        } else {
            defaultString = getEnvironment().getMessageProvider().translate("Value", "<value defined>");
        }        
        final IDisplayStringProvider displayStringProvider = this.getDisplayStringProvider();
        if (displayStringProvider == null) {
            return defaultString;
        }
        return displayStringProvider.format(getEditMask(), value, defaultString);
    }

    @Override
    protected boolean isButtonCanChangeValue(final QAbstractButton button) {
        if (button==editBtn){
            return !isReadOnly();
        }else{
            return super.isButtonCanChangeValue(button);
        }
    }
    
    public void setContextClassId(final Id contextId) {
        this.contextClassId = contextId;
    }
    
    public void setAliases(final Map<String, Id> aliases) {
        if (aliases==null){
            this.aliases = null;
        }else{
            this.aliases = new HashMap<>(aliases);
        }
    }

    @Override
    public void setValue(final Sqml value) {
        super.setValue(value==null ? null : (Sqml)value.copy());
    }    
    
    public void setSqmlParameters(final ISqmlParameters parameters){
        this.parameters = parameters;
    }
    
    public void setImportAccessible(final boolean isAccessible){
        this.isImportAccessible = isAccessible;
    }
    
    public void setExportAccessible(final boolean isAccessible){
        this.isExportAccessible = isAccessible;
    }
    
    public void setTranslateButtonEnabled(final boolean isEnabled){
        this.isTranslateSqmlEnabled = isEnabled;
    }    

    @Override
    public Sqml getValue() {
        final Sqml value = super.getValue();
        return value==null ? null : (Sqml)value.copy();
    }

    public Id getContextClassId() {
        return contextClassId;
    }
        
    public boolean isImportAccessible(){
        return isImportAccessible;
    }   
    
    public boolean isExportAccessible(){
        return isExportAccessible;
    }
    
    public boolean isTranslateSqmlEnabled(){
        return isTranslateSqmlEnabled;
    }
    
    public ISqmlParameters getParamters(){
        return parameters;
    }    
    
    public Map<String,Id> getAliases(){
        return aliases==null ? null : new HashMap<>(aliases);
    }
    
    public void editSqml(){
        final SqmlEditorDialog dialog = new SqmlEditorDialog(getEnvironment(), this, contextClassId);
        final String dialogTitle = getDialogTitle();
        if (dialogTitle!=null && !dialogTitle.isEmpty()){
            dialog.setWindowTitle(dialogTitle);
        }
        dialog.setReadOnly(isReadOnly());
        if (aliases!=null){
            dialog.setAliases(aliases);
        }
        dialog.setImportAccessible(isImportAccessible);
        dialog.setExportAccessible(isExportAccessible);
        dialog.setTranslateButtonEnabled(isTranslateSqmlEnabled);
        dialog.setSqml(getValue(), parameters);
        if (dialog.exec()==QDialog.DialogCode.Accepted.value()){
            setValue(dialog.getSqml());
        }
    }
    
    @Override
    public QSize sizeHint() {
        final QSize size = super.sizeHint();
        return new QSize(Math.max(size.width(), 200), size.height());
    }

    @Override
    public QSize minimumSizeHint() {
        final QSize size = super.sizeHint();
        return new QSize(Math.max(size.width(), 200), size.height());
    }    
}
