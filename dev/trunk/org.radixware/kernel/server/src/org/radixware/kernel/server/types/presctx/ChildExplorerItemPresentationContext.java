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

package org.radixware.kernel.server.types.presctx;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.kernel.server.types.Pid;


public final class ChildExplorerItemPresentationContext extends TreePresentationContext{

    private final Pid parentEntityPid;
    private final RadEditorPresentationDef parentEntityPresentation;
    
    public ChildExplorerItemPresentationContext(final Pid parentEntityPid, 
                                                                        final RadEditorPresentationDef parentEntityPresentation,
                                                                        final Id explorerItemId, 
                                                                        final EntityGroup group,
                                                                        final Id selectorPresentationId){
        super(explorerItemId, group, selectorPresentationId);
        this.parentEntityPid = parentEntityPid;
        this.parentEntityPresentation = parentEntityPresentation;
    }
    
    public Pid getParentEntityPid(){
        return parentEntityPid;
    }    
    
    public RadEditorPresentationDef getParentEntityEditorPresentation(){
        return parentEntityPresentation;
    }
}
