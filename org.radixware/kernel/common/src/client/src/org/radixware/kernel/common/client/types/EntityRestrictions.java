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

import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.enums.ERestriction;

/**
 * Класс ограничений редактора.
 * При смене ограничения редактор обновляется.
 * Можно только усиливать ограничения модели сущности.
 * При попытке ослабить генерируется исключение InvalidUsageError.
 */
public final class EntityRestrictions extends ModelRestrictions {

    private final EntityModel entity;
    private EntityRestrictions synchronizedRestrictions;
    private boolean changing;

    public EntityRestrictions(EntityModel model) {
        super(model);
        entity = model;
    }

    public void setDeleteRestricted(boolean flag) {
        setMask(ERestriction.DELETE, flag);
    }

    public void setUpdateRestricted(boolean flag) {
        setMask(ERestriction.UPDATE, flag);
        refreshProperties();
    }
    
    public void setViewRestricted(boolean flag) {
        setMask(ERestriction.VIEW, flag);        
    }    

    public void setCopyRestricted(boolean flag) {
        setMask(ERestriction.COPY, flag);
    }

    @Override
    public boolean getIsDeleteRestricted() {
        if (super.getIsDeleteRestricted()) {
            return true;
        }
        //RADIX-1462, RADIX-2232
        final GroupRestrictions ownerGroupRestrictions = getOwnerGroupRestrictions();
        return ownerGroupRestrictions != null ? ownerGroupRestrictions.getIsDeleteRestricted() : false;
    }

    @Override
    public boolean getIsUpdateRestricted() {
        if (super.getIsUpdateRestricted() || !entity.canOpenEntityView()) {
            return true;
        }        
        //RADIX-1462, RADIX-2232
        final GroupRestrictions ownerGroupRestrictions = getOwnerGroupRestrictions();
        return ownerGroupRestrictions != null ? ownerGroupRestrictions.getIsUpdateRestricted() : false;
    }

    private GroupRestrictions getOwnerGroupRestrictions() {
        final GroupModel ownerGroup;
        if (entity.getContext() instanceof IContext.SelectorRow) {
            ownerGroup = ((IContext.SelectorRow) entity.getContext()).parentGroupModel;
        } else if (entity.getContext() instanceof IContext.InSelectorEditing) {
            ownerGroup = ((IContext.InSelectorEditing) entity.getContext()).getGroupModel();
        } else {
            ownerGroup = null;
        }
        return ownerGroup != null ? ownerGroup.getRestrictions() : null;
    }

    public void startSynchronization(EntityRestrictions restrictions) {
        if (synchronizedRestrictions == null && restrictions!=null && restrictions.synchronizedRestrictions==null) {
            synchronizedRestrictions = restrictions;
            restrictions.synchronizedRestrictions = this;
        }
    }

    public void stopSynchronization() {
        if (synchronizedRestrictions != null) {
            synchronizedRestrictions.synchronizedRestrictions = null;
            synchronizedRestrictions = null;
        }
    }

    @Override
    protected void setMask(ERestriction restriction, boolean flag) {
        if (changing){
            return;
        }        
        changing = true;
        try{
            super.setMask(restriction, flag);
            if (synchronizedRestrictions != null) {
                synchronizedRestrictions.setMask(restriction, flag);
            }
        }finally{
            changing = false;
        }
    }

    @Override
    protected IView getView() {
        if (entity.getContext() instanceof IContext.SelectorRow){
            return ((IContext.SelectorRow)entity.getContext()).parentGroupModel.getView();
        }
        return entity.getEntityView();
    }

    @Override
    void refreshView(final boolean refreshPropEditors) {
        entity.onChangeRestriction();
        if (entity.getEntityView()!=null) {            
            entity.getEntityView().refresh();
            if (refreshPropEditors){
                refreshProperties();
            }
        }
        if (synchronizedRestrictions==null && entity.getContext() instanceof IContext.SelectorRow){
            final GroupModel ownerGroup = ((IContext.SelectorRow) entity.getContext()).parentGroupModel;
            if (ownerGroup.getGroupView()!=null){
                ownerGroup.getGroupView().refresh();
            }
        }
    }

    private void refreshProperties() {
        for (Property property : entity.getActiveProperties()) {
            property.afterModify();
        }
    }
    
    @Override
    public void add(final Restrictions from) {
        if (changing){
            return;
        }
        changing = true;
        try{
            final boolean updateRestricted = getIsUpdateRestricted();
            super.add(from);
            refreshView(updateRestricted != getIsUpdateRestricted());
            if (synchronizedRestrictions != null) {
                synchronizedRestrictions.add(from);
            }
        }finally{
            changing = false;
        }
    }
}
