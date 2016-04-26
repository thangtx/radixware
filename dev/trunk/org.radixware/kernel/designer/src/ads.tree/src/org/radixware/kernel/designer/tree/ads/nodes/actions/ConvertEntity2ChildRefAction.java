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

package org.radixware.kernel.designer.tree.ads.nodes.actions;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsChildRefExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsEntityExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.designer.ads.editors.exploreritems.ChooseExplorerItemTargetPanel;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.radixware.kernel.designer.common.general.utils.RequestProcessor;
import org.radixware.kernel.designer.tree.ads.nodes.defs.exploreritems.AdsExplorerItemNode;


public class ConvertEntity2ChildRefAction extends AdsDefinitionAction {

    public static class Cookie implements Node.Cookie {

        private final AdsEntityExplorerItemDef explorerItem;
        private final AdsExplorerItemNode node;

        public Cookie(AdsExplorerItemNode node, AdsEntityExplorerItemDef explorerItem) {
            this.explorerItem = explorerItem;
            this.node = node;
        }

        public boolean isEnabled() {
            if (node.isNodeInherited()) {
                return false;
            }
            return explorerItem != null && !explorerItem.isReadOnly();
        }

        public void perform() {
            if (explorerItem != null) {
                JPanel component = new JPanel();
                GridBagLayout l = new GridBagLayout();

                component.setLayout(l);
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.insets = new Insets(5, 5, 5, 5);

                component.add(new JLabel("Referenced selector:"), c);

                final ChooseExplorerItemTargetPanel panel = new ChooseExplorerItemTargetPanel();
                c.gridx = 1;
                c.gridwidth = GridBagConstraints.REMAINDER;
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 1;
                component.add(panel, c);
                StateDisplayer sd = new StateDisplayer();
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 0;
                c.gridwidth = 2;
                c.gridy = 1;
                component.add(sd, c);
                

                final ModalDisplayer displayer = new ModalDisplayer(component,"Convert Entity Reference to Child Reference") {
                    @Override
                    protected boolean canClose() {
                        return panel.isComplete();
                    }
                };

                final AdsChildRefExplorerItemDef converted = explorerItem.convert2ChildReference();
                panel.open(converted);
                panel.addChangeListener(new ChangeListener() {

                    @Override
                    public void stateChanged(ChangeEvent e) {
                        panel.isComplete();
                    }
                });
                
                panel.isComplete();
                if (displayer.showModal() && converted.findReferencedSelectorPresentation().get() != null) {
                    RequestProcessor.submit(new Runnable() {
                        @Override
                        public void run() {
                            RadixMutex.writeAccess(new Runnable() {
                                @Override
                                public void run() {
                                    final ExplorerItems.Children container = explorerItem.getOwnerExplorerItems().getChildren();
                                    if (converted.delete()) {
                                        container.getLocal().remove(explorerItem);
                                        container.getLocal().add(converted);
                                    }
                                }
                            });
                        }
                    });
                }
            }
        }
    }

    @Override
    protected boolean calcEnabled(Node[] activatedNodes) {
        if (activatedNodes.length == 0) {
            return false;
        }
        final Cookie c = activatedNodes[0].getCookie(Cookie.class);
        return c != null && c.isEnabled();
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes.length == 0) {
            return;
        }
        final Cookie c = activatedNodes[0].getCookie(Cookie.class);
        if (c != null) {
            c.perform();
        }
    }

    @Override
    public String getName() {
        return "Convert to Child Reference";
    }
}
