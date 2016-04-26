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
 *  9/26/11 2:14 PM
 */
package org.radixware.kernel.designer.ads.editors.common.adsvalasstr;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.designer.common.dialogs.components.ValAsStrEditPanel;


public class ValAsStrEditorComponent extends BaseEditorComponent<ValAsStrEditorComponent.LocalModel> {

    public static final class LocalModel extends BaseEditorComponent.BaseEditorModel<ValAsStr> {

        @Override
        protected ValAsStr toLocal(AdsValAsStr value) {
            if (value != null) {
                return value.getValAsStr();
            }
            return null;//ValAsStr.Factory.loadFrom("");
        }

        @Override
        protected AdsValAsStr toExternal(ValAsStr local) {
            return AdsValAsStr.Factory.newInstance(local);
        }

        @Override
        public void updateValue(Object... params) {
            ValAsStr value = (ValAsStr) params[0];
            setLocalValue(value, true);
        }
    }
    
    private ValAsStrEditPanel editor;
    private final ChangeListener listener;
    
    public static final ValAsStr NULL_VALUE = new ValAsStr("") {

        @Override
        public boolean equals(Object obj) {
            return obj == this;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            return hash;
        }
        
    };

    public ValAsStrEditorComponent() {
        super(new LocalModel());

        editor = new ValAsStrEditPanel();
        
        listener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                notifyComponentChanged();
            }
        };
    }

    @Override
    public ValAsStrEditPanel getEditorComponent() {
        return editor;
    }

    @Override
    public void updateModelValue() {
        getModel().updateValue(editor.getValue());
    }

    @Override
    protected void updateEditorComponent() {
        try {
            editor.setValue(getModel().getContext().getContextType().getTypeId(), getModel().getLocalValue());
        } catch (Exception e) {
            editor.setValue(getModel().getContext().getContextType().getTypeId(), NULL_VALUE);
        }
    }

    @Override
    protected void connectEditorComponent() {
        editor.addChangeListener(listener);
    }

    @Override
    protected void disconnectEditorComponent() {
        editor.removeChangeListener(listener);
    }
}