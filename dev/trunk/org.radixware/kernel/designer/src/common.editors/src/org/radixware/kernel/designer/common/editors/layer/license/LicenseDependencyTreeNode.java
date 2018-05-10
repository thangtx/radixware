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
package org.radixware.kernel.designer.common.editors.layer.license;

import java.awt.Image;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.RadixWareIcons;

final class LicenseDependencyTreeNode extends AbstractNode {

    private final Object object;
    private final boolean isTarget;
    private boolean isTargetParent;

    public LicenseDependencyTreeNode(Children children, Object object, boolean isTarget) {
        super(children);
        this.object = object;
        this.isTarget = isTarget;

        assert object != null : "Node source object is null";

        String name = object instanceof Module ? ((Module) object).getQualifiedName() : ((Layer.License) object).getFullName();
        String toolTip = object instanceof Module ? ((Module) object).getToolTip() : ((Layer.License) object).getFullName();

        setDisplayName(name);
        setShortDescription(toolTip);
    }

    @Override
    public Image getIcon(int type) {
        if (object instanceof Module) {
            return ((Module) object).getIcon().getImage();
        } else {
            return RadixWareIcons.LICENSES.LICENSE.getImage();
        }
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    @Override
    public String getHtmlDisplayName() {
        if (isTarget) {
            return "<font color='0000FF'>" + getDisplayName() + "</font>";
        }
        if (isTargetParent) {
            return "<b>" + getDisplayName() + "</b>";
        }
        return null;
    }

    public void setIsTargetParent(boolean isTargetParent) {
        this.isTargetParent = isTargetParent;
    }
}
