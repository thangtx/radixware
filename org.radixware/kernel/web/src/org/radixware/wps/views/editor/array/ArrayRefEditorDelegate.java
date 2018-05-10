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

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.arreditor.AbstractArrayRefEditorDelegate;
import org.radixware.kernel.common.client.widgets.arreditor.ArrayItemEditingOptions;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.utils.UIObjectUtils;
import org.radixware.wps.views.editors.valeditors.ValEditorController;
import org.radixware.wps.views.editors.valeditors.ValReferenceEditorController;


public class ArrayRefEditorDelegate extends AbstractArrayRefEditorDelegate<ValEditorController,UIObject>{

    @Override
    public ValEditorController createEditor(final UIObject parent, 
                                                              final IClientEnvironment environment, 
                                                              final ArrayItemEditingOptions options, 
                                                              final int index, 
                                                              final List<Object> currentValues) {
        final ValReferenceEditorController controller = new ValReferenceEditorController(environment);
        controller.setMandatory(options.isMandatory());
        final List<Reference> refs = new LinkedList<>();
        if (currentValues!=null && !currentValues.isEmpty()){
            for (Object val: currentValues){
                if (val instanceof Reference){
                    refs.add((Reference)val);
                }
            }
        }
        final ToolButton button = new ToolButton();
        button.setToolTip(environment.getMessageProvider().translate("ArrayEditor", "Select Object"));
        final ClientIcon icon = ClientIcon.Definitions.SELECTOR;
        button.setIcon(environment.getApplication().getImageManager().getIcon(icon));
        button.addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(final IButton source) {
                final Reference currentValue = controller.getValue();
                final Reference newValue = 
                    ArrayRefEditorDelegate.this.selectEntity(parent, environment, options, currentValue, refs);
                if (newValue!=currentValue){
                    controller.setValue(newValue);
                }
            }
        });
        controller.addButton(button);
        return controller;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValueToEditor(final ValEditorController editor, final Object value) {
        editor.setValue(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getValueFromEditor(final ValEditorController editor) {
        return editor.getValue();
    }

    @Override
    protected Model findNearestModel(final IWidget arrayEditor) {
        return UIObjectUtils.findNearestModel((UIObject)arrayEditor);
    }
}
