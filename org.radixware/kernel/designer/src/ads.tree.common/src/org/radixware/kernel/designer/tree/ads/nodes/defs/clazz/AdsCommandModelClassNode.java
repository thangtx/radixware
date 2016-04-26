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

import java.awt.datatransfer.Transferable;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;
import org.radixware.kernel.common.defs.ads.command.AdsCommandModelClassDef;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.tree.ads.nodes.actions.SyncCommandModelClassAction;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsAbstractClassNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;


public class AdsCommandModelClassNode extends AdsAbstractClassNode {

    private SyncCommandModelClassAction.SyncCommandModelClassCookie syncCookie;

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<AdsCommandModelClassDef> {

        @Override
        public Node newInstance(AdsCommandModelClassDef object) {
            return new AdsCommandModelClassNode(object);
        }
    }

    public AdsCommandModelClassNode(AdsCommandModelClassDef classDef) {
        super(classDef);
        addCookie(syncCookie = new SyncCommandModelClassAction.SyncCommandModelClassCookie(classDef));
    }

    @Override
    protected Class<? extends MixedNodeChildrenProvider<?>>[] getChildrenProviders() {
        return MixedNodeChildrenAdapter.simpleClassProviders();
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);
        actions.add(SystemAction.get(SyncCommandModelClassAction.class));
    }

    @Override
    protected void createPasteTypes(Transferable transferable, List<PasteType> pasteTypes) {
        super.createPasteTypes(transferable, pasteTypes);
    }
}
