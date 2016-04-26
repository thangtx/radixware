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

package org.radixware.wps.views.selector;

import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.wps.rwt.IGrid.CellEditor;
import org.radixware.wps.rwt.IGrid.CellEditorProvider;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.views.editor.property.PropEditor;


public final class PropertyCellEditorProvider implements CellEditorProvider{
    
    private final static PropertyCellEditorProvider INSTANCE = new PropertyCellEditorProvider();
    
    private PropertyCellEditorProvider(){        
    }
        
    public static PropertyCellEditorProvider getInstance(){
        return INSTANCE;
    }

    private static class PropCellEditor implements CellEditor {

        private Property prop;
        private PropEditor editor;

        @Override
        public void setValue(final int r, final int c, final Object value) {
            if (prop!=value && value!=null){//NOPMD
                this.prop = (Property) value;                    
                editor = (PropEditor) prop.createPropertyEditor();
                editor.setPreferredHeight(21);
                editor.bind();
            }
        }

        @Override
        public Object getValue() {
            return prop;
        }

        @Override
        public void applyChanges() {
            if (editor != null) {
                editor.finishEdit();
            }
        }

        @Override
        public UIObject getUI() {
            return SelectorWidgetController.canUseStandardCheckBox(prop) ? null : editor;
        }

        @Override
        public void cancelChanges() {
            if (editor != null) {
                editor.refresh(prop);
            }
        }
    }    
    
    @Override
    public CellEditor newCellEditor(final int r, final int c) {
        return new PropCellEditor();
}
    
}
