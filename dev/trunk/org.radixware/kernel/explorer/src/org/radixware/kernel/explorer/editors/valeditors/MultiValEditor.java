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

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QStackedWidget;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.text.ITextOptionsProvider;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.types.UnacceptableInput;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;



public class MultiValEditor extends QStackedWidget {

    private enum EValEditorType {

        ARR_BIN,
        ARR_BOOL,
        ARR_CHAR,
        ARR_DATE_TIME,
        ARR_TIME_INTERVAL,
        ARR_INT,
        ARR_NUM,
        ARR_REFERENCE,
        ARR_STR,
        ARR_ENUM,
        ARR_LIST,
        BIN,
        BOOL,
        CHAR,
        ENUM,
        DATE_TIME,
        INT,
        LIST,
        NUM,
        REFERENCE,
        STR,
        TIME_INTERVAL,
        XML
    }

    private static EValEditorType getValEditorType(final EValType valType, final EEditMaskType maskType) {
        if (valType.isArrayType()) {
            final EValType itemType = valType.getArrayItemType();
            if (maskType == null) {//maskType is EditMaskNone 
                switch (itemType) {
                    case BIN:
                    case BLOB:
                        return EValEditorType.ARR_BIN;
                    default:
                        return null;
                }
            } else {
                switch (maskType) {
                    case BOOL: 
                        return EValEditorType.ARR_BOOL;
                    case DATE_TIME:
                        return EValEditorType.ARR_DATE_TIME;
                    case ENUM:
                        return EValEditorType.ARR_ENUM;
                    case INT:
                        return EValEditorType.ARR_INT;
                    case LIST:
                        return EValEditorType.ARR_LIST;
                    case NUM:
                        return EValEditorType.ARR_NUM;
                    case STR:
                        return valType == EValType.ARR_CHAR ? EValEditorType.ARR_CHAR : EValEditorType.ARR_STR;
                    case TIME_INTERVAL:
                        return EValEditorType.ARR_TIME_INTERVAL;
                    case FILE_PATH:
                        return EValEditorType.ARR_STR;
                    case OBJECT_REFERENCE:
                        return EValEditorType.ARR_REFERENCE;
                    default:
                        return null;
                }
            }
        } else if (maskType == null) {
            switch (valType) {
                case BIN:
                case BLOB:
                    return EValEditorType.BIN;
                case XML:
                    return EValEditorType.XML;
                default:
                    return null;
            }
        } else {
            switch (maskType) {
                case BOOL:
                    return EValEditorType.BOOL;
                case DATE_TIME:
                    return EValEditorType.DATE_TIME;
                case ENUM:
                    return EValEditorType.ENUM;
                case INT:
                    return EValEditorType.INT;
                case LIST:
                    return EValEditorType.LIST;
                case NUM:
                    return EValEditorType.NUM;
                case STR:
                    return valType == EValType.CHAR ? EValEditorType.CHAR : EValEditorType.STR;
                case TIME_INTERVAL:
                    return EValEditorType.TIME_INTERVAL;
                case FILE_PATH: 
                    return EValEditorType.STR;
                case OBJECT_REFERENCE:
                    return EValEditorType.REFERENCE;
                default:
                    return null;
            }
        }
    }
    private final Map<EValEditorType, ValEditor> valEditors = new HashMap<>();
    private EValType currentValType;
    private EEditMaskType currentEditMaskType;
    private final IClientEnvironment environment;
    public final com.trolltech.qt.QSignalEmitter.Signal1<ValAsStr> valueChanged = new com.trolltech.qt.QSignalEmitter.Signal1<>();
    private ITextOptionsProvider textOptionsProvider;
    private ExplorerTextOptions customTextOptions;
    private EnumMap<ETextOptionsMarker,ExplorerTextOptions> customTextOptionsByMarker;

    public MultiValEditor(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        this.environment = environment;
        showValEditor(EValType.STR, null, false, false);
    }

    @SuppressWarnings("unchecked")
    public final ValEditor showValEditor(final EValType valType, EditMask editMask, final boolean isMandatory, final boolean isReadonly) {
        if (editMask == null) {
            editMask = EditMask.newInstance(valType);
        }
        if (!editMask.getSupportedValueTypes().contains(valType)) {
            throw new IllegalArgumentException(valType.getName() + " is not supported by " + editMask.getClass().getSimpleName() + " edit mask");
        }
        final EValEditorType valEditorType = getValEditorType(valType, editMask.getType());
        if (valEditorType == null) {
            throw new IllegalArgumentException("Can't create value editor for " + valType.getName() + " type and " + editMask.getClass().getSimpleName() + " edit mask");
        }
        ValEditor editor = valEditors.get(valEditorType);
        if (editor == null) {
            editor = ValEditor.createForValType(environment, valType, editMask, isMandatory, isReadonly, null);
            editor.valueChanged.connect(this, "onValueChanged()");
            valEditors.put(valEditorType, editor);
            addWidget(editor);
        } else {
            editor.setEditMask(editMask);
            editor.setMandatory(isMandatory);
            editor.setReadOnly(isReadonly);
            editor.setValue(null);
        }
        editor.setTextOptionsProvider(textOptionsProvider);
        editor.setDefaultTextOptions(customTextOptions);        
        if (customTextOptionsByMarker!=null){            
            for (Map.Entry<ETextOptionsMarker,ExplorerTextOptions> entry: customTextOptionsByMarker.entrySet()){
                editor.setTextOptionsForMarker(entry.getKey(), entry.getValue());
            }
        }
        setCurrentWidget(editor);
        currentValType = valType;
        currentEditMaskType = editMask.getType();
        return editor;
    }

    @SuppressWarnings("unused")
    private void onValueChanged() {
        if (getCurrentValType() == EValType.PARENT_REF) {
            final ValRefEditor refEditor = (ValRefEditor) getCurrentValEditor();
            final Reference ref = refEditor.getValue();
            final Pid pid = ref == null ? null : ref.getPid();
            valueChanged.emit(pid == null ? null : ValAsStr.Factory.newInstance(pid.toString(), EValType.STR));
        } else {
            valueChanged.emit(getValue());
        }
    }

    public ValEditor getCurrentValEditor() {
        return (ValEditor) currentWidget();
    }

    public EValType getCurrentValType() {
        return currentValType;
    }

    public final boolean isMandatory() {
        return getCurrentValEditor().isMandatory();
    }

    public void setMandatory(final boolean mandatory) {
        getCurrentValEditor().setMandatory(mandatory);
    }

    public final boolean isReadOnly() {
        return getCurrentValEditor().isReadOnly();
    }

    public void setReadOnly(final boolean readOnly) {
        getCurrentValEditor().setReadOnly(readOnly);
    }

    public final EditMask getEditMask() {
        return getCurrentValEditor().getEditMask();
    }

    public void setEditMask(final EditMask editMask) {
        getCurrentValEditor().setEditMask(editMask);
    }

    public QLineEdit getLineEdit() {
        return getCurrentValEditor().getLineEdit();
    }

    public final String getNoValueStr() {
        return getCurrentValEditor().getNoValueStr();
    }

    public ValAsStr getValue() {
        final Object objValue = getCurrentValEditor().getValue();
        if (objValue == null) {
            return null;
        }else{
            return ValueConverter.obj2ValAsStr(objValue, currentValType);
        }
    }

    @SuppressWarnings("unchecked")
    public void setValue(final ValAsStr value) {
        if (value == null) {
            getCurrentValEditor().setValue(null);            
        } else {
            final Object objValue = ValueConverter.valAsStr2Obj(value, currentValType);
            getCurrentValEditor().setValue(objValue);            
        }
    }

    public void refresh() {
        getCurrentValEditor().refresh();
    }

    public final QToolButton addButton(final String text, final QAction action) {
        return getCurrentValEditor().addButton(text, action);
    }

    public final void addButton(final QToolButton button) {
        getCurrentValEditor().addButton(button);
    }

    public QPushButton insertButton(final String buttonText) {
        return getCurrentValEditor().insertButton(buttonText);
    }
    
    public final boolean addTextOptionsMarkers(final ETextOptionsMarker...markers){
        return getCurrentValEditor().addTextOptionsMarkers(markers);
    }
    
    public final boolean removeTextOptionsMarkers(final ETextOptionsMarker...markers){
        return getCurrentValEditor().removeTextOptionsMarkers(markers);
    }
    
    public final boolean setTextOptionsMarkers(final ETextOptionsMarker...markers){
        return getCurrentValEditor().setTextOptionsMarkers(markers);
    }
    
    @SuppressWarnings("unchecked")//invalid java warning on getCurrentValEditor().getTextOptionsMarkers() call
    public final EnumSet<ETextOptionsMarker> getTextOptionsMarkers(){
        return getCurrentValEditor().getTextOptionsMarkers();
    }
    
    public final void setTextOptionsProvider(final ITextOptionsProvider textOptionsProvider){
        this.textOptionsProvider = textOptionsProvider;
        getCurrentValEditor().setTextOptionsProvider(textOptionsProvider);
    }
    
    public final void setDefaultTextOptions(final ExplorerTextOptions options){
        customTextOptions = options;
        getCurrentValEditor().setDefaultTextOptions(options);
    }
    
    public final void setTextOptionsForMarker(final ETextOptionsMarker marker, final ExplorerTextOptions options){
        if (marker!=null){
            if (customTextOptionsByMarker==null && options!=null){
                customTextOptionsByMarker = new EnumMap<>(ETextOptionsMarker.class);
            }
            if (!Objects.equals(options, customTextOptionsByMarker.get(marker))){
                customTextOptionsByMarker.put(marker, options);
            }
        }
        getCurrentValEditor().setTextOptionsForMarker(marker, options);
    }
    
    public final ExplorerTextOptions getTextOptions(){
        return getCurrentValEditor().getTextOptions();
    }    
    
    public final ValidationResult getValidationResult(){
        return getCurrentValEditor().getValidationResult();
    }
    
    public final UnacceptableInput getUnacceptableInput(){
        return getCurrentValEditor().getUnacceptableInput();
    }
        
    public final boolean checkInput(){
        return getCurrentValEditor().checkInput();
    }
    
    public final boolean checkInput(final String title, final String firstMessageLine){
        return getCurrentValEditor().checkInput(title,firstMessageLine);
    }
    
    public final boolean hasAcceptableInput(){
        return getCurrentValEditor().hasAcceptableInput();
    }

    @Override
    public QSize sizeHint() {
        return getCurrentValEditor().sizeHint();
    }

    @Override
    public QSize minimumSizeHint() {
        return getCurrentValEditor().minimumSizeHint();
    }

    @Override
    protected void closeEvent(QCloseEvent closeEvent) {
        for (ValEditor editor : valEditors.values()) {
            editor.close();
        }
    }
}