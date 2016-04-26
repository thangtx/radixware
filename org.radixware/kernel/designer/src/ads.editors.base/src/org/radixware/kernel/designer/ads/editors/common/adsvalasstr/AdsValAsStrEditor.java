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
 * 9/16/11 5:26 PM
 */
package org.radixware.kernel.designer.ads.editors.common.adsvalasstr;

import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ComponentListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.ads.AdsValAsStr.IValueController;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.components.values.IValueEditorFactory.IFeature;
import org.radixware.kernel.designer.common.dialogs.components.values.*;

/**
 * Editor panel for AdsValAsStr
 *
 */
public final class AdsValAsStrEditor extends ContextableValueEditorPanel<AdsValAsStr, IValueController> implements ICompositeEditor<AdsValAsStr> {

    private IValueController context;
    private BaseEditorComponent<?> editor;
    private final ValueChangeListener<AdsValAsStr> changeListener;
    private final List<IValueEditorFactory.IFeature<?>> futures;

    public AdsValAsStrEditor() {
        super();

        super.setLayout(new EditorLayout());

        changeListener = new ValueChangeListener<AdsValAsStr>() {

            @Override
            public void valueChanged(ValueChangeEvent<AdsValAsStr> e) {
                fireValueChange(e.newValue, e.oldValue);
            }
        };

        futures = new ArrayList<>();
        AdsValAsStrEditorBuilder.createInstance(this, null, null).buildDefaultEditor();

        addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChanged(ValueChangeEvent e) {
                updateFutures();
            }
        });
    }

    @Override
    public void update() {
        check();
        setValue(getContext().getValue());
    }
    
    private void removeMouseListeners(){
        if (this.editor != null){
            Component editorComponent = this.editor.getEditorComponent();
            for (MouseListener listener : editorComponent.getMouseListeners()){
                editorComponent.removeMouseListener(listener);
            }
        }
    }

    @Override
    public void open(IValueController context) {
        this.context = context; 
        removeMouseListeners();
        this.editor = null;
        assembly(context, context.getValue());
    }

    @Override
    public void open(IValueController context, AdsValAsStr currValue) {
        this.context = context;
        removeMouseListeners();
        this.editor = null;

        assembly(context, currValue);
    }

    @Override
    public boolean isOpened() {
        return context != null;
    }

    @Override
    public AdsValAsStr getValue() {
        check();
        AdsValAsStr value = editor.getValue();
        return value != null ? value : AdsValAsStr.NULL_VALUE;
    }

    @Override
    public void commit() {
        check();
        editor.commit();
    }

    @Override
    public boolean isSetValue() {
        return isOpened() && editor.isSetValue();
    }

    @Override
    public boolean isValueChanged() {
        check();
        return editor.isValueChanged();
    }

    @Override
    public void setValue(AdsValAsStr value) {
        check();
        if (value != null && getValue().typeEquals(value.getValueType())) {
            editor.setValue(value);
        } else {
            AdsValAsStr oldValue = editor.getValue();
            assembly(context, value);

            check();
            AdsValAsStr currValue = editor.getValue();
            if (!Utils.equals(oldValue, editor.getValue())) {
                editor.fireValueChange(currValue, oldValue);
            }
        }
    }

    @Override
    public boolean isEditable() {
        check();
        return editor.isEditable();
    }

    @Override
    public void setEditable(boolean editable) {
        check();
        editor.setEditable(editable);
    }

    @Override
    public final void setLayout(LayoutManager mgr) {
    }

    @Override
    public int getBaseline(int width, int height) {
        if (isOpened()) {
            return editor.getBaseline(width, height);
        }
        return -1;
    }

    @Override
    public void requestFocus() {
        if (isOpened()) {
            editor.requestFocus();
        }
    }

    @Override
    public boolean isFocusOwner() {
        if (isOpened()) {
            return editor.isFocusOwner();
        }
        return false;
    }

    @Override
    public AdsValAsStr getNullValue() {
        check();
        return editor.getNullValue();
    }

    @Override
    public boolean isValidValue(AdsValAsStr value) {
        check();
        return editor.isValidValue(value);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (isOpened()) {
            editor.setEnabled(enabled);
        }
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled() && (!isOpened() || editor.isEnabled());
    }

    @Override
    public void addFeature(IFeature<?> feature) {
        assert feature != null : "Null future";
        if (feature != null) {
            futures.add(feature);
        }
    }

    @Override
    public void setEditor(IValueEditorComponent<AdsValAsStr> editor) {
        if (this.editor != null) {
            this.editor.removeValueChangeListener(changeListener);
        }
        if (editor != null) {
            editor.removeValueChangeListener(changeListener);
            editor.addValueChangeListener(changeListener);
        }
        this.editor = (BaseEditorComponent<?>) editor;
    }

    @Override
    public List<IFeature<?>> getFutures() {
        return futures;
    }

    @Override
    public IValueEditorComponent<AdsValAsStr> getSingleEditor() {
        return editor;
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void disassemble() {
        futures.clear();
        this.removeAll();
    }

    @Override
    public void updateFutures() {
        for (final IFeature feature : futures) {
            feature.checkCondition();
        }
    }

    private void check() {
        if (!isOpened()) {
            throw new NullPointerException("Context is null (editor not opened)");
        }
    }

    private void assembly(IValueController context, AdsValAsStr currValue) {
        final IValueEditorFactory<AdsValAsStrEditor> factory = AdsValAsStrEditorBuilder.createInstance(this, context, currValue);

        if (!factory.assembly()) {
            Logger.getLogger(AdsValAsStrEditor.class.getName()).log(Level.WARNING, "Editor for type '" + context.getContextType() + "' not set");

            factory.buildDefaultEditor();
        }

        if (editor == null || (Object) editor == this) {
            Logger.getLogger(AdsValAsStrEditor.class.getName()).log(Level.WARNING, "Invalid editor");
        }
    }

    IValueController getContext() {
        return context;
    }
    private IContextableValueEditorComponent<AdsValAsStr, IValueController> externalEditor;
    private final ValueChangeListener<AdsValAsStr> externalEditorListener = new ValueChangeListener<AdsValAsStr>() {

        @Override
        public void valueChanged(ValueChangeEvent<AdsValAsStr> e) {
            setValue(e.newValue);
        }
    };

    public boolean connectExternalEditor(IContextableValueEditorComponent<AdsValAsStr, IValueController> editor) {

        if (editor != null) {
            disconnectExternalEditor();
            this.externalEditor = editor;
            editor.open(context, getValue());
            editor.addValueChangeListener(externalEditorListener);
            return true;
        }

        return false;
    }

    public boolean disconnectExternalEditor() {

        if (externalEditor != null) {
            externalEditor.removeValueChangeListener(externalEditorListener);
            externalEditor = null;
        }
        return true;
    }
}
