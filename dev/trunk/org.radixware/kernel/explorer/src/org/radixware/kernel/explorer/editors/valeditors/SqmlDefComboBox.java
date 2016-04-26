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
import java.util.ArrayList;
import java.util.List;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.KeyboardModifier;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QWheelEvent;
import java.util.EnumSet;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.SystemTools;

import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;


public final class SqmlDefComboBox extends ValEditor<ISqmlDefinition> {//Править вместе с ValConstSetEditor

    private final List<ISqmlDefinition> definitions = new ArrayList<>();
    private EDefinitionDisplayMode displayMode = EDefinitionDisplayMode.SHOW_TITLES;
    private String selfStyleSheet;
    private boolean changeFontInternal;
    private final QComboBox combo = new QComboBox(this) {

        @Override
        public void hidePopup() {
            super.hidePopup();
            SqmlDefComboBox.this.setFocus();
        }
        
        @Override
        protected void wheelEvent(final QWheelEvent event) {
            //Do not process this event: RADIX-7110
        }        
    };

    public SqmlDefComboBox(IClientEnvironment environment, final QWidget parent, final Iterable<? extends ISqmlDefinition> definitions, final boolean mandatory, final boolean readOnly, final EDefinitionDisplayMode mode) {
        super(environment, parent, new EditMaskNone(), mandatory, readOnly);

        combo.setObjectName("comboBox");
        combo.setLineEdit(getLineEdit());
        combo.setFrame(false);
        combo.setSizePolicy(Policy.Expanding, Policy.Expanding);
        getLineEdit().setFocusProxy(null);
        combo.setFocusProxy(getLineEdit());
        combo.setContextMenuPolicy(Qt.ContextMenuPolicy.NoContextMenu);
        combo.activatedIndex.connect(this, "onActivatedIndex(int)");

        addWidget(combo);

        getLineEdit().setReadOnly(true);

        for (ISqmlDefinition definition : definitions) {
            this.definitions.add(definition);
        }
        displayMode = mode;

        if (isReadOnly()) {
            setReadOnly(true);
        } else {
            updateComboItems();
        }
    }

    public SqmlDefComboBox(IClientEnvironment environment, final Iterable<? extends ISqmlDefinition> definitions, final QWidget parent) {
        this(environment, parent, definitions, true, false, EDefinitionDisplayMode.SHOW_TITLES);
    }

    private void updateStyleSheet(final boolean forced) {
        final EnumSet<ETextOptionsMarker> markers = getTextOptionsMarkers();
        markers.remove(ETextOptionsMarker.UNDEFINED_VALUE);
        markers.remove(ETextOptionsMarker.MANDATORY_VALUE);
        markers.remove(ETextOptionsMarker.READONLY_VALUE);
        markers.remove(ETextOptionsMarker.OVERRIDDEN_VALUE);
        markers.remove(ETextOptionsMarker.INHERITED_VALUE);
        final ExplorerTextOptions listOptions = calculateTextOptions(markers);        
        final StringBuffer styleSheet = new StringBuffer();
        styleSheet.append("QComboBox {border: 0px;\n");
        {
            styleSheet.append("border: 0px;\n");
            styleSheet.append("padding-top: 0px;\n");
            styleSheet.append("padding-bottom: 0px;\n");
        }
        {
            styleSheet.append("}\n");
            styleSheet.append("QComboBox QAbstractItemView {\n");
            styleSheet.append("border: 2px solid darkgray;\n");
            if (listOptions.getBackground()!=null){
                styleSheet.append("background-color: ");
                styleSheet.append(listOptions.getBackground().name());
                styleSheet.append(";\n");
            }
            if (listOptions.getForeground()!=null){
                styleSheet.append("color: ");
                styleSheet.append(listOptions.getForeground().name());
                styleSheet.append(";\n");
            }
        }
        if (!isPopupEnabled()) {
            styleSheet.append("}\n");
            styleSheet.append("QComboBox::drop-down {\n");
            styleSheet.append("width: 0px\n");
        }
        styleSheet.append("}\n");
        changeSelfStyleSheet(styleSheet.toString(),forced);
    }
    
    private void changeSelfStyleSheet(final String newStyleSheet, final boolean forced){
        if (!Objects.equals(newStyleSheet, selfStyleSheet) || forced){//optimization
            selfStyleSheet = newStyleSheet;
            setStyleSheet(selfStyleSheet);
        }        
    }


    private boolean isPopupEnabled() {
        return !isReadOnly()
                && combo.count() > 0
                && (combo.count() > 1 || getValue() == null || !isMandatory());
    }

    private void updateComboItems() {
        final List<String> names = new ArrayList<>();

        final ISqmlDefinition currentValue = getValue();
        int newIndex = -1;
        for (int i = 0; i < definitions.size(); ++i) {
            final String name = definitions.get(i).getDisplayableText(displayMode);
            final ISqmlDefinition newValue = definitions.get(i);
            if (!isReadOnly()) {
                if (eq(newValue, currentValue)) {
                    newIndex = i;
                }
                names.add(name);
            } else if (eq(newValue, currentValue)) {
                names.add(name);
                newIndex = 0;
                break;
            }
        }
        combo.clear();
        combo.addItems(names);
        if (!isMandatory() && !isReadOnly() && currentValue != null) {
            combo.addItem(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CLEAR), getNoValueStr());
        }
        combo.setCurrentIndex(newIndex);
        if (getValue() == null) {
            getLineEdit().setText(getNoValueStr());
            getLineEdit().clearFocus();
        }
        updateStyleSheet(false);
    }

    @Override
    protected boolean eq(final ISqmlDefinition value1, final ISqmlDefinition value2) {
        return value1 == null ? value2 == null : value2 != null && value1.getId().equals(value2.getId());
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        if (readOnly!=isReadOnly()){
            super.setReadOnly(readOnly);
            updateComboItems();
        }
    }

    @Override
    public void setEditMask(final EditMask editMask) {
        super.setEditMask(editMask);
        updateComboItems();
    }

    @Override
    public void setMandatory(boolean mandatory) {
        super.setMandatory(mandatory);
        updateComboItems();
    }

    public void setDisplayMode(EDefinitionDisplayMode mode) {
        displayMode = mode;
        updateComboItems();
    }

    public EDefinitionDisplayMode getDisplayMode() {
        return displayMode;
    }

    @SuppressWarnings("unused")
    protected void onActivatedIndex(final int index) {
        if (index < 0) {
            return;
        }
        if (index == definitions.size()) {
            setValue(null);
        } else {
            final ISqmlDefinition newValue = definitions.get(index);
            setValue(newValue);
        }
    }

    @Override
    public void setValue(final ISqmlDefinition value) {
        setOnlyValue(value);
        updateComboItems();
        clearBtn.setVisible(!isMandatory() && !isReadOnly() && getValue() != null);
    }

    public void setCurrentDefinitionId(final Id definitionId) {
        if (definitionId == null) {
            setValue(null);
        } else {
            for (ISqmlDefinition definition : definitions) {
                if (definition.getId().equals(definitionId)) {
                    setValue(definition);
                    break;
                }
            }
        }
    }

    @Override
    public boolean eventFilter(final QObject obj, final QEvent event) {
        if (isMandatory()) {
            return super.eventFilter(obj, event);
        }
        if (isReadOnly()) {
            return super.eventFilter(obj, event);
        }
        if (event==null || event.type() != QEvent.Type.KeyPress || !(event instanceof QKeyEvent)) {
            return super.eventFilter(obj, event);
        }
        final QKeyEvent keyEvent = (QKeyEvent) event;
        final boolean isControl = keyEvent.modifiers().isSet(KeyboardModifier.ControlModifier)
                || (keyEvent.modifiers().isSet(KeyboardModifier.MetaModifier) && SystemTools.isOSX);
        final boolean isSpace = keyEvent.key() == Qt.Key.Key_Space.value();
        final boolean isEnter = keyEvent.key() == Qt.Key.Key_Enter.value()
                || keyEvent.key() == Qt.Key.Key_Return.value();

        if (isControl && isSpace) {
            setValue(null);
        }
        //On Enter combobox make activate 0  index. Qt bug?
        return isEnter ? true : super.eventFilter(obj, event);
    }

   @Override
    protected void applyFont(final ExplorerFont newFont) {
        if (newFont!=null){
            final QLineEdit editor = getLineEdit();
            final String styleSheet = editor.styleSheet();
            if (!styleSheet.isEmpty()){
                changeLineEditStyleSheet("");
            }
            editor.setFont(newFont.getQFont());
            combo.setFont(newFont.getQFont());        
            if (!changeFontInternal && !styleSheet.isEmpty()){
                changeLineEditStyleSheet(styleSheet);
            }
        }
    }                

    @Override
    protected void applyTextOptions(final ExplorerTextOptions options) {        
        changeFontInternal = true;
        if (options.getFont()!=null){
            try{
                applyFont(options.getFont());
            }finally{
                changeFontInternal = false;
            }
        }
        if (isEnabled()){
            final StringBuffer styleSheet = new StringBuffer();
            styleSheet.append("QLineEdit {");
            styleSheet.append(options.getStyleSheet());
            styleSheet.append("}\nQAbstractItemView {");
            styleSheet.append(options.getStyleSheet());
            styleSheet.append("}");
            changeLineEditStyleSheet(styleSheet.toString());
        }else{
            changeLineEditStyleSheet("");
        }
        updateStyleSheet(true);
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

    @Override
    protected void onMouseClick() {
        if (isPopupEnabled()) {
            combo.showPopup();
        }
    }
}