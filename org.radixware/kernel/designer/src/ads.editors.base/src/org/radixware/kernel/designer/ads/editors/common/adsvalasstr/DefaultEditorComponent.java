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
 * 10/14/11 5:00 PM
 */
package org.radixware.kernel.designer.ads.editors.common.adsvalasstr;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.ads.AdsValAsStr.IValueController;


public class DefaultEditorComponent extends BaseEditorComponent<DefaultEditorComponent.LocalModel> {

    private JTextField defEditor;

    public static class LocalModel extends BaseEditorComponent.BaseEditorModel<AdsValAsStr> {

        @Override
        public void updateValue(Object... params) {
        }

        @Override
        protected AdsValAsStr toLocal(AdsValAsStr value) {
            return value;
        }

        @Override
        protected AdsValAsStr toExternal(AdsValAsStr local) {
            return local;
        }
    }

    public DefaultEditorComponent() {
        super(new LocalModel());
        defEditor = new JTextField(AdsValAsStr.NULL_VALUE.toString());
        defEditor.setEditable(false);
        defEditor.setEnabled(false);
    }

    @Override
    public JTextField getEditorComponent() {
        return defEditor;
    }

    @Override
    protected void updateModelValue() {
    }

    @Override
    protected void updateEditorComponent() {
        final IValueController context = getModel().getContext();

        try {
            if (context != null) {
                defEditor.setText(AdsValAsStr.DefaultPresenter.getAsString(getValue(), context.getContextDefinition(), context.getContextType()));
            } else if (getValue() != null) {
                defEditor.setText(String.valueOf(getValue()));
            } else {
                defEditor.setText(AdsValAsStr.NULL_VALUE.toString());
            }
        } catch (Exception e) {
            Logger.getLogger(DefaultEditorComponent.class.getName()).log(Level.INFO, null, e);
            defEditor.setText(AdsValAsStr.NULL_VALUE.toString());
        }
    }

    @Override
    protected void connectEditorComponent() {
    }

    @Override
    protected void disconnectEditorComponent() {
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public void setEditable(boolean editable) {
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        defEditor.setEnabled(false);
    }
}