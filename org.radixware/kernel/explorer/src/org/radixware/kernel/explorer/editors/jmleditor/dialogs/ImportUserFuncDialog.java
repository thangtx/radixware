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

package org.radixware.kernel.explorer.editors.jmleditor.dialogs;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QRadioButton;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QVBoxLayout;
import java.awt.Color;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.EnumSet;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.schemas.xscml.JmlType;


public class ImportUserFuncDialog extends  ExplorerDialog{
    private QRadioButton rbAddToEnd;
    private QRadioButton rbAddToBegining;
    private QRadioButton rbReplace;
    private QLabel lbUpdateProfileWarning;
    private QLineEdit edNewSrcVersionName;
    private boolean isSaveAsNewSrcVersion=true;
    private ImportAction importAction;
    private final JmlEditor editor;
    private final JmlType jmlTypeFromFile;
    private JmlType resultJmlType=null;
    private boolean needUpdateProfile=false;
    private boolean doImportChangeLog = false;
    private final boolean profileCanBeUpdated;
    private final boolean changeLogCanBeImported;
    
    public enum ImportAction{
        REPLACE,
        ADD_TO_BEGINING,
        ADD_TO_END
    }
    
    public ImportUserFuncDialog(final JmlEditor editor, JmlEditor.UserFuncImportInfo info){
         super(editor.getEnvironment(), editor, "ImportUserFuncDialog");
         this.setWindowTitle(Application.translate("JmlEditor", "Import User-Defined Function"));
         this.editor=editor;
         this.jmlTypeFromFile=info.getSrc();
         this.profileCanBeUpdated = editor.getUserFunc().isFreeForm() && info.getProfInfo() != null;
         this.changeLogCanBeImported = info.getChangeLog() != null;
         createUI();
    }
    
    private void createUI() {       
        this.setMinimumSize(100, 270);
        
        final QGroupBox actionPanel=createActionPanel();
        final QGroupBox newSrcVersionPanel=createNewSrcVersionPanel();
        
        final QPushButton btnPreview=new QPushButton(this);
        btnPreview.setText(Application.translate("JmlEditor", "Preview"));    
        btnPreview.clicked.connect(this, "preview(boolean)");
        //btnPreview.setEnabled(false);
        
        dialogLayout().addWidget(actionPanel);
        dialogLayout().addWidget(newSrcVersionPanel);
        
        if (profileCanBeUpdated) {
            final QGroupBox gbUpdateProfile = new QGroupBox(Application.translate("JmlEditor",
                    "Update profile"), this);
            gbUpdateProfile.setCheckable(true);
            gbUpdateProfile.setChecked(false);
            gbUpdateProfile.toggled.connect(this, "updateProfileToggle(Boolean)");

            lbUpdateProfileWarning = new QLabel(this);
            changeWarningLabelColor(needUpdateProfile);
            lbUpdateProfileWarning.setText(Application.translate("JmlEditor",
                    "Warning: The integrity of the existing function invocations may be violated"));
            final QVBoxLayout ltUpdateProfile = new QVBoxLayout(gbUpdateProfile);
            ltUpdateProfile.addWidget(lbUpdateProfileWarning);
            gbUpdateProfile.setLayout(ltUpdateProfile);
            
            dialogLayout().addWidget(gbUpdateProfile);
        }
        
        if (changeLogCanBeImported) {
            final QCheckBox cbImportChangeLog = new QCheckBox(
                    Application.translate("JmlEditor", "Import change log"), this);
            cbImportChangeLog.setChecked(true);
            cbImportChangeLog.toggled.connect(this, "importChangeLogToggle(Boolean)");
            
            dialogLayout().addWidget(cbImportChangeLog);
            doImportChangeLog = true;
        }
        
        dialogLayout().addWidget(btnPreview);
        
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL),true);        
        this.setWindowModality(Qt.WindowModality.WindowModal);
    }       
    
    private QGroupBox createActionPanel(){
        final QGroupBox groupBox = new QGroupBox(Application.translate("JmlEditor" ,"Action"));        
        final QVBoxLayout layout=new QVBoxLayout();
        rbReplace=new QRadioButton(Application.translate("JmlEditor" ,"Replace existing function"),groupBox);
        rbReplace.toggled.connect(this, "changeAction(boolean)");
        rbAddToEnd=new QRadioButton(Application.translate("JmlEditor" ,"Add to the end"),groupBox);
        rbAddToEnd.toggled.connect(this, "changeAction(boolean)");        
        rbAddToBegining=new QRadioButton(Application.translate("JmlEditor" ,"Add to the beginning"),groupBox);
        rbAddToBegining.toggled.connect(this, "changeAction(boolean)");
        rbReplace.setChecked(true);
        
        layout.addWidget(rbReplace);
        layout.addWidget(rbAddToBegining);
        layout.addWidget(rbAddToEnd);                 
        
        groupBox.setLayout(layout);
        groupBox.setSizePolicy(QSizePolicy.Policy.Preferred, QSizePolicy.Policy.Maximum);
        return groupBox;
    } 
    
    private QGroupBox createNewSrcVersionPanel(){
        final QGroupBox groupBox = new QGroupBox(Application.translate("JmlEditor" ,"Save current source code to new version"));
        groupBox.setCheckable(true); 
        groupBox.toggled.connect(this, "saveAsNewSrcVersion(boolean)"); 
        
        final QHBoxLayout layout=new QHBoxLayout();
        final QLabel lbNewSrcVersionName=new QLabel(this);
        lbNewSrcVersionName.setText(Application.translate("JmlEditor" ,"Name of source code version")+":");
        edNewSrcVersionName=new QLineEdit(this);   
        final DateFormat dateFormat= DateFormat.getDateInstance(DateFormat.SHORT);//DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,locale);
        final DateFormat timeFormat= DateFormat.getTimeInstance();
        final String s=dateFormat.format(Calendar.getInstance().getTime()) + "_" + timeFormat.format(Calendar.getInstance().getTime());        
        edNewSrcVersionName.setText("src_version_"+s);
        edNewSrcVersionName.textChanged.connect(this, "srcVersionNameChanged(String)");

        layout.addWidget(lbNewSrcVersionName);    
        layout.addWidget(edNewSrcVersionName);         
        
        groupBox.setLayout(layout);
        groupBox.setSizePolicy(QSizePolicy.Policy.Preferred, QSizePolicy.Policy.Maximum);
        return groupBox;
    } 
    
    @SuppressWarnings("unused")
    private void srcVersionNameChanged(final String text){
        final boolean okEnabled=!(isSaveAsNewSrcVersion && edNewSrcVersionName.text().isEmpty());
        getButton(EDialogButtonType.OK).setEnabled(okEnabled);
    }
    
    @SuppressWarnings("unused")
    private void saveAsNewSrcVersion(final boolean checked){
        isSaveAsNewSrcVersion=checked;
        edNewSrcVersionName.setEnabled(checked);
        final boolean okEnabled=!(checked && edNewSrcVersionName.text().isEmpty());
        getButton(EDialogButtonType.OK).setEnabled(okEnabled);
    }
    
    public boolean getIsSaveAsNewSrcVersion(){
        return isSaveAsNewSrcVersion;
    }
    
    @SuppressWarnings("unused")
    private void updateProfileToggle(final Boolean checked){
        needUpdateProfile = checked;
        changeWarningLabelColor(needUpdateProfile);
    }
    
    private void changeWarningLabelColor(boolean isUpdateEnabled) {
        final Color c = isUpdateEnabled ? Color.RED : Color.GRAY;
        ExplorerTextOptions.Factory.getOptions(c).applyTo(lbUpdateProfileWarning);
    }
    
    public boolean needUpdateProfile(){
        return needUpdateProfile;
    }
    
    @SuppressWarnings("unused")
    private void importChangeLogToggle(final Boolean checked){
        doImportChangeLog = checked;
    }
    
    public boolean doImportChangeLog() {
        return doImportChangeLog;
    }
    
    @SuppressWarnings("unused")
    private void changeAction(final boolean b){
       if(rbReplace.isChecked()){
           rbAddToEnd.setChecked(false);
           rbAddToBegining.setChecked(false);   
           importAction=ImportAction.REPLACE;
       }else if(rbAddToEnd.isChecked()){
           rbReplace.setChecked(false);
           rbAddToBegining.setChecked(false);  
           importAction=ImportAction.ADD_TO_END;
       }
       else if(rbAddToBegining.isChecked()){
           rbReplace.setChecked(false);
           rbAddToEnd.setChecked(false); 
           importAction=ImportAction.ADD_TO_BEGINING;
       }
       resultJmlType=null;
    }
    
    @SuppressWarnings("unused")
    private void preview(final boolean b){
       final JmlType newJmlType=getNewJmlType();       
       final PreviewImportDialog dialog=new PreviewImportDialog(this, editor, newJmlType);
       dialog.exec();
    }
    
    public JmlType getJmlType(){
        return resultJmlType==null ? getNewJmlType() : resultJmlType;
    }
    
    private JmlType getNewJmlType(){
        JmlType jmlType = JmlType.Factory.newInstance();
        editor.getUserFunc().getSource().getItems().clear();
        editor.getJmlProcessor().toXml(editor.getTextEditor().toPlainText(), editor.getTextEditor().textCursor());
        ((Jml)editor.getJmlProcessor().getSource())/*getUserFunc().getSource(editor.getEditingVersionName())*/.appendTo(jmlType, AdsDefinition.ESaveMode.NORMAL);
        switch(importAction){
            case REPLACE:
                jmlType=jmlTypeFromFile;
                break;
            case ADD_TO_END:
                jmlType.getItemList().addAll(jmlType.getItemList().size(), jmlTypeFromFile.getItemList());
                break;
            case ADD_TO_BEGINING:
                jmlType.getItemList().addAll(0, jmlTypeFromFile.getItemList());
                break;
        }
        return jmlType;
    }
    
    public ImportAction getImportAction(){
        return importAction;
    }
    
    public String getSrcVersionName(){
        return edNewSrcVersionName.text();
    }
     
    @Override
    public void accept() {
        saveGeometryToConfig();
        super.accept();
    }
}
