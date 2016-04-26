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

/*
 * 9/26/11 2:48 PM
 */
package org.radixware.kernel.designer.ads.editors.common.adsvalasstr;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.ads.AdsValAsStr.IValueController;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.utils.Utils;


public class AdsEnumDefEditorComponent extends BaseEditorComponent<AdsEnumDefEditorComponent.LocalModel> {

    public static final class LocalModel extends BaseEditorComponent.BaseEditorModel<ValAsStr> {

        @Override
        public void updateValue(Object... params) {
            if (params[0] instanceof ValAsStr) {
                setLocalValue((ValAsStr) params[0], true);
            }
        }

        @Override
        protected ValAsStr toLocal(AdsValAsStr value) {
            if (value != null) {
                return value.getValAsStr();
            } else {
                return null;
            }
        }

        @Override
        protected AdsValAsStr toExternal(ValAsStr local) {
            return AdsValAsStr.Factory.newInstance(local);
        }
    }

    private static final class NameValuePair {

        final String name;
        final ValAsStr value;
        static final NameValuePair UNDEFINED = new NameValuePair("<Not Defined>", null);

        public NameValuePair(String name, ValAsStr value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static final class EnumComboBoxModel extends AbstractListModel implements ComboBoxModel {

        NameValuePair selValue;
        List<NameValuePair> values;

        public EnumComboBoxModel(AdsEnumDef enumClass) {
            super();
            values = new ArrayList<NameValuePair>();


            if (enumClass != null) {
                for (AdsEnumItemDef item : enumClass.getItems().list(EScope.LOCAL_AND_OVERWRITE)) {
                    values.add(new NameValuePair(item.getName(), item.getValue()));
                }

                Collections.sort(values, new Comparator<NameValuePair>() {

                    @Override
                    public int compare(NameValuePair o1, NameValuePair o2) {
                        return o1.name.compareTo(o2.name);
                    }
                });
            }
        }

        public EnumComboBoxModel(AdsEnumDef enumClass, AdsValAsStr value) {
            this(enumClass);

            findAndSet(value);
        }

        private void findAndSet(AdsValAsStr value) {
            if (value != null) {
                for (NameValuePair pair : values) {
                    if (pair.value.equals(value.getValAsStr())) {
                        setSelectedItem(pair);
                        return;
                    }
                }
            }
            setSelectedItem(NameValuePair.UNDEFINED);
        }

        @Override
        public void setSelectedItem(Object anItem) {
            if (anItem instanceof NameValuePair) {
                selValue = (NameValuePair) anItem;
            } else if (anItem instanceof AdsValAsStr) {
                findAndSet((AdsValAsStr) anItem);
            } else {
                selValue = NameValuePair.UNDEFINED;
            }
        }

        @Override
        public NameValuePair getSelectedItem() {
            return selValue;
        }

        @Override
        public int getSize() {
            return values.size();
        }

        @Override
        public NameValuePair getElementAt(int index) {
            return values.get(index);
        }
    }
    
    private static final class EnumComboBoxCellRenderer extends DefaultListCellRenderer{
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            c.setForeground(Color.black);
            revalidate();
            return c;
        }
    }

    private final JComboBox editor;
    private final ItemListener listener;

    public AdsEnumDefEditorComponent() {
        super(new LocalModel());

        editor = new JComboBox();
        listener = new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                notifyComponentChanged();
                editor.setForeground(Color.black);
            }
        };
        editor.setRenderer(new EnumComboBoxCellRenderer());
    }

    @Override
    public boolean isSetValue() {
        return super.isSetValue() && editor.getModel().getSelectedItem() != null;
    }

    @Override
    public JComboBox getEditorComponent() {
        return editor;
    }

    @Override
    protected void updateEditorComponent() {
        ValAsStr currVal = getModel().getLocalValue();

        boolean find = false;
        for (NameValuePair nvp : ((EnumComboBoxModel) editor.getModel()).values) {
            if (Utils.equals(nvp.value, currVal)) {
                editor.setSelectedItem(nvp);
                find = true;
                break;
            }
        }

        if (!find) {
            editor.setSelectedIndex(-1);
            if (currVal != null){
                editor.setForeground(Color.red);
            }
        } else {
            editor.setForeground(Color.black);
        }
        
        editor.updateUI();
    }

    @Override
    protected void updateModelValue() {
        getModel().updateValue(((NameValuePair) editor.getModel().getSelectedItem()).value);
    }

    @Override
    protected void connectEditorComponent() {
        editor.addItemListener(listener);
    }

    @Override
    protected void disconnectEditorComponent() {
        editor.removeItemListener(listener);
    }

    @Override
    protected void openImpl(IValueController context) {
        openImpl(context, context.getValue());
    }

    @Override
    protected void openImpl(IValueController context, AdsValAsStr currValue) {
        if (context != null) {
            AdsType adsType = context.getContextType().resolve(context.getContextDefinition()).get();
            if (adsType != null && adsType instanceof AdsEnumType) {
                AdsEnumDef enumClass = ((AdsEnumType) adsType).getSource();
                EnumComboBoxModel enumComboBoxModel = new EnumComboBoxModel(enumClass, currValue);
                editor.setModel(enumComboBoxModel);
            }
            super.openImpl(context, currValue);
        }
    }
}
