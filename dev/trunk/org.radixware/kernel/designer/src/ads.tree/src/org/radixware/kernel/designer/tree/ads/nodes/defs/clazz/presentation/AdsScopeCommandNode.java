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

package org.radixware.kernel.designer.tree.ads.nodes.defs.clazz.presentation;

import java.util.List;
import javax.swing.Action;
import org.openide.nodes.Children;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.tree.ads.nodes.actions.GoToClientCommandHandlerAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.GoToServerCommandHandlerAction;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsObjectNode;


public class AdsScopeCommandNode extends AdsObjectNode<AdsScopeCommandDef> {

    @NodeFactoryRegistration
    public static class Factory implements INodeFactory<AdsScopeCommandDef> {

        @Override // Registered in layer.xml
        public RadixObjectNode newInstance(AdsScopeCommandDef ddsPlSqlObject) {
            return new AdsScopeCommandNode(ddsPlSqlObject);
        }
    }

    public AdsScopeCommandNode(AdsScopeCommandDef definition) {
        super(definition, Children.LEAF);

        final GoToServerCommandHandlerAction.Cookie serverSideCookie = new GoToServerCommandHandlerAction.Cookie(definition);
        addCookie(serverSideCookie);
        final GoToClientCommandHandlerAction.Cookie clientSideCookie = new GoToClientCommandHandlerAction.Cookie(definition);
        addCookie(clientSideCookie);
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);

        actions.add(null);
        actions.add(SystemAction.get(GoToServerCommandHandlerAction.class));
        actions.add(SystemAction.get(GoToClientCommandHandlerAction.class));
    }
}
