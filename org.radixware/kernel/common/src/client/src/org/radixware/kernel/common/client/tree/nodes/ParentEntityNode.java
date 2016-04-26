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
import org.radixware.kernel.common.client.meta.explorerItems.RadParentRefExplorerItemDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.IPresentationChangedHandler;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.RawEntityModelData;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.common.types.Id;


public final class ParentEntityNode extends ExplorerItemNode implements IPresentationChangedHandler {

    public ParentEntityNode(final IExplorerTree tree, final ExplorerTreeNode parent, final RadParentRefExplorerItemDef ei) {
        super(tree, parent, ei.getId());
    }

    @Override
    protected boolean definitionIsExplorerItemsHolder() {
        return true;
    }    

    @Override
    protected Model openModel() throws Exception {
        final EntityModel result = (EntityModel) super.openModel();
        ((IContext.Entity) result.getContext()).setPresentationChangedHandler(this);
        return result;
    }

    @Override
    public EntityModel onChangePresentation(final RawEntityModelData rawData,
            final Id newPresentationClassId,
            final Id newPresentationId) {
        final EntityModel entity = (EntityModel) getRadixModel();
        final RadEditorPresentationDef presentation = getEnvironment().getApplication().getDefManager().getEditorPresentationDef(newPresentationId);
        final EntityModel model = presentation.createModel(entity.getContext());
        model.activate(rawData);
        setModel(model);
        entity.clean();
        return model;
    }
}
