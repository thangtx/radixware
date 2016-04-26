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
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.RadFormDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.VerticalBox;
import org.radixware.wps.views.CommandToolBar;
import org.radixware.wps.views.editor.TabSet;


public class RwtStandardForm extends RwtForm {

    private CommandToolBar commandBar;
    private TabSet tabSet;
    private VerticalBox content = new VerticalBox();

    public RwtStandardForm(IClientEnvironment environment) {
        super(environment);
        commandBar = new CommandToolBar();
        add(content);
        content.add(commandBar);
    }

    @Override
    public void open(Model model_) {
        super.open(model_);
        if (model_.getView()==null){//Form was closed in beforeOpenView handler
            return;
        }        
        commandBar.setModel(model_);
        {//TabSet opening
            final RadFormDef def = getFormModel().getFormDef();
            List<Id> idsWithLevelOne = new ArrayList<>();

            for (RadEditorPageDef page : def.getEditorPages().getTopLevelPages()) {
                idsWithLevelOne.add(page.getId());
            }

            List<EditorPageModelItem> modelItems = new ArrayList<>();
            for (Id id : idsWithLevelOne) {
                EditorPageModelItem modelItem = getFormModel().getEditorPage(id);
                modelItems.add(modelItem);
            }
            tabSet = new TabSet((WpsEnvironment) model_.getEnvironment(), this, modelItems, model_.getDefinition().getId());
            tabSet.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
            content.add(tabSet);
            tabSet.bind();
        }        
        setupButtons();
        show();
        fireOpened();        
    }

    public void setCommandBarHidden(final boolean hidden) {
        commandBar.setPersistentHidden(hidden);
    }

    @Override
    public boolean setFocusedProperty(final Id propertyId) {
        Property property;
        try {
            property = getFormModel().getProperty(propertyId);
        } catch (RuntimeException ex) {
            final String message = getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot set focus to editor of property #%s: %s\n%s");
            final String reason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex);
            final String stack = ClientException.exceptionStackToString(ex);
            getEnvironment().getTracer().debug(String.format(message, String.valueOf(propertyId), reason, stack));
            return false;
        }
        if (property != null && property.getOwner().getView() == this) {
            return tabSet.setFocus(property);
        }
        return false;
    }

    @Override
    public void finishEdit() {
        if (tabSet!=null){
            tabSet.finishEdit();
        }
        super.finishEdit();
    }
}
