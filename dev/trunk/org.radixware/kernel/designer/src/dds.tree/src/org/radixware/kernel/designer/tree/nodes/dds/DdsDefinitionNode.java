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
import org.openide.nodes.Children;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.defs.dds.IDdsDbDefinition;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectEditCookie;
import org.radixware.kernel.designer.common.tree.actions.DefinitionRenameAction.RenameCookie;
import org.radixware.kernel.designer.dds.editors.diagram.widgets.DdsEditorsManager;
import org.radixware.kernel.designer.tree.actions.dds.DdsDefinitionViewRecreateSqlAction;
import org.radixware.kernel.designer.tree.actions.dds.DdsDefinitionViewSqlAction;


public class DdsDefinitionNode extends RadixObjectNode {

    protected DdsDefinitionNode(DdsDefinition definition, Children children) {
        super(definition, children);

        if (definition instanceof IDdsDbDefinition) {
            addCookie(new DdsDefinitionViewSqlAction.Cookie(definition));
            if (definition instanceof DdsTableDef && !(definition instanceof DdsViewDef)) {
                addCookie(new DdsDefinitionViewRecreateSqlAction.Cookie(definition));
            }
        }
    }

    private DdsDefinition getDefinition() {
        return (DdsDefinition) getRadixObject();
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        final DdsDefinition definition = getDefinition();

        if (definition instanceof IDdsDbDefinition) {
            actions.add(SystemAction.get(DdsDefinitionViewSqlAction.class));
            if (definition instanceof DdsTableDef) {
                actions.add(SystemAction.get(DdsDefinitionViewRecreateSqlAction.class));
            }
        }
    }

    private static class DdsDefinitionEditCookie extends RadixObjectEditCookie {

        public DdsDefinitionEditCookie(DdsDefinition definition) {
            super(definition);
        }

        @Override
        public void edit() {
            final DdsDefinition definition = (DdsDefinition) getRadixObject();
            DdsEditorsManager.open(definition);
        }
    }

    @Override
    protected RadixObjectEditCookie createEditCookie() {
        final DdsDefinition definition = getDefinition();
        final DdsDefinitionEditCookie editCookie = new DdsDefinitionEditCookie(definition);
        return editCookie;
    }

    @Override
    protected RenameCookie createRenameCookie() {
        return null; // only from editors
    }

    @NodeFactoryRegistration
    public static class Factory implements INodeFactory<DdsDefinition> {

        @Override
        public RadixObjectNode newInstance(DdsDefinition ddsDefinition) {
            return new DdsDefinitionNode(ddsDefinition, Children.LEAF);
        }
    }
}
