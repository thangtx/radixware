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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ImageManager;
import org.radixware.kernel.common.client.meta.IExplorerItemsHolder;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItems;
import org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.types.Id;


public abstract class UserEICreator {
    
    private final GroupModel model;
    private final Model parentModelForInsertUserEI;
    private final IButton insertButton;
    
    public UserEICreator(final GroupModel groupModel){
        model = groupModel;
        final boolean canInsertUserEI;        
        if (model.getContext() instanceof IContext.TableSelect){
            final IExplorerItemView itemView = model.getExplorerItemView();
            if (itemView==null){
                final Model parentModel = findParentModelToInsertUserEI();
                canInsertUserEI = 
                    parentModel!=null && parentModel.getExplorerItemView()!=null && parentModel.getView()!=null;
                parentModelForInsertUserEI = canInsertUserEI ? parentModel : null;
            }else{
                canInsertUserEI = true;
                parentModelForInsertUserEI = null;
            }
        }else{
            canInsertUserEI = false;
            parentModelForInsertUserEI = null;
        }
        
        if (canInsertUserEI){
            final IClientEnvironment environment = model.getEnvironment();
            insertButton = environment.getApplication().getWidgetFactory().newToolButton();            
            final ImageManager imageManager = environment.getApplication().getImageManager();
            insertButton.setIcon(imageManager.getIcon(ClientIcon.Selector.INSERT_INTO_TREE));
            insertButton.setObjectName("insert user ei button");
            final String toolTip = 
                environment.getMessageProvider().translate("Selector", "Save Settings in Explorer Tree");
            insertButton.setToolTip(toolTip);
            insertButton.setEnabled(false);
            insertButton.addClickHandler(new IButton.ClickHandler() {
                @Override
                public void onClick(final IButton source) {
                    UserEICreator.this.processButtonClick();
                }
            });
        }else{
            insertButton = null;
        }        
    }
    
    private Model findParentModelToInsertUserEI(){
        if (model.getContext() instanceof IContext.TableSelect){
            final RadExplorerItemDef explorerItemDef = 
                ((IContext.TableSelect)model.getContext()).explorerItemDef;
            if (explorerItemDef.isVisible() && explorerItemDef instanceof RadSelectorExplorerItemDef){
                final EntityModel parentEntityModel = model.getContext().getHolderEntityModel();
                if (parentEntityModel==null){
                    return null;
                }else{
                    final IExplorerItemsHolder itemsHolder = parentEntityModel.getEditorPresentationDef();
                    final RadExplorerItems items = itemsHolder.getChildrenExplorerItems();                
                    final boolean isChildEI = items.findExplorerItem(explorerItemDef.getId())!=null;               
                    if (isChildEI && explorerItemDef.isVisible()){
                        final boolean isEIVisible = explorerItemDef.isVisible() &&
                            itemsHolder.isExplorerItemVisible(itemsHolder.getId(), explorerItemDef.getId());
                        if (isEIVisible){
                            if (parentEntityModel.getExplorerItemView()!=null){
                                return parentEntityModel;
                            }
                            else if (parentEntityModel.getContext() instanceof IContext.InSelectorEditing){
                                final IContext.InSelectorEditing context = 
                                    (IContext.InSelectorEditing)parentEntityModel.getContext();
                                return context.getGroupModel();
                            }else if (parentEntityModel.getContext() instanceof IContext.SelectorRow){
                                final IContext.SelectorRow context = 
                                    (IContext.SelectorRow)parentEntityModel.getContext();
                                return context.parentGroupModel;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public IButton getToolButton(){
        return insertButton;
    }
    
    public void updateInsertButton(){
        if (insertButton==null){
            return;
        }
        final boolean isSomeFilterApplyed = model.getCurrentFilter()!=null
                                            && model.getGroupView()!=null
                                            && !model.getGroupView().isDisabled();        
        if (isSomeFilterApplyed){
            if (parentModelForInsertUserEI==null){
                insertButton.setEnabled(true);
            }else if (parentModelForInsertUserEI instanceof GroupModel){                
                final ISelector parentView = ((GroupModel)parentModelForInsertUserEI).getGroupView();
                insertButton.setEnabled(parentView!=null && parentView.getActions().getInsertAction().isEnabled());
            }else {
                insertButton.setEnabled(parentModelForInsertUserEI.getView()!=null);
            }
        }else{
            insertButton.setEnabled(false);
        }        
    }    

    private void processButtonClick() {
        if (parentModelForInsertUserEI==null){
            final IExplorerItemView itemView = model.getExplorerItemView();
            final String eiTitle = getUserExplorerItemTitle(itemView.suggestUserExplorerItemTitle(model));
            if (eiTitle!=null){
                itemView.insertUserExplorerItem(model, eiTitle);
            }            
        }else{
            final IExplorerItemView parentItemView = parentModelForInsertUserEI.getExplorerItemView();
            parentItemView.getExplorerTree().lock();
            try{
                final int prevCount = parentItemView.getChildsCount();
                final IExplorerItemView parentObjectView;
                final boolean parentObjectInserted;
                if (parentModelForInsertUserEI instanceof GroupModel){
                    parentObjectView = 
                        ((GroupModel)parentModelForInsertUserEI).getGroupView().insertEntity();
                    parentObjectInserted = prevCount < parentItemView.getChildsCount();
                }else{
                    parentObjectView = parentModelForInsertUserEI.getExplorerItemView();
                    parentObjectInserted = false;
                }
                final RadExplorerItemDef explorerItemDef = 
                        ((IContext.TableSelect)model.getContext()).explorerItemDef;
                final IExplorerItemView itemView = 
                    findChildExplorerItemViewByDefnitionId(parentObjectView, explorerItemDef.getId());
                if (itemView==null){
                    if (parentObjectInserted){
                        parentObjectView.remove();
                    }
                }else{
                    final String eiTitle = getUserExplorerItemTitle(itemView.suggestUserExplorerItemTitle(model));
                    if (eiTitle!=null){                
                        if (itemView.insertUserExplorerItem(model, eiTitle)==null && parentObjectInserted){
                            parentObjectView.remove();
                        }
                    }else if (parentObjectInserted){
                        parentObjectView.remove();
                    }                                
                }
            }finally{
                parentItemView.getExplorerTree().unlock();
            }            
        }
    }
    
    private IExplorerItemView findChildExplorerItemViewByDefnitionId(final IExplorerItemView rootView, final Id explorerItemId){
        final Collection<Id> viewedExplorerItems = new ArrayList<>();
        final Stack<IExplorerItemView> stack = new Stack<>();
        int childsCount = rootView.getChildsCount();
        for (int i=0; i<childsCount; i++){
            stack.push(rootView.getChild(i));
        }
        while(!stack.isEmpty()){
            final IExplorerItemView current = stack.pop();
            if (current.getExplorerItemId()!=null){
                if (current.getExplorerItemId().equals(explorerItemId)){
                    return current;
                }else if (!viewedExplorerItems.contains(current.getExplorerItemId())){
                    viewedExplorerItems.add(current.getExplorerItemId());
                    childsCount = current.getChildsCount();
                    for (int i=0; i<childsCount; i++){
                        stack.push(current.getChild(i));
                    }                    
                }            
            }
        }
        return null;
    }    
    
    protected abstract String getUserExplorerItemTitle(final String initialTitle);
}
