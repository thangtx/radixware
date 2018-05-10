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

import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.IPresentationChangedHandler;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.RawEntityModelData;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.types.Id;


public class ChoosenEntityNode extends ExplorerTreeNode implements IPresentationChangedHandler {

    private final int hashCode;
    private final Pid pid;
    private final Id classId;
    private Id presentationClassId, presentationId;
    private final String title;
    private final String contextAsStr;

    public ChoosenEntityNode(final IExplorerTree tree, final EntityModel entity, final ExplorerTreeNode parent) {
        super(tree, parent);
        pid = entity.getPid();
        classId = entity.getClassId();
        presentationClassId = entity.getEditorPresentationDef().getOwnerClassId();
        presentationId = entity.getEditorPresentationDef().getId();
        title = entity.getTitle();
        IContext.Entity ctx = (IContext.Entity) entity.getContext();
        ctx.setPresentationChangedHandler(this);
        contextAsStr = ctx.saveToString();        
        hashCode = 7
                + classId.toString().hashCode() * 67
                + pid.hashCode() * 67
                + parent.getInternalId() * 67;
        setModel(entity);
    }

    private ChoosenEntityNode(final IExplorerTree tree, final ChoosenEntityNode source) {
        super(tree, null);
        pid = source.pid;
        classId = source.classId;
        presentationClassId = source.presentationClassId;
        presentationId = source.presentationId;
        title = source.title;
        contextAsStr = source.contextAsStr;
        hashCode = source.hashCode;
    }

    @Override
    public Id getOwnerDefinitionId() {
        return presentationId;
    }

    @Override
    protected boolean definitionIsExplorerItemsHolder() {
        return true;
    }        

    @Override
    protected int getInternalId() {
        return hashCode;
    }

    @Override
    public Id getExplorerItemId() {
        return null;
    }

    @Override
    protected Model openModel() throws Exception {
        final RadEditorPresentationDef presentation = getExplorerTree().getEnvironment().getApplication().getDefManager().getEditorPresentationDef(presentationId);
        final IContext.Entity context = 
            IContext.Entity.Factory.loadFromStr(getExplorerTree().getEnvironment(), contextAsStr);
        final EntityModel entity = presentation.createModel(context);
        context.setPresentationChangedHandler(this);
        entity.activate(pid.toString(), title, classId, null);
        return entity;
    }

    @Override
    public String getTitle() {
        if (isValid()) {
            return super.getTitle();
        } else {
            return "??? " + pid.toString() + " ???";
        }
    }

    @Override
    public EntityModel onChangePresentation(final RawEntityModelData rawData,
            final Id newPresentationClassId,
            final Id newPresentationId) {
        final EntityModel entity = (EntityModel) getRadixModel();
        final RadEditorPresentationDef presentation = getExplorerTree().getEnvironment().getApplication().getDefManager().getEditorPresentationDef(newPresentationId);
        final EntityModel model = presentation.createModel(entity.getContext());
        model.activate(rawData);
        presentationClassId = newPresentationClassId;
        presentationId = newPresentationId;
        setModel(model);
        getExplorerTree().update(this);
        entity.clean();        
        return model;
    }

    @Override()
    public String getCreationModelExceptionMessage() {
        final String msg = getEnvironment().getMessageProvider().translate("ExplorerTree", "Can't open object \'%s\' (class: #%s; PID: %s)\n in presentation #%s (class: #%s)");
        return String.format(msg, title, classId.toString(), pid, presentationId, presentationClassId);
    }

    @Override
    public ExplorerTreeNode clone(final IExplorerTree tree) {
        return new ChoosenEntityNode(tree, this);
    }
    
    @Override
    public String getName() {
        return "rx_explorer_tree_node_"+pid.getTableId().toString()+"_"+pid.toString();
    }    
    
    @Override
    public org.radixware.schemas.clientstate.ExplorerTreeNode writeToXml(org.radixware.schemas.clientstate.ExplorerTreeNode node) {
        final org.radixware.schemas.clientstate.ExplorerTreeNode xml;
        if (node==null){
            xml = org.radixware.schemas.clientstate.ExplorerTreeNode.Factory.newInstance();
        }else{
            xml = node;
        }
        final org.radixware.schemas.clientstate.ExplorerTreeNode.Object objectNode = xml.addNewObject();
        objectNode.setTableId(pid.getTableId());
        objectNode.setPID(pid.toString());
        objectNode.setEditorPresentationId(presentationId);        
        objectNode.setClassId(classId);
        objectNode.setContext(contextAsStr);
        return xml;
    }    
}
