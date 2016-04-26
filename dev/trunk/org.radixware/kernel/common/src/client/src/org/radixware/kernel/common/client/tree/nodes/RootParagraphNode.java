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

import org.radixware.kernel.common.client.meta.explorerItems.RadParagraphDef;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.common.types.Id;


public class RootParagraphNode extends ExplorerItemNode {

    private final Id paragraphId;

    public RootParagraphNode(final IExplorerTree tree, final RadParagraphDef paragraph) {
        super(tree, null, paragraph.getId());
        paragraphId = paragraph.getId();
    }

    @Override
    public Id getOwnerDefinitionId() {
        return paragraphId;
    }        

    @Override
    protected boolean definitionIsExplorerItemsHolder() {
        return true;
    }    

    @Override
    protected Model openModel() throws Exception {
        RadParagraphDef paragraph = getEnvironment().getDefManager().getParagraphDef(paragraphId);
        Model model = paragraph.createModel(new IContext.Paragraph(getEnvironment(), null, paragraph));
        return model;
    }

    @Override
    public ExplorerTreeNode clone(final IExplorerTree tree) {
        final RadParagraphDef paragraph = getEnvironment().getDefManager().getParagraphDef(paragraphId);
        final RootParagraphNode node = new RootParagraphNode(tree, paragraph);
        return node;
    }
}
