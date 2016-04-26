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
package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration.TypeArgument;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration.TypeArgument.Derivation;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.choosetype.ChooseType;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeEditDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.TunedTable.TunedCellEditor;
import org.radixware.kernel.designer.common.dialogs.spellchecker.Spellchecker;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;

public class CommonParametersEditorCellLib {

//    public enum ParameterEvents {
//
//        NAME_CHANGE("NameChange", 0),
//        TYPE_CHANGE("TypeChange", 1),
//        DESCRIPTION_CHANGE("DescriptionChange", 2);
//
//        private String title;
//        private int value;
//
//        ParameterEvents(String title, int value){
//            this.title = title;
//            this.value = value;
//        }
//
//        public String getTitle(){
//            return this.title;
//        }
//
//        public int getValue(){
//            return this.value;
//        }
//
//    }
    public static class ParameterNameChangeEvent extends RadixEvent {
//        private ParameterEvents eventCode;
//
//        public ParameterChangeEvent(ParameterEvents eventCode){
//            this.eventCode = eventCode;
//        }
//
//        public ParameterEvents getEventCode(){
//            return this.eventCode;
//        }
    }

    public static interface ParameterNameChangeListener extends IRadixEventListener<ParameterNameChangeEvent> {
    }

    public static class ParametersNameChangeSupport extends RadixEventSource<ParameterNameChangeListener, ParameterNameChangeEvent> {
    }

    @Deprecated
    public static class NameCellEditor extends StringCellEditor {

        public NameCellEditor(final ParametersNameChangeSupport changeSupport, AbstractTableModel ownerModel, boolean readonly) {
            super(null, ownerModel, readonly);
            if (changeSupport != null) {
                editor.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        changedUpdate(e);
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        changedUpdate(e);
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        StringCellValue present = (StringCellValue) val;
                        present.setName(editor.getText());
                        changeSupport.fireEvent(new ParameterNameChangeEvent());
                    }
                });
            }
        }
    }

    public static class StringCellEditor extends AbstractCellEditor
            implements TableCellEditor {

        protected JTextField editor = new JTextField();
        protected Object val;
        private Object original;
        private int row;
        private int col;
        private AbstractTableModel ownerModel;
        private boolean readonly = false;
        private UpdateLocker locker = new UpdateLocker();

        public StringCellEditor(final ChangeSupport changeSupport, AbstractTableModel ownerModel, boolean readonly) {
            this(changeSupport, ownerModel, null, null, readonly);
        }

        public StringCellEditor(final ChangeSupport changeSupport, AbstractTableModel ownerModel, EIsoLanguage lang, RadixObject context, boolean readonly) {
            this.ownerModel = ownerModel;
            this.readonly = readonly;
            editor.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent e) {

                    StringCellValue present = (StringCellValue) val;
                    present.setName(editor.getText());
                    if (changeSupport != null) {
                        if (!locker.inUpdate()) {
                            changeSupport.fireChange();
                        }
                    }
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    changedUpdate(e);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    changedUpdate(e);
                }
            });
            if (lang != null) {
                Spellchecker.register(editor, lang, context);
            }

        }

        public int getCurrentRow() {
            return row;
        }

        public Component getComponent() {
            return editor;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                int r, int c) {
            this.row = r;
            this.col = c;
            val = value;
            original = new StringCellValue(value.toString());
            locker.enterUpdate();
            editor.setText(value != null ? value.toString() : "");
            editor.setCaretPosition(editor.getText().length());
            editor.getCaret().setVisible(true);
            editor.select(0, editor.getText().length());
            locker.leavUpdate();
            return editor;
        }

        @Override
        public Object getCellEditorValue() {
            return val;
        }

        @Override
        public boolean stopCellEditing() {
            int count = ownerModel.getRowCount();
            if (row > -1 && row <= count - 1) {
                ownerModel.setValueAt(val, row, col);
                fireEditingStopped();
            }
            return true;
        }

        public void setReadonly(boolean readonly) {
            this.readonly = readonly;
        }

        @Override
        public void cancelCellEditing() {
            if (!val.toString().equals(original.toString())) {
                int count = ownerModel.getRowCount();
                if (row > -1 && row <= count - 1) {
                    editor.setText(original.toString());
                    ownerModel.setValueAt(original, row, col);
                }
            }
            super.cancelCellEditing();
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return !readonly;
        }
    }

    public static class TypeCellEditor extends AbstractCellEditor
            implements TableCellEditor, TunedTable.TunedCellEditor {

        private ExtendableTextField editor = new ExtendableTextField(true);
        private JButton btn;
        private JButton resetButton;
        private int row;
        private int col;
        private Object val;
        private Object original;
        private boolean readonly = false;
        private boolean isTransparent = false;
        private AbstractTableModel ownerModel;
        private ChangeSupport changeSupport;
        private ActionListener btnListener;

        private void initEditor() {
            if (isTransparent) {
                btn = editor.addButton();
                btn.setIcon(RadixWareIcons.CHECK.CHECK.getIcon(13, 13));
                btn.setToolTipText(NbBundle.getMessage(CommonParametersEditorCellLib.class, "ParametersEditor-JavaTypeBtn-Tip"));
                btnListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (val != null && val instanceof TypePresentation) {
                            AdsTypeDeclaration oldType = ((TypePresentation) val).type;
                            String extstr = oldType.getExtStr();
                            Definition context = ((TypePresentation) val).context;
                            IPlatformClassPublisher publisher = ((AdsSegment) context.getModule().getSegment()).getBuildPath().getPlatformPublishers().findPublisherByName(extstr);
                            if (publisher != null) {
                                AdsTypeDeclaration newType = null;
                                if (publisher instanceof AdsClassDef) {
                                    newType = AdsTypeDeclaration.Factory.newInstance((AdsClassDef) publisher);
                                } else if (publisher instanceof AdsEnumDef) {
                                    newType = AdsTypeDeclaration.Factory.newInstance((AdsEnumDef) publisher);
                                }
                                resetButton.setVisible(true);
                                ((TypePresentation) val).setType(newType);
                                editor.setValue(((TypePresentation) val).toString());
                                if (changeSupport != null) {
                                    changeSupport.fireChange();
                                }
                                stopCellEditing();
                            } else {
                                DialogUtils.messageInformation(NbBundle.getMessage(CommonParametersEditorCellLib.class, "ParametersEditor-JavaType-Publish"));
                            }
                        }
                    }
                };
                btn.addActionListener(btnListener);
            } else {
                btn = editor.addButton();
                btn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
                btnListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (val != null && val instanceof TypePresentation) {
                            TypePresentation type = (TypePresentation) val;
                            TypeEditDisplayer typeEditDisplayer = new TypeEditDisplayer();

                            ChooseType.DefaultTypeFilter typeFilter = new ChooseType.DefaultTypeFilter(type.getContext(), type.getParameter());
                            // typeFilter.except(ETypeNature.JAVA_PRIMITIVE);
                            AdsTypeDeclaration newType = typeEditDisplayer.editType(type.getType(), typeFilter);

                            if (newType != null
                                    && !newType.equals(type.getType())) {
                                type.setType(newType);
                                editor.setValue(type.toString());
                                resetButton.setVisible(true);
                                if (changeSupport != null) {
                                    changeSupport.fireChange();
                                }
                                ownerModel.fireTableDataChanged();
                            }
                        }
                    }
                };
                btn.setToolTipText(NbBundle.getMessage(CommonParametersEditorCellLib.class, "PP-Column-Type"));
                btn.addActionListener(btnListener);
            }
            if (!readonly) {
                resetButton = editor.addButton(RadixWareDesignerIcon.DELETE.CLEAR.getIcon());
                resetButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (val != null && val instanceof TypePresentation) {
                            TypePresentation type = (TypePresentation) val;
                            type.setType(null);
                            type.derivation = Derivation.NONE;
                            editor.setValue("<Not Defined>");
                            resetButton.setVisible(false);
                            if (changeSupport != null) {
                                changeSupport.fireChange();
                            }
                        }
                    }
                });
            }
        }

        public TypeCellEditor(final ChangeSupport changeSupport, boolean readonly, boolean isTransparent, final AbstractTableModel ownerModel) {
            this.ownerModel = ownerModel;
            this.readonly = readonly;
            this.isTransparent = isTransparent;
            this.changeSupport = changeSupport;
            initEditor();
        }

        public TypeCellEditor(final ChangeSupport changeSupport, AdsMethodProfileSettings settings, final AbstractTableModel ownerModel) {
            this.ownerModel = ownerModel;
            this.changeSupport = changeSupport;
            this.readonly = settings.layerReadonly || settings.optionalReadonly;
            this.isTransparent = !settings.layerReadonly && !settings.isPresentationSlot ? settings.isTransparent : false;
            initEditor();
        }

        public Component getComponent() {
            return editor;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                int r, int c) {
            this.row = r;
            this.col = c;
            val = value;
            if (val instanceof TypePresentation) {
                if (((TypePresentation) val).isTypeArgument()) {
                    original = new TypePresentation(((TypePresentation) val).getTypeArgument(), ((TypePresentation) val).getContext());
                } else {
                    original = new TypePresentation(((TypePresentation) val).getType(), ((TypePresentation) val).getContext());
                }

                if (isTransparent) {
                    AdsTypeDeclaration currentType = ((TypePresentation) val).getType();
                    editor.setReadOnly(!currentType.getTypeId().equals(EValType.JAVA_CLASS));
                }
                resetButton.setVisible(((TypePresentation) val).getType() != null);
            } else {
                original = value;
            }
            editor.setValue(value != null ? value.toString() : "");

            return editor;
        }

        @Override
        public Object getCellEditorValue() {
            return val;
        }

        @Override
        public boolean stopCellEditing() {
            int count = ownerModel.getRowCount();
            if (row > -1 && row <= count - 1) {
                ownerModel.setValueAt(val, row, col);
                fireEditingStopped();
            }
            return true;
        }

        @Override
        public void cancelCellEditing() {
            if (val instanceof TypePresentation
                    && original instanceof TypePresentation) {
                if (!((TypePresentation) val).getType().equals(((TypePresentation) original).getType())) {
                    int count = ownerModel.getRowCount();
                    if (row > -1 && row <= count - 1) {
                        ownerModel.setValueAt(original, row, col);
                    }
                }
            }
            super.cancelCellEditing();
        }

        void setReadonly(boolean readonly) {
            this.readonly = readonly;
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            if (isTransparent) {
                return true;
            }
            return !readonly;
        }

        @Override
        public void editingPerformed(ActionEvent e) {
            btnListener.actionPerformed(new ActionEvent(btn, e.getID(), e.getActionCommand(), e.getWhen(), e.getModifiers()));
        }
    }

    public static class TypeCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setText(value.toString());
            if (value instanceof TypePresentation) {
                TypePresentation present = (TypePresentation) value;
                RadixIcon icon = present.getType() != null ? present.getType().getIcon() : null;
                if (icon != null) {
                    setIcon(icon.getIcon(16, 16));
                } else {
                    setIcon(RadixObjectIcon.UNKNOWN.getIcon(16, 16));
                }
                setToolTipText(present.getToolTip());
            }
            return this;
        }
    }

    public static class StringCellValue {

        private boolean isChanged = false;
        private String name;

        public StringCellValue(String name) {
            this.name = name;
        }

        void setName(String name) {
            this.name = name;
            this.isChanged = true;
        }

        public String getName() {
            return name;
        }

        boolean beenChanged() {
            return isChanged;
        }

        @Override
        public String toString() {
            return name != null ? name : "";
        }
    }

    public static class TypePresentation {

        private AdsTypeDeclaration type;
        private Definition context;
        private IAdsTypedObject parameter;
        private boolean isChanged = false;
        private Derivation derivation = null;
        private AdsTypeDeclaration.TypeArgument argument = null;

        public TypePresentation(TypeArgument typeArg, Definition context) {
            this.argument = typeArg;
            this.derivation = typeArg.getDerivation();
            this.type = typeArg.getType();
            this.context = context;
        }

        public TypePresentation(TypeArgument typeArg, Definition context, IAdsTypedObject parameter) {
            this.argument = typeArg;
            this.derivation = typeArg.getDerivation();
            this.type = typeArg.getType();
            this.parameter = parameter;
            this.context = context;
        }

        public TypePresentation(AdsTypeDeclaration type, Definition context) {
            this.type = type;
            this.context = context;
        }

        public TypePresentation(AdsTypeDeclaration type, Definition context, IAdsTypedObject parameter) {
            this.type = type;
            this.context = context;
            this.parameter = parameter;
        }

        public String getToolTip() {
            if (type != null) {
                AdsType res = type.resolve(context).get();
                return res != null ? res.getToolTip() : type.getQualifiedName();
            }
            return null;
        }

        public AdsTypeDeclaration.TypeArgument getTypeArgument() {
            return argument;
        }

        public boolean isTypeArgument() {
            return argument != null;
        }

        public Definition getContext() {
            return context;
        }

        public IAdsTypedObject getParameter() {
            return parameter;
        }

        public AdsTypeDeclaration getType() {
            return type;
        }

        public Derivation getDerivation() {
            return derivation;
        }

        public void setType(AdsTypeDeclaration type) {
            this.type = type;
            isChanged = true;
        }

        public boolean beenChanged() {
            return isChanged;
        }

        @Override
        public String toString() {
            return type != null && context != null ? type.getName(context) : "<Not Defined>";
        }
    }

    public static class LabelWithInsetsRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {

            final javax.swing.JLabel component = (javax.swing.JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            component.setText(value != null ? value.toString() : "");
            javax.swing.JPanel content = new javax.swing.JPanel() {
                @Override
                public void setEnabled(boolean enabled) {
                    component.setEnabled(enabled);
                    super.setEnabled(enabled);
                }
            };
            content.setLayout(new BorderLayout());
            content.add(component, BorderLayout.CENTER);
            content.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
            content.setBackground(component.getBackground());
            return content;
        }
    }

    public static class DescriptionCellEditor extends AbstractCellEditor
            implements TableCellEditor, TunedCellEditor {

        private ExtendableTextField editor = new ExtendableTextField(true);
        private ActionListener btnListener;
        private JButton btn;
        private boolean readonly = false;
        private AbstractTableModel ownerModel;
        private StringCellValue val;
        private StringCellValue original;
        private int row;
        private int col;

        public boolean isShowing() {
            return editor.isShowing();
        }

        public void setReadonly(boolean readonly) {
            this.readonly = readonly;
        }

        public DescriptionCellEditor(final ChangeSupport changeSupport, final boolean readonly, AbstractTableModel ownerModel) {
            this.ownerModel = ownerModel;
            this.readonly = readonly;
            btn = editor.addButton();
            btn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
            btnListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DescriptionEditPanel panel = new DescriptionEditPanel();
                    String res = panel.edit(val.toString());
                    if (!res.equals(val.toString())) {
                        editor.setValue(res);
                        val.setName(res);
                        if (changeSupport != null) {
                            changeSupport.fireChange();
                        }
                    }
                }
            };
            btn.addActionListener(btnListener);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                int r, int c) {
            this.val = value != null ? (StringCellValue) value : new StringCellValue("");
            this.original = new StringCellValue(val.toString());
            this.row = r;
            this.col = c;
            editor.setValue(val);
            return editor;
        }

        @Override
        public Object getCellEditorValue() {
            return val;
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return !readonly;
        }

        @Override
        public boolean stopCellEditing() {
            int count = ownerModel.getRowCount();
            if (row > -1 && row <= count - 1) {
                ownerModel.setValueAt(val, row, col);
                fireEditingStopped();
            }
            return true;
        }

        @Override
        public void cancelCellEditing() {
            if (!val.equals(original)) {
                int count = ownerModel.getRowCount();
                if (row > -1 && row <= count - 1) {
                    ownerModel.setValueAt(original, row, col);
                }
            }
            super.cancelCellEditing();
        }

        @Override
        public void editingPerformed(ActionEvent e) {
            btnListener.actionPerformed(new ActionEvent(btn, e.getID(), e.getActionCommand(), e.getWhen(), e.getModifiers()));
        }
    }
}
