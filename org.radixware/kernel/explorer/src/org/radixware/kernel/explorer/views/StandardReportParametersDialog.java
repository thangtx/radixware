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

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QShowEvent;
import java.util.ArrayList;
import java.util.List;

import com.trolltech.qt.gui.QDialogButtonBox;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.RadReportPresentationDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.IExplorerModelWidget;

import org.radixware.kernel.explorer.widgets.PropertiesGrid;
import org.radixware.kernel.explorer.widgets.TabSet;
import org.radixware.kernel.explorer.widgets.commands.CommandToolBar;

public final class StandardReportParametersDialog extends ReportParamDialogView {

    private final QMainWindow content = new QMainWindow();
    private final CommandToolBar commandBar;
    private boolean wasShown;

    public StandardReportParametersDialog(final IClientEnvironment environment) {
        super(environment);
        setMaximumSize(700, 500);
        commandBar = new CommandToolBar(environment);
        final QWidget top = new QWidget(this);
        setLayout(new QVBoxLayout(this));
        layout().addWidget(top);
        top.setLayout(WidgetUtils.createVBoxLayout(top));
        top.layout().addWidget(content);
        content.addToolBar(commandBar);
    }
    private IExplorerModelWidget widget;

    @Override
    public void open(final Model model_) {
        super.open(model_);
        commandBar.setModel(model_);
        final RadReportPresentationDef reportDef = getReportModel().getReportPresentationDef();
        if (reportDef.getEditorPages().getTopLevelPages().isEmpty()) {//PropertiesGrid opening
            final PropertiesGrid propGrid = new PropertiesGrid(this,getEnvironment());
            for (Id propertyId : reportDef.getPropertyIds()) {
                propGrid.addProperty(getReportModel().getProperty(propertyId));
            }
            propGrid.bind();
            content.setCentralWidget(propGrid);
            widget = propGrid;
        } else {//TabSet opening

            final List<Id> idsWithLevelOne = new ArrayList<>();

            for (RadEditorPageDef page : reportDef.getEditorPages().getTopLevelPages()) {
                idsWithLevelOne.add(page.getId());
            }

            final List<EditorPageModelItem> modelItems = new ArrayList<>();
            for (Id id : idsWithLevelOne) {
                final EditorPageModelItem modelItem = getReportModel().getEditorPage(id);
                modelItems.add(modelItem);
            }
            final TabSet tabSet = new TabSet(model_.getEnvironment(), this, modelItems, model_.getDefinition().getId().toString());
            tabSet.setParent(content);
            tabSet.bind();
            content.setCentralWidget(tabSet);
            widget = tabSet;
        }

        {//Buttons setup
            final QDialogButtonBox btnBox = new QDialogButtonBox(this);
            setupButtonBox(btnBox);
            layout().addWidget(btnBox);
        }
        content.show();
        opened.emit(this);
    }

    public void setCommandBarHidden(final boolean hidden) {
        commandBar.setPersistentHidden(hidden);
    }

    @Override
    public boolean setFocusedProperty(final Id propertyId) {
        final Property property;
        try {
            property = getReportModel().getProperty(propertyId);
        } catch (RuntimeException ex) {
            final String message =
                    getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot set focus to editor of property #%s: %s\n%s");
            final String reason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex);
            final String stack = ClientException.exceptionStackToString(ex);
            getEnvironment().getTracer().debug(String.format(message, String.valueOf(propertyId), reason, stack));
            return false;
        }
        if (property != null && property.getOwner().getView() == this) {
            return widget.setFocus(property);
        }
        return false;
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
    public QSize sizeHint() {
        if (!wasShown && widget!=null && widget.asQWidget()!=null){
            widget.asQWidget().updateGeometry();
        }
        return super.sizeHint();
    }        

    @Override
    protected void showEvent(final QShowEvent event) {
        wasShown = true;
        super.showEvent(event);
    }
}
