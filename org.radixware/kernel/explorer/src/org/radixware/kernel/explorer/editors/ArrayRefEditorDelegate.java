/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.editors;

import org.radixware.kernel.common.client.widgets.arreditor.ArrayItemEditingOptions;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QWidget;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.arreditor.AbstractArrayRefEditorDelegate;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.utils.WidgetUtils;

public class ArrayRefEditorDelegate extends AbstractArrayRefEditorDelegate<ValEditor,QWidget>{
    
    private final class ValRefEditor extends ValEditor<Reference> {
        
        private final ArrayItemEditingOptions editingOptions;
        private final List<Reference> currentValues;

        public ValRefEditor(final IClientEnvironment environment, 
                                     final QWidget parent, 
                                     final ArrayItemEditingOptions options,
                                     final List<Reference> values) {
            super(environment, parent, options.getEditMask(), options.isMandatory(), false);
            getLineEdit().setReadOnly(true);
            final QAction action = new QAction(this);
            action.setToolTip(environment.getMessageProvider().translate("ArrayEditor", "Select Object"));
            action.setIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.SELECTOR));
            action.triggered.connect(this, "selectEntity()");
            action.setObjectName("select_object");
            addButton(null, action);
            editingOptions = options;
            currentValues = values;
        }
        
        private void selectEntity(){
            final Reference currentValue = getValue();
            final Reference newValue = 
                ArrayRefEditorDelegate.this.selectEntity(this, getEnvironment(), editingOptions, currentValue, currentValues);
            if (currentValue!=newValue){
                setValue(newValue);
            }
        }
    }

    @Override
    public ValEditor<Reference> createEditor(final QWidget parent, 
                                                                   final IClientEnvironment environment, 
                                                                   final ArrayItemEditingOptions options, 
                                                                   final int index,
                                                                   final List<Object> currentValues) {
        final List<Reference> refs = new LinkedList<>();
        if (currentValues!=null && !currentValues.isEmpty()){
            for (Object val: currentValues){
                if (val instanceof Reference){
                    refs.add((Reference)val);
                }
            }
        }
        return new ValRefEditor(environment, parent, options, refs);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void setValueToEditor(final ValEditor editor, final Object value) {
        editor.setValue((Reference)value);
    }

    @Override
    public Object getValueFromEditor(final ValEditor editor) {
        return editor.getValue();
    }    
    
    @Override
    protected Model findNearestModel(final IWidget arrayEditor) {
        return WidgetUtils.findNearestModel((QWidget)arrayEditor);
    }       
}
