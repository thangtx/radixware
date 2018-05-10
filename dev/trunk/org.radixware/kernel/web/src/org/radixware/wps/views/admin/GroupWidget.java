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
package org.radixware.wps.views.admin;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.html.Table;
import org.radixware.wps.rwt.AbstractContainer;
import org.radixware.wps.rwt.GroupBox;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.views.editors.valeditors.ValEditorController;

class GroupWidget extends GroupBox {

    private final Table table = new Table();
    private final List<UIObject> childrenList = new LinkedList<>();
    private final AbstractContainer container = new AbstractContainer();
    GroupWidget(String title) {
        setTitle(title);
        setSizePolicy(SizePolicy.MINIMUM_EXPAND, SizePolicy.MINIMUM_EXPAND);
        setAdjustMode(AdjustMode.EXPAND_HEIGHT_BY_CONTENT);
        container.getHtml().layout("$RWT.propertiesGrid.layout");
        table.setCss("width", "100%");
        createColumns(2);
        container.getHtml().add(table);
        add(container);
    }

    public void addNewRow(String title, ValEditorController editorController) {
        Table.Row row = table.addRow();
        Table.DataCell labelCell = row.addCell();
        labelCell.setAttr("role", "label");
        labelCell.setAttr("width", "1px");
        Label lbl = new Label(title);
        lbl.setParent(container);
        lbl.getHtml().setCss("white-space", "nowrap");
        labelCell.add(lbl.getHtml());
        Table.DataCell ediditorCell = row.addCell();
        ediditorCell.setAttr("role", "editor");
        UIObject editor = ((UIObject) editorController.getValEditor());
        editor.setParent(container);
        ediditorCell.add(editor.getHtml());
        childrenList.add(editor);
    }

    public String getTableHtmlId() {
        return table.getId();
    }

    public final void createColumns(final int count) {
        Html column;
        for (int i = 0; i < count; i++) {
            column = new Html("col");
            table.add(column);
        }
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject obj = super.findObjectByHtmlId(id); 
        if (obj != null) {
            return obj;
        } else {
            for (UIObject child : childrenList) {
                obj = child.findObjectByHtmlId(id);
                if (obj != null) {
                    return obj;
                }
            }
        }
        return null;
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor); 
        for (UIObject obj : childrenList) {
            obj.visit(visitor);
        }
    }  
}
