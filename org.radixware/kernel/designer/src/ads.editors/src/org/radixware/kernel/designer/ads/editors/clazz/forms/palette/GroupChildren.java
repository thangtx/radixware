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

package org.radixware.kernel.designer.ads.editors.clazz.forms.palette;

import java.util.List;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


public class GroupChildren extends Children.Keys<Group> {

    private Group[] groups = new Group[]{
        Group.GROUP_CUSTOM_WIDGETS,
        Group.GROUP_RADIX_WIDGETS,
        Group.GROUP_LAYOUTS,
        Group.GROUP_SPACERS,
        Group.GROUP_BUTTONS,
        Group.GROUP_CONTAINERS,
        Group.GROUP_DISPLAY_WIDGETS,
        Group.GROUP_INPUT_WIDGETS,
        Group.GROUP_ITEM_WIDGETS
    };
    private Group[] webGroups = new Group[]{
        Group.GROUP_WEB_CUSTOM_WIDGETS,
        Group.GROUP_WEB_RADIX_WIDGETS,
        Group.GROUP_WEB_BUTTONS,
        Group.GROUP_WEB_INPUTS,
        Group.GROUP_WEB_DISPLAY,
        Group.GROUP_WEB_ITEM_WIDGETS,
        Group.GROUP_WEB_CONTAINERS,
        Group.GROUP_WEB_ACTIONS,};
    private ERuntimeEnvironmentType env;

    public GroupChildren(ERuntimeEnvironmentType env) {
        this.env = env;
    }

    public GroupChildren(ERuntimeEnvironmentType env, List<Item> customItems) {
        if (env == ERuntimeEnvironmentType.EXPLORER) {
            Item.registerCustom(customItems);
        } else {
            Item.registerCustomWeb(customItems);
        }

        this.env = env;
    }

    @Override
    protected Node[] createNodes(Group key) {
        return new Node[]{NodesManager.findOrCreateNode(key)};//new GroupNode(key)};
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        setKeys(env == ERuntimeEnvironmentType.EXPLORER ? groups : webGroups);
    }
}
