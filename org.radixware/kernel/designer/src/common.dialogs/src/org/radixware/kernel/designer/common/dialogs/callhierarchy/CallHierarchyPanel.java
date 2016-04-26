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

/*
 * AdsMethodCallHierarchyPanel.java
 *
 * Created on Jan 24, 2012, 2:46:29 PM
 */
package org.radixware.kernel.designer.common.dialogs.callhierarchy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.components.TreeViewPanel;
import org.radixware.kernel.designer.common.dialogs.components.UtilTabbedTopComponent;


final class CallHierarchyPanel extends UtilTabbedTopComponent.UtilTabPanel<AdsMethodDef> {

    public CallHierarchyPanel() {
        super(new TreeViewPanel());
    }

    @Override
    public void update() {
        if (getObject() != null) {
            final Node root = new CallHierarchyNode(Children.create(new CallHierarchyChildFactory(getObject()), true), getObject());
            ((TreeViewPanel)getContentPanel()).getExplorerManager().setRootContext(root);
        }
    }

    @Override
    public String getTitle() {
        return getObject().getProfile().getName();
    }

    @Override
    public Icon getIcon() {
        return getObject().getIcon().getIcon();
    }

    @Override
    public JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();

        JButton update = new JButton();
        update.setFocusable(false);
        update.setIcon(RadixWareIcons.ARROW.CIRCLE.getIcon(13, 13));
        update.setToolTipText(NbBundle.getMessage(CallHierarchyPanel.class, "refresh.tooltip.text"));
        update.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
        toolBar.add(update);

        return toolBar;
    }
}
