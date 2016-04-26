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
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ParagraphModel;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.AbstractViewController;
import org.radixware.kernel.common.client.views.IEmbeddedView;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;
import org.radixware.kernel.explorer.widgets.QWidgetProxy;

public abstract class ParagraphEditor extends ExplorerWidget implements IExplorerView {
    
    private final class ParagraphViewController extends AbstractViewController{
        
        public ParagraphViewController(final IClientEnvironment environment, final IExplorerView view){
            super(environment,view);
        }
        
        @Override
        protected List<IEmbeddedView> findChildrenViews() {
            return WidgetUtils.findExplorerViews(ParagraphEditor.this);
        }
        
        public void close(){
            closeEmbeddedViews();
        }
    }

    private ParagraphModel paragraph;
    private final ParagraphViewController  controller;

    public ParagraphEditor(IClientEnvironment environment) {
        super(environment);
        controller = new ParagraphViewController(environment,this);
    }

    @Override
    public /*final*/ Model getModel() {
        return paragraph;
    }

    public final ParagraphModel getParagraphModel() {
        return paragraph;
    }
    final public Signal1<QWidget> opened = new Signal1<>();
    final public Signal0 closed = new Signal0();
    protected final QWidget content = this;//Для совместимости с Selector и Editor

    @Override
    public void open(Model model_) {
        paragraph = (ParagraphModel) model_;
        model_.setView(this);
    }

    private boolean wasClosed;

    @Override
    public boolean close(boolean forced) {
        if (wasClosed){
            return true;
        }
        if (!forced && !paragraph.canSafelyClean(CleanModelController.DEFAULT_INSTANCE)) {
            return false;
        }
        controller.close();
        closed.emit();
        paragraph.setView(null);
        controller.afterCloseView();
        WidgetUtils.closeChildrenEmbeddedViews(paragraph, content);
        close();
        return true;
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        wasClosed = true;
        super.closeEvent(event);
    }


    @Override
    public void finishEdit() {
        Collection<Property> properties = paragraph.getActiveProperties();
        for (Property property : properties) {
            property.finishEdit();
        }
    }

    @Override
    public boolean setFocusedProperty(Id propertyId) {
        return false;
    }

    @Override
    public void reread() throws ServiceClientException {
    }
    
    @Override
    public void visitChildren(Visitor visitor, boolean recursively) {
        controller.visitChildren(visitor, recursively);
    }

    @Override
    public ViewRestrictions getRestrictions() {
        return controller.getViewRestrictions();
    }     

    @Override
    public boolean canSafelyClose(final CleanModelController cleanController) {
        return controller.canSafelyClose(this, cleanController);
    }    

    @Override
    public QWidget asQWidget() {
        return this;
    }

    @Override
    public boolean isDisabled() {
        return !asQWidget().isEnabled();
    }

    @Override
    public IView findParentView() {
        return QWidgetProxy.findParentView(asQWidget());
    }

    @Override
    public IWidget getParentWindow() {
        return QWidgetProxy.getParentWindow(asQWidget());
    }

    @Override
    public boolean hasUI() {
        return asQWidget() != null && asQWidget().nativePointer() != null;
    }
}
