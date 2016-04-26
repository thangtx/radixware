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

package org.radixware.kernel.designer.common.dialogs.hierarchy;

import java.awt.Image;
import java.util.Collections;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.designer.common.dialogs.tree.ICustomFilterActionsProvider;
import org.radixware.kernel.designer.common.general.displaying.IconSupport;
import org.radixware.kernel.designer.common.general.displaying.IconSupportsManager;


public class AbstractClassNode extends AbstractNode {

    private final IconSupport iconSupport;
    private final InstanceContent instanceContent;

    public AbstractClassNode(AdsClassDef classDef, Children children) {
        this(classDef, children, new InstanceContent());

    }

    protected AbstractClassNode(AdsClassDef classDef, Children children, InstanceContent instanceContent) {
        super(children, new AbstractLookup(instanceContent));
        this.instanceContent = instanceContent;
        this.instanceContent.add(new ICustomFilterActionsProvider() {

            @Override
            public List<Action> getActions() {
                return Collections.emptyList();
            }

            @Override
            public Action getPrefferedAction() {
                return null;
            }
        });
        instanceContent.add(classDef);
        setDisplayName(classDef.getName() + "    [" + classDef.getLayer().getName() + "::" + classDef.getModule().getName() + "]");
        iconSupport = IconSupportsManager.newInstance(classDef);
        fireIconChange();
    }

    @Override
    public Image getIcon(int type) {
        return iconSupport.getIcon().getImage();
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }
}
