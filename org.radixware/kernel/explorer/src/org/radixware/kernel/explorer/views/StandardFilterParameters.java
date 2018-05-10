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

package org.radixware.kernel.explorer.views;

import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.PropertiesGrid;
import com.trolltech.qt.gui.QSizePolicy;
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

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.widgets.IExplorerModelWidget;
import org.radixware.kernel.explorer.widgets.TabSet;


public class StandardFilterParameters extends FilterParameters {

    private final static int DEFAULT_COLUMNS_COUNT = 2;

    public StandardFilterParameters(IClientEnvironment environment) {
        super(environment);
        content.setLayout(WidgetUtils.createVBoxLayout(content));
    }
    private IExplorerModelWidget widget;

    @Override
    public void open(Model model) {
        super.open(model);
        if (filter.getFilterDef().getParameters().isEmpty()) {
            setVisible(false);
        } else {
            final RadFilterDef filterDef = getFilterModel().getFilterDef();
            if (filterDef.getEditorPages().getTopLevelPages().isEmpty()) {//PropertiesGrid opening
                final PropertiesGrid propGrid = new PropertiesGrid(content,getEnvironment());
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
                widget = propGrid;
            } else {//TabSet opening

                final List<Id> idsWithLevelOne = new ArrayList<>();

                for (RadEditorPageDef page : filterDef.getEditorPages().getTopLevelPages()) {
                    idsWithLevelOne.add(page.getId());
                }

                final List<EditorPageModelItem> modelItems = new ArrayList<>();
                for (Id id : idsWithLevelOne) {
                    final EditorPageModelItem modelItem = getFilterModel().getEditorPage(id);
                    modelItems.add(modelItem);
                }
                final TabSet tabSet = new TabSet(model.getEnvironment(), content, this, modelItems, model.getDefinition().getId().toString());
                widget = tabSet;
            }
            widget.bind();
            widget.asQWidget().setSizePolicy(QSizePolicy.Policy.Minimum, QSizePolicy.Policy.Minimum);            
            content.layout().addWidget(widget.asQWidget());
            setFocusProxy(widget.asQWidget());
        }
        opened.emit(content);
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
        final Property property;
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
    
    @Override
    public void setCollapsable(final boolean collapsable) {
        super.setCollapsable(collapsable);
        if (widget!=null){
            widget.asQWidget().setSizePolicy(QSizePolicy.Policy.Minimum, collapsable ? QSizePolicy.Policy.Maximum : QSizePolicy.Policy.Minimum);
        }
    }    

    private FilterModel getFilterModel() {
        return (FilterModel) getModel();
    }
}
