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

package org.radixware.kernel.designer.common.dialogs.dependency;

import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.dialogs.actions.AbstractRadixAction;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


final class ModuleTreeNode extends AbstractNode {

    private static final class SelectAction extends AbstractRadixAction {

        private final RadixObject target;

        public SelectAction(RadixObject target) {
            super("go-to-object");
            this.target = target;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            DialogUtils.goToObject(target);
        }
    }

    private Action preferredAction;
    private final Module object;

    public ModuleTreeNode(Children children, Module object) {
        super(children);
        this.object = object;

        assert object != null : "Node source object is null";

        setDisplayName(object.getQualifiedName());
        setShortDescription(object.getToolTip());
    }

    @Override
    public Image getIcon(int type) {
        return getRadixObject().getIcon().getImage();
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    @Override
    public Action getPreferredAction() {
        if (preferredAction == null) {
            preferredAction = new SelectAction(getRadixObject());
        }
        return preferredAction;
    }

    @Override
    public Action[] getActions(boolean context) {
        final Action action = getPreferredAction();
        return new Action[]{action};
    }

    private RadixObject getRadixObject() {
        return object;
    }
}