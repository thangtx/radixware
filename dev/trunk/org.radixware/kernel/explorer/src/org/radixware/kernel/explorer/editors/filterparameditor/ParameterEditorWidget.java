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

package org.radixware.kernel.explorer.editors.filterparameditor;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;


public final class ParameterEditorWidget extends ExplorerWidget {

    private final boolean readonly;
    private final ISqmlTableDef contextTable;
    private final QGridLayout gridLayout = new QGridLayout(this);
    private final Map<EFilterParamAttribute, AbstractAttributeEditor> attributeEditors = new LinkedHashMap<>(10);

    public ParameterEditorWidget(IClientEnvironment environment, final ISqmlTableDef context, final boolean isReadonly, final QWidget parent) {
        super(environment, parent);
        readonly = isReadonly;
        contextTable = context;
        gridLayout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignTop));
        gridLayout.setVerticalSpacing(4);
        gridLayout.setHorizontalSpacing(4);
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void open(final ISqmlParameter parameter, final List<String> restrictedNames) {
        final List<EFilterParamAttribute> attributes = new ArrayList<>();
        attributes.add(EFilterParamAttribute.NAME);

        if (parameter.getBasePropertyId() == null) {
            attributes.add(EFilterParamAttribute.VALUE_TYPE);
            attributes.add(EFilterParamAttribute.ENUM);
            attributes.add(EFilterParamAttribute.EDIT_MASK);
            attributes.add(EFilterParamAttribute.IS_MANDATORY);
            attributes.add(EFilterParamAttribute.NULL_TITLE);
            attributes.add(EFilterParamAttribute.SELECTOR_PRESENTATION);
        } else {
            attributes.add(EFilterParamAttribute.PROPERTY);
            attributes.add(EFilterParamAttribute.IS_MANDATORY);
        }
        attributes.add(EFilterParamAttribute.ADDITIONAL_SELECTOR_CONDITION);
        attributes.add(EFilterParamAttribute.PARENT_REF_EDITING_MODE);
        if (parameter.getType() != EValType.PARENT_REF && parameter.getType() != EValType.ARR_REF) {
            attributes.add(EFilterParamAttribute.DEFAULT_VALUE);
        } else {
            if (attributes.contains(EFilterParamAttribute.EDIT_MASK)) {
                attributes.remove(EFilterParamAttribute.EDIT_MASK);
            }
        }

        if (parameter.getType() == EValType.BOOL || parameter.getType() == EValType.ARR_BOOL) {
            if (attributes.contains(EFilterParamAttribute.EDIT_MASK)) {
                attributes.remove(EFilterParamAttribute.EDIT_MASK);
            }
        }

        if (parameter.canHavePersistentValue()) {
            attributes.add(EFilterParamAttribute.PERSISTENT_VALUE_DEFINED);
            attributes.add(EFilterParamAttribute.PERSISTENT_VALUE);
        }
        open(attributes, EnumSet.of(EFilterParamAttribute.PROPERTY), restrictedNames);
        readAttributes(parameter);
    }

    private void open(final List<EFilterParamAttribute> attributes, final EnumSet<EFilterParamAttribute> readonlyAttributes, final List<String> restrictedNames) {
        initAttributeEditors(attributes, readonlyAttributes, restrictedNames);
    }

    void open(final List<EFilterParamAttribute> attributes, final List<String> restrictedNames) {
        initAttributeEditors(attributes, EnumSet.noneOf(EFilterParamAttribute.class), restrictedNames);
    }

    public void readAttributes(final ISqmlParameter parameter) {
        for (AbstractAttributeEditor editor : attributeEditors.values()) {
            editor.updateEditor(parameter);
        }
    }

    public boolean writeAttributes(final ISqmlParameter parameter) {
        boolean canEdit;
        for (AbstractAttributeEditor editor : attributeEditors.values()) {
            canEdit = editor.getAttribute() == EFilterParamAttribute.PERSISTENT_VALUE;
            if ((!isReadonly() || canEdit) && !editor.updateParameter(parameter)) {
                return false;
            }
        }
        return true;
    }

    public void clear() {
        for (AbstractAttributeEditor editor : attributeEditors.values()) {
            gridLayout.removeWidget(editor.getEditorWidget());
            editor.getEditorWidget().setVisible(false);
            editor.getEditorWidget().setParent(null);
            if (editor.getLabel() != null) {
                gridLayout.removeWidget(editor.getLabel());
                editor.getLabel().setVisible(false);
                editor.getLabel().setParent(null);
            }
            editor.free();
        }
        attributeEditors.clear();
    }

    @SuppressWarnings("unchecked")
    private void initAttributeEditors(final List<EFilterParamAttribute> attributes, final EnumSet<EFilterParamAttribute> readonlyAttributes, final List<String> restrictedNames) {
        {//create editors
            QWidget editor = null, previousEditor = null;
            AbstractAttributeEditor attrEditor;
            int row = 0;
            for (EFilterParamAttribute attribute : attributes) {
                attrEditor =
                        AbstractAttributeEditor.Factory.newInstance(getEnvironment(), attribute, readonly || readonlyAttributes.contains(attribute), contextTable, restrictedNames, this);
                editor = attrEditor.getEditorWidget();
                if (attrEditor.getLabel() != null) {
                    gridLayout.addWidget(attrEditor.getLabel(), row, 0);
                    gridLayout.addWidget(editor, row, 1);
                } else {
                    gridLayout.addWidget(editor, row, 0, row, 2);
                }
                attributeEditors.put(attribute, attrEditor);
                row++;
                if (previousEditor != null) {
                    QWidget.setTabOrder(previousEditor, editor);
                }
                previousEditor = editor;
            }
        }
        {//link editors
            final Collection<EFilterParamAttribute> baseAttributes = new ArrayList<>();
            AbstractAttributeEditor baseAttributeEditor;
            for (AbstractAttributeEditor editor : attributeEditors.values()) {
                baseAttributes.clear();
                baseAttributes.addAll(editor.getBaseAttributes());
                for (EFilterParamAttribute attribute : baseAttributes) {
                    baseAttributeEditor = attributeEditors.get(attribute);
                    if (baseAttributeEditor != null) {
                        baseAttributeEditor.attributeChanged.connect(editor, "onBaseAttributeChanged(AbstractAttributeEditor)");
                    }
                }
            }
        }
    }
}
