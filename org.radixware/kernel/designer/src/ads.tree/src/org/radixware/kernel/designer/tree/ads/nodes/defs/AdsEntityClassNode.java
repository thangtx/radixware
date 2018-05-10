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

package org.radixware.kernel.designer.tree.ads.nodes.defs;

import java.util.List;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.rights.SystemPresentationBuilder;
import org.radixware.kernel.designer.tree.ads.nodes.actions.CheckLoadersAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.CheckLoadersAction.CheckLoadersCookie;
import org.radixware.kernel.designer.tree.ads.nodes.actions.CreateColumnBasedPropsAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.CreateEntityFeatureAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.GenerateDrcInfoAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.GenerateDrcInfoAction.GenerateDrcInfoCookie;


class AdsEntityClassNode extends AdsAbstractClassNode {

    public AdsEntityClassNode(AdsEntityObjectClassDef definition) {
        super(definition);
        addCookie(new CheckLoadersCookie(definition, null));
        if (definition.getId() == SystemPresentationBuilder.USER2ROLE_ID || definition.getId() == SystemPresentationBuilder.USERGROUP2ROLE_ID) {
            addCookie(new GenerateDrcInfoCookie((AdsEntityClassDef) definition));
        }

        addCookie(new CreateColumnBasedPropsAction.CreateColumnBasedPropsCookie(definition));
        addCookie(new CreateEntityFeatureAction.CreateEntityFeatureCookie(definition));
    }

    @Override
    protected Class[] getChildrenProviders() {
        return MixedNodeChildrenAdapter.presentableClassProviders();
    }

    @Override
    @SuppressWarnings({"unchecked", "fallthrough"})
    public void addCustomActions(List actions) {
        super.addCustomActions(actions);
//        actions.add(SystemAction.get(GenerateDrcInfoAction.class));//hide in RADIX-12593 - this action is depricated
        actions.add(SystemAction.get(CheckLoadersAction.class));
        actions.add(SystemAction.get(CreateColumnBasedPropsAction.class));
        switch (getRadixObject().getClassDefType()) {
            case ENTITY:
                actions.add(SystemAction.get(CreateEntityFeatureAction.CreateAGCAction.class));
            case APPLICATION:
                actions.add(SystemAction.get(CreateEntityFeatureAction.CreateAPAAction.class));
                break;

        }
    }
}
