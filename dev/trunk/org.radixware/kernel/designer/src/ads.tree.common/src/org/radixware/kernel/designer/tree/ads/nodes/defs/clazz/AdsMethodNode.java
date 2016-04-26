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
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.enums.EMethodNature;
import org.radixware.kernel.designer.ads.method.refactoring.MethodRefactoringProvider;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.actions.DefinitionRenameAction.RenameCookie;
import org.radixware.kernel.designer.tree.ads.nodes.actions.CallHierarchyAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.ToggleMethodBreakpointAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.UpdateCommandHandlerProfileAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.UpdateCommandHandlerProfileAction.UpdateCommandHandlerProfileCookie;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsMixedNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;
import org.radixware.kernel.designer.tree.ads.nodes.defs.actions.AdsMethodProfileAction;


class AdsMethodNode extends AdsMixedNode<AdsMethodDef> {

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<AdsMethodDef> {

        @Override
        public Node newInstance(AdsMethodDef object) {
            return new AdsMethodNode(object);
        }
    }
    private UpdateCommandHandlerProfileCookie updateHandlerProfileCookie = null;
    private final ToggleMethodBreakpointAction.ToggleMethodBreakpointCookie toggleBreakpointCookie;

    private AdsMethodNode(AdsMethodDef method) {
        super(method);
        EMethodNature nature = method.getNature();
        if (nature == EMethodNature.ALGO_METHOD || nature == EMethodNature.USER_DEFINED) {
            addCookie(new AdsMethodProfileAction.Cookie(method));
        }
        if (nature == EMethodNature.COMMAND_HANDLER || nature == EMethodNature.RPC) {
            updateHandlerProfileCookie = new UpdateCommandHandlerProfileCookie(method);
            addCookie(updateHandlerProfileCookie);
        }
        toggleBreakpointCookie = new ToggleMethodBreakpointAction.ToggleMethodBreakpointCookie(method);
        addCookie(toggleBreakpointCookie);

        addCookie(new CallHierarchyAction.CallHierarchyCookie());

        refactoringInit(method);
    }

    private void refactoringInit(AdsMethodDef method) {
        // && !method.isReadOnly()
        if (method != null && !method.isConstructor() && !AdsTransparence.isTransparent(method)) {

            switch (method.getNature()) {
                case PRESENTATION_SLOT:
                case COMMAND_HANDLER:
                case SYSTEM:
                    break;
                default:
                    getLookupContent().add(new MethodRefactoringProvider(method));
            }
        }
    }

    private AdsMethodDef getMethod() {
        return getRadixObject();
    }

    @Override
    protected RenameCookie createRenameCookie() {
        final AdsMethodDef method = getMethod();
        final EMethodNature nature = method.getNature();
        switch (nature) {
            case ALGO_METHOD:
            case USER_DEFINED:
                if (AdsTransparence.isTransparent(method) || method.isConstructor()) {
                    return null;
                }
                break;
            case ALGO_STROB:
                return null;

        }
        return super.createRenameCookie();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addCustomActions(List actions) { // impossible to parametrize - strange bug in compiler
        super.addCustomActions(actions);

        actions.add(SystemAction.get(AdsMethodProfileAction.class));
        final AdsMethodDef method = getMethod();
        if (method.getNature() == EMethodNature.COMMAND_HANDLER || method.getNature() == EMethodNature.RPC) {
            actions.add(SystemAction.get(UpdateCommandHandlerProfileAction.class));
        }
        actions.add(SystemAction.get(ToggleMethodBreakpointAction.class));
        actions.add(SystemAction.get(CallHierarchyAction.class));
    }

    @Override
    protected Class<? extends MixedNodeChildrenProvider<?>>[] getChildrenProviders() {
        return null;
    }

    @Override
    public boolean canCut() {
        final AdsMethodDef method = getMethod();
        return method.getOwnerClass() != null && !method.getOwnerClass().isReadOnly();
    }

    @Override
    protected CreationSupport createNewCreationSupport() {
        return null;
    }
}
