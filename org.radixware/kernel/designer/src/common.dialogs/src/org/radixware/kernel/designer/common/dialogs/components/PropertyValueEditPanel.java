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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.IEnumDef.IItem;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.environment.IRadixEnvironment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.valstreditor.NotNullEditor;
import org.radixware.kernel.designer.common.dialogs.components.valstreditor.NullEditor;
import org.radixware.kernel.designer.common.dialogs.components.valstreditor.ValAsStrEditor;
import org.radixware.kernel.designer.common.dialogs.components.valstreditor.ValueEditor;


public class PropertyValueEditPanel extends ValAsStrEditPanel {

    private class ValAsStrEnumEditor extends ValAsStrEditor {

        @Override
        protected ValueEditor getValueEditor() {
            return enumEditor;
        }

        private class EnumEditor extends ValueEditor<Object> {

            private class NotNullEnumEditor extends NotNullEditor<Object> {

                private final JComboBox comboBox = new JComboBox() {
                    @Override
                    public boolean requestFocusInWindow() {
                        if (this.isValid() && this.isShowing()) {
                            this.showPopup();
                        }
                        return super.requestFocusInWindow();
                    }

                    @Override
                    public void processKeyEvent(KeyEvent ev) {
                        int keyCode = ev.getKeyCode();
                        if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_ESCAPE) {
                            this.getParent().dispatchEvent(ev);
                            ev.consume();
                        } else {
                            super.processKeyEvent(ev);
                        }
                    }
                };
                private final Map<String, Object> valueForName;
                private final Map<Object, String> nameForValue;

                public NotNullEnumEditor(EnumEditor editor, List<? extends IEnumDef.IItem> items) {
                    super(editor);
                    valueForName = new HashMap<>(items.size());
                    nameForValue = new HashMap<>(items.size());
                    List<String> options = new ArrayList<>(items.size());
                    for (IItem item : items) {
                        String name = item.getName();
                        ValAsStr value = item.getValue();
                        options.add(name);
                        Object obj = value != null ? value.toObject(getValType()) : null;
                        valueForName.put(name, obj);
                        if (obj != null) {
                            nameForValue.put(obj, name);
                        }
                    }
                    comboBox.setModel(new DefaultComboBoxModel(options.toArray()));
                    comboBox.addItemListener(new ItemListener() {
                        @Override
                        public void itemStateChanged(ItemEvent e) {
                            fireValueChanged();
                        }
                    });
                }

                @Override
                public void setValue(Object value) {
                    if (value != null) {
                        comboBox.setSelectedItem(nameForValue.get(value));
                    } else {
                        comboBox.setSelectedItem(NULL_ITEM_NAME);
                    }
                    fireValueChanged();
                }

                @Override
                public Object getValue() {
                    return valueForName.get((String) comboBox.getSelectedItem());
                }

                @Override
                public JComponent getEditor() {
                    return comboBox;
                }

                public void setNullAble(boolean nullAble) {
                    if (nullAble && !((String) comboBox.getItemAt(0)).equals(NULL_ITEM_NAME)) {
                        comboBox.insertItemAt(NULL_ITEM_NAME, 0);
                    } else if (!nullAble && ((String) comboBox.getItemAt(0)).equals(NULL_ITEM_NAME)) {
                        comboBox.removeItem(NULL_ITEM_NAME);
                    }
                }
            }
            private final NotNullEnumEditor notNullEnumEditor;
            private final Object defaultValue;
            private final String NULL_ITEM_NAME = "NULL";
            private final IEnumDef.IItem NULL_ITEM = new IEnumDef.IItem() {
                @Override
                public Id getId() {
                    return null;
                }

                @Override
                public String getName() {
                    return NULL_ITEM_NAME;
                }

                @Override
                public String getTitle(IRadixEnvironment env) {
                    return NULL_ITEM_NAME;
                }

                @Override
                public String getTitle(IRadixEnvironment env, EIsoLanguage lang) {
                    return NULL_ITEM_NAME;
                }

                @Override
                public ValAsStr getValue() {
                    return null;
                }

                @Override
                public Collection<Id> getDomainIds() {
                    return null;
                }
            };

            public EnumEditor(ValAsStrEditor editor, IEnumDef enumDef) {
                super(editor);
                ArrayList<IEnumDef.IItem> items = new ArrayList<>();
                items.add(NULL_ITEM);
                items.addAll(enumDef.getItems().list(EScope.ALL));
                defaultValue = items.size() > 1 ? items.get(1).getValue().toObject(getValType()) : null;
                notNullEnumEditor = new NotNullEnumEditor(this, items);
            }

            @Override
            public void setDefaultValue() {
                notNullEnumEditor.setValue(defaultValue);
            }

            @Override
            public NotNullEditor getNotNullEditor() {
                return notNullEnumEditor;
            }

            @Override
            public NullEditor getNullEditor() {
                return null;
            }

            @Override
            public void setNullAble(boolean nullAble) {
                notNullEnumEditor.setNullAble(nullAble);
                super.setNullAble(nullAble);
            }
        }
        private final IEnumDef enumDef;
        private final EnumEditor enumEditor;

        public ValAsStrEnumEditor(IEnumDef enumDef) {
            this(enumDef, null);
        }

        public ValAsStrEnumEditor(IEnumDef enumDef, String nullIndicator) {
            super(nullIndicator);
            this.enumDef = enumDef;
            enumEditor = new EnumEditor(this, enumDef);
            enumEditor.setDefaultValue();
        }

        public IEnumDef getEnum() {
            return enumDef;
        }

        @Override
        public EValType getValType() {
            return enumDef.getItemType();
        }
    }

    public PropertyValueEditPanel() {
        this(null);
    }

    public PropertyValueEditPanel(String nullIndicator) {
        super(nullIndicator);
    }

    public void setEnum(IEnumDef enumDef, ValAsStr value) {
        setValAsStrEditor(getValAsStrEditorForEnum(enumDef));
        getValAsStrEditor().setValue(value);
    }

    private ValAsStrEditor getValAsStrEditorForEnum(IEnumDef enumDef) {
        return new ValAsStrEnumEditor(enumDef);
    }

    public void activateByKeyInput(KeyEvent e) {
        getValAsStrEditor().activateByKeyInput(e);
    }
}
