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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QWidget;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.scml.SqmlExpression;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.sqmleditor.SqmlEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.schemas.xscml.Sqml;


public class SqmlEditorDialog extends ExplorerDialog{
    
    private static final QIcon SQML_ICON = ExplorerIcon.getQIcon(new ClientIcon("classpath:images/sqml.svg", true){});
    
    private final SqmlEditor editor;
    private boolean wasClosed;
    private Sqml sqml;
    private boolean isReadOnly;
    private ISqmlTableDef contextClassDef;
    private boolean isModified;
    private boolean isImportAccessible;
    private boolean isExportAccessible;
    private boolean isTranslateSqmlEnabled;
    private ISqmlParameters parameters;
    
    public SqmlEditorDialog(final IClientEnvironment environment, final QWidget parent, final Id contextId) {
        this(environment, parent, contextId, null);
    }
    
    public SqmlEditorDialog(final IClientEnvironment environment, final QWidget parent, final Id contextId, final ISqmlParameters param) {
        super(environment, parent, true);
        setWindowTitle(environment.getMessageProvider().translate("SqmlEditor", "SQML Condition"));
        setWindowIcon(SQML_ICON);
        editor = new SqmlEditor(environment, this, contextId, param);
        dialogLayout().addWidget(editor);
        setupDialogButtons();
        setDisposeAfterClose(true);
        acceptButtonClick.connect(this, "onOKButtonClidk()");
        rejectButtonClick.connect(this,"reject()");
    }

    private void setupDialogButtons(){
        clearButtons();
        if (editor.isReadonly()){
            addButton(EDialogButtonType.CLOSE);
        }else{
            addButton(EDialogButtonType.OK);
            addButton(EDialogButtonType.CANCEL);
        }
    }        
    
    @SuppressWarnings("unused")
    private void onOKButtonClidk(){
        accept();
    }
    
    public void setReadOnly(final boolean isReadOnly){
        if (editor.isReadonly()!=isReadOnly){
            editor.setReadonly(isReadOnly);
            setupDialogButtons();
        }
    }    
    
    public void setContextClass(final Id contextId) {
        if (!wasClosed){
            editor.setContextClass(contextId);
        }
    }
    
    public void sortPropTree() {
        if (!wasClosed){
            editor.sortPropTree();
        }
    }

    public void setAliases(final Map<String, Id> aliases) {
        if (!wasClosed){
            editor.setAliases(aliases);
        }
    }    
    
    public void setSqml(final Sqml sqml) {
        if (!wasClosed){
            editor.setSqml(sqml);
        }
    }
    
    public void setSqml(final SqmlExpression sqml) {
        if (!wasClosed){
            editor.setSqml(sqml);
        }
    }    
    
    public void setSqml(final Sqml sqml, final ISqmlParameters parameters) {
        if (!wasClosed){
            editor.setSqml(sqml, parameters);
        }
    }
    
    public void setImportAccessible(final boolean isAccessible){
        if (!wasClosed){
            editor.setImportAccessible(isAccessible);
        }
    }
    
    public void setExportAccessible(final boolean isAccessible){
        if (!wasClosed){
            editor.setExportAccessible(isAccessible);
        }
    }
    
    public void setTranslateButtonEnabled(final boolean isEnabled){
        if (!wasClosed){
            editor.setTranslateButtonEnabled(isEnabled);
        }
    }    
    
    public void clear() {
        if (!wasClosed){
            editor.clear();
        }
    }    
        
    public Sqml getSqml(){
        return wasClosed ? sqml : editor.getSqml();
    }
    
    public boolean isReadOnly(){
        return wasClosed ? isReadOnly : editor.isReadonly();
    }
    
    public boolean isModified() {
        return wasClosed ? isModified : editor.isModified();
    }

    public ISqmlTableDef getContextClassDef() {
        return wasClosed ? contextClassDef : editor.getContextClassDef();
    }
        
    public boolean isImportAccessible(){
        return wasClosed ? isImportAccessible : editor.isImportAccessible();
    }   
    
    public boolean isExportAccessible(){
        return wasClosed ? isExportAccessible : editor.isExportAccessible();
    }    
    
    public boolean isTranslateSqmlEnabled(){
        return wasClosed ? isTranslateSqmlEnabled : editor.isTranslateSqmlEnabled();
    }
    
    public ISqmlParameters getParamters(){
        return wasClosed ? parameters : editor.getParamters();
    }

    @Override
    public void done(final int result) {        
        sqml = editor.getSqml();
        isReadOnly = editor.isReadonly();
        isModified = editor.isModified();
        contextClassDef = editor.getContextClassDef();
        isImportAccessible = editor.isImportAccessible();
        isExportAccessible = editor.isExportAccessible();
        isTranslateSqmlEnabled = editor.isTranslateSqmlEnabled();
        parameters = editor.getParamters();
        wasClosed = true;
        super.done(result);
    }        
}
