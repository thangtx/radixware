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

package org.radixware.kernel.designer.common.dialogs.components.values;


public abstract class EditorFactory<EditorType extends ICompositeEditor<?>> implements IValueEditorFactory<EditorType> {

    private final EditorType compositeEditor;
    protected final ComponentSequence<Integer> componentSequence;

    protected EditorFactory(EditorType editor) {
        this.compositeEditor = editor;

        componentSequence = new ComponentSequence<>(editor.getComponent());
    }

    public void setEditor(IValueEditorComponent editor) {
        this.getEditor().setEditor(editor);
        componentSequence.addComponent(0, editor.getEditorComponent(), EditorLayout.LEADER_CONSTRAINT);
    }

    public void addFeature(IValueEditorFactory.IFeature<EditorType> feature) {
        assert feature != null : "Null future";
        if (feature != null) {
            getEditor().addFeature(feature);
            componentSequence.addComponent(componentSequence.getSequenceCount() + 1, feature.install(getEditor()), null);
        }
    }

    public void clear() {
        componentSequence.clear();
        getEditor().disassemble();
    }

    @Override
    public boolean assembly() {

        clear();

        if (assemblyImpl()) {
            componentSequence.build();
            getEditor().updateFutures();
            getEditor().getComponent().updateUI();
            return true;
        }
        return false;
    }

    protected final EditorType getEditor() {
        return compositeEditor;
    }

    protected void addDefaultFeatures() {
    }

    protected abstract boolean assemblyImpl();
}