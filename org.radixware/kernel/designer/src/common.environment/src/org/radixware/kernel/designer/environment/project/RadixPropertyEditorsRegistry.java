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

package org.radixware.kernel.designer.environment.project;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.beans.PropertyEditorSupport;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import org.openide.actions.CopyAction;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;
import org.radixware.kernel.designer.common.dialogs.utils.ClipboardUtils;


public class RadixPropertyEditorsRegistry {

    public static class IdPropertyEditor extends PropertyEditorSupport {

        @Override
        public String getAsText() {
            final Object value = getValue();
            if (value instanceof Id) {
                return ((Id) value).toString();
            } else {
                return super.getAsText();
            }
        }

        @Override
        public Component getCustomEditor() {
            final JPanel panel = new JPanel();
            panel.setBorder(new EmptyBorder(8, 8, 8, 8));
            panel.setLayout(new BorderLayout());

            final String text = getAsText();
            final ExtendableTextField editor = new ExtendableTextField();
            editor.setTextFieldValue(text);
            editor.setEditable(false);

            final JButton copyButton = editor.addButton();
            copyButton.setIcon(SystemAction.get(CopyAction.class).getIcon());

            copyButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    final String text = getAsText();
                    ClipboardUtils.copyToClipboard(text);
                }
            });

            panel.add(editor, BorderLayout.NORTH);
            panel.setPreferredSize(new Dimension(300, panel.getPreferredSize().height));

            return panel;
        }

        @Override
        public boolean supportsCustomEditor() {
            return true;
        }
    }

    private static class DefinitionPropertyInplaceEditor implements InplaceEditor {

        private DefinitionLinkEditPanel panel = new DefinitionLinkEditPanel();
        private PropertyEditor editor;
        private PropertyModel model;

        @Override
        public void connect(PropertyEditor propertyEditor, PropertyEnv env) {
            editor = propertyEditor;
            reset();
        }

        @Override
        public JComponent getComponent() {
            return panel;
        }

        @Override
        public void clear() {
            editor = null;
            model = null;
        }

        @Override
        public Object getValue() {
            return panel.getDefinition();
        }

        @Override
        public void setValue(Object object) {
            if (object instanceof Definition) {
                final Definition definition = (Definition) object;
                panel.open(definition, definition.getId());
            } else {
                panel.open(null, null);
            }
        }

        @Override
        public void reset() {
            setValue(editor.getValue());
        }

        @Override
        public KeyStroke[] getKeyStrokes() {
            return new KeyStroke[0];
        }

        @Override
        public PropertyEditor getPropertyEditor() {
            return editor;
        }

        @Override
        public PropertyModel getPropertyModel() {
            return model;
        }

        @Override
        public void setPropertyModel(PropertyModel propertyModel) {
            this.model = propertyModel;
        }

        @Override
        public boolean isKnownComponent(Component component) {
            return component == panel || panel.isAncestorOf(component);
        }

        @Override
        public boolean supportsTextEntry() {
            return false;
        }

        @Override
        public void addActionListener(ActionListener actionListener) {
            //do nothing - not needed for this component
        }

        @Override
        public void removeActionListener(ActionListener actionListener) {
            //do nothing - not needed for this component
        }
    }

    public static class RadixObjectPropertyEditor extends PropertyEditorSupport {

        public RadixObjectPropertyEditor() {
        }

        public RadixObjectPropertyEditor(Object source) {
            super(source);
        }

        @Override
        public String getAsText() {
            final Object value = getValue();
            if (value instanceof RadixObject) {
                return ((RadixObject) value).getQualifiedName();
            } else {
                return super.getAsText();
            }
        }


        @Override
        public Component getCustomEditor() {
            final JPanel panel = new JPanel();
            panel.setBorder(new EmptyBorder(8, 8, 8, 8));
            panel.setLayout(new BorderLayout());

            final DefinitionLinkEditPanel editor = new DefinitionLinkEditPanel();
            final Definition definition = (Definition)getValue();
            editor.open(definition, definition.getId());
            editor.setEnabled(false);

            panel.add(editor, BorderLayout.NORTH);
            panel.setPreferredSize(new Dimension(400, panel.getPreferredSize().height));

            return panel;
        }

        @Override
        public boolean supportsCustomEditor() {
            return getValue() instanceof Definition;
        }

    }

    public static class DefinitionPropertyEditor extends RadixObjectPropertyEditor /*implements ExPropertyEditor, InplaceEditor.Factory*/ {

        public DefinitionPropertyEditor() {
        }

        public DefinitionPropertyEditor(Object source) {
            super(source);
        }
  
//        @Override
//        public void attachEnv(PropertyEnv env) {
//            env.registerInplaceEditorFactory(this);
//        }
//        private InplaceEditor inplaceEditor = null;
//
//        @Override
//        public InplaceEditor getInplaceEditor() {
//            if (inplaceEditor == null) {
//                inplaceEditor = new DefinitionPropertyInplaceEditor();
//            }
//            return inplaceEditor;
//        }
    }

    public final void initialize() {
        PropertyEditorManager.registerEditor(Id.class, IdPropertyEditor.class);
        PropertyEditorManager.registerEditor(RadixObject.class, RadixObjectPropertyEditor.class);
        PropertyEditorManager.registerEditor(Definition.class, DefinitionPropertyEditor.class);
        PropertyEditorManager.registerEditor(DdsTableDef.class, DefinitionPropertyEditor.class);
        PropertyEditorManager.registerEditor(DdsColumnDef.class, DefinitionPropertyEditor.class);
        PropertyEditorManager.registerEditor(AdsEnumDef.class, DefinitionPropertyEditor.class);
        PropertyEditorManager.registerEditor(DdsReferenceDef.class, DefinitionPropertyEditor.class);
    }

    public RadixPropertyEditorsRegistry() {
        initialize();
    }
    private static RadixPropertyEditorsRegistry INSTANCE = new RadixPropertyEditorsRegistry();

    public static RadixPropertyEditorsRegistry getInstance() {
        return INSTANCE;
    }
}
