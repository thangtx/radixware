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

package org.radixware.wps.views.editor;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.errors.CantOpenEditorError;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.UIObject;


public class StandardEditor extends Editor {

    private TabSet tabs;

    public StandardEditor(WpsEnvironment env) {
        super(env);
    }

    @Override
    public void open(Model model) {
        super.open(model);
        RadEditorPresentationDef def = getEntityModel().getEditorPresentationDef();
        List<Id> idsWithLevelOne = new ArrayList<Id>();

        for (RadEditorPageDef page : def.getEditorPages().getTopLevelPages()) {
            idsWithLevelOne.add(page.getId());
        }

        List<EditorPageModelItem> modelItems = new ArrayList<EditorPageModelItem>();
        for (Id id : idsWithLevelOne) {
            EditorPageModelItem modelItem = getEntityModel().getEditorPage(id);
            if (modelItem != null) {
                modelItems.add(modelItem);
            }
        }
        tabs = new TabSet((WpsEnvironment) model.getEnvironment(), this, modelItems, model.getDefinition().getId());
        tabs.setObjectName("tabSet");
        //tabSet.setParent(content);
        add(tabs);
        tabs.setTop(getMainComponentTop());
        tabs.setLeft(0);
        tabs.setVCoverage(100);
        tabs.getAnchors().setBottom(new Anchors.Anchor(1, -1));
        try {
            tabs.bind();
        } catch (java.lang.RuntimeException ex) {
            throw new CantOpenEditorError(getEntityModel(), ex);
        }
        //content.setFocusProxy(tabSet);
        notifyOpened();
    }

    @Override
    public boolean close(boolean forced) {
        if (super.close(forced)) {
            if (tabs != null) {
                tabs.close();
                tabs = null;
            }
            return true;
        } else {
            return false;
        }
    }

    private void notifyOpened() {
    }

    @Override
    public boolean setFocusedProperty(final Id propertyId) {
        Property property = null;
        try {
            property = getEntityModel().getProperty(propertyId);
        } catch (RuntimeException ex) {
            final String message = getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot set focus to editor of property #%s: %s\n%s");
            final String reason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex);
            final String stack = ClientException.exceptionStackToString(ex);
            getEnvironment().getTracer().debug(String.format(message, String.valueOf(propertyId), reason, stack));
            return false;
        }
        if (property != null && property.getOwner().getView() == this && tabs != null) {
            return tabs.setFocus(property);
        }
        return false;
    }

    @Override
    public void finishEdit() {
        if (tabs != null) {
            tabs.finishEdit();
        }
        super.finishEdit();
    }

    @Override
    public void reread() throws ServiceClientException {
        super.reread();
        if (tabs != null) {
            tabs.reread();
        }
    }

    @Override
    UIObject getMainComponent() {
        return tabs;
    }       
}
