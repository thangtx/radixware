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

package org.radixware.kernel.designer.tree.nodes.dds;

import java.util.List;
import javax.swing.Action;
import org.openide.nodes.Children;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.dds.DdsExtTableDef;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectEditCookie;
import org.radixware.kernel.designer.tree.actions.dds.DdsExtTableEditTableAction;
import org.radixware.kernel.designer.tree.actions.dds.DdsExtTableGoToTableAction;


public class DdsExtTableNode extends DdsDefinitionNode {

    public DdsExtTableNode(DdsExtTableDef extTable) {
        super(extTable, Children.LEAF);
        addCookie(new DdsExtTableEditTableAction.Cookie(extTable));
        addCookie(new DdsExtTableGoToTableAction.Cookie(extTable));
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);
        actions.add(SystemAction.get(DdsExtTableGoToTableAction.class));
        actions.add(SystemAction.get(DdsExtTableEditTableAction.class));
    }

    @Override
    protected RadixObjectEditCookie createEditCookie() {
        return null;
    }

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<DdsExtTableDef> {

        public Factory() {
        }

        @Override
        public DdsExtTableNode newInstance(DdsExtTableDef extTable) {
            return new DdsExtTableNode(extTable);
        }
    }
}
