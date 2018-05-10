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

package org.radixware.wps.dialogs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EFileDialogOpenMode;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyArr;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrRef;
import org.radixware.kernel.common.client.models.items.properties.SimpleProperty;
import org.radixware.kernel.common.client.views.ArrayEditorEventListener;
import org.radixware.kernel.common.client.views.IArrayEditorDialog;
import org.radixware.kernel.common.client.views.IPropertyStorePossibility;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.arreditor.AbstractArrayEditorDelegate;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.common.enums.EPropertyValueStorePossibility;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.views.editor.array.ArrayEditor;
import org.radixware.wps.views.editors.valeditors.ValEditorController;


public class ArrayEditorDialog extends Dialog implements IArrayEditorDialog {

    private final ArrayEditor editor;
    private final BeforeCloseButtonListener closeListener = new BeforeCloseButtonListener() {
        @Override
        public boolean beforeClose(EDialogButtonType button, DialogResult result) {
            editor.finishEdit();
            if (result != DialogResult.ACCEPTED
                    || (editor.checkForDuplicates() && editor.checkValues(false))) {
                editor.removeEventListener(arrayListener);
                editor.removeStartCellModificationListener(startModificationListener);
                return true;
            }
            return false;
        }
    };
    private final ArrayEditorEventListener arrayListener = new ArrayEditorEventListener() {
        @Override
        public void onRowsRemoved(int startRow, int count) {
            refreshAcceptButton();
        }

        @Override
        public void onRowsInserted(int starRow, int count) {
            refreshAcceptButton();
        }

        @Override
        public void onDefineValue() {
            refreshAcceptButton();
        }

        @Override
        public void onUndefineValue() {
            refreshAcceptButton();
        }

        @Override
        public void onCellChanged(final int row, final Object newValue) {
            refreshAcceptButton();
        }
    };
    private final ArrayEditor.StartCellModificationListener startModificationListener = new ArrayEditor.StartCellModificationListener() {
        @Override
        public void onStartModification(final int row) {
            final List<Integer> rows = editor.getRowsWithInvalidValues();
            rows.remove(Integer.valueOf(row));
            setActionEnabled(EDialogButtonType.OK, rows.isEmpty());
        }
    };

    private class PropertyValueStorePossibility implements IPropertyStorePossibility {

        private final PropertyArr property;

        public PropertyValueStorePossibility(final PropertyArr prop) {
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
        public void writePropertyValue(final Object value) {
            FileOutputStream output = null;
            File f = null;
            try {
                File dir = createTempDir();
                String fileName = property.getTitle().replaceAll("[/</>*?|'\":]", "");//exclude windows file name prohibited chars
                if (dir != null && dir.isDirectory() && dir.exists()) {
                    f = new File(dir, fileName);
                    f.createNewFile();
                    output = new FileOutputStream(f);
                    property.saveItemToStream(output, value, getCurrentItemIndex());
                    if (f.exists() && f.isFile()) {
                        final EnumSet<EMimeType> types = property.getFileDialogSettings(EFileDialogOpenMode.LOAD).getMimeTypes();
                        final String mimeTypes;
                        if (types==null || types.isEmpty()){
                            mimeTypes = null;
                        }else{
                            final StringBuilder typesBuilder = new StringBuilder();
                            for (EMimeType type: types){
                                if (type!=EMimeType.ALL_FILES && type!=EMimeType.ALL_SUPPORTED){
                                    if (typesBuilder.length()>0){
                                        typesBuilder.append(',');                                    
                                    }
                                    typesBuilder.append(type.getValue());
                                }
                            }
                            mimeTypes = typesBuilder.toString();
                        }
                        ((WpsEnvironment) getEnvironment()).sendFileToTerminal(f.getName(), f, mimeTypes, false, false);//send file to terminal
                    } else {
                        throw new FileNotFoundException("Could not create file for property value storing.");
                    }
                } else {
                    throw new FileNotFoundException("Could not create temporary directory for property value storing.");
                }
            } catch (FileNotFoundException ex) {
                final String mess = String.format("File not found \n%s", f);
                getEnvironment().processException(mess, ex);
            } catch (IOException ex) {
                final String mess = String.format("Failed to save value of property %s to a file\n%s", property, f);
                getEnvironment().processException(mess, ex);
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException ex) {
                        getEnvironment().getTracer().error(ex);
                    }
                }
            }
        }

        @Override
        public Object readPropertyValue(final InputStream stream) {
            try {
                return property.loadItemFromStream(stream, getCurrentItemIndex());
            } catch (IOException ex) {
                getEnvironment().processException(ex);
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException ex) {
                        getEnvironment().getTracer().error(ex);
                    }
                }
            }
            return null;
        }

        private File createTempDir() {
            final File tmpDir = RadixLoader.getInstance().createTempFile("ArrayPropertyValueStore");
            return tmpDir.mkdir() ? tmpDir : null;
        }
    }

    public ArrayEditorDialog(final IClientEnvironment environment, final EValType valType, final Class<?> valClass, final boolean readonly, final IDialogDisplayer displayer) {
        super(displayer, "");
        editor = new ArrayEditor(environment, valType, valClass);
        setupUi(readonly);
    }

    public ArrayEditorDialog(final IClientEnvironment environment, final RadSelectorPresentationDef presentation,
            final boolean readonly, final IDialogDisplayer displayer) {
        super(displayer, "");
        editor = new ArrayEditor(environment, presentation);
        setupUi(readonly);
    }

    public ArrayEditorDialog(final Property property, final IDialogDisplayer displayer) {
        super(displayer, "");
        final boolean isReadOnly;

        if (property instanceof PropertyArrRef) {
            final PropertyArrRef propertyArrRef = (PropertyArrRef) property;
            editor = new ArrayEditor(propertyArrRef);
            isReadOnly = propertyArrRef.isReadonly() || !propertyArrRef.canOpenParentSelector();
            if (!property.isOwnValueAcceptable(null)) {
                editor.setMandatory(true);
            }
        } else {
            final IClientEnvironment environment = property.getEnvironment();
            editor = new ArrayEditor(environment, property.getDefinition().getType(), property.getValClass());
            editor.setEditMask(property.getEditMask());
            editor.setDuplicatesEnabled(property.getDefinition().isDuplicatesEnabled());
            editor.setMandatory(property.isMandatory() || !property.isOwnValueAcceptable(null));
            if (property instanceof PropertyArr) {
                final PropertyArr arrayProperty = (PropertyArr) property;
                editor.setItemMandatory(arrayProperty.isArrayItemMandatory());
                editor.setFirstArrayItemIndex(arrayProperty.getFirstArrayItemIndex());
                editor.setMaxArrayItemsCount(arrayProperty.getMaxArrayItemsCount());
                editor.setMinArrayItemsCount(arrayProperty.getMinArrayItemsCount());
            }
            isReadOnly = property.isReadonly();
        }
        if (property instanceof SimpleProperty) {
            editor.setPropertyStorePossibility(new PropertyValueStorePossibility((PropertyArr) property));
        }
        setupUi(isReadOnly);
    }

    private void setupUi(final boolean readonly) {
        setWindowTitle(getEnvironment().getMessageProvider().translate("ArrayEditor", "Array Editor"));
        add(editor);
        editor.getAnchors().setLeft(new Anchors.Anchor(0, 5));
        editor.getAnchors().setRight(new Anchors.Anchor(1, -5));
        editor.getAnchors().setTop(new Anchors.Anchor(0, 5));
        editor.getAnchors().setBottom(new Anchors.Anchor(1, -5));
        editor.setReadOnly(readonly);
        editor.addEventListener(arrayListener);
        editor.addStartCellModificationListener(startModificationListener);
        editor.setFocused(true);
        updateDialogButtons(readonly);
        setMinimumWidth(315);
        setMinimumHeight(275);
        editor.getHtml().setCss("min-width", null);
        editor.getHtml().setCss("min-height", null);
        refreshAcceptButton();
    }

    private void updateDialogButtons(final boolean readonly) {
        clearCloseActions();
        if (readonly) {
            addCloseAction(EDialogButtonType.CLOSE).setDefault(true);
        } else {
            addCloseAction(EDialogButtonType.OK).setDefault(true);
            addCloseAction(EDialogButtonType.CANCEL);
            addBeforeCloseListener(closeListener);
        }
    }

    @Override
    public void setCurrentValue(Arr arr) {
        editor.setCurrentValue(arr);
    }

    @Override
    public Arr getCurrentValue() {
        return editor.getCurrentValue();
    }        

    @Override
    public boolean isReadonly() {
        return editor.isReadOnly();
    }

    @Override
    public void setReadonly(boolean readOnly) {
        editor.setReadOnly(readOnly);
        updateDialogButtons(readOnly);
    }

    @Override
    public void setEditorReadonly(boolean readonly) {
        editor.setReadOnly(readonly);
    }

    public void setEditMask(EditMask editMask) {
        editor.setEditMask(editMask);
    }

    public EditMask getEditMask() {
        return editor.getEditMask();
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

    @Override
    public void setItemsMovable(final boolean isItemsMovable) {
        editor.setItemsMovable(isItemsMovable);
    }

    @Override
    public boolean isItemsMovable() {
        return editor.isItemsMovable();
    }

    @Override
    public void setFirstArrayItemIndex(int index) {
        editor.setFirstArrayItemIndex(index);
    }

    @Override
    public int getFirstArrayItemIndex() {
        return editor.getFirstArrayItemIndex();
    }

    @Override
    public void setMinArrayItemsCount(int count) {
        editor.setMinArrayItemsCount(count);
    }

    @Override
    public int getMinArrayItemsCount() {
        return editor.getMinArrayItemsCount();
    }

    @Override
    public void setMaxArrayItemsCount(int count) {
        editor.setMaxArrayItemsCount(count);
    }

    @Override
    public int getMaxArrayItemsCount() {
        return editor.getMaxArrayItemsCount();
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

    private void refreshAcceptButton() {
        setActionEnabled(EDialogButtonType.OK, editor.checkValues(true));
    }

    @Override
    public void setPredefinedValues(final List<Object> values) {
        editor.setPredefinedValues(values);
    }

    @Override
    public void addCustomAction(final Action action) {
        editor.addCustomAction(action);
    }

    @Override
    public void removeCustomAction(final Action action) {
        editor.removeCustomAction(action);
    }        

    @Override
    public void addEventListener(final ArrayEditorEventListener listener) {
        editor.addEventListener(listener);
    }

    @Override
    public void removeEventListener(final ArrayEditorEventListener listener) {
        editor.removeEventListener(listener);
    }

    public void addStartCellModificationListener(final ArrayEditor.StartCellModificationListener listener) {
        editor.addStartCellModificationListener(listener);
    }

    public void removeStartCellModificationListener(final ArrayEditor.StartCellModificationListener listener) {
        editor.removeStartCellModificationListener(listener);
    }

    @Override
    public int getCurrentItemIndex() {
        return editor.getCurrentIndex();
    }

    @Override
    public void setCurrentItemIndex(final int index) {
        editor.setCurrentIndex(index);
    }        
    
    public AbstractArrayEditorDelegate<ValEditorController,UIObject> getEditorDelegate(){
        return editor.getEditorDelegate();
    }
    
    public void setEditorDelegate(final AbstractArrayEditorDelegate<ValEditorController,UIObject> delegate){
        editor.setEditorDelegate(delegate);
    }    
}