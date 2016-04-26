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

import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.views.IEditorPageView;
import org.radixware.kernel.common.client.views.IEditorPageWidget;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.rwt.UIObject;



public class EditorPage extends UIObject implements IEditorPageWidget {

    private EditorPageModelItem page;
    private IEditorPageView pageView;

    public EditorPage(EditorPageModelItem item) {
        super(new Div());
        this.page = item;
    }

    public EditorPageModelItem getEditorPageModelItem() {
        return page;
    }

    public Id getEditorPageId() {
        return page.getId();
    }

    @Override
    public void refresh(ModelItem changedItem) {
        if (pageView != null) {
            ((UIObject) pageView).setVisible(page.isVisible());
        }
    }

    @Override
    public boolean setFocus(Property property) {
        if (pageView instanceof StandardEditorPage) {
            StandardEditorPage w = (StandardEditorPage) pageView;
            return w.setFocusedProperty(property.getId());
        }
        return false;
    }
    private UIObject inner = null;

    private void set(UIObject o) {
        if (inner != null) {
            remove(inner);
        }
        html.add(o.getHtml());
        o.setParent(this);
        o.setLeft(0);
        o.setTop(0);
        o.setVCoverage(100);
        o.setHCoverage(100);
        inner = o;
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject obj = super.findObjectByHtmlId(id);
        if (obj != null) {
            return obj;
        }
        if (inner != null) {
            return inner.findObjectByHtmlId(id);
        } else {
            return null;
        }
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        if (inner != null) {
            inner.visit(visitor);
        }
    }

    private void remove(UIObject o) {
        html.remove(o.getHtml());
        o.setParent(null);
        if (o == inner) {
            inner = null;
        }
    }

    @Override
    public void bind() {
        if (page == null) {
            throw new IllegalStateException("Editor page was not defined");
        }
        UIObject w = null;
        try {
            if (errorView != null) {
                errorView.setVisible(false);
                remove(errorView);
                errorView = null;
            }            
            pageView = page.getView();
            if (pageView==null){
                pageView = page.createView();
            }
            w = (UIObject) pageView;
            w.setParent(this);
            pageView.open(page.getOwner());
            set(w);
            page.getOwner().afterOpenEditorPageView(page.getId());
        } catch (Exception ex) {
            if (pageView != null) {
                if (w != null) {
                    w.setVisible(false);
                }
                pageView.close(true);
                if (w != null) {
                    remove(w);
                }
                pageView = null;
            }
            final String message = page.getEnvironment().getMessageProvider().translate("ExplorerError", "Can't open editor page '%s'");
            getErrorView().setError(String.format(message, page.getTitle()), ex);
            getErrorView().setVisible(true);
            page.getEnvironment().getTracer().put(ex);
            return;
        }
        page.subscribe(this);
        refresh(page);
        //setFocusProxy(w);
    }

    public void reread() {
        if (pageView != null) {
            try {
                pageView.reread();
            } catch (ServiceClientException e) {
                pageView.getModel().showException(e);
            }
        }
    }

    public void finishEdit() {
        if (pageView != null) {
            pageView.finishEdit();
        }
    }

    public void close() {
        if (pageView != null) {
            pageView.close(true);
            remove((UIObject) pageView);
        }
    }
    private ErrorView errorView;

    private ErrorView getErrorView() {
        if (errorView == null) {
            errorView = new ErrorView();
            errorView.setVisible(false);
            errorView.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
            this.set(errorView);
        }
        return errorView;
    }
}
