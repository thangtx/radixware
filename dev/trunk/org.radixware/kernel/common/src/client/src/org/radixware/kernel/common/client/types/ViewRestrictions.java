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

package org.radixware.kernel.common.client.types;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.views.IEmbeddedView;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.types.Id;


@AdsPublication("adcAAXJ3IBDIZB7RNTVRKW7JZRGVI")
public final class ViewRestrictions extends Restrictions{
    
    @AdsPublication("prdHCPT5H72VZHTDLOGODRYP4EO4I")
    public static EnumSet<ERestriction> READ_ONLY = 
        EnumSet.of(ERestriction.CREATE, ERestriction.UPDATE, ERestriction.DELETE, ERestriction.NOT_READ_ONLY_COMMANDS);
    
    private static class ChildViewUpdater implements IView.Visitor{
        
        private static ChildViewUpdater INSTANCE_FOR_UPDATE_PROPEDITORS = new ChildViewUpdater(true,false);
        private static ChildViewUpdater INSTANCE_FOR_UPDATE_COMMANDS = new ChildViewUpdater(false,true);
        private static ChildViewUpdater INSTANCE_FOR_UPDATE_ALL = new ChildViewUpdater(true,true);
        private static ChildViewUpdater INSTANCE = new ChildViewUpdater(false,false);
        
        private final boolean updatePropEditors, updateCommands;
        
        private ChildViewUpdater(final boolean updatePropEditors, final boolean updateCommands){
            this.updatePropEditors = updatePropEditors;
            this.updateCommands = updateCommands;
        }
        
        @Override
        public void visit(final IEmbeddedView embeddedView) {
            if (embeddedView.isOpened()){
                embeddedView.getView().getRestrictions().afterChange(updatePropEditors,updateCommands);
            }
        }

        @Override
        public boolean cancelled() {
            return false;
        }   
        
        public static ChildViewUpdater getInstance(final boolean updatePropEditors, final boolean updateCommands){
            if (updateCommands){
                return updatePropEditors ? INSTANCE_FOR_UPDATE_ALL : INSTANCE_FOR_UPDATE_COMMANDS;
            }else{
                return updatePropEditors ? INSTANCE_FOR_UPDATE_PROPEDITORS : INSTANCE;
            }
        }
    }
    
    private final IView view;
    private boolean parentViewRestrictionsInited;
    private ViewRestrictions parentViewRestrictions;
    private final List<Id> restrictedCommands = new LinkedList<>();
    
    @AdsPublication("mth7DCWPPK4ANCYVONAE3VVSMBKPM")
    public ViewRestrictions(){
        this(null);
    }    
    
    @AdsPublication("mthMROXZ6LUIVFXZJLPKXFHAIKTNU")
    public ViewRestrictions(final IView view){
        super(EnumSet.noneOf(ERestriction.class));
        this.view = view;
    }
    
    private ViewRestrictions getParentRestrictions(){
        if (!parentViewRestrictionsInited && view!=null){
            final IView parentView = view.findParentView();
            parentViewRestrictions = parentView == null ? null : parentView.getRestrictions();
            parentViewRestrictionsInited = true;
        }
        return parentViewRestrictions;
    }
        
    @AdsPublication("mth3K2C34AFHFBYDIWAP5RC35IEU4")
    public boolean isRestrictedInView(final ERestriction restriction){
       return restrictions.contains(restriction) || 
              (getParentRestrictions()!=null && restriction!=ERestriction.CHANGE_POSITION &&
               getParentRestrictions().isRestrictedInView(restriction));
    }
    
    private ModelRestrictions getModelRestrictions(){
        if (view==null){
            return null;
        }else if (view.getModel() instanceof EntityModel){
            return ((EntityModel)view.getModel()).getRestrictions();
        }else if (view.getModel() instanceof GroupModel){       
            return ((GroupModel)view.getModel()).getRestrictions();
        }
        return null;
    }
    
    private boolean isRestrictedInModel(final ERestriction restriction){
        final ModelRestrictions modelRestrictions = getModelRestrictions();        
        return modelRestrictions==null ? false : modelRestrictions.isRestrictedInModel(restriction);
    }
        
    boolean isCommandRestrictedInView(final Id commandId, final boolean isReadOnly){
        return (isRestrictedInView(ERestriction.NOT_READ_ONLY_COMMANDS) && !isReadOnly) ||
               isRestrictedInView(ERestriction.ANY_COMMAND) ||
               restrictedCommands.contains(commandId) ||               
               (getParentRestrictions()!=null && getParentRestrictions().isCommandRestrictedInView(commandId,isReadOnly));
    }
        
    private boolean isCommandRestrictedInModel(final Id commandId, final boolean isReadOnly){
        final ModelRestrictions modelRestrictions = getModelRestrictions();
        return modelRestrictions==null ? false : modelRestrictions.isCommandRestrictedInModel(commandId, isReadOnly);
    }

    @Override
    @Deprecated
    public boolean getIsCommandRestricted(final Id cmdId) {
        return isCommandRestrictedInModel(cmdId, false) || isCommandRestrictedInView(cmdId, false);
    }
    
    @Override
    public boolean getIsCommandRestricted(final Id cmdId, final boolean isReadOnly) {
        return isCommandRestrictedInModel(cmdId, isReadOnly) || isCommandRestrictedInView(cmdId, isReadOnly);
    }    
    
    @Override
    protected boolean isRestricted(final ERestriction restriction){
        return isRestrictedInModel(restriction) || isRestrictedInView(restriction);
    }
    
    private void afterChange(final boolean updateChanged, final boolean commandsChanged){
        if (view!=null){
            final ModelRestrictions modelRestrictions = getModelRestrictions();
            if (modelRestrictions!=null){
                modelRestrictions.refreshView(updateChanged);
            }
            if (commandsChanged && view.getModel()!=null){
                for (Id commanId: view.getModel().getAccessibleCommandIds()){
                    view.getModel().getCommand(commanId).afterModify();
                }
            }
            view.visitChildren(ChildViewUpdater.getInstance(updateChanged,commandsChanged), false);
        }
    }
    
    private void changeRestrictions(final EnumSet<ERestriction> actions, final boolean restricted){
        final boolean wasChanged, 
                      updateChanged, 
                      commandChanged;
        if (restricted){            
            updateChanged = 
                !restrictions.contains(ERestriction.UPDATE) && actions.contains(ERestriction.UPDATE);
            commandChanged = 
                (!restrictions.contains(ERestriction.ANY_COMMAND) && actions.contains(ERestriction.ANY_COMMAND))
                || (!restrictions.contains(ERestriction.NOT_READ_ONLY_COMMANDS) && actions.contains(ERestriction.NOT_READ_ONLY_COMMANDS));
            wasChanged = restrictions.addAll(actions);
        }else{
            updateChanged = 
                restrictions.contains(ERestriction.UPDATE) && actions.contains(ERestriction.UPDATE);
            commandChanged = 
                (restrictions.contains(ERestriction.ANY_COMMAND) && actions.contains(ERestriction.ANY_COMMAND))
                || (restrictions.contains(ERestriction.NOT_READ_ONLY_COMMANDS) && actions.contains(ERestriction.NOT_READ_ONLY_COMMANDS));
            wasChanged = restrictions.removeAll(actions);
        }
        if (wasChanged){
            afterChange(updateChanged,commandChanged);
        }
    }
    
    @AdsPublication("mthIU5T7C4BQBDFZGFZYQ6VALUFOY")
    public void addRestrictedCommand(final Id commandId){
        if (!restrictedCommands.contains(commandId)){
            restrictedCommands.add(commandId);        
            afterChange(false, true);
        }
    }
    
    @AdsPublication("mthXSHDNFJPCVGVHPPE63IWZHL6JU")
    public void removeRestrictedCommand(final Id commandId){
        if (restrictedCommands.contains(commandId)){
            restrictedCommands.remove(commandId);
            afterChange(false, true);
        }
    }
    
    @AdsPublication("mthX3XPXIOSQNAB7OEJCCZEU4VXC4")
    public void add(final ERestriction restriction){
        changeRestrictions(EnumSet.of(restriction), true);
    }
    
    @AdsPublication("mthSEFACAXRABBNNGHKBWF7G76HDQ")
    public void add(final EnumSet<ERestriction> restrictions){
        changeRestrictions(restrictions, true);
    }
    
    @AdsPublication("mthJOEMFLBJABHZ7IOVZ7IUR65AOY")
    public void add(final ViewRestrictions other){
        boolean isCommandRestrictionChanged = false;
        final EnumSet<ERestriction> mergedRestrictions = EnumSet.noneOf(ERestriction.class);
        for (ViewRestrictions r=other; r!=null; r=r.getParentRestrictions()){
            for (Id commandId: r.restrictedCommands){
                if (!restrictedCommands.contains(commandId)){
                    restrictedCommands.add(commandId);
                    isCommandRestrictionChanged = true;
                }
            }
            mergedRestrictions.addAll(r.restrictions);
        }        
        if (!isCommandRestrictionChanged){
            isCommandRestrictionChanged = mergedRestrictions.contains(ERestriction.ANY_COMMAND);
        }
        final boolean isUpdateChanged = 
            !restrictions.contains(ERestriction.UPDATE) && mergedRestrictions.contains(ERestriction.UPDATE);
        if (restrictions.addAll(mergedRestrictions) || isCommandRestrictionChanged){
            afterChange(isUpdateChanged, isCommandRestrictionChanged);
        }
    }
    
    @AdsPublication("mthPB3YZFH535CGBNRDL5XWBYWA4A")
    public void add(final Restrictions other){
        if (other instanceof ViewRestrictions){
            add((ViewRestrictions)other);
        }else{
            changeRestrictions(other.restrictions, true);
        }
    }
    
    @AdsPublication("mthV6THWZBI4NE3RFUHWEFIRA6OYI")
    public void remove(final ERestriction restriction){
        changeRestrictions(EnumSet.of(restriction), false);
    }
    
    @AdsPublication("mthURWZ5VBZYZAAVO6YZE4U4ECDYU")
    public void remove(final EnumSet<ERestriction> restrictions){
        changeRestrictions(restrictions, false);
    }
    
    @AdsPublication("mthMA26J2K3UJHT3GWLJBSENAJRFI")
    public void clear(){
        if (!restrictions.isEmpty() || !restrictedCommands.isEmpty()){
            final boolean isUpdateRestricted = restrictions.contains(ERestriction.UPDATE);
            final boolean isSomeCommandRestricted = restrictions.contains(ERestriction.ANY_COMMAND) 
                                                    || !restrictedCommands.isEmpty();        
            restrictions.clear();
            restrictedCommands.clear();
            afterChange(isUpdateRestricted,isSomeCommandRestricted);
        }
    }
    
    public boolean greaterThan(final ModelRestrictions modelRestrictions){
        for (ERestriction restriction: EnumSet.allOf(ERestriction.class)){
            if (!modelRestrictions.isRestrictedInModel(restriction) && isRestrictedInView(restriction)){
                return true;
            }
        }
        return false;
    }
}