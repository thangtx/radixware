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

package org.radixware.kernel.designer.tree.nodes.dds;

import java.util.List;
import javax.swing.Action;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.repository.dds.DdsScriptsDir;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectEditCookie;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.common.tree.RadixObjectsNode;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeSortedChildren;
import org.radixware.kernel.designer.tree.actions.dds.CreateDdsReverseScriptAction;
import org.radixware.kernel.designer.tree.actions.dds.CreateDdsScriptAction;


public class DdsScriptsDirNode extends RadixObjectsNode {

    protected DdsScriptsDirNode(final DdsScriptsDir scriptsDir) {
        super(scriptsDir, new RadixObjectsNodeSortedChildren(scriptsDir));
        addCookie(new CreateDdsScriptAction.Cookie(scriptsDir));
        if (CreateDdsReverseScriptAction.isAllowed(scriptsDir)) {
            addCookie(new CreateDdsReverseScriptAction.Cookie(scriptsDir));
        }
    }

    @Override
    protected RadixObjectEditCookie createEditCookie() {
        return null;
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        actions.add(SystemAction.get(CreateDdsScriptAction.class));
        if (CreateDdsReverseScriptAction.isAllowed(getRadixObject())) {
            actions.add(SystemAction.get(CreateDdsReverseScriptAction.class));
        }
    }

    @Override
    public boolean canCheck() {
        return false;
    }

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<DdsScriptsDir> {

        @Override
        public RadixObjectNode newInstance(final DdsScriptsDir scriptsDir) {
            return new DdsScriptsDirNode(scriptsDir);
        }
    }
}
