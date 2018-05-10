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

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.IExplorerItemsHolder;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItems;
import org.radixware.kernel.common.client.meta.explorerItems.RadParentRefExplorerItemDef;
import org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ModelWithPages;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.views.IEmbeddedViewContext;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.selector.Selector;
import org.radixware.kernel.explorer.widgets.ChildPagesWidget;
import org.radixware.kernel.explorer.widgets.EmbeddedView;
import org.radixware.kernel.explorer.widgets.IExplorerModelWidget;
import org.radixware.kernel.explorer.widgets.PropertiesGrid;
import org.radixware.kernel.explorer.widgets.PropertiesGridDisassembler;
import org.radixware.kernel.explorer.widgets.TabSet;

public final class StandardEditorPage extends EditorPageView {

    private final IExplorerModelWidget generalPageWidget;
    private final ChildPagesWidget childPagesWidget;
    private final QSize size = new QSize();

    @SuppressWarnings("LeakingThisInConstructor")
    public StandardEditorPage(final IClientEnvironment environment, final IExplorerView parentView, final RadEditorPageDef page) {
        super(environment, parentView, page);

        if (parentView.getModel() instanceof ModelWithPages == false) {
            throw new IllegalArgumentError("parent view must be editor, form, report or filter");
        }
        final EditorPageModelItem editorPage =
                ((ModelWithPages) parentView.getModel()).getEditorPage(page.getId());

        if (page instanceof RadStandardEditorPageDef) {
            final RadStandardEditorPageDef pageDef = (RadStandardEditorPageDef)page;
            if (pageDef.hasProperties()){
                final PropertiesGrid propertiesGrid = new PropertiesGrid(this, environment);
                generalPageWidget = propertiesGrid;
                propertiesGrid.setObjectName("propertiesGrid");
                propertiesGrid.setAutoHide(true);
                if (pageDef.hasSubPages()){
                    childPagesWidget = 
                        new ChildPagesWidget(environment, this, parentView, editorPage.getChildPages(), page.getId().toString());
                }else{
                    childPagesWidget = null;
                }
            }else if (pageDef.hasSubPages()){
                final TabSet tabSet = 
                    new TabSet(environment, parentView, editorPage.getChildPages(), page.getId().toString());
                generalPageWidget = tabSet;
                tabSet.setParent(this);
                tabSet.setAutoHide(true);
                childPagesWidget = null;
            }else{
                generalPageWidget = null;
                childPagesWidget = null;
            }
        } else if (page instanceof RadContainerEditorPageDef) {
            final RadContainerEditorPageDef container = (RadContainerEditorPageDef) page;
            final Id explorerItemId = container.getExplorerItemId();
            final RadExplorerItems childItems;
            if (editorPage.getOwner().getDefinition() instanceof IExplorerItemsHolder) {
                childItems =
                        ((IExplorerItemsHolder) editorPage.getOwner().getDefinition()).getChildrenExplorerItems();
            } else {
                childItems = null;
            }
            final boolean isSynchronized =
                    childItems == null ? false : childItems.findExplorerItem(explorerItemId) instanceof RadParentRefExplorerItemDef;
            generalPageWidget =
                    new EmbeddedView(environment, parentView, explorerItemId, isSynchronized, new IEmbeddedViewContext.EditorPage(editorPage));
            ((QWidget) generalPageWidget).setParent(this);
            ((QWidget) generalPageWidget).setObjectName(editorPage.getTitle());
            if (container.hasSubPages()){
                childPagesWidget = 
                    new ChildPagesWidget(environment, this, parentView, editorPage.getChildPages(), page.getId().toString());                    
            }else{
                childPagesWidget = null;
            }
        } else {
            throw new IllegalArgumentError("Unknown page class " + page.getClass().getName());
        }
        if (generalPageWidget!=null || childPagesWidget!=null){
            final QVBoxLayout layout = WidgetUtils.createVBoxLayout(this);
            if (generalPageWidget != null) {
                layout.addWidget(generalPageWidget.asQWidget());
                setFocusProxy(generalPageWidget.asQWidget());
                if (childPagesWidget==null){
                    generalPageWidget.asQWidget().setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
                }else{
                    generalPageWidget.asQWidget().setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Fixed);
                }
            }
            if (childPagesWidget!=null){
                layout.addWidget(childPagesWidget);
                childPagesWidget.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
            }
            setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        }else{
            setVisible(false);
        }
    }

    @Override
    public void open(final Model model_) {
        super.open(model_);
        if (childPagesWidget!=null){
            childPagesWidget.bind();
        }
        if (generalPageWidget!=null){
            generalPageWidget.bind();
            if (generalPageWidget instanceof EmbeddedView) {
                final EmbeddedView embeddedView = (EmbeddedView) generalPageWidget;
                if (embeddedView.isOpened()) {
                    try {
                        final Model child = getModel().getChildModel(embeddedView.getExplorerItemId());
                        if (child.getView() instanceof Editor) {
                            //Прячем панель инструментов вложенного редактора
                            ((Editor) child.getView()).setToolBarHidden(true);
                        } else if (child.getView() instanceof Selector) {
                            //Уменьшаем размеры пиктограмм на панелях селектора
                            final Selector selector = ((Selector) child.getView());
                            final int buttonsSize = selector.getToolButtonsSize();
                            selector.setToolButtonsSize(buttonsSize - 2);
                        }
                    } catch (ServiceClientException ex) {
                        //К этому моменту child уже должен быть создан
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    } catch (InterruptedException ex) {
                        //К этому моменту child уже должен быть создан
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }
            }
            if (!generalPageWidget.asQWidget().isHidden()){
                generalPageWidget.asQWidget().setVisible(true);
                //generalPageWidget.asQWidget().setFocus(); it is not corrent when changing selector row
            }
        }        
        opened.emit(this);
    }

    @Override
    public QSize minimumSizeHint() {
        if (generalPageWidget==null && childPagesWidget==null){
            return super.minimumSizeHint();
        }else if (generalPageWidget==null){
            return childPagesWidget.minimumSizeHint();
        }else{
            final QSize sizeHint = generalPageWidget.asQWidget().minimumSizeHint();
            if (childPagesWidget==null || childPagesWidget.isHidden()){
                return sizeHint;
            }else{
                final QSize childPagesSizeHint = childPagesWidget.minimumSizeHint();
                size.setWidth(Math.max(sizeHint.width(), childPagesSizeHint.width()));
                size.setHeight(sizeHint.height()+ childPagesSizeHint.height());
                return size;
            }
        }    
    }

    @Override
    public QSize sizeHint() {
        if (generalPageWidget==null && childPagesWidget==null){
            return super.sizeHint();
        }else if (generalPageWidget==null){
            return childPagesWidget.sizeHint();
        }else{
            final QSize sizeHint = generalPageWidget.asQWidget().sizeHint();
            if (childPagesWidget==null || childPagesWidget.isHidden()){
                return sizeHint;
            }else{
                final QSize childPagesSizeHint = childPagesWidget.sizeHint();
                size.setWidth(Math.max(sizeHint.width(), childPagesSizeHint.width()));
                size.setHeight(sizeHint.height()+ childPagesSizeHint.height());
                return size;
            }
        }
    }

    public IModelWidget getWidget() {
        return generalPageWidget;
    }    

    @Override
    public void finishEdit() {
        if (childPagesWidget!=null){
            childPagesWidget.finishEdit();
        }
        if (generalPageWidget instanceof TabSet) {
            ((TabSet) generalPageWidget).finishEdit();
        } else if (generalPageWidget instanceof EmbeddedView) {
            ((EmbeddedView) generalPageWidget).finishEdit();
        } else {
            super.finishEdit();
        }
    }

    @Override
    public boolean setFocusedProperty(final Id propertyId) {
        if (getWidget() != null || childPagesWidget!=null) {
            final Property property;
            try {
                property = getModel().getProperty(propertyId);
            } catch (RuntimeException ex) {
                final String message =
                        getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot set focus to editor of property #%s: %s\n%s");
                final String reason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex);
                final String stack = ClientException.exceptionStackToString(ex);
                getEnvironment().getTracer().debug(String.format(message, String.valueOf(propertyId), reason, stack));
                return false;
            }
            if (getWidget()!=null && getWidget().setFocus(property)){
                return true;
            }
            if (childPagesWidget!=null && childPagesWidget.setFocus(property)){
                return true;
            }
        }
        return super.setFocusedProperty(propertyId);
    }

    @Override
    public void reread() {
        if (generalPageWidget instanceof EmbeddedView) {
            final EmbeddedView view = (EmbeddedView) generalPageWidget;
            if (view.isOpened()) {
                view.reread();
            } else if (getModel() != null) {
                open(getModel());
            }
        } else if (generalPageWidget instanceof TabSet) {
            ((TabSet) generalPageWidget).reread();
        }
        if (childPagesWidget!=null){
            childPagesWidget.reread();
        }
    }

    @Override
    protected void closeEvent(final QCloseEvent event) {
        if (generalPageWidget!=null && generalPageWidget.asQWidget() != null) {
            if (generalPageWidget instanceof PropertiesGrid){
                PropertiesGridDisassembler.getInstance().disassemble((PropertiesGrid)generalPageWidget);
            }else{
                generalPageWidget.asQWidget().close();
            }
        }
        
        if (childPagesWidget!=null){
            childPagesWidget.close();
        }
        super.closeEvent(event);
    }
    
    @Override
    protected void closeEmbeddedViews(){    
        
    }
}