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
package org.radixware.kernel.explorer.editors.valeditors;

import java.util.Objects;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.io.File;
import java.nio.file.Paths;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskFilePath;
import org.radixware.kernel.common.client.meta.mask.validators.IInputValidator;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.enums.EFileDialogOpenMode;
import org.radixware.kernel.common.enums.EFileSelectionMode;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.common.utils.SystemTools;
import static org.radixware.kernel.explorer.editors.valeditors.ValEditor.createAction;

import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.utils.WidgetUtils;

public final class ValFilePathEditor extends ValEditor<String> {
    
    private static class InputValidator implements IInputValidator{
        
        private final boolean checkIfPathExists;
        
        public InputValidator(final boolean checkIfExists){
            checkIfPathExists = checkIfExists;
        }

        @Override
        public String fixup(final String input) {
            return input;
        }

        @Override
        public ValidationResult validate(final IClientEnvironment environment, final String input, final int position) {
            if (input == null) {
                return ValidationResult.ACCEPTABLE;
            }
            if (checkIfPathExists && SystemTools.isWindows && input.contains("[<\">?*\']")) {
                return ValidationResult.INVALID;
            } else {
                return ValidationResult.ACCEPTABLE;
            }    
        }        
    }
    
    private final static String DEFAULT_INITIAL_PATH_CONFIG_SETTING = SettingNames.SYSTEM+"/last_select_file_path";

    private final QToolButton dialogBtn = addButton(null, createAction(this, "onChangeValueClick()","edit"));
    private boolean showFileNameOnly;
    private String initialPathConfigSetting;
    private String lastUsedPath;

    public ValFilePathEditor(final IClientEnvironment env, final QWidget parent, final EditMaskFilePath editMask, final boolean mandatory, final boolean readOnly) {
        super(env, parent, editMask, mandatory, readOnly);
        if (editMask == null) {
            setEditMask(new EditMaskFilePath());
        }
        afterUpdateEditMask(getEditMaskFilePath());
        dialogBtn.setToolTip(env.getMessageProvider().translate("ValFilePathEditor", "Edit"));
        dialogBtn.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.OPEN));
        dialogBtn.setDisabled(readOnly);
    }

    public ValFilePathEditor(final IClientEnvironment env, final QWidget parent) {
        this(env, parent, new EditMaskFilePath(), true, false);
    }
    
    public void setInitialPathConfigKey(final String setting){
        lastUsedPath = null;
        initialPathConfigSetting = setting;
    }        
    
    public String getInitialPathConfigKey(){
        return initialPathConfigSetting;
    }
    
    private String getActualInitialPathConfigSetting(){
        if (initialPathConfigSetting==null || initialPathConfigSetting.isEmpty()){
            return DEFAULT_INITIAL_PATH_CONFIG_SETTING;
        }else{
            return initialPathConfigSetting;
        }
    }    
    
    private String getActualInitialPath(){
        if (lastUsedPath==null || lastUsedPath.isEmpty()){
            final String path = getInitialPath();
            if ((path==null || path.isEmpty()) && getEditMaskFilePath().getStoreLastPathInConfig()){
                return getEnvironment().getConfigStore().readString(getActualInitialPathConfigSetting(), null);
            }
            return path;
        }else{
            return lastUsedPath;
        }
    }

    @Override
    protected void onFocusIn() {
        if (!isReadOnly()) {
            final String val = this.getValue();
            final EditMaskFilePath mask = getEditMaskFilePath();
            if (mask.isSpecialValue(val)) {
                setOnlyText("", "");
            } else {
                setOnlyText(mask.toStr(getEnvironment(), val), "");
            }
            getLineEdit().home(false);
        }
        updateValueMarkers(false);
    }

    @Override
    protected void onFocusOut() {
        super.onFocusOut();
        if (!isReadOnly()) {
            setOnlyText(getStringToShow(getValue()), "");
        }
    }
    
    @Override
    protected void onTextEdited(final String newText) {
        setOnlyValue(newText);
    }    

    @Override
    public void setEditMask(final EditMask editMask) {
        super.setEditMask(editMask);
        lastUsedPath = null;
        final EditMaskFilePath mask = (EditMaskFilePath) editMask;
        getLineEdit().setReadOnly(!mask.getHandleInputAvailable() || isReadOnly());
        afterUpdateEditMask(mask);
    }
    
    private void afterUpdateEditMask(final EditMaskFilePath mask){
        final String title = mask.getFileDialogTitle(getEnvironment().getDefManager());
        if (title==null){
            setDialogTitle(getEnvironment().getMessageProvider().translate("ValFilePathEditor", "Select File"));
        }else{
            setDialogTitle(title);
        }
        setInputValidator(new InputValidator(mask.getCheckIfPathExists()));
        getLineEdit().setReadOnly(isReadOnly() || !mask.getHandleInputAvailable());
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        getLineEdit().setReadOnly(readOnly || !getEditMaskFilePath().getHandleInputAvailable());
        dialogBtn.setDisabled(readOnly);
        super.setReadOnly(readOnly);
    }
    
    private void storeInitialPath(final String initialPath){
        if (initialPath!=null && !initialPath.isEmpty()){
            lastUsedPath = initialPath;
            if (getEditMaskFilePath().getStoreLastPathInConfig()){
                getEnvironment().getConfigStore().writeString(getActualInitialPathConfigSetting(), initialPath);
            }
        }
        
    }

    public void setInitialPath(final String initialPath) {
        final EditMaskFilePath mask = getEditMaskFilePath();
        if (!Objects.equals(initialPath, mask.getInitialPath())){
            mask.setInitialPath(initialPath);
            setEditMask(mask);
        }        
    }

    public String getInitialPath() {
        return getEditMaskFilePath().getInitialPath();
    }
    
    private EditMaskFilePath getEditMaskFilePath(){
        return (EditMaskFilePath)getEditMask();
    }

    public void showFileNameOnly(final boolean fullPath) {
        this.showFileNameOnly = fullPath;
    }

    public boolean getIsFileNameOnlyShows() {        
        return this.showFileNameOnly;
    }

    public void setCheckIfPathExists(final boolean check) {
        final EditMaskFilePath mask = getEditMaskFilePath();
        if (mask.getCheckIfPathExists()!=check){
            mask.setCheckIfPathExists(check);
            setEditMask(mask);
        }
    }

    public boolean getCheckIfPathExists() {
        return getEditMaskFilePath().getCheckIfPathExists();
    }

    @SuppressWarnings("unused")
    private void onChangeValueClick() {
        final EditMaskFilePath mask = getEditMaskFilePath();        

        final EFileSelectionMode selectionMode = mask.getSelectionMode();
        final boolean checkIfPathExists = mask.getCheckIfPathExists();
        final String fileName;
        final EnumSet<EMimeType> mimeTypes;
        String initialPath = getActualInitialPath();
        if (initialPath==null || initialPath.isEmpty()){
            initialPath = System.getProperty("user.home");            
        } else {
            initialPath = Paths.get(initialPath).normalize().toString();
        }
        
        if (selectionMode == EFileSelectionMode.SELECT_DIRECTORY) {
            mimeTypes = null;
            fileName = QFileDialog.getExistingDirectory(this, getDialogTitle(), initialPath, QFileDialog.Option.ShowDirsOnly);
        } else if (mask.getDialogMode()==EFileDialogOpenMode.LOAD){
            mimeTypes = mask.getMimeTypes();
            final QFileDialog.Filter filter = 
                mimeTypes==null ? null : new QFileDialog.Filter(WidgetUtils.getQtFileDialogFilter(mimeTypes, getEnvironment().getMessageProvider()));
            fileName = QFileDialog.getOpenFileName(this, getDialogTitle(), initialPath, filter);
        } else{
            mimeTypes = mask.getMimeTypes();
            final QFileDialog.Filter filter = 
                mimeTypes==null ? null : new QFileDialog.Filter(WidgetUtils.getQtFileDialogFilter(mimeTypes, getEnvironment().getMessageProvider()));
            fileName = QFileDialog.getSaveFileName(this, getDialogTitle(), initialPath, filter);
        }
        if (fileName != null && !fileName.isEmpty()) {
            final File file = new File(fileName);
            final boolean isFileExists = file.exists();
            if (!checkIfPathExists || isFileExists) {
                if (file.isDirectory()) {
                    storeInitialPath(file.getAbsolutePath());
                } else {
                    storeInitialPath(file.getParent());
                }
                final EMimeType mimeType = mimeTypes==null || mimeTypes.isEmpty() ? null : mimeTypes.iterator().next();                
                if (isFileExists || mimeType==null || mimeType==EMimeType.ALL_FILES || mimeType==EMimeType.ALL_SUPPORTED || fileName.endsWith("."+mimeType.getExt())){
                    setValue(file.getAbsolutePath());
                }else{
                    setValue(file.getAbsolutePath()+"."+mimeType.getExt());
                }
                getLineEdit().end(false);
            }
        }
    }
}