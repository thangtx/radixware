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
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.widgets.arreditor.AbstractArrayEditorDelegate;
import org.radixware.kernel.explorer.editors.valeditors.ValConstSetEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditorFactory;

public class ArrayEditorDelegate extends AbstractArrayEditorDelegate<ValEditor,QWidget>{
    
    @Override
    public ValEditor createEditor(final QWidget parent,
                                               final IClientEnvironment environment,
                                               final ArrayItemEditingOptions options,
                                               final int index,
                                               final List<Object> currentValues) {
        final ValEditor editor;
        if (options.getEditMask() instanceof EditMaskConstSet) {
            EditMaskConstSet mask = (EditMaskConstSet) options.getEditMask();
            if (!options.isDuplicatesEnabled() && currentValues!=null && !currentValues.isEmpty()) {
                final List<Object> values = new ArrayList<>(currentValues); 
                values.remove(index);
                mask =
                    excludeExistingItems(environment.getApplication(), mask, values);
            }
            editor = new ValConstSetEditor(environment, parent, mask, options.isMandatory(), false);
        } else {
            editor = 
                ValEditorFactory.getDefault().createValEditor(options.getValType(), options.getEditMask(), environment, parent);
            editor.setMandatory(options.isMandatory());
        }
        return editor;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void setValueToEditor(final ValEditor editor, final Object value) {        
        editor.setValue(value);
    }

    @Override
    public Object getValueFromEditor(final ValEditor editor) {
        return editor.getValue();
    }
    
}
