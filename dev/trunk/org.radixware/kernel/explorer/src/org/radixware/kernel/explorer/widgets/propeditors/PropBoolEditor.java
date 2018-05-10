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

package org.radixware.kernel.explorer.widgets.propeditors;

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EWidgetMarker;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskBool;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.editors.valeditors.AdvancedValBoolEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValBoolEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditorFactory;

public class PropBoolEditor extends PropEditor {

    private static class ValEditorFactoryImpl extends ValEditorFactory {

        public static final ValEditorFactoryImpl INSTANCE = new ValEditorFactoryImpl();

        private ValEditorFactoryImpl() {
        }

        @Override
        public ValEditor createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment environment, final QWidget parentWidget) {
            if (editMask instanceof EditMaskBool) {
                return new AdvancedValBoolEditor<>(environment, parentWidget, editMask, false, false);
            } else {
                return new ValBoolEditor(environment, parentWidget, editMask, false, false);
            }
        }
    }

    public PropBoolEditor(final Property property) {
        this(property, getValEditorFactory());
    }
    
    @SuppressWarnings("LeakingThisInConstructor")
    protected PropBoolEditor(final Property property, final ValEditorFactory factory){
        super(property, factory);        
        if (property.getEditMask() instanceof EditMaskBool) {
            AdvancedValBoolEditor valEditor = (AdvancedValBoolEditor) getValEditor();            
            setFocusProxy(valEditor.getCheckBox());
            valEditor.getCheckBox().installEventFilter(focusListener);
        } else {
            ValBoolEditor valEditor = (ValBoolEditor) getValEditor();
            setFocusProxy(valEditor.getCheckBox());
            valEditor.getCheckBox().installEventFilter(focusListener);
        }
        //при смене галочки значение сразу записывается в свойство:
        edited.connect(this, "finishEdit();");
    }

    @Override
    void setProperty(final Property property) {
        super.setProperty(property);
        if (property!=null){
            edited.connect(this, "finishEdit();");
        }
    }
    
    @Override
    public void setHighlightedFrame(final boolean isHighlighted) {
        final ValEditor valEditor = getValEditor();
        if (valEditor != null) {     
            valEditor.setHighlightedFrame(isHighlighted);
        }
    }
    
    public static ValEditorFactory getValEditorFactory(){
        return ValEditorFactoryImpl.INSTANCE;
    }

    @Override
    public final EWidgetMarker getWidgetMarker() {
        return EWidgetMarker.BOOL_PROP_EDITOR;
    }    
}
