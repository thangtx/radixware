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

package org.radixware.kernel.explorer.widgets.selector;

import com.trolltech.qt.QSignalEmitter;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QCloseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.errors.CantOpenSelectorError;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.FilteredByPropertiesGroupModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.GroupModelReader;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.ProxyGroupModel;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;

import org.radixware.kernel.explorer.views.selector.Selector;


public class SelectorTreeBySingleGroupModel extends SelectorTree {
    
    private final static String EXPANDED_ITEMS_SETTING_NAME = "expandedItems";
    private final static String EXPANDED_ITEM_SETTING_NAME = "items";

    private static class SelectorModelImpl extends SelectorModel {

        private final Map<Pid, GroupModel> childGroups = new HashMap<>();
        private final GroupModel sourceGroup;
        private final Id keyPropertyId, refPropertyId;

        public SelectorModelImpl(final GroupModel sourceGroup, final Id keyPropertyId, final Id refPropertyId, final Object keyPropertyValue) {
            super(new FilteredByPropertiesGroupModel(sourceGroup));
            getRootGroupModel().setView(sourceGroup.getGroupView());
            ((FilteredByPropertiesGroupModel) getRootGroupModel()).setPropertyFilter(refPropertyId, keyPropertyValue);
            this.sourceGroup = sourceGroup;
            this.keyPropertyId = keyPropertyId;
            this.refPropertyId = refPropertyId;
        }

        private GroupModel getChildGroup(final EntityModel parent) {
            if (parent == null) {
                return getRootGroupModel();
            }
            if (!childGroups.containsKey(parent.getPid())) {
                final FilteredByPropertiesGroupModel group = new FilteredByPropertiesGroupModel(sourceGroup);
                group.setPropertyFilter(refPropertyId, parent.getProperty(keyPropertyId).getValueObject());
                childGroups.put(parent.getPid(), group);
                return group;
            }
            return childGroups.get(parent.getPid());
        }

        @Override
        protected GroupModel createChildGroupModel(EntityModel parentEntity) {
            return getChildGroup(parentEntity);
        }

        @Override
        public boolean canCreateChild(EntityModel parentEntity) {
            return !getRootGroupModel().getRestrictions().getIsCreateRestricted();
        }

        @Override
        protected boolean hasChildren(EntityModel parent) {
            try {
                return new GroupModelReader(getChildGroup(parent)).getFirstEntityModel() != null;
            } catch (ServiceClientException ex) {
                getRootGroupModel().getEnvironment().getTracer().put(ex);
                return false;
            } catch (InterruptedException ex) {
                return false;
            }
        }

        @Override
        protected void clear(QModelIndex parent) {
            childGroups.clear();
            super.clear(parent);
        }

        @Override
        public void reread(QModelIndex parent) throws ServiceClientException, InterruptedException  {
            super.reread(null);
            lock();
            try {
                new GroupModelReader(sourceGroup).loadEntireGroup();
            }
            finally {
                unlock();
            }

        }

        @Override
        public boolean removeRows(final int row, final int count, QModelIndex parent) {
            if (row >= 0 && (row + count - 1) < rowCount(parent)) {
                final GroupModel removedFrom = getChildGroup(parent);
                final List<Integer> sourceGroupIndexes = new ArrayList<>();
                //собрать в массив индексы удаляемых сущностей в исходной группе
                try{
                    for (int i = 0; i < count; i++) {
                        collectChildEntities(index(row + i, 0, parent), sourceGroupIndexes);
                    }
                }
                catch(InterruptedException exception){
                    return false;
                }
                catch (ServiceClientException exception){
                    showErrorOnReceivingData(exception);
                    return false;
                }                    
                if (super.removeRows(row, count, parent)) {
                    Collections.sort(sourceGroupIndexes);
                    for (int i = sourceGroupIndexes.size() - 1; i >= 0; i--) {
                        sourceGroup.removeRow(sourceGroupIndexes.get(i));
                    }
                    fillChildGroup(removedFrom);
                    return true;
                }
            }
            return false;
        }

        private void collectChildEntities(final QModelIndex startWith, final List<Integer> collectTo) throws ServiceClientException, InterruptedException  {
            final Stack<QModelIndex> stack = new Stack<>();
            stack.push(startWith);
            QModelIndex index = startWith;
            ProxyGroupModel group = (ProxyGroupModel) getChildGroup(index.parent());
            collectTo.add(group.mapEntityIndexToSource(index.row()));

            while (!stack.empty()) {
                index = stack.pop();
                while (canReadMore(index)) {
                    readMore(index);
                }
                group = (ProxyGroupModel) getChildGroup(index);
                for (int i = 0; i < rowCount(index); i++) {
                    stack.push(index(i, 0, index));
                    collectTo.add(group.mapEntityIndexToSource(i));
                }
            }
        }

        private void fillChildGroup(final GroupModel group) {
            try {
                new GroupModelReader(group).loadEntireGroup();
            } catch (ServiceClientException ex) {
                sourceGroup.showException(ex);
            } catch (InterruptedException ex) {
            }
        }

        public void resetModel() {
            reset();
        }
    }
    
    private final GroupModel sourceGroupModel;
    private final Id keyPropertyId, refPropertyId;    
    private boolean storeExpandedItems;
    private String expandedItemsSettingsPath;    

    public SelectorTreeBySingleGroupModel(final Selector selector, final Id keyPropertyId, final Id parentRefPropertyId) {
        super(selector, new SelectorModelImpl(selector.getGroupModel(), keyPropertyId, parentRefPropertyId, null));
        sourceGroupModel = selector.getGroupModel();
        this.keyPropertyId = keyPropertyId;
        this.refPropertyId = parentRefPropertyId;
    }

    public SelectorTreeBySingleGroupModel(final Selector selector, final GroupModel group, final Id keyPropertyId, final Id parentRefPropertyId) {
        super(selector, new SelectorModelImpl(group, keyPropertyId, parentRefPropertyId, null));
        sourceGroupModel = group;
        this.keyPropertyId = keyPropertyId;
        this.refPropertyId = parentRefPropertyId;
    }

    public SelectorTreeBySingleGroupModel(final Selector selector, final GroupModel group, final Id keyPropertyId, final Id parentRefPropertyId, final Object keyPropertyValue) {
        super(selector, new SelectorModelImpl(group, keyPropertyId, parentRefPropertyId, keyPropertyValue));
        sourceGroupModel = group;
        this.keyPropertyId = keyPropertyId;
        this.refPropertyId = parentRefPropertyId;
    }

    @Override
    public void bind() {
        try{
            new GroupModelReader(sourceGroupModel).loadEntireGroup();
        } catch (ServiceClientException ex) {
            throw new CantOpenSelectorError(sourceGroupModel, ex);
        } catch (InterruptedException ex) {
            return;
        }
        super.bind();
        if (storeExpandedItems){
            restoreExpandedItems();
        }
    }

    @Override
    public void closeEvent(final QCloseEvent event) {
        if (storeExpandedItems){
            storeExpandedItems();
        }
        super.closeEvent(event);
    }
    
    

    @Override
    public void afterPrepareCreate(EntityModel entity) {
        final SelectorModel model = (SelectorModel) model();

        if (!(entity.getContext() instanceof IContext.InSelectorCreating)) {
            if (entity.getContext() == null) {
                throw new IllegalArgumentException("Invalid entity context.\n"
                        + IContext.InSelectorCreating.class.getSimpleName()
                        + " context expected, but no context found.");
            } else {
                throw new IllegalArgumentException("Invalid entity context.\n"
                        + IContext.InSelectorCreating.class.getSimpleName()
                        + " context expected, but "
                        + entity.getContext().getClass().getSimpleName()
                        + " context found.");
            }
        }

        final QModelIndex currentIndex = currentIndex();
        final QModelIndex parentIndex = currentIndex != null ? currentIndex.parent() : null;
        final QSignalEmitter action = QObject.signalSender();
        final EntityModel parentEntity;
        if (currentIndex == null) {
            if (model.rowCount() == 0) {
                parentEntity = null;
            } else {
                throw new IllegalUsageError("Cannot find parent entity");
            }
        } else if (action == actions.createSiblingAction || action == selector.getActions().getCreateAction()) {
            parentEntity = model.getEntity(parentIndex);
        } else if (action == actions.createChildAction) {
            parentEntity = model.getEntity(currentIndex);
        } else//action==null or unknown action
        {
            parentEntity = null;
        }

        if (parentEntity != null) {
            entity.getProperty(refPropertyId).setValueObject(parentEntity.getProperty(keyPropertyId).getValueObject());
        }
    }

    public void expandAllBranches() throws ServiceClientException, InterruptedException  {
        final Stack<QModelIndex> parents = new Stack<>();
        QModelIndex index;
        lockInput();
        try {
            readAll(null);
            for (int i = 0; i < model().rowCount(null); i++) {
                parents.push(model().index(i, 0));
            }

            while (!parents.isEmpty()) {
                index = parents.pop();
                readAll(index);
                if (model().hasChildren(index)){
                    expand(index);
                    for (int i = 0; i < model().rowCount(index); i++) {
                        parents.push(model().index(i, 0, index));
                    }
                }
            }
        } finally {
            unlockInput();
        }
    }
    
    public void setTopLevelKeyValue(Object value) throws ServiceClientException, InterruptedException  {
        if (selector.leaveCurrentEntity(false)) {
            FilteredByPropertiesGroupModel root = (FilteredByPropertiesGroupModel) controller.model.getRootGroupModel();
            root.setPropertyFilter(refPropertyId, value);
            if (model() != null) {
                lockInput();
                try {
                    beforeReread();
                    ((SelectorModelImpl) controller.model).resetModel();
                    root.invalidate();
                    ((SelectorModelImpl) controller.model).clear(null);
                    controller.model.readMore(null);
                    selector.refresh();
                    if (model().rowCount(null) > 0) {
                        controller.enterEntity(model().index(0, 0));
                    }
                } finally {
                    unlockInput();
                }
            }
        }
    }

    public Object getTopLevelKeyValue() {
        FilteredByPropertiesGroupModel root = (FilteredByPropertiesGroupModel) controller.model.getRootGroupModel();
        return root.getPropertyFilters().get(refPropertyId);
    }
    
    final public void setStoreExpandedItems(boolean store){
        storeExpandedItems = store;
    }
    
    final public boolean isExpandedItemsStored(){
        return storeExpandedItems;
    }

    final public void setExpandedItemsSettingGroupName(final String settingName){
        expandedItemsSettingsPath = settingName;
    }
    
    final public String getExpandedItemsSettingGroupName(){
        if (expandedItemsSettingsPath==null || expandedItemsSettingsPath.isEmpty()){
            final StringBuilder settingPathBuilder = new StringBuilder(sourceGroupModel.getConfigStoreGroupName());
            settingPathBuilder.append("/");
            settingPathBuilder.append(SettingNames.SYSTEM);
            settingPathBuilder.append("/tree");
            return settingPathBuilder.toString();
        }
        return expandedItemsSettingsPath;
    }
    
    final public void storeExpandedItems(){
        storeExpandedItems(null);
    }

    final public void storeExpandedItems(final ClientSettings config){
        final Collection<String> expandedItems = getExpandedItems();
        final int itemsCount = expandedItems.size();
        if (itemsCount>0){
            final ClientSettings actualSettings;
            if (config==null){
                actualSettings = sourceGroupModel.getEnvironment().getConfigStore();
            }else{
                actualSettings = config;
            }
            actualSettings.beginGroup(getExpandedItemsSettingGroupName());
            try{                                
                actualSettings.beginWriteArray(EXPANDED_ITEMS_SETTING_NAME,itemsCount);
                try{
                    int i = 0;
                    for (String expandedItem: expandedItems){
                        actualSettings.setArrayIndex(i);
                        actualSettings.writeString(EXPANDED_ITEM_SETTING_NAME, expandedItem);
                        i++;
                    }
                }finally{
                    actualSettings.endArray();
                }                        
            }finally{
                actualSettings.endGroup();
            }
        }        
    }
    
    final public void restoreExpandedItems(){
        restoreExpandedItems(null);
    }
    
    final public void restoreExpandedItems(final ClientSettings config){
        final ClientSettings actualSettings;
        if (config==null){
            actualSettings = sourceGroupModel.getEnvironment().getConfigStore();
        }else{
            actualSettings = config;
        }
        final Collection<String> expandedItems = new LinkedList<>();
        actualSettings.beginGroup(getExpandedItemsSettingGroupName());
        try{                                
            final int itemsCount = actualSettings.beginReadArray(EXPANDED_ITEMS_SETTING_NAME);
            try{
                for (int i=0; i<itemsCount; i++){
                    actualSettings.setArrayIndex(i);
                    expandedItems.add(actualSettings.readString(EXPANDED_ITEM_SETTING_NAME));
                }
            }finally{
                actualSettings.endArray();
            }
        }finally{
            actualSettings.endGroup();
        }
        if (!expandedItems.isEmpty()){
            try{
                expandItems(expandedItems);
            }catch(ServiceClientException | InterruptedException exception){
                //All data loaded at this moment - never thrown
            }
        }
    }
}
