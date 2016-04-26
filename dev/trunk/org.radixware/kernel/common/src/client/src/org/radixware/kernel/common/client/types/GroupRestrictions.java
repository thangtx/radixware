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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;

/**
 * Класс ограничений селектора.
 * При смене ограничения селектор обновляется.
 * Можно только усиливать ограничения модели группы.
 * При попытке ослабить генерируется исключение InvalidUsageError.
 */
public final class GroupRestrictions extends ModelRestrictions {
    
    private final GroupModel group;
    private final List<Pid> restrictedEntities = new ArrayList<>();

    public GroupRestrictions(GroupModel model) {
        super(model);
        group = model;
    }

    @Override
    public boolean getIsCreateRestricted() {
        final ERuntimeEnvironmentType curEnvType = 
            group.getEnvironment().getApplication().getRuntimeEnvironmentType();                
        ERuntimeEnvironmentType presEnvType;
        for (Id presentationId: group.getSelectorPresentationDef().getCreationPresentationIds()){
            try{
                presEnvType = 
                    group.getEnvironment().getDefManager().getEditorPresentationDef(presentationId).getRuntimeEnvironmentType();
                if (presEnvType==ERuntimeEnvironmentType.COMMON_CLIENT || presEnvType==curEnvType){
                    return super.getIsCreateRestricted();
                }
            }
            catch(DefinitionError error){
                continue;
            }
        }
        return true;//no suitable creation presentation.
    }

    public boolean getIsEntityRestricted(Pid pid) {
        return restrictedEntities.contains(pid);
    }

    public List<Pid> getRestrictedEntities() {
        return Collections.unmodifiableList(restrictedEntities);
    }

    public void setInsertIntoTreeRestricted(boolean flag) {
        setMask(ERestriction.INSERT_INTO_TREE, flag);
    }

    public void setInsertAllIntoTreeRestricted(boolean flag) {
        setMask(ERestriction.INSERT_ALL_INTO_TREE, flag);
    }

    public void setRunEditorRestricted(boolean flag) {
        setMask(ERestriction.RUN_EDITOR, flag);
    }

    public void setEditorRestricted(boolean flag) {
        setMask(ERestriction.EDITOR, flag);
        if (group.getGroupView() != null) {
            group.getGroupView().refresh();
        }
    }

    public void setUpdateRestricted(boolean flag) {
        setMask(ERestriction.UPDATE, flag);
    }

    public void setCreateRestricted(boolean flag) {
        setMask(ERestriction.CREATE, flag);
    }

    public void setDeleteRestricted(boolean flag) {
        setMask(ERestriction.DELETE, flag);
    }

    public void setDeleteAllRestricted(boolean flag) {
        setMask(ERestriction.DELETE_ALL, flag);
    }

    public void setTransferInRestricted(boolean flag) {
        setMask(ERestriction.TRANSFER_IN, flag);
    }

    public void setTransferOutRestricted(boolean flag) {
        setMask(ERestriction.TRANSFER_OUT, flag);
    }

    public void setTransferOutAllRestricted(boolean flag) {
        setMask(ERestriction.TRANSFER_OUT_ALL, flag);
    }

    public void setMultipleCopyRestricted(boolean flag) {
        setMask(ERestriction.MULTIPLE_COPY, flag);
    }

    public void setChangePositionRestricted(boolean flag) {
        if (flag) {
            restrictions.add(ERestriction.CHANGE_POSITION);
        } else {
            restrictions.remove(ERestriction.CHANGE_POSITION);
        }
    }
    
    public void setMultipleSelectionRestricted(boolean flag){
        setMask(ERestriction.MULTIPLE_SELECTION, flag);
    }
    
    public void setSelectAllRestricted(boolean flag){
        setMask(ERestriction.SELECT_ALL, flag);
    }    
    
    public void setMultipleDeleteRestricted(boolean flag){
        setMask(ERestriction.MULTIPLE_DELETE, flag);
    }    

    /**
     * Позволяет установить/снять ограничение на получение сущности из группы.
     * @param pid идентификатор сущности, на  которую накладывается/снимается ограничения
     * @param flag если true - указанная сущность не будет присутствовать в группе.
     */
    public void setEntityRestricted(final Pid pid, final boolean flag) {
        if (flag && !restrictedEntities.contains(pid)) {
            restrictedEntities.add(pid);
        } else if (!flag && restrictedEntities.contains(pid)) {
            restrictedEntities.remove(pid);
        }
    }

    /**
     * Позволяет установить/снять ограничение на получение сущностей из группы.
     * @param pids идентификаторы сущностей, на  которые накладывается/снимается ограничения
     * @param flag если true - указанные сущности не будет присутствовать в группе.
     */
    public void setEntityRestricted(final List<Pid> pids, final boolean flag) {
        for (Pid pid : pids) {
            if (flag && !restrictedEntities.contains(pid)) {
                restrictedEntities.add(pid);
            } else if (!flag && restrictedEntities.contains(pid)) {
                restrictedEntities.remove(pid);
            }
        }
    }

    @Override
    void refreshView(final boolean refreshPropEditors) {
        group.onChangeRestriction();
        if (getView() != null) {
            group.getGroupView().refresh();
        }
    }

    @Override
    protected IView getView() {
        return group.getGroupView();
    }        
}
