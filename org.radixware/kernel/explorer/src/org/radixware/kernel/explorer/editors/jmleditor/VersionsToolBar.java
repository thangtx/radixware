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

package org.radixware.kernel.explorer.editors.jmleditor;

import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QToolButton;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.ChooseSrcVersionDialog;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.CreateScrVersionDialog;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.ImportUserFuncDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.schemas.xscml.JmlType;


public class VersionsToolBar {
    private final JmlEditor editor;
    private final QToolButton btnCreateSrcVersion = new QToolButton();
    private ValStrEditor edVersion;
    private final QToolBar toolBar;
    private boolean isReadOnlyMode=false;

    VersionsToolBar(final JmlEditor editor){        
        this.editor = editor;        
        toolBar = new QToolBar(editor);        
        createToolBar();
    }  
    
    public void setReadOnlyMode(final boolean isReadOnlyMode) {
       this.isReadOnlyMode=isReadOnlyMode;
       btnCreateSrcVersion.setEnabled(!isReadOnlyMode);
       edVersion.setEnabled(!isReadOnlyMode);
    }
    
    private void createToolBar() {        
        toolBar.setObjectName("JmlEditorToolBar");        
        final QToolButton btnLoadVersion = new QToolButton();
        btnLoadVersion.setObjectName("btnLoadVersion");
        btnLoadVersion.setText("...");
        btnLoadVersion.clicked.connect(this, "btnLoadVersionClicked()"); 
        final QLabel lb=new QLabel(Application.translate("JmlEditor", "Version")+":" ,toolBar);
        final EditMaskStr editMask=new EditMaskStr();
        editMask.setNoValueStr(ChooseSrcVersionDialog.CURRENT_SOURCE);
        edVersion=new ValStrEditor(editor.getEnvironment(), toolBar, editMask, true,true);        
        edVersion.addButton(btnLoadVersion);
        edVersion.setToolTip(Application.translate("JmlEditor", "Select Source Version") );        
        
        btnCreateSrcVersion.setObjectName("btnNewVersion");
        btnCreateSrcVersion.setToolTip(Application.translate("JmlEditor", "Create Source Version"));
        btnCreateSrcVersion.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.CREATE_SOURCE_VERSION));
        btnCreateSrcVersion.clicked.connect(this, "btnNewVersionClicked()");              

        toolBar.addWidget(lb);
        toolBar.addWidget(edVersion);
        toolBar.addWidget(btnCreateSrcVersion);        
    }
     
     @SuppressWarnings("unused")
     private void btnNewVersionClicked(){
         final AdsUserFuncDef userFunc=editor.findUserFunc(null);
         final CreateScrVersionDialog dialog=new CreateScrVersionDialog(editor,userFunc, "CreateScrVersionDialog");
         if ((dialog.exec() == 1)) {
             final String name=dialog.getVersionName();
             editor.createNewSrcVersion(name,dialog.isCopySource());
             editor.openSourceVersion(name);
             editor.setEnabledToBtnSave(true);
          }  
     }

     @SuppressWarnings("unused")
     private void btnLoadVersionClicked(){        
         final AdsUserFuncDef userFunc=editor.findUserFunc(null);
         final ChooseSrcVersionDialog dialog=new ChooseSrcVersionDialog(editor, "ChooseSrcVersionDialog",userFunc,isReadOnlyMode);
         final String editingVersionName=editor.getEditingVersionName();
         final boolean wasSrcChanged=editor.wasSrcChanged();
         final int res=dialog.exec();
         if(dialog.isEditVersionList()){//были изменения в именах
             userFunc.getSourceVersions().clear();
             userFunc.getSourceVersions().putAll(dialog.getSourceVersions()); 
             editor.saveSourceVersions(dialog.getChangedSourceVersions());
             editor.clearErrorListFromOldVersions(dialog.getOldSourceVersions());
             editor.setEnabledToBtnSave(true);
         } 
         if (res == 1) {             
             //editor.saveUserFunc(false, false);
             final boolean isLoadAsCurrent=dialog.isLoadAsCurrent();
             final String newEditingVersionName=dialog.getVersionName();
             if(isLoadAsCurrent){//загружаем исходный текст из выбранной версии в редактируемую версию                 
                 final Jml newSource=userFunc.getSource(newEditingVersionName);              
                 final JmlType jmlType=JmlType.Factory.newInstance();
                 newSource.appendTo(jmlType, ESaveMode.NORMAL);                    
                 editor.openSourceVersion(null);
                 editor.loadXmlToEditor( jmlType, ImportUserFuncDialog.ImportAction.REPLACE);             
             }else{ //открываем выбранную версию в редакторе             
                 if(newEditingVersionName!=null && !newEditingVersionName.equals(editingVersionName)||
                     editingVersionName!=null && !editingVersionName.equals(newEditingVersionName)){
                     editor.openSourceVersion(newEditingVersionName);
                     if(wasSrcChanged){
                         editor.setEnabledToBtnSave(true);
                     }
                 }
             }
         }else if(editingVersionName!=null && !userFunc.getSourceVersions().containsKey(editingVersionName)){
             editor.openSourceVersion(null);
             if(wasSrcChanged){
                 editor.setEnabledToBtnSave(true);
             }
         }        
     }
     
     public void updateSourceVersionEditLine(){
         edVersion.setValue(editor.getEditingVersionName());
     }
     
    public QToolBar getToolBar() {
        return toolBar;
    }
    
}
