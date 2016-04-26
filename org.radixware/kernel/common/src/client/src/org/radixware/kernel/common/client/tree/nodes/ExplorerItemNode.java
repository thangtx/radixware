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

import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.common.types.Id;



public class ExplorerItemNode extends ExplorerTreeNode{

    private final int hashCode;
    private final Id explorerItemId, ownerDefinitionId;
    
    public ExplorerItemNode(final IExplorerTree tree,  final ExplorerTreeNode parent, final Id explorerItemId){
        super(tree,parent);
        this.explorerItemId = explorerItemId;
        this.ownerDefinitionId = parent!=null ? parent.getOwnerDefinitionId() : explorerItemId;
        hashCode = 7+
                            explorerItemId.toString().hashCode()*67+
                            ownerDefinitionId.toString().hashCode()*67+
                            (parent!=null ? parent.getInternalId()*67 : 0);
    }

    private ExplorerItemNode(final IExplorerTree tree, final ExplorerItemNode source){
        super(tree,null);
        explorerItemId = source.explorerItemId;
        ownerDefinitionId = source.ownerDefinitionId;
        hashCode = source.hashCode;        
    }

    @Override
    public Id getOwnerDefinitionId() {
        return ownerDefinitionId;
    }

    @Override
    protected boolean definitionIsExplorerItemsHolder() {
        return false;
    }    

    @Override
    public Id getExplorerItemId(){
        return explorerItemId;
    }

    @Override
    protected int getInternalId() {
        return hashCode;
    }

    @Override
    protected Model openModel()  throws Exception{
        IExplorerTreeNode parent = getParentNode();
        if (!parent.isValid())
            throw parent.getCreationModelException();
        return parent.getView().getModel().getChildModel(explorerItemId);
    }

    @Override
    public String getTitle() {
        if (isValid())
            return super.getTitle();
        else
            return "??? #"+explorerItemId.toString()+" ???";
    }

    public ExplorerTreeNode clone(final IExplorerTree tree){
        return new ExplorerItemNode(tree, this);
    }
}