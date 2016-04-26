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

import java.io.File;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.Children;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.repository.dds.DdsScript;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectEditCookie;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.common.tree.RadixObjectNodeDeleteAction;


public class DdsScriptNode extends RadixObjectNode {

    protected DdsScriptNode(DdsScript script) {
        super(script, Children.LEAF);
    }

    private static class ScriptEditCookie extends RadixObjectEditCookie {

        public ScriptEditCookie(DdsScript script) {
            super(script);
        }

        public DdsScript getScript() {
            return (DdsScript) getRadixObject();
        }

        @Override
        public void edit() {
            final DdsScript script = getScript();
            final File file = script.getFile();
            DialogUtils.editFile(file);
        }
    }

    public DdsScript getScript() {
        return (DdsScript) getRadixObject();
    }

    @Override
    protected RadixObjectEditCookie createEditCookie() {
        final DdsScript script = getScript();
        return new ScriptEditCookie(script);
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);
        actions.add(SystemAction.get(RadixObjectNodeDeleteAction.class));
    }

    @Override
    public boolean canCheck() {
        return false;
    }

    @NodeFactoryRegistration
    public static class Factory implements INodeFactory<DdsScript> {

        @Override 
        public RadixObjectNode newInstance(final DdsScript script) {
            return new DdsScriptNode(script);
        }
    }
}
