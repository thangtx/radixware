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

import java.util.List;
import org.radixware.kernel.common.client.meta.IExplorerItemsHolder;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.types.Id;


public interface IExplorerItemView {

    public static class EntityInfo {

        public final Pid pid;
        public final Id classId, presentationClassId, presentationId;
        public final String title;
        public final IContext.Entity context;

        public EntityInfo(EntityModel entity) {
            pid = new Pid(entity.getPid());
            classId = entity.getClassId();
            presentationClassId = entity.getDefinition().getOwnerClassId();
            presentationId = entity.getDefinition().getId();
            context = (IContext.Entity) entity.getContext();
            title = entity.getTitle();
        }
    }

    public Id getExplorerItemId();
    
    public IExplorerItemsHolder getTopLevelExplorerItemsHolder();

    public IExplorerTree getExplorerTree();

    public String getTitle();

    public void setTitle(String title);

    public void remove();

    public void refresh();

    public Model getModel();

    public Model getParentModel();

    public boolean isGroupView();

    public boolean isEntityView();
    
    public boolean isUserItemView();

    public IExplorerItemView insertEntity(EntityModel entity);

    public IExplorerItemView insertEntity(final int index, EntityModel entity, final boolean expandAfterInsert);

    public List<IExplorerItemView> getChoosenEntities();

    public List<IExplorerItemView> getChoosenEntities(final Id tableId);

    public EntityInfo getChoosenEntityInfo();

    public IExplorerItemView autoInsertEntity(EntityModel entity);
    
    public IExplorerItemView insertUserExplorerItem(GroupModel groupModel, String title);
    
    public String suggestUserExplorerItemTitle(GroupModel groupModel);

    public int getChildsCount();

    public IExplorerItemView getChild(int index);

    boolean isParagraphView();

    boolean setCurrent();

    boolean isChoosenObject();

    void removeChoosenEntities();

    boolean hasChoosenObjects();

    public void expand();

    public Id getDefinitionId();

    public Id getDefinitionOwnerClassId();        

    public Icon getIcon();

    public void setVisible(boolean visible);

    public boolean isVisible();

    public Id getTableId();

    public List<EntityModel> getParentEntityModels();

    public void removeChoosenEntity(final Pid pid);
}
