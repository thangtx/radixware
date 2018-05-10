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

package org.radixware.wps.views.editor.array;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.widgets.arreditor.AbstractArrayEditorDelegate;
import org.radixware.kernel.common.client.widgets.arreditor.ArrayItemEditingOptions;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.views.editors.valeditors.ValConstSetEditorController;
import org.radixware.wps.views.editors.valeditors.ValEditorController;
import org.radixware.wps.views.editors.valeditors.ValEditorFactory;


public class ArrayEditorDelegate extends AbstractArrayEditorDelegate<ValEditorController,UIObject>{

    @Override
    public ValEditorController createEditor(final UIObject parent,
                                                              final IClientEnvironment environment,
                                                              final ArrayItemEditingOptions options,
                                                              final int index,
                                                              final List<Object> currentValues) {
            
        ValEditorController controller;
        if (options.getEditMask() instanceof EditMaskConstSet) {
            EditMaskConstSet mask = (EditMaskConstSet) options.getEditMask();
            if (!options.isDuplicatesEnabled() && currentValues!=null && !currentValues.isEmpty()) {
                final List<Object> values = new ArrayList<>(currentValues); 
                values.remove(index);
                mask =
                    excludeExistingItems(environment.getApplication(), mask, values);
            }
            controller = new ValConstSetEditorController(environment, mask);
        } else {
            controller = 
                ValEditorFactory.getDefault().createValEditor(options.getValType(), options.getEditMask(), environment).getController();
        }
        controller.setMandatory(options.isMandatory());
        return controller;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValueToEditor(ValEditorController editor, Object value) {
        editor.setValue(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getValueFromEditor(ValEditorController editor) {
        return editor.getValue();
    }

}
