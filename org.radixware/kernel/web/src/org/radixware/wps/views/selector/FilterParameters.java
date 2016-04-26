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

package org.radixware.wps.views.selector;

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
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.AbstractContainer;
import org.radixware.wps.rwt.GridLayout;
import org.radixware.wps.rwt.UIObject;

import org.radixware.wps.views.ViewSupport;
import org.radixware.wps.views.editor.PropertiesGrid;


public class FilterParameters extends AbstractContainer implements IView, IFilterParametersView {

    protected FilterModel filter;
    private PropertiesGrid customPropertiesGrid;
    private WpsEnvironment environment;
    private ViewSupport<FilterParameters> viewSupport = new ViewSupport<FilterParameters>(this);
    private final AbstractViewController controller;
    GridLayout layout = new GridLayout();

    protected FilterParameters(final IClientEnvironment environment) {
        super(new Div());
        add(layout);
        this.environment = (WpsEnvironment) environment;
        controller = new AbstractViewController(environment,this) {

            @Override
            protected List<IEmbeddedView> findChildrenViews() {
                return viewSupport.findEmbeddedViews();
            }
        };
        html.setCss("overflow-y", "auto");
        html.setCss("overflow-x", "hidden");
    }

    UIObject getCustomGrid() {
        return customPropertiesGrid;
    }

    @Override
    public void open(Model model_) {
        filter = (FilterModel) model_;
        filter.setView(this);
        final RadFilterParameters parameters = filter.getFilterDef().getParameters();
        if (parameters.customParametersCount() > 0 && parameters.customParametersCount() != parameters.size()) {
            final List<Property> customProperties = new LinkedList<Property>();
            for (ISqmlParameter parameter : parameters.getAll()) {
                if (parameter instanceof ISqmlModifiableParameter) {
                    customProperties.add(filter.getProperty(parameter.getId()));
                }
            }
            customPropertiesGrid = new PropertiesGrid();
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
            customPropertiesGrid.bind();
//            add(customPropertiesGrid);
//            customPropertiesGrid.setTop(0);
//            customPropertiesGrid.setLeft(0);
//            customPropertiesGrid.getAnchors().setRight(new Anchors.Anchor(1, -5));
//            customPropertiesGrid.adjustToContent(true);
            GridLayout.Row r = new GridLayout.Row();
            layout.add(r);
            GridLayout.Cell cell = new GridLayout.Cell();
            r.add(cell);
            cell.setVCoverage(100);
            cell.add(customPropertiesGrid);
        }
    }

    @Override
    public boolean close(boolean forced) {
        filter.finishEdit();
        if (!forced && !filter.canSafelyClean(CleanModelController.DEFAULT_INSTANCE)) {
            return false;
        }
        if (customPropertiesGrid != null) {
            customPropertiesGrid.close();
        }
        viewSupport.fireViewClosed();
        filter.setView(null);
        controller.afterCloseView();
        //    close();
        //  WidgetUtils.closeChildrenEmbeddedViews(filter, this);
//        WidgetUtils.disconnectSignalRecursively(Application.getInstance().actions.settingsChanged, this);
        return true;
    }

    @Override
    public boolean canSafelyClose(final CleanModelController cleanController) {
        return controller.canSafelyClose(this, cleanController);
    }

//    @Override
//    protected void closeEvent(QCloseEvent event) {
//        ..WidgetUtils.closeChildrenWidgets(this);
//        super.closeEvent(event);
//    }
    @Override
    public Model getModel() {
        return filter;
    }

    @Override
    public void finishEdit() {
    }

    @Override
    public boolean setFocusedProperty(Id propertyId) {
        return false;
    }

    @Override
    public void visitChildren(IView.Visitor visitor, boolean recursively) {
        controller.visitChildren(visitor, recursively);
    }

    @Override
    public ViewRestrictions getRestrictions() {
        return controller.getViewRestrictions();
    }        

    @Override
    public void reread() throws ServiceClientException {
    }

//    @Override
//    public QWidget asQWidget() {
//        return this;
//    }
    @Override
    public IView findParentView() {
        return viewSupport.findParentView();
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
        return !super.isEnabled();
    }

    @Override
    public void setFocus() {
        setFocused(true);
    }

    public void removeViewListener(IViewListener<FilterParameters> l) {
        viewSupport.removeViewListener(l);
    }

    public void addViewListener(IViewListener<FilterParameters> l) {
        viewSupport.addViewListener(l);
    }

    protected void fireViewOpened() {
        viewSupport.fireViewOpened();
    }
    //for automatically generated code
    protected void fireOpened() {
        fireViewOpened();
    }

    protected void fireViewClosed() {
        viewSupport.fireViewClosed();
    }
}
