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
package org.radixware.kernel.designer.tree.ads.nodes.defs.clazz;

import java.util.List;
import javax.swing.Action;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ads.clazz.AdsPropertyGroup;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.tree.ads.nodes.actions.ImplementAbstractMemberAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.OverrideClassMemberAction;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsMixedNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.clazz.ClassPropertiesProvider;
import org.radixware.kernel.designer.tree.ads.nodes.defs.clazz.ClassPropertyGroupsProvider;

class AdsPropertyGroupNode extends AdsMixedNode<AdsPropertyGroup> {

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<AdsPropertyGroup> {

        @Override
        public Node newInstance(AdsPropertyGroup object) {
            return new AdsPropertyGroupNode(object);
        }
    }

    private AdsPropertyGroupNode(AdsPropertyGroup definition) {
        super(definition);
        addCookie(OverrideClassMemberAction.CookieFactory.newInstance(definition));
        addCookie(ImplementAbstractMemberAction.CookieFactory.newInstance(definition));
        getChildren();
    }

    @Override
    protected Class<?>[] getChildrenProviders() {
        return new Class[]{ClassPropertyGroupsProvider.class, ClassPropertiesProvider.class};
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        actions.add(null);
        actions.add(SystemAction.get(OverrideClassMemberAction.class));
        actions.add(SystemAction.get(ImplementAbstractMemberAction.class));
        super.addCustomActions(actions);
    }
}
