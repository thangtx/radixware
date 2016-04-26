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
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QWidget;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.filters.RadFilterParameters;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.AbstractViewController;
import org.radixware.kernel.common.client.views.IEmbeddedView;
import org.radixware.kernel.common.client.views.IFilterParametersView;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.ExplorerFrame;
import org.radixware.kernel.explorer.widgets.PropertiesGrid;
import org.radixware.kernel.explorer.widgets.QWidgetProxy;


public class FilterParameters extends ExplorerFrame implements IExplorerView, IFilterParametersView {

    protected FilterModel filter;
    protected final QWidget content = new QWidget(this);
    private PropertiesGrid customPropertiesGrid;
    final public Signal1<QWidget> opened = new Signal1<QWidget>();
    final public Signal0 closed = new Signal0();
    final private AbstractViewController controller;

    protected FilterParameters(final IClientEnvironment environment) {
        super(environment);
        controller = new AbstractViewController(environment,this){
            @Override
            protected List<IEmbeddedView> findChildrenViews() {
                return WidgetUtils.findExplorerViews(content);
            }            
        };
        setFrameShape(QFrame.Shape.StyledPanel);
        setLayout(WidgetUtils.createVBoxLayout(this));
        layout().addWidget(content);        
    }

    @Override
    public void open(Model model_) {
        filter = (FilterModel) model_;
        filter.setView(this);
        final RadFilterParameters parameters = filter.getFilterDef().getParameters();        
        if (parameters.customParametersCount()>0 && parameters.customParametersCount()!=parameters.size()){
            final List<Property> customProperties = new LinkedList<Property>();
            for (ISqmlParameter parameter: parameters.getAll()){
                if (parameter instanceof ISqmlModifiableParameter){
                    customProperties.add(filter.getProperty(parameter.getId()));
                }
            }
            customPropertiesGrid = new PropertiesGrid(this,getEnvironment());
            layout().addWidget(customPropertiesGrid);
            customPropertiesGrid.bind();            
            int column = 0, row = 0;
            for (Property property : customProperties) {
                customPropertiesGrid.addProperty(property, column, row);
                if (column == 2) {
                    column = 0;
                    row++;
                } else {
                    column++;
                }
            }
        }
    }

    @Override
    public boolean close(boolean forced) {
        filter.finishEdit();
        if (!forced && !filter.canSafelyClean(CleanModelController.DEFAULT_INSTANCE)) {
            return false;
        }
        if (customPropertiesGrid!=null){
            customPropertiesGrid.close();            
        }
        closed.emit();
        filter.setView(null);
        controller.afterCloseView();
        close();
        WidgetUtils.closeChildrenEmbeddedViews(filter, this);
        return true;
    }

    @Override
    public boolean canSafelyClose(final CleanModelController cleanController) {
        return controller.canSafelyClose(this, cleanController);
    }        

    @Override
    protected void closeEvent(QCloseEvent event) {
        WidgetUtils.closeChildrenWidgets(this);
        super.closeEvent(event);
    }
    
    @Override
    public Model getModel() {
        return filter;
    }

    @Override
    public ViewRestrictions getRestrictions() {
        return controller.getViewRestrictions();
    }        

    @Override
    public void finishEdit() {
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
    public QWidget asQWidget() {
        return this;
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
    public boolean hasUI() {
        return this.nativePointer() != null;
    }

    @Override
    public boolean isDisabled() {
        return !super.isEnabled();
    }
}
