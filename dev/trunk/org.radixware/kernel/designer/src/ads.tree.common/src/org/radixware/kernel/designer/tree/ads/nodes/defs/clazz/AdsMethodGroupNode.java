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
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsMethodGroup;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.tree.ads.nodes.actions.CheckLoadersAction.CheckLoadersCookie;
import org.radixware.kernel.designer.tree.ads.nodes.actions.*;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsMixedNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;


class AdsMethodGroupNode extends AdsMixedNode<AdsMethodGroup> {

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<AdsMethodGroup> {

        @Override
        public Node newInstance(AdsMethodGroup object) {
            return new AdsMethodGroupNode(object);
        }
    }

    private AdsMethodGroupNode(AdsMethodGroup definition) {
        super(definition);
        addCookie(ImplementAbstractMemberAction.CookieFactory.newInstance(definition));

        AdsClassDef clazz = definition.getOwnerClass();
        if (clazz instanceof AdsModelClassDef || clazz instanceof IAdsPresentableClass) {
            addCookie(new CreateCommandHandlerAction.Cookie(clazz.getMethodGroup()));
        }
        if (clazz instanceof AdsEntityObjectClassDef) {
            addCookie(new CheckLoadersCookie((AdsEntityObjectClassDef) clazz, definition));
        }
        addCookie(OverrideClassMemberAction.CookieFactory.newInstance(definition));

    }

    @Override
    @SuppressWarnings("unchecked")
    protected Class<? extends MixedNodeChildrenProvider<?>>[] getChildrenProviders() {
        return new Class[]{ClassMethodGroupsProvider.class, ClassMethodsProvider.class};
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        actions.add(null);
        actions.add(SystemAction.get(OverrideClassMemberAction.class));
        actions.add(SystemAction.get(ImplementAbstractMemberAction.class));
        actions.add(null);
        EClassType ct = getRadixObject().getOwnerClass().getClassDefType();
        switch (ct) {
            case ENTITY:
            case APPLICATION:
            case ENTITY_GROUP:
            case ENTITY_MODEL:
            case GROUP_MODEL:
            case FORM_MODEL:
            case PARAGRAPH_MODEL:
            case DIALOG_MODEL:
            case PROP_EDITOR_MODEL:
                actions.add(SystemAction.get(CreateCommandHandlerAction.class));
                actions.add(null);
        }
        switch (ct) {
            case FORM_MODEL:
            case ENTITY_MODEL:
            case ENTITY_GROUP:
                actions.add(SystemAction.get(SyncPresentationPropsAction.class));
                actions.add(null);
        }
        actions.add(SystemAction.get(CheckLoadersAction.class));
        super.addCustomActions(actions);
    }
}
