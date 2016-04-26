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

package org.radixware.wps.views.editor.property;

import java.util.EnumSet;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.property.PropEditorOptions;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.models.items.properties.PropertyXml;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.editors.xmleditor.XmlEditorOperation;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.XmlObjectProcessor;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.XmlEditorDialog;
import org.radixware.wps.rwt.InputBox.DisplayController;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.views.editors.valeditors.IValEditor;
import org.radixware.wps.views.editors.valeditors.ValEditorFactory;
import org.radixware.wps.views.editors.valeditors.ValObjectAsStrEditorController;


public class PropXmlEditor extends PropEditor {

    private static class ValEditorFactoryImpl extends ValEditorFactory {
                
        private final PropertyXml property;
        
        public class XmlDisplayController implements DisplayController<Object>{

            @Override
            public String getDisplayValue(final Object value, final boolean isFocused, final boolean isReadOnly) {
                if (value==null){
                    return property.getEditMask().toStr(property.getEnvironment(), null);
                }else{
                    return property.getEnvironment().getMessageProvider().translate("Value", "<value defined>");
                }
            }
            
        }

        public class ValXmlEditorController extends ValObjectAsStrEditorController {
            
            private final ToolButton xmlDialogBtn;
            private boolean isReadOnly;

            public ValXmlEditorController(final IClientEnvironment env) {
                super(env);
                xmlDialogBtn = new ToolButton();
                xmlDialogBtn.addClickHandler(new IButton.ClickHandler() {
                    @Override
                    public void onClick(final IButton source) {
                        showXmlDialog();
                    }
                });
                addButton(xmlDialogBtn);
            }

            @Override
            protected DisplayController<Object> createDisplayController() {
                return new PropertyDisplayController<>(new XmlDisplayController(), property);
            }

            @Override
            protected Label createLabel() {
                return LabelComponentFactory.getDefault().createPropLabelComponent(property);
            }

            private void showXmlDialog() {
                final XmlObject value;
                final SchemaTypeSystem typeSystem;
                if (property.getValueObject() instanceof XmlObject) {
                    value = (XmlObject) property.getValueObject();
                    typeSystem = value.schemaType() == null ? null : value.schemaType().getTypeSystem();
                } else {
                    final Class documentClass = property.getValClass();
                    if (documentClass == null) {
                        value = null;
                        typeSystem = null;
                    } else {
                        final XmlObject docInstance =
                                XmlObjectProcessor.createNewInstance((ClassLoader) getEnvironment().getApplication().getDefManager().getClassLoader(), documentClass);//NOPMD
                        final SchemaType schemaType = docInstance.schemaType();
                        if (schemaType == null) {
                            value = null;
                            typeSystem = null;
                        } else {
                            typeSystem = schemaType.getTypeSystem();
                            if (schemaType.getDocumentElementName() == null) {
                                value = null;
                            } else {
                                final XmlCursor cursor = docInstance.newCursor();
                                try {
                                    cursor.toNextToken();
                                    cursor.beginElement(schemaType.getDocumentElementName());
                                } finally {
                                    cursor.dispose();
                                }
                                value = docInstance;
                            }
                        }//if (schemaType==null){
                    }//if (documentClass==null){
                }//if (property.getValueObject() instanceof XmlObject){
                final XmlEditorDialog dialog =
                        new XmlEditorDialog((WpsEnvironment) getEnvironment(), typeSystem, isReadOnly);
                final EnumSet<XmlEditorOperation> disabledOperations = EnumSet.of(XmlEditorOperation.CHANGE_ROOT);
                if (isReadOnly){
                    disabledOperations.add(XmlEditorOperation.OPEN);
                }
                dialog.setDisabledOperations(disabledOperations);
                dialog.setWindowTitle(property.getTitle());
                final Icon icon =
                        getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.ValueTypes.XML);
                dialog.setWindowIcon(icon);
                if (value != null) {
                    dialog.setValue(value);
                }
                if (dialog.execDialog() == IDialog.DialogResult.ACCEPTED) {
                    property.setValueObject(dialog.getValue());
                }
            }

            public void update(final boolean isReadOnly) {
                this.isReadOnly = isReadOnly;
                final ClientIcon clientIcon = isReadOnly ? ClientIcon.CommonOperations.VIEW : ClientIcon.CommonOperations.EDIT;
                final Icon icon =
                        getEnvironment().getApplication().getImageManager().getIcon(clientIcon);
                xmlDialogBtn.setVisible(getValue()!=null || !isReadOnly);
                xmlDialogBtn.setIcon(icon);
                if (isReadOnly){
                    xmlDialogBtn.setToolTip(getEnvironment().getMessageProvider().translate("PropertyEditor", "View Value"));
                }else{
                    xmlDialogBtn.setToolTip(getEnvironment().getMessageProvider().translate("PropertyEditor", "Edit Value"));
                }
            }
        }                

        public ValEditorFactoryImpl(final PropertyXml property) {
            this.property = property;
        }

        @Override
        public IValEditor createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment env) {
            ValXmlEditorController controller = new ValXmlEditorController(env);
            return controller.getValEditor();
        }
    }

    public PropXmlEditor(final PropertyXml property) {
        super(property, new ValEditorFactoryImpl(property));
    }

    @Override
    protected void updateEditor(Object currentValue, Object initialValue, PropEditorOptions options) {
        super.updateEditor(currentValue, initialValue, options);
        getEditorController().update(options.isReadOnly());
    }

    private ValEditorFactoryImpl.ValXmlEditorController getEditorController() {
        return (ValEditorFactoryImpl.ValXmlEditorController) getValEditor();
    }
}
