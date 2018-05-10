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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPen;
import com.trolltech.qt.gui.QResizeEvent;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
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
import org.radixware.kernel.explorer.widgets.ExplorerWidget;
import org.radixware.kernel.explorer.widgets.PropertiesGrid;
import org.radixware.kernel.explorer.widgets.QWidgetProxy;


public class FilterParameters extends ExplorerWidget implements IExplorerView, IFilterParametersView {        
    
    private static final class CollapseButton extends QToolButton{
        
        public static enum State{COLLAPSED, EXPANDED};
        
        private final WidgetUtils.UpArrow upArrow;
        private final WidgetUtils.DownArrow downArrow;
        private final String collapseHint;
        private final String expandHint;
        private final QPen pen = new QPen();
        private State state;
        
        public CollapseButton(final IClientEnvironment environment, final QWidget parent){
            super(parent);
            setFixedSize(200, 14);
            upArrow = new WidgetUtils.UpArrow(100, 4, 6);
            downArrow = new WidgetUtils.DownArrow(100, 4, 6);
            pen.setWidth(0);
            setObjectName("rx_collapse_filter_parameters_content_btn");
            collapseHint = environment.getMessageProvider().translate("Selector", "Hide Filter Parameters");
            expandHint = environment.getMessageProvider().translate("Selector", "Show Filter Parameters");
            setState(State.EXPANDED);
        }

        @Override
        protected void paintEvent(final QPaintEvent paintEvent) {
            super.paintEvent(paintEvent);            
            final QPainter painter = new QPainter(this);
            final QBrush color = palette().buttonText();
            pen.setBrush(color);         
            painter.setPen(pen);
            try{
                if (state==State.COLLAPSED){
                    downArrow.draw(painter);
                }else{
                    upArrow.draw(painter);
                }
            }finally{
                painter.end();
            }
        }
        
        public void setState(final State newState){
            if (newState!=null && state!=newState){
                state = newState;
                setToolTip(state==State.COLLAPSED ? expandHint : collapseHint);
                repaint();
            }
        }
        
        public State getState(){
            return state;
        }
    }

    protected FilterModel filter;
    protected final QFrame content = new QFrame(this);
    private final AbstractViewController controller;
    private final CollapseButton collapseButton;
    public final  Signal1<QWidget> opened = new Signal1<>();    
    public final Signal0 closed = new Signal0();
    public final  Signal0 collapsed = new Signal0();
    public final  Signal0 expanded = new Signal0();
    private PropertiesGrid customPropertiesGrid;
    private boolean isCollapsable;

    protected FilterParameters(final IClientEnvironment environment) {
        super(environment);
        collapseButton = new CollapseButton(environment, this);
        controller = new AbstractViewController(environment,this){
            @Override
            protected List<IEmbeddedView> findChildrenViews() {
                return WidgetUtils.findExplorerViews(content);
            }
        };
        content.setFrameShape(QFrame.Shape.StyledPanel);
        content.setObjectName("rx_filter_parameters_content");
        content.setSizePolicy(QSizePolicy.Policy.Minimum, QSizePolicy.Policy.Minimum);
        final QVBoxLayout layout = WidgetUtils.createVBoxLayout(this);
        setLayout(layout);
        layout.addWidget(content);
        layout.addWidget(collapseButton,0,Qt.AlignmentFlag.AlignHCenter);
        collapseButton.clicked.connect(this,"onCollapseButtonClick()");        
        collapseButton.setVisible(false);
    }

    @Override
    public void open(final Model model_) {
        filter = (FilterModel) model_;
        filter.setView(this);
        final RadFilterParameters parameters = filter.getFilterDef().getParameters();        
        if (parameters.customParametersCount()>0 && parameters.customParametersCount()!=parameters.size()){
            final List<Property> customProperties = new LinkedList<>();
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
        setObjectName("rx_filter_params_view_#"+filter.getId());
    }    

    @Override
    public boolean close(final boolean forced) {
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
    protected void resizeEvent(final QResizeEvent event) {
        super.resizeEvent(event);
        updateButtonState();
    }        
    
    @Override
    protected void closeEvent(final QCloseEvent event) {
        WidgetUtils.closeChildrenWidgets(this);
        collapsed.disconnect();
        expanded.disconnect();
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
    public boolean setFocusedProperty(final Id propertyId) {
        return false;
    }

    @Override
    public void reread() throws ServiceClientException {
    }

    @Override
    public void visitChildren(final Visitor visitor, final boolean recursively) {
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
    
    @SuppressWarnings("unused")
    private void onCollapseButtonClick(){
        if (collapseButton.getState()==CollapseButton.State.COLLAPSED){
            expand();
        }else{
            collapse();
        }
    }

    @Override
    public void collapse() {
        if (isCollapsable()){
            content.setVisible(false);
            collapseButton.setState(CollapseButton.State.COLLAPSED);
            collapsed.emit();
        }
    }

    @Override
    public void expand() {
        content.setVisible(true);
        updateButtonState();
        expanded.emit();
    }
    
    private void updateButtonState(){
        if (isCollapsable() && !isCollapsed()){
            if (content.height()<content.sizeHint().height()){
                collapseButton.setState(CollapseButton.State.COLLAPSED);
            }else{
                collapseButton.setState(CollapseButton.State.EXPANDED);
            }
        }        
    }

    @Override
    public boolean isCollapsed() {
        return content.isHidden();
    }

    @Override
    public boolean isCollapsable() {
        return isCollapsable;
    }

    @Override
    public void setCollapsable(final boolean collapsable) {
        if (isCollapsable!=collapsable){
            isCollapsable = collapsable;
            collapseButton.setVisible(collapsable);
            if (collapseButton.getState()==CollapseButton.State.COLLAPSED){
                expand();
            }
            content.setSizePolicy(QSizePolicy.Policy.Minimum, collapsable ? QSizePolicy.Policy.Maximum : QSizePolicy.Policy.Minimum);
        }
    }    
}
