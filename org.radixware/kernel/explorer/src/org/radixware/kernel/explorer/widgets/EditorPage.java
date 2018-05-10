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

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.views.IEditorPageView;
import org.radixware.kernel.common.client.views.IEditorPageWidget;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.views.ErrorView;
import org.radixware.kernel.explorer.views.StandardEditorPage;

public class EditorPage extends ExplorerWidget implements IExplorerModelWidget, IEditorPageWidget {

    private EditorPageModelItem page;
    private EditorPage parentEditorPage;
    private IEditorPageView pageView;
    private final QVBoxLayout layout = new QVBoxLayout(this);
    private ErrorView errWidget;

    public EditorPage(EditorPageModelItem modelItem) {
        super(modelItem.getEnvironment());
        page = modelItem;
        setupUi();
    }

    public EditorPage(IClientEnvironment environment) {
        super(environment);
        page = null;
        setupUi();
    }

    private void setupUi() {
        layout.setSpacing(0);
        layout.setMargin(0);
    }

    private ErrorView getErrorView() {
        if (errWidget == null) {
            errWidget = new ErrorView(getEnvironment(), this);
            layout.addWidget(errWidget);
        }
        return errWidget;
    }

    @Override
    public void bind() {
        if (page == null) {
            throw new IllegalStateException("Editor page was not defined");
        }
        QWidget w = null;
        final String modelTitle = page.getTitle();
        final long time = System.currentTimeMillis();
        {
            final String message = 
                getEnvironment().getMessageProvider().translate("TraceMessage", "Start opening editor page \'%1$s\'");
            getEnvironment().getTracer().debug(String.format(message, modelTitle));
        }
        try{
            try {
                if (errWidget != null) {
                    errWidget.hide();
                }            
                pageView = page.getView();
                if (pageView==null){
                    pageView = page.createView();
                }
                w = (QWidget) pageView;
                w.setParent(this);
                layout.addWidget(w);
                pageView.open(page.getOwner());
                page.getOwner().afterOpenEditorPageView(page.getId());
            } catch (Exception ex) {
                getEnvironment().getTracer().put(ex);
                if (pageView != null) {
                    if (w != null) {
                        w.hide();
                    }
                    pageView.close(true);
                    if (w != null) {
                        w.disposeLater();
                    }
                    pageView = null;
                }            
                final String message = getEnvironment().getMessageProvider().translate("ExplorerError", "Can't open editor page '%s'");            
                getErrorView().setError(String.format(message, page.getTitle()), ex);
                getErrorView().show();            
                return;
            }
            page.subscribe(this);
            refresh(page);
            setFocusProxy(w);
        }finally{
            final long elapsedTime = System.currentTimeMillis()-time;        
            final String message = 
                getEnvironment().getMessageProvider().translate("TraceMessage", "Opening editor page \'%1$s\' finished. Elapsed time: %2$s ms");
            getEnvironment().getTracer().debug(String.format(message, modelTitle, elapsedTime));            
        }
    }

    public void setEditorPage(final EditorPageModelItem modelItem) {
        if (page != null) {
            page.unsubscribe(this);
        }
        if (pageView != null) {
            if (!pageView.close(false)) {
                return;
            }
            layout.removeWidget((QWidget) pageView);
        }
        page = modelItem;
    }
    
    public Id getPageId(){
        return page==null ? null : page.getId();
    }

    @Override
    public void refresh(ModelItem changedItem) {
        if (pageView != null) {
            ((QWidget) pageView).setVisible(page.isVisible());
        }
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }

    @Override
    public QSize minimumSizeHint() {
        return pageView == null ? super.minimumSizeHint() : ((QWidget) pageView).minimumSizeHint();
    }

    @Override
    public QSize sizeHint() {
        return pageView == null ? super.sizeHint() : ((QWidget) pageView).sizeHint();
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        if (page != null) {
            page.unsubscribe(this);
        }
        if (pageView != null) {
            pageView.close(true);
            ((QWidget) pageView).disposeLater();
            pageView = null;
        }
        super.closeEvent(event);
    }

    public EditorPage getParentEditorPage() {
        return parentEditorPage;
    }

    public void setParentEditorPage(EditorPage parentEditorPage) {
        this.parentEditorPage = parentEditorPage;
    }

    public EditorPageModelItem getEditorPageModelItem() {
        return page;
    }

    @Override
    public boolean setFocus(Property property) {
        if (pageView instanceof StandardEditorPage) {
            final IModelWidget w = ((StandardEditorPage) pageView).getWidget();            
            return w==null ? false : w.setFocus(property);
        }
        return false;
    }

    public void finishEdit() {
        if (pageView != null) {            
            pageView.finishEdit();
        }
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
    
    public IEditorPageView getView(){
        return pageView;
    }
}
