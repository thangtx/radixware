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

import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QFileDialog.Filter;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskFilePath;
import org.radixware.kernel.common.client.meta.mask.validators.IInputValidator;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.enums.EFileSelectionMode;
import org.radixware.kernel.common.enums.EMimeType;
import static org.radixware.kernel.explorer.editors.valeditors.ValEditor.createAction;

import org.radixware.kernel.explorer.env.ExplorerIcon;

public final class ValFilePathEditor extends ValEditor<String> {
    
    private static class InputValidator implements IInputValidator{
        
        private final EFileSelectionMode selectionMode;
        private final boolean checkIfPathExists;
        
        public InputValidator(final EFileSelectionMode mode, final boolean checkIfExists){
            selectionMode = mode;
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
            if (checkIfPathExists) {
                if (selectionMode == EFileSelectionMode.SELECT_DIRECTORY) {
                    final Path path = Paths.get(input);
                    if (path.toFile().exists() && path.toFile().isDirectory()) {
                        return ValidationResult.ACCEPTABLE;
                    }
                } else {
                    //if path contains Windows forbidden file name symbols mark value as invalid
                    if (input.contains("[<\">?*\']")) {
                        return ValidationResult.INVALID;
                    }
                    try {
                        final Path path = Paths.get(input);
                        if (path.toFile().exists() && path.toFile().isFile()) {
                            return ValidationResult.ACCEPTABLE;
                        }
                    } catch (InvalidPathException e) {
                        return ValidationResult.INVALID;
                    }
                }
            } else {
                return ValidationResult.ACCEPTABLE;
            }
            return ValidationResult.INVALID;            
        }
        
    }

    private String initPath;
    private boolean handleInput;
    private String dialogTitle;    
    private EFileSelectionMode selectionMode;
    private final QToolButton dialogBtn = addButton(null, createAction(this, "onChangeValueClick()"));
    private boolean showFileNameOnly;
    private boolean checkIfPathExists;

    public ValFilePathEditor(final IClientEnvironment env, final QWidget parent, final EditMaskFilePath editMask, final boolean mandatory, final boolean readOnly) {
        super(env, parent, editMask, mandatory, readOnly);
        if (editMask == null) {
            setEditMask(new EditMaskFilePath());
        }
        initPath = ""; //get from config 
        afterUpdateEditMask();
        dialogBtn.setToolTip(env.getMessageProvider().translate("ValFilePathEditor", "Edit"));
        dialogBtn.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.OPEN));
        dialogBtn.setDisabled(readOnly);       
    }

    public ValFilePathEditor(final IClientEnvironment env, final QWidget parent) {
        this(env, parent, new EditMaskFilePath(), true, false);
    }

    @Override
    protected void onFocusIn() {
        if (!isReadOnly()) {
            final String val = this.getValue();
            final EditMaskFilePath mask = (EditMaskFilePath) getEditMask();
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
        getLineEdit().setReadOnly(!((EditMaskFilePath) editMask).getHandleInputAvailable() || isReadOnly());
        afterUpdateEditMask();        
    }
    
    private void afterUpdateEditMask(){
        final EditMaskFilePath mask = (EditMaskFilePath) getEditMask();
        handleInput = mask.getHandleInputAvailable();
        dialogTitle = mask.getFileDialogTitleAsStr() != null ? mask.getFileDialogTitleAsStr() : "File";
        selectionMode = mask.getSelectionMode();

        checkIfPathExists = mask.getCheckIfPathExists();
        setInputValidator(new InputValidator(selectionMode, checkIfPathExists));
        getLineEdit().setReadOnly(isReadOnly() || !handleInput);
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        getLineEdit().setReadOnly(readOnly || !handleInput);
        dialogBtn.setDisabled(readOnly);
        super.setReadOnly(readOnly);
    }

    public void setDialogTitle(final String newTitle) {
        dialogTitle = newTitle;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    private String buildFileFilter(final EMimeType type) {
        if (type==null){
            return null;
        }
        final StringBuilder filterBuilder = new StringBuilder(type.getTitle());
        filterBuilder.append("  (*.");
        filterBuilder.append(type.getExt());
        filterBuilder.append(')');
        return filterBuilder.toString();
    }

    public void setInitialPath(final String initialPath) {
        if (initialPath == null || initialPath.isEmpty()) {
            initPath = System.getProperty("user.home");            
        } else {
            initPath = Paths.get(initialPath).normalize().toString();
        }
    }

    public String getInitialPath() {
        return initPath;
    }

    public void showFileNameOnly(final boolean fullPath) {
        this.showFileNameOnly = fullPath;
    }

    public boolean getIsFileNameOnlyShows() {
        return this.showFileNameOnly;
    }

    public void setCheckIfPathExists(final boolean check) {
        this.checkIfPathExists = check;
    }

    public boolean getCheckIfPathExists() {
        return checkIfPathExists;
    }

    @SuppressWarnings("unused")
    private void onChangeValueClick() {
        final EditMaskFilePath mask = (EditMaskFilePath) getEditMask();
        final EMimeType mimeType;        

        selectionMode = mask.getSelectionMode();
        final String fileName;
        if (selectionMode == EFileSelectionMode.SELECT_DIRECTORY) {
            mimeType = null;
            fileName = QFileDialog.getExistingDirectory(this, getDialogTitle(), getInitialPath(), QFileDialog.Option.ShowDirsOnly);
        } else if (checkIfPathExists){
            mimeType = mask.getMimeType();
            final QFileDialog.Filter filter = mimeType==null ? null : new QFileDialog.Filter(buildFileFilter(mimeType));
            fileName = QFileDialog.getOpenFileName(this, getDialogTitle(), getInitialPath(), filter);
        } else{
            mimeType = mask.getMimeType();
            final QFileDialog.Filter filter = mimeType==null ? null : new QFileDialog.Filter(buildFileFilter(mimeType));
            fileName = QFileDialog.getSaveFileName(this, getDialogTitle(), getInitialPath(), filter);
        }
        if (fileName != null && !fileName.isEmpty()) {
            final File file = new File(fileName);
            final boolean isFileExists = file.exists();
            if (!checkIfPathExists || isFileExists) {                
                if (file.isDirectory()) {
                    setInitialPath(file.getAbsolutePath());
                } else {
                    setInitialPath(file.getParent());
                }
                if (isFileExists || mimeType==null || fileName.endsWith("."+mimeType.getExt())){
                    setValue(file.getAbsolutePath());
                }else{
                    setValue(file.getAbsolutePath()+"."+mimeType.getExt());
                }
                getLineEdit().end(false);
            }
        }
    }
}
