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
import javax.swing.Action;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.designer.tree.ads.nodes.actions.SyncPresentationPropsAction.SyncPresentationPropsCookie;
import org.radixware.kernel.designer.tree.ads.nodes.actions.*;


public class AdsAbstractClassNode extends AdsMixedNode<AdsClassDef> {

    private final transient EClassType classType;

    public AdsAbstractClassNode(final AdsClassDef clazz) {
        super(clazz);
        classType = clazz.getClassDefType();
        addCookie(OverrideClassMemberAction.CookieFactory.newInstance(clazz));

        if (AdsTransparence.isTransparent(clazz)) {
            addCookie(new SynchronizePublishedClassAction.Cookie(clazz));
        }

        addCookie(ImplementAbstractMemberAction.CookieFactory.newInstance(clazz));

        if (clazz instanceof AdsModelClassDef || clazz instanceof IAdsPresentableClass) {
            addCookie(new CreateCommandHandlerAction.Cookie(clazz.getMethodGroup()));
        }

        switch (classType) {
            case FORM_MODEL:
            case REPORT_MODEL:
            case ENTITY_MODEL:
            case GROUP_MODEL:
            case FILTER_MODEL:
                addCookie(new SyncPresentationPropsCookie((AdsModelClassDef) clazz));
                break;
            default:
                break;
        }
    }

    @Override
    protected Class<? extends MixedNodeChildrenProvider<?>>[] getChildrenProviders() {
        return MixedNodeChildrenAdapter.simpleClassProviders();
    }

    protected AdsClassDef getClassDef() {
        return getRadixObject();
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        actions.add(null);
        actions.add(SystemAction.get(OverrideClassMemberAction.class));
        actions.add(SystemAction.get(ImplementAbstractMemberAction.class));
        actions.add(null);
        switch (classType) {
            case ENTITY:
            case APPLICATION:
            case ENTITY_GROUP:
            case ENTITY_MODEL:
            case GROUP_MODEL:
            case FORM_MODEL:
            case PARAGRAPH_MODEL:
            case DIALOG_MODEL:
            case FILTER_MODEL:
            case PROP_EDITOR_MODEL:
            case FORM_HANDLER:            
                actions.add(SystemAction.get(CreateCommandHandlerAction.class));
                actions.add(null);
        }
        switch (classType) {
            case FORM_MODEL:
            case ENTITY_MODEL:
            case ENTITY_GROUP:
            case REPORT_MODEL:
            case FILTER_MODEL:
                actions.add(SystemAction.get(SyncPresentationPropsAction.class));
                actions.add(null);
        }
        AdsTransparence t = getClassDef().getTransparence();
        if (t != null && t.isTransparent()) {
            actions.add(SystemAction.get(SynchronizePublishedClassAction.class));
        }

        actions.add(new InspectHierarchyAction(getRadixObject()));

        super.addCustomActions(actions);
    }
}
