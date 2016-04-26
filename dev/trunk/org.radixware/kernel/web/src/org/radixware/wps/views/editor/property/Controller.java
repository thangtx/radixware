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

package org.radixware.wps.views.editor.property;

import org.radixware.kernel.common.client.editors.property.PropEditorController;
import org.radixware.kernel.common.client.editors.property.PropEditorOptions;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.UnacceptableInput;
import org.radixware.kernel.common.client.widgets.IModificationListener;
import org.radixware.wps.rwt.UIObject;


class Controller extends PropEditorController<AbstractPropEditor> {

    public Controller(AbstractPropEditor editor, Property property) {
        super(property, editor);
    }

    @Override
    protected boolean isPropVisualizerInUITree() {
        return ((UIObject) getPropEditor()).getParent() != null;
    }

    @Override
    protected IModificationListener findParentModificationListener() {
        for (UIObject parentWidget = getPropEditor().getParent(); parentWidget!=null; parentWidget=parentWidget.getParent()){
            if (parentWidget instanceof IModificationListener){
                return (IModificationListener)parentWidget;
            }
        }
        return null;
    }

    @Override
    protected void closeEditor() {
        getPropEditor().close();
    }

    @Override
    protected void updateSettings() {
        getPropEditor().updateSettings();
    }

    @Override
    protected void updateEditor(final Object value, final PropEditorOptions options) {
        final Object initialValue;
        if (getProperty().getOwner() instanceof FilterModel){
            initialValue = value;
        }else{
            initialValue = getPropertyInitialValue();
        }
        getPropEditor().updateEditor(value, initialValue, options);
    }

    @Override
    protected Object getCurrentValueInEditor() {
        return getPropEditor().getCurrentValueInEditor();
    }
    
    @Override
    protected UnacceptableInput getUnacceptableInput() {
        return getPropEditor().getUnacceptableInput();
    }
}
