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

package org.radixware.kernel.common.client.tree.nodes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.AuthError;
import org.radixware.kernel.common.client.errors.BrokenConnectionError;
import org.radixware.kernel.common.client.errors.EasError;
import org.radixware.kernel.common.client.errors.IAlarm;
import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.errors.UnsupportedDefinitionVersionError;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.Definition;
import org.radixware.kernel.common.client.meta.IExplorerItemsHolder;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadParagraphLinkDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItems;
import org.radixware.kernel.common.client.meta.explorerItems.RadParentRefExplorerItemDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadSelectorUserExplorerItemDef;
import org.radixware.kernel.common.client.meta.filters.RadContextFilter;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.client.tree.ExplorerItemView;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.common.client.tree.UserExplorerItemsStorage;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;



public abstract class ExplorerTreeNode implements IExplorerTreeNode {

    private IExplorerTree tree;
    private ExplorerTreeNode parent;
    private long definitionVersion = -1;
    private long internalModelIndex = -1;
    private Exception error;
    private ExplorerItemView view;
    private final List<IExplorerTreeNode> childs = new ArrayList<>();
    private int choosenEntitiesCount = 0;
    private boolean childsWasInited;
    private boolean initingChilds;

    public ExplorerTreeNode(final IExplorerTree owner, final ExplorerTreeNode parent) {
        tree = owner;
        this.parent = parent;
    }

    protected final IClientEnvironment getEnvironment() {
        return tree.getEnvironment();
    }

    @Override
    public final boolean isValid() {
        if (tree==null){
            return false;//node was removed
        }
        if (view == null
                && definitionVersion != getEnvironment().getDefManager().getAdsVersion().getNumber()) {
            initExplorerItemView();
        }
        return error == null && getView() != null && getView().getModel() != null;

    }

    protected final Model getRadixModel() {
        return isValid() ? getView().getModel() : null;
    }

    private void initExplorerItemView() {
        error = null;
        definitionVersion = -1;
        try {
            setModel(openModel());
        } catch (Exception exception) {
            error = exception;
            view = null;
            definitionVersion = getEnvironment().getDefManager().getAdsVersion().getNumber();
            traceException(getEnvironment(), exception, getTitle());
        }
    }

    protected final void setModel(Model model) {
        view = new ExplorerItemView(model, getExplorerItemId(), this);
        model.setExplorerItemView(view);
    }

    public String getTitle() {
        if (isValid()) {
            return getView().getTitle();
        } else {
            return "????";
        }
    }

    public Icon getIcon() {
        return isValid() ? getView().getIcon() : null;
    }

    @Override
    public Exception getCreationModelException() {
        return error;
    }

    protected static void traceException(IClientEnvironment environment, final Exception error, final String explorer_item_title) {
        final ClientTracer tracer = environment.getTracer();
        if (error instanceof InterruptedException) {
            final String msg = environment.getMessageProvider().translate("TraceMessage", "Cannot add explorer item %s: canceled by user");
            environment.getTracer().put(EEventSeverity.DEBUG, String.format(msg, explorer_item_title),
                    EEventSource.EXPLORER);
        }
        if (error instanceof IClientError) {
            final IClientError err = (IClientError) error;
            final String msg = environment.getMessageProvider().translate("TraceMessage", "Cannot add explorer item %s:\n %s");
            tracer.put(EEventSeverity.ERROR, String.format(msg, explorer_item_title, err.getLocalizedMessage(environment.getMessageProvider())), EEventSource.EXPLORER);
            tracer.put(EEventSeverity.DEBUG, err.getDetailMessage(environment.getMessageProvider()), EEventSource.EXPLORER);
        } else {
            final String msg = environment.getMessageProvider().translate("TraceMessage", "Cannot add explorer item %s:\n %s"),
                    reason = ClientException.getExceptionReason(environment.getMessageProvider(), error),
                    stack = ClientException.exceptionStackToString(error);
            tracer.put(EEventSeverity.ERROR, String.format(msg, explorer_item_title, reason), EEventSource.EXPLORER);
            tracer.put(EEventSeverity.DEBUG, stack, EEventSource.EXPLORER);
        }
    }

    protected abstract Model openModel() throws Exception;

    public abstract Id getOwnerDefinitionId();

    public abstract Id getExplorerItemId();

    protected abstract int getInternalId();    
    
    protected abstract boolean definitionIsExplorerItemsHolder();

    @Override
    public ExplorerTreeNode getParentNode() {
        return parent;
    }

    @Override
    public IExplorerTree getExplorerTree() {
        return tree;
    }

    @Override
    public String getCreationModelExceptionMessage() {
        final Id ownerClassId = getOwnerDefinitionId();
        if (ownerClassId != null) {
            final String msg = getEnvironment().getMessageProvider().translate("ExplorerTree", "Can't open explorer item #%s\n(class GUID: #%s)");
            return String.format(msg, getExplorerItemId().toString(), ownerClassId.toString());
        } else {
            final String msg = getEnvironment().getMessageProvider().translate("ExplorerTree", "Can't open explorer item #%s");
            return String.format(msg, getExplorerItemId().toString());
        }
    }

    @Override
    public ExplorerItemView getView() {
        if (view != null && view.getModel().getClass().getClassLoader() != getEnvironment().getDefManager().getClassLoader()) {
            
            if (parent != null && !parent.getChildNodes().contains(this)) {
                //parent node exists, but its child nodes does not holds this node.
                //It means that this node was removed (for example during update definitions version).
                view = null;
                error = new IllegalStateException("Broken node");
                return null;
            }
            error = null;
            initExplorerItemView();

            if (childsWasInited) //actualize array of childs
            {
                initChilds();
            }
        } else if (view == null && (error == null || definitionVersion != getEnvironment().getDefManager().getAdsVersion().getNumber())) {
            initExplorerItemView();
        }

        return view;
    }

    private void close() {
        for (IExplorerTreeNode child : childs) {
            ((ExplorerTreeNode) child).close();
        }
        childs.clear();
        if (view != null && view.getModel() != null) {
            view.getModel().clean();
        }
        parent = null;        
        tree = null;
        view = null;
    }

    @Override
    public List<IExplorerTreeNode> getChildNodes() {
        if (!childsWasInited && !initingChilds) {
            initChilds();
        }
        return Collections.unmodifiableList(childs);
    }

    @Override
    public boolean isChildNodesInited(){
        return childsWasInited;
    }
        
    @Override
    public List<IExplorerTreeNode> getChildNodesRecursively() {
        final List<IExplorerTreeNode> result = new ArrayList<>();
        final Stack<ExplorerTreeNode> parents = new Stack<>();
        parents.add(this);
        ExplorerTreeNode node;
        while (!parents.isEmpty()) {
            node = parents.pop();
            if (node.childsWasInited) {
                result.addAll(node.childs);
                for (IExplorerTreeNode child : node.childs) {
                    parents.push((ExplorerTreeNode) child);
                }
            }
        }
        return Collections.unmodifiableList(result);
    }
    
    public final IExplorerItemsHolder getTopLevelExplorerItemsHolder(){
        if (getRadixModel().getDefinition().getId().equals(getOwnerDefinitionId())){//top level definition
            if (getRadixModel().getDefinition() instanceof IExplorerItemsHolder){
                return (IExplorerItemsHolder)getRadixModel().getDefinition();
            }else{
                return null;
            }
        }else{ //inner definition
            final Definition ownerDefnition = 
                    getEnvironment().getDefManager().getDefinition(getOwnerDefinitionId());
            if (ownerDefnition  instanceof IExplorerItemsHolder){
                return (IExplorerItemsHolder)ownerDefnition;
            }else{
                return null;
            }
        }
    }
    
    private IExplorerItemsHolder findNearestExplorerItemsHolder(){
        for (ExplorerTreeNode node=this; node!=null; node=node.getParentNode()){
            if (node.definitionIsExplorerItemsHolder()
                && node.getRadixModel().getDefinition() instanceof IExplorerItemsHolder){
                return (IExplorerItemsHolder)node.getRadixModel().getDefinition();
            }
        }
        return getTopLevelExplorerItemsHolder();
    }

    private void initChilds() {
        initingChilds = true;
        try{
            final Model model = getRadixModel();
            final UserExplorerItemsStorage userExplorerItems = UserExplorerItemsStorage.getInstance(getEnvironment());
            if (isValid() && !(model instanceof GroupModel)) {
                if (getEnvironment().getEasSession().isBusy()){
                    return;
                }
                //move existsing child nodes (except of choosen entities) into temporary array
                final Collection<ExplorerTreeNode> prevChilds = new ArrayList<>();
                for (int i = childs.size() - 1; i >= choosenEntitiesCount; i--) {
                    if (childs.get(i) instanceof ExplorerTreeNode) {
                        prevChilds.add((ExplorerTreeNode) childs.get(i));
                    }
                    childs.remove(i);
                }
                final IExplorerItemsHolder eiHolder = findNearestExplorerItemsHolder();
                final Id userExplorerItemContextId = getUserExplorerItemsStorageContextId();
                if (eiHolder!=null && model.getDefinition() instanceof IExplorerItemsHolder){
                    RadExplorerItems items = 
                        ((IExplorerItemsHolder)model.getDefinition()).getChildrenExplorerItems();                
                    final Id parentDefinitionId = model.getDefinition().getId();
                    if (!eiHolder.getId().equals(model.getDefinition().getId())){//possibly we need to reorder items
                        final Id[] newItemsOrder = eiHolder.getChildrenExplorerItemsOrder(parentDefinitionId);
                        if (newItemsOrder.length>0){
                            //reordering items
                            items = new RadExplorerItems(null, items, newItemsOrder);
                        }
                    }
                    boolean isAccessible, isEntityModel;
                    for (RadExplorerItemDef ei : items) {
                        if (!ei.isValid()) {
                            final String message = getEnvironment().getMessageProvider().translate("TraceMessage", "explorer item #%s is not valid: omitting");
                            getEnvironment().getTracer().debug(String.format(message, ei.getId()));
                            continue;
                        }
                        isEntityModel = model instanceof EntityModel || model.getContext().getHolderEntityModel() != null;
                        try {
                            isAccessible = model.isExplorerItemAccessible(ei.getId());
                        } catch (InterruptedException ex) {
                            traceException(getEnvironment(), ex, ei.toString());
                            break;
                        } catch (ServiceCallException ex) {
                            throw new BrokenConnectionError(ex);
                        } catch (ServiceClientException ex) {
                            traceException(getEnvironment(), ex, ei.toString());
                            break;
                        }
                        isAccessible = isAccessible && (isEntityModel || tree.isExplorerItemAccessible(ei.getId()));
                        if (ei.isVisible() && isAccessible && eiHolder.isExplorerItemVisible(parentDefinitionId, ei.getId())) {

                            ExplorerTreeNode childNode = findNodeByExplorerItemId(prevChilds, ei.getId());

                            if (childNode == null) {
                                //Creating new subnode
                                childNode = addNewChildNode(ei);
                            }else{
                                //subnode corresponding to explorer item was already created
                                childs.add(childNode);                       
                            }

                            if (childNode!=null && childNode.isValid()){
                                final List<RadSelectorUserExplorerItemDef> userItemsList =
                                    userExplorerItems.findUserExplorerItemsForSourceExplorerItem(userExplorerItemContextId, ei.getId(), items);
                                if (userItemsList!=null && !userItemsList.isEmpty()){
                                    for (RadSelectorUserExplorerItemDef userEI: userItemsList){
                                        if (validateSelectorUserExplorerItem(userEI)){
                                            final ExplorerTreeNode userChildNode = 
                                                findNodeByExplorerItemId(prevChilds, userEI.getId());
                                            if (userChildNode==null){
                                                addNewChildNode(userEI);
                                            }else{
                                                childs.add(userChildNode);
                                            }
                                        }
                                    }
                                }
                            }
                        }//if (ei.isVisible() && isAccessible)

                    }//for (ExplorerItemDef ei : items)
                }
            } else if (!isValid()) {
                childs.clear();
            }
            childsWasInited = true;
            for (IExplorerTreeNode childNode : childs) {
                if (childNode.isValid()) {
                    try {
                        getRadixModel().afterInsertChildItem(childNode.getView());
                    }catch(UnsupportedDefinitionVersionError err){
                        final String message = getEnvironment().getMessageProvider().translate("ExplorerError", "Exception occurred in handler of '%s' event:");
                        getEnvironment().getTracer().debug(String.format(message, "afterInsertChildItem") + "\n" + ClientException.exceptionStackToString(err));
                        getEnvironment().processException(err);
                    } catch(EasError | AuthError err){
                        if (err instanceof IAlarm){
                            getEnvironment().processException(err);
                            break;                            
                        }else{
                            final String message = getEnvironment().getMessageProvider().translate("ExplorerError", "Exception occurred in handler of '%s' event:");
                            getEnvironment().getTracer().error(String.format(message, "afterInsertChildItem") + "\n" + ClientException.exceptionStackToString(err));
                        }
                    } catch (Exception ex) {
                        final String message = getEnvironment().getMessageProvider().translate("ExplorerError", "Exception occurred in handler of '%s' event:");
                        getEnvironment().getTracer().error(String.format(message, "afterInsertChildItem") + "\n" + ClientException.exceptionStackToString(ex));
                    }
                }
            }
        }finally{
            initingChilds = false;
        }
    }
    
    private boolean validateSelectorUserExplorerItem(final RadSelectorUserExplorerItemDef explorerItem){        
        final List<RadContextFilter> filters = explorerItem.getContextFilters();
        for (RadContextFilter filter: filters){
            final String message = filter.validate(getEnvironment(), explorerItem.getModelDefinition());
            if (message!=null){                
                final String messageTemplate = 
                    getEnvironment().getMessageProvider().translate("TraceMessage", "Failed to restore user explorer item '%1$s'\n%2$s");
                final String traceMessage = String.format(messageTemplate, explorerItem.getTitle(), message);
                getEnvironment().getTracer().debug(traceMessage);
                return false;
            }
        }
        return true;
    }
    
    private static ExplorerTreeNode findNodeByExplorerItemId(final Collection<ExplorerTreeNode> nodes, final Id explorerItemId){
        for (ExplorerTreeNode node : nodes) {
            if (explorerItemId.equals(node.getExplorerItemId())) {
                return node;
            }
        }
        return null;
    }
    
    private ExplorerTreeNode addNewChildNode(final RadExplorerItemDef ei){
        final ExplorerTreeNode childNode;        
        if (ei instanceof RadParentRefExplorerItemDef) {
            childNode = new ParentEntityNode(tree, this, (RadParentRefExplorerItemDef) ei);
            if (childNode.getRadixModel() == null && childNode.getCreationModelException() == null) {
                return null;//Не существует родительской сущности для данного узла - нормальная ситуация
            }
        } else if (ei instanceof RadParagraphLinkDef){
            childNode = new ParagraphLinkNode(tree, this, ei.getId());
        } else {
            childNode = new ExplorerItemNode(tree, this, ei.getId());
        }

        if (childNode.isValid()) {//обработка событий
            final Model model = getRadixModel();
            if (model instanceof EntityModel) {
                EntityModel entity = (EntityModel) model;
                if (!entity.beforeInsertChildItem(childNode.getView())) {
                    return null;
                }
            } else if (model.getContext().getHolderEntityModel() != null) {
                if (!model.getContext().getHolderEntityModel().beforeInsertChildItem(childNode.getView())) {
                    return null;
                }
            }
        }
        childs.add(childNode);
        return childNode;
    }

    public int getActualIndexForNewChoosenEntity(final int index) {
        if (index <= 0) {
            return 0;
        } else if (index >= choosenEntitiesCount) {
            return choosenEntitiesCount;
        } else {
            return index;
        }
    }

    public void addNode(final IExplorerTreeNode node, final int index) {
        if (!childsWasInited) {
            initChilds();
        }
        childs.add(index, node);
        if (node instanceof ChoosenEntityNode){
            choosenEntitiesCount++;
        }
    }

    public void removeNode(final int index) {
        if (index < choosenEntitiesCount) {
            choosenEntitiesCount--;
        }
        if (index < childs.size()) {
            final ExplorerTreeNode childToRemove = (ExplorerTreeNode) childs.get(index);
            childs.remove(index);            
            final String settingsGroupName;
            if (isValid() && childToRemove.isValid()) {
                settingsGroupName = childToRemove.getRadixModel().getConfigStoreGroupName();
                childToRemove.getRadixModel().setExplorerItemView(null);
                getRadixModel().afterRemoveChildItem(childToRemove.getView());
            }else{
                settingsGroupName = null;
            }
            final Id childExplorerItemId = childToRemove.getExplorerItemId();
            childToRemove.close();
            if (childExplorerItemId!=null && 
                childExplorerItemId.getPrefix()==EDefinitionIdPrefix.USER_EXPLORER_ITEM){
                final Id ownerDefinitionId = getUserExplorerItemsStorageContextId();
                final UserExplorerItemsStorage storage = UserExplorerItemsStorage.getInstance(getEnvironment());
                storage.removeUserExplorerItem(ownerDefinitionId, childExplorerItemId, settingsGroupName);
            }            
        }
    }    
    
    private Id getUserExplorerItemsStorageContextId(){
        return UserExplorerItemsStorage.getContextId(getTopLevelExplorerItemsHolder());
    }
    
    @Override
    public boolean isRemovable() {
        return (isValid() && (getView().isChoosenObject() || isChoosenEntityExists(getChildNodes())))
               || (getExplorerItemId()!=null && getExplorerItemId().getPrefix()==EDefinitionIdPrefix.USER_EXPLORER_ITEM);
    }
    
    private static boolean isChoosenEntityExists(final List<IExplorerTreeNode> nodes) {
        for (IExplorerTreeNode node : nodes) {
            if (node.isValid() && node.getView().isChoosenObject()) {
                return true;
            }
        }
        return false;
    }    

    @Override
    public int hashCode() {
        return getInternalId();
    }

    @Override
    public long getIndexInExplorerTree() {
        return internalModelIndex;
    }

    public void setInternalIndexInExplorerTree(long index) {
        internalModelIndex = index;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof ExplorerTreeNode) {
            final ExplorerTreeNode other = (ExplorerTreeNode) obj;
            return other.getInternalId() == getInternalId();
        }

        return false;
    }

    @Override
    public String getPath() {
        return toString();
    }

    @Override
    public String toString() {
        String result = isValid() ? getView().toString() : getTitle();
        for (IExplorerTreeNode node = parent; node != null; node = node.getParentNode()) {
            result = isValid() ? node.getView().toString() + "/" + result : getTitle();
        }
        return result;
    }

    public abstract ExplorerTreeNode clone(final IExplorerTree tree);
}
