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

package org.radixware.kernel.explorer.editors.xml;

import org.apache.xmlbeans.SchemaAnnotation;
import org.apache.xmlbeans.SchemaLocalAttribute;
import org.apache.xmlbeans.XmlObject;

import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import java.awt.Color;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.mask.validators.EValidatorState;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.valeditors.ValBoolEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;



class XTreeAttribute extends XTreeElement {

    private SchemaLocalAttribute definition;
    private boolean odd = false;

    public XTreeAttribute(final TreeWindow tw, final XmlObject n, final SchemaLocalAttribute def, final boolean readonly) {
        super(tw);
        node = n;
        definition = def;
        final ValEditor editor;
        if (definition != null) {
            editor = createEditor();
            if (editor != null) {
                editor.valueChanged.connect(this, "changeValue(Object)");
                editor.changeStateForGrid();
            }
            setupAttribute();
        } else {
            odd = true;
            editor = createOddEditor();
            //setupOddAttribute(readonly);
        }
        if (editor != null) {
            editor.setReadOnly(readonly);
            setEditor(editor);
            if (odd) {
                setupOddAttribute(readonly);
            }
            this.setSizeHint(0, editor.sizeHint());
        }
    }


    @Override
    public boolean isExternalTypeSystem() {
        return false;
    }

    @Override
    public SchemaType getSchemaType() {
        return node.schemaType();
    }

    private void setupAttribute() {
        if (node != null) {
            setText(0, node.getDomNode().getNodeName());
            /* commented by yremizov setup of color settings makes in XTreeElement and XTreeWindow
            String editorProp = SettingNames.Properties.OTHER_PROPERTY;
            if (editor.isReadOnly())
            editorProp = SettingNames.Properties.READONLY_PROPERTY;
            else if (editor.isMandatory())
            editorProp = SettingNames.Properties.MANDATORY_PROPERTY;
            //TITLE PROP
            Settings s = Environment.getConfigStore().readPropertySettings(
            SettingNames.SYSTEM+"/"+
            SettingNames.EDITOR_GROUP+"/"+
            SettingNames.Editor.PROPERTY_TITLES_GROUP+"/"+
            editorProp);
            QColor color = s.foreground;
            setForeground(0, new QBrush(color));
            QFont f = s.font;
            setFont(0,f);
            color = s.background;
            if (color!=null) //by yremizov (background color may be not defined for title )
            setBackground(0,new QBrush(color));
            //VALUE PROP
            editor.applySettings(Environment.getConfigStore().readPropertySettings(
            SettingNames.SYSTEM+"/"+
            SettingNames.EDITOR_GROUP+"/"+
            SettingNames.Editor.PROPERTY_VALUES_GROUP+"/"+
            editorProp));
             */
        }

    }

    private void setupOddAttribute(boolean readonly) {
        String oldFont;
        String oldBackground;

        if (node != null && getEditor() != null) {
            if (node.getDomNode().getPrefix().equals("xsi")) {
                setText(0, node.getDomNode().getPrefix() + ":" + node.getDomNode().getLocalName());
                final ExplorerTextOptions options = getEditor().getTextOptions();
                getEditor().setDefaultTextOptions(options.changeForegroundColor(Color.GREEN));
                setForeground(0, new QBrush(QColor.darkGreen));
            } else {
                setText(0, node.getDomNode().getNodeName());
                getEditor().setValidationResult(ValidationResult.INVALID);
                setForeground(0, new QBrush(QColor.red));
            }
        }
    }

    private ValEditor createEditor() {
        ValEditor editor = null;
        if (node != null && definition != null) {
            if (definition.getType() != null) {
                SchemaAnnotation sa = definition.getType().getAnnotation();
                XmlObject presentation = XEditorBuilder.getPresentationTag(sa);
                //***********************************************************************************
                if (presentation != null) {
                    XmlObject titleNode = XElementTools.getAttribute(presentation, "TitleId");
                    if (titleNode != null) {
                        Id titleId = Id.Factory.loadFrom(titleNode.getDomNode().getNodeValue());
                        Id schemaId = tw.getEditor().getSchemaId();
                        String atrTitle = tw.getEnvironment().getApplication().getDefManager().getMlStringValue(schemaId, titleId);
                        if (atrTitle != null
                                && !atrTitle.isEmpty()) {
                            setText(0, atrTitle);
                        }
                    }
                }
                //***********************************************************************************
                EditMask mask = XEditorBuilder.getMaskFromPresentation(presentation);
                Id constID = XEditorBuilder.getConstSetID(sa);
                try {
                    //editor
                    if (constID != null) {
                        RadEnumPresentationDef constDef = tw.getEnvironment().getApplication().getDefManager().getEnumPresentationDef(constID);
                        if (constDef != null) {
                            editor = XEditorBuilder.getConstSetEditor(tw.getEnvironment(), constDef);
                        } else {
                            editor = XEditorTools.getRelevantEditor(tw.getEnvironment(), node.schemaType());//???
                        }
                        //edit mask
                        if (mask != null) {
                            editor.setEditMask(mask);
                        }
                    } else {
                        editor = editorForUndefinedConstDef(mask);
                    }
                } catch (DefinitionError defError) {
                    editor = editorForUndefinedConstDef(mask);
                    final String mess = Application.translate("XmlEditor", "Cannot get enumeration #%s");
                   tw.getEnvironment().getTracer().error(String.format(mess, constID.toString()), defError);
                }
                XEditorBuilder.setupEditorWithPresentation(editor, presentation);
            } else {
                editor = XEditorTools.getRelevantEditor(tw.getEnvironment(), null);
            }
            //***********************************************************************************
            final XmlCursor cursor = node.newCursor();
            try{
                final String textValue = cursor==null ? null : cursor.getTextValue();
                if (textValue!=null) {
                    final EditMask mask = editor.getEditMask();
                    if (!textValue.isEmpty() 
                        || mask.validate(editor.getEnvironment(), "").getState()== EValidatorState.ACCEPTABLE){
                        XEditorTools.setProperValueToEditor(editor, textValue);
                    }
                }
            }finally{
                if (cursor!=null){
                    cursor.dispose();
                }
            }
        }

        if (editor!=null){
            //changed by yremizov: RADIX-4917
            //old code:
            //editor.setMandatory(definition != null && definition.getUse() == SchemaLocalAttribute.REQUIRED);
            //new code:
            editor.setMandatory(definition == null || !definition.isNillable() || definition.getUse() == SchemaLocalAttribute.REQUIRED);
        }
        return editor;
    }

    private ValEditor createOddEditor() {
        ValEditor editor = XEditorTools.getRelevantEditor(tw.getEnvironment(), null);
        if (node != null && node.getDomNode().getNodeValue() != null) {
            if (node.getDomNode().getPrefix().equals("xsi")) {
                if (node.getDomNode().getLocalName().equals("nil")) {
                    editor = new ValBoolEditor(tw.getEnvironment(), null, new EditMaskNone(), false, false);
                }
            }
            XEditorTools.setProperValueToEditor(editor, node.getDomNode().getNodeValue());
            editor.valueChanged.connect(this, "changeValue(Object)");
            editor.changeStateForGrid();
        }
        return editor;
    }

    public SchemaLocalAttribute getDefinition() {
        return definition;
    }

    public boolean isOdd() {
        return odd;
    }
}
