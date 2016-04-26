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

package org.radixware.kernel.designer.tree.ads.nodes.defs.exploreritems;

import java.util.List;
import javax.swing.Action;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsChildRefExplorerItemDef;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.tree.ads.nodes.actions.ConvertChildRef2EntityAction;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;


public class AdsChildRefExplorerItemNode extends AdsExplorerItemNode<AdsChildRefExplorerItemDef> {

    @NodeFactoryRegistration
    public static class Factory implements INodeFactory<AdsChildRefExplorerItemDef> {

        @Override // Registered in layer.xml
        public RadixObjectNode newInstance(AdsChildRefExplorerItemDef ddsPlSqlObject) {
            return new AdsChildRefExplorerItemNode(null, ddsPlSqlObject);
        }
    }

    public AdsChildRefExplorerItemNode(MixedNodeChildrenAdapter adapter, AdsChildRefExplorerItemDef definition) {
        super(adapter, definition);
        addCookie(new ConvertChildRef2EntityAction.Cookie(this, definition));
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);
        actions.add(null);
        actions.add(SystemAction.get(ConvertChildRef2EntityAction.class));
    }
    
}
