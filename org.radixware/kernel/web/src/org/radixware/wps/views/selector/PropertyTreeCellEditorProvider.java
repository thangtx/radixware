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
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.tree.Node;
import org.radixware.wps.rwt.tree.Node.INodeCellEditor;
import org.radixware.wps.rwt.tree.Tree;
import org.radixware.wps.views.editor.property.PropEditor;


public final class PropertyTreeCellEditorProvider implements Tree.ICellEditorProvider {


    private final static PropertyTreeCellEditorProvider INSTANCE = new PropertyTreeCellEditorProvider();

    private PropertyTreeCellEditorProvider() {
    }

    public static PropertyTreeCellEditorProvider getInstance() {
        return INSTANCE;
    }

    @Override
    public INodeCellEditor newCellEditor(Node node, int columnIndex) {
        return new PropCellEditor();
    }

    private static class PropCellEditor implements INodeCellEditor {

        private Property prop;
        private PropEditor editor;

        public PropCellEditor() {
        }

        @Override
        public void setValue(Node n, int c, Object value) {
            this.prop = (Property) value;
            if (!this.prop.isReadonly()) {
                editor = (PropEditor) prop.createPropertyEditor();
                editor.setPreferredHeight(21);
                editor.bind();
            } else {
                this.editor = null;
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
            return editor;
        }

        @Override
        public void cancelChanges() {

            if (editor != null) {
                editor.refresh(prop);
            }
        }
    }
}
