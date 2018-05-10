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

import com.trolltech.qt.gui.QCloseEvent;
import java.util.Collection;

import com.trolltech.qt.gui.QWidget;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EWidgetMarker;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ModelWithPages;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.AbstractViewController;
import org.radixware.kernel.common.client.views.IEditorPageView;
import org.radixware.kernel.common.client.views.IEmbeddedView;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;
import org.radixware.kernel.common.client.widgets.IModifableComponent;
import org.radixware.kernel.common.client.widgets.IModificationListener;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.QWidgetProxy;

public abstract class EditorPageView extends ExplorerWidget implements IExplorerView, IModificationListener, IEditorPageView {
    
    private final static class EditorPageViewController extends AbstractViewController{
        
        private EditorPageView pageView;
        
        public EditorPageViewController(final IClientEnvironment environment, final EditorPageView pageView){
            super(environment,pageView);
            this.pageView = pageView;
        }
        
        @Override
        protected List<IEmbeddedView> findChildrenViews() {
            return WidgetUtils.findExplorerViews(pageView);
        }
        
        public void close(){
            closeEmbeddedViews();
        }
    }

    private ModelWithPages model;
    private final Id pageId;
    private final List<IModifableComponent> children = new LinkedList<>();
    private ViewRestrictions restrictions;
    protected IExplorerView parentView;
    final public Signal1<QWidget> opened = new Signal1<>();
    final public Signal0 closed = new Signal0();
    private boolean wasClosed;

    protected EditorPageView(IClientEnvironment environment, final IExplorerView parentView, final RadEditorPageDef page) {
        super(environment);
        this.parentView = parentView;
        this.pageId = page.getId();
        setAutoFillBackground(true);
        restrictions = new ViewRestrictions(this);
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
        if (parentView instanceof Editor) {
            ((Editor) parentView).modifiedStateChanged.connect(this, "refreshEditedState(Boolean)");
        }
    }

    @Override
    public ModelWithPages getModel() {
        return model;
    }

    @Override
    public boolean close(final boolean forced) {
        closed.emit();
        close();
        return true;
    }

    @Override
    protected void closeEvent(final QCloseEvent event) {
        wasClosed = true;
        if (parentView instanceof Editor) {
            ((Editor) parentView).modifiedStateChanged.disconnect(this);
        }
        closeEmbeddedViews();
        parentView = null;
        restrictions = null;
        children.clear();
        super.closeEvent(event);
    }
    
    protected void closeEmbeddedViews(){
        //to protect from mistakes in application code
        new EditorPageViewController(getEnvironment(), this).close();
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
    public QWidget asQWidget() {
        return this;
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

    @Override
    public void visitChildren(Visitor visitor, boolean recursively) {
        //do nothing
    }    
    
    @Override
    public boolean canSafelyClose(final CleanModelController cleanController) {
        return true;
    }        

    @SuppressWarnings("unused")
    private void refreshEditedState(final Boolean wasEdited) {
        if (!wasEdited) {
            getPage().setEdited(someChildComponentWasModified());
        }
    }

    @Override
    public IView findParentView() {
        return QWidgetProxy.findParentView(this);
    }        

    @Override
    public IWidget getParentWindow() {
        return QWidgetProxy.getParentWindow(this);
    }

    @Override
    public ViewRestrictions getRestrictions() {
        return restrictions;
    }        

    @Override
    public boolean hasUI() {
        return !wasClosed && asQWidget() != null && asQWidget().nativePointer() != null;
    }

    @Override
    public boolean isDisabled() {
        return !asQWidget().isEnabled();
    }
    
    public final EWidgetMarker getWidgetMarker(){
        return EWidgetMarker.EDITOR_PAGE;
    }
}
