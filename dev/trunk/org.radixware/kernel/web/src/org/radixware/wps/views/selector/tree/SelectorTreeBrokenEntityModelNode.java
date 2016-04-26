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

package org.radixware.wps.views.selector.tree;

import java.awt.Color;
import org.radixware.kernel.common.client.models.BrokenEntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.wps.rwt.tree.Node;


class SelectorTreeBrokenEntityModelNode extends Node{
    
    private final BrokenEntityModel entityModel;
        
    public SelectorTreeBrokenEntityModelNode(final GroupModel groupModel, final BrokenEntityModel entityModel){
        super(Children.LEAF);
        this.entityModel = entityModel;
        setUserData(entityModel);
        
        final String pidAsStr = entityModel.getPid() == null ? "" : entityModel.getPid().toString();
        if (entityModel.getExceptionMessage() == null) {
            setCellValue(0, pidAsStr + " " + entityModel.getExceptionClass());
        } else {
            setCellValue(0, pidAsStr + " " + entityModel.getExceptionClass() + ": " + entityModel.getExceptionMessage());
        }
        
        setSpanned(true);
        setForeground(Color.red);
    }
    
}
