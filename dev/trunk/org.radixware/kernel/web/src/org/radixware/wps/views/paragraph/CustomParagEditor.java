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

package org.radixware.wps.views.paragraph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ParagraphModel;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.AbstractViewController;
import org.radixware.kernel.common.client.views.IEmbeddedView;
import org.radixware.kernel.common.client.views.IParagraphEditor;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.views.ViewSupport;


public class CustomParagEditor extends Container implements IParagraphEditor {

    private ParagraphModel model;
    private final WpsEnvironment env;
    private final ViewSupport<CustomParagEditor> viewSupport;
    private final AbstractViewController controller;
    private final List<RwtParagraphEditorListener> listeners = new LinkedList<>();

    public static abstract class RwtParagraphEditorListener {
        public void opened(){};
        public void closed(){};
    }

    public CustomParagEditor(IClientEnvironment env) {
        this.env = (WpsEnvironment) env;
        this.viewSupport = new ViewSupport<>(this);
        controller = new AbstractViewController(env,this) {
            @Override
            protected List<IEmbeddedView> findChildrenViews() {
                return viewSupport.findEmbeddedViews();
            }
        };        
    }

    @Override
    public void open(Model model) {
        if (model instanceof ParagraphModel) {
            this.model = (ParagraphModel) model;
            model.setView(this);
        }
    }

    protected final void fireOpened() {
        final List<RwtParagraphEditorListener> lss = new ArrayList<>(listeners);
        for (RwtParagraphEditorListener l : lss) {
            l.opened();
        }
    }
    
    protected final void fireClosed(){
        final List<RwtParagraphEditorListener> lss = new ArrayList<>(listeners);
        for (RwtParagraphEditorListener l : lss) {
            l.closed();
        }        
    }

    public void addParagraphListener(final RwtParagraphEditorListener listener) {
        synchronized (listeners) {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
    }

    public void removeParagraphListener(final RwtParagraphEditorListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    @Override
    public void setFocus() {
    }

    @Override
    public boolean setFocusedProperty(final Id id) {
        return false;
    }

    @Override
    public boolean close(final boolean forced) {
        if (forced || model.canSafelyClean(CleanModelController.DEFAULT_INSTANCE)){
            fireClosed();
            return true;
        }
        return false;
    }

    @Override
    public boolean canSafelyClose(final CleanModelController cleanController) {
        return controller.canSafelyClose(this, cleanController);
    }    

    @Override
    public ViewRestrictions getRestrictions() {
        return controller.getViewRestrictions();
    }        

    @Override
    public void finishEdit() {
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public void reread() throws ServiceClientException {
    }

    @Override
    public boolean hasUI() {
        return true;
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public IClientEnvironment getEnvironment() {
        return env;
    }

    @Override
    public IWidget getParentWindow() {
        return viewSupport.getParentWindow();
    }

    @Override
    public void visitChildren(IView.Visitor visitor, boolean recursively) {
        controller.visitChildren(visitor, recursively);
    }        

    @Override
    public IView findParentView() {
        return viewSupport.findParentView();
    }
}
