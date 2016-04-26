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
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.tree.ads.nodes.actions.ImplementAbstractMemberAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.OverrideClassMemberAction;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;

public class AdsPropertyPresentationPropertyNode extends AdsPropertyNode {

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<AdsPropertyPresentationPropertyDef> {

        @Override
        public Node newInstance(AdsPropertyPresentationPropertyDef property) {
            return new AdsPropertyPresentationPropertyNode(property);
        }
    }

    private AdsPropertyPresentationPropertyNode(AdsPropertyDef def) {
        super(def);

        addCookie(OverrideClassMemberAction.CookieFactory.newInstance((AdsPropertyPresentationPropertyDef) def));
        addCookie(ImplementAbstractMemberAction.CookieFactory.newInstance((AdsPropertyPresentationPropertyDef) def));
    }

    @Override
    protected Class[] getChildrenProviders() {

        final Class[] propertyProviders = super.getChildrenProviders();
        final Class[] enumClassFieldProviders = MixedNodeChildrenAdapter.enumClassFieldProviders();
        final Class[] providers = new Class[propertyProviders.length + enumClassFieldProviders.length];

        System.arraycopy(propertyProviders, 0, providers, 0, propertyProviders.length);
        System.arraycopy(enumClassFieldProviders, 0, providers, propertyProviders.length, enumClassFieldProviders.length);

        return providers;
    }

    @Override
    public boolean canCopy() {
        return super.canCopy();//(AdsPropertyPresentationPropertyDef) getRadixObject()).isLocal() ? super.canCopy() : false;
    }

    @Override
    public boolean canCut() {
        return super.canCut();// return ((AdsPropertyPresentationPropertyDef) getRadixObject()).isLocal() ? super.canCut() : false;
    }

    @Override
    public void addCustomActions(List<Action> actions) {

        actions.add(null);
        actions.add(SystemAction.get(OverrideClassMemberAction.class));
        actions.add(null);

        super.addCustomActions(actions);
    }

    @Override
    protected boolean isNewCreationSupport() {
        return true;
    }
}
