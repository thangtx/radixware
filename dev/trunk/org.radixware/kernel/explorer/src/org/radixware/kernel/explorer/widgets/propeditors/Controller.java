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
import org.radixware.kernel.common.client.editors.property.PropEditorController;
import org.radixware.kernel.common.client.editors.property.PropEditorOptions;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.UnacceptableInput;
import org.radixware.kernel.common.client.widgets.IModificationListener;


class Controller extends PropEditorController<AbstractPropEditor> {
    

    public Controller(Property property, AbstractPropEditor propEditor) {
        super(property, propEditor);
    }
    
    public Controller(AbstractPropEditor propEditor){
        super(propEditor);
    }

    @Override
    protected boolean isPropVisualizerInUITree() {
        return getWidget().parentWidget() != null;
    }

    @Override
    protected IModificationListener findParentModificationListener() {
        for (QWidget parentWidget = getWidget().parentWidget();
                parentWidget != null && parentWidget.window() == getWidget().window();
                parentWidget = parentWidget.parentWidget()) {
            if (parentWidget instanceof IModificationListener) {
                return (IModificationListener) parentWidget;
            }
        }
        return null;
    }
       
    @Override
    protected void closeEditor() {
        getPropEditor().closeEditor();
    }

    @Override
    protected void updateSettings() {
        getPropEditor().updateSettings();
    }

    @Override
    protected void updateEditor(Object value, PropEditorOptions options) {
        getPropEditor().updateEditor(value, options);
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
