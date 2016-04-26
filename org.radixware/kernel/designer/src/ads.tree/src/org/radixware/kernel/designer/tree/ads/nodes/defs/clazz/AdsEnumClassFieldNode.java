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
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEnumClassFieldDef;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.tree.ads.nodes.actions.ImplementAbstractMemberAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.OverrideClassMemberAction;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsMixedNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;


public class AdsEnumClassFieldNode extends AdsMixedNode<AdsEnumClassFieldDef> {

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<AdsEnumClassFieldDef> {

        @Override
        public Node newInstance(AdsEnumClassFieldDef field) {
            return new AdsEnumClassFieldNode(field);
        }
    }

    public AdsEnumClassFieldNode(AdsEnumClassFieldDef def) {
        super(def);

        addCookie(OverrideClassMemberAction.CookieFactory.newInstance(def));
        addCookie(ImplementAbstractMemberAction.CookieFactory.newInstance(def));
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    protected Class<? extends MixedNodeChildrenProvider>[] getChildrenProviders() {
        return MixedNodeChildrenAdapter.enumClassFieldProviders();
    }

    @Override
    public boolean canCopy() {
        return false;
    }

    @Override
    public boolean canCut() {
        return false;
    }

    @Override
    public void addCustomActions(List<Action> actions) {

        actions.add(null);
        actions.add(SystemAction.get(OverrideClassMemberAction.class));
        actions.add(SystemAction.get(ImplementAbstractMemberAction.class));
        actions.add(null);

        super.addCustomActions(actions);
    }
}
