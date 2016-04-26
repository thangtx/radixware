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

package org.radixware.kernel.server.arte.services.eas;

import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.presentations.RadExplorerItemDef;
import org.radixware.kernel.server.meta.presentations.RadExplorerRootDef;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.presctx.RootExplorerItemPresentationContext;


class RootItemContext extends TreeContext{
    private final Id rootId;

    RootItemContext(final SessionRequest rq, 
                    final org.radixware.schemas.eas.Context.TreePath.RootItem xml,
                    final org.radixware.schemas.eas.PropertyList groupProps){
        this(rq, xml.getRootId(), xml.getExplorerItemId(), groupProps);
    }

    RootItemContext(final SessionRequest rq, 
                    final Id rootId, 
                    final Id explorerItemId,
                    final org.radixware.schemas.eas.PropertyList groupProps) {
      super(rq, explorerItemId, groupProps);
      this.rootId = rootId;
    };

    @Override
    protected RadExplorerItemDef getExplorerItem() {
        return getRoot().findChildExplorerItemById(explorerItemId);
    }

    RadExplorerRootDef getRoot() {
        try {
            return (RadExplorerRootDef)rq.getArte().getDefManager().getExplorerParagraphDef(rootId);
        } catch (ClassCastException | DefinitionNotFoundError e) {
            throw EasFaults.newDefWithIdNotFoundFault("Root", "Context", rootId);
        }
    }

    @Override
    EContextRefRole getContextReferenceRole() {
        return EContextRefRole.NONE;
    }

    @Override
    Pid getContextObjectPid() {
        return null;
    }

    @Override
    void checkAccessible() {
    	if (!getRoot().getCurUserCanAccessItemById(explorerItemId)) {
            throw EasFaults.newDefinitionAccessViolationFault(rq.getArte(), 
                                                              Messages.MLS_ID_INSUF_PRIV_TO_ACCESS_THIS_EI, 
                                                              "(#" + explorerItemId + ")",
                                                              EDefType.EXPLORER_ITEM,
                                                              new Id[]{getRoot().getId(),explorerItemId});
        }
    }

    @Override
    RootExplorerItemPresentationContext getPresentationContext(EntityGroup entityGroup) {
        return new RootExplorerItemPresentationContext(rootId,explorerItemId,entityGroup);
    }        

    @Override
    public String toString() {
        final RadExplorerRootDef root = getRoot();
        final RadExplorerItemDef ei = getExplorerItem();
        return String.valueOf(root.getTitle(rq.getArte())) + " (#" + root.getId().toString() + ") -> " +
               "Explorer Item #"+ ei.getId();
    }
}
