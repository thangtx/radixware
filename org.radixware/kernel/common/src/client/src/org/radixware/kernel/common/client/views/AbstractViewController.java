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

package org.radixware.kernel.common.client.views;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import static org.radixware.kernel.common.client.IClientEnvironment.Locator.getEnvironment;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.IView.Visitor;
import org.radixware.kernel.common.client.widgets.actions.Action;


public abstract class AbstractViewController {
    
    private IProgressHandle pHandle;
    private final IClientEnvironment environment;    
    private ViewRestrictions restrictions;
    
    private static abstract class ChildViewVisitor implements IView.Visitor{
        
        protected final IProgressHandle progressHandle;
        
        public ChildViewVisitor(final IProgressHandle progressHandle){
            this.progressHandle = progressHandle;
        }

        @Override
        public boolean cancelled() {
            return progressHandle==null ? false : progressHandle.wasCanceled();
        }       
    }
    
    private static class ModificationsFinder implements IView.Visitor{
        
        private boolean inModifiedState;

        @Override
        public void visit(final IEmbeddedView embeddedView) {
            if (embeddedView.isSynchronizedWithParentView()){
                //embeddedView.getView().finishEdit();
                inModifiedState = embeddedView.inModifiedStateNow();
            }
        }                
        
        @Override
        public final boolean cancelled() {
            return inModifiedState;
        }
        
        public boolean inModifiedState(){
            return inModifiedState;
        }        
    }
    
    private static class SafelyCloseChecker extends ChildViewVisitor{
                
        private boolean canSafelyClose = true;
        private final CleanModelController cleanController;
        
        public SafelyCloseChecker(final IProgressHandle progressHandle, final CleanModelController cleanController){
            super(progressHandle);
            this.cleanController = cleanController;
        }

        @Override
        public void visit(final IEmbeddedView embeddedView) {
            if (embeddedView.isSynchronizedWithParentView() || 
                embeddedView.getViewContext().getType()==IEmbeddedViewContext.EType.EDITOR_PAGE){
                canSafelyClose = embeddedView.getModel().canSafelyClean(cleanController);
            }
        }
        
        @Override
        public final boolean cancelled() {
            if (progressHandle.wasCanceled()){
                canSafelyClose = false;
            }
            return !canSafelyClose;
        }
        
        public boolean canSafelyClose(){
            return canSafelyClose;
        }        
    }
    
    private static class InputChecker extends ChildViewVisitor{
        
        private final List<Property> propertiesWithUnacceptableInput = new LinkedList<>();

        public InputChecker(){
            super(null);
        }
        
        @Override
        public void visit(final IEmbeddedView embeddedView) {
            if (embeddedView.isOpened()){
                propertiesWithUnacceptableInput.addAll(embeddedView.getModel().findPropertiesWithUnacceptableInput());
            }
        }
        
        public List<Property> getPropsWithUnacceptableInput(){
            return propertiesWithUnacceptableInput;
        }        
    }
    
    protected AbstractViewController(final IClientEnvironment environment, final IView view){
        this.environment = environment;
        restrictions = new ViewRestrictions(view);
    }        
    
    private IProgressHandle getProgressHandle() {
        if (pHandle == null) {
            pHandle = environment.getProgressHandleManager().newStandardProgressHandle();                
        }
        return pHandle;
    }
    
    protected void rereadSynchronizedEmbeddedViews(){
        getProgressHandle().startProgress(environment.getMessageProvider().translate("Wait Dialog", "Reading Data..."), true);            
        try{
            visitChildren(new ChildViewVisitor(getProgressHandle()) {
                    @Override
                    public void visit(IEmbeddedView embeddedView) {
                        if (embeddedView.isSynchronizedWithParentView() &&
                            //для перечитывания страниц редактора используется отдельный механизм
                            embeddedView.getViewContext().getType()!=IEmbeddedViewContext.EType.EDITOR_PAGE &&
                            //редактор в режиме создания не перечитывается
                            (embeddedView instanceof IEmbeddedEditor==false || ((IEmbeddedEditor)embeddedView).getModel().isExists())){
                                embeddedView.reread();
                        }
                    }
                }, 
                false);
        }
        finally{
            getProgressHandle().finishProgress();
        }
    }
    
    protected void updateSynchronizedEmbeddedViews(){
        getProgressHandle().startProgress(environment.getMessageProvider().translate("Wait Dialog", "Saving Changes..."), true);
        try{
            visitChildren(new ChildViewVisitor(getProgressHandle()) {
                    @Override
                    public void visit(IEmbeddedView embeddedView) {
                        if (embeddedView.isSynchronizedWithParentView() && embeddedView.inModifiedStateNow()){
                            final Action updateAction;
                            final IView view = embeddedView.getView();
                            if (view instanceof IEditor){
                                updateAction = ((IEditor)view).getActions().getUpdateAction();
                            }
                            else if (view instanceof ISelector){
                                updateAction = ((ISelector)view).getActions().getUpdateAction();
                            }
                            else{
                                updateAction = null;                        
                            }                       
                            if (updateAction!=null && updateAction.isEnabled()){
                                updateAction.trigger();
                            }
                        }
                    }
                }, 
                false);
        }
        finally{
            getProgressHandle().finishProgress();
        }
    }
    
    protected void finishEditInSynchronizedEmbeddedViews(){
        visitChildren(new ChildViewVisitor(getProgressHandle()) {
                @Override
                public void visit(IEmbeddedView embeddedView) {
                    if (embeddedView.isSynchronizedWithParentView()){
                        embeddedView.getModel().finishEdit();
                    }
                }
            }, 
            false);        
    }
        
    protected void cancelChangesInSynchronizedEmbeddedViews(){
        visitChildren(new IView.Visitor() {
                            @Override
                            public void visit(IEmbeddedView embeddedView) {
                                if (embeddedView.isSynchronizedWithParentView()){
                                    final IView view = embeddedView.getView();
                                    if (view instanceof IEditor) {
                                        ((IEditor)view).cancelChanges();
                                    }
                                    else if (view instanceof ISelector) {
                                        ((ISelector)view).getActions().getCancelChangesAction().trigger();
                                    }
                                }
                            }

                            @Override
                            public boolean cancelled() {
                                return false;
                            }
                        },                
                        false);
    }
    
    protected boolean isSynchronizedEmbeddedViewsInModifiedState(){
        final ModificationsFinder modificationsFinder = new ModificationsFinder();
        visitChildren(modificationsFinder, true);
        return modificationsFinder.inModifiedState();
    }
    
    public boolean canSafelyClose(final IView view, final CleanModelController cleanController){
        if (view.getModel()!=null){
            view.getModel().finishEdit();
        }
        final ModificationsList modificationsList = new ModificationsList(view);
        if (!modificationsList.isEmpty()){
            final List<ModificationsList.ModifiedObject> objects = 
                                    modificationsList.askForApplyChanges(environment, view);
            if (objects==null){
                return false;
            }
            else if (objects.isEmpty()){
                modificationsList.cancelChangesCascade();
                return true;
            }
            else if (!modificationsList.applyChangesOnClosingView(environment, view, objects)){
                return false;
            }
        }
        if (view.getModel()!=null){// view can be closed in modificationsList.applyChangesOnClosingView
            final List<Property> propertiesWithUnacceptableInput = new LinkedList<>();
            propertiesWithUnacceptableInput.addAll(view.getModel().findPropertiesWithUnacceptableInput());
            final InputChecker inputChecker = new InputChecker();
            visitChildren(inputChecker, false);
            propertiesWithUnacceptableInput.addAll(inputChecker.getPropsWithUnacceptableInput());
            if (!propertiesWithUnacceptableInput.isEmpty()){
                final String messageTitle = getEnvironment().getMessageProvider().translate("ExplorerDialog", "Confirm to Close");                
                final String modelTitle = view.getModel().getTitle();
                final String messageText;
                if (propertiesWithUnacceptableInput.size()==1){                    
                    final Property property = propertiesWithUnacceptableInput.get(0);
                    final String messageTemplate = 
                        getEnvironment().getMessageProvider().translate("ExplorerDialog", "Field '%1$s' has unacceptable input.\n%2$s\nDo you really want to close '%3$s'?");
                    final String reason = property.getUnacceptableInput().getMessageText();
                    messageText = String.format(messageTemplate, property.getTitle(), reason, modelTitle);
                }else{
                    final StringBuilder propertyTitles = new StringBuilder();
                    for (Property property: propertiesWithUnacceptableInput){
                        if (propertyTitles.length()>0){
                            propertyTitles.append(",\n");
                        }
                        propertyTitles.append(property.getTitle());
                    }
                    final String messageTemplate = 
                        getEnvironment().getMessageProvider().translate("ExplorerDialog", "Following fields has unacceptable input:\n%1$s\nDo you really want to close '%2$s'?");
                    messageText = String.format(messageTemplate, propertyTitles.toString(), modelTitle);                    
                }
                if (getEnvironment().messageConfirmation(messageTitle, messageText)){
                    for (Property property: propertiesWithUnacceptableInput){
                        property.removeUnacceptableInputRegistration();
                    }
                }else{
                    propertiesWithUnacceptableInput.get(0).setFocused();
                    return false;
                }                
            }
        }
        getProgressHandle().startProgress(environment.getMessageProvider().translate("Wait Dialog", "Closing Editor..."), true);
        try{
            final SafelyCloseChecker closeChecker = new SafelyCloseChecker(getProgressHandle(), cleanController);
            visitChildren(closeChecker, false);
            return closeChecker.canSafelyClose();
        }
        finally{
            getProgressHandle().finishProgress();
        }        
    }
    
    public void afterCloseView(){
        restrictions = null;
    }
    
    public ViewRestrictions getViewRestrictions(){
        return restrictions;
    }
    
    protected void closeEmbeddedViews(){
        visitChildren(new IView.Visitor() {
                            @Override
                            public void visit(final IEmbeddedView embeddedView) {
                                if (embeddedView.getViewContext().getType()==IEmbeddedViewContext.EType.EMBEDDED_VIEW){
                                    embeddedView.close();
                                }
                            }

                            @Override
                            public boolean cancelled() {
                                return false;
                            }
                        },                
                        false);
    }
    
    public final void visitChildren(final Visitor visitor, final boolean recursively){
        final List<IEmbeddedView> children = findChildrenViews();
        for (final IEmbeddedView childView: children){
            if (childView.isOpened()){
                visitor.visit(childView);
                if (recursively && !visitor.cancelled()){
                    childView.getView().visitChildren(visitor, true);
                }
            }
            if (visitor.cancelled()){
                break;
            }            
        }
    }
    
    protected abstract List<IEmbeddedView> findChildrenViews();//not recursively
}
