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

package org.radixware.kernel.designer.ads.editors.clazz.enumeration;

import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.ads.AdsValAsStr.IValueController;
import org.radixware.kernel.designer.ads.editors.common.adsvalasstr.AdsValAsStrEditor;
import org.radixware.kernel.designer.common.dialogs.components.values.ContextableValueEditorPanel;
import org.radixware.kernel.designer.common.dialogs.components.values.EditorLayout;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeEvent;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeListener;


class InitialValueEditorComponent extends ContextableValueEditorPanel<AdsValAsStr, IValueController> {

    protected final AdsValAsStrEditor editor;
    protected IValueController context;

    public InitialValueEditorComponent() {
        super();

        setLayout(new EditorLayout());

        editor = new AdsValAsStrEditor();
        editor.addValueChangeListener(new ValueChangeListener<AdsValAsStr>() {

            @Override
            public void valueChanged(ValueChangeEvent<AdsValAsStr> e) {
                fireValueChange(e.newValue, e.oldValue);
                
                commit();
            }
        });

        add(editor, EditorLayout.LEADER_CONSTRAINT);
    }

    @Override
    public void update() {

    }

    @Override
    public void open(IValueController context, AdsValAsStr currValue) {
        if (context != null) {
            this.context = context;
            editor.open(context, currValue);
        }
    }

    @Override
    public void open(IValueController context) {
        if (context != null) {
            this.context = context;
            editor.open(context);
        }
    }

    @Override
    public AdsValAsStr getValue() {
        if (isSetValue()) {
            return editor.getValue();
        } else {
            return null;
        }
    }

    @Override
    public void commit() {
        editor.commit();
    }

    @Override
    public boolean isSetValue() {
        return isOpened() && editor.isSetValue();
    }

    @Override
    public void setValue(AdsValAsStr value) {
        editor.setValue(value);
    }

    @Override
    public InitialValueEditorComponent getEditorComponent() {
        return this;
    }

    @Override
    public boolean isValueChanged() {
        return editor.isValueChanged();
    }

    @Override
    public boolean isOpened() {
        return context != null && editor.isOpened();
    }

    @Override
    public void setEditable(boolean editable) {
        editor.setEditable(editable);
    }

    @Override
    public boolean isEditable() {
        return editor.isEditable();
    }

    @Override
    public AdsValAsStr getNullValue() {
        return AdsValAsStr.NULL_VALUE;
    }
}
