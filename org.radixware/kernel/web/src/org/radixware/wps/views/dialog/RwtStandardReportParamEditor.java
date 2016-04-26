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

package org.radixware.wps.views.dialog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.RadReportPresentationDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.views.CommandToolBar;
import org.radixware.wps.views.editor.PropertiesGrid;
import org.radixware.wps.views.editor.TabSet;


public class RwtStandardReportParamEditor extends RwtReportParamDialog {

    private CommandToolBar commandBar;
    private TabSet tabSet;
    private PropertiesGrid propGrid;

    public RwtStandardReportParamEditor(IClientEnvironment environment) {
        super(environment);
        this.commandBar = new CommandToolBar();
        add(commandBar);
        commandBar.getAnchors().setRight(new Anchors.Anchor(1, -3));
        commandBar.setTop(3);
        commandBar.setLeft(3);
    }

    @Override
    public void open(Model model_) {
        super.open(model_);
        html.setAttr("dlgId", model_.getDefinition().getId().toString());
        UIObject widget;
        commandBar.setModel(model_);
        final RadReportPresentationDef reportDef = getReportModel().getReportPresentationDef();
        if (reportDef.getEditorPages().getTopLevelPages().isEmpty()) {//PropertiesGrid opening
            List<Property> props = new LinkedList<Property>();

            for (Id propertyId : reportDef.getPropertyIds()) {
                props.add(getReportModel().getProperty(propertyId));
            }
            widget = propGrid = new PropertiesGrid(props);
            add(propGrid);
            propGrid.bind();
        } else {//TabSet opening
            final RadReportPresentationDef def = getReportModel().getReportPresentationDef();
            List<Id> idsWithLevelOne = new ArrayList<Id>();

            for (RadEditorPageDef page : def.getEditorPages().getTopLevelPages()) {
                idsWithLevelOne.add(page.getId());
            }

            List<EditorPageModelItem> modelItems = new ArrayList<EditorPageModelItem>();
            for (Id id : idsWithLevelOne) {
                EditorPageModelItem modelItem = getReportModel().getEditorPage(id);
                modelItems.add(modelItem);
            }
            widget = tabSet = new TabSet((WpsEnvironment) model_.getEnvironment(), this, modelItems, model_.getDefinition().getId());
            tabSet.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
            add(tabSet);
            tabSet.bind();
        }
        if (widget != null) {
            widget.getAnchors().setBottom(new Anchors.Anchor(1, -3));
            widget.getAnchors().setRight(new Anchors.Anchor(1, -3));
            if (commandBar.isVisible()) {
                widget.getAnchors().setTop(new Anchors.Anchor(1, 3, commandBar));
            } else {
                widget.getAnchors().setTop(new Anchors.Anchor(0, 3));
            }
            widget.setLeft(3);
        }


        setupButtonBox();

        fireOpened();
    }

    public void setCommandBarHidden(final boolean hidden) {
        commandBar.setPersistentHidden(hidden);
    }

    @Override
    public boolean setFocusedProperty(final Id propertyId) {
        Property property = null;
        try {
            property = getReportModel().getProperty(propertyId);
        } catch (RuntimeException ex) {
            final String message = getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot set focus to editor of property #%s: %s\n%s");
            final String reason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex);
            final String stack = ClientException.exceptionStackToString(ex);
            getEnvironment().getTracer().debug(String.format(message, String.valueOf(propertyId), reason, stack));
            return false;
        }
        if (property != null && property.getOwner().getView() == this) {
            if (propGrid != null) {
                return propGrid.setFocus(property);
            } else {
                return tabSet.setFocus(property);
            }
        }
        return false;
    }

    @Override
    public void finishEdit() {
        if (propGrid != null) {
            propGrid.finishEdit();
        } else {
            tabSet.finishEdit();
        }
        super.finishEdit();
    }

    protected void setupButtonBox() {
        clearCloseActions();
        final ArrStr buttons = getReportModel().getButtons();
        if (buttons != null) {
            for (String buttonType : buttons) {
                IPushButton button = getReportModel().createButton(buttonType);
                button.setObjectName(buttonType);
                addCloseAction(button, DialogResult.NONE);
            }
        }
    }
}
