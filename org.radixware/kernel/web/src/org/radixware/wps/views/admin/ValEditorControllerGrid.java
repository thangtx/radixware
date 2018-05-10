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
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.views.editors.valeditors.ValEditorController;

public class ValEditorControllerGrid extends AbstractContainer {

    private final Table propertiesTable = new Table();
    private final List<UIObject> childrenList = new LinkedList<>();

    public ValEditorControllerGrid() {
        super();
        getHtml().add(propertiesTable);
        getHtml().layout("$RWT.propertiesGrid.layout");
        setupUI();
    }

    private void setupUI() {
        propertiesTable.setCss("width", "100%");
        propertiesTable.add(new Html("col"));
        propertiesTable.add(new Html("col"));
    }

    public void addLabeledEditor(String title, ValEditorController editorController) {
        Table.Row row = propertiesTable.addRow();
        Table.DataCell labelCell = row.addCell();
        labelCell.setAttr("role", "label");
        labelCell.setAttr("width", "1px");
        Label lbl = new Label(title);
        lbl.setParent(this);
        lbl.getHtml().setCss("white-space", "nowrap");
        labelCell.add(lbl.getHtml());
        Table.DataCell ediditorCell = row.addCell();
        ediditorCell.setAttr("role", "editor");
        UIObject editor = ((UIObject) editorController.getValEditor());
        editor.setParent(this);
        ediditorCell.add(editor.getHtml());
        childrenList.add(editor);
    }

    void addGroupWidgetRow(GroupWidget widget) {
        Table.DataCell settingsCell = propertiesTable.addRow().addCell();
        settingsCell.setAttr("innertable", widget.getTableHtmlId());
        settingsCell.setAttr("role", "group");
        settingsCell.setAttr("colspan", 2);
        settingsCell.add(widget.getHtml());
        widget.setParent(this);
        childrenList.add(widget);
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject obj = super.findObjectByHtmlId(id);
        if (obj != null) {
            return obj;
        } else {
            for (UIObject childObj : childrenList) {
                obj = childObj.findObjectByHtmlId(id);
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
