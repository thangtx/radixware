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

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.filters.RadFilterDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;

import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.GridLayout;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.views.editor.PropertiesGrid;
import org.radixware.wps.views.editor.TabSet;


public class StandardFilterParameters extends FilterParameters {

    private final static int DEFAULT_COLUMNS_COUNT = 2;

    public StandardFilterParameters(IClientEnvironment environment) {
        super(environment);

    }
    private IModelWidget widget;

    @Override
    public void open(Model model) {
        super.open(model);
        if (filter.getFilterDef().getParameters().isEmpty()) {
            setVisible(false);
        } else {
            final RadFilterDef filterDef = getFilterModel().getFilterDef();
            if (filterDef.getEditorPages().getTopLevelPages().isEmpty()) {//PropertiesGrid opening
                final PropertiesGrid propGrid = new PropertiesGrid();
                propGrid.setObjectName("propertiesGrid");
                int column = 0, row = 0;
                for (ISqmlParameter parameterDef : filterDef.getParameters().getAll()) {
                    if ((column % DEFAULT_COLUMNS_COUNT) == 0) {
                        row++;
                        column = 0;
                    }
                    propGrid.addProperty(getFilterModel().getProperty(parameterDef.getId()), column, row);
                    column++;
                }
                propGrid.bind();
                propGrid.adjustToContent(true);
                widget = propGrid;
            } else {//TabSet opening

                final List<Id> idsWithLevelOne = new ArrayList<Id>();

                for (RadEditorPageDef page : filterDef.getEditorPages().getTopLevelPages()) {
                    idsWithLevelOne.add(page.getId());
                }

                final List<EditorPageModelItem> modelItems = new ArrayList<EditorPageModelItem>();
                for (Id id : idsWithLevelOne) {
                    final EditorPageModelItem modelItem = getFilterModel().getEditorPage(id);
                    if (modelItem != null) {
                        modelItems.add(modelItem);
                    }
                }
                final TabSet tabSet = new TabSet((WpsEnvironment) model.getEnvironment(), this, modelItems, model.getDefinition().getId());
                tabSet.adjustToContent(true);
                widget = tabSet;
            }
            widget.bind();
            UIObject obj = (UIObject) widget;
            //UIObject customGrid = getCustomGrid();

            //add(0, obj);
            GridLayout.Row r = new GridLayout.Row();
            layout.add(0, r);
            GridLayout.Cell cell = new GridLayout.Cell();
            r.add(cell);
            cell.setVCoverage(100);
            cell.add(obj);

//            obj.setTop(0);
//            obj.setLeft(0);
//            obj.getAnchors().setRight(new Anchors.Anchor(1, -5));


//            if (customGrid != null) {
//                customGrid.getAnchors().setTop(new Anchors.Anchor(1, 5, obj));
//            }
        }
        fireViewOpened();
    }

    @Override
    public void finishEdit() {
        if (widget instanceof TabSet) {
            ((TabSet) widget).finishEdit();
        } else if (widget instanceof PropertiesGrid) {
            ((PropertiesGrid) widget).finishEdit();
        }
        super.finishEdit();
    }

    @Override
    public boolean setFocusedProperty(Id propertyId) {
        Property property = null;
        try {
            property = getFilterModel().getProperty(propertyId);
        } catch (RuntimeException ex) {
            return false;
        }
        if (property != null && property.getOwner().getView() == this) {
            return widget.setFocus(property);
        }
        return false;
    }

    private FilterModel getFilterModel() {
        return (FilterModel) getModel();
    }
}
