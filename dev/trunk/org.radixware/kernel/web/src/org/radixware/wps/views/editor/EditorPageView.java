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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ModelWithPages;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.IEditorPageView;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IModifableComponent;
import org.radixware.kernel.common.client.widgets.IModificationListener;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.views.ViewSupport;


public abstract class EditorPageView extends Container implements IModificationListener, IEditorPageView {

    private ViewSupport<EditorPageView> viewSupport = new ViewSupport<>(this);
    private ModelWithPages model;
    private final Id pageId;
    private IClientEnvironment env;
    private final List<IModifableComponent> children = new LinkedList<>();
    private final ViewRestrictions restrictions = new ViewRestrictions(this);
    protected IView parentView;

    public static abstract class EditorPageOpenHandler implements IEditorPageListener {

        @Override
        public void closed(IEditorPageView page) {
            //do nothing on close
        }
    }

    public static abstract class EditorPageCloseHandler implements IEditorPageListener {

        @Override
        public void opened(IEditorPageView page) {
            //do nothing on open
        }
    }

    protected EditorPageView(IClientEnvironment environment, final IView parentView, final RadEditorPageDef page) {
        this.env = environment;
        this.parentView = parentView;
        this.pageId = page.getId();

    }

    public EditorPageModelItem getPage() {
        if (model == null) {
            throw new IllegalStateException("editor page view was not opened");
        }
        return model.getEditorPage(pageId);
    }

    @Override
    public void open(final Model model_) {
        if (model_ instanceof ModelWithPages==false){
            throw new IllegalUsageError("not supported model " + model.getClass().getSimpleName());
        }
        model = (ModelWithPages)model_;
    }
    private final List<IEditorPageListener> listeners = new LinkedList<>();

    protected void fireOpened() {
        final List<IEditorPageListener> list;
        synchronized (listeners) {
            list = new ArrayList<>(listeners);
        }
        for (IEditorPageListener l : list) {
            l.opened(this);
        }
    }

    protected void fireClosed() {
        final List<IEditorPageListener> list;
        synchronized (listeners) {
            list = new ArrayList<>(listeners);
        }
        for (IEditorPageListener l : list) {
            l.closed(this);
        }
    }

    @Override
    public ModelWithPages getModel() {
        return model;
    }

    @Override
    public boolean close(final boolean forced) {
        if (parentView instanceof Editor) {
            //   ((IModificationListener) parentView).modifiedStateChanged.disconnect(this);
        }
        parentView = null;
        children.clear();
        fireClosed();
        return true;
    }

    public void addEditorPageListener(IEditorPageListener l) {
        synchronized (listeners) {
            if (!listeners.contains(l)) {
                listeners.add(l);
            }
        }
    }

    public void removeEditorPageListener(IEditorPageListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    @Override
    public void finishEdit() {
        final Collection<Property> properties = model.getActiveProperties();
        for (Property property : properties) {
            property.finishEdit();
        }
    }

    @Override
    public boolean setFocusedProperty(final Id propertyId) {
        return false;
    }

    @Override
    public void reread() {
    }

    @Override
    public void childComponentWasClosed(IModifableComponent childComponent) {
        children.remove(childComponent);
        if (parentView instanceof IModificationListener) {
            ((IModificationListener) parentView).childComponentWasClosed(childComponent);
        }
    }

    @Override
    public void notifyComponentModificationStateChanged(IModifableComponent childComponent, boolean modified) {
        if (modified) {
            getPage().setEdited(true);
            if (!children.contains(childComponent)) {
                children.add(childComponent);
            }
        } else if (!someChildComponentWasModified()) {
            getPage().setEdited(false);
        }
        if (parentView instanceof IModificationListener) {
            ((IModificationListener) parentView).notifyComponentModificationStateChanged(childComponent, modified);
        }
    }

    @Override
    public void notifyPropertyModificationStateChanged(Property property, boolean modified) {
        if (modified) {
            getPage().setEdited(true);
        } else {
            if (!someChildComponentWasModified()) {
                boolean hasModifiedProperty = false;
                final Collection<Property> properties = getPage().getProperties();
                for (Property pageProperty : properties) {
                    if (pageProperty.isValEdited() && !pageProperty.getId().equals(property.getId())) {
                        hasModifiedProperty = true;
                        break;
                    }
                }
                getPage().setEdited(hasModifiedProperty);
            }
        }
        if (parentView instanceof IModificationListener) {
            ((IModificationListener) parentView).notifyPropertyModificationStateChanged(property, modified);
        }
    }

    private boolean someChildComponentWasModified() {
        for (IModifableComponent component : children) {
            if (component.inModifiedStateNow()) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unused")
    private void refreshEditedState(final Boolean wasEdited) {
        if (!wasEdited) {
            getPage().setEdited(someChildComponentWasModified());
        }
    }

    @Override
    public IView findParentView() {
        return viewSupport.findParentView();
    }

    @Override
    public void visitChildren(IView.Visitor visitor, boolean recursively) {
        //do nothing;
    }        

    @Override
    public ViewRestrictions getRestrictions() {
        return restrictions;
    }
    
    @Override
    public boolean canSafelyClose(final CleanModelController cleanController) {
        return true;
    }        

    @Override
    public IWidget getParentWindow() {
        return viewSupport.getParentWindow();
    }

    @Override
    public boolean hasUI() {
        return true;
    }

    @Override
    public boolean isDisabled() {
        return !isEnabled();
    }

    @Override
    public IClientEnvironment getEnvironment() {
        return env;
    }

    @Override
    public void setFocus() {
    }
}
