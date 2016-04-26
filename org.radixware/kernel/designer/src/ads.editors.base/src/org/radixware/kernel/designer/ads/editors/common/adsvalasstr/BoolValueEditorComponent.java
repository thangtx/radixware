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
 * 9/27/11 2:50 PM
 */
package org.radixware.kernel.designer.ads.editors.common.adsvalasstr;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.enums.EValType;


public class BoolValueEditorComponent extends BaseEditorComponent<BoolValueEditorComponent.Model> {

    public static final AdsValAsStr TRUE = AdsValAsStr.Factory.newInstance("1");
    public static final AdsValAsStr FALSE = AdsValAsStr.Factory.newInstance("0");

    public static final class Model extends BaseEditorComponent.BaseEditorModel<Boolean> {

        @Override
        protected Boolean toLocal(AdsValAsStr value) {
            if (value != null && value != AdsValAsStr.NULL_VALUE) {
                return TRUE.equals(value, EValType.BOOL);
            }
            return null;
        }

        @Override
        protected AdsValAsStr toExternal(Boolean local) {
            if (local != null) {
                return AdsValAsStr.Factory.newInstance(local ? "1" : "0");
            } else {
                return AdsValAsStr.NULL_VALUE;
            }
        }

        @Override
        public void updateValue(Object... params) {
            if (params[0] instanceof Boolean) {
                setLocalValue((Boolean) params[0], true);
            }
        }
    }

    private static final class BoolValueWrap {

        public final AdsValAsStr value;
        private final Boolean internal;

        public BoolValueWrap(Boolean value) {
            this.internal = value;
            this.value = value != null ? AdsValAsStr.Factory.newInstance(value ? "1" : "0") : AdsValAsStr.NULL_VALUE;
        }

        @Override
        public String toString() {
            if (internal != null) {
                return internal ? "true" : "false";
            }
            return AdsValAsStr.NULL_VALUE.toString();
        }
    }

    private static final class BoolComboBoxModel extends AbstractListModel implements ComboBoxModel {

        static final BoolValueWrap TRUE = new BoolValueWrap(Boolean.TRUE);
        static final BoolValueWrap FALSE = new BoolValueWrap(Boolean.FALSE);
        static final BoolValueWrap UNDEFINED = new BoolValueWrap(null);
        private BoolValueWrap selValue;
        private BoolValueWrap[] values;

        public BoolComboBoxModel() {

            values = new BoolValueWrap[]{ TRUE, FALSE };
            selValue = null;
        }

        @Override
        public void setSelectedItem(Object anItem) {
            selValue = (BoolValueWrap) anItem;
        }

        @Override
        public BoolValueWrap getSelectedItem() {
            if (selValue != null) {
                return selValue;
            } else {
                return UNDEFINED;
            }
        }

        @Override
        public int getSize() {
            return values.length;
        }

        @Override
        public BoolValueWrap getElementAt(int index) {
            if (index >= 0 && index < values.length) {
                return values[index];
            } else {
                return UNDEFINED;
            }
        }
    }
    private final JComboBox editor;
    private final ItemListener listener;

    public BoolValueEditorComponent() {
        super(new Model());

        editor = new JComboBox(new BoolComboBoxModel());
        editor.setEditable(false);

        listener = new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                notifyComponentChanged();
            }
        };
    }

    @Override
    public JComboBox getEditorComponent() {
        return editor;
    }

    @Override
    protected void updateEditorComponent() {
        Boolean val = getModel().getLocalValue();

        if (val == Boolean.TRUE) {
            editor.setSelectedIndex(0);
        } else if (val == Boolean.FALSE) {
            editor.setSelectedIndex(1);
        } else {
            editor.setSelectedIndex(-1);
            editor.updateUI();
        }
    }

    @Override
    protected void updateModelValue() {
        BoolValueWrap sel = (BoolValueWrap) editor.getSelectedItem();
        getModel().updateValue(sel != null ? sel.internal : null);
    }

    @Override
    protected void connectEditorComponent() {
        editor.addItemListener(listener);
    }

    @Override
    protected void disconnectEditorComponent() {
        editor.removeItemListener(listener);
    }
}
