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

import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QWidget;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IFileDialogSettings;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyArr;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrRef;
import org.radixware.kernel.common.client.models.items.properties.SimpleProperty;
import org.radixware.kernel.common.client.views.IArrayEditorDialog;
import org.radixware.kernel.common.client.views.ArrayEditorEventListener;
import org.radixware.kernel.common.client.views.IPropertyStorePossibility;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.common.enums.EPropertyValueStorePossibility;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.explorer.editors.AbstractArrayEditor;
import org.radixware.kernel.explorer.editors.ArrayEditor;

public final class ArrayEditorDialog extends ExplorerDialog implements IArrayEditorDialog {

    final private ArrayEditor editor;
    private List<ArrayEditorEventListener> listeners;
    public final Signal2<Integer, Integer> cellDoubleClicked = new Signal2<>();

    private class PropertyStorePossiblity implements IPropertyStorePossibility {

        PropertyArr property;

        public PropertyStorePossiblity(PropertyArr prop) {
            this.property = prop;
        }

        @Override
        public boolean canPropertySaveValue() {
            return (property != null
                    && (property.getPropertyValueStorePossibility() == EPropertyValueStorePossibility.FILE_SAVE
                    || property.getPropertyValueStorePossibility() == EPropertyValueStorePossibility.FILE_SAVE_AND_LOAD));
        }

        @Override
        public boolean canPropertyReadValue() {

            return (property != null
                    && (property.getPropertyValueStorePossibility() == EPropertyValueStorePossibility.FILE_LOAD
                    || property.getPropertyValueStorePossibility() == EPropertyValueStorePossibility.FILE_SAVE_AND_LOAD));
        }

        @Override
        @SuppressWarnings("unchecked")
        public void writePropertyValue(Object value) {
            if (property == null) {
                return;
            }
            IFileDialogSettings settings = property.getFileDialogSettings(IFileDialogSettings.EFileDialogOpenMode.SAVE);
            FileOutputStream output = null;

            EMimeType type = settings.getMimeType();
            String mimeType = "";
            String strAllFiles=getEnvironment().getMessageProvider().translate("PropertyEditor", "All Files");
            if (type != null) {
                mimeType = type.getName().toUpperCase() + "  (*." + type.getExt() + ");; " + strAllFiles + "(*.*)";
            }
            QFileDialog dialog = new QFileDialog(null, settings.getFileDialogTitle(), settings.getInitialPath(), mimeType);
            dialog.setFileMode(QFileDialog.FileMode.AnyFile);
            getEnvironment().getProgressHandleManager().blockProgress();
            dialog.setAcceptMode(QFileDialog.AcceptMode.AcceptSave);
            String fileName = "";
            try {
                int dialogExec = dialog.exec();
                if (dialogExec == QDialog.DialogCode.Accepted.value()) {
                    fileName = dialog.selectedFiles().get(0);
                }
                if (fileName == null || fileName.isEmpty()) {
                    return;
                }
                File f = new File(fileName);
                try {
                    f.createNewFile();
                } catch (IOException ex) {
                    String mess = String.format("Failed to create new file \n%s", f);
                    getEnvironment().processException(mess, ex);
                }
                if (f.exists() && f.isFile()) {
                    output = new FileOutputStream(f);
                    Object val = property.getValueObject();
                    property.saveToStream(output, val);
                }
            } catch (FileNotFoundException ex) {
                String mess = String.format("Failed to save value of property %s to a file\n%s", property, fileName);
                getEnvironment().processException(mess, ex);
            } catch (IOException ex) {
                getEnvironment().processException(ex);
            } finally {
                try {
                    getEnvironment().getProgressHandleManager().unblockProgress();
                    if (output != null) {
                        output.close();
                    }
                } catch (IOException ex) {
                    getEnvironment().getTracer().error(ex);
                }
            }
        }

        @Override
        public Object readPropertyValue(InputStream stream) {            
            if (property == null) {
                return null;
            }
            final Object  currentValue = property.getValueObject();
            IFileDialogSettings settings = property.getFileDialogSettings(IFileDialogSettings.EFileDialogOpenMode.LOAD);
            EMimeType type = settings.getMimeType();
            String mimeType = "";
            FileInputStream input = null;
            if (type != null) {
                mimeType = type.getName().toLowerCase() + "  (*." + type.getExt() + ")";
            }
            QFileDialog dialog = new QFileDialog(null, settings.getFileDialogTitle(), settings.getInitialPath(), mimeType);
            dialog.setFileMode(QFileDialog.FileMode.ExistingFile);
            dialog.setAcceptMode(QFileDialog.AcceptMode.AcceptOpen);
            getEnvironment().getProgressHandleManager().blockProgress();
            String fileName = "";
            try {

                if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
                    fileName = dialog.selectedFiles().get(0);
                } else {
                    return currentValue;
                }
                if (fileName == null || fileName.isEmpty()) {
                    throw new FileNotFoundException("Invalid file name " + fileName);
                }                
                Path fp = Paths.get(fileName);
                if (fp == null || !fp.toFile().exists()) {
                    throw new FileNotFoundException();
                }
                File f = new File(fileName);
                if (f.canRead()) {
                    input = new FileInputStream(f);
                    return property.loadItemFromStream(input, getCurrentItemIndex());
                }
            } catch (FileNotFoundException ex) {
                String mess = String.format("Failed to load value of property %s from file\n%s", property, fileName);
                getEnvironment().processException(mess, ex);
            } catch (IOException ex) {
                getEnvironment().processException(ex);
            } finally {
                try {
                    getEnvironment().getProgressHandleManager().unblockProgress();
                    if (input != null) {
                        input.close();
                    }
                } catch (IOException ex) {
                    getEnvironment().getTracer().error(ex);
                }
            }
            return currentValue;
        }
    }

    public ArrayEditorDialog(final IClientEnvironment environment, final EValType valType, final Class<?> valClass, final boolean readonly, QWidget parent) {
        super(environment, parent, null);
        editor = new ArrayEditor(getEnvironment(), valType, valClass, this);
        editor.setReadonly(readonly);
        setupUi();
    }

    public ArrayEditorDialog(final IClientEnvironment environment, final RadSelectorPresentationDef presentation,
            final boolean readonly,
            QWidget parent) {
        super(environment, parent, null);
        editor = new ArrayEditor(getEnvironment(), presentation, this);
        editor.setReadonly(readonly);
        setupUi();
    }

    public ArrayEditorDialog(Property property, QWidget parent) {
        super(property.getEnvironment(), parent, null);
        if (property instanceof PropertyArrRef) {
            final PropertyArrRef propertyArrRef = (PropertyArrRef) property;
            editor = new ArrayEditor(propertyArrRef, this);
            if (!propertyArrRef.canOpenParentSelector()) {
                editor.setReadonly(true);
            }
            if (!property.isOwnValueAcceptable(null)) {
                editor.setMandatory(true);
            }
        } else {
            editor = new ArrayEditor(getEnvironment(), property.getDefinition().getType(), property.getValClass(), this);
            editor.setReadonly(property.isReadonly());
            editor.setEditMask(property.getEditMask());
            editor.setDuplicatesEnabled(property.getDefinition().isDuplicatesEnabled());
            editor.setMandatory(property.isMandatory() || !property.isOwnValueAcceptable(null));
            if (property instanceof PropertyArr) {
                PropertyArr arrayProperty = (PropertyArr) property;
                editor.setItemMandatory(arrayProperty.isArrayItemMandatory());
                editor.setFirstArrayItemIndex(arrayProperty.getFirstArrayItemIndex());
                editor.setMinArrayItemsCount(arrayProperty.getMinArrayItemsCount());
                editor.setMaxArrayItemsCount(arrayProperty.getMaxArrayItemsCount());
            }
        }
        if (property instanceof SimpleProperty) {
            editor.setPropertyStorePossibility(new PropertyStorePossiblity((PropertyArr) property));
        }        
        setupUi();
    }

    public void setEditMask(EditMask editMask) {
        editor.setEditMask(editMask);
    }

    public EditMask getEditMask() {
        return editor.getEditMask();
    }

    @Override
    public void setCurrentValue(Arr value) {
        editor.setCurrentValue(value);
        final IPushButton buttonOk = getButton(EDialogButtonType.OK);
        if (buttonOk != null) {
            buttonOk.setEnabled(editor.validateCurrentValue() == ValidationResult.ACCEPTABLE);
        }
    }

    @Override
    public Arr getCurrentValue() {
        return editor.getCurrentValue();
    }

    public int getCurrentItemIndex() {
        return editor.getCurrentIndex();
    }

    @Override
    public void setMandatory(final boolean mandatory) {
        editor.setMandatory(mandatory);
    }

    @Override
    public void setItemMandatory(final boolean mandatory) {
        editor.setItemMandatory(mandatory);
    }

    @Override
    public void setReadonly(final boolean readonly) {
        editor.setReadonly(readonly);
        clearButtons();
        if (readonly) {
            addButton(EDialogButtonType.CLOSE).setDefault(true);
        } else {
            addButton(EDialogButtonType.OK).setDefault(true);            
            addButton(EDialogButtonType.CANCEL);
            revalidateSize();
        }
    }

    @Override
    public boolean isReadonly() {
        return editor.isReadonly();
    }

    @Override
    public void setEditorReadonly(final boolean readonly) {
        editor.setReadonly(readonly);
    }

    @Override
    public boolean isMandatory() {
        return editor.isMandatory();
    }

    @Override
    public boolean isItemMandatory() {
        return editor.isItemMandatory();
    }

    @Override
    public void setDuplicatesEnabled(final boolean duplicates) {
        editor.setDuplicatesEnabled(duplicates);
    }

    @Override
    public boolean isDuplicatesEnabled() {
        return editor.isDuplicatesEnabled();
    }

    public void setItemMoveMode(final AbstractArrayEditor.EItemMoveMode mode) {
        editor.setItemMoveMode(mode);
    }

    @Override
    public void setItemsMovable(boolean isMovable) {
        editor.setItemMoveMode(isMovable ? AbstractArrayEditor.EItemMoveMode.DRAG_DROP : AbstractArrayEditor.EItemMoveMode.NOT_MOVABLE);
    }

    public AbstractArrayEditor.EItemMoveMode getItemMoveMode() {
        return editor.getItemMoveMode();
    }

    @Override
    public boolean isItemsMovable() {
        return editor.getItemMoveMode() != AbstractArrayEditor.EItemMoveMode.NOT_MOVABLE;
    }

    @Override
    public void setOperationsVisible(final boolean isVisible) {
        editor.setOperationsVisible(isVisible);
    }

    @Override
    public boolean isEmptyArray() {
        return editor.isEmpty();
    }

    @Override
    public Object getSelectedValue() {
        return editor.getSelectedValue();
    }

    private void setupUi() {
        setWindowTitle(getEnvironment().getMessageProvider().translate("ArrayEditor", "Array Editor"));
        layout().addWidget(editor);
        if (editor.isReadonly()) {
            addButton(EDialogButtonType.CLOSE).setDefault(true);
            rejectButtonClick.connect(this, "reject()");
        } else {
            addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
        }
        editor.cellDoubleClicked.connect(cellDoubleClicked);
        editor.cellDoubleClicked.connect(this, "notifyCellDoubleClick(Integer)");
        editor.rowsRemoved.connect(this, "onRowsRemoved(Integer,Integer)");
        editor.rowsInserted.connect(this, "onRowsInserted(Integer,Integer)");
        editor.valueUndefined.connect(this, "notifyValueIsUndefined()");
        editor.valueDefined.connect(this, "onValueIsDefined()");
        editor.rowEdited.connect(this, "onRowEdited(Integer,Object)");

    }

    @SuppressWarnings("unused")
    private boolean revalidateSize() {
        final boolean isValid = editor.validateCurrentValueSize() == ValidationResult.ACCEPTABLE;
        getButton(EDialogButtonType.OK).setEnabled(isValid);
        return isValid;
    }

    @Override
    public void done(int result) {
        if (result != QDialog.DialogCode.Accepted.value() 
            || (editor.checkItems() && editor.checkForDuplicates())){
            super.done(result);
        }     
    }

    @Override
    public void setPredefinedValues(final List<Object> values) {
        editor.setPredefinedValues(values);
    }

    //listeners
    @Override
    public void addEventListener(final ArrayEditorEventListener listener) {
        if (listeners == null) {
            listeners = new LinkedList<>();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeEventListener(final ArrayEditorEventListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    @SuppressWarnings("unused")
    private void notifyCellDoubleClick(final Integer row) {
        if (listeners != null) {
            for (ArrayEditorEventListener listener : listeners) {
                listener.onCellDoubleClick(row);
            }
        }
    }

    @SuppressWarnings("unused")
    private void onRowsRemoved(final Integer startRow, final Integer count) {
        revalidateSize();            
        if (listeners != null) {
            for (ArrayEditorEventListener listener : listeners) {
                listener.onRowsRemoved(startRow, count);
            }
        }
    }

    @SuppressWarnings("unused")
    private void onRowsInserted(final Integer startRow, final Integer count) {
        revalidateSize();
        if (listeners != null) {
            for (ArrayEditorEventListener listener : listeners) {
                listener.onRowsInserted(startRow, count);
            }
        }
    }

    @SuppressWarnings("unused")
    private void notifyValueIsUndefined() {
        if (listeners != null) {
            for (ArrayEditorEventListener listener : listeners) {
                listener.onUndefineValue();
            }
        }
    }

    @SuppressWarnings("unused")
    private void onValueIsDefined() {
        revalidateSize();//check for minimum items count
        if (listeners != null) {
            for (ArrayEditorEventListener listener : listeners) {
                listener.onDefineValue();
            }
        }
    }

    @SuppressWarnings("unused")
    private void onRowEdited(final Integer row, final Object newValue) {
        if (listeners != null) {
            for (ArrayEditorEventListener listener : listeners) {
                listener.onCellChanged(row, newValue);
            }
        }
    }
}